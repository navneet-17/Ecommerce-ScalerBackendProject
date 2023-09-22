package dev.navneet.productservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
@Entity(name="orders")
public class Order extends BaseModel{
    @ManyToMany
    @JoinTable(name = "product_orders",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private List<Product> product;
}







