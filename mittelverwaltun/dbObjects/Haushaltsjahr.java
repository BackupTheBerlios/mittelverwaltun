package dbObjects;

import java.io.Serializable;
import java.sql.Date;


/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class Haushaltsjahr implements Serializable {

	private Date von = null;
	private Date bis = null;
	private int status = 3;
	/* status=0: aktiv
	 * status=1: inaktiv, noch nicht abgeschlossene ZV-Konten
	 * status=2: inaktiv, abgeschlossen
	 * status=3: inaktiv, setup
	 */
	public Haushaltsjahr(Date von){
		this.von = von;
	}
	
	public Haushaltsjahr(Date von, Date bis){
		
		this.von = von;
		this.bis = bis;
	}
	
	public void setVon(Date von){
		this.von = von;
	}
	
	public Date getVon(){
		return this.von;
	}
	
	public void setBis(Date bis){
		this.bis = bis;
	}
	
	public Date getBis(){
		return this.bis;
	}
	
	public void setStatus(int status){
		this.status = status;
	}
	
	public int getStatus(){
		return this.status;
	}
}
