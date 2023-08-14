package com.rserenity.barcodeservice.business.services.impl;

import com.rserenity.barcodeservice.business.dto.BarcodeDto;
import com.rserenity.barcodeservice.business.services.BarcodeServices;
import com.rserenity.barcodeservice.data.entity.repository.BarcodeRepository;
import com.rserenity.barcodeservice.data.enums.BarcodeType;
import com.rserenity.barcodeservice.util.CategoryServiceUtil;
import com.rserenity.barcodeservice.util.ProductServiceUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BarcodeGeneratorImplTest {

    @Mock
    BarcodeRepository barcodeRepository;
    @Mock
    CategoryServiceUtil categoryServiceUtil;

    @Mock
    ProductServiceUtil productServiceUtil;

    @InjectMocks
    BarcodeGeneratorImpl barcodeGenerator;

    @Test
    void generateBarcodeScaleBarcode_ShouldGenerate() {
        when(productServiceUtil.getProductCode(1L)).thenReturn("ME2br");
        when(barcodeRepository.findById("ME2brabc")).thenReturn(Optional.empty());

        BarcodeDto barcode = barcodeGenerator.generateBarcode(1L, BarcodeType.ScaleBarcode,"abc");

        assertEquals(8, barcode.getBarcode().length());
        assertEquals("ME2brabc", barcode.getBarcode());
        assertEquals(1L, barcode.getProductId());
        assertEquals(BarcodeType.ScaleBarcode, barcode.getBarcodeType());
    }
    @Test
    void generateBarcodeCashierBarcode_ShouldGenerate() {
        when(barcodeRepository.findById("abcd")).thenReturn(Optional.empty());

        BarcodeDto barcode = barcodeGenerator.generateBarcode(1L, BarcodeType.CashierBarcode,"abcd");

        assertEquals(4, barcode.getBarcode().length());
        assertEquals("abcd", barcode.getBarcode());
        assertEquals(1L, barcode.getProductId());
        assertEquals(BarcodeType.CashierBarcode, barcode.getBarcodeType());
    }
    @Test
    void generateBarcodeProductBarcode_ShouldGenerate() {
        when(barcodeRepository.findById("abcdetyzf")).thenReturn(Optional.empty());

        BarcodeDto barcode = barcodeGenerator.generateBarcode(1L, BarcodeType.ProductBarcode,"abcdetyzf");

        assertEquals("abcdetyzf", barcode.getBarcode());
        assertEquals(9, barcode.getBarcode().length());
        assertEquals(1L, barcode.getProductId());
        assertEquals(BarcodeType.ProductBarcode, barcode.getBarcodeType());
    }

    @Test
    void generateRandomPart_ShouldGenerate_AccordingToGivenLength() {
        String barcode = barcodeGenerator.generateRandomPart(5);

        assertFalse(barcode.isBlank());
        assertEquals(5, barcode.length());
    }

    // Category-Meyve UnitType-Kilogram (ProductBarcode and CashierBarcode üretilebilmeli)
    @Test
    void checkBarcodeTypeIsOkey_ShouldReturnTrueForBarcodeAndCashier_ShouldReturnFalseScale() {
        when(productServiceUtil.getCategoryId(1L)).thenReturn(2L);
        when(categoryServiceUtil.getCategoryCodeById(2L)).thenReturn("ME");
        when(productServiceUtil.getProductUnitType(1L)).thenReturn("Kilogram");

        boolean isProductBarcodeOkey = barcodeGenerator.checkBarcodeTypeIsOkey(1L, BarcodeType.ProductBarcode);
        boolean isCashierBarcodeOkey = barcodeGenerator.checkBarcodeTypeIsOkey(1L, BarcodeType.CashierBarcode);
        boolean isScaleBarcodeOkey = barcodeGenerator.checkBarcodeTypeIsOkey(1L, BarcodeType.ScaleBarcode);

        assertTrue(isProductBarcodeOkey);
        assertTrue(isCashierBarcodeOkey);
        assertFalse(isScaleBarcodeOkey);
    }
    // Sadece ScaleBarcode
    @Test
    void checkBarcodeTypeIsOkey_ShouldReturnTrueForScaleBarcodes() {
        when(productServiceUtil.getCategoryId(1L)).thenReturn(2L);
        when(productServiceUtil.getCategoryId(2L)).thenReturn(2L);
        when(categoryServiceUtil.getCategoryCodeById(2L)).thenReturn("ET");
        when(productServiceUtil.getProductUnitType(1L)).thenReturn("Kilogram");
        when(productServiceUtil.getProductUnitType(2L)).thenReturn("Adet");

        boolean isProductBarcodeOkeyKilogram = barcodeGenerator.checkBarcodeTypeIsOkey(1L, BarcodeType.ProductBarcode);
        boolean isCashierBarcodeOkeyKilogram = barcodeGenerator.checkBarcodeTypeIsOkey(1L, BarcodeType.CashierBarcode);
        boolean isScaleBarcodeOkeyKilogram = barcodeGenerator.checkBarcodeTypeIsOkey(1L, BarcodeType.ScaleBarcode);
        boolean isProductBarcodeOkeyAdet = barcodeGenerator.checkBarcodeTypeIsOkey(2L, BarcodeType.ProductBarcode);
        boolean isCashierBarcodeOkeyAdet = barcodeGenerator.checkBarcodeTypeIsOkey(2L, BarcodeType.CashierBarcode);
        boolean isScaleBarcodeOkeyAdet = barcodeGenerator.checkBarcodeTypeIsOkey(2L, BarcodeType.ScaleBarcode);

        assertFalse(isProductBarcodeOkeyKilogram);
        assertFalse(isCashierBarcodeOkeyKilogram);
        assertTrue(isScaleBarcodeOkeyKilogram);

        assertFalse(isProductBarcodeOkeyAdet);
        assertFalse(isCashierBarcodeOkeyAdet);
        assertTrue(isScaleBarcodeOkeyAdet);
    }

    // Category-BA UnitType-Adet,Kilogram (ProductBarcode and CashierBarcode üretilebilmeli)
    @Test
    void checkBarcodeTypeIsOkey_TestForBACategory() {
        when(productServiceUtil.getCategoryId(1L)).thenReturn(2L);
        when(productServiceUtil.getCategoryId(2L)).thenReturn(2L);
        when(categoryServiceUtil.getCategoryCodeById(2L)).thenReturn("BA");
        when(productServiceUtil.getProductUnitType(1L)).thenReturn("Adet");
        when(productServiceUtil.getProductUnitType(2L)).thenReturn("Kilogram");

        boolean isProductBarcodeOkeyAdet = barcodeGenerator.checkBarcodeTypeIsOkey(1L, BarcodeType.ProductBarcode);
        boolean isCashierBarcodeOkeyAdet = barcodeGenerator.checkBarcodeTypeIsOkey(1L, BarcodeType.CashierBarcode);
        boolean isScaleBarcodeOkeyAdet = barcodeGenerator.checkBarcodeTypeIsOkey(1L, BarcodeType.ScaleBarcode);

        boolean isProductBarcodeOkeyKilogram = barcodeGenerator.checkBarcodeTypeIsOkey(2L, BarcodeType.ProductBarcode);
        boolean isCashierBarcodeOkeyKilogram = barcodeGenerator.checkBarcodeTypeIsOkey(2L, BarcodeType.CashierBarcode);
        boolean isScaleBarcodeOkeyKilogram = barcodeGenerator.checkBarcodeTypeIsOkey(2L, BarcodeType.ScaleBarcode);

        assertFalse(isProductBarcodeOkeyAdet);
        assertTrue(isCashierBarcodeOkeyAdet);
        assertFalse(isScaleBarcodeOkeyAdet);

        assertTrue(isProductBarcodeOkeyKilogram);
        assertFalse(isCashierBarcodeOkeyKilogram);
        assertTrue(isScaleBarcodeOkeyKilogram);
    }
}