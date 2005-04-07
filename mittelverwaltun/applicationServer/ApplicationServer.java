package applicationServer;

import java.rmi.*;
import java.sql.Date;
import java.util.ArrayList;

import dbObjects.*;

public interface ApplicationServer extends Remote {

	/**
	 * pr�ft ob der Benutzer in der MySQL-Datenbank existiert und ob das Passwort �bereinstimmt.
	 * Gibt nach dem erfolgreichem einloggen den Benutzer mit allen Objekten und Informationen zur�ck.
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
	 * gibt alle nicht gel�schten Benutzer im System zur�ck
	 * @return Benutzer-Array
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public Benutzer[] getUsers () throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt ein Array aller Benutzer des �bergebenen Instituts zur�ck
	 * @param institut
	 * @return	Benutzer Array
	 * @throws RemoteException, ApplicationServerException
	 */
	public Benutzer[] getUsers (Institut institut) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt alle Institute zur�ck
	 * @return Institut-Array
	 * @throws RemoteException, ApplicationServerException
	 */
	public Institut[] getInstitutes () throws RemoteException, ApplicationServerException;
	
	/**
	 * aktualisiert ein Institut
	 * @param editedInst - Institut mit �nderungen
	 * @param clientInst - Orginal Institut beim Client
	 * @throws RemoteException, ApplicationServerException
	 */
	public void setInstitute (Institut editedInst, Institut clientInst) throws RemoteException, ApplicationServerException;
	
	/**
	 * l�scht das �bergebene Institut
	 * @param institut - das zu l�schende Institut
	 * @throws RemoteException, ApplicationServerException
	 */
	public void delInstitute (Institut institut) throws RemoteException, ApplicationServerException;
	
	/**
	 * f�gt ein neues Institut hinzu
	 * @param institut - neues Institut
	 * @return neue Id des Instituts
	 * @throws RemoteException, ApplicationServerException
	 */
	public int addInstitute(Institut institut) throws RemoteException, ApplicationServerException;
	
	/**
	 * aktualisiert einen Benutzer im System
	 * @param editedUser - Benutzer mit �nderungen
	 * @param clientUser - Original Benutzer beim Client
	 * @throws RemoteException, ApplicationServerException
	 */
	public void setUser (Benutzer editedUser, Benutzer clientUser) throws RemoteException, ApplicationServerException;
	
	/**
	 * l�scht den �bergebenen Benutzer aus dem System
	 * @param benutzer - der zu l�schende Benutzer
	 * @throws RemoteException, ApplicationServerException
	 */
	public void delUser (Benutzer benutzer) throws RemoteException, ApplicationServerException;
	
	/**
	 * f�gt einen neuen Benutzer ein
	 * @param benutzer - neuer Benutzer
	 * @return Id des Benutzers
	 * @throws RemoteException, ApplicationServerException
	 */
	public int addUser(Benutzer benutzer) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt alle Rollen im System zur�ck
	 * @return Rollen - Array
	 * @throws RemoteException, ApplicationServerException
	 */
	public Rolle[] getRollen () throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt alle Fachbereiche zur�ck. (Im Moment nur ein Fachbereich m�glich)
	 * @return Fachbereich-Array
	 * @throws RemoteException, ApplicationServerException
	 */
	public Fachbereich[] getFachbereiche() throws RemoteException, ApplicationServerException;
	
	/**
	 * aktualisiert den Fachbereich
	 * @param editedFB - Fachbereich mit �nderungen
	 * @param fb - original Fachbereich beim Client
	 * @return gibt den aktualisierten Fachberich zur�ck
	 * @throws RemoteException, ApplicationServerException
	 */
	public Fachbereich setFachbereich(Fachbereich editedFB, Fachbereich fb) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt das Haushaltsjahr zur�ck
	 * @return Haushaltsjahr
	 * @throws RemoteException, ApplicationServerException
	 */
	public Haushaltsjahr getHaushaltsjahr() throws RemoteException, ApplicationServerException;

	/**
	 * aktualisiert den Haushaltsjahr
	 * @param editedHhj - Haushaltsjahr mit �nderungen
	 * @param clientHhj - original Haushaltsjahr beim Client
	 * @throws RemoteException, ApplicationServerException
	 */
	public void setHaushaltsjahr(Haushaltsjahr editedHhj, Haushaltsjahr clientHhj) throws RemoteException, ApplicationServerException;
	
