package dbObjects;

import java.io.Serializable;

/**
 * author robert
 * 
 * Klasse für die Benutzer im System
 */
public class Benutzer implements Serializable {

	/**
	 * Id des Benutzers
	 */
	private int id;

	/**
	 * Benutzername für den Login (eindeutig)
	 */
	private String benutzername;

	/**
	 * Passwort des Benutzers
	 */
	private String passwort;

	/**
	 * Rolle des Benutzers
	 */
	private Rolle rolle;

	/**
	 * Temporäre Rollen des Benutzers
	 */
	private TmpRolle[] tmpRollen;

	/**
	 * Institut dem der Benutzer zugeordnete ist
	 */
	private Institut kostenstelle;

	/**
	 * Titel des Benutzers z.B. Prof.
	 */
	private String titel;

	/**
	 * Vorname des Benutzers
	 */
	private String vorname;

	/**
	 * Name des Benutzers
	 */
	private String name;

	/**
	 * Email-Ardesse des Benutzers
	 */
	private String email;

	/**
	 * ID des Privat-Kontos des Benutzers, kann nur eine FBUnterkonto sein
	 */
	private int privatKonto;

	/**
	 * Flag ob der Benutzer geloscht wurde
	 */
	private boolean geloescht;

	/**
	 * Flag ob der Benutzer geändert wurde
	 */
	private boolean geaendert;
	
	/**
	 * Telefon des Benutzers
	 */
	private String telefon;

	/**
	 * Fax des Benutzers
	 */
	private String fax;

	/**
	 * Bau des Benutzers
	 */
	private String bau;

	/**
	 * Raum des Benutzers
	 */
	private String raum;

	/**
	 * Angabe ob der Benutzer ein Software-Beauftragte ist
	 */
	private boolean swBeauftragter;

	/**
	 * Sichtbarkeit des Benutzers - siehe Konstanten
	 */
	private int sichtbarkeit;
	
	/**
	 * Konstante für die Sichtbarkeit des Benutzers => nur Sicht auf Private Objekte z.B. PrivatKonto
	 */
	public static int VIEW_PRIVAT = 0;
	
	/**
	 * Konstante für die Sichtbarkeit des Benutzers => Institutweite Sicht
	 */
	public static int VIEW_INSTITUT = 1;
	
	/**
	 * Konstante für die Sichtbarkeit des Benutzers => Fachbereichsweite Sicht
	 */
	public static int VIEW_FACHBEREICH = 2;	

	/**
	 * Konstuktor
	 * @param id - Id des Benutzers
	 * @param benutzername - Benutzername für den Login (eindeutig)
	 * @param passwort
	 * @param rolle - Rolle des Benutzers im System
	 * @param tmpRollen - Temporäre Rollen des Benutzers
	 * @param kostenstelle - Institut dem der Benutzer zugeordnete ist
	 * @param titel - Titel des Benutzers z.B. Prof.
	 * @param vorname - Vorname des Benutzers
	 * @param name - Name des Benutzers
	 * @param email - Email-Ardesse des Benutzers
	 * @param privatKonto - ID des Privat-Kontos des Benutzers, kann nur eine FBUnterkonto sein
	 * @param telefon - Telefon des Benutzers
	 * @param fax - Fax des Benutzers
	 * @param bau - Bau des Benutzers
	 * @param raum - Raum des Benutzers
	 * @param swBeauftragter - Angabe ob der Benutzer ein Software-Beauftragte is
	 * @param geloescht - Flag ob der Benutzer geloscht wurde
	 * @param geaendert - Flag ob der Benutzer geändert wurde
	 * @param sichtbarkeit - Sichtbarkeit des Benutzers - siehe Klassen Konstanten
	 */
	public Benutzer(int id, String benutzername, String passwort, Rolle rolle, TmpRolle[] tmpRollen, Institut kostenstelle, String titel,
			String vorname, String name, String email, int privatKonto, String telefon, String fax, String bau, String raum, boolean swBeauftragter, 
			boolean geloescht, boolean geaendert, int sichtbarkeit){
		this.id = id;
		this.benutzername = benutzername;
		this.passwort = passwort;
		this.rolle = rolle;
		this.tmpRollen = tmpRollen;
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
		this.geloescht = geloescht;
		this.geaendert = geaendert;
		this.sichtbarkeit = sichtbarkeit;
	}
	
	/**
	 * Konstuktor
	 * @param benutzername - Benutzername für den Login (eindeutig)
	 * @param passwort
	 * @param rolle - Rolle des Benutzers im System
	 * @param kostenstelle - Institut dem der Benutzer zugeordnete ist
	 * @param titel - Titel des Benutzers z.B. Prof.
	 * @param name - Name des Benutzers
	 * @param vorname - Vorname des Benutzers
	 * @param email - Email-Ardesse des Benutzers
	 * @param privatKonto - ID des Privat-Kontos des Benutzers, kann nur eine FBUnterkonto sein
	 * @param telefon - Telefon des Benutzers
	 * @param fax - Fax des Benutzers
	 * @param bau - Bau des Benutzers
	 * @param raum - Raum des Benutzers
	 * @param swBeauftragter - Angabe ob der Benutzer ein Software-Beauftragte is
	 * @param sichtbarkeit - Sichtbarkeit des Benutzers - siehe Klassen Konstanten
	 */
	public Benutzer(String benutzername, String passwort, Rolle rolle, Institut kostenstelle, 
									String titel, String name, String vorname, String email, int privatKonto, 
									String telefon, String fax, String bau, String raum, boolean swBeauftragter, int sichtbarkeit){
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
		this.sichtbarkeit = sichtbarkeit;
	}
	
