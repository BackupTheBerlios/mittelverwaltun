package dbObjects;

import java.io.Serializable;

/**
 * @author robert *  * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern: * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 * 
 */


public class FBUnterkonto implements Serializable{

	/**
	 * 
	 */
	private int id;

	/**
	 * 
	 */
	private Institut institut;



	private String hauptkonto;

	/**
	 * 
	 */
	private String unterkonto;

	/**
	 * 
	 */
	private String bezeichnung;

	/**
	 * 
	 */
	private float budget;

	/**
	 * 
	 */
	private boolean geloescht;

	
	private int haushaltsJahrID;
		
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
	 * Gleichheit von zwei FBUnterkonten
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
		setGeloescht( konto.getGeloescht() );
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
												this.getBezeichnung(), this.getHauptkonto(), this.getUnterkonto(),
												this.getBudget(), this.getGeloescht() );
	}

	/**
	 * Ein Kopie von einem FBUnterkonto erstellen. Der Institut wird auch kopiert.
	 * @return ein kopiertes FBUnterkonto
	 */
	public Object clone() {
		return new FBUnterkonto( this.getId(), this.getHaushaltsJahrID(), (Institut)this.getInstitut().clone(),
												this.getBezeichnung(), this.getHauptkonto(), this.getUnterkonto(),
												this.getBudget(), this.getGeloescht() );
	}

}