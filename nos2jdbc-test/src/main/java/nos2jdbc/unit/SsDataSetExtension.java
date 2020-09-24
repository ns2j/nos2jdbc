package nos2jdbc.unit;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Optional;
import javax.sql.DataSource;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.impl.SqlWriter;
import org.seasar.extension.dataset.impl.SsReader;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.util.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SsDataSetExtension implements ParameterResolver, BeforeAllCallback, BeforeTestExecutionCallback {
    final private static Logger logger = LoggerFactory.getLogger(SsDataSetExtension.class);

    private final ExtensionContext.Namespace namespace = ExtensionContext.Namespace.create(getClass());
    private final String INSERTION_KEY = "INSERTION";
    private final String READING_KEY = "READING";
    private final String READING_INDEX_KEY = "READING_INDEX_KEY";

    private ExtensionContext.Store getStore(final ExtensionContext context) {
        return context.getStore(namespace);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        Class<?> c = parameterContext.getParameter().getType();
        if (c == DataSet.class || c == DataSetOpe.class)
            return true;
        return false;
    }


    private DataSet resolveSsInsertion(SsInsertion si, ExtensionContext extensionContext) {
        DataSet dataSet = (DataSet)getStore(extensionContext).remove(INSERTION_KEY);
        if (dataSet != null) {
            if (!si.value().equals(""))
                logger.warn("SsInsertion annotation value is ignored. value=" + si.value());
            return dataSet;
        } else {
            dataSet = new SsReader(si.value(), si.trim()).read();
            writeDataSet(dataSet, extensionContext, si.resetSequeanceFolder());
            return dataSet;
        }
    }
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter p = parameterContext.getParameter();
        if (p.getType() == DataSet.class) {
            SsInsertion si = p.getAnnotation(SsInsertion.class);
            if (si != null)
                return resolveSsInsertion(si, extensionContext);
            SsReading psr = p.getAnnotation(SsReading.class);
            if (psr != null)
                return new SsReader(psr.value(), psr.trim()).read();
            
            Store store = getStore(extensionContext);
            SsReading[] srs = (SsReading[])store.get(READING_KEY);
            if (srs == null)
                return null;
            int ind = (int)store.get(READING_INDEX_KEY);
            if (ind >= srs.length)
                return null;
            SsReading sr = srs[ind];
            store.put(READING_INDEX_KEY, ++ind);
            return new SsReader(sr.value(), sr.trim()).read();
        }
        if (p.getType() == DataSetOpe.class) {
            return findDataSource(extensionContext)
                    .map(jm -> new DataSetOpe(jm))
                    .get();
        }

        return null;
    }

    private Optional<JdbcManager> findDataSource(ExtensionContext extensionContext) {
        Optional<Class<?>> hasJdbcManagerClass = extensionContext.getTestClass()
                .map(this::findClassHasJdbcManager);
        Object testInstance = extensionContext.getRequiredTestInstances().findInstance(hasJdbcManagerClass.get()).get();

        return hasJdbcManagerClass
                .map(this::getJdbcManagerField)
                .map(f -> getJdbcManagerInstance(f, testInstance));
    }

    private Class<?> findClassHasJdbcManager(Class<?> clazz) {
        for (Class<?> c = clazz; c != null; c = c.getEnclosingClass())
            for (Field f: c.getDeclaredFields())
                if (f.getType() == JdbcManager.class)
                    return c;
        throw new SsDataSetExtensionException("No Class which has JdbcManager field present");
    }

    private Field getJdbcManagerField(Class<?> clazz) {
        for (Field f: clazz.getDeclaredFields())
            if (f.getType() == JdbcManager.class)
                return f;
        throw new SsDataSetExtensionException("No JdbcManager field present");
    }

    private JdbcManager getJdbcManagerInstance(Field f, Object i) {
        f.setAccessible(true);
        JdbcManager jm = null;
        try {
            jm = (JdbcManager)f.get(i);
            if (jm == null)
                throw new SsDataSetExtensionException("JdbcManager is null");
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new SsDataSetExtensionException(e);
        }
        return jm;
    }

    private Optional<Class<?>> findClassHasSsInsertion(Class<?> clazz) {
        for (Class<?> c = clazz; c != null; c = c.getEnclosingClass()) {
            SsInsertion a = c.getAnnotation(SsInsertion.class);
            if (a != null)
                return Optional.of(c);
        }
        return Optional.empty();
    }

    private void writeDataSet(DataSet dataSet, ExtensionContext extensionContext, String rsFolder) {
        findDataSource(extensionContext).ifPresent(jm -> {
            DataSource ds = ((JdbcManagerImplementor)jm).getDataSource();
            new SqlWriter(ds).write(dataSet);
            if (rsFolder == null || "".equals(rsFolder)) return;
            File rsFile = ResourceUtil.getResourceAsFile(rsFolder);
            logger.debug("folder: " + rsFile);
            for (File f: rsFile.listFiles()) {
                logger.debug("file: " + f.getName());
                jm.callBySqlFile(rsFolder + "/" + f.getName(), null).execute();
            }
        });
    }

    private void insert(SsInsertion si, ExtensionContext extensionContext) {
        DataSet dataSet = new SsReader(si.value(), si.trim()).read();
        writeDataSet(dataSet, extensionContext, si.resetSequeanceFolder());
        getStore(extensionContext).put(INSERTION_KEY, dataSet);
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        extensionContext.getTestMethod().ifPresent(m -> {
            SsInsertion si = m.getAnnotation(SsInsertion.class);
            if (si != null)
                insert(si, extensionContext);
        });

        if (getStore(extensionContext).get(INSERTION_KEY) == null) {
            findClassHasSsInsertion(extensionContext.getTestClass().get())
            .map(c -> c.getAnnotation(SsInsertion.class))
            .ifPresent(si -> insert(si, extensionContext));
        }
        
        extensionContext.getTestMethod().ifPresent(m -> {
            SsReading[] srs = m.getAnnotationsByType(SsReading.class);
            if (srs == null || srs.length == 0)
                return;
            
            getStore(extensionContext).put(READING_KEY, srs);
            getStore(extensionContext).put(READING_INDEX_KEY, 0);
        });
    }

    public class SsDataSetExtensionException extends RuntimeException {
        public SsDataSetExtensionException() {
            super();
        }
        public SsDataSetExtensionException(String message) {
            super(message);
        }
        public SsDataSetExtensionException(Throwable cause) {
            super(cause);
        }
        public SsDataSetExtensionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
