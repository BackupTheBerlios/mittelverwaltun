package dbObjects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 */


public class ZVKonto implements Serializable {

	private int id;
	private int haushaltsJahrId;
	private String kapitel;
	private String titelgruppe;
	private String bezeichnung;
	private boolean zweckgebunden;
	private short uebernahmeStatus;
	private float dispoLimit;
	private boolean geloescht;
	private char freigegeben;
	private ArrayList titel = new ArrayList();
	private float tgrBudget;

	public ZVKonto( int id, int haushaltsJahrId, String bezeichnung, String kapitel, String titelgruppe, float tgrBudget,
					float dispoLimit, boolean zweckgebunden, char freigegeben, short uebernahmeStatus ) {
		this.id = id;
		this.haushaltsJahrId = haushaltsJahrId;
		this.bezeichnung = bezeichnung;
		this.kapitel = kapitel;
		this.titelgruppe = titelgruppe;
		this.tgrBudget = tgrBudget;
		this.dispoLimit = dispoLimit;
		this.zweckgebunden = zweckgebunden;
		this.freigegeben = freigegeben;
		this.uebernahmeStatus = uebernahmeStatus;
	}
	
	public ZVKonto( int id, int haushaltsJahrId, String bezeichnung, String kapitel, String titelgruppe, float tgrBudget,
					float dispoLimit, boolean zweckgebunden, char freigegeben, short uebernahmeStatus, boolean geloescht ) {
		this.id = id;
		this.haushaltsJahrId = haushaltsJahrId;
		this.bezeichnung = bezeichnung;
		this.kapitel = kapitel;
		this.titelgruppe = titelgruppe;
		this.tgrBudget = tgrBudget;
		this.dispoLimit = dispoLimit;
		this.zweckgebunden = zweckgebunden;
		this.freigegeben = freigegeben;
		this.uebernahmeStatus = uebernahmeStatus;
		this.geloescht = geloescht;
	}
	
	public ZVKonto(String bezeichnung, String kapitel, String titelgruppe, float dispo ) {
		this.bezeichnung = bezeichnung;
		this.kapitel = kapitel;
		this.titelgruppe = titelgruppe;
		this.dispoLimit = dispo;
		this.tgrBudget = 0;
	}
	
	public ZVKonto(int id, String bezeichnung, String kapitel, String titelgruppe, boolean zweckgebunden){
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.kapitel = kapitel;
		this.titelgruppe = titelgruppe;
		this.zweckgebunden = zweckgebunden;
	}

	public ZVKonto(){
	}

	/**
	 * 
	 */	
	public int getId() {
		return id;
	}
	
	/**
	 * Eine Kopie von einem ZVKonto erstellen. Die ZVTitel werden nicht kopiert.
	 * @return ein kopiertes ZVKonto
	 */
	public Object clone() {
		ZVKonto result = new ZVKonto( this.getId(), this.getHaushaltsJahrId(), this.getBezeichnung(), this.getKapitel(),
										this.getTitelgruppe(), this.getTgrBudget(), this.getDispoLimit(), this.getZweckgebunden(),
										this.getFreigegeben(), this.getUebernahmeStatus(), this.getGeloescht() );
		result.setSubTitel( new ArrayList() );
		
		return result;
	}
	
	/**
	 * Eine Kopie von einem ZVKonto erstellen. Die ZVTitel werden auch kopiert.
	 * @return ein kopiertes ZVKonto
	 */
	public Object cloneWhole() {
		ZVKonto result = new ZVKonto( this.getId(), this.getHaushaltsJahrId(), this.getBezeichnung(), this.getKapitel(),
										this.getTitelgruppe(), this.getTgrBudget(), this.getDispoLimit(), this.getZweckgebunden(),
										this.getFreigegeben(), this.getUebernahmeStatus(), this.getGeloescht() );
		ArrayList zvTitel = new ArrayList();
		ZVTitel temp = null;
		for( int i = 0; i < this.getSubTitel().size(); i++ ) {
			zvTitel.add( temp = (ZVTitel)((ZVTitel)this.getSubTitel().get(i)).cloneWhole() );
			temp.setZVKonto( result );
		}
		result.setSubTitel( zvTitel );
		
		return result;
	}
	
