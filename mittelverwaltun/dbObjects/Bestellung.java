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

	private int id;

	private String referenznr;

	private String huel;

	private Date datum;

	private Benutzer besteller;

	private Benutzer auftraggeber;

	private Benutzer empfaenger;

	private ZVTitel zvtitel;

	private FBUnterkonto fbkonto;

	private float bestellwert;

	private char phase;

	public Bestellung(int id, String referenznr, Date datum, Benutzer besteller, char phase, Benutzer auftraggeber,
										Benutzer empfaenger, ZVTitel zvtitel, FBUnterkonto fbkonto, float bestellwert){
		this.id = id;
		this.referenznr = referenznr;
		this.datum = datum;
		this.besteller = besteller;
		this.phase = phase;
		this.auftraggeber = auftraggeber;
		this.empfaenger = empfaenger;
		this.zvtitel = zvtitel;
		this.fbkonto = fbkonto;
		this.bestellwert = bestellwert;
	}
	
	public Bestellung(String referenznr, Date datum, Benutzer besteller, char phase, Benutzer auftraggeber,
										Benutzer empfaenger, ZVTitel zvtitel, FBUnterkonto fbkonto, float bestellwert){
		this.referenznr = referenznr;
		this.datum = datum;
		this.besteller = besteller;
		this.phase = phase;
		this.auftraggeber = auftraggeber;
		this.empfaenger = empfaenger;
		this.zvtitel = zvtitel;
		this.fbkonto = fbkonto;
		this.bestellwert = bestellwert;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReferenznr() {
		return referenznr;
	}

	public void setReferenznr(String referenznr) {
		this.referenznr = referenznr;
	}

	public String getHuel() {
		return huel;
	}

	public void setHuel(String huel) {
		this.huel = huel;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public Benutzer getBesteller() {
		return besteller;
	}

	public void setBesteller(Benutzer besteller) {
		this.besteller = besteller;
	}

	public char getPhase() {
		return phase;
	}

	public void setPhase(char phase) {
		this.phase = phase;
	}

	public Benutzer getAuftraggeber() {
		return auftraggeber;
	}

	public void setAuftraggeber(Benutzer auftraggeber) {
		this.auftraggeber = auftraggeber;
	}

	public Benutzer getEmpfaenger() {
		return empfaenger;
	}

	public void setEmpfaenger(Benutzer empfaenger) {
		this.empfaenger = empfaenger;
	}

	public ZVTitel getZvtitel() {
		return zvtitel;
	}

	public void setZvtitel(ZVTitel zvtitel) {
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
	
}
