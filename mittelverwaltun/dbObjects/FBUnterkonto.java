package dbObjects;

import java.io.Serializable;

/**
 * FBUnterkonto. <br> 
 * Diese Klasse repräsentiert ein FB-Unterkonto, welches einem Professor <br> 
 * oder einem Mitarbeiter eines Institus zugeordnet ist. <br>
 * @author w.flat
 */
public class FBUnterkonto implements Serializable{

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
	 * Id des Haushaltsjahres, welchem das FBUnterkonto zugeordnet ist.
	 */
	private int haushaltsJahrID;
	
	/**
	 * Flag zum Anzeogen, ob ein FBHauptkonto für Kleinbestellungen verwendet werden kann.
	 */
	private boolean kleinbestellungen;
	
	
	public FBUnterkonto( int id, int haushaltsJahrID, Institut inst, String bez, String haupt, String unter, float budget ){
		this.id = id;
		this.bezeichnung = bez;
		this.institut = inst;
		this.hauptkonto = haupt;
		this.unterkonto = unter;
		this.budget = budget;
		this.haushaltsJahrID = haushaltsJahrID;
	}
	
	public FBUnterkonto( int id, int haushaltsJahrID, Institut inst, String bez, String haupt,
																		String unter, float budget, boolean geloescht ){
		this.id = id;
		this.bezeichnung = bez;
		this.institut = inst;
		this.hauptkonto = haupt;
		this.unterkonto = unter;
		this.budget = budget;
		this.haushaltsJahrID = haushaltsJahrID;
		this.geloescht = geloescht;
	}

	/**
	 * Konstruktor, welcher alle Attribute enthält. <br>
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
	}

	
	public FBUnterkonto( int id, String bez, Institut inst, String haupt, String unter, float budget ){
		this.id = id;
		this.bezeichnung = bez;
		this.institut = inst;
		this.hauptkonto = haupt;
		this.unterkonto = unter;
		this.budget = budget;
	}
	
	public FBUnterkonto( String bez, Institut inst, String haupt, String unter ){
		this.bezeichnung = bez;
		this.institut = inst;
		this.hauptkonto = haupt;
		this.unterkonto = unter;
	}
	
	
	/**
	 * Gleichheit von zwei FBUnterkonten. <br>
	 * Gleichheit ist gegeben, wenn Institut, Hauptkonto und Unterkonto gleich sind. <br>
	 * @param unterkonto 
	 * @return boolean
	 */
	public boolean equals( FBUnterkonto unterkonto ){
		if( this.getInstitut().getKostenstelle().equalsIgnoreCase( unterkonto.getInstitut().getKostenstelle() ) &&
											this.getHauptkonto().equalsIgnoreCase( unterkonto.getHauptkonto() ) &&
											this.getUnterkonto().equalsIgnoreCase( unterkonto.getUnterkonto() ) )
			return true;
		
		return false;
	}
	
	/**
	 * Das FBHauptkonto aktualisieren.
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
	 * 
	 */
	public boolean getKleinbestellungen() {
		return kleinbestellungen;
	}
	
	/**
	 * 
	 */
	public void setKleinbestellungen(boolean kleinbestellungen) {
		this.kleinbestellungen = kleinbestellungen;
	}
	
	/**
	 * 
	 */
	public float getVormerkungen() {
		return vormerkungen;
	}
	
	/**
	 * 
	 */
	public void setVormerkungen(float vormerkungen) {
		this.vormerkungen = vormerkungen;
	}


	/**
	 * 
	 */
	public int getHaushaltsJahrID() {
		return haushaltsJahrID;
	}

	/**
	 * 
	 */
	public void setHaushaltsJahrID(int haushaltsJahrID) {
		this.haushaltsJahrID = haushaltsJahrID;
	}

	/**
	 * 
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 */
	public Institut getInstitut() {
		return institut;
	}
	
	/**
	 * 
	 */
	public void setInstitut(Institut institut) {
		this.institut = institut;
	}

	/**
	 * 
	 */
	public String getHauptkonto() {
		return hauptkonto;
	}

	/**
	 * 
	 */
	public void setHauptkonto(String hautpkonto) {
		this.hauptkonto = hautpkonto;
	}

	/**
	 * 
	 */
	public String getUnterkonto() {
		return unterkonto;
	}

	/**
	 * 
	 */
	public void setUnterkonto(String unterkonto) {
		this.unterkonto = unterkonto;
	}

	/**
	 * 
	 */
	public String getBezeichnung() {
		return bezeichnung;
	}

	/**
	 * 
	 */
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	/**
	 * 
	 */
	public float getBudget() {
		return budget;
	}

	/**
	 * 
	 */
	public void setBudget(float budget) {
		this.budget = budget;
	}

	/**
	 * 
	 */
	public boolean getGeloescht() {
		return geloescht;
	}

	/**
	 * 
	 */
	public void setGeloescht(boolean geloescht) {
		this.geloescht = geloescht;
	}

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

}