package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.dtos.ProductDto;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProductServiceAdapter {
    private final ProductService productService;
    private final FakeStoreProductService fakeStoreProductService;
    private final String serviceToBeUsed;

    @Autowired
    public ProductServiceAdapter(ProductService productService,
                                 FakeStoreProductService fakeStoreProductService,
                                 Environment environment) {
        this.productService = productService;
        this.fakeStoreProductService = fakeStoreProductService;
        this.serviceToBeUsed = environment.getProperty("service.type");
        System.out.println("Service to be used: " + serviceToBeUsed);
    }


    public GenericProductDto getProductById(String id) {
        if ("fakestore".equals(serviceToBeUsed)) {
            Long longId = Long.parseLong(id);
            return fakeStoreProductService.getProductById(longId);
        } else {
            UUID uuidId = UUID.fromString(id);
            return productService.getProductById(uuidId);
        }
    }

    public GenericProductDto updateProductById(String id, ProductDto productDto) {
        if ("fakestore".equals(serviceToBeUsed)) {
            Long longId = Long.parseLong(id);
            return fakeStoreProductService.updateProductById(longId, productDto);
        } else {
            UUID uuidId = UUID.fromString(id);
            return productService.updateProductById(uuidId, productDto);
        }
    }

    public GenericProductDto deleteProductById(String id) {
        if ("fakestore".equals(serviceToBeUsed)) {
            Long longId = Long.parseLong(id);
            return fakeStoreProductService.deleteProductById(longId);
        } else {
            UUID uuidId = UUID.fromString(id);
            return productService.deleteProductById(uuidId);
        }
    }

    public List<GenericProductDto> getAllProductsInCategory(String categoryName) {
        if ("fakestore".equals(serviceToBeUsed)) {
            return fakeStoreProductService.getAllProductsInCategory(categoryName);
        } else {
            return productService.getAllProductsInCategory(categoryName);
        }
    }

    public List<String> getAllProductCategories() {
        if ("fakestore".equals(serviceToBeUsed)) {
            return fakeStoreProductService.getAllProductCategories();
        }
        else {
            return productService.getAllProductCategories();
        }
    }

    public List<GenericProductDto> getAllProducts() {
        if ("fakestore".equals(serviceToBeUsed)) {
            return fakeStoreProductService.getAllProducts();
        } else {
            return productService.getAllProducts();
        }
    }

    public GenericProductDto createProduct(ProductDto productDto) {
        if ("fakestore".equals(serviceToBeUsed)) {
            return fakeStoreProductService.createProduct(productDto);
        } else {
            return productService.createProduct(productDto);
        }
    }
}

