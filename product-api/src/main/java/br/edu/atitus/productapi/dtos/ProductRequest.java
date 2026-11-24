package br.edu.atitus.productapi.dtos;

import br.edu.atitus.productapi.entities.ProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductRequest(
        @Size(max = 255) String title,
        @Size(max = 255) String author,
        @Size(max = 5000) String description,
        @NotBlank @Size(max = 251) String category,
        @NotNull ProductType type,
        @Positive @Max(50) Integer bookCount,
        @NotNull @DecimalMin("0.01") BigDecimal price,
        String currency,
        @Size(max = 255) String image
) { }
