package com.rserenity.categoryservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class CategoryExistsException extends  RuntimeException implements Serializable {

    private static final Long serialVersionUID=1L;

    public CategoryExistsException(String message) {
        super(message);
    }
}
