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

<#list importNameSet as importName>
import ${importName}
</#list>
<#if staticImportNameSet?size gt 0>

  <#list staticImportNameSet as importName>
import ${importName}
  </#list>
</#if>

<#if componentType == "cdi">
<@cdi.importForService/>
</#if>
<#if componentType == "ejb">
<@ejb.importForService/>
</#if>
<#if componentType == "spring">
<@spring.importForService/>
</#if>

/**
 * {@link ${shortEntityClassName}}のサービスクラスです。
 * 
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
@Generated(value = [<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>], date = "${currentDate?datetime?string["yyyy/MM/dd HH:mm:ss"]}")
<#if componentType == "cdi">
<@cdi.annotationForService/>
</#if>
<#if componentType == "ejb">
<@ejb.annotationForService/>
</#if>
<#if componentType == "spring">
<@spring.annotationForService/>
</#if>
open class ${shortClassName} : ${shortSuperclassName}<${shortEntityClassName}>() {
<#if jdbcManagerSetterNecessary>

    /**
     * JDBCマネージャを設定します。
     * 
     * @param jdbcManager
     *            JDBCマネージャ
     */
    @Resource(name = "${jdbcManagerName}")
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void setJdbcManager(jdbcManager: JdbcManager) {
        this.jdbcManager = jdbcManager
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
    fun findById(<#list idPropertyMetaList as prop>${prop.name}: ${prop.propertyClass.simpleName}<#if prop_has_next>, </#if></#list>) : ${shortEntityClassName} {
        return select().id(<#list idPropertyMetaList as prop>${prop.name}<#if prop_has_next>, </#if></#list>).getSingleResult()
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
    fun findByIdVersion(<#list idPropertyMetaList as prop>${prop.name}: ${getKotlinTypeName(prop.propertyClass.simpleName)}, </#list>${versionPropertyMeta.name}: ${getKotlinTypeName(versionPropertyMeta.propertyClass.simpleName)}) : ${shortEntityClassName} {
        return select().id(<#list idPropertyMetaList as prop>${getKotlinTypeName(prop.name)}<#if prop_has_next>, </#if></#list>).version(${versionPropertyMeta.name}).getSingleResult()
    }
</#if>
<#if namesModel?? && idPropertyMetaList?size gt 0>

    /**
     * 識別子の昇順ですべてのエンティティを検索します。
     * 
     * @return エンティティのリスト
     */
    fun findAllOrderById() : List<${shortEntityClassName}> {
        return select().orderBy(<#list idPropertyMetaList as prop>asc(${prop.name}())<#if prop_has_next>, </#if></#list>).getResultList()
    }
</#if>
}