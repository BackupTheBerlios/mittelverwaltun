package dbObjects;

import java.io.Serializable;

/**
 * FBUnterkonto. <br> 
 * Diese Klasse repräsentiert ein FB-Unterkonto, welches einem Professor <br> 
 * oder einem Mitarbeiter eines Institus zugeordnet ist. <br>
 * @author w.flat
 */
public class FBUnterkonto implements Serializable {

	/**
	 * Eideutige Id zur Identifizierung des FB-Unterkontos.
	 */
	private int id;

	/**
	 * Institut, welchem das FBUnterkonto zugeordnet ist.
	 */
	private Institut institut;

	/**
	 * Nummer des Hauptkontos dargestellt als String.
	 */
	private String hauptkonto;

	/**
	 * Nummer des Unterkontos dargestellt als String.
	 */
	private String unterkonto;

	/**
	 * Die Bezeichnung des FBUnterkontos.
	 */
	private String bezeichnung;

	/**
	 * Budget, welches diesem FBUnterkonto zugeordnet ist.
	 */
	private float budget;

	/**
	 * Budget, welches schon z.B. für Bestellungen vorgemerkt ist.
	 */
	private float vormerkungen;

	/**
	 * Flag zum Markieren, dass das FBUNterkonto gelöscht ist.
	 */
	private boolean geloescht;

	/**
	 * Id des Haushaltsjahres, in dme das FBUnterkonto gültig ist.
	 */
	private int haushaltsJahrID;
	
	/**
	 * Flag zum Anzeogen, ob ein FBHauptkonto für Kleinbestellungen verwendet werden kann.
	 */
	private boolean kleinbestellungen;
	
	/**
	 * Die ZVKonten, denen dieses FBKonto zugeordnet ist. 
	 */
	private Kontenzuordnung[] zuordnung;
	

	/**
	 * Konstruktor, zum Erzeugen von einem FBUnterkonto. 
	 * @param id = FBUnterkontoId
	 * @param haushaltsJahrID = HaushaltsJahrID
	 * @param inst = Institut, welchem das Konto zugeordnet ist.
	 * @param bez = Bezeichnung des Kontos
	 * @param haupt = Nummer des Hauptkontos
	 * @param unter = Nummer des Unterkontos
	 * @param budget = Budget, welches dieses Konto enthält
	 */
	public FBUnterkonto( int id, int haushaltsJahrID, Institut inst, String bez, String haupt, String unter, float budget ){
		this.id = id;
		this.bezeichnung = bez;
		this.institut = inst;
		this.hauptkonto = haupt;
		this.unterkonto = unter;
		this.budget = budget;
		this.vormerkungen = 0.0f;
		this.haushaltsJahrID = haushaltsJahrID;
		this.geloescht = false;
		this.kleinbestellungen = false;
		this.zuordnung = null;
	}
	
	/**
	 * Konstruktor, zum Erzeugen von einem FBUnterkonto. 
	 * @param id = FBUnterkontoId
	 * @param haushaltsJahrID = HaushaltsJahrID
	 * @param inst = Institut, welchem das Konto zugeordnet ist.
	 * @param bez = Bezeichnung des Kontos
	 * @param haupt = Nummer des Hauptkontos
	 * @param unter = Nummer des Unterkontos
	 * @param budget = Budget, welches dieses Konto enthält
	 * @param geloescht = Flag, ob Konto gelöscht
	 */
	public FBUnterkonto( int id, int haushaltsJahrID, Institut inst, String bez, String haupt,
																		String unter, float budget, boolean geloescht ){
		this.id = id;
		this.bezeichnung = bez;
		this.institut = inst;
		this.hauptkonto = haupt;
		this.unterkonto = unter;
		this.budget = budget;
		this.vormerkungen = 0.0f;
		this.haushaltsJahrID = haushaltsJahrID;
		this.geloescht = geloescht;
		this.kleinbestellungen = false;
		this.zuordnung = null;
	}

