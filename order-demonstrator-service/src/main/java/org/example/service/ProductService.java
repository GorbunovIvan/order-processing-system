package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public Product findByArticle(String article) {
        log.info("Finding product by article: {}", article);
        return productRepository.findByArticle(article)
                .orElse(null);
    }

    public List<Product> findAllByArticleIn(Collection<String> articles) {
        log.info("Finding products by articles in: {}", articles);
        return productRepository.findAllByArticleIn(articles);
    }

    @Transactional
    public List<Product> findOrSaveAll(Collection<Product> productsIn) {

        var products = new ArrayList<>(productsIn);

        var productArticles = products.stream()
                .map(Product::getArticle)
                .collect(Collectors.toSet());

        var productsFound = findAllByArticleIn(productArticles);
        if (productsFound.size() == productArticles.size()) {
            return productsFound;
        }

        products.removeAll(productsFound);

        log.info("Saving new products: {}", products);
        var productsSaved = productRepository.saveAll(products);
        productsSaved.addAll(productsFound);

        return productsSaved;
    }
}
