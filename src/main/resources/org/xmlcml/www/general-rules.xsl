<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cml="http://www.xml-cml.org/schema"
                xmlns:report="http://www.xml-cml.org/report/"
                xmlns:xhtml="http://www.w3.org/1999/xhtml"
        >
    <!-- error report -->
    <xsl:template name="error">
        <xsl:param name="location"/>
        <xsl:param name="text"/>
        <report:error>
            <xsl:attribute name="location" select="$location">
            </xsl:attribute>
            <xsl:value-of select="$text"/>
        </report:error>
    </xsl:template>

    <!-- warning report -->
    <xsl:template name="warning">
        <xsl:param name="location"/>
        <xsl:param name="text"/>
        <report:warning>
            <xsl:attribute name="location" select="$location">
            </xsl:attribute>
            <xsl:value-of select="$text"/>
        </report:warning>
    </xsl:template>

    <!-- info report -->
    <xsl:template name="info">
        <xsl:param name="location"/>
        <xsl:param name="text"/>
        <report:info>
            <xsl:attribute name="location" select="$location">
            </xsl:attribute>
            <xsl:value-of select="$text"/>
        </report:info>
    </xsl:template>

    <!--MODE: FULL-PATH-->
    <xsl:template match="*" mode="get-full-path">
        <xsl:apply-templates select="parent::*" mode="get-full-path"/>
        <xsl:text>/</xsl:text>
        <xsl:choose>
            <xsl:when test="namespace-uri()=''">
                <xsl:value-of select="name()"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>*[local-name()='</xsl:text>
                <xsl:value-of select="local-name()"/>
                <xsl:text>' and namespace-uri()='</xsl:text>
                <xsl:value-of select="namespace-uri()"/>
                <xsl:text>']</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:variable name="preceding"
                      select="count(preceding-sibling::*[local-name()=local-name(current()) and namespace-uri() = namespace-uri(current())])"/>
        <xsl:text>[</xsl:text>
        <xsl:value-of select="1+ $preceding"/>
        <xsl:text>]</xsl:text>
    </xsl:template>
    <xsl:template match="@*" mode="get-full-path">
        <xsl:apply-templates select="parent::*" mode="get-full-path"/>
        <xsl:text>@*[local-name()='</xsl:text>
        <xsl:value-of select="local-name()"/>
        <xsl:text>' and namespace-uri()='</xsl:text>
        <xsl:value-of select="namespace-uri()"/>
        <xsl:text>']</xsl:text>
    </xsl:template>
</xsl:stylesheet>