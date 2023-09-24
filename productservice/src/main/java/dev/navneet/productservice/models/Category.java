package dev.navneet.productservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter @Getter
@Entity(name = "categories")
public class Category extends BaseModel {
    @Column
    private String name;
    @OneToMany(mappedBy = "category")
    private List<Product> products;

    public String getId() {
        return super.getId();
    }
}








