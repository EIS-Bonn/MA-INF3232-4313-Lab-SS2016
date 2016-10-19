<?xml version="1.0" encoding="utf-8"?>

<!DOCTYPE stylesheet  [
<!ENTITY rdf "http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<!ENTITY rdfs "http://www.w3.org/2000/01/rdf-schema#">
<!ENTITY dc "http://purl.org/dc/elements/1.1/">
<!ENTITY opcua "https://w3id.org/i40/opcua/">
<!ENTITY xsd "http://www.w3.org/2001/XMLSchema#">
<!ENTITY schema "http://schema.org/">
]>


<xsl:transform version="2.0" 
               xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
               xmlns:xd="http://www.pnp-software.com/XSLTdoc"
               xmlns:krextor="http://kwarc.info/projects/krextor"
               xmlns:krextor-genuri="http://kwarc.info/projects/krextor/genuri"
               xmlns:xs="http://www.w3.org/2001/XMLSchema"
                      exclude-result-prefixes="">
                      
<!--  UANodeSet -->

<!--<xsl:param name="autogenerate-fragment-uris" select="'generate-id'"/>-->

 <xsl:template match="/" mode="krextor:main">
      <xsl:apply-imports>
        <xsl:with-param
          name="krextor:base-uri"
          select="xs:anyURI('https://w3id.org/i40/opcua/')"
          as="xs:anyURI"
          tunnel="yes"/>
      </xsl:apply-imports>
 </xsl:template>

 <xd:doc>uses ElementN as the fragment URI of the N-th occurrence of an element named
   <code>Element</code> in document order, starting from N=1.</xd:doc>
    <xsl:function name="krextor:global-element-index" as="xs:string?">
	<xsl:param name="node" as="node()"/>
	<xsl:value-of select="concat(local-name($node), count(root($node)//*[local-name() eq local-name($node) and . &lt;&lt; $node]) + 1)"/>
    </xsl:function>

    <!-- copied and adapted from generic/uri.xsl -->
    <xsl:template match="krextor-genuri:global-element-index" as="xs:string?">
	<xsl:param name="node" as="node()"/>
	<xsl:param name="base-uri" as="xs:anyURI"/>
	<xsl:sequence select="
	    resolve-uri(krextor:global-element-index($node), $base-uri)"/>
    </xsl:template>
    
<xsl:param name="autogenerate-fragment-uris" select="'global-element-index'" />
<!-- <xsl:param name="autogenerate-fragment-uris" select="'pseudo-xpath'" /> -->

 
 
<xsl:variable name="krextor:resources">
	<UANodeSet type="&opcua;UANodeSet"/>

	<NamespaceUris type="&opcua;NamespaceUris" related-via-properties="&opcua;hasNamespaceUris"/>
	
	<Aliases type="&opcua;Aliases" related-via-properties="&opcua;hasAliases"/>
	
	<UAObject type="&opcua;UAObject" related-via-properties="&opcua;hasUAObject"/>
	
	<UAVariable type="&opcua;UAVariable" related-via-properties="&opcua;hasUAVariable"/>
	
	<UAObjectType type="&opcua;UAObjectType" related-via-properties="&opcua;hasUAObjectType"/>

	<Alias type="&opcua;Alias" related-via-properties="&opcua;hasAlias"/>

	<Uri type="&opcua;Uri" related-via-properties="&opcua;hasUri"/>

	<DisplayName type="&opcua;DisplayName" related-via-properties="&opcua;hasDisplayName"/>

	<Descriprion type="&opcua;Description" related-via-properties="&opcua;hasDescription"/>

	<References type="&opcua;References" related-via-properties="&opcua;hasReferences"/>

	<Reference type="&opcua;Reference" related-via-properties="&opcua;hasReference"/>

	<Value type="&opcua;Value" related-via-properties="&opcua;hasValue"/>

	<String type="&opcua;String" related-via-properties="&opcua;hasString"/>

</xsl:variable>

<xsl:template match="UANodeSet
					|UANodeSet/NamespaceUris
					|UANodeSet/Aliases
					|UANodeSet/UAObject
					|UANodeSet/UAVariable
					|UANodeSet/UAVariable/Value
					|UANodeSet/UAObjectType
					|DisplayName
					|Description
					|References
					|References/Reference" mode="krextor:main">
	   <xsl:apply-templates select="." mode="krextor:create-resource"/>
