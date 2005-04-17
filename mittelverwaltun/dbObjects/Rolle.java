package dbObjects;

import java.io.Serializable;

/**
 * @author robert
 *
 * Klasse f�r eine Rolle f�r einen Benutzer im System
 */
public class Rolle implements Serializable {

	/**
	 * Id der Rolle
	 */
	private int id;

	/**
	 * Array f�r die Id der Aktivit�ten der Rolle
	 */
	private int[] aktivitaeten = null;

	private String bezeichnung;
	
	/**
	 * Array f�r die Aktivit�ten der Rolle
	 */
	private Aktivitaet[] aktivitaetenFull =null;

	/**
	 * Konstutktor
	 * @param id
	 */
	public Rolle (int id){
		this.id = id;
		this.bezeichnung = null;
		this.aktivitaeten = null;
	}
	
	/**
	 * Konstutktor
	 * @param id
	 * @param bezeichnung
	 */
	public Rolle(int id, String bezeichnung){
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.aktivitaeten = null;
	}
	
	/**
	 * Konstutktor
	 * @param id
	 * @param aktivitaeten - Array mit Id der Aktivit�ten
	 * @param bezeichnung
	 */
	public Rolle(int id, int[] aktivitaeten, String bezeichnung){
		this.id = id;
		this.aktivitaeten = aktivitaeten;
		this.bezeichnung = bezeichnung;
	}

  /**
   * Konstutktor
   * @param id
   * @param bezeichnung
   * @param aktivitaeten
   */
	public Rolle(int id, String bezeichnung, Aktivitaet[] aktivitaeten){			
		this.id = id;
		this.aktivitaetenFull = aktivitaeten;
		this.bezeichnung = bezeichnung;
		this.aktivitaeten = null;
	}

	/**
	 * Konstutktor
	 * @param id
	 * @param bezeichnung
	 * @param aktivitaetenFull
	 * @param aktivitaeten - Array mit Id der Aktivit�ten
	 */
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

	/**
	 * gibt ein Array mit Id�s der Aktivit�ten zur�ck
	 * @return Int-Array
	 */
	public int[] getAktivitaeten() {
		return aktivitaeten;
	}

	/**
	 * setzt das Array mit Id�s der Aktivit�ten
	 * @param aktivitaeten
	 */
	public void setAktivitaeten(int[] aktivitaeten) {
		this.aktivitaeten = aktivitaeten;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	
	/**
	 * gibt ein Array der Aktivit�ten zur�ck nicht nur die Id�s zur�ck
	 * @return Aktivitaet-Array
	 */
	public Aktivitaet[] getAktivitaetenFull() {
		return aktivitaetenFull;
	}

	/**
	 * setzt das Array der Aktivit�ten
	 * @param aktivitaetenFull
	 */
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

	public boolean hasAktivitaet(int a){
		boolean r = false;
		
		if (aktivitaeten != null){
			for (int i=0; (i<aktivitaeten.length) && !r; i++){
				r = aktivitaeten[i] == a;
			}
		}
		
		return r;
	}
}
