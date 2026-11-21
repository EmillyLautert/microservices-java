package br.edu.atitus.productapi.dtos;

import br.edu.atitus.productapi.entities.ProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank String title,
        String author,
        @NotBlank String description,
        @NotBlank String category,
        @NotNull ProductType type,
        @Positive @Max(50) Integer bookCount,
        @NotNull @DecimalMin("0.01") BigDecimal price,
        String currency,
        String image
) { }
