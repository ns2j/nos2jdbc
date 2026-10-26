package org.seasar.framework.beans.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
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

    /**
     * Beanからrecordを生成します。
     * <p>
     * recordのコンポーネントと同名のプロパティがBeanにあれば値をコピーします。
     * コンポーネントがrecord、またはrecordを要素とするListの場合は、再帰的にrecordへ変換します。
     * Beanに対応するプロパティがないコンポーネントは、null(プリミティブ型は0/false)になります。
     */
    @SuppressWarnings("unchecked")
    public static <T> T createRecord(Class<T> recordClass, Object src) {
        if (src == null) {
            return null;
        }
        if (!recordClass.isRecord())
            throw new NotRecordException(recordClass);
        BeanDesc bd = BeanDescFactory.getBeanDesc(src.getClass());

        RecordComponent[] rcs = recordClass.getRecordComponents();

        Object[] args = new Object[rcs.length];
        for (int i = 0; i < rcs.length; i++) {
            RecordComponent rc = rcs[i];
            if (!bd.hasPropertyDesc(rc.getName())) {
                args[i] = rc.getType().isPrimitive() ? defaultValueMap.get(rc.getType()) : null;
                continue;
            }
            PropertyDesc pd = bd.getPropertyDesc(rc.getName());
            Object value = pd.getValue(src);
            if (List.class.isAssignableFrom(rc.getType())) {
                // List<record> のときだけ要素を変換する。生のList、List<?>などは値をそのまま渡す。
                Class<?> elementType = elementClass(rc.getGenericType());
                if (elementType != null && elementType.isRecord()) {
                    args[i] = createRecordList(elementType, (List<Object>) value);
                } else {
                    args[i] = value;
                }
            } else if (rc.getType().isRecord()) {
                args[i] = createRecord(rc.getType(), value);
            } else {
                args[i] = value;
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

    /**
     * recordの値をBeanにコピーします。nullも含めてコピーします。
     *
     * @see #copyToBean(Object, Object, boolean)
     */
    public static <T> void copyToBean(T recObj, Object target) {
        copyToBean(recObj, target, false);
    }

    /**
     * recordの値をBeanにコピーします。
     * <p>
     * 値はフィールドではなくアクセサメソッド経由で読みます。アクセサをオーバーライドして
     * 値を補完しているrecordは、その補完後の値がコピーされます。
     * Beanに対応するプロパティがないコンポーネントは無視します。
     * <ul>
     * <li>コンポーネントの値がrecordで、Beanのプロパティがrecordでないとき: Beanを生成して再帰的にコピーします。</li>
     * <li>コンポーネントの値が、recordを要素とするListで、BeanのプロパティがList&lt;recordでない型&gt;のとき:
     * 要素ごとにBeanを生成してコピーします。要素の型が分からないときや、インターフェース・抽象クラス・Objectのときは、Listをそのまま渡します。</li>
     * </ul>
     *
     * @param skipNull
     *            trueのとき、値がnullのコンポーネントはコピーせず、Bean側の既存の値を残します
     */
    public static <T> void copyToBean(T recObj, Object target, boolean skipNull) {
        if (recObj == null || target == null) {
            return;
        }
        Class<?> recordClass = recObj.getClass();
        if (!recordClass.isRecord())
            throw new NotRecordException(recordClass);
        BeanDesc bd = BeanDescFactory.getBeanDesc(target.getClass());

        for (RecordComponent rc : recordClass.getRecordComponents()) {
            if (!bd.hasPropertyDesc(rc.getName())) {
                continue;
            }
            PropertyDesc pd = bd.getPropertyDesc(rc.getName());
            Object val = readComponent(rc, recObj);
            if (val == null) {
                if (!skipNull) {
                    pd.setValue(target, null);
                }
                continue;
            }
            pd.setValue(target, toBeanValue(val, pd, skipNull));
        }
    }

    private static Object readComponent(RecordComponent rc, Object recObj) {
        Method accessor = rc.getAccessor();
        try {
            accessor.setAccessible(true);
            return accessor.invoke(recObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object toBeanValue(Object val, PropertyDesc pd, boolean skipNull) {
        if (val.getClass().isRecord() && !pd.getPropertyType().isRecord()) {
            Object nestedBean = ClassUtil.newInstance(pd.getPropertyType());
            copyToBean(val, nestedBean, skipNull);
            return nestedBean;
        }
        if (val instanceof List<?> list && List.class.isAssignableFrom(pd.getPropertyType())) {
            Class<?> elementType = resolveElementType(pd);
            if (isInstantiableBean(elementType) && containsRecord(list)) {
                List<Object> beans = new ArrayList<>(list.size());
                for (Object e : list) {
                    if (e != null && e.getClass().isRecord()) {
                        Object bean = ClassUtil.newInstance(elementType);
                        copyToBean(e, bean, skipNull);
                        beans.add(bean);
                    } else {
                        beans.add(e);
                    }
                }
                return beans;
            }
        }
        return val;
    }

    private static boolean containsRecord(List<?> list) {
        for (Object e : list) {
            if (e != null && e.getClass().isRecord()) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInstantiableBean(Class<?> c) {
        return c != null && c != Object.class && !c.isRecord() && !c.isInterface()
                && !Modifier.isAbstract(c.getModifiers());
    }

    private static Class<?> resolveElementType(PropertyDesc pd) {
        Method writeMethod = pd.getWriteMethod();
        if (writeMethod != null && writeMethod.getGenericParameterTypes().length == 1) {
            return elementClass(writeMethod.getGenericParameterTypes()[0]);
        }
        Field field = pd.getField();
        if (field != null) {
            return elementClass(field.getGenericType());
        }
        return null;
    }

    /** List&lt;X&gt; の X を返す。Xがクラスまたはパラメータ化された型のときだけ。それ以外(生のList、?、型変数)はnull。 */
    private static Class<?> elementClass(Type type) {
        if (!(type instanceof ParameterizedType pt)) {
            return null;
        }
        Type[] args = pt.getActualTypeArguments();
        if (args.length != 1) {
            return null;
        }
        Type arg = args[0];
        if (arg instanceof Class<?> c) {
            return c;
        }
        if (arg instanceof ParameterizedType p && p.getRawType() instanceof Class<?> c) {
            return c;
        }
        return null;
    }
}
