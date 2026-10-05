package com.example.demo.dao;

import java.sql.*;
import java.util.*;
import com.example.demo.model.*;

class LegacyOrderDaoImpl_24133058 {
    private final DBConnection_24133058 db = new DBConnection_24133058();

    public long createCodOrder(String username, String receiverName, String phone,
                               String address, String note, Cart_24133058 cart) {
        String insertOrder = "INSERT INTO Orders (Username, ReceiverName, Phone, Address, Note, "
                + "PaymentMethod, Status, TotalAmount) OUTPUT INSERTED.OrderId "
                + "VALUES (?, ?, ?, ?, ?, 'COD', 'NEW', ?)";
        String checkStock = "SELECT Stock FROM Videos WITH (UPDLOCK, ROWLOCK) "
                + "WHERE VideoId = ? AND Active = 1";
        String insertItem = "INSERT INTO OrderItems (OrderId, VideoId, VideoTitle, UnitPrice, Quantity) "
                + "VALUES (?, ?, ?, ?, ?)";
        String updateStock = "UPDATE Videos SET Stock = Stock - ? "
                + "WHERE VideoId = ? AND Active = 1 AND Stock >= ?";

        try (Connection connection = db.getConnection()) {
            connection.setAutoCommit(false);
            try {
                for (CartItem_24133058 item : cart.getItems()) {
                    try (PreparedStatement ps = connection.prepareStatement(checkStock)) {
                        ps.setString(1, item.getVideo().getVideoId());
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next() || rs.getInt("Stock") < item.getQuantity()) {
                                throw new IllegalArgumentException("Video '" + item.getVideo().getTitle()
                                        + "' không đủ tồn kho");
                            }
                        }
                    }
                }

                long orderId;
                try (PreparedStatement ps = connection.prepareStatement(insertOrder)) {
                    ps.setString(1, username);
                    ps.setString(2, receiverName);
                    ps.setString(3, phone);
                    ps.setString(4, address);
                    ps.setString(5, note);
                    ps.setBigDecimal(6, cart.getTotalAmount());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("Không lấy được mã đơn hàng");
                        orderId = rs.getLong(1);
                    }
                }

                for (CartItem_24133058 item : cart.getItems()) {
                    Video_24133058 video = item.getVideo();
                    try (PreparedStatement ps = connection.prepareStatement(insertItem)) {
                        ps.setLong(1, orderId);
                        ps.setString(2, video.getVideoId());
                        ps.setString(3, video.getTitle());
                        ps.setBigDecimal(4, video.getPrice());
                        ps.setInt(5, item.getQuantity());
                        ps.executeUpdate();
                    }
                    try (PreparedStatement ps = connection.prepareStatement(updateStock)) {
                        ps.setInt(1, item.getQuantity());
                        ps.setString(2, video.getVideoId());
                        ps.setInt(3, item.getQuantity());
                        if (ps.executeUpdate() != 1) {
                            throw new SQLException("Tồn kho vừa thay đổi, vui lòng thử lại");
                        }
                    }
                }

                connection.commit();
                return orderId;
            } catch (Exception e) {
                connection.rollback();
                if (e instanceof IllegalArgumentException) throw (IllegalArgumentException) e;
                throw new IllegalStateException("Không thể tạo đơn hàng COD", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể kết nối cơ sở dữ liệu", e);
        } catch (Exception e) {
            if (e instanceof RuntimeException) throw (RuntimeException) e;
            throw new IllegalStateException(e);
        }
    }

    public List<Order_24133058> findByUsername(String username, OrderStatus_24133058 status) {
        String sql = "SELECT o.OrderId, o.Username, o.ReceiverName, o.Phone, o.Address, o.Note, "
                + "o.PaymentMethod, o.Status, o.TotalAmount, o.CreatedAt, oi.OrderItemId, "
                + "oi.VideoId, oi.VideoTitle, oi.UnitPrice, oi.Quantity FROM Orders o "
                + "LEFT JOIN OrderItems oi ON o.OrderId = oi.OrderId WHERE o.Username = ? "
                + (status == null ? "" : "AND o.Status = ? ")
                + "ORDER BY o.CreatedAt DESC, o.OrderId DESC, oi.OrderItemId";
        Map<Long, Order_24133058> orders = new LinkedHashMap<>();

        try (Connection connection = db.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            if (status != null) ps.setString(2, status.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long orderId = rs.getLong("OrderId");
                    Order_24133058 order = orders.get(orderId);
                    if (order == null) {
                        Timestamp createdAt = rs.getTimestamp("CreatedAt");
                        order = Order_24133058.builder()
                                .orderId(orderId)
                                .username(rs.getString("Username"))
                                .receiverName(rs.getString("ReceiverName"))
                                .phone(rs.getString("Phone"))
                                .address(rs.getString("Address"))
                                .note(rs.getString("Note"))
                                .paymentMethod(rs.getString("PaymentMethod"))
                                .status(OrderStatus_24133058.valueOf(rs.getString("Status")))
                                .totalAmount(rs.getBigDecimal("TotalAmount"))
                                .createdAt(createdAt == null ? null : createdAt.toLocalDateTime())
                                .build();
                        orders.put(orderId, order);
                    }

                    long itemId = rs.getLong("OrderItemId");
                    if (!rs.wasNull()) {
                        order.getItems().add(OrderItem_24133058.builder()
                                .orderItemId(itemId)
                                .videoId(rs.getString("VideoId"))
                                .videoTitle(rs.getString("VideoTitle"))
                                .unitPrice(rs.getBigDecimal("UnitPrice"))
                                .quantity(rs.getInt("Quantity"))
                                .build());
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Không thể tải lịch sử đơn hàng", e);
        }
        return new ArrayList<>(orders.values());
    }
}
