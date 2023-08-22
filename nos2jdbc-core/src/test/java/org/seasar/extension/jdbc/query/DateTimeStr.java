package org.seasar.extension.jdbc.query;

public class DateTimeStr {
    private static String DATE = "([0-9]{4}-[0-9]{2}-[0-9]{2})";
    private static String TIME = "([0-9]{2}:[0-9]{2}:[0-9]{2}[0-9\\.+:]*)";
    public static String REPLACE = "REPLACED";
    
    public static String replace(String str) {
        return str.replaceAll("'" + DATE + "[T ]" + TIME + "'", "'" + REPLACE + "'");
    }
}
