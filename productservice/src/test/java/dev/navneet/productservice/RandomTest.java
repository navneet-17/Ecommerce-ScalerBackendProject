package dev.navneet.productservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
public class RandomTest {
    @Test
    void testLessThan3() {
        Random random = new Random();
        int number = random.nextInt();
        System.out.println(number);
        assert(number < 10000000);
    }
}
