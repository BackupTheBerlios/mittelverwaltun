package dbObjects;

import java.io.Serializable;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class Position implements Serializable{

	private int id;

	private float einzelPreis;

	private int menge;
	private float rabatt;

	private float mwst;

	private boolean beglichen;

	private Institut institut;

	private String artikel;

	public Position(int id){}
	
	
	public String toString(){
		return "";
	}
  
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public boolean getBeglichen() {
		return beglichen;
	}

	public void setBeglichen(boolean beglichen) {
		this.beglichen = beglichen;
	}

	public Institut getInstitut() {
		return institut;
	}

	public void setInstitut(Institut institut) {
		this.institut = institut;
	}

	public String getArtikel() {
		return artikel;
	}

	public void setArtikel(String artikel) {
		this.artikel = artikel;
	}

}
