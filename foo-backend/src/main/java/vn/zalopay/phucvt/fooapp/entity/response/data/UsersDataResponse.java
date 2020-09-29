package vn.zalopay.phucvt.fooapp.entity.response.data;

import lombok.Builder;
import vn.zalopay.phucvt.fooapp.model.User;

import java.util.List;

@Builder
public class UsersDataResponse {
  private List<User> items;
}
