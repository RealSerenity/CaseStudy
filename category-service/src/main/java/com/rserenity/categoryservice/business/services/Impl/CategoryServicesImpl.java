package com.rserenity.categoryservice.business.services.Impl;

import com.rserenity.categoryservice.business.dto.CategoryDto;
import com.rserenity.categoryservice.business.services.CategoryServices;
import com.rserenity.categoryservice.data.entity.CategoryEntity;
import com.rserenity.categoryservice.data.entity.repository.CategoryRepository;
import com.rserenity.categoryservice.data.enums.CategoryType;
import com.rserenity.categoryservice.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

import static com.rserenity.categoryservice.contants.MessageConstants.CATEGORY_DELETED;
import static com.rserenity.categoryservice.contants.MessageConstants.CATEGORY_DOESNT_EXIST;

@Service
@RequiredArgsConstructor
 public class CategoryServicesImpl implements CategoryServices {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CategoryDto> getAll() {
        List<CategoryDto> dtos = new ArrayList<>();
        Iterable<CategoryEntity> entityList= categoryRepository.findAll();
        entityList.forEach(categoryEntity -> dtos.add(entityToDto(categoryEntity)));
        return dtos;
    }

    @Override
    public CategoryDto getCategoryByName(String name) {
        boolean exists = isCategoryExists(name);
        if(!exists){
            throw new CategoryNotFoundException(CATEGORY_DOESNT_EXIST);
        }
        return entityToDto(categoryRepository.getByCategoryName(name));
    }

    @Override
    public boolean isCategoryExists(String name) {
        return categoryRepository.getByCategoryName(name) != null;
    }


    @Override
    public CategoryDto create(CategoryDto dto) {
        if (isCategoryExists(dto.getCategoryName())){
            return null;
        }
        CategoryEntity entity = dtoToEntity(dto);
        entity = categoryRepository.save(entity);
        return entityToDto(entity);
    }

    @Override
    public Map<String, Boolean> delete(Long id) {
        CategoryEntity categoryEntity = findById(id);

        categoryRepository.delete(categoryEntity);
        Map<String, Boolean> response = new HashMap<>();
        response.put(MessageFormat.format(CATEGORY_DELETED, categoryEntity.getId()), Boolean.TRUE);
        return response;
    }

    @Override
    public CategoryDto updateById(Long id, CategoryDto dto) {
        CategoryEntity newEntity = dtoToEntity(dto);
        CategoryEntity oldEntity = findById(id);

        //&& !newEntity.getCategoryType().getCategoryName().equals(oldEntity.getCategoryType().getCategoryName())
        if(!newEntity.getCategoryType().getCategoryName().isBlank())
        {
            oldEntity.setCategoryType(newEntity.getCategoryType());
        }
        newEntity = categoryRepository.save(oldEntity);
        return entityToDto(newEntity);
    }

    @Override
    public CategoryDto getById(Long id) {
        return entityToDto(findById(id));
    }

    @Override
    public CategoryDto entityToDto(CategoryEntity entity) {
        return modelMapper.map(entity, CategoryDto.class);
    }

    @Override
    public CategoryEntity dtoToEntity(CategoryDto dto) {
        CategoryEntity entity = modelMapper.map(dto, CategoryEntity.class);
        entity.setCategoryType(CategoryType.getCategoryType(dto.getCategoryCode()));
        return entity;
    }

    private CategoryEntity findById(Long id){
        return categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException(CATEGORY_DOESNT_EXIST));
    }
}
