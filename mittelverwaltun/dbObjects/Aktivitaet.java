package dbObjects;

import java.io.Serializable;

/**
 *@author robert
 * Aktivit�t werden f�r die Rollen im System verwendet. Anhand der Aktivit�t werden die Men�eintr�ge aktiviert oder deaktiviert.
 */
public class Aktivitaet implements Serializable {

	private int id;

	private String bezeichnung;

	private String bemerkung;

	/**
	 * Konstruktor
	 * @param id - Id der Aktivit�t
	 * @param bezeichnung
	 * @param bemerkung
	 */
	public Aktivitaet(int id, String bezeichnung, String bemerkung){
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.bemerkung = bemerkung;
	}

  /**
   * gibt die Id der Aktivit�t zur�ck
   * @return Id
   */
	public int getId() {
		return id;
	}

	/**
	 * setzt die Id der Aktivit�t 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * gibt die Bezeichnung der Aktivit�t zur�ck
	 * @return Bezeichnung
	 */
	public String getBezeichnung() {
		return bezeichnung;
	}

	/**
	 * setzt die Bezeichnung der Aktivit�t
	 * @param bezeichnung
	 */
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	/**
	 * gibt die Bemerkung der Aktivit�t zur�ck
	 * @return Bemerkung
	 */
	public String getBemerkung() {
		return bemerkung;
	}

	/**
	 * setzt die Bemerkung der Aktivit�t
	 * @param Bemerkung
	 */
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	public String toString(){
		return bezeichnung; 
	}
	
	public boolean equals(Object aktivitaet){
		if( this.id == ((Aktivitaet)aktivitaet).getId())
			return true;
		else
			return false;
	}
	
	/**
	 * Erstellt eine Kopie der Aktivit�t
	 */
	public Object clone(){
		return new Aktivitaet(this.id, this.bemerkung, this.bezeichnung);
	}
}
