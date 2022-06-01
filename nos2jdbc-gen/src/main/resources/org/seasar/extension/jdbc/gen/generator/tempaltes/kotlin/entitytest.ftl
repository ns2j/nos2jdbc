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
<#if componentType == "cdi" || componentType == "ejb">
<@cdi.importForTest/>
</#if>
<#if componentType == "spring">
<@spring.importForTest/>
</#if>
<#if staticImportNameSet?size gt 0>

  <#list staticImportNameSet as importName>
import ${importName}
  </#list>
</#if>

/**
 * {@link ${shortEntityClassName}}のテストクラスです。
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
</#if>
<#if componentType == "spring">
    @Autowired
</#if>
    lateinit var ${jdbcManagerName}: JdbcManager

<#if idExpressionList?size == 0>
    /**
     * 全件取得をテストします。
     */
     @Test
    fun findAll() {
        ${jdbcManagerName}.from(${shortEntityClassName}::class.java).getResultList()
    }
<#else>
    /**
     * 識別子による取得をテストします。
     */
    @Test
    fun findById() {
        ${jdbcManagerName}.from(${shortEntityClassName}::class.java).id(<#list idExpressionList as idExpression>${idExpression}<#if idExpression_has_next>, </#if></#list>).getSingleResult()
    }
  <#if namesModel??>
    <#list namesModel.namesAssociationModelList as namesAssociationModel>

    /**
     * ${namesAssociationModel.name}との外部結合をテストします。
     */
    @Test
    fun leftOuterJoin_${namesAssociationModel.name}() {
        ${jdbcManagerName}.from(${shortEntityClassName}::class.java).leftOuterJoin(${namesAssociationModel.name}()).id(<#list idExpressionList as idExpression>${idExpression}<#if idExpression_has_next>, </#if></#list>).getSingleResult()
    }
    </#list>
  <#else>
    <#list associationNameList as associationName>

    /**
     * ${associationName}との外部結合をテストします。
     * 
     * @throws Exception
     */
    @Test
    leftOuterJoin_${associationName}() {
        ${jdbcManagerName}.from(${shortEntityClassName}::class.java).leftOuterJoin("${associationName}").id(<#list idExpressionList as idExpression>${idExpression}<#if idExpression_has_next>, </#if></#list>).getSingleResult()
    }
    </#list>
  </#if>
</#if>

<#if componentType == "cdi" || componentType == "ejb">
    @Deployment
    fun createTestArchive() : Archive<Any> {
	return ArchiveTestUtil.createTestArchive()
    }
</#if>
}