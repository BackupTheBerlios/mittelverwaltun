package dbObjects;

import java.io.Serializable;

/**
 * @author robert
 *
 * Folgendes ausw�hlen, um die Schablone f�r den erstellten Typenkommentar zu �ndern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class Position implements Serializable{

	private int id;

	private String beschreibung;

	private float einzelPreis;

	private int menge;
	private float rabatt;

	private float mwst;

	public Position(int id, String beschreibung, float einzelPreis, int menge, float mwst, float rabatt){
		this.id = id;
		this.beschreibung = beschreibung;
		this.einzelPreis = einzelPreis;
		this.menge = menge;
		this.mwst = mwst;
		this.rabatt = rabatt;
	}
	public Position(String beschreibung, float einzelPreis, int menge, float mwst, float rabatt){
		this.beschreibung = beschreibung;
		this.einzelPreis = einzelPreis;
		this.menge = menge;
		this.mwst = mwst;
		this.rabatt = rabatt;
	}
	
	public String toString(){
		return "Id:" + id 	
				+ " Beschreibung:" + beschreibung 
				+ " Einzelpreis:" + einzelPreis
				+ " Menge:" + menge
				+ " MwSt.:" + rabatt;
	}
  
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public float getEinzelPreis() {
		return einzelPreis;
	}

	public void setEinzelPreis(float einzelPreis) {
		this.einzelPreis = einzelPreis;
	}

	public int getMenge() {
		return menge;
	}

	public void setMenge(int menge) {
		this.menge = menge;
	}

	public float getRabatt() {
		return rabatt;
	}

	public void setRabatt(float rabatt) {
		this.rabatt = rabatt;
	}

	public float getMwst() {
		return mwst;
	}

	public void setMwst(float mwst) {
		this.mwst = mwst;
	}
	
	public float getGesamtpreis(){
		return this.menge * this.einzelPreis + (this.menge * this.einzelPreis * this.mwst) - this.rabatt;
	}

}
