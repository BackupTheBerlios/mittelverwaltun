package dbObjects;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class StandardBestellung extends Bestellung implements Serializable {

	private ArrayList angebote;

	private Kostenart kostenart;

	private boolean ersatzbeschaffung;

	private String ersatzbeschreibung;

	private String inventarNr;

	private String verwendungszweck;

	private boolean planvorgabe;

	private String begruendung;
	
	private String bemerkung;

	/**
	 * 
	 * @param angebote
	 * @param auswahl
	 * @param kostenart
	 * @param ersatzbeschaffung
	 * @param ersatzbeschreibung
	 * @param inventarNr
	 * @param verwendungszweck
	 * @param planvorgabe
	 * @param begruendung
	 * @param bemerkung
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
	public StandardBestellung(ArrayList angebote, Kostenart kostenart, boolean ersatzbeschaffung, String ersatzbeschreibung,
														String inventarNr, String verwendungszweck, boolean planvorgabe, String begruendung, String bemerkung,
														String referenznr, Date datum, Benutzer besteller, char phase, Benutzer auftraggeber,
														Benutzer empfaenger, ZVTitel zvtitel, FBUnterkonto fbkonto, float bestellwert){
		super(referenznr, datum, besteller, phase, auftraggeber, empfaenger, zvtitel, fbkonto, bestellwert);
		this.angebote = angebote;
		this.kostenart = kostenart;
		this.ersatzbeschaffung = ersatzbeschaffung;
		this.ersatzbeschreibung = ersatzbeschreibung;
		this.inventarNr = inventarNr;
		this.verwendungszweck = verwendungszweck;
		this.planvorgabe = planvorgabe;
		this.begruendung = begruendung;
		this.bemerkung = bemerkung;
	}
	
	/**
	 * 
	 * @param angebote
	 * @param auswahl
	 * @param kostenart
	 * @param ersatzbeschaffung
	 * @param ersatzbeschreibung
	 * @param inventarNr
	 * @param verwendungszweck
	 * @param planvorgabe
	 * @param begruendung
	 * @param bemerkung
	 * @param id
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
	public StandardBestellung(ArrayList angebote, Kostenart kostenart, boolean ersatzbeschaffung, String ersatzbeschreibung,
														String inventarNr, String verwendungszweck, boolean planvorgabe, String begruendung, String bemerkung,
														int id, String referenznr, Date datum, Benutzer besteller, char phase, Benutzer auftraggeber,
														Benutzer empfaenger, ZVTitel zvtitel, FBUnterkonto fbkonto, float bestellwert){
		super(id, referenznr, datum, besteller, phase, auftraggeber, empfaenger, zvtitel, fbkonto, bestellwert);
		this.angebote = angebote;
		this.kostenart = kostenart;
		this.ersatzbeschaffung = ersatzbeschaffung;
		this.ersatzbeschreibung = ersatzbeschreibung;
		this.inventarNr = inventarNr;
		this.verwendungszweck = verwendungszweck;
		this.planvorgabe = planvorgabe;
		this.begruendung = begruendung;
		this.bemerkung = bemerkung;
	}
	
	/**
	 * Konstruktor ohne positionen und auswahl
	 * @param kostenart
	 * @param ersatzbeschaffung
	 * @param ersatzbeschreibung
	 * @param inventarNr
	 * @param verwendungszweck
	 * @param planvorgabe
	 * @param begruendung
	 * @param bemerkung
	 * @param id
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
	public StandardBestellung(Kostenart kostenart, boolean ersatzbeschaffung, String ersatzbeschreibung,
														String inventarNr, String verwendungszweck, boolean planvorgabe, String begruendung, String bemerkung,
														int id, String referenznr, Date datum, Benutzer besteller, char phase, Benutzer auftraggeber,
														Benutzer empfaenger, ZVTitel zvtitel, FBUnterkonto fbkonto, float bestellwert){
		super(id, referenznr, datum, besteller, phase, auftraggeber, empfaenger, zvtitel, fbkonto, bestellwert);
		this.kostenart = kostenart;
		this.ersatzbeschaffung = ersatzbeschaffung;
		this.ersatzbeschreibung = ersatzbeschreibung;
		this.inventarNr = inventarNr;
		this.verwendungszweck = verwendungszweck;
		this.planvorgabe = planvorgabe;
		this.begruendung = begruendung;
		this.bemerkung = bemerkung;
	}

	public ArrayList getAngebote() {
		return angebote;
	}

	public void setAngebote(ArrayList angebote) {
		this.angebote = angebote;
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

	public String getBemerkung() {
		return bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}
	
	public boolean equals(Object o){
		if(o != null){
			StandardBestellung b = (StandardBestellung)o;
			if( ((kostenart == null || b.getKostenart() == null) ? true : kostenart.equals(b.getKostenart())) &&
					ersatzbeschaffung == b.getErsatzbeschaffung() &&
					((ersatzbeschreibung == null || b.getErsatzbeschreibung() == null) ? true : ersatzbeschreibung.equals(b.getErsatzbeschreibung())) &&
					((inventarNr == null || b.getInventarNr() == null) ? true : inventarNr.equals(b.getInventarNr())) &&
					((verwendungszweck == null || b.getVerwendungszweck() == null) ? true : verwendungszweck.equals(b.getVerwendungszweck())) &&
					planvorgabe == b.getPlanvorgabe() &&
					((begruendung == null || b.getBegruendung() == null) ? true : begruendung.equals(b.getBegruendung())) &&
					((bemerkung == null || b.getBegruendung() == null) ? true : bemerkung.equals(b.getBemerkung())) && 
					// Bestellung
					getId() == b.getId() &&
					((getReferenznr() == null || b.getReferenznr() == null) ? true : getReferenznr().equals(b.getReferenznr())) &&
					((getHuel() == null || b.getHuel() == null) ? true : getHuel().equals(b.getHuel())) &&
					((getDatum() == null || b.getDatum() == null) ? true : getDatum().equals(b.getDatum())) &&
					((getBesteller() == null || b.getBesteller() == null) ? true : getBesteller().equals(b.getBesteller())) &&
					getPhase() == b.getPhase() &&
					((getAuftraggeber() == null || b.getAuftraggeber() == null) ? true : getAuftraggeber().equals(b.getAuftraggeber())) &&
					((getEmpfaenger() == null || b.getEmpfaenger() == null) ? true : getEmpfaenger().equals(b.getEmpfaenger())) &&
					((getZvtitel() == null || b.getZvtitel() == null) ? true : getZvtitel().equals(b.getZvtitel())) &&
					((getFbkonto() == null || b.getFbkonto() == null) ? true : getFbkonto().equals(b.getFbkonto())) &&
					getBestellwert() == b.getBestellwert()
					)
				return true;
			else
				return false;
		}else
			return false;
	}
}
