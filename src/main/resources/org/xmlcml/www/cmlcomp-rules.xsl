<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet
        version="2.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:cml="http://www.xml-cml.org/schema"
        xmlns:o="http://www.xml-cml.org/report"
        xmlns:other="http://www.other.org/"
        xmlns:svrl="http://www.a.com">

    <xsl:output method="xml" omit-xml-declaration="no" standalone="yes" indent="yes" />
    <xsl:variable name="conventionNS">http://www.xml-cml.org/conventions/</xsl:variable>

    <!-- Apply templates to root node. The matching pattern here apply to any root tag
         but we are expecting cml container -->
    <xsl:template match="/">
        <o:result>
            <xsl:apply-templates mode="cmlcomp"/>
        </o:result>
    </xsl:template>

    <!-- Match top level text data in xml and apply templates -->
    <xsl:template match="*|@*|text()">
        <xsl:apply-templates />
    </xsl:template>

    <!-- Choosing convention from cml root element. Convention has become a required
         attribute. -->
    <xsl:template match="cml:cml[@convention]" mode="cmlcomp">
        <xsl:choose>
            <xsl:when test="namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = 'cmlcomp'">
                <xsl:choose>
                    <xsl:when test="count(child::cml:module[@role='joblist']) = count(child::*)">
                        <xsl:apply-templates mode="cmlcomp" />
                    </xsl:when>
                    <xsl:otherwise>
                        <o:error>
                            <xsl:attribute name="location">
                                <xsl:apply-templates select="." mode="get-full-path" />
                            </xsl:attribute>
                            CMLComp can only have children of module[@role='joblist']
                        </o:error>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <o:warning>No cml element found with correct convention</o:warning>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="cml:module[@role='joblist']" mode="cmlcomp">
        <xsl:if test="not(parent::cml:cml)">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:attribute>
                module[@role='joblist'] must be within cml element
            </o:error>
        </xsl:if>
        <xsl:if test="count(child::cml:module[@role='job']) = 0">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:attribute>
                module[@role='joblist'] must contains at least one module[@role='job']
            </o:error>
        </xsl:if>
        <xsl:if test="not(count(child::cml:module[@role='job']) + count(child::cml:identifier) = count(child::*))">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:attribute>
                module[@role='joblist'] can only contains at least one module[@role='job'] with optional identifier.
                other cml elements are not allowed here.
            </o:error>
        </xsl:if>
        <xsl:apply-templates mode="cmlcomp" />
    </xsl:template>

    <!-- process the job list jobs -->
    <xsl:template match="cml:module[@role='job']" mode="cmlcomp">
        <xsl:if test="not(parent::cml:module[@role='joblist'])">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:attribute>
                module job must be within module joblist 
            </o:error>
        </xsl:if>
        <xsl:if test="not(@title)">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:attribute>
                module job must have a title
            </o:error>
        </xsl:if>
        <!-- test if module[@role='job'] must contain module[@role='init'] and module[@role='final']. it may contain optional module[@role='calculation']-->
        <xsl:if test="not(count(child::cml:module[@role='init'])=1 and count(child::cml:module[@role='final'])=1 and count(child::cml:module[@role='calculation'])&lt;=1)">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:attribute>
                module[@role='job'] must contain 1 module[@role='init'] and 1 module[@role='final'] and optional module[@role='calculation']
            </o:error>
        </xsl:if>
        <xsl:if test="not(count(child::cml:module[@role='init']) + count(child::cml:module[@role='final']) + count(child::cml:module[@role='calculation']) = count(child::*))">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:attribute>
                module[@role='job'] must contain 1 module[@role='init'] and 1 module[@role='final'] and optional module[@role='calculation'].
                other cml elements are not allowed here.
            </o:error>
        </xsl:if>
        <!-- test that the title is unique with in the joblist -->
        <xsl:choose>
            <xsl:when test="count(ancestor::cml:module[@role='joblist']//cml:module[@role = 'job'][@title = current()/@title]) = 1"/>
            <xsl:otherwise>
                <o:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:attribute>
                    module job titles must be unique within the job list
                    <xsl:text/>
                    <xsl:value-of select="@title"/>
                    <xsl:text/>
                    )
                </o:error>
            </xsl:otherwise>
        </xsl:choose>
        <!-- test if init is the first element -->
        <xsl:if test="not(child::*[1]/attribute::role = 'init')">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:attribute>
                module[@role='init'] must be the first module in module[@role='job']
            </o:error>
        </xsl:if>
        <!-- test if final is the last element -->
        <xsl:if test="not(child::*[last()]/attribute::role = 'final')">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:attribute>
                module[@role='final'] must be the last module in module[@role='job']
            </o:error>
        </xsl:if>

        <xsl:apply-templates mode="cmlcomp" />
    </xsl:template>

    <xsl:template match="cml:module[@role='init']" mode="cmlcomp">
        <!-- Contain : molecule [Must = 1?], parameterList [Must = 1?], propertyList [optional = 1] -->
        <xsl:if test="not(parent::cml:module[@role='job'])">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:attribute>
                module[@role='init'] must be within module module[@role='job']
            </o:error>
        </xsl:if>
        <xsl:if test="not(count(child::cml:molecule) + count(child::cml:parameterList) + count(child::cml:propertyList) = count(child::cml:*))">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:attribute>
                found elements other than molecule, parameterList and propertyList
            </o:error>
        </xsl:if>
        <xsl:choose>
            <!-- Apply cmllite if cml:molecule is found. -->
            <xsl:when test="count(child::cml:molecule) = 1">
                <xsl:for-each select="cml:molecule">
                    <xsl:apply-templates select="." mode="cmllite"/>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
                <o:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:attribute>
                        exactly one molecule is allowed in module[@role='init']
                </o:error>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="count(child::cml:parameterList) = 1">
                <xsl:apply-templates mode="cmlcomp"/>
            </xsl:when>
            <xsl:otherwise>
                <o:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:attribute>
                    exactly one parameterList must be in module[@role='init'], an initialization module must contain
                    parameters for setting up computational job.
                </o:error>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="count(child::cml:propertyList) &lt;= 1">
                <xsl:apply-templates mode="cmlcomp"/>
            </xsl:when>
            <xsl:otherwise>
                <o:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:attribute>
                    only 1 or no propertyList is allowed in module[@role='init'], it is unusal to have preoperty in
                    the initializing step.
                </o:error>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="cml:module[@role='calculation']" mode="cmlcomp">
        <!-- Contain : molecule [Optional], parameterList [Optional], propertyList [Must = 1] -->
        <xsl:if test="not(parent::cml:module[@role='job'])">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:attribute>
                module[@role='calculation'] must be within module module[@role='job']
            </o:error>
        </xsl:if>
        <xsl:if test="not(count(child::cml:molecule) + count(child::cml:parameterList) + count(child::cml:propertyList) = count(child::cml:*))">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:attribute>
                found elements other than molecule, parameterList and propertyList
            </o:error>
        </xsl:if>
        <xsl:choose>
            <!-- Apply cmllite if cml:molecule is found. -->
            <xsl:when test="count(child::cml:molecule) &lt;= 1">
                <xsl:for-each select="cml:molecule">
                    <xsl:apply-templates select="." mode="cmllite"/>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
                <o:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:attribute>
                        zero or one 1 molecule is allowed in module[@role='calculation']
                </o:error>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="count(child::cml:parameterList) &lt;= 1">
                <xsl:apply-templates mode="cmlcomp"/>
            </xsl:when>
            <xsl:otherwise>
                <o:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:attribute>
                    zero or one parameterList is allowed in module[@role='calculation']
                </o:error>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="count(child::cml:propertyList) = 1">
                <xsl:apply-templates mode="cmlcomp"/>
            </xsl:when>
            <xsl:otherwise>
                <o:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:attribute>
                    exactly one propertyList must be in module[@role='calculation']
                </o:error>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="cml:module[@role='final']" mode="cmlcomp">
        <!-- Contain : molecule [Must = 1?], parameterList [None], propertyList [Must = 1] -->
        <xsl:if test="not(parent::cml:module[@role='job'])">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:attribute>
                module[@role='final'] must be within module module[@role='job']
            </o:error>
        </xsl:if>
        <xsl:if test="not(count(child::cml:molecule) + count(child::cml:parameterList) + count(child::cml:propertyList) = count(child::cml:*))">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:attribute>
                found elements other than molecule and propertyList
            </o:error>
        </xsl:if>
        <xsl:choose>
            <!-- Apply cmllite if cml:molecule is found. -->
            <xsl:when test="count(child::cml:molecule) = 1">
                <xsl:for-each select="cml:molecule">
                    <xsl:apply-templates select="." mode="cmllite"/>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
                <o:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:attribute>
                        exactly one molecule is allowed in module[@role='final']
                </o:error>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="not(count(child::cml:parameterList) = 0)">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:attribute>
                no parameterList is allowed in module[@role='final']
            </o:error>
        </xsl:if>
        <xsl:choose>
            <xsl:when test="count(child::cml:propertyList) = 1">
                <xsl:apply-templates mode="cmlcomp"/>
            </xsl:when>
            <xsl:otherwise>
                <o:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:attribute>
                    exactly one propertyList must be in module[@role='final']
                </o:error>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="cml:parameterList" mode="cmlcomp">
        <xsl:if test="not(parent::cml:module[@role='init'] or parent::cml:module[@role='calculation'])">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:attribute>
                parameterList must be within module[@role='init'], module[@role='calculation']
            </o:error>
        </xsl:if>
        <xsl:if test="not(count(child::cml:parameter) = count(child::cml:*))">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:attribute>
                found elements other than parameter in parameterList
            </o:error>
        </xsl:if>
    </xsl:template>
    <xsl:template match="cml:propertyList" mode="cmlcomp">
        <xsl:if test="not(parent::cml:module[@role='init'] or parent::cml:module[@role='calculation'] or parent::cml:module[@role='final'])">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:attribute>
                propertyList must be within module module[@role='init'], module module[@role='calculation'] or module module[@role='final']
            </o:error>
        </xsl:if>
        <xsl:if test="not(count(child::cml:property) = count(child::cml:*))">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:attribute>
                found elements other than property in propertyList
            </o:error>
        </xsl:if>
    </xsl:template>

    <xsl:template match="cml:property" mode="cmlcomp">
        <!-- Contain : @dictRef [Must], one of scalar, array and matrix  -->
        <xsl:choose>
            <xsl:when test="@dictRef">
                <xsl:choose>
                    <xsl:when test="count(child::*) = 1">
                        <xsl:if test="not(count(child::cml:scalar) + count(child::cml:array) + count(child::cml:matrix) = 1)">
                            <o:error>
                                <xsl:attribute name="location">
                                    <xsl:apply-templates select="." mode="get-full-path"/>
                                </xsl:attribute>
                                found '
                                <xsl:value-of select="name((child::*)[1])"/>' element. only 'scalar', 'array' or 'matrix' is allowed.
                            </o:error>
                        </xsl:if>
                    </xsl:when>
                    <xsl:otherwise>
                        <o:error>
                            <xsl:attribute name="location">
                                <xsl:apply-templates select="." mode="get-full-path"/>
                            </xsl:attribute>
                            <xsl:value-of select="count(child::*)"/> elements found. exactly one element is expected.
                        </o:error>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <o:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:attribute>
                    @dictRef is required for a scalar.
                </o:error>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="cml:parameter" mode="cmlcomp">
        <!-- Contain : @dictRef [Must], one of scalar, array and matrix  -->
        <xsl:choose>
            <xsl:when test="@dictRef">
                <xsl:choose>
                    <xsl:when test="count(child::*) = 1">
                        <xsl:if test="not(count(child::cml:scalar) + count(child::cml:array) + count(child::cml:matrix) = 1)">
                            <o:error>
                                <xsl:attribute name="location">
                                    <xsl:apply-templates select="." mode="get-full-path"/>
                                </xsl:attribute>
                                found '
                                <xsl:value-of select="name((child::*)[1])"/>' element. only 'scalar', 'array' or 'matrix' is allowed.
                            </o:error>
                        </xsl:if>
                    </xsl:when>
                    <xsl:otherwise>
                        <o:error>
                            <xsl:attribute name="location">
                                <xsl:apply-templates select="." mode="get-full-path"/>
                            </xsl:attribute>
                            <xsl:value-of select="count(child::*)"/> elements found. exactly one element is expected.
                        </o:error>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <o:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:attribute>
                    @dictRef is required for a scalar.
                </o:error>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="cml:scalar" mode="cmlcomp">
        <!-- Contain : @dataType [Must] in xsd:double, .. 
        parent must be property or parameter
        -->
        <xsl:if test="not(@dataType)">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:attribute>
                @dataType is required for a scalar.
            </o:error>
        </xsl:if>
    </xsl:template>

    <xsl:template match="cml:array" mode="cmlcomp">
        <!-- Contain : @dataType [Must], -->
        <xsl:if test="not(@dataType)">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:attribute>
                @dataType is required for an array.
            </o:error>
        </xsl:if>
    </xsl:template>

    <xsl:template match="cml:matrix" mode="cmlcomp">
        <!-- Contain : @dataType [Must],  -->
        <xsl:if test="not(@dataType)">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:attribute>
                @dataType is required for a matrix.
            </o:error>
        </xsl:if>
    </xsl:template>



    <!-- Apply template rules to text -->
    <xsl:template match="*|@*|text()" mode="cmlcomp">
        <xsl:apply-templates mode="cmlcomp" />
    </xsl:template>

    <xsl:template match="cml:molecule" mode="cmllite">
        <xsl:if test="not(parent::cml:cml or parent::cml:molecule or parent::cml:module)">
            <o:error>
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:attribute>
                molecule must be within molecule or cml elements
            </o:error>
        </xsl:if>
        <xsl:if test="parent::cml:molecule">
            <xsl:if test="not(@count)">
                <o:error>
                    <xsl:attribute name="location">
                        <xsl:apply-templates select="." mode="get-full-path" />
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
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:attribute>
                atomArray must contain atoms
            </o:error>
        </xsl:if>
        <xsl:if test="not(parent::cml:formula or parent::cml:molecule)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:with-param>
                <xsl:with-param name="text">
                    atomArray must be within molecule or formula elements
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
                    <xsl:apply-templates select="." mode="get-full-path" />
                </xsl:attribute>
                <svrl:text>
                    atoms must have id unless they are part of an atomArray in a formula
                </svrl:text>
            </svrl:failed-assert>
        </xsl:if>

        <!-- atoms must have elementType -->
        <xsl:if test="not(@elementType)">
            <svrl:failed-assert test="not(@elementType)">
                <xsl:attribute name="location">
                    <xsl:apply-templates select="." mode="get-full-path" />
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