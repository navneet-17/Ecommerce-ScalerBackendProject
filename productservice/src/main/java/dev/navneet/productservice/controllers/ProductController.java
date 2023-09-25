package dev.navneet.productservice.controllers;

import dev.navneet.productservice.dtos.CategoryDto;
import dev.navneet.productservice.dtos.ExceptionDto;
import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.dtos.ProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;
import dev.navneet.productservice.models.Product;
import dev.navneet.productservice.services.ProductService;
import dev.navneet.productservice.thirdpartyclients.productservice.fakestore.FakeStoreProductDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping("")
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }
    @GetMapping("/")
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("{id}")
    public ProductDto getProductById(@PathVariable("id") String id) throws NotFoundException {
        UUID uuid = UUID.fromString(id);
        return productService.getProductById(uuid);
    }


    @DeleteMapping("{id}")
    public ProductDto deleteProductById(@PathVariable("id") String id) throws NotFoundException {
        UUID uuid = UUID.fromString(id);
        return productService.deleteProductById(uuid);
    }

    @PutMapping("{id}")
    public ProductDto updateProductById(@PathVariable("id") String id, @RequestBody ProductDto productDto) {
        UUID uuid = UUID.fromString(id);
        return productService.updateProductById(uuid,productDto);
    }

    @GetMapping("/categories")
    public List<String> getAllProductCategories() {
        return productService.getAllProductCategories();
    }

    @GetMapping("/categories/{categoryName}")
    public List<ProductDto> getAllProductsInCategory(@PathVariable("categoryName") String categoryName) {
        return productService.getAllProductsInCategory(categoryName);
    }


    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<ExceptionDto> handleNotFoundException(NotFoundException notFoundException) {
        ExceptionDto exceptionDto = new ExceptionDto(HttpStatus.NOT_FOUND,
                notFoundException.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }


}