package dbObjects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 */


public class ZVTitel extends ZVUntertitel implements Serializable {
	
	// CHANGE

	private ArrayList untertitel = new ArrayList();
	private ZVKonto zvKonto = null;

	/**
	 * Konstruktor, der alle Attribute vom ZVUntertitel enth�lt.
	 * @param id = Eindeutige Id vom ZVTitel
	 * @param zvKonto = ZVKonto, dem sieser ZVTitel angeh�rt
	 * @param bezeichnung = Bezeichnung des ZVUntertitels
	 * @param titel = Titel vom ZVUntertitel
	 * @param untertitel = Untertitel vom ZVUntertitel
	 * @param budget = Betrag, den dieser ZVUntertitel besitzt
	 * @param vormerkungen = Betrag, der f�r die Bestellungen vorgemerkt ist, aber noch nicht bezahlt.
	 * @param bemerkung = Bemerkungen f�r das Konto
	 * @param bedingung = Pruebedingung, ab welcher oder bis welche Summe der ZVUntertitel freigegeben ist
	 * @param geloescht = ob der ZVUntertitel gel�scht ist oder nicht.
	 */
	public ZVTitel( int id, ZVKonto zvKonto, String bezeichnung, String titel, String untertitel,
							float budget, float vormerkungen, String bemerkung, String bedingung, boolean geloescht ){
		super( id, null, bezeichnung, titel, untertitel, budget, vormerkungen, bemerkung, bedingung, geloescht );
		this.zvKonto = zvKonto;
	}
	
	public ZVTitel( int id, ZVKonto zvKonto, String bezeichnung, String titel, String untertitel, float budget, 
																					String bemerkung, String bedingung ){
		super( id, null, bezeichnung, titel, untertitel, budget, bemerkung, bedingung );
		this.zvKonto = zvKonto;
	}
	
	public ZVTitel( int id, ZVKonto zvKonto, String bezeichnung, String titel, String untertitel, float budget, 
																	String bemerkung, String bedingung, boolean geloescht ){
		super( id, null, bezeichnung, titel, untertitel, budget, bemerkung, bedingung, geloescht );
		this.zvKonto = zvKonto;
	}
	
	public ZVTitel( String bezeichnung, String titel, String bemerkung ){
		this.setBezeichnung( bezeichnung );
		this.setTitel( titel );
		this.setBemerkung( bemerkung );
	}
	
	public ZVKonto getZVKonto() {
		return zvKonto;
	}
	
	/**
	 * Eine Kopie von einem ZVTitel erstellen. Es werden keine ZVUntertitel kopiert. ZVKonto wird kopiert.
	 * @return ein kopiertes ZVTitel
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
	 * Eine Kopie von einem ZVTitel erstellen. Es werden auch ZVUntertitel kopiert. ZVKonto wird nicht kopiert.
	 * @return ein kopiertes ZVTitel
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
	 * Gleichheit von zwei ZVTitel.
	 * @param zvTitel
	 * @return boolean
	 */
	public boolean equals( ZVTitel zvTitel ) {
		if( this.zvKonto.getKapitel().equalsIgnoreCase( zvTitel.getZVKonto().getKapitel() ) &&
			this.getTitel().equalsIgnoreCase( zvTitel.getTitel() ) &&
			this.getUntertitel().equalsIgnoreCase( zvTitel.getUntertitel() ) )
			return true;
		else
			return false;
	}
	
	public void setZVKonto( ZVKonto zvKonto ) {
		this.zvKonto = zvKonto;
	}
	
	public ArrayList getAllUntertitel() {
		return untertitel;
	}
	
	public String toString() {
		return getBezeichnung() + ", " + zvKonto.getKapitel() + "/" + getTitel();
	}
	
	/**
	 * Den ZVTitel aktualisieren. Id und ZVKonto werden nicht aktualisiert.
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
	 */
	public void setNewTGR( String tgr ) {
		setTitel( this.getKategorie() + tgr );
		
		for( int i = 0; i < untertitel.size(); i++ ) {
			((ZVUntertitel)untertitel.get(i)).setNewTGR( tgr );
		}
	}
	
	public float getBudgetsumme() {
		return 0;
	}
	
	public boolean getHatUntertitel() {
		if( this.untertitel == null )
			return false;
		if( this.untertitel.size() == 0 )
			return false;
		
		return true;
	}

	public ArrayList getSubUntertitel() {
		return untertitel;
	}

	public void setSubUntertitel(ArrayList untertitel) {
		this.untertitel = untertitel;
	}

}
