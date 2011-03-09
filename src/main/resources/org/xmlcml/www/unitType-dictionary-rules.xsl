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

    <xsl:variable name="conventionName">unitType-dictionary</xsl:variable>

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

    <xsl:template match="*|@*|text()" mode="unitType-dictionary">
        <xsl:apply-templates mode="unitType-dictionary"/>
    </xsl:template>

    <xsl:template
            match="cml:*[@convention and namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName]">
        <xsl:choose>
            <xsl:when test="self::cml:unitTypeList">
                <xsl:call-template name="unitTypeList-template"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="@convention" mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">the only valid cml element which can specify the unitType-dictionary
                        convention is "unitTypeList"
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="cml:*" mode="unitType-dictionary">
        <xsl:call-template name="info">
            <xsl:with-param name="location">
                <xsl:apply-templates select="." mode="get-full-path"/>
            </xsl:with-param>
            <xsl:with-param name="text">
                <xsl:value-of select="local-name()"/> is not a part of the
                http://www.xml-cml.org/convention/unitType-dictionary convention
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="cml:unitTypeList" mode="unitType-dictionary" name="unitTypeList-template">
        <xsl:choose>
            <xsl:when test="@namespace">
                <xsl:call-template name="unitTypeList-namespace"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">The unitTypeList element MUST have a namespace attribute
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:choose>
            <xsl:when test="@title">
                <xsl:call-template name="title-attribute"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="warning">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">a unitTypeList must have a title attribute
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
                        <xsl:with-param name="text">The unitTypeList element can only have a single description child
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
                    <xsl:with-param name="text">The unitTypeList element SHOULD have a single description child element
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:if test="not(.//cml:unitType)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">a unitTypeList must have at least one unitType element child
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:apply-templates mode="unitType-dictionary"/>
    </xsl:template>

    <!-- unitType template -->
    <xsl:template match="cml:unitType" mode="unitType-dictionary">
        <xsl:if test="not(parent::cml:unitTypeList)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">unitType must be the child of unitTypeList</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:choose>
            <xsl:when test="@id">
                <xsl:if test="count(ancestor::cml:unitTypeList//cml:unitType[@id = current()/@id]) > 1">
                    <xsl:call-template name="error">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="." mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">the id of a unitType must be unique within the unitTypeList
                            (duplicate found: <xsl:value-of select="@id"/>)
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">a unitType must have an id</xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:choose>
            <xsl:when test="@title">
                <xsl:call-template name="title-attribute"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">a unitType must have a title attribute - the value of which will
                        typically be the full name of the unitType
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:choose>
            <xsl:when test="@name">
                <xsl:call-template name="name-attribute"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">a unitType must have a name attribute - the value of which
                        linguistically identifies the type of the unit
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:if test="@preserve">
            <xsl:choose>
                <xsl:when test="@preserve = 'true'"/>
                <xsl:when test="@preserve = 'false'"/>
                <xsl:otherwise>
                    <xsl:call-template name="warning">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="@preserve" mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">it is RECOMMENDED that the value of the preserve attribute is "true"
                            or "false" found ='<xsl:value-of select="@preserve/."/>'
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>

        <xsl:if test="count(.//cml:definition) != 1">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">A unitType element MUST contain a single definition child element
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="count(.//cml:description) > 1">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">A unitType element MAY contain a single description child element
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="not(cml:dimension)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">A unitType element MUST contain at least one dimension child element
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:apply-templates mode="unitType-dictionary"/>
    </xsl:template>

    <xsl:template match="cml:definition" mode="unitType-dictionary">
        <xsl:if test="not(parent::cml:unitType)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">definition must be the child of unitType</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:choose>
            <xsl:when test="string-length(.) = 0">
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">definition MUST contain text</xsl:with-param>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="string-length(normalize-space(.)) = 0">
                    <xsl:call-template name="error">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="." mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">definition MUST contain non whitespace text</xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>

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

        <xsl:apply-templates mode="unitType-dictionary"/>
    </xsl:template>


    <xsl:template match="cml:description" mode="unitType-dictionary">
        <xsl:if test="not(parent::cml:unitType or parent::cml:unitTypeList)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">description must be the child of unitType or unitTypeList</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:choose>
            <xsl:when test="string-length(.) = 0">
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">description MUST contain text</xsl:with-param>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="string-length(normalize-space(.)) = 0">
                    <xsl:call-template name="error">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="." mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">description MUST contain non whitespace text</xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>

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

        <xsl:apply-templates mode="unitType-dictionary"/>
    </xsl:template>

    <xsl:template match="cml:dimension" mode="unitType-dictionary">
        <xsl:if test="not(parent::cml:unitType)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">dimension must be the child of unitType</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:choose>
            <xsl:when test="@name">
                <xsl:call-template name="name-attribute"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">a dimension must have a name attribute - the value of which
                        linguistically identifies the type of the unit
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:if test="not(@unitType)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">a dimension must have a unitType attribute
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="not(@power)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">a dimension must have a power attribute
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:apply-templates mode="unitType-dictionary"/>
    </xsl:template>

    <xsl:template match="cml:unitTypeList/@namespace" name="unitTypeList-namespace" mode="unitType-dictionary">
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
    </xsl:template>

    <xsl:template match="@title" name="title-attribute" mode="unitType-dictionary">
        <xsl:if test="string-length(normalize-space(@title)) = 0">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="@title" mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">the title attribute MUST NOT be empty and MUST contain non-whitespace
                    characters
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="not(translate(@title, $ascii-chars, '') = '')">
            <xsl:call-template name="warning">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="@title" mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">the value of the title attribute MAY contain any valid unicode character,
                    however it is RECOMMENDED that any character from outside of the ASCII subset (codepoints 32-127) is
                    represented using an entity reference.
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template match="@name" name="name-attribute" mode="unitType-dictionary">
        <xsl:if test="string-length(normalize-space(@name)) = 0">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="@name" mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">the name attribute MUST NOT be empty and MUST contain non-whitespace
                    characters
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="not(translate(@name, $ascii-chars, '') = '')">
            <xsl:call-template name="warning">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="@title" mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">the value of the name attribute MAY contain any valid unicode character,
                    however it is RECOMMENDED that any character from outside of the ASCII subset (codepoints 32-127) is
                    represented using an entity reference.
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
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