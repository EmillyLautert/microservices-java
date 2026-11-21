package br.edu.atitus.orderapi.dtos;

import java.math.BigDecimal;

public record ProductResponse(Long id, String title, String author, String description, String category,
                              String type, Integer bookCount, BigDecimal price, String currency, String image) { }
