package com.n26.controller;


import static org.springframework.http.HttpStatus.NO_CONTENT;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.n26.exceptions.InvalidTimestampException;

@ControllerAdvice(basePackages = {"com.n26.controller"} )
public class TransactionControllerAdvice {
	
	private static final Logger logger = LogManager.getLogger(TransactionControllerAdvice.class);
	
    @ResponseStatus(NO_CONTENT)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException() {
    }

    @ResponseStatus(NO_CONTENT)
    @ExceptionHandler(InvalidTimestampException.class)
    public void handleInvalidTimestampException(Exception e) {
    	logger.error(e);
    }
}
