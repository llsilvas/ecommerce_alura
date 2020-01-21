package br.com.alura.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        
        var producer = new KafkaProducer<String, String>(properties());

        var value = "12345,67890, 12345234523";
        var record = new ProducerRecord<String, String>("ECOMMERCE_NEW_ORDER", value, value);

        Callback callback = (data, ex)->{
            if(ex != null){
                ex.printStackTrace();
                return;
            }
            System.out.println("sucesso enviado: " + data.topic() + " offset "+ data.offset());
        };

        producer.send(record, callback).get();

        var email = "Obrigado por sua compra, estamos processando!";
        var emailRecord = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", email, email);

        producer.send(emailRecord, callback).get();

    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return properties;
    }
}
