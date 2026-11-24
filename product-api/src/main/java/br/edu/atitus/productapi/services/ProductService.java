package br.edu.atitus.productapi.services;

import br.edu.atitus.productapi.dtos.ProductRequest;
import br.edu.atitus.productapi.dtos.ProductResponse;
import br.edu.atitus.productapi.entities.ProductEntity;
import br.edu.atitus.productapi.entities.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProductService {
    Page<ProductResponse> findAll(String category, ProductType type, String search, Pageable pageable);
    List<String> findCategories();
    ProductResponse findById(Long id);
    ProductResponse create(ProductRequest request);
    ProductResponse update(Long id, ProductRequest request);
    ProductResponse updateImage(Long id, String imagePath);
    void delete(Long id);
    List<ProductEntity> findKitBooks(Long kitId, int quantity);
}
