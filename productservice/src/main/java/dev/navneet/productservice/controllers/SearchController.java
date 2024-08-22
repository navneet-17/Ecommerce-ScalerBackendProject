package dev.navneet.productservice.controllers;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.dtos.SearchRequestDto;
import dev.navneet.productservice.services.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }
    @PostMapping
    public Page<GenericProductDto> searchProduct(@RequestBody SearchRequestDto searchRequestDto){
        String query = searchRequestDto.getQuery();
        int pageNumber = searchRequestDto.getPageNumber();
        int pageSize = searchRequestDto.getSizeOfEachPage();
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        return searchService.searchProducts(query,pageable);
    }
}
