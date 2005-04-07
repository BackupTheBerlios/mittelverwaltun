package applicationServer;

import java.rmi.*;
import java.sql.Date;
import java.util.ArrayList;

import dbObjects.*;

public interface ApplicationServer extends Remote {

	/**
	 * prüft ob der Benutzer in der MySQL-Datenbank existiert und ob das Passwort übereinstimmt.
	 * Gibt nach dem erfolgreichem einloggen den Benutzer mit allen Objekten und Informationen zurück.
	 * @param user - Login-Benutzer
	 * @param password - Passwort des Login-Benutzers
	 * @return Benutzer mit allen Objekten
	 * @throws ConnectionException
	 * @throws ApplicationServerException
	 */
	public Benutzer login(String user, String password) throws RemoteException, ConnectionException, ApplicationServerException;
	
	/**
	 * Beenden der Verbindung mit dem Server
	 * @throws RemoteException, ConnectionException
	 */
	public void logout()throws RemoteException, ConnectionException;
	
	/**
	 * gibt alle nicht gelöschten Benutzer im System zurück
	 * @return Benutzer-Array
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public Benutzer[] getUsers () throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt ein Array aller Benutzer des übergebenen Instituts zurück
	 * @param institut
	 * @return	Benutzer Array
	 * @throws RemoteException, ApplicationServerException
	 */
	public Benutzer[] getUsers (Institut institut) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt alle Institute zurück
	 * @return Institut-Array
	 * @throws RemoteException, ApplicationServerException
	 */
	public Institut[] getInstitutes () throws RemoteException, ApplicationServerException;
	
	/**
	 * aktualisiert ein Institut
	 * @param editedInst - Institut mit Änderungen
	 * @param clientInst - Orginal Institut beim Client
	 * @throws RemoteException, ApplicationServerException
	 */
	public void setInstitute (Institut editedInst, Institut clientInst) throws RemoteException, ApplicationServerException;
	
	/**
	 * löscht das übergebene Institut
	 * @param institut - das zu löschende Institut
	 * @throws RemoteException, ApplicationServerException
	 */
	public void delInstitute (Institut institut) throws RemoteException, ApplicationServerException;
	
	/**
	 * fügt ein neues Institut hinzu
	 * @param institut - neues Institut
	 * @return neue Id des Instituts
	 * @throws RemoteException, ApplicationServerException
	 */
	public int addInstitute(Institut institut) throws RemoteException, ApplicationServerException;
	
	/**
	 * aktualisiert einen Benutzer im System
	 * @param editedUser - Benutzer mit Änderungen
	 * @param clientUser - Original Benutzer beim Client
	 * @throws RemoteException, ApplicationServerException
	 */
	public void setUser (Benutzer editedUser, Benutzer clientUser) throws RemoteException, ApplicationServerException;
	
	/**
	 * löscht den übergebenen Benutzer aus dem System
	 * @param benutzer - der zu löschende Benutzer
	 * @throws RemoteException, ApplicationServerException
	 */
	public void delUser (Benutzer benutzer) throws RemoteException, ApplicationServerException;
	
	/**
	 * fügt einen neuen Benutzer ein
	 * @param benutzer - neuer Benutzer
	 * @return Id des Benutzers
	 * @throws RemoteException, ApplicationServerException
	 */
	public int addUser(Benutzer benutzer) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt alle Rollen im System zurück
	 * @return Rollen - Array
	 * @throws RemoteException, ApplicationServerException
	 */
	public Rolle[] getRollen () throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt alle Fachbereiche zurück. (Im Moment nur ein Fachbereich möglich)
	 * @return Fachbereich-Array
	 * @throws RemoteException, ApplicationServerException
	 */
	public Fachbereich[] getFachbereiche() throws RemoteException, ApplicationServerException;
	
