package com.example.demo.dao;

import java.util.List;
import com.example.demo.model.Product_24133058;

public interface ProductDao_24133058 {
    List<Product_24133058> findAll();
    Product_24133058 findById(int productId);
}