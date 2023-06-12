package com.antonino.book101server.services;

import com.antonino.book101server.models.Product;
import com.antonino.book101server.models.ProductCategory;
import com.antonino.book101server.models.Review;
import com.antonino.book101server.repositories.ProductRepository;
import com.antonino.book101server.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public Optional<Review> getReview(Long id) {return reviewRepository.findById(id);}

    @Transactional(readOnly = false)
    public Optional<Review> addReview(Review review) {
        Optional<Review> reviewOptional = getReview(review.getId());
        if (reviewOptional.isEmpty()) {
            return Optional.of(reviewRepository.save(review));
        }
        return Optional.empty();
    }
    @Transactional(readOnly = true)
    public List<Review> showAllReviewsByProduct(Product product, int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Review> pagedResult = reviewRepository.findReviewByProduct(product, paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }
}
