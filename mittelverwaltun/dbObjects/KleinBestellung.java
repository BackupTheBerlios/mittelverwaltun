package dbObjects;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

/**
 * @author robert
 */
public class KleinBestellung extends Bestellung implements Serializable {

	private ArrayList belege;

	private String projektNr;

	private String verwendungszweck;

	private String labor;

	private String kartei;

	private String verzeichnis;
	
	public KleinBestellung() {
	}

	/**
	 * Konstruktor zum Erstellen einer Kleinbestellung. 
	 */
	public KleinBestellung(int id, Date datum, Benutzer besteller, Benutzer empfaenger, 
							ZVUntertitel zvtitel, FBUnterkonto fbkonto, float bestellwert, String projektNr, 
							String verwendungszweck, String labor, String kartei, String verzeichnis, ArrayList belege) {
		super(id, "", datum, besteller, '2', "", empfaenger, empfaenger, zvtitel, fbkonto, bestellwert);
		this.belege = belege;
		this.projektNr = projektNr;
		this.verwendungszweck = verwendungszweck;
		this.labor = labor;
		this.kartei = kartei;
		this.verzeichnis = verzeichnis;
	}

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
