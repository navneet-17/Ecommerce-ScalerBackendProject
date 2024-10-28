package dev.navneet.productservice.utilities;

import dev.navneet.productservice.models.Product;
import dev.navneet.productservice.repositories.ProductRepository;
import dev.navneet.productservice.services.ElasticSearchService;
//import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchIndexingService {

    public static final Logger log = LoggerFactory.getLogger(BatchIndexingService.class);

    private final ProductRepository productRepository;
    private final ElasticSearchService elasticSearchService;


    public BatchIndexingService(ProductRepository productRepository, ElasticSearchService elasticSearchService) {
        this.productRepository = productRepository;
        this.elasticSearchService = elasticSearchService;
    }

//    @PostConstruct --> Removing PostConstruct because it is not necessary anymore.
//    It was only used to index existing data into Elasticsearch.

    public void indexExistingData() {
        log.info("Starting batch indexing of existing products into Elasticsearch...");

        List<Product> allProducts = productRepository.findAll();
        for (Product product : allProducts) {
            try {
                elasticSearchService.indexProduct(product);
            } catch (Exception e) {
                log.error("Failed to index product with ID: " + product.getUuid(), e);
            }
        }

        log.info("Batch indexing completed.");
    }
}

