package com.se.focusclock.utils;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

public class CheckUtils {
    private static CheckUtils cu = null;

    private CheckUtils() {
    }

    public static CheckUtils getInstance() {
        if (cu == null) {
            cu = new CheckUtils();
        }
        return cu;
    }

    /**
     * 判断对象是否为空，如果是数值型，null/0为true；如果是String类型，null/"null"/""为true；如果是Collection，null/无数据为true
     * @param obj 被判断的对象
     * @return true/false(空/非空)
     */
    public static boolean isEmpty(Object obj) {
        if (null == obj) {
            return true;
        }
        if(obj instanceof Integer) {
            return ((Integer)obj).equals(0);
        } else if(obj instanceof Long) {
            return ((Long)obj).equals(0L);
        } else if (obj instanceof String) {
            return "NULL".equals(obj.toString().trim().toUpperCase())
                    || "".equals(obj.toString().trim());
        } else if (obj instanceof Collection) {
            return ((Collection<?>) obj).size() == 0;
        } else if (obj instanceof Map) {
            return ((Map<?, ?>) obj).size() == 0;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else {
            return false;
        }
    }

    /**
     * 验证字符串是否超出length长度
     * @param resource 字符串
     * @param length 最大长度
     * @return true/false(超出/不超出)
     */
    public static boolean outLength(String resource, int length) {
        return resource.getBytes(StandardCharsets.UTF_8).length <= length;
    }

    /**
     * 验证字符串是否是由数字组成
     * @param str 验证的字符串
     * @return true/false(是/否 )
     */
    public static boolean isNumeric(String str) {
        return str.matches("\\d+(\\.?\\d+)?");
    }
}
