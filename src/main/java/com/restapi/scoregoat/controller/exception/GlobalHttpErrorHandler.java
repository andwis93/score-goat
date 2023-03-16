package com.restapi.scoregoat.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalHttpErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SeasonNotFoundException.class)
    public ResponseEntity<Object> handlerSeasonNotFoundException(SeasonNotFoundException exception) {
        return new ResponseEntity<>("Couldn't find current season", HttpStatus.BAD_REQUEST);
    }
}
