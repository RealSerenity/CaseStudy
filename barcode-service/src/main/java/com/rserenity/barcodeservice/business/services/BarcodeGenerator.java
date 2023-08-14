package com.rserenity.barcodeservice.business.services;

import com.rserenity.barcodeservice.business.dto.BarcodeDto;
import com.rserenity.barcodeservice.data.enums.BarcodeType;

public interface BarcodeGenerator {
    BarcodeDto generateBarcode(Long productId, BarcodeType requiredBarcodeType, String randomPart);

    String generateRandomPart(int length);

    boolean checkBarcodeTypeIsOkey(Long productId, BarcodeType barcodeType);
}
