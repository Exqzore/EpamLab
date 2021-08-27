package com.epam.esm.validator;

import com.epam.esm.dto.CertificateDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Component
public class CertificateDtoValidator {
  private static final String NAME_REGEX = "[a-zA-Z][a-zA-Z0-9_]{2,30}";
  private static final String DESCRIPTION_REGEX = "[a-zA-Z0-9_,.\\/\\\\<> ;:?!-]{2,300}";

  private static final String ID_IS_INVALID = "Id cannot be less than 0";
  private static final String NAME_IS_EMPTY = "Name must be quoted";
  private static final String NAME_IS_INVALID = "Name should be between 2 and 30 characters";
  private static final String DESCRIPTION_IS_EMPTY = "Description must be quoted";
  private static final String DESCRIPTION_IS_INVALID =
      "Description should be between 2 and 300 characters";
  private static final String PRICE_IS_EMPTY = "Price must be quoted";
  private static final String PRICE_IS_INVALID = "Price cannot be less than 0";
  private static final String DURATION_IS_EMPTY = "Duration must be quoted";
  private static final String DURATION_IS_INVALID = "Duration cannot be less than 1";


  public static List<String> createCertificateDtoValidate(CertificateDto certificateDto) {
    Long id = certificateDto.getId();
    List<String> errors = new ArrayList<>(Collections.emptyList());
    if (id != null && id < 1) {
      errors.add(ID_IS_INVALID);
    }
    String name = certificateDto.getName();
    if (name == null) {
      errors.add(NAME_IS_EMPTY);
    }
    if (name != null && !name.matches(NAME_REGEX)) {
      errors.add(NAME_IS_INVALID);
    }
    String description = certificateDto.getDescription();
    if (description == null) {
      errors.add(DESCRIPTION_IS_EMPTY);
    }
    if (description != null && !description.matches(DESCRIPTION_REGEX)) {
      errors.add(DESCRIPTION_IS_INVALID);
    }
    Integer duration = certificateDto.getDuration();
    if (duration == null) {
      errors.add(DURATION_IS_EMPTY);
    }
    if (duration != null && duration < 1) {
      errors.add(DURATION_IS_INVALID);
    }
    Double price = certificateDto.getPrice();
    if (price == null) {
      errors.add(PRICE_IS_EMPTY);
    }
    if (price != null && price < 0) {
      errors.add(PRICE_IS_INVALID);
    }
    errors.addAll(TagDtoValidator.tagsValidate(certificateDto.getTags()));
    errors = new HashSet<>(errors).stream().toList();
    return errors;
  }

  public static List<String> updateCertificateDtoValidate(CertificateDto certificateDto) {
    List<String> errors = new ArrayList<>(Collections.emptyList());
    String name = certificateDto.getName();
    if (name != null && !name.matches(NAME_REGEX)) {
      errors.add(NAME_IS_INVALID);
    }
    String description = certificateDto.getDescription();
    if (description != null && !description.matches(DESCRIPTION_REGEX)) {
      errors.add(DESCRIPTION_IS_INVALID);
    }
    Integer duration = certificateDto.getDuration();
    if (duration != null && duration < 1) {
      errors.add(DURATION_IS_INVALID);
    }
    Double price = certificateDto.getPrice();
    if (price != null && price < 0) {
      errors.add(PRICE_IS_INVALID);
    }
    errors.addAll(TagDtoValidator.tagsValidate(certificateDto.getTags()));
    errors = new HashSet<>(errors).stream().toList();
    return errors;
  }
}