	/**
	 * aktualisiert den Fachbereich
	 * @param editedFB - Fachbereich mit Änderungen
	 * @param fb - original Fachbereich beim Client
	 * @return gibt den aktualisierten Fachberich zurück
	 * @throws RemoteException, ApplicationServerException
	 */
	public Fachbereich setFachbereich(Fachbereich editedFB, Fachbereich fb) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt das Haushaltsjahr zurück
	 * @return Haushaltsjahr
	 * @throws RemoteException, ApplicationServerException
	 */
	public Haushaltsjahr getHaushaltsjahr() throws RemoteException, ApplicationServerException;

	/**
	 * aktualisiert den Haushaltsjahr
	 * @param editedHhj - Haushaltsjahr mit Änderungen
	 * @param clientHhj - original Haushaltsjahr beim Client
	 * @throws RemoteException, ApplicationServerException
	 */
	public void setHaushaltsjahr(Haushaltsjahr editedHhj, Haushaltsjahr clientHhj) throws RemoteException, ApplicationServerException;
	
	/**
	 * Abfrage der FBKonten, die ein Benutzer für seine Kleinbestellung verwenden kann.
	 * @param user = Benutzer, für den die Konten ermittelt werden sollen.
	 * @return Institut-Array(1) mit den ermittelten Konten. 
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public Institut[] getFBKontenForUser(Benutzer user) throws RemoteException, ApplicationServerException;
	
	/**
	 * Abfrage von Hauptkonten eines bestimmten Insituts.
	 * @param institut = Institut von dem die KOnten abgefragt werden.
	 * @return Liste FBHauptkonten, die zu einem bestimmten Institut angehören.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public ArrayList getFBHauptkonten( Institut institut ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Abfrage von Unterkonten eines bestimmten Instituts von einem bestimmten Hauptkonto.
	 * @param institut = Institut, welchem die FBKonten zugeordnet sind.
	 * @param hauptkonto = FBHauptkonto, welchem dei FBUnterkonten zugeordnet sind.
	 * @return FBUnterkonten
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public ArrayList getFBUnterkonten( Institut institut, FBHauptkonto hauptkonto ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Abfrage der Institute mit den dazugehörigen FBHauptkonten und FBUnterkonten
	 * @return Institut-Array
	 * @throws RemoteException, ApplicationServerException
	 */
	public Institut[] getInstitutesWithAccounts() throws RemoteException, ApplicationServerException;
	
	/**
	 * Ein neues FBHauptkonto erstellen.
	 * @param FBHauptkonto, das erstellt werden soll.
	 * @return kontoId des eingefügten Hauptkontos
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int addFBHauptkonto( FBHauptkonto konto ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Ein neues FBUnterkonto erstellen.
	 * @param FBUnterkonto, das erstellt werden soll.
	 * @return kontoId des eingefügten Unterkontos
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int addFBUnterkonto( FBUnterkonto konto ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Ein FBHauptkonto löschen. Dabei werden auch die Unterkonten gelöscht.
	 * @param FBHauptkonto, das gelöscht werden soll.
	 * @return kontoId des gelöschten FBHauptkontos
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int delFBHauptkonto( FBHauptkonto konto ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Ein FBUnterkonto löschen.
	 * @param FBUnterkonto, das gelöscht werden soll.
	 * @return kontoId des gelöschten FBUnterkontos
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int delFBUnterkonto( FBUnterkonto konto ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Ein FBHauptkonto aktualisieren.
	 * @param FBHauptkonto, welches aktualisiert werden soll
	 * @return kontoId des aktualisierten FBHauptkontos
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int setFBHauptkonto( FBHauptkonto konto ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Ein FBUnterkonto aktualisieren.
	 * @param FBUnterkonto, welches aktualisiert werden soll
	 * @return kontoId des aktualisierten FBUnterkontos
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int setFBUnterkonto( FBUnterkonto konto ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Abfrage der ZVKonten mit den dazugehörigen ZVTiteln und ZVUntertiteln.
	 * @return Liste mit den ZVKonten
	 * author w.flat
	 */
	public ArrayList getZVKonten() throws RemoteException, ApplicationServerException;
	
