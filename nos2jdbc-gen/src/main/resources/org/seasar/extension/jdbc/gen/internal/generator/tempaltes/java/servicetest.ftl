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
@SpringBootTest
</#if>
@Generated(value = {<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>}, date = "${currentDate?datetime?string["yyyy/MM/dd HH:mm:ss"]}")
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