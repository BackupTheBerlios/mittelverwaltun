package dbObjects;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class ASKBestellung extends Bestellung implements Serializable {

	private Angebot angebot;

	private String bemerkung;

	private Benutzer swbeauftragter;
	
	public ASKBestellung(	int id, String referenznr, String huel, Date datum, Benutzer besteller, Benutzer auftraggeber, Benutzer empfaenger,
			ZVUntertitel zvtitel, FBUnterkonto fbkonto, float bestellwert, char phase, char typ, Angebot angebot, String bemerkung, Benutzer swbeauftragter){
		
		super(id, referenznr, huel, datum, besteller, auftraggeber, empfaenger, zvtitel, fbkonto, bestellwert, phase, typ);
		this.angebot = angebot;
		this.bemerkung = bemerkung;
		this.swbeauftragter = swbeauftragter;
		
	}
	
	
	public ASKBestellung(String referenznr, Date datum, Benutzer besteller, char phase, Benutzer auftraggeber, Benutzer empfaenger, ZVTitel zvtitel, FBUnterkonto fbkonto, float bestellwert) {
		super(datum, besteller, phase, auftraggeber, empfaenger, zvtitel, fbkonto, bestellwert);
		// TODO Automatisch erstellter Konstruktoren-Stub
	}

	public Angebot getAngebot() {
		return angebot;
	}

	public void setAngebot(Angebot angebot) {
		this.angebot = angebot;
	}

	public String getBemerkung() {
		return bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	public Benutzer getSwbeauftragter() {
		return swbeauftragter;
	}

	public void setSwbeauftragter(Benutzer swbeauftragter) {
		this.swbeauftragter = swbeauftragter;
	}

}
