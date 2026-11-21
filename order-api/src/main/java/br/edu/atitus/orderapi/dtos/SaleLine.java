package br.edu.atitus.orderapi.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record SaleLine(Long orderId, String productTitle, OffsetDateTime date, Integer quantity, BigDecimal total) { }
