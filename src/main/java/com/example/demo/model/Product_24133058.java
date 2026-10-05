package com.example.demo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product_24133058 implements Serializable {
    private int productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private int stock;
    private String image;
    private boolean active;
}