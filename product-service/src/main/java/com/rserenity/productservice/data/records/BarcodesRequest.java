package com.rserenity.productservice.data.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
public class BarcodesRequest implements Serializable {
    public Long productId;
    public String barcodeType;
    public int count;
}
