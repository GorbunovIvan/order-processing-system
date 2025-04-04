package org.example.repository;

import org.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByArticle(String article);
    List<Product> findAllByArticleIn(Collection<String> articles);
}
