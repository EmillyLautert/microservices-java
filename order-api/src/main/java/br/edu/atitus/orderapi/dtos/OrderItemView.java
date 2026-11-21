package br.edu.atitus.orderapi.dtos;

import java.math.BigDecimal;
import java.util.List;

public record OrderItemView(Long productId, String title, String type, BigDecimal unitPrice,
                            Integer quantity, BigDecimal subtotal, List<DrawnBookView> drawnBooks) { }
