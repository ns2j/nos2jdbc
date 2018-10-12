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
package org.seasar.extension.sql.parser;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.seasar.extension.sql.SqlTokenizer;
import org.seasar.extension.sql.TokenNotClosedRuntimeException;

/**
 * @author higa
 * 
 */
class SqlTokenizerImplTest {

    /**
     * @throws Exception
     */
    @Test
    void testNext() throws Exception {
        String sql = "SELECT * FROM emp";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "1");
        assertEquals(sql, tokenizer.getToken(), "2");
        assertEquals(SqlTokenizer.EOF, tokenizer.next(), "3");
        assertEquals(null, tokenizer.getToken(), "4");
    }

    /**
     * @throws Exception
     */
    @Test
    void testCommentEndNotFound() throws Exception {
        String sql = "SELECT * FROM emp/*hoge";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "1");
        assertEquals("SELECT * FROM emp", tokenizer.getToken(), "2");
        try {
            tokenizer.next();
            fail("3");
        } catch (TokenNotClosedRuntimeException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void testBindVariable() throws Exception {
        String sql = "SELECT * FROM emp WHERE job = /*job*/'CLER K' AND deptno = /*deptno*/20";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "1");
        assertEquals("SELECT * FROM emp WHERE job = ", tokenizer.getToken(), "2");
        assertEquals(SqlTokenizer.COMMENT, tokenizer.next(), "3");
        assertEquals("job", tokenizer.getToken(), "4");
        tokenizer.skipToken();
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "5");
        assertEquals(" AND deptno = ", tokenizer.getToken(), "6");
        assertEquals(SqlTokenizer.COMMENT, tokenizer.next(), "7");
        assertEquals("deptno", tokenizer.getToken(), "8");
        tokenizer.skipToken();
        assertEquals(SqlTokenizer.EOF, tokenizer.next(), "9");
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseBindVariable2() throws Exception {
        String sql = "SELECT * FROM emp WHERE job = /*job*/'CLERK'/**/";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "1");
        assertEquals("SELECT * FROM emp WHERE job = ", tokenizer.getToken(), "2");
        assertEquals(SqlTokenizer.COMMENT, tokenizer.next(), "3");
        assertEquals("job", tokenizer.getToken(), "4");
        tokenizer.skipToken();
        assertEquals(SqlTokenizer.COMMENT, tokenizer.next(), "5");
        assertEquals("", tokenizer.getToken(), "6");
        assertEquals(SqlTokenizer.EOF, tokenizer.next(), "7");
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseBindVariable3() throws Exception {
        String sql = "/*job*/'CLERK',";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals(SqlTokenizer.COMMENT, tokenizer.next(), "1");
        assertEquals("job", tokenizer.getToken(), "2");
        tokenizer.skipToken();
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "3");
        assertEquals(",", tokenizer.getToken(), "4");
        assertEquals(SqlTokenizer.EOF, tokenizer.next(), "5");
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseElse() throws Exception {
        String sql = "SELECT * FROM emp WHERE /*IF job != null*/job = /*job*/'CLERK'-- ELSE job is null/*END*/";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "1");
        assertEquals("SELECT * FROM emp WHERE ", tokenizer.getToken(), "2");
        assertEquals(SqlTokenizer.COMMENT, tokenizer.next(), "3");
        assertEquals("IF job != null", tokenizer.getToken(), "4");
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "5");
        assertEquals("job = ", tokenizer.getToken(), "6");
        assertEquals(SqlTokenizer.COMMENT, tokenizer.next(), "7");
        assertEquals("job", tokenizer.getToken(), "8");
        tokenizer.skipToken();
        assertEquals(SqlTokenizer.ELSE, tokenizer.next(), "9");
        tokenizer.skipWhitespace();
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "10");
        assertEquals("job is null", tokenizer.getToken(), "11");
        assertEquals(SqlTokenizer.COMMENT, tokenizer.next(), "12");
        assertEquals("END", tokenizer.getToken(), "13");
        assertEquals(SqlTokenizer.EOF, tokenizer.next(), "14");
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseElse2() throws Exception {
        String sql = "/*IF false*/aaa -- ELSE bbb = /*bbb*/123/*END*/";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals(SqlTokenizer.COMMENT, tokenizer.next(), "1");
        assertEquals("IF false", tokenizer.getToken(), "2");
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "3");
        assertEquals("aaa ", tokenizer.getToken(), "4");
        assertEquals(SqlTokenizer.ELSE, tokenizer.next(), "5");
        tokenizer.skipWhitespace();
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "6");
        assertEquals("bbb = ", tokenizer.getToken(), "7");
        assertEquals(SqlTokenizer.COMMENT, tokenizer.next(), "8");
        assertEquals("bbb", tokenizer.getToken(), "9");
        tokenizer.skipToken();
        assertEquals(SqlTokenizer.COMMENT, tokenizer.next(), "10");
        assertEquals("END", tokenizer.getToken(), "11");
        assertEquals(SqlTokenizer.EOF, tokenizer.next(), "12");
    }

    /**
     * @throws Exception
     */
    @Test
    void testAnd() throws Exception {
        String sql = " AND bbb";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals(" ", tokenizer.skipWhitespace(), "1");
        assertEquals("AND", tokenizer.skipToken(), "2");
        assertEquals(" AND", tokenizer.getBefore(), "3");
        assertEquals(" bbb", tokenizer.getAfter(), "3");
    }

    /**
     * @throws Exception
     */
    @Test
    void testBindVariable2() throws Exception {
        String sql = "? abc ? def ?";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals(SqlTokenizer.BIND_VARIABLE, tokenizer.next(), "1");
        assertEquals("$1", tokenizer.getToken(), "2");
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "3");
        assertEquals(" abc ", tokenizer.getToken(), "4");
        assertEquals(SqlTokenizer.BIND_VARIABLE, tokenizer.next(), "5");
        assertEquals("$2", tokenizer.getToken(), "6");
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "7");
        assertEquals(" def ", tokenizer.getToken(), "8");
        assertEquals(SqlTokenizer.BIND_VARIABLE, tokenizer.next(), "9");
        assertEquals("$3", tokenizer.getToken(), "10");
        assertEquals(SqlTokenizer.EOF, tokenizer.next(), "11");
    }

    /**
     * @throws Exception
     */
    @Test
    void testBindVariable3() throws Exception {
        String sql = "abc ? def";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "1");
        assertEquals("abc ", tokenizer.getToken(), "2");
        assertEquals(SqlTokenizer.BIND_VARIABLE, tokenizer.next(), "3");
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "4");
        assertEquals(" def", tokenizer.getToken(), "5");
        assertEquals(SqlTokenizer.EOF, tokenizer.next(), "6");
    }

    /**
     * @throws Exception
     */
    @Test
    void testBindVariable4() throws Exception {
        String sql = "/*IF false*/aaa--ELSE bbb = /*bbb*/123/*END*/";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals(SqlTokenizer.COMMENT, tokenizer.next(), "1");
        assertEquals("IF false", tokenizer.getToken(), "2");
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "3");
        assertEquals("aaa", tokenizer.getToken(), "4");
        assertEquals(SqlTokenizer.ELSE, tokenizer.next(), "5");
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "6");
        assertEquals(" bbb = ", tokenizer.getToken(), "7");
        assertEquals(SqlTokenizer.COMMENT, tokenizer.next(), "8");
        assertEquals("bbb", tokenizer.getToken(), "9");
    }

    /**
     * @throws Exception
     */
    @Test
    void testSkipTokenForParent() throws Exception {
        String sql = "INSERT INTO TABLE_NAME (ID) VALUES (/*id*/20)";
        SqlTokenizer tokenizer = new SqlTokenizerImpl(sql);
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "1");
        assertEquals(SqlTokenizer.COMMENT, tokenizer.next(), "2");
        assertEquals("20", tokenizer.skipToken(), "3");
        assertEquals(SqlTokenizer.SQL, tokenizer.next(), "4");
        assertEquals(")", tokenizer.getToken(), "5");
        assertEquals(SqlTokenizer.EOF, tokenizer.next(), "6");
    }
}
