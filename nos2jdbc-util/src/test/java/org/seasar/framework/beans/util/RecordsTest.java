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
package org.seasar.framework.beans.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;


class RecordsTest {

    /**
     * @throws Exception
     */
    @Test
    void createRecord() throws Exception {
        MyClass src = new MyClass();
        src.setAaa("111");
        src.setCcc("333");
        MyRecord actual = Records.createRecord(MyRecord.class, src);
        assertEquals("111", actual.aaa());
        assertNull(actual.bbb());
        assertEquals("333", actual.ccc());
        
        MyClass3 src2 = new MyClass3();
        src2.aaa = "222";
        src2.bbb = "444";
        actual = Records.createRecord(MyRecord.class, src2);
        assertEquals("222", actual.aaa());
        assertEquals("444", actual.bbb());
        assertNull(actual.ccc());
        assertEquals(0, actual.ddd());
        assertNull(actual.eee());
        src2.ddd = 5;
        src2.eee = 6;
        actual = Records.createRecord(MyRecord.class, src2);
        assertEquals(5, actual.ddd());
        assertEquals(6, actual.eee());
        
    }

    @Test
    void createRecord2() throws Exception {
        MyClass4 src3 = new MyClass4();
        src3.fff = "777";
        src3.ggg = new MyClass3();
        src3.ggg.aaa = "888";

        MyRecord2 actual2 = Records.createRecord(MyRecord2.class, src3);
        assertEquals("888", actual2.ggg().aaa());

    }

    @Test
    void createRecord3() throws Exception {
        MyClass5 src = new MyClass5();
        src.hhh = new ArrayList<>();
        MyClass3 c1 = new MyClass3();
        c1.aaa = "999";
        src.hhh.add(c1);
        MyClass3 c2 = new MyClass3();
        c2.aaa = "000";
        src.hhh.add(c2);
        src.iii = src.hhh;

        MyRecord3 actual = Records.createRecord(MyRecord3.class, src);
        assertEquals("999", actual.hhh().get(0).aaa());
        assertEquals("000", actual.hhh().get(1).aaa());
        assertEquals("000", actual.iii().get(1).aaa);

    }

    /**
     * 生のList、List&lt;?&gt;のコンポーネントは、要素を変換せずそのまま渡す。
     */
    @Test
    void createRecordRawList() throws Exception {
        MyClass8 src = new MyClass8();
        src.raw = new ArrayList<>(List.of("a"));
        src.wildcard = new ArrayList<>(List.of("b"));

        MyRecord7 actual = Records.createRecord(MyRecord7.class, src);
        assertSame(src.raw, actual.raw());
        assertSame(src.wildcard, actual.wildcard());
    }
    
    @Test
    void createRecordList() throws Exception {
        MyClass3 c1 = new MyClass3();
        c1.aaa = "111";
        c1.eee = 2;
        MyClass3 c2 = new MyClass3();
        c2.bbb = "333";
        c2.ddd = 4;
        List<MyClass3> l = new ArrayList<>();
        l.add(c1);
        l.add(c2);
        List<MyRecord> actual = Records.createRecordList(MyRecord.class, l);
        assertEquals("111", actual.get(0).aaa);
        assertNull(actual.get(0).bbb);
        assertEquals(0, actual.get(0).ddd);
        assertEquals(2, actual.get(0).eee);
        assertNull(actual.get(1).aaa);
        assertEquals("333", actual.get(1).bbb);
        assertEquals(4, actual.get(1).ddd);
        assertNull(actual.get(1).eee);
    }

    @Test
    void copyToBean() {
        MyRecord r = new MyRecord("000", "111", "222", 3, null);
        MyClass c = new MyClass();
        Records.copyToBean(r, c);
        assertEquals("000", c.getAaa());
        assertEquals("111", c.getBbb());
        MyClass2 c2 = new MyClass2();
        Records.copyToBean(r, c2);
        assertEquals(0, c2.eee);
    }

    @Test
    void copyToBean2() {
        MyRecord r = new MyRecord("000", "111", "222", 3, null);
        MyRecord2 r2 = new MyRecord2("444", r);
        MyClass c = new MyClass();
        Records.copyToBean(r, c);
        assertEquals("000", c.getAaa());
        assertEquals("111", c.getBbb());
        MyClass2 c2 = new MyClass2();
        Records.copyToBean(r, c2);
        assertEquals(0, c2.eee);
    }

    /**
     * 引数2つのcopyToBeanは、nullも含めてコピーする。
     */
    @Test
    void copyToBeanNull() {
        MyClass c = new MyClass();
        c.setBbb("old");
        Records.copyToBean(new MyRecord("000", null, "222", 3, null), c);
        assertEquals("000", c.getAaa());
        assertNull(c.getBbb());
    }

