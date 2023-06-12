package com.antonino.book101server.controllers;

import com.antonino.book101server.repositories.ProductRepository;
import com.antonino.book101server.models.Review;
import com.antonino.book101server.payload.response.MessageResponse;
import com.antonino.book101server.services.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping(value = "/add/{id}", consumes = {"application/json"})
    public ResponseEntity<?> addReview(@RequestBody @Valid Review review) {
        Optional<Review> optionalReview = reviewService.addReview(review);
        if (optionalReview.isPresent()) {
            return ResponseEntity.ok(optionalReview.get());
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Recensione esistente"));
        }
    }

    @GetMapping(value = "/{product}/paged", produces = "application/json")
    public ResponseEntity<List<Review>> getReviewsByProduct(
            @PathVariable("product") Long productID,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "9") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<Review> result = reviewService.showAllReviewsByProduct(productRepository.findProductById(productID), pageNumber, pageSize, sortBy);
        return ResponseEntity.ok(result);
    }
}

