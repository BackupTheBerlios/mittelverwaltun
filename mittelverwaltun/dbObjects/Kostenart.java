package dbObjects;

import java.io.Serializable;

/**
 * @author robert
 *
 * Kostenart für die Standardbestellung
 */
public class Kostenart implements Serializable {

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
	
	public boolean equals(Object o){
		if(o != null){
			Kostenart k = (Kostenart)o;
			if( (id == k.getId()) &&
					((beschreibung == null || k.getBeschreibung() == null) ? true : beschreibung.equals(k.getBeschreibung()))
				)
				return true;
			else
				return false;
		}else
			return false;
	}
}
