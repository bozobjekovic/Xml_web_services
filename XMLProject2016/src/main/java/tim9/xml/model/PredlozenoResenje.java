//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.30 at 05:12:13 PM CET 
//


package tim9.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
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
 *       &lt;choice>
 *         &lt;element name="brisanje" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="izmena" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="dodavanje" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "brisanje",
    "izmena",
    "dodavanje"
})
@XmlRootElement(name = "predlozenoResenje")
public class PredlozenoResenje {

    protected Object brisanje;
    protected Object izmena;
    protected Object dodavanje;

    /**
     * Gets the value of the brisanje property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getBrisanje() {
        return brisanje;
    }

    /**
     * Sets the value of the brisanje property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setBrisanje(Object value) {
        this.brisanje = value;
    }

    /**
     * Gets the value of the izmena property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getIzmena() {
        return izmena;
    }

    /**
     * Sets the value of the izmena property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setIzmena(Object value) {
        this.izmena = value;
    }

    /**
     * Gets the value of the dodavanje property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getDodavanje() {
        return dodavanje;
    }

    /**
     * Sets the value of the dodavanje property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setDodavanje(Object value) {
        this.dodavanje = value;
    }

}