	/**
	 * Ein neues ZVKonto in die Datenbank einfügen.
	 * @param ZVKonto, welches eingefügt werden soll.
	 * @return kontoId vom eingefügten ZVKonto
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int addZVKonto( ZVKonto zvKonto ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Einen neuen ZVTitel in die Datenbank erstellen.
	 * @param ZVTitel, welcher erstellt werden soll
	 * @return ZVTitelId vom erstellten ZVTitel
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int addZVTitel( ZVTitel zvTitel ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Einen neuen ZVUntertitel in die Datenbank erstellen.
	 * @param ZVUntertitel, welcher erstellt werden soll
	 * @return ZVUntertitelId vom eingefügtem ZVUntertitel
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int addZVUntertitel( ZVUntertitel zvUntertitel ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Ein ZVKonto aus der Datenbank löschen. <br>
	 * Dabei müssen auch alle dazugehörigen ZVTitel und ZVUntertitel gelöscht werden.
	 * @return ZVKontoId vom gelöschten ZVKonto
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int delZVKonto( ZVKonto zvKonto ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Einen ZVTitel in der Datenbank löschen. Dabei müssen auch alle ZVUntertitel gelöscht werden.
	 * @param ZVTitel, welcher gelöscht werden sollte.
	 * @return ZVTitelId vom gelöschten ZVTitel
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int delZVTitel( ZVTitel zvTitel ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Einen ZVUntertitel in der Datenbank löschen.
	 * @param ZVUntertitel der gelöscht werden sollte.
	 * @return ZVUntertitelId vom gelöschten ZVUntertitel.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int delZVUntertitel( ZVUntertitel zvUntertitel ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Ein ZVKonto in der Datenbank aktualisieren. Es werden auch alle <br>
	 * ZVTitel und ZVUntertitel aktualisiert, wenn die Änderung diese betreffen.
	 * @param ZVKonto, das aktualisiert werden soll.
	 * @return zvKontoId des ZVKontos
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int setZVKonto( ZVKonto zvKonto ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Einen ZVTitel in der Datenbank aktualisieren. <br>
	 * Es werden auch die ZVUntertitel aktualisiert, wenn die Änderungen diese betreffen.
	 * @param ZVTitel, der aktualisiert werden soll
	 * @return ZVTitelId des übergebenen ZVTitels
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int setZVTitel( ZVTitel zvTitel ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Einen ZVUntertitel in der Datenbank aktualisieren.
	 * @param ZVUntertitel, der aktualisiert werden soll
	 * @return ZVUntertitelId des aktualisierten ZVUntertitels
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int setZVUntertitel( ZVUntertitel zvUntertitel ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Abfrage der Id von einem ZVTitel.
	 * @param ZVTitel, welcher abgefragt werden soll
	 * @return ZVTitelId des ZVTitels
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int getZVTitelId( ZVTitel zvTitel ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Überprüfung ob ein ZVKonto zweckgebunden sein kann. <br>
	 * Dabei wird ermittelt ob mehr als ein ZVKonto zu dem FBKonto einer Kontozuordnung existiert.
	 * @param ZVKonto für welches die Überprüfung durchgeführt werden soll
	 * @return Zahl > 0, wenn das ZVKonto nicht zweckgebunden sein kann. Sonst Zahl = 0.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int getNumberOfKontenzuordnungen( ZVKonto zvKonto ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Abfrage des Namen des ApplicationServers.
	 * @return Name des Servers.
	 * author w.flat
	 */
	public String getName() throws RemoteException;

