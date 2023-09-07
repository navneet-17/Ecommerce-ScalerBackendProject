package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.GenericProductDto;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductService {
    // This service class will all have the methods to interact with the Fakestore API.
    GenericProductDto createProduct(GenericProductDto product);
    public GenericProductDto getProductById(@PathVariable("id") Long id) ;
}
