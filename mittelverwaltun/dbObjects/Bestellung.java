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

	private short phase;

	private Benutzer auftraggeber;

	private Benutzer empfaenger;

	private ZVTitel zvtitel;

	private FBUnterkonto fbkonto;

	private float bestellwert;

	public Bestellung(){}
	
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

	public short getPhase() {
		return phase;
	}

	public void setPhase(short phase) {
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
