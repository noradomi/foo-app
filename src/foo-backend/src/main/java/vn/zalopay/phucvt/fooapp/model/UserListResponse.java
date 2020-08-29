package vn.zalopay.phucvt.fooapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserListResponse {
    List<UserListItem> items;

    public UserListResponse() {
        this.items = items = new ArrayList<>();
    }
}
