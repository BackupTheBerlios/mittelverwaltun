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

	private float einzelPreis;

	private int menge;
	private float rabatt;

	private float mwst;

	private boolean beglichen;

	private Institut institut;

	private String artikel;

	public Position(int id, String artikel, float einzelPreis, int menge, float mwst, float rabatt, Institut institut){
		this.menge = menge;
		this.artikel = artikel;
		this.einzelPreis = einzelPreis;
		this.mwst = mwst;
		this.rabatt = rabatt;
		this.id = id;
		this.institut = institut;
	}
	
	public Position(int id, String artikel, float einzelPreis, int menge, float mwst, float rabatt){
		this.menge = menge;
		this.artikel = artikel;
		this.einzelPreis = einzelPreis;
		this.mwst = mwst;
		this.rabatt = rabatt;
		this.id = id;
	}
	
	public Position(String artikel, float einzelPreis, int menge, float mwst, float rabatt, Institut institut){
		this.menge = menge;
		this.artikel = artikel;
		this.einzelPreis = einzelPreis;
		this.mwst = mwst;
		this.rabatt = rabatt;
		this.institut = institut;
	}
	
	public Position(String artikel, float einzelPreis, int menge, float mwst, float rabatt){
		this.menge = menge;
		this.artikel = artikel;
		this.einzelPreis = einzelPreis;
		this.mwst = mwst;
		this.rabatt = rabatt;
	}
	
	
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
	
	public boolean equals(Object o){
		if(o != null){
			Position position = (Position)o;
			if( id == position.getId() &&
					einzelPreis == position.getEinzelPreis() &&
					menge == position.getMenge() &&
					rabatt == position.getRabatt() &&
					mwst == position.getMwst() &&
					beglichen == position.getBeglichen() &&
					(institut == null || position.getInstitut() == null) ? true : institut == position.getInstitut() &&
					artikel == position.getArtikel()
				)
				return true;
			else
				return false;
		}else
			return false;
	}

}
