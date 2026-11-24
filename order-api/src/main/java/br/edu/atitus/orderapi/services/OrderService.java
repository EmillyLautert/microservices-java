package br.edu.atitus.orderapi.services;

import br.edu.atitus.orderapi.dtos.OrderDTO;
import br.edu.atitus.orderapi.dtos.OrderDetails;
import br.edu.atitus.orderapi.dtos.OrderSummary;
import br.edu.atitus.orderapi.dtos.SalesResponse;
import br.edu.atitus.orderapi.dtos.SalesProductFilter;
import java.time.YearMonth;
import java.util.List;

public interface OrderService {
    OrderDetails createOrder(OrderDTO request, Long customerId);
    List<OrderSummary> findMyOrders(Long customerId);
    OrderDetails findMyOrder(Long id, Long customerId);
    SalesResponse findSales(YearMonth period, SalesProductFilter type);
}
