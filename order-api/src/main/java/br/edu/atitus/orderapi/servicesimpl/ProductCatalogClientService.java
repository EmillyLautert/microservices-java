package br.edu.atitus.orderapi.servicesimpl;

import br.edu.atitus.orderapi.clients.ProductClient;
import br.edu.atitus.orderapi.dtos.DrawnBookResponse;
import br.edu.atitus.orderapi.dtos.ProductResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductCatalogClientService {
    private final ProductClient productClient;
    public ProductCatalogClientService(ProductClient productClient) { this.productClient = productClient; }

    @Retry(name = "product-api")
    @CircuitBreaker(name = "product-api")
    public ProductResponse getProduct(Long id) { return productClient.getProductById(id); }

    @Retry(name = "product-api")
    @CircuitBreaker(name = "product-api")
    public List<DrawnBookResponse> drawKitBooks(Long id, int quantity) {
        return productClient.drawKitBooks(id, quantity);
    }
}
