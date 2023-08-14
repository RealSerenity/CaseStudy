package com.rserenity.barcodeservice.data.entity;

import com.rserenity.barcodeservice.data.enums.BarcodeType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Data
@NoArgsConstructor
@Log4j2
@Builder
@Entity
@Table(name = "barcode")
public class BarcodeEntity {

    @Id
    @Column(name = "barcode",nullable = false)
    String barcode;

    @Column(name = "productId",nullable = false)
    Long productId;

    @Column(name = "barcodeType",nullable = false)
    BarcodeType barcodeType;

    public BarcodeEntity(String barcode, Long productId, BarcodeType barcodeType) {
        this.barcode = barcode;
        this.productId = productId;
        this.barcodeType = barcodeType;
    }
}
