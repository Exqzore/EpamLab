package com.epam.esm.controller;

import com.epam.esm.constant.CustomHttpStatus;
import com.epam.esm.controller.dto.ErrorMessageDto;
import com.epam.esm.exception.CreationException;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.ValidationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
@RestController
public class ExceptionController extends ResponseEntityExceptionHandler {
  private static final String UNSUPPORTED_MEDIA_TYPE = "Unsupported media type";
  private static final String NOT_FOUND = "Not found";
  private static final String TYPE_MISMATCH = "Type mismatch";

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public ErrorMessageDto handleNotFound(NotFoundException exception) {
    return new ErrorMessageDto(exception.getErrorCode(), exception.getErrorMessages());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ValidationException.class)
  public ErrorMessageDto handleValidation(ValidationException exception) {
    return new ErrorMessageDto(exception.getErrorCode(), exception.getErrorMessages());
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(CreationException.class)
  public ErrorMessageDto handleConflict(CreationException exception) {
    return new ErrorMessageDto(exception.getErrorCode(), List.of(exception.getErrorMessage()));
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return new ResponseEntity<>(
        new ErrorMessageDto(CustomHttpStatus.BAD_REQUEST.UNDEFINED, List.of(TYPE_MISMATCH)),
        HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return new ResponseEntity<>(
        new ErrorMessageDto(
            CustomHttpStatus.UNSUPPORTED_MEDIA_TYPE, List.of(UNSUPPORTED_MEDIA_TYPE)),
        HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return new ResponseEntity<>(
        new ErrorMessageDto(CustomHttpStatus.NOT_FOUND.UNDEFINED, List.of(NOT_FOUND)),
        HttpStatus.NOT_FOUND);
  }
}
