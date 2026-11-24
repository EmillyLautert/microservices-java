package br.edu.atitus.orderapi.repositories;

import br.edu.atitus.orderapi.entities.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.OffsetDateTime;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<OrderEntity> findByCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByCreatedAtDesc(
            OffsetDateTime start, OffsetDateTime end);
    java.util.Optional<OrderEntity> findByIdAndCustomerId(Long id, Long customerId);
}
