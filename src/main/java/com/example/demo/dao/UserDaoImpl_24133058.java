package com.example.demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.example.demo.model.User_24133058;

@Repository
public class UserDaoImpl_24133058 implements UserDao_24133058 {

    private DBConnection_24133058 dbConn = new DBConnection_24133058();

    @Override
    public User_24133058 findByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE Username = ?";
        try (Connection conn = dbConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public User_24133058 findByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE Email = ?";
        try (Connection conn = dbConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void insert(User_24133058 user) {
        String sql = "INSERT INTO Users (Username, Password, Phone, Fullname, Email, Admin, Active, Images) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getFullname());
            ps.setString(5, user.getEmail());
            ps.setBoolean(6, user.isAdmin());
            ps.setBoolean(7, user.isActive());
            ps.setString(8, user.getImages());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void update(User_24133058 user) {
        String sql = "UPDATE Users SET Password=?, Phone=?, Fullname=?, Email=?, Admin=?, Active=?, Images=? WHERE Username=?";
        try (Connection conn = dbConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getFullname());
            ps.setString(4, user.getEmail());
            ps.setBoolean(5, user.isAdmin());
            ps.setBoolean(6, user.isActive());
            ps.setString(7, user.getImages());
            ps.setString(8, user.getUsername());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void delete(String username) {
        String sql = "DELETE FROM Users WHERE Username = ?";
        try (Connection conn = dbConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void activateAccount(String email) {
        String sql = "UPDATE Users SET Active = 1 WHERE Email = ?";
        try (Connection conn = dbConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public List<User_24133058> findAllPage(int page, int pageSize) {
        List<User_24133058> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM Users ORDER BY Username OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = dbConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUser(rs));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public int countUsers() {
        String sql = "SELECT COUNT(*) FROM Users";
        try (Connection conn = dbConn.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    private User_24133058 mapUser(ResultSet rs) throws Exception {
        return User_24133058.builder()
                .username(rs.getString("Username"))
                .password(rs.getString("Password"))
                .phone(rs.getString("Phone"))
                .fullname(rs.getString("Fullname"))
                .email(rs.getString("Email"))
                .admin(rs.getBoolean("Admin"))
                .active(rs.getBoolean("Active"))
                .images(rs.getString("Images"))
                .build();
    }
}