<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:cml="http://www.xml-cml.org/schema"
        xmlns:o="http://www.xml-cml.org/report"
        xmlns:xsd="http://www.w3.org/2001/XMLSchema"
        xmlns:func="http://www.xml-cml.org/function">

    <xsl:template match="/">
        <o:result>
            <xsl:if test="function-available('namespace-uri-for-prefix')">
                namespace-uri-for-prefix is available
            </xsl:if>
            <xsl:if test="function-available('index-of')">
                index-of is available
            </xsl:if>
        </o:result>
    </xsl:template>
    <!--MODE: SCHEMATRON-FULL-PATH-->
    <!--xsl:template match="*" mode="get-full-path">
        <xsl:apply-templates select="parent::*" mode="get-full-path" />
        <xsl:text>/</xsl:text>
        <xsl:choose>
            <xsl:when test="namespace-uri()=''">
                <xsl:value-of select="name()" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>*[local-name()='</xsl:text>
                <xsl:value-of select="local-name()" />
                <xsl:text>' and namespace-uri()='</xsl:text>
                <xsl:value-of select="namespace-uri()" />
                <xsl:text>']</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:variable name="preceding"
                select="count(preceding-sibling::*[local-name()=local-name(current()) and namespace-uri() = namespace-uri(current())])" />
        <xsl:text>[</xsl:text>
        <xsl:value-of select="1+ $preceding" />
        <xsl:text>]</xsl:text>
    </xsl:template>
    <xsl:template match="@*" mode="get-full-path">
        <xsl:apply-templates select="parent::*" mode="get-full-path" />
        <xsl:text>@*[local-name()='</xsl:text>
        <xsl:value-of select="local-name()" />
        <xsl:text>' and namespace-uri()='</xsl:text>
        <xsl:value-of select="namespace-uri()" />
        <xsl:text>']</xsl:text>
    </xsl:template-->
</xsl:stylesheet>
