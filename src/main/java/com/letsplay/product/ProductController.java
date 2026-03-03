package com.letsplay.product;

import com.letsplay.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public Product create(@RequestBody Product product, @AuthenticationPrincipal Object principal) {
        if (principal instanceof User) {
            product.setUserId(((User) principal).getId());
        }
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable String id, @RequestBody Product product, @AuthenticationPrincipal Object principal) {
        Product existingProduct = productService.getProductById(id);
        if (principal instanceof User currentUser) {
            if (!existingProduct.getUserId().equals(currentUser.getId()) && !"ROLE_ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).build();
            }
        } else {
             return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, @AuthenticationPrincipal Object principal) {
        Product existingProduct = productService.getProductById(id);
        if (principal instanceof User currentUser) {
            if (!existingProduct.getUserId().equals(currentUser.getId()) && !"ROLE_ADMIN".equals(currentUser.getRole())) {
                return ResponseEntity.status(403).build();
            }
        } else {
             return ResponseEntity.status(401).build();
        }
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}