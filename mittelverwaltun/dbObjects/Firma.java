package dbObjects;

import java.io.Serializable;


/**
 * 
 * @author w.flat
 * 17.10.2004
 */
public class Firma implements Serializable {
	
	/**
	 * Eindeutige Id zur Identifizierung der Firma. Wird von der Datenbank vergeben. 
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
	 * @param id = Id der Firma. 
	 * @param name = Der Firmenname. 
	 * @param strasseNr = Der Strassenname und die Strassennummer.
	 * @param plz = Die PLZ des Standorts. 
	 * @param ort = Der Standort der Firma. 
	 * @param kundenNr = Die Kundenummer, bei der Firma. 
	 * @param telNr = Die Telefonnummer der Firma. 
	 * @param faxNr = Die Faxnummer der Firma.
	 * @param eMail = Die E-Mail-Adresse der Firma. 
	 * @param www = Die Homepage der Firma. 
	 * @param ask = Flag ob die Firma für die ASK-Bestellung verwendet werden darf. 
	 * @param geloescht = Flag ob die Firma gelöscht ist oder nicht. 
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
	 * Default-Konstruktor.
	 */
	public Firma() {
	}
	
	/**
	 * Konstruktor, zum Erzeugen einer neuen Firma.
	 * @param name = Der Firmenname. 
	 * @param strasseNr = Der Strassenname und die Strassennummer.
	 * @param plz = Die PLZ des Standorts. 
	 * @param ort = Der Standort der Firma. 
	 * @param kundenNr = Die Kundenummer, bei der Firma. 
	 * @param telNr = Die Telefonnummer der Firma. 
	 * @param faxNr = Die Faxnummer der Firma.
	 * @param eMail = Die E-Mail-Adresse der Firma. 
	 * @param www = Die Homepage der Firma. 
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
	 * @return Die kopierte Firma. 
	 */
	public Object clone() {
		return new Firma( this.id, this.name, this.strasseNr, this.plz, this.ort, this.kundenNr,  
							this.telNr, this.faxNr, this.eMail, this.www, this.ask, this. geloescht );
	}
	
	/**
	 * Die Firma aktualisieren, indem man die Angaben, außer der id aus der übergebenen Firma entnimmt. 
	 * @param firma = Firma von der die Änderungen übernommen werden. 
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
		this.geloescht = firma.geloescht;
	}
	
	/**
	 * Ausgabe der Firma als String. D.h. Ausgabe vom Firmennamen, Ort.
	 * @return Firmenname, Standort. 
	 */
	public String toString() {
		return name + ", " + ort;
	}
	
	/**
	 * Ermittlung ob zwei Firmen gleich sind. Dabei ist der Name, Straße, PLZ und Ort entscheidend. 
	 * @param o = Die Zweite Firma. 
	 */
	public boolean equals( Object o ) {
		if(o != null){
			Firma firma = (Firma)o;
			if( ((name == null || firma.getName() == null) ? true : name.equals(firma.getName())) && 
					((strasseNr == null || firma.getStrasseNr() == null) ? true : strasseNr.equals(firma.getStrasseNr())) && 
					((plz == null || firma.getPlz() == null) ? true : plz.equals(firma.getPlz())) && 
					((ort == null || firma.getOrt() == null) ? true : ort.equals(firma.getOrt()))
				 )
				return true;
		}
		
		return false;
	}
	
	/**
	 * Id der Firma zurückgeben.
	 * @return Id der Firma. 
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Id der Firma aktualisieren. 
	 * @param id = Neue Id der Firma. 
	 */
	public void setId( int id ) {
		this.id = id;
	}
	
	/**
	 * Den Namen der Firma zurückgeben.
	 * @return Der Firmenname. 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Name der Firma aktualisieren. 
	 * @param name = Neuer Firmenname. 
	 */
	public void setName( String name ) {
		this.name = name;
	}
	
	/**
	 * StrasseNr der Firma zurückgeben. 
	 * @return Strasenname und die Starssennummer der Firma. 
	 */
	public String getStrasseNr() {
		return strasseNr;
	}
	
