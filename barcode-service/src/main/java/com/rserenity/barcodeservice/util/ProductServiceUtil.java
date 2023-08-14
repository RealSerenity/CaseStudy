package com.rserenity.barcodeservice.util;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "ProductClient", url = "http://localhost:8070/api/products")
public interface ProductServiceUtil {

    @RequestMapping(method = RequestMethod.GET,value = "getProductUnitType")
    String getProductUnitType(@RequestParam(value = "productId") Long productId);

    @RequestMapping(method = RequestMethod.GET,value = "getCategoryId")
    Long getCategoryId(@RequestParam(value = "productId") Long productId);

    @RequestMapping(method = RequestMethod.GET,value = "getProductCode")
    String getProductCode(@RequestParam(value = "productId") Long productId);
}

