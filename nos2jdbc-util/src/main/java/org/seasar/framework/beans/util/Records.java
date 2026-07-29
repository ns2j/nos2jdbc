package org.seasar.framework.beans.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.IllegalPropertyRuntimeException;
import org.seasar.framework.beans.NotRecordException;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.ClassUtil;

public class Records {
    private static Map<Class<?>, Object> defaultValueMap = new HashMap<>();

    static {
        defaultValueMap.put(char.class, Character.valueOf((char) 0));
        defaultValueMap.put(byte.class, Byte.valueOf((byte) 0));
        defaultValueMap.put(short.class, Short.valueOf((short) 0));
        defaultValueMap.put(int.class, Integer.valueOf(0));
        defaultValueMap.put(long.class, Long.valueOf(0L));
        defaultValueMap.put(double.class, Double.valueOf(0.0d));
        defaultValueMap.put(float.class, Float.valueOf(0.0f));
        defaultValueMap.put(boolean.class, Boolean.FALSE);
    }

    @SuppressWarnings("unchecked")
    public static <T> T createRecord(Class<T> recordClass, Object src) {
        if (src == null) {
            return null;
        }
        if (!recordClass.isRecord())
            throw new NotRecordException(recordClass);
        BeanDesc bd = BeanDescFactory.getBeanDesc(src.getClass());

        RecordComponent[] rcs = recordClass.getRecordComponents();

        Constructor<?>[] cs = recordClass.getDeclaredConstructors();
        Object[] args = new Object[rcs.length];
        for (int i = 0; i < rcs.length; i++) {
            RecordComponent rc = rcs[i];
            if (bd.hasPropertyDesc(rc.getName())) {
                PropertyDesc pd = bd.getPropertyDesc(rc.getName());
                if (List.class.isAssignableFrom(rc.getType())) {
                    ParameterizedType pt = (ParameterizedType) rc.getGenericType();
                    Type ts = pt.getActualTypeArguments()[0];
                    Class<?> c = null;
                    try {
                        ClassLoader cl = Thread.currentThread().getContextClassLoader();
                        if (cl == null)
                            cl = Records.class.getClassLoader();
                        c = Class.forName(ts.getTypeName(), true, cl);

                    } catch (ClassNotFoundException | IllegalStateException e) {
                        throw new RuntimeException(e);
                    }
                    if (c.isRecord()) {
                        args[i] = createRecordList(c, (List<Object>) pd.getValue(src));
                    } else {
                        args[i] = pd.getValue(src);
                    }
                } else if (rc.getType().isRecord()) {
                    args[i] = createRecord(rc.getType(), pd.getValue(src));
                } else {
                    args[i] = pd.getValue(src);
                }
            } else {
                if (rc.getType().isPrimitive()) {
                    args[i] = defaultValueMap.get(rc.getType());
                } else {
                    args[i] = null;
                }
            }
        }
        try {
            Class<?>[] paramTypes = new Class[rcs.length];
            for (int i = 0; i < rcs.length; i++) {
                paramTypes[i] = rcs[i].getType();
            }
            Constructor<T> c = recordClass.getDeclaredConstructor(paramTypes);
            c.setAccessible(true);
            return c.newInstance(args);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> createRecordList(Class<T> recordClass, List<?> src) {
        List<T> list = new ArrayList<>();
        if (src == null) {
            return list;
        }
        for (Object o : src) {
            T r = createRecord(recordClass, o);
            list.add(r);
        }
        return list;
    }

    public static <T> void copyToBean(T recObj, Object target) {
        if (recObj == null || target == null) {
            return;
        }
        Class<?> recordClass = recObj.getClass();
        if (!recordClass.isRecord())
            throw new NotRecordException(recordClass);
        BeanDesc bd = BeanDescFactory.getBeanDesc(target.getClass());
        for (int i = 0; i < bd.getPropertyDescSize(); i++) {
            System.out.println(bd.getPropertyDesc(i));
        }

        RecordComponent[] rcs = recordClass.getRecordComponents();
        for (int i = 0; i < rcs.length; i++) {
            RecordComponent rc = rcs[i];
            if (bd.hasPropertyDesc(rc.getName())) {
                PropertyDesc pd = bd.getPropertyDesc(rc.getName());
                try {
                    Field f = recordClass.getDeclaredField(rc.getName());
                    f.setAccessible(true);
                    Object val = f.get(recObj);
                    if (val != null && val.getClass().isRecord() && !pd.getPropertyType().isRecord()) {
                        Object nestedBean = ClassUtil.newInstance(pd.getPropertyType());
                        copyToBean(val, nestedBean);
                        pd.setValue(target, nestedBean);
                    } else {
                        pd.setValue(target, val);
                    }
                } catch (NoSuchFieldException | SecurityException | IllegalPropertyRuntimeException
                        | IllegalStateException | IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
