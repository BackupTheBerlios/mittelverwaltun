package dbObjects;

import java.util.ArrayList;
/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */


public class Angebot {

	private String datum;

	private String firma;

	private String strasse;

	private String plz;

	private String ort;

	private ArrayList positionen;
	
	private float summe;

	public Angebot(String datum, String firma, String strasse, String plz, String ort, ArrayList positionen, float summe){
		this.datum = datum;
		this.firma = firma;
		this.strasse = strasse;
		this.plz = plz;
		this.ort = ort;
		this.positionen = positionen;
		this.summe = summe;
	}
	
	

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public ArrayList getPositionen() {
		return positionen;
	}

	public void setPositionen(ArrayList positionen) {
		this.positionen = positionen;
	}

	public String toString(){
		return firma + ", " + ort;
	}
	public float getSumme() {
		return summe;
	}

	public void setSumme(float summe) {
		this.summe = summe;
	}

}
