package org.easystogu.utils;

import java.math.BigDecimal;

public class Strings {

    public static boolean isEmpty(String string) {
        return (string == null) || (string.isEmpty());
    }

    public static boolean isNotEmpty(String string) {
        return (string != null) && (!string.isEmpty());
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static double getDecimalWith2Scale(double num) {
        if (Double.isNaN(num)) {
            return 0;
        }
        BigDecimal bd = new BigDecimal(num);
        num = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return num;
    }
}
