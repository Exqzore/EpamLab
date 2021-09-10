package com.epam.esm.service.validator;

import com.epam.esm.service.constant.CustomHttpStatus;
import com.epam.esm.service.dto.ErrorMessageDto;
import com.epam.esm.service.locale.LanguageManager;
import com.epam.esm.service.locale.LocaleMessages;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaginateValidator {
  public List<ErrorMessageDto> pageValidate(int page) {
    List<ErrorMessageDto> errors = new ArrayList<>();
    if (page < 1) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.INVALID_PAGE.getCode(),
              LanguageManager.getMessage(LocaleMessages.PAGE_INVALID)));
    }
    return errors;
  }

  public List<ErrorMessageDto> sizeValidate(int size) {
    List<ErrorMessageDto> errors = new ArrayList<>();
    if (size > 100 || size < 1) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.INVALID_SIZE.getCode(),
              LanguageManager.getMessage(LocaleMessages.SIZE_INVALID)));
    }
    return errors;
  }
}
