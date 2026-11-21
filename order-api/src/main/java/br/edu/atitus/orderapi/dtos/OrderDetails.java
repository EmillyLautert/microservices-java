package br.edu.atitus.orderapi.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record OrderDetails(Long id, OffsetDateTime createdAt, BigDecimal total, List<OrderItemView> items) { }
