package dbObjects;

/**
 * @author robert
 *
 * Kostenart f�r die Standardbestellung
 */
public class Kostenart {

	private int id;

	private String beschreibung;
	
	public Kostenart(int id, String beschreibung){
		this.id = id;
		this.beschreibung = beschreibung;		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String toString(){
		return beschreibung;
	}
}
