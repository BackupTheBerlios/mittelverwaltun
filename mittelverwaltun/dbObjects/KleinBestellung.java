package dbObjects;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Die Klasse zum Speichern einer Kleinbestellung [= Auszahlungsanordnung]. 
 * @author w.flat
 */
public class KleinBestellung extends Bestellung implements Serializable {

	/**
	 * Liste mit den Belegen der Auszahlungsanordnng. 
	 */
	private ArrayList belege;

	/**
	 * Die Projektnummer der Bestellung. 
	 */
	private String projektNr;

	/**
	 * Die Begründung oder Verwendungszweck für die Bestellung. 
	 */
	private String verwendungszweck;

	/**
	 * Labor, wo die Bestellung eingetragen wurde. 
	 */
	private String labor;

	/**
	 * Die Karteibezeichnung, wo die Auszahlungsanordnung eingetragen wurde. 
	 */
	private String kartei;

	/**
	 * Das Verzeichnis für die Bestellung. 
	 */
	private String verzeichnis;

	/**
	 * Konstruktor zum Erstellen einer Kleinbestellung. 
	 * @param id = Die Id der Bestellung. 
	 * @param datum = Das Datum, an dem die Bestellung durchgeführt wurde. 
	 * @param besteller = Der Benutzer, der in dem System angemeldet ist. 
	 * @param empfaenger = Der Benutzer, der den Betrag erhält. 
	 * @param zvtitel = Der ZVTitel, der für die Bestellung verwendet wurde. 
	 * @param fbkonto = Das FBKonto, welches für die Auszahlungsanordnung benutzt wurde. 
	 * @param bestellwert = Der Wert aller Belege. 
	 * @param projektNr = Die eingetragene ProjektNr.
	 * @param verwendungszweck = Die Begründung oder Verwendungszweck für die Bestellung. 
	 * @param labor = Labor, wo die Bestellung eingetragen wurde. 
	 * @param kartei = Die Karteibezeichnung, wo die Auszahlungsanordnung eingetragen wurde. 
	 * @param verzeichnis = Verzeichnis.
	 * @param belege = Liste mit allen dazugehörigen Belegen. 
	 */
	public KleinBestellung(int id, Date datum, Benutzer besteller, Benutzer empfaenger, 
							ZVUntertitel zvtitel, FBUnterkonto fbkonto, float bestellwert, String projektNr, 
							String verwendungszweck, String labor, String kartei, String verzeichnis, ArrayList belege) {
		// Die Auszahlungsanordnung ist immer abgeschlossen
		super(id, "", datum, besteller, '2', "", empfaenger, empfaenger, zvtitel, fbkonto, bestellwert, 0.0f);
		this.belege = belege;
		this.projektNr = projektNr;
		this.verwendungszweck = verwendungszweck;
		this.labor = labor;
		this.kartei = kartei;
		this.verzeichnis = verzeichnis;
		this.setTyp('2');
	}

	/**
	 * Abfragen aller Belege der Kleinbestellug. 
	 * @return <code>ArrayList</code> mit den Belegen.
	 */
	public ArrayList getBelege() {
		return belege;
	}

	/**
	 * Neue Belege der Auszahlungsanordnung hizufügen. 
	 * @param belege = Neue Belege der Bestellung. 
	 */
	public void setBelege(ArrayList belege) {
		this.belege = belege;
	}

	/**
	 * Die Projektnummer der Bestellugn abfragen. 
	 * @return Die Porjektnummer der Bestellung. 
	 */
	public String getProjektNr() {
		return projektNr;
	}

	/**
	 * Neue Projektnummer der Bestellung zuweisen. 
	 * @param projektNr = Neue Projektnummer der Auszahungsanordnung. 
	 */
	public void setProjektNr(String projektNr) {
		this.projektNr = projektNr;
	}

	/**
	 * Den Verwendungszweck oder/und die Begründung für die Bestellung abfragen. 
	 * @return Verwendungszweck der Bestellung. 
	 */
	public String getVerwendungszweck() {
		return verwendungszweck;
	}

	/**
	 * Neuen Verwendungszweck oder/und neue Begründung der Bestellung zuweisen. 
	 * @param verwendungszweck
	 */
	public void setVerwendungszweck(String verwendungszweck) {
		this.verwendungszweck = verwendungszweck;
	}

	/**
	 * Die Laborbezeichnung abfragen. 
	 * @return Der Name des Labors. 
	 */
	public String getLabor() {
		return labor;
	}

	/**
	 * Neue Laborbezeichnung der Bestellung zuweisen. 
	 * @param labor = Die neue Bezeichnung für das Labor.
	 */
	public void setLabor(String labor) {
		this.labor = labor;
	}

	/**
	 * Die Kartei abfragen. 
	 * @return Die Karteibezeichnung. 
	 */
	public String getKartei() {
		return kartei;
	}

	/**
	 * Neue Karteibezeichnung der Auszahlungsanordnung zuweisen. 
	 * @param kartei = Neue Karteibezeichnung.
	 */
	public void setKartei(String kartei) {
		this.kartei = kartei;
	}

	/**
	 * Das Verzeichnis der Bestellung abfragen.
	 * @return Das Verzeichnis der Bestellung. 
	 */
	public String getVerzeichnis() {
		return verzeichnis;
	}

	/**
	 * Neues Verzeichnis der Kleinbestellung zuweisen. 
	 * @param verzeichnis = Neue Verzeichnisbezeichnung. 
	 */
	public void setVerzeichnis(String verzeichnis) {
		this.verzeichnis = verzeichnis;
	}

}
