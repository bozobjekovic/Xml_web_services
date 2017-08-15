<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:akt="http://www.tim9.com/akt"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    version="2.0">
    
    <xsl:output encoding="UTF-8" />
    <xsl:variable name="aktId" select="/*/@id"/>
    <xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyzšđž'" />
	<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZŠĐŽ'" />
	
	<xsl:variable name="naslovAkta" select="/*/@naslov" />
	<xsl:variable name="status" select="/*/akt:Preambula/akt:Status"/>
    
    
    <!-- AKT -->
    <xsl:template match="/">
      	<h1><xsl:value-of select="translate($naslovAkta, $smallcase, $uppercase)" /></h1>
      	
	      	
		<!-- CLANOVI -->
   		<xsl:apply-templates select="child::*/child::akt:Clan"/>
    </xsl:template>
</xsl:stylesheet>