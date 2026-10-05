package com.example.demo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class Cart_24133058 implements Serializable {

    private final Map<String, CartItem_24133058> items =
            new LinkedHashMap<>();

    public Collection<CartItem_24133058> getItems() {
        return new ArrayList<>(items.values());
    }

    public CartItem_24133058 getItem(String videoId) {
        return items.get(videoId);
    }

    public void put(CartItem_24133058 item) {
        items.put(
            item.getVideo().getVideoId(),
            item
        );
    }

    public void remove(String videoId) {
        items.remove(videoId);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getTotalQuantity() {
        return items.values()
                .stream()
                .mapToInt(CartItem_24133058::getQuantity)
                .sum();
    }

    public BigDecimal getTotalAmount() {
        return items.values()
                .stream()
                .map(CartItem_24133058::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}