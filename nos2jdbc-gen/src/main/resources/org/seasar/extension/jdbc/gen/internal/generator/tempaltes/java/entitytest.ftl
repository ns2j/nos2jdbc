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
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
<#if componentType == "cdi" || componentType == "ejb">
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;

import ${rootPackageName}.ArchiveTestUtil;
</#if>
<#if componentType == "spring">
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
@RunWith(Arquillian.class)
</#if>
<#if componentType == "spring">
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {${springAppConfig}.class})
</#if>
@Generated(value = {<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>}, date = "${currentDate?datetime}")
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