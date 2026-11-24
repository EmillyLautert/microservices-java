package br.edu.atitus.orderapi.dtos;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public record SalesResponse(YearMonth period, String type, BigDecimal totalRevenue,
                            int orderCount, int itemsSold, List<SaleOrder> sales) { }
