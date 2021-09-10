package com.epam.esm.service.constant;

public enum CustomHttpStatus {
  UNSUPPORTED_MEDIA_TYPE(415000);

  private final int code;

  CustomHttpStatus(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public enum NotFound {
    UNDEFINED(404000),
    TAG(404100),
    CERTIFICATE(404200),
    ORDER(404300),
    USER(404400);

    private final int code;

    NotFound(int code) {
      this.code = code;
    }

    public int getCode() {
      return code;
    }
  }

  public enum BadRequest {
    UNDEFINED(400000),
    INVALID_ID(400001),
    NEGATIVE_ID(400002),
    INVALID_PAGE(400003),
    INVALID_SIZE(400004),

    TAG(400100),
    INVALID_TAG_NAME(400101),
    EMPTY_TAG_NAME(400102),
    TAG_ALL_PARAMS(400103),
    EMPTY_TAG(400104),

    CERTIFICATE(400200),
    INVALID_CERTIFICATE_NAME(400201),
    EMPTY_CERTIFICATE_NAME(400202),
    INVALID_CERTIFICATE_DESCRIPTION(400203),
    EMPTY_CERTIFICATE_DESCRIPTION(400204),
    INVALID_CERTIFICATE_PRICE(400205),
    EMPTY_CERTIFICATE_PRICE(400206),
    INVALID_CERTIFICATE_DURATION(400207),
    EMPTY_CERTIFICATE_DURATION(400208),
    EMPTY_CERTIFICATE_ID(400209),

    ORDER(400300),
    USER(400400),
    INVALID_USER_NAME(400401),
    EMPTY_USER_NAME(400402),
    INVALID_USER_SURNAME(400403),
    EMPTY_USER_SURNAME(400404);

    private final int code;

    BadRequest(int code) {
      this.code = code;
    }

    public int getCode() {
      return code;
    }
  }

  public enum Conflict {
    UNDEFINED(409000),
    TAG(409100),
    CERTIFICATE(409200),
    ORDER(409300),
    USER(409400);

    private final int code;

    Conflict(int code) {
      this.code = code;
    }

    public int getCode() {
      return code;
    }
  }
}
