<#import "/lib.ftl" as lib>
<#import "cdi.ftl" as cdi>
<#import "spring.ftl" as spring>
<#if lib.copyright??>
${lib.copyright}
</#if>
<#if !lib.copyright??>
<#include "/copyright.ftl">
</#if>
<#if packageName??>
package ${packageName};
</#if>

import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.annotation.Generated;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.framework.exception.ResourceNotFoundRuntimeException;
import org.seasar.framework.util.InputStreamReaderUtil;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.ReaderUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StatementUtil;

<#if componentType == "cdi" || componentType == "ejb">
<@cdi.importForTest/>
</#if>
<#if componentType == "spring">
<@spring.importForTest/>
</#if>

/**
 * SQLファイルのテストクラスです。
 * <p>
 * このファイルは修正されることを意図していません。
 * SQLファイルのテストを独自に行いたい場合は、サービスやエンティティのテストクラスを使用してください。
 * </p>
 *
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
<#if componentType == "cdi" || componentType == "ejb">
@ExtendWith(ArquillianExtension.class)
</#if>
<#if componentType == "spring">
@SpringBootTest
</#if>
@Generated(value = {<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>}, date = "${currentDate?datetime?string["yyyy/MM/dd HH:mm:ss"]}")
public class ${shortClassName} {
<#if componentType == "cdi" || componentType == "ejb">
    @Inject
    private JdbcManagerImplementor ${jdbcManagerName};
</#if>
<#if componentType == "spring">
    @Autowired
    private JdbcManager ${jdbcManagerName};
</#if>
<#if componentType == "none">
    private JdbcManager ${jdbcManagerName};
</#if>

<#if sqlFilePathList?size == 0>

    /**
     * SQLファイルがひとつもありません。
     * ダミーのテストメソッドです。
     * 
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
    }
<#else>
  <#list sqlFilePathList as path>

    /**
     * SQLファイルをテストします。
     * 
     * @throws Exception
     */
    @Test
    public void testSqlFile${path_index}() throws Exception {
        String path = "${path}";
        new SqlFile(path).execute();
    }
  </#list>
</#if>

    /**
     * SQLファイルを表すクラスです。
     * 
     * @author S2JDBC-Gen
     */
    public class SqlFile {

        /** SQL */
        protected String sql;

        /** 内部的なJDBCマネジャ */
        protected JdbcManagerImplementor implementor;

        /**
         * インスタンスを構築します。
         * 
         * @param path
         *            SQLファイルのパス
         */
        public SqlFile(String path) {
            implementor = JdbcManagerImplementor.class.cast(${jdbcManagerName});
            this.sql = getSql(path);
        }

        /**
         * SQLを返します。
         * 
         * @param path
         *            SQLファイルのパス
         * @return SQL
         */
        protected String getSql(String path) {
            if (path.endsWith(".sql")) {
                path = path.substring(0, path.length() - 4);
            }
            String dbmsName = implementor.getDialect().getName();
            if (dbmsName != null) {
                String sql = readSql(path + "_" + dbmsName);
                if (sql != null) {
                    return sql;
                }
            }
            String sql = readSql(path);
            if (sql != null) {
                return sql;
            }
            throw new ResourceNotFoundRuntimeException(path);
        }

        /**
         * SQLをファイルから読み取ります。
         * 
         * @param path
         *            SQLファイルのパス
         * @return SQL
         */
        protected String readSql(String path) {
            InputStream is = ResourceUtil.getResourceAsStreamNoException(path,
                    "sql");
            if (is == null) {
                return null;
            }
            Reader reader = InputStreamReaderUtil.create(is, "UTF-8");
            String sql = ReaderUtil.readText(reader);
            if (sql.length() > 0 && sql.charAt(0) == '\uFEFF') {
                sql = sql.substring(1);
            }
            sql = sql.trim();
            if (sql.endsWith(";")) {
                sql = sql.substring(0, sql.length() - 1);
            }
            return sql;
        }

        /**
         * SQLを実行します。
         */
        public void execute() {
            System.out.println(sql);
            Connection connection = org.seasar.extension.jdbc.util.DataSourceUtil
                    .getConnection(implementor.getDataSource());
            try {
                PreparedStatement ps = ConnectionUtil.prepareStatement(
                        connection, sql);
                try {
                    PreparedStatementUtil.execute(ps);
                } finally {
                    StatementUtil.close(ps);
                }
            } finally {
                ConnectionUtil.close(connection);
            }
        }
    }

<#if componentType == "cdi" || componentType == "ejb">
    @Deployment
    public static Archive<?> createTestArchive() {
	return ArchiveTestUtil.createTestArchive();
    }
</#if>
}