	/**
	 * Abfrage der FBKonten, die ein Benutzer f�r seine Kleinbestellung verwenden kann.
	 * @param user = Benutzer, f�r den die Konten ermittelt werden sollen.
	 * @return Institut-Array(1) mit den ermittelten Konten. 
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public Institut[] getFBKontenForUser(Benutzer user) throws RemoteException, ApplicationServerException;
	
	/**
	 * Abfrage von Hauptkonten eines bestimmten Insituts.
	 * @param institut = Institut von dem die KOnten abgefragt werden.
	 * @return Liste FBHauptkonten, die zu einem bestimmten Institut angeh�ren.
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
	 * Abfrage der Institute mit den dazugeh�rigen FBHauptkonten und FBUnterkonten
	 * @return Institut-Array
	 * @throws RemoteException, ApplicationServerException
	 */
	public Institut[] getInstitutesWithAccounts() throws RemoteException, ApplicationServerException;
	
	/**
	 * Ein neues FBHauptkonto erstellen.
	 * @param FBHauptkonto, das erstellt werden soll.
	 * @return kontoId des eingef�gten Hauptkontos
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int addFBHauptkonto( FBHauptkonto konto ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Ein neues FBUnterkonto erstellen.
	 * @param FBUnterkonto, das erstellt werden soll.
	 * @return kontoId des eingef�gten Unterkontos
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int addFBUnterkonto( FBUnterkonto konto ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Ein FBHauptkonto l�schen. Dabei werden auch die Unterkonten gel�scht.
	 * @param FBHauptkonto, das gel�scht werden soll.
	 * @return kontoId des gel�schten FBHauptkontos
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int delFBHauptkonto( FBHauptkonto konto ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Ein FBUnterkonto l�schen.
	 * @param FBUnterkonto, das gel�scht werden soll.
	 * @return kontoId des gel�schten FBUnterkontos
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
	 * Abfrage der ZVKonten mit den dazugeh�rigen ZVTiteln und ZVUntertiteln.
	 * @return Liste mit den ZVKonten
	 * author w.flat
	 */
	public ArrayList getZVKonten() throws RemoteException, ApplicationServerException;
	
