package dbObjects;

import java.io.Serializable;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class Benutzer implements Serializable {

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

	
	private String telefon;

	private String fax;

	private String bau;

	private String raum;

	private boolean swBeauftragter;

	public Benutzer(String benutzername, String passwort, Rolle rolle, Institut kostenstelle, 
									String titel, String name, String vorname, String email, int privatKonto, 
									String telefon, String fax, String bau, String raum, boolean swBeauftragter){
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
		this.telefon = telefon;
		this.fax = fax;
		this.bau = bau;
		this.raum = raum;
		this.swBeauftragter = swBeauftragter;
	}
	
	public Benutzer(int id, String benutzername, String passwort, Rolle rolle, Institut kostenstelle, 
									String titel, String name, String vorname, String email, int privatKonto, 
									String telefon, String fax, String bau, String raum, boolean swBeauftragter){
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
		this.telefon = telefon;
		this.fax = fax;
		this.bau = bau;
		this.raum = raum;
		this.swBeauftragter = swBeauftragter;
	}
	
	public Benutzer(int id, String benutzername, String name,String vorname){
		this.id = id;
		this.benutzername = benutzername;
		this.vorname = vorname;
		this.name = name;
	}
	
	public Benutzer(String benutzername, String passwort, String vorname,String name){
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
	
	public boolean equals(Object o){
		if(o != null){
			Benutzer user = (Benutzer)o;
			if( (id == user.getId()) &&
					((benutzername == null || user.getBenutzername() == null) ? true : benutzername.equals(user.getBenutzername())) &&
					((name == null || user.getName() == null) ? true : name.equals(user.getName())) &&
					((vorname == null || user.getVorname() == null) ? true : vorname.equals(user.getVorname())) && 
					((titel == null || user.getTitel() == null) ? true : titel.equals(user.getTitel())) &&
					((email == null || user.getEmail() == null) ? true : email.equals(user.getEmail())) &&
					 (privatKonto == (user.getPrivatKonto())) &&
					((rolle == null || user.getRolle() == null) ? true : rolle.equals(user.getRolle())) &&
					((kostenstelle == null || user.getKostenstelle() == null) ? true : kostenstelle.equals(user.getKostenstelle())) &&
					((bau == null || user.getBau() == null) ? true : bau.equals(user.getBau())) &&
					((raum == null || user.getRaum() == null) ? true : raum.equals(user.getRaum())) &&
					((telefon == null || user.getTelefon() == null) ? true : telefon.equals(user.getTelefon())) &&
					((fax == null || user.getFax() == null) ? true : fax.equals(user.getFax())) &&
					(swBeauftragter == user.getSwBeauftragter()) 
				)
				return true;
			else
				return false;
		}else
			return false;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getBau() {
		return bau;
	}

	public void setBau(String bau) {
		this.bau = bau;
	}

	public String getRaum() {
		return raum;
	}

	public void setRaum(String raum) {
		this.raum = raum;
	}

	public boolean getSwBeauftragter() {
		return swBeauftragter;
	}

	public void setSwBeauftragter(boolean swBeauftragter) {
		this.swBeauftragter = swBeauftragter;
	}

}
