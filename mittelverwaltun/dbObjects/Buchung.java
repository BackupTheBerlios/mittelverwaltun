package dbObjects;

import java.sql.Timestamp;
/**
 * @author robert, mario
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class Buchung {

	private Benutzer benutzer;

	private Timestamp timestamp;

	private String typ;

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

	public Buchung (Benutzer benutzer, String typ, ZVKonto k, float buchung){
		this.benutzer = benutzer;
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.typ = typ;
		this.beschreibung = "";
		this.bestellung = null;
		this.zvKonto = k;
		this.betragZvKonto = buchung;
		this.zvTitel1 = null;
		this.betragZvTitel1 = 0;
		this.zvTitel2 = null;
		this.betragZvTitel2 = 0;
		this.fbKonto1 = null;
		this.betragFbKonto1 = 0;
		this.fbKonto2 = null;
		this.betragFbKonto2 = 0;
	}
	
	public Buchung (Benutzer benutzer, String typ, ZVUntertitel t, float buchung){
		this.benutzer = benutzer;
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.typ = typ;
		this.beschreibung = "";
		this.bestellung = null;
		this.zvKonto = null;
		this.betragZvKonto = 0;
		this.zvTitel1 = t;
		this.betragZvTitel1 = buchung;
		this.zvTitel2 = null;
		this.betragZvTitel2 = 0;
		this.fbKonto1 = null;
		this.betragFbKonto1 = 0;
		this.fbKonto2 = null;
		this.betragFbKonto2 = 0;
	}
	
	public Buchung (Benutzer benutzer, String typ, FBUnterkonto k, float buchung){
		this.benutzer = benutzer;
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.typ = typ;
		this.beschreibung = "";
		this.bestellung = null;
		this.zvKonto = null;
		this.betragZvKonto = 0;
		this.zvTitel1 = null;
		this.betragZvTitel1 = 0;
		this.zvTitel2 = null;
		this.betragZvTitel2 = 0;
		this.fbKonto1 = k;
		this.betragFbKonto1 = buchung;
		this.fbKonto2 = null;
		this.betragFbKonto2 = 0;
	}
	
	public Buchung (Benutzer benutzer, String typ, ZVUntertitel t1, float buchung1, ZVUntertitel t2, float buchung2){
		this.benutzer = benutzer;
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.typ = typ;
		this.beschreibung = "";
		this.bestellung = null;
		this.zvKonto = null;
		this.betragZvKonto = 0;
		this.zvTitel1 = t1;
		this.betragZvTitel1 = buchung1;
		this.zvTitel2 = t2;
		this.betragZvTitel2 = buchung2;
		this.fbKonto1 = null;
		this.betragFbKonto1 = 0;
		this.fbKonto2 = null;
		this.betragFbKonto2 = 0;
	}
	
	public Buchung (Benutzer benutzer, String typ, FBUnterkonto k1, float buchung1, FBUnterkonto k2, float buchung2){
		this.benutzer = benutzer;
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.typ = typ;
		this.beschreibung = "";
		this.bestellung = null;
		this.zvKonto = null;
		this.betragZvKonto = 0;
		this.zvTitel1 = null;
		this.betragZvTitel1 = 0;
		this.zvTitel2 = null;
		this.betragZvTitel2 = 0;
		this.fbKonto1 = k1;
		this.betragFbKonto1 = buchung1;
		this.fbKonto2 = k2;
		this.betragFbKonto2 = buchung2;
	}
	
	public Buchung (Benutzer benutzer, String typ, Bestellung bestellung, ZVUntertitel t, FBUnterkonto k, float buchung){
		this.benutzer = benutzer;
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.typ = typ;
		this.beschreibung = "";
		this.bestellung = bestellung;
		this.zvKonto = null;
		this.betragZvKonto = 0;
		this.zvTitel1 = t;
		this.betragZvTitel1 = buchung;
		this.zvTitel2 = null;
		this.betragZvTitel2 = 0;
		this.fbKonto1 = k;
		this.betragFbKonto1 = buchung;
		this.fbKonto2 = null;
		this.betragFbKonto2 = 0;
	}
	
	public Buchung (Benutzer benutzer, String typ, Bestellung bestellung, ZVKonto zvk, float tgrBuchung, ZVUntertitel t, float titelBuchung, FBUnterkonto fbk, float kontoBuchung){
		this.benutzer = benutzer;
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.typ = typ;
		this.beschreibung = "";
		this.bestellung = bestellung;
		this.zvKonto = zvk;
		this.betragZvKonto = tgrBuchung;
		this.zvTitel1 = t;
		this.betragZvTitel1 = titelBuchung;
		this.zvTitel2 = null;
		this.betragZvTitel2 = 0;
		this.fbKonto1 = fbk;
		this.betragFbKonto1 = kontoBuchung;
		this.fbKonto2 = null;
		this.betragFbKonto2 = 0;
	}
	
	
	public Benutzer getBenutzer() {
		return benutzer;
	}

	public void setBenutzer(Benutzer benutzer) {
		this.benutzer = benutzer;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp t) {
		this.timestamp = t;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
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
