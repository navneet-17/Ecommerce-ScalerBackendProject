package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.dtos.ProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;
import dev.navneet.productservice.thirdpartyclients.productservice.fakestore.FakeStoreProductDto;
import dev.navneet.productservice.thirdpartyclients.productservice.fakestore.FakeStoreProductServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//@Primary
@Service("fakestoreProductService")
public class FakeStoreProductServiceImpl implements FakeStoreProductService {
// This service class will all have the method implementations to interact with the Fakestore API.
    private static final Logger log = LoggerFactory.getLogger(SelfProductServiceImpl.class);
    private final FakeStoreProductServiceClient fakeStoreProductServiceClient;

    public FakeStoreProductServiceImpl(FakeStoreProductServiceClient fakeStoreProductServiceClient) {
        log.info("Creating bean FakeStoreProductServiceImpl");
        this.fakeStoreProductServiceClient = fakeStoreProductServiceClient;
    }

    @Override
    public GenericProductDto createProduct(ProductDto productDto) {
        GenericProductDto genericProductDto = convertProductDtoIntoGenericProduct(productDto);
        return convertFakeStoreProductIntoGenericProduct(fakeStoreProductServiceClient.createProduct(genericProductDto));
    }

    @Override
    public GenericProductDto getProductById(Long id) throws NotFoundException {
        return convertFakeStoreProductIntoGenericProduct(fakeStoreProductServiceClient.getProductById(id));
    }

    public List<GenericProductDto> getAllProducts() {
        List<GenericProductDto> genericProductDtos = new ArrayList<>();
        List<FakeStoreProductDto> fakeStoreProductDtos = fakeStoreProductServiceClient.getAllProducts();

        for (FakeStoreProductDto fakeStoreProductDto: fakeStoreProductDtos) {
            genericProductDtos.add(convertFakeStoreProductIntoGenericProduct(fakeStoreProductDto));
        }
        return genericProductDtos;
    }
    @Override
    public GenericProductDto updateProductById(Long id, ProductDto productDto) throws NotFoundException {
        GenericProductDto genericProductDto = convertProductDtoIntoGenericProduct(productDto);
        return convertFakeStoreProductIntoGenericProduct(fakeStoreProductServiceClient.updateProductById(id, genericProductDto));
    }

    @Override
    public GenericProductDto deleteProductById(Long id) throws NotFoundException {
        return convertFakeStoreProductIntoGenericProduct(fakeStoreProductServiceClient.deleteProductById(id));
    }

    @Override
    public List<String> getAllProductCategories() {
        return null;
    }

    @Override
    public List<GenericProductDto> getAllProductsInCategory(String categoryName) {
        return null;
    }

    private GenericProductDto convertFakeStoreProductIntoGenericProduct(FakeStoreProductDto fakeStoreProductDto) {

        GenericProductDto product = new GenericProductDto();
        product.setId(String.valueOf(fakeStoreProductDto.getId()));
        product.setImage(fakeStoreProductDto.getImage());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setTitle(fakeStoreProductDto.getTitle());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setCategory(fakeStoreProductDto.getCategory());

        return product;
    }

    private GenericProductDto convertProductDtoIntoGenericProduct(ProductDto productDto) {

        GenericProductDto product = new GenericProductDto();
        product.setId(String.valueOf(productDto.getId()));
        product.setImage(productDto.getImage());
        product.setDescription(productDto.getDescription());
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());

        return product;
    }

    /* ***************************************************************************************************  */
}
