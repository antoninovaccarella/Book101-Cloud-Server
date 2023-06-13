package com.antonino.book101server.repositories;

import com.antonino.book101server.models.Product;
import com.antonino.book101server.models.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    //Product findProductById(Long id);
    Page<Product> findProductByCategory(@NotNull(message = "Category is required") ProductCategory category, Pageable pageable);
    List<Product> findByNameContaining(String name);
    List<Product> findByNameContainingIgnoreCase(String name);

}
