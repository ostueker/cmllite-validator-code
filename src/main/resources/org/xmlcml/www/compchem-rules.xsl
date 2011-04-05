<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cml="http://www.xml-cml.org/schema"
                xmlns:report="http://www.xml-cml.org/report/"
                xmlns:xhtml="http://www.w3.org/1999/xhtml"
        >
    <xsl:output method="xml" omit-xml-declaration="no" standalone="yes" indent="yes"/>

    <xsl:variable name="conventionName">compchem</xsl:variable>

    <xsl:variable name="conventionNS">http://www.xml-cml.org/convention/</xsl:variable>
    <xsl:variable name="compChemDict">http://www.xml-cml.org/dictionary/compchem/</xsl:variable>

    <xsl:variable name="cmlNS">http://www.xml-cml.org/schema</xsl:variable>
    <xsl:variable name="xhtmlNS">http://www.w3.org/1999/xhtml</xsl:variable>
    <xsl:variable name="ascii-chars">&#32;!"#$%&amp;'()*+,-./0123456789:;&lt;=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~&#127;</xsl:variable>

    <xsl:variable name="jobListName">jobList</xsl:variable>
    <xsl:variable name="jobName">job</xsl:variable>


    <xsl:template match="/">
        <report:result>
            <xsl:apply-templates/>
        </report:result>
    </xsl:template>

    <xsl:template match="*|@*|text()">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*|@*|text()" mode="compchem">
        <xsl:apply-templates mode="compchem"/>
    </xsl:template>

    <xsl:template
            match="cml:*[@convention and namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName]">
        <xsl:choose>
            <xsl:when test="self::cml:module">
                <xsl:call-template name="compchem-template"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="@convention" mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">Only the module element can specify the compchem convention
                    </xsl:with-param>
                </xsl:call-template>
                <xsl:apply-templates/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <!-- the module specifying the convention -->
    <xsl:template match="cml:module[@convention and namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName]" mode="compchem" name="compchem-template">
        <xsl:if
                test="not(child::cml:module[@dictRef and namespace-uri-for-prefix(substring-before(@dictRef/., ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $jobListName])">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">A compchem module MUST contain at least one jobList module element e.g.
                    &lt;module dictRef='compchem:jobList' &gt;
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:apply-templates mode="compchem"/>
    </xsl:template>

    <!-- jobList module -->
    <xsl:template
            match="cml:module[@dictRef and namespace-uri-for-prefix(substring-before(@dictRef, ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $jobListName]"
            mode="compchem">

        <xsl:choose>
            <xsl:when test="@id">

            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">A jobList module element MUST have an id attribute specified.
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

    <!-- job module -->
    <xsl:template
            match="cml:module[@dictRef and namespace-uri-for-prefix(substring-before(@dictRef, ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $jobName]"
            mode="compchem">

        <xsl:choose>
            <xsl:when test="@id">

            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">A job module element MUST have an id attribute specified.
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

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