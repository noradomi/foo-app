package vn.zalopay.phucvt.fooapp.cache;

import vn.zalopay.phucvt.fooapp.model.WsMessage;

public class CacheKey {
  private static final String PREFIX = "ZALOPAY:FOOAPP:";
  private static final String PREFIX_USER_INFO = PREFIX + "USER:";
  private static final String PREFIX_STATUS_USER = PREFIX + "USER:";
  private static final String PREFIX_USERS = PREFIX + "USERS";
  private static final String PREFIX_BLACKLIST = PREFIX + "BLACKLIST:";
  private static final String PREFIX_MESSAGE = PREFIX + "MESSAGE:";

  public static String getUserKey(String userId) {
    return PREFIX_USER_INFO + userId + ":INFO";
  }
  public static String getBlacklistKey(String token) {
    return  PREFIX_BLACKLIST + token;
  }
  public static String getMessageKey(String firstUserId,String secondUserId) {
    if(firstUserId.compareTo(secondUserId) < 0)
      return  PREFIX_MESSAGE + firstUserId + secondUserId;
    else
      return PREFIX_MESSAGE + secondUserId + firstUserId;
  }
  public static String getUserListKey() {
    return  PREFIX_USERS;
  }

  public static String getUserStatusKey(String userId){
    return PREFIX_STATUS_USER + userId + ":STATUS";
  }
}
