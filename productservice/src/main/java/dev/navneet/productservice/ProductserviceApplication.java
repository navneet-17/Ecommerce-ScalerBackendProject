package dev.navneet.productservice;


import dev.navneet.productservice.inheritanceDemo.tableperclass.MentorRepository;
import dev.navneet.productservice.inheritanceDemo.tableperclass.UserRepository;
import dev.navneet.productservice.models.Category;
import dev.navneet.productservice.models.Product;
import dev.navneet.productservice.models.Price;
import dev.navneet.productservice.repositories.CategoryRepository;
import dev.navneet.productservice.repositories.PriceRepository;
import dev.navneet.productservice.repositories.ProductRepository;
import dev.navneet.productservice.services.CategoryService;
import org.springframework.beans.factory.annotation.Qualifier;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "dev.navneet.productservice.repositories") // JPA repositories
@EnableElasticsearchRepositories(basePackages = "dev.navneet.productservice.repository_elasticsearch") // Elasticsearch repositories
public class ProductserviceApplication{
		public static void main(String[] args) {
			SpringApplication.run(ProductserviceApplication.class, args);

		}
}





