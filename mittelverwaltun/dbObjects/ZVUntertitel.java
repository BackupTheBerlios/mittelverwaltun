package dbObjects;

import java.io.Serializable;


/**
 * Die Klasse repräsentiert einen ZVUntertitel von einem <code>ZVTitel</code>. <br>
 * Ist genau einem <code>ZVTitel</code> zugeordnet. 
 * @author w.flat
 */
public class ZVUntertitel implements Serializable {
	
	/**
	 * Eine eindeutige Id, die von der Datenbank vergeben wird. 
	 */
	private int id;
	
	/**
	 * <code>ZVTitel</code> dem der <code>ZVUntertitel</code> zugeordnet ist. 
	 */
	private ZVTitel zvTitel;
	
	/**
	 * Die Titelnummer des ZVUntertitels. 
	 */
	private String titel;
	
	/**
	 * Die Untertitelnummer des ZVUntertitels. 
	 */
	private String untertitel;
	
	/**
	 * Die Bezeichnung des ZVUntertitels. 
	 */
	private String bezeichnung;
	
	/**
	 * Das aktuelle auf dem ZVUntertitel.
	 */
	private float budget;
	
	/**
	 * Der Teilbetrag des Budget, welches für die Ausgaben vorgemerkt ist. 
	 */
	private float vormerkungen;
	
	/**
	 * Eine Bemerkung für den ZVUntertitel. Z.B. Verwendungszweck von diesem <code>ZVUntertitel</code>. 
	 */
	private String bemerkung;
	
	/**
	 * Prüfbedingung für welche Beträge dieser <code>ZVUntertitel</code> verwendet werden darf. <br>
	 * Wenn der String leer ist, dann existiert keine Prüfbedingung für diesen <code>ZVUntertitel</code>. <br>
	 * Die bedingung setzt sich wie folgt zusammen : [&lt;|&gt;][0..9]+
	 */
	private String bedingung;
	
	/**
	 * Flag, ob dieser <code>ZVUntertitel</code> gelöscht ist oder nicht. 
	 */
	private boolean geloescht;

	/**
	 * Konstruktor, der alle Attribute vom <code>ZVUntertitel</code> enthält.
	 * @param id = Eindeutige Id vom ZVUntertitel
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
	
	/**
	 * Konstruktor, zum Erzeugen vom <code>ZVUntertitel</code> mit bekannter Id und das nicht gelöscht ist.
	 * @param id = Eindeutige Id vom ZVUntertitel
	 * @param zvTitel = ZVTitel, dem sieser ZVUntertitel angehört
	 * @param bezeichnung = Bezeichnung des ZVUntertitels
	 * @param titel = Titel vom ZVUntertitel
	 * @param untertitel = Untertitel vom ZVUntertitel
	 * @param budget = Betrag, den dieser ZVUntertitel besitzt
	 * @param bemerkung = Bemerkungen für das Konto
	 * @param bedingung = Pruebedingung, ab welcher oder bis welche Summe der ZVUntertitel freigegeben ist
	 */
	public ZVUntertitel( int id, ZVTitel zvTitel, String bezeichnung, String titel, String untertitel, float budget, 
																					String bemerkung, String bedingung ){
		this.id = id;
		this.zvTitel = zvTitel;
		this.bezeichnung = bezeichnung;
		this.titel = titel;
		this.untertitel = untertitel;
		this.bemerkung = bemerkung;
		this.budget = budget;
		this.vormerkungen = 0.0f;
		this.bemerkung = bemerkung;
		this.bedingung = bedingung;
		this.geloescht = false;
	}
	
