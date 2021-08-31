package com.epam.esm.exception;

import com.epam.esm.constant.CustomHttpStatus;

import java.util.List;

public class ValidationException extends RuntimeException {
  private static final String ERROR = "Error";

  private int errorCode = CustomHttpStatus.BAD_REQUEST.UNDEFINED;
  private List<String> errorMessages = List.of(ERROR);

  public ValidationException() {
    super();
  }

  public ValidationException(String message) {
    super(message);
  }

  public ValidationException(List<String> errorMessages, int errorCode) {
    this.errorCode = errorCode;
    this.errorMessages = errorMessages;
  }

  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ValidationException(Throwable cause) {
    super(cause);
  }

  public List<String> getErrorMessages() {
    return errorMessages;
  }

  public int getErrorCode() {
    return errorCode;
  }
}
