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
public class Bestellung implements Serializable {

	private int id;

	private String referenznr;

	private String huel;

	private short status;

	private short kostenart;

	private Date datum;

	private String lieferant;

	private String empfaenger;

	private float zahlung;

	private float bestellsumme;

	private ZVUntertitel zvTitel;

	private FBUnterkonto fbKonto;

	private Benutzer besteller;

	private ArrayList[] angebote;

	public Bestellung(int id, String referenzNr, String huelNr, ZVUntertitel zvTitel, FBUnterkonto fbKonto, short status, 
										short kostenart, Date datum, Benutzer besteller, String lieferant, String empfaenger, float zahlung){
		this.id = id;
		this.referenznr = referenzNr;
		this.huel = huelNr;
		this.zvTitel =zvTitel;
		this.fbKonto = fbKonto;
		this.status = status;
		this.kostenart = kostenart;
		this.datum = datum;
		this.besteller = besteller;
		this.lieferant = lieferant;
		this.empfaenger = empfaenger;
		this.zahlung = zahlung;		
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

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public short getKostenart() {
		return kostenart;
	}

	public void setKostenart(short kostenart) {
		this.kostenart = kostenart;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public String getLieferant() {
		return lieferant;
	}

	public void setLieferant(String lieferant) {
		this.lieferant = lieferant;
	}

	public String getEmpfaenger() {
		return empfaenger;
	}

	public void setEmpfaenger(String empfaenger) {
		this.empfaenger = empfaenger;
	}

	public float getZahlung() {
		return zahlung;
	}

	public void setZahlung(float zahlung) {
		this.zahlung = zahlung;
	}

	public float getBestellsumme() {
		return bestellsumme;
	}

	public void setBestellsumme(float bestellsumme) {
		this.bestellsumme = bestellsumme;
	}

	public FBUnterkonto getFbKonto() {
		return fbKonto;
	}

	public void setFbKonto(FBUnterkonto fbKonto) {
		this.fbKonto = fbKonto;
	}

	public ZVUntertitel getZvTitel() {
		return zvTitel;
	}

	public void setZvTitel(ZVUntertitel zvTitel) {
		this.zvTitel = zvTitel;
	}

	public Benutzer getBesteller() {
		return besteller;
	}

	public void setBesteller(Benutzer besteller) {
		this.besteller = besteller;
	}

	public ArrayList[] getAngebote() {
		return angebote;
	}

	public void setAngebote(ArrayList[] angebote) {
		this.angebote = angebote;
	}

}
