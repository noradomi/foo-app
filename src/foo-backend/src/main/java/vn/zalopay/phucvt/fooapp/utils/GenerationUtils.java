package vn.zalopay.phucvt.fooapp.utils;

import java.util.UUID;

public class GenerationUtils {
    private GenerationUtils(){};

    public static String generateId(){
        return UUID.randomUUID().toString();
    }
}
