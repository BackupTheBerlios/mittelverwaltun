package dbObjects;

import java.io.Serializable;


public class ZVUntertitel implements Serializable {
	

	private int id;
	private ZVTitel zvTitel;
	private String titel;
	private String untertitel;
	private String bezeichnung;
	private float budget;
	private float vormerkungen;
	private String bemerkung;
	private String bedingung;
	private boolean geloescht;

	
	public ZVUntertitel() {
	}
	
	/**
	 * Konstruktor, der alle Attribute vom ZVUntertitel enthält.
	 * @param id = Eindeutige Id vom ZVTitel
	 * @param zvTitel = ZVTitel, dem sieser ZVUntertitel angehört
	 * @param bezeichnung = Bezeichnung des ZVUntertitels
	 * @param titel = Titel vom ZVUntertitel
	 * @param untertitel = Untertitel vom ZVUntertitel
	 * @param budget = Betrag, den dieser ZVUntertitel besitzt
	 * @param vormerkungen = Betrag, der für die Bestellungen vorgemerkt ist, aber noch nicht bezahlt.
	 * @param bemerkung = Bemerkungen für das Konto
	 * @param bedingung = Pruebedingung, ab welcher oder bis welche Summe der ZVUntertitel freigegeben ist
	 * @param geloescht = ob der ZVUntertitel gelöscht ist oder nicht.
	 */
	public ZVUntertitel( int id, ZVTitel zvTitel, String bezeichnung, String titel, String untertitel, 
								float budget, float vormerkungen, String bemerkung, String bedingung, boolean geloescht ){
		this.id = id;
		this.zvTitel = zvTitel;
		this.bezeichnung = bezeichnung;
		this.titel = titel;
		this.untertitel = untertitel;
		this.budget = budget;
		this.vormerkungen = vormerkungen;
		this.bemerkung = bemerkung;
		this.bedingung = bedingung;
		this.geloescht = geloescht;
	}
	
	public ZVUntertitel( int id, ZVTitel zvTitel, String bezeichnung, String titel, String untertitel, float budget, 
																					String bemerkung, String bedingung ){
		this.id = id;
		this.zvTitel = zvTitel;
		this.bezeichnung = bezeichnung;
		this.titel = titel;
		this.untertitel = untertitel;
		this.bemerkung = bemerkung;
		this.budget = budget;
		this.bemerkung = bemerkung;
		this.bedingung = bedingung;
	}
	
	public ZVUntertitel( int id, ZVTitel zvTitel, String bezeichnung, String titel, String untertitel, float budget, 
																	String bemerkung, String bedingung, boolean geloescht ){
		this.id = id;
		this.zvTitel = zvTitel;
		this.bezeichnung = bezeichnung;
		this.titel = titel;
		this.untertitel = untertitel;
		this.bemerkung = bemerkung;
		this.budget = budget;
		this.bemerkung = bemerkung;
		this.bedingung = bedingung;
		this.geloescht = geloescht;
	}
	
	public ZVUntertitel( String bezeichnung, String untertitel, String bemerkung ){
		this.setBezeichnung( bezeichnung );
		this.setUntertitel( untertitel );
		this.setBemerkung( bemerkung );
		this.budget = 0;
	}
	
	/**
	 * Eine Kopie von einem ZVUntertitel erstellen. Es wird auch ZVTitel kopiert.
	 * @return ein kopiertes ZVUntertitel
	 */
	public Object clone() {
		ZVUntertitel result = new ZVUntertitel( this.getId(), null, this.getBezeichnung(), this.getTitel(), this.getUntertitel(),
												this.getBudget(), this.getVormerkungen(), this.getBemerkung(),
												this.getBedingung(), this.getGeloescht() );
		result.setZVTitel( (ZVTitel)(this.zvTitel.clone()) );
		
		return result;
	}
	
	/**
	 * Eine Kopie von einem ZVUntertitel erstellen. ZVTitel wird nicht kopiert.
	 * @return ein kopiertes ZVUntertitel
	 */
	public Object cloneWhole() {
		ZVUntertitel result = new ZVUntertitel( this.getId(), null, this.getBezeichnung(), this.getTitel(), this.getUntertitel(),
												this.getBudget(), this.getVormerkungen(), this.getBemerkung(), 
												this.getBedingung(), this.getGeloescht() );
		
		return result;
	}
	
	public String toString() {
		return bezeichnung + ", " + zvTitel.getZVKonto().getKapitel() + "/" + zvTitel.getTitel() + "/" + untertitel;
	}
	
	/**
	 * Den ZVTitel aktualisieren. Id und der ZVTitel werden nicht aktualisiert.
	 */
	public void setZVUntertitel( ZVUntertitel zvUntertitel ) {
		setBezeichnung( zvUntertitel.getBezeichnung() );
		setTitel( zvUntertitel.getTitel() );
		setUntertitel( zvUntertitel.getUntertitel() );
		setBudget( zvUntertitel.getBudget() );
		setVormerkungen(zvUntertitel.getVormerkungen());
		setBemerkung( zvUntertitel.getBemerkung() );
		setBedingung( zvUntertitel.getBedingung() );
		setGeloescht( zvUntertitel.getGeloescht() );
	}
	
