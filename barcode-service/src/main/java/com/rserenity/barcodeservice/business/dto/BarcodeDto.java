package com.rserenity.barcodeservice.business.dto;

import com.rserenity.barcodeservice.data.enums.BarcodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
@Builder
public class BarcodeDto {
    String barcode;
    Long productId;
    BarcodeType barcodeType;

}
