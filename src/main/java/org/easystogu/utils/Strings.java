package org.easystogu.utils;

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
}
