<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:akt="http://www.tim9.com/akt" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	version="2.0">

	<xsl:output encoding="UTF-8" />
	<xsl:variable name="aktId" select="/*/@id" />
	<xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyzšđž'" />
	<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZŠĐŽ'" />

	<xsl:variable name="naslovAkta" select="/*/@naslov" />
	<xsl:variable name="status" select="/*/akt:Preambula/akt:Status" />


	<!-- AKT -->
	<xsl:template match="/">
		<h1 class="tim9">
			<xsl:value-of select="translate($naslovAkta, $smallcase, $uppercase)" />
		</h1>

		<!-- CLANOVI -->
		<xsl:apply-templates select="child::*/child::akt:Clan" />

		<!-- DELOVI -->
		<xsl:for-each select="child::*/child::akt:Deo">
			<xsl:variable name="naslovDela" select="@naslov" />
			<div class="sakriveno" id="{@id}"></div>
			<h2 class="tim9 naslovDeo">
				<xsl:value-of select="@redniBroj" />
			</h2>
			<br></br>
			<h3 class="tim9">
				<xsl:value-of select="translate($naslovDela, $smallcase, $uppercase)" />
			</h3>

			<xsl:apply-templates select="akt:Clan" />

			<!-- GLAVE -->
			<xsl:for-each select="akt:Glava">
				<xsl:variable name="naslovGlave" select="@naslov" />
				<div class="sakriveno" id="{@id}"></div>
				<h3 class="tim9 naslovGlava">
					<xsl:value-of select="@redniBroj" />
					<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;]]></xsl:text>
					<xsl:value-of select="translate($naslovGlave, $smallcase, $uppercase)" />
				</h3>

				<xsl:apply-templates select="akt:Clan" />

				<!-- ODELJCI -->
				<div class="sakriveno" id="{@id}"></div>
				<xsl:for-each select="akt:Odeljak">
					<h4 class="tim9 naslovOdeljak">
						<xsl:value-of select="@redniBroj" />
						<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;]]></xsl:text>
						<xsl:value-of select="@naslov" />
					</h4>

					<xsl:apply-templates select="akt:Clan" />

					<!-- PODODELJCI -->
					<div class="sakriveno" id="{@id}"></div>
					<xsl:for-each select="akt:Pododeljak">
						<h5 class="tim9 naslovPododeljak">
							<xsl:value-of select="@redniBroj" />
							<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;]]></xsl:text>
							<xsl:value-of select="@naslov" />
						</h5>
						<xsl:apply-templates select="akt:Clan" />
					</xsl:for-each>

				</xsl:for-each>

			</xsl:for-each>

		</xsl:for-each>

	</xsl:template>
	
	<xsl:template match="akt:Clan">
    	<div class="sakriveno" id="{@id}"></div>
		<h6 class="tim9 naslovClan"><xsl:value-of select="@naslov"/></h6>
		<h6 class="tim9"><xsl:value-of select="@redniBroj"/></h6>
		<xsl:for-each select="akt:Stav">
			<div class="sakriveno" id="{@id}"></div>
			<xsl:apply-templates select="akt:Sadrzaj"/>
			<xsl:apply-templates select="akt:Tacka"/>
		</xsl:for-each>
    </xsl:template>
	
	<xsl:template match="akt:Sadrzaj">
        <p class="tim9 uvucen" > 
            <xsl:apply-templates />
        </p>
    </xsl:template>
    
    <xsl:template match="akt:Tacka">
        <p class="tim9 uvucen tacka">  
        	<div class="sakriveno" id="{@id}"></div>
        	<xsl:value-of select="@redniBroj"/>
        	<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;]]></xsl:text>
        	<xsl:value-of select="akt:Sadrzaj"/>
        	<xsl:apply-templates select="akt:Podtacka"/>
        </p>
    </xsl:template>
    
    <xsl:template match="akt:Podtacka">
        <p class="tim9 uvucen podtacka">  
        	<div class="sakriveno" id="{@id}"></div>
        	<xsl:value-of select="@redniBroj"/>
        	<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;]]></xsl:text>
        	<xsl:value-of select="akt:Sadrzaj"/>
        	<xsl:apply-templates select="akt:Alineja"/>
        </p>
    </xsl:template>
    
    <xsl:template match="akt:Alineja">
        <p class="tim9 uvucen alineja">  
        	<div class="sakriveno" id="{@id}"></div>
        	<xsl:value-of select="akt:Sadrzaj"/>
        </p>
    </xsl:template>
    
    <xsl:template match="akt:Sadrzaj//akt:Referenca">
    	<xsl:variable name="id_url" select="substring-after(@URL, 'akt/')"/>
	    <xsl:choose>
	    	<xsl:when test="substring(@URL, 1, 1) != '#'">
				<a  class="referenca" title="prikaži" href="#/preview/{$id_url}" target="_blank">
	            	<xsl:apply-templates />
	        	</a>
	        </xsl:when>
			<!--  kada referenca pocinje sa #, referencira se unutar dokumenta -->
	        <xsl:otherwise>
	        	<xsl:variable name="withoutHash" select="substring-after(@URL, '#')"/>
	        	<a  class="referenca" title="prikaži" href="#/preview/{$aktId}skrol{$withoutHash}" target="_self">
	            	<xsl:apply-templates />
	        	</a>
	        </xsl:otherwise>
		</xsl:choose>
    </xsl:template>
    
</xsl:stylesheet>