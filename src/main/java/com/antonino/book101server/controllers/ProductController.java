package com.antonino.book101server.controllers;

import com.antonino.book101server.models.Product;
import com.antonino.book101server.models.ProductCategory;
import com.antonino.book101server.payload.response.MessageResponse;
import com.antonino.book101server.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping(value = "/add", consumes = {"application/json"})
    public ResponseEntity<?> addProduct(@RequestBody @Valid Product product) {
        if (product.getId() != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("L'ID del Prodotto è auto-generato."));
        }
        Optional<Product> optionalProduct = productService.addProduct(product);
        if (optionalProduct.isPresent()) {
            return ResponseEntity.ok(optionalProduct.get());
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Prodotto NON aggiunto."));
        }
    }


    @PostMapping(value = "/edit", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid Product product) {
        Optional<Product> optionalProduct = productService.updateProduct(product);
        if (optionalProduct.isPresent()) {
            return ResponseEntity.ok(optionalProduct.get());
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Prodotto non trovato"));
        }
    }

    @GetMapping(value = "/all/paged", produces = "application/json")
    public ResponseEntity<List<Product>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "9") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<Product> result = productService.showAllProducts(pageNumber, pageSize, sortBy);
        return ResponseEntity.ok(result);
    }



    @GetMapping(value = "/byname", produces = "application/json")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @RequestParam(value = "name", defaultValue = "") String name) {
        List<Product> result = productService.showProductsByName(name);
        return ResponseEntity.ok(result);
    }


    @GetMapping(value = "/{category}/paged", produces = "application/json")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @PathVariable("category") String category,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "9") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<Product> result = productService.showAllProductsByCategory(ProductCategory.valueOf(category.toUpperCase()), pageNumber, pageSize, sortBy);
        return ResponseEntity.ok(result);
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        Optional<Product> optionalProduct = productService.getProduct(id);
        if (optionalProduct.isPresent()) {
            return ResponseEntity.ok(optionalProduct.get());
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Prodotto non trovato"));
        }
    }
}
