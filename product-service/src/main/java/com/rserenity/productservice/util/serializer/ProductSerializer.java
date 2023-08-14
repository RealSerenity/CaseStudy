package com.rserenity.productservice.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.rserenity.productservice.business.dto.ProductDto;
import com.rserenity.productservice.data.records.BarcodesRequest;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

// extends StdSerializer<BarcodesRequest>
@Component
public class ProductSerializer implements Serializer<BarcodesRequest> {


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, BarcodesRequest data) {
        System.out.println("topic : " + topic);
        System.out.println(data);
        try {
            if (data == null){
                System.out.println("Null received at serializing");
                return null;
            }
            System.out.println("Serializing...");
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing MessageDto to byte[]");
        }
    }

    @Override
    public void close() {
    }




//    @Override
//    public void serialize(BarcodesRequest request, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//            jsonGenerator.writeStartObject();
//            jsonGenerator.writeNumberField("productId", request.productId);
//            jsonGenerator.writeStringField("barcodeType", request.barcodeType);
//            jsonGenerator.writeNumberField("count", request.count);
//            jsonGenerator.writeEndObject();
//    }

}
