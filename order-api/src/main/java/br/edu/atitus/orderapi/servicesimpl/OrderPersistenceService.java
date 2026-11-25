package br.edu.atitus.orderapi.servicesimpl;

import br.edu.atitus.orderapi.dtos.OrderDetails;
import br.edu.atitus.orderapi.entities.OrderEntity;
import br.edu.atitus.orderapi.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OrderPersistenceService {
    private final OrderRepository orderRepository;
    private final OrderDetailsMapper orderDetailsMapper;

    public OrderPersistenceService(OrderRepository orderRepository, OrderDetailsMapper orderDetailsMapper) {
        this.orderRepository = orderRepository;
        this.orderDetailsMapper = orderDetailsMapper;
    }

    @Transactional
    public OrderDetails save(OrderEntity order) {
        return orderDetailsMapper.fromEntity(orderRepository.save(order));
    }
}
