package com.letsplay.product;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product createProduct(Product product) {
        System.out.println("RECEIVED: " + product);
        Product saved = productRepository.save(product);
        System.out.println("SAVED: " + saved);
        return saved;
    }
}