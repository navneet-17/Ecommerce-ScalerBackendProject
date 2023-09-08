package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.GenericProductDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ProductService {
    // This service class will all have the methods to interact with the Fakestore API.
    GenericProductDto createProduct(GenericProductDto product);
    GenericProductDto getProductById(@PathVariable("id") Long id) ;
    List<GenericProductDto> getAllProducts();

}
