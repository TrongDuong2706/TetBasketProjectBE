package com.hanu.isd.repository;

import com.hanu.isd.entity.Basket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    @Query(value = "SELECT * FROM basket b WHERE " +
            "(:name IS NULL OR b.name LIKE %:name%) AND " +
            "(:minPrice IS NULL OR b.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR b.price <= :maxPrice) AND " +
            "(:categoryId IS NULL OR b.category_id = :categoryId) AND " +
            "(:status IS NULL OR b.status = :status)",
            nativeQuery = true)
    Page<Basket> findByNameAndPriceAndCategoryAndStatus(@Param("name") String name,
                                                        @Param("minPrice") BigDecimal minPrice,
                                                        @Param("maxPrice") BigDecimal maxPrice,
                                                        @Param("categoryId") Long categoryId,
                                                        @Param("status") Integer status,
                                                        Pageable pageable);

    @Query(value = "SELECT * FROM basket b WHERE b.name LIKE %:name%", nativeQuery = true)
    Page<Basket> findByName(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT * FROM basket b WHERE b.category_id = 1", nativeQuery = true)
    Page<Basket> findAllBasketCategory(Pageable pageable);

    @Query(value = "SELECT * FROM basket b WHERE b.category_id = :categoryId", nativeQuery = true)
    Page<Basket> findAllRelatedBasket(@Param("categoryId") Long categoryId,Pageable pageable);

    @Query(value = "SELECT DISTINCT b.id, b.name, b.description, b.price, b.quantity, b.created_at, " +
            "b.status, b.category_id, b.basketshell_id " +
            "FROM basket b " +
            "LEFT JOIN item i ON i.basket_id = b.id " +
            "WHERE (:name IS NULL OR b.name LIKE CONCAT('%', :name, '%')) " +
            "AND (:minPrice IS NULL OR b.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR b.price <= :maxPrice) " +
            "AND (:categoryId IS NULL OR b.category_id = :categoryId) " +
            "AND (:status IS NULL OR b.status = :status) " +
            "AND (:basketShellId IS NULL OR b.basketshell_id = :basketShellId) " +
            "AND (:hasAlcohol IS NULL OR " +
            "    (:hasAlcohol = true AND EXISTS (" +
            "        SELECT 1 FROM item i2 " +
            "        WHERE i2.basket_id = b.id " +
            "        AND LOWER(i2.name) LIKE '%rượu%'" +
            "    )) " +
            "    OR (:hasAlcohol = false AND b.id NOT IN (" +
            "        SELECT DISTINCT i3.basket_id " +
            "        FROM item i3 " +
            "        WHERE LOWER(i3.name) LIKE '%rượu%'" +
            "    ))) ",
            countQuery = "SELECT COUNT(DISTINCT b.id) FROM basket b " +
                    "LEFT JOIN item i ON i.basket_id = b.id " +
                    "WHERE (:name IS NULL OR b.name LIKE CONCAT('%', :name, '%')) " +
                    "AND (:minPrice IS NULL OR b.price >= :minPrice) " +
                    "AND (:maxPrice IS NULL OR b.price <= :maxPrice) " +
                    "AND (:categoryId IS NULL OR b.category_id = :categoryId) " +
                    "AND (:status IS NULL OR b.status = :status) " +
                    "AND (:basketShellId IS NULL OR b.basketshell_id = :basketShellId) " +
                    "AND (:hasAlcohol IS NULL OR " +
                    "    (:hasAlcohol = true AND EXISTS (" +
                    "        SELECT 1 FROM item i2 " +
                    "        WHERE i2.basket_id = b.id " +
                    "        AND LOWER(i2.name) LIKE '%rượu%'" +
                    "    )) " +
                    "    OR (:hasAlcohol = false AND b.id NOT IN (" +
                    "        SELECT DISTINCT i3.basket_id " +
                    "        FROM item i3 " +
                    "        WHERE LOWER(i3.name) LIKE '%rượu%'" +
                    "    ))) ",
            nativeQuery = true)
    Page<Basket> findByFilters(@Param("name") String name,
                               @Param("minPrice") BigDecimal minPrice,
                               @Param("maxPrice") BigDecimal maxPrice,
                               @Param("categoryId") Long categoryId,
                               @Param("status") Integer status,
                               @Param("basketShellId") Long basketShellId,
                               @Param("hasAlcohol") Boolean hasAlcohol,
                               Pageable pageable);











}
