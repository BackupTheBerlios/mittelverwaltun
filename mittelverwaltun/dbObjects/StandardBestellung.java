package dbObjects;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class StandardBestellung extends Bestellung implements Serializable {

	private ArrayList angebote;

	private Kostenart kostenart;

	private boolean ersatzbeschaffung;

	private String ersatzbeschreibung;

	private String inventarNr;

	private String verwendungszweck;

	private boolean planvorgabe;

	private String begruendung;
	
	private String bemerkung;

	/**
	 * 
	 * @param angebote
	 * @param auswahl
	 * @param kostenart
	 * @param ersatzbeschaffung
	 * @param ersatzbeschreibung
	 * @param inventarNr
	 * @param verwendungszweck
	 * @param planvorgabe
	 * @param begruendung
	 * @param bemerkung
	 * @param referenznr
	 * @param datum
	 * @param besteller
	 * @param phase
	 * @param auftraggeber
	 * @param empfaenger
	 * @param zvtitel
	 * @param fbkonto
	 * @param bestellwert
	 */
	public StandardBestellung(ArrayList angebote, Kostenart kostenart, boolean ersatzbeschaffung, String ersatzbeschreibung,
							String inventarNr, String verwendungszweck, boolean planvorgabe, String begruendung, String bemerkung,
							String referenznr, Date datum, Benutzer besteller, char phase, String huel, Benutzer auftraggeber,
							Benutzer empfaenger, ZVUntertitel zvtitel, FBUnterkonto fbkonto, float bestellwert, float verbindlichkeiten){
		super(referenznr, datum, besteller, phase, huel, auftraggeber, empfaenger, zvtitel, fbkonto, bestellwert, verbindlichkeiten);
		this.angebote = angebote;
		this.kostenart = kostenart;
		this.ersatzbeschaffung = ersatzbeschaffung;
		this.ersatzbeschreibung = ersatzbeschreibung;
		this.inventarNr = inventarNr;
		this.verwendungszweck = verwendungszweck;
		this.planvorgabe = planvorgabe;
		this.begruendung = begruendung;
		this.bemerkung = bemerkung;
		setTyp('0');
	}
	
	/**
	 * 
	 * @param angebote
	 * @param auswahl
	 * @param kostenart
	 * @param ersatzbeschaffung
	 * @param ersatzbeschreibung
	 * @param inventarNr
	 * @param verwendungszweck
	 * @param planvorgabe
	 * @param begruendung
	 * @param bemerkung
	 * @param id
	 * @param referenznr
	 * @param datum
	 * @param besteller
	 * @param phase
	 * @param auftraggeber
	 * @param empfaenger
	 * @param zvtitel
	 * @param fbkonto
	 * @param bestellwert
	 */
	public StandardBestellung(ArrayList angebote, Kostenart kostenart, boolean ersatzbeschaffung, String ersatzbeschreibung,
							String inventarNr, String verwendungszweck, boolean planvorgabe, String begruendung, String bemerkung,
							int id, String referenznr, Date datum, Benutzer besteller, char phase, String huel, Benutzer auftraggeber,
							Benutzer empfaenger, ZVUntertitel zvtitel, FBUnterkonto fbkonto, float bestellwert, float verbindlichkeiten){
		super(id, referenznr, datum, besteller, phase, huel, auftraggeber, empfaenger, zvtitel, fbkonto, bestellwert, verbindlichkeiten);
		this.angebote = angebote;
		this.kostenart = kostenart;
		this.ersatzbeschaffung = ersatzbeschaffung;
		this.ersatzbeschreibung = ersatzbeschreibung;
		this.inventarNr = inventarNr;
		this.verwendungszweck = verwendungszweck;
		this.planvorgabe = planvorgabe;
		this.begruendung = begruendung;
		this.bemerkung = bemerkung;
		setTyp('0');
	}
	
	/**
	 * Konstruktor ohne positionen und auswahl
	 * @param kostenart
	 * @param ersatzbeschaffung
	 * @param ersatzbeschreibung
	 * @param inventarNr
	 * @param verwendungszweck
	 * @param planvorgabe
	 * @param begruendung
	 * @param bemerkung
	 * @param id
	 * @param referenznr
	 * @param datum
	 * @param besteller
	 * @param phase
	 * @param auftraggeber
	 * @param empfaenger
	 * @param zvtitel
	 * @param fbkonto
	 * @param bestellwert
	 */
	public StandardBestellung(Kostenart kostenart, boolean ersatzbeschaffung, String ersatzbeschreibung,
							String inventarNr, String verwendungszweck, boolean planvorgabe, String begruendung, String bemerkung,
							int id, String referenznr, Date datum, Benutzer besteller, char phase, String huel, Benutzer auftraggeber,
							Benutzer empfaenger, ZVUntertitel zvtitel, FBUnterkonto fbkonto, float bestellwert, float verbindlichkeiten){
		super(id, referenznr, datum, besteller, phase, huel, auftraggeber, empfaenger, zvtitel, fbkonto, bestellwert, verbindlichkeiten);
		this.kostenart = kostenart;
		this.ersatzbeschaffung = ersatzbeschaffung;
		this.ersatzbeschreibung = ersatzbeschreibung;
		this.inventarNr = inventarNr;
		this.verwendungszweck = verwendungszweck;
		this.planvorgabe = planvorgabe;
		this.begruendung = begruendung;
		this.bemerkung = bemerkung;
		setTyp('0');
	}

	public ArrayList getAngebote() {
		return angebote;
	}

	public void setAngebote(ArrayList angebote) {
		this.angebote = angebote;
	}

	public Kostenart getKostenart() {
		return kostenart;
	}

	public void setKostenart(Kostenart kostenart) {
		this.kostenart = kostenart;
	}

	public boolean getErsatzbeschaffung() {
		return ersatzbeschaffung;
	}

	public void setErsatzbeschaffung(boolean ersatzbeschaffung) {
		this.ersatzbeschaffung = ersatzbeschaffung;
	}

	public String getErsatzbeschreibung() {
		return ersatzbeschreibung;
	}

	public void setErsatzbeschreibung(String ersatzbeschreibung) {
		this.ersatzbeschreibung = ersatzbeschreibung;
	}

	public String getInventarNr() {
		return inventarNr;
	}

	public void setInventarNr(String inventarNr) {
		this.inventarNr = inventarNr;
	}

	public String getVerwendungszweck() {
		return verwendungszweck;
	}

	public void setVerwendungszweck(String verwendungszweck) {
		this.verwendungszweck = verwendungszweck;
	}

	public boolean getPlanvorgabe() {
		return planvorgabe;
	}

	public void setPlanvorgabe(boolean planvorgabe) {
		this.planvorgabe = planvorgabe;
	}

	public String getBegruendung() {
		return begruendung;
	}

	public void setBegruendung(String begruendung) {
		this.begruendung = begruendung;
	}

	public String getBemerkung() {
		return bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}
	
	public int getAngenommenesAngebot(){
		int result = -1;
		
		if (getPhase() != '0'){
			if (angebote != null){
				for (int i=0; i<angebote.size();i++){
					Angebot a = (Angebot)angebote.get(i);
						if (a.getAngenommen()){
							result = i;
							break;
						}
				}
			}
		}
		return result;
	}
	
	public Object clone(){
		ArrayList angeboteClone = null;
		
		if ( angebote != null ){
			angeboteClone = new ArrayList();
			for (int i=0; i<angebote.size(); i++)
				angeboteClone.add(((Angebot)angebote.get(i)).clone());
		}
		
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
		
		return new StandardBestellung(
						angeboteClone, this.kostenart==null?null:(Kostenart)this.kostenart.clone(),
						this.ersatzbeschaffung, this.ersatzbeschreibung, this.inventarNr, this.verwendungszweck, this.planvorgabe, this.begruendung,
						this.bemerkung, this.getId(), this.getReferenznr(), this.getDatum()==null?null:(Date)this.getDatum().clone(), 
						this.getBesteller()==null?null:(Benutzer)this.getBesteller().clone(), this.getPhase(), this.getHuel(),
						this.getAuftraggeber()==null?null:(Benutzer)this.getAuftraggeber().clone(), 
						this.getEmpfaenger()==null?null:(Benutzer)this.getEmpfaenger().clone(), t, k, this.getBestellwert(), this.getVerbindlichkeiten());
	}
	
	public boolean equals(Object o){
		if(o != null){
			StandardBestellung b = (StandardBestellung)o;
			
//			System.out.println((kostenart == null || b.getKostenart() == null) ? true : kostenart.equals(b.getKostenart()));
//			System.out.println(ersatzbeschaffung == b.getErsatzbeschaffung());
//			System.out.println((ersatzbeschreibung == null || b.getErsatzbeschreibung() == null) ? true : ersatzbeschreibung.equals(b.getErsatzbeschreibung()));
//			System.out.println((inventarNr == null || b.getInventarNr() == null) ? true : inventarNr.equals(b.getInventarNr()));
//			System.out.println((verwendungszweck == null || b.getVerwendungszweck() == null) ? true : verwendungszweck.equals(b.getVerwendungszweck()));
//			System.out.println(	planvorgabe == b.getPlanvorgabe());
//			System.out.println((begruendung == null || b.getBegruendung() == null) ? true : begruendung.equals(b.getBegruendung()));
//			System.out.println((bemerkung == null || b.getBegruendung() == null) ? true : bemerkung.equals(b.getBemerkung())); 
//			System.out.println((angebote == null || b.getAngebote() == null) ? true : angebote.equals(b.getAngebote()));
//			System.out.println(getId() == b.getId());
//			System.out.println((getReferenznr() == null || b.getReferenznr() == null) ? true : getReferenznr().equals(b.getReferenznr())) ;
//			System.out.println((getHuel() == null || b.getHuel() == null) ? true : getHuel().equals(b.getHuel()));
//			System.out.println((getDatum() == null || b.getDatum() == null) ? true : getDatum().equals(b.getDatum()));
//			System.out.println((getBesteller() == null || b.getBesteller() == null) ? true : getBesteller().equals(b.getBesteller())) ;
//			System.out.println(getPhase() == b.getPhase() );
//			System.out.println((getAuftraggeber() == null || b.getAuftraggeber() == null) ? true : getAuftraggeber().equals(b.getAuftraggeber())) ;
//			System.out.println((getEmpfaenger() == null || b.getEmpfaenger() == null) ? true : getEmpfaenger().equals(b.getEmpfaenger())) ;
//			System.out.println((getZvtitel() == null || b.getZvtitel() == null) ? true : getZvtitel().equals(b.getZvtitel())) ;
//			System.out.println((getFbkonto() == null || b.getFbkonto() == null) ? true : getFbkonto().equals(b.getFbkonto())) ;
//			System.out.println(getBestellwert() == b.getBestellwert() );
//			System.out.println(getVerbindlichkeiten()==b.getVerbindlichkeiten());
			
			if( ((kostenart == null || b.getKostenart() == null) ? true : kostenart.equals(b.getKostenart())) &&
					ersatzbeschaffung == b.getErsatzbeschaffung() &&
					((ersatzbeschreibung == null || b.getErsatzbeschreibung() == null) ? true : ersatzbeschreibung.equals(b.getErsatzbeschreibung())) &&
					((inventarNr == null || b.getInventarNr() == null) ? true : inventarNr.equals(b.getInventarNr())) &&
					((verwendungszweck == null || b.getVerwendungszweck() == null) ? true : verwendungszweck.equals(b.getVerwendungszweck())) &&
					planvorgabe == b.getPlanvorgabe() &&
					((begruendung == null || b.getBegruendung() == null) ? true : begruendung.equals(b.getBegruendung())) &&
					((bemerkung == null || b.getBemerkung() == null) ? true : bemerkung.equals(b.getBemerkung())) && 
					((angebote == null || b.getAngebote() == null) ? true : angebote.equals(b.getAngebote())) &&
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
