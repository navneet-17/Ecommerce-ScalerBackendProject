package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.dtos.ProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;

import java.util.List;

public interface FakeStoreProductService {

        GenericProductDto createProduct(ProductDto productDto);
        GenericProductDto getProductById(Long id) throws NotFoundException;
        GenericProductDto updateProductById(Long id, ProductDto productDto)throws NotFoundException;
        GenericProductDto deleteProductById(Long id) throws NotFoundException;
        List<GenericProductDto> getAllProducts();
        List<String> getAllProductCategories();
        List<GenericProductDto> getAllProductsInCategory(String categoryName);
}