	/**
	 * Budget auf ein ZVKonto buchen.
	 * @param benutzer = Benutzer, der die Buchung durchgeführt hat. 
	 * @param ZVKonto auf das der Betrag gebucht wird.
	 * @param Betrag, der auf das ZVKonto gebucht wird.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public void buche( Benutzer benutzer, ZVKonto konto, float betrag ) throws RemoteException, ApplicationServerException;
	/**
	 * Budget auf einen ZVTitel buchen.
	 * @param benutzer = Benutzer, der die Buchung durchgeführt hat. 
	 * @param ZVTitel auf den der Betrag gebucht wird.
	 * @param Betrag, der auf den ZVTitel gebucht wird.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public void buche( Benutzer benutzer, ZVTitel konto, float betrag ) throws RemoteException, ApplicationServerException;
	/**
	 * Budget auf einen ZVUntertitel buchen.
	 * @param benutzer = Benutzer, der die Buchung durchgeführt hat. 
	 * @param ZVUntertitel auf den der Betrag gebucht wird.
	 * @param Betrag, der auf den ZVUntertitel gebucht wird.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public void buche( Benutzer benutzer, ZVUntertitel konto, float betrag ) throws RemoteException, ApplicationServerException;
	/**
	 * Budget von einem FBUnterkonto auf ein FBHauptkonto buchen.
	 * @param benutzer = Benutzer, der die Buchung durchgeführt hat. 
	 * @param FBUnterkonto, von dem der Betrag abgebucht wird.
	 * @param FBHauptkonto, das den abgebuchten Betrag erhält.
	 * @param Betrag, der von dem FBUnterkonto abgebucht wird und welchen das FBHauptkonto erhält.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public void buche( Benutzer benutzer, FBUnterkonto unter, FBHauptkonto haupt, float betrag ) throws RemoteException, ApplicationServerException;
	/**
	 * Budget von einem FBHauptkonto auf ein anderes FBHauptkonto buchen.
	 * @param benutzer = Benutzer, der die Buchung durchgeführt hat. 
	 * @param FBHauptkonto, von dem der Betrag abgebucht wird.
	 * @param FBHauptkonto, das den abgebuchten Betrag erhält.
	 * @param Betrag, der vom ersten FBHauptkonto abgebucht wird und welchen das zweite FBHauptkonto erhält.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public void buche( Benutzer benutzer, FBHauptkonto from, FBHauptkonto to, float betrag ) throws RemoteException, ApplicationServerException;
	/**
	 * Budget von einem FBHauptkonto auf ein FBUnterkonto buchen.
	 * @param benutzer = Benutzer, der die Buchung durchgeführt hat. 
	 * @param FBHauptkonto, von dem der Betrag abgebucht wird.
	 * @param FBUnterkonto, das den abgebuchten Betrag erhält.
	 * @param Betrag, der vom FBHauptkonto abgebucht wird und welchen das FBUnterkonto erhält.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public void buche( Benutzer benutzer, FBHauptkonto haupt, FBUnterkonto unter, float betrag ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Ermittlung der HaushaltsjahrId vom aktuellem Jahr
	 * @return Id des aktuellen Haushaltsjahrs
	 * @throws RemoteException, ApplicationServerException
	 */
	public int getCurrentHaushaltsjahrId() throws RemoteException, ApplicationServerException;

	/**
	 * gibt alle Aktivitäten die Rollen zugeordnet werden können.
	 * @return Aktivitaet-Array
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public Aktivitaet[] getAktivitaeten() throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt alle Rollen mit allen zugehörigen Aktivitäten zurück
	 * @return Rollen-Array
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
  public Rolle[] getRollenFull () throws RemoteException, ApplicationServerException;
  
  /**
   * fügt eine neue Zuordnung einer Aktivität zu einer Rolle
   * @param rolle - Id der Rolle
   * @param aktivitaet - Id der Aktivität
   * @throws RemoteException, ApplicationServerException
   * author robert
   */
  public void addRollenAktivitaet(int rolle, int aktivitaet) throws RemoteException, ApplicationServerException;
  
  /**
   * löscht die Zuordnung einer Aktivität zu einer Rolle
   * @param rolle - Id der Rolle
   * @param aktivitaet - Id der Aktivität
   * @throws RemoteException, ApplicationServerException
   * author robert
   */
  public void delRollenAktivitaet(int rolle, int aktivitaet) throws RemoteException, ApplicationServerException;

