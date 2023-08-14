package com.rserenity.barcodeservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BarcodeException extends  RuntimeException implements Serializable {
    private static final Long serialVersionUID=1L;

    public BarcodeException(String message) {
        super(message);
    }
}
