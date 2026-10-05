package com.example.demo.dao;

import java.util.List;
import com.example.demo.model.Category_24133058;
import com.example.demo.model.Video_24133058;

public interface VideoDao_24133058 {
    Video_24133058 findById(String videoId);
    List<Video_24133058> findByCategoryIdPage(int categoryId, int page, int pageSize);
    int countByCategoryId(int categoryId);
    List<Category_24133058> findAllCategoriesWithCount();
    Category_24133058 findCategoryById(int categoryId);
}