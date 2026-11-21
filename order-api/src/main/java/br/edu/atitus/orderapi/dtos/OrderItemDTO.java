package br.edu.atitus.orderapi.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Max;

public record OrderItemDTO(@NotNull @Positive Long productId, @NotNull @Positive @Max(50) Integer quantity) { }
