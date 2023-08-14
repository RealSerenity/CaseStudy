package com.rserenity.barcodeservice.util;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "CategoryClient", url = "http://localhost:8060/api/categories")
public interface CategoryServiceUtil {

    @RequestMapping(method = RequestMethod.GET,value = "getCategoryCodeById")
    String getCategoryCodeById(@RequestParam(value = "categoryId") Long categoryId);
}