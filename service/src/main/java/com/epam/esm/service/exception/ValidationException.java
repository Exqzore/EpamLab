package com.epam.esm.service.exception;

import com.epam.esm.service.constant.CustomHttpStatus;
import com.epam.esm.service.dto.ErrorMessageDto;
import com.epam.esm.service.locale.LanguageManager;
import com.epam.esm.service.locale.LocaleMessages;

import java.util.List;

public class ValidationException extends RuntimeException {
  private static final String UNDEFINED_ERROR =
      LanguageManager.getMessage(LocaleMessages.VALIDATION_UNDEFINED_ERROR);

  private List<ErrorMessageDto> errorMessages =
      List.of(
          new ErrorMessageDto(CustomHttpStatus.BadRequest.UNDEFINED.getCode(), UNDEFINED_ERROR));

  public ValidationException() {
    super();
  }

  public ValidationException(String message) {
    super(message);
  }

  public ValidationException(List<ErrorMessageDto> errorMessages) {
    this.errorMessages = errorMessages;
  }

  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ValidationException(Throwable cause) {
    super(cause);
  }

  public List<ErrorMessageDto> getErrorMessages() {
    return errorMessages;
  }
}
