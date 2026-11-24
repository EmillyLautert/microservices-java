package br.edu.atitus.orderapi.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record SaleOrder(Long orderId, OffsetDateTime date, BigDecimal total, List<SaleLine> items) { }
