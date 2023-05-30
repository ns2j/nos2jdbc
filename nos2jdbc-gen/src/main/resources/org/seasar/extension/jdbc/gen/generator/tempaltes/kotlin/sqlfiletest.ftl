<#import "/lib.ftl" as lib>
<#import "spring.ftl" as spring>
<#if lib.copyright??>
${lib.copyright}
</#if>
<#if !lib.copyright??>
<#include "/copyright.ftl">
</#if>
<#if packageName??>
package ${packageName}
</#if>

import java.io.InputStream
import java.io.Reader
import java.sql.Connection
import java.sql.PreparedStatement
import jakarta.annotation.Generated

import org.seasar.extension.jdbc.JdbcManager
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor
import org.seasar.extension.jdbc.util.ConnectionUtil
import org.seasar.framework.exception.ResourceNotFoundRuntimeException
import org.seasar.framework.util.InputStreamReaderUtil
import org.seasar.framework.util.PreparedStatementUtil
import org.seasar.framework.util.ReaderUtil
import org.seasar.framework.util.ResourceUtil
import org.seasar.framework.util.StatementUtil

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
@Generated(value = [<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>], date = "${currentDate?datetime?string["yyyy/MM/dd HH:mm:ss"]}")
class ${shortClassName} {
<#if componentType == "cdi" || componentType == "ejb">
    @Inject
    lateinit var ${jdbcManagerName}: JdbcManagerImplementor
</#if>
<#if componentType == "spring">
    @Autowired
    lateinit var ${jdbcManagerName}: JdbcManager
</#if>
<#if componentType == "none">
    var JdbcManager ${jdbcManagerName}
</#if>

<#if sqlFilePathList?size == 0>

    /**
     * SQLファイルがひとつもありません。
     * ダミーのテストメソッドです。
     * 
     * @throws Exception
     */
    @Test
    fun test() {
    }
<#else>
  <#list sqlFilePathList as path>

    /**
     * SQLファイルをテストします。
     * 
     * @throws Exception
     */
    @Test
    fun SqlFile${path_index}() {
        val path: String = "${path}"
        SqlFile(path).execute()
    }
  </#list>
</#if>

    /**
     * SQLファイルを表すクラスです。
     * 
     * @author S2JDBC-Gen
     */
    inner class SqlFile(path: String) {

        /** SQL */
        protected var sql: String? = null

        /** 内部的なJDBCマネジャ */
        protected lateinit var implementor: JdbcManagerImplementor

        /**
         * インスタンスを構築します。
         * 
         * @param path
         *            SQLファイルのパス
         */
        init {
            implementor = jdbcManager as JdbcManagerImplementor
            this.sql = getSql(path)
        }

        /**
         * SQLを返します。
         * 
         * @param path
         *            SQLファイルのパス
         * @return SQL
         */
        protected fun getSql(path0: String) : String {
            var path = path0
            if (path.endsWith(".sql")) {
                path = path.substring(0, path.length - 4)
            }
            var dbmsName: String? = implementor.getDialect().getName()
            if (dbmsName != null) {
                var sql: String? = readSql(path + "_" + dbmsName)
                if (sql != null) {
                    return sql
                }
            }
            var sql: String? = readSql(path)
            if (sql != null) {
                return sql
            }
            throw ResourceNotFoundRuntimeException(path)
        }

        /**
         * SQLをファイルから読み取ります。
         * 
         * @param path
         *            SQLファイルのパス
         * @return SQL
         */
        protected fun readSql(path: String) : String? {
            var is0: InputStream? = ResourceUtil.getResourceAsStreamNoException(path, "sql")
            if (is0 == null) {
                return null
            }
            val reader: Reader = InputStreamReaderUtil.create(is0, "UTF-8")
            var sql: String = ReaderUtil.readText(reader)
            if (sql.length > 0 && sql[0] == '\uFEFF') {
                sql = sql.substring(1)
            }
            sql = sql.trim()
            if (sql.endsWith(";")) {
                sql = sql.substring(0, sql.length - 1)
            }
            return sql
        }

        /**
         * SQLを実行します。
         */
        fun execute() {
            System.out.println(sql)
            val connection: Connection = org.seasar.extension.jdbc.util.DataSourceUtil
                    .getConnection(implementor.getDataSource())
            try {
                val ps: PreparedStatement = ConnectionUtil.prepareStatement(
                        connection, sql)
                try {
                    PreparedStatementUtil.execute(ps)
                } finally {
                    StatementUtil.close(ps)
                }
            } finally {
                ConnectionUtil.close(connection)
            }
        }
    }

<#if componentType == "cdi" || componentType == "ejb">
    @Deployment
    fun createTestArchive(): Archive<?> {
	return ArchiveTestUtil.createTestArchive()
    }
</#if>
}