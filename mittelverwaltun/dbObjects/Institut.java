package dbObjects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Die Klasse zu Speichern eines Instituts. <br>
 * Das Institut wird verwendet als Kostenstelle von allen Bestellungen. <br>
 * Die Kostenstelle ist eine 6.stellige Nummer, dass den Institut eindeutig identifiziert. <br>
 * Nur für ein Institut können Hauptkonten erstellt werden. 
 * @author w.flat
 */
public class Institut implements Serializable {

	/**
	 * Eindeutige Id des Instituts. Wird von der Datenbank vergeben. 
	 */
	private int id;

	/**
	 * Die Instituts-Bezeichnung.
	 */
	private String bezeichnung;

	/**
	 * Die Kostenstelle ist eine 6.stellige (Instituts-)Nummer, welche das Institut eindeutig identifiziert. 
	 */
	private String kostenstelle;

	/**
	 * Alle FBHauptkonten vom Institut. 
	 */
	private ArrayList hauptkonten = new ArrayList();

	/**
	 * Flag ob der Institut gelöcht ist oder nicht. 
	 */
	private boolean geloescht;

	/**
	 * Der Benutzer, der der Institutsleiter ist. 
	 */
	private Benutzer institutsleiter;

	/**
	 * Konstruktor, der alle Attribute des Instituts enthält. 
	 * @param id = Id des Instituts. 
	 * @param bezeichnung = Institusbezeichnung. 
	 * @param kostenstelle = Die Institutsnummer. 
	 * @param institutsleiter = Der Leiter des Instituts. 
	 * @param geloescht = Ist das Institut gelöscht oder nicht. 
	 */
	public Institut(int id, String bezeichnung, String kostenstelle, Benutzer institutsleiter, boolean geloescht){
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.kostenstelle = kostenstelle;
		this.institutsleiter = institutsleiter;
		this.geloescht = geloescht;
	}
	
	/**
	 * Konstruktor, zum Erstellen eines Instituts. 
	 * @param id = Id des Instituts. 
	 * @param bezeichnung = Institusbezeichnung. 
	 * @param kostenstelle = Die Institutsnummer. 
	 * @param geloescht = Ist das Institut gelöscht oder nicht. 
	 */
	public Institut(int id, String bezeichnung, String kostenstelle, boolean geloescht){
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.kostenstelle = kostenstelle;
		this.institutsleiter = null;
		this.geloescht = geloescht;
	}

	/**
	 * Konstruktor, zum Erstellen eines Instituts, das nicht gelöscht ist. 
	 * @param id = Id des Instituts. 
	 * @param bezeichnung = Institusbezeichnung. 
	 * @param kostenstelle = Die Institutsnummer. 
	 */
	public Institut(int id, String bezeichnung, String kostenstelle){
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.kostenstelle = kostenstelle;
		this.institutsleiter = null;
		this.geloescht = false;
	}
	
	/**
	 * Konstruktor, zum Erstellen eines Instituts, das nicht gelöscht ist. 
	 * @param id = Id des Instituts. 
	 * @param bezeichnung = Institusbezeichnung. 
	 * @param kostenstelle = Die Institutsnummer. 
	 * @param institutsleiter = Der Leiter des Instituts. 
	 */
	public Institut(int id, String bezeichnung, String kostenstelle, Benutzer institutsleiter){
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.kostenstelle = kostenstelle;
		this.institutsleiter = institutsleiter;
		this.geloescht = false;
	}

	/**
	 * Konstruktor, zum Erstellen eines neuen Instituts. 
	 * @param bezeichnung = Institusbezeichnung. 
	 * @param kostenstelle = Die Institutsnummer. 
	 */
	public Institut( String bezeichnung, String kostenstelle ) {
		this.id = 0;
		this.bezeichnung = bezeichnung;
		this.kostenstelle = kostenstelle;
		this.institutsleiter = null;
		this.geloescht = false;
	}
	
	/**
	 * Ein Kopie von einem Institut erstellen. 
	 * @return ein kopiertes Institut
	 */
	public Object clone() {
		return new Institut( this.getId(), this.getBezeichnung(), this.getKostenstelle(), this.getGeloescht() );
	}

	/**
	 * Abfragen aller Hauptkonten, die dem Institut angehören.
	 * @return <code>ArrayList</code> mit allen FBHauptkonten. 
	 */
	public ArrayList getHauptkonten() {
		return this.hauptkonten;
	}

	/**
	 * Neue FBHaupkonten dem Institut zuweisen. 
	 * @param hauptkonten = Liste mit neuen Hauptkonten. 
	 */
	public void setHauptkonten(ArrayList hauptkonten) {
		if(hauptkonten == null)
			this.hauptkonten.clear();
		this.hauptkonten = hauptkonten;
	}

	/**
	 * Vergleich von zwei Insituten auf Gleichheit. 
	 * @return true = Wenn die Institute gleich sind, sonst = false.
	 */
	public boolean equals(Object o){
		if(o == null)
			return false;
		if(!this.getClass().getName().equalsIgnoreCase(o.getClass().getName()))
			return false;
		
		return this.getKostenstelle().equalsIgnoreCase(((Institut)o).getKostenstelle());
	}

	/**
	 * Die Id des Instituts abfragen.
	 * @return Id des Instituts. 
	 */
	public int getId() {
		return id;
	}

	/**
	 * Neue Id dem Institut zuweisen. 
	 * @param id = Neue Id des Instituts. 
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Die Institutsbezeichnung abfragen. 
	 * @return Insitutsbezeichnung. 
	 */
	public String getBezeichnung() {
		return bezeichnung;
	}

	/**
	 * Neue Institutsbezeichnung zuweisen.
	 * @param bezeichnung = Neue Institutsbezeichnung.
	 */
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Die Kostenstelle [= Institutsnummer] abfragen. 
	 * @return Die Institutsnummer. 
	 */
	public String getKostenstelle() {
		return kostenstelle;
	}

	/**
	 * Neue Institutsnummer dem Institut zuweisen.
	 * @param kostenstelle = Neue Institutsnummer. 
	 */
	public void setKostenstelle(String kostenstelle) {
		if(kostenstelle == null)		return;		// Keine Kostenstelle angegeben
		if(kostenstelle.length() != 6)	return;		// Kostenstelle falsches Format
		this.kostenstelle = kostenstelle;
	}

	/**
	 * Überlagerte toString-Methode. 
	 * @return Die Institutsbezeichnung. 
	 */
	public String toString(){
		return bezeichnung;
	}

	/**
	 * Abfrage ob das Institut gelöscht ist. 
	 * @return true = Institut ist gelöscht, sonst = false.  
	 */
	public boolean getGeloescht() {
		return geloescht;
	}

	/**
	 * Neuen Wert dem Flag gelöscht zuweisen. 
	 * @param geloescht = Neuer Wert für das Flag gelöscht. 
	 */
	public void setGeloescht(boolean geloescht) {
		this.geloescht = geloescht;
	}

	/**
	 * Den Institutsleiter des Instituts abfragen. 
	 * @return <code>Benutzer</code>, der der Institutsleiter. 
	 */
	public Benutzer getInstitutsleiter() {
		return institutsleiter;
	}

	/**
	 * Neuen Institutsleiter dem Institut zuweisen. 
	 * @param institutsleiter = Neuer Institutsleiter des Instituts. 
	 */
	public void setInstitutsleiter(Benutzer institutsleiter) {
		this.institutsleiter = institutsleiter;
	}

}