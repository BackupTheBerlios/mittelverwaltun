package dbObjects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Die Klasse repräsentiert einen ZVTitel von einem <code>ZVKonto</code>. <br>
 * Ein ZVTitel kann mehrere <code>ZVUntertitel</code> haben und ist nur einem <code>ZVKonto</code> zugeordnet. 
 * @author w.flat
 */
public class ZVTitel extends ZVUntertitel implements Serializable {
	
	/**
	 * <code>ArrayList</code> mit <code>ZVUntertitel</code>n, die dem ZVTitel angehören. 
	 */
	private ArrayList untertitel = new ArrayList();
	
	/**
	 * Das <code>ZVKonto</code>, welchem der ZVTitel angehört. 
	 */
	private ZVKonto zvKonto = null;

	/**
	 * Konstruktor, der alle Attribute vom <code>ZVTitel</code> enthält, außer der Liste mit den ZVUntertiteln.
	 * @param id = Eindeutige Id vom ZVTitel
	 * @param zvKonto = ZVKonto, dem sieser ZVTitel angehört
	 * @param bezeichnung = Bezeichnung des ZVUntertitels
	 * @param titel = Titel vom ZVUntertitel
	 * @param untertitel = Untertitel vom ZVUntertitel
	 * @param budget = Betrag, den dieser ZVUntertitel besitzt
	 * @param vormerkungen = Betrag, der für die Bestellungen vorgemerkt ist, aber noch nicht bezahlt.
	 * @param bemerkung = Bemerkungen für das Konto
	 * @param bedingung = Pruebedingung, ab welcher oder bis welche Summe der ZVUntertitel freigegeben ist
	 * @param geloescht = ob der ZVUntertitel gelöscht ist oder nicht.
	 */
	public ZVTitel( int id, ZVKonto zvKonto, String bezeichnung, String titel, String untertitel,
							float budget, float vormerkungen, String bemerkung, String bedingung, boolean geloescht ){
		super( id, null, bezeichnung, titel, untertitel, budget, vormerkungen, bemerkung, bedingung, geloescht );
		setZVTitel(this);
		this.zvKonto = zvKonto;
	}
	
	/**
	 * Konstruktor, zum Erzeugen eienes ZVTitels mit einer bekannten Id und welcher nicht gelöscht ist.
	 * @param id = Eindeutige Id vom ZVTitel
	 * @param zvKonto = ZVKonto, dem sieser ZVTitel angehört
	 * @param bezeichnung = Bezeichnung des ZVUntertitels
	 * @param titel = Titel vom ZVUntertitel
	 * @param untertitel = Untertitel vom ZVUntertitel
	 * @param budget = Betrag, den dieser ZVUntertitel besitzt
	 * @param bemerkung = Bemerkungen für das Konto
	 * @param bedingung = Pruebedingung, ab welcher oder bis welche Summe der ZVUntertitel freigegeben ist
	 */
	public ZVTitel( int id, ZVKonto zvKonto, String bezeichnung, String titel, String untertitel, float budget, 
																					String bemerkung, String bedingung ){
		super( id, null, bezeichnung, titel, untertitel, budget, bemerkung, bedingung );
		setZVTitel(this);
		this.zvKonto = zvKonto;
	}
	
	/**
	 * Konstruktor, zum Erzeugen eienes ZVTitels mit einer bekannten Id.
	 * @param id = Eindeutige Id vom ZVTitel
	 * @param zvKonto = ZVKonto, dem sieser ZVTitel angehört
	 * @param bezeichnung = Bezeichnung des ZVUntertitels
	 * @param titel = Titel vom ZVUntertitel
	 * @param untertitel = Untertitel vom ZVUntertitel
	 * @param budget = Betrag, den dieser ZVUntertitel besitzt
	 * @param bemerkung = Bemerkungen für das Konto
	 * @param bedingung = Pruebedingung, ab welcher oder bis welche Summe der ZVUntertitel freigegeben ist
	 * @param geloescht = ob der ZVUntertitel gelöscht ist oder nicht.
	 */
	public ZVTitel( int id, ZVKonto zvKonto, String bezeichnung, String titel, String untertitel, float budget, 
																	String bemerkung, String bedingung, boolean geloescht ){
		super( id, null, bezeichnung, titel, untertitel, budget, bemerkung, bedingung, geloescht );
		setZVTitel(this);
		this.zvKonto = zvKonto;
	}
	
	/**
	 * Konstruktor, zum Erzeugen von eienem neuen <code>ZVTitel</code>.
	 * @param bezeichnung = Bezeichnung des ZVUntertitels
	 * @param titel = Titel vom ZVUntertitel
	 * @param bemerkung = Bemerkungen für das Konto
	 */
	public ZVTitel( String bezeichnung, String titel, String bemerkung ){
		super(bezeichnung, "", bemerkung);
		setZVTitel(this);
		this.setTitel( titel );
	}
	
	/**
	 * Abfrage vom <code>ZVKonto</code>, dem der <code>ZVTitel</code> zugeordnet ist. 
	 * @return Das <code>ZVKonto</code>, dem der <code>ZVTitel</code> zugeordnet ist. 
	 */
	public ZVKonto getZVKonto() {
		return zvKonto;
	}
	
