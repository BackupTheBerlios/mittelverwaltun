package dbObjects;

import java.io.Serializable;


/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class Haushaltsjahr implements Serializable{

	private String beschreibung;
	private String von;
	private String bis;
	private int status;
	
	public Haushaltsjahr(String beschreibung, String von, String bis){
		this.beschreibung = beschreibung;
		this.von = von;
		this.bis = bis;
	}
	
	public void setBeschreibung(String beschreibung){
		this.beschreibung = beschreibung;
	}
	
	public String getBeschreibung(){
		return this.beschreibung;
	}
	
	public void setVon(String von){
		this.von = von;
	}
	
	public String getVon(){
		return this.von;
	}
	
	public void setBis(String bis){
		this.bis = bis;
	}
	
	public String getBis(){
		return this.bis;
	}
	
	public void setStatus(int status){
		this.status = status;
	}
	
	public int getStatus(){
		return this.status;
	}
}
