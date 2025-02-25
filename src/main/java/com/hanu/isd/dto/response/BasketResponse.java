package com.hanu.isd.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BasketResponse {
    Long id;
    String description;
    String name;
    double price;
    int quantity;
    int categoryId;
    int status;
    Long basketShellId;
    Timestamp createAt;
    List<BasketImageResponse> images;
    Set<String> itemNames; // Danh sách tên các mục

}
