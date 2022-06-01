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
 * {@link ${shortServiceClassName}}のテストクラスです。
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
    @Inject @${serviceClassNameSuffix}Qualifier
</#if>
<#if componentType == "spring">
    @Autowired
</#if>
    lateinit var ${shortServiceClassName?uncap_first}: ${shortServiceClassName}

    /**
     * {@link #${shortServiceClassName?uncap_first}}が利用可能であることをテストします。
     * 
     * @throws Exception
     */
    @Test
    fun testAvailable() {
        assertNotNull(${shortServiceClassName?uncap_first})
    }
    
<#if componentType == "cdi" || componentType == "ejb">
    @Deployment
    fun createTestArchive() : Archive<?> {
	return ArchiveTestUtil.createTestArchive()
    }
</#if>

}