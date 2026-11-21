package br.edu.atitus.productapi.dtos;

import br.edu.atitus.productapi.entities.ProductEntity;
import br.edu.atitus.productapi.entities.ProductType;
import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String title,
        String author,
        String description,
        String category,
        ProductType type,
        Integer bookCount,
        BigDecimal price,
        String currency,
        String image
) {
    public static ProductResponse fromEntity(ProductEntity entity) {
        return new ProductResponse(entity.getId(), entity.getTitle(), entity.getAuthor(),
                entity.getDescription(), entity.getCategory(), entity.getType(),
                entity.getBookCount(), entity.getPrice(), entity.getCurrency(), entity.getImage());
    }
}
