package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.dtos.ProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;
import dev.navneet.productservice.models.Price;
import dev.navneet.productservice.models.Product;
import dev.navneet.productservice.repositories.ProductRepository;
import dev.navneet.productservice.thirdpartyclients.productservice.fakestore.FakeStoreProductDto;
import dev.navneet.productservice.thirdpartyclients.productservice.fakestore.FakeStoreProductServiceClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import dev.navneet.productservice.exceptions.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

//@Primary
@Service("selfProductServiceImpl")
public class SelfProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public SelfProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Override
    public GenericProductDto createProduct(ProductDto productDto) {
        // create a product object from the GenericProductDto
        Product product = new Product();
        product.setUuid(UUID.randomUUID());
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setImage(productDto.getImage()==null?productDto.getTitle()+".jpg":productDto.getImage());
        Product savedProduct = productRepository.save(product);

        productDto.setId(savedProduct.getUuid().toString());

        // convert the ProductDto object to GenericProductDto
        GenericProductDto genericProductDto = convertProductDtoToGenericProductDto(productDto);
        return genericProductDto;
    }

    @Override
    public GenericProductDto getProductById(UUID id) throws NotFoundException {
       Product product = productRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Product with id " + id + "is not found"));
        return convertProductDtoToGenericProductDto(convertProductToProductDto(product));
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

    public GenericProductDto deleteProductById(UUID id){
        Product product = productRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Product with id " + id + "is not found"));
        GenericProductDto deletedProduct = convertProductDtoToGenericProductDto(convertProductToProductDto(product));
        productRepository.deleteById(id);
        return deletedProduct;
    }


    public GenericProductDto updateProductById(UUID id, ProductDto productDto) {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Product with id " + id + "is not found"));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setImage(productDto.getImage()==null?productDto.getTitle()+".jpg":productDto.getImage());
        Product updatedProduct = productRepository.save(product);
        return convertProductDtoToGenericProductDto(convertProductToProductDto(updatedProduct));
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