	/**
	 * Ermittlung ob zwei ZVKontos gleich sind
	 */
	public boolean equals( Object o ) {
		if(o != null){
			ZVKonto zvKonto = (ZVKonto)o;
			
			if( this.kapitel.equalsIgnoreCase( zvKonto.getKapitel() ) && this.titelgruppe.equalsIgnoreCase( zvKonto.getTitelgruppe() ) )
				return true;
			else
				return false;
		}else
			return false;
	}
	
	public boolean isTGRKonto() {
		if( titelgruppe.equalsIgnoreCase( "" ) )
			return false;
		else
			return true;
	}
		
	public String toString(){
		if( titelgruppe.equalsIgnoreCase( "" ) )
			return bezeichnung + ", " + kapitel;
		else
			return bezeichnung + ", " + kapitel + "/" + titelgruppe;
	}

	/**
	 * 
	 */
	public void setHaushaltsJahrId(int haushaltsJahrId) {
		this.haushaltsJahrId = haushaltsJahrId;
	}

	/**
	 * 
	 */
	public int getHaushaltsJahrId() {
		return haushaltsJahrId;
	}

	/**
	 * 
	 */
	public void setFreigegeben(char freigegeben) {
		this.freigegeben = freigegeben;
	}

	/**
	 * 
	 */
	public char getFreigegeben() {
		return freigegeben;
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
	public String getKapitel() {
		return kapitel;
	}

	/**
	 * 
	 */
	public void setKapitel(String kapitel) {
		this.kapitel = kapitel;
	}

	/**
	 * 
	 */
	public String getTitelgruppe() {
		return titelgruppe;
	}

	
	/**
	 * Aktualisieren eines ZVKontos. Id wird nicht aktualisiert
	 */
	public void setZVKonto( ZVKonto zvKonto ) {
		setHaushaltsJahrId( zvKonto.getHaushaltsJahrId() );
		setBezeichnung( zvKonto.getBezeichnung() );
		setKapitel( zvKonto.getKapitel() );
		setTitelgruppe( zvKonto.getTitelgruppe() );
		setTgrBudget( zvKonto.getTgrBudget() );
		setDispoLimit( zvKonto.getDispoLimit() );
		setZweckgebunden( zvKonto.getZweckgebunden() );
		setFreigegeben( zvKonto.getFreigegeben() );
		setUebernahmeStatus( zvKonto.getUebernahmeStatus() );
		setGeloescht( zvKonto.getGeloescht() );
	}

	/**
	 * Ändern der Titelgruppe, und wenn es noch Titel gibt dann wird auch dort beim Titel geändert
	 * 
	 */
	public void setTitelgruppe(String titelgruppe) {
		this.titelgruppe = titelgruppe;

		if (isTGRKonto()) {
			for (int i = 0; i < titel.size(); i++) {
				((ZVTitel) titel.get(i)).setNewTGR(titelgruppe);
			}
		}
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
		if (!this.isTGRKonto()) {
			((ZVTitel) this.titel.get(0)).setBezeichnung(bezeichnung);
		}
	}

	/**
	 * 
	 */
	public boolean getZweckgebunden() {
		return zweckgebunden;
	}

	/**
	 * 
	 */
	public void setZweckgebunden(boolean zweckgebunden) {
		this.zweckgebunden = zweckgebunden;
	}

	/**
	 * 
	 */
	public short getUebernahmeStatus() {
		return uebernahmeStatus;
	}

	/**
	 * 
	 */
	public void setUebernahmeStatus(short uebernahmeStatus) {
		this.uebernahmeStatus = uebernahmeStatus;
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
	public boolean getGeloescht() {
		return geloescht;
	}

	/**
	 * 
	 */
	public void setGeloescht(boolean geloescht) {
		this.geloescht = geloescht;
	}


	public ArrayList getSubTitel() {
		return titel;
	}

	public void setSubTitel(ArrayList titel) {
		this.titel = titel;
	}

	/**
	 * 
	 */
	public float getTgrBudget() {
		return tgrBudget;
	}

	/**
	 * 
	 */
	public void setTgrBudget(float tgrBudget) {
		this.tgrBudget = tgrBudget;
	}
	
	public boolean isZweckgebunden(){
		return zweckgebunden;
	}

}