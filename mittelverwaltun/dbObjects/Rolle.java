package dbObjects;

import java.io.Serializable;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class Rolle implements Serializable {

	private int id;

	private int[] aktivitaeten;

	private String bezeichnung;
	
	private Aktivitaet[] aktivitaetenFull;

	public Rolle (int id){
		this.id = id;
		this.bezeichnung = null;
		this.aktivitaeten = null;
	}
	
	public Rolle(int id, String bezeichnung){
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.aktivitaeten = null;
	}
	
	public Rolle(int id, int[] aktivitaeten, String bezeichnung){
		this.id = id;
		this.aktivitaeten = aktivitaeten;
		this.bezeichnung = bezeichnung;
	}

//	Änderung Anfang 01.09.2004	
	public Rolle(int id, String bezeichnung, Aktivitaet[] aktivitaeten){			
		this.id = id;
		this.aktivitaetenFull = aktivitaeten;
		this.bezeichnung = bezeichnung;
		this.aktivitaeten = null;
	}
//	Änderung Ende 01.09.2004
	
	public Rolle(int id, String bezeichnung, Aktivitaet[] aktivitaetenFull, int[] aktivitaeten){			
		this.id = id;
		this.aktivitaetenFull = aktivitaetenFull;
		this.bezeichnung = bezeichnung;
		this.aktivitaeten = aktivitaeten;
	}	
	
	public String toString(){
		return bezeichnung;
	}

	public boolean equals(Object o){
		if(o != null){
			Rolle rolle = (Rolle)o;
			if( (id == ((Rolle)rolle).getId()) && 
					((bezeichnung == null || ((Rolle)rolle).getBezeichnung() == null) ? true : bezeichnung.equals(((Rolle)rolle).getBezeichnung()))
				)
				return true;
			else
				return false;
		}else
			return false;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int[] getAktivitaeten() {
		return aktivitaeten;
	}

	public void setAktivitaeten(int[] aktivitaeten) {
		this.aktivitaeten = aktivitaeten;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	
	
	public Aktivitaet[] getAktivitaetenFull() {
		return aktivitaetenFull;
	}

	public void setAktivitaetenFull(Aktivitaet[] aktivitaetenFull) {
		this.aktivitaetenFull = aktivitaetenFull;
	}

	public Object clone(){
		int[] a = null;
		Aktivitaet[] af = null;
		
		if (this.aktivitaeten!=null){
			a = new int[aktivitaeten.length];
			for (int i=0; i<aktivitaeten.length;i++)
				a[i] = aktivitaeten[i];
		}
		if (this.aktivitaetenFull!=null){
			af = new Aktivitaet[aktivitaetenFull.length];
			for (int i=0; i<aktivitaeten.length;i++)
				af[i] = (Aktivitaet)aktivitaetenFull[i].clone();
		}
		return new Rolle(this.id, this.bezeichnung, af, a);
	}
}
