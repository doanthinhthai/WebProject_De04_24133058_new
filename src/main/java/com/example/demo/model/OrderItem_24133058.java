package com.example.demo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem_24133058 implements Serializable {
    private long orderItemId;
    private String videoId;
    private String videoTitle;
    private BigDecimal unitPrice;
    private int quantity;

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
