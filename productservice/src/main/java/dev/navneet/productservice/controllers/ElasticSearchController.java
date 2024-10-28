package dev.navneet.productservice.controllers;

import dev.navneet.productservice.documents.ProductDocument;
import dev.navneet.productservice.models.Product;
import dev.navneet.productservice.services.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search/es")
public class ElasticSearchController {

    private final ElasticSearchService elasticSearchService;

    @Autowired
    public ElasticSearchController(ElasticSearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    // New endpoint to get all products in Elasticsearch with pagination
    @GetMapping("/all")
    public Page<ProductDocument> getAllProducts(@PageableDefault(size = 25) Pageable pageable) {
        return elasticSearchService.getAllProducts(pageable);
    }
        // Endpoint to search for products in Elasticsearch by title
    @GetMapping("")
    public Page<ProductDocument> searchProductsByTitleOrDescription(@RequestParam String query,
                                                       @PageableDefault(size = 25) Pageable pageable) {
        return elasticSearchService.searchProductsByTitleOrDescription(query, pageable);
    }

    // Endpoint to index a product in Elasticsearch
    @PostMapping("/index")
    public ProductDocument indexProduct(@RequestBody Product product) {
        return elasticSearchService.indexProduct(product);
    }

}
