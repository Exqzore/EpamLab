package com.epam.esm.service.validator;

import com.epam.esm.service.constant.CustomHttpStatus;
import com.epam.esm.service.dto.ErrorMessageDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.locale.LanguageManager;
import com.epam.esm.service.locale.LocaleMessages;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagDtoValidator {
  private static final String NAME_REGEX = "[a-zA-Z][a-zA-Z0-9_]{2,30}";

  public List<ErrorMessageDto> tagValidate(TagDto tagDto) {
    Long id = tagDto.getId();
    List<ErrorMessageDto> errors = new ArrayList<>();
    if (id != null && id < 1) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.NEGATIVE_ID.getCode(),
              LanguageManager.getMessage(LocaleMessages.ID_INVALID)));
    }
    String name = tagDto.getName();
    if (name == null) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.EMPTY_TAG_NAME.getCode(),
              LanguageManager.getMessage(LocaleMessages.TAG_NAME_EMPTY)));
    }
    if (name != null && !name.matches(NAME_REGEX)) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.INVALID_TAG_NAME.getCode(),
              LanguageManager.getMessage(LocaleMessages.TAG_NAME_INVALID)));
    }
    return errors;
  }

  public List<ErrorMessageDto> tagsValidate(List<TagDto> tags) {
    List<ErrorMessageDto> errors = new ArrayList<>();
    if (tags != null) {
      tags.forEach(
          tagDto -> {
            Long tagId = tagDto.getId();
            String tagName = tagDto.getName();
            if (tagId == null && tagName == null) {
              errors.add(
                  new ErrorMessageDto(
                      CustomHttpStatus.BadRequest.EMPTY_TAG.getCode(),
                      LanguageManager.getMessage(LocaleMessages.TAG_EMPTY)));
            }
            if (tagId != null && tagName != null) {
              errors.add(
                  new ErrorMessageDto(
                      CustomHttpStatus.BadRequest.TAG_ALL_PARAMS.getCode(),
                      LanguageManager.getMessage(LocaleMessages.TAG_ALL_PARAMS)));
            }
            if (tagId != null && tagId < 1) {
              errors.add(
                  new ErrorMessageDto(
                      CustomHttpStatus.BadRequest.NEGATIVE_ID.getCode(),
                      LanguageManager.getMessage(LocaleMessages.ID_INVALID)));
            }
            if (tagName != null && !tagName.matches(NAME_REGEX)) {
              errors.add(
                  new ErrorMessageDto(
                      CustomHttpStatus.BadRequest.INVALID_TAG_NAME.getCode(),
                      LanguageManager.getMessage(LocaleMessages.TAG_NAME_INVALID)));
            }
          });
    }
    return errors;
  }
}
