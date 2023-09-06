package dev.navneet.productservice.services;

import org.springframework.web.bind.annotation.PathVariable;

public interface ProductService {
    // This service class will all have the methods to interact with the Fakestore API.
    public String getProductById(@PathVariable("id") Long id) ;
}
