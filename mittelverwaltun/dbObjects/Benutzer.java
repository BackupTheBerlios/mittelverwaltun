package dbObjects;

import java.io.Serializable;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class Benutzer implements Serializable{

	private int id;

	private String benutzername;

	private String passwort;

	private Rolle rolle;

	private TmpRolle[] tmpRollen;

	private Institut kostenstelle;

	private String titel;

	private String vorname;

	private String name;

	private String email;

	private int privatKonto;

	private boolean geloescht;

	private boolean geaendert;

	
	public Benutzer(String benutzername, String passwort, Rolle rolle, Institut kostenstelle, 
					String titel, String name, String vorname, String email, int privatKonto){
		this.id = 0;
		this.benutzername = benutzername;
		this.passwort = passwort;
		this.rolle = rolle;
		this.kostenstelle = kostenstelle;
		this.titel = titel;
		this.vorname = vorname;
		this.name = name;
		this.email = email;
		this.privatKonto = privatKonto;
	}
	
	public Benutzer(int id, String benutzername, String passwort, Rolle rolle, Institut kostenstelle, 
						String titel, String name, String vorname, String email, int privatKonto){
		this.id = id;
		this.benutzername = benutzername;
		this.passwort = passwort;
		this.rolle = rolle;
		this.kostenstelle = kostenstelle;
		this.titel = titel;
		this.vorname = vorname;
		this.name = name;
		this.email = email;
		this.privatKonto = privatKonto;
	}
	
	
	public Benutzer(String benutzername, String passwort, String vorname, String name){
		this.benutzername = benutzername;
		this.passwort = passwort;
		this.vorname = vorname;
		this.name = name;
	}
	
	public String toString(){
		return vorname + " " + name + ", " + benutzername; 
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBenutzername() {
		return benutzername;
	}

	public void setBenutzername(String benutzername) {
		this.benutzername = benutzername;
	}

	public String getPasswort() {
		return passwort;
	}

	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	public Rolle getRolle() {
		return rolle;
	}

	public void setRolle(Rolle rolle) {
		this.rolle = rolle;
	}

	public TmpRolle[] getTmpRollen() {
		return tmpRollen;
	}

	public void setTmpRollen(TmpRolle[] tmpRollen) {
		this.tmpRollen = tmpRollen;
	}

	public Institut getKostenstelle() {
		return kostenstelle;
	}

	public void setKostenstelle(Institut kostenstelle) {
		this.kostenstelle = kostenstelle;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPrivatKonto() {
		return privatKonto;
	}

	public void setPrivatKonto(int privatKonto) {
		this.privatKonto = privatKonto;
	}

	public boolean getGeloescht() {
		return geloescht;
	}

	public void setGeloescht(boolean geloescht) {
		this.geloescht = geloescht;
	}

	public boolean getGeaendert() {
		return geaendert;
	}

	public void setGeaendert(boolean geaendert) {
		this.geaendert = geaendert;
	}
	
	public boolean equals(Object benutzer){
		if(benutzer != null){
			if( this.id == ((Benutzer)benutzer).getId() && 
				this.benutzername.equals(((Benutzer)benutzer).getBenutzername())&&
				this.name.equals(((Benutzer)benutzer).getName())&&
				this.vorname.equals(((Benutzer)benutzer).getVorname())&&
				this.titel.equals(((Benutzer)benutzer).getTitel())&&
				this.email.equals(((Benutzer)benutzer).getEmail())&&
				this.privatKonto == (((Benutzer)benutzer).getPrivatKonto()) &&
				this.rolle.equals(((Benutzer)benutzer).getRolle())&&
				this.kostenstelle.equals(((Benutzer)benutzer).getKostenstelle()))
				return true;
			else
				return false;
		}else
			return false;
	}

}
