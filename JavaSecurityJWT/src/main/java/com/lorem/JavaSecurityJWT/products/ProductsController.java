package com.lorem.JavaSecurityJWT.products;


import com.lorem.JavaSecurityJWT.authentication.AuthenticationResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductsController {

    @Autowired
    private ProductsRepository productsRepository;


    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                productsRepository.findAll()
        );
    }

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody @Valid Product product){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                productsRepository.save(product)
        );
    }
}
