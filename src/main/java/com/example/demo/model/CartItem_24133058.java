package com.example.demo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem_24133058 implements Serializable {

    private Video_24133058 video;
    private int quantity;

    public BigDecimal getSubtotal() {
        if (video == null || video.getPrice() == null) {
            return BigDecimal.ZERO;
        }

        return video.getPrice()
                .multiply(BigDecimal.valueOf(quantity));
    }
}