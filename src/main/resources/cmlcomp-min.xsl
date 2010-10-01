<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:cml="http://www.xml-cml.org/schema"
        xmlns:o="http://www.xml-cml.org/report"
        xmlns:xsd="http://www.w3.org/2001/XMLSchema">

    <xsl:template match="/">
        <o:result>
            <xsl:apply-templates mode="cmlcomp" />
        </o:result>
    </xsl:template>
    
    <xsl:template name="cmlcomp-step-mod">
        <xsl:param name="has-molecule" as="xsd:boolean"/>
        <xsl:param name="has-parameterList" as="xsd:boolean"/>
        <xsl:param name="has-propertyList" as="xsd:boolean"/>
        <!-- Contain : molecule [Must = 1?], parameterList [Must = 1?], propertyList [optional = 1] -->
        <xsl:if test="not(parent::cml:module[@role='job'])">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:attribute>
                module[@role='init'] must be within module module[@role='job']
            </o:error>
        </xsl:if>
        <xsl:if test="$has-molecule">
            <xsl:choose>
                <!-- Apply cmllite if cml:molecule is found. -->
                <xsl:when test="count(child::cml:molecule) = 1">
                    <xsl:for-each select="cml:molecule">
                        <xsl:apply-templates select="." mode="cmllite"/>
                    </xsl:for-each>
                </xsl:when>
                <xsl:otherwise>
                    <o:error>
                        <xsl:attribute name="location">
                            <xsl:apply-templates select="." mode="get-full-path"/>
                        </xsl:attribute>
                        only 1 molecule is allowed in module[@role='init']
                    </o:error>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
        <xsl:if test="$has-parameterList">
            <xsl:if test="not(count(child::cml:parameterList) = 1)">
                <o:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:attribute>
                only 1 parameterList is allowed in module[@role='init'], an initialization module must contain
                parameters for setting up computational job.
                </o:error>
            </xsl:if>
            <xsl:if test="not(count(child::cml:parameterList) &lt;= 1)">
                <o:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:attribute>
                only 1 or no propertyList is allowed in module[@role='init'], it is unusal to have preoperty in
                the initializing step.
                </o:error>
            </xsl:if>
        </xsl:if>
    </xsl:template>
    <!--MODE: SCHEMATRON-FULL-PATH-->
    <xsl:template match="*" mode="get-full-path">
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
    </xsl:template>
</xsl:stylesheet>
