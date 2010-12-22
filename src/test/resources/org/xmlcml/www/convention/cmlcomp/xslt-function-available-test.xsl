<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:report="http://www.xml-cml.org/report/"
        xmlns:xsd="http://www.w3.org/2001/XMLSchema">

    <xsl:template match="/">
        <report:result>
            <xsl:call-template name="check-report">
                <xsl:with-param name="func">namespace-uri-for-prefix</xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="check-report">
                <xsl:with-param name="func">index-of</xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="check-report">
                <xsl:with-param name="func">string</xsl:with-param>
            </xsl:call-template>
        </report:result>
    </xsl:template>

    <xsl:template name="check-report">
        <xsl:param name="func"></xsl:param>
        <xsl:choose>
            <xsl:when test="function-available(string($func))">
                <report:available>
                    <xsl:value-of select="$func"/>
                </report:available>
            </xsl:when>
            <xsl:otherwise>
                <report:unavailable>
                    <xsl:value-of select="$func"/>
                </report:unavailable>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
