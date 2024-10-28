package dev.navneet.productservice.documents;


import lombok.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "products")
public class ProductDocument {

    @Id
    private String id;  // For Elasticsearch, store UUID as a String

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    private String image;

    // Store only the name of the category to keep it simple for Elasticsearch indexing
    @Field(type = FieldType.Keyword)
    private String categoryName;

    // Method to set the id from Product's UUID
    public void setIdFromUUID(UUID uuid) {
        this.id = uuid.toString();
    }
}