package dbObjects;

import java.sql.Date;
import java.util.ArrayList;
/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class KleinBestellung extends Bestellung {

	/**
	 * @param referenznr
	 * @param datum
	 * @param besteller
	 * @param phase
	 * @param auftraggeber
	 * @param empfaenger
	 * @param zvtitel
	 * @param fbkonto
	 * @param bestellwert
	 */
	public KleinBestellung(String referenznr, Date datum, Benutzer besteller, char phase, Benutzer auftraggeber, Benutzer empfaenger, ZVTitel zvtitel, FBUnterkonto fbkonto, float bestellwert) {
		super(referenznr, datum, besteller, phase, auftraggeber, empfaenger, zvtitel, fbkonto, bestellwert);
		// TODO Automatisch erstellter Konstruktoren-Stub
	}

	private ArrayList belege;

	private String projektNr;

	private String verwendungszweck;

	private String labor;

	private String kartei;

	private String verzeichnis;

	public ArrayList getBelege() {
		return belege;
	}

	public void setBelege(ArrayList belege) {
		this.belege = belege;
	}

	public String getProjektNr() {
		return projektNr;
	}

	public void setProjektNr(String projektNr) {
		this.projektNr = projektNr;
	}

	public String getVerwendungszweck() {
		return verwendungszweck;
	}

	public void setVerwendungszweck(String verwendungszweck) {
		this.verwendungszweck = verwendungszweck;
	}

	public String getLabor() {
		return labor;
	}

	public void setLabor(String labor) {
		this.labor = labor;
	}

	public String getKartei() {
		return kartei;
	}

	public void setKartei(String kartei) {
		this.kartei = kartei;
	}

	public String getVerzeichnis() {
		return verzeichnis;
	}

	public void setVerzeichnis(String verzeichnis) {
		this.verzeichnis = verzeichnis;
	}

}
