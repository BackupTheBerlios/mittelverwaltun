package dbObjects;

import java.io.Serializable;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class Rolle implements Serializable {

	private int id;

	private int[] aktivitaeten;

	private String bezeichnung;
	
	private Aktivitaet[] aktivitaetenFull;

	public Rolle (int id){
		this.id = id;
		this.bezeichnung = null;
		this.aktivitaeten = null;
	}
	
	public Rolle(int id, String bezeichnung){
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.aktivitaeten = null;
	}
	
	public Rolle(int id, int[] aktivitaeten, String bezeichnung){
		this.id = id;
		this.aktivitaeten = aktivitaeten;
		this.bezeichnung = bezeichnung;
	}

//	Änderung Anfang 01.09.2004	
	public Rolle(int id, String bezeichnung, Aktivitaet[] aktivitaeten){			
		this.id = id;
		this.aktivitaetenFull = aktivitaeten;
		this.bezeichnung = bezeichnung;
		this.aktivitaeten = null;
	}
//	Änderung Ende 01.09.2004
	
	public String toString(){
		return bezeichnung;
	}

	public boolean equals(Object rolle){
		if( this.id == ((Rolle)rolle).getId() && 
			this.bezeichnung.equals(((Rolle)rolle).getBezeichnung()))
			return true;
		else
			return false;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int[] getAktivitaeten() {
		return aktivitaeten;
	}

	public void setAktivitaeten(int[] aktivitaeten) {
		this.aktivitaeten = aktivitaeten;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	
	
	public Aktivitaet[] getAktivitaetenFull() {
		return aktivitaetenFull;
	}

	public void setAktivitaetenFull(Aktivitaet[] aktivitaetenFull) {
		this.aktivitaetenFull = aktivitaetenFull;
	}

}
