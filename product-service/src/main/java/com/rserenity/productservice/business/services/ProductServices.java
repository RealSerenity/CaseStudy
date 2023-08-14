package com.rserenity.productservice.business.services;

import com.rserenity.productservice.business.dto.ProductDto;
import com.rserenity.productservice.data.entity.ProductEntity;
import org.aspectj.apache.bcel.generic.LineNumberGen;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface ProductServices{

    List<ProductDto> getAll();

    ProductDto getProductByName(String name);

    ProductDto getProductByCode(String productCode);

    boolean isProductNameExists(String name);
    boolean isProductCodeExists(String code);

    ProductDto create(ProductDto dto);

    Map<String, Boolean> delete(Long code);

    ProductDto getProductById(Long productId);

    ProductDto updateById(Long productId, ProductDto dto);

    ProductDto entityToDto(ProductEntity entity);
    ProductEntity dtoToEntity(ProductDto dto);
}
