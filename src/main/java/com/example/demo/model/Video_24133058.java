package com.example.demo.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video_24133058 implements Serializable {
    private String videoId;
    private String title;
    private String poster;
    private int views;
    private String description;
    private boolean active;
    private int categoryId;
    private String categoryName;
    private int shareCount;
    private int likeCount;

    private BigDecimal price;
    private int stock;
    private String videoUrl;
}
