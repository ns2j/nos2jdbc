/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.extension.dataset.types;

import java.sql.Timestamp;
import java.util.Calendar;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.framework.util.CalendarConversionUtil;
import org.seasar.framework.util.DateConversionUtil;
import org.seasar.framework.util.SqlDateConversionUtil;
import org.seasar.framework.util.TimeConversionUtil;
import org.seasar.framework.util.TimestampConversionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日付用の {@link ColumnType}です。
 * 
 * @author higa
 * 
 */
public class TimestampType extends ObjectType {
    final static private Logger logger = LoggerFactory.getLogger(TimestampType.class);

    TimestampType() {
    }

    public Object convert(String value, String formatPattern) {
        if (value.contains(" "))
            return TimestampConversionUtil.toTimestamp(value, "yyyy/MM/dd HH:mm:ss");
        else
            return TimestampConversionUtil.toTimestamp(value, formatPattern);
    }
    @Override
    public Object convert(Object value, String formatPattern) {
        if (value == null) return null;
        if (!(value instanceof String))
            return TimestampConversionUtil.toTimestamp(value, formatPattern);
        return convert((String)value, formatPattern);
    }

    @Override
    public Object convert(Class<?> clazz, Object value) {
        if (clazz == java.sql.Date.class)
            return SqlDateConversionUtil.toDate(value);
        if (clazz == java.sql.Timestamp.class)
            return TimestampConversionUtil.toTimestamp(value);
        if (clazz == java.sql.Time.class)
            return TimeConversionUtil.toTime(value);
        if (clazz == java.util.Date.class)
            return DateConversionUtil.toDate(value);
        if (clazz == Calendar.class)
            return CalendarConversionUtil.toCalendar(value);
        
        return value;
    }

    @Override
    public Class getType() {
        return Timestamp.class;
    }
}