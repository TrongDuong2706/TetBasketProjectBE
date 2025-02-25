package com.hanu.isd.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BasketRequest {
    String description;
    String name;
    double price;
    int quantity;
    Long categoryId;
    int status;
    Long basketShellId;
    Set<String> itemNames = new HashSet<>();


}
