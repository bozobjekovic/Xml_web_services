//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.08.14 at 05:56:57 PM CEST 
//


package com.tim9.amandman;

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
 *         &lt;element name="RazlogPodnosenja" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Objasnjenje" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CiljPodnosenja" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ProcenaUticaja" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "razlogPodnosenja",
    "objasnjenje",
    "ciljPodnosenja",
    "procenaUticaja"
})
@XmlRootElement(name = "Obrazlozenje")
public class Obrazlozenje {

    @XmlElement(name = "RazlogPodnosenja", required = true)
    protected String razlogPodnosenja;
    @XmlElement(name = "Objasnjenje", required = true)
    protected String objasnjenje;
    @XmlElement(name = "CiljPodnosenja", required = true)
    protected String ciljPodnosenja;
    @XmlElement(name = "ProcenaUticaja", required = true)
    protected String procenaUticaja;
    @XmlAttribute(name = "id")
    @XmlSchemaType(name = "anySimpleType")
    protected String id;

    /**
     * Gets the value of the razlogPodnosenja property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazlogPodnosenja() {
        return razlogPodnosenja;
    }

    /**
     * Sets the value of the razlogPodnosenja property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazlogPodnosenja(String value) {
        this.razlogPodnosenja = value;
    }

    /**
     * Gets the value of the objasnjenje property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjasnjenje() {
        return objasnjenje;
    }

    /**
     * Sets the value of the objasnjenje property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjasnjenje(String value) {
        this.objasnjenje = value;
    }

    /**
     * Gets the value of the ciljPodnosenja property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCiljPodnosenja() {
        return ciljPodnosenja;
    }

    /**
     * Sets the value of the ciljPodnosenja property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCiljPodnosenja(String value) {
        this.ciljPodnosenja = value;
    }

    /**
     * Gets the value of the procenaUticaja property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcenaUticaja() {
        return procenaUticaja;
    }

    /**
     * Sets the value of the procenaUticaja property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcenaUticaja(String value) {
        this.procenaUticaja = value;
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