    /**
     * skipNullがtrueのとき、nullのコンポーネントはBean側の既存の値を残す。
     */
    @Test
    void copyToBeanSkipNull() {
        MyRecord r = new MyRecord("000", null, "222", 3, null);
        MyClass c = new MyClass();
        c.setBbb("old");
        Records.copyToBean(r, c, true);
        assertEquals("000", c.getAaa());
        assertEquals("old", c.getBbb());

        MyClass2 c2 = new MyClass2();
        c2.eee = 5;
        Records.copyToBean(r, c2, true);
        assertEquals(5, c2.eee);
    }

    /**
     * アクセサをオーバーライドして補完しているrecordは、補完後の値がコピーされる。
     */
    @Test
    void copyToBeanAccessor() {
        MyClass c = new MyClass();
        Records.copyToBean(new MyRecord6("000", null), c);
        assertEquals("000", c.getAaa());
        assertEquals("DEFAULT", c.getBbb());
    }

    @Test
    void copyToBeanNestedRecord() {
        MyRecord5 r = new MyRecord5("444", new MyRecord4("555", 6), null, null);
        MyClass7 c = new MyClass7();
        Records.copyToBean(r, c);
        assertEquals("444", c.fff);
        assertInstanceOf(MyClass6.class, c.ggg);
        assertEquals("555", c.ggg.aaa);
        assertEquals(6, c.ggg.ddd);
    }

    /**
     * List&lt;record&gt;は、BeanのプロパティがList&lt;Bean&gt;なら要素をBeanに変換する。
     */
    @Test
    void copyToBeanRecordList() {
        List<MyRecord4> items = List.of(new MyRecord4("111", 1), new MyRecord4("222", 2));
        MyClass7 c = new MyClass7();
        Records.copyToBean(new MyRecord5("444", null, items, null), c);
        assertEquals(2, c.items.size());
        assertInstanceOf(MyClass6.class, (Object) c.items.get(0));
        assertEquals("111", c.items.get(0).aaa);
        assertEquals(2, c.items.get(1).ddd);
    }

    /**
     * BeanのプロパティがList&lt;Object&gt;のとき、要素の型が決まらないのでListをそのまま渡す。
     */
    @Test
    void copyToBeanRecordListAsIs() {
        List<Object> objects = new ArrayList<>();
        objects.add(new MyRecord4("111", 1));
        MyClass7 c = new MyClass7();
        Records.copyToBean(new MyRecord5("444", null, null, objects), c);
        assertSame(objects, c.objects);
    }


    public static class MyClass {
        private String aaa;

        private String bbb;

        private String ccc;

        /**
         * @return Returns the aaa.
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * @param aaa
         *            The aaa to set.
         */
        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        /**
         * @return Returns the bbb.
         */
        public String getBbb() {
            return bbb;
        }

        /**
         * @param bbb
         *            The bbb to set.
         */
        public void setBbb(String bbb) {
            this.bbb = bbb;
        }

        /**
         * @return Returns the ccc.
         */
        public String getCcc() {
            return ccc;
        }

        /**
         * @param ccc
         *            The ccc to set.
         */
        public void setCcc(String ccc) {
            this.ccc = ccc;
        }
    }

    record MyRecord(
        String aaa,
        String bbb,
        String ccc,
        int ddd,
        Integer eee
            ) {
        public void doNothing() {}
    }
    class MyClass2 {
        public int eee;
    }
    class MyClass3 {
        public String aaa;
        public String bbb;
        public int ddd;
        public Integer eee;
    }
    
    record MyRecord2(
            String fff,
            MyRecord ggg
            ) {}
    class MyClass4 {
        public String fff;
        public MyClass3 ggg;
    }

    record MyRecord3(
            List<MyRecord> hhh,
            List<MyClass3> iii
            ) {}
    class MyClass5 {
        public List<MyClass3> hhh;
        public List<MyClass3> iii;
    }

    // ---- 以下は copyToBean(skipNull)、アクセサ経由の読み出し、List変換のテスト用 ----
    // copyToBeanがBeanを生成する(ClassUtil.newInstance)ので、生成されるBeanはstaticにする。

    record MyRecord4(
            String aaa,
            int ddd
            ) {}
    public static class MyClass6 {
        public String aaa;
        public int ddd;
    }

    record MyRecord5(
            String fff,
            MyRecord4 ggg,
            List<MyRecord4> items,
            List<Object> objects
            ) {}
    public static class MyClass7 {
        public String fff;
        public MyClass6 ggg;
        public List<MyClass6> items;
        public List<Object> objects;
    }

    /** bbb()をオーバーライドして、nullのとき補完する */
    record MyRecord6(
            String aaa,
            String bbb
            ) {
        public String bbb() {
            return bbb == null ? "DEFAULT" : bbb;
        }
    }

    @SuppressWarnings("rawtypes")
    record MyRecord7(
            List raw,
            List<?> wildcard
            ) {}
    public static class MyClass8 {
        public List<Object> raw;
        public List<Object> wildcard;
    }
}
