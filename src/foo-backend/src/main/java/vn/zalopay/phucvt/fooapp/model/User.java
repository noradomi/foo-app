package vn.zalopay.phucvt.fooapp.model;

import vn.zalopay.phucvt.fooapp.common.mapper.DBTable;
import lombok.*;

import java.util.UUID;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @DBTable(columnName = "id")
    String userId;

    @DBTable(columnName = "username")
    String username;

    @DBTable(columnName = "password")
    String password;

    @DBTable(columnName = "fullname")
    String fullname;

    @DBTable(columnName = "online")
    boolean online;
}
