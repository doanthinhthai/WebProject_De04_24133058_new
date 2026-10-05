package com.example.demo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order_24133058 implements Serializable {
    private long orderId;
    private String username;
    private String receiverName;
    private String phone;
    private String address;
    private String note;
    private String paymentMethod;
    private PaymentStatus_24133058 paymentStatus;
    private String paymentTransactionNo;
    private LocalDateTime paidAt;
    private OrderStatus_24133058 status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    @Builder.Default
    private List<OrderItem_24133058> items = new ArrayList<>();

    public boolean isVideoAccessGranted() {
        return paymentStatus == PaymentStatus_24133058.PAID
                || ("COD".equals(paymentMethod) && status == OrderStatus_24133058.DELIVERED);
    }
}
