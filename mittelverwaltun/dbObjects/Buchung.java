package dbObjects;

import java.sql.Date;
/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class Buchung {

	private Benutzer benutzer;

	private Date datum;

	private char typ;

	private String beschreibung;

	private Bestellung bestellung;

	private ZVKonto zvKonto;

	private float betragZvKonto;

	private ZVUntertitel zvTitel1;

	private float betragZvTitel1;

	private ZVUntertitel zvTitel2;

	private float betragZvTitel2;

	private FBUnterkonto fbKonto1;

	private float betragFbKonto1;

	private FBUnterkonto fbKonto2;

	private float betragFbKonto2;

	public Benutzer getBenutzer() {
		return benutzer;
	}

	public void setBenutzer(Benutzer benutzer) {
		this.benutzer = benutzer;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public char getTyp() {
		return typ;
	}

	public void setTyp(char typ) {
		this.typ = typ;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public Bestellung getBestellung() {
		return bestellung;
	}

	public void setBestellung(Bestellung bestellung) {
		this.bestellung = bestellung;
	}

	public ZVKonto getZvKonto() {
		return zvKonto;
	}

	public void setZvKonto(ZVKonto zvKonto) {
		this.zvKonto = zvKonto;
	}

	public float getBetragZvKonto() {
		return betragZvKonto;
	}

	public void setBetragZvKonto(float betragZvKonto) {
		this.betragZvKonto = betragZvKonto;
	}

	public ZVUntertitel getZvTitel1() {
		return zvTitel1;
	}

	public void setZvTitel1(ZVUntertitel zvTitel1) {
		this.zvTitel1 = zvTitel1;
	}

	public float getBetragZvTitel1() {
		return betragZvTitel1;
	}

	public void setBetragZvTitel1(float betragZvTitel1) {
		this.betragZvTitel1 = betragZvTitel1;
	}

	public ZVUntertitel getZvTitel2() {
		return zvTitel2;
	}

	public void setZvTitel2(ZVUntertitel zvTitel2) {
		this.zvTitel2 = zvTitel2;
	}

	public float getBetragZvTitel2() {
		return betragZvTitel2;
	}

	public void setBetragZvTitel2(float betragZvTitel2) {
		this.betragZvTitel2 = betragZvTitel2;
	}

	public FBUnterkonto getFbKonto1() {
		return fbKonto1;
	}

	public void setFbKonto1(FBUnterkonto fbKonto1) {
		this.fbKonto1 = fbKonto1;
	}

	public float getBetragFbKonto1() {
		return betragFbKonto1;
	}

	public void setBetragFbKonto1(float betragFbKonto1) {
		this.betragFbKonto1 = betragFbKonto1;
	}

	public FBUnterkonto getFbKonto2() {
		return fbKonto2;
	}

	public void setFbKonto2(FBUnterkonto fbKonto2) {
		this.fbKonto2 = fbKonto2;
	}

	public float getBetragFbKonto2() {
		return betragFbKonto2;
	}

	public void setBetragFbKonto2(float betragFbKonto2) {
		this.betragFbKonto2 = betragFbKonto2;
	}

}
