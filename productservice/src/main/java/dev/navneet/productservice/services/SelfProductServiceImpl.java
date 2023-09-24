package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.dtos.ProductDto;
import dev.navneet.productservice.models.Product;
import dev.navneet.productservice.repositories.ProductRepository;
import dev.navneet.productservice.thirdpartyclients.productservice.fakestore.FakeStoreProductDto;
import dev.navneet.productservice.thirdpartyclients.productservice.fakestore.FakeStoreProductServiceClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//@Primary
@Service("selfProductServiceImpl")
public class SelfProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public SelfProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Override
    public GenericProductDto createProduct(GenericProductDto product) {
        return null;
    }

    @Override
    public GenericProductDto getProductById(Long id) {
        return null;
    }

    @Override
    public List<GenericProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product: products) {
            productDtos.add(convertProductToProductDto(product));
        }
        // Convert the list of ProductDto to list of GenericProductDto using Streams and Lambda
         List<GenericProductDto> genericProductDtos = productDtos.stream()
                .map(this::convertProductDtoToGenericProductDto)
                .collect(Collectors.toList());

        return genericProductDtos;
    }

    public GenericProductDto deleteProductById(Long id){
        return null;

    }


    public GenericProductDto updateProductById(Long id, GenericProductDto product) {
        return null;
    }


    private ProductDto convertProductToProductDto(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setId(String.valueOf(product.getUuid()));
        productDto.setDescription(product.getDescription());
        productDto.setTitle(product.getTitle());
        productDto.setImage(product.getTitle() + ".jpg");
        productDto.setPrice(product.getPrice());
        productDto.setCategory(product.getCategory());
        return productDto;
    }

    private GenericProductDto convertProductDtoToGenericProductDto(ProductDto productDto){
        GenericProductDto genericProductDto = new GenericProductDto();
        genericProductDto.setId(productDto.getId());
        genericProductDto.setImage(productDto.getImage());
        genericProductDto.setDescription(productDto.getDescription());
        genericProductDto.setTitle(productDto.getTitle());
        genericProductDto.setPrice(productDto.getPrice().getPrice());
        genericProductDto.setCategory(productDto.getCategory().getName());
        return genericProductDto;
    }
}
