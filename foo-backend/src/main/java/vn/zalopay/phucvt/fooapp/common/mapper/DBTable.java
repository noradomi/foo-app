package vn.zalopay.phucvt.fooapp.common.mapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Created by thinhda. Date: 2019-11-07 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DBTable {
  String columnName();
}
