package com.rserenity.productservice.util;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "BarcodeClient", url = "http://localhost:8080/api/barcodes")
public interface BarcodeServiceUtil {

    @RequestMapping(method = RequestMethod.POST,value = "createBarcode")
    void createBarcode(@RequestParam("productId") Long productId, @RequestParam("barcodeType") String barcodeType);

    @RequestMapping(method = RequestMethod.POST,value = "updateBarcodes")
    ResponseEntity<Map<String, Boolean>> updateBarcodes(@RequestParam("productId") Long productId);

    @RequestMapping(method = RequestMethod.GET, value = "getSuitableBarcodeTypes")
    List<String> getSuitableBarcodeTypes(@RequestParam("productId") Long productId);
}
