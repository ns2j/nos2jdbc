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
package org.seasar.framework.beans.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.IllegalDiiguRuntimeException;
import org.seasar.framework.beans.MethodNotFoundRuntimeException;
import org.seasar.framework.beans.PropertyDesc;

/**
 * @author higa
 * @author manhole
 */
class BeanDescImplTest {

    /**
     * @throws Exception
     */
    @Test
    void testPropertyDesc() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertEquals(5, beanDesc.getPropertyDescSize());
        PropertyDesc propDesc = beanDesc.getPropertyDesc("aaa");
        assertEquals("aaa", propDesc.getPropertyName());
        assertEquals(String.class, propDesc.getPropertyType());
        assertNotNull(propDesc.getReadMethod());
        assertNull(propDesc.getWriteMethod());
        assertNotNull(propDesc.getField());
        propDesc = beanDesc.getPropertyDesc("CCC");
        assertEquals("CCC", propDesc.getPropertyName());
        assertEquals(boolean.class, propDesc.getPropertyType());
        assertNotNull(propDesc.getReadMethod());
        assertNull(propDesc.getWriteMethod());
        propDesc = beanDesc.getPropertyDesc("eee");
        assertEquals("eee", propDesc.getPropertyName());
        assertEquals(String.class, propDesc.getPropertyType());
        assertNotNull(propDesc.getReadMethod());
        assertNotNull(propDesc.getWriteMethod());
        propDesc = beanDesc.getPropertyDesc("fff");
        assertEquals("fff", propDesc.getPropertyName());
        assertEquals(Boolean.class, propDesc.getPropertyType());
        assertFalse(beanDesc.hasPropertyDesc("hhh"));
        assertFalse(beanDesc.hasPropertyDesc("iii"));
    }

    /**
     * @throws Exception
     */
    @Test
    void testInvoke() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertEquals(new Integer(3), beanDesc.invoke(new MyBean(), "add", new Object[] { new Integer(1), new Integer(2) }), "1");
    }

    /**
     * @throws Exception
     */
    @Test
    void testInvoke2() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertEquals(new Integer(3), beanDesc.invoke(new MyBean(), "add2", new Object[] { new BigDecimal(1), new BigDecimal(2) }), "1");
    }

    /**
     * @throws Exception
     */
    @Test
    void testInvoke3() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(Math.class);
        assertEquals(new Integer(3), beanDesc.invoke(null, "max", new Object[] { new Integer(1), new Integer(3) }), "1");
        assertEquals(new Long(3), beanDesc.invoke(null, "max", new Object[] { new Long(1), new Long(3) }), "2");
    }

    /**
     * @throws Exception
     */
    @Test
    void testInvoke4() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(Math.class);
        assertEquals(new Double(3), beanDesc.invoke(null, "ceil", new Object[] { new BigDecimal(2.1) }), "1");
    }

    /**
     * @throws Exception
     */
    @Test
    void testInvoke5() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertEquals(new Integer("3"), beanDesc.invoke(new MyBean(), "echo", new Object[] { new Double("3") }), "1");
    }

    /**
     * @throws Exception
     */
    @Test
    void testInvokeForException() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        try {
            beanDesc.invoke(new MyBean(), "throwException", null);
            fail("1");
        } catch (IllegalStateException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void testNewInstance() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(Integer.class);
        Integer i = new Integer(10);
        Object[] args = new Object[] { i };
        assertEquals(i, beanDesc.newInstance(args), "1");
        Object[] args2 = new Object[] { "10" };
        assertEquals(i, beanDesc.newInstance(args2), "2");
    }

    /**
     * @throws Exception
     */
    @Test
    void testNewInstance2() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(Integer.class);
        BigDecimal d = new BigDecimal(10);
        Object[] args = new Object[] { d };
        assertEquals(new Integer(10), beanDesc.newInstance(args), "1");
    }

    /**
     * @throws Exception
     */
    @Test
    void testGetFields() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertTrue(beanDesc.hasField("HOGE"), "1");
        Field field = beanDesc.getField("HOGE");
        assertEquals("hoge2", field.get(null), "2");
        assertTrue(beanDesc.hasField("aaa"), "3");
        assertFalse(beanDesc.hasField("aaA"), "4");
    }

    /**
     * @throws Exception
     */
    @Test
    void testHasMethod() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertTrue(beanDesc.hasMethod("getAaa"));
        assertFalse(beanDesc.hasMethod("getaaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    void testGetMethod() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        Method method = beanDesc.getMethod("getAaa", new Class[0]);
        assertNotNull(method);
        assertEquals("getAaa", method.getName());
        try {
            beanDesc.getMethod("getaaa", null);
            fail();
        } catch (MethodNotFoundRuntimeException expected) {
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void testGetMethodNoException() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        Method method = beanDesc.getMethodNoException("getAaa", new Class[0]);
        assertNotNull(method);
        assertEquals("getAaa", method.getName());
        method = beanDesc.getMethodNoException("getaaa", new Class[0]);
        assertNull(method);
    }

    /**
     * @throws Exception
     */
    @Test
    void testGetMethodNames() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(getClass());
        String[] names = beanDesc.getMethodNames();
        for (int i = 0; i < names.length; ++i) {
            System.out.println(names[i]);
        }
        assertTrue(names.length > 0, "1");
    }

    /**
     * @throws Exception
     */
    @Test
    void testInvalidProperty() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean2.class);
        assertEquals(false, beanDesc.hasPropertyDesc("aaa"), "1");
    }

    /**
     * @throws Exception
     */
    @Test
    void testAddFields() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        Field eee = beanDesc.getField("eee");
        assertTrue(eee.isAccessible());
        PropertyDesc pd = beanDesc.getPropertyDesc("ggg");
        assertNotNull(pd);
        assertEquals("ggg", pd.getPropertyName());
        assertEquals(String.class, pd.getPropertyType());
    }

    /**
     * @throws Exception
     */
    @Test
    void testGetMethodParameterNames() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        Method m = beanDesc.getMethod("echo", new Class[] { Integer.class });
        try {
            beanDesc.getMethodParameterNames(m);
            fail();
        } catch (IllegalDiiguRuntimeException e) {
            assertTrue(true);
        }
    }

    /**
     * 
     */
    public static interface MyInterface {

        /**
         * 
         */
        String HOGE = "hoge";
    }

    /**
     * 
     */
    public static interface MyInterface2 extends MyInterface {

        /**
         * 
         */
        String HOGE = "hoge2";
    }

    /**
     * 
     */
    public static class MyBean implements MyInterface2 {

        private String aaa;

        private String eee;

        /**
         * 
         */
        public String ggg;

        /**
         * @return
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * @param a
         * @return
         */
        public String getBbb(Object a) {
            return null;
        }

        /**
         * @return
         */
        public boolean isCCC() {
            return true;
        }

        /**
         * @return
         */
        public Object isDdd() {
            return null;
        }

        /**
         * @return
         */
        public String getEee() {
            return eee;
        }

        /**
         * @param eee
         */
        public void setEee(String eee) {
            this.eee = eee;
        }

        /**
         * @return
         */
        public Boolean isFff() {
            return null;
        }

        /**
         * @param hhh
         * @return
         */
        public MyBean setHhh(String hhh) {
            return this;
        }

        /**
         * 
         */
        public void getIii() {
        }

        /**
         * @param arg1
         * @param arg2
         * @return
         */
        public Number add(Number arg1, Number arg2) {
            return new Integer(3);
        }

        /**
         * @param arg1
         * @param arg2
         * @return
         */
        public int add2(int arg1, int arg2) {
            return arg1 + arg2;
        }

        /**
         * @param arg
         * @return
         */
        public Integer echo(Integer arg) {
            return arg;
        }

        /**
         * 
         */
        public void throwException() {
            throw new IllegalStateException("hoge");
        }
    }

    /**
     * 
     */
    public class MyBean2 {

        /**
         * 
         */
        public MyBean2() {
        }

        /**
         * @param num
         * @param text
         * @param bean1
         * @param bean2
         */
        public MyBean2(int num, String text, MyBean bean1, MyBean2 bean2) {
        }

        /**
         * @param i
         */
        public void setAaa(int i) {
        }

        /**
         * @param s
         */
        public void setAaa(String s) {
        }
    }

    /**
     * 
     */
    public static class MyBean3 {

        /**
         * 
         */
        public MyBean3() {
        }

        /**
         * @param num
         * @param text
         * @param bean1
         * @param bean2
         */
        public MyBean3(int num, String text, MyBean bean1, MyBean2 bean2) {
        }

        /**
         * @param foo$bar
         * @param hoge$hoge$hoge
         */
        public static void foo(MyBean foo$bar, MyBean2 hoge$hoge$hoge) {
        }
    }
}
