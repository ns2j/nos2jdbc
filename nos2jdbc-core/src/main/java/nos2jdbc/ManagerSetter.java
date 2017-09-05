package nos2jdbc;

import javax.sql.DataSource;
import javax.transaction.TransactionSynchronizationRegistry;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import static org.seasar.framework.util.StringUtil.*;

public class ManagerSetter {
    static synchronized public JdbcManager setToJdbcManagerImpl(JdbcManagerImpl jdbcManagerImpl, 
    		DataSource ds, DbmsDialect dialect, TransactionSynchronizationRegistry sr) {
        jdbcManagerImpl.setDataSource(ds);
        jdbcManagerImpl.setDialect(dialect);
        jdbcManagerImpl.setSyncRegistry(sr);
        PersistenceConventionImpl pc = new PersistenceConventionImpl();
        TableMetaFactoryImpl tmf = new TableMetaFactoryImpl();
        tmf.setPersistenceConvention(pc);
        EntityMetaFactoryImpl emf = new EntityMetaFactoryImpl();
        emf.setPersistenceConvention(pc);
        emf.setTableMetaFactory(tmf);
        PropertyMetaFactoryImpl pmf = new PropertyMetaFactoryImpl();
        pmf.setPersistenceConvention(pc);
        ColumnMetaFactoryImpl cmf = new ColumnMetaFactoryImpl();
        cmf.setPersistenceConvention(pc);
        pmf.setColumnMetaFactory(cmf);
        emf.setPropertyMetaFactory(pmf);
        jdbcManagerImpl.setEntityMetaFactory(emf);
        jdbcManagerImpl.setPersistenceConvention(pc);

        String enumTypeValueOldStr = ltrim(rtrim(System.getProperty("isEnumValueTypeOld"))); 
        if (equalsIgnoreCase(enumTypeValueOldStr, "true")) {
            try {
        	ValueTypes.setEnumDefaultValueType(org.seasar.extension.jdbc.types.EnumType.class);
            } catch (NoSuchMethodException nsme) {
        	nsme.printStackTrace();
            }
        }
        
        return jdbcManagerImpl;
    }
}
