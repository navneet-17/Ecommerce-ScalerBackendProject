package dev.navneet.productservice.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter@Setter
public class SearchRequestDto {
    private  String query;
    private int pageNumber;
    private int sizeOfEachPage;
    private List<SortParameter> sortByParameters;
}
