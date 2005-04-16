package dbObjects;

import java.io.Serializable;

import java.lang.String;
/**
 * @author robert
 *
 * Klasse für den Fachbereich
 */
public class Fachbereich extends Institut implements Serializable {
	
	/**
	 * Pauschale für einen Professor
	 */
	private float profPauschale;
	
	/**
	 * String für PLZ mit Ort
	 */
	private String plzOrt;

	/**
	 * String für Strasse und Haus-Nummer
	 */
	private String strasseHausNr;

	/**
	 * Bezeichnung der Fachhochschule
	 */
	private String fhBezeichnung;

	/**
	 * Beschreibung der Fachhochschule
	 */
	private String fhBeschreibung;

	/**
	 * Bezeichnung der Fachbereichs
	 */
	private String fbBezeichnung;
	
	/**
	 * Konstruktor
	 * @param id - Id des Fachbereichs (Institu)
	 * @param fbBezeichnung - Bezeichnung der Fachbereichs
	 * @param profPauschale - Pauschale für einen Professor
	 * @param bezeichnung - Bezeichnung des Instuts
	 * @param kostenstelle - Kostenstelle
	 * @param strasseHausNr
	 * @param plzOrt
	 * @param fhBezeichnung - Bezeichnung der Fachhochschule
	 * @param fhBeschreibung - Beschreibung der Fachhochschule
	 */
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
	
	/**
	 * Konstruktor
	 * @param id - Id des Fachbereichs (Institu)
	 * @param fbBezeichnung - Bezeichnung der Fachbereichs
	 * @param profPauschale - Pauschale für einen Professor
	 * @param strasseHausNr
	 * @param plzOrt
	 * @param fhBezeichnung - Bezeichnung der Fachhochschule
	 * @param fhBeschreibung - Beschreibung der Fachhochschule
	 */
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
	
	/**
	 * gibt die ProfessorPauschale zurück
	 * @return ProfessorPauschale
	 */
	public float getProfPauschale() {
		return profPauschale;
	}

	/**
	 * setzt die ProfessorPauschale
	 * @param profPauschale
	 */
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

	/**
	 * gibt die Bezeichnung der Fachhoschule zurück
	 * @return fhBezeichnung
	 */
	public String getFhBezeichnung() {
		return fhBezeichnung;
	}

	/**
	 * setzt die Bezeichnung der Fachhoschule
	 * @param fhBezeichnung
	 */
	public void setFhBezeichnung(String fhBezeichnung) {
		this.fhBezeichnung = fhBezeichnung;
	}

	/**
	 * gibt die Beschreibung der Fachhoschule zurück
	 * @return fhBezeichnung
	 */
	public String getFhBeschreibung() {
		return fhBeschreibung;
	}

	/**
	 * setzt die Beschreibung der Fachhoschule
	 * @param fhBeschreibung
	 */
	public void setFhBeschreibung(String fhBeschreibung) {
		this.fhBeschreibung = fhBeschreibung;
	}

	/**
	 * gibt die Bezeichnung des Fachbereichs zurück
	 * @return fbBezeichnung
	 */
	public String getFbBezeichnung() {
		return fbBezeichnung;
	}

	/**
	 * setzt die Bezeichnung des Fachbereichs
	 * @param fbBezeichnung
	 */
	public void setFbBezeichnung(String fbBezeichnung) {
		this.fbBezeichnung = fbBezeichnung;
	}

}
