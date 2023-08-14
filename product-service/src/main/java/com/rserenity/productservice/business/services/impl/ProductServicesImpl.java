package com.rserenity.productservice.business.services.impl;

import com.rserenity.productservice.business.dto.ProductDto;
import com.rserenity.productservice.business.services.ProductServices;
import com.rserenity.productservice.data.entity.ProductEntity;
import com.rserenity.productservice.data.entity.repository.ProductRepository;
import com.rserenity.productservice.data.enums.UnitType;
import com.rserenity.productservice.exception.ProductNotFoundException;
import com.rserenity.productservice.util.BarcodeServiceUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;

import static com.rserenity.productservice.constants.MessageConstants.PRODUCT_DELETED;
import static com.rserenity.productservice.constants.MessageConstants.PRODUCT_DOESNT_EXIST;

@Service
@RequiredArgsConstructor
public class ProductServicesImpl implements ProductServices {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final BarcodeServiceUtil barcodeServiceUtil;
    private final CodeGenerator codeGenerator;


    @Override
    public List<ProductDto> getAll() {
        List<ProductDto> dtos = new ArrayList<>();
        Iterable<ProductEntity> entityList= productRepository.findAll();
        entityList.forEach(productEntity -> dtos.add(entityToDto(productEntity)));
        return dtos;
    }

    @Override
    public ProductDto getProductByName(String name) {
        ProductEntity entity = productRepository.getByName(name);
        if(entity==null){
            throw new ProductNotFoundException(PRODUCT_DOESNT_EXIST);
        }
        return entityToDto(entity);
    }

    @Override
    public ProductDto getProductByCode(String productCode) {
        ProductEntity entity = productRepository.getByCode(productCode);
        if(entity==null){
            throw new ProductNotFoundException(PRODUCT_DOESNT_EXIST);
        }
        return entityToDto(entity);
    }

    @Override
    public ProductDto getProductById(Long productId) {
        return entityToDto(findById(productId));
    }

    @Override
    public boolean isProductNameExists(String name) {
        return productRepository.getByName(name) != null;
    }

    @Override
    public boolean isProductCodeExists(String code) {
        return productRepository.getByCode(code) !=null;
    }

    @Override
    public ProductDto create(ProductDto dto) {
        if(isProductNameExists(dto.getName())){
            return null;
        }

        dto.setCode(codeGenerator.generateProductCode(dto.getCategoryId(), codeGenerator.generateRandomCode(3)));

        ProductEntity entity = dtoToEntity(dto);
        entity = productRepository.save(entity);
        return entityToDto(entity);
    }

    @Override
    public Map<String, Boolean> delete(Long productId) {
        ProductEntity productEntity = dtoToEntity(getProductById(productId));

        productRepository.delete(productEntity);
        Map<String, Boolean> response = new HashMap<>();
        response.put(MessageFormat.format(PRODUCT_DELETED, productEntity.getId()), Boolean.TRUE);
        return response;
    }

    @Override
    public ProductDto updateById(Long productId, ProductDto dto) {
        ProductEntity newEntity = dtoToEntity(dto);
        ProductEntity oldEntity = findById(productId);
        boolean barcodeUpdateRequired=false;

        if(!newEntity.getName().isBlank()){
            isProductNameExists(newEntity.getName());
            oldEntity.setName(newEntity.getName());
        }
        if(newEntity.getUnitType()!=null && !newEntity.getUnitType().equals(oldEntity.getUnitType())){
            oldEntity.setUnitType(newEntity.getUnitType());
            barcodeUpdateRequired=true;
        }
        if(newEntity.getCategoryId() != null && !newEntity.getCategoryId().equals(oldEntity.getCategoryId())){
            oldEntity.setCategoryId(newEntity.getCategoryId());
            //ürün kodu değiştir
            //barkod kontrolü yap -> desteklenmeyen barkod türünde barkodu varsa sil - kategorinin desteklediği farklı barkod türlerinde barkod üretimine izin ver
            oldEntity.setCode(codeGenerator.generateProductCode(oldEntity.getCategoryId(), codeGenerator.generateRandomCode(3)));
            barcodeUpdateRequired = true;
        }
        if(newEntity.getPrice()!=null){
            oldEntity.setPrice(newEntity.getPrice());
        }
        if(!newEntity.getBrand().isBlank()){
            oldEntity.setBrand(newEntity.getBrand());
        }
        newEntity = productRepository.save(oldEntity);
        if(barcodeUpdateRequired) {
            barcodeServiceUtil.updateBarcodes(newEntity.getId());
            barcodeServiceUtil.getSuitableBarcodeTypes(newEntity.getId());
        }
        return entityToDto(newEntity);
    }

    @Override
    public ProductDto entityToDto(ProductEntity entity) {
        ProductDto dto =  modelMapper.map(entity, ProductDto.class);
        dto.setUnitType(entity.getUnitType().getUnitTypeText());
        return dto;
    }

    @Override
    public ProductEntity dtoToEntity(ProductDto dto) {
        ProductEntity entity =
                modelMapper.map(dto, ProductEntity.class);

        entity.setUnitType(UnitType.getUnitType(dto.getUnitType()));
        return entity;
    }


//  *****    private methods  ******

    private ProductEntity findById(Long productId){
        return productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException(PRODUCT_DOESNT_EXIST));
    }
}
