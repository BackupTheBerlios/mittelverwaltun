package dbObjects;

import java.io.Serializable;

/**
 *@author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class Aktivitaet implements Serializable {
//hjsdfklhdl
	private int id;

	private String bezeichnung;

	private String bemerkung;

	public Aktivitaet(int id, String bezeichnung, String bemerkung){
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.bemerkung = bemerkung;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public String getBemerkung() {
		return bemerkung;
	}

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
}
