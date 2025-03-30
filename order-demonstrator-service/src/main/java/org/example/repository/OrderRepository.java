package org.example.repository;

import org.example.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.orderItems items WHERE items.product.article = :article")
    List<Order> findAllByProductArticle(@Param("article") String article);
}
