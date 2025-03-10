package com.hanu.isd.repository;

import com.hanu.isd.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT * FROM orders o WHERE " +
            "(:fullName IS NULL OR o.full_name LIKE %:fullName%) " +
            "AND (:minPrice IS NULL OR o.final_amount >= :minPrice) " +
            "AND (:maxPrice IS NULL OR o.final_amount <= :maxPrice)",
            nativeQuery = true)
    Page<Order> findByFullNameAndPriceRange(
            @Param("fullName") String fullName,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );
}
