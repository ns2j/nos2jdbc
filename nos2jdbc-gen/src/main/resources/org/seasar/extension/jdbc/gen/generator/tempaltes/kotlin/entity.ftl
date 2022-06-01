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
import ${importName}
  </#list>
</#if>

/**
<#if useComment && comment??>
 * ${comment}
<#else>
 * ${shortClassName}エンティティクラス
</#if>
 * 
<#if lib.author??>
 * @author ${lib.author}
</#if>
 */
@Entity
<#if catalogName?? || schemaName?? || tableName?? || compositeUniqueConstraintModelList?size gt 0>
@Table(<#if catalogName??>catalog = "${catalogName}"</#if><#if schemaName??><#if catalogName??>, </#if>schema = "${schemaName}"</#if><#if tableName??><#if catalogName?? || schemaName??>, </#if>name = "${tableName}"</#if><#if compositeUniqueConstraintModelList?size gt 0><#if catalogName?? || schemaName?? || tableName??>, </#if>uniqueConstraints = { <#list compositeUniqueConstraintModelList as uniqueConstraint>@UniqueConstraint(columnNames = { <#list uniqueConstraint.columnNameList as columnName>"${columnName}"<#if columnName_has_next>, </#if></#list> })<#if uniqueConstraint_has_next>, </#if></#list> }</#if>)
</#if>
@Generated(value = [<#list generatedInfoList as info>"${info}"<#if info_has_next>, </#if></#list>], date = "${currentDate?datetime?string["yyyy/MM/dd HH:mm:ss"]}")
class ${shortClassName}<#if shortSuperclassName??> : ${shortSuperclassName}</#if> : Serializable {

    private val serialVersionUID: Long = 1L
<#list attributeModelList as attr>

  <#if attr.unsupportedColumnType>
    /**
     * FIXME このプロパティに対応するカラムの型(${attr.columnTypeName})はサポート対象外です。
     */
  <#else>
    <#if useComment && attr.comment??>
    /** ${attr.comment} */
    <#else>
    /** ${attr.name}プロパティ */
    </#if>
  </#if>
  <#if attr.id>
    @Id
    <#if attr.generationType??>
    @GeneratedValue(strategy = GenerationType.${attr.generationType}<#if attr.generationType?matches("SEQUENCE|TABLE")>, generator = "generator"</#if>)
      <#if attr.generationType == "SEQUENCE">
    @SequenceGenerator(name = "generator", initialValue = ${attr.initialValue}, allocationSize = ${attr.allocationSize})
      <#elseif attr.generationType == "TABLE">
    @TableGenerator(name = "generator", initialValue = ${attr.initialValue}, allocationSize = ${attr.allocationSize})
      </#if>
    </#if>
  </#if>
  <#if attr.lob>
    @Lob
  </#if>
  <#if attr.temporalType??>
    @Temporal(TemporalType.${attr.temporalType})
  </#if>
  <#if attr.transient>
    @Transient
  </#if>
  <#if attr.version>
    @Version
  </#if>
  <#if attr.createdAt>
    @CreatedAt
  </#if>
  <#if attr.updatedAt>
    @UpdatedAt
  </#if>
  <#if !attr.transient>
    @Column(<#if attr.columnName??>name = "${attr.columnName}", </#if><#if attr.columnDefinition??>columnDefinition = "${attr.columnDefinition}", <#else><#if attr.length??>length = ${attr.length}, </#if><#if attr.precision??>precision = ${attr.precision}, </#if><#if attr.scale??>scale = ${attr.scale}, </#if></#if>nullable = ${attr.nullable?string}, unique = ${attr.unique?string})
  </#if>
    <#if useAccessor>private</#if> var ${attr.name} : ${attr.attributeClass.simpleName}? = null
</#list>
<#list associationModelList as asso>

    /** ${asso.name}関連プロパティ */
    @${asso.associationType.annotation.simpleName}<#if asso.mappedBy??>(mappedBy = "${asso.mappedBy}")</#if>
  <#if asso.joinColumnModel??>
    @JoinColumn(name = "${asso.joinColumnModel.name}", referencedColumnName = "${asso.joinColumnModel.referencedColumnName}")
  <#elseif asso.joinColumnsModel??>
    @JoinColumns( [
    <#list asso.joinColumnsModel.joinColumnModelList as joinColumnModel>
        @JoinColumn(name = "${joinColumnModel.name}", referencedColumnName = "${joinColumnModel.referencedColumnName}")<#if joinColumnModel_has_next>,<#else> ])</#if>
    </#list>
  </#if>
    <#if useAccessor>private</#if> var ${asso.name} : ${asso.shortClassName}<#if asso.associationType.annotation.simpleName?ends_with("One")>? = null<#else> = mutableListOf()</#if>
</#list>
<#if useAccessor>
  <#list attributeModelList as attr>

    /**
     * ${attr.name}を返します。
     * 
     * @param ${attr.name}
     */
    fun <#if attr.attributeClass.getSimpleName()?matches("[bB]oolean")>is<#else>get</#if>${attr.name?cap_first}() : ${attr.attributeClass.simpleName} {
        return ${attr.name}
    }

    /**
     * ${attr.name}を設定します。
     * 
     * @param ${attr.name}
     */
    fun set${attr.name?cap_first}(${attr.attributeClass.simpleName} ${attr.name}) {
        this.${attr.name} = ${attr.name}
    }
  </#list>
  <#list associationModelList as asso>

    /**
     * ${asso.name}を返します。
     * 
     * @param ${asso.name}
     */
    fun get${asso.name?cap_first}() : ${asso.shortClassName} {
        return ${asso.name}
    }

    /**
     * ${asso.name}を設定します。
     * 
     * @param ${asso.name}
     */
    fun set${asso.name?cap_first}(${asso.shortClassName} ${asso.name}) {
        this.${asso.name} = ${asso.name}
    }
  </#list>
</#if>
}