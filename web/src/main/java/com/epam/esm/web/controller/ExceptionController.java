package com.epam.esm.web.controller;

import com.epam.esm.service.constant.CustomHttpStatus;
import com.epam.esm.service.dto.ErrorMessageDto;
import com.epam.esm.service.exception.CreationException;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.locale.LanguageManager;
import com.epam.esm.service.locale.LocaleMessages;
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
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public ErrorMessageDto handleNotFound(NotFoundException exception) {
    return exception.getErrorMessage();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ValidationException.class)
  public List<ErrorMessageDto> handleValidation(ValidationException exception) {
    return exception.getErrorMessages();
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(CreationException.class)
  public ErrorMessageDto handleConflict(CreationException exception) {
    return exception.getErrorMessage();
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return new ResponseEntity<>(
        new ErrorMessageDto(
            CustomHttpStatus.BadRequest.CERTIFICATE.getCode(),
            LanguageManager.getMessage(LocaleMessages.TYPE_MISMATCH)),
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
            CustomHttpStatus.UNSUPPORTED_MEDIA_TYPE.getCode(),
            LanguageManager.getMessage(LocaleMessages.UNSUPPORTED_MEDIA_TYPE)),
        HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return new ResponseEntity<>(
        new ErrorMessageDto(
            CustomHttpStatus.NotFound.UNDEFINED.getCode(),
            LanguageManager.getMessage(LocaleMessages.NOT_FOUND)),
        HttpStatus.NOT_FOUND);
  }
}
