package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ProductService {
    // This service class will all have the methods to interact with the Fakestore API.
    GenericProductDto createProduct(GenericProductDto product);
    GenericProductDto getProductById(Long id) throws NotFoundException;
    List<GenericProductDto> getAllProducts();
    GenericProductDto deleteProductById(Long id);
//    GenericProductDto updateProductById(Long id,GenericProductDto product);

}
