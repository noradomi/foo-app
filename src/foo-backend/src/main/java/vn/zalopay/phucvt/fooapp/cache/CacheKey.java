package vn.zalopay.phucvt.fooapp.cache;

public class CacheKey {
  private static final String PREFIX = "ZALOPAY:FOOAPP:";
  private static final String PREFIX_USER = PREFIX + "ONLINE-USERS:";
  private static final String PREFIX_USERS = PREFIX + "USERS";
  private static final String PREFIX_BLACKLIST = PREFIX + "BLACKLIST:";

  public static String getUserKey(String userId) {
    return PREFIX_USER + userId;
  }
  public static String getBlacklistKey(String token) {
    return  PREFIX_BLACKLIST + token;
  }
  public static String getUserListKey() {
    return  PREFIX_USERS;
  }
}
