package dev.navneet.productservice.services;

import dev.navneet.productservice.documents.ProductDocument;
import dev.navneet.productservice.models.Product;
import dev.navneet.productservice.repository_elasticsearch.ElasticSearchProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchService {

    private final ElasticSearchProductRepository elasticSearchProductRepository;

    @Autowired
    public ElasticSearchService(ElasticSearchProductRepository elasticSearchProductRepository) {
        this.elasticSearchProductRepository = elasticSearchProductRepository;
    }

    public Page<ProductDocument> getAllProducts(Pageable pageable) {
        return elasticSearchProductRepository.findAll(pageable);
    }

    // Method to index a Product as ProductDocument in Elasticsearch
    public ProductDocument indexProduct(Product product) {
        ProductDocument productDocument = mapToProductDocument(product);
        return elasticSearchProductRepository.save(productDocument);
    }

    // Method to search for products in Elasticsearch by title
    public Page<ProductDocument> searchProductsByTitleOrDescription(String query, Pageable pageable) {
        return elasticSearchProductRepository.findByTitleOrDescription(query, query, pageable);
    }

    // Helper method to map Product to ProductDocument
    private ProductDocument mapToProductDocument(Product product) {
        return ProductDocument.builder()
                .id(product.getUuid().toString()) // Convert UUID to String
                .title(product.getTitle())
                .description(product.getDescription())
                .image(product.getImage())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .build();
    }

    public void deleteProductFromIndex(String productId) {
        elasticSearchProductRepository.deleteById(productId);
    }


}