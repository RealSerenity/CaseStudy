package com.rserenity.barcodeservice.data.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.core.serializer.Serializer;

import java.io.Serializable;
public class BarcodesRequest implements Serializable {
    public Long productId;
    public String barcodeType;
    public int count;

    public BarcodesRequest(Long productId, String barcodeType, int count) {
        this.productId = productId;
        this.barcodeType = barcodeType;
        this.count = count;
    }

    @Override
    public String toString() {
        return "BarcodesRequest{" +
                "productId=" + productId +
                ", barcodeType='" + barcodeType + '\'' +
                ", count=" + count +
                '}';
    }
}
