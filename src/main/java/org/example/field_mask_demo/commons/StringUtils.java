package org.example.field_mask_demo.commons;

public class StringUtils {
    private StringUtils() {
        //no instance
    }

    public static String getNotNull(String str) {
        return str == null ? "" : str;
    }
}
