//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.09.05 at 09:10:57 PM CEST 
//


package tim9.xml.model.amandman;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NazivAkta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.tim9.com/amandman}OdredbaAkta"/>
 *         &lt;element name="PredlozenoResenje" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://www.tim9.com/amandman}Obrazlozenje"/>
 *         &lt;element ref="{http://www.tim9.com/amandman}Predlog"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "nazivAkta",
    "odredbaAkta",
    "predlozenoResenje",
    "obrazlozenje",
    "predlog"
})
@XmlRootElement(name = "Sadrzaj")
public class Sadrzaj {

    @XmlElement(name = "NazivAkta")
    protected String nazivAkta;
    @XmlElement(name = "OdredbaAkta", required = true)
    protected String odredbaAkta;
    @XmlElement(name = "PredlozenoResenje", required = true)
    protected String predlozenoResenje;
    @XmlElement(name = "Obrazlozenje", required = true)
    protected Obrazlozenje obrazlozenje;
    @XmlElement(name = "Predlog", required = true)
    protected Predlog predlog;
    @XmlAttribute(name = "id")
    @XmlSchemaType(name = "anySimpleType")
    protected String id;

    /**
     * Gets the value of the nazivAkta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNazivAkta() {
        return nazivAkta;
    }

    /**
     * Sets the value of the nazivAkta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNazivAkta(String value) {
        this.nazivAkta = value;
    }

    /**
     * Gets the value of the odredbaAkta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOdredbaAkta() {
        return odredbaAkta;
    }

    /**
     * Sets the value of the odredbaAkta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOdredbaAkta(String value) {
        this.odredbaAkta = value;
    }

    /**
     * Gets the value of the predlozenoResenje property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPredlozenoResenje() {
        return predlozenoResenje;
    }

    /**
     * Sets the value of the predlozenoResenje property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPredlozenoResenje(String value) {
        this.predlozenoResenje = value;
    }

    /**
     * Gets the value of the obrazlozenje property.
     * 
     * @return
     *     possible object is
     *     {@link Obrazlozenje }
     *     
     */
    public Obrazlozenje getObrazlozenje() {
        return obrazlozenje;
    }

    /**
     * Sets the value of the obrazlozenje property.
     * 
     * @param value
     *     allowed object is
     *     {@link Obrazlozenje }
     *     
     */
    public void setObrazlozenje(Obrazlozenje value) {
        this.obrazlozenje = value;
    }

    /**
     * Gets the value of the predlog property.
     * 
     * @return
     *     possible object is
     *     {@link Predlog }
     *     
     */
    public Predlog getPredlog() {
        return predlog;
    }

    /**
     * Sets the value of the predlog property.
     * 
     * @param value
     *     allowed object is
     *     {@link Predlog }
     *     
     */
    public void setPredlog(Predlog value) {
        this.predlog = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
