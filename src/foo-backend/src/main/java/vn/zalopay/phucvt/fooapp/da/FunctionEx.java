package vn.zalopay.phucvt.fooapp.da;

/** Created by thinhda. Date: 2019-11-08 */
@FunctionalInterface
public interface FunctionEx<T, R> {
  R apply(T t) throws Exception;
}
