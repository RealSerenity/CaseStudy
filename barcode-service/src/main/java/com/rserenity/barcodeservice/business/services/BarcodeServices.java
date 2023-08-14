package com.rserenity.barcodeservice.business.services;

import com.rserenity.barcodeservice.business.dto.BarcodeDto;
import com.rserenity.barcodeservice.data.entity.BarcodeEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BarcodeServices {
    public List<BarcodeDto> getAll();

    public BarcodeDto create(Long productId, String barcodeType);

    public Map<String, Boolean> delete(String barcode);

    public Map<String, Boolean> deleteBarcodes(Long productId);

    public Map<String, Boolean> updateBarcodes(Long productId);

    public boolean isBarcodeAvailable(String barcode);

    public List<String> getSuitableBarcodeTypes(Long productId);

    public BarcodeDto getByBarcode(String barcode);
    public void transactionTestThrowException();

    public BarcodeDto entityToDto(BarcodeEntity entity);
    public BarcodeEntity dtoToEntity(BarcodeDto dto);
}
