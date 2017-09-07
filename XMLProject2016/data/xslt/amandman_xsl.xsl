<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:amandman="http://www.tim9.com/amandman" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	version="2.0">

	<xsl:output encoding="UTF-8" />
	
	<xsl:template match="/">
		<xsl:variable name="urlAkta" select="amandman:Amandman/@aktURL" />
		<xsl:variable name="idAkta" select="tokenize($urlAkta,'/')[last()]"/>
		<h1 class="tim9">
			<xsl:value-of select="amandman:Amandman/amandman:Naslov" />
		</h1>
		<h3 class="tim9">Pravni osnov</h3>
		<p>
			<xsl:value-of select="amandman:Amandman/amandman:PravniOsnov" />
		</p>

		<!-- SADRZAJ AMANDMANA -->
		<xsl:for-each select="amandman:Amandman/amandman:Sadrzaj">
			<h3 class="tim9">Predlog amandmana</h3>
			
			<p>
				Predlaže se
				<span style="text-transform: lowercase">
					<xsl:value-of select="amandman:PredlozenoResenje" />
				</span>
				u
				<b>
					<xsl:value-of select="amandman:OdredbaAkta" />
				</b>
				zakona
				<a href="#/preview/{$idAkta}" id="linkAkta">
					<xsl:value-of select="amandman:NazivAkta" />
				</a>
				.
				<br />
				<br />
				Predlaže se sledeće rešenje:
				<xsl:value-of select="amandman:Predlog" />
			</p>
			<p style="font-size:29px; text-align: center;">Obrazloženje</p>
			<p>
				Razlog podnošenja:
				<xsl:value-of select="amandman:Obrazlozenje/amandman:RazlogPodnosenja" />
				<br />
				Cilj podnošenja:
				<xsl:value-of select="amandman:Obrazlozenje/amandman:CiljPodnosenja" />
				<br />
				Objašnjenje rešenja:
				<xsl:value-of select="amandman:Obrazlozenje/amandman:Objasnjenje" />
				<br />
				Procena uticaja:
				<xsl:value-of select="amandman:Obrazlozenje/amandman:ProcenaUticaja" />
			</p>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>