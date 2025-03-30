package org.example.service;

import org.example.Utils;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockitoSpyBean
    private ProductRepository productRepository;

    @Test
    void shouldReturnNullWhenFindByArticle() {
        var article = "absent@test.com";
        var productResult = productService.findByArticle(article);
        assertNull(productResult);
        verify(productRepository, times(1)).findByArticle(article);
    }

    @Test
    @Sql(scripts = "/setup-test-products.sql",
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)  // Forces separate transaction
    )
    void shouldReturnProductWhenFindByArticle() {
        var article = "111article111";  // Should exist in the DB!
        var productResult = productService.findByArticle(article);
        assertNotNull(productResult);
        assertEquals(article, productResult.getArticle());
        verify(productRepository, times(1)).findByArticle(article);
    }

    @Test
    void shouldReturnEmptyListWhenFindAllByArticleIn() {
        List<String> articles = List.of("111article111", "222article222", "333article333");
        var productsResult = productService.findAllByArticleIn(articles);
        assertTrue(productsResult.isEmpty());
        verify(productRepository, times(1)).findAllByArticleIn(articles);
    }

    @Test
    @Sql(scripts = "/setup-test-products.sql",
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)  // Forces separate transaction
    )
    void shouldReturnProductsWhenFindAllByArticleIn() {
        List<String> articles = List.of("111article111", "222article222", "333article333");  // Should exist in the DB!
        var productsResult = productService.findAllByArticleIn(articles);
        assertFalse(productsResult.isEmpty());
        assertEquals(3, productsResult.size());
        verify(productRepository, times(1)).findAllByArticleIn(articles);
    }

    @Test
    void shouldCreateAndReturnAllProductsWhenFindOrSaveProducts() {

        List<Product> products = List.of(
                Utils.generateRandomProduct(true),
                Utils.generateRandomProduct(true),
                Utils.generateRandomProduct(true)
        );

        var articles = products.stream().map(Product::getArticle).collect(Collectors.toSet());

        var productsResult = productService.findOrSaveAll(products);
        assertEquals(products.size(), productsResult.size());
        assertEquals(products, productsResult);

        verify(productRepository, times(1)).findAllByArticleIn(articles);
        verify(productRepository, times(1)).saveAll(products);
    }

    @Test
    @Sql(scripts = "/setup-test-products.sql",
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)  // Forces separate transaction
    )
    void shouldReturnExistingProductsWhenFindOrSaveProducts() {

        var products = productRepository.findAll();
        assertEquals(3, products.size());  // Should exist in the DB!
        products.forEach(product -> product.setId(null));

        var articles = products.stream().map(Product::getArticle).collect(Collectors.toSet());

        var productsResult = productService.findOrSaveAll(products);
        assertEquals(products.size(), productsResult.size());
        assertEquals(products, productsResult);

        verify(productRepository, times(1)).findAllByArticleIn(articles);
        verify(productRepository, never()).saveAll(any());
    }

    @Test
    @Sql(scripts = "/setup-test-products.sql",
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)  // Forces separate transaction
    )
    void shouldReturnPartiallyExistingAndNewlyCreatedProductsWhenFindOrSaveProducts() {

        // Existing products
        var productsExisting = productRepository.findAll();
        assertEquals(3, productsExisting.size());  // Should exist in the DB!
        productsExisting.forEach(product -> product.setId(null));

        // New products
        List<Product> productsNew = List.of(
                Utils.generateRandomProduct(true),
                Utils.generateRandomProduct(true),
                Utils.generateRandomProduct(true)
        );

        // All products mixed
        var products = new ArrayList<>(productsExisting);
        products.addAll(productsNew);

        var articles = products.stream().map(Product::getArticle).collect(Collectors.toSet());

        var productsResult = productService.findOrSaveAll(products);
        assertEquals(products.size(), productsResult.size());
        assertEquals(new HashSet<>(products), new HashSet<>(productsResult));

        verify(productRepository, times(1)).findAllByArticleIn(articles);
        verify(productRepository, times(1)).saveAll(productsNew);
    }
}