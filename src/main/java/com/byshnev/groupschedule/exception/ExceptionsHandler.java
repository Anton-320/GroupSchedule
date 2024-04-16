package com.byshnev.groupschedule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Simple exception handler.
 * */
@RestControllerAdvice
public class ExceptionsHandler {

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(RuntimeException.class)
  public ErrorResponse handlerInternalServerError(RuntimeException ex) {
    return new ErrorResponse(ex.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({HttpClientErrorException.class, MethodArgumentNotValidException.class})
  public ErrorResponse handlerBadRequestException(Exception ex) {
    return new ErrorResponse("400 error, BAD REQUEST");
  }

  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ErrorResponse handlerMethodNotAllowed(Exception ex) {
    return new ErrorResponse("405 error, METHOD NOT ALLOWED");
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
  public ErrorResponse handlerFoundException(Exception ex) {
    return new ErrorResponse("404 error, NOT FOUND");
  }
}