	/**
	 * Eine Kopie von einem <code>ZVTitel</code> erstellen. <br>
	 * Es werden keine <code>ZVUntertitel</code> kopiert. <br>
	 * Das <code>ZVKonto</code> wird kopiert.
	 * @return Ein kopierter ZVTitel. 
	 */
	public Object clone() {
		ZVTitel result = new ZVTitel( this.getId(), null, this.getBezeichnung(), this.getTitel(), this.getUntertitel(),
									this.getBudget(), this.getVormerkungen(), this.getBemerkung(), 
									this.getBedingung(), this.getGeloescht() );
		
		result.setZVKonto( (ZVKonto)(this.zvKonto.clone()) );
		result.setSubUntertitel( new ArrayList() );
		
		return result;
	}
	
	/**
	 * Eine Kopie von einem <code>ZVTitel</code> erstellen. <br>
	 * Es werden auch alle <code>ZVUntertitel</code> kopiert. <br>
	 * Das <code>ZVKonto</code> wird nicht kopiert.
	 * @return Ein kopierter ZVTitel. 
	 */
	public Object cloneWhole() {
		ZVTitel result = new ZVTitel( this.getId(), null, this.getBezeichnung(), this.getTitel(), this.getUntertitel(),
									this.getBudget(), this.getVormerkungen(), this.getBemerkung(), 
									this.getBedingung(), this.getGeloescht() );
		ArrayList unter = new ArrayList();
		ZVUntertitel temp = null;
		for( int i = 0; i < this.getSubUntertitel().size(); i++ ) {
			unter.add( temp = (ZVUntertitel)((ZVUntertitel)this.getSubUntertitel().get(i)).cloneWhole() );
			temp.setZVTitel( result );
		}
		result.setSubUntertitel( unter );
		
		return result;
	}
	
	/**
	 * Gleichheit von zwei <code>ZVTitel</code>n. <br>
	 * Zwei ZVTitel sind gleich, wenn der das Kapitel, Titel und Untertitel gleich sind.
	 * @param zvTitel = Der zweite ZVTitel, mit dem die Gleichheit überprüft werden soll. 
	 * @return true = wenn die ZVTitel gleich sind, sonst = false
	 */
	public boolean equals( ZVTitel zvTitel ) {
		if(zvTitel == null)
			return false;
		if(zvTitel.getClass().getName().equalsIgnoreCase(this.getClass().getName())) {
			if( this.zvKonto.getKapitel().equalsIgnoreCase( zvTitel.getZVKonto().getKapitel() ) &&
											this.getTitel().equalsIgnoreCase( zvTitel.getTitel() ) &&
											this.getUntertitel().equalsIgnoreCase( zvTitel.getUntertitel() ) )
				return true;
		}
		return false;
	}
	
	/**
	 * Neues ZVKonto dem ZVTitel zuweisen. 
	 * @param zvKonto = Neues ZVKonto, dem der ZVTitel zugeordnet ist. 
	 */
	public void setZVKonto( ZVKonto zvKonto ) {
		this.zvKonto = zvKonto;
	}
	
	/**
	 * <code>ArrayList</code> mit allen Untertiteln des ZVTitels abfragen.  
	 * @return <code>ArrayList</code> mit allen vorhandenen ZVUntertiteln.
	 */
	public ArrayList getAllUntertitel() {
		return untertitel;
	}
	
	/**
	 * Überlagerung der toString-Methode. <br>
	 * Es wird folgendes Format ausgegeben : Bezeichnung, Kapitel/Titel
	 * @return String der den ZVTitel repräsentiert. 
	 */
	public String toString() {
		return getBezeichnung() + ", " + zvKonto.getKapitel() + "/" + getTitel();
	}
	
	/**
	 * Den ZVTitel aktualisieren. Id und ZVKonto werden nicht aktualisiert.
	 * @param zvTitel = ZVTitel von dem die Änderungen übernommen werden. 
	 */
	public void setZVTitel( ZVTitel zvTitel ) {
		setBezeichnung( zvTitel.getBezeichnung() );
		setTitel( zvTitel.getTitel() );
		setUntertitel( zvTitel.getUntertitel() );
		setBudget( zvTitel.getBudget() );
		setVormerkungen( zvTitel.getVormerkungen() );
		setBemerkung( zvTitel.getBemerkung() );
		setBedingung( zvTitel.getBedingung() );
		setGeloescht( zvTitel.getGeloescht() );
	}
	
	/**
	 * Ersetzen der Titelgruppe bei einem TGR-Konto bedeutet Ersetzen auch bei den Titeln.
	 * Es wird auch bei den Untertiteln ersetzt.
	 * @param tgr = Neuer Wert für die Titelgruppe. 
	 */
	public void setNewTGR( String tgr ) {
		setTitel( this.getKategorie() + tgr );
		
		for( int i = 0; i < untertitel.size(); i++ ) {
			((ZVUntertitel)untertitel.get(i)).setNewTGR( tgr );
		}
	}

	/**
	 * Alle ZVUntertitel des ZVTitels abfragen. 
	 * @return <code>ArrayList</code> mit allen ZVUntertiteln, die dem ZVTitel zugeordnet ist. 
	 */
	public ArrayList getSubUntertitel() {
		return untertitel;
	}
	
	/**
	 * Liste mit ZVUntertiteln dem ZVTitel zuweisen. 
	 * @param untertitel = Liste mit den ZVUntertiteln, die dem ZVTitel zugeordnet sind. 
	 */
	public void setSubUntertitel(ArrayList untertitel) {
		this.untertitel = untertitel;
	}

}
