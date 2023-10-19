package com.github.onsync.ecommerce;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ECommerceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void mainTest() {
        Assertions.assertDoesNotThrow(() -> ECommerceApplication.main(new String[]{}));
    }

}
