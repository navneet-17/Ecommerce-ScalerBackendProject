package dev.navneet.productservice.thirdpartyclients.productservice;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;

import java.util.List;

public interface ThirdPartyProductServiceClient {
    // This service class will all have the methods to interact with the Fakestore API.
    GenericProductDto createProduct(GenericProductDto product);
    GenericProductDto getProductById(Long id) throws NotFoundException;
    List<GenericProductDto> getAllProducts();
    GenericProductDto deleteProductById(Long id) throws NotFoundException;
    GenericProductDto updateProductById(Long id,GenericProductDto product);
}
