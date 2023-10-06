package dev.navneet.productservice.controllers;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.dtos.ProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;
import dev.navneet.productservice.services.FakestoreProductService;
import dev.navneet.productservice.services.ProductService;
import dev.navneet.productservice.thirdpartyclients.productservice.fakestore.FakeStoreProductServiceClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Random;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductControllerTest {

    @Autowired
    private FakeStoreProductServiceClient fakeStoreProductServiceClient;

    @Autowired
    private ProductController productController;

    @MockBean
    private ProductService productService;

    @Autowired
    private FakestoreProductService fakestoreProductService;

//    @Test
//    void returnsNullWhenProductDoesntExist() throws NotFoundException {
//        when(
//                productService.getProductById(any(Long.class))
//        ).thenReturn(null);
//
//
//        GenericProductDto genericProductDto = productController.getProductById(121L);
////        given
//
//
//        assertNull(genericProductDto);
//    }

    @Test
    void throwsExceptionWhenProductDoesntExist() throws NotFoundException {
        when(
                productService.getProductById(any(UUID.class))
        )
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> productController.getProductById(String.valueOf(123L)));
    }

    @Test
    void returnsSameProductAsServiceWhenProductExists() throws NotFoundException {
        ProductDto productDto = new ProductDto();
        when(
                productService.getProductById(any(UUID.class))
        )
                .thenReturn(productDto);

        assertEquals(productDto.getPrice(), productController.getProductById(String.valueOf(123L)).getPrice());

//        assertThrows(NotFoundException.class, () -> productController.getProductById(123L));
    }

    @Test
    void shouldReturnTitleNamanWithProductID1() throws NotFoundException {
        ProductDto productDto = new ProductDto();
        productDto.setTitle("Naman");
        when(
                productService.getProductById(1L)
        ).thenReturn(
                productDto
        );


        ProductDto genericProductDto1 = productController.getProductById(String.valueOf(1L));
        assertEquals("Naman", genericProductDto1.getTitle());
    }

    @Test
    @DisplayName("1 + 1 equals 2")
    void onePlusOneEqualsTrue() throws NotFoundException {
//        System.out.println("It is true");
//        assertEquals(11, 1 + 1, "one plus is not coming to be 11");

//        assert

//        assertNull(fakeStoryProductServiceClient.getProductById(101L));

//        Exception e;
//
//        try {
//            fakeStoryProductServiceClient.getProductById(101L);
//        } catch (Exception ex) {
//            e = ex;
//        }
//
//        assertNotNull(e);
//        assertEquals(NotFoundException.class, e.getClass());

//        assertEquals(null, fakeStoryProductServiceClient.getProductById(101L));
//        assertThrows(NotFoundException.class, () -> fakeStoryProductServiceClient.getProductById(101L));
//
//        assertEquals(true, 1 + 1 == 2);
        assertTrue(returnSomething());
    }

    boolean returnSomething() {
        Random random = new Random();
        return random.nextInt() % 2 == 0;
    }

    @Test
    void additionShouldBeCorrect() {
        assertTrue(-1 + -1 == -2, "adding 2 negatives is not correct");

        assertTrue(-1 + 0 == -1, "adding a negative and a zero is giving wrong answer");

        assertTrue(-1 + 1 == 0);

        assert 1 + 0 == 1;

        assert 1 + 1 == 2;
    }
}

// Assertion Framework
// -> allows you to make assertions
// -> allows you to make checks