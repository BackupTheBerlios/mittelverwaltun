package dbObjects;

import java.io.Serializable;

import java.lang.String;
/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class Fachbereich extends Institut implements Serializable{
	private float profPauschale;
	private String plzOrt;

	private String strasseHausNr;

	private String fhBezeichnung;

	private String fhBeschreibung;

	private String fbBezeichnung;

	/*public Fachbereich(int id, String hochschule, float profPauschale, String bezeichnung, String kostenstelle) {
		super(id, bezeichnung, kostenstelle, false);
		this.profPauschale = profPauschale;
		this.hochschule = hochschule;
	}*/
	
	public Fachbereich(int id, String fbBezeichnung, float profPauschale, String bezeichnung, String kostenstelle,
											String strasseHausNr, String plzOrt, String fhBezeichnung, String fhBeschreibung) {
		super(id, bezeichnung, kostenstelle, false);
		this.profPauschale = profPauschale;
		this.fbBezeichnung = fbBezeichnung;
		this.plzOrt = plzOrt;
		this.strasseHausNr = strasseHausNr;
		this.fhBezeichnung = fhBezeichnung;
		this.fhBeschreibung = fhBeschreibung;
	}
	
	public Fachbereich(int id, String fbBezeichnung, float profPauschale,
											String strasseHausNr, String plzOrt,  String fhBezeichnung, String fhBeschreibung) {
		super(id, null, null, false);
		this.profPauschale = profPauschale;
		this.fbBezeichnung = fbBezeichnung;
		this.plzOrt = plzOrt;
		this.strasseHausNr = strasseHausNr;
		this.fhBezeichnung = fhBezeichnung;
		this.fhBeschreibung = fhBeschreibung;
	}
	
	public float getProfPauschale() {
		return profPauschale;
	}

	public void setProfPauschale(float profPauschale) {
		this.profPauschale = profPauschale;
	}

	public String toString(){
		return fbBezeichnung;
	}

	public boolean equals(Object fb){
		if(fb != null){
			if( this.getId() == ((Fachbereich)fb).getId() && 
					this.fbBezeichnung.equals(((Fachbereich)fb).getFbBezeichnung()) &&
					this.profPauschale == ((Fachbereich)fb).getProfPauschale() &&
					this.strasseHausNr.equals(((Fachbereich)fb).getStrasseHausNr()) &&
					this.plzOrt.equals(((Fachbereich)fb).getPlzOrt()) &&
					this.fhBezeichnung.equals(((Fachbereich)fb).getFhBezeichnung()) &&
					this.fhBeschreibung.equals(((Fachbereich)fb).getFhBeschreibung())	)
				return true;
			else
				return false;
		}else
			return false;
	}	

	public String getPlzOrt() {
		return plzOrt;
	}

	public void setPlzOrt(String plzOrt) {
		this.plzOrt = plzOrt;
	}

	public String getStrasseHausNr() {
		return strasseHausNr;
	}

	public void setStrasseHausNr(String strasseHausNr) {
		this.strasseHausNr = strasseHausNr;
	}

	public String getFhBezeichnung() {
		return fhBezeichnung;
	}

	public void setFhBezeichnung(String fhBezeichnung) {
		this.fhBezeichnung = fhBezeichnung;
	}

	public String getFhBeschreibung() {
		return fhBeschreibung;
	}

	public void setFhBeschreibung(String fhBeschreibung) {
		this.fhBeschreibung = fhBeschreibung;
	}

	public String getFbBezeichnung() {
		return fbBezeichnung;
	}

	public void setFbBezeichnung(String fbBezeichnung) {
		this.fbBezeichnung = fbBezeichnung;
	}

}
