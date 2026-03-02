/*
 * Decompiled with CFR 0.152.
 */
package dev.sergiferry.playernpc.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MathUtils {
    public static String getFormat(double d) {
        return MathUtils.getFormat(d, 2);
    }

    public static String getFormat(double d, int decimals) {
        return MathUtils.getDecimalFormat(decimals).format(d);
    }

    public static String getSimpleFormat(double d, int decimals) {
        return MathUtils.getSimpleDecimalFormat(decimals).format(d);
    }

    public static String getFormat(int i) {
        return MathUtils.getFormat(i, 2);
    }

    public static String getFormat(int i, int decimals) {
        return MathUtils.getDecimalFormat(decimals).format(i);
    }

    public static DecimalFormat getDecimalFormat(int decimals) {
        DecimalFormat formatea = new DecimalFormat("#,###,##0.00");
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        formatea.setDecimalFormatSymbols(otherSymbols);
        formatea.setRoundingMode(RoundingMode.FLOOR);
        formatea.setMinimumFractionDigits(0);
        formatea.setMaximumFractionDigits(decimals);
        return formatea;
    }

    public static DecimalFormat getSimpleDecimalFormat(int decimals) {
        DecimalFormat formatea = new DecimalFormat("0.00");
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setDecimalSeparator('.');
        formatea.setDecimalFormatSymbols(otherSymbols);
        formatea.setRoundingMode(RoundingMode.FLOOR);
        formatea.setMinimumFractionDigits(0);
        formatea.setMaximumFractionDigits(decimals);
        return formatea;
    }

    public static boolean isInteger(Object object) {
        try {
            Integer.parseInt(object.toString());
            return true;
        }
        catch (Exception exc) {
            return false;
        }
    }

    public static boolean isDouble(Object object) {
        try {
            Double.parseDouble(object.toString());
            return true;
        }
        catch (Exception exc) {
            return false;
        }
    }
}

