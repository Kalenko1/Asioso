package com.example.asiosoapi.service;
import org.springframework.web.client.RestTemplate;
import com.example.asiosoapi.model.Product;
import com.example.asiosoapi.model.ProductResponse;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    @Test
    public void testGetAllProductsReturnsProductsWithDetails() {
        // Fake RestTemplate that returns fixed responses
        RestTemplate restTemplate = new RestTemplate() {

            public <T> T getForObject(String url, Class<T> responseType) {
                if (url.equals("https://api.predic8.de/shop/v2/products/")) {
                    Product product = new Product();
                    product.setId(1L);
                    product.setName("Test Product");
                    product.setSelfLink("/shop/v2/products/1");

                    ProductResponse response = new ProductResponse();
                    response.setProducts(Arrays.asList(product));
                    return responseType.cast(response);
                } else if (url.equals("https://api.predic8.de/shop/v2/products/1")) {
                    Product detailed = new Product();
                    detailed.setPrice(10.99);  // ✅ Ispravljeno
                    detailed.setImageLink("/img/test.png");
                    return responseType.cast(detailed);
                }
                return null;
            }
        };

        ProductService productService = new ProductService(restTemplate);
        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).getName());
        assertEquals(10.99, products.get(0).getPrice());
        assertEquals("https://api.predic8.de/img/test.png", products.get(0).getImageLink());
    }

    @Test
    public void testGetProductByIdReturnsDetailedProduct() {
        RestTemplate restTemplate = new RestTemplate() {

            public <T> T getForObject(String url, Class<T> responseType) {
                if (url.equals("https://api.predic8.de/shop/v2/products/5")) {
                    Product product = new Product();
                    product.setId(5L);
                    product.setName("Specific Product");
                    product.setImageLink("/img/product.png");
                    return responseType.cast(product);
                }
                return null;
            }
        };

        ProductService productService = new ProductService(restTemplate);
        Product product = productService.getProductById(5L);

        assertNotNull(product);
        assertEquals("Specific Product", product.getName());
        assertEquals("https://api.predic8.de/img/product.png", product.getImageLink());
    }
}