	/**
	 * Konstruktor, welcher alle Attribute enthält. 
	 * @param id = FBUnterkontoId
	 * @param haushaltsJahrID = HaushaltsJahrID
	 * @param inst = Institut, welchem das Konto zugeordnet ist.
	 * @param bez = Bezeichnung des Kontos
	 * @param haupt = Nummer des Hauptkontos
	 * @param unter = Nummer des Unterkontos
	 * @param budget = Budget, welches dieses Konto enthält
	 * @param vormerkungen = Vorgemerkte Mittel
	 * @param kleinbestellungen = Ob dieses Konto für Kleinbestellungen verwendet werden kann.
	 * @param geloescht = Flag, ob Konto gelöscht
	 */
	public FBUnterkonto( int id, int haushaltsJahrID, Institut inst, String bez, String haupt,
								String unter, float budget, float vormerkungen, boolean kleinbestellungen, boolean geloescht ){
		this.id = id;
		this.bezeichnung = bez;
		this.institut = inst;
		this.hauptkonto = haupt;
		this.unterkonto = unter;
		this.budget = budget;
		this.vormerkungen = vormerkungen;
		this.haushaltsJahrID = haushaltsJahrID;
		this.geloescht = geloescht;
		this.kleinbestellungen = kleinbestellungen;
		this.zuordnung = null;
	}

	/**
	 * Konstruktor, zum Erzeugen von einem FBUnterkonto. 
	 * @param id = FBUnterkontoId
	 * @param inst = Institut, welchem das Konto zugeordnet ist.
	 * @param bez = Bezeichnung des Kontos
	 * @param haupt = Nummer des Hauptkontos
	 * @param unter = Nummer des Unterkontos
	 * @param budget = Budget, welches dieses Konto enthält
	 */
	public FBUnterkonto( int id, String bez, Institut inst, String haupt, String unter, float budget ){
		this.id = id;
		this.bezeichnung = bez;
		this.institut = inst;
		this.hauptkonto = haupt;
		this.unterkonto = unter;
		this.budget = budget;
		this.vormerkungen = 0.0f;
		this.haushaltsJahrID = 0;
		this.geloescht = false;
		this.kleinbestellungen = false;
		this.zuordnung = null;
	}
	
	/**
	 * Konstruktor, zum Erzeugen von einem neuen FBUnterkonto. 
	 * @param inst = Institut, welchem das Konto zugeordnet ist.
	 * @param bez = Bezeichnung des Kontos
	 * @param haupt = Nummer des Hauptkontos
	 * @param unter = Nummer des Unterkontos
	 * @param budget = Budget, welches dieses Konto enthält
	 */
	public FBUnterkonto( String bez, Institut inst, String haupt, String unter ){
		this.id = 0;
		this.bezeichnung = bez;
		this.institut = inst;
		this.hauptkonto = haupt;
		this.unterkonto = unter;
		this.budget = 0.0f;
		this.vormerkungen = 0.0f;
		this.haushaltsJahrID = 0;
		this.geloescht = false;
		this.kleinbestellungen = false;
		this.zuordnung = null;
	}
	
	/**
	 * Konstruktor, zum Erzeugen von einem neuen FBUnterkonto. 
	 * @param inst = Institut, welchem das Konto zugeordnet ist.
	 * @param bez = Bezeichnung des Kontos
	 * @param haupt = Nummer des Hauptkontos
	 * @param unter = Nummer des Unterkontos
	 * @param budget = Budget, welches dieses Konto enthält
	 */
	public FBUnterkonto( int id, String bez, Institut inst, String haupt, String unter ){
		this.id = id;
		this.bezeichnung = bez;
		this.institut = inst;
		this.hauptkonto = haupt;
		this.unterkonto = unter;
		this.budget = 0.0f;
		this.vormerkungen = 0.0f;
		this.haushaltsJahrID = 0;
		this.geloescht = false;
		this.kleinbestellungen = false;
		this.zuordnung = null;
	}	
	/**
	 * Gleichheit von zwei FBUnterkonten. <br>
	 * Gleichheit ist gegeben, wenn Institut, Hauptkonto und Unterkonto gleich sind. <br>
	 * @param unterkonto = Das Zweite FBUnterkonto
	 * @return true = Wenn die zwei FBUnterkonten gleich sind, sonst = false. 
	 */
	public boolean equals( FBUnterkonto unterkonto ) {
		if(unterkonto == null)
			return false;
		if(!this.getClass().getName().equalsIgnoreCase(unterkonto.getClass().getName()))
			return false;
		if( this.getInstitut().getKostenstelle().equalsIgnoreCase( unterkonto.getInstitut().getKostenstelle() ) &&
											this.getHauptkonto().equalsIgnoreCase( unterkonto.getHauptkonto() ) &&
											this.getUnterkonto().equalsIgnoreCase( unterkonto.getUnterkonto() ) )
			return true;
		
		return false;
	}
	
