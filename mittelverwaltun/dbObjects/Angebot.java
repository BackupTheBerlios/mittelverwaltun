package dbObjects;

import java.util.ArrayList;
import java.io.Serializable;
import java.sql.Date;
/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */


public class Angebot implements Serializable {

	private ArrayList positionen;
	
	private Firma anbieter;

	private int id;

	private Date datum;

	private boolean angenommen;

	public Angebot(int id, ArrayList positionen, Date datum, Firma anbieter, boolean angenommen){
		this.id = id;
		this.positionen = positionen;
		this.datum = datum;
		this.anbieter = anbieter;
		this.angenommen = angenommen;
	}
	
	public Angebot(ArrayList positionen, Date datum, Firma anbieter, boolean angenommen){
		this.positionen = positionen;
		this.datum = datum;
		this.anbieter = anbieter;
		this.angenommen = angenommen;
	}
	
	public java.sql.Date getDatum() {
		return datum;
	}

	public void setDatum(java.sql.Date datum) {
		this.datum = datum;
	}

	public ArrayList getPositionen() {
		return positionen;
	}

	public void setPositionen(ArrayList positionen) {
		this.positionen = positionen;
	}

	public String toString(){
		return "" + anbieter;
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
		float gesamtSumme = 0;
		
		if (positionen.size() > 0)
			for(int i = 0; i < positionen.size(); i++){
				Position position = (Position)positionen.get(i);
				gesamtSumme += position.getGesamtpreis();
			}
		
		return gesamtSumme;
	}

	public boolean equals(Object o){
		if(o != null){
			Angebot angebot = (Angebot)o;
			
			if( (getSumme() == angebot.getSumme()) &&
					((positionen == null || angebot.getPositionen() == null) ? true : positionen.equals(angebot.getPositionen())) &&
					(id == angebot.getId()) &&
					((anbieter == null || angebot.getAnbieter() == null) ? true : (anbieter.getId() == angebot.getAnbieter().getId())) &&
					datum.equals(angebot.getDatum()) &&
					(angenommen == angebot.getAngenommen())
				){
				return true;
			}else{
				return false;
			}
		}else
			return false;
	}

	public boolean getAngenommen() {
		return angenommen;
	}

	public void setAngenommen(boolean angenommen) {
		this.angenommen = angenommen;
	}
	
	/**
	 * erstellt eine Kopie von einem Angebot
	 */
	public Object clone(){
		ArrayList pos = new ArrayList();
		for(int i = 0; i < positionen.size(); i++){
			pos.add(((Position)positionen.get(i)).clone());
		}
		return new Angebot( this.id, pos, (Date)this.datum.clone(), (Firma)this.anbieter.clone(), this.angenommen);
	}

}
