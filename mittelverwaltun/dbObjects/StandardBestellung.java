package dbObjects;

import java.util.ArrayList;
/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class StandardBestellung extends Bestellung {

	private ArrayList angebote;

	/**
	 * Die ID des ausgewählten Angebots
	 */
	private int auswahl;

	private Kostenart kostenart;

	private boolean ersatzbeschaffung;

	private String ersatzbeschreibung;

	private String inventarNr;

	private String verwendungszweck;

	private boolean planvorgabe;

	private String begruendung;

	public ArrayList getAngebote() {
		return angebote;
	}

	public void setAngebote(ArrayList angebote) {
		this.angebote = angebote;
	}

	public int getAuswahl() {
		return auswahl;
	}

	public void setAuswahl(int auswahl) {
		this.auswahl = auswahl;
	}

	public Kostenart getKostenart() {
		return kostenart;
	}

	public void setKostenart(Kostenart kostenart) {
		this.kostenart = kostenart;
	}

	public boolean getErsatzbeschaffung() {
		return ersatzbeschaffung;
	}

	public void setErsatzbeschaffung(boolean ersatzbeschaffung) {
		this.ersatzbeschaffung = ersatzbeschaffung;
	}

	public String getErsatzbeschreibung() {
		return ersatzbeschreibung;
	}

	public void setErsatzbeschreibung(String ersatzbeschreibung) {
		this.ersatzbeschreibung = ersatzbeschreibung;
	}

	public String getInventarNr() {
		return inventarNr;
	}

	public void setInventarNr(String inventarNr) {
		this.inventarNr = inventarNr;
	}

	public String getVerwendungszweck() {
		return verwendungszweck;
	}

	public void setVerwendungszweck(String verwendungszweck) {
		this.verwendungszweck = verwendungszweck;
	}

	public boolean getPlanvorgabe() {
		return planvorgabe;
	}

	public void setPlanvorgabe(boolean planvorgabe) {
		this.planvorgabe = planvorgabe;
	}

	public String getBegruendung() {
		return begruendung;
	}

	public void setBegruendung(String begruendung) {
		this.begruendung = begruendung;
	}

}
