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

    <xsl:variable name="conventionName">dictionary</xsl:variable>

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

    <xsl:template match="*|@*|text()" mode="dictionary">
        <xsl:apply-templates mode="dictionary"/>
    </xsl:template>

    <xsl:template
            match="cml:*[@convention and namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName]">
        <xsl:choose>
            <xsl:when test="self::cml:cml">
                <xsl:choose>
                    <xsl:when test="count(cml:*) = 1">
                        <xsl:choose>
                            <xsl:when test="cml:dictionary">
                                <xsl:choose>
                                    <xsl:when test="cml:dictionary[not(@convention)]">
                                        <xsl:apply-templates mode="dictionary"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:choose>
                                            <xsl:when
                                                    test="namespace-uri-for-prefix(substring-before(cml:dictionary/@convention, ':'),cml:dictionary) = $conventionNS and substring-after(cml:dictionary/@convention, ':') = $conventionName">
                                                <xsl:apply-templates mode="dictionary"/>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:call-template name="error">
                                                    <xsl:with-param name="location">
                                                        <xsl:apply-templates select="cml:dictionary"
                                                                             mode="get-full-path"/>
                                                    </xsl:with-param>
                                                    <xsl:with-param name="text">the must be a single dictionary element
                                                        as a child of the
                                                        "cml" element declaring the
                                                        http://www.xml-cml.org/convention/dictionary convention and it
                                                        should either have no convention specified or it may declare the
                                                        http://www.xml-cml.org/convention/dictionary convention
                                                    </xsl:with-param>
                                                </xsl:call-template>
                                                <xsl:apply-templates/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:call-template name="error">
                                    <xsl:with-param name="location">
                                        <xsl:apply-templates select="." mode="get-full-path"/>
                                    </xsl:with-param>
                                    <xsl:with-param name="text">there must be a single dictionary element as a child of
                                        the
                                        "cml" element declaring the http://www.xml-cml.org/convention/dictionary
                                        convention
                                    </xsl:with-param>
                                </xsl:call-template>
                                <xsl:apply-templates mode="dictionary"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="error">
                            <xsl:with-param name="location">
                                <xsl:apply-templates select="." mode="get-full-path"/>
                            </xsl:with-param>
                            <xsl:with-param name="text">there must be a single dictionary element as a child of the
                                "cml" element declaring the http://www.xml-cml.org/convention/dictionary convention
                            </xsl:with-param>
                        </xsl:call-template>
                        <xsl:apply-templates mode="dictionary"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>

            <xsl:when test="self::cml:dictionary">
                <xsl:call-template name="dictionary-template"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="@convention" mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">the only valid cml elements which specify the dictionary convention are
                        "cml" and "dictionary"
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="cml:*" mode="dictionary">
        <xsl:call-template name="error">
            <xsl:with-param name="location">
                <xsl:apply-templates select="." mode="get-full-path"/>
            </xsl:with-param>
            <xsl:with-param name="text">
                <xsl:value-of select="local-name()"/> is not a valid element in the
                http://www.xml-cml.org/convention/dictionary convention
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="cml:dictionary" mode="dictionary" name="dictionary-template">
        <xsl:if test="..">
            <xsl:if test="parent::cml:* and not(parent::cml:cml)">
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">the dictionary element can be the root node or a child of a "cml"
                        element only
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:if>
        </xsl:if>

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
                            <xsl:with-param name="text">The dictionary's namespace URI SHOULD end with either a '/'
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
                    <xsl:with-param name="text">The dictionary element MUST have a namespace attribute</xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:if test="not(@dictionaryPrefix)">
            <xsl:call-template name="warning">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">The dictionary element SHOULD have a dictionaryPrefix attribute specifying
                    the default prefix to use when referencing dictionary entries
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="not(@title)">
            <xsl:call-template name="warning">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">The dictionary element SHOULD have a title attribute intended for
                    human-readability
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:choose>
            <xsl:when test="cml:description">
                <xsl:if test="count(cml:description) > 1">
                    <xsl:call-template name="error">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="." mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">The dictionary element can only have a single description child
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
                    <xsl:with-param name="text">The dictionary element SHOULD have a single description child element
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:if test="not(.//cml:entry)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">a dictionary must have at least one entry</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:apply-templates mode="dictionary"/>
    </xsl:template>


    <xsl:template match="cml:entry" mode="dictionary">
        <xsl:if test="not(parent::cml:dictionary)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">entry must be the child of dictionary</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:choose>
            <xsl:when test="@id">
                <xsl:if test="count(ancestor::cml:dictionary//cml:entry[@id = current()/@id]) > 1">
                    <xsl:call-template name="error">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="." mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">the id of an entry must be unique within the dictionary (duplicate found: <xsl:value-of select="@id"/>)
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">an entry must have an id</xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:choose>
            <xsl:when test="@term">
                <xsl:if test="not(translate(@term, $ascii-chars, '') = '')">
                    <xsl:call-template name="warning">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="@term" mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">the value of the term attribute MAY contain any valid unicode character, however it is RECOMMENDED that any character from outside of the ASCII subset (codepoints 32-127) is represented using an entity reference.</xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">an entry must have a term attribute - this is a unique nounal phrase
                        linguistically identifying the object
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:if test="count(.//cml:definition) != 1">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">An entry element MUST contain a single definition child element
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="count(cml:description) > 1">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">An entry element MAY contain a single description child element
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="not(@dataType)">
            <xsl:call-template name="warning">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">When applicable to the concept defined, an entry SHOULD have dataType
                    attribute
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="not(@unitType)">
            <xsl:call-template name="warning">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">When applicable to the concept defined, an entry SHOULD have unitType
                    attribute
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="not(@units)">
            <xsl:call-template name="warning">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">When applicable to the concept defined, an entry SHOULD have a units
                    attribute, the value of which is a QName referencing the default units
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:apply-templates mode="dictionary"/>
    </xsl:template>

    <xsl:template match="cml:definition" mode="dictionary">
        <xsl:if test="not(parent::cml:entry)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">definition must be the child of entry</xsl:with-param>
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

        <xsl:if test="*[not(namespace-uri()= $xhtmlNS)]">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">The definition element MUST NOT contain any child elements not in the
                    http://www.w3.org/1999/xhtml namespace.
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:apply-templates mode="dictionary"/>
    </xsl:template>


    <xsl:template match="cml:description" mode="dictionary">
        <xsl:if test="not(parent::cml:entry or parent::cml:dictionary)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">description must be the child of entry or dictionary</xsl:with-param>
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

        <xsl:if test="*[not(namespace-uri()= $xhtmlNS)]">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">The description element MUST NOT contain any child elements not in the
                    http://www.w3.org/1999/xhtml namespace.
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:apply-templates mode="dictionary"/>
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