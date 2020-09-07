package vn.zalopay.phucvt.fooapp.config;

public class APIPath {
  private static final String PUBLIC_API = "/api/public/";
  private static final String PROTECTED = "/api/protected/";
  public static final String LOGIN = PUBLIC_API + "login";
  public static final String SIGNUP = PUBLIC_API + "signup";
  public static final String LOGOUT = PROTECTED + "logout";
  public static final String USER_LIST = PROTECTED + "users";
  public static final String MESSAGE_LIST = PROTECTED + "message/:friendId/:offset";
}
