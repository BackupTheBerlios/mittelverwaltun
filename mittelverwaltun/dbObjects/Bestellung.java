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

	private ZVUntertitel zvtitel;

	private FBUnterkonto fbkonto;

	private float bestellwert;

	private char phase;
	
	private char typ;

	public Bestellung(	int id, String referenznr, String huel, Date datum, Benutzer besteller, Benutzer auftraggeber, Benutzer empfaenger,
				ZVUntertitel zvtitel, FBUnterkonto fbkonto, float bestellwert, char phase, char typ){
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
		this.phase = phase;
		this.typ = typ;
	}

	public Bestellung(int id, String referenznr, Date datum, Benutzer besteller, char phase, String huel, Benutzer auftraggeber,
										Benutzer empfaenger, ZVUntertitel zvtitel, FBUnterkonto fbkonto, float bestellwert){
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
	}
	
	public Bestellung(String referenznr, Date datum, Benutzer besteller, char phase, String huel, Benutzer auftraggeber,
										Benutzer empfaenger, ZVTitel zvtitel, FBUnterkonto fbkonto, float bestellwert){
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
	}
	
	public Bestellung(Date datum, Benutzer besteller, char phase, Benutzer auftraggeber,
										Benutzer empfaenger, ZVTitel zvtitel, FBUnterkonto fbkonto, float bestellwert){
		this.datum = datum;
		this.besteller = besteller;
		this.phase = phase;
		this.auftraggeber = auftraggeber;
		this.empfaenger = empfaenger;
		this.zvtitel = zvtitel;
		this.fbkonto = fbkonto;
		this.bestellwert = bestellwert;
	}

	public Bestellung(int id, Date datum, char typ, char phase, Benutzer besteller, Benutzer auftraggeber,
					  Benutzer empfaenger, float bestellwert){
		this.id = id;
		this.datum = datum;
		this.typ = typ;
		this.phase = phase;
		this.besteller = besteller;
		this.auftraggeber = auftraggeber;
		this.empfaenger = empfaenger;
		this.bestellwert = bestellwert;
	}
	
	
	public Bestellung() {
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
	
	public char getTyp() {
		return typ;
	}

	public void setTyp(char typ) {
		this.typ = typ;
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

	public ZVUntertitel getZvtitel() {
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