	/**
	 * fügt eine neue Rolle hinzu
	 * @param rolle - neue Rolle
	 * @return id - neue Id der Rolle
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public int addRolle(Rolle rolle) throws RemoteException, ApplicationServerException;
	
	/**
	 * aktualisiert eine Rolle
	 * @param editedRolle - Rolle mit Änderungen
	 * @param clientRolle - original Rolle beim Client
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public void setRolle(Rolle editedRolle, Rolle clientRolle) throws RemoteException, ApplicationServerException;
	
	/**
	 * löscht eine Rolle
	 * @param rolle - zu löschende Rolle
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public void delRolle(Rolle rolle) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt einen Benutzer anhand der übergebenen Benutzernamens und Passworts
	 * @param user - Benutzername
	 * @param password
	 * @return zugehörige Benutzer
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public Benutzer getUser(String user, String password) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt nur die ZVKonten zurück ohne die Titel
	 * @return ArrayList mit ZVKonten
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public ArrayList getZVKontenOnly() throws RemoteException, ApplicationServerException;

	/**
	 * gibt Institute mit FBHauptkonten, FBHauptkonten sind mit ihren Kontenzuordnungen
	 * @return Institut-Array
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public Institut[] getInstitutZuordnungen() throws RemoteException, ApplicationServerException;
	
	/**
	 * aktualisiert die Kontenzuordnung
	 * @param fbKonto - FBKonto der Kontenzuordnung
	 * @param clientZuordnung - ZVKonto und Status der Kontenzuordnung
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public void setKontenZuordnung(FBHauptkonto fbKonto, Kontenzuordnung clientZuordnung) throws RemoteException, ApplicationServerException;
	
	/**
	 * fügt eine neue Kontenzuordnung hinzu
	 * @param fbKonto
	 * @param zvKonto
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public void addKontenZuordnung(FBHauptkonto fbKonto, ZVKonto zvKonto) throws RemoteException, ApplicationServerException;
	
	/**
	 * löscht eine Kontenzuordnung
	 * @param fbKonto
	 * @param zvKonto
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public void delKontenZuordnung(FBHauptkonto fbKonto, ZVKonto zvKonto) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt das FBKonto mit der übergebenen Id zurück
	 * @param fbKontoId - Id des FBKontos
	 * @return FBKonto als FBUnterkonto
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public FBUnterkonto getFBKonto(int fbKontoId) throws RemoteException, ApplicationServerException;
	
	/**
	 * Abfrage aller Firmen in der Datenbank.
	 * @return Liste mit allen Firmen in der Datenbank.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public ArrayList getFirmen() throws RemoteException, ApplicationServerException;
	
	/**
	 * Eine neue Firma erstellen.
	 * @param Firma, die erstellt werden soll.
	 * @return id der erstellten Firma.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int addFirma( Firma firma ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Eine Firma in der Datenbank aktualisieren.
	 * @param Firma, die aktualisiert werden soll.
	 * @return id der aktualisierten Firma.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int setFirma( Firma firma ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Eine Firma in der Datenbank löschen.
	 * @param Firma, die gelöscht werden soll.
	 * @return id der gelöschten Firma.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int delFirma( Firma firma ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Abfrage aller FBHauptkonten in der Datenbank, die zum angegebenem Institut gehören
	 * @param institut
	 * @return ArrayList mit FBHauptkonten
	 * @throws RemoteException, ApplicationServerException
	 */
	public ArrayList getNoPurposeFBHauptkonten( Institut institut ) throws RemoteException, ApplicationServerException;
	
	/**
	 *  Gibt die Benutzer eines Instituts mit einer bestimmten Rolle zurück
	 * @param i - Institut
	 * @param rollenId - Id der Rolle
	 * @return Benutzer-Array
	 * @throws RemoteException, ApplicationServerException
	 */
 	public Benutzer[] getUsersByRole(Institut i, int rollenId) throws RemoteException, ApplicationServerException;
 	
 	/**
 	 * Abfrage der Institute mit den dazugehörigen FBHauptkonten mit/ohne FBUnterkonten
 	 * @param subAccountsIncluded - true = FBUnterkonten mit einbeziehen, sonst false
 	 * @return Institut-Array
 	 * @throws RemoteException, ApplicationServerException
 	 */
 	public Institut[] getInstitutesWithAccounts (boolean subAccountsIncluded) throws RemoteException, ApplicationServerException;
 	
