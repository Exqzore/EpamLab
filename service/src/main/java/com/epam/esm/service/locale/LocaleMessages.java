package com.epam.esm.service.locale;

public enum LocaleMessages {
  ID_NEGATIVE("id.negative"),
  ID_INVALID("id.invalid"),

  TAG_NOT_FOUND("tag.not_found"),
  TAG_NOT_FOUND_BY_NAME("tag.not_found.by_name"),
  TAG_NOT_FOUND_WITHOUT_ID("tag.not_found.without_id"),
  TAG_NOT_CREATE("tag.not_create"),
  TAG_NAME_EMPTY("tag.name.empty"),
  TAG_NAME_INVALID("tag.name.invalid"),
  TAG_ALL_PARAMS("tag.all_params"),
  TAG_EMPTY("tag.empty"),
  TAG_DELETED("tag.deleted"),
  TAG_NOT_DELETED("tag.not_deleted"),

  CERTIFICATE_ID_EMPTY("certificate.id.empty"),
  CERTIFICATE_NOT_FOUND("certificate.not_found"),
  CERTIFICATE_NAME_EMPTY("certificate.name.empty"),
  CERTIFICATE_NAME_INVALID("certificate.name.invalid"),
  CERTIFICATE_DESCRIPTION_EMPTY("certificate.description.empty"),
  CERTIFICATE_DESCRIPTION_INVALID("certificate.description.invalid"),
  CERTIFICATE_PRICE_EMPTY("certificate.price.empty"),
  CERTIFICATE_PRICE_NEGATIVE("certificate.price.negative"),
  CERTIFICATE_DURATION_EMPTY("certificate.duration.empty"),
  CERTIFICATE_DURATION_NEGATIVE("certificate.duration.negative"),
  CERTIFICATE_DELETED("certificate.deleted"),
  CERTIFICATE_NOT_DELETED("certificate.not_deleted"),
  CERTIFICATE_NOT_CREATE("certificate.not_create"),
  CERTIFICATE_NOT_UPDATE("certificate.not_update"),

  USER_NOT_FOUND("user.not_found"),
  USER_NAME_EMPTY("user.name.empty"),
  USER_NAME_INVALID("user.name.invalid"),
  USER_SURNAME_EMPTY("user.surname.empty"),
  USER_SURNAME_INVALID("user.surname.invalid"),

  ORDER_NOT_FOUND("order.not_found"),
  ORDER_NOT_CREATE("order.not_create"),
  ORDER_EMPTY_CERTIFICATE("order.empty_certificate"),
  ORDER_DELETED("order.deleted"),
  ORDER_NOT_DELETED("order.not_deleted"),

  PAGE_INVALID("page.invalid"),
  SIZE_INVALID("size.invalid"),

  UNSUPPORTED_MEDIA_TYPE("unsupported_media_type"),
  NOT_FOUND("not_found"),
  PAGE_NOT_FOUND("page.not_found"),
  TYPE_MISMATCH("type_mismatch"),
  VALIDATION_UNDEFINED_ERROR("validation.undefined_error"),
  NOT_FOUND_UNDEFINED_ERROR("not_found.undefined_error"),
  CREATION_UNDEFINED_ERROR("creation.undefined_error"),
  UPDATE_UNDEFINED_ERROR("update.undefined_error");

  private final String propertyPath;

  LocaleMessages(String propertyPath) {
    this.propertyPath = propertyPath;
  }

  public String getPropertyPath() {
    return propertyPath;
  }
}
