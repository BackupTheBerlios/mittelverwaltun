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
	public ASKBestellung(String referenznr, Date datum, Benutzer besteller, char phase, Benutzer auftraggeber, Benutzer empfaenger, ZVTitel zvtitel, FBUnterkonto fbkonto, float bestellwert) {
		super(datum, besteller, phase, auftraggeber, empfaenger, zvtitel, fbkonto, bestellwert);
		// TODO Automatisch erstellter Konstruktoren-Stub
	}

	private Angebot angebot;

	private String bemerkung;

	private Benutzer swbeauftragter;

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