 	/**
 	 * Abfrage der Institute mit den dazugehörigen FBHauptkonten
 	 * @return Institut-Array
 	 * @throws RemoteException, ApplicationServerException
 	 */
 	public Institut[] getInstitutesWithMainAccounts () throws RemoteException, ApplicationServerException;

	/**
	 * Ermittelt den Betrag des noch nicht an FB-Konten verteilten Budgets 
	 * über alle _zweckungebundenen_ ZV-Konten
	 * @return der Betrag des verteilungsfähigen zweckungebundenen Budgets
	 * @throws RemoteException, ApplicationServerException
	 * author Mario
	 */	
	public float getAvailableNoPurposeBudget() throws RemoteException, ApplicationServerException;
	
	/**
	 * Ermittelt den größtmöglichen Betrag der dem übergebenen FB-Hauptkonto
	 * zugewiesen werden kann
	 * @return der zuweisungsfähige Betrag
	 * @throws RemoteException, ApplicationServerException
	 * author Mario
	 */
	public float getAvailableBudgetForAccount (FBHauptkonto account) throws RemoteException, ApplicationServerException;
	
	/**
	 * Ermittelt den Betrag des noch nicht an FB-Konten verteilten Budgets des übergebenen ZV-Kontos   
	 * VORSICHT: Liefert nur korrekte Ergebnisse für _zweckgebundenen_ ZV-Konten!!!
	 * @return der Betrag des verteilungsfähigen Budgets
	 * @throws RemoteException, ApplicationServerException
	 * author Mario
	 */
	public float getAvailableAccountBudget (ZVKonto account) throws RemoteException, ApplicationServerException;
	
	/**
	 * Budget eines FBHauptkontos aktualisieren.
	 * @param b - Benutzer der die Änderung durchführt
	 * @param acc - FBHauptkonto
	 * @param remmitance - Betrag
	 * @throws RemoteException, ApplicationServerException
	 * author Mario
	 */
 	public void setAccountBudget ( Benutzer b, FBHauptkonto acc, float remmitance ) throws RemoteException, ApplicationServerException;

	/**
	 * gibt ein Kostenarten-Array für die Standardbestellung 
	 * @return Kostenarten ArrayList
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public Kostenart[] getKostenarten() throws RemoteException, ApplicationServerException;
	
	/**
	 * speichert eine StandardBestellung
	 * @param bestellung - Standardbestellung
	 * @return id - gibt die neue Id der Bestellung
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public int addBestellung(StandardBestellung bestellung) throws RemoteException, ApplicationServerException;
	
	/**
	 * speichert eine ASKBestellung
	 * @param bestellung - Standardbestellung
	 * @return id - gibt die neue Id der Bestellung
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public int addBestellung(ASKBestellung bestellung) throws RemoteException, ApplicationServerException;
	
	/**
	 * speichert die Standardbestellung
	 * @param original - orginale Standardbestellung 
	 * @param edited - geänderte Standardbestellung
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public void setBestellung(Benutzer benutzer, StandardBestellung original, StandardBestellung edited) throws RemoteException, ApplicationServerException;
	
	/**
	 * speichert die ASKBestellung
	 * @param original - orginale ASKBestellung 
	 * @param edited - geänderte ASKBestellung
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public void setBestellung(Benutzer benutzer, ASKBestellung original, ASKBestellung edited) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt eine StandardBestellung mit allen Objekten zurück
	 * @param id - BestellungId
	 * @return StandardBestellung mit der zugehörigen Id
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public StandardBestellung getStandardBestellung(int id) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt eine ASKBestellung mit allen Objekten zurück
	 * @param id - BestellungId
	 * @return ASKBestellung mit der zugehörigen Id
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public ASKBestellung getASKBestellung(int id) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt Bestellungen ggf. eines bestimmten Typs zurück
	 * @param filter
	 * @return ArrayList mit Bestellungen
	 * @throws RemoteException, ApplicationServerException
	 * author Mario
	 */	
	public ArrayList getBestellungen(int filter) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt alle Bestellungen zurück
	 * @param filter
	 * @return ArrayList mit Bestellungen
	 * @throws RemoteException, ApplicationServerException
	 * author Mario
	 */	
	public ArrayList getBestellungen() throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt FBHauptkonten mit/ohne FBUnterkonten eines Instituts
	 * @param subAccountsIncluded
	 * @return Intitut-Array
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public Institut[] getInstituteWithAccounts(Institut institute, boolean subAccountsIncluded) throws RemoteException, ApplicationServerException;