	/**
	 * Konstuktor
	 * @param id - Id des Benutzers
	 * @param benutzername - Benutzername für den Login (eindeutig)
	 * @param passwort
	 * @param rolle - Rolle des Benutzers im System
	 * @param kostenstelle - Institut dem der Benutzer zugeordnete ist
	 * @param titel - Titel des Benutzers z.B. Prof.
	 * @param name - Name des Benutzers
	 * @param vorname - Vorname des Benutzers
	 * @param email - Email-Ardesse des Benutzers
	 * @param privatKonto - ID des Privat-Kontos des Benutzers, kann nur eine FBUnterkonto sein
	 * @param telefon - Telefon des Benutzers
	 * @param fax - Fax des Benutzers
	 * @param bau - Bau des Benutzers
	 * @param raum - Raum des Benutzers
	 * @param swBeauftragter - Angabe ob der Benutzer ein Software-Beauftragte is
	 * @param sichtbarkeit - Sichtbarkeit des Benutzers - siehe Klassen Konstanten
	 */
	public Benutzer(int id, String benutzername, String passwort, Rolle rolle, Institut kostenstelle, 
									String titel, String name, String vorname, String email, int privatKonto, 
									String telefon, String fax, String bau, String raum, boolean swBeauftragter, int sichtbarkeit){
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
		this.sichtbarkeit = sichtbarkeit;
	}
	
	/**
	 * Konstuktor
	 * @param id - Id des Benutzers
	 * @param benutzername - Benutzername für den Login (eindeutig)
	 * @param name - Name des Benutzers
	 * @param vorname - Vorname des Benutzers
	 */
	public Benutzer(int id, String benutzername, String name, String vorname){
		this.id = id;
		this.benutzername = benutzername;
		this.vorname = vorname;
		this.name = name;
	}
	
	/**
	 * Konstuktor
	 * @param benutzername - Benutzername für den Login (eindeutig)
	 * @param passwort
	 * @param name - Name des Benutzers
	 * @param vorname - Vorname des Benutzers
	 */
	public Benutzer(String benutzername, String passwort, String vorname,String name){
		this.benutzername = benutzername;
		this.passwort = passwort;
		this.vorname = vorname;
		this.name = name;
	}
	
	/**
	 * Konstuktor
	 * @param name - Name des Benutzers
	 * @param vorname - Vorname des Benutzers
	 */
	public Benutzer(String name, String vorname){
		this.vorname = vorname;
		this.name = name;
	}
	
	public String toString(){
		return vorname + " " + name + ", " + benutzername; 
	}

	/**
	 * Ausgabe für die Bestellung. 
	 * @return Name und Vorname als String
	 * author w.flat
	 */
	public String toBestellString(){
		return titel + " " + vorname + " " + name; 
	}

	/**
	 * gibt die Id des Benutzers zurück
	 * @return Id
	 */
	public int getId() {
		return id;
	}

	/**
	 * setzt die Id des Benutzers
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * gibt den Benutzernamen des Bentzers zurück
	 * @return Benutzername
	 */
	public String getBenutzername() {
		return benutzername;
	}

	/**
	 * setzt den Benutzernamen des Bentzers
	 * @param benutzername
	 */
	public void setBenutzername(String benutzername) {
		this.benutzername = benutzername;
	}

	/**
	 * gibt das Passwort des Bentzers zurück (wird nicht verwendet)
	 * @return Passwort
	 */
	public String getPasswort() {
		return passwort;
	}

	/**
	 * setzt das Passwort des Bentzers
	 * @param passwort
	 */
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	/**
	 * gibt die Rolle des Benutzers zurück
	 * @return Rolle
	 */
	public Rolle getRolle() {
		return rolle;
	}

	/**
	 * setzt  die Rolle des Benutzers
	 * @param rolle
	 */
	public void setRolle(Rolle rolle) {
		this.rolle = rolle;
	}

	/**
	 * gibt die temporären Rollen des Benutzers zurück
	 * @return TmpRolle-Array
	 */
	public TmpRolle[] getTmpRollen() {
		return tmpRollen;
	}

	/**
	 * setzt die temporären Rollen des Benutzers
	 * @param tmpRollen-Array
	 */
	public void setTmpRollen(TmpRolle[] tmpRollen) {
		this.tmpRollen = tmpRollen;
	}

	/**
	 * gibt die Kostenstelle des Benutzers zurück
	 * @return Kostenstelle (Institut)
	 */
	public Institut getKostenstelle() {
		return kostenstelle;
	}

	/**
	 * setzt die Kostenstelle des Benutzers
	 * @param kostenstelle
	 */
	public void setKostenstelle(Institut kostenstelle) {
		this.kostenstelle = kostenstelle;
	}

