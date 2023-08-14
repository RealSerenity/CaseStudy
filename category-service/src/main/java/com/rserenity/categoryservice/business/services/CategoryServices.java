package com.rserenity.categoryservice.business.services;

import com.rserenity.categoryservice.business.dto.CategoryDto;
import com.rserenity.categoryservice.data.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

public interface CategoryServices {

    List<CategoryDto> getAll();

    CategoryDto getCategoryByName(String name);

    boolean isCategoryExists(String name);

    CategoryDto create(CategoryDto dto);

    Map<String, Boolean> delete(Long id);

    CategoryDto updateById(Long id, CategoryDto dto);

    CategoryDto getById(Long id);


    CategoryDto entityToDto(CategoryEntity entity);
    CategoryEntity dtoToEntity(CategoryDto dto);
}
