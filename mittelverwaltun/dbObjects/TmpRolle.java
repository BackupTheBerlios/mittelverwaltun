package dbObjects;

import java.io.Serializable;
import java.sql.Date;
import java.text.DateFormat;
/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class TmpRolle extends Rolle implements Serializable {

	private Date gueltigBis = null;

	private int besitzer = 0;
	
	private Benutzer empfaenger;
	
	public TmpRolle(int id, int besitzer, Date gueltigBis){
		super(id);
		this.besitzer = besitzer;
		this.gueltigBis = gueltigBis;
	}

	public TmpRolle(int id, String bezeichnung, int besitzer, Date gueltigBis){
		super(id, bezeichnung);
		this.besitzer = besitzer;
		this.gueltigBis = gueltigBis;
	}
	
	public TmpRolle(int id, int besitzer, Benutzer empfaenger, Date gueltigBis){
		super(id);
		this.besitzer = besitzer;
		this.empfaenger = empfaenger;
		this.gueltigBis = gueltigBis;
	}
	
	public TmpRolle(int id, String bezeichnung, int[] aktivitaeten, int besitzer, Date gueltigBis){
		super(id, aktivitaeten, bezeichnung);
		this.besitzer = besitzer;
		this.gueltigBis = gueltigBis;
	}
	
	public Date getGueltigBis() {
		return gueltigBis;
	}

	public void setGueltigBis(Date gueltigBis) {
		this.gueltigBis = gueltigBis;
	}

	public int getBesitzer() {
		return besitzer;
	}

	public void setBesitzer(int besitzer) {
		this.besitzer = besitzer;
	}
	
	public Benutzer getEmpfaenger() {
		return empfaenger;
	}

	public void setEmpfaenger(Benutzer empfaenger) {
		this.empfaenger = empfaenger;
	}
	
	public Object clone(){
		int[] a = null;
		Aktivitaet[] af = null;
		
		if (this.getAktivitaeten()!=null){
			a = new int[this.getAktivitaeten().length];
			for (int i=0; i<this.getAktivitaeten().length;i++)
				a[i] = this.getAktivitaeten()[i];
		}
//		if (this.aktivitaetenFull!=null){
//			af = new Aktivitaet[aktivitaetenFull.length];
//			for (int i=0; i<aktivitaeten.length;i++)
//				af[i] = (Aktivitaet)aktivitaetenFull[i].clone();
//		}
		return new TmpRolle(this.getId(), this.getBezeichnung(), a, this.besitzer, (Date)this.gueltigBis.clone());
	}
	
	public String toString(){
		String s = "";
		if(empfaenger != null)
			s = empfaenger.getName() + ", " + empfaenger.getVorname() + " [" + DateFormat.getDateInstance().format(gueltigBis) + "]";
		return s;
	}
}