	/**
	 * Ein neues ZVKonto in die Datenbank einf�gen.
	 * @param ZVKonto, welches eingef�gt werden soll.
	 * @return kontoId vom eingef�gten ZVKonto
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
	 * @return ZVUntertitelId vom eingef�gtem ZVUntertitel
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int addZVUntertitel( ZVUntertitel zvUntertitel ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Ein ZVKonto aus der Datenbank l�schen. <br>
	 * Dabei m�ssen auch alle dazugeh�rigen ZVTitel und ZVUntertitel gel�scht werden.
	 * @return ZVKontoId vom gel�schten ZVKonto
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int delZVKonto( ZVKonto zvKonto ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Einen ZVTitel in der Datenbank l�schen. Dabei m�ssen auch alle ZVUntertitel gel�scht werden.
	 * @param ZVTitel, welcher gel�scht werden sollte.
	 * @return ZVTitelId vom gel�schten ZVTitel
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int delZVTitel( ZVTitel zvTitel ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Einen ZVUntertitel in der Datenbank l�schen.
	 * @param ZVUntertitel der gel�scht werden sollte.
	 * @return ZVUntertitelId vom gel�schten ZVUntertitel.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int delZVUntertitel( ZVUntertitel zvUntertitel ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Ein ZVKonto in der Datenbank aktualisieren. Es werden auch alle <br>
	 * ZVTitel und ZVUntertitel aktualisiert, wenn die �nderung diese betreffen.
	 * @param ZVKonto, das aktualisiert werden soll.
	 * @return zvKontoId des ZVKontos
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int setZVKonto( ZVKonto zvKonto ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Einen ZVTitel in der Datenbank aktualisieren. <br>
	 * Es werden auch die ZVUntertitel aktualisiert, wenn die �nderungen diese betreffen.
	 * @param ZVTitel, der aktualisiert werden soll
	 * @return ZVTitelId des �bergebenen ZVTitels
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
	 * �berpr�fung ob ein ZVKonto zweckgebunden sein kann. <br>
	 * Dabei wird ermittelt ob mehr als ein ZVKonto zu dem FBKonto einer Kontozuordnung existiert.
	 * @param ZVKonto f�r welches die �berpr�fung durchgef�hrt werden soll
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
	 * @param benutzer = Benutzer, der die Buchung durchgef�hrt hat. 
	 * @param ZVKonto auf das der Betrag gebucht wird.
	 * @param Betrag, der auf das ZVKonto gebucht wird.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public void buche( Benutzer benutzer, ZVKonto konto, float betrag ) throws RemoteException, ApplicationServerException;
	/**
	 * Budget auf einen ZVTitel buchen.
	 * @param benutzer = Benutzer, der die Buchung durchgef�hrt hat. 
	 * @param ZVTitel auf den der Betrag gebucht wird.
	 * @param Betrag, der auf den ZVTitel gebucht wird.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public void buche( Benutzer benutzer, ZVTitel konto, float betrag ) throws RemoteException, ApplicationServerException;
	/**
	 * Budget auf einen ZVUntertitel buchen.
	 * @param benutzer = Benutzer, der die Buchung durchgef�hrt hat. 
	 * @param ZVUntertitel auf den der Betrag gebucht wird.
	 * @param Betrag, der auf den ZVUntertitel gebucht wird.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public void buche( Benutzer benutzer, ZVUntertitel konto, float betrag ) throws RemoteException, ApplicationServerException;
	/**
	 * Budget von einem FBUnterkonto auf ein FBHauptkonto buchen.
	 * @param benutzer = Benutzer, der die Buchung durchgef�hrt hat. 
	 * @param FBUnterkonto, von dem der Betrag abgebucht wird.
	 * @param FBHauptkonto, das den abgebuchten Betrag erh�lt.
	 * @param Betrag, der von dem FBUnterkonto abgebucht wird und welchen das FBHauptkonto erh�lt.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public void buche( Benutzer benutzer, FBUnterkonto unter, FBHauptkonto haupt, float betrag ) throws RemoteException, ApplicationServerException;
	/**
	 * Budget von einem FBHauptkonto auf ein anderes FBHauptkonto buchen.
	 * @param benutzer = Benutzer, der die Buchung durchgef�hrt hat. 
	 * @param FBHauptkonto, von dem der Betrag abgebucht wird.
	 * @param FBHauptkonto, das den abgebuchten Betrag erh�lt.
	 * @param Betrag, der vom ersten FBHauptkonto abgebucht wird und welchen das zweite FBHauptkonto erh�lt.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public void buche( Benutzer benutzer, FBHauptkonto from, FBHauptkonto to, float betrag ) throws RemoteException, ApplicationServerException;
	/**
	 * Budget von einem FBHauptkonto auf ein FBUnterkonto buchen.
	 * @param benutzer = Benutzer, der die Buchung durchgef�hrt hat. 
	 * @param FBHauptkonto, von dem der Betrag abgebucht wird.
	 * @param FBUnterkonto, das den abgebuchten Betrag erh�lt.
	 * @param Betrag, der vom FBHauptkonto abgebucht wird und welchen das FBUnterkonto erh�lt.
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
	 * gibt alle Aktivit�ten die Rollen zugeordnet werden k�nnen.
	 * @return Aktivitaet-Array
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public Aktivitaet[] getAktivitaeten() throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt alle Rollen mit allen zugeh�rigen Aktivit�ten zur�ck
	 * @return Rollen-Array
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
  public Rolle[] getRollenFull () throws RemoteException, ApplicationServerException;
  
  /**
   * f�gt eine neue Zuordnung einer Aktivit�t zu einer Rolle
   * @param rolle - Id der Rolle
   * @param aktivitaet - Id der Aktivit�t
   * @throws RemoteException, ApplicationServerException
   * author robert
   */
  public void addRollenAktivitaet(int rolle, int aktivitaet) throws RemoteException, ApplicationServerException;
  
  /**
   * l�scht die Zuordnung einer Aktivit�t zu einer Rolle
   * @param rolle - Id der Rolle
   * @param aktivitaet - Id der Aktivit�t
   * @throws RemoteException, ApplicationServerException
   * author robert
   */
  public void delRollenAktivitaet(int rolle, int aktivitaet) throws RemoteException, ApplicationServerException;

