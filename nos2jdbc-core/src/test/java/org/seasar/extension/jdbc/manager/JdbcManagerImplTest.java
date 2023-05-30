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
package org.seasar.extension.jdbc.manager;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.entity.Eee;
import org.seasar.extension.jdbc.entity.Iii;
import org.seasar.extension.jdbc.exception.NoIdPropertyRuntimeException;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl.SynchronizationImpl;
import org.seasar.extension.jdbc.query.AutoBatchDeleteImpl;
import org.seasar.extension.jdbc.query.AutoBatchInsertImpl;
import org.seasar.extension.jdbc.query.AutoBatchUpdateImpl;
import org.seasar.extension.jdbc.query.AutoDeleteImpl;
import org.seasar.extension.jdbc.query.AutoFunctionCallImpl;
import org.seasar.extension.jdbc.query.AutoInsertImpl;
import org.seasar.extension.jdbc.query.AutoProcedureCallImpl;
import org.seasar.extension.jdbc.query.AutoSelectImpl;
import org.seasar.extension.jdbc.query.AutoUpdateImpl;
import org.seasar.extension.jdbc.query.SqlBatchUpdateImpl;
import org.seasar.extension.jdbc.query.SqlFileBatchUpdateImpl;
import org.seasar.extension.jdbc.query.SqlFileFunctionCallImpl;
import org.seasar.extension.jdbc.query.SqlFileProcedureCallImpl;
import org.seasar.extension.jdbc.query.SqlFileSelectImpl;
import org.seasar.extension.jdbc.query.SqlFileUpdateImpl;
import org.seasar.extension.jdbc.query.SqlFunctionCallImpl;
import org.seasar.extension.jdbc.query.SqlProcedureCallImpl;
import org.seasar.extension.jdbc.query.SqlSelectImpl;
import org.seasar.extension.jdbc.query.SqlUpdateImpl;
import org.seasar.extension.jta.TransactionImpl;

import jakarta.transaction.TransactionManager;

/**
 * @author higa
 * 
 */
class JdbcManagerImplTest {

    private JdbcManagerImpl manager;

