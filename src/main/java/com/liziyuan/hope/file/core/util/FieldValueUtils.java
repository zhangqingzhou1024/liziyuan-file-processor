package com.liziyuan.hope.file.core.util;


import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 一些常用方法util
 *
 * @author zqz
 * @version 1.0
 * @date 2020-04-14 21:55
 */
public class FieldValueUtils {

    /**
     * 判断是否为空的形式
     *
     * @param object Object 对象
     * @return 是否为空
     */
    public static boolean isNullType(Object object) {
        if (null == object) {
            return true;
        }
        // 如果是字符串，要判断长度是否为空
        if (object instanceof String) {
            return ((String) object).trim().length() == 0;
        }
        if (object instanceof Map) {
            return CollectionUtils.isEmpty(((Map) object));
        }

        if (object instanceof List) {
            return CollectionUtils.isEmpty((List) object);
        }

        if (object instanceof Set) {
            return CollectionUtils.isEmpty(((Set) object));
        }

        return false;
    }

    /**
     * 判断全部参数是否为空
     *
     * @param object 可变参数Object
     * @return 是否为空
     */
    public static boolean isAllNull(Object... object) {
        if (isNullType(object)) {
            return true;
        }
        // 只要有一个非空即为false
        for (Object o : object) {
            if (!isNullType(o)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断至少有一个参数是否为空
     *
     * @param object 可变参数Object
     * @return 至少有一个参数是否为空
     */
    public static boolean isHaveNull(Object... object) {
        if (isNullType(object)) {
            return true;
        }

        // 有一个为空即为true
        for (Object o : object) {
            if (isNullType(o)) {
                return true;
            }
        }

        return false;
    }


    /**
     * 判断字符是否满足正则表达式
     *
     * @param source 源字符串
     * @param regex  正则表达式
     * @return 是否满足正则表达式
     */
    public static boolean isMatchRegex(String source, String regex) {
        return Pattern.matches(regex, source);
    }


    /**
     * 判断是不是date 类型
     *
     * @param obj obj
     * @return 判断是不是date 类型
     */
    public static boolean isDateType(Object obj) {
        if (null == obj) {
            return false;
        }
        return obj instanceof Date;
    }


    /**
     * 去除多个空格，只保留一个空格
     *
     * @param sourceStr 元字符
     * @return 去除多个空格，只保留一个空格
     */
    public static String removeMoreBlanks(String sourceStr) {
        if (isNullType(sourceStr)) {
            return null;
        }

        return sourceStr.replaceAll(" +", " ");
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString().substring(10));
    }

}
