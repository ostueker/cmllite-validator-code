<xsl:stylesheet version="2.0"
                 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                 xmlns:cml="http://www.xml-cml.org/schema"
                 xmlns:report="http://www.xml-cml.org/report/">

     <!-- template 1 -->
     <xsl:template match="/">
         <report:result>
             <xsl:apply-templates/>
         </report:result>
     </xsl:template>

     <!-- template 2 -->
     <xsl:template match="*|@*|text()">
         <xsl:apply-templates/>
     </xsl:template>

     <!-- template 3 -->
     <xsl:template match="*|@*|text()" mode="simpleUnit">
         <xsl:apply-templates mode="simpleUnit"/>
     </xsl:template>

     <!-- template 4 -->
     <xsl:template
             match="cml:*[@convention and namespace-uri-for-prefix(
         substring-before(@convention, ':'),.) = 'http://www.xml-cml.org/convention/'
         and substring-after(@convention, ':') = 'simpleUnit']">
         <xsl:choose>
             <xsl:when test="self::cml:unitList">
                 <xsl:call-template name="simpleUnit-template"/>
             </xsl:when>
             <xsl:otherwise>
                 <xsl:call-template name="error">
                     <xsl:with-param name="location">
                         <xsl:apply-templates select="@convention" mode="get-full-path"/>
                     </xsl:with-param>
                     <xsl:with-param name="text">the only valid cml elements which can specify the
                     simpleUnit convention is "unitList"</xsl:with-param>
                 </xsl:call-template>
                 <xsl:apply-templates/>
             </xsl:otherwise>
         </xsl:choose>
     </xsl:template>

     <!-- template 5 -->
     <xsl:template match="cml:unitList" mode="simpleUnit"
                   name="simpleUnit-template">
         <xsl:if test="not(cml:unit)">
               <xsl:call-template name="error">
                     <xsl:with-param name="location">
                         <xsl:apply-templates select="." mode="get-full-path"/>
                     </xsl:with-param>
                     <xsl:with-param name="text"> A unit list MUST contain child cml:unit elements</xsl:with-param>
                 </xsl:call-template>
         </xsl:if>
         <xsl:apply-templates mode="simpleUnit"/>
     </xsl:template>

     <!-- template 6 -->
     <xsl:template match="cml:unit" mode="simpleUnit">
         <xsl:apply-templates mode="simpleUnit"/>
     </xsl:template>

     <!-- template 7 -->
     <xsl:template match="cml:*" mode="simpleUnit">
         <xsl:call-template name="info">
                     <xsl:with-param name="location">
                         <xsl:apply-templates select="." mode="get-full-path"/>
                     </xsl:with-param>
                     <xsl:with-param name="text">  <xsl:value-of select="local-name()"/>
             is not a part of the http://www.xml-cml.org/convention/simpleUnit
             convention and may be ignored by some processors.</xsl:with-param>
                 </xsl:call-template>

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
