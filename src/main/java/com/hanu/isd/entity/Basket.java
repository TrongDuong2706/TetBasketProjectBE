package com.hanu.isd.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "basket")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", length = 255, nullable = false)
    String name;

    @Column(name = "description", length = 255, nullable = false)
    String description;

    @Column(name = "price")
    Double price;

    @Column(name = "quantity")
    int quantity;

    @Column(name = "created_at", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    Timestamp createdAt;

    @Column(name = "status")
    int status;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = true, foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    @OnDelete(action = OnDeleteAction.SET_NULL)
    BasketCategory category;

    @ManyToOne
    @JoinColumn(name = "basketshell_id", nullable = true, referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    BasketShell basketShell;

    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC") // Đảm bảo thứ tự hình ảnh theo ID tăng dần
    List<BasketImage> images;
    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<CartItem> cartItems;
    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Item> items; // Thêm danh sách item
}
