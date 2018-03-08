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

<#if componentType == "cdi">
import javax.enterprise.context.Dependent;
import javax.transaction.Transactional;
</#if>
<#if componentType == "ejb">
import javax.ejb.Stateless;
</#if>
<#if componentType == "spring">
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
</#if>

/**
 * {@link ${shortEntityClassName}}のサービスクラスです。
 * 
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
@Generated(value = {<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>}, date = "${currentDate?datetime}")
<#if componentType == "cdi">
@Dependent
@Transactional
@${serviceClassNameSuffix}Qualifier
</#if>
<#if componentType == "ejb">
@Stateless
</#if>
<#if componentType == "spring">
@Service
@Transactional
</#if>
public class ${shortClassName} extends ${shortSuperclassName}<${shortEntityClassName}> {
<#if jdbcManagerSetterNecessary>

    /**
     * JDBCマネージャを設定します。
     * 
     * @param jdbcManager
     *            JDBCマネージャ
     */
    @Resource(name = "${jdbcManagerName}")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void setJdbcManager(JdbcManager jdbcManager) {
        this.jdbcManager = jdbcManager;
    }
</#if>
<#if idPropertyMetaList?size gt 0>

    /**
     * 識別子でエンティティを検索します。
     * 
  <#list idPropertyMetaList as prop>
     * @param ${prop.name}
     *            識別子
  </#list>
     * @return エンティティ
     */
    public ${shortEntityClassName} findById(<#list idPropertyMetaList as prop>${prop.propertyClass.simpleName} ${prop.name}<#if prop_has_next>, </#if></#list>) {
        return select().id(<#list idPropertyMetaList as prop>${prop.name}<#if prop_has_next>, </#if></#list>).getSingleResult();
    }
</#if>
<#if idPropertyMetaList?size gt 0 && versionPropertyMeta??>

    /**
     * 識別子とバージョン番号でエンティティを検索します。
     * 
  <#list idPropertyMetaList as prop>
     * @param ${prop.name}
     *            識別子
  </#list>
     * @param ${versionPropertyMeta.name}
     *            バージョン番号
     * @return エンティティ
     */
    public ${shortEntityClassName} findByIdVersion(<#list idPropertyMetaList as prop>${prop.propertyClass.simpleName} ${prop.name}, </#list>${versionPropertyMeta.propertyClass.simpleName} ${versionPropertyMeta.name}) {
        return select().id(<#list idPropertyMetaList as prop>${prop.name}<#if prop_has_next>, </#if></#list>).version(${versionPropertyMeta.name}).getSingleResult();
    }
</#if>
<#if namesModel?? && idPropertyMetaList?size gt 0>

    /**
     * 識別子の昇順ですべてのエンティティを検索します。
     * 
     * @return エンティティのリスト
     */
    public List<${shortEntityClassName}> findAllOrderById() {
        return select().orderBy(<#list idPropertyMetaList as prop>asc(${prop.name}())<#if prop_has_next>, </#if></#list>).getResultList();
    }
</#if>
}