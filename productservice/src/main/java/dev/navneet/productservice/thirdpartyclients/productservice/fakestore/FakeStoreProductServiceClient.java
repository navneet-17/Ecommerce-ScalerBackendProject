package dev.navneet.productservice.thirdpartyclients.productservice.fakestore;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
@Service
public class FakeStoreProductServiceClient {
    private final RestTemplateBuilder restTemplateBuilder;


    @Value("${fakestore.api.url}")
    private String fakeStoreApiUrl;

    @Value("${fakestore.api.paths.product}")
    private String fakeStoreProductsApiPath;

    private final String specificProductRequestUrl ;
    private final String productRequestsBaseUrl ;

    public FakeStoreProductServiceClient(RestTemplateBuilder restTemplateBuilder,
                                         @Value("${fakestore.api.url}") String fakeStoreApiUrl,
                                         @Value("${fakestore.api.paths.product}") String fakeStoreProductsApiPath) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.productRequestsBaseUrl  = fakeStoreApiUrl + fakeStoreProductsApiPath;
        this.specificProductRequestUrl = fakeStoreApiUrl + fakeStoreProductsApiPath + "/{id}";
    }

    public FakeStoreProductDto createProduct(GenericProductDto product) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto> response = restTemplate.postForEntity(
                productRequestsBaseUrl, product, FakeStoreProductDto.class
        );
        return response.getBody();
    }

    public FakeStoreProductDto getProductById(Long id) throws NotFoundException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto> response =
                restTemplate.getForEntity(specificProductRequestUrl, FakeStoreProductDto.class, id);

        FakeStoreProductDto fakeStoreProductDto = response.getBody();
        if (fakeStoreProductDto == null) {
            throw new NotFoundException("Product with id " + id + " not found");
        }
        return fakeStoreProductDto;
    }

    public List<FakeStoreProductDto> getAllProducts() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto[]> response =restTemplate.getForEntity(productRequestsBaseUrl, FakeStoreProductDto[].class);

        // converting the returned array of FakeStoreProductDto to a list of FakeStoreProductDto
        List<FakeStoreProductDto> products = Arrays.asList(response.getBody());
        return products;
    }

    public FakeStoreProductDto deleteProductById(Long id) throws NotFoundException {
        RestTemplate restTemplate = restTemplateBuilder.build();

        RequestCallback requestCallback = restTemplate.acceptHeaderRequestCallback(FakeStoreProductDto.class);
        ResponseExtractor<ResponseEntity<FakeStoreProductDto>> responseExtractor =
                restTemplate.responseEntityExtractor(FakeStoreProductDto.class);
        ResponseEntity<FakeStoreProductDto> response = restTemplate.
                execute(specificProductRequestUrl, HttpMethod.DELETE, requestCallback, responseExtractor, id);

       return response.getBody();
    }

    // TODO: Implementing the updateProductById method [H/W]
    public FakeStoreProductDto updateProductById(Long id, GenericProductDto genericProductDto){

        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(genericProductDto, FakeStoreProductDto.class);
        ResponseExtractor<ResponseEntity<FakeStoreProductDto>> responseExtractor =
                restTemplate.responseEntityExtractor(FakeStoreProductDto.class);
        ResponseEntity<FakeStoreProductDto> responseEntity = restTemplate.execute(
                specificProductRequestUrl, HttpMethod.PUT, requestCallback, responseExtractor, id);

        FakeStoreProductDto fakeStoreProductDto = responseEntity.getBody();
        return fakeStoreProductDto;
    }
}
