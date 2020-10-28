package vn.zalopay.phucvt.fooapp.common.mapper;

import com.google.common.collect.ImmutableMap;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import vn.zalopay.phucvt.fooapp.utils.JsonProtoUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** Created by thinhda. Date: 2019-11-07 */
public class EntityMapper {
  private static final Map<Class, Class> primitive2Object =
      ImmutableMap.<Class, Class>builder()
          .put(int.class, Integer.class)
          .put(long.class, Long.class)
          .put(double.class, Double.class)
          .put(float.class, Float.class)
          .put(boolean.class, Boolean.class)
          .put(byte.class, Byte.class)
          .put(char.class, Character.class)
          .put(short.class, Short.class)
          .put(String.class, String.class)
          .build();
  private static EntityMapper instance = new EntityMapper();

  private EntityMapper() {}

  public static EntityMapper getInstance() {
    return instance;
  }

  public static List<Field> getAllFields(Class<?> type) {
    List<Field> fields = new ArrayList<>(Arrays.asList(type.getDeclaredFields()));

    Class<?> supperClass = type.getSuperclass();
    if (supperClass != null) {
      fields.addAll(getAllFields(supperClass));
    }

    return fields;
  }

  public void loadResultSetIntoObject(ResultSet rst, Object object) throws Exception {
    Class<?> zclass = object.getClass();

    for (Field field : getAllFields(zclass)) {
      field.setAccessible(true);
      DBTable column = field.getAnnotation(DBTable.class);
      if (column == null) {
        continue;
      }

      Class<?> type = field.getType();
      Object value;
      if (isPrimitive(type)) {
        value = rst.getObject(column.columnName(), boxPrimitiveClass(type));
      } else if (isGrpcObject(type)) {
        String data = rst.getString(column.columnName());
        Class<?> barClass = Class.forName(type.getName());
        Method newBuilderMethod = barClass.getMethod("newBuilder");
        value = JsonProtoUtils.parse(data, (Message.Builder) newBuilderMethod.invoke(null));
      } else {
        String data = rst.getString(column.columnName());
        value = JsonProtoUtils.parseGson(data, type);
      }
      field.set(object, value);
    }
  }

  private boolean isPrimitive(Class<?> type) {
    return null != primitive2Object.get(type);
  }

  private boolean isGrpcObject(Class<?> type) {
    if (type.getSuperclass() == null) {
      return false;
    }
    return type.getSuperclass().isAssignableFrom(GeneratedMessageV3.class);
  }

  private Class<?> boxPrimitiveClass(Class<?> type) {
    Class<?> objectType = primitive2Object.get(type);
    if (null != objectType) {
      return objectType;
    } else {
      throw new IllegalArgumentException("class '" + type.getName() + "' is not a primitive");
    }
  }
}
