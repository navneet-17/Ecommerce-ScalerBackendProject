package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.models.Product;
import dev.navneet.productservice.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final ProductRepository productRepository;
    private final SelfProductServiceImpl selfProductServiceImpl;
    SearchService(ProductRepository productRepository,
                  SelfProductServiceImpl selfProductServiceImpl){
        this.productRepository = productRepository;
        this.selfProductServiceImpl = selfProductServiceImpl;
    }

    public Page<GenericProductDto> searchProducts (
           String query, Pageable pageable){
        Page<Product> productPage = productRepository
                                                .findAllByTitleContaining(query,pageable);
        List<Product> productList = productPage.get().toList();
        List<GenericProductDto> genericProductDtos = new ArrayList<>();
        // Convert each Product to a GenericProductDto and add it to the list
        for (Product product : productPage) {
            GenericProductDto dto = selfProductServiceImpl
                                                .convertProductToGenericProductDto(product);
            genericProductDtos.add(dto);
        }
        // Create a new PageImpl object using the list of DTOs, the Pageable,
        // and the total number of elements and return it
        return new PageImpl<>(
                genericProductDtos,
                productPage.getPageable(),
                productPage.getTotalElements()
        );
    }
}