	/**
	 * f�gt eine neue Rolle hinzu
	 * @param rolle - neue Rolle
	 * @return id - neue Id der Rolle
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public int addRolle(Rolle rolle) throws RemoteException, ApplicationServerException;
	
	/**
	 * aktualisiert eine Rolle
	 * @param editedRolle - Rolle mit �nderungen
	 * @param clientRolle - original Rolle beim Client
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public void setRolle(Rolle editedRolle, Rolle clientRolle) throws RemoteException, ApplicationServerException;
	
	/**
	 * l�scht eine Rolle
	 * @param rolle - zu l�schende Rolle
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public void delRolle(Rolle rolle) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt einen Benutzer anhand der �bergebenen Benutzernamens und Passworts
	 * @param user - Benutzername
	 * @param password
	 * @return zugeh�rige Benutzer
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public Benutzer getUser(String user, String password) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt nur die ZVKonten zur�ck ohne die Titel
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
	 * f�gt eine neue Kontenzuordnung hinzu
	 * @param fbKonto
	 * @param zvKonto
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public void addKontenZuordnung(FBHauptkonto fbKonto, ZVKonto zvKonto) throws RemoteException, ApplicationServerException;
	
	/**
	 * l�scht eine Kontenzuordnung
	 * @param fbKonto
	 * @param zvKonto
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public void delKontenZuordnung(FBHauptkonto fbKonto, ZVKonto zvKonto) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt das FBKonto mit der �bergebenen Id zur�ck
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
	 * Eine Firma in der Datenbank l�schen.
	 * @param Firma, die gel�scht werden soll.
	 * @return id der gel�schten Firma.
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */
	public int delFirma( Firma firma ) throws RemoteException, ApplicationServerException;
	
	/**
	 * Abfrage aller FBHauptkonten in der Datenbank, die zum angegebenem Institut geh�ren
	 * @param institut
	 * @return ArrayList mit FBHauptkonten
	 * @throws RemoteException, ApplicationServerException
	 */
	public ArrayList getNoPurposeFBHauptkonten( Institut institut ) throws RemoteException, ApplicationServerException;
	
	/**
	 *  Gibt die Benutzer eines Instituts mit einer bestimmten Rolle zur�ck
	 * @param i - Institut
	 * @param rollenId - Id der Rolle
	 * @return Benutzer-Array
	 * @throws RemoteException, ApplicationServerException
	 */
 	public Benutzer[] getUsersByRole(Institut i, int rollenId) throws RemoteException, ApplicationServerException;
 	
 	/**
 	 * Abfrage der Institute mit den dazugeh�rigen FBHauptkonten mit/ohne FBUnterkonten
 	 * @param subAccountsIncluded - true = FBUnterkonten mit einbeziehen, sonst false
 	 * @return Institut-Array
 	 * @throws RemoteException, ApplicationServerException
 	 */
 	public Institut[] getInstitutesWithAccounts (boolean subAccountsIncluded) throws RemoteException, ApplicationServerException;
 	
 	/**
 	 * Abfrage der Institute mit den dazugeh�rigen FBHauptkonten
 	 * @return Institut-Array
 	 * @throws RemoteException, ApplicationServerException
 	 */
 	public Institut[] getInstitutesWithMainAccounts () throws RemoteException, ApplicationServerException;

	/**
	 * Ermittelt den Betrag des noch nicht an FB-Konten verteilten Budgets 
	 * �ber alle _zweckungebundenen_ ZV-Konten
	 * @return der Betrag des verteilungsf�higen zweckungebundenen Budgets
	 * @throws RemoteException, ApplicationServerException
	 * author Mario
	 */	
	public float getAvailableNoPurposeBudget() throws RemoteException, ApplicationServerException;
	
	/**
	 * Ermittelt den gr��tm�glichen Betrag der dem �bergebenen FB-Hauptkonto
	 * zugewiesen werden kann
	 * @return der zuweisungsf�hige Betrag
	 * @throws RemoteException, ApplicationServerException
	 * author Mario
	 */
	public float getAvailableBudgetForAccount (FBHauptkonto account) throws RemoteException, ApplicationServerException;
	
	/**
	 * Ermittelt den Betrag des noch nicht an FB-Konten verteilten Budgets des �bergebenen ZV-Kontos   
	 * VORSICHT: Liefert nur korrekte Ergebnisse f�r _zweckgebundenen_ ZV-Konten!!!
	 * @return der Betrag des verteilungsf�higen Budgets
	 * @throws RemoteException, ApplicationServerException
	 * author Mario
	 */
	public float getAvailableAccountBudget (ZVKonto account) throws RemoteException, ApplicationServerException;
	
