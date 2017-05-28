//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.30 at 04:28:27 PM CET 
//


package tim9.xml.model;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="Deo" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Glava" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Odeljak" maxOccurs="unbounded">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="Pododeljak" maxOccurs="unbounded">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element ref="{http://parlament.gov.rs/propis}Clan"/>
 *                                               &lt;/sequence>
 *                                               &lt;attGroup ref="{http://parlament.gov.rs/propis}atributi"/>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                     &lt;attGroup ref="{http://parlament.gov.rs/propis}atributi"/>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                           &lt;attGroup ref="{http://parlament.gov.rs/propis}atributi"/>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attGroup ref="{http://parlament.gov.rs/propis}atributi"/>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://parlament.gov.rs/propis}atributi"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "deo"
})
@XmlRootElement(name = "Akt")
public class Akt {

    @XmlElement(name = "Deo", required = true)
    protected List<Akt.Deo> deo;
    @XmlAttribute(name = "id")
    @XmlSchemaType(name = "anySimpleType")
    protected String id;
    @XmlAttribute(name = "naslov")
    @XmlSchemaType(name = "anySimpleType")
    protected String naslov;

    /**
     * Gets the value of the deo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the deo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Akt.Deo }
     * 
     * 
     */
    public List<Akt.Deo> getDeo() {
        if (deo == null) {
            deo = new ArrayList<Akt.Deo>();
        }
        return this.deo;
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

    /**
     * Gets the value of the naslov property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNaslov() {
        return naslov;
    }

    /**
     * Sets the value of the naslov property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNaslov(String value) {
        this.naslov = value;
    }

    @Override
	public String toString() {
		return "Akt [deo=" + deo + ", id=" + id + ", naslov=" + naslov + "]";
	}



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
     *         &lt;element name="Glava" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Odeljak" maxOccurs="unbounded">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="Pododeljak" maxOccurs="unbounded">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element ref="{http://parlament.gov.rs/propis}Clan"/>
     *                                     &lt;/sequence>
     *                                     &lt;attGroup ref="{http://parlament.gov.rs/propis}atributi"/>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                           &lt;attGroup ref="{http://parlament.gov.rs/propis}atributi"/>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *                 &lt;attGroup ref="{http://parlament.gov.rs/propis}atributi"/>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attGroup ref="{http://parlament.gov.rs/propis}atributi"/>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "glava"
    })
    public static class Deo {

        @XmlElement(name = "Glava", required = true)
        protected List<Akt.Deo.Glava> glava;
        @XmlAttribute(name = "id")
        @XmlSchemaType(name = "anySimpleType")
        protected String id;
        @XmlAttribute(name = "naslov")
        @XmlSchemaType(name = "anySimpleType")
        protected String naslov;

        /**
         * Gets the value of the glava property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the glava property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getGlava().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Akt.Deo.Glava }
         * 
         * 
         */
        public List<Akt.Deo.Glava> getGlava() {
            if (glava == null) {
                glava = new ArrayList<Akt.Deo.Glava>();
            }
            return this.glava;
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

        /**
         * Gets the value of the naslov property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNaslov() {
            return naslov;
        }

        /**
         * Sets the value of the naslov property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNaslov(String value) {
            this.naslov = value;
        }

        

        @Override
		public String toString() {
			return "Deo [glava=" + glava + ", id=" + id + ", naslov=" + naslov + "]";
		}



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
         *         &lt;element name="Odeljak" maxOccurs="unbounded">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="Pododeljak" maxOccurs="unbounded">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element ref="{http://parlament.gov.rs/propis}Clan"/>
         *                           &lt;/sequence>
         *                           &lt;attGroup ref="{http://parlament.gov.rs/propis}atributi"/>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *                 &lt;attGroup ref="{http://parlament.gov.rs/propis}atributi"/>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *       &lt;attGroup ref="{http://parlament.gov.rs/propis}atributi"/>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "odeljak"
        })
        public static class Glava {

            @XmlElement(name = "Odeljak", required = true)
            protected List<Akt.Deo.Glava.Odeljak> odeljak;
            @XmlAttribute(name = "id")
            @XmlSchemaType(name = "anySimpleType")
            protected String id;
            @XmlAttribute(name = "naslov")
            @XmlSchemaType(name = "anySimpleType")
            protected String naslov;

            /**
             * Gets the value of the odeljak property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the odeljak property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getOdeljak().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Akt.Deo.Glava.Odeljak }
             * 
             * 
             */
            public List<Akt.Deo.Glava.Odeljak> getOdeljak() {
                if (odeljak == null) {
                    odeljak = new ArrayList<Akt.Deo.Glava.Odeljak>();
                }
                return this.odeljak;
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

            /**
             * Gets the value of the naslov property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getNaslov() {
                return naslov;
            }

            /**
             * Sets the value of the naslov property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setNaslov(String value) {
                this.naslov = value;
            }


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
             *         &lt;element name="Pododeljak" maxOccurs="unbounded">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element ref="{http://parlament.gov.rs/propis}Clan"/>
             *                 &lt;/sequence>
             *                 &lt;attGroup ref="{http://parlament.gov.rs/propis}atributi"/>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *       &lt;attGroup ref="{http://parlament.gov.rs/propis}atributi"/>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "pododeljak"
            })
            public static class Odeljak {

                @XmlElement(name = "Pododeljak", required = true)
                protected List<Akt.Deo.Glava.Odeljak.Pododeljak> pododeljak;
                @XmlAttribute(name = "id")
                @XmlSchemaType(name = "anySimpleType")
                protected String id;
                @XmlAttribute(name = "naslov")
                @XmlSchemaType(name = "anySimpleType")
                protected String naslov;

                /**
                 * Gets the value of the pododeljak property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the pododeljak property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getPododeljak().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Akt.Deo.Glava.Odeljak.Pododeljak }
                 * 
                 * 
                 */
                public List<Akt.Deo.Glava.Odeljak.Pododeljak> getPododeljak() {
                    if (pododeljak == null) {
                        pododeljak = new ArrayList<Akt.Deo.Glava.Odeljak.Pododeljak>();
                    }
                    return this.pododeljak;
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

                /**
                 * Gets the value of the naslov property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getNaslov() {
                    return naslov;
                }

                /**
                 * Sets the value of the naslov property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setNaslov(String value) {
                    this.naslov = value;
                }


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
                 *         &lt;element ref="{http://parlament.gov.rs/propis}Clan"/>
                 *       &lt;/sequence>
                 *       &lt;attGroup ref="{http://parlament.gov.rs/propis}atributi"/>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "clan"
                })
                public static class Pododeljak {

                    @XmlElement(name = "Clan", required = true)
                    protected Clan clan;
                    @XmlAttribute(name = "id")
                    @XmlSchemaType(name = "anySimpleType")
                    protected String id;
                    @XmlAttribute(name = "naslov")
                    @XmlSchemaType(name = "anySimpleType")
                    protected String naslov;

                    /**
                     * Gets the value of the clan property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link Clan }
                     *     
                     */
                    public Clan getClan() {
                        return clan;
                    }

                    /**
                     * Sets the value of the clan property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link Clan }
                     *     
                     */
                    public void setClan(Clan value) {
                        this.clan = value;
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

                    /**
                     * Gets the value of the naslov property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getNaslov() {
                        return naslov;
                    }

                    /**
                     * Sets the value of the naslov property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setNaslov(String value) {
                        this.naslov = value;
                    }

                }

            }

        }

    }

}
