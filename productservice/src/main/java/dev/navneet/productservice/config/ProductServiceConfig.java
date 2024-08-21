package dev.navneet.productservice.config;

import dev.navneet.productservice.services.FakeStoreProductServiceImpl;
import dev.navneet.productservice.services.ProductService;
import dev.navneet.productservice.services.SelfProductServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.UUID;

@Configuration
public class ProductServiceConfig {

    @Bean
    @ConditionalOnProperty(name = "service.type", havingValue = "self")
    public ProductService<UUID> selfProductService(
            SelfProductServiceImpl selfProductServiceImpl) {
        return selfProductServiceImpl;
    }

    @Bean
    @ConditionalOnProperty(name = "service.type", havingValue = "fakestore")
    public ProductService<Long> fakeStoreProductService(
            FakeStoreProductServiceImpl fakeStoreProductServiceImpl) {
        return fakeStoreProductServiceImpl;
    }
}


