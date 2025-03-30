package org.example.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@ConditionalOnProperty(name = "spring.kafka.consumer.enabled", havingValue = "true")
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public JsonDeserializer<Order> orderJsonDeserializer() {
        JsonDeserializer<Order> deserializer = new JsonDeserializer<>(Order.class);
        deserializer.addTrustedPackages("*");
        return deserializer;
    }

    @Bean
    public Map<String, Object> consumerConfig() {

        var properties = new HashMap<String, Object>();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, orderJsonDeserializer());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        return properties;
    }

    @Bean
    public ConsumerFactory<String, Order> consumerFactory(JsonDeserializer<Order> OrderJsonDeserialize) {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(), OrderJsonDeserialize);
    }

    @Bean
    public KafkaListenerContainerFactory<?> kafkaListenerContainerFactory(ConsumerFactory<String, Order> consumerFactor) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Order>();
        factory.setConsumerFactory(consumerFactor);
        return factory;
    }
}
