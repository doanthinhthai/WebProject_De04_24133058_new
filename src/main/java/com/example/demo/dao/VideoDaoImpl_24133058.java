package com.example.demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Category_24133058;
import com.example.demo.model.Video_24133058;

@Repository
public class VideoDaoImpl_24133058 implements VideoDao_24133058 {

    private DBConnection_24133058 dbConn = new DBConnection_24133058();

    @Override
    public Video_24133058 findById(String videoId) {
        String sql = "SELECT v.*, c.Categoryname, "
                + "(SELECT COUNT(*) FROM Shares s WHERE s.VideoId = v.VideoId) AS ShareCount, "
                + "(SELECT COUNT(*) FROM Favorites f WHERE f.VideoId = v.VideoId) AS LikeCount "
                + "FROM Videos v "
                + "LEFT JOIN Category c ON v.CategoryId = c.CategoryId "
                + "WHERE v.VideoId = ?";
        try (Connection conn = dbConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, videoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Video_24133058.builder()
                            .videoId(rs.getString("VideoId"))
                            .title(rs.getString("Title"))
                            .poster(rs.getString("Poster"))
                            .views(rs.getInt("Views"))
                            .description(rs.getString("Description"))
                            .active(rs.getBoolean("Active"))
                            .categoryId(rs.getInt("CategoryId"))
                            .categoryName(rs.getString("Categoryname"))
                            .shareCount(rs.getInt("ShareCount"))
                            .likeCount(rs.getInt("LikeCount"))
                            .price(rs.getBigDecimal("Price"))
                            .stock(rs.getInt("Stock"))
                            .videoUrl(rs.getString("VideoUrl"))
                            .build();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<Video_24133058> findByCategoryIdPage(int categoryId, int page, int pageSize) {
        List<Video_24133058> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT v.*, c.Categoryname, "
                + "(SELECT COUNT(*) FROM Shares s WHERE s.VideoId = v.VideoId) AS ShareCount, "
                + "(SELECT COUNT(*) FROM Favorites f WHERE f.VideoId = v.VideoId) AS LikeCount "
                + "FROM Videos v "
                + "LEFT JOIN Category c ON v.CategoryId = c.CategoryId "
                + "WHERE v.CategoryId = ? "
                + "ORDER BY v.VideoId "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = dbConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.setInt(2, offset);
            ps.setInt(3, pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(Video_24133058.builder()
                            .videoId(rs.getString("VideoId"))
                            .title(rs.getString("Title"))
                            .poster(rs.getString("Poster"))
                            .views(rs.getInt("Views"))
                            .description(rs.getString("Description"))
                            .active(rs.getBoolean("Active"))
                            .categoryId(rs.getInt("CategoryId"))
                            .categoryName(rs.getString("Categoryname"))
                            .shareCount(rs.getInt("ShareCount"))
                            .likeCount(rs.getInt("LikeCount"))
                            .price(rs.getBigDecimal("Price"))
                            .stock(rs.getInt("Stock"))
                            .videoUrl(rs.getString("VideoUrl"))
                            .build());
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public int countByCategoryId(int categoryId) {
        String sql = "SELECT COUNT(*) FROM Videos WHERE CategoryId = ?";
        try (Connection conn = dbConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    @Override
    public List<Category_24133058> findAllCategoriesWithCount() {
        List<Category_24133058> list = new ArrayList<>();
        String sql = "SELECT c.*, (SELECT COUNT(*) FROM Videos v WHERE v.CategoryId = c.CategoryId) AS VideoCount FROM Category c";
        try (Connection conn = dbConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(Category_24133058.builder()
                        .categoryId(rs.getInt("CategoryId"))
                        .categoryname(rs.getString("Categoryname"))
                        .categorycode(rs.getString("Categorycode"))
                        .images(rs.getString("Images"))
                        .status(rs.getBoolean("Status"))
                        .videoCount(rs.getInt("VideoCount"))
                        .build());
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public Category_24133058 findCategoryById(int categoryId) {
        String sql = "SELECT c.*, (SELECT COUNT(*) FROM Videos v WHERE v.CategoryId = c.CategoryId) AS VideoCount FROM Category c WHERE c.CategoryId = ?";
        try (Connection conn = dbConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Category_24133058.builder()
                            .categoryId(rs.getInt("CategoryId"))
                            .categoryname(rs.getString("Categoryname"))
                            .categorycode(rs.getString("Categorycode"))
                            .images(rs.getString("Images"))
                            .status(rs.getBoolean("Status"))
                            .videoCount(rs.getInt("VideoCount"))
                            .build();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}
