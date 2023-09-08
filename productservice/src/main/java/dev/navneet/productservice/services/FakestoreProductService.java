package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.FakeStoreProductDto;
import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("fakestoreProductService")
public class FakestoreProductService implements ProductService{
// This service class will all have the method implementations to interact with the Fakestore API.
    private RestTemplateBuilder restTemplateBuilder;
    private String  specificProductRequestUrl = "https://fakestoreapi.com/products/{id}";
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

    public GenericProductDto getProductById(Long id) throws NotFoundException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto> response =
                restTemplate.getForEntity(specificProductRequestUrl, FakeStoreProductDto.class, id);

        FakeStoreProductDto fakeStoreProductDto = response.getBody();
        if (fakeStoreProductDto == null) {
         throw new NotFoundException("Product with id " + id + " not found");
        }
        return convertFakeStoreProductIntoGenericProduct(fakeStoreProductDto);
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
            for (FakeStoreProductDto fakeStoreProductDto : returnedProducts) {
                products.add (convertFakeStoreProductIntoGenericProduct(fakeStoreProductDto));
        }
        return products;
    }

    public GenericProductDto deleteProductById(Long id){
        RestTemplate restTemplate = restTemplateBuilder.build();

        RequestCallback requestCallback = restTemplate.acceptHeaderRequestCallback(FakeStoreProductDto.class);
        ResponseExtractor<ResponseEntity<FakeStoreProductDto>> responseExtractor =
                restTemplate.responseEntityExtractor(FakeStoreProductDto.class);
        ResponseEntity<FakeStoreProductDto> response = restTemplate.
                execute(specificProductRequestUrl, HttpMethod.DELETE, requestCallback, responseExtractor, id);

        FakeStoreProductDto fakeStoreProductDto = response.getBody();
        return convertFakeStoreProductIntoGenericProduct(fakeStoreProductDto);
    }

    // TODO: Implementing the updateProductById method [H/W]
    public GenericProductDto updateProductById(Long id, GenericProductDto product){

        return null;
    }

    public GenericProductDto convertFakeStoreProductIntoGenericProduct(
                                FakeStoreProductDto fakeStoreProductDto){
        GenericProductDto product = new GenericProductDto();
        product.setId(fakeStoreProductDto.getId());
        product.setImage(fakeStoreProductDto.getImage());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setTitle(fakeStoreProductDto.getTitle());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setCategory(fakeStoreProductDto.getCategory());
        return product;
    }

}
