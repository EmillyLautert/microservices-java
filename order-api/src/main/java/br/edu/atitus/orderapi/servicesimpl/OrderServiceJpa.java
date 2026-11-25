package br.edu.atitus.orderapi.servicesimpl;

import br.edu.atitus.orderapi.dtos.*;
import br.edu.atitus.orderapi.entities.*;
import br.edu.atitus.orderapi.repositories.OrderRepository;
import br.edu.atitus.orderapi.services.OrderService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Service
public class OrderServiceJpa implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductCatalogClientService productClient;
    private final OrderPersistenceService orderPersistenceService;
    private final OrderDetailsMapper orderDetailsMapper;

    public OrderServiceJpa(OrderRepository orderRepository, ProductCatalogClientService productClient,
                           OrderPersistenceService orderPersistenceService, OrderDetailsMapper orderDetailsMapper) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.orderPersistenceService = orderPersistenceService;
        this.orderDetailsMapper = orderDetailsMapper;
    }

    @Override
    public OrderDetails createOrder(OrderDTO request, Long customerId) {
        OrderEntity order = new OrderEntity();
        order.setCustomerId(customerId);
        order.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        order.setTotal(BigDecimal.ZERO.setScale(2));
        List<OrderItemEntity> items = new ArrayList<>();
        Set<Long> selectedBookIds = new HashSet<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemDTO requested : request.items()) {
            ProductResponse product = getProduct(requested.productId());
            if (product.price() == null || product.price().signum() <= 0)
                throw new ResponseStatusException(HttpStatus.CONFLICT, "O produto não possui preço válido");
            OrderItemEntity item = new OrderItemEntity();
            item.setProductId(product.id());
            item.setTitle(product.title());
            item.setType(product.type());
            item.setUnitPrice(product.price());
            item.setQuantity(requested.quantity());
            item.setCurrency("BRL");
            item.setOrder(order);

            List<DrawnBookSnapshot> drawn = List.of();
            if ("KIT".equals(product.type())) {
                List<DrawnBookResponse> chosen = drawBooks(product.id(), requested.quantity(), selectedBookIds);
                chosen.stream().map(DrawnBookResponse::id).filter(java.util.Objects::nonNull).forEach(selectedBookIds::add);
                drawn = chosen.stream().map(book -> new DrawnBookSnapshot(book.title(), book.author(), book.image())).toList();
            } else if (!"BOOK".equals(product.type())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Tipo de produto inválido");
            } else {
                selectedBookIds.add(product.id());
            }
            item.setDrawnBooks(new ArrayList<>(drawn));
            items.add(item);
            total = total.add(orderDetailsMapper.subtotal(item));
        }
        order.setItems(items);
        order.setTotal(total.setScale(2, RoundingMode.HALF_UP));
        return orderPersistenceService.save(order);
    }

    @Override
    @Transactional
    public List<OrderSummary> findMyOrders(Long customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(order -> new OrderSummary(order.getId(), order.getCreatedAt(), "COMPLETED", order.getTotal(),
                        order.getItems().stream().mapToInt(OrderItemEntity::getQuantity).sum()))
                .toList();
    }

    @Override
    @Transactional
    public OrderDetails findMyOrder(Long id, Long customerId) {
        OrderEntity order = orderRepository.findByIdAndCustomerId(id, customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        return orderDetailsMapper.fromEntity(order);
    }

    @Override
    @Transactional
    public SalesResponse findSales(YearMonth period, SalesProductFilter type) {
        YearMonth selectedPeriod = period == null ? YearMonth.now(ZoneOffset.UTC) : period;
        SalesProductFilter selectedType = type == null ? SalesProductFilter.ALL : type;
        OffsetDateTime start = selectedPeriod.atDay(1).atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = selectedPeriod.plusMonths(1).atDay(1).atStartOfDay().atOffset(ZoneOffset.UTC);
        List<OrderEntity> orders = orderRepository
                .findByCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByCreatedAtDesc(start, end);

        List<SaleOrder> sales = new ArrayList<>();
        BigDecimal totalRevenue = BigDecimal.ZERO;
        int itemsSold = 0;
        for (OrderEntity order : orders) {
            List<OrderItemEntity> matchingItems = order.getItems().stream()
                    .filter(item -> selectedType == SalesProductFilter.ALL || item.getType().equals(selectedType.name()))
                    .toList();
            if (matchingItems.isEmpty()) continue;

            List<SaleLine> lines = matchingItems.stream()
                    .map(item -> new SaleLine(item.getTitle(), item.getType(), item.getQuantity(), orderDetailsMapper.subtotal(item)))
                    .toList();
            BigDecimal orderTotal = lines.stream().map(SaleLine::total).reduce(BigDecimal.ZERO, BigDecimal::add);
            itemsSold += matchingItems.stream().mapToInt(OrderItemEntity::getQuantity).sum();
            totalRevenue = totalRevenue.add(orderTotal);
            sales.add(new SaleOrder(order.getId(), order.getCreatedAt(), orderTotal, lines));
        }
        return new SalesResponse(selectedPeriod, selectedType.name(),
                totalRevenue.setScale(2, RoundingMode.HALF_UP), sales.size(), itemsSold, sales);
    }

    private ProductResponse getProduct(Long id) {
        try { return productClient.getProduct(id); }
        catch (HttpStatusCodeException exception) {
            if (exception.getStatusCode().value() == 404) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Não foi possível consultar o catálogo");
        } catch (CallNotPermittedException exception) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Catálogo temporariamente indisponível");
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Catálogo temporariamente indisponível");
        } catch (RestClientException exception) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Catálogo temporariamente indisponível");
        }
    }

    private List<DrawnBookResponse> drawBooks(Long productId, int quantity, Set<Long> excludedBookIds) {
        try { return productClient.drawKitBooks(productId, quantity, excludedBookIds); }
        catch (HttpStatusCodeException exception) {
            if (exception.getStatusCode().value() == 409) throw new ResponseStatusException(HttpStatus.CONFLICT, "Não há livros suficientes para montar o kit");
            if (exception.getStatusCode().value() == 404) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kit não encontrado");
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Não foi possível selecionar os livros do kit");
        } catch (CallNotPermittedException exception) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Catálogo temporariamente indisponível");
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Catálogo temporariamente indisponível");
        } catch (RestClientException exception) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Catálogo temporariamente indisponível");
        }
    }

}
