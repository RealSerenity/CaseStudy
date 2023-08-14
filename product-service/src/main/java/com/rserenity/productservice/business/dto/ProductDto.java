package com.rserenity.productservice.business.dto;

import com.rserenity.productservice.data.enums.UnitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
@Builder
public class ProductDto implements Serializable {
    Long id;
    String code;
    String name;
    Long categoryId;
    Double price;
    String brand;
    String unitType;
}
