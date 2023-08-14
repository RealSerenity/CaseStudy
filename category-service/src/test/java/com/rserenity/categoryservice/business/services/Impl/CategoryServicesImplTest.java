package com.rserenity.categoryservice.business.services.Impl;

import com.rserenity.categoryservice.business.dto.CategoryDto;
import com.rserenity.categoryservice.data.entity.CategoryEntity;
import com.rserenity.categoryservice.data.entity.repository.CategoryRepository;
import com.rserenity.categoryservice.data.enums.CategoryType;
import com.rserenity.categoryservice.exception.CategoryNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.text.MessageFormat;
import java.util.*;

import static com.rserenity.categoryservice.contants.MessageConstants.CATEGORY_DELETED;
import static com.rserenity.categoryservice.contants.MessageConstants.CATEGORY_DOESNT_EXIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServicesImplTest{

    @Mock
    private CategoryRepository categoryRepository;
    @Spy
    private ModelMapper modelMapper;
    @InjectMocks
    private CategoryServicesImpl categoryServices;

    @Test
    void getAll_ShouldReturnAllEntities_WhenEntitiesExits() {
        List <CategoryEntity> entityList = List.of(new CategoryEntity(1L, "Balik", CategoryType.Balik),
                new CategoryEntity(2L, "Meyve", CategoryType.Meyve));

        when(categoryRepository.findAll()).thenReturn(entityList);

        List<CategoryDto> dtos = categoryServices.getAll();

        assertEquals(2,dtos.size());
        assertEquals(dtos.get(0), CategoryDto.builder().categoryName("Balik").categoryCode("BA").id(1L).build());
        assertEquals(dtos.get(1), CategoryDto.builder().categoryName("Meyve").categoryCode("ME").id(2L).build());
    }

    @Test
    void getAll_ShouldReturn0Entities_WhenDatabaseHas0Entity() {
        List <CategoryEntity> entityList = List.of();

        when(categoryRepository.findAll()).thenReturn(entityList);

        List<CategoryDto> dtos = categoryServices.getAll();

        assertEquals(0,dtos.size());
    }

    @Test
    void getCategoryByName_ShouldReturn_WhenEntityExists(){
        CategoryDto expectedDto = CategoryDto.builder().categoryName("Balik").id(1L).categoryCode("BA").build();

        when(categoryRepository.getByCategoryName("Balik")).thenReturn(new CategoryEntity(1L, "Balik", CategoryType.Balik));

        CategoryDto categoryDto = categoryServices.getCategoryByName("Balik");

        assertEquals(expectedDto, categoryDto);
    }

    @Test
    void getCategoryByName_ShouldThrowException_WhenEntityDoesntExist(){
        when(categoryRepository.getByCategoryName("Balik")).thenReturn(null);

        assertThrows(CategoryNotFoundException.class, ()->categoryServices.getCategoryByName("Balik"), CATEGORY_DOESNT_EXIST);
    }

    @Test
    void isCategoryExists_ShouldReturnTrue_WhenEntityExists() {
        when(categoryRepository.getByCategoryName("Balik")).thenReturn(new CategoryEntity(1L, "Balik", CategoryType.Balik));

        boolean isExists = categoryServices.isCategoryExists("Balik");

        assertTrue(isExists);
    }

    @Test
    void isCategoryExists_ShouldReturnFalse_WhenEntityDoesntExist() {
        when(categoryRepository.getByCategoryName("Balik")).thenReturn(null);

        boolean isExists = categoryServices.isCategoryExists("Balik");

        assertFalse(isExists);
    }

    @Test
    void create_ShouldCreate_WhenCategoryDoesntExist() {
        CategoryDto expectedDto = CategoryDto.builder().categoryName("Balik").categoryCode("BA").id(1L).build();

        when(categoryRepository.save(new CategoryEntity(null, "Balik", CategoryType.Balik))).thenReturn(new CategoryEntity(1L, "Balik", CategoryType.Balik));

        CategoryDto categoryDto = categoryServices.create(CategoryDto.builder().categoryName("Balik").categoryCode("BA").build());

        assertEquals(expectedDto, categoryDto);
    }
    @Test
    void create_ShouldReturnBadRequest_WhenCategoryAlreadyCreated() {
        when(categoryRepository.getByCategoryName("Balik")).thenReturn(new CategoryEntity(1L, "Balik", CategoryType.Balik));

        CategoryDto categoryDto = categoryServices.create(CategoryDto.builder().categoryName("Balik").categoryCode("BA").build());
        assertNull(categoryDto);
    }


    @Test
    void create_ShouldThrowException_WhenUnknownCategoryType() {
        assertThrows(CategoryNotFoundException.class, () -> categoryServices.create(CategoryDto.builder().categoryCode("DCN").categoryName("DummyCategoryName").build()), "CategoryType doesn't exist");
    }


    @Test
    void delete_ShouldDelete_WhenEntityExists() {
        Map<String, Boolean> expectedResponse = new HashMap<>();
        expectedResponse.put(MessageFormat.format(CATEGORY_DELETED, 1L), Boolean.TRUE);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new CategoryEntity(1L, "Balik", CategoryType.Balik)));

        Map<String, Boolean> response = categoryServices.delete(1L);

        assertEquals(expectedResponse, response);
    }


    @Test
    void delete_ShouldThrowException_WhenEntityDoesntExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, ()->categoryServices.delete(1L), CATEGORY_DOESNT_EXIST);
    }


    @Test
    void updateById_ShouldUpdate_WhenNormallyEntityExist() {
        CategoryEntity entity = new CategoryEntity(1L, "Balik", CategoryType.Balik);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(categoryRepository.save(entity)).thenReturn(new CategoryEntity(1L, "Meyve", CategoryType.Meyve));

        CategoryDto updatedDto = categoryServices.updateById(1L, CategoryDto.builder().categoryName("Meyve").categoryCode("ME").build());

        assertEquals("Meyve", updatedDto.getCategoryName());
        assertEquals("ME", updatedDto.getCategoryCode());
    }


    @Test
    void updateById_ShouldNotUpdate_WhenCategoryNameIsBlank() {
        CategoryEntity entity = new CategoryEntity(1L, "Balik", CategoryType.Balik);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(categoryRepository.save(entity)).thenReturn(new CategoryEntity(1L, "Balik", CategoryType.Balik));

        CategoryDto updatedDto = categoryServices.updateById(1L, CategoryDto.builder().categoryName(" ").categoryCode("BA").build());

        assertEquals("Balik", updatedDto.getCategoryName());
    }


    @Test
    void updateById_ShouldThrowException_WhenEntityDoesntExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                ()-> categoryServices.updateById(1L,CategoryDto.builder()
                        .categoryName("Balik")
                        .categoryCode("BA")
                        .build()),
                CATEGORY_DOESNT_EXIST);
    }

    @Test
    void getById_ShouldReturn_WhenEntityExists() {
        CategoryDto expectedDto = CategoryDto.builder().id(1L).categoryCode("BA").categoryName("Balik").build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new CategoryEntity(1L, "Balik", CategoryType.Balik)));

        CategoryDto dto = categoryServices.getById(1L);
        assertEquals(expectedDto, dto);

    }

    @Test
    void getById_ShouldThrowException_WhenEntityDoesntExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, ()->categoryServices.getById(1L), CATEGORY_DOESNT_EXIST);
    }

    @Test
    void entityToDto() {
        CategoryDto expectedDto = CategoryDto.builder().id(1L).categoryCode("BA").categoryName("Balik").build();

        CategoryEntity entity = new CategoryEntity(1L, "Balik", CategoryType.Balik);
        CategoryDto dto = categoryServices.entityToDto(entity);

        assertEquals(expectedDto, dto);

    }

    @Test
    void dtoToEntity() {
        CategoryEntity expectedEntity = new CategoryEntity(1L, "Balik", CategoryType.Balik);

        CategoryDto dto = CategoryDto.builder().id(1L).categoryCode("BA").categoryName("Balik").build();
        CategoryEntity entity = categoryServices.dtoToEntity(dto);

        assertEquals(expectedEntity, entity);
    }
}