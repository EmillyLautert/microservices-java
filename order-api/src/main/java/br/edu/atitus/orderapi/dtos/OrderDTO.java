package br.edu.atitus.orderapi.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record OrderDTO(@NotEmpty List<@Valid OrderItemDTO> items) { }
