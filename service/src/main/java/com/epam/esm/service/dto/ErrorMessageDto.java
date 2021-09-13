package com.epam.esm.service.dto;

public class ErrorMessageDto {
  private int errorCode;
  private String errorMessage;

  public ErrorMessageDto(int errorCode, String errorMessage) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ErrorMessageDto that = (ErrorMessageDto) o;

    if (errorCode != that.errorCode) return false;
    return errorMessage != null
        ? errorMessage.equals(that.errorMessage)
        : that.errorMessage == null;
  }

  @Override
  public int hashCode() {
    int result = errorCode;
    result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ErrorMessageDto{");
    sb.append("errorCode=").append(errorCode);
    sb.append(", errorMessage=").append(errorMessage);
    sb.append('}');
    return sb.toString();
  }
}
