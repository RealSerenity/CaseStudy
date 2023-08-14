package com.rserenity.productservice.controller;

import com.rserenity.productservice.business.dto.ProductDto;
import com.rserenity.productservice.business.services.ProductServices;
import com.rserenity.productservice.data.records.BarcodesRequest;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:8070")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServices productServices;
    private final KafkaTemplate<String, Object> kafkaBarcodeTemplate;

    @GetMapping("/getAllProducts")
    public List<ProductDto> getAllProducts() {
        return productServices.getAll();
    }

    @PostMapping(value = "/createProduct", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto dto){
        ProductDto createdDto = productServices.create(dto);
        return ResponseEntity.ok(createdDto);
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity<Map<String, Boolean>> deleteProduct(@RequestParam Long productId){
        return ResponseEntity.ok(productServices.delete(productId));
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<ProductDto> updateProduct(@RequestParam Long productId,@RequestBody ProductDto dto){
        return ResponseEntity.ok(productServices.updateById(productId, dto));
    }

    @GetMapping("/getProductById")
    public ResponseEntity<ProductDto> getProductById(@RequestParam Long productId) {
        return ResponseEntity.ok(productServices.getProductById(productId));
    }

    @GetMapping("/getProductUnitType")
    public String getProductUnitType(@RequestParam(value = "productId") Long productId) {
        return productServices.getProductById(productId).getUnitType();
    }

    @GetMapping("/getCategoryId")
    public Long getCategoryId(@RequestParam(value = "productId") Long productId) {
        return productServices.getProductById(productId).getCategoryId();
    }

    @GetMapping("/getProductCode")
    public String getProductCode(@RequestParam(value = "productId") Long productId) {
        return productServices.getProductById(productId).getCode();
    }

    @PostMapping(value = "/createBarcodesForProduct", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String createBarcodesForProduct(@RequestBody BarcodesRequest request){
        ProducerRecord<String, Object> message = new ProducerRecord<>("create_barcodes", request);
//        Message message = MessageBuilder.withPayload(request).build();
        CompletableFuture<SendResult<String, Object>> completableFuture = kafkaBarcodeTemplate.send(message);
        System.out.println(completableFuture);
        return request.count + " adet barcode oluşturma isteği gönderildi";
    }


    @PostMapping(value = "/barcodeTest")
    public String barcodeTest(@RequestParam(value = "message")String message){

        CompletableFuture<SendResult<String, Object>> completableFuture = kafkaBarcodeTemplate.send("barcode_test", message);
        return completableFuture.toString();
    }

}
