package dev.navneet.productservice.controllers;

import dev.navneet.productservice.dtos.ExceptionDto;
import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.dtos.ProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;
import dev.navneet.productservice.services.ProductServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductServiceAdapter productServiceAdapter;

    @Autowired
    public ProductController(ProductServiceAdapter productServiceAdapter) {
        this.productServiceAdapter = productServiceAdapter;
    }
    @PostMapping("")
    public GenericProductDto createProduct(@RequestBody ProductDto productDto) {
        return productServiceAdapter.createProduct(productDto);
    }
    @GetMapping("/")
    public List<GenericProductDto> getAllProducts() {
        return productServiceAdapter.getAllProducts();
    }
    @GetMapping("{id}")
    public GenericProductDto getProductById(@PathVariable("id") String productId) throws NotFoundException {
        return productServiceAdapter.getProductById(productId);
    }

    @PutMapping("{id}")
    public GenericProductDto updateProductById(@PathVariable("id") String productId, @RequestBody ProductDto productDto) {
        return productServiceAdapter.updateProductById(productId,productDto);
    }

    @DeleteMapping("{id}")
    public GenericProductDto deleteProductById(@PathVariable("id") String productId) throws NotFoundException {
        return productServiceAdapter.deleteProductById(productId);
    }


    @GetMapping("/categories")
    public List<String> getAllProductCategories() {
        return productServiceAdapter.getAllProductCategories();
    }

    @GetMapping("/categories/{categoryName}")
    public List<GenericProductDto> getAllProductsInCategory(@PathVariable("categoryName") String categoryName) {
        return productServiceAdapter.getAllProductsInCategory(categoryName);
    }


    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<ExceptionDto> handleNotFoundException(NotFoundException notFoundException) {
        ExceptionDto exceptionDto = new ExceptionDto(HttpStatus.NOT_FOUND,
                notFoundException.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }
}