	/**
	 * gibt den Titel des Benutzers zurück
	 * @return Titel
	 */
	public String getTitel() {
		if(titel == null)
			return "";
		return titel;
	}

	/**
	 * setzt den Titel des Benutzers
	 * @param titel
	 */
	public void setTitel(String titel) {
		this.titel = titel;
	}

	/**
	 * gibt den Vornamen des Benutzers zurück
	 * @return Titel
	 */
	public String getVorname() {
		return vorname;
	}

	/**
	 * setzt den Vornamen des Benutzers
	 * @param vorname
	 */
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	/**
	 * gibt den Namen des Benutzers zurück
	 * @return Titel
	 */
	public String getName() {
		return name;
	}

	/**
	 * setzt den Namen des Benutzers
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * gibt die Email des Benutzers zurück
	 * @return email
	 */
	public String getEmail() {
		if(email == null)
					return "";
		return email;
	}

	/**
	 * setzt die Email des Benutzers
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * gibt die Id des PrivatKontos des Benutzers zurück
	 * @return privatKonto
	 */
	public int getPrivatKonto() {
		return privatKonto;
	}

	/**
	 * setzt die Id des PrivatKontos des Benutzers
	 * @param privatKonto
	 */
	public void setPrivatKonto(int privatKonto) {
		this.privatKonto = privatKonto;
	}

	/**
	 * gibt an ob der Benutzer im System gelöscht wurde
	 * @return gelöscht ?
	 */
	public boolean getGeloescht() {
		return geloescht;
	}

	/**
	 * setzt das Flag gelöscht
	 * @param geloescht
	 */
	public void setGeloescht(boolean geloescht) {
		this.geloescht = geloescht;
	}

	/**
	 * gibt an ob der Benutzer im System geaendert wurde
	 * @return geaendert ?
	 */
	public boolean getGeaendert() {
		return geaendert;
	}

	/**
	 * setzt das Flag geaendert
	 * @param geaendert
	 */
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
					((fax == null || user.getFax() == null) ? true : fax.equals(user.getFax())) 
				)
				return true;
			else
				return false;
		}else
			return false;
	}

	/**
	 * gibt das Telefon des Benutzers zurück
	 * @return Telefon
	 */
	public String getTelefon() {
		if(telefon == null)
			return "";
		return telefon;
	}

	/**
	 * setzt das Telefon des Benutzers
	 * @param telefon
	 */
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	/**
	 * gibt das Fax des Benutzers zurück
	 * @return Fax
	 */
	public String getFax() {
		if(fax == null)
			return "";
		return fax;
	}

	/**
	 * setzt das Fax des Benutzers
	 * @param fax
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * gibt das Bau des Benutzers zurück
	 * @return Bau
	 */
	public String getBau() {
		return bau;
	}

	/**
	 * setzt das Bau des Benutzers
	 * @param bau
	 */
	public void setBau(String bau) {
		this.bau = bau;
	}

	/**
	 * gibt den Raum des Benutzers zurück
	 * @return Raum
	 */
	public String getRaum() {
		return raum;
	}

	/**
	 * setzt den Raum des Benutzers
	 * @param raum
	 */
	public void setRaum(String raum) {
		this.raum = raum;
	}

	/**
	 * gibt an ob der Benutzer ein Software-Beauftragter ist
	 * @return Software-Beauftragter ?
	 */
	public boolean getSwBeauftragter() {
		return swBeauftragter;
	}

	/**
	 * setzt das Flag ob der Benutzer ein Software-Beauftragter ist
	 * @param swBeauftragter
	 */
	public void setSwBeauftragter(boolean swBeauftragter) {
		this.swBeauftragter = swBeauftragter;
	}
	
	/**
	 * gibt die Sichtbarkeit des Benutzers zurück
	 * @return sichtbarkeit ( Integer-Wert siehe Konstant (Benutzer))
	 */
	public int getSichtbarkeit() {
		return sichtbarkeit;
	}

	/**
	 * setzt die Sichtbarkeit des Benutzers
	 * @param sichtbarkeit ( Integer-Wert siehe Konstant (Benutzer))
	 */
	public void setSichtbarkeit(int sichtbarkeit) {
		this.sichtbarkeit = sichtbarkeit;
	}

	/**
	 * gibt eine Kopie des Benutzers zurück
	 */
	public Object clone(){
		TmpRolle[] tr = null;
		
		if (this.tmpRollen != null){
			tr = new TmpRolle[this.tmpRollen.length];
			for (int i=0; i<this.tmpRollen.length; i++)
				tr[i] = (TmpRolle)tmpRollen[i].clone();
		}
		
		return new Benutzer(
						this.id, this.benutzername, this.passwort, rolle==null?null:(Rolle)rolle.clone(), tr, kostenstelle==null?null:(Institut)kostenstelle.clone(),
						this.titel, this.vorname, this.name, this.email, this.privatKonto, this.telefon, this.fax, this.bau, this.raum, this.swBeauftragter, 
						this.geloescht, this.geaendert, this.sichtbarkeit);
	}
	
}
