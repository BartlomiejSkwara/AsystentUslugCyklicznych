package com.example.uslugicykliczne.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public  ResponseEntity<String> handleConstraintViolation(SQLIntegrityConstraintViolationException exception){
        //TODO jakieś lepsze body które coś faktycznie tłumaczy XD
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Data violates SQL constraint");

    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public  ResponseEntity<String> handleJsonException(){
        return ResponseEntity.badRequest().body("Message not readable, check json formatting !!!");

    }
    @ExceptionHandler({CannotCreateTransactionException.class})
    public ResponseEntity<String> handleTransactionException(){
        return ResponseEntity.internalServerError().body("Can't create transaction, db may not be accessible !!!");
    }

    @ExceptionHandler({InvalidFormatException.class, MismatchedInputException.class})
    public ResponseEntity<String> handlerIllegalArgumentException(JsonProcessingException exception ) throws IOException {
        if(exception instanceof InvalidFormatException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
            //LOGGER.error(exception.getMessage(), exception);
        } else if (exception instanceof MismatchedInputException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
            //LOGGER.error(exception.getMessage(), exception);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("something went really wrong"); ///
    }
}
