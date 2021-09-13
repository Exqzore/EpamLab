package com.epam.esm.dao.exception;

public class NoSuchResultException extends Exception {
  public NoSuchResultException() {
    super();
  }

  public NoSuchResultException(String message) {
    super(message);
  }

  public NoSuchResultException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoSuchResultException(Throwable cause) {
    super(cause);
  }
}
