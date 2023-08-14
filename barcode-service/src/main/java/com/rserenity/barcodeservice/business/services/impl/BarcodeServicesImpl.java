package com.rserenity.barcodeservice.business.services.impl;

import com.rserenity.barcodeservice.business.dto.BarcodeDto;
import com.rserenity.barcodeservice.business.services.BarcodeGenerator;
import com.rserenity.barcodeservice.business.services.BarcodeServices;
import com.rserenity.barcodeservice.constants.MessageConstants;
import com.rserenity.barcodeservice.data.entity.BarcodeEntity;
import com.rserenity.barcodeservice.data.entity.repository.BarcodeRepository;
import com.rserenity.barcodeservice.data.enums.BarcodeType;
import com.rserenity.barcodeservice.exception.BarcodeException;
import com.rserenity.barcodeservice.exception.BarcodeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;

import static com.rserenity.barcodeservice.constants.MessageConstants.*;

@Service
@RequiredArgsConstructor
public class BarcodeServicesImpl implements BarcodeServices {

    private final BarcodeRepository barcodeRepository;
    private final ModelMapper modelMapper;
    private final BarcodeGenerator barcodeGenerator;

    @Override
    public List<BarcodeDto> getAll() {
        List<BarcodeDto> dtos = new ArrayList<>();
        Iterable<BarcodeEntity> entityList= barcodeRepository.findAll();
        entityList.forEach(productEntity -> dtos.add(entityToDto(productEntity)));
        return dtos;
    }

    // Todo :: önceden responseentity ile bad request döndürüyordum şimdi nasıl bir uyarı döndürebilirim
    @Override
    //@Transactional(propagation = Propagation.NOT_SUPPORTED) // calls works independently
    // NEVER  throws exception if there is an active transaction
    // MANDATORY if there is an active transaction, then it will be used. If there isn't an active transaction throws an exception
    @Transactional
    public BarcodeDto create(Long productId, String barcodeType) {
        if(!barcodeGenerator.checkBarcodeTypeIsOkey(productId,BarcodeType.getBarcodeType(barcodeType))){
            throw new BarcodeException(MessageFormat.format(INAPPROPRIATE_BARCODETYPE, productId));
        }
        BarcodeType convertedBarcodeType = BarcodeType.getBarcodeType(barcodeType);
        BarcodeDto dto = barcodeGenerator.generateBarcode(productId, convertedBarcodeType,
                barcodeGenerator.generateRandomPart(convertedBarcodeType.getBarcodeRandomPartLength()));
        BarcodeEntity entity = dtoToEntity(dto);
        entity = barcodeRepository.save(entity);
        return entityToDto(entity);
    }

    @Override
    public Map<String, Boolean> delete(String barcode) {
        BarcodeEntity categoryEntity = findBarcode(barcode);

        barcodeRepository.delete(categoryEntity);
        Map<String, Boolean> response = new HashMap<>();
        response.put(MessageFormat.format(BARCODE_DELETED, categoryEntity.getBarcode()), Boolean.TRUE);
        return response;
    }

    @Override
    public Map<String, Boolean> deleteBarcodes(Long productId) {
        List<BarcodeEntity> entities = barcodeRepository.getBarcodeEntitiesByProductId(productId);
        barcodeRepository.deleteAll(entities);

        Map<String, Boolean> response = new HashMap<>();
        response.put(MessageFormat.format(BARCODES_DELETED, entities.size()), Boolean.TRUE);
        return response;
    }

    //TODO: tekrar gözden geçir
    @Override
    public Map<String, Boolean> updateBarcodes(Long productId) {
        List<BarcodeEntity> entities = barcodeRepository.getBarcodeEntitiesByProductId(productId);
        int count = 0;
        for (BarcodeEntity entity: entities) {
            if(!barcodeGenerator.checkBarcodeTypeIsOkey(productId,entity.getBarcodeType())){
                delete(entity.getBarcode());
                count++;
            }
        }
        Map<String, Boolean> response = new HashMap<>();
        response.put(MessageFormat.format(BARCODES_DELETED, count), Boolean.TRUE);
        return response;
    }

    @Override
    public boolean isBarcodeAvailable(String barcode) {
        return barcodeRepository.findById(barcode).orElse(null) == null;
    }

    @Override
    public List<String> getSuitableBarcodeTypes(Long productId) {
        List<String> barcodeTypes = new ArrayList<>();
        for (BarcodeType type : BarcodeType.values()) {
            if(barcodeGenerator.checkBarcodeTypeIsOkey(productId, type)){
                barcodeTypes.add(type.getBarcodeTypeText());
            }
        }
        return barcodeTypes;
    }


/*

 @Override
 public ResponseEntity<BarcodeDto> updateById(String barcode, Long productId, String barcodeType) {
     if(!barcodeGenerator.checkBarcodeTypeIsOkey(productId, BarcodeType.getBarcodeType(barcodeType))){
         return ResponseEntity.badRequest().header("BarcodeTypeError","Uygun olmayan barkod tipi").
                 body(BarcodeDto.builder().productId(productId).barcodeType(BarcodeType.getBarcodeType(barcodeType)).build());
     }
     BarcodeEntity entity = findBarcode(barcode);
     entity.setBarcodeType(BarcodeType.getBarcodeType(barcodeType));
     entity.setProductId(productId);
     entity = barcodeRepository.save(entity);
     return ResponseEntity.ok(entityToDto(entity));
 }
*/

    @Override
    public BarcodeDto getByBarcode(String barcode) {
        return entityToDto(findBarcode(barcode));
    }

    @Override
    @Transactional
    public void transactionTestThrowException() {
        System.out.println("ExceptionThrown");
        throw new BarcodeNotFoundException(BARCODE_DOESNT_EXIST);
    }



    @Override
    public BarcodeDto entityToDto(BarcodeEntity entity) {
       BarcodeDto barcodeDto = modelMapper.map(entity, BarcodeDto.class);
       barcodeDto.setBarcode(entity.getBarcode());
       return barcodeDto;
    }

    @Override
    public BarcodeEntity dtoToEntity(BarcodeDto dto) {
        BarcodeEntity entity =  modelMapper.map(dto, BarcodeEntity.class);
        entity.setBarcode(dto.getBarcode());
        return entity;
  }
    private BarcodeEntity findBarcode(String barcode){
        return barcodeRepository.findById(barcode).orElseThrow(
                () -> new BarcodeNotFoundException(BARCODE_DOESNT_EXIST));
    }

}
