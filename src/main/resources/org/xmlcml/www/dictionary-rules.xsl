<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cml="http://www.xml-cml.org/schema"
                xmlns:report="http://www.xml-cml.org/report/" >
    <xsl:output method="xml" omit-xml-declaration="no"
                standalone="yes" indent="yes"/>
    <xsl:param name="absoluteXPathToStartElement" select="/" />

    <xsl:variable name="conventionName">dictionary</xsl:variable>

    <xsl:variable name="conventionNS">http://www.xml-cml.org/convention/</xsl:variable>
    <xsl:variable name="cmlNS">http://www.xml-cml.org/schema</xsl:variable>

    <xsl:template match="/">
        <report:result>
            <xsl:apply-templates />
        </report:result>
    </xsl:template>

    <xsl:template match="*|@*|text()">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="*|@*|text()" mode="dictionary">
        <xsl:apply-templates mode="dictionary"/>
    </xsl:template>

     <xsl:template
            match="cml:*[@convention and namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName]">
        <xsl:choose>
            <xsl:when test="self::cml:cml">
                <xsl:choose>
                    <xsl:when test="not(cml:dictionary)">
                        <!-- no dictionary children -->
                        <xsl:call-template name="error">
                            <xsl:with-param name="location">
                                <xsl:apply-templates select="." mode="get-full-path"/>
                            </xsl:with-param>
                            <xsl:with-param name="text">there must be at least one "dictionary" element as a child of the
                                "cml" element declaring the dictionary convention
                            </xsl:with-param>
                        </xsl:call-template>
                        <xsl:apply-templates mode="dictionary"/>
                    </xsl:when>
                    <xsl:when test="cml:dictionary[not(@convention)]">
                        <xsl:apply-templates mode="dictionary" />
                    </xsl:when>
                    <xsl:otherwise>
                        <!-- there are dictionary children -->
                        <xsl:choose>
                            <xsl:when test="cml:dictionary[@convention]">
                                <!-- which have convention specified -->
                                <xsl:choose>
                                    <xsl:when
                                            test="count(cml:dictionary[@convention and namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName]) > 0">
                                        <!-- there are dictionary elements in the dictionary convention -->
                                        <count><xsl:value-of select="count(cml:dictionary[@convention and namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName])" /></count>
                                        <xsl:apply-templates mode="dictionary"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:call-template name="error">
                                            <xsl:with-param name="location">
                                                <xsl:apply-templates select="." mode="get-full-path"/>
                                            </xsl:with-param>
                                            <xsl:with-param name="text">there must be at least one "dictionary" element in
                                                the dictionary convention
                                            </xsl:with-param>
                                        </xsl:call-template>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:when>
                            <xsl:otherwise>
                                <!-- without any convention specified therefore they must be in the dictionary convention -->
                                <xsl:apply-templates mode="dictionary"/>
                            </xsl:otherwise>
                        </xsl:choose>
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
        <xsl:choose>
            <xsl:when test="@convention">
                <xsl:choose>
                    <xsl:when test="namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName">
                        <xsl:apply-templates mode="dictionary"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates />
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="warning">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="@convention" mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">the <xsl:value-of select="$conventionName" /> convention does not include the element "<xsl:value-of select="name(.)" />". A different convention should be specified on this element</xsl:with-param>
                </xsl:call-template>
                <xsl:apply-templates mode="dictionary" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="cml:dictionary" mode="dictionary" name="dictionary-template">
        <xsl:if test="..">
            <xsl:if test="parent::cml:* and not(parent::cml:cml)">
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">the dictionary element can be the root node or a child of a "cml" element only</xsl:with-param>
                </xsl:call-template>
            </xsl:if>
        </xsl:if>

        <xsl:if test="not(cml:entry)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">a dictionary must have at least one entry</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="not(@namespace)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">a dictionary must declare the namespace attribute</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="not(@title)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">a dictionary must have a title attribute</xsl:with-param>
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
                        <xsl:with-param name="text">the id of an entry must be unique within the eldest containing
                            dictionary (duplicate found: <xsl:value-of select="@id"/>)</xsl:with-param>
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

        <xsl:if test="not(@dataType)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">an entry must have a dataType</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="not(cml:description)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">an entry must have a description</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:apply-templates mode="dictionary"/>
    </xsl:template>

    <xsl:template match="cml:description" mode="dictionary">
          <xsl:if test="not(parent::cml:entry)">
              <xsl:call-template name="error">
                  <xsl:with-param name="location">
                      <xsl:apply-templates select="." mode="get-full-path"/>
                  </xsl:with-param>
                  <xsl:with-param name="text">description must be the child of entry</xsl:with-param>
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