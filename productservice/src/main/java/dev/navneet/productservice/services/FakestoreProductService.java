package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.FakeStoreProductDto;
import dev.navneet.productservice.dtos.GenericProductDto;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("fakestoreProductService")
public class FakestoreProductService implements ProductService{
// This service class will all have the method implementations to interact with the Fakestore API.
    private RestTemplateBuilder restTemplateBuilder;
    private String getProductRequestUrl = "https://fakestoreapi.com/products/{id}";
    private String productRequestsBaseUrl = "https://fakestoreapi.com/products";


    public FakestoreProductService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    public GenericProductDto createProduct(GenericProductDto product) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<GenericProductDto> response = restTemplate.postForEntity(
                productRequestsBaseUrl, product, GenericProductDto.class
        );
        return response.getBody();
    }

    public GenericProductDto getProductById(Long id) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto> response =
                restTemplate.getForEntity(getProductRequestUrl, FakeStoreProductDto.class, id);

        FakeStoreProductDto fakeStoreProductDto = response.getBody();

        GenericProductDto product = new GenericProductDto();
        product.setImage(fakeStoreProductDto.getImage());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setTitle(fakeStoreProductDto.getTitle());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setCategory(fakeStoreProductDto.getCategory());
        return product;
    }

    @Override
    public List<GenericProductDto> getAllProducts() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto[]> response =restTemplate.getForEntity(productRequestsBaseUrl, FakeStoreProductDto[].class);
        // converting the returned array of FakeStoreProductDto to a list of FakeStoreProductDto
        List<FakeStoreProductDto> returnedProducts = Arrays.asList(response.getBody());
        System.out.println(returnedProducts);

        // converting the list of FakeStoreProductDto to a list of GenericProductDto
        List<GenericProductDto> products = new ArrayList<>();
//        for (FakeStoreProductDto fakeStoreProductDto : response.getBody()) {
            for (FakeStoreProductDto fakeStoreProductDto : returnedProducts) {
            GenericProductDto product = new GenericProductDto();
            product.setImage(fakeStoreProductDto.getImage());
            product.setDescription(fakeStoreProductDto.getDescription());
            product.setTitle(fakeStoreProductDto.getTitle());
            product.setPrice(fakeStoreProductDto.getPrice());
            product.setCategory(fakeStoreProductDto.getCategory());
            products.add(product);
        }
        return products;
    }
}
