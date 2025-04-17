package com.example.asiosoapi.service;
import com.example.asiosoapi.model.Product;
import com.example.asiosoapi.model.ProductResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProductService {

    private final RestTemplate restTemplate;
    private static final String BASE_URL = "https://api.predic8.de";
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Method for fetching all products
    public List<Product> getAllProducts() {
        logger.info("Fetching list of all products...");
        try {
            ProductResponse response = restTemplate.getForObject(BASE_URL + "/shop/v2/products/", ProductResponse.class);

            if (response == null || response.getProducts() == null) {
                logger.warn("ProductResponse or product list is null.");
                return Collections.emptyList();
            }

            List<Product> enrichedProducts = new ArrayList<>();
            for (Product p : response.getProducts()) {
                try {
                    // Fetching detailed product data
                    Product detailed = restTemplate.getForObject(BASE_URL + p.getSelfLink(), Product.class);
                    if (detailed != null) {
                        p.setPrice(detailed.getPrice());  // Set price

                        // Handle image link properly
                        if (detailed.getImageLink() != null) {
                            String imageUrl = detailed.getImageLink();
                            // Ensure the URL is correctly formed (only append BASE_URL if necessary)
                            if (!imageUrl.startsWith("http")) {
                                imageUrl = BASE_URL + imageUrl;
                            }
                            p.setImageLink(imageUrl);
                        }

                        p.setVendors(detailed.getVendors());  // Set vendors
                    }
                } catch (Exception ex) {
                    logger.error("Failed to fetch detailed info for product with ID: " + p.getId(), ex);
                }
                enrichedProducts.add(p);
            }

            return enrichedProducts;
        } catch (Exception e) {
            logger.error("Error while fetching all products", e);
            return Collections.emptyList();
        }
    }

    // Method for fetching product by ID
    public Product getProductById(Long id) {
        logger.info("Fetching product by ID: {}", id);
        try {
            Product product = restTemplate.getForObject(BASE_URL + "/shop/v2/products/" + id, Product.class);
            if (product != null && product.getImageLink() != null) {
                String imageUrl = product.getImageLink();
                if (!imageUrl.startsWith("http")) {
                    imageUrl = BASE_URL + imageUrl;
                }
                product.setImageLink(imageUrl);  // Ensure image URL is fully qualified
            }
            return product;
        } catch (Exception e) {
            logger.error("Error while fetching product by ID: {}", id, e);
            return null;
        }
    }
}