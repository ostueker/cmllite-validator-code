<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cml="http://www.xml-cml.org/schema"
                xmlns:report="http://www.xml-cml.org/report/"
                xmlns:xhtml="http://www.w3.org/1999/xhtml"
        >
    <xsl:output method="xml" omit-xml-declaration="no"
                standalone="yes" indent="yes"/>

    <xsl:param name="absoluteXPathToStartElement" select="/"/>

    <xsl:variable name="conventionName">unit-dictionary</xsl:variable>

    <xsl:variable name="conventionNS">http://www.xml-cml.org/convention/</xsl:variable>
    <xsl:variable name="cmlNS">http://www.xml-cml.org/schema</xsl:variable>
    <xsl:variable name="xhtmlNS">http://www.w3.org/1999/xhtml</xsl:variable>
    <xsl:variable name="ascii-chars">&#32;!"#$%&amp;'()*+,-./0123456789:;&lt;=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~&#127;</xsl:variable>

    <xsl:template match="/">
        <report:result>
            <xsl:apply-templates/>
        </report:result>
    </xsl:template>

    <xsl:template match="*|@*|text()">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*|@*|text()" mode="unit-dictionary">
        <xsl:apply-templates mode="unit-dictionary"/>
    </xsl:template>

    <xsl:template
            match="cml:*[@convention and namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName]">
        <xsl:choose>
            <xsl:when test="self::cml:unitList">
                <xsl:call-template name="unitList-template"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="@convention" mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">the only valid cml elements which can specify the unit-dictionary convention is "unitList"</xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="cml:*" mode="unit-dictionary">
        <xsl:call-template name="info">
            <xsl:with-param name="location">
                <xsl:apply-templates select="." mode="get-full-path"/>
            </xsl:with-param>
            <xsl:with-param name="text">
                <xsl:value-of select="local-name()"/> is not a part of the
                http://www.xml-cml.org/convention/unit-dictionary convention
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="cml:unitList" mode="unit-dictionary" name="unitList-template">
        <xsl:choose>
            <xsl:when test="@namespace">
                <xsl:choose>
                    <xsl:when test="ends-with(@namespace, '/')"/>
                    <xsl:when test="ends-with(@namespace, '#')"/>
                    <xsl:otherwise>
                        <xsl:call-template name="warning">
                            <xsl:with-param name="location">
                                <xsl:apply-templates select="@namespace" mode="get-full-path"/>
                            </xsl:with-param>
                            <xsl:with-param name="text">The namespace URI SHOULD end with either a '/'
                                character or a '#' character so that terms may be referenced by appending them to the
                                URI
                            </xsl:with-param>
                        </xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">The unit element MUST have a namespace attribute</xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:choose>
            <xsl:when test="@title">
                <xsl:if test="string-length(normalize-space(@title)) = 0">
                    <xsl:call-template name="error">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="@title" mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">the title attribute MUST NOT be empty and MUST contain non-whitespace characters
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
                <xsl:if test="not(translate(@title, $ascii-chars, '') = '')">
                    <xsl:call-template name="warning">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="@title" mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">the value of the title attribute MAY contain any valid unicode character, however it is RECOMMENDED that any character from outside of the ASCII subset (codepoints 32-127) is represented using an entity reference.</xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="warning">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">a unit must have a title attribute - the value of which will typically be the full name of the unit
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
         </xsl:choose>

        <xsl:choose>
            <xsl:when test="cml:description">
                <xsl:if test="count(cml:description) > 1">
                    <xsl:call-template name="error">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="." mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">The unit element can only have a single description child
                            element
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="warning">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">The unit element SHOULD have a single description child element
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:if test="not(.//cml:unit)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">a unitList must have at least one unit element child</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:apply-templates mode="unit-dictionary" />
    </xsl:template>

    <!-- unit template -->
    <xsl:template match="cml:unit" mode="unit-dictionary">
        <xsl:if test="not(parent::cml:unitList)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">unit must be the child of unitList</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:choose>
            <xsl:when test="@id">
                <xsl:if test="count(ancestor::cml:unitList//cml:unit[@id = current()/@id]) > 1">
                    <xsl:call-template name="error">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="." mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">the id of a unit must be unique within the unitList (duplicate found: <xsl:value-of select="@id"/>)
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">a unit must have an id</xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:choose>
            <xsl:when test="@title">
                <xsl:if test="string-length(normalize-space(@title)) = 0">
                    <xsl:call-template name="error">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="@title" mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">the title attribute MUST NOT be empty and MUST contain non-whitespace characters
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
                <xsl:if test="not(translate(@title, $ascii-chars, '') = '')">
                    <xsl:call-template name="warning">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="@title" mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">the value of the title attribute MAY contain any valid unicode character, however it is RECOMMENDED that any character from outside of the ASCII subset (codepoints 32-127) is represented using an entity reference.</xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">a unit must have a title attribute - the value of which will typically be the full name of the unit
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:choose>
            <xsl:when test="@symbol">
                <xsl:if test="string-length(normalize-space(@symbol)) = 0">
                    <xsl:call-template name="error">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="@symbol" mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">the symbol attribute MUST NOT be empty and MUST contain non-whitespace characters
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
                <xsl:if test="not(translate(@symbol, $ascii-chars, '') = '')">
                    <xsl:call-template name="warning">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="@symbol" mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">the value of the symbol attribute MAY contain any valid unicode character, however it is RECOMMENDED that any character from outside of the ASCII subset (codepoints 32-127) is represented using an entity reference.</xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">a unit must have a symbol attribute - the value of which is the full symbol used to represent this unit
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:if test="not(@parentSI)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">A unit element MUST have a parentSI attribute
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="not(@constantToSI or @multiplierToSI)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">A unit element MUST have at least one of the constantToSI and multiplierToSI attributes
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="not(@unitType)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">A unit element MUST have a unitType attribute
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="count(.//cml:definition) != 1">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">A unit element MUST contain a single definition child element
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="count(cml:description) > 1">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">A unit element MAY contain a single description child element
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:apply-templates mode="unit-dictionary"/>
    </xsl:template>

    <xsl:template match="cml:definition" mode="unit-dictionary">
        <xsl:if test="not(parent::cml:unit)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">definition must be the child of unit</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="string-length(.) = 0">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">definition MUST contain text</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="string-length(normalize-space(.)) = 0">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">definition MUST contain non whitespace text</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="not(xhtml:*)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">The definition element MUST contain one or more child elements in the
                    http://www.w3.org/1999/xhtml namespace.
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:apply-templates mode="unit-dictionary"/>
    </xsl:template>


    <xsl:template match="cml:description" mode="unit-dictionary">
        <xsl:if test="not(parent::cml:unit or parent::cml:unitList)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">description must be the child of unit or unitList</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="string-length(.) = 0">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">description MUST contain text</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="string-length(normalize-space(.)) = 0">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">description MUST contain non whitespace text</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="not(xhtml:*)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">The description element MUST contain one or more child elements in the
                    http://www.w3.org/1999/xhtml namespace.
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:apply-templates mode="unit-dictionary"/>
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