	/**
	 * Budget eines FBHauptkontos aktualisieren.
	 * @param b - Benutzer der die �nderung durchf�hrt
	 * @param acc - FBHauptkonto
	 * @param remmitance - Betrag
	 * @throws RemoteException, ApplicationServerException
	 * author Mario
	 */
 	public void setAccountBudget ( Benutzer b, FBHauptkonto acc, float remmitance ) throws RemoteException, ApplicationServerException;

	/**
	 * gibt ein Kostenarten-Array f�r die Standardbestellung 
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
	 * @param edited - ge�nderte Standardbestellung
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public void setBestellung(Benutzer benutzer, StandardBestellung original, StandardBestellung edited) throws RemoteException, ApplicationServerException;
	
	/**
	 * speichert die ASKBestellung
	 * @param original - orginale ASKBestellung 
	 * @param edited - ge�nderte ASKBestellung
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public void setBestellung(Benutzer benutzer, ASKBestellung original, ASKBestellung edited) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt eine StandardBestellung mit allen Objekten zur�ck
	 * @param id - BestellungId
	 * @return StandardBestellung mit der zugeh�rigen Id
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public StandardBestellung getStandardBestellung(int id) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt eine ASKBestellung mit allen Objekten zur�ck
	 * @param id - BestellungId
	 * @return ASKBestellung mit der zugeh�rigen Id
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public ASKBestellung getASKBestellung(int id) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt Bestellungen ggf. eines bestimmten Typs zur�ck
	 * @param filter
	 * @return ArrayList mit Bestellungen
	 * @throws RemoteException, ApplicationServerException
	 * author Mario
	 */	
	public ArrayList getBestellungen(int filter) throws RemoteException, ApplicationServerException;
	
	/**
	 * gibt alle Bestellungen zur�ck
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
	 * l�scht eine StandardBestellung komplett aus der Datenbank. Die Bestellung darf noch nicht in der Abwicklungsphase sein.
	 * @param delOrder - StandardBestellung die gel�scht werden soll
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public void delBestellung(StandardBestellung delOrder) throws RemoteException, ApplicationServerException;

	/**
	 * l�scht eine ASKBestellung komplett aus der Datenbank. Die Bestellung darf noch nicht in der Abwicklungsphase sein.
	 * @param delOrder - ASKBestellung die gel�scht werden soll
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public void delBestellung(ASKBestellung delOrder) throws RemoteException, ApplicationServerException;
	
	/**
	 * Eine Kleinbestellung in die Datenbank einf�gen.
	 * @param bestellung = Kleinbestellung, die erstellt werden soll. 
	 * @return Id der eingef�gten Bestellung. 
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */	
	public int addKleinbestellung(KleinBestellung bestellung) throws RemoteException, ApplicationServerException;
	
	/**
	 * Alle Kleinbestellung ausw�hlen.
	 * @return Liste mit Bestellungen. 
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */	
	public ArrayList getKleinbestellungen() throws RemoteException, ApplicationServerException;

	/**
	 * Alle gel�schten Kleinbestellung ausw�hlen.
	 * @return Liste mit gel�schten Bestellungen. 
	 * @throws RemoteException, ApplicationServerException
	 * author w.flat
	 */	
	public ArrayList getDelKleinbestellungen() throws RemoteException, ApplicationServerException;
		
	/**
	 * Eine Kleinbestellung l�schen. 
	 * @param Kleinbestellung, die gel�scht werden soll. 
	 * @return Id der gel�schten Bestellung. 
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
	 * gibt die Firma f�r eine ASK-Bestellung zur�ck
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
	 * Gibt eine ArrayList mit Inhalten f�r den entsprechenden Report zur�ck, siehe Reports Klasse f�r
	 * den Aufbau der ArrayListe
	 * @param typ	- Typen der Reports z.B. Reports.REPORT_1
	 * @param von - Datum f�r den Startpunkt
	 * @param bis - Datum f�r den Endpunkt
	 * @return ArrayListe mit den Angaben f�r den Report siehe n�he Infos zu den Objekten in Reports
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public ArrayList getReport(int typ, Date von, Date bis) throws RemoteException, ApplicationServerException;

	/**
	 * Gibt eine ArrayList mit allen �nderungen in der Datenbank zur�ck
	 * @param von - Datum f�r den Startpunkt
	 * @param bis - Datum f�r den Endpunkt
	 * @return	ArrayList
	 * @throws RemoteException, ApplicationServerException
	 * author robert
	 */
	public ArrayList getLogList(Date von, Date bis) throws RemoteException, ApplicationServerException;

}