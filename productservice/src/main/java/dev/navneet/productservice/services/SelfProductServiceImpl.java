package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.dtos.ProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;
import dev.navneet.productservice.models.Category;
import dev.navneet.productservice.models.Price;
import dev.navneet.productservice.models.Product;
import dev.navneet.productservice.repositories.ProductRepository;
import dev.navneet.productservice.repositories.PriceRepository;
import dev.navneet.productservice.repositories.CategoryRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Service("selfProductServiceImpl")
public class SelfProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private PriceRepository priceRepository;

    public SelfProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, PriceRepository priceRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.priceRepository = priceRepository;
    }
    @Override
    public ProductDto createProduct(ProductDto productDto) {
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
        return convertProductToProductDto(savedProduct);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
//        List<ProductDto> productDtoList = new ArrayList<>();
//        for(Product product: products){
//            productDtoList.add(convertProductToProductDto(product));
//        }
        // Replace using streams:
        List<ProductDto> productDtoList = products.stream().
                    map(this::convertProductToProductDto).
                    collect(Collectors.toList());

        return productDtoList;
    }
    @Override
    public ProductDto getProductById(UUID id) throws NotFoundException {
        Optional<Product> productObj = productRepository.findById(id);
        if(productObj.isEmpty()){
            throw new NotFoundException("Product with id " + id + "is not found");
        }
        Product product = productObj.get();
        return convertProductToProductDto(product);
    }

    public ProductDto deleteProductById(UUID id) throws NotFoundException{
        Optional<Product> productObj = productRepository.findById(id);
        if(productObj.isEmpty()){
            throw new NotFoundException("Product with id " + id + "is not found");
        }
        Product product = productObj.get();
        ProductDto deletedProductDto = convertProductToProductDto(product);
        productRepository.deleteById(id);
        return deletedProductDto;
    }

    public ProductDto updateProductById(UUID id, ProductDto productDto) throws NotFoundException {
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
        return convertProductToProductDto(updatedProduct);
    }

    @Override
    public List<String> getAllProductCategories() {
//        return productRepository.getAllProductCategory();
        return categoryRepository.findAll().stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getAllProductsInCategory(String categoryName) {
        List<Product> productList = productRepository.getAllProductByCategory(categoryName);
        return productList.stream()
                .map(this::convertProductToProductDto)
                .collect(Collectors.toList());
    }

    private ProductDto convertProductToProductDto(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setId(String.valueOf(product.getUuid()));
        productDto.setTitle(product.getTitle());
        productDto.setDescription(product.getDescription());
        productDto.setImage(product.getImage());
        productDto.setCategory(product.getCategory().getName());
        productDto.setPrice(product.getPrice().getPrice());
        productDto.setCurrency(product.getPrice().getCurrency());
        return productDto;
    }
}