    private TransactionManager transactionManager;

    
    @BeforeEach
    void setUp() throws Exception {
	JdbcManagerTestImpl util = new JdbcManagerTestImpl();
	transactionManager = util.getTransactionManager();
	manager = util;
    }

    
    @AfterEach
    void tearDown() throws Exception {
        manager = null;
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testSelectBySql() throws Exception {
        String sql = "select * from aaa";
        SqlSelectImpl<Aaa> select = (SqlSelectImpl<Aaa>) manager.selectBySql(
                Aaa.class, sql);
        assertNotNull(select);
        assertSame(manager, select.getJdbcManager());
        assertEquals(Aaa.class, select.getBaseClass());
        assertEquals(sql, select.getSql());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testSelectBySql_parameters() throws Exception {
        String sql = "select * from aaa where id = ?";
        SqlSelectImpl<Aaa> query = (SqlSelectImpl<Aaa>) manager.selectBySql(
                Aaa.class, sql, 1);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(Aaa.class, query.getBaseClass());
        assertEquals(sql, query.getSql());
        Object[] vars = query.getParamValues();
        assertEquals(1, vars.length);
        assertEquals(1, vars[0]);
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testUpdateBySql() throws Exception {
        String sql = "update aaa set name = ? where id = ?";
        SqlUpdateImpl query = (SqlUpdateImpl) manager.updateBySql(sql,
                String.class, Integer.class);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(sql, query.getExecutedSql());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testUpdateBatchBySql() throws Exception {
        String sql = "update aaa set name = ? where id = ?";
        SqlBatchUpdateImpl query = (SqlBatchUpdateImpl) manager
                .updateBatchBySql(sql, String.class, Integer.class);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(sql, query.getExecutedSql());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testSelectBySqlFile() throws Exception {
        String path = "select.sql";
        SqlFileSelectImpl<Aaa> query = (SqlFileSelectImpl<Aaa>) manager
                .selectBySqlFile(Aaa.class, path);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(Aaa.class, query.getBaseClass());
        assertEquals(path, query.getPath());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testUpdateBySqlFile() throws Exception {
        String path = "update.sql";
        SqlFileUpdateImpl query = (SqlFileUpdateImpl) manager.updateBySqlFile(
                path, 1);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(path, query.getPath());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    void testUpdateBatchBySqlFile() throws Exception {
        String path = "update.sql";
        SqlFileBatchUpdateImpl<String> query = (SqlFileBatchUpdateImpl<String>) manager
                .updateBatchBySqlFile(path, "foo", "bar");
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(path, query.getPath());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testCall_procedure() throws Exception {
        manager.maxRows = 100;
        manager.fetchSize = 10;
        manager.queryTimeout = 5;
        AutoProcedureCallImpl query = (AutoProcedureCallImpl) manager.call(
                "myProc", 1);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(100, query.getMaxRows());
        assertEquals(10, query.getFetchSize());
        assertEquals(5, query.getQueryTimeout());
        assertEquals(1, query.getParameter());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testCallBySql_procedure() throws Exception {
        manager.maxRows = 100;
        manager.fetchSize = 10;
        manager.queryTimeout = 5;
        String sql = "{call hoge(?)}";
        SqlProcedureCallImpl query = (SqlProcedureCallImpl) manager.callBySql(
                sql, 1);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(100, query.getMaxRows());
        assertEquals(10, query.getFetchSize());
        assertEquals(5, query.getQueryTimeout());
        assertEquals(sql, query.getExecutedSql());
        assertEquals(1, query.getParameter());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testCallBySqlFile_procedure() throws Exception {
        manager.maxRows = 100;
        manager.fetchSize = 10;
        manager.queryTimeout = 5;
        String path = "call.sql";
        SqlFileProcedureCallImpl query = (SqlFileProcedureCallImpl) manager
                .callBySqlFile(path, 1);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(100, query.getMaxRows());
        assertEquals(10, query.getFetchSize());
        assertEquals(5, query.getQueryTimeout());
        assertEquals(path, query.getPath());
        assertEquals(1, query.getParameter());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testCall_function() throws Exception {
        manager.maxRows = 100;
        manager.fetchSize = 10;
        manager.queryTimeout = 5;
        AutoFunctionCallImpl<String> query = (AutoFunctionCallImpl<String>) manager
                .call(String.class, "myFunc", 1);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(100, query.getMaxRows());
        assertEquals(10, query.getFetchSize());
        assertEquals(5, query.getQueryTimeout());
        assertEquals(1, query.getParameter());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testCallBySql_function() throws Exception {
        manager.maxRows = 100;
        manager.fetchSize = 10;
        manager.queryTimeout = 5;
        String sql = "{? = call hoge(?)}";
        SqlFunctionCallImpl<String> query = (SqlFunctionCallImpl<String>) manager
                .callBySql(String.class, sql, 1);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(100, query.getMaxRows());
        assertEquals(10, query.getFetchSize());
        assertEquals(5, query.getQueryTimeout());
        assertEquals(sql, query.getExecutedSql());
        assertEquals(1, query.getParameter());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testCallBySqlFile_function() throws Exception {
        manager.maxRows = 100;
        manager.fetchSize = 10;
        manager.queryTimeout = 5;
        String path = "call.sql";
        SqlFileFunctionCallImpl<String> query = (SqlFileFunctionCallImpl<String>) manager
                .callBySqlFile(String.class, path, 1);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(100, query.getMaxRows());
        assertEquals(10, query.getFetchSize());
        assertEquals(5, query.getQueryTimeout());
        assertEquals(path, query.getPath());
        assertEquals(1, query.getParameter());
    }

    /**
     * @throws Exception
     */
    @Test
    void testFrom() throws Exception {
        AutoSelectImpl<Aaa> query = (AutoSelectImpl<Aaa>) manager
                .from(Aaa.class);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(Aaa.class, query.getBaseClass());
    }

    /**
     * @throws Exception
     */
    @Test
    void testAutoInsert() throws Exception {
        Eee eee = new Eee();
        AutoInsertImpl<Eee> query = (AutoInsertImpl<Eee>) manager.insert(eee);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertSame(eee, query.getEntity());
    }

    /**
     * @throws Exception
     */
    @Test
    void testAutoBatchInsert_array() throws Exception {
        Eee[] entities = new Eee[] { new Eee(1, "foo"), new Eee(2, "bar"),
                new Eee(3, "baz") };
        AutoBatchInsertImpl<Eee> query = (AutoBatchInsertImpl<Eee>) manager
                .insertBatch(entities);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(3, query.getEntities().size());
        assertSame(entities[0], query.getEntities().get(0));
        assertSame(entities[1], query.getEntities().get(1));
        assertSame(entities[2], query.getEntities().get(2));
    }

    /**
     * @throws Exception
     */
    @Test
    void testAutoBatchInsert_list() throws Exception {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchInsertImpl<Eee> query = (AutoBatchInsertImpl<Eee>) manager
                .insertBatch(entities);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertSame(entities, query.getEntities());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testAutoUpdate() throws Exception {
        Eee eee = new Eee();
        AutoUpdateImpl<Eee> query = (AutoUpdateImpl<Eee>) manager.update(eee);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertSame(eee, query.getEntity());

        try {
            manager.update(new Iii());
            fail();
        } catch (NoIdPropertyRuntimeException expected) {
            System.out.println(expected);
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void testAutoBatchUpdate_array() throws Exception {
        Eee[] entities = new Eee[] { new Eee(1, "foo"), new Eee(2, "bar"),
                new Eee(3, "baz") };
        AutoBatchUpdateImpl<Eee> query = (AutoBatchUpdateImpl<Eee>) manager
                .updateBatch(entities);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(3, query.getEntities().size());
        assertSame(entities[0], query.getEntities().get(0));
        assertSame(entities[1], query.getEntities().get(1));
        assertSame(entities[2], query.getEntities().get(2));

        try {
            manager.updateBatch(new Iii[] { new Iii() });
            fail();
        } catch (NoIdPropertyRuntimeException expected) {
            System.out.println(expected);
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void testAutoBatchUpdate_list() throws Exception {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = (AutoBatchUpdateImpl<Eee>) manager
                .updateBatch(entities);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertSame(entities, query.getEntities());

        try {
            manager.updateBatch(Arrays.asList(new Iii()));
            fail();
        } catch (NoIdPropertyRuntimeException expected) {
            System.out.println(expected);
        }
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testAutoDelete() throws Exception {
        Eee eee = new Eee();
        AutoDeleteImpl<Eee> query = (AutoDeleteImpl<Eee>) manager.delete(eee);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertSame(eee, query.getEntity());

        try {
            manager.delete(new Iii());
            fail();
        } catch (NoIdPropertyRuntimeException expected) {
            System.out.println(expected);
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void testAutoBatchDelete_array() throws Exception {
        Eee[] entities = new Eee[] { new Eee(1, "foo"), new Eee(2, "bar"),
                new Eee(3, "baz") };
        AutoBatchDeleteImpl<Eee> query = (AutoBatchDeleteImpl<Eee>) manager
                .deleteBatch(entities);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(3, query.getEntities().size());
        assertSame(entities[0], query.getEntities().get(0));
        assertSame(entities[1], query.getEntities().get(1));
        assertSame(entities[2], query.getEntities().get(2));

        try {
            manager.deleteBatch(new Iii[] { new Iii() });
            fail();
        } catch (NoIdPropertyRuntimeException expected) {
            System.out.println(expected);
        }
    }

    /**
     * @throws Exception
     */
    @Test
    void testAutoBatchDelete_list() throws Exception {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchDeleteImpl<Eee> query = (AutoBatchDeleteImpl<Eee>) manager
                .deleteBatch(entities);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertSame(entities, query.getEntities());

        try {
            manager.deleteBatch(Arrays.asList(new Iii()));
            fail();
        } catch (NoIdPropertyRuntimeException expected) {
            System.out.println(expected);
        }
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetJdbcContext_tx() throws Exception {
        transactionManager.begin();
        JdbcContext ctx = manager.getJdbcContext();
        assertNotNull(ctx);
        assertTrue(ctx.isTransactional());
        assertSame(ctx, manager.getJdbcContext());
        TransactionImpl tx = (TransactionImpl) transactionManager
                .getTransaction();
        assertEquals(1, tx.getInterposedSynchronizations().size());
        SynchronizationImpl sync = SynchronizationImpl.class.cast(tx
                .getInterposedSynchronizations().get(0));
        assertSame(manager.getJdbcContext(), sync.context);
    }

    /**
     * 
     * @throws Exception
     */
//i
    /*
    @Test
    void testGetJdbcContext_tx_selectableDataSource() throws Exception {
        DataSourceFactory dataSourceFactory = new MockDataSourceFactory();
        SelectableDataSourceProxy dataSource = new SelectableDataSourceProxy();
        dataSource.setDataSourceFactory(dataSourceFactory);
        manager.setDataSource(dataSource);
        manager.setDataSourceFactory(dataSourceFactory);

        transactionManager.begin();

        dataSourceFactory.setSelectableDataSourceName("hoge");
        JdbcContext hogeCtx = manager.getJdbcContext();
        assertNotNull(hogeCtx);
        assertTrue(hogeCtx.isTransactional());
        assertSame(hogeCtx, manager.getJdbcContext());
        assertEquals("hoge", manager.getSelectableDataSourceName());

        dataSourceFactory.setSelectableDataSourceName("foo");
        JdbcContext fooCtx = manager.getJdbcContext();
        assertNotNull(fooCtx);
        assertTrue(fooCtx.isTransactional());
        assertSame(fooCtx, manager.getJdbcContext());
        assertEquals("foo", manager.getSelectableDataSourceName());

        TransactionImpl tx = (TransactionImpl) transactionManager
                .getTransaction();
        assertEquals(2, tx.getInterposedSynchronizations().size());
        SynchronizationImpl sync = SynchronizationImpl.class.cast(tx
                .getInterposedSynchronizations().get(0));
        assertSame(hogeCtx, sync.context);
        sync = SynchronizationImpl.class.cast(tx
                .getInterposedSynchronizations().get(1));
        assertSame(fooCtx, sync.context);
    }
*/
    /**
     * @throws Exception
     * 
     */
    @Test
    void testBeforeCompletion() throws Exception {
        transactionManager.begin();
        JdbcContextImpl ctx = (JdbcContextImpl) manager.getJdbcContext();
        ctx.getStatement();
        transactionManager.commit();
        assertTrue(ctx.isConnectionNull());
        assertTrue(ctx.isStatementNull());
        assertTrue(manager.isJdbcContextNull());
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    void testGetJdbcContext_notx() throws Exception {
        JdbcContext ctx = manager.getJdbcContext();
        assertNotNull(ctx);
        assertFalse(ctx.isTransactional());
        assertNotSame(ctx, manager.getJdbcContext());
    }

    /**
     * 
     * @author taedium
     */
    //i
    /*
    public static class MockDataSourceFactory extends DataSourceFactoryImpl {

        
        public DataSource getDataSource(String name) {
            return new MockDataSource();
        }
    }
    */
}
