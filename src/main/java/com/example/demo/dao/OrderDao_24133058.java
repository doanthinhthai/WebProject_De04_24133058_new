package com.example.demo.dao;

import java.math.BigDecimal;
import java.util.List;
import com.example.demo.model.*;

public interface OrderDao_24133058 {

    long createCodOrder(
            String username,
            String receiverName,
            String phone,
            String address,
            String note,
            Cart_24133058 cart);

    long createVnpayOrder(
            String username,
            String receiverName,
            String phone,
            String address,
            String note,
            Cart_24133058 cart);

    Order_24133058 findById(long orderId);

    boolean completeVnpayPayment(long orderId, BigDecimal amount, String transactionNo);

    void failVnpayPayment(long orderId);

    String findPurchasedVideoUrl(long orderId, String username, String videoId);

    List<Order_24133058> findByUsername(
            String username,
            OrderStatus_24133058 status);
}