	/**
	 * Konstruktor, zum Erzeugen vom <code>ZVUntertitel</code> mit bekannter Id.
	 * @param id = Eindeutige Id vom ZVUntertitel
	 * @param zvTitel = ZVTitel, dem sieser ZVUntertitel angehört
	 * @param bezeichnung = Bezeichnung des ZVUntertitels
	 * @param titel = Titel vom ZVUntertitel
	 * @param untertitel = Untertitel vom ZVUntertitel
	 * @param budget = Betrag, den dieser ZVUntertitel besitzt
	 * @param bemerkung = Bemerkungen für das Konto
	 * @param bedingung = Pruebedingung, ab welcher oder bis welche Summe der ZVUntertitel freigegeben ist
	 * @param geloescht = ob der ZVUntertitel gelöscht ist oder nicht.
	 */
	public ZVUntertitel( int id, ZVTitel zvTitel, String bezeichnung, String titel, String untertitel, float budget, 
																	String bemerkung, String bedingung, boolean geloescht ){
		this.id = id;
		this.zvTitel = zvTitel;
		this.bezeichnung = bezeichnung;
		this.titel = titel;
		this.untertitel = untertitel;
		this.bemerkung = bemerkung;
		this.budget = budget;
		this.vormerkungen = 0.0f;
		this.bemerkung = bemerkung;
		this.bedingung = bedingung;
		this.geloescht = geloescht;
	}
	
	/**
	 * Konstruktor, zum Erzeugen von einem neuen <code>ZVUntertitel</code>.
	 * @param bezeichnung = Bezeichnung des ZVUntertitels
	 * @param untertitel = Untertitel vom ZVUntertitel
	 * @param bemerkung = Bemerkungen für das Konto
	 */
	public ZVUntertitel( String bezeichnung, String untertitel, String bemerkung ){
		this.id = 0;
		this.zvTitel = null;
		this.bezeichnung = bezeichnung;
		this.titel = "";
		this.untertitel = untertitel;
		this.budget = 0.0f;
		this.vormerkungen = 0.0f;
		this.bemerkung = bemerkung;
		this.bedingung = "";
		this.geloescht = false;
	}
	
	/**
	 * Eine Kopie von einem <code>ZVUntertitel</code> erstellen. <br>
	 * Es wird auch der <code>ZVTitel</code> kopiert.
	 * @return Ein kopierter ZVUntertitel
	 */
	public Object clone() {
		ZVUntertitel result = new ZVUntertitel( this.getId(), null, this.getBezeichnung(), this.getTitel(), this.getUntertitel(),
												this.getBudget(), this.getVormerkungen(), this.getBemerkung(),
												this.getBedingung(), this.getGeloescht() );
		result.setZVTitel( (ZVTitel)(this.zvTitel.clone()) );
		
		return result;
	}
	
	/**
	 * Eine Kopie von einem <code>ZVUntertitel</code> erstellen. <br>
	 * Der <code>ZVTitel</code> wird nicht kopiert.
	 * @return Ein kopiertes ZVUntertitel
	 */
	public Object cloneWhole() {
		ZVUntertitel result = new ZVUntertitel( this.getId(), null, this.getBezeichnung(), this.getTitel(), this.getUntertitel(),
												this.getBudget(), this.getVormerkungen(), this.getBemerkung(), 
												this.getBedingung(), this.getGeloescht() );
		
		return result;
	}
	
	/**
	 * Die toString-Methode wird überlagert. <br>
	 * Ausgabeformat : Bezeichnung, Kapitel/Titel/Untertitel
	 * @return String der den <code>ZVUntertitel</code> repräsentiert. 
	 */
	public String toString() {
		return bezeichnung + ", " + zvTitel.getZVKonto().getKapitel() + "/" + zvTitel.getTitel() + "/" + untertitel;
	}
	
