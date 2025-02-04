<#import "/lib.ftl" as lib>
<#if lib.copyright??>
${lib.copyright}
</#if>
<#if !lib.copyright??>
<#include "/copyright.ftl">
</#if>
<#if packageName??>
package ${packageName};
</#if>

<#list importNameSet as importName>
import ${importName};
</#list>
<#if staticImportNameSet?size gt 0>

  <#list staticImportNameSet as importName>
import static ${importName};
  </#list>
</#if>
import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;

<#if componentType == "cdi">
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
</#if>
<#if componentType == "spring">
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
</#if>

import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.SqlFileSelect;
import org.seasar.extension.jdbc.SqlFileUpdate;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.parameter.Parameter;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.GenericUtil;

/**
 * サービスの抽象クラスです。
 * 
<#if lib.author??>
 * @author ${lib.author}
</#if>
 * @param <ENTITY>
 *            エンティティの型 
 */
@Generated(value = {<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>}, date = "${currentDate?datetime?string["yyyy/MM/dd HH:mm:ss"]}")
@Transactional
public abstract class ${shortClassName}<T> {

    /**
     * JDBCマネージャです。
     */
<#if componentType == "cdi">
    @Inject
</#if>
<#if componentType == "spring">
    @Autowired
</#if>
    protected JdbcManager jdbcManager;

    /**
     * エンティティのクラスです。
     */
    protected Class<T> entityClass;

    /**
     * SQLファイルのパスのプレフィックスです。
     */
    protected String sqlFilePathPrefix;

    /**
     * コンストラクタです。
     * 
     */
    @SuppressWarnings("unchecked")
    public ${shortClassName}() {
        Map<TypeVariable<?>, Type> map = GenericUtil
                .getTypeVariableMap(getClass());
        for (Class<?> c = getClass(); c != Object.class; c = c.getSuperclass()) {
            if (c.getSuperclass() == ${shortClassName}.class) {
                Type type = c.getGenericSuperclass();
                Type[] arrays = GenericUtil.getGenericParameter(type);
                setEntityClass((Class<T>) GenericUtil.getActualClass(arrays[0],
                        map));
                break;
            }
        }
    }

    /**
     * コンストラクタです。
     * 
     * @param entityClass
     *            エンティティのクラス
     */
    public ${shortClassName}(Class<T> entityClass) {
        setEntityClass(entityClass);
    }
<#if componentType="none">

    /**
     * JDBCマネージャを設定します。
     * 
     * @param jdbcManager
     *            JDBCマネージャ
     */
    public void setJdbcManager(JdbcManager jdbcManager) {
        this.jdbcManager = jdbcManager;
    }
</#if>

    /**
     * 自動検索を返します。
     * 
     * @return 自動検索
     */
    protected AutoSelect<T> select() {
        return jdbcManager.from(entityClass);
    }

    /**
     * すべてのエンティティを検索します。
     * 
     * @return すべてのエンティティ
     */
    public List<T> findAll() {
        return select().getResultList();
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
    public List<T> findByCondition(BeanMap conditions) {
        return select().where(conditions).getResultList();
    }

    /**
     * 件数を返します。
     * 
     * @return 件数
     */
    public long getCount() {
        return select().getCount();
    }

    /**
     * エンティティを挿入します。
     * 
     * @param entity
     *            エンティティ
     * @return 更新した行数
     */
    public int insert(T entity) {
        return jdbcManager.insert(entity).execute();
    }

    /**
     * エンティティを更新します。
     * 
     * @param entity
     *            エンティティ
     * @return 更新した行数
     */
    public int update(T entity) {
        return jdbcManager.update(entity).execute();
    }

    /**
     * エンティティをidがnullの場合は挿入し、null以外の場合は更新します。
     * 
     * @param entity
     *            エンティティ
     * @return 更新した行数
     */
    public int upsert(T entity) {
        final JdbcManagerImplementor jmi = (JdbcManagerImplementor)jdbcManager;
        final EntityMeta entityMeta = jmi.getEntityMetaFactory().getEntityMeta(entity.getClass());
        for (final PropertyMeta propertyMeta : entityMeta.getAllColumnPropertyMeta()) {
            final Object value = FieldUtil.get(propertyMeta.getField(), entity);
            if (propertyMeta.isId()) {
                if (value != null) {
                    return update(entity);
                }
            }
        }
        return insert(entity);
    }

    /**
     * エンティティを削除します。
     * 
     * @param entity
     *            エンティティ
     * @return 更新した行数
     */
    public int delete(T entity) {
        return jdbcManager.delete(entity).execute();
    }

    public int[] insertBatch(List<T> entity) {
        return jdbcManager.insertBatch(entity).execute();
    }
    public int[] updateBatch(List<T> entity) {
        return jdbcManager.updateBatch(entity).execute();
    }
    public int[] deleteBatch(List<T> entity) {
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
    protected <T2> SqlFileSelect<T2> selectBySqlFile(Class<T2> baseClass,
            String path) {
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
    protected <T2> SqlFileSelect<T2> selectBySqlFile(Class<T2> baseClass,
            String path, Object parameter) {
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
    protected SqlFileUpdate updateBySqlFile(String path) {
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
    protected SqlFileUpdate updateBySqlFile(String path, Object parameter) {
        return jdbcManager.updateBySqlFile(sqlFilePathPrefix + path, parameter);
    }

    /**
     * エンティティのクラスを設定します。
     * 
     * @param entityClass
     *            エンティティのクラス
     */
    protected void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
        sqlFilePathPrefix = "META-INF/sql/"
                + StringUtil.replace(entityClass.getName(), ".", "/") + "/";
    }
}