package dbObjects;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class ASKBestellung extends Bestellung {

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
