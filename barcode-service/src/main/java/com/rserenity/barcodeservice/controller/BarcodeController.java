package com.rserenity.barcodeservice.controller;


import com.rserenity.barcodeservice.business.dto.BarcodeDto;
import com.rserenity.barcodeservice.business.services.BarcodeGenerator;
import com.rserenity.barcodeservice.business.services.BarcodeServices;
import com.rserenity.barcodeservice.data.enums.BarcodeType;
import com.rserenity.barcodeservice.exception.BarcodeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.rserenity.barcodeservice.constants.MessageConstants.BARCODE_DOESNT_EXIST;

@RestController
@RequestMapping("/api/barcodes")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class BarcodeController {

    private final BarcodeServices barcodeServices;
    private final BarcodeGenerator barcodeGenerator;

    @GetMapping("/getAllBarcodes")
    public List<BarcodeDto> getAllBarcodes() {
        return barcodeServices.getAll();
    }

    @PostMapping(value = "/createBarcode")
    @Transactional
    public ResponseEntity<BarcodeDto> createBarcode(@RequestParam Long productId, @RequestParam String barcodeType){
        BarcodeDto dto = barcodeServices.create(productId, barcodeType);
        System.out.println("created dto : " + dto);
        BarcodeDto secondDto = barcodeServices.create(productId, "ScaleBarcode");
        System.out.println(secondDto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/deleteBarcode")
    public ResponseEntity<Map<String, Boolean>> deleteBarcode(@RequestParam String barcode){
        return ResponseEntity.ok(barcodeServices.delete(barcode));
    }

    @DeleteMapping("/deleteBarcodes")
    public ResponseEntity<Map<String, Boolean>> deleteBarcodes(@RequestParam Long productId){
        return ResponseEntity.ok(barcodeServices.deleteBarcodes(productId));
    }

/*
    @PutMapping("/updateBarcode")
    public ResponseEntity<BarcodeDto> updateBarcode(@RequestParam String barcode, @RequestParam Long productId, @RequestParam String barcodeType){
        return barcodeServices.updateById(barcode, productId, barcodeType);
    }
*/

    @GetMapping("/getBarcode")
    public ResponseEntity<BarcodeDto> getBarcode(@RequestParam String barcode) {
        return ResponseEntity.ok(barcodeServices.getByBarcode(barcode));
    }

    @PostMapping("/updateBarcodes") // clears unappropriate barcodes
    public ResponseEntity<Map<String, Boolean>> updateBarcodes(@RequestParam Long productId){
        Map<String, Boolean> response = barcodeServices.updateBarcodes(productId);
        System.out.println(response);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/checkBarcodeTypeIsOkey")
    public boolean checkBarcodeTypeIsOkey(@RequestParam Long productId, @RequestParam String barcodeType) {
        return barcodeGenerator.checkBarcodeTypeIsOkey(productId, BarcodeType.getBarcodeType(barcodeType));
    }


    @GetMapping("/getSuitableBarcodeTypes")
    public List<String> getSuitableBarcodeTypes(@RequestParam Long productId) {
        return barcodeServices.getSuitableBarcodeTypes(productId);
    }
}
