package dbObjects;

import java.util.ArrayList;
import java.util.Date;
/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */


public class Angebot {

	private ArrayList positionen;
	
	private Date datum;

	private Firma anbieter;

	private int id;

	/**
	 * Falls keine Positionen Angegeben wurden kann trotzdem eine Summe für das Angebot abgegeben werden
	 */
	private float summe;

	public Angebot(int id, ArrayList positionen, Date datum, Firma anbieter){
		this.id = id;
		this.positionen = positionen;
		this.datum = datum;
		this.anbieter = anbieter;
	}
	
	public Angebot(int id, ArrayList positionen, Date datum, Firma anbieter, float summe){
		this.id = id;
		this.positionen = positionen;
		this.datum = datum;
		this.anbieter = anbieter;
		this.summe = summe;
	}
	
	public Angebot(ArrayList positionen, Date datum, Firma anbieter){
		this.positionen = positionen;
		this.datum = datum;
		this.anbieter = anbieter;
	}
	
	public Angebot(ArrayList positionen, Date datum, Firma anbieter, float summe){
		this.positionen = positionen;
		this.datum = datum;
		this.anbieter = anbieter;
		this.summe = summe;
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

}
