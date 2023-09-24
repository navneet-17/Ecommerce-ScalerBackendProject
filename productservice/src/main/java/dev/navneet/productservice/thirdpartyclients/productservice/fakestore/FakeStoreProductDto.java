package dev.navneet.productservice.thirdpartyclients.productservice.fakestore;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FakeStoreProductDto {
//    private Long id; // changing long to String as the FakeStore API returns String
    private String id;
    private String title;
    private double price;
    private String category;
    private String image;
    private String description;
}
