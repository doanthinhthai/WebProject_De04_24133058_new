package com.example.demo.dao;

import java.sql.*;
import java.util.*;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Product_24133058;

@Repository
public class ProductDaoImpl_24133058 implements ProductDao_24133058 {

    private final DBConnection_24133058 db =
            new DBConnection_24133058();

    @Override
    public List<Product_24133058> findAll() {
        List<Product_24133058> list = new ArrayList<>();

        String sql = """
            SELECT ProductId, ProductName, Description,
                   Price, Stock, Image, Active
            FROM Products
            WHERE Active = 1
            ORDER BY ProductId DESC
            """;

        try (
            Connection connection = db.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                list.add(mapProduct(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("Không thể tải sản phẩm", e);
        }

        return list;
    }

    @Override
    public Product_24133058 findById(int productId) {
        String sql = """
            SELECT ProductId, ProductName, Description,
                   Price, Stock, Image, Active
            FROM Products
            WHERE ProductId = ? AND Active = 1
            """;

        try (
            Connection connection = db.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapProduct(rs);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Không thể tải sản phẩm", e);
        }

        return null;
    }

    private Product_24133058 mapProduct(ResultSet rs)
            throws SQLException {
        return Product_24133058.builder()
                .productId(rs.getInt("ProductId"))
                .productName(rs.getString("ProductName"))
                .description(rs.getString("Description"))
                .price(rs.getBigDecimal("Price"))
                .stock(rs.getInt("Stock"))
                .image(rs.getString("Image"))
                .active(rs.getBoolean("Active"))
                .build();
    }
}