	/**
	 * Das FBUnterkonto aktualisieren. 
	 * @param konto = Das FBUnterkonto, von dem die Änderungen übernommen werden. 
	 */
	public void setFBUnterkonto( FBUnterkonto konto ) {
		setHaushaltsJahrID( konto.getHaushaltsJahrID() );
		setBezeichnung( konto.getBezeichnung() );
		setHauptkonto( konto.getHauptkonto() );
		setUnterkonto( konto.getUnterkonto() );
		setBudget( konto.getBudget() );
		setVormerkungen(konto.getVormerkungen());
		setKleinbestellungen(konto.getKleinbestellungen());
		setGeloescht( konto.getGeloescht() );
	}
	
	/**
	 * Abfragen des Flags kleinbestellungen. 
	 * @return true = Wenn das FBKonto für Kleinbestellungen verwendet werden darf, sonst = false. 
	 */
	public boolean getKleinbestellungen() {
		return kleinbestellungen;
	}
	
	/**
	 * Neuen Wert dem Flag kleinbestellungen zuweisen. 
	 * @param kleinbestellungen = Neuer Wert des Flags kleinbestellungen. 
	 */
	public void setKleinbestellungen(boolean kleinbestellungen) {
		this.kleinbestellungen = kleinbestellungen;
	}
	
	/**
	 * Abfragen der Summe der vorgemerkten Mitteln. 
	 * @return Die vorgemerkten Mitteln.
	 */
	public float getVormerkungen() {
		return vormerkungen;
	}
	
	/**
	 * Neuen Wert den vorgemerkten Mitteln zuweisen. 
	 * @param vormerkungen = Neuer Wert der Vormerkungen. 
	 */
	public void setVormerkungen(float vormerkungen) {
		this.vormerkungen = vormerkungen;
	}


	/**
	 * Die Id des Haushaltsjahres abfragen.
	 * @return Die HaushaltsJahrID. 
	 */
	public int getHaushaltsJahrID() {
		return haushaltsJahrID;
	}

	/**
	 * Neues Haushaltsjahr dem FBKonto zuweisen. 
	 * @param haushaltsJahrID = Neues Haushaltsjahr. 
	 */
	public void setHaushaltsJahrID(int haushaltsJahrID) {
		this.haushaltsJahrID = haushaltsJahrID;
	}

	/**
	 * Die FBKonten-Id abfragen. 
	 * @return Die Id des Kontos. 
	 */
	public int getId() {
		return id;
	}

	/**
	 * Die des FBKontos aktualisieren. 
	 * @param id = Neuer Wert der Identifikationsnummer. 
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Die Institut, dem dieses FBKonto angehört ermitteln. 
	 * @return Das Institut. 
	 */
	public Institut getInstitut() {
		return institut;
	}
	
	/**
	 * Neuen Institut dem FBKonto zuweisen. 
	 * @param institut = Neuer Wert für das Institut. 
	 */
	public void setInstitut(Institut institut) {
		this.institut = institut;
	}

	/**
	 * Die Nummer vom Hauptkonto abfragen.
	 * @return Die aktuelle Hauptkontennummer. 
	 */
	public String getHauptkonto() {
		return hauptkonto;
	}

