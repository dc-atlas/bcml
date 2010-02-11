<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<xsl:output method="xml" indent="yes"/>

	<xsl:template match="EntrezGeneID" />

	<xsl:template match="*[EntrezGeneID]">
		<xsl:copy>
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
			<Organism name="HS">
				<xsl:for-each select="EntrezGeneID">
					<annotation DB="EntrezGeneID">
						<xsl:attribute name="ID">
   	    					<xsl:value-of select="." />
   	    				</xsl:attribute>
					</annotation>
				</xsl:for-each>
			</Organism>
		</xsl:copy>
	</xsl:template>


	<xsl:template match="* | @*">
		<xsl:copy>
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>


</xsl:stylesheet>
