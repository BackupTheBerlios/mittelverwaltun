package dbObjects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Diese Klasse repräsentiert ein Fachbereichs-Hauptkonto. <br>
 * Ein FBHauptkonto ist immer einem Institut zugeordnet. <br>
 * Das Hauptkonto ist eine 2.stellige Nummer, dass das FBHauptkonto innerhalb eines Instituts eindeutig identifiziert. <br>
 * Die Unterkonto-Nummer eines FBHaupkontos ist immer "0000". <br>
 * Das FBHauptkonto kann mehrere FBUnterkonten haben. 
 * @author w.flat
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
	 * Flag für den Jahresabschluss - kein entsprechendes Feld in der Datenbank 
	 */
	private boolean portieren = false ;

	/**
	 * Flag für den Jahresabschluss - keine entsprechendes Feld in der Datenbank 
	 */	
	private boolean budgetUebernehmen = false;
	
	/**
	 * Konstruktor, zum Erzeugen von einem FBHauptkonto. 
	 * @param id = FBHauptkontoId
	 * @param haushaltsJahrID = HaushaltsJahrID
	 * @param inst = Institut, welchem das Konto zugeordnet ist.
	 * @param bez = Bezeichnung des Kontos
	 * @param haupt = Nummer des Hauptkontos
	 * @param unter = Nummer des Unterkontos
	 * @param budget = Budget, welches dieses Konto enthält
	 * @param dispo = Dispositionskredit für ein FBHauptkonto
	 * @param pruefung = Pruefbedingungs-String
	 */
	public FBHauptkonto( int id, int haushaltsJahrID, Institut inst, String bez, String haupt, String unter,
							float budget, float dispo, String pruefung ){
		super( id, haushaltsJahrID, inst, bez, haupt, unter, budget, 0.0f, false, false );
		this.dispoLimit = dispo;
		this.pruefung = pruefung;
		this.unterkonten = null;
	}
	
	/**
	 * Konstruktor, zum Erzeugen von einem FBHauptkonto. 
	 * @param id = FBHauptkontoId
	 * @param bez = Bezeichnung des Kontos
	 * @param inst = Institut, welchem das Konto zugeordnet ist.
	 * @param haupt = Nummer des Hauptkontos
	 * @param unter = Nummer des Unterkontos
	 * @param budget = Budget, welches dieses Konto enthält
	 * @param dispo = Dispositionskredit für ein FBHauptkonto
	 */
	public FBHauptkonto( int id, String bez, Institut inst, String haupt, String unter, float budget, float dispo ){
		super( id, 0, inst, bez, haupt, unter, budget, 0.0f, false, false );
		this.dispoLimit = dispo;
		this.pruefung = null;
		this.unterkonten = null;
	}
	
	/**
	 * Konstruktor, zum Erzeugen von einem neuen FBHauptkonto. 
	 * @param bez = Bezeichnung des Kontos
	 * @param inst = Institut, welchem das Konto zugeordnet ist.
	 * @param haupt = Nummer des Hauptkontos
	 * @param unter = Nummer des Unterkontos
	 */
	public FBHauptkonto( String bez, Institut inst, String haupt, String unter ){
		super(  bez, inst, haupt, unter );
		this.dispoLimit = 0.0f;
		this.pruefung = null;
		this.unterkonten = null;
	}
	
	/**
	 * Konstruktor, zum Erzeugen von einem neuen FBHauptkonto. 
	 * @param bez = Bezeichnung des Kontos
	 * @param inst = Institut, welchem das Konto zugeordnet ist.
	 * @param haupt = Nummer des Hauptkontos
	 */
	public FBHauptkonto(int id, String bez, Institut inst, String haupt ){
		super(id,  bez, inst, haupt, "0000" );
		this.dispoLimit = 0.0f;
		this.pruefung = null;
		this.unterkonten = null;
	}
	/**
	 * Konstruktor, zum Erzeugen von einem FBHauptkonto. 
	 * @param id = FBHauptkontoId
	 * @param haushaltsJahrID = HaushaltsJahrID
	 * @param inst = Institut, welchem das Konto zugeordnet ist.
	 * @param bez = Bezeichnung des Kontos
	 * @param haupt = Nummer des Hauptkontos
	 * @param unter = Nummer des Unterkontos
	 * @param budget = Budget, welches dieses Konto enthält
	 * @param dispo = Dispositionskredit für ein FBHauptkonto
	 * @param pruefung = Pruefbedingungs-String
	 * @param geloescht = Flag, ob Konto gelöscht
	 */
	public FBHauptkonto( int id, int haushaltsJahrID, Institut inst, String bez, String haupt, String unter,
							float budget, float dispo, String pruefung, boolean geloescht ){
		super( id, haushaltsJahrID, inst, bez, haupt, unter, budget, 0.0f, false, geloescht );
		this.dispoLimit = dispo;
		this.pruefung = pruefung;
		this.unterkonten = null;
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
		this.unterkonten = null;
	}
	
	
	/**
	 * Gleichheit von zwei FBHauptkonten. Zwei Hauptkonten sind gleich, <br>
	 * wenn die Institutsnummer, Hauptkontonummer und Unterkontonummer übereinstimmen.
	 * @param hauptkonto = Das zweite FBHauptkonto. 
	 * @return true = Wenn die beiden Konsten identisch sind, sont = false.
	 */
	public boolean equals( FBHauptkonto hauptkonto ){
		if(hauptkonto == null)
			return false;
		if(!this.getClass().getName().equalsIgnoreCase(hauptkonto.getClass().getName()))
			return false;
		if( this.getInstitut().getKostenstelle().equalsIgnoreCase( hauptkonto.getInstitut().getKostenstelle() ) &&
											this.getHauptkonto().equalsIgnoreCase( hauptkonto.getHauptkonto() ) &&
											this.getUnterkonto().equalsIgnoreCase( hauptkonto.getUnterkonto() ) )
			return true;
		
		return false;
	}

	
	/**
	 * Das FBHauptkonto aktualisieren.
	 * @param konto = FBHauptkonto von dem die Änderungen übernommen werden. 
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
	 * Wenn die Hauptkontonummer geändert wird dann muss auch <br>
	 * bei den Unterkonten die Hauptkontonummer geändert werden. 
	 * @param hauptkonto = Der Neue Wert der Hauptkontonummer. 
	 */
	public void setHauptkonto( String hauptkonto ) {
		for( int i = 0; i < unterkonten.size(); i++ ) {
			((FBUnterkonto)unterkonten.get(i)).setHauptkonto( hauptkonto );
		}
		
		super.setHauptkonto( hauptkonto );
	}

	/**
	 * Die Unterkonten von dem Hauptkonto ermittteln. 
	 * @return <code>ArrayList</code> mit allen Unterkonten.
	 */
	public ArrayList getUnterkonten() {
		return unterkonten;
	}

	/**
	 * Die Unterkonten zum Hauptkonto hinzufügen.  
	 * @param unterkonten = Die Neuen Unterkonten des FBHauptkontos. 
	 */
	public void setUnterkonten(ArrayList unterkonten) {
		this.unterkonten = unterkonten;
	}

	
	/**
	 * Abfrage ob die Pruefung bis zum einem gewissem Betrag geht
	 * @return true = Wenn die Prüfung bis zu einem bestimmten Betrag geht, sonst = false.
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
	 * Abfrage ob die Prüfung aktiv ist. 
	 * @return true = Wenn die Prüfung aktiviert ist, sonst = false.
	 */
	public boolean isPruefungActive(){
		if( pruefung == null )
			return false;
		if( pruefung.length() > 0 )
			return true;
		return false;
	}
	
	/**
	 * Rückgabe des Prüfsumme als String. 
	 * @return Die Prüfsumme als String. 
	 */ 
	public String getPruefsumme(){
		if( pruefung == null )
			return "0";
		if( pruefung.length() <= 1 )
			return "0";
			
		return pruefung.substring(1);
	}
	
	/**
	 * Neuen Prüfstring vom FBHauptkonto erstellen. 
	 * @param active = Ist Prüfbedingung aktiviert oder nicht. 
	 * @param bis = Geht die Prüfbedingung bis zu einem bestimmten Betrag oder ob einem bestimmten Betrag. 
	 * @param value = Der Betrag der Prüfbedingung.
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
	 * Neuen Prüfstring erstellen.
	 * @param active = Ist Prüfbedingung aktiviert oder nicht. 
	 * @param bis = Geht die Prüfbedingung bis zu einem bestimmten Betrag oder ob einem bestimmten Betrag. 
	 * @param value = Der Betrag der Prüfbedingung.
	 * @return Prüfstring, der erstellt wurde. 
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
	 * Den Dispositionskredit des FBHauptkontos abfragen. 
	 * @return Der aktuelle Dispositionskredit. 
	 */
	public float getDispoLimit() {
		return dispoLimit;
	}

	/**
	 * Neue Summe vom Dispositionskredit dem FBHauptkonto zuweisen.
	 * @param dispoLimit = Neuer Wert des DispoLimits.
	 */
	public void setDispoLimit(float dispoLimit) {
		this.dispoLimit = dispoLimit;
	}

	/**
	 * Den Prüfstring vom FBHauptkonto abfragen. 
	 * @return Der aktuelle Prüfstring vom FBHauptkonto. 
	 */
	public String getPruefung() {
		return pruefung;
	}

	/**
	 * Neuen Prüfstring dem FBHauptkonto zuweisen. 
	 * @param pruefung = Neuer Prüfstring. 
	 */
	public void setPruefung(String pruefung) {
		this.pruefung = pruefung;
	}

	/**
	 * Überlagerte toString-Methode.
	 * @return Bezeichnung, Institutsnummer-Hauptkontonummer-Unterkontonummer.
	 */
	public String toString(){
		return getBezeichnung() + ", " + getInstitut().getKostenstelle()  + "-" + getHauptkonto() +  "-" + getUnterkonto();
	}
	
	/**
	 * Abfrage ob das FBHauptkonto und das angegebene FBHauptkonto gleich sind. 
	 * @param acc = Das angegebene FBHauptkonto.
	 * @return true = Wenn beide FBHauptkonten gleich sind, sonst = false.
	 */
	public boolean is(FBHauptkonto acc){
		return ( this.getId() == acc.getId() )
				&& ( this.getHaushaltsJahrID()== acc.getHaushaltsJahrID() )
				&& ( this.getInstitut().getId() == acc.getInstitut().getId() )
				&& ( this.getHauptkonto().equalsIgnoreCase(acc.getHauptkonto()) )
				&& (this.getUnterkonto().equalsIgnoreCase(acc.getUnterkonto()));
	}

	/**
	 * Ein Kopie von einem FBHauptkonto erstellen. Es werden auch die FBUnterkonten kopiert.
	 * @return Ein kopiertes FBHauptkonto. 
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
	 * @return Ein kopiertes FBHauptkonto. 
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

	public boolean getPortieren(){
		return portieren;
	}
	
	public void setPortieren(boolean portieren){
		this.portieren = portieren;
	}
	
	public boolean getBudgetUebernehmen(){
		return budgetUebernehmen;
	}
	
	public void setBudgetUebernehmen(boolean budgetUebernehmen){
		this.budgetUebernehmen = budgetUebernehmen;
	}
	
	public float getGesamtbudget(){
		float budget = getBudget();
		if (unterkonten != null){
			for (int i=0; i<unterkonten.size(); i++){
				budget += ((FBUnterkonto)unterkonten.get(i)).getBudget();
			}
		}
		return budget;
	}
	
	public float getGesamtvormerkungen(){
		float debit = getVormerkungen();
		if (unterkonten != null){
			for (int i=0; i<unterkonten.size(); i++){
				debit += ((FBUnterkonto)unterkonten.get(i)).getVormerkungen();
			}
		}		
		return debit;		
	}
}