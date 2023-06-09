package com.antonino.book101server.services;

import com.antonino.book101server.models.Product;
import com.antonino.book101server.models.ProductCategory;
import com.antonino.book101server.repositories.ProductRepository;
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
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;

    @Transactional(readOnly = true)
    public Optional<Product> getProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            product.get().setPicture(googleCloudStorageService.downloadAnteprima(product.get().getId() + "_jpg.jpg"));
            product.get().setPdf(googleCloudStorageService.downloadPDF(product.get().getId() + "_pdf.pdf"));
        }
        return product;
    }


    @Transactional(readOnly = false)
    public Optional<Product> addProduct(Product product) {
        product = productRepository.save(product);
        googleCloudStorageService.uploadFile(product.getPdf(), product.getId() + "_pdf.pdf");
        googleCloudStorageService.uploadFile(product.getPicture(), product.getId() + "_jpg.jpg");
        return Optional.of(product);
    }


    @Transactional(readOnly = false)
    public Optional<Product> updateProduct(Product product) {
        Optional<Product> productOptional = getProduct(product.getId());
        return productOptional.map(value -> productRepository.save(
                value
                        .setName(product.getName())
                        .setStock(product.getStock())
                        .setIsbn(product.getIsbn())
                        .setDescription(product.getDescription())
                        .setAuthor(product.getAuthor())
                        .setLanguage(product.getLanguage())
                        .setPublisher(product.getPublisher())
                        .setPicture(product.getPicture())
                        .setPdf(product.getPdf())
                        .setPrice(product.getPrice())));
    }

    @Transactional(readOnly = true)
    public List<Product> showAllProducts() {
        List<Product> products = productRepository.findAll();
        for (Product p : products) {
            p.setPicture(googleCloudStorageService.downloadAnteprima(p.getId() + "_jpg.jpg"));
            p.setPdf(googleCloudStorageService.downloadPDF(p.getId() + "_pdf.pdf"));
        }
        return products;
    }

    @Transactional(readOnly = true)
    public List<Product> showAllProducts(int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            pagedResult.getContent().stream().forEach(
                    product -> product.setPicture(googleCloudStorageService.downloadAnteprima(product.getId() + "_jpg.jpg"))
            );
            pagedResult.getContent().stream().forEach(
                    product -> product.setPdf(googleCloudStorageService.downloadPDF(product.getId() + "_pdf.pdf"))

            );
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }


    @Transactional(readOnly = true)
    public List<Product> showAllProductsByCategory(ProductCategory category, int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.findProductByCategory(category, paging);
        if (pagedResult.hasContent()) {
            pagedResult.getContent().stream().forEach(
                    product -> product.setPicture(googleCloudStorageService.downloadAnteprima(product.getId() + "_jpg.jpg"))
            );
            pagedResult.getContent().stream().forEach(
                    product -> product.setPdf(googleCloudStorageService.downloadPDF(product.getId() + "_pdf.pdf"))

            );
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<Product> showProductsByName(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        for (Product p : products) {
            p.setPicture(googleCloudStorageService.downloadAnteprima(p.getId() + "_jpg.jpg"));
            p.setPdf(googleCloudStorageService.downloadPDF(p.getId() + "_pdf.pdf"));
        }
        return products;
    }
}
