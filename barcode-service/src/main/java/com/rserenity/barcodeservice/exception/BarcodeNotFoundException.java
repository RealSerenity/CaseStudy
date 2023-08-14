package com.rserenity.barcodeservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BarcodeNotFoundException extends  RuntimeException implements Serializable {
    private static final Long serialVersionUID=1L;

    public BarcodeNotFoundException(String message) {
        super(message);
    }}