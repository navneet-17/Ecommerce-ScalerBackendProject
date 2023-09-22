package dev.navneet.productservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

@Builder @Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@Entity(name = "products")
public class Product extends BaseModel {
    private String title;
    private String description;
    private String image;
    @ManyToOne
    private Category category;
    @OneToOne
    private Price price;
}