package com.example.javaintern.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.DateTimeException;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class ExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> EntityNotFoundHandler(NoSuchElementException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<String> EntityNotFoundHandler(DateTimeException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

   @ResponseBody
   @ExceptionHandler(ConstraintViolationException.class)
   public ResponseEntity<List<String>> validationError(ConstraintViolationException ex) {
       List<String> errorMessages = ex.getConstraintViolations().
               stream().map(ConstraintViolation::getMessage)
               .collect(toList());

       return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
   }
}
