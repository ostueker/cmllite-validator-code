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
    <xsl:variable name="cmlNS">http://www.xml-cml.org/schema</xsl:variable>

	<xsl:variable name="other-value">other</xsl:variable>

    <!--
	<xsl:template match="/">
		<report:result>
            <xsl:variable name="root" select="saxon:evaluate($absoluteXPathToStartElement)" />
            <root><xsl:value-of select="$root"/> </root>
			<xsl:apply-templates select="saxon:evaluate($absoluteXPathToStartElement)" mode="molecular"/>
			<xsl:apply-templates />
		</report:result>
	</xsl:template>
     -->
    <xsl:template match="/">
        <report:result>
            <xsl:apply-templates />
        </report:result>
    </xsl:template>

    <!--
    <xsl:template match="cml:cml[@convention and namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName]">
        <xsl:apply-templates mode="molecular" />
    </xsl:template>

    <xsl:template match="cml:molecule[@convention and namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName]">
        <xsl:apply-templates mode="molecular" />
    </xsl:template>
    -->

    <xsl:template match="cml:*[@convention and namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName]">
        <xsl:choose>
            <xsl:when test="self::cml:cml or self::cml:molecule">
                <xsl:apply-templates mode="molecular" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="@convention" mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">the only valid cml elements which specify the molecular convention are "cml" and "molecule"</xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="cml:*" mode="molecular">
        <matched-priority>
            element name: <xsl:value-of select="name(.)"/>
        <xsl:choose>
            <xsl:when test="@convention">
                <has-convention />
                <xsl:choose>
                    <xsl:when test="namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName">
                        <is-molecular />
                        <xsl:apply-templates mode="molecular"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <not-molecular />
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
                <xsl:apply-templates mode="molecular" />
            </xsl:otherwise>
        </xsl:choose>
        </matched-priority>
    </xsl:template>
 <!--
    <xsl:template match="*[namespace-uri()='http://www.xml-cml.org/schema'][@convention]">
        <xsl:choose>
            <xsl:when test="namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName">
                   <xsl:
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates />
            </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="not(@convention)">
            <xsl:call-template name="warning">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text"></xsl:with-param>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
