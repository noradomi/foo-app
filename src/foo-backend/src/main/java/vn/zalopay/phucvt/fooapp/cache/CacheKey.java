package vn.zalopay.phucvt.fooapp.cache;

public class CacheKey {
  private static final String PREFIX = "ZALOPAY:FOOAPP:";
  private static final String PREFIX_USER = PREFIX + "USERS:";
  private static final String BLACKLIST = PREFIX + "BLACKLIST";

  public static String getUserKey(String userId) {
    return PREFIX_USER + userId;
  }
  public static String getBlacklist() {
    return BLACKLIST;
  }
}
