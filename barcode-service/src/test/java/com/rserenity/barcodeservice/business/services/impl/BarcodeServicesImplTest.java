package com.rserenity.barcodeservice.business.services.impl;

import com.rserenity.barcodeservice.business.dto.BarcodeDto;
import com.rserenity.barcodeservice.business.services.BarcodeGenerator;
import com.rserenity.barcodeservice.data.entity.BarcodeEntity;
import com.rserenity.barcodeservice.data.entity.repository.BarcodeRepository;
import com.rserenity.barcodeservice.data.enums.BarcodeType;
import com.rserenity.barcodeservice.exception.BarcodeNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.text.MessageFormat;
import java.util.*;

import static com.rserenity.barcodeservice.constants.MessageConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BarcodeServicesImplTest {

    @Spy
    ModelMapper modelMapper;
    @Mock
    BarcodeRepository barcodeRepository;
    @Mock
    BarcodeGenerator barcodeGenerator;
    @InjectMocks
    BarcodeServicesImpl barcodeServices;

    @Test
    void getAll_ShouldReturnAllEntities_WhenEntitiesExits() {
        List<BarcodeEntity> entityList = List.of(new BarcodeEntity("abc1",1L, BarcodeType.CashierBarcode),
                new BarcodeEntity("abc2",1L,BarcodeType.CashierBarcode));

        when(barcodeRepository.findAll()).thenReturn(entityList);

        List<BarcodeDto> dtos = barcodeServices.getAll();

        assertEquals(2, dtos.size());
        assertEquals(dtos.get(0), BarcodeDto.builder().barcode("abc1").productId(1L).barcodeType(BarcodeType.CashierBarcode).build());
        assertEquals(dtos.get(1), BarcodeDto.builder().barcode("abc2").productId(1L).barcodeType(BarcodeType.CashierBarcode).build());
    }

    @Test
    void getAll_ShouldReturn0Entities_WhenDatabaseHas0Entity() {
        when(barcodeRepository.findAll()).thenReturn(List.of());

        assertEquals(0, barcodeServices.getAll().size());
    }

    @Test
    void create_ShouldCreate_WhenBarcodeAvailable() {
        when(barcodeRepository.save(new BarcodeEntity("abc1",1L,BarcodeType.CashierBarcode)))
                .thenReturn(new BarcodeEntity("abc1",1L,BarcodeType.CashierBarcode));
        when(barcodeGenerator.checkBarcodeTypeIsOkey(1L,BarcodeType.CashierBarcode)).thenReturn(true);
        when(barcodeGenerator.generateBarcode(1L, BarcodeType.CashierBarcode,"abc1")).thenReturn(BarcodeDto.builder().barcode("abc1").barcodeType(BarcodeType.CashierBarcode).productId(1L).build());
        when(barcodeGenerator.generateRandomPart(4)).thenReturn("abc1");

        BarcodeDto barcodeDtoResponseEntity = barcodeServices.create(1L, "CashierBarcode");

        assertEquals(BarcodeDto.builder().barcode("abc1").productId(1L).barcodeType(BarcodeType.CashierBarcode).build(), barcodeDtoResponseEntity);
    }

    @Test
    void create_ShouldReturnNull_WhenBarcodeTypeIsInappropriate() {
        when(barcodeGenerator.checkBarcodeTypeIsOkey(1L,BarcodeType.CashierBarcode)).thenReturn(false);

        BarcodeDto barcodeDtoResponseEntity = barcodeServices.create(1L, "CashierBarcode");

        assertNull(barcodeDtoResponseEntity);
    }

    @Test
    void delete_ShouldDelete_WhenEntityExists() {
        Map<String, Boolean> expectedResponse = new HashMap<>();
        expectedResponse.put(MessageFormat.format(BARCODE_DELETED, "abcd"), Boolean.TRUE);

        when(barcodeRepository.findById("abcd")).thenReturn(Optional.of(new BarcodeEntity("abcd",1L,BarcodeType.CashierBarcode)));

        Map<String, Boolean> response = barcodeServices.delete("abcd");

        assertEquals(expectedResponse, response);
    }

    @Test
    void delete_ShouldThrowException_WhenEntityDoesntExist() {
        when(barcodeRepository.findById("abcd")).thenReturn(Optional.empty());

        assertThrows(BarcodeNotFoundException.class, () -> barcodeServices.delete("abcd"), BARCODE_DOESNT_EXIST);
    }

    @Test
    void deleteBarcodes_ShouldDeleteAll_WhenEntitiesExist() {
        Map<String, Boolean> expectedResponse = new HashMap<>();
        expectedResponse.put(MessageFormat.format(BARCODES_DELETED, 3), Boolean.TRUE);

        List<BarcodeEntity> entityList = List.of(new BarcodeEntity("abc1",1L, BarcodeType.CashierBarcode),
                new BarcodeEntity("abc2",1L,BarcodeType.CashierBarcode),
                new BarcodeEntity("abc3",1L, BarcodeType.ProductBarcode));
        when(barcodeRepository.getBarcodeEntitiesByProductId(1L)).thenReturn(entityList);

        Map<String, Boolean> response = barcodeServices.deleteBarcodes(1L);

        assertEquals(expectedResponse, response);
    }

    @Test
    void updateBarcodes_ShouldDelete_WhenFindInappropriateBarcodeType() {
        Map<String, Boolean> expectedResponse = new HashMap<>();
        expectedResponse.put(MessageFormat.format(BARCODES_DELETED, 3), Boolean.TRUE);

        List<BarcodeEntity> entityList = List.of(new BarcodeEntity("abc1",1L, BarcodeType.CashierBarcode),
                new BarcodeEntity("abc2",1L,BarcodeType.CashierBarcode),
                new BarcodeEntity("abc3",1L, BarcodeType.ProductBarcode),
                new BarcodeEntity("abc4",1L, BarcodeType.ScaleBarcode));

        when(barcodeRepository.findById("abc1")).thenReturn(Optional.ofNullable(entityList.get(0)));
        when(barcodeRepository.findById("abc2")).thenReturn(Optional.ofNullable(entityList.get(1)));
        when(barcodeRepository.findById("abc4")).thenReturn(Optional.ofNullable(entityList.get(3)));

        when(barcodeRepository.getBarcodeEntitiesByProductId(1L)).thenReturn(entityList);

        when(barcodeGenerator.checkBarcodeTypeIsOkey(1L, BarcodeType.CashierBarcode)).thenReturn(false);
        when(barcodeGenerator.checkBarcodeTypeIsOkey(1L, BarcodeType.ProductBarcode)).thenReturn(true);
        when(barcodeGenerator.checkBarcodeTypeIsOkey(1L, BarcodeType.ScaleBarcode)).thenReturn(false);

        Map<String,Boolean> response = barcodeServices.updateBarcodes(1L);



        assertEquals(expectedResponse, response);
    }


    @Test
    void isBarcode_ShouldReturnFalse_WhenBarcodeIsntAvailable() {
        when(barcodeRepository.findById("abcd")).thenReturn(Optional.of(new BarcodeEntity("abcd", 1L, BarcodeType.CashierBarcode)));

        boolean available = barcodeServices.isBarcodeAvailable("abcd");

        assertFalse(available);
    }

    @Test
    void isBarcode_ShouldReturnTrue_WhenBarcodeIsAvailable() {
        when(barcodeRepository.findById("abcd")).thenReturn(Optional.empty());

        boolean available = barcodeServices.isBarcodeAvailable("abcd");

        assertTrue(available);
    }

    @Test
    void getSuitableBarcodeTypes_ShouldReturn_WhenFindAnyAppropriateBarcodeType() {
        List<String> suitableBarcodeTypes;

        when(barcodeGenerator.checkBarcodeTypeIsOkey(1L,BarcodeType.ScaleBarcode)).thenReturn(true);
        when(barcodeGenerator.checkBarcodeTypeIsOkey(1L,BarcodeType.CashierBarcode)).thenReturn(false);
        when(barcodeGenerator.checkBarcodeTypeIsOkey(1L,BarcodeType.ProductBarcode)).thenReturn(true);

        suitableBarcodeTypes = barcodeServices.getSuitableBarcodeTypes(1L);
        assertEquals(List.of("ProductBarcode","ScaleBarcode"), suitableBarcodeTypes);
    }

    @Test
    void getByBarcode_ShouldReturn_WhenExists() {
        BarcodeDto expectedDto = BarcodeDto.builder().barcode("abcd").barcodeType(BarcodeType.CashierBarcode).productId(1L).build();

        when(barcodeRepository.findById("abcd")).thenReturn(Optional.of(new BarcodeEntity("abcd", 1L, BarcodeType.CashierBarcode)));

        BarcodeDto barcodeDto = barcodeServices.getByBarcode("abcd");

        assertEquals(expectedDto, barcodeDto);
    }

    @Test
    void getByBarcode_ShouldThrowException_WhenDoesntExist() {
        when(barcodeRepository.findById("abcd")).thenReturn(Optional.empty());

        assertThrows(BarcodeNotFoundException.class,() -> barcodeServices.getByBarcode("abcd"), BARCODE_DOESNT_EXIST);
    }


    @Test
    void entityToDto() {
        BarcodeDto expectedDto = BarcodeDto.builder().barcode("abcd").barcodeType(BarcodeType.CashierBarcode).productId(1L).build();

        BarcodeEntity entity = new BarcodeEntity("abcd", 1L, BarcodeType.CashierBarcode);
        BarcodeDto convertedDto = barcodeServices.entityToDto(entity);

        assertEquals(expectedDto, convertedDto);
    }

    @Test
    void dtoToEntity() {
        BarcodeEntity expectedEntity = new BarcodeEntity("abcd", 1L, BarcodeType.CashierBarcode);

        BarcodeDto dto = BarcodeDto.builder().barcode("abcd").barcodeType(BarcodeType.CashierBarcode).productId(1L).build();
        BarcodeEntity convertedEntity = barcodeServices.dtoToEntity(dto);

        assertEquals(expectedEntity, convertedEntity);
    }
/*
     TODO:: ...
    @Test
    void updateById_ShouldUpdate_WhenBarcodeTypeIsOkey() {
        BarcodeEntity entity = new BarcodeEntity("abcd", 1L, BarcodeType.CashierBarcode);
        BarcodeDto expectedDto = BarcodeDto.builder().barcode("abcd").productId(2L).barcodeType(BarcodeType.CashierBarcode).build();

        when(barcodeRepository.findById("abcd")).thenReturn(Optional.of(entity));
        when(barcodeGenerator.checkBarcodeTypeIsOkey(1L, BarcodeType.CashierBarcode)).thenReturn(true);
        when(barcodeRepository.save(entity)).thenReturn(new BarcodeEntity("abcd",2L,BarcodeType.CashierBarcode));

        ResponseEntity<BarcodeDto> updatedDto = barcodeServices.updateById("abcd", 1L, "CashierBarcode");
        assertEquals(ResponseEntity.ok(expectedDto), updatedDto);
    }
*/
}