<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:akt="http://www.tim9.com/akt" xmlns:amandman="http://www.tim9.com/amandman"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" version="2.0">

	<xsl:template match="/">
		<xsl:variable name="urlAkta" select="amandman:Amandman/@aktURL" />
		<xsl:variable name="idAkta" select="tokenize($urlAkta,'/')[last()]" />
		<fo:root>
			<fo:layout-master-set>
				<fo:simple-page-master master-name="amandman-stranica">
					<fo:region-body margin="0.75in" />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="amandman-stranica">
				<fo:flow flow-name="xsl-region-body">
					<fo:block font-family="Arial" text-align="center"
						font-size="22px" font-weight="bold" margin="10px">
						<xsl:value-of select="amandman:Amandman/amandman:Naslov" />
					</fo:block>
					<fo:block font-family="Arial" text-align="center"
						font-size="20px" font-weight="bold" margin="10px">
						<xsl:text>Pravni osnov</xsl:text>
					</fo:block>
					<fo:block text-indent="10px" text-align="justify">
						<xsl:value-of select="amandman:Amandman/amandman:PravniOsnov" />
					</fo:block>

					<xsl:for-each select="amandman:Amandman/amandman:Sadrzaj">
						<fo:block font-family="Arial" text-align="center"
							font-size="20px" font-weight="bold" margin="10px">
							<xsl:text>Predlog amandmana</xsl:text>
						</fo:block>
						<fo:block font-family="Arial" font-size="12" text-align="justify"
							margin="10px">
							<xsl:text>Predlaže se</xsl:text>
							<xsl:text> </xsl:text>
							<xsl:value-of select="lower-case(amandman:PredlozenoResenje)" />
							<xsl:text> </xsl:text>
							<xsl:text>u</xsl:text>
							<xsl:text> </xsl:text>
							<xsl:value-of select="amandman:OdredbaAkta" />
							<xsl:text> </xsl:text>
							<xsl:text>zakona</xsl:text>
							<xsl:text> </xsl:text>
							<fo:basic-link color="#618587" show-destination="new"
								external-destination="url(http://localhost:8080/#/preview/{$idAkta})">
								<xsl:value-of select="amandman:NazivAkta" />
							</fo:basic-link>
							<xsl:text>.</xsl:text>
							<fo:block>
								<xsl:text> </xsl:text>
							</fo:block>
							<xsl:text>Predlaže se sledeće rešenje:</xsl:text>
							<xsl:value-of select="amandman:Predlog" />
						</fo:block>
						<fo:block font-family="Arial" text-align="center"
							font-size="20px" margin="10px">Obrazloženje</fo:block>
						<fo:block font-family="Arial" font-size="12" text-align="justify"
							margin="10px">
							<xsl:text>Razlog podnošenja:</xsl:text>
							<xsl:value-of select="amandman:Obrazlozenje/amandman:RazlogPodnosenja" />
							<fo:block></fo:block>
							<xsl:text>Cilj podnošenja:</xsl:text>
							<xsl:value-of select="amandman:Obrazlozenje/amandman:CiljPodnosenja" />
							<fo:block></fo:block>
							<xsl:text>Objašnjenje rešenja:</xsl:text>
							<xsl:value-of select="amandman:Obrazlozenje/amandman:Objasnjenje" />
							<fo:block></fo:block>
							<xsl:text>Procena uticaja:</xsl:text>
							<xsl:value-of select="amandman:Obrazlozenje/amandman:ProcenaUticaja" />
						</fo:block>
					</xsl:for-each>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>