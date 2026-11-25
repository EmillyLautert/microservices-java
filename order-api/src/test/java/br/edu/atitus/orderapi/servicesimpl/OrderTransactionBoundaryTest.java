package br.edu.atitus.orderapi.servicesimpl;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderTransactionBoundaryTest {
    @Test
    void catalogCallsAreOutsideTheDatabaseTransactionAndPersistenceIsTransactional() throws Exception {
        assertNull(OrderServiceJpa.class.getMethod("createOrder",
                br.edu.atitus.orderapi.dtos.OrderDTO.class, Long.class).getAnnotation(Transactional.class));
        assertNotNull(OrderPersistenceService.class.getMethod("save",
                br.edu.atitus.orderapi.entities.OrderEntity.class).getAnnotation(Transactional.class));
    }
}
