package dbObjects;

import java.io.Serializable;


/**
 * @author w.flat
 * 17.10.2004
 */
public class Firma implements Serializable {
	
	/**
	 * Eindeutige Id zur Identifizierung der Firma.
	 */
	int id;
	
	/**
	 * Der Firmen-Name.
	 */
	String name;
	
	/**
	 * Der Strassen-Name und die Strassen-Nummer. 
	 */
	String strasseNr;
	
	/**
	 * Die Postleitzahl.
	 */
	String plz;
	
	/**
	 * Der Standort der Firma.
	 */
	String ort;
	
	/**
	 * Die Kundennummer, die man bei der Firma hat.
	 */
	String kundenNr;
	
	/**
	 * Die Telefonnummer der Firma.
	 */
	String telNr;
	
	/**
	 * Die Fax-Nummer der Firma.
	 */
	String faxNr;
	
	/**
	 * Die E-Mail-Adresse der Firma.
	 */
	String eMail;
	
	/**
	 * Die Homepage der Firma.
	 */
	String www;
	
	/**
	 * Flag ob es sich bei der Firma um die ASK.
	 */
	boolean ask;
	
	/**
	 * Flag ob die Firma in der Datenbank gelöscht ist.
	 */
	boolean geloescht;
	
	/**
	 * Konstruktor, der alle Attribute enthält.
	 */
	public Firma( int id, String name, String strasseNr, String plz, String ort, String kundenNr,  
						String telNr, String faxNr, String eMail, String www, boolean ask, boolean geloescht ) {
		this.id = id;
		this.name = name;
		this.strasseNr = strasseNr;
		this.plz = plz;
		this.ort = ort;
		this.kundenNr = kundenNr;
		this.telNr = telNr;
		this.faxNr = faxNr;
		this.eMail = eMail;
		this.www = www;
		this.ask = ask;
		this.geloescht = geloescht;
	}
	
	/**
	 * Konstruktor, der alle Attribute enthält außer der Id und dem Flag gelöscht.
	 */
	public Firma( String name, String strasseNr, String plz, String ort, String kundenNr,  
							String telNr, String faxNr, String eMail, String www ) {
		this.id = 0;
		this.name = name;
		this.strasseNr = strasseNr;
		this.plz = plz;
		this.ort = ort;
		this.kundenNr = kundenNr;
		this.telNr = telNr;
		this.faxNr = faxNr;
		this.eMail = eMail;
		this.www = www;
		this.ask = false;
		this.geloescht = false;
	}
	
	/**
	 * Eine Kopie einer Firma erstellen.
	 */
	public Object clone() {
		return new Firma( this.id, this.name, this.strasseNr, this.plz, this.ort, this.kundenNr,  
							this.telNr, this.faxNr, this.eMail, this.www, this.ask, this. geloescht );
	}
	
	/**
	 * Die Firma aktualisieren, indem man die Angaben, außer der id aus der übergebenen Firma entnimmt. 
	 */
	public void setFirma( Firma firma ) {
		this.name = firma.name;
		this.strasseNr = firma.strasseNr;
		this.plz = firma.plz;
		this.ort = firma.ort;
		this.kundenNr = firma.kundenNr;
		this.telNr = firma.telNr;
		this.faxNr = firma.faxNr;
		this.eMail = firma.eMail;
		this.www = firma.www;
		this.ask = firma.ask;
		this.geloescht = firma.geloescht;
	}
	
	/**
	 * Ausgabe der Firma als String. D.h. Ausgabe vom Firmennamen, Ort.
	 */
	public String toString() {
		return name + ", " + ort;
	}
	
	/**
	 * Ermittlung ob zwei Firmen gleich sind. Dabei ist der Name, Straße, PLZ und Ort entscheidend.
	 */
	public boolean equals( Firma firma ) {
		if( this.name == firma.name && this.strasseNr == firma.strasseNr && this.plz == firma.plz && this.ort == firma.ort )
			return true;
		else
			return false;
	}
	
	/**
	 * Id der Firma zurückgeben.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Id der Firma aktualisieren.
	 */
	public void setId( int id ) {
		this.id = id;
	}
	
	/**
	 * Namen der Firma zurückgeben.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Name der Firma aktualisieren.
	 */
	public void setName( String name ) {
		this.name = name;
	}
	
	/**
	 * StrasseNr der Firma zurückgeben.
	 */
	public String getStrasseNr() {
		return strasseNr;
	}
	
	/**
	 * StrasseNr der Firma aktualisieren.
	 */
	public void setStrasseNr( String strasseNr ) {
		this.strasseNr = strasseNr;
	}
	
	/**
	 * Plz der Firma zurückgeben.
	 */
	public String getPlz() {
		return plz;
	}
	
	/**
	 * Plz der Firma aktualisieren.
	 */
	public void setPlz( String plz ) {
		this.plz = plz;
	}
	
	/**
	 * Ort der Firma zurückgeben.
	 */
	public String getOrt() {
		return ort;
	}
	
	/**
	 * Ort der Firma aktualisieren.
	 */
	public void setOrt( String ort ) {
		this.ort = ort;
	}
	
	/**
	 * KundenNr der Firma zurückgeben.
	 */
	public String getKundenNr() {
		return kundenNr;
	}
	
	/**
	 * KundenNr der Firma aktualisieren.
	 */
	public void setKundenNr( String kundenNr ) {
		this.kundenNr = kundenNr;
	}
	
	/**
	 * TelNr der Firma zurückgeben.
	 */
	public String getTelNr() {
		return telNr;
	}
	
	/**
	 * TelNr der Firma aktualisieren.
	 */
	public void setTelNr( String telNr ) {
		this.telNr = telNr;
	}
	
	/**
	 * FaxNr der Firma zurückgeben.
	 */
	public String getFaxNr() {
		return faxNr;
	}
	
	/**
	 * FaxNr der Firma aktualisieren.
	 */
	public void setFaxNr( String faxNr ) {
		this.faxNr = faxNr;
	}
	
	/**
	 * EMail der Firma zurückgeben.
	 */
	public String getEMail() {
		return eMail;
	}
	
	/**
	 * EMail der Firma aktualisieren.
	 */
	public void setEMail( String eMail ) {
		this.eMail = eMail;
	}
	
	/**
	 * Homepage der Firma zurückgeben.
	 */
	public String getWWW() {
		return www;
	}
	
	/**
	 * Homepage der Firma aktualisieren.
	 */
	public void setWWW( String www ) {
		this.www = www;
	}
	
	/**
	 * Ermittlung des ASK-Flags.
	 */
	public boolean getASK() {
		return ask;
	}
	
	/**
	 * ASK-Flag der Firma aktualisieren.
	 */
	public void setASK(boolean ask) {
		this.ask = ask;
	}
	
	/**
	 * Ermittlung ob die Firma gelöscht ist.
	 */
	public boolean getGeloescht() {
		return geloescht;
	}
	
	/**
	 * Die Firma als gelöscht markieren.
	 */
	public void setGeloescht( boolean geloescht ) {
		this.geloescht = geloescht;
	}

}
