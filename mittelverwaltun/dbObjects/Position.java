package dbObjects;

import java.io.Serializable;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class Position implements Serializable {

	private int id;

	private float einzelPreis;

	private int menge;
	private float rabatt;

	private float mwst;

	private boolean beglichen;

	private Institut institut;

	private String artikel;

	/**
	 * Konstruktor für die Position mit allen Attributen
	 * @param id
	 * @param artikel
	 * @param einzelPreis
	 * @param menge
	 * @param mwst
	 * @param rabatt
	 * @param institut
	 */
	public Position(int id, String artikel, float einzelPreis, int menge, float mwst, float rabatt, Institut institut){
		this.menge = menge;
		this.artikel = artikel;
		this.einzelPreis = einzelPreis;
		this.mwst = mwst;
		this.rabatt = rabatt;
		this.id = id;
		this.institut = institut;
	}
	
	public Position(int id, String artikel, float einzelPreis, int menge, float mwst, float rabatt, Institut institut, boolean beglichen){
		this.menge = menge;
		this.artikel = artikel;
		this.einzelPreis = einzelPreis;
		this.mwst = mwst;
		this.rabatt = rabatt;
		this.id = id;
		this.institut = institut;
		this.beglichen = beglichen;
	}
	
	/**
	 * Konstruktor für die Position ohne die institut
	 * @param id
	 * @param artikel
	 * @param einzelPreis
	 * @param menge
	 * @param mwst
	 * @param rabatt
	 */
	public Position(int id, String artikel, float einzelPreis, int menge, float mwst, float rabatt){
		this.menge = menge;
		this.artikel = artikel;
		this.einzelPreis = einzelPreis;
		this.mwst = mwst;
		this.rabatt = rabatt;
		this.id = id;
	}
	
	/**
	 * Konstruktor für die Position ohne die Id
	 * @param artikel
	 * @param einzelPreis
	 * @param menge
	 * @param mwst
	 * @param rabatt
	 * @param institut
	 */
	public Position(String artikel, float einzelPreis, int menge, float mwst, float rabatt, Institut institut){
		this.menge = menge;
		this.artikel = artikel;
		this.einzelPreis = einzelPreis;
		this.mwst = mwst;
		this.rabatt = rabatt;
		this.institut = institut;
	}
	
	/**
	 * Konstruktor für die Position ohne die Id, institut
	 * @param artikel
	 * @param einzelPreis
	 * @param menge
	 * @param mwst
	 * @param rabatt
	 */
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
