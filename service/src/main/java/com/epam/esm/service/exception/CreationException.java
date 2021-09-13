package com.epam.esm.service.exception;

import com.epam.esm.service.constant.CustomHttpStatus;
import com.epam.esm.service.dto.ErrorMessageDto;

public class CreationException extends RuntimeException {
  private static final String UNDEFINED_ERROR = "Undefined creation error";

  private ErrorMessageDto errorMessage =
      new ErrorMessageDto(CustomHttpStatus.Conflict.UNDEFINED.getCode(), UNDEFINED_ERROR);

  public CreationException() {
    super();
  }

  public CreationException(String message) {
    super(message);
  }

  public CreationException(ErrorMessageDto errorMessage) {
    this.errorMessage = errorMessage;
  }

  public CreationException(String message, Throwable cause) {
    super(message, cause);
  }

  public CreationException(Throwable cause) {
    super(cause);
  }

  public ErrorMessageDto getErrorMessage() {
    return errorMessage;
  }
}
