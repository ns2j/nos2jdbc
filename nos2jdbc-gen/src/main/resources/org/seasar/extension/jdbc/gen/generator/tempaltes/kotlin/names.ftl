<#import "/lib.ftl" as lib>
<#if lib.copyright??>
${lib.copyright}
</#if>
<#if !lib.copyright??>
<#include "/copyright.ftl">
</#if>
<#if packageName??>
package ${packageName}.${shortClassName}
</#if>

<#list importNameSet as importName>
import ${importName}
</#list>
<#if staticImportNameSet?size gt 0>

  <#list staticImportNameSet as importName>
import ${importName};
  </#list>
</#if>

/**
 * {@link ${shortEntityClassName}}のプロパティ名の集合です。
 * 
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
@Generated(value = [<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>], date = "${currentDate?datetime?string["yyyy/MM/dd HH:mm:ss"]}")
<#list namesAttributeModelList as attr>
    /**
     * ${attr.name}のプロパティ名を返します。
     * 
     * @return ${attr.name}のプロパティ名
     */
    fun ${attr.name}() : PropertyName<${getKotlinTypeName(attr.attributeClass.simpleName)}> {
        return PropertyName<${getKotlinTypeName(attr.attributeClass.simpleName)}>("${attr.name}")
    }
</#list>
<#list namesAssociationModelList as asso>

    /**
     * ${asso.name}のプロパティ名を返します。
     * 
     * @return ${asso.name}のプロパティ名
     */
    fun ${asso.name}() : ${asso.shortClassName} {
        return ${asso.shortClassName}(null, "${asso.name}")
    }
</#list>

    /**
     * @author S2JDBC-Gen
     */
    class ${shortInnerClassName}(val parent: PropertyName<*>? = null, val name: String? = null) : PropertyName<${shortEntityClassName}>(parent, name) {
<#list namesAttributeModelList as attr>

        /**
         * ${attr.name}のプロパティ名を返します。
         *
         * @return ${attr.name}のプロパティ名
         */
        fun ${attr.name}() : PropertyName<${attr.attributeClass.simpleName}> {
            return PropertyName<${attr.attributeClass.simpleName}>(this, "${attr.name}")
        }
</#list>
<#list namesAssociationModelList as asso>

        /**
         * ${asso.name}のプロパティ名を返します。
         * 
         * @return ${asso.name}のプロパティ名
         */
        fun ${asso.name}() : ${asso.shortClassName} {
            return ${asso.shortClassName}(this, "${asso.name}")
        }
</#list>
    }
