package com.example.demo.model;

public enum PaymentStatus_24133058 {
    UNPAID("Chưa thanh toán", "secondary"),
    PENDING("Chờ thanh toán", "warning"),
    PAID("Đã thanh toán", "success"),
    FAILED("Thanh toán thất bại", "danger"),
    REFUNDED("Đã hoàn tiền", "dark");

    private final String label;
    private final String badgeClass;

    PaymentStatus_24133058(String label, String badgeClass) {
        this.label = label;
        this.badgeClass = badgeClass;
    }

    public String getLabel() { return label; }
    public String getBadgeClass() { return badgeClass; }
}
