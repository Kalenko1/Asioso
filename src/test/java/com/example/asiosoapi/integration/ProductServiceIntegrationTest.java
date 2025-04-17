package com.example.asiosoapi.integration;

import com.example.asiosoapi.model.Product;
import com.example.asiosoapi.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;// Spring will automatically inject the ProductService
    @Test
    public void testGetAllProductsFromRealApi() {

        List<Product> products = productService.getAllProducts();


        assertNotNull(products, "The product list cannot be null");
        assertFalse(products.isEmpty(), "The product list cannot be empty");


        Product first = products.get(0);
        assertNotNull(first.getName(), "Product name cannot be null");
        assertTrue(first.getPrice() > 0, "The product price must be positive");
    }

    @Test
    public void testGetProductByIdFromRealApi() {
        Product product = productService.getProductById(1L);

        assertNotNull(product, "The product cannot be null");
        assertEquals(1L, product.getId(), "The product ID should be 1");
        assertNotNull(product.getName(), "The product name cannot be null");
        assertTrue(product.getPrice() > 0, "The product price must be positive");
    }
}
