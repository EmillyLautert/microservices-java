package br.edu.atitus.orderapi.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record OrderSummary(Long id, OffsetDateTime createdAt, BigDecimal total, int itemCount) { }
