package com.rserenity.barcodeservice.util;


import com.google.gson.Gson;
import com.rserenity.barcodeservice.business.dto.BarcodeDto;
import com.rserenity.barcodeservice.business.services.BarcodeServices;
import com.rserenity.barcodeservice.data.records.BarcodesRequest;
import com.rserenity.barcodeservice.util.deserializer.ProductDeserializer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Stack;

@Component
@RequiredArgsConstructor
public class KafkaListeners
{
    private final BarcodeServices barcodeServices;

    @KafkaListener(topics = "create_barcodes",
            groupId = "barcode_listener_1")
    void listener(String data){
        Gson gson = new Gson();
        BarcodesRequest request = gson.fromJson(data, BarcodesRequest.class);
        System.out.println(request);
        for (int i =0; i<request.count;i++){
            System.out.println(barcodeServices.create(request.productId, request.barcodeType));
        }

    }

    @KafkaListener(topics = "barcode_test",
            groupId = "barcode_listener_2")
    void listener2(String data){
        System.out.println(data);
    }

}

//            try {
//                Thread.sleep(400);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }