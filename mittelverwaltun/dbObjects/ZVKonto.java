package dbObjects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Diese Klasse repräsentiert ein ZVKonto der Zentralverwaltung. <br>
 * Ein ZVKonto kann folgendende Muster besitzen: <br>
 *    1. Kapitel/Titelgruppe <br>
 *    2. Kapitel/Titel <br>
 *    3. Kapitel/Titel/Untertitel <br>
 * Ein Kapitel ist eine 5.stellige Nummer. Eine Titelgruppe ist eine 2.stellige Nummer. <br>
 * Ein Titel ist eine 5.stellige Nummer. Ein Untertitel ist eine 2.stellige Nummer. <br>
 * Ist ein Titel einer Titelgruppe zugeordnet, dann sind die ersten drei Stellen die Kategorie <br>
 * und die letzten zwei Stellen die Titelgruppe. Wenn ein Kapitel mehrere Titel besitzt, dann gehören <br>
 * diese Titel einer Titelgruppe an. Sonst sind hat ein ZVKonto nur einen Titel.
 * @author w.flat
 */
public class ZVKonto implements Serializable {

	/**
	 * Eindeutige Id dieses ZVKontos. Wird durch die Datenbank vergeben.
	 */
	private int id;
	
	/**
	 * Das Haushaltsjahr in dem das ZVKonto gültig ist.
	 */
	private int haushaltsJahrId;

	/**
	 * Die Bezeichnung des Kontos.
	 */
	private String bezeichnung;
	
	/**
	 * Kapitel ist eine 5.stellige Nummer
	 */
	private String kapitel;
	
	/**
	 * Die Titelgruppe des ZVKontos ist eine 2.stellige Nummer. <br>
	 * Ist die Titelgruppe ein leerer String, dann hat das Kapitel nur einen Titel. 
	 */
	private String titelgruppe;
	
	/**
	 * Wenn ein ZVKonto zweckgebunden ist, dann kann dieses Konto als Einziges einem FBKonto zugeordnet werden. 
	 */
	private boolean zweckgebunden;
	
	/**
	 * Der Status der Übernahme bei HaushaltsjahresAbschluß. <br>
	 * 0 : keine Übernahme | 1 : Übernahme beantragt | 2 : Übernahme bewilligt | 3 : Übernommen
	 */
	private short uebernahmeStatus;
	
	/**
	 * Der DispoKredit, der vorläufig dem ZVKonto eingeräumt wurde. 
	 */
	private float dispoLimit;
	
	/**
	 * Das aktuelle Guthaben des ZVKontos. 
	 */
	private float tgrBudget;
		
	/**
	 * Liste mit den Titeln, die zu dem ZVKonto gehören. 
	 */
	private ArrayList titel = new ArrayList();

	/**
	 * Flag ob dieses Konto gelöscht wurde. 
	 */
	private boolean geloescht;
	
	/**
	 * Ist dieses ZVKonto für die Ausgaben freigegeben. 
	 */
	private char freigegeben;

	/**
	 * Konstruktor zum Erzeugen eines bereits in der Datenbank existierenden ZVKontos, welches nicht gelöscht ist. <br>
	 * Enthält alle Attribute außer dem Flag 'gelöscht' und der Liste mit den 'Titeln'. 
	 * @param id = Eindeutige Id dieses ZVKontos. Wird durch die Datenbank vergeben.
	 * @param haushaltsJahrId = Das Haushaltsjahr in dem das ZVKonto gültig ist.
	 * @param bezeichnung = Die Bezeichnung des Kontos.
	 * @param kapitel = Kapitel ist eine 5.stellige Nummer
	 * @param titelgruppe = Die Titelgruppe des ZVKontos ist eine 2.stellige Nummer.
	 * @param tgrBudget = Das aktuelle Guthaben des ZVKontos. 
	 * @param dispoLimit = Der DispoKredit, der vorläufig dem ZVKonto eingeräumt wurde. 
	 * @param zweckgebunden = Ist das ZVKonto zweckgebunden oder nicht. 
	 * @param freigegeben = Ist dieses ZVKonto für die Ausgaben freigegeben. 
	 * @param uebernahmeStatus = Der Status der Übernahme bei HaushaltsjahresAbschluß. 
	 */
	public ZVKonto( int id, int haushaltsJahrId, String bezeichnung, String kapitel, String titelgruppe, float tgrBudget,
					float dispoLimit, boolean zweckgebunden, char freigegeben, short uebernahmeStatus ) {
		this.id = id;
		this.haushaltsJahrId = haushaltsJahrId;
		this.bezeichnung = bezeichnung;
		this.kapitel = kapitel;
		this.titelgruppe = titelgruppe;
		this.tgrBudget = tgrBudget;
		this.dispoLimit = dispoLimit;
		this.zweckgebunden = zweckgebunden;
		this.freigegeben = freigegeben;
		this.uebernahmeStatus = uebernahmeStatus;
		this.geloescht = false;
	}
	
	/**
	 * Konstruktor zum Erzeugen eines bereits in der Datenbank existierenden ZVKontos. <br>
	 * Enthält alle Attribute der Liste mit den 'Titeln'. 
	 * @param id = Eindeutige Id dieses ZVKontos. Wird durch die Datenbank vergeben.
	 * @param haushaltsJahrId = Das Haushaltsjahr in dem das ZVKonto gültig ist.
	 * @param bezeichnung = Die Bezeichnung des Kontos.
	 * @param kapitel = Kapitel ist eine 5.stellige Nummer
	 * @param titelgruppe = Die Titelgruppe des ZVKontos ist eine 2.stellige Nummer.
	 * @param tgrBudget = Das aktuelle Guthaben des ZVKontos. 
	 * @param dispoLimit = Der DispoKredit, der vorläufig dem ZVKonto eingeräumt wurde. 
	 * @param zweckgebunden = Ist das ZVKonto zweckgebunden oder nicht. 
	 * @param freigegeben = Ist dieses ZVKonto für die Ausgaben freigegeben. 
	 * @param uebernahmeStatus = Der Status der Übernahme bei HaushaltsjahresAbschluß. 
	 * @param geloescht = Flag ob dieses Konto gelöscht wurde. 
	 */
	public ZVKonto( int id, int haushaltsJahrId, String bezeichnung, String kapitel, String titelgruppe, float tgrBudget,
					float dispoLimit, boolean zweckgebunden, char freigegeben, short uebernahmeStatus, boolean geloescht ) {
		this.id = id;
		this.haushaltsJahrId = haushaltsJahrId;
		this.bezeichnung = bezeichnung;
		this.kapitel = kapitel;
		this.titelgruppe = titelgruppe;
		this.tgrBudget = tgrBudget;
		this.dispoLimit = dispoLimit;
		this.zweckgebunden = zweckgebunden;
		this.freigegeben = freigegeben;
		this.uebernahmeStatus = uebernahmeStatus;
		this.geloescht = geloescht;
	}
	
	/**
	 * Konstruktor zum Erzeugen eines neuen ZVKontos. <br>
	 * @param bezeichnung = Die Bezeichnung des Kontos.
	 * @param kapitel = Kapitel ist eine 5.stellige Nummer
	 * @param titelgruppe = Die Titelgruppe des ZVKontos ist eine 2.stellige Nummer.
	 * @param dispoLimit = Der DispoKredit, der vorläufig dem ZVKonto eingeräumt wurde. 
	 */
	public ZVKonto(String bezeichnung, String kapitel, String titelgruppe, float dispo ) {
		this.id = 0;						// Id ist noch nicht bekannt
		this.haushaltsJahrId = 0;			// haushaltsJahrId ist nicht bekannt
		this.bezeichnung = bezeichnung;
		this.kapitel = kapitel;
		this.titelgruppe = titelgruppe;
		this.tgrBudget = 0.0f;				// Budget ist am Anfang = 0.0
		this.dispoLimit = dispo;
		this.zweckgebunden = false;			// Ist am Anfang nicht zweckgebunden
		this.freigegeben = '1';				// Ist freigegeben
		this.uebernahmeStatus = 0;			// Keine Übernahme 
		this.geloescht = false;				// Ist nicht gelöscht
	}
	
	/**
	 * Konstruktor zum Erzeugen eines neuen ZVKontos für die KOntenzuordnungen. <br>
	 * @param id = Eindeutige Id dieses ZVKontos.
	 * @param bezeichnung = Die Bezeichnung des Kontos.
	 * @param kapitel = Kapitel ist eine 5.stellige Nummer
	 * @param titelgruppe = Die Titelgruppe des ZVKontos ist eine 2.stellige Nummer.
	 * @param dispoLimit = Der DispoKredit, der vorläufig dem ZVKonto eingeräumt wurde. 
	 */
	public ZVKonto(int id, String bezeichnung, String kapitel, String titelgruppe, boolean zweckgebunden){
		this.id = id;
		this.haushaltsJahrId = 0;			// haushaltsJahrId ist nicht bekannt
		this.bezeichnung = bezeichnung;
		this.kapitel = kapitel;
		this.titelgruppe = titelgruppe;
		this.tgrBudget = 0.0f;				// Budget ist am Anfang = 0.0
		this.dispoLimit = 0.0f;
		this.zweckgebunden = zweckgebunden;
		this.freigegeben = '1';				// Ist freigegeben
		this.uebernahmeStatus = 0;			// Keine Übernahme
		this.geloescht = false;				// Ist nicht gelöscht
	}
	
	/**
	 * Eine Kopie von einem ZVKonto erstellen. Die ZVTitel werden nicht kopiert.
	 * @return ein kopiertes ZVKonto
	 */
	public Object clone() {
		ZVKonto result = new ZVKonto( this.getId(), this.getHaushaltsJahrId(), this.getBezeichnung(), this.getKapitel(),
										this.getTitelgruppe(), this.getTgrBudget(), this.getDispoLimit(), this.getZweckgebunden(),
										this.getFreigegeben(), this.getUebernahmeStatus(), this.getGeloescht() );
		result.setSubTitel( new ArrayList() );
		
		return result;
	}
	
	/**
	 * Eine Kopie von einem ZVKonto erstellen. Die ZVTitel werden auch kopiert.
	 * @return ein kopiertes ZVKonto
	 */
	public Object cloneWhole() {
		ZVKonto result = new ZVKonto( this.getId(), this.getHaushaltsJahrId(), this.getBezeichnung(), this.getKapitel(),
										this.getTitelgruppe(), this.getTgrBudget(), this.getDispoLimit(), this.getZweckgebunden(),
										this.getFreigegeben(), this.getUebernahmeStatus(), this.getGeloescht() );
		ArrayList zvTitel = new ArrayList();
		ZVTitel temp = null;
		for( int i = 0; i < this.getSubTitel().size(); i++ ) {
			zvTitel.add( temp = (ZVTitel)((ZVTitel)this.getSubTitel().get(i)).cloneWhole() );
			temp.setZVKonto( result );
		}
		result.setSubTitel( zvTitel );
		
		return result;
	}
	
	/**
	 * Ermittlung ob zwei ZVKonten gleich sind. <br>
	 * Zwei ZVKonten sind identisch, wenn das Kapitel und Titelgruppe gleich sind. 
	 * @return true = wenn die Konten gleich sind, Sonst = false 
	 */
	public boolean equals( Object o ) {
		if(o == null)
			return false;
		if(o.getClass().getName().equalsIgnoreCase(this.getClass().getName())) {
			ZVKonto zvKonto = (ZVKonto)o;
			if(this.kapitel.equalsIgnoreCase(zvKonto.getKapitel()) && this.titelgruppe.equalsIgnoreCase(zvKonto.getTitelgruppe()))
				return true;
		}
		return false;
	}
	
	/**
	 * Handelt es sich bei dem ZVKonto um ein Titelgruppenkonto, d.h. kann mehr als einen Titel haben. 
	 * @return true = wenn ein Titelgruppenkonto ist, sonst = false
	 */
	public boolean isTGRKonto() {
		if( titelgruppe.equalsIgnoreCase( "" ) )	// Wenn die Titelgruppe leer, dann kein TGRKonto
			return false;
		else
			return true;
	}
	
	/**
	 * Die toString-Methode überlagern. <br>
	 * Bei dem TGRKonto wird Bezeichnung, Kapitel/Titelgruppe ausgegeben. <br>
	 * Sonst wird nur Bezeichnung, Kapitel ausgegeben.
	 * @return String, welches das Konto repräsentiert. 
	 */
	public String toString(){
		if( titelgruppe.equalsIgnoreCase( "" ) )
			return bezeichnung + ", " + kapitel;
		else
			return bezeichnung + ", " + kapitel + "/" + titelgruppe;
	}

	/**
	 * Id des ZVKontos abfragen. 
	 * @return Der aktuelle Wert des Attributs Id. 
	 */	
	public int getId() {
		return id;
	}
	
	/**
	 * Zuweisung der Id. 
	 * @param id = Neuer wert des Attributs id. 
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Zuweisung einer HaushaltsJahresId. 
	 * @param haushaltsJahrId = Neuer Wert für das Attribut haushaltsJahrId. 
	 */
	public void setHaushaltsJahrId(int haushaltsJahrId) {
		this.haushaltsJahrId = haushaltsJahrId;
	}

	/**
	 * Abfrage der HaushaltsJahresId. 
	 * @return Der aktuelle Wert des Attributs haushaltsJahrId. 
	 */
	public int getHaushaltsJahrId() {
		return haushaltsJahrId;
	}

	/**
	 * Zuweisung des Flags Freigegeben. 
	 * @param freigegeben = Neuer Wert für das Flag Freigegeben. 
	 */
	public void setFreigegeben(char freigegeben) {
		this.freigegeben = freigegeben;
	}

	/**
	 * Abfrage des Flags Freigegeben. 
	 * @return Der aktuelle Wert des Flags Freigegeben. 
	 */
	public char getFreigegeben() {
		return freigegeben;
	}

	/**
	 * Abfrage des Kapitels. 
	 * @return Der aktuelle wert des Kapitels. 
	 */
	public String getKapitel() {
		return kapitel;
	}

	/**
	 * Zuweisung eines Kapitels. 
	 * @param kapitel = Neuer Wert für die Titelgruppe.  
	 */
	public void setKapitel(String kapitel) {
		this.kapitel = kapitel;
	}

	/**
	 * Abfrage der Titelgruppe. 
	 * @return Der aktuelle Wert der Titelgruppe. 
	 */
	public String getTitelgruppe() {
		return titelgruppe;
	}

	/**
	 * Ändern der Titelgruppe, und wenn noch Titel existieren, dann wird auch dort die Änderung übernommen. 
	 * @param titelgruppe = Die neue Titelgruppe des ZVKontos.
	 */
	public void setTitelgruppe(String titelgruppe) {
		this.titelgruppe = titelgruppe;
		if (isTGRKonto()) {		// Wenn ein TGRKOnto, dann auch bei den Titeln ändern
			for (int i = 0; i < titel.size(); i++) {
				((ZVTitel) titel.get(i)).setNewTGR(titelgruppe);
			}
		}
	}
	
	/**
	 * Aktualisieren eines ZVKontos. Id wird nicht aktualisiert. 
	 * @param zvKonto = ZVKonto von dem die neuen Daten übernommen werden. 
	 */
	public void setZVKonto( ZVKonto zvKonto ) {
		setHaushaltsJahrId( zvKonto.getHaushaltsJahrId() );
		setBezeichnung( zvKonto.getBezeichnung() );
		setKapitel( zvKonto.getKapitel() );
		setTitelgruppe( zvKonto.getTitelgruppe() );
		setTgrBudget( zvKonto.getTgrBudget() );
		setDispoLimit( zvKonto.getDispoLimit() );
		setZweckgebunden( zvKonto.getZweckgebunden() );
		setFreigegeben( zvKonto.getFreigegeben() );
		setUebernahmeStatus( zvKonto.getUebernahmeStatus() );
		setGeloescht( zvKonto.getGeloescht() );
	}

	/**
	 * Die Bezeichnung des Kontos abfragen.
	 * @return Die aktuelle Bezeichnung vom ZVKonto.  
	 */
	public String getBezeichnung() {
		return bezeichnung;
	}

	/**
	 * Neue Bezeichnung dem ZVkonto zuweisen. 
	 * @param bezeichnung = Neue Bezeichnung des ZVKontos. 
	 */
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
		if (!this.isTGRKonto()) {
			((ZVTitel) this.titel.get(0)).setBezeichnung(bezeichnung);
		}
	}

	/**
	 * Flag Zweckgebunden abfragen. 
	 * @return Der aktuelle Wert des Flags zweckgebunden. 
	 */
	public boolean getZweckgebunden() {
		return zweckgebunden;
	}

	/**
	 * Flag Zweckgebunden ändern. 
	 * @param zweckgebunden = Neuer Wert für das Flag zweckgebunden. 
	 */
	public void setZweckgebunden(boolean zweckgebunden) {
		this.zweckgebunden = zweckgebunden;
	}

	/**
	 * Flag UebernahmeStatus abfragen. 
	 * @return Der aktuelle Wert des Flags UebernahmeStatus. 
	 */
	public short getUebernahmeStatus() {
		return uebernahmeStatus;
	}

	/**
	 * Flag UebernahmeStatus ändern. 
	 * @param uebernahmeStatus = Neuer Wert für das Flag uebernahmeStatus. 
	 */
	public void setUebernahmeStatus(short uebernahmeStatus) {
		this.uebernahmeStatus = uebernahmeStatus;
	}

	/**
	 * Den aktuellen DispoLimit des ZVKontos abfragen. 
	 * @return Der aktuelle DispoLimit.
	 */
	public float getDispoLimit() {
		return dispoLimit;
	}

	/**
	 * Neuen Wert für den DispoLimit dem ZVKonot zuweisen. 
	 * @param dispoLimit = Neuer für das Attribut DispoLimit. 
	 */
	public void setDispoLimit(float dispoLimit) {
		this.dispoLimit = dispoLimit;
	}

	/**
	 * Das Flag geloescht abfragen. 
	 * @return Der aktuelle Wert ds Flags geloescht. 
	 */
	public boolean getGeloescht() {
		return geloescht;
	}

	/**
	 * Neuen Wert dem Flag geloescht zuweisen.
	 * @param geloescht = Neuer Wert für das Flag geloescht. 
	 */
	public void setGeloescht(boolean geloescht) {
		this.geloescht = geloescht;
	}

	/**
	 * Abfrage aller ZVTitel, die dem ZVKonto angehören.
	 * @return <code>ArrayList</code> mit allen ZVTiteln. 
	 */
	public ArrayList getSubTitel() {
		return titel;
	}

	/**
	 * ZVTitel dem ZVKonto zuweisen. 
	 * @param titel = <code>ArrayList</code> mit den ZVTiteln die dem ZVKonto angehören.
	 */
	public void setSubTitel(ArrayList titel) {
		this.titel = titel;
	}

	/**
	 * Das aktuelle Budget des ZVkontos abfragen. 
	 * @return Das aktuelle Budget des ZVKontos. 
	 */
	public float getTgrBudget() {
		return tgrBudget;
	}

	/**
	 * Neues Budget dem ZVKonto zuweisen.
	 * @param tgrBudget = Neues Budget für das ZVKonto. 
	 */
	public void setTgrBudget(float tgrBudget) {
		this.tgrBudget = tgrBudget;
	}
	
	/**
	 * Abfrage ob das ZVKonto zweckgebunden ist.
	 * @return Der aktuelle Wert des Flags Zweckgebunden. 
	 */
	public boolean isZweckgebunden(){
		return zweckgebunden;
	}

}