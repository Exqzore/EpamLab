package com.epam.esm.exception;

import com.epam.esm.constant.CustomHttpStatus;

import java.util.List;

public class NotFoundException extends RuntimeException {
  private static final String ERROR = "Error";

  private int errorCode = CustomHttpStatus.NOT_FOUND.UNDEFINED;
  private List<String> errorMessages = List.of(ERROR);

  public NotFoundException() {
    super();
  }

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(List<String> errorMessages, int errorCode) {
    this.errorMessages = errorMessages;
    this.errorCode = errorCode;
  }

  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotFoundException(Throwable cause) {
    super(cause);
  }

  public List<String> getErrorMessages() {
    return errorMessages;
  }

  public int getErrorCode() {
    return errorCode;
  }
}