	/**
	 * StrasseNr der Firma aktualisieren. 
	 * @param strasseNr = Neuer Strassenname und die neue Strassennummer. 
	 */
	public void setStrasseNr( String strasseNr ) {
		this.strasseNr = strasseNr;
	}
	
	/**
	 * Plz der Firma zurückgeben. 
	 * @return Die PLZ des Standorts. 
	 */
	public String getPlz() {
		return plz;
	}
	
	/**
	 * Plz der Firma aktualisieren. 
	 * @param plz = Die Postleitzahl des Standorts. 
	 */
	public void setPlz( String plz ) {
		this.plz = plz;
	}
	
	/**
	 * Ort der Firma zurückgeben. 
	 * @return Der Standort der Firma. 
	 */
	public String getOrt() {
		return ort;
	}
	
	/**
	 * Ort der Firma aktualisieren.
	 * @param ort = Der Standort der Firma. 
	 */
	public void setOrt( String ort ) {
		this.ort = ort;
	}
	
	/**
	 * KundenNr bei der Firma zurückgeben.
	 * @return Die Kundennummer, die man bei der Firma hat. 
	 */
	public String getKundenNr() {
		return kundenNr;
	}
	
	/**
	 * KundenNr der Firma aktualisieren. 
	 * @param kundenNr = Neue Kundennummer bei der Firma.
	 */
	public void setKundenNr( String kundenNr ) {
		this.kundenNr = kundenNr;
	}
	
	/**
	 * TelNr der Firma zurückgeben.
	 * @return Die Telefonnummer der Firma. 
	 */
	public String getTelNr() {
		return telNr;
	}
	
	/**
	 * TelNr der Firma aktualisieren.
	 * @param telNr = Neue Telefonnummer der Firma. 
	 */
	public void setTelNr( String telNr ) {
		this.telNr = telNr;
	}
	
	/**
	 * FaxNr der Firma zurückgeben. 
	 * @return Die Faxnummer der Firma. 
	 */
	public String getFaxNr() {
		return faxNr;
	}
	
	/**
	 * FaxNr der Firma aktualisieren.
	 * @param faxNr = Neue Faxnummer der Firma. 
	 */
	public void setFaxNr( String faxNr ) {
		this.faxNr = faxNr;
	}
	
	/**
	 * EMail der Firma zurückgeben. 
	 * @return Die E-Mail-Adresse der Firma. 
	 */
	public String getEMail() {
		return eMail;
	}
	
	/**
	 * EMail der Firma aktualisieren.
	 * @param eMail = Neue E-Mail-Adresse der Firma. 
	 */
	public void setEMail( String eMail ) {
		this.eMail = eMail;
	}
	
	/**
	 * Homepage der Firma zurückgeben. 
	 * @return Die Homepage der Firma. 
	 */
	public String getWWW() {
		return www;
	}
	
	/**
	 * Homepage der Firma aktualisieren.
	 * @param www = Die Neue Homepage der Firma. 
	 */
	public void setWWW( String www ) {
		this.www = www;
	}
	
	/**
	 * Ermittlung des ASK-Flags. 
	 * @return true = Wenn das Flag gesetzt ist, sonst = flase.
	 */
	public boolean getASK() {
		return ask;
	}
	
	/**
	 * ASK-Flag der Firma aktualisieren.
	 * @param ask = Neuer Wert für das Flag ask. 
	 */
	public void setASK(boolean ask) {
		this.ask = ask;
	}
	
	/**
	 * Ermittlung ob die Firma gelöscht ist. 
	 * @return true = Wenn die Firma gelöscht ist, sonst = false. 
	 */
	public boolean getGeloescht() {
		return geloescht;
	}
	
	/**
	 * Den Neuen Wert dem Flag gelöscht zuweisen. 
	 * @param geloescht = Neuer Wert des Flags gelöscht. 
	 */
	public void setGeloescht( boolean geloescht ) {
		this.geloescht = geloescht;
	}

}
