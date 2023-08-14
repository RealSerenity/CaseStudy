package com.rserenity.barcodeservice.business.services.impl;

import com.rserenity.barcodeservice.business.dto.BarcodeDto;
import com.rserenity.barcodeservice.business.services.BarcodeGenerator;
import com.rserenity.barcodeservice.business.services.BarcodeServices;
import com.rserenity.barcodeservice.data.entity.repository.BarcodeRepository;
import com.rserenity.barcodeservice.data.enums.BarcodeType;
import com.rserenity.barcodeservice.data.enums.CategoryUnitType;
import com.rserenity.barcodeservice.util.CategoryServiceUtil;
import com.rserenity.barcodeservice.util.ProductServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BarcodeGeneratorImpl implements BarcodeGenerator {


    private final CategoryServiceUtil categoryServiceUtil;
    private final ProductServiceUtil productServiceUtil;
    private final BarcodeRepository barcodeRepository;


    // Check requiredBarcodeType is okey for product before calling method
    @Override
    public BarcodeDto generateBarcode(Long productId, BarcodeType requiredBarcodeType, String randomPart) {
        String barcode = switch (requiredBarcodeType) {
            case ProductBarcode, CashierBarcode -> randomPart;
            case ScaleBarcode -> productServiceUtil.getProductCode(productId) + randomPart;
        };

        if(barcodeRepository.findById(barcode).isEmpty()){
            return BarcodeDto.builder().barcodeType(requiredBarcodeType).barcode(barcode).productId(productId).build();
        }
        return generateBarcode(productId, requiredBarcodeType, randomPart);
    }

    @Override
    public String generateRandomPart(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

//    @Override
//    public BarcodeType tryBarcodeType(Long productId, BarcodeType requiredBarcodeType) {
//        String categoryCode = categoryServiceUtil.getCategoryCodeById(productServiceUtil.getCategoryId(productId));
//        String unitType = productServiceUtil.getProductUnitType(productId);
//        BarcodeType applicableBarcodeType = Arrays.stream(BarcodeType.values()).map(curretBarcodeType -> Arrays.stream(curretBarcodeType.getApplicableCategories()).map(categoryUnitType -> {
//            if (categoryUnitType.category().equals(categoryCode)) {
//                if(categoryUnitType.unitType().equals(unitType))
//                    return curretBarcodeType;
//            }
//            return null;
//        })).toList().get(0).toList().get(0);
//
//        if(applicableBarcodeType==null){
//            return BarcodeType.ProductBarcode;
//        }
//        return applicableBarcodeType;
//    }

    @Override
    public boolean checkBarcodeTypeIsOkey(Long productId, BarcodeType barcodeTypeToCheck) {
        String categoryCode = categoryServiceUtil.getCategoryCodeById(productServiceUtil.getCategoryId(productId));
        String unitType = productServiceUtil.getProductUnitType(productId);
        for (CategoryUnitType categoryUnitType: barcodeTypeToCheck.getApplicableCategories()) {
            if (categoryUnitType.category().equals(categoryCode) && categoryUnitType.unitType().equals(unitType))
            {
                return true;
            }
        }
//        // yukarıdaki for döngüsünde eşitlik bulunmazsa kesinlikle productbarcode olmalı
//        if(barcodeTypeToCheck==BarcodeType.ProductBarcode){
//            return true;
//        }
        // productBarcode da değilse false dön
        return false;
    }
}