	/**
	 * Den <code>ZVUntertitel</code> aktualisieren. <br>
	 * Id vom <code>ZVUntertitel</code> und der <code>ZVTitel</code> werden nicht aktualisiert.
	 * @param zvUntertitel = ZVUntertitel von dem die Änderungen übernommen werden. 
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
	 * Gleichheit von zwei <code>ZVUntertitel</code>. <br>
	 * Zwei <code>ZVUntertitel</code> sind gleich, wenn 
	 * @param zvUntertitel = Der zweite ZVUntertitel, mit dem die Gleichheit überprüft werden soll. 
	 * @return true = wenn die ZVUntertitel gleich sind, sonst = false. 
	 */
	public boolean equals( ZVUntertitel zvUntertitel ) {
		if(zvUntertitel == null)
			return false;
		if (this.getClass().getName().equalsIgnoreCase(ZVTitel.class.getName())) {
			return((ZVTitel)this).equals((ZVTitel)zvUntertitel);
		} else if(zvUntertitel.getClass().getName().equalsIgnoreCase(this.getClass().getName())){
			if( this.zvTitel.getZVKonto().getKapitel().equalsIgnoreCase( zvUntertitel.getZVTitel().getZVKonto().getKapitel() ) &&
											this.zvTitel.getTitel().equalsIgnoreCase( zvUntertitel.getZVTitel().getTitel() ) &&
											this.getUntertitel().equalsIgnoreCase( zvUntertitel.getUntertitel() ) )
				return true;
		}
		return false;
	}
	
	/**
	 * Ermittlung von der Titelgruppe aus dem Titel. 
	 * @return Die ermittelte Titelgruppe.
	 */
	public String getTGR() {
		if( this.getTitel().length() == 5 )
			return this.getTitel().substring( 3 );		// Die letzten zwei Buchstaben
		else
			return "00";
	}
	
	/**
	 * Ermittlung von der Kategorie aus dem Titel. 
	 * @return Die ermittelte Kategorie.
	 */
	public String getKategorie() {
		if( this.getTitel().length() == 5 )
			return this.getTitel().substring( 0, 3 );	// Die ersten drei Buchstaben
		else
			return "000";
	}
	
	/**
	 * Zuweisen der neuen Titelgruppe bei einem TGR-Konto. 
	 * @param tgr = Neue Titelgruppe für den Untertitel. 
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
	
	/**
	 * Abfrage des ZVTitels, dem der ZVUntertitel zugeordnet ist. 
	 * @return Der Ermittelte ZVTitel. 
	 */
	public ZVTitel getZVTitel() {
		return zvTitel;
	}
	
	/**
	 * Neuen ZVTitel dem ZVUntertitel zuweisen. 
	 * @param zvTitel = Der Neue ZVTitel. 
	 */
	public void setZVTitel(ZVTitel zvTitel) {
		this.zvTitel = zvTitel;
	}
		
	/**
	 * Die Pruefung geht bis einschließlich einem bestimmten Betrag. 
	 * @return true = wenn bis zu einem Betrag, sonst false.
	 */
	public boolean isPruefungBis(){
		if( bedingung == null )
			return false;
		if( bedingung.length() == 0 )	// Wenn es einen prüfstring gibt
			return false;
		if( bedingung.charAt(0) == '<' )	// Wenn der erste Buchstabe kleiner-Zeichen ist
			return true;				// dann ist es bis einschliesslich dieser summe
		
		return false;		// alle anderen fälle liefern false
	}
	
	/**
	 * Die Pruefung geht ab einem gewissen Betrag. 
	 * @return true = wenn ab einem bestimmten Betrag, sonst = false
	 */
	public boolean isPruefungAb(){
		if( bedingung == null )
			return false;
		if( bedingung.length() == 0 )	// Wenn es einen prüftring gibt
			return false;
		if( bedingung.charAt(0) == '>' )	// Wenn der erste Buchstabe grösser-Zeichen ist
			return true;					// dann ist es ab dieser summe
		
		return false;		// alle anderen fälle liefern false
	}
	
