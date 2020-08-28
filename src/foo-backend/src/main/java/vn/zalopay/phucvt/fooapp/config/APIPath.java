package vn.zalopay.phucvt.fooapp.config;

public class APIPath {
  private static final String PUBLIC_API = "/api/public/";
  private static final String PROTECTED = "/api/protected/";
  public static final String LOGIN = PUBLIC_API + "login";
  public static final String ECHO = PUBLIC_API + "echo";
  public static final String EXAMPLE = PUBLIC_API + "example";
  public static final String SIGNUP = PUBLIC_API + "signup";
  public static final String SIGNOUT = PROTECTED + "signout";
}
