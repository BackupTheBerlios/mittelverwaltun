package applicationServer;

import java.rmi.Remote;

import java.sql.ResultSet;
import java.util.ArrayList;

import dbObjects.*;

public interface ApplicationServer extends Remote {

	public Benutzer login(String user, String password) throws ConnectionException, ApplicationServerException;
	public void logout()throws ConnectionException;
	public Benutzer[] getUsers () throws ApplicationServerException;
	/**
	 * gibt ein Array aller Benutzer des übergebenen Instituts zurück
	 * @param institut
	 * @return	Benutzer Array
	 * @throws ApplicationServerException
	 */
	public Benutzer[] getUsers (Institut institut) throws ApplicationServerException;
	public Institut[] getInstitutes () throws ApplicationServerException;
	public void setInstitute (Institut editedInst, Institut clientInst) throws ApplicationServerException;
	public void delInstitute (Institut institut) throws ApplicationServerException;
	public int addInstitute(Institut institut) throws ApplicationServerException;
	public void setUser (Benutzer editedUser, Benutzer clientUser) throws ApplicationServerException;
	public void delUser (Benutzer benutzer) throws ApplicationServerException;
	public int addUser(Benutzer benutzer) throws ApplicationServerException;
	public Rolle[] getRollen () throws ApplicationServerException;
	public Fachbereich[] getFachbereiche() throws ApplicationServerException;
	public Fachbereich setFachbereich(Fachbereich fachbereich, Fachbereich fb) throws ApplicationServerException;
	public Haushaltsjahr getHaushaltsjahr() throws ApplicationServerException;

	public void setHaushaltsjahr(Haushaltsjahr editedHhj, Haushaltsjahr clientHhj) throws ApplicationServerException;
	public ResultSet query(String query);
	public void update(String query);

	public Institut[] getFBKontenForUser(Benutzer user) throws ApplicationServerException;
	public ArrayList getFBHauptkonten( Institut institut ) throws ApplicationServerException;
	public ArrayList getFBUnterkonten( Institut institut, FBHauptkonto hauptkonto ) throws ApplicationServerException;
	public Institut[] getInstitutesWithAccounts() throws ApplicationServerException;
	public int addFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException;
	public int addFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException;
	public int delFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException;
	public int delFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException;
	public int setFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException;
	public int setFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException;
	public ArrayList getZVKonten() throws ApplicationServerException;

	public int addZVKonto( ZVKonto zvKonto ) throws ApplicationServerException;
	public int addZVTitel( ZVTitel zvTitel ) throws ApplicationServerException;
	public int addZVUntertitel( ZVUntertitel zvUntertitel ) throws ApplicationServerException;
	public int delZVKonto( ZVKonto zvKonto ) throws ApplicationServerException;
	public int delZVTitel( ZVTitel zvTitel ) throws ApplicationServerException;
	public int delZVUntertitel( ZVUntertitel zvUntertitel ) throws ApplicationServerException;
	public int setZVKonto( ZVKonto zvKonto ) throws ApplicationServerException;
	public int setZVTitel( ZVTitel zvTitel ) throws ApplicationServerException;
	public int setZVUntertitel( ZVUntertitel zvUntertitel ) throws ApplicationServerException;
	
	public int getZVTitelId( ZVTitel zvTitel ) throws ApplicationServerException;
	public int getNumberOfKontenzuordnungen( ZVKonto zvKonto ) throws ApplicationServerException;
	public int getId();
	
	public void buche( ZVKonto konto, float betrag ) throws ApplicationServerException;
	public void buche( ZVTitel konto, float betrag ) throws ApplicationServerException;
	public void buche( ZVUntertitel konto, float betrag ) throws ApplicationServerException;
	
	public void buche( FBUnterkonto unter, FBHauptkonto haupt, float betrag ) throws ApplicationServerException;
	public void buche( FBHauptkonto from, FBHauptkonto to, float betrag ) throws ApplicationServerException;
	public void buche( FBHauptkonto haupt, FBUnterkonto unter, float betrag ) throws ApplicationServerException;
	
	public int getCurrentHaushaltsjahrId() throws ApplicationServerException;

	public Aktivitaet[] getAktivitaeten() throws ApplicationServerException;
    public Rolle[] getRollenFull () throws ApplicationServerException;
    public void addRollenAktivitaet(int rolle, int aktivitaet) throws ApplicationServerException;
    public void delRollenAktivitaet(int rolle, int aktivitaet) throws ApplicationServerException;

	public int addRolle(Rolle rolle) throws ApplicationServerException;
	public void setRolle(Rolle editedRolle, Rolle clientRolle) throws ApplicationServerException;
	public void delRolle(Rolle rolle) throws ApplicationServerException;
	public Benutzer getUser(String user, String password) throws ApplicationServerException;
	public ArrayList getZVKontenOnly() throws ApplicationServerException;

	public Institut[] getInstitutZuordnungen() throws ApplicationServerException;
	public void setKontenZuordnung(FBHauptkonto fbKonto, Kontenzuordnung clientZuordnung) throws ApplicationServerException;
	public void addKontenZuordnung(FBHauptkonto fbKonto, ZVKonto zvKonto) throws ApplicationServerException;
	public void delKontenZuordnung(FBHauptkonto fbKonto, ZVKonto zvKonto) throws ApplicationServerException;

