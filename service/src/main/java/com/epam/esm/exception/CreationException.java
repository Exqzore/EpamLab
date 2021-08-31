package com.epam.esm.exception;

import com.epam.esm.constant.CustomHttpStatus;

public class CreationException extends RuntimeException {
  private int errorCode = CustomHttpStatus.CONFLICT.UNDEFINED;

  public CreationException() {
    super();
  }

  public CreationException(String message) {
    super(message);
  }

  public CreationException(String errorMessage, int errorCode) {
    super(errorMessage);
    this.errorCode = errorCode;
  }

  public CreationException(String message, Throwable cause) {
    super(message, cause);
  }

  public CreationException(Throwable cause) {
    super(cause);
  }

  public String getErrorMessage() {
    return this.getMessage();
  }

  public int getErrorCode() {
    return errorCode;
  }
}
