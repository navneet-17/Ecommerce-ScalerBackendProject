package dev.navneet.cartservice.dtos;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CartItemDto implements Serializable {
    private Long productId;
    private String title;
    private Double price;
    private Integer quantity;
    private Double totalItemPrice;

    // Map for handling unknown dynamic fields
    private Map<String, Object> additionalAttributes = new HashMap<>();  // Generic map for dynamic fields

    // Jackson's annotation to handle any dynamic fields
    @JsonAnySetter
    public void setAdditionalAttributes(String key, Object value) {
        additionalAttributes.put(key, value);
    }

    @Override
    public String toString() {
        return "CartItemDto{" +
                "productId=" + productId +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", additionalAttributes=" + additionalAttributes +
                '}';
    }

}
