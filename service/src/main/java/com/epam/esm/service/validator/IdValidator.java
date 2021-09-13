package com.epam.esm.service.validator;

import com.epam.esm.service.constant.CustomHttpStatus;
import com.epam.esm.service.dto.ErrorMessageDto;
import com.epam.esm.service.locale.LanguageManager;
import com.epam.esm.service.locale.LocaleMessages;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IdValidator {
  public List<ErrorMessageDto> idValidate(Long id) {
    List<ErrorMessageDto> errors = new ArrayList<>();
    if (id < 1) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.NEGATIVE_ID.getCode(),
              LanguageManager.getMessage(LocaleMessages.ID_INVALID)));
    }
    return errors;
  }
}
