//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.09.06 at 10:30:36 PM CEST 
//


package tim9.xml.model.akt;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tim9.akt package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Sadrzaj_QNAME = new QName("http://www.tim9.com/akt", "Sadrzaj");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tim9.akt
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Preambula }
     * 
     */
    public Preambula createPreambula() {
        return new Preambula();
    }

    /**
     * Create an instance of {@link Akt }
     * 
     */
    public Akt createAkt() {
        return new Akt();
    }

    /**
     * Create an instance of {@link Preambula.Oblast }
     * 
     */
    public Preambula.Oblast createPreambulaOblast() {
        return new Preambula.Oblast();
    }

    /**
     * Create an instance of {@link Preambula.Status }
     * 
     */
    public Preambula.Status createPreambulaStatus() {
        return new Preambula.Status();
    }

    /**
     * Create an instance of {@link Preambula.DatumPredaje }
     * 
     */
    public Preambula.DatumPredaje createPreambulaDatumPredaje() {
        return new Preambula.DatumPredaje();
    }

    /**
     * Create an instance of {@link Preambula.DatumObjave }
     * 
     */
    public Preambula.DatumObjave createPreambulaDatumObjave() {
        return new Preambula.DatumObjave();
    }

    /**
     * Create an instance of {@link Preambula.BrojGlasovaZa }
     * 
     */
    public Preambula.BrojGlasovaZa createPreambulaBrojGlasovaZa() {
        return new Preambula.BrojGlasovaZa();
    }

    /**
     * Create an instance of {@link Preambula.BrojGlasovaProtiv }
     * 
     */
    public Preambula.BrojGlasovaProtiv createPreambulaBrojGlasovaProtiv() {
        return new Preambula.BrojGlasovaProtiv();
    }

    /**
     * Create an instance of {@link Preambula.BrojGlasovaUzdrzano }
     * 
     */
    public Preambula.BrojGlasovaUzdrzano createPreambulaBrojGlasovaUzdrzano() {
        return new Preambula.BrojGlasovaUzdrzano();
    }

    /**
     * Create an instance of {@link Deo }
     * 
     */
    public Deo createDeo() {
        return new Deo();
    }

    /**
     * Create an instance of {@link Glava }
     * 
     */
    public Glava createGlava() {
        return new Glava();
    }

    /**
     * Create an instance of {@link Odeljak }
     * 
     */
    public Odeljak createOdeljak() {
        return new Odeljak();
    }

    /**
     * Create an instance of {@link Pododeljak }
     * 
     */
    public Pododeljak createPododeljak() {
        return new Pododeljak();
    }

    /**
     * Create an instance of {@link Clan }
     * 
     */
    public Clan createClan() {
        return new Clan();
    }

    /**
     * Create an instance of {@link Stav }
     * 
     */
    public Stav createStav() {
        return new Stav();
    }

    /**
     * Create an instance of {@link Tacka }
     * 
     */
    public Tacka createTacka() {
        return new Tacka();
    }

    /**
     * Create an instance of {@link Podtacka }
     * 
     */
    public Podtacka createPodtacka() {
        return new Podtacka();
    }

    /**
     * Create an instance of {@link Alineja }
     * 
     */
    public Alineja createAlineja() {
        return new Alineja();
    }

    /**
     * Create an instance of {@link SadrzajTip }
     * 
     */
    public SadrzajTip createSadrzajTip() {
        return new SadrzajTip();
    }

    /**
     * Create an instance of {@link Referenca }
     * 
     */
    public Referenca createReferenca() {
        return new Referenca();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SadrzajTip }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.tim9.com/akt", name = "Sadrzaj")
    public JAXBElement<SadrzajTip> createSadrzaj(SadrzajTip value) {
        return new JAXBElement<SadrzajTip>(_Sadrzaj_QNAME, SadrzajTip.class, null, value);
    }

}
