package dev.navneet.productservice.repository_elasticsearch;

import dev.navneet.productservice.documents.ProductDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticSearchProductRepository extends ElasticsearchRepository<ProductDocument, String> {
    Page<ProductDocument> findByTitleOrDescription(String title, String description, Pageable pageable);
}