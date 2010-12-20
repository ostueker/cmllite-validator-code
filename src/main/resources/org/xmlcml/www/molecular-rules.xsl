<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cml="http://www.xml-cml.org/schema"
                xmlns:report="http://www.xml-cml.org/report/"
                xmlns:saxon="http://saxon.sf.net/"

        >
    <xsl:output method="xml" omit-xml-declaration="no"
                standalone="yes" indent="yes"/>
    <xsl:param name="absoluteXPathToStartElement" select="/" />

    <xsl:variable name="conventionName">molecular</xsl:variable>

    <xsl:variable name="conventionNS">http://www.xml-cml.org/convention/</xsl:variable>

    <xsl:template match="/">
        <report:result>
           <xsl:apply-templates select="saxon:evaluate($absoluteXPathToStartElement)" mode="molecular"/>
        </report:result>
    </xsl:template>

    <xsl:template match="cml:cml[@convention]">
        <xsl:choose>
            <xsl:when
                    test="namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = 'molecular'"
                    >
                <xsl:apply-templates mode="molecular"/>
            </xsl:when>
            <xsl:otherwise>
                <report:warning>No cml element found with correct convention</report:warning>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="*|@*|text()">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*|@*|text()" mode="molecular">
        <xsl:apply-templates mode="molecular"/>
    </xsl:template>
    <xsl:template match="cml:molecule" mode="molecular">
        <molecule>
            <xsl:value-of select="@id"/>
        </molecule>
        <xsl:if test="not(parent::cml:cml or parent::cml:molecule)">
            <report:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:attribute>
                molecule must be within molecule or cml elements
            </report:error>
        </xsl:if>
        <xsl:if test="parent::cml:molecule">
            <xsl:if test="not(@count)">
                <report:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="."
                                             mode="get-full-path"/>
                    </xsl:attribute>
                    child molecules must have a count specified
                </report:error>
            </xsl:if>
        </xsl:if>
        <xsl:if test="cml:molecule and cml:atomArray">
            <report:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="."
                                             mode="get-full-path"/>
                    </xsl:attribute>molecules with child molecules must not contain an atomArray
                </report:error>
        </xsl:if>
        <xsl:apply-templates mode="molecular"/>
    </xsl:template>


    <xsl:template match="cml:atomArray" mode="molecular">
        <xsl:if test="count(child::cml:atom) = 0">
            <report:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="."
                                         mode="get-full-path"/>
                </xsl:attribute>
                atomArray must contain atoms
            </report:error>
        </xsl:if>
        <xsl:if test="index-of((cml:molecule, cml:formula), parent::*) = 0">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="."
                                         mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">atomArray must be within molecule or formula
                    elements
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="count(../cml:atomArray) > 1">
            <report:error>
                                <xsl:attribute name="location">
                                    <xsl:apply-templates select="."
                                                         mode="get-full-path"/>
                                </xsl:attribute>
                                there should only be one atomArray child in a <xsl:value-of select="name(..)" /> element
                            </report:error>

        </xsl:if>
        <xsl:apply-templates mode="molecular"/>
    </xsl:template>
    <xsl:template match="cml:atom" mode="molecular">
        <!--
          atoms must have id unless they are part of an atomArray in a
          formula
        -->
        <xsl:if test="not(@id)">
            <xsl:choose>
                <xsl:when test="ancestor::cml:atomArray[ancestor::cml:formula]">

                </xsl:when>
                <xsl:otherwise>
                    <report:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="."
                                         mode="get-full-path"/>
                </xsl:attribute>
                atoms must have id unless they are part of an
                atomArray in a formula
            </report:error>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>

        <!-- atoms must have elementType -->
        <xsl:if test="not(@elementType)">
            <report:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="."
                                         mode="get-full-path"/>
                </xsl:attribute>
                atoms must have elementType specified
            </report:error>
        </xsl:if>
        <!--
          the ids of atoms must be unique within the eldest containing
          molecule
        -->
        <xsl:if test="@id">
        <xsl:choose>
            <xsl:when
                    test="@id and count(ancestor::cml:molecule//cml:atom[@id = current()/@id]) = 1"/>
            <xsl:otherwise>
                <report:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="."
                                             mode="get-full-path"/>
                    </xsl:attribute>
                    the id of a atom must be unique within the eldest containing
                    molecule (duplicate found: <xsl:value-of select="@id"/>)</report:error>
            </xsl:otherwise>
        </xsl:choose>
         </xsl:if>
        <xsl:choose>
            <xsl:when test="not(@x2) or (@x2 and @y2)"/>
            <xsl:otherwise>
                <report:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="."
                                             mode="get-full-path"/>
                    </xsl:attribute>
                    if atom has @x2 then it must have @y2
                </report:error>
            </xsl:otherwise>
        </xsl:choose>

        <!--ASSERT -->
        <xsl:choose>
            <xsl:when test="not(@y2) or (@x2 and @y2)"/>
            <xsl:otherwise>
                <report:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="."
                                             mode="get-full-path"/>
                    </xsl:attribute>
                    if atom has @y2 then it must have @x2
                </report:error>
            </xsl:otherwise>
        </xsl:choose>

        <!--ASSERT -->
        <xsl:choose>
            <xsl:when test="not(@x3) or (@x3 and @y3 and @z3)"/>
            <xsl:otherwise>
                <report:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="."
                                             mode="get-full-path"/>
                    </xsl:attribute>
                    if atom has @x3 then it must have @y3 and @z3
                </report:error>
            </xsl:otherwise>
        </xsl:choose>

        <!--ASSERT -->
        <xsl:choose>
            <xsl:when test="not(@y3) or (@x3 and @y3 and @z3)"/>
            <xsl:otherwise>
                <report:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="."
                                             mode="get-full-path"/>
                    </xsl:attribute>
                    if atom has @32 then it must have @x3 and @z3
                </report:error>
            </xsl:otherwise>
        </xsl:choose>

        <!--ASSERT -->
        <xsl:choose>
            <xsl:when test="not(@z3) or (@x3 and @y3 and @z3)"/>
            <xsl:otherwise>
                <report:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="."
                                             mode="get-full-path"/>
                    </xsl:attribute>
                    if atom has @z3 then it must have @x3 and @y3
                </report:error>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:apply-templates mode="molecular"/>
    </xsl:template>

    <xsl:template match="cml:bondArray" mode="molecular">
        <xsl:if test="count(child::cml:bond) = 0">
            <report:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="."
                                         mode="get-full-path"/>
                </xsl:attribute>
               bondArray must contain bonds
            </report:error>
        </xsl:if>
        <xsl:if test="not(../cml:molecule)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="."
                                         mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">bondArray must be within a molecule element</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
         <xsl:if test="count(../cml:bondArray) > 1">
            <report:error>
                                <xsl:attribute name="location">
                                    <xsl:apply-templates select="."
                                                         mode="get-full-path"/>
                                </xsl:attribute>
                                there should only be one bondArray child in a <xsl:value-of select="name(..)" /> element
                            </report:error>

        </xsl:if>
        <xsl:apply-templates mode="molecular"/>
    </xsl:template>

    <xsl:template match="cml:bond" mode="molecular">
        <!--ASSERT -->
        <xsl:choose>
            <xsl:when test="@atomRefs2"/>
            <xsl:otherwise>
                <report:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="."
                                             mode="get-full-path"/>
                    </xsl:attribute>
                    bond must have atomRefs2
                </report:error>
            </xsl:otherwise>
        </xsl:choose>
        <!--ASSERT -->
        <xsl:choose>
            <xsl:when
                    test="ancestor::cml:molecule[1]//cml:atom[@id = substring-before(current()/@atomRefs2, ' ')]"/>
            <xsl:otherwise>
                <report:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="."
                                             mode="get-full-path"/>
                    </xsl:attribute>
                    the atoms in the atomRefs2 must be within the eldest
                    containing molecule (not found [
                    <xsl:text/>
                    <xsl:value-of select="substring-before(@atomRefs2, ' ')"/>
                    <xsl:text/>
                    ])
                </report:error>
            </xsl:otherwise>
        </xsl:choose>
        <!--ASSERT -->
        <xsl:choose>
            <xsl:when
                    test="ancestor::cml:molecule[1]//cml:atom[@id = substring-after(current()/@atomRefs2, ' ')]"/>
            <xsl:otherwise>
                <report:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="."
                                             mode="get-full-path"/>
                    </xsl:attribute>
                    the atoms in the atomRefs2 must be within the eldest
                    containing molecule (not found [
                    <xsl:text/>
                    <xsl:value-of select="substring-after(@atomRefs2, ' ')"/>
                    <xsl:text/>
                    ])
                </report:error>
            </xsl:otherwise>
        </xsl:choose>

        <!--ASSERT -->
        <xsl:choose>
            <xsl:when
                    test="not(substring-before(@atomRefs2, ' ') = substring-after(@atomRefs2, ' '))"/>
            <xsl:otherwise>
                <report:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="."
                                             mode="get-full-path"/>
                    </xsl:attribute>
                    a bond must be between different atoms
                </report:error>
            </xsl:otherwise>
        </xsl:choose>

        <!--if the bond has an id it must be unique within the eldest containing molecule
            if it doesn't have an id it is a warning-->
        <xsl:choose>
                   <xsl:when test="@id">
                       <xsl:choose>
                           <xsl:when
                                   test="count(ancestor::cml:molecule[1]//cml:bond[@id = current()/@id]) = 1"/>
                           <xsl:otherwise>
                               <report:error>
                                   <xsl:attribute name="location">
                                       <xsl:apply-templates select="."
                                                            mode="get-full-path"/>
                                   </xsl:attribute>
                                   the id of a bond must be unique within the eldest containing
                                   molecule (duplicate found: <xsl:value-of select="@id"/>)
                               </report:error>
                           </xsl:otherwise>
                       </xsl:choose>
                   </xsl:when>
                    <xsl:otherwise>
                        <report:warning>
                            <xsl:attribute name="location">
                                    <xsl:apply-templates select="."
                                                            mode="get-full-path"/>
                            </xsl:attribute>
                            it is recommended that bonds have id attributes
                        </report:warning>
                    </xsl:otherwise>
        </xsl:choose>
        <xsl:apply-templates mode="molecular"/>
    </xsl:template>



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

    <!--MODE: SCHEMATRON-FULL-PATH-->
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