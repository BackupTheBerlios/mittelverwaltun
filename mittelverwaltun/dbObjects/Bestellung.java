package dbObjects;

import java.io.Serializable;
import java.sql.Date;
/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class Bestellung implements Serializable {

	/**
	 * Id der Bestellung
	 */
	private int id;

	/**
	 * Referenznummer der Bestellung (im Moment nur für Standard-Bestellung)
	 */
	private String referenznr;

	/**
	 * Hül-Nummer der Bestellung
	 */
	private String huel;

	/**
	 * Datum der Bestellung
	 */
	private Date datum;

	/**
	 * Benutzer der die Bestellung bestellt
	 */
	private Benutzer besteller;

	/**
	 * Auftraggeber der Bestellung
	 */
	private Benutzer auftraggeber;

	/**
	 * Empfänger der Bestellung
	 */
	private Benutzer empfaenger;

	private ZVUntertitel zvtitel;

	private FBUnterkonto fbkonto;

	/**
	 * Bestellwert der Bestellung
	 */
	private float bestellwert;
	
	/**
	 * Verbindlichkeiten der Bestellung
	 */
	private float verbindlichkeiten;

	/**
	 * Phase der Bestellung: 0 - Sondierung, 1 - Abwicklung, 2 - Abgeschlossen, 3 - Storno
	 */
	private char phase;
	
	/**
	 * Typ der Bestellung: 0 - StandardBestellung, 1 - ASKBestellung, 2 - Kleinbestellung
	 */
	private char typ;
	
	/**
	 * Flag ob die Bestellung gelöscht wurde
	 */
	private boolean geloescht;

	/**
	 * Konstuktor
	 * @param id - Id der Bestellung
	 * @param referenznr - Referenznummer der Bestellung (im Moment nur für Standard-Bestellung)
	 * @param huel - Hül-Nummer der Bestellung
	 * @param datum - Datum der Bestellung
	 * @param besteller
	 * @param auftraggeber
	 * @param empfaenger
	 * @param zvtitel
	 * @param fbkonto
	 * @param bestellwert - Bestellwert der Bestellung
	 * @param verbindlichkeiten - Verbindlichkeiten der Bestellung
	 * @param phase - Phase der Bestellung: 0 - Sondierung, 1 - Abwicklung, 2 - Abgeschlossen, 3 - Storno
	 * @param typ - Typ der Bestellung: 0 - StandardBestellung, 1 - ASKBestellung, 2 - Kleinbestellung
	 */
	public Bestellung(	int id, String referenznr, String huel, Date datum, Benutzer besteller, Benutzer auftraggeber, Benutzer empfaenger,
				ZVUntertitel zvtitel, FBUnterkonto fbkonto, float bestellwert, float verbindlichkeiten, char phase, char typ){
		this.id = id;
		this.referenznr = referenznr;
		this.huel = huel;
		this.datum = datum;
		this.besteller = besteller;
		this.auftraggeber = auftraggeber;
		this.empfaenger = empfaenger;
		this.zvtitel = zvtitel;
		this.fbkonto = fbkonto;
		this.bestellwert = bestellwert;
		this.verbindlichkeiten = verbindlichkeiten;
		this.phase = phase;
		this.typ = typ;
	}
	
	/**
	 * Konstuktor
	 * @param id - Id der Bestellung
	 * @param referenznr - Referenznummer der Bestellung (im Moment nur für Standard-Bestellung)
	 * @param datum - Datum der Bestellung
	 * @param besteller
	 * @param phase - Phase der Bestellung: 0 - Sondierung, 1 - Abwicklung, 2 - Abgeschlossen, 3 - Storno
	 * @param huel - Hül-Nummer der Bestellung
	 * @param auftraggeber
	 * @param empfaenger
	 * @param zvtitel
	 * @param fbkonto
	 * @param bestellwert - Bestellwert der Bestellung
	 * @param verbindlichkeiten - Verbindlichkeiten der Bestellung
	 */
	public Bestellung(int id, String referenznr, Date datum, Benutzer besteller, char phase, String huel, Benutzer auftraggeber,
										Benutzer empfaenger, ZVUntertitel zvtitel, FBUnterkonto fbkonto, float bestellwert, float verbindlichkeiten){
		this.id = id;
		this.referenznr = referenznr;
		this.datum = datum;
		this.besteller = besteller;
		this.phase = phase;
		this.huel = huel;
		this.auftraggeber = auftraggeber;
		this.empfaenger = empfaenger;
		this.zvtitel = zvtitel;
		this.fbkonto = fbkonto;
		this.bestellwert = bestellwert;
		this.verbindlichkeiten = verbindlichkeiten;
		this.geloescht = false;
	}
	
	/**
	 * Konstuktor
	 * @param referenznr - Referenznummer der Bestellung (im Moment nur für Standard-Bestellung)
	 * @param datum - Datum der Bestellung
	 * @param besteller
	 * @param phase - Phase der Bestellung: 0 - Sondierung, 1 - Abwicklung, 2 - Abgeschlossen, 3 - Storno
	 * @param huel - Hül-Nummer der Bestellung
	 * @param auftraggeber
	 * @param empfaenger
	 * @param zvtitel
	 * @param fbkonto
	 * @param bestellwert - Bestellwert der Bestellung
	 * @param verbindlichkeiten - Verbindlichkeiten der Bestellung
	 */
	public Bestellung(String referenznr, Date datum, Benutzer besteller, char phase, String huel, Benutzer auftraggeber,
										Benutzer empfaenger, ZVUntertitel zvtitel, FBUnterkonto fbkonto, float bestellwert, float verbindlichkeiten){
		this.referenznr = referenznr;
		this.datum = datum;
		this.besteller = besteller;
		this.phase = phase;
		this.huel = huel;
		this.auftraggeber = auftraggeber;
		this.empfaenger = empfaenger;
		this.zvtitel = zvtitel;
		this.fbkonto = fbkonto;
		this.bestellwert = bestellwert;
		this.verbindlichkeiten = verbindlichkeiten;
		this.geloescht = false;
	}
	
	/**
	 * Konstuktor
	 * @param datum - Datum der Bestellung
	 * @param besteller
	 * @param phase - Phase der Bestellung: 0 - Sondierung, 1 - Abwicklung, 2 - Abgeschlossen, 3 - Storno
	 * @param auftraggeber
	 * @param empfaenger
	 * @param zvtitel
	 * @param fbkonto
	 * @param bestellwert - Bestellwert der Bestellung
	 * @param verbindlichkeiten - Verbindlichkeiten der Bestellung
	 */
	public Bestellung(Date datum, Benutzer besteller, char phase, Benutzer auftraggeber,
										Benutzer empfaenger, ZVUntertitel zvtitel, FBUnterkonto fbkonto, float bestellwert, float verbindlichkeiten){
		this.datum = datum;
		this.besteller = besteller;
		this.phase = phase;
		this.auftraggeber = auftraggeber;
		this.empfaenger = empfaenger;
		this.zvtitel = zvtitel;
		this.fbkonto = fbkonto;
		this.bestellwert = bestellwert;
		this.verbindlichkeiten = verbindlichkeiten;
		this.geloescht = false;
	}

	/**
	 * Konstuktor
	 * @param id - Id der Bestellung
	 * @param datum - Datum der Bestellung
	 * @param typ - Typ der Bestellung: 0 - StandardBestellung, 1 - ASKBestellung, 2 - Kleinbestellung
	 * @param phase - Phase der Bestellung: 0 - Sondierung, 1 - Abwicklung, 2 - Abgeschlossen, 3 - Storno
	 * @param besteller
	 * @param auftraggeber
	 * @param empfaenger
	 * @param bestellwert - Bestellwert der Bestellung
	 * @param verbindlichkeiten - Verbindlichkeiten der Bestellung
	 */
	public Bestellung(int id, Date datum, char typ, char phase, Benutzer besteller, Benutzer auftraggeber,
					  Benutzer empfaenger, float bestellwert, float verbindlichkeiten){
		this.id = id;
		this.datum = datum;
		this.typ = typ;
		this.phase = phase;
		this.besteller = besteller;
		this.auftraggeber = auftraggeber;
		this.empfaenger = empfaenger;
		this.bestellwert = bestellwert;
		this.verbindlichkeiten = verbindlichkeiten;
		this.geloescht = false;
	}
	
	/**
	 * Konstuktor
	 * @param id - Id der Bestellung
	 * @param datum - Datum der Bestellung
	 * @param typ - Typ der Bestellung: 0 - StandardBestellung, 1 - ASKBestellung, 2 - Kleinbestellung
	 * @param phase - Phase der Bestellung: 0 - Sondierung, 1 - Abwicklung, 2 - Abgeschlossen, 3 - Storno
	 * @param besteller
	 * @param auftraggeber
	 * @param empfaenger
	 * @param titel - ZVTitel
	 * @param konto - FBKonto
	 * @param bestellwert - Bestellwert der Bestellung
	 */
	public Bestellung(int id, Date datum, char typ, char phase, Benutzer besteller, Benutzer auftraggeber,
				  ZVUntertitel titel, FBUnterkonto konto, float bestellwert){
		this.id = id;
		this.referenznr = "";
		this.datum = datum;
		this.besteller = besteller;
		this.typ = typ;
		this.phase = phase;
		this.huel = "";
		this.auftraggeber = auftraggeber;
		this.empfaenger = null;
		this.zvtitel = titel;
		this.fbkonto = konto;
		this.bestellwert = bestellwert;
		this.verbindlichkeiten = 0.0f;
		this.geloescht = false;
	}
	
	/**
	 * gibt die Id der Bestellung zurück
	 * @return Id
	 */
	public int getId() {
		return id;
	}

	/**
	 * setzt die Id der Bestellung
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * setzt das Flag geloescht
	 * @param geloescht
	 */
	public void setGeloescht(boolean geloescht) {
		this.geloescht = geloescht;
	}

	/**
	 * gibt an ob die Bestellung gelöscht wurde
	 * @return geloescht ?
	 */
	public boolean getGeloescht() {
		return geloescht;
	}

	/**
	 * gibt die Referenz-Nummer der Bestellung zurück
	 * @return referenznr
	 */
	public String getReferenznr() {
		return referenznr;
	}

	/**
	 * setzt  die Referenz-Nummer der Bestellung
	 * @param referenznr
	 */
	public void setReferenznr(String referenznr) {
		this.referenznr = referenznr;
	}

	/**
	 * gibt die Huel-Nummer der Bestellung zurück
	 * @return huel
	 */
	public String getHuel() {
		return huel;
	}

	/**
	 * setzt die Huel-Nummer der Bestellung
	 * @param huel
	 */
	public void setHuel(String huel) {
		this.huel = huel;
	}

	/**
	 * gibt das Datum der Bestellung zurück
	 * @return Datum
	 */
	public Date getDatum() {
		return datum;
	}

	/**
	 * setzt das Datum der Bestellung
	 * @param datum
	 */
	public void setDatum(Date datum) {
		this.datum = datum;
	}

	/**
	 * gibt den Besteller der Bestellung zurück
	 * @return Besteller
	 */
	public Benutzer getBesteller() {
		return besteller;
	}

	/**
	 * setzt den Besteller der Bestellung
	 * @param besteller
	 */
	public void setBesteller(Benutzer besteller) {
		this.besteller = besteller;
	}

	/**
	 * gibt die Phase der Bestellung zurück in der diese sich befindet
	 * @return Phase: 0 - Sondierung, 1 - Abwicklung, 2 - Abgeschlossen, 3 - Storno
	 */
	public char getPhase() {
		return phase;
	}

	/**
	 * setzt die Phase der Bestellung
	 * @param phase - 0 - Sondierung, 1 - Abwicklung, 2 - Abgeschlossen, 3 - Storno
	 */
	public void setPhase(char phase) {
		this.phase = phase;
	}
	
	/**
	 * gibt den Typ der Bestellung zurück
	 * @return Typ: 0 - StandardBestellung, 1 - ASKBestellung, 2 - Kleinbestellung
	 */
	public char getTyp() {
		return typ;
	}

	/**
	 * setzt den Typ der Bestellung
	 * @param typ
	 */
	public void setTyp(char typ) {
		this.typ = typ;
	}

	/**
	 * gibt den Auftrageber der Bestellung zurück
	 * @return Auftrageber
	 */
	public Benutzer getAuftraggeber() {
		return auftraggeber;
	}

	/**
	 * setzt den Auftrageber der Bestellung
	 * @param auftraggeber
	 */
	public void setAuftraggeber(Benutzer auftraggeber) {
		this.auftraggeber = auftraggeber;
	}

	/**
	 * gibt den Empfaenger der Bestellung zurück
	 * @return Empfaenger
	 */
	public Benutzer getEmpfaenger() {
		return empfaenger;
	}

	/**
	 * setzt den Empfaenger der Bestellung
	 * @param empfaenger
	 */
	public void setEmpfaenger(Benutzer empfaenger) {
		this.empfaenger = empfaenger;
	}

	public ZVUntertitel getZvtitel() {
		return zvtitel;
	}

	public void setZvtitel(ZVUntertitel zvtitel) {
		this.zvtitel = zvtitel;
	}

	public FBUnterkonto getFbkonto() {
		return fbkonto;
	}

	public void setFbkonto(FBUnterkonto fbkonto) {
		this.fbkonto = fbkonto;
	}

	public float getBestellwert() {
		return bestellwert;
	}

	public void setBestellwert(float bestellwert) {
		this.bestellwert = bestellwert;
	}
	
	public float getVerbindlichkeiten() {
		return verbindlichkeiten;
	}

	public void setVerbindlichkeiten(float verbindlichkeiten) {
		this.verbindlichkeiten = verbindlichkeiten;
	}
	
}
