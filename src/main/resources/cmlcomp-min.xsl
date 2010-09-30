<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:cml="http://www.xml-cml.org/schema"
        xmlns:o="http://www.xml-cml.org/report"
        xmlns:xsd="http://www.w3.org/2001/XMLSchema"
        xmlns:func="http://www.xml-cml.org/function">

    <xsl:template match="/">
        <o:result>
            <xsl:apply-templates mode="cmlcomp" />
        </o:result>
    </xsl:template>
    <xsl:template match="cml:property" mode="cmlcomp">
        <!-- Contain : @dictRef [Must], one of scalar, array and matrix  -->
        <xsl:choose>
            <xsl:when test="@dictRef">
                <xsl:choose>
                    <xsl:when test="count(child::*) = 1">
                        <xsl:if test="not(count(child::cml:scalar) + count(child::cml:array) + count(child::cml:matrix) = 1)">
                            <o:error>
                                <xsl:attribute name="location">
                                    <xsl:apply-templates select="." mode="get-full-path"/>
                                </xsl:attribute>
                                found '<xsl:value-of select="name((child::*)[1])"/>' element. Only 'scalar', 'array' or 'matrix' is allowed.
                            </o:error>
                        </xsl:if>
                    </xsl:when>
                    <xsl:otherwise>
                        <o:error>
                            <xsl:attribute name="location">
                                <xsl:apply-templates select="." mode="get-full-path"/>
                            </xsl:attribute>
                            <xsl:value-of select="count(child::*)"/> elements found. exactly one element is expected.
                        </o:error>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <o:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:attribute>
                    @dictRef is required for a scalar.
                </o:error>
            </xsl:otherwise>
        </xsl:choose>
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
