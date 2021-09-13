package com.epam.esm.service.exception;

import com.epam.esm.service.constant.CustomHttpStatus;
import com.epam.esm.service.dto.ErrorMessageDto;
import com.epam.esm.service.locale.LanguageManager;
import com.epam.esm.service.locale.LocaleMessages;

public class NotFoundException extends RuntimeException {
  private static final String UNDEFINED_ERROR =
      LanguageManager.getMessage(LocaleMessages.NOT_FOUND_UNDEFINED_ERROR);

  private ErrorMessageDto errorMessage =
      new ErrorMessageDto(CustomHttpStatus.NotFound.UNDEFINED.getCode(), UNDEFINED_ERROR);

  public NotFoundException() {
    super();
  }

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(ErrorMessageDto errorMessage) {
    this.errorMessage = errorMessage;
  }

  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotFoundException(Throwable cause) {
    super(cause);
  }

  public ErrorMessageDto getErrorMessage() {
    return errorMessage;
  }
}
