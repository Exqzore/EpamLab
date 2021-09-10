package com.epam.esm.service.validator;

import com.epam.esm.service.constant.CustomHttpStatus;
import com.epam.esm.service.dto.ErrorMessageDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.locale.LanguageManager;
import com.epam.esm.service.locale.LocaleMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDtoValidator {
  private final CertificateDtoValidator certificateDtoValidator;

  @Autowired
  public OrderDtoValidator(CertificateDtoValidator certificateDtoValidator) {
    this.certificateDtoValidator = certificateDtoValidator;
  }

  public List<ErrorMessageDto> orderDtoValidate(OrderDto orderDto) {
    List<ErrorMessageDto> errors = new ArrayList<>();
    if (orderDto.getCertificates() != null && orderDto.getCertificates().isEmpty()) {
      errors.add(
          new ErrorMessageDto(
              CustomHttpStatus.BadRequest.ORDER.getCode(),
              LanguageManager.getMessage(LocaleMessages.ORDER_EMPTY_CERTIFICATE)));
    }
    errors.addAll(certificateDtoValidator.certificateIdsValidate(orderDto.getCertificates()));
    return errors;
  }
}
