package dbObjects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author robert *  * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern: * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */


public class FBHauptkonto extends FBUnterkonto implements Serializable {

	/**
	 * DispositionsKredit, den ein FBHauptkonto verwenden kann. 
	 */
	private float dispoLimit;
	
	/**
	 * Eine Liste von allen FBUnterkonten, die ein FBHauptkonto hat.
	 */
	private ArrayList unterkonten = new ArrayList();
	
	/**
	 * Prüfbedingung, ob ein Konto nur bis zu einem bestimmten Betrag verwendet werden darf <br>
	 * oder ab einem bestimmten Betrag. Wenn der String leer ist, dann keine Pruefbedingung <br>
	 * für dieses Konto. 
	 */
	private String pruefung;
	
	/**
	 * Kontenzuordnungen. Welchen ZVKonten ist ein FBHauptkonto zugeordnet.
	 */
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
	 * 
	 */
	public FBHauptkonto( int id, int haushaltsJahrID, Institut inst, String bez, String haupt, String unter,
							float budget, float dispo, String pruefung, boolean geloescht ){
		super( id, haushaltsJahrID, inst, bez, haupt, unter, budget, geloescht );
		this.dispoLimit = dispo;
		this.pruefung = pruefung;
	}
	
	/**
	 * Konstruktor, welcher alle Attribute enthält. <br>
	 * @param id = FBHauptkontoId
	 * @param haushaltsJahrID = HaushaltsJahrID
	 * @param inst = Institut, welchem das Konto zugeordnet ist.
	 * @param bez = Bezeichnung des Kontos
	 * @param haupt = Nummer des Hauptkontos
	 * @param unter = Nummer des Unterkontos
	 * @param budget = Budget, welches dieses Konto enthält
	 * @param dispo = Dispositionskredit für ein FBHauptkonto
	 * @param vormerkungen = Vorgemerkte Mittel
	 * @param pruefung = Pruefbedingungs-String
	 * @param kleinbestellungen = Ob dieses Konto für Kleinbestellungen verwendet werden kann.
	 * @param geloescht = Flag, ob Konto gelöscht
	 */
	public FBHauptkonto( int id, int haushaltsJahrID, Institut inst, String bez, String haupt, String unter,
				float budget, float dispo, float vormerkungen, String pruefung, boolean kleinbestellungen, boolean geloescht ){
		super( id, haushaltsJahrID, inst, bez, haupt, unter, budget, vormerkungen, kleinbestellungen, geloescht );
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
		setVormerkungen(konto.getVormerkungen());
		setPruefung( konto.getPruefung() );
		setKleinbestellungen(konto.getKleinbestellungen());
		setGeloescht( konto.getGeloescht() );
	}
	
	/**
	 * Wenn das Hauptkonto geändert wird dann muss auch bei den Unterkonten das Hauptkonto geändert werden. 
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
	 */
	public ArrayList getUnterkonten() {
		return unterkonten;
	}

	/**
	 * Die Unterkonten zum Hauptkonto hinzufügen 
	 * @param unterkonten
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
		if( pruefung.length() <= 0 )	// Wenn es einen prüftring gibt
			return false;
		if( pruefung.charAt(0) == '<' )	// Wenn der erste Buchstabe kleiner-Zeichen ist
			return true;				// dann ist es bis einschliesslich dieser summe
		
		return false;		// alle anderen fälle liefern false
	}
	
	/**
	 * Abfrage ob die Prüfung aktiv ist
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
	 * Rückgabe des Pruesumme als String
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
	 * @return Prüfstring
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
												this.getDispoLimit(), this.getVormerkungen(),
												this.getPruefung(), this.getKleinbestellungen(), this.getGeloescht() );
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
												this.getDispoLimit(), this.getVormerkungen(), this.getPruefung(),
												this.getKleinbestellungen(), this.getGeloescht() );
		result.setInstitut( (Institut)this.getInstitut().clone() );
		ArrayList hauptkonten = new ArrayList();
		hauptkonten.add( result );
		result.getInstitut().setHauptkonten( hauptkonten );
		result.setUnterkonten( new ArrayList() );

		return result;
	}

}