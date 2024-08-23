package dev.navneet.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SortParameter {
    private String sortBy; // sort by column name
    private String sortOrder; // ascending / descending
}
