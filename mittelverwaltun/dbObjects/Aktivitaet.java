package dbObjects;

import java.io.Serializable;

/**
 *@author robert
 * Aktivität werden für die Rollen im System verwendet. Anhand der Aktivität werden die Menüeinträge aktiviert oder deaktiviert.
 */
public class Aktivitaet implements Serializable {

	private int id;

	private String bezeichnung;

	private String bemerkung;

	/**
	 * Konstruktor
	 * @param id - Id der Aktivität
	 * @param bezeichnung
	 * @param bemerkung
	 */
	public Aktivitaet(int id, String bezeichnung, String bemerkung){
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.bemerkung = bemerkung;
	}

  /**
   * gibt die Id der Aktivität zurück
   * @return Id
   */
	public int getId() {
		return id;
	}

	/**
	 * setzt die Id der Aktivität 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * gibt die Bezeichnung der Aktivität zurück
	 * @return Bezeichnung
	 */
	public String getBezeichnung() {
		return bezeichnung;
	}

	/**
	 * setzt die Bezeichnung der Aktivität
	 * @param bezeichnung
	 */
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	/**
	 * gibt die Bemerkung der Aktivität zurück
	 * @return Bemerkung
	 */
	public String getBemerkung() {
		return bemerkung;
	}

	/**
	 * setzt die Bemerkung der Aktivität
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
	 * Erstellt eine Kopie der Aktivität
	 */
	public Object clone(){
		return new Aktivitaet(this.id, this.bemerkung, this.bezeichnung);
	}
}
