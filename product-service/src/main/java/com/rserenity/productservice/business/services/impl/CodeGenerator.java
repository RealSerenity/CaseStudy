package com.rserenity.productservice.business.services.impl;

import com.rserenity.productservice.business.services.ProductServices;
import com.rserenity.productservice.data.entity.repository.ProductRepository;
import com.rserenity.productservice.util.CategoryServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class CodeGenerator {

    private final CategoryServiceUtil categoryServiceUtil;
    private final ProductRepository productRepository;
    private ProductServices productServices;



    public String generateProductCode(Long categoryId, String randomPart) {
        String categoryCode = categoryServiceUtil.getCategoryCode(categoryId);

        String productCode = categoryCode + randomPart;
        if(productRepository.getByCode(productCode)!=null){
            return generateProductCode(categoryId, randomPart);
        }
        return productCode;
    }

    public String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

}
