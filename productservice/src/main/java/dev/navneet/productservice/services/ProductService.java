package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.dtos.ProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService<T> {
    GenericProductDto createProduct(ProductDto productDto);
    GenericProductDto getProductById(T id) throws NotFoundException;
    GenericProductDto updateProductById(T id, ProductDto productDto) throws NotFoundException;
    GenericProductDto deleteProductById(T id) throws NotFoundException;
    List<GenericProductDto> getAllProducts();
    List<String> getAllProductCategories();
    List<GenericProductDto> getAllProductsInCategory(String categoryName);

    Page<GenericProductDto> getAllProductsPageByPage(Pageable pageable);
}













