package com.epam.esm.service.validator;

import com.epam.esm.service.constant.CustomHttpStatus;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.ErrorMessageDto;
import com.epam.esm.service.locale.LanguageManager;
import com.epam.esm.service.locale.LocaleMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class CertificateDtoValidator {
  private static final String NAME_REGEX = "[a-zA-Z][a-zA-Z0-9_]{2,30}";
  private static final String DESCRIPTION_REGEX = "[a-zA-Z0-9_,.\\/\\\\<> ;:?!-]{2,300}";

  private final TagDtoValidator tagDtoValidator;

  @Autowired
  public CertificateDtoValidator(TagDtoValidator tagDtoValidator) {
    this.tagDtoValidator = tagDtoValidator;
  }

  public List<ErrorMessageDto> creationValidate(CertificateDto certificateDto) {
    List<ErrorMessageDto> errors = new ArrayList<>();
    String name = certificateDto.getName();
    if (name == null) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.EMPTY_CERTIFICATE_NAME.getCode(),
              LanguageManager.getMessage(LocaleMessages.CERTIFICATE_NAME_EMPTY)));
    } else if (!name.matches(NAME_REGEX)) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.INVALID_CERTIFICATE_NAME.getCode(),
              LanguageManager.getMessage(LocaleMessages.CERTIFICATE_NAME_INVALID)));
    }
    String description = certificateDto.getDescription();
    if (description == null) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.EMPTY_CERTIFICATE_DESCRIPTION.getCode(),
              LanguageManager.getMessage(LocaleMessages.CERTIFICATE_DESCRIPTION_EMPTY)));
    } else if (!description.matches(DESCRIPTION_REGEX)) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.INVALID_CERTIFICATE_DESCRIPTION.getCode(),
              LanguageManager.getMessage(LocaleMessages.CERTIFICATE_DESCRIPTION_INVALID)));
    }
    Integer duration = certificateDto.getDuration();
    if (duration == null) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.EMPTY_CERTIFICATE_DURATION.getCode(),
              LanguageManager.getMessage(LocaleMessages.CERTIFICATE_DURATION_EMPTY)));
    } else if (duration < 1) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.INVALID_CERTIFICATE_DURATION.getCode(),
              LanguageManager.getMessage(LocaleMessages.CERTIFICATE_DURATION_NEGATIVE)));
    }
    Double price = certificateDto.getPrice();
    if (price == null) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.EMPTY_CERTIFICATE_PRICE.getCode(),
              LanguageManager.getMessage(LocaleMessages.CERTIFICATE_PRICE_EMPTY)));
    } else if (price < 0) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.INVALID_CERTIFICATE_PRICE.getCode(),
              LanguageManager.getMessage(LocaleMessages.CERTIFICATE_PRICE_NEGATIVE)));
    }
    errors.addAll(tagDtoValidator.tagsValidate(certificateDto.getTags()));
    errors = new HashSet<>(errors).stream().toList();
    return errors;
  }

  public List<ErrorMessageDto> updateValidate(CertificateDto certificateDto) {
    List<ErrorMessageDto> errors = new ArrayList<>();
    String name = certificateDto.getName();
    if (name != null && !name.matches(NAME_REGEX)) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.INVALID_CERTIFICATE_NAME.getCode(),
              LanguageManager.getMessage(LocaleMessages.CERTIFICATE_NAME_INVALID)));
    }
    String description = certificateDto.getDescription();
    if (description != null && !description.matches(DESCRIPTION_REGEX)) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.INVALID_CERTIFICATE_DESCRIPTION.getCode(),
              LanguageManager.getMessage(LocaleMessages.CERTIFICATE_DESCRIPTION_INVALID)));
    }
    Integer duration = certificateDto.getDuration();
    if (duration != null && duration < 1) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.INVALID_CERTIFICATE_DURATION.getCode(),
              LanguageManager.getMessage(LocaleMessages.CERTIFICATE_DURATION_NEGATIVE)));
    }
    Double price = certificateDto.getPrice();
    if (price != null && price < 0) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.INVALID_CERTIFICATE_PRICE.getCode(),
              LanguageManager.getMessage(LocaleMessages.CERTIFICATE_PRICE_NEGATIVE)));
    }
    errors.addAll(tagDtoValidator.tagsValidate(certificateDto.getTags()));
    errors = new HashSet<>(errors).stream().toList();
    return errors;
  }

  public List<ErrorMessageDto> certificateIdsValidate(List<CertificateDto> certificateDtoList) {
    List<ErrorMessageDto> errors = new ArrayList<>();
    if (certificateDtoList != null) {
      certificateDtoList.forEach(
          certificateDto -> {
            Long id = certificateDto.getId();
            if (id == null) {
              errors.add(
                  new ErrorMessageDto(
                      CustomHttpStatus.BadRequest.EMPTY_CERTIFICATE_ID.getCode(),
                      LanguageManager.getMessage(LocaleMessages.CERTIFICATE_ID_EMPTY)));
            } else if (id < 1) {
              errors.add(
                  new ErrorMessageDto(
                      CustomHttpStatus.BadRequest.INVALID_ID.getCode(),
                      LanguageManager.getMessage(LocaleMessages.ID_INVALID)));
            }
          });
    }
    return errors;
  }
}
