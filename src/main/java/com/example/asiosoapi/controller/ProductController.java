package com.example.asiosoapi.controller;

import com.example.asiosoapi.model.Product;
import com.example.asiosoapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String getAllProducts(Model model) {
        long start = System.currentTimeMillis(); //  start

        logger.info("GET /products request – loading all products.");
        List<Product> products = productService.getAllProducts();

        long end = System.currentTimeMillis(); //  end
        logger.info("Product loading completed in {} ms", (end - start));

        if (products == null || products.isEmpty()) {
            logger.warn("Product list is empty or null.");
        } else {
            logger.info("Loaded {} products.", products.size());
        }

        model.addAttribute("products", products);
        return "product-list";
    }


    @GetMapping("/products/{id}")
    public String getProductDetail(@PathVariable("id") Long productId, Model model) {
        logger.info("GET /products/{} request – product details", productId);

        Product product = productService.getProductById(productId);

        if (product == null) {
            logger.error("Product with ID {} not found", productId);
            model.addAttribute("errorMessage", "Product not found.");
            return "error"; // shows the error page (error.html)
        }

        logger.info("Product details: {} (Price: {})", product.getName(), product.getPrice());

        model.addAttribute("product", product);
        return "product-detail";
    }
}