	/**
	 * Die Hauptkontennummer aktualisieren. 
	 * @param = Neuer Wert von dem Hauptkonto. 
	 */
	public void setHauptkonto(String hautpkonto) {
		this.hauptkonto = hautpkonto;
	}

	/**
	 * Die Nummer vom Unterkonto abfragen.
	 * @return Die aktuelle Unterkontennummer. 
	 */
	public String getUnterkonto() {
		return unterkonto;
	}

	/**
	 * Die Unterkontennummer aktualisieren. 
	 * @param = Neuer Wert von dem Unterkonto. 
	 */
	public void setUnterkonto(String unterkonto) {
		this.unterkonto = unterkonto;
	}

	/**
	 * Die Kontobezeichnung des FBKontos abfragen. 
	 * @return Die aktuelle Kontenbezeichnung.
	 */
	public String getBezeichnung() {
		return bezeichnung;
	}

	/**
	 * Die Bezeichnung von dem FBKonto ändern. 
	 * @param bezeichnung = Neue Kontobezeichnung.
	 */
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Die Budgetsumme des FBKontos bfragen. 
	 * @return Das aktuelle Budget auf dem FBKonto.
	 */
	public float getBudget() {
		return budget;
	}

	/**
	 * Neue Budgetsumme dem FBKonto zuweisen. 
	 * @param budget = Neue Budgetsumme. 
	 */
	public void setBudget(float budget) {
		this.budget = budget;
	}

	/**
	 * Den Wert des Flags gelöscht abfragen. 
	 * @return true = Wenn das FBKonto gelöscht ist, Sonst = flase.
	 */
	public boolean getGeloescht() {
		return geloescht;
	}

	/**
	 * Neuen Wert dem Flag gelöscht zuweisen. 
	 * @param geloescht = Neuer Wert des Flags gelöscht. 
	 */
	public void setGeloescht(boolean geloescht) {
		this.geloescht = geloescht;
	}

	/**
	 * Überlagerte toString-Methode.
	 * @return Wenn Institut vorhanden ist: Bezeichnung, Institutsnummer-Hauptkontonummer-Unterkontonummer. <br>
	 * Sonst : Bezeichnung, Hauptkontonummer-Unterkontonummer.
	 */
	public String toString(){
		if(institut != null)
			return bezeichnung + ", " + institut.getKostenstelle()  + "-" + hauptkonto +  "-" + unterkonto;
		else
			return bezeichnung + ", " + hauptkonto +  "-" + unterkonto;
	}
	
	/**
	 * Ein Kopie von einem FBUnterkonto erstellen. Der Institut wird nicht kopiert.
	 * @return ein kopiertes FBUnterkonto
	 */
	public Object cloneWhole() {
		return new FBUnterkonto( this.getId(), this.getHaushaltsJahrID(), null,
									this.getBezeichnung(), this.getHauptkonto(), this.getUnterkonto(), this.getBudget(),
									this.getVormerkungen(), this.getKleinbestellungen(), this.getGeloescht() );
	}

	/**
	 * Ein Kopie von einem FBUnterkonto erstellen. Der Institut wird auch kopiert.
	 * @return ein kopiertes FBUnterkonto
	 */
	public Object clone() {
		return new FBUnterkonto( this.getId(), this.getHaushaltsJahrID(), (Institut)this.getInstitut().clone(),
								this.getBezeichnung(), this.getHauptkonto(), this.getUnterkonto(), this.getBudget(),
								this.getVormerkungen(), this.getKleinbestellungen(), this.getGeloescht() );
	}

	/**
	 * Die Kontenzuordnungen des FBKontos abfragen. 
	 * @return Die ZVKonten, denen das FBKonto zugeordnet ist. 
	 */
	public Kontenzuordnung[] getZuordnung() {
		return zuordnung;
	}

	/**
	 * Die Kontenzuordnungen des FBKontos aktualisieren. 
	 * @param zuordnung = Die neuen Kontenzuordnungen. 
	 */
	public void setZuordnung(Kontenzuordnung[] zuordnung) {
		this.zuordnung = zuordnung;
	}

}