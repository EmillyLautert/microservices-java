package br.edu.atitus.orderapi.servicesimpl;

import br.edu.atitus.orderapi.dtos.DrawnBookView;
import br.edu.atitus.orderapi.dtos.OrderDetails;
import br.edu.atitus.orderapi.dtos.OrderItemView;
import br.edu.atitus.orderapi.entities.OrderEntity;
import br.edu.atitus.orderapi.entities.OrderItemEntity;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class OrderDetailsMapper {
    public OrderDetails fromEntity(OrderEntity order) {
        List<OrderItemView> items = order.getItems().stream().map(item -> new OrderItemView(
                item.getProductId(), item.getTitle(), item.getType(), item.getUnitPrice(), item.getQuantity(), subtotal(item),
                item.getDrawnBooks().stream().map(book -> new DrawnBookView(book.getTitle(), book.getAuthor(), book.getImage())).toList()
        )).toList();
        return new OrderDetails(order.getId(), order.getCreatedAt(), "COMPLETED", order.getTotal(), items);
    }

    public BigDecimal subtotal(OrderItemEntity item) {
        return item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())).setScale(2, RoundingMode.HALF_UP);
    }
}
