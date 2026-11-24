package br.edu.atitus.productapi.repositories;

import br.edu.atitus.productapi.entities.ProductEntity;
import br.edu.atitus.productapi.entities.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {
    List<ProductEntity> findByCategoryIgnoreCaseAndType(String category, ProductType type);

    @Query("select distinct p.category from ProductEntity p where p.category is not null order by p.category")
    List<String> findDistinctCategories();
}