</xsl:template>


<xsl:variable name="krextor:literal-properties">

<!-- Aliases -->
		<Alias property="&opcua;hasAlias" krextor:attribute="yes" datatype="&xsd;string"/>

<!-- UAObject  -->
		<NodeId property="&opcua;hasNodeId" krextor:attribute="yes" datatype="&xsd;string"/>
	    <BrowseName property="&opcua;hasBrowseName" krextor:attribute="yes" datatype="&xsd;string"/>

<!-- Reference -->
		<ReferenceType property="&dc;referenceType" krextor:attribute="yes" datatype="&xsd;string"/>
		<IsForward property="&dc;isForward" krextor:attribute="yes" datatype="&xsd;boolean"/>

<!-- UAVariable -->
		<ParentNodeId property="&dc;parentNodeId" krextor:attribute="yes" datatype="&xsd;string"/>
		<DataType property="&dc;hasDataType" krextor:attribute="yes" datatype="&xsd;string"/>
		<AccessLevel property="&dc;accessLevel" krextor:attribute="yes" datatype="&xsd;string"/>
		<UserAccessLevel property="&dc;userAccessLevel" krextor:attribute="yes" datatype="&xsd;string"/>

		<String property="&dc;hasString" krextor:attribute="yes" datatype="&xsd;string"/>


<!-- the following mapping rules will be simplified in the second example, this version can be treated as standard test case -->
</xsl:variable>
<xsl:template match="UANodeSet
	                  |UANodeSet/NamespaceUris
	                  |UANodeSet/NamespaceUris/Uri
                      |UANodeSet/Aliases
	                  |UANodeSet/Aliases/Alias/@Alias
	                  |UANodeSet/UAObject/@NodeId
	                  |UANodeSet/UAObject/@BrowseName
	                  |UANodeSet/UAObject/DisplayName
	                  |UANodeSet/UAObject/Description
	                  |UANodeSet/UAObject/References/Reference/@ReferenceType
	                  |UANodeSet/UAObject/References/Reference/@IsForward
	                  |UANodeSet/UAVariable/@NodeId
	                  |UANodeSet/UAVariable/@BrowseName
	                  |UANodeSet/UAVariable/@ParentNodeId
	                  |UANodeSet/UAVariable/@DataType
	                  |UANodeSet/UAVariable/DisplayName
	                  |UANodeSet/UAVariable/References
	                  |UANodeSet/UAVariable/References/Reference/@ReferenceType
	                  |UANodeSet/UAVariable/References/Reference/@IsForward
	                  |UANodeSet/UAVariable/Value
	                  |UANodeSet/UAVariable/Value/String
	                  |UANodeSet/UAObjectType/@NodeId
	                  |UANodeSet/UAObjectType/@BrowseName
	                  |UANodeSet/UAObjectType/DisplayName
	                  |UANodeSet/UAObjectType/Description
	                  |UANodeSet/UAObjectType/References/Reference/@ReferenceType
	                  |UANodeSet/UAObjectType/References/Reference/@IsForward"
	                  mode="krextor:main">
 <xsl:apply-templates select="." mode="krextor:add-literal-property"/>
</xsl:template>

<!--
<xsl:template match="UANodeSet/AdditionalInformation/@AutomationMLVersion" mode="krextor:main">
  <xsl:call-template name="krextor:add-literal-property">
    <xsl:with-param name="property" select="'&opcua;hasAutomationMLVersion'"/>
    <xsl:with-param name="datatype " select="'&xsd;string'"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="//RefSemantic/@CorrespondingAttributePath" mode="krextor:main">
  <xsl:call-template name="krextor:add-literal-property">
    <xsl:with-param name="property" select="'&opcua;hasCorrespondingAttributePath'"/>
    <xsl:with-param name="datatype " select="'&xsd;string'"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="//Attribute/@Value" mode="krextor:main">
  <xsl:call-template name="krextor:add-literal-property">
    <xsl:with-param name="property" select="'&opcua;hasAttributeValue'"/>
    <xsl:with-param name="datatype " select="'&xsd;string'"/>
  </xsl:call-template>
</xsl:template>

-->


</xsl:transform>
