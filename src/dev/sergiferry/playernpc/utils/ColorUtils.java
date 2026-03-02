/*
 * Decompiled with CFR 0.152.
 */
package dev.sergiferry.playernpc.utils;

import java.awt.Color;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;

public class ColorUtils {
    public static Color getMostCommonColour(Map map) {
        LinkedList list = new LinkedList(map.entrySet());
        if (list.size() == 0) {
            return null;
        }
        Collections.sort(list, new Comparator<Object>(){

            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable)((Map.Entry)o1).getValue()).compareTo(((Map.Entry)o2).getValue());
            }
        });
        Map.Entry me = (Map.Entry)list.get(list.size() - 1);
        int[] rgb = ColorUtils.getRGB((Integer)me.getKey());
        return new Color(rgb[0], rgb[1], rgb[2]);
    }

    public static int[] getRGB(int pixel) {
        int alpha = pixel >> 24 & 0xFF;
        int red = pixel >> 16 & 0xFF;
        int green = pixel >> 8 & 0xFF;
        int blue = pixel & 0xFF;
        return new int[]{red, green, blue};
    }

    public static Color getColorFromRGB(int[] rgb) {
        return new Color(rgb[0], rgb[1], rgb[2]);
    }

    public static boolean isGray(int[] rgbArr) {
        if (rgbArr[0] < 150 && rgbArr[1] < 150 && rgbArr[2] < 150) {
            return true;
        }
        if (rgbArr[0] > 220 && rgbArr[1] > 200) {
            return true;
        }
        int rgDiff = rgbArr[0] - rgbArr[1];
        int rbDiff = rgbArr[0] - rgbArr[2];
        int tolerance = 10;
        return rgDiff <= tolerance && rgDiff >= -tolerance || rbDiff <= tolerance && rbDiff >= -tolerance;
    }
}

