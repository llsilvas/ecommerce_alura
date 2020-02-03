package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Properties;

public class FraudeDetectorService {

    public static void main(String[] args) {

        var fraudService = new FraudeDetectorService();

        try(var service = new KafkaService(EmailService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER", fraudService::parse)){
            service.run();
        }


    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("-----------------------------------------");
        System.out.println("Processando pedido, checando fraude");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());

        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("Pedido processado.");
    }

    private static Properties properties() {
        var properties = new Properties();



        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudeDetectorService.class.getSimpleName());


        return properties;
    }
}