	/**
	 * Abfrage ob die Prüfung aktiv ist
	 * @return true = wenn es eine Bedingung gist, sonst = false. 
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
	 * Neuen Prüfstring erstellen und speichern. 
	 * @param active = Flag ob die Prüfbedingung aktiviert ist. 
	 * @param bis = Flag ob die Prüfbedingung bis zu einem bestimmten Betrag geht, oder ab einem bestimmten Betrag.
	 * @param value = Der Betrag.
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
	 * Einen Prüfstring erstellen. 
	 * @param active = Flag ob die Prüfbedingung aktiviert ist. 
	 * @param bis = Flag ob die Prüfbedingung bis zu einem bestimmten Betrag geht, oder ab einem bestimmten Betrag.
	 * @param value = Der Betrag.
	 * @return Der erstellte String. 
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
	 * Die vom ZVUntertitel abfragen. 
	 * @return Die Id vom ZVUntertitel. 
	 */
	public int getId() {
		return id;
	}

	/**
	 * Neue Id dem ZVUntertitel zuweisen. 
	 * @param id = Neue des ZVUntertitels. 
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Abfrage des Titels vom ZVUntertitel. 
	 * @return Der Titel vom ZVUntertitel.
	 */
	public String getTitel() {
		return titel;
	}

	/**
	 * Neuen Titel dem ZVTitel zuweisen. 
	 * @param titel = Neuer Titel für den ZVUntertitel. 
	 */
	public void setTitel(String titel) {
		this.titel = titel;
	}

	/**
	 * Neue Bedingung zuweisen. 
	 * @param bedingung = Neue Bedingung für den ZVUntertitel. 
	 */
	public void setBedingung(String bedingung) {
		this.bedingung = bedingung;
	}

	/**
	 * Die aktuelle Bedingung vom ZVUntertitel abfragen. 
	 * @return Die aktuelle Bedingungvom ZVUntertitel. 
	 */
	public String getBedingung() {
		return bedingung;
	}

	/**
	 * Abfrage des Ubtertitels vom ZVUntertitel. 
	 * @return Der aktuelle Untertitel vom ZVUntertitel. 
	 */
	public String getUntertitel() {
		return untertitel;
	}

	/**
	 * Neuen Untertitel dem ZVUntertitel zuweisen. 
	 * @param untertitel = Neuer Untertitel vom ZVUntertitel. 
	 */
	public void setUntertitel(String untertitel) {
		this.untertitel = untertitel;
	}

	/**
	 * Die aktuelle Bezeichnung vom ZVUntertitel abfragen. 
	 * @param Die aktuelle Bezeichnung des ZVUntertitels. 
	 */
	public String getBezeichnung() {
		return bezeichnung;
	}

	/**
	 * Neue Bezeichnung dem ZVUntertitel zuweisen. 
	 * @param bezeichnung = Neue Bezeichnung des ZVUntertitels. 
	 */
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Das aktuelle Budget des ZVUntertitels abfragen. 
	 * @return Das aktuelle Budget vom ZVUntertitel. 
	 */
	public float getBudget() {
		return budget;
	}

	/**
	 * Neues Budget dem ZVUntertitel zuweisen. 
	 * @param budget = Neues Budget. 
	 */
	public void setBudget(float budget) {
		this.budget = budget;
	}

	/**
	 * Die Bemerkungen vom ZVUntertitel abfragen. 
	 * @return Die Bemerkungen des ZVUntertitels. 
	 */
	public String getBemerkung() {
		return bemerkung;
	}

	/**
	 * Neue Bemerkungen dem ZVUntertitel zuweisen. 
	 * @param bemerkung = Neue Bemerkungen. 
	 */
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	/**
	 * Abfrage ob der ZVUntertitel gelösct ist oder nicht. 
	 * @return true = wenn das Flag gelöscht gesetzt ist, sonst = false.
	 */
	public boolean getGeloescht() {
		return geloescht;
	}

	/**
	 * Neuen Wert dem Flag gelöscht zuweisen. 
	 * @param geloescht = Neuer Wert für das Flag gelöscht. 
	 */
	public void setGeloescht(boolean geloescht) {
		this.geloescht = geloescht;
	}

}
