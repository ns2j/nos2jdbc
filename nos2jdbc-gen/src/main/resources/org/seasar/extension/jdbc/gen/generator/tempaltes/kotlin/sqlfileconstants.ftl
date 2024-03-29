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
import ${importName?replace(".*", "")}
  </#list>
</#if>

/**
 * SQLファイルの定数クラスです。
 * 
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
@Generated(value = [<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>], date = "${currentDate?datetime?string["yyyy/MM/dd HH:mm:ss"]}")
object ${shortClassName} {
<#list sqlFileConstantFieldModelList as constant>

    /**
     * {@code ${constant.path}}の定数です。
     */
    const val ${constant.name}: String = "${constant.path}"
</#list>
}