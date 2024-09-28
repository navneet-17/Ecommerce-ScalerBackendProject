package dev.navneet.cartservice.models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CartItem {
    private Long id;
    private Long productId;
    private String title;
    private Double price;
    private Integer quantity;
    private Double totalItemPrice;

    // Map for handling unknown dynamic fields
    private Map<String, Object> additionalAttributes = new HashMap<>();

    @JsonAnySetter
    public void setAdditionalAttributes(String key, Object value) {
        additionalAttributes.put(key, value);
    }
    @Override
    public String toString() {
        return "CartItem{" +
                "productId=" + productId +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", additionalAttributes=" + additionalAttributes +
                '}';
    }
}
