package dbObjects;

import java.io.Serializable;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class Fachbereich extends Institut implements Serializable{
	private float profPauschale;
	private String hochschule;

	
	public Fachbereich(int id, String hochschule, float profPauschale, String bezeichnung, String kostenstelle) {
		super(id, bezeichnung, kostenstelle, false);
		this.profPauschale = profPauschale;
		this.hochschule = hochschule;
	}
	
	public Fachbereich(int id, String hochschule, float profPauschale) {
		super(id, null, null, false);
		this.profPauschale = profPauschale;
		this.hochschule = hochschule;
	}
	
	public float getProfPauschale() {
		return profPauschale;
	}

	public void setProfPauschale(float profPauschale) {
		this.profPauschale = profPauschale;
	}

	public String getHochschule() {
		return hochschule;
	}

	public void setHochschule(String hochschule) {
		this.hochschule = hochschule;
	}

	public String toString(){
		return hochschule;
	}

	public boolean equals(Object fb){
		if(fb != null){
			if( this.getId() == ((Fachbereich)fb).getId() && 
				this.hochschule.equals(((Fachbereich)fb).getHochschule()) &&
				this.profPauschale == ((Fachbereich)fb).getProfPauschale())
				return true;
			else
				return false;
		}else
			return false;
	}	

}
