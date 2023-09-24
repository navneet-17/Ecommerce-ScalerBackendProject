package dev.navneet.productservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "categories")
public class Category extends BaseModel {
    @Column
    private String name;
    @OneToMany(mappedBy = "category")
    @Fetch(FetchMode.SUBSELECT)
    private List<Product> products = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public Category() {
    }

    public String getName() {
        return this.name;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
    public String getId() {
        return super.getId();
    }
}








