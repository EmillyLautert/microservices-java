package br.edu.atitus.productapi.servicesimpl;

import br.edu.atitus.productapi.dtos.ProductRequest;
import br.edu.atitus.productapi.entities.ProductEntity;
import br.edu.atitus.productapi.entities.ProductType;
import br.edu.atitus.productapi.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceJpaTest {
    private ProductRepository repository;
    private ProductServiceJpa service;

    @BeforeEach
    void setUp() {
        repository = mock(ProductRepository.class);
        service = new ProductServiceJpa(repository, new RandomKitSelectionStrategy<>());
        when(repository.save(any(ProductEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void createsKitWithRequestedMinimumAndMaximumBookCounts() {
        when(repository.count(any(Specification.class))).thenReturn(5L);

        Integer oneBookKitCount = service.create(kitRequest(1)).bookCount();
        Integer fiveBookKitCount = service.create(kitRequest(5)).bookCount();

        assertEquals(1, oneBookKitCount);
        assertEquals(5, fiveBookKitCount);
        verify(repository, times(2)).save(any(ProductEntity.class));
    }

    @Test
    void rejectsKitCountsOutsideOneThroughFive() {
        ResponseStatusException tooFew = assertThrows(ResponseStatusException.class,
                () -> service.create(kitRequest(0)));
        ResponseStatusException tooMany = assertThrows(ResponseStatusException.class,
                () -> service.create(kitRequest(6)));

        assertEquals(400, tooFew.getStatusCode().value());
        assertEquals(400, tooMany.getStatusCode().value());
        verify(repository, never()).save(any(ProductEntity.class));
    }

    @Test
    void rejectsKitWhenCategoryHasFewerBooksThanRequested() {
        when(repository.count(any(Specification.class))).thenReturn(4L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.create(kitRequest(5)));

        assertEquals(409, exception.getStatusCode().value());
        verify(repository, never()).save(any(ProductEntity.class));
    }

    @Test
    void drawsTheConfiguredNumberOfDistinctBooks() {
        ProductEntity kit = product(50L, ProductType.KIT, 5);
        List<ProductEntity> books = List.of(
                product(1L, ProductType.BOOK, null), product(2L, ProductType.BOOK, null),
                product(3L, ProductType.BOOK, null), product(4L, ProductType.BOOK, null),
                product(5L, ProductType.BOOK, null));
        when(repository.findById(50L)).thenReturn(Optional.of(kit));
        when(repository.findAll(any(Specification.class), any(Sort.class))).thenReturn(books);

        List<ProductEntity> selected = service.findKitBooks(50L, 1, java.util.Set.of());

        assertEquals(5, selected.size());
        assertEquals(5, selected.stream().map(ProductEntity::getId).distinct().count());
    }

    private static ProductRequest kitRequest(Integer bookCount) {
        return new ProductRequest(null, null, null, "Terror", ProductType.KIT, bookCount,
                new BigDecimal("69.90"), "BRL", null);
    }

    private static ProductEntity product(Long id, ProductType type, Integer bookCount) {
        ProductEntity product = new ProductEntity();
        product.setId(id);
        product.setTitle(type == ProductType.KIT ? "Kit Terror" : "Livro " + id);
        product.setAuthor(type == ProductType.BOOK ? "Autor" : null);
        product.setCategory("Terror");
        product.setType(type);
        product.setBookCount(bookCount);
        product.setPrice(new BigDecimal("20.00"));
        product.setCurrency("BRL");
        return product;
    }
}
