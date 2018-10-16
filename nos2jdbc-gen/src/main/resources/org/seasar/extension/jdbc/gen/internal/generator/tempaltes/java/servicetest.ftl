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

<#if componentType == "cdi" || componentType == "ejb">
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;

import ${rootPackageName}.ArchiveTestUtil;
</#if>
<#if componentType == "spring">
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
</#if>
<#if staticImportNameSet?size gt 0>
  <#list staticImportNameSet as importName>
import static ${importName};
  </#list>
</#if>

/**
 * {@link ${shortServiceClassName}}のテストクラスです。
 * 
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
<#if componentType == "cdi" || componentType == "ejb">
@RunWith(Arquillian.class)
</#if>
<#if componentType == "spring">
@SpringJUnitConfig(${springAppConfig}.class)
</#if>
@Generated(value = {<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>}, date = "${currentDate?datetime}")
public class ${shortClassName} {

<#if componentType == "cdi" || componentType == "ejb">
    @Inject @${serviceClassNameSuffix}Qualifier
</#if>
<#if componentType == "spring">
    @Autowired
</#if>
    private ${shortServiceClassName} ${shortServiceClassName?uncap_first};

    /**
     * {@link #${shortServiceClassName?uncap_first}}が利用可能であることをテストします。
     * 
     * @throws Exception
     */
    @Test
    public void testAvailable() throws Exception {
        assertNotNull(${shortServiceClassName?uncap_first});
    }
    
<#if componentType == "cdi" || componentType == "ejb">
    @Deployment
    public static Archive<?> createTestArchive() {
	return ArchiveTestUtil.createTestArchive();
    }
</#if>

}