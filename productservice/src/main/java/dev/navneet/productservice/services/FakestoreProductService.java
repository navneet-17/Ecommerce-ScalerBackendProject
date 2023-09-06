package dev.navneet.productservice.services;

import dev.navneet.productservice.models.Product;
import org.springframework.stereotype.Service;

@Service("fakestoreProductService")
public class FakestoreProductService implements ProductService{
// This service class will all have the method implementations to interact with the Fakestore API.
    public String getProductById(Long id) {
        return "Here's the product by id : " + id;
//        return null;
    }
}
