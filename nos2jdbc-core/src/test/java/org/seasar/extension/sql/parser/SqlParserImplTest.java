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

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.seasar.extension.sql.EndCommentNotFoundRuntimeException;
import org.seasar.extension.sql.Node;
import org.seasar.extension.sql.SemicolonNotAllowedRuntimeException;
import org.seasar.extension.sql.SqlContext;
import org.seasar.extension.sql.SqlParser;
import org.seasar.extension.sql.TokenNotClosedRuntimeException;
import org.seasar.extension.sql.VariableSqlNotAllowedRuntimeException;
import org.seasar.extension.sql.context.SqlContextImpl;
import org.seasar.extension.sql.node.BindVariableNode;
import org.seasar.extension.sql.node.IfNode;
import org.seasar.extension.sql.node.SqlNode;

/**
 * @author higa
 * 
 */
class SqlParserImplTest {

    /**
     * @throws Exception
     */
    @Test
    void testParse() throws Exception {
        String sql = "SELECT * FROM emp";
        SqlParser parser = new SqlParserImpl(sql);
        SqlContext ctx = new SqlContextImpl();
        Node node = parser.parse();
        node.accept(ctx);
        assertEquals(sql, ctx.getSql(), "1");
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseEndSemicolon() throws Exception {
        testParseEndSemicolon(";");
        testParseEndSemicolon(";\t");
        testParseEndSemicolon("; ");
    }

    /**
     * @param endChar
     */
    public void testParseEndSemicolon(String endChar) {
        String sql = "SELECT * FROM emp";
        SqlParser parser = new SqlParserImpl(sql + endChar);
        SqlContext ctx = new SqlContextImpl();
        Node node = parser.parse();
        node.accept(ctx);
        assertEquals(sql, ctx.getSql(), "1");
    }

    /**
     * @throws Exception
     */
    @Test
    void testCommentEndNotFound() throws Exception {
        String sql = "SELECT * FROM emp/*hoge";
        SqlParser parser = new SqlParserImpl(sql);
        try {
            parser.parse();
            fail("1");
        } catch (TokenNotClosedRuntimeException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseBindVariable() throws Exception {
        String sql = "SELECT * FROM emp WHERE job = /*job*/'CLERK' AND deptno = /*deptno*/20";
        String sql2 = "SELECT * FROM emp WHERE job = ? AND deptno = ?";
        String sql3 = "SELECT * FROM emp WHERE job = ";
        String sql4 = " AND deptno = ";
        SqlParser parser = new SqlParserImpl(sql);
        SqlContext ctx = new SqlContextImpl();
        String job = "CLERK";
        Integer deptno = Integer.valueOf(20);
        ctx.addArg("job", job, job.getClass());
        ctx.addArg("deptno", deptno, deptno.getClass());
        Node root = parser.parse();
        root.accept(ctx);
        System.out.println(ctx.getSql());
        assertEquals(sql2, ctx.getSql(), "1");
        Object[] vars = ctx.getBindVariables();
        assertEquals(2, vars.length, "2");
        assertEquals(job, vars[0], "3");
        assertEquals(deptno, vars[1], "4");
        assertEquals(4, root.getChildSize(), "5");
        SqlNode sqlNode = (SqlNode) root.getChild(0);
        assertEquals(sql3, sqlNode.getSql(), "6");
        BindVariableNode varNode = (BindVariableNode) root.getChild(1);
        assertEquals("job", varNode.getExpression(), "7");
        SqlNode sqlNode2 = (SqlNode) root.getChild(2);
        assertEquals(sql4, sqlNode2.getSql(), "8");
        BindVariableNode varNode2 = (BindVariableNode) root.getChild(3);
        assertEquals("deptno", varNode2.getExpression(), "9");
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseBindVariable2() throws Exception {
        String sql = "SELECT * FROM emp WHERE job = /* job*/'CLERK'";
        String sql2 = "SELECT * FROM emp WHERE job = 'CLERK'";
        String sql3 = "SELECT * FROM emp WHERE job = ";
        String sql4 = "'CLERK'";
        SqlParser parser = new SqlParserImpl(sql);
        SqlContext ctx = new SqlContextImpl();
        Node root = parser.parse();
        root.accept(ctx);
        System.out.println(ctx.getSql());
        assertEquals(sql2, ctx.getSql(), "1");
        assertEquals(2, root.getChildSize(), "2");
        SqlNode sqlNode = (SqlNode) root.getChild(0);
        assertEquals(sql3, sqlNode.getSql(), "3");
        SqlNode sqlNode2 = (SqlNode) root.getChild(1);
        assertEquals(sql4, sqlNode2.getSql(), "4");
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseBindVariable3() throws Exception {
        String sql = "BETWEEN sal ? AND ?";
        SqlParser parser = new SqlParserImpl(sql);
        Node root = parser.parse();
        SqlContext ctx = new SqlContextImpl();
        ctx.addArg("$1", Integer.valueOf(0), Integer.class);
        ctx.addArg("$2", Integer.valueOf(1000), Integer.class);
        root.accept(ctx);
        System.out.println(ctx.getSql());
        assertEquals("BETWEEN sal ? AND ?", ctx.getSql());
        Object[] vars = ctx.getBindVariables();
        assertEquals(2, vars.length, "2");
        assertEquals(Integer.valueOf(0), vars[0], "3");
        assertEquals(Integer.valueOf(1000), vars[1], "4");
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseBindVariable4() throws Exception {
        String sql = "SELECT * FROM emp WHERE job = #*job*#'CLERK' AND deptno = #*deptno*#20";
        SqlParser parser = new SqlParserImpl(sql);
        SqlContext ctx = new SqlContextImpl();
        String job = "CLERK";
        Integer deptno = Integer.valueOf(20);
        ctx.addArg("job", job, job.getClass());
        ctx.addArg("deptno", deptno, deptno.getClass());
        Node root = parser.parse();
        root.accept(ctx);
        System.out.println(ctx.getSql());
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseBindVariable5() throws Exception {
        String sql = "SELECT * FROM emp WHERE job = /*job*/'CLERK'";
        SqlParser parser = new SqlParserImpl(sql);
        SqlContext ctx = new SqlContextImpl();
        String job = "HOGE";
        ctx.addArg("job", job, job.getClass());
        Node root = parser.parse();
        root.accept(ctx);
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseWhiteSpace() throws Exception {
        String sql = "SELECT * FROM emp WHERE empno = /*empno*/1 AND 1 = 1";
        String sql2 = "SELECT * FROM emp WHERE empno = ? AND 1 = 1";
        String sql3 = " AND 1 = 1";
        SqlParser parser = new SqlParserImpl(sql);
        SqlContext ctx = new SqlContextImpl();
        Integer empno = Integer.valueOf(7788);
        ctx.addArg("empno", empno, empno.getClass());
        Node root = parser.parse();
        root.accept(ctx);
        System.out.println(ctx.getSql());
        assertEquals(sql2, ctx.getSql(), "1");
        SqlNode sqlNode = (SqlNode) root.getChild(2);
        assertEquals(sql3, sqlNode.getSql(), "2");
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseIf() throws Exception {
        String sql = "SELECT * FROM emp/*IF job != null*/ WHERE job = /*job*/'CLERK'/*END*/";
        String sql2 = "SELECT * FROM emp WHERE job = ?";
        String sql3 = "SELECT * FROM emp";
        SqlParser parser = new SqlParserImpl(sql);
        SqlContext ctx = new SqlContextImpl();
        String job = "CLERK";
        ctx.addArg("job", job, job.getClass());
        Node root = parser.parse();
        root.accept(ctx);
        System.out.println(ctx.getSql());
        assertEquals(sql2, ctx.getSql(), "1");
        Object[] vars = ctx.getBindVariables();
        assertEquals(1, vars.length, "2");
        assertEquals(job, vars[0], "3");
        assertEquals(2, root.getChildSize(), "4");
        SqlNode sqlNode = (SqlNode) root.getChild(0);
        assertEquals(sql3, sqlNode.getSql(), "5");
        IfNode ifNode = (IfNode) root.getChild(1);
        assertEquals("job != null", ifNode.getExpression(), "6");
        assertEquals(2, ifNode.getChildSize(), "7");
        SqlNode sqlNode2 = (SqlNode) ifNode.getChild(0);
        assertEquals(" WHERE job = ", sqlNode2.getSql(), "8");
        BindVariableNode varNode = (BindVariableNode) ifNode.getChild(1);
        assertEquals("job", varNode.getExpression(), "9");
        SqlContext ctx2 = new SqlContextImpl();
        root.accept(ctx2);
        System.out.println(ctx2.getSql());
        assertEquals(sql3, ctx2.getSql(), "10");
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseIf2() throws Exception {
        String sql = "/*IF aaa != null*/aaa/*IF bbb != null*/bbb/*END*//*END*/";
        SqlParser parser = new SqlParserImpl(sql);
        SqlContext ctx = new SqlContextImpl();
        Node root = parser.parse();
        root.accept(ctx);
        System.out.println("[" + ctx.getSql() + "]");
        assertEquals("", ctx.getSql(), "1");
        ctx.addArg("aaa", null, String.class);
        ctx.addArg("bbb", "hoge", String.class);
        root.accept(ctx);
        System.out.println("[" + ctx.getSql() + "]");
        assertEquals("", ctx.getSql(), "2");
        ctx.addArg("aaa", "hoge", String.class);
        root.accept(ctx);
        System.out.println("[" + ctx.getSql() + "]");
        assertEquals("aaabbb", ctx.getSql(), "3");
        SqlContext ctx2 = new SqlContextImpl();
        ctx2.addArg("aaa", "hoge", String.class);
        ctx2.addArg("bbb", null, String.class);
        root.accept(ctx2);
        System.out.println("[" + ctx2.getSql() + "]");
        assertEquals("aaa", ctx2.getSql(), "4");
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseIf3() throws Exception {
        String sql = "/*IF aaa != null*/aaa,/*END*/";
        SqlParser parser = new SqlParserImpl(sql);
        SqlContext ctx = new SqlContextImpl();
        Node root = parser.parse();
        root.accept(ctx);
        System.out.println("[" + ctx.getSql() + "]");
        assertEquals("", ctx.getSql());
        ctx.addArg("aaa", "hoge", String.class);
        root.accept(ctx);
        System.out.println("[" + ctx.getSql() + "]");
        assertEquals("aaa,", ctx.getSql());
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseIf4() throws Exception {
        String sql = "/*IF has_aaa*/aaa,/*END*/";
        SqlParser parser = new SqlParserImpl(sql);
        SqlContext ctx = new SqlContextImpl();
        Node root = parser.parse();
        root.accept(ctx);
        System.out.println("[" + ctx.getSql() + "]");
        assertEquals("", ctx.getSql());
        ctx.addArg("aaa", null, String.class);
        root.accept(ctx);
        System.out.println("[" + ctx.getSql() + "]");
        assertEquals("aaa,", ctx.getSql());
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseIf5() throws Exception {
        String sql = "/*BEGIN*//*IF has_aaa*/,aaa/*END*//*END*/";
        SqlParser parser = new SqlParserImpl(sql);
        SqlContext ctx = new SqlContextImpl();
        Node root = parser.parse();
        root.accept(ctx);
        System.out.println("[" + ctx.getSql() + "]");
        assertEquals("", ctx.getSql());
        ctx.addArg("aaa", null, String.class);
        root.accept(ctx);
        System.out.println("[" + ctx.getSql() + "]");
        assertEquals("aaa", ctx.getSql());
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseIf_NotAllowed() throws Exception {
        String sql = "SELECT * FROM emp/*IF job != null*/ WHERE job = /*job*/'CLERK'/*END*/";
        SqlParser parser = new SqlParserImpl(sql, false);
        try {
            parser.parse();
            fail();
        } catch (VariableSqlNotAllowedRuntimeException expected) {
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseElse() throws Exception {
        String sql = "SELECT * FROM emp WHERE /*IF job != null*/job = /*job*/'CLERK'-- ELSE job is null/*END*/";
        String sql2 = "SELECT * FROM emp WHERE job = ?";
        String sql3 = "SELECT * FROM emp WHERE job is null";
        SqlParser parser = new SqlParserImpl(sql);
        SqlContext ctx = new SqlContextImpl();
        String job = "CLERK";
        ctx.addArg("job", job, job.getClass());
        Node root = parser.parse();
        root.accept(ctx);
        System.out.println("[" + ctx.getSql() + "]");
        assertEquals(sql2, ctx.getSql(), "1");
        Object[] vars = ctx.getBindVariables();
        assertEquals(1, vars.length, "2");
        assertEquals(job, vars[0], "3");
        SqlContext ctx2 = new SqlContextImpl();
        root.accept(ctx2);
        System.out.println("[" + ctx2.getSql() + "]");
        assertEquals(sql3, ctx2.getSql(), "4");
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseElse2() throws Exception {
        String sql = "/*IF false*/aaa--ELSE bbb = /*bbb*/123/*END*/";
        SqlParser parser = new SqlParserImpl(sql);
        SqlContext ctx = new SqlContextImpl();
        Integer bbb = Integer.valueOf(123);
        ctx.addArg("bbb", bbb, bbb.getClass());
        Node root = parser.parse();
        root.accept(ctx);
        System.out.println("[" + ctx.getSql() + "]");
        assertEquals("bbb = ?", ctx.getSql(), "1");
        Object[] vars = ctx.getBindVariables();
        assertEquals(1, vars.length, "2");
        assertEquals(bbb, vars[0], "3");
    }

    /**
     * @throws Exception
     */
    @Test
    void testParseElse3() throws Exception {
        String sql = "/*IF false*/aaa--ELSE bbb/*IF false*/ccc--ELSE ddd/*END*//*END*/";
        SqlParser parser = new SqlParserImpl(sql);
        SqlContext ctx = new SqlContextImpl();
        Node root = parser.parse();
        root.accept(ctx);
        System.out.println("[" + ctx.getSql() + "]");
        assertEquals("bbbddd", ctx.getSql(), "1");
    }

    /**
     * @throws Exception
     */
    @Test
    void testElse4() throws Exception {
        String sql = "SELECT * FROM emp/*BEGIN*/ WHERE /*IF false*/aaa-- ELSE AND deptno = 10/*END*//*END*/";
        String sql2 = "SELECT * FROM emp WHERE deptno = 10";
        SqlParser parser = new SqlParserImpl(sql);
        Node root = parser.parse();
        SqlContext ctx = new SqlContextImpl();
        root.accept(ctx);
        System.out.println(ctx.getSql());
        assertEquals(sql2, ctx.getSql(), "1");
    }

    /**
     * @throws Exception
     */
    @Test
    void testBegin() throws Exception {
        String sql = "SELECT * FROM emp/*BEGIN*/ WHERE /*IF job != null*/job = /*job*/'CLERK'/*END*//*IF deptno != null*/ AND deptno = /*deptno*/20/*END*//*END*/";
        String sql2 = "SELECT * FROM emp";
        String sql3 = "SELECT * FROM emp WHERE job = ?";
        String sql4 = "SELECT * FROM emp WHERE job = ? AND deptno = ?";
        String sql5 = "SELECT * FROM emp WHERE deptno = ?";
        SqlParser parser = new SqlParserImpl(sql);
        Node root = parser.parse();
        SqlContext ctx = new SqlContextImpl();
        root.accept(ctx);
        System.out.println(ctx.getSql());
        assertEquals(sql2, ctx.getSql(), "1");
        SqlContext ctx2 = new SqlContextImpl();
        ctx2.addArg("job", "CLERK", String.class);
        ctx2.addArg("deptno", null, Integer.class);
        root.accept(ctx2);
        System.out.println(ctx2.getSql());
        assertEquals(sql3, ctx2.getSql(), "2");
        SqlContext ctx3 = new SqlContextImpl();
        ctx3.addArg("job", "CLERK", String.class);
        ctx3.addArg("deptno", Integer.valueOf(20), Integer.class);
        root.accept(ctx3);
        System.out.println(ctx3.getSql());
        assertEquals(sql4, ctx3.getSql(), "3");
        SqlContext ctx4 = new SqlContextImpl();
        ctx4.addArg("deptno", Integer.valueOf(20), Integer.class);
        ctx4.addArg("job", null, String.class);
        root.accept(ctx4);
        System.out.println(ctx4.getSql());
        assertEquals(sql5, ctx4.getSql(), "4");
    }

    /**
     * @throws Exception
     */
    @Test
    void testBegin2() throws Exception {
        String sql = "SELECT * FROM emp /*BEGIN*/WHERE /*IF job != null*/job = /*job*/'CLERK'/*END*//*END*/";
        SqlParser parser = new SqlParserImpl(sql);
        SqlContext ctx = new SqlContextImpl();
        String job = "HOGE";
        ctx.addArg("job", job, job.getClass());
        Node root = parser.parse();
        root.accept(ctx);
    }

    /**
     * @throws Exception
     */
    @Test
    void testBeginAnd() throws Exception {
        String sql = "/*BEGIN*/WHERE /*IF true*/aaa BETWEEN /*bbb*/111 AND /*ccc*/123/*END*//*END*/";
        String sql2 = "WHERE aaa BETWEEN ? AND ?";
        SqlParser parser = new SqlParserImpl(sql);
        Node root = parser.parse();
        SqlContext ctx = new SqlContextImpl();
        ctx.addArg("bbb", "111", String.class);
        ctx.addArg("ccc", "222", String.class);
        root.accept(ctx);
        System.out.println("[" + ctx.getSql() + "]");
        assertEquals(sql2, ctx.getSql());
    }

    /**
     * @throws Exception
     */
    @Test
    void testPrefixSql_comma() throws Exception {
        String sql = "/*BEGIN*//*IF false*/aaa/*END*//*IF true*/,bbb/*END*//*END*/";
        SqlParser parser = new SqlParserImpl(sql);
        Node root = parser.parse();
        SqlContext ctx = new SqlContextImpl();
        root.accept(ctx);
        System.out.println("[" + ctx.getSql() + "]");
        assertEquals("bbb", ctx.getSql());
    }

    /**
     * @throws Exception
     */
    @Test
    void testIn() throws Exception {
        String sql = "SELECT * FROM emp WHERE deptno IN /*deptnoList*/(10, 20) ORDER BY ename";
        String sql2 = "SELECT * FROM emp WHERE deptno IN (?, ?) ORDER BY ename";
        SqlParser parser = new SqlParserImpl(sql);
        Node root = parser.parse();
        SqlContext ctx = new SqlContextImpl();
        List deptnoList = new ArrayList();
        deptnoList.add(Integer.valueOf(10));
        deptnoList.add(Integer.valueOf(20));
        ctx.addArg("deptnoList", deptnoList, List.class);
        root.accept(ctx);
        System.out.println(ctx.getSql());
        assertEquals(sql2, ctx.getSql(), "1");
        Object[] vars = ctx.getBindVariables();
        assertEquals(2, vars.length, "2");
        assertEquals(Integer.valueOf(10), vars[0], "3");
        assertEquals(Integer.valueOf(20), vars[1], "4");
    }

    /**
     * @throws Exception
     */
    @Test
    void testIn2() throws Exception {
        String sql = "SELECT * FROM emp WHERE deptno IN /*deptnoList*/(10, 20) ORDER BY ename";
        String sql2 = "SELECT * FROM emp WHERE deptno IN (?, ?) ORDER BY ename";
        SqlParser parser = new SqlParserImpl(sql);
        Node root = parser.parse();
        SqlContext ctx = new SqlContextImpl();
        int[] deptnoArray = { 10, 20 };
        ctx.addArg("deptnoList", deptnoArray, deptnoArray.getClass());
        root.accept(ctx);
        System.out.println(ctx.getSql());
        assertEquals(sql2, ctx.getSql(), "1");
        Object[] vars = ctx.getBindVariables();
        assertEquals(2, vars.length, "2");
        assertEquals(Integer.valueOf(10), vars[0], "3");
        assertEquals(Integer.valueOf(20), vars[1], "4");
    }

    /**
     * @throws Exception
     */
    @Test
    void testIn3() throws Exception {
        String sql = "SELECT * FROM emp WHERE ename IN /*enames*/('SCOTT','MARY') AND job IN /*jobs*/('ANALYST', 'FREE')";
        String sql2 = "SELECT * FROM emp WHERE ename IN (?, ?) AND job IN (?, ?)";
        SqlParser parser = new SqlParserImpl(sql);
        Node root = parser.parse();
        SqlContext ctx = new SqlContextImpl();
        String[] enames = { "SCOTT", "MARY" };
        String[] jobs = { "ANALYST", "FREE" };
        ctx.addArg("enames", enames, enames.getClass());
        ctx.addArg("jobs", jobs, jobs.getClass());
        root.accept(ctx);
        System.out.println(ctx.getSql());
        assertEquals(sql2, ctx.getSql(), "1");
        Object[] vars = ctx.getBindVariables();
        assertEquals(4, vars.length, "2");
        assertEquals("SCOTT", vars[0], "3");
        assertEquals("MARY", vars[1], "4");
        assertEquals("ANALYST", vars[2], "5");
        assertEquals("FREE", vars[3], "6");
    }

    /**
     * @throws Exception
     */
    @Test
    void testEndNotFound() throws Exception {
        String sql = "/*BEGIN*/";
        SqlParser parser = new SqlParserImpl(sql);
        try {
            parser.parse();
            fail("1");
        } catch (EndCommentNotFoundRuntimeException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void testEndParent() throws Exception {
        String sql = "INSERT INTO ITEM (ID, NUM) VALUES (/*id*/1, /*num*/20)";
        SqlParser parser = new SqlParserImpl(sql);
        Node root = parser.parse();
        SqlContext ctx = new SqlContextImpl();
        ctx.addArg("id", Integer.valueOf(0), Integer.class);
        ctx.addArg("num", Integer.valueOf(1), Integer.class);
        root.accept(ctx);
        System.out.println(ctx.getSql());
        assertEquals(true, ctx.getSql().endsWith(")"), "1");
    }

    /**
     * @throws Exception
     */
    @Test
    void testEmbeddedValue() throws Exception {
        String sql = "/*$aaa*/";
        SqlParser parser = new SqlParserImpl(sql);
        Node root = parser.parse();
        SqlContext ctx = new SqlContextImpl();
        ctx.addArg("aaa", Integer.valueOf(0), Integer.class);
        root.accept(ctx);
        System.out.println(ctx.getSql());
        assertEquals("0", ctx.getSql(), "1");
    }

    /**
     * @throws Exception
     */
    @Test
    void testEmbeddedValue_semicolon() throws Exception {
        String sql = "/*$aaa*/";
        SqlParser parser = new SqlParserImpl(sql);
        Node root = parser.parse();
        SqlContext ctx = new SqlContextImpl();
        ctx.addArg("aaa", ";update", String.class);
        try {
            root.accept(ctx);
            fail();
        } catch (SemicolonNotAllowedRuntimeException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void testEmbeddedValue_orderBy() throws Exception {
        String sql = "/*orderBy*/";
        SqlParser parser = new SqlParserImpl(sql);
        Node root = parser.parse();
        SqlContext ctx = new SqlContextImpl();
        ctx.addArg("orderBy", "id desc", String.class);
        root.accept(ctx);
        System.out.println(ctx.getSql());
        assertEquals("id desc", ctx.getSql());
    }

    /**
     * @throws Exception
     */
    @Test
    void testEmbeddedValue_NotAllowed() throws Exception {
        String sql = "/*$aaa*/";
        SqlParser parser = new SqlParserImpl(sql, false);
        try {
            parser.parse();
            fail();
        } catch (VariableSqlNotAllowedRuntimeException expected) {
        }
    }
}
