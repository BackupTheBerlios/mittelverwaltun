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
	 * author w.flat 
	 */
	public Beleg(int id, int nummer, Firma firma, String artikel, float summe) {
		this.id = id;
		this.nummer = nummer;
		this.firma = firma;
		this.artikel = artikel;
		this.summe = summe;
	}

	/**
	 * Bfrage der Beleg-Id.
	 * @return Die Id des Belegs.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Neue Id dem Beleg zuweisen. 
	 * @param id = Neue Id des Belegs. 
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Die Nummer des Belegs innerhalb einer Bestellung abfragen. 
	 * @return Die Nummer des Belegs. 
	 */
	public int getNummer() {
		return nummer;
	}

	/**
	 * Neue Nummer dem Beleg zuweisen. 
	 * @param nummer = Neue Nummer des Belegs. 
	 */
	public void setNummer(int nummer) {
		this.nummer = nummer;
	}

	/**
	 * Die Firma, bei der der Artikel gekauft wurde abfragen. 
	 * @return Die Firma. 
	 */
	public Firma getFirma() {
		return firma;
	}

	/**
	 * Neue Firma dem Beleg zuweisen. 
	 * @param firma = Neue Firma für das Beleg. 
	 */
	public void setFirma(Firma firma) {
		this.firma = firma;
	}

	/**
	 * Den Artikelnamen abfragen.
	 * @return Die Bezeichnung des Atrikels. 
	 */
	public String getArtikel() {
		return artikel;
	}

	/**
	 * Neue Artikelbezeichnung dem beleg hinzufügen. 
	 * @param artikel = Der neue Name der gekauften Ware. 
	 */
	public void setArtikel(String artikel) {
		this.artikel = artikel;
	}

	/**
	 * Den Preis der Ware abfragen. 
	 * @return Der Preis der gekauften Ware. 
	 */
	public float getSumme() {
		return summe;
	}

	/**
	 * Neuen Betrag dem Belege zuweisen. 
	 * @param summe = Neuer Wert des Artikels. 
	 */
	public void setSumme(float summe) {
		this.summe = summe;
	}

}
