package dev.navneet.productservice.services;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.dtos.SortParameter;
import dev.navneet.productservice.exceptions.InvalidSortTypeParameter;
import dev.navneet.productservice.models.Product;
import dev.navneet.productservice.repositories.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SearchService {
    private final ProductRepository productRepository;
    private final SelfProductServiceImpl selfProductServiceImpl;
    SearchService(ProductRepository productRepository,
                  SelfProductServiceImpl selfProductServiceImpl){
        this.productRepository = productRepository;
        this.selfProductServiceImpl = selfProductServiceImpl;
    }

    public Page<GenericProductDto> searchProducts (
           String query, Pageable pageable,
           List<SortParameter> sortByParameters){
      // Initialize with a default value
        Sort sort = Sort.unsorted();

      // Implement the sorting logic only if the sorting parameter is not null
        if (sortByParameters != null && !sortByParameters.isEmpty()){
            // Iterate through the list of sorting parameters and sort accordingly.
                for(SortParameter sortParam: sortByParameters){
                    if (!sortParam.getSortOrder().equalsIgnoreCase("ASC") &&
                            !sortParam.getSortOrder().equalsIgnoreCase("DESC")){
                        throw new InvalidSortTypeParameter("Invalid sort order: '" + sortParam.getSortOrder() +
                                "'.  Use 'ASC' for ascending and 'DESC' for descending ");
                    }

                    // Apply sorting logic
                    Sort currentSortingParam = sortParam.getSortOrder().equalsIgnoreCase("ASC")
                            ? Sort.by(sortParam.getSortBy())
                            :  Sort.by(sortParam.getSortBy()).descending();

                    // Combine the sorts if there are multiple sort parameters
                    if (sort.isUnsorted()) {
                        sort = currentSortingParam;
                    } else {
                        sort = sort.and(currentSortingParam);
                    }
                }
        }

        // we append the pageable object to also accommodate our sort parameter now
        pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), sort);

        // Fetch products from the repository
        Page<Product> productPage = productRepository
                                                .findAllByTitleContaining(query, pageable);

        // Convert each Product to a GenericProductDto and add it to the list
        List<GenericProductDto> genericProductDtos = new ArrayList<>();

        for (Product product : productPage) {
            GenericProductDto dto = selfProductServiceImpl
                                                .convertProductToGenericProductDto(product);
            genericProductDtos.add(dto);
        }
        // Create a new PageImpl object using the list of DTOs, the Pageable,
        // and the total number of elements and return it
        return new PageImpl<>(
                genericProductDtos,
                productPage.getPageable(),
                productPage.getTotalElements()
        );
    }
}
