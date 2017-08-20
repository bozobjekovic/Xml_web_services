<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:amandman="http://www.tim9.com/amandman" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	version="2.0">

	<xsl:output encoding="UTF-8" />
	
	<xsl:variable name="amandmanId" select="/*/@id" />
	<xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyzšđž'" />
	<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZŠĐŽ'" />

	<xsl:variable name="naslovAmandmana" select="/*/@naslov" />
	<xsl:variable name="pravniOsnov" select="/*/@pravniOsnov" />

	<xsl:template match="/">
		<h1 class="tim9">
			<xsl:value-of select="translate($naslovAmandmana, $smallcase, $uppercase)" />
		</h1>
		<h3 class="tim9">Pravni osnov</h3>
		<p>
			<xsl:value-of select="translate($pravniOsnov, $smallcase, $uppercase)" />
		</p>

		<!-- SADRZAJ AMANDMANA -->
		<xsl:for-each select="child::*/child::amandman:Sadrzaj">
			<h3 class="tim9">Predlog amandmana</h3>
			
			<p>
				Predlaže se
				<span style="text-transform: lowercase">
					<xsl:value-of select="amandman:Obrazlozenje" />
				</span>
				u
				<b>
					<xsl:value-of select="amandman:OdredbaAkta" />
				</b>
				zakona
				<!-- <a href="#/akti/amandman/{$idAkta}" id="linkAkta">
					<xsl:value-of select="amandman:NazivAkta" />
				</a> -->
				.
				<br />
				<br />
				Predlaže se sledeće rešenje:
				<xsl:value-of select="amandman:PredlozenoResenje" />
			</p>
			<p style="font-size:29px; text-align: center;">Obrazloženje</p>
			<!-- <p>
				Razlog podnošenja:
				<xsl:value-of select="amd:Obrazlozenje/amd:RazlogPodnosenja" />
				<br />
				Cilj podnošenja:
				<xsl:value-of select="amd:Obrazlozenje/amd:CiljPodnosenja" />
				<br />
				Objašnjenje rešenja:
				<xsl:value-of select="amd:Obrazlozenje/amd:ObjasnjenjeResenja" />
				<br />
				Procena uticaja:
				<xsl:value-of select="amd:Obrazlozenje/amd:ProcenaUticaja" />
			</p> -->
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>