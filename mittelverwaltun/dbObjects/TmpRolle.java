package dbObjects;

import java.io.Serializable;
import java.sql.Date;
/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class TmpRolle extends Rolle implements Serializable{

	private Date gueltigBis = null;

	private int besitzer = 0;
	
	public TmpRolle(int id, int besitzer, Date gueltigBis){
		super(id);
		this.besitzer = besitzer;
		this.gueltigBis = gueltigBis;
	}

	public TmpRolle(int id, String bezeichnung, int besitzer, Date gueltigBis){
		super(id, bezeichnung);
		this.besitzer = besitzer;
		this.gueltigBis = gueltigBis;
	}
	
	public TmpRolle(int id, String bezeichnung, int[] aktivitaeten, int besitzer, Date gueltigBis){
		super(id, aktivitaeten, bezeichnung);
		this.besitzer = besitzer;
		this.gueltigBis = gueltigBis;
	}

	public Date getGueltigBis() {
		return gueltigBis;
	}

	public void setGueltigBis(Date gueltigBis) {
		this.gueltigBis = gueltigBis;
	}

	public int getBesitzer() {
		return besitzer;
	}

	public void setBesitzer(int besitzer) {
		this.besitzer = besitzer;
	}
}
