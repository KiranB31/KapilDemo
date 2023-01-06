package com.example.excel.excel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Custom Exception
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourseNotFoundException extends RuntimeException{

    private static final Long serialVersion = 1L;

    public ResourseNotFoundException(String message){
        super(message);
    }
}
