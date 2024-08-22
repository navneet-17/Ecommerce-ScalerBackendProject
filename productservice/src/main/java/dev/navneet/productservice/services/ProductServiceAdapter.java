package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.dtos.ProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;
import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Getter
@Component
public class ProductServiceAdapter {
    private final ProductService<?> productService;

    @Autowired
    public ProductServiceAdapter(
                                FakeStoreProductServiceImpl fakeStoreProductService,
                                 SelfProductServiceImpl selfProductService,
                                 Environment environment) {
        String serviceToBeUsed = environment.getProperty("service.type");
        System.out.println("Service to be used: " + serviceToBeUsed);

        if ("self".equals(serviceToBeUsed)) {
            // Use SelfService implementation
            this.productService = selfProductService;
        } else if ("fakestore".equals(serviceToBeUsed)) {
            // Use FakeStore implementation
            this.productService = fakeStoreProductService;
        } else {
            throw new IllegalArgumentException("Invalid service type: " + serviceToBeUsed);
        }
    }

    // We'll use Method Overloading to invoke the Correct Service based on the id parameter passed (Long/UUID)
    // Method to get Product by ID, determining the correct service to use
    public GenericProductDto getProductById(String id) throws NotFoundException {
        if (productService instanceof FakeStoreProductServiceImpl) {
            Long longId = Long.parseLong(id);
            return ((FakeStoreProductServiceImpl) productService).getProductById(longId);
        } else if (productService instanceof SelfProductServiceImpl) {
            UUID uuidId = UUID.fromString(id);
            return ((SelfProductServiceImpl) productService).getProductById(uuidId);
        } else {
            throw new IllegalStateException("Unknown service type");
        }
    }

    public GenericProductDto updateProductById(String id, ProductDto productDto) throws NotFoundException {
        if (productService instanceof FakeStoreProductServiceImpl) {
            Long longId = Long.parseLong(id);
            return ((FakeStoreProductServiceImpl) productService).updateProductById(longId, productDto);
        } else if (productService instanceof SelfProductServiceImpl) {
            UUID uuidId = UUID.fromString(id);
            return ((SelfProductServiceImpl) productService).updateProductById(uuidId, productDto);
        } else {
            throw new IllegalStateException("Unknown service type");
        }
    }

    public GenericProductDto deleteProductById(String id) throws NotFoundException {
        if (productService instanceof FakeStoreProductServiceImpl) {
            Long longId = Long.parseLong(id);
            return ((FakeStoreProductServiceImpl) productService).deleteProductById(longId);
        } else if (productService instanceof SelfProductServiceImpl) {
            UUID uuidId = UUID.fromString(id);
            return ((SelfProductServiceImpl) productService).deleteProductById(uuidId);
        } else {
            throw new IllegalStateException("Unknown service type");
        }
    }

    public List<GenericProductDto> getAllProductsInCategory(String categoryName) {
        return productService.getAllProductsInCategory(categoryName);
    }

    public List<String> getAllProductCategories() {
        return productService.getAllProductCategories();
    }

    public List<GenericProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

















    public GenericProductDto createProduct(ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    public Page<GenericProductDto> getAllProductsPageByPage(Pageable pageable) {
        return productService.getAllProductsPageByPage(pageable);
    }
}















