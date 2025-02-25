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
}
