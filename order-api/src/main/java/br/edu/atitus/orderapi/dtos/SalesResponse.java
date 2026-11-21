package br.edu.atitus.orderapi.dtos;

import java.math.BigDecimal;
import java.util.List;

public record SalesResponse(BigDecimal totalRevenue, List<SaleLine> sales) { }
