package dbObjects;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author robert
 * ASKBestellung wird bentzt um eine Software-Bestellung zu tätigen. Dabei gibt es nur ein
 * Angebot von einer Firma.
 */
public class ASKBestellung extends Bestellung implements Serializable {

	private Angebot angebot;

	private String bemerkung;

	private Benutzer swbeauftragter;
	
	/**
	 * Konstuktor
	 * @param id - Id der Bestellung
	 * @param referenznr - Referenz-Nummer
	 * @param huel - Hül Nummer
	 * @param datum - Datum der Bestellung
	 * @param besteller - Benutzer der die Bestellung bestellt
	 * @param auftraggeber - Benutzer der die Bestellung in Auftag gegeben hat
	 * @param empfaenger - Benutzer der die Bestellung empfangen soll
	 * @param zvtitel - ZVTitel der für die Bestellung benutzt wird
	 * @param fbkonto - FBKonto der für die Bestellung benutzt wird
	 * @param bestellwert - Bestellwert der Bestellung
	 * @param verbindlichkeiten - Verbindlichkeiten der Bestellung
	 * @param phase - Phase in der sich die Bestellung befindet - siehe Klasse Bestellung
	 * @param angebot - Angebot der Bestellung
	 * @param bemerkung - Bemerkungen der Bestellung
	 * @param swbeauftragter - Software-Beauftragter für die Bestellung
	 */
	public ASKBestellung(	int id, String referenznr, String huel, Date datum, Benutzer besteller, Benutzer auftraggeber, Benutzer empfaenger,
			ZVUntertitel zvtitel, FBUnterkonto fbkonto, float bestellwert, float verbindlichkeiten, char phase, Angebot angebot, String bemerkung, Benutzer swbeauftragter){
		
		super(id, referenznr, huel, datum, besteller, auftraggeber, empfaenger, zvtitel, fbkonto, bestellwert, verbindlichkeiten, phase, '1');
		this.angebot = angebot;
		this.bemerkung = bemerkung;
		this.swbeauftragter = swbeauftragter;
	}
	
	/**
	 * Konstuktor ohne die Angabe der Id der Bestellung - z.B. für neue ASK-Bestellungen
	 * @param referenznr - Referenz-Nummer
	 * @param huel - Hül Nummer
	 * @param datum - Datum der Bestellung
	 * @param besteller - Benutzer der die Bestellung bestellt
	 * @param auftraggeber - Benutzer der die Bestellung in Auftag gegeben hat
	 * @param empfaenger - Benutzer der die Bestellung empfangen soll
	 * @param zvtitel - ZVTitel der für die Bestellung benutzt wird
	 * @param fbkonto - FBKonto der für die Bestellung benutzt wird
	 * @param bestellwert - Bestellwert der Bestellung
	 * @param verbindlichkeiten - Verbindlichkeiten der Bestellung
	 * @param phase - Phase in der sich die Bestellung befindet - siehe Klasse Bestellung
	 * @param angebot - Angebot der Bestellung
	 * @param bemerkung - Bemerkungen der Bestellung
	 * @param swbeauftragter - Software-Beauftragter für die Bestellung
	 */
	public ASKBestellung(String referenznr, String huel, Date datum, Benutzer besteller, Benutzer auftraggeber, Benutzer empfaenger,
			ZVUntertitel zvtitel, FBUnterkonto fbkonto, float bestellwert, float verbindlichkeiten, char phase, Angebot angebot, String bemerkung, Benutzer swbeauftragter){
	
		super(0, referenznr, huel, datum, besteller, auftraggeber, empfaenger, zvtitel, fbkonto, bestellwert, verbindlichkeiten, phase, '1');
		this.angebot = angebot;
		this.bemerkung = bemerkung;
		this.swbeauftragter = swbeauftragter;
	}
	
	/**
	 * Konstuktor
	 * @param referenznr - Referenz-Nummer
	 * @param huel - Hül Nummer
	 * @param datum - Datum der Bestellung
	 * @param besteller - Benutzer der die Bestellung bestellt
	 * @param auftraggeber - Benutzer der die Bestellung in Auftag gegeben hat
	 * @param empfaenger - Benutzer der die Bestellung empfangen soll
	 * @param zvtitel - ZVTitel der für die Bestellung benutzt wird
	 * @param fbkonto - FBKonto der für die Bestellung benutzt wird
	 * @param bestellwert - Bestellwert der Bestellung
	 * @param verbindlichkeiten - Verbindlichkeiten der Bestellung
	 * @param phase - Phase in der sich die Bestellung befindet - siehe Klasse Bestellung
	 */
	public ASKBestellung(String referenznr, Date datum, Benutzer besteller, char phase, Benutzer auftraggeber, Benutzer empfaenger, ZVUntertitel zvtitel, FBUnterkonto fbkonto, float bestellwert, float verbindlichkeiten) {
		super(datum, besteller, phase, auftraggeber, empfaenger, zvtitel, fbkonto, bestellwert, verbindlichkeiten);
		setTyp('1');
	}

	/**
	 * gibt das Angebot der Bestellung zurück
	 * @return Angebot
	 */
	public Angebot getAngebot() {
		return angebot;
	}

	/**
	 * setzt das Angebot der Bestellung
	 * @param angebot
	 */
	public void setAngebot(Angebot angebot) {
		this.angebot = angebot;
	}

	/**
	 * gibt die Bemerkung der Bestellung zurück
	 * @return Bemerkung
	 */
	public String getBemerkung() {
		return bemerkung;
	}

	/**
	 * setzt die Bemerkung der Bestellung
	 * @param bemerkung
	 */
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	/**
	 * gibt den Software-Beauftragten zurück
	 * @return Software-Beauftragten
	 */
	public Benutzer getSwbeauftragter() {
		return swbeauftragter;
	}

	/**
	 * setzt den Software-Beauftragten
	 * @param swbeauftragter - Software-Beauftragten
	 */
	public void setSwbeauftragter(Benutzer swbeauftragter) {
		this.swbeauftragter = swbeauftragter;
	}
	
	/**
	 * begleicht alle Positionen des Angebots
	 */
	public void payPositionen (){
		if (angebot != null) angebot.payPositionen();
	}
	
	/**
	 * setzt alle Positione auf nicht beglichen
	 */
	public void owePositionen (){
		if (angebot != null) angebot.owePositionen();
	}
	
	/**
	 * aktualisiert den Bestellwert
	 */
	public void updateBestellwert(){
		if (angebot != null) 
			setBestellwert(angebot.getSumme());
	}
	
	/**
	 * aktualisiert die Verbindlichkeiten
	 */
	public void updateVerbindlichkeiten(){
		if (angebot != null) 
			setVerbindlichkeiten(angebot.getVerbindlichkeiten());
	}
	
	/**
	 * macht eine Kopie der ASKBestellung
	 */
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
