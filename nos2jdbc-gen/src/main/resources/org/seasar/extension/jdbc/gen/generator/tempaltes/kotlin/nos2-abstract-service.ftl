<#import "/lib.ftl" as lib>
<#if lib.copyright??>
${lib.copyright}
</#if>
<#if !lib.copyright??>
<#include "/copyright.ftl">
</#if>
<#if packageName??>
package ${packageName}
</#if>

<#list importNameSet as importName>
import ${importName}
</#list>
<#if staticImportNameSet?size gt 0>

  <#list staticImportNameSet as importName>
import static ${importName}
  </#list>
</#if>
import java.io.Serializable
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable

<#if componentType == "cdi">
import javax.inject.Inject
</#if>
<#if componentType == "spring">
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
</#if>

import org.seasar.extension.jdbc.AutoSelect
import org.seasar.extension.jdbc.JdbcManager
import org.seasar.extension.jdbc.SqlFileSelect
import org.seasar.extension.jdbc.SqlFileUpdate
import org.seasar.extension.jdbc.parameter.Parameter
import org.seasar.framework.beans.util.BeanMap
import org.seasar.framework.util.StringUtil
import org.seasar.framework.util.GenericUtil

/**
 * サービスの抽象クラスです。
 * 
<#if lib.author??>
 * @author ${lib.author}
</#if>
 * @param <ENTITY>
 *            エンティティの型 
 */
@Generated(value = [<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>], date = "${currentDate?datetime?string["yyyy/MM/dd HH:mm:ss"]}")
@Service
open class ${shortClassName}<T>() {

    /**
     * JDBCマネージャです。
     */
<#if componentType == "cdi">
    @Inject
</#if>
<#if componentType == "spring">
    @Autowired
</#if>
    lateinit var jdbcManager: JdbcManager

    /**
     * エンティティのクラスです。
     */
    var entityClass: Class<T>? = null
    set(value) {
	field = value;
        sqlFilePathPrefix = "META-INF/sql/" +
             StringUtil.replace(value?.getName(), ".", "/") + "/";
    }

    /**
     * SQLファイルのパスのプレフィックスです。
     */
    var sqlFilePathPrefix: String? = null;
 
    init {
        var map: Map<TypeVariable<*>, Type> = GenericUtil
                .getTypeVariableMap(this.javaClass);
        var c: Class<in Any> = this.javaClass
	while(c != Any::class.java) {
		println("c: " + c + ",c.superclass: " + c.superclass);
	    if (NoS2AbstractServiceBase::class.java as Class<in Any> == c.superclass) {
                val type: Type = c.genericSuperclass
		    println("type: " + type)
               
                val arrays: Array<Type> = GenericUtil.getGenericParameter(type)
                entityClass = GenericUtil.getActualClass(arrays[0],
                        map) as Class<T>
                        break
		
	    }
	    c = c.superclass
	}
    }
<#if componentType="none">

    /**
     * JDBCマネージャを設定します。
     * 
     * @param jdbcManager
     *            JDBCマネージャ
     */
    fun setJdbcManager(jdbcManager: JdbcManager) {
        this.jdbcManager = jdbcManager;
    }
</#if>

    /**
     * 自動検索を返します。
     * 
     * @return 自動検索
     */
    protected fun select() : AutoSelect<T> {
        return jdbcManager.from(entityClass)
    }

    /**
     * すべてのエンティティを検索します。
     * 
     * @return すべてのエンティティ
     */
    fun findAll() : List<T> {
        return select().getResultList()
    }

    /**
     * 条件付で検索します。
     * 
     * @param conditions
     *            条件
     * 
     * @return エンティティのリスト
     * @see AutoSelect#where(Map)
     */
    fun findByCondition(conditions: BeanMap) :  List<T> {
        return select().where(conditions).getResultList()
    }

    /**
     * 件数を返します。
     * 
     * @return 件数
     */
    fun getCount() : Long {
        return select().getCount()
    }

    /**
     * エンティティを挿入します。
     * 
     * @param entity
     *            エンティティ
     * @return 更新した行数
     */
    fun insert(entity: T) : Int {
        return jdbcManager.insert(entity).execute();
    }

    /**
     * エンティティを更新します。
     * 
     * @param entity
     *            エンティティ
     * @return 更新した行数
     */
    fun update(entity: T) : Int{
        return jdbcManager.update(entity).execute();
    }

    /**
     * エンティティを削除します。
     * 
     * @param entity
     *            エンティティ
     * @return 更新した行数
     */
    fun delete(entity: T) : Int {
        return jdbcManager.delete(entity).execute();
    }

    fun insertBatch(entity: List<T>) : IntArray {
        return jdbcManager.insertBatch(entity).execute();
    }
    fun updateBatch(entity: List<T>) : IntArray {
        return jdbcManager.updateBatch(entity).execute();
    }
    fun deleteBatch(entity: List<T>) : IntArray {
        return jdbcManager.deleteBatch(entity).execute();
    }

    /**
     * SQLファイル検索を返します。
     * 
     * @param <T2>
     *            戻り値のJavaBeansの型
     * @param baseClass
     *            戻り値のJavaBeansのクラス
     * @param path
     *            エンティティのディレクトリ部分を含まないSQLファイルのパス
     * @return SQLファイル検索
     */
    protected fun <T2> selectBySqlFile(baseClass: Class<T2>,
            path: String) : SqlFileSelect<T2> {
        return jdbcManager.selectBySqlFile(baseClass, sqlFilePathPrefix + path);
    }

    /**
     * SQLファイル検索を返します。
     * 
     * @param <T2>
     *            戻り値のJavaBeansの型
     * @param baseClass
     *            戻り値のJavaBeansのクラス
     * @param path
     *            エンティティのディレクトリ部分を含まないSQLファイルのパス
     * @param parameter
     *            <p>
     *            パラメータ。
     *            </p>
     *            <p>
     *            パラメータが1つしかない場合は、値を直接指定します。 パラメータが複数ある場合は、JavaBeansを作って、
     *            プロパティ名をSQLファイルのバインド変数名とあわせます。
     *            JavaBeansはpublicフィールドで定義することもできます。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link Date}、{@link Calendar}のいずれか場合、{@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link String}、<code>ｂyte[]</code>、{@link Serializable}のいずれかの場合、{@link Parameter}に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return SQLファイル検索
     */
    protected fun <T2> selectBySqlFile(baseClass: Class<T2>,
            path: String, parameter: Any) : SqlFileSelect<T2> {
        return jdbcManager.selectBySqlFile(baseClass, sqlFilePathPrefix + path,
                parameter);
    }

    /**
     * SQLファイル更新を返します。
     * 
     * @param path
     *            エンティティのディレクトリ部分を含まないSQLファイルのパス
     * @return SQLファイル更新
     */
    protected fun updateBySqlFile(path: String) : SqlFileUpdate {
        return jdbcManager.updateBySqlFile(sqlFilePathPrefix + path);
    }

    /**
     * SQLファイル更新を返します。
     * 
     * @param path
     *            エンティティのディレクトリ部分を含まないSQLファイルのパス
     * @param parameter
     *            パラメータ用のJavaBeans
     * 
     * @return SQLファイル更新
     */
    protected fun updateBySqlFile(path: String, parameter: Any) : SqlFileUpdate {
        return jdbcManager.updateBySqlFile(sqlFilePathPrefix + path, parameter);
    }
}