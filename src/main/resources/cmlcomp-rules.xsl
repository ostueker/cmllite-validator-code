<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet
    version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cml="http://www.xml-cml.org/schema"
    xmlns:o="http://www.xml-cml.org/report" xmlns:other="http://www.other.org/"
    xmlns:svrl="http://www.a.com">

    <xsl:output method="xml" omit-xml-declaration="no" standalone="yes" indent="yes" />
    <xsl:variable name="conventionNS">http://www.xml-cml.org/convention/</xsl:variable>
    <xsl:variable name="cmllite">cmllite</xsl:variable>
    <xsl:variable name="cmlcomp">cmlcomp</xsl:variable>

    <!-- Apply templates to root node. The matching pattern here apply to any root tag
         but we are expecting cml container -->
    <xsl:template match="/">
        <o:result>
            <xsl:apply-templates />
        </o:result>
    </xsl:template>

    <!-- Match top level text data in xml. Simply ignore them.-->
    <xsl:template match="*|@*|text()">
        <xsl:apply-templates />
    </xsl:template>

    <!-- Choosing convention from cml root element. Convention has become a required
         attribute. -->
    <xsl:template match="cml:cml[@convention]">
        <xsl:choose>
            <xsl:when
                test="namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $cmlcomp">
                <xsl:apply-templates mode="$cmlcomp" />
            </xsl:when>
            <xsl:otherwise>
                <o:warning>No cml element found with correct convention</o:warning>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <xsl:template match="*|@*|text()" mode="cmllite">
        <xsl:apply-templates mode="cmllite" />
    </xsl:template>
    <xsl:template match="cml:molecule" mode="cmllite">
        <molecule>
            <xsl:value-of select="@id" />
        </molecule>
        <xsl:if test="not(parent::cml:cml or parent::cml:molecule)">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="."
          mode="get-full-path" />
                </xsl:attribute>molecule must be within molecule or cml elements
            </o:error>
        </xsl:if>
        <xsl:if test="parent::cml:molecule">
            <xsl:if test="not(@count)">
                <o:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="."
            mode="get-full-path" />
                    </xsl:attribute>
          child molecules must have a count specified
                </o:error>
            </xsl:if>
        </xsl:if>
        <xsl:apply-templates mode="cmllite" />
    </xsl:template>
    <xsl:template match="cml:atomArray" mode="cmllite">
        <xsl:if test="count(child::cml:atom) = 0">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="."
          mode="get-full-path" />
                </xsl:attribute>
       atomArray must contain atoms
            </o:error>
        </xsl:if>
        <xsl:if test="index-of((cml:molecule, cml:formula), parent::*) = 0">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="."
          mode="get-full-path" />
                </xsl:with-param>
                <xsl:with-param name="text"> atomArray must be within molecule or formula
          elements
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:apply-templates mode="cmllite" />
    </xsl:template>
    <xsl:template match="cml:atom" mode="cmllite">
    <!--
      atoms must have id unless they are part of an atomArray in a
      formula
    -->
        <xsl:if test="not(@id) and ../../cml:formula">
            <svrl:failed-assert test="not(@id) and ../../cml:formula">
                <xsl:attribute name="location">
                    <xsl:apply-templates select="."
          mode="get-full-path" />
                </xsl:attribute>
                <svrl:text> atoms must have id unless they are part of an
          atomArray in a formula
                </svrl:text>
            </svrl:failed-assert>
        </xsl:if>

    <!-- atoms must have elementType -->
        <xsl:if test="not(@elementType)">
            <svrl:failed-assert test="not(@elementType)">
                <xsl:attribute name="location">
                    <xsl:apply-templates select="."
          mode="get-full-path" />
                </xsl:attribute>
                <svrl:text> atoms must have elementType specified</svrl:text>
            </svrl:failed-assert>
        </xsl:if>
    <!--
      the ids of atoms must be unique within the eldest containing
      molecule
    -->
        <xsl:choose>
            <xsl:when
        test="count(ancestor::cml:molecule//cml:atom[@id = current()/@id]) = 1" />
            <xsl:otherwise>
                <svrl:failed-assert
          test="count(ancestor::cml:molecule//cml:atom[@id = current()/@id]) = 1"
          xmlns:svrl="http://purl.oclc.org/dsdl/svrl"
        >
                    <xsl:attribute name="location">
                        <xsl:apply-templates
            select="." mode="schematron-get-full-path" />
                    </xsl:attribute>
                    <svrl:text>
            the id of a atom must be unique within the eldest containing
            molecule (duplicate found:
                        <xsl:text />
                        <xsl:value-of select="@id" />
                        <xsl:text />
            )
                    </svrl:text>
                </svrl:failed-assert>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="not(@x2) or (@x2 and @y2)" />
            <xsl:otherwise>
                <svrl:failed-assert test="not(@x2) or (@x2 and @y2)"
          xmlns:svrl="http://purl.oclc.org/dsdl/svrl"
        >
                    <xsl:attribute name="location">
                        <xsl:apply-templates
            select="." mode="schematron-get-full-path" />
                    </xsl:attribute>
                    <svrl:text>if atom has @x2 then it must have @y2</svrl:text>
                </svrl:failed-assert>
            </xsl:otherwise>
        </xsl:choose>

    <!--ASSERT -->
        <xsl:choose>
            <xsl:when test="not(@y2) or (@x2 and @y2)" />
            <xsl:otherwise>
                <svrl:failed-assert test="not(@y2) or (@x2 and @y2)"
          xmlns:svrl="http://purl.oclc.org/dsdl/svrl"
        >
                    <xsl:attribute name="location">
                        <xsl:apply-templates
            select="." mode="schematron-get-full-path" />
                    </xsl:attribute>
                    <svrl:text>if atom has @y2 then it must have @x2</svrl:text>
                </svrl:failed-assert>
            </xsl:otherwise>
        </xsl:choose>

    <!--ASSERT -->
        <xsl:choose>
            <xsl:when test="not(@x3) or (@x3 and @y3 and @z3)" />
            <xsl:otherwise>
                <svrl:failed-assert test="not(@x3) or (@x3 and @y3 and @z3)"
          xmlns:svrl="http://purl.oclc.org/dsdl/svrl"
        >
                    <xsl:attribute name="location">
                        <xsl:apply-templates
            select="." mode="schematron-get-full-path" />
                    </xsl:attribute>
                    <svrl:text>if atom has @x3 then it must have @y3 and @z3
                    </svrl:text>
                </svrl:failed-assert>
            </xsl:otherwise>
        </xsl:choose>

    <!--ASSERT -->
        <xsl:choose>
            <xsl:when test="not(@y3) or (@x3 and @y3 and @z3)" />
            <xsl:otherwise>
                <svrl:failed-assert test="not(@y3) or (@x3 and @y3 and @z3)"
          xmlns:svrl="http://purl.oclc.org/dsdl/svrl"
        >
                    <xsl:attribute name="location">
                        <xsl:apply-templates
            select="." mode="schematron-get-full-path" />
                    </xsl:attribute>
                    <svrl:text>if atom has @32 then it must have @x3 and @z3
                    </svrl:text>
                </svrl:failed-assert>
            </xsl:otherwise>
        </xsl:choose>

    <!--ASSERT -->
        <xsl:choose>
            <xsl:when test="not(@z3) or (@x3 and @y3 and @z3)" />
            <xsl:otherwise>
                <svrl:failed-assert test="not(@z3) or (@x3 and @y3 and @z3)"
          xmlns:svrl="http://purl.oclc.org/dsdl/svrl"
        >
                    <xsl:attribute name="location">
                        <xsl:apply-templates
            select="." mode="schematron-get-full-path" />
                    </xsl:attribute>
                    <svrl:text>if atom has @z3 then it must have @x3 and @y3
                    </svrl:text>
                </svrl:failed-assert>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:apply-templates mode="cmllite" />
    </xsl:template>
    <xsl:template match="cml:bond" mode="cmllite">
    <!--ASSERT -->
        <xsl:choose>
            <xsl:when test="@atomRefs2" />
            <xsl:otherwise>
                <svrl:failed-assert test="@atomRefs2"
          xmlns:svrl="http://purl.oclc.org/dsdl/svrl"
        >
                    <xsl:attribute name="location">
                        <xsl:apply-templates
            select="." mode="schematron-get-full-path" />
                    </xsl:attribute>
                    <svrl:text>bond must have atomRefs2</svrl:text>
                </svrl:failed-assert>
            </xsl:otherwise>
        </xsl:choose>
    <!--ASSERT -->
        <xsl:choose>
            <xsl:when
        test="ancestor::cml:molecule[1]//cml:atom[@id = substring-before(current()/@atomRefs2, ' ')]" />
            <xsl:otherwise>
                <svrl:failed-assert
          test="ancestor::cml:molecule[1]//cml:atom[@id = substring-before(current()/@atomRefs2, ' ')]"
          xmlns:svrl="http://purl.oclc.org/dsdl/svrl"
        >
                    <xsl:attribute name="location">
                        <xsl:apply-templates
            select="." mode="schematron-get-full-path" />
                    </xsl:attribute>
                    <svrl:text>
            the atoms in the atomRefs2 must be within the eldest
            containing molecule (not found [
                        <xsl:text />
                        <xsl:value-of select="substring-before(@atomRefs2, ' ')" />
                        <xsl:text />
            ])
                    </svrl:text>
                </svrl:failed-assert>
            </xsl:otherwise>
        </xsl:choose>
    <!--ASSERT -->
        <xsl:choose>
            <xsl:when
        test="ancestor::cml:molecule[1]//cml:atom[@id = substring-after(current()/@atomRefs2, ' ')]" />
            <xsl:otherwise>
                <svrl:failed-assert
          test="ancestor::cml:molecule[1]//cml:atom[@id = substring-after(current()/@atomRefs2, ' ')]"
          xmlns:svrl="http://purl.oclc.org/dsdl/svrl"
        >
                    <xsl:attribute name="location">
                        <xsl:apply-templates
            select="." mode="schematron-get-full-path" />
                    </xsl:attribute>
                    <svrl:text>
            the atoms in the atomRefs2 must be within the eldest
            containing molecule (not found [
                        <xsl:text />
                        <xsl:value-of select="substring-after(@atomRefs2, ' ')" />
                        <xsl:text />
            ])
                    </svrl:text>
                </svrl:failed-assert>
            </xsl:otherwise>
        </xsl:choose>

    <!--ASSERT -->
        <xsl:choose>
            <xsl:when
        test="not(substring-before(@atomRefs2, ' ') = substring-after(@atomRefs2, ' '))" />
            <xsl:otherwise>
                <svrl:failed-assert
          test="not(substring-before(@atomRefs2, ' ') = substring-after(@atomRefs2, ' '))"
          xmlns:svrl="http://purl.oclc.org/dsdl/svrl"
        >
                    <xsl:attribute name="location">
                        <xsl:apply-templates
            select="." mode="schematron-get-full-path" />
                    </xsl:attribute>
                    <svrl:text>a bond must be between different atoms</svrl:text>
                </svrl:failed-assert>
            </xsl:otherwise>
        </xsl:choose>

    <!--ASSERT -->
        <xsl:choose>
            <xsl:when
        test="count(ancestor::cml:molecule[1]//cml:bond[@id = current()/@id]) = 1" />
            <xsl:otherwise>
                <svrl:failed-assert
          test="count(ancestor::cml:molecule[1]//cml:bond[@id = current()/@id]) = 1"
          xmlns:svrl="http://purl.oclc.org/dsdl/svrl"
        >
                    <xsl:attribute name="location">
                        <xsl:apply-templates
            select="." mode="schematron-get-full-path" />
                    </xsl:attribute>
                    <svrl:text>
            the id of a bond must be unique within the eldest containing
            molecule (duplicate found:
                        <xsl:text />
                        <xsl:value-of select="@id" />
                        <xsl:text />
            )
                    </svrl:text>
                </svrl:failed-assert>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:apply-templates select="@*|*|comment()|processing-instruction()"
      mode="M13" />
    </xsl:template>

  <!-- error report -->
    <xsl:template name="error">
        <xsl:param name="location" />
        <xsl:param name="text" />
        <here></here>
        <o:error>
            <xsl:attribute name="location" select="$location">
            </xsl:attribute>
            <xsl:value-of select="$text" />
        </o:error>
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