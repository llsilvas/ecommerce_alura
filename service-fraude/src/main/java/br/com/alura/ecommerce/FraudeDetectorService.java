package br.com.alura.ecommerce;

import br.com.alura.ecommerce.model.Order;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class FraudeDetectorService {

    public static void main(String[] args) {
        var fraudService = new FraudeDetectorService();
        try(var service = new KafkaService<>(FraudeDetectorService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                fraudService::parse,
                Order.class,
                Map.of())){
            service.run();
        }
    }

    private final KafkaDispatcher<Order> orderKafkaDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, checking for fraud");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignoring
            e.printStackTrace();
        }

        var order = record.value();
        if(isFraud(order)){
            System.out.println("Order is a fraud!!");
            orderKafkaDispatcher.send("ECOMMERCE_ORDER_REJECTED",order.getEmail(), order);
        }else{
            System.out.println("Approved: " + order);
            orderKafkaDispatcher.send("ECOMMERCE_ORDER_APPROVED", order.getEmail(), order);
        }

    }

    private boolean isFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;
    }

}
