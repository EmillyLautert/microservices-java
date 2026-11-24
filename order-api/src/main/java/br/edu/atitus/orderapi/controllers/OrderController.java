package br.edu.atitus.orderapi.controllers;

import br.edu.atitus.orderapi.dtos.OrderDTO;
import br.edu.atitus.orderapi.dtos.OrderDetails;
import br.edu.atitus.orderapi.dtos.OrderSummary;
import br.edu.atitus.orderapi.dtos.SalesResponse;
import br.edu.atitus.orderapi.dtos.SalesProductFilter;
import br.edu.atitus.orderapi.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;
import java.time.YearMonth;

@RestController
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDetails createOrder(@Valid @RequestBody OrderDTO request, @RequestHeader("X-User-Id") Long customerId) {
        return orderService.createOrder(request, customerId);
    }

    @GetMapping("/orders/me")
    public List<OrderSummary> myOrders(@RequestHeader("X-User-Id") Long customerId) {
        return orderService.findMyOrders(customerId);
    }

    @GetMapping("/orders/{id}")
    public OrderDetails orderDetails(@PathVariable Long id, @RequestHeader("X-User-Id") Long customerId) {
        return orderService.findMyOrder(id, customerId);
    }

    @GetMapping("/ws/orders/sales")
    public SalesResponse sales(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth period,
            @RequestParam(defaultValue = "ALL") SalesProductFilter type) {
        return orderService.findSales(period, type);
    }
}
