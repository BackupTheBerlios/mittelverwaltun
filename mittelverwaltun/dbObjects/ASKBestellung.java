package dbObjects;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class ASKBestellung extends Bestellung implements Serializable {

	private Angebot angebot;

	private String bemerkung;

	private Benutzer swbeauftragter;
	
	public ASKBestellung(	int id, String referenznr, String huel, Date datum, Benutzer besteller, Benutzer auftraggeber, Benutzer empfaenger,
			ZVUntertitel zvtitel, FBUnterkonto fbkonto, float bestellwert, float verbindlichkeiten, char phase, Angebot angebot, String bemerkung, Benutzer swbeauftragter){
		
		super(id, referenznr, huel, datum, besteller, auftraggeber, empfaenger, zvtitel, fbkonto, bestellwert, verbindlichkeiten, phase, '1');
		this.angebot = angebot;
		this.bemerkung = bemerkung;
		this.swbeauftragter = swbeauftragter;
		
	}
	
	
	public ASKBestellung(String referenznr, Date datum, Benutzer besteller, char phase, Benutzer auftraggeber, Benutzer empfaenger, ZVTitel zvtitel, FBUnterkonto fbkonto, float bestellwert, float verbindlichkeiten) {
		super(datum, besteller, phase, auftraggeber, empfaenger, zvtitel, fbkonto, bestellwert, verbindlichkeiten);
		// TODO Automatisch erstellter Konstruktoren-Stub
	}

	public Angebot getAngebot() {
		return angebot;
	}

	public void setAngebot(Angebot angebot) {
		this.angebot = angebot;
	}

	public String getBemerkung() {
		return bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	public Benutzer getSwbeauftragter() {
		return swbeauftragter;
	}

	public void setSwbeauftragter(Benutzer swbeauftragter) {
		this.swbeauftragter = swbeauftragter;
	}
	
	public Object clone(){
			
		ZVUntertitel t = null;
		
		if (getZvtitel() != null){
			if (getZvtitel().getClass().getName().equalsIgnoreCase(ZVUntertitel.class.getName())){
				t = (ZVUntertitel)getZvtitel().clone();
			}else t = (ZVTitel)getZvtitel().clone();
		}
		
		FBUnterkonto k = null;
		
		if (getFbkonto() != null){
			if (getFbkonto().getClass().getName().equalsIgnoreCase(FBUnterkonto.class.getName())){
				k = (FBUnterkonto)getFbkonto().clone();
			}else k = (FBHauptkonto)getFbkonto().clone();
		}
		
		return new ASKBestellung(
						this.getId(), this.getReferenznr(), this.getHuel(), this.getDatum()==null?null:(Date)this.getDatum().clone(), 
						this.getBesteller()==null?null:(Benutzer)this.getBesteller().clone(), this.getAuftraggeber()==null?null:(Benutzer)this.getAuftraggeber().clone(), 
						this.getEmpfaenger()==null?null:(Benutzer)this.getEmpfaenger().clone(), t, k, this.getBestellwert(), this.getVerbindlichkeiten(),
						this.getPhase(), this.angebot == null ? null : (Angebot)this.angebot.clone(), this.bemerkung,
						this.swbeauftragter == null ? null : (Benutzer)this.swbeauftragter.clone());
	}

	public boolean equals(Object o){
		if(o != null){
			ASKBestellung b = (ASKBestellung)o;
	
			if( 	((angebot == null || b.getAngebot() == null) ? true : angebot.equals(b.getAngebot())) &&
					((bemerkung == null || b.getBemerkung() == null) ? true : bemerkung.equals(b.getBemerkung())) && 
					((swbeauftragter == null || b.getSwbeauftragter() == null) ? true : swbeauftragter.equals(b.getSwbeauftragter())) &&
					// Bestellung
					getId() == b.getId() &&
					((getReferenznr() == null || b.getReferenznr() == null) ? true : getReferenznr().equals(b.getReferenznr())) &&
					((getHuel() == null || b.getHuel() == null) ? true : getHuel().equals(b.getHuel())) &&
					((getDatum() == null || b.getDatum() == null) ? true : getDatum().equals(b.getDatum())) &&
					((getBesteller() == null || b.getBesteller() == null) ? true : getBesteller().equals(b.getBesteller())) &&
					getPhase() == b.getPhase() &&
					((getAuftraggeber() == null || b.getAuftraggeber() == null) ? true : getAuftraggeber().equals(b.getAuftraggeber())) &&
					((getEmpfaenger() == null || b.getEmpfaenger() == null) ? true : getEmpfaenger().equals(b.getEmpfaenger())) &&
					((getZvtitel() == null || b.getZvtitel() == null) ? true : getZvtitel().equals(b.getZvtitel())) &&
					((getFbkonto() == null || b.getFbkonto() == null) ? true : getFbkonto().equals(b.getFbkonto())) &&
					getBestellwert() == b.getBestellwert() && getVerbindlichkeiten()==b.getVerbindlichkeiten()
					)
				return true;
			else
				return false;
		}else
			return false;
	}	
	
}