	/**
	 * löscht eine StandardBestellung komplett aus der Datenbank. Die Bestellung darf noch nicht in der Abwicklungsphase sein.
	 * @param delOrder - StandardBestellung die gelöscht werden soll
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public void delBestellung(StandardBestellung delOrder) throws RemoteException, ApplicationServerException;

	/**
	 * löscht eine ASKBestellung komplett aus der Datenbank. Die Bestellung darf noch nicht in der Abwicklungsphase sein.
	 * @param delOrder - ASKBestellung die gelöscht werden soll
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public void delBestellung(ASKBestellung delOrder) throws RemoteException, ApplicationServerException;
	
	/**
	 * Eine Kleinbestellung in die Datenbank einfügen.
	 * @param bestellung = Kleinbestellung, die erstellt werden soll. 
	 * @return Id der eingefügten Bestellung. 
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */	
	public int addKleinbestellung(KleinBestellung bestellung) throws RemoteException, ApplicationServerException;
	
	/**
	 * Alle Kleinbestellung auswählen.
	 * @return Liste mit Bestellungen. 
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */	
	public ArrayList getKleinbestellungen() throws RemoteException, ApplicationServerException;

	/**
	 * Alle gelöschten Kleinbestellung auswählen.
	 * @return Liste mit gelöschten Bestellungen. 
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */	
	public ArrayList getDelKleinbestellungen() throws RemoteException, ApplicationServerException;
		
	/**
	 * Eine Kleinbestellung löschen. 
	 * @param Kleinbestellung, die gelöscht werden soll. 
	 * @return Id der gelöschten Bestellung. 
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */	
	public int delKleinbestellung(KleinBestellung bestellung) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt alle Softwarebeauftragte des Fachbereichs
	 * @return Benutzer-Array der SW-Beauftragten
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public Benutzer[] getSwBeauftragte() throws RemoteException, ApplicationServerException;

	/**
	 * gibt die Firma für eine ASK-Bestellung zurück
	 * @return ASK-Firma
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public Firma getASKFirma() throws RemoteException, ApplicationServerException;

	/**
	 * Eine Kleinbestellung mit einer bestimmter Id abfragen. 
	 * @param Id des Kontos. 
	 * @return Kleinbestellung die abgefragt wurde. 
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */	
	public KleinBestellung getKleinbestellung(int id) throws RemoteException, ApplicationServerException;

	/**
	 * Gibt eine ArrayList mit Inhalten für den entsprechenden Report zurück, siehe Reports Klasse für
	 * den Aufbau der ArrayListe
	 * @param typ	- Typen der Reports z.B. Reports.REPORT_1
	 * @param von - Datum für den Startpunkt
	 * @param bis - Datum für den Endpunkt
	 * @return ArrayListe mit den Angaben für den Report siehe nähe Infos zu den Objekten in Reports
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public ArrayList getReport(int typ, Date von, Date bis) throws RemoteException, ApplicationServerException;

	/**
	 * Gibt eine ArrayList mit allen Änderungen in der Datenbank zurück
	 * @param von - Datum für den Startpunkt
	 * @param bis - Datum für den Endpunkt
	 * @return	ArrayList
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public ArrayList getLogList(Date von, Date bis) throws RemoteException, ApplicationServerException;

}