	public FBUnterkonto getFBKonto(int fbKontoId) throws ApplicationServerException;
	
	
	public ArrayList getFirmen() throws ApplicationServerException;
	public int addFirma( Firma firma ) throws ApplicationServerException;
	public int setFirma( Firma firma ) throws ApplicationServerException;
	public int delFirma( Firma firma ) throws ApplicationServerException;
	
	
	 public ArrayList getNoPurposeFBHauptkonten( Institut institut ) throws ApplicationServerException;
	 public Benutzer[] getUsersByRole(Institut i, int rollenId) throws ApplicationServerException;
	 public Institut[] getInstitutesWithAccounts (boolean subAccountsIncluded) throws ApplicationServerException;
	 public Institut[] getInstitutesWithMainAccounts () throws ApplicationServerException;
	
	 public float getAvailableNoPurposeBudget() throws ApplicationServerException;
	 public float getAvailableBudgetForAccount (FBHauptkonto account) throws ApplicationServerException;
	 public float getAvailableAccountBudget (ZVKonto account) throws ApplicationServerException;
	 public void setAccountBudget ( FBHauptkonto acc, float budget ) throws ApplicationServerException;

	/**
	 * gibt ein Kostenarten-Array für die Standardbestellung 
	 * @return Kostenarten ArrayList
	 * @throws ApplicationServerException
	 * @author robert
	 */
	public Kostenart[] getKostenarten() throws ApplicationServerException;
	
	/**
	 * speichert eine StandardBestellung
	 * @param bestellung - Standardbestellung
	 * @throws ApplicationServerException
	 * @author robert
	 */
	public void addBestellung(StandardBestellung bestellung) throws ApplicationServerException;
	
	/**
	 * speichert eine ASKBestellung
	 * @param bestellung - Standardbestellung
	 * @throws ApplicationServerException
	 * @author robert
	 */
	public void addBestellung(ASKBestellung bestellung) throws ApplicationServerException;
	
	/**
	 * speichert die Standardbestellung
	 * @param original - orginale Standardbestellung 
	 * @param edited - geänderte Standardbestellung
	 * @throws ApplicationServerException
	 * @author robert
	 */
	public void setBestellung(StandardBestellung original, StandardBestellung edited) throws ApplicationServerException;
	
	/**
	 * speichert die ASKBestellung
	 * @param original - orginale ASKBestellung 
	 * @param edited - geänderte ASKBestellung
	 * @throws ApplicationServerException
	 * @author robert
	 */
	public void setBestellung(ASKBestellung original, ASKBestellung edited) throws ApplicationServerException;
	
	/**
	 * gibt eine StandardBestellung mit allen Objekten zurück
	 * @param id - BestellungId
	 * @return StandardBestellung mit der zugehörigen Id
	 * @throws ApplicationServerException
	 * @author robert
	 */
	public StandardBestellung getStandardBestellung(int id) throws ApplicationServerException;
	
	/**
	 * gibt Bestellungen ggf. eines bestimmten Typs zurück
	 * @param filter
	 * @return
	 * @throws ApplicationServerException
	 * @author Mario
	 */	
	public ArrayList getBestellungen(int filter) throws ApplicationServerException;
	
	/**
	 * gibt alle Bestellungen zurück
	 * @param filter
	 * @return
	 * @throws ApplicationServerException
	 * @author Mario
	 */	
	public ArrayList getBestellungen() throws ApplicationServerException;
	
	/**
	 * gibt FBHauptkonten mit/ohne FBUnterkonten eines Instituts
	 * @param subAccountsIncluded
	 * @return
	 * @throws ApplicationServerException
	 * @author robert
	 */
	public Institut[] getInstituteWithAccounts(Institut institute, boolean subAccountsIncluded) throws ApplicationServerException;

	
	/**
	 * 
	 * @param delOrder - StandardBestellung die gelöscht werden soll
	 * @throws ApplicationServerException
	 * @author robert
	 */
	public void delBestellung(StandardBestellung delOrder) throws ApplicationServerException;

	
	/**
	 * Eine Kleinbestellung in die Datenbank einfügen.
	 * @param bestellung = Kleinbestellung, die erstellt werden soll. 
	 * @return Id der eingefügten Bestellung. 
	 * @throws ApplicationServerException
	 * @author w.flat
	 */	
	public int addKleinbestellung(KleinBestellung bestellung) throws ApplicationServerException;
	
	/**
	 * Alle Kleinbestellung auswählen.
	 * @return Liste mit Bestellungen. 
	 * @throws ApplicationServerException
	 * @author w.flat
	 */	
	public ArrayList getKleinbestellungen() throws ApplicationServerException;

	/**
	 * Alle gelöschten Kleinbestellung auswählen.
	 * @return Liste mit gelöschten Bestellungen. 
	 * @throws ApplicationServerException
	 * @author w.flat
	 */	
	public ArrayList getDelKleinbestellungen() throws ApplicationServerException;
		
	/**
	 * Eine Kleinbestellung löschen. 
	 * @param Kleinbestellung, die gelöscht werden soll. 
	 * @return Id der gelöschten Bestellung. 
	 * @throws ApplicationServerException
	 * @author w.flat
	 */	
	public int delKleinbestellung(KleinBestellung bestellung) throws ApplicationServerException;

}