	/**
	 * Gleichheit von zwei ZVUntertitel
	 * @param zvUntertitel
	 * @return boolean
	 */
	public boolean equals( ZVUntertitel zvUntertitel ) {
		
		if(zvUntertitel.getClass().getName().equalsIgnoreCase(this.getClass().getName()))
			if (this.getClass().getName().equalsIgnoreCase(ZVTitel.class.getName())){
				return((ZVTitel)this).equals((ZVTitel)zvUntertitel);
			}else{
				if( this.zvTitel.getZVKonto().getKapitel().equalsIgnoreCase( zvUntertitel.getZVTitel().getZVKonto().getKapitel() ) &&
						this.zvTitel.getTitel().equalsIgnoreCase( zvUntertitel.getZVTitel().getTitel() ) &&
						this.getUntertitel().equalsIgnoreCase( zvUntertitel.getUntertitel() ) )
						return true;
					else
						return false;
			}
		else return false;
	}
	
	/**
	 * Ermittlung von der Titelgruppe aus dem Titel
	 */
	public String getTGR() {
		if( this.getTitel().length() == 5 )
			return this.getTitel().substring( 3 );		// Die letzten zwei Buchstaben
		else
			return "00";
	}
	
	/**
	 * Ermittlung von der Kategorie aus dem Titel
	 */
	public String getKategorie() {
		if( this.getTitel().length() == 5 )
			return this.getTitel().substring( 0, 3 );	// Die ersten drei Buchstaben
		else
			return "000";
	}
	
	/**
	 * Ersetzen der Titelgruppe bei einem TGR-Konto
	 */
	public void setNewTGR( String tgr ) {
		setTitel( this.getKategorie() + tgr );
	}
	
	/**
	 * Vormerkungen des ZVUntertitels ermitteln.
	 * @return Vormerkungen eines ZVUntertitels.
	 */
	public float getVormerkungen() {
		return vormerkungen;
	}
	
	/**
	 * Neuen Betrag für Vormerkungen festlegen.
	 * @param vormerkungen = Neue Betrag für Vormerkungen.
	 */
	public void setVormerkungen(float vormerkungen) {
		this.vormerkungen = vormerkungen;
	}
	
	public ZVTitel getZVTitel() {
		return zvTitel;
	}

	public void setZVTitel(ZVTitel zvTitel) {
		this.zvTitel = zvTitel;
	}
		
	/**
	 * Die Pruefung geht bis zum einem gewissem Betrag
	 */
	public boolean isPruefungBis(){
		if( bedingung == null )
			return false;
		if( bedingung.length() <= 0 )	// Wenn es einen prüfstring gibt
			return false;
		if( bedingung.charAt(0) == '<' )	// Wenn der erste Buchstabe kleiner-Zeichen ist
			return true;				// dann ist es bis einschliesslich dieser summe
		
		return false;		// alle anderen fälle liefern false
	}
	
	/**
	 * Die Pruefung geht ab einem gewissen Betrag
	 */
	public boolean isPruefungAb(){
		if( bedingung == null )
			return false;
		if( bedingung.length() <= 0 )	// Wenn es einen prüftring gibt
			return false;
		if( bedingung.charAt(0) == '>' )	// Wenn der erste Buchstabe grösser-Zeichen ist
			return true;				// dann ist es ab dieser summe
		
		return false;		// alle anderen fälle liefern false
	}
	
	/**
	 * Abfrage ob die Prüfung aktiv ist
	 * @return boolean
	 */
	public boolean isPruefungActive(){
		if( bedingung == null )
			return false;
		if( bedingung.length() == 0 )
			return false;
		
		return true;
	}
	
	/**
	 * Rückgabe des Pruesumme als String
	 * @return Prüfsummestring
	 */ 
	public String getPruefsumme(){
		if( bedingung == null )
			return "0";
		if( bedingung.length() <= 1 )
			return "0";
			
		return bedingung.substring(1);
	}
	
	/**
	 * Neuen Prüfstring erstellen
	 * @param active
	 * @param bis
	 * @param value
	 */
	public void setPruefung( boolean active, boolean bis, Object value ){
		if( !active )
			bedingung = "";
		else if( bis )
			bedingung = "<" + value.toString();
		else
			bedingung = ">" + value.toString();
	}
	
	/**
	 * Neuen Prüfstring erstellen
	 * @param active
	 * @param bis
	 * @param value
	 * @return Prüfung
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
	public String getTitel() {
		return titel;
	}

	/**
	 * 
	 */
	public void setBedingung(String bedingung) {
		this.bedingung = bedingung;
	}

	/**
	 * 
	 */
	public String getBedingung() {
		return bedingung;
	}

	/**
	 * 
	 */
	public void setTitel(String titel) {
		this.titel = titel;
	}

	/**
	 * 
	 */
	public String getUntertitel() {
		return untertitel;
	}

	/**
	 * 
	 */
	public void setUntertitel(String untertitel) {
		this.untertitel = untertitel;
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
	public String getBemerkung() {
		return bemerkung;
	}

	/**
	 * 
	 */
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
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

}
