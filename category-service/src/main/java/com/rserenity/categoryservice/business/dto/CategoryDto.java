package com.rserenity.categoryservice.business.dto;

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
public class CategoryDto implements Serializable {
    Long id;
    String categoryCode;
    String categoryName;
}
