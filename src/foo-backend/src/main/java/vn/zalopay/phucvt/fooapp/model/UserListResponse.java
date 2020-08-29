package vn.zalopay.phucvt.fooapp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserListResponse {
    List<UserListItem> items;
}
