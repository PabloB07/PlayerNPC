/*
 * Decompiled with CFR 0.152.
 */
package dev.sergiferry.playernpc.utils;

import java.util.List;

public class StringUtils {
    public static String getFirstCharUpperCase(String s, boolean force_low) {
        if (force_low) {
            s = s.toLowerCase();
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        int length = searchStr.length();
        if (length == 0) {
            return true;
        }
        for (int i = str.length() - length; i >= 0; --i) {
            if (!str.regionMatches(true, i, searchStr, 0, length)) continue;
            return true;
        }
        return false;
    }

    public static boolean containsIgnoreCase(List<String> strings, String searchStr) {
        for (String string : strings) {
            if (!StringUtils.containsIgnoreCase(string, searchStr)) continue;
            return true;
        }
        return false;
    }
}

