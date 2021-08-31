package com.epam.esm.constant;

/** The http response states interface. */
public class CustomHttpStatus {
  public static int UNSUPPORTED_MEDIA_TYPE = 41500;

  /** The error code of not finding the resource. */
  public static class NOT_FOUND {
    public static int UNDEFINED = 40400;
    public static int TAG = 40401;
    public static int CERTIFICATE = 40402;
  }

  /** The error code of bad request. */
  public static class BAD_REQUEST {
    public static int UNDEFINED = 40000;
    public static int TAG = 40001;
    public static int CERTIFICATE = 40002;
  }

  /** The error code of conflict. */
  public static class  CONFLICT {
    public static int UNDEFINED = 40900;
    public static int TAG = 40901;
    public static int CERTIFICATE = 40902;
  }
}
