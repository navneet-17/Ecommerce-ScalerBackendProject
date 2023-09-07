package dev.navneet.productservice.controllers;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    ProductService productService;

    public ProductController(@Qualifier("fakestoreProductService")ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String getAllProducts() {
        return "Get all products";
    }

    @GetMapping("{id}")
    public GenericProductDto getProductById(@PathVariable("id") Long id) {
        return productService.getProductById(id);
    }

    @DeleteMapping("{id}")
    public void deleteProductById() {

    }

    @PostMapping
    public GenericProductDto createProduct(@RequestBody GenericProductDto product) {
//        System.out.println(product.name);
        return productService.createProduct(product);

//        public void updateProductById() {

    }
}