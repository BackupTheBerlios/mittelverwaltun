package dbObjects;

import java.io.Serializable;

/**
 * Klasse zum Speichern der Belege bei der Auszahlungsanordnung. 
 * @author w.flat
 */
public class Beleg implements Serializable {

	/**
	 * Eindeutige Identifikationsnummer. Wird von der DB vergeben.
	 */
	private int id;
	
	/**
	 * Die Nummer des Belegs bei der Bestellung.
	 */
	private int nummer;

	/**
	 * Die Firma bei der der Artikel gekauft wurde. 
	 */
	private Firma firma;

	/**
	 * Die Artikelbezeichnung. 
	 */
	private String artikel;

	/**
	 * Der Preis der gekauften Sache. 
	 */
	private float summe;
	
	/**
	 * Erstellt ein Beleg. Es müssen alle Attribute angegeben werden. 
	 * @param id = Eindeutige id des Belegs.
	 * @param nummer = Die Beleg-Nummer.
	 * @param firma = Die Firma bei der der Artikel gekauft wurde. 
	 * @param artikel = Die Artikelbezeichnung.
	 * @param summe = Der Preis des Artikels.
	 * @author w.flat 
	 */
	public Beleg(int id, int nummer, Firma firma, String artikel, float summe) {
		this.id = id;
		this.nummer = nummer;
		this.firma = firma;
		this.artikel = artikel;
		this.summe = summe;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNummer() {
		return nummer;
	}

	public void setNummer(int nummer) {
		this.nummer = nummer;
	}

	public Firma getFirma() {
		return firma;
	}

	public void setFirma(Firma firma) {
		this.firma = firma;
	}

	public String getArtikel() {
		return artikel;
	}

	public void setArtikel(String artikel) {
		this.artikel = artikel;
	}

	public float getSumme() {
		return summe;
	}

	public void setSumme(float summe) {
		this.summe = summe;
	}

}