-->

     <!--
    <xsl:template match="*[namespace-uri()='http://www.xml-cml.org/schema'][@convention]">
        <xsl:choose>
            <xsl:when
                    test="namespace-uri-for-prefix(substring-before(@convention, ':'),.) = $conventionNS and substring-after(@convention, ':') = $conventionName"
                    >
                <xsl:choose>
                    <xsl:when test=". = cml:cml or . = cml:molecule">
                        <xsl:apply-templates mode="molecular" />
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="error">
                            <xsl:with-param name="location">
                                <xsl:apply-templates select="@convention" mode="get-full-path"/>
                            </xsl:with-param>
                            <xsl:with-param name="text">the only valid cml elements which specify the molecular convention are "cml" and "molecule"</xsl:with-param>
                        </xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    -->
	<xsl:template match="*|@*|text()">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="*|@*|text()" mode="molecular">
		<xsl:apply-templates mode="molecular"/>
	</xsl:template>

	<xsl:template match="cml:molecule" mode="molecular">
		<xsl:if test="..">
			<xsl:if test="parent::cml:*">
                <xsl:if test="not(parent::cml:cml or parent::cml:molecule)">
                    <xsl:call-template name="error">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="." mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">the only valid cml elements which can be parents of molecule are: molecule and cml</xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
            </xsl:if>
		</xsl:if>

		<xsl:if test="parent::cml:molecule">
			<xsl:if test="not(@count)">
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">child molecules must have a count specified</xsl:with-param>
                </xsl:call-template>
			</xsl:if>
		</xsl:if>

		<xsl:if test="cml:molecule and cml:atomArray">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">molecules with child molecules must not contain an atomArray</xsl:with-param>
            </xsl:call-template>
		</xsl:if>
		<xsl:apply-templates mode="molecular"/>
	</xsl:template>


	<xsl:template match="cml:atomArray" mode="molecular">
        <xsl:if test="not(cml:atom)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">atomArray must contain atoms</xsl:with-param>
            </xsl:call-template>
		</xsl:if>

        <xsl:if test="not(parent::cml:molecule or parent::cml:formula)">
			<xsl:call-template name="error">
				<xsl:with-param name="location">
					<xsl:apply-templates select="." mode="get-full-path"/>
				</xsl:with-param>
				<xsl:with-param name="text">atomArray must a child of a molecule or formula element</xsl:with-param>
			</xsl:call-template>
		</xsl:if>

		<xsl:if test="count(../cml:atomArray) > 1">
			<xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">there should only be one atomArray child in a <xsl:value-of select="name(..)" /> element</xsl:with-param>
            </xsl:call-template>
		</xsl:if>
		<xsl:apply-templates mode="molecular"/>
	</xsl:template>


	<xsl:template match="cml:atom" mode="molecular">
		<!--
          atoms must have id unless they are part of an atomArray in a
          formula
        -->
		<xsl:if test="not(@id)">
			<xsl:if test="not(ancestor::cml:atomArray[ancestor::cml:formula])">
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">atoms must have id unless they are part of an
                    atomArray in a formula</xsl:with-param>
                </xsl:call-template>
            </xsl:if>
        </xsl:if>

		<!-- atoms must have elementType -->
		<xsl:if test="not(@elementType)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">atoms must have elementType specified</xsl:with-param>
             </xsl:call-template>
		</xsl:if>
		<!--
          the ids of atoms must be unique within the eldest containing
          molecule
        -->
		<xsl:if test="@id">
			<xsl:if test="count(ancestor::cml:molecule//cml:atom[@id = current()/@id]) > 1">
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">the id of a atom must be unique within the eldest containing
                        molecule (duplicate found: <xsl:value-of select="@id"/>)</xsl:with-param>
                </xsl:call-template>
            </xsl:if>
		</xsl:if>

        <xsl:if test="@x2 and not(@y2)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">if atom has x2 then it must have y2</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="@y2 and not(@x2)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">if atom has y2 then it must have x2</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="@x3 and not(@y3 and @z3)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">if atom has x3 then it must have y3 and z3</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="@y3 and not(@x3 and @z3)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">if atom has y3 then it must have x3 and z3</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

        <xsl:if test="@z3 and not(@x3 and @y3)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">if atom has z3 then it must have x3 and y3</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

		<xsl:apply-templates mode="molecular"/>
	</xsl:template>

	<xsl:template match="cml:bondArray" mode="molecular">
		<xsl:if test="count(child::cml:bond) = 0">
            <xsl:call-template name="error">
				<xsl:with-param name="location">
					<xsl:apply-templates select="." mode="get-full-path"/>
				</xsl:with-param>
				<xsl:with-param name="text">bondArray must contain bonds</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="not(parent::cml:molecule)">
			<xsl:call-template name="error">
				<xsl:with-param name="location">
					<xsl:apply-templates select="."
                                         mode="get-full-path"/>
				</xsl:with-param>
				<xsl:with-param name="text">bondArray must be the child of a molecule element</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="count(../cml:bondArray) > 1">
			<xsl:call-template name="error">
				<xsl:with-param name="location">
					<xsl:apply-templates select="." mode="get-full-path"/>
				</xsl:with-param>
                <xsl:with-param name="text">there should only be one bondArray child in a <xsl:value-of select="name(..)" /> element</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<xsl:apply-templates mode="molecular"/>
	</xsl:template>

	<xsl:template match="cml:bond" mode="molecular">
		<xsl:if test="not(@atomRefs2)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">bond must have atomRefs2</xsl:with-param>
            </xsl:call-template>
        </xsl:if>

		<xsl:if test="not(ancestor::cml:molecule[1]//cml:atom[@id = substring-before(current()/@atomRefs2, ' ')])">
			    <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">the atoms in the atomRefs2 must be within the eldest containing molecule (not found [<xsl:value-of select="substring-before(@atomRefs2, ' ')"/>])</xsl:with-param>
                </xsl:call-template>
	    </xsl:if>

		<xsl:if test="not(ancestor::cml:molecule[1]//cml:atom[@id = substring-after(current()/@atomRefs2, ' ')])">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">the atoms in the atomRefs2 must be within the eldest containing molecule (not found [<xsl:value-of select="substring-before(@atomRefs2, ' ')"/>])</xsl:with-param>
            </xsl:call-template>
		</xsl:if>

		<xsl:if test="substring-before(@atomRefs2, ' ') = substring-after(@atomRefs2, ' ')">
			<xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">a bond must be between different atoms</xsl:with-param>
			</xsl:call-template>
		</xsl:if>

		<!--if the bond has an id it must be unique within the eldest containing molecule
            if it doesn't have an id it is a warning-->
		<xsl:choose>
			<xsl:when test="@id">
					<xsl:if test="not(count(ancestor::cml:molecule[1]//cml:bond[@id = current()/@id]) = 1)">
                        <xsl:call-template name="error">
                            <xsl:with-param name="location">
                                <xsl:apply-templates select="." mode="get-full-path"/>
                            </xsl:with-param>
                            <xsl:with-param name="text">the id of a bond must be unique within the eldest containing
                                   molecule (duplicate found: <xsl:value-of select="@id"/>)</xsl:with-param>
                        </xsl:call-template>
                </xsl:if>
			</xsl:when>
			<xsl:otherwise>
                 <xsl:call-template name="warning">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">it is recommended that bonds have id attributes</xsl:with-param>
                 </xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>

		<xsl:choose>
			<xsl:when test="@order">
				<xsl:if test="@order/. = $other-value">
					<xsl:if test="not(@dictRef)">
                        <xsl:call-template name="warning">
                           <xsl:with-param name="location">
                               <xsl:apply-templates select="." mode="get-full-path"/>
                           </xsl:with-param>
                           <xsl:with-param name="text">it is recommended that bonds with order "other" have further semantics given using the dictRef attribute</xsl:with-param>
                        </xsl:call-template>
					</xsl:if>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
                <xsl:call-template name="error">
                   <xsl:with-param name="location">
                       <xsl:apply-templates select="." mode="get-full-path"/>
                   </xsl:with-param>
                   <xsl:with-param name="text">bonds must have the bond order specified using the "order" attribute</xsl:with-param>
                </xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>

		<xsl:apply-templates mode="molecular"/>
	</xsl:template>

	<xsl:template match="cml:bondStereo" mode="molecular">
		<!-- bond stereo must be direct child of bond -->
		<xsl:if test="not(parent::cml:bond)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                   <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">bondStereo must be a direct child of a bond</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test=". = 'W' and @atomRefs4">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                   <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">a wedge bondStereo must not have an atomRefs4 attribute</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test=". = 'H' and @atomRefs4">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                   <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">a hatch bondStereo must not have an atomRefs4 attribute</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test=". = 'C' and not(@atomRefs4)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                   <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">a cis bondStereo must have an atomRefs4 attribute</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test=". = 'T' and not(@atomRefs4)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                   <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">a trans bondStereo must have an atomRefs4 attribute</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="@atomRefs2">
            <xsl:call-template name="check-all-atomRefs-values-are-different">
                <xsl:with-param name="atomRefsX" select="@atomRefs2"/>
            </xsl:call-template>
            <!-- the atoms must match those in the parent bond (order doesn't matter) -->
            <xsl:variable name="parent-atom-1" select="substring-before(../@atomRefs2/., ' ')"/>
            <xsl:variable name="parent-atom-2" select="substring-after(../@atomRefs2/., ' ')"/>
            <xsl:if test="not(contains(@atomRefs2/., $parent-atom-1) and contains(@atomRefs2/., $parent-atom-2))">
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                       <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">the atomRefs2 in a bondStereo must contain the atoms in the parent bond (in any order)</xsl:with-param>
                </xsl:call-template>
            </xsl:if>
        </xsl:if>

        <xsl:if test="@atomRefs4">
            <xsl:call-template name="check-all-atomRefs-values-are-different">
                <xsl:with-param name="atomRefsX" select="@atomRefs4"/>
            </xsl:call-template>
            <!-- the atomsRefs4 must contain the two atoms in the parent bond (order doesn't matter) -->
            <xsl:variable name="atom-1" select="substring-before(@atomRefs2/., ' ')"/>
            <xsl:variable name="atom-2" select="substring-after(@atomRefs2/., ' ')"/>
            <xsl:variable name="parent-atom-1" select="substring-before(../@atomRefs2/., ' ')"/>
            <xsl:variable name="parent-atom-2" select="substring-after(../@atomRefs2/., ' ')"/>
            <xsl:if test="not(contains(@atomRefs4/., $parent-atom-1) and contains(@atomRefs4/., $parent-atom-2))">
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                       <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">the atomRefs4 in a bondStereo must contain the atoms in the parent bond (in any order)</xsl:with-param>
                </xsl:call-template>
            </xsl:if>
        </xsl:if>

        <xsl:if test=". = $other-value and not(@dictRef)">
            <xsl:call-template name="warning">
                <xsl:with-param name="location">
                   <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">a bondStereo of type "other" should use a dictRef to add further semantics</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
		<xsl:apply-templates mode="molecular"/>
	</xsl:template>

    <xsl:template match="cml:atomParity" mode="molecular">
        <xsl:if test="not(parent::cml:atom)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                   <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">atomParity must be a direct child of atom</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:choose>
            <xsl:when test="@atomRefs4">
                <xsl:call-template name="check-all-atomRefs-values-are-different">
                    <xsl:with-param name="atomRefsX" select="@atomRefs4"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                       <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">atomParity must have an atomRefs4 attribute</xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:apply-templates mode="molecular"/>
    </xsl:template>

    <xsl:template match="cml:formula" mode="molecular">
        <xsl:choose>
            <xsl:when test="parent::cml:molecule">
                <!-- molecule can be the child of formula -->
            </xsl:when>
            <xsl:when test="parent::cml:formula">
                <xsl:if test="not(@count)">
                    <xsl:call-template name="error">
                        <xsl:with-param name="location">
                            <xsl:apply-templates select="." mode="get-full-path"/>
                        </xsl:with-param>
                        <xsl:with-param name="text">a formula that is a child of formula must have a count attribute specified</xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="error">
                    <xsl:with-param name="location">
                        <xsl:apply-templates select="." mode="get-full-path"/>
                    </xsl:with-param>
                    <xsl:with-param name="text">formula must be a child of molecule or formula</xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="not(cml:atomArray or @concise or @inline)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">formula must have at least one of: an atomArray child, a concise attribute, a inline attribute</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:apply-templates mode="molecular"/>
    </xsl:template>

    <xsl:template match="cml:property" mode="molecular">
        <xsl:if test="not(@dictRef)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                   <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">properties must have dictRef attributes</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="not(@title)">
            <xsl:call-template name="warning">
                <xsl:with-param name="location">
                   <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">properties should have a title attribute</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:apply-templates mode="molecular"/>
    </xsl:template>

    <xsl:template match="cml:scalar" mode="molecular">
        <xsl:if test="not(parent::cml:property)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                   <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">scalar must be the child of property</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="not(@units)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                   <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">scalars must have units specified</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:if test="not(@dataType)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                   <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">scalars must have dataType specified</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:apply-templates mode="molecular"/>
    </xsl:template>

    <xsl:template match="cml:label" mode="molecular">
          <xsl:apply-templates mode="molecular"/>
    </xsl:template>

    <xsl:template match="cml:name" mode="molecular">
        <xsl:if test="not(@dictRef)">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="." mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">names must have dataType specified</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        <xsl:apply-templates mode="molecular"/>
    </xsl:template>

    <xsl:template name="check-all-atomRefs-values-are-different">
        <xsl:param name="atomRefsX"/>
        <xsl:variable name="atomRefTokens" select="tokenize($atomRefsX, ' ')"/>
        <xsl:if test="not(count($atomRefTokens) = count(distinct-values($atomRefTokens)))">
            <xsl:call-template name="error">
                <xsl:with-param name="location">
                    <xsl:apply-templates select="$atomRefsX" mode="get-full-path"/>
                </xsl:with-param>
                <xsl:with-param name="text">the atoms in an atomRefs<xsl:value-of select="count($atomRefTokens)" /> must not contain duplicates</xsl:with-param>
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