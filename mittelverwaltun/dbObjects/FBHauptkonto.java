package dbObjects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author robert *  * Folgendes ausw�hlen, um die Schablone f�r den erstellten Typenkommentar zu �ndern: * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */


public class FBHauptkonto extends FBUnterkonto implements Serializable{

	private float dispoLimit;
	private ArrayList unterkonten = new ArrayList();
	private String pruefung;
	private Kontenzuordnung[] zuordnung;

	
	/**
	 * 
	 */
	public FBHauptkonto( int id, int haushaltsJahrID, Institut inst, String bez, String haupt, String unter,
							float budget, float dispo, String pruefung ){
		super( id, haushaltsJahrID, inst, bez, haupt, unter, budget );
		this.dispoLimit = dispo;
		this.pruefung = pruefung;
	}
	
	/**
	 * 
	 */
	public FBHauptkonto( int id, String bez, Institut inst, String haupt, String unter, float budget, float dispo ){
		super( id, bez, inst, haupt, unter, budget );
		this.dispoLimit = dispo;
	}
	
	public FBHauptkonto( String bez, Institut inst, String haupt, String unter ){
		super(  bez, inst, haupt, unter );
	}
	
	
	/**
	 * Dieser Konstruktor enth�lt alle Attribute
	 */
	public FBHauptkonto( int id, int haushaltsJahrID, Institut inst, String bez, String haupt, String unter,
							float budget, float dispo, String pruefung, boolean geloescht ){
		super( id, haushaltsJahrID, inst, bez, haupt, unter, budget, geloescht );
		this.dispoLimit = dispo;
		this.pruefung = pruefung;
	}
	
	
	/**
	 * Gleichheit von zwei FBHauptkonten
	 * @param hauptkonto 
	 * @return boolean
	 */
	public boolean equals( FBHauptkonto hauptkonto ){
		if( this.getInstitut().getKostenstelle().equalsIgnoreCase( hauptkonto.getInstitut().getKostenstelle() ) &&
											this.getHauptkonto().equalsIgnoreCase( hauptkonto.getHauptkonto() ) &&
											this.getUnterkonto().equalsIgnoreCase( hauptkonto.getUnterkonto() ) )
			return true;
		
		return false;
	}

	
	/**
	 * Das FBHauptkonto aktualisieren.
	 */
	public void setFBHauptkonto( FBHauptkonto konto ) {
		setHaushaltsJahrID( konto.getHaushaltsJahrID() );
		setBezeichnung( konto.getBezeichnung() );
		setHauptkonto( konto.getHauptkonto() );
		setUnterkonto( konto.getUnterkonto() );
		setBudget( konto.getBudget() );
		setDispoLimit( konto.getDispoLimit() );
		setPruefung( konto.getPruefung() );
		setGeloescht( konto.getGeloescht() );
	}
	
	/**
	 * Wenn das Hauptkonto ge�ndert wird dann muss auch bei den Unterkonten das Hauptkonto ge�ndert werden. 
	 */
	public void setHauptkonto( String hauptkonto ) {
		for( int i = 0; i < unterkonten.size(); i++ ) {
			((FBUnterkonto)unterkonten.get(i)).setHauptkonto( hauptkonto );
		}
		
		super.setHauptkonto( hauptkonto );
	}

	/**
	 * Die Unterkonten von dem Hauptkonto ermittteln
	 * @return
	 * 
	 * @uml.property name="unterkonten"
	 */
	public ArrayList getUnterkonten() {
		return unterkonten;
	}

	/**
	 * Die Unterkonten zum Hauptkonto hinzuf�gen 
	 * @param unterkonten
	 * 
	 * @uml.property name="unterkonten"
	 */
	public void setUnterkonten(ArrayList unterkonten) {
		this.unterkonten = unterkonten;
	}

	
	/**
	 * Abfrage ob die Pruefung bis zum einem gewissem Betrag geht
	 * @return boolean
	 */
	public boolean isPruefungBis(){
		if( pruefung == null )
			return false;
		if( pruefung.length() <= 0 )	// Wenn es einen pr�ftring gibt
			return false;
		if( pruefung.charAt(0) == '<' )	// Wenn der erste Buchstabe kleiner-Zeichen ist
			return true;				// dann ist es bis einschliesslich dieser summe
		
		return false;		// alle anderen f�lle liefern false
	}
	
	/**
	 * Abfrage ob die Pr�fung aktiv ist
	 * @return boolean
	 */
	public boolean isPruefungActive(){
		if( pruefung == null )
			return false;
		if( pruefung.length() > 0 )
			return true;
		return false;
	}
	
	/**
	 * R�ckgabe des Pruesumme als String
	 * @return String
	 */ 
	public String getPruefsumme(){
		if( pruefung == null )
			return "0";
		if( pruefung.length() <= 1 )
			return "0";
			
		return pruefung.substring(1);
	}
	
	/**
	 * Neuen Pruefstring erstellen
	 * @param active
	 * @param bis
	 * @param value
	 */
	public void setPruefung( boolean active, boolean bis, Object value ){
		if( !active )
			pruefung = "";
		else if( bis )
			pruefung = "<" + value.toString();
		else
			pruefung = ">" + value.toString();
	}
	
	/**
	 * Neuen Pruefstring erstellen
	 * @param active
	 * @param bis
	 * @param value
	 * @return Pr�fstring
	 */
	public static String getPruefung( boolean active, boolean bis, Object value ){
		if( !active )
			return "";
		
		if( bis )
			return "<" + value.toString();
		else
			return ">" + value.toString();
	}

	/**
	 * 
	 */
	public float getDispoLimit() {
		return dispoLimit;
	}

	/**
	 * 
	 */
	public void setDispoLimit(float dispoLimit) {
		this.dispoLimit = dispoLimit;
	}

	/**
	 * 
	 */
	public String getPruefung() {
		return pruefung;
	}

	/**
	 * 
	 */
	public void setPruefung(String pruefung) {
		this.pruefung = pruefung;
	}

	/**
	 * 
	 */
	public Kontenzuordnung[] getZuordnung() {
		return zuordnung;
	}

	/**
	 * 
	 */
	public void setZuordnung(Kontenzuordnung[] zuordnung) {
		this.zuordnung = zuordnung;
	}

	

	public String toString(){
		return getBezeichnung() + ", " + getInstitut().getKostenstelle()  + "-" + getHauptkonto() +  "-" + getUnterkonto();
	}
	
	public boolean is (FBHauptkonto acc){
		return ( this.getId() == acc.getId() )
				&& ( this.getHaushaltsJahrID()== acc.getHaushaltsJahrID() )
				&& ( this.getInstitut().getId() == acc.getInstitut().getId() )
				&& ( this.getHauptkonto().equalsIgnoreCase(acc.getHauptkonto()) )
				&& (this.getUnterkonto().equalsIgnoreCase(acc.getUnterkonto()));
	}
	
	/**
	 * Ein Kopie von einem FBHauptkonto erstellen. Es werden auch die FBUnterkonten kopiert.
	 * @return ein kopiertes FBHauptkonto
	 */
	public Object cloneWhole() {
		FBHauptkonto result = new FBHauptkonto( this.getId(), this.getHaushaltsJahrID(), null, this.getBezeichnung(),
														this.getHauptkonto(), this.getUnterkonto(), this.getBudget(),
														this.getDispoLimit(), this.getPruefung(), this.getGeloescht() );
		result.setInstitut( (Institut)this.getInstitut().clone() );
		ArrayList hauptkonten = new ArrayList();
		hauptkonten.add( result );
		result.getInstitut().setHauptkonten( hauptkonten );

		ArrayList unterkonten = new ArrayList();
		FBUnterkonto temp = null;
		for( int i = 0; i < this.getUnterkonten().size(); i++ ) {
			unterkonten.add( temp = (FBUnterkonto)((FBUnterkonto)this.getUnterkonten().get(i)).cloneWhole() );
			temp.setInstitut( result.getInstitut() );
		}
		result.setUnterkonten( unterkonten );

		return result;
	}

	/**
	 * Ein Kopie von einem FBHauptkonto erstellen. Die Unterkonten werden nicht kopiert.
	 * @return ein kopiertes FBHauptkonto
	 */
	public Object clone() {
		FBHauptkonto result = new FBHauptkonto( this.getId(), this.getHaushaltsJahrID(), null, this.getBezeichnung(),
														this.getHauptkonto(), this.getUnterkonto(), this.getBudget(),
														this.getDispoLimit(), this.getPruefung(), this.getGeloescht() );
		result.setInstitut( (Institut)this.getInstitut().clone() );
		ArrayList hauptkonten = new ArrayList();
		hauptkonten.add( result );
		result.getInstitut().setHauptkonten( hauptkonten );
		result.setUnterkonten( new ArrayList() );

		return result;
	}

}