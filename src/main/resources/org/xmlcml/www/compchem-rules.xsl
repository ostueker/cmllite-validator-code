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
    <xsl:variable name="initializationName">initialization</xsl:variable>
    <xsl:variable name="calculationName">calculation</xsl:variable>
    <xsl:variable name="finalizationName">finalization</xsl:variable>
    <xsl:variable name="environmentName">environment</xsl:variable>

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
    <xsl:template
            match="cml:module[@convention and namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName]"
            mode="compchem" name="compchem-template">
        <xsl:if
                test="not(child::cml:module[namespace-uri-for-prefix(substring-before(@dictRef, ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $jobListName])">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">A compchem module MUST contain at least one jobList module element
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
                <xsl:if test="count(ancestor::cml:module[namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName]//cml:*[@id = current()/@id]) > 1">
                    <xsl:call-template name="error">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="." mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">the id of a module but be unique within the containing compchem
                            module (note: this means all cml elements with ids) (duplicate found: <xsl:value-of
                                    select="@id"/>)
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
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

        <xsl:if
                test="not(child::cml:module[namespace-uri-for-prefix(substring-before(@dictRef, ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $jobName])">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">A jobList module MUST contain at least one job module child element
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:choose>
            <xsl:when test="@title">
                <xsl:apply-templates select="@title" mode="title-attribute-rules" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="warning">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">a jobList module SHOULD have a title attribute</xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:apply-templates mode="compchem"/>
    </xsl:template>

    <!-- job module -->
    <xsl:template
            match="cml:module[@dictRef and namespace-uri-for-prefix(substring-before(@dictRef, ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $jobName]"
            mode="compchem">

        <xsl:choose>
            <xsl:when test="@id">
                <xsl:if test="count(ancestor::cml:module[namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName]//cml:*[@id = current()/@id]) > 1">
                    <xsl:call-template name="error">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="." mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">the id of a module but be unique within the containing compchem
                            module (note: this means all cml elements with ids) (duplicate found: <xsl:value-of
                                    select="@id"/>)
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
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

        <!-- must have exactly one initialization modules -->
        <xsl:if
                test="not(count(child::cml:module[namespace-uri-for-prefix(substring-before(@dictRef, ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $initializationName])=1)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">A job module MUST have exactly one initialization module
                    (found <xsl:value-of
                            select="count(child::cml:module[namespace-uri-for-prefix(substring-before(@dictRef, ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $initializationName])"/>)
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!--a job module element MUST NOT contain more than one finalization module element child-->
        <xsl:if test="count(child::cml:module[namespace-uri-for-prefix(substring-before(@dictRef, ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $finalizationName]) > 1">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    a job module element MUST NOT contain more than one finalization module element child
                    (found <xsl:value-of
                        select="count(child::cml:module[namespace-uri-for-prefix(substring-before(@dictRef, ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $finalizationName])"/>)
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!-- a job module element MUST NOT contain more than one environment module element child -->
        <xsl:if test="count(child::cml:module[namespace-uri-for-prefix(substring-before(@dictRef, ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $environmentName]) > 1">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    a job module element MUST NOT contain more than one environment module element child
                    (found <xsl:value-of
                        select="count(child::cml:module[namespace-uri-for-prefix(substring-before(@dictRef, ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $environmentName])"/>)
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!--If a calculation modules element is present, a finalization module element MUST also be present as a child of a job module element.-->
        <xsl:if test="child::cml:module[namespace-uri-for-prefix(substring-before(@dictRef, ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $calculationName]
                                and not(child::cml:module[namespace-uri-for-prefix(substring-before(@dictRef, ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $finalizationName])">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    if a calculation module is present there must be finalization module
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!--A job module element SHOULD contain a title the value of which MUST be a non-empty string specifying a human-readable title for the module-->
        <xsl:choose>
            <xsl:when test="@title">
                <xsl:apply-templates select="@title" mode="title-attribute-rules"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="warning">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">a job module SHOULD have a title attribute</xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:apply-templates mode="compchem"/>
    </xsl:template>

    <!-- initialization module -->
    <xsl:template
            match="cml:module[@dictRef and namespace-uri-for-prefix(substring-before(@dictRef, ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $initializationName]"
            mode="compchem">

        <!--An initialization module element MUST NOT contain more than one molecule child element. -->
        <xsl:if test="count(child::cml:molecule) > 1">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    An initialization module element MUST NOT contain more than one molecule child element
                    (found <xsl:value-of select="count(child::cml:molecule)"/>)
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!-- The molecule MUST specify a convention using the convention attribute -->
        <xsl:choose>
            <xsl:when test="child::cml:molecule[@convention]">
                <!-- and the convention SHOULD be one of the RECOMMENDED molecular conventions -->
                <xsl:call-template name="molecule-convention-rules">
                    <xsl:with-param name="convention-attribute" select="child::cml:molecule/@convention"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">
                        The molecule MUST specify a convention using the convention attribute
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <!--An initialization module element MUST NOT contain more than one parameterList element.-->
        <xsl:if test="count(child::cml:parameterList) > 1">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    An initialization module element MUST NOT contain more than one parameterList element
                    (found <xsl:value-of select="count(child::cml:parameterList)"/>)
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!--An initialization module element MUST contain at least one child of molecule, parameterList or user defined module element.-->
        <xsl:if test="not(child::cml:molecule or child::cml:module[@dictRef])">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    An initialization module element MUST contain at least one child of molecule, parameterList or user
                    defined module element
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:apply-templates mode="compchem"/>
    </xsl:template>

    <!-- calculation module -->
    <xsl:template
            match="cml:module[@dictRef and namespace-uri-for-prefix(substring-before(@dictRef, ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $calculationName]"
            mode="compchem">

        <!-- A calculation module element MUST NOT contain more than one molecule child element -->
        <xsl:if test="count(child::cml:molecule) > 1">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    A calculation module element MUST NOT contain more than one molecule child element
                    (found <xsl:value-of select="count(child::cml:molecule)"/>)
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!-- The molecule MUST specify a convention using the convention attribute -->
        <xsl:choose>
            <xsl:when test="child::cml:molecule[@convention]">
                <!-- and the convention SHOULD be one of the RECOMMENDED molecular conventions -->
                <xsl:call-template name="molecule-convention-rules">
                    <xsl:with-param name="convention-attribute" select="child::cml:molecule/@convention"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">
                        The molecule MUST specify a convention using the convention attribute
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <!--A calculation module element MUST NOT contain more than one parameterList element.-->
        <xsl:if test="count(child::cml:parameterList) > 1">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    A calculation module element MUST NOT contain more than one parameterList element
                    (found <xsl:value-of select="count(child::cml:parameterList)"/>)
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!--A calculation module element MUST NOT contain more than one propertyList element.-->
        <xsl:if test="count(child::cml:propertyList) > 1">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    A calculation module element MUST NOT contain more than one parameterList element
                    (found <xsl:value-of select="count(child::cml:propertyList)"/>)
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!-- A calculation module element MUST contain at least one child of molecule, parameterList, propertyList or user
        defined module elements. -->
        <xsl:if test="not(child::cml:molecule or child::cml:parameterList or child::cml:propertyList or child::cml:module[@dictRef])">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    A calculation module element MUST contain at least one child of molecule, parameterList,
                    propertyList or user
                    defined module element
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:apply-templates mode="compchem"/>
    </xsl:template>

    <!-- finalization module -->
    <xsl:template
            match="cml:module[@dictRef and namespace-uri-for-prefix(substring-before(@dictRef, ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $finalizationName]"
            mode="compchem">

        <!-- A finalization module element MUST NOT contain more than one molecule child element -->
        <xsl:if test="count(child::cml:molecule) > 1">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    A finalization module element MUST NOT contain more than one molecule child element
                    (found <xsl:value-of select="count(child::cml:molecule)"/>)
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!-- The molecule MUST specify a convention using the convention attribute -->
        <xsl:choose>
            <xsl:when test="child::cml:molecule[@convention]">
                <!-- and the convention SHOULD be one of the RECOMMENDED molecular conventions -->
                <xsl:call-template name="molecule-convention-rules">
                    <xsl:with-param name="convention-attribute" select="child::cml:molecule/@convention"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">
                        The molecule MUST specify a convention using the convention attribute
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <!--A finalization module element MUST NOT contain more than one propertyList element.-->
        <xsl:if test="count(child::cml:propertyList) > 1">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    A finalization module element MUST NOT contain more than one parameterList element
                    (found <xsl:value-of select="count(child::cml:propertyList)"/>)
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!-- A finalization module element MUST contain at least one child of molecule, propertyList or user
        defined module elements. -->
        <xsl:if test="not(child::cml:molecule or child::cml:propertyList or child::cml:module[@dictRef])">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    A finalization module element MUST contain at least one child of molecule, propertyList or user
                    defined module element
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!--A finalization module MUST NOT contain a parameterList child element -->
        <xsl:if test="child::cml:parameterList">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="child::cml:parameterList" mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    A finalization module MUST NOT contain a parameterList child element
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!--A finalization module MUST NOT contain a parameter child element -->
        <xsl:if test="child::cml:parameter">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="child::cml:parameter" mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    A finalization module MUST NOT contain a parameter child element
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:apply-templates mode="compchem"/>
    </xsl:template>

    <!-- environment module -->
    <xsl:template
            match="cml:module[@dictRef and namespace-uri-for-prefix(substring-before(@dictRef, ':'),.) = $compChemDict and substring-after(@dictRef, ':') = $environmentName]"
            mode="compchem">

        <!--A environment module element MUST NOT contain more than one propertyList element.-->
        <xsl:if test="count(child::cml:propertyList) > 1">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    A environment module element MUST NOT contain more than one parameterList element
                    (found <xsl:value-of select="count(child::cml:propertyList)"/>)
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!-- An environment property element MUST be a child of a propertyList element -->
        <xsl:if test="child::cml:property">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="child::cml:property" mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    An environment property element MUST be a child of a propertyList element
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!-- An environment module element MUST contain at least one child of propertyList or user
        defined module elements. -->
        <xsl:if test="not(child::cml:propertyList or child::cml:module[@dictRef])">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    An environment module element MUST contain at least one child of propertyList or user
                    defined module elements
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!--An environment module MUST NOT contain a parameterList child element -->
        <xsl:if test="child::cml:parameterList">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="child::cml:parameterList" mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    An environment module MUST NOT contain a parameterList child element
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <!--An environment module MUST NOT contain a parameter child element -->
        <xsl:if test="child::cml:parameter">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="child::cml:parameter" mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    An environment module MUST NOT contain a parameter child element
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:apply-templates mode="compchem"/>
    </xsl:template>

    <!-- parameterList rules -->
    <xsl:template match="cml:parameterList" mode="compchem">
        <xsl:if test="not(child::cml:parameter)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    A parameterList must contain at least one parameter element child
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>


        <xsl:apply-templates mode="compchem"/>
    </xsl:template>

    <!-- propertyList rules -->
    <xsl:template match="cml:propertyList" mode="compchem">
        <xsl:if test="not(child::cml:property)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    A propertyList must contain at least one property element child
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:apply-templates mode="compchem"/>
    </xsl:template>

    <!-- parameter rules -->
    <xsl:template match="cml:parameter" mode="compchem">

        <xsl:if test="not(@dictRef)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    A parameter MUST declare a dictRef attribute
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:choose>
            <xsl:when test="count(child::*) = 1">
                <xsl:if test="not(child::cml:scalar or child::cml:array or child::cml:matrix)">
                    <xsl:call-template name="error">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="." mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">
                            A parameter MUST have either a scalar, array or matrix child
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">
                        A parameter MUST have a single element child (found:
                        <xsl:value-of select="count(child::*)"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:apply-templates mode="compchem"/>
    </xsl:template>

    <!-- property rules -->
    <xsl:template match="cml:property" mode="compchem">

        <xsl:if test="not(@dictRef)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">
                    A property MUST declare a dictRef attribute
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:choose>
            <xsl:when test="count(child::*) = 1">
                <xsl:if test="not(child::cml:scalar or child::cml:array or child::cml:matrix)">
                    <xsl:call-template name="error">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="." mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">
                            A property MUST have either a scalar, array or matrix child
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">
                        A property MUST have a single element child (found:
                        <xsl:value-of select="count(child::*)"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>


        <xsl:apply-templates mode="compchem"/>
    </xsl:template>

    <xsl:template match="cml:module[not(@dictRef)]" mode="compchem">
        <xsl:call-template name="error">
            <xsl:with-param name="location">
                <xsl:apply-templates mode="get-full-path" select="."/>
            </xsl:with-param>
            <xsl:with-param name="text">
                All module elements must have a dictRef
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>


    <xsl:template name="molecule-convention-rules">
        <xsl:param name="convention-attribute"/>
        <xsl:call-template name="info">
            <xsl:with-param name="location">
                <xsl:apply-templates select="$convention-attribute" mode="get-full-path"/>
            </xsl:with-param>
            <xsl:with-param name="text">
                We need to check that the convention of the molecule matches one of the
                recommended list - otherwise raise a warning.
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="@title" mode="title-attribute-rules">
        <xsl:if test="string-length(normalize-space(.)) = 0">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">the title attribute MUST NOT be empty and MUST contain non-whitespace
                    characters
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="not(translate(., $ascii-chars, '') = '')">
            <xsl:call-template name="info">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">the value of the title attribute MAY contain any valid unicode character,
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