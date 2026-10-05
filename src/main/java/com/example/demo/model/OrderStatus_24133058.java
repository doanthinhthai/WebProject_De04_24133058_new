package com.example.demo.model;

public enum OrderStatus_24133058 {
    NEW("Đơn hàng mới", "secondary"),
    CONFIRMED("Đã xác nhận", "primary"),
    PREPARING("Chuẩn bị hàng", "info"),
    SHIPPING("Vận chuyển", "warning"),
    DELIVERING("Giao hàng", "warning"),
    DELIVERED("Đã giao", "success"),
    CANCELLED("Đơn hàng hủy", "danger"),
    RETURNED("Đơn hàng hoàn", "dark");

    private final String label;
    private final String badgeClass;

    OrderStatus_24133058(String label, String badgeClass) {
        this.label = label;
        this.badgeClass = badgeClass;
    }

    public String getLabel() {
        return label;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}