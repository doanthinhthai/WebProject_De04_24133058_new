package com.example.demo.dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import org.springframework.stereotype.Repository;
import com.example.demo.model.*;

@Repository
public class OrderDaoJdbc_24133058 implements OrderDao_24133058 {
    private final DBConnection_24133058 db = new DBConnection_24133058();

    @Override
    public long createCodOrder(String username, String receiverName, String phone,
                               String address, String note, Cart_24133058 cart) {
        return createOrder(username, receiverName, phone, address, note, cart,
                "COD", PaymentStatus_24133058.UNPAID);
    }

    @Override
    public long createVnpayOrder(String username, String receiverName, String phone,
                                 String address, String note, Cart_24133058 cart) {
        return createOrder(username, receiverName, phone, address, note, cart,
                "VNPAY", PaymentStatus_24133058.PENDING);
    }

    private long createOrder(String username, String receiverName, String phone,
                             String address, String note, Cart_24133058 cart,
                             String paymentMethod, PaymentStatus_24133058 paymentStatus) {
        String insertOrder = "INSERT INTO Orders (Username, ReceiverName, Phone, Address, Note, "
                + "PaymentMethod, PaymentStatus, Status, TotalAmount) OUTPUT INSERTED.OrderId "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, 'NEW', ?)";
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
                    ps.setString(6, paymentMethod);
                    ps.setString(7, paymentStatus.name());
                    ps.setBigDecimal(8, cart.getTotalAmount());
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
                throw new IllegalStateException("Không thể tạo đơn hàng", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Không thể kết nối cơ sở dữ liệu", e);
        }
    }

    @Override
    public Order_24133058 findById(long orderId) {
        String sql = "SELECT OrderId, Username, ReceiverName, Phone, Address, Note, PaymentMethod, "
                + "PaymentStatus, PaymentTransactionNo, PaidAt, Status, TotalAmount, CreatedAt "
                + "FROM Orders WHERE OrderId = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapOrder(rs) : null;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Không thể tải đơn hàng", e);
        }
    }

    @Override
    public boolean completeVnpayPayment(long orderId, BigDecimal amount, String transactionNo) {
        String select = "SELECT PaymentMethod, PaymentStatus, TotalAmount FROM Orders "
                + "WITH (UPDLOCK, HOLDLOCK) WHERE OrderId = ?";
        String update = "UPDATE Orders SET PaymentStatus = 'PAID', PaymentTransactionNo = ?, "
                + "PaidAt = SYSDATETIME() WHERE OrderId = ? AND PaymentStatus = 'PENDING'";
        try (Connection connection = db.getConnection()) {
            connection.setAutoCommit(false);
            try {
                String method;
                String status;
                BigDecimal expectedAmount;
                try (PreparedStatement ps = connection.prepareStatement(select)) {
                    ps.setLong(1, orderId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            connection.rollback();
                            return false;
                        }
                        method = rs.getString("PaymentMethod");
                        status = rs.getString("PaymentStatus");
                        expectedAmount = rs.getBigDecimal("TotalAmount");
                    }
                }
                if (!"VNPAY".equals(method) || expectedAmount.compareTo(amount) != 0) {
                    connection.rollback();
                    return false;
                }
                if ("PAID".equals(status)) {
                    connection.commit();
                    return true;
                }
                if (!"PENDING".equals(status)) {
                    connection.rollback();
                    return false;
                }
                try (PreparedStatement ps = connection.prepareStatement(update)) {
                    ps.setString(1, transactionNo);
                    ps.setLong(2, orderId);
                    if (ps.executeUpdate() != 1) {
                        connection.rollback();
                        return false;
                    }
                }
                connection.commit();
                return true;
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Không thể xác nhận thanh toán VNPay", e);
        }
    }

    @Override
    public void failVnpayPayment(long orderId) {
        String select = "SELECT PaymentMethod, PaymentStatus FROM Orders WITH (UPDLOCK, HOLDLOCK) "
                + "WHERE OrderId = ?";
        String updateOrder = "UPDATE Orders SET PaymentStatus = 'FAILED', Status = 'CANCELLED' "
                + "WHERE OrderId = ? AND PaymentStatus = 'PENDING'";
        String restoreStock = "UPDATE v SET v.Stock = v.Stock + x.Quantity FROM Videos v JOIN "
                + "(SELECT VideoId, SUM(Quantity) Quantity FROM OrderItems WHERE OrderId = ? "
                + "GROUP BY VideoId) x ON x.VideoId = v.VideoId";
        try (Connection connection = db.getConnection()) {
            connection.setAutoCommit(false);
            try {
                boolean pending = false;
                try (PreparedStatement ps = connection.prepareStatement(select)) {
                    ps.setLong(1, orderId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            pending = "VNPAY".equals(rs.getString("PaymentMethod"))
                                    && "PENDING".equals(rs.getString("PaymentStatus"));
                        }
                    }
                }
                if (pending) {
                    try (PreparedStatement ps = connection.prepareStatement(updateOrder)) {
                        ps.setLong(1, orderId);
                        if (ps.executeUpdate() == 1) {
                            try (PreparedStatement restore = connection.prepareStatement(restoreStock)) {
                                restore.setLong(1, orderId);
                                restore.executeUpdate();
                            }
                        }
                    }
                }
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Không thể cập nhật giao dịch VNPay thất bại", e);
        }
    }

    @Override
    public String findPurchasedVideoUrl(long orderId, String username, String videoId) {
        String sql = "SELECT v.VideoUrl FROM Orders o JOIN OrderItems oi ON oi.OrderId = o.OrderId "
                + "JOIN Videos v ON v.VideoId = oi.VideoId WHERE o.OrderId = ? AND o.Username = ? "
                + "AND oi.VideoId = ? AND ((o.PaymentMethod = 'VNPAY' AND o.PaymentStatus = 'PAID') "
                + "OR (o.PaymentMethod = 'COD' AND o.Status = 'DELIVERED'))";
        try (Connection connection = db.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setString(2, username);
            ps.setString(3, videoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                String url = rs.getString("VideoUrl");
                return url == null || url.isBlank() ? null : url.trim();
            }
        } catch (Exception e) {
            throw new IllegalStateException("Không thể mở video đã mua", e);
        }
    }

    @Override
    public List<Order_24133058> findByUsername(String username, OrderStatus_24133058 status) {
        String sql = "SELECT o.OrderId, o.Username, o.ReceiverName, o.Phone, o.Address, o.Note, "
                + "o.PaymentMethod, o.PaymentStatus, o.PaymentTransactionNo, o.PaidAt, o.Status, "
                + "o.TotalAmount, o.CreatedAt, oi.OrderItemId, oi.VideoId, oi.VideoTitle, "
                + "oi.UnitPrice, oi.Quantity FROM Orders o LEFT JOIN OrderItems oi "
                + "ON o.OrderId = oi.OrderId WHERE o.Username = ? "
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
                        order = mapOrder(rs);
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

    private Order_24133058 mapOrder(ResultSet rs) {
        try {
            Timestamp createdAt = rs.getTimestamp("CreatedAt");
            Timestamp paidAt = rs.getTimestamp("PaidAt");
            return Order_24133058.builder()
                    .orderId(rs.getLong("OrderId"))
                    .username(rs.getString("Username"))
                    .receiverName(rs.getString("ReceiverName"))
                    .phone(rs.getString("Phone"))
                    .address(rs.getString("Address"))
                    .note(rs.getString("Note"))
                    .paymentMethod(rs.getString("PaymentMethod"))
                    .paymentStatus(PaymentStatus_24133058.valueOf(rs.getString("PaymentStatus")))
                    .paymentTransactionNo(rs.getString("PaymentTransactionNo"))
                    .paidAt(paidAt == null ? null : paidAt.toLocalDateTime())
                    .status(OrderStatus_24133058.valueOf(rs.getString("Status")))
                    .totalAmount(rs.getBigDecimal("TotalAmount"))
                    .createdAt(createdAt == null ? null : createdAt.toLocalDateTime())
                    .build();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
