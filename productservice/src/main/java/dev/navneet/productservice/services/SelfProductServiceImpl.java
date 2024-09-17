package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.dtos.ProductDto;
import dev.navneet.productservice.dtos.UserDto;
import dev.navneet.productservice.exceptions.NotFoundException;
import dev.navneet.productservice.models.Category;
import dev.navneet.productservice.models.Price;
import dev.navneet.productservice.models.Product;
import dev.navneet.productservice.repositories.ProductRepository;
import dev.navneet.productservice.repositories.PriceRepository;
import dev.navneet.productservice.repositories.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

//@Primary
@Service("selfProductServiceImpl")
public class SelfProductServiceImpl  implements ProductService<UUID> {
    private static final Logger log = LoggerFactory.getLogger(SelfProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PriceRepository priceRepository;
    private final RestTemplate restTemplate;

    public SelfProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
                                  PriceRepository priceRepository, RestTemplate restTemplate) {
        log.info("Creating bean SelfProductServiceImpl");

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.priceRepository = priceRepository;
        this.restTemplate = restTemplate;
    }
    @Override
    public GenericProductDto createProduct(ProductDto productDto) {
        // fetch the existing category or create a new category and save it.
        Category category = categoryRepository.findByName(productDto.getCategory());
        if(category == null){
            category = new Category();
            category.setName(productDto.getCategory());
            categoryRepository.save(category);
        }

        Product product = new Product();
        String currency = productDto.getCurrency()!= null ? productDto.getCurrency():"INR";
        Price price = new Price(currency, productDto.getPrice());

        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setImage(productDto.getImage()!= null ? productDto.getImage(): productDto.getTitle() + ".jpg");
        product.setCategory(category);
        product.setPrice(price);
        productRepository.save(product);

        // Convert the Product to ProductDto and return it
        Product savedProduct = productRepository.save(product);
        return convertProductToGenericProductDto(savedProduct);
    }

    @Override
    public List<GenericProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        List<GenericProductDto> productDtoList = products.stream().
                    map(this::convertProductToGenericProductDto).
                    collect(Collectors.toList());

        return productDtoList;
    }

    @Override
    public Page<GenericProductDto> getAllProductsPageByPage(Pageable pageable)  {
        Page<Product> productsPage = productRepository.findAll(pageable);
        // Create a list to hold the converted DTOs
        List<GenericProductDto> productDtos = new ArrayList<GenericProductDto>();
        // From the product page obtained using the Pageable object, get the list of products
        List<Product> productsList = productsPage.getContent();
        for (Product product : productsList) {
            // Iterate over the products and convert each one to a DTO
            GenericProductDto dto = convertProductToGenericProductDto(product);
            productDtos.add(dto);
        }
        // Create a new PageImpl object
        PageImpl<GenericProductDto> dtoPage = new PageImpl<GenericProductDto>(
                productDtos,
                pageable,
                productsPage.getTotalElements()
        );
        // Return the PageImpl object
        return dtoPage;
    }

    @Override
    public GenericProductDto getProductById(UUID id) throws NotFoundException {
        System.out.println("In product service, calling the user service for user with id-1");
        ResponseEntity<UserDto> userResponse =
                restTemplate.getForEntity("http://userservice/users/1", UserDto.class);
        UserDto userDto = userResponse.getBody();

        //Fetch the actual product details:
        Optional<Product> productObj = productRepository.findById(id);
        if(productObj.isEmpty()){
            throw new NotFoundException("Product with id " + id + "is not found");
        }
        Product product = productObj.get();
        GenericProductDto productDto = convertProductToGenericProductDto(product);

        // Set the UserDto in the response
        productDto.setUser(userDto);
//        System.out.println(productDto.toString());
        return productDto;
    }

    public GenericProductDto deleteProductById(UUID id) throws NotFoundException{
        Optional<Product> productObj = productRepository.findById(id);
        if(productObj.isEmpty()){
            throw new NotFoundException("Product with id " + id + "is not found");
        }
        Product product = productObj.get();
        GenericProductDto deletedProductDto = convertProductToGenericProductDto(product);
        productRepository.deleteById(id);
        return deletedProductDto;
    }

    public GenericProductDto updateProductById(UUID id, ProductDto productDto) throws NotFoundException {
        Optional<Product> productObj = productRepository.findById(id);
        if(productObj.isEmpty()){
            throw new NotFoundException("Product with id " + id + "is not found");
        }
        Product product = productObj.get();
        Price price = product.getPrice();

        if(productDto.getPrice() != null){
            price.setPrice(productDto.getPrice());
        }
        if(productDto.getCurrency() != null){
            price.setCurrency(productDto.getCurrency());
        }
        priceRepository.save(price);

        if(productDto.getCategory() != null){
            Category category = categoryRepository.findByName(productDto.getCategory());
            if(category == null){
                category = new Category();
                category.setName(productDto.getCategory());
                categoryRepository.save(category);
            }
            product.setCategory(category);
        }

        if (productDto.getTitle() != null) {
            product.setTitle(productDto.getTitle());
        }
        if (productDto.getDescription() != null) {
            product.setDescription(productDto.getDescription());
        }
        if (productDto.getImage() != null) {
            product.setImage(productDto.getImage());
        }

        Product updatedProduct = productRepository.save(product);
        return convertProductToGenericProductDto(updatedProduct);
    }

    @Override
    public List<String> getAllProductCategories() {
//        return productRepository.getAllProductCategory();
        return categoryRepository.findAll().stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<GenericProductDto> getAllProductsInCategory(String categoryName) {
        List<Product> productList = productRepository.getAllProductByCategory(categoryName);
        return productList.stream()
                .map(this::convertProductToGenericProductDto)
                .collect(Collectors.toList());
    }

    public GenericProductDto convertProductToGenericProductDto(Product product){
        GenericProductDto genericProductDto = new GenericProductDto();
        genericProductDto.setId(String.valueOf(product.getUuid()));
        genericProductDto.setTitle(product.getTitle());
        genericProductDto.setDescription(product.getDescription());
        genericProductDto.setImage(product.getImage());
        genericProductDto.setCategory(product.getCategory().getName());
        genericProductDto.setPrice(product.getPrice().getPrice());
//        productDto.setCurrency(product.getPrice().getCurrency());
        return genericProductDto;
    }

}







