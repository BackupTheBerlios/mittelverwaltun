package dbObjects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author robert *  * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern: * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 * 
 */


public class Institut implements Serializable{

	/**
	 * 
	 */
	private int id;

	/**
	 * 
	 */
	private String bezeichnung;

	/**
	 * 
	 */
	private String kostenstelle;

	/**
	 * 
	 */
	private ArrayList hauptkonten;

	/**
	 */
	private boolean geloescht;

	/**
	 */
	public int getId() {
		return id;
	}


	public Institut(int id, String bezeichnung, String kostenstelle, boolean geloescht){
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.kostenstelle = kostenstelle;
		this.geloescht = geloescht;
	}

	public Institut(int id, String bezeichnung, String kostenstelle){
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.kostenstelle = kostenstelle;
	}

	public Institut( String bezeichnung, String kostenstelle ) {
		this.bezeichnung = bezeichnung;
		this.kostenstelle = kostenstelle;
	}
	
	/**
	 * Ein Kopie von einem Institut erstellen
	 * @return ein kopiertes Institut
	 */
	public Object clone() {
		return new Institut( this.getId(), this.getBezeichnung(), this.getKostenstelle(), this.getGeloescht() );
	}

	/**
	 * 
	 */
	public ArrayList getHauptkonten() {
		return this.hauptkonten;
	}

	/**
	 * 
	 */
	public void setHauptkonten(ArrayList hauptkonten) {
		this.hauptkonten = hauptkonten;
	}

	
	public boolean equals(Object inst){
		if(inst != null){
			if( this.id == ((Institut)inst).getId() &&
				this.bezeichnung.equals(((Institut)inst).getBezeichnung())&&
				this.kostenstelle.equals(((Institut)inst).getKostenstelle()))
				return true;
			else
				return false;
		}else
			return false;
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
	public String getKostenstelle() {
		return kostenstelle;
	}

	/**
	 * 
	 */
	public void setKostenstelle(String kostenstelle) {
		this.kostenstelle = kostenstelle;
	}


	public String toString(){
		return bezeichnung;
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