package br.edu.atitus.orderapi.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record SaleLine(String productTitle, String type, Integer quantity, BigDecimal total) { }
