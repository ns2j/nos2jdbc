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

<#list importNameSet as importName>
import ${importName};
</#list>
<#if componentType == "cdi" || componentType == "ejb">
<@cdi.importForTest/>
</#if>
<#if componentType == "spring">
<@spring.importForTest/>
</#if>
<#if staticImportNameSet?size gt 0>

  <#list staticImportNameSet as importName>
import static ${importName};
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
@Generated(value = {<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>}, date = "${currentDate?datetime?string["yyyy/MM/dd HH:mm:ss"]}")
public class ${shortClassName} {
<#if componentType == "cdi" || componentType == "ejb">
    @Inject
</#if>
<#if componentType == "spring">
    @Autowired
</#if>
    private JdbcManager ${jdbcManagerName};

<#if idExpressionList?size == 0>
    /**
     * 全件取得をテストします。
     * 
     * @throws Exception
     */
     @Test
    public void testFindAll() throws Exception {
        ${jdbcManagerName}.from(${shortEntityClassName}.class).getResultList();
    }
<#else>
    /**
     * 識別子による取得をテストします。
     * 
     * @throws Exception
     */
    @Test
    public void testFindById() throws Exception {
        ${jdbcManagerName}.from(${shortEntityClassName}.class).id(<#list idExpressionList as idExpression>${idExpression}<#if idExpression_has_next>, </#if></#list>).getSingleResult();
    }
  <#if namesModel??>
    <#list namesModel.namesAssociationModelList as namesAssociationModel>

    /**
     * ${namesAssociationModel.name}との外部結合をテストします。
     * 
     * @throws Exception
     */
    @Test
    public void testLeftOuterJoin_${namesAssociationModel.name}() throws Exception {
        ${jdbcManagerName}.from(${shortEntityClassName}.class).leftOuterJoin(${namesAssociationModel.name}()).id(<#list idExpressionList as idExpression>${idExpression}<#if idExpression_has_next>, </#if></#list>).getSingleResult();
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
    public void testLeftOuterJoin_${associationName}() throws Exception {
        ${jdbcManagerName}.from(${shortEntityClassName}.class).leftOuterJoin("${associationName}").id(<#list idExpressionList as idExpression>${idExpression}<#if idExpression_has_next>, </#if></#list>).getSingleResult();
    }
    </#list>
  </#if>
</#if>

<#if componentType == "cdi" || componentType == "ejb">
    @Deployment
    public static Archive<?> createTestArchive() {
	return ArchiveTestUtil.createTestArchive();
    }
</#if>
}