package com.epam.esm.constant;

/** The http response states interface. */
public interface CustomHttpStatus {
  int UNSUPPORTED_MEDIA_TYPE = 41500;

  /** The error code of not finding the resource. */
  interface NOT_FOUND {
    int UNDEFINED = 40400;
    int TAG = 40401;
    int CERTIFICATE = 40402;
  }

  /** The error code of bad request. */
  interface BAD_REQUEST {
    int UNDEFINED = 40000;
    int TAG = 40001;
    int CERTIFICATE = 40002;
  }

  /** The error code of conflict. */
  interface CONFLICT {
    int UNDEFINED = 40900;
    int TAG = 40901;
    int CERTIFICATE = 40902;
  }
}
