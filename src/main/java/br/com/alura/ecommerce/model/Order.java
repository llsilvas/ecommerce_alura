package br.com.alura.ecommerce.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Order implements Serializable {

    private final String userId, orderId;

    private final BigDecimal amount;

    public Order(String userId, String orderId, BigDecimal amount) {
        this.userId = userId;
        this.orderId = orderId;
        this.amount = amount;
    }
}
