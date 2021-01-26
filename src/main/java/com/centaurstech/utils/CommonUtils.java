package com.centaurstech.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @author: TTC
 * @date: 2020/10/13 10:42
 * @desc: ...
 **/
public class CommonUtils {
    public static boolean stringIsEmptyOrNull(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean stringNotEmptyOrNull(String s) {
        return !stringIsEmptyOrNull(s);
    }

    public static boolean isCollectionEmptyOrNull(Collection targetCollection) {
        return targetCollection == null || targetCollection.size() <= 0;
    }

    public static boolean isCollectionEmptyOrNull(Map map) {
        return map == null || map.size() <= 0;
    }
}
