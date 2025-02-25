package com.hanu.isd.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name; // Tên item

    @ManyToOne
    @JoinColumn(name = "basket_id", nullable = false)
    Basket basket; // Mối quan hệ với Basket
}
