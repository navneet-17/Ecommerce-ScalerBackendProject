package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.dtos.ProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;
import dev.navneet.productservice.thirdpartyclients.productservice.fakestore.FakeStoreProductServiceClient;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);
    ProductDto getProductById(UUID id) throws NotFoundException;

    List<ProductDto> getAllProducts();

    ProductDto deleteProductById(UUID id) throws NotFoundException;
    ProductDto updateProductById(UUID id, ProductDto productDto)throws NotFoundException;

    List<String> getAllProductCategories();

    List<ProductDto> getAllProductsInCategory(String categoryName);


}












