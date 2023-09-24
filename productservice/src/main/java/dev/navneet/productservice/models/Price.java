package dev.navneet.productservice.models;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@ Setter @NoArgsConstructor
@AllArgsConstructor
@Entity(name="prices")
public class Price extends BaseModel {
    @Column(columnDefinition = "varchar(3) default 'INR'")
    private String currency;
    private double price;
}

