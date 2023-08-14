package com.rserenity.barcodeservice.util.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic create_barcodes(){
        return TopicBuilder.name("create_barcodes")
                .build();
    }

    @Bean
    public NewTopic barcode_test(){
        return TopicBuilder.name("barcode_test")
                .build();
    }
}
