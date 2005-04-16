package dbObjects;

import java.util.ArrayList;
import java.io.Serializable;
import java.sql.Date;
/**
 * @author robert
 *
 * Angebot für eine Standard- bzw. eine ASKBestellung. Mit einem Angebot können Bestellungen getätigt werden.
 */
public class Angebot implements Serializable {

	private ArrayList positionen;
	
	private Firma anbieter;

	private int id;

	private Date datum;

	private boolean angenommen;

	/**
	 * Konstruktor
	 * @param id - Id vom Angebot
	 * @param positionen - Array mit Positionen für das Angebot
	 * @param datum - Datum der Angebots
	 * @param anbieter - Firma die das Angebot anbietet
	 * @param angenommen - Angebot angenommen oder nicht
	 */
	public Angebot(int id, ArrayList positionen, Date datum, Firma anbieter, boolean angenommen){
		this.id = id;
		this.positionen = positionen;
		this.datum = datum;
		this.anbieter = anbieter;
		this.angenommen = angenommen;
	}
	
	/**
	 * Konstruktor
	 * @param positionen - Array mit Positionen für das Angebot
	 * @param datum - Datum der Angebots
	 * @param anbieter - Firma die das Angebot anbietet
	 * @param angenommen - Angebot angenommen oder nicht
	 */
	public Angebot(ArrayList positionen, Date datum, Firma anbieter, boolean angenommen){
		this.positionen = positionen;
		this.datum = datum;
		this.anbieter = anbieter;
		this.angenommen = angenommen;
	}
	
	/**
	 * gibt das Datum vom Angebot
	 * @return Datum
	 */
	public Date getDatum() {
		return datum;
	}

	/**
	 * setzt das Datum vom Angebot
	 * @param datum
	 */
	public void setDatum(Date datum) {
		this.datum = datum;
	}

	/**
	 * gibt die Positionen für das Angebot zurück
	 * @return ArrayList mit Positionen
	 */
	public ArrayList getPositionen() {
		return positionen;
	}

	/**
	 * setzt die Positionen für das Angebot
	 * @param positionen - ArrayList
	 */
	public void setPositionen(ArrayList positionen) {
		this.positionen = positionen;
	}

	public String toString(){
		return "" + anbieter;
	}
	
	/**
	 * gibt den Anbieter für das Angebot zurück
	 * @return Firma
	 */
	public Firma getAnbieter() {
		return anbieter;
	}

	/**
	 * setzt den Anbieter für das Angebot
	 * @param anbieter - Firma
	 */
	public void setAnbieter(Firma anbieter) {
		this.anbieter = anbieter;
	}

	/**
	 * gibt die Id des Angebots zurück
	 * @return Id
	 */
	public int getId() {
		return id;
	}

	/**
	 * setzt die Id des Angebots
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * gibt die Gesammt-Summe der Positionen zurück
	 * @return Gesammtsumme
	 */
	public float getSumme(){
		float gesamtSumme = 0;
		
		if (positionen.size() > 0)
			for(int i = 0; i < positionen.size(); i++){
				Position position = (Position)positionen.get(i);
				gesamtSumme += position.getGesamtpreis();
			}
		
		return gesamtSumme;
	}

	/**
	 * gibt die Verbindlichkeiten des Angebots zurück, also die Summe der Positionen die noch
	 * nicht beglichen wurden.
	 * @return Verbindlichkeiten
	 */
	public float getVerbindlichkeiten(){
		float summe = 0;
		
		if (positionen.size() > 0)
			for(int i = 0; i < positionen.size(); i++){
				Position position = (Position)positionen.get(i);
				if (!position.getBeglichen())
					summe += position.getGesamtpreis();
			}
		
		return summe;
	}
	
	
	public boolean equals(Object o){
		if(o != null){
			Angebot angebot = (Angebot)o;
			
//			System.out.println(getSumme() == angebot.getSumme());
//			System.out.println((positionen == null || angebot.getPositionen() == null) ? true : positionen.equals(angebot.getPositionen()));
//			System.out.println(id == angebot.getId());
//			System.out.println((anbieter == null || angebot.getAnbieter() == null) ? true : (anbieter.getId() == angebot.getAnbieter().getId()));
//			System.out.println(datum.equals(angebot.getDatum()));
//			System.out.println(angenommen == angebot.getAngenommen());
			
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

	/**
	 * gibt zurück ob das Angebot angenommen wurde oder nicht
	 * @return angenommen ?
	 */
	public boolean getAngenommen() {
		return angenommen;
	}

	/**
	 * setzt das Flag ob das Angebot angenommen wird oder nicht
	 * @param angenommen
	 */
	public void setAngenommen(boolean angenommen) {
		this.angenommen = angenommen;
	}
	
	/**
	 * begleicht alle Positionen des Angebots => setzt alle Positionen auf beglichen
	 */
	public void payPositionen (){
		if (positionen != null){
			for(int i=0; i<positionen.size(); i++)
				((Position)positionen.get(i)).setBeglichen(true);
		}
	}
	
	/**
	 * setzt alle Positionen auf nicht beglichen
	 */
	public void owePositionen (){
		if (positionen != null){
				for(int i=0; i<positionen.size(); i++)
					((Position)positionen.get(i)).setBeglichen(false);
		}
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
