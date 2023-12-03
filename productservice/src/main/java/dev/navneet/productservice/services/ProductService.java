package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.dtos.ProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    GenericProductDto createProduct(ProductDto productDto);
    GenericProductDto getProductById(UUID id) throws NotFoundException;
    GenericProductDto updateProductById(UUID id, ProductDto productDto) throws NotFoundException;
    GenericProductDto deleteProductById(UUID id) throws NotFoundException;
    List<GenericProductDto> getAllProducts();
    List<String> getAllProductCategories();
    List<GenericProductDto> getAllProductsInCategory(String categoryName);
}












