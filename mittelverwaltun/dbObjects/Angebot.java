package dbObjects;

import java.util.ArrayList;
import java.sql.Date;
/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */


public class Angebot {

	private ArrayList positionen;
	
	private Firma anbieter;

	private int id;

	/**
	 * Falls keine Positionen Angegeben wurden kann trotzdem eine Summe für das Angebot abgegeben werden
	 */
	private float summe;

	private java.sql.Date datum;

	private boolean angenommen;

	public Angebot(int id, ArrayList positionen, Date datum, Firma anbieter, boolean angenommen){
		this.id = id;
		this.positionen = positionen;
		this.datum = datum;
		this.anbieter = anbieter;
		this.angenommen = angenommen;
	}
	
	public Angebot(int id, ArrayList positionen, Date datum, Firma anbieter, boolean angenommen, float summe){
		this.id = id;
		this.positionen = positionen;
		this.datum = datum;
		this.anbieter = anbieter;
		this.angenommen = angenommen;
		this.summe = summe;
	}
	
	public Angebot(ArrayList positionen, Date datum, Firma anbieter, boolean angenommen){
		this.positionen = positionen;
		this.datum = datum;
		this.anbieter = anbieter;
		this.angenommen = angenommen;
	}
	
	public Angebot(ArrayList positionen, Date datum, Firma anbieter, boolean angenommen, float summe){
		this.positionen = positionen;
		this.datum = datum;
		this.anbieter = anbieter;
		this.angenommen = angenommen;
		this.summe = summe;
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
		else
			gesamtSumme = summe;
		return gesamtSumme;
	}

	public void setSumme(float summe) {
		this.summe = summe;
	}
	
	public boolean equals(Object o){
		if(o != null){
			Angebot angebot = (Angebot)o;
			if( ((positionen != null) ? false : (positionen == angebot.getPositionen())) &&
					id == angebot.getId() &&
					anbieter == angebot.getAnbieter() &&
					datum == angebot.getDatum() &&
					summe == angebot.getSumme() )
				return true;
			else
				return false;
		}else
			return false;
	}

	public boolean getAngenommen() {
		return angenommen;
	}

	public void setAngenommen(boolean angenommen) {
		this.angenommen = angenommen;
	}

}
