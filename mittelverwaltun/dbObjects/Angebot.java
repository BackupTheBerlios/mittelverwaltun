package dbObjects;

import java.util.ArrayList;
import java.sql.Date;
/**
 * @author robert
 *
 * Folgendes ausw�hlen, um die Schablone f�r den erstellten Typenkommentar zu �ndern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */


public class Angebot {

	private ArrayList positionen;
	
	private Date datum;

	private Firma anbieter;

	private int id;

	public Angebot(){
	}
	
	

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public ArrayList getPositionen() {
		return positionen;
	}

	public void setPositionen(ArrayList positionen) {
		this.positionen = positionen;
	}

	public String toString(){
		return "";
	}
	public Firma getAnbieter() {
		return anbieter;
	}

	public void setAnbieter(Firma anbieter) {
		this.anbieter = anbieter;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public float getSumme(){
		float summe = 0;
		
		for(int i = 0; i < positionen.size(); i++){
			Position position = (Position)positionen.get(i);
			summe += position.getGesamtpreis();
		}
		return summe;
	}

}
