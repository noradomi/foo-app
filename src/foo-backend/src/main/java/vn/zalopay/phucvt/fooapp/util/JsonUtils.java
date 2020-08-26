package vn.zalopay.phucvt.fooapp.util;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

public class JsonUtils {
    public static String successResponse(Object message){
        JsonObject objectResult = new JsonObject(Json.encodePrettily(message));
        JsonObject object = new JsonObject().put("data",objectResult);
        return Json.encodePrettily(object);
    }

    public static String successResponse(String message){
        JsonObject object = new JsonObject().put("data",message);
        return Json.encodePrettily(object);
    }

    public static String errorResponse(Object message){
        JsonObject objectResult = new JsonObject(Json.encodePrettily(message));
        JsonObject object = new JsonObject().put("error",objectResult);
        return Json.encodePrettily(object);
    }

    public static String errorResponse(String message){
        JsonObject object = new JsonObject().put("error",message);
        return Json.encodePrettily(object);
    }
}
