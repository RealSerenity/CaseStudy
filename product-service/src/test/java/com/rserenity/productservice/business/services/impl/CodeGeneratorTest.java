package com.rserenity.productservice.business.services.impl;

import com.rserenity.productservice.business.services.ProductServices;
import com.rserenity.productservice.data.entity.repository.ProductRepository;
import com.rserenity.productservice.util.CategoryServiceUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.StartsWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CodeGeneratorTest {

    @InjectMocks
    CodeGenerator codeGenerator;
    @Mock
    ProductRepository productRepository;
    @Mock
    CategoryServiceUtil categoryServiceUtil;


    @Test
    void generateProductCode_ShouldGenerate_WhenProductCodeAvailable() {
        when(categoryServiceUtil.getCategoryCode(1L)).thenReturn("ME");
        when(productRepository.getByCode("MEabc")).thenReturn(null);

        String code = codeGenerator.generateProductCode(1L, "abc");
        assertEquals(5, code.length());
        assertFalse(code.isBlank());
        assertEquals("MEabc", code);
    }

    @Test
    void generateRandomCode() {
        String code = codeGenerator.generateRandomCode(3);
        assertEquals(3, code.length());
        assertFalse(code.isBlank());
    }
}