package br.com.alura.ecommerce;

import br.com.alura.ecommerce.model.Order;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try(var orderDispatcher = new KafkaDispatcher<Order>()){
            try(var emailDispacher = new KafkaDispatcher<String>()) {
                for (int i = 0; i < 10; i++) {

                    var key = UUID.randomUUID().toString();
                    var userId = UUID.randomUUID().toString();
                    var orderId = UUID.randomUUID().toString();

                    var amount = new BigDecimal(Math.random() * 5000 + 1);

                    var order = new Order(userId, orderId, amount);

                    var value = key + ",67523,1234";
                    orderDispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);

                    var email = "Thank you for your order! We are processing your order!";
                    emailDispacher.send("ECOMMERCE_SEND_EMAIL", userId, email);
                }
            }
        }
    }

}
