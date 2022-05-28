/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package nos2jdbc.standalone;

import static org.seasar.framework.util.StringUtil.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.dialect.OracleDialect;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.exception.IllegalAccessRuntimeException;
import org.seasar.framework.exception.InstantiationRuntimeException;
import org.seasar.framework.exception.InvocationTargetRuntimeException;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;

import nos2jdbc.ManagerSetter;
import nos2jdbc.TransactionManagerRegistry;

public class NoS2JdbcManager {
    private JdbcManager jdbcManager; 

    final static String PROPS_NAME_PREFIX = "nos2jdbc-datasource";
    final static String PROPS_NAME_SUFFIX = ".properties";  

    private NoS2JdbcManager(String propertiesFilename, DataSource ds) {
        if (propertiesFilename == null) {
            String database = System.getProperty("database");
            if (database != null && !database.isBlank())
                propertiesFilename = PROPS_NAME_PREFIX + "-" + database + PROPS_NAME_SUFFIX;
            else
                propertiesFilename = PROPS_NAME_PREFIX + PROPS_NAME_SUFFIX;
        }
        InputStream is = NoS2JdbcManager.class.getClassLoader().getResourceAsStream(propertiesFilename);
        if (is == null)
            throw new RuntimeException(new FileNotFoundException(propertiesFilename));
        Properties ps = new Properties();
        try {
            ps.load(is);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }

        DataSource dataSource = ds;

        TransactionManager tm = TransactionManagerRegistry.get();
        TransactionSynchronizationRegistry syncRegistry = new TransactionSynchronizationRegistryImpl(tm);

        DbmsDialect dialect = new OracleDialect();;
        String mock = rtrim(ltrim(ps.getProperty("mock")));  //for no2sjdbc-gen test
        if (isNotBlank(mock)) {
            dataSource = getMockDataSource(mock);
        } else {
            if (ds == null) {
                String driverName = ps.getProperty("driverClassName");
                String url = resolveEnvVars(ps.getProperty("URL"));
                String user = resolveEnvVars(ps.getProperty("user"));
                String password = resolveEnvVars(ps.getProperty("password", ""));
                int poolSize = Integer.valueOf(resolveEnvVars(ps.getProperty("poolSize", "900")));
                dataSource = new NoS2JdbcDataSource(tm, driverName, url, user, password, poolSize);
            }
            String dialectName = ltrim(rtrim(resolveEnvVars(ps.getProperty("dialect"))));
            dialect = getDialect(dialectName);
        }

        JdbcManagerImpl jdbcManagerImpl = new JdbcManagerImpl();
        ManagerSetter.setToJdbcManagerImpl(jdbcManagerImpl, dataSource, dialect, syncRegistry);

        jdbcManager = jdbcManagerImpl;

        TransactionManagerRegistry.register(tm);
    }

    private String resolveEnvVars(String input) {
        if (null == input)
            return null;

        // match ${ENV_VAR_NAME}, ${ENV_VAR_NAME:default_value} or $ENV_VAR_NAME
         Pattern p = Pattern.compile("\\$\\{(\\w+)(:(.*))?\\}|\\$(\\w+)");
        Matcher m = p.matcher(input); // get a matcher object
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            String envVarName = null == m.group(1) ? m.group(4) : m.group(1);
            String defaultValue = null == m.group(3) ? "" : m.group(3);
            String envVarValue = System.getenv(envVarName);
            m.appendReplacement(sb, null == envVarValue ? defaultValue : envVarValue);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private DataSource getMockDataSource(String mock) {
        Class<?> c;
        try {
            c = Class.forName(mock);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundRuntimeException(e);
        }
        try {
            return (DataSource)c.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException e) {
            throw new InvocationTargetRuntimeException(DataSource.class, e);
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodRuntimeException(DataSource.class, "getDeclaredConstructor", new Class[0], e);
        } catch (InstantiationException e) {
            throw new InstantiationRuntimeException(DataSource.class, e);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(DataSource.class, e);
        }
    }

    private DbmsDialect getDialect(String dialectName) {
        Class<?> clazz;
        try {
            clazz = Class.forName("org.seasar.extension.jdbc.dialect." + dialectName);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundRuntimeException(e);
        }

        try {
            return (DbmsDialect)clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new InstantiationRuntimeException(DbmsDialect.class, e);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(DbmsDialect.class, e);
        } catch (InvocationTargetException e) {
            throw new InvocationTargetRuntimeException(DbmsDialect.class, e);
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodRuntimeException(DbmsDialect.class, "getDeclaredConstructor", new Class[0], e);
        }
    }

    static public synchronized JdbcManager getJdbcManager() {
        return new NoS2JdbcManager(null, null).jdbcManager;
    }

    static public synchronized JdbcManager getJdbcManager(String propertiesFilename, DataSource ds) {
        return new NoS2JdbcManager(propertiesFilename, ds).jdbcManager;
    }

    static public synchronized JdbcManager getJdbcManager(String propertiesFilename) {
        return new NoS2JdbcManager(propertiesFilename, null).jdbcManager;
    }

    static public synchronized JdbcManager getJdbcManager(DataSource ds) {
        return new NoS2JdbcManager(null, ds).jdbcManager;
    }

}
