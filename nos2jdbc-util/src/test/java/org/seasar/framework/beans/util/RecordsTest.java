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
}
