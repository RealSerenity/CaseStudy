package com.rserenity.productservice.business.services.impl;

import com.rserenity.productservice.business.dto.ProductDto;
import com.rserenity.productservice.data.entity.ProductEntity;
import com.rserenity.productservice.data.entity.repository.ProductRepository;
import com.rserenity.productservice.data.enums.UnitType;
import com.rserenity.productservice.exception.ProductNotFoundException;
import com.rserenity.productservice.util.BarcodeServiceUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.rserenity.productservice.constants.MessageConstants.PRODUCT_DELETED;
import static com.rserenity.productservice.constants.MessageConstants.PRODUCT_DOESNT_EXIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ProductServicesImplTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    CodeGenerator codeGenerator;
    @Mock
    BarcodeServiceUtil barcodeServiceUtil;
    @Spy
    ModelMapper modelMapper = new ModelMapper();
    @InjectMocks
    ProductServicesImpl productServices;


    @Test
    void getAll_ShouldReturnAllEntities_WhenEntitiesExits() {
        List<ProductEntity> entityList = List.of(new ProductEntity(1L,"MEr2a","Elma",2L,2.0,"none", UnitType.Adet),
                new ProductEntity(2L,"MEbL0","Çilek",2L,9.0,"none", UnitType.Kilogram));
        when(productRepository.findAll()).thenReturn(entityList);
        List<ProductDto> dtos = productServices.getAll();
        assertEquals(2,dtos.size());
        assertEquals(dtos.get(0), ProductDto.builder().id(1L).code("MEr2a").name("Elma").categoryId(2L).price(2.0).brand("none").unitType("Adet").build());
        assertEquals(dtos.get(1), ProductDto.builder().id(2L).code("MEbL0").name("Çilek").categoryId(2L).price(9.0).brand("none").unitType("Kilogram").build());

    }

    @Test
    void getAll_ShouldReturn0Entities_WhenDatabaseHas0Entity() {
        List <ProductEntity> entityList = List.of();
        when(productRepository.findAll()).thenReturn(entityList);
        List<ProductDto> dtos = productServices.getAll();
        assertEquals(0,dtos.size());
    }

    @Test
    void getProductByName_ShouldReturn_WhenExists() {
        ProductDto expectedDto = ProductDto.builder().id(1L).name("Elma").code("MEr2a").categoryId(2L).price(2.0).brand("none").unitType("Adet").build();
        when(productRepository.getByName("Elma")).thenReturn(new ProductEntity(1L,"MEr2a","Elma",2L,2.0,"none", UnitType.Adet));

        ProductDto productDto = productServices.getProductByName("Elma");
        assertEquals(expectedDto, productDto);
    }

    @Test
    void getProductByName_ShouldThrowException_WhenDoesntExist() {
        when(productRepository.getByName("Elma")).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> productServices.getProductByName("Elma"), PRODUCT_DOESNT_EXIST);
    }

    @Test
    void getProductByCode_ShouldReturn_WhenExists() {
        ProductDto expectedDto = ProductDto.builder().id(1L).name("Elma").code("MEr2a").categoryId(2L).price(2.0).brand("none").unitType("Adet").build();
        when(productRepository.getByCode("MEr2a")).thenReturn(new ProductEntity(1L,"MEr2a","Elma",2L,2.0,"none", UnitType.Adet));

        ProductDto productDto = productServices.getProductByCode("MEr2a");
        assertEquals(expectedDto, productDto);
    }

    @Test
    void getProductByCode_ShouldThrowException_WhenDoesntExist() {
        when(productRepository.getByCode("MEr2a")).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> productServices.getProductByCode("MEr2a"), PRODUCT_DOESNT_EXIST);
    }

    @Test
    void getProductById_ShouldReturn_WhenEntityExists() {
        ProductDto expectedDto = ProductDto.builder().id(1L).name("Elma").code("MEr2a").categoryId(2L).price(2.0).brand("none").unitType("Adet").build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(new ProductEntity(1L, "MEr2a", "Elma", 2L, 2.0, "none", UnitType.Adet)));

        ProductDto productDto = productServices.getProductById(1L);
        assertEquals(expectedDto, productDto);

    }

    @Test
    void getProductById_ShouldThrowException_WhenEntityDoesntExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productServices.getProductById(1L), PRODUCT_DOESNT_EXIST);
    }
    @Test
    void isProductNameExists_ShouldReturnTrue_WhenEntityExists() {
        when(productRepository.getByName("Elma")).thenReturn(new ProductEntity(1L,"MEr2a","Elma",2L,2.0,"none", UnitType.Adet));

        boolean exists = productServices.isProductNameExists("Elma");
        assertTrue(exists);
    }

    @Test
    void isProductNameExists_ShouldReturnFalse_WhenEntityDoesntExist() {
        when(productRepository.getByName("Elma")).thenReturn(null);
        boolean exists = productServices.isProductNameExists("Elma");
        assertFalse(exists);
    }

    @Test
    void isProductCodeExists_ShouldReturnTrue_WhenEntityExists() {
        when(productRepository.getByCode("MEr2a")).thenReturn(new ProductEntity(1L,"MEr2a","Elma",2L,2.0,"none", UnitType.Adet));
        boolean exists = productServices.isProductCodeExists("MEr2a");
        assertTrue(exists);
    }

    @Test
    void isProductCodeExists_ShouldReturnFalse_WhenEntityDoesntExist() {
        when(productRepository.getByCode("MEr2a")).thenReturn(null);
        boolean exists = productServices.isProductCodeExists("MEr2a");
        assertFalse(exists);
    }

    @Test
    void create_ShouldCreateWithRandomCode() {
        when(codeGenerator.generateRandomCode(3)).thenReturn("abc");
        when(codeGenerator.generateProductCode(2L, "abc")).thenReturn("MEabc");
        when(productRepository.save(new ProductEntity(null,"MEabc","Elma",2L,2.0,"none", UnitType.Adet)))
                .thenReturn(new ProductEntity(1L,"MEabc","Elma",2L,2.0,"none", UnitType.Adet));


        ProductDto expectedDto = ProductDto.builder().id(1L).code("MEabc").name("Elma").categoryId(2L).price(2.0).brand("none").unitType("Adet").build();

        ProductDto productDtoResponseEntity = productServices.create(ProductDto.builder().name("Elma").categoryId(2L).price(2.0).brand("none").unitType("Adet").build());

        assertEquals(expectedDto, productDtoResponseEntity);
    }

    @Test
    void create_ShouldReturnBadRequest_WhenProductAlreadyInDatabase() {
        ProductDto dto = ProductDto.builder().name("Elma").categoryId(2L).price(2.0).brand("none").unitType("Adet").build();
        when(productRepository.getByName("Elma")).thenReturn(new ProductEntity(1L,"MEr2a","Elma",2L,2.0,"none", UnitType.Adet));

        ProductDto response = productServices.create(dto);
        assertNull(response);
    }

    @Test
    void delete_ShouldDelete_WhenEntityExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(new ProductEntity(1L,"MEr2a","Elma",2L,2.0,"none", UnitType.Adet)));
        Map<String, Boolean> response = productServices.delete(1L);
        Map<String, Boolean> expectedResponse = new HashMap<>();
        expectedResponse.put(MessageFormat.format(PRODUCT_DELETED, 1L), Boolean.TRUE);
        assertEquals(expectedResponse, response);
    }

    @Test
    void delete_ShouldThrowException_WhenEntityDoesntExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productServices.delete(1L), PRODUCT_DOESNT_EXIST);
    }

    @Test
    void updateById_ShouldUpdate_WhenAllPropsDifference() {
        ProductEntity entity = new ProductEntity(1L,"MEr2a","Elma",2L,2.0,"none", UnitType.Kilogram);
        ProductDto expectedDto = ProductDto.builder().id(1L).code("MEr2a").name("Somon").categoryId(2L).price(4.0).brand("Migros").unitType("Adet").build();
        ProductDto dtoAllPropertiesChanged = ProductDto.builder().id(1L).code("MEr2a").name("Somon").categoryId(2L).price(4.0).brand("Migros").unitType("Adet").build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(productRepository.save(entity)).thenReturn(entity);

        ProductDto productDtoResponseEntity= productServices.updateById(1L, dtoAllPropertiesChanged);
        assertEquals(expectedDto, productDtoResponseEntity);
    }

    @Test
    void updateById_ShouldNotUpdate_WhenNamePropertyIsBlank() {
        ProductEntity entity = new ProductEntity(1L,"MEr2a","Elma",2L,2.0,"none", UnitType.Adet);
        ProductDto expectedDto = ProductDto.builder().id(1L).code("MEr2a").name("Elma").categoryId(2L).price(2.0).brand("none").unitType("Adet").build();
        ProductDto dtoWithBlankName = ProductDto.builder().id(1L).code("MEr2a").name(" ").categoryId(2L).price(2.0).brand("none").unitType("Adet").build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(productRepository.save(entity)).thenReturn(entity);

        ProductDto productDtoResponseEntity= productServices.updateById(1L, dtoWithBlankName);
        assertEquals(expectedDto, productDtoResponseEntity);
    }

    @Test
    void updateById_ShouldNotUpdate_WhenPriceIsNull() {
        ProductEntity entity = new ProductEntity(1L,"MEr2a","Elma",2L,2.0,"none", UnitType.Adet);
        ProductDto expectedDto = ProductDto.builder().id(1L).code("MEr2a").name("Elma").categoryId(2L).price(2.0).brand("none").unitType("Adet").build();
        ProductDto dtoWithNullPrice = ProductDto.builder().id(1L).code("MEr2a").name("Elma").categoryId(2L).price(null).brand("none").unitType("Adet").build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(productRepository.save(entity)).thenReturn(entity);

        ProductDto productDtoResponseEntity= productServices.updateById(1L, dtoWithNullPrice);
        assertEquals(expectedDto, productDtoResponseEntity);
    }

    @Test
    void updateById_ShouldNotUpdate_WhenBrandIsBlank() {
        ProductEntity entity = new ProductEntity(1L,"MEr2a","Elma",2L,2.0,"none", UnitType.Adet);
        ProductDto expectedDto = ProductDto.builder().id(1L).code("MEr2a").name("Elma").categoryId(2L).price(2.0).brand("none").unitType("Adet").build();
        ProductDto dtoWithBlankBrand = ProductDto.builder().id(1L).code("MEr2a").name("Elma").categoryId(2L).price(2.0).brand(" ").unitType("Adet").build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(productRepository.save(entity)).thenReturn(entity);

        ProductDto productDtoResponseEntity= productServices.updateById(1L, dtoWithBlankBrand);
        assertEquals(expectedDto, productDtoResponseEntity);
    }

    @Test
    void updateById_ShouldThrowException_WhenEntityDoesntExist(){
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productServices.getProductById(1L), PRODUCT_DOESNT_EXIST);
    }

    @Test
    void entityToDto() {
        ProductEntity entity = new ProductEntity(1L,"MEr2a","Elma",2L,2.0,"none", UnitType.Adet);
        ProductDto expectedDto = ProductDto.builder().id(1L).code("MEr2a").name("Elma").categoryId(2L).price(2.0).brand("none").unitType("Adet").build();
        ProductDto dto = productServices.entityToDto(entity);
        assertEquals(expectedDto, dto);
    }

    @Test
    void dtoToEntity() {
        ProductDto dto = ProductDto.builder().id(1L).code("MEr2a").name("Elma").categoryId(2L).price(2.0).brand("none").unitType("Adet").build();
        ProductEntity expectedEntity = new ProductEntity(1L,"MEr2a","Elma",2L,2.0,"none", UnitType.Adet);
        ProductEntity entity = productServices.dtoToEntity(dto);
        assertEquals(expectedEntity, entity);
    }
}