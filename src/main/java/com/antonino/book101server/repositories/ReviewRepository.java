package com.antonino.book101server.repositories;

import com.antonino.book101server.models.Product;
import com.antonino.book101server.models.ProductCategory;
import com.antonino.book101server.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    Page<Review> findReviewByProduct(@NotNull(message = "Product is required") Product prodotto, Pageable pageable);

}
