<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:o="http://www.xml-cml.org/report"
        xmlns:xsd="http://www.w3.org/2001/XMLSchema">

    <xsl:template match="/">
        <o:result>
            <xsl:call-template name="check-report">
                <xsl:with-param name="func">namespace-uri-for-prefix</xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="check-report">
                <xsl:with-param name="func">index-of</xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="check-report">
                <xsl:with-param name="func">string</xsl:with-param>
            </xsl:call-template>
        </o:result>
    </xsl:template>

    <xsl:template name="check-report">
        <xsl:param name="func"></xsl:param>
        <xsl:choose>
            <xsl:when test="function-available(string($func))">
                <o:available>
                    <xsl:value-of select="$func"/>
                </o:available>
            </xsl:when>
            <xsl:otherwise>
                <o:unavailable>
                    <xsl:value-of select="$func"/>
                </o:unavailable>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
