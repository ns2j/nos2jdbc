package org.seasar.extension.jdbc.util;

import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Date;

import org.seasar.extension.jdbc.PropertyMeta;

import jakarta.persistence.TemporalType;

public class TimestampUtil {
    public static Object getTimestamp(PropertyMeta pm) {
        Class<?> c = pm.getPropertyClass();
        if (c == OffsetDateTime.class) return OffsetDateTime.now();
        if (pm.getTemporalType() == TemporalType.TIMESTAMP) {
            if (c == Date.class)
                return new Date();
            if (c == Calendar.class)
                return Calendar.getInstance();
        }
        return null;
    }
}
