package applicationServer;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;

import dbObjects.*;

public class ApplicationServerImpl implements ApplicationServer, Serializable {

	private Database db;

	int id;

	public ApplicationServerImpl(){

		// ToDo lesen aus .ini Datei
		//db = new Database("com.mysql.jdbc.Driver", "192.168.1.2", "fbmittelverwaltung", "mittelverwaltung");
		db = new Database("com.mysql.jdbc.Driver", "localhost", "fbmittelverwaltung", "mittelverwaltung");
	}


	/**
	 * Die id des Servers setzen.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Abfrage der id.
	 */
	public int getId() {
		return id;
	}


	public Benutzer login(String user, String password) throws ConnectionException, ApplicationServerException{

		db.connect(user);

		Benutzer b = db.selectUser(user, password);

		b.getRolle().setAktivitaeten( db.selectAktivitaeten( b.getRolle().getId() ) );

		TmpRolle[] tmpRollen = db.selectTempRollen(b.getId());

		for (int i=0; i < tmpRollen.length; i++){
			tmpRollen[i].setAktivitaeten(db.selectAktivitaeten(tmpRollen[i].getId()));
		}

		b.setTmpRollen(tmpRollen);

		return b;
	}

	public void logout()throws ConnectionException{
		db.disconnect();
	}

	public Benutzer[] getUsers () throws ApplicationServerException{
		return db.selectUsers();
	}

	public Institut[] getInstitutes () throws ApplicationServerException{
		return db.selectInstitutes();
	}

	public Rolle[] getRollen () throws ApplicationServerException{
		return db.selectRollen();
	}

	public static void main(String[] args) throws Exception {
		ApplicationServer as = new ApplicationServerImpl();
		as.login("m.schmitt","m.schmitt");
		Institut[] x = as.getInstitutes();
		System.out.println(x.length);
		for(int i=0; i<x.length;i++)
			System.out.println(x[i].toString());


		as.logout();
	}

	/**
	 * Ermittlung der HaushaltsjahrId vom aktuellem Jahr.
	 */
	public int getCurrentHaushaltsjahrId() throws ApplicationServerException {
		return db.selectHaushaltsjahrId();
	}
	
	 /**
	  * Abfrage der Institute mit den dazugehörigen FBHauptkonten und FBUnterkonten.
	  */
	 public Institut[] getInstitutesWithAccounts() throws ApplicationServerException {
			 return getInstitutesWithAccounts(true);
	 }
	 
	 /**
	  * Abfrage der Institute mit den dazugehörigen FBHauptkonten mit/ohne FBUnterkonten.
	  */
	 public Institut[] getInstitutesWithAccounts (boolean subAccountsIncluded) throws ApplicationServerException {
		 Institut[] instituts = db.selectInstitutes();	// Es werden alle Institute ermittelt
		 ArrayList hauptkonten;

		 if( instituts == null )		// Keine Institute vorhanden
			 return null;

		 // Schleife zur Ermittlung der FBHauptkonten eines Instituts
		 for( int i = 0; i < instituts.length; i++ ) {
			 if( instituts[i] == null )	// kein Institut
				 continue;

			 instituts[i].setHauptkonten( hauptkonten = db.selectFBHauptkonten( instituts[i] ) );

			 if (( subAccountsIncluded )&& ( hauptkonten != null )){
				 // Schleife zur Ermittlung aller FBUnterkonten von einem FBHauptkonto
				 for( int j = 0; j < hauptkonten.size(); j++ ) {
					 if( hauptkonten.get(j)== null )	// kein FBHauptkonto
						 continue;
					 // Ermittlung der Unterkonten vom Hauptkonto
					 ((FBHauptkonto)hauptkonten.get(j)).setUnterkonten( db.selectFBUnterkonten( instituts[i],
																		 (FBHauptkonto)hauptkonten.get(j) ) );
				 }
			 }
		 }

		 return instituts;
	 }
	 
	 /**
	  * Abfrage der Institute mit den dazugehörigen FBHauptkonten.
	  */
	 public Institut[] getInstitutesWithMainAccounts() throws ApplicationServerException {
		 return getInstitutesWithAccounts(false);
	 }
	 
	 public ArrayList getNoPurposeFBHauptkonten( Institut institut ) throws ApplicationServerException {
		 //return db.selectFBHauptkonten( institut );
		 return db.selectNoPurposeFBHauptkonten( institut );
	 }
	 
	 /**
	  * Budget eines FBHauptkontos aktualisieren.
	  */
	 public void setAccountBudget ( FBHauptkonto acc, float budget ) throws ApplicationServerException{

		 //TODO: Parameter nicht budget sondern überweisungebetrag, dann test auf maximal zuweisungsfähigen betrag?!
		 FBHauptkonto accOld = db.selectForUpdateFBHauptkonto(acc.getId());

		 if (accOld == null){
			 throw new ApplicationServerException(64);
			 //Konto existiert nicht mehr 64
		 }else if (accOld.getGeloescht()){
			 throw new ApplicationServerException(152);
			 //Konto wurde geloescht 152
		 }else if (acc.is(accOld)){
			 if (acc.getBudget() == accOld.getBudget()){
				 accOld.setBudget(budget);
				 if(db.updateFBHauptkonto(accOld)!=1){
					 throw new ApplicationServerException(155);
				 }
			 }else{
				 acc.setBudget(accOld.getBudget());
				 throw new ApplicationServerException(153);
				 //Budget wurde zwischenzeitlich aktualisiert 153
			 }
		 }else{
			 throw new ApplicationServerException(154);
			 // Konten stimmen nicht überein
		 }
		 
		 db.commit();
	 }
	 
	 public Benutzer[] getUsersByRole(Institut i, int rollenId) throws ApplicationServerException {
		 return db.selectUsersByRole(i, rollenId);
	 }
	 
	//TODO: Methode umbenennen -> getRemmitanceMax
	public float getAvailableBudgetForAccount (FBHauptkonto account) throws ApplicationServerException{

		Kontenzuordnung[] joins = null;

		if ((joins=account.getZuordnung())==null){
			if ((joins=db.selectKontenzuordnungen(account))== null){
				return 0;
			}else{
				account.setZuordnung(joins);
			}
		}

		if (joins.length > 0){
			if (joins[0].getZvKonto().isZweckgebunden()){
				return getAvailableAccountBudget(joins[0].getZvKonto());
			} else {
				return getNoPurposeBudgetAmount();
			}
		} else return 0;
	}
	
	public float getAvailableAccountBudget (ZVKonto account) throws ApplicationServerException{
		return db.selectTotalAccountBudget(account) - db.selectDistributedAccountBudget(account);
	}
	
	//TODO: Methode umbenennen -> getAvailableNoPurposeBudget
	public float getNoPurposeBudgetAmount () throws ApplicationServerException{
		return (db.selectNoPurposeZVBudgetSum() - db.selectNoPurposeFBBudgetSum());
	}
	
	/**
	 * Abfrage von Hauptkonten eines bestimmten Insituts.
	 * @return Liste FBHauptkonten, die zu einem bestimmten Institut angehören.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public ArrayList getFBHauptkonten( Institut institut ) throws ApplicationServerException {
		ArrayList hauptkonten;		// Liste mit den Hauptkonten

		hauptkonten = db.selectFBHauptkonten(institut);		// Abfrage der Hauptkonten
		for( int j = 0; j < hauptkonten.size(); j++ ) {		// Schleife zum Ermitteln der Kontenzuordnungen
			if( hauptkonten.get(j) == null )		// kein FBHauptkonto enthhalten
				continue;
			// Ermittlung der Kontenzuordnungen vom Hauptkonto
			((FBHauptkonto)hauptkonten.get(j)).setZuordnung( db.selectKontenzuordnungen( ((FBHauptkonto)hauptkonten.get(j)) ) );
		}
		return hauptkonten;
	}

	/**
	 * Abfrage von Unterkonten eines bestimmten Instituts von einem bestimmten Hauptkonto.
	 * @param institut
	 * @param hauptkonto
	 * @return FBUnterkonten
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public ArrayList getFBUnterkonten( Institut institut, FBHauptkonto hauptkonto ) throws ApplicationServerException {
		return db.selectFBUnterkonten( institut, hauptkonto );	// Die ermittelten Konten zurückgeben
	}
	
	/**
	 * Ein neues FBHauptkonto erstellen.
	 * @return kontoId des eingefügten Hauptkontos
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int addFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {
		if( konto == null )		// Kein konto übergeben
			return 0;

		if( db.existsFBKonto(konto) > 0 )				// Wenn dieses FBHauptkonto bereits existiert 
			throw new ApplicationServerException( 17 );	// dann kann man es nicht mehr erstellen
		
		try {
			db.setAutoCommit(false);
			int id = 0;
			if( (id = db.existsDeleteFBKonto( konto )) > 0 ) {	// Existiert schon ein gelöschtes FBKonto
				konto.setId( id );		// KontoId beim Konto setzen
				db.selectForUpdateFBHauptkonto( konto );	// Zum Aktualisieren auswählen
				return db.updateFBHauptkonto( konto );
			}
			
			return db.insertFBHauptkonto( konto );			// Sonst ein neues FBHauptkonto erstellen
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}

	/**
	 * Ein neues FBUnterkonto erstellen.
	 * @return kontoId des eingefügten Unterkontos
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int addFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {
		if( konto == null )		// Wenn kein Konto übergeben wurde
			return 0;
		if( db.existsFBKonto( konto ) > 0 )		// Wenn ein FBUnterkonto bereits existiert
			throw new ApplicationServerException( 19 );
		
		try {
			db.setAutoCommit(false);
			int id = 0;
			if( (id = db.existsDeleteFBKonto( konto )) > 0 ){	// Gibt es ein solches gelöschtes Konto
				konto.setId( id );		// Id des gelöschten Kontos an das neue Konto übergeben
				db.selectForUpdateFBUnterkonto( konto );	// Das Konto zum Aktualiseren auswählen
				return db.updateFBUnterkonto( konto );	// Das Konto aktualisieren
			}
			
			return db.insertFBUnterkonto( konto );			// Sonst ein neues FBUnterkonto erstellen
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}

	/**
	 * Ein FBHauptkonto löschen. Dabei werden auch die Unterkonten gelöscht.
	 * @return kontoId des gelöschten FBHauptkontos
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int delFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {
		if( konto == null )		// Kein FBHauptkonto
			return 0;
		
		if( db.countActiveBestellungen( konto ) > 0 )		// Wenn Bestellungen noch nicht abgeschlossen sind 
			throw new ApplicationServerException( 20 );
		if( db.countKontenzuordnungen( konto ) > 0 )		// Wenn Kontenzuordnungen existieren
			throw new ApplicationServerException( 21 );
	
		ArrayList temp = ((FBHauptkonto)konto).getUnterkonten();
		// Nachsehen ob sich bei den Unterkonten Fehler(Exception) ergeben
		if( temp != null ) {		// Gibt es Unterkonten
			for( int i = 0; i < temp.size(); i++ ) {
				if( temp.get(i) == null )	// Kein Unterkonto
					continue;
				if( db.countActiveBenutzer( (FBUnterkonto)temp.get(i) ) >  0 )		// Unterkonto ist einem Benutzer zugeordnet
					throw new ApplicationServerException( 22 );
				if( db.countActiveBestellungen( (FBUnterkonto)temp.get(i) ) > 0 )	// Es gibt Bestellungen 
					throw new ApplicationServerException( 20 );
			}
		}
		
		try {
			db.setAutoCommit(false);
			boolean delFBHauptkonto = true;	// Variable zur Ermittlung ob das FBHauptkonto ganz gelöscht werden sollte
			// Löschen der Unterkonten
			if( temp != null ) {	// Es gibt Unterkonten
				for( int i = 0; i < temp.size(); i++ ) {
					if( temp.get(i) == null )	// Kein Unterkonto
						continue;
					// Entscheidung ob nur das gelöscht-Flag gesetzt wird oder komplett aus der Datenbank
					// Wenn es abgeschlossene Bestellungen, Benutzer oder Buchungen noch gibt , die man zur Statistik braucht,
					// dann wird nur das Flag gesetzt, sonst wird ganz gelöscht
					if( (db.countBestellungen( (FBUnterkonto)temp.get(i) ) > 0) ||		
												(db.countBuchungen( (FBUnterkonto)temp.get(i) ) > 0) ||
												(db.countBenutzer( (FBUnterkonto)temp.get(i) ) > 0) ) {
						db.selectForUpdateFBUnterkonto( (FBUnterkonto)temp.get(i) );	// Zum Aktualisieren auswählen
						((FBUnterkonto)temp.get(i)).setGeloescht( true );		// Flag gelöscht setzen
						db.updateFBUnterkonto( (FBUnterkonto)temp.get(i) );		// Und dann aktualisieren
						delFBHauptkonto = false;
					} else {
						db.deleteFBKonto( (FBUnterkonto)temp.get(i) );			// Sonst ganz löschen
					}
				}
			}
			
			// Es gibt abgeschlossene Bestellungen noch oder Buchungen oder das FBHauptkonto
			// darf nicht ganz gelöscht werden, da es noch FBUnterkonten enthält, bei denen nur das Flag gesetzt ist
			if( (db.countBestellungen( konto ) > 0) || (db.countBuchungen( konto ) > 0) || !delFBHauptkonto ) {
				db.selectForUpdateFBHauptkonto( konto );	// Zum Aktualiseren auswählen
				konto.setGeloescht( true );					// Flag gelöscht setzen
				return db.updateFBHauptkonto( konto );		// Das FBHauptkonto aktualsieren
			}
			// Sonst wird das Hauptkonto ganz gelöscht
			if( db.deleteFBKonto( konto ) > 0)
				return konto.getId();
			else
				return 0;
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}
	
	/**
	 * Ein FBUnterkonto löschen.
	 * @return kontoId des gelöschten FBUnterkontos
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int delFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {
		if( konto == null )		// Kein FBUnterkonto
			return 0;

		if( db.countActiveBestellungen( konto ) > 0 )		// Wenn Bestellungen noch nicht abgeschlossen sind 
			throw new ApplicationServerException( 20 );
		if( db.countActiveBenutzer( konto ) >  0 )			// Unterkonto ist einem Benutzer zugeordnet
			throw new ApplicationServerException( 22 );
		
		try {
			db.setAutoCommit(false);
			// Es gibt abgeschlossene Bestellungen noch oder Buchungen oder Benutzer
			if( (db.countBestellungen( konto ) > 0) || (db.countBuchungen( konto ) > 0) || (db.countBenutzer( konto ) > 0) ) {
				db.selectForUpdateFBUnterkonto( konto );	// Das FBUnterkonto zum Aktualisieren auswählen
				konto.setGeloescht( true );					// Flag gellöscht setzen
				return db.updateFBUnterkonto( konto );		// Das FBUnterkonto in der Datenbank aktualisieren
			}
			// Sonst wird das FBUnterkonto ganz gelöscht
			if( db.deleteFBKonto( konto ) > 0)
				return konto.getId();
			else
				return 0;
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}

	/**
	 * Ein FBHauptkonto aktualisieren.
	 * @param FBHauptkonto, welches aktualisiert werden soll
	 * @return kontoId des aktualisierten FBHauptkontos
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int setFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {
		if( konto == null )		// Kein Konto angegeben
			return 0;
		
		try {
			db.setAutoCommit(false);
			FBHauptkonto old = db.selectForUpdateFBHauptkonto( konto );	// Zum aktualisieren auswählen
			if( old == null ) {			// Kein FBHauptonto gefunden
				throw new ApplicationServerException( 16 );
			}	
			if( old.getGeloescht() ) {	// Das FBHauptonto ist gelöscht
				throw new ApplicationServerException( 23 );
			}
			if( old.equals( konto ) ) {						// Die FBKontos [DB-Application] sind gleich
				return db.updateFBHauptkonto( konto );	// Dann kann man das Konto aktualisieren
			}
			if(db.existsFBKonto(konto) > 0)	{				// Wenn das Konto bereits exisitiert
				throw new ApplicationServerException(17);
			}
			// Wenn es Bestellungen, Buchungen, Benutzer gibt
			if( (db.countBestellungen( konto ) > 0) || (db.countBuchungen( konto ) > 0) || (db.countBenutzer( konto ) > 0) ) {
				throw new ApplicationServerException( 40 );		// Das Konto kann nicht verändert werden, da es 
																// zu Inkonsistenzen kommen kann
			}
			ArrayList temp = ((FBHauptkonto)konto).getUnterkonten();
			// Nachsehen ob sich bei den Unterkonten Fehler(Exception) ergeben
			if( temp != null ) {		// Es gibt Unterkonten
				for( int i = 0; i < temp.size(); i++ ) {
					if( temp.get(i) == null )	// Kein Unterkonto
						continue;
					if( (db.countBestellungen((FBUnterkonto)temp.get(i)) > 0) ||
											(db.countBuchungen((FBUnterkonto)temp.get(i)) > 0) ||
											(db.countBenutzer((FBUnterkonto)temp.get(i)) > 0) ) {
						throw new ApplicationServerException( 40 );		// Das Konto kann nicht verändert werden, da es 
																		// zu Inkonsistenzen kommen kann
					}
				}
			}
			
			// Alle Unterkonten aktualisieren 
			if( temp != null ) {		// Es gibt Unterkonten
				for( int i = 0; i < temp.size(); i++ ) {
					if( temp.get(i) == null )	// Kein Unterkonto
						continue;
					db.updateFBUnterkonto( (FBUnterkonto)temp.get(i) );
				}
			}
			
			return db.updateFBHauptkonto( konto );	// Sonst kann das FBHauptkonto aktualisiert werden
		} finally {
			db.setAutoCommit(true);
			db.commit();			
		}
	}

	/**
	 * Ein FBUnterkonto aktualisieren.
	 * @param FBUnterkonto, welches aktualisiert werden soll
	 * @return kontoId des aktualisierten FBUnterkontos
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int setFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {
		if( konto == null )		// Kein Konto angegeben
			return 0;
		try {
			db.setAutoCommit(false);
			FBUnterkonto old = db.selectForUpdateFBUnterkonto( konto );	// Zum aktualisieren auswählen
			if( old == null ) {			// Kein FBUnterkonto gefunden
				throw new ApplicationServerException( 18 );
			}
			if( old.getGeloescht() ) {	// Das FBUnterkonto ist gelöscht
				throw new ApplicationServerException( 24 );
			}
			if( old.equals( konto ) ) {		// Wenn die Konten gleich sind
				return db.updateFBUnterkonto( konto );
			}
			if( db.existsFBKonto( konto ) > 0 ) {			// Das neue Konto existiert bereits
				throw new ApplicationServerException( 19 );
			}
			if( (db.countBestellungen( konto ) > 0) || (db.countBuchungen( konto ) > 0) || (db.countBenutzer( konto ) > 0) ) {
				throw new ApplicationServerException( 40 );		// Das Konto kann nicht verändert werden, da es 
																// zu Inkonsistenzen kommen kann
			}
			
			return db.updateFBUnterkonto( konto );		// Sonst kann das FBUnterkonto aktualisiert werden
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}
	
	/**
	 * Budget von einem FBHauptkonto auf ein FBUnterkonto buchen.
	 * @param FBHauptkonto, von dem der Betrag abgebucht wird.
	 * @param FBUnterkonto, das den abgebuchten Betrag erhält.
	 * @param Betrag, der vom FBHauptkonto abgebucht wird und welchen das FBUnterkonto erhält.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public void buche( FBHauptkonto haupt, FBUnterkonto unter, float betrag ) throws ApplicationServerException {
		if( haupt == null || unter == null )	// Ein Konto wurde nicht angegeben
			return;
		try {
			db.setAutoCommit(false);
			FBHauptkonto oldHaupt = db.selectForUpdateFBHauptkonto( haupt );	// Das FBHauptkonto auswählen
			if( oldHaupt == null )				// Wenn kein FBHauptkonto in der Datenbank gefunden
				throw new ApplicationServerException( 16 );
			if( oldHaupt.getGeloescht() )		// Wenn das FBHauptkonto gelöscht ist
				throw new ApplicationServerException( 23 );
			if( !oldHaupt.equals( haupt ) )		// Wenn die FBHauptkonten [DB-Application] nicht gleich sind
				throw new ApplicationServerException( 25 );
			// Wenn die Budgets oder Dispolimits nicht übereinstimmen
			if( haupt.getBudget() != oldHaupt.getBudget() || haupt.getDispoLimit() != oldHaupt.getDispoLimit() )
				throw new ApplicationServerException( 26 );
			
			FBUnterkonto oldUnter = db.selectForUpdateFBUnterkonto( unter );		// Das FBUnterkonto auswählen
			if( oldUnter == null )				// Wenn kein FBUnterkonto in der Datenbank gefunden
				throw new ApplicationServerException( 18 );
			if( oldUnter.getGeloescht() )		// Wenn das FBUnterkonto gelöscht ist
				throw new ApplicationServerException( 24 );
			if( !oldUnter.equals( unter ) )		// Wenn die FBUnterkonten [DB-Application] nicht gleich sind
				throw new ApplicationServerException( 27 );
			
			// Das FBHauptkonto aktualisieren
			haupt.setBudget( haupt.getBudget() - betrag );	// Betrag abbuchen
			db.updateFBHauptkonto( haupt );			// In der Datenbank aktualisieren
			// Das FBUnterkonto aktualisieren
			unter.setBudget( unter.getBudget() + betrag );	// Betrag gutschreiben
			db.updateFBUnterkonto( unter );			// In der Datenbank aktualisieren
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}
	
	/**
	 * Budget von einem FBHauptkonto auf ein anderes FBHauptkonto buchen.
	 * @param FBHauptkonto, von dem der Betrag abgebucht wird.
	 * @param FBHauptkonto, das den abgebuchten Betrag erhält.
	 * @param Betrag, der vom ersten FBHauptkonto abgebucht wird und welchen das zweite FBHauptkonto erhält.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public void buche( FBHauptkonto from, FBHauptkonto to, float betrag ) throws ApplicationServerException {
		if( from == null || to == null )
			return;
		try {
			db.setAutoCommit(false);
			FBHauptkonto oldFrom = db.selectForUpdateFBHauptkonto( from );	// Das FBHauptkonto auswählen
			if( oldFrom == null )				// Wenn kein FBHauptkonto in der Datenbank gefunden
				throw new ApplicationServerException( 16 );
			if( oldFrom.getGeloescht() )		// Wenn das FBHauptkonto gelöscht ist
				throw new ApplicationServerException( 23 );
			if( !oldFrom.equals( from ) )		// Wenn die FBHauptkonten [DB-Application] nicht gleich sind
				throw new ApplicationServerException( 25 );
			// wenn die Budgets oder Dispolimits nicht übereinstimmen
			if( oldFrom.getBudget() != from.getBudget() || oldFrom.getDispoLimit() != from.getDispoLimit() )
				throw new ApplicationServerException( 26 );
			
			FBHauptkonto oldTo = db.selectForUpdateFBHauptkonto( to );	// Das FBHauptkonto auswählen
			if( oldTo == null )				// Wenn kein FBHauptkonto in der Datenbank gefunden
				throw new ApplicationServerException( 16 );
			if( oldTo.getGeloescht() )		// Wenn das FBHauptkonto gelöscht ist
				throw new ApplicationServerException( 23 );
			if( !oldTo.equals( to ) )		// Wenn die FBHauptkonten [DB-Application]nicht gleich sind
				throw new ApplicationServerException( 25 );

			// Das erste FBHauptkonto aktualisieren
			from.setBudget( from.getBudget() - betrag );	// Betrag abbuchen
			db.updateFBHauptkonto( from );					// In der Datenbank aktualisieren
			// Das zweite FBHauptkonto aktualisieren
			to.setBudget( to.getBudget() + betrag );		// Betrag gutschreiben
			db.updateFBHauptkonto( to );					// In der Datenbank aktualisieren
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}
	
	/**
	 * Budget von einem FBUnterkonto auf ein FBHauptkonto buchen.
	 * @param FBUnterkonto, von dem der Betrag abgebucht wird.
	 * @param FBHauptkonto, das den abgebuchten Betrag erhält.
	 * @param Betrag, der von dem FBUnterkonto abgebucht wird und welchen das FBHauptkonto erhält.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public void buche( FBUnterkonto unter, FBHauptkonto haupt, float betrag ) throws ApplicationServerException {
		if( unter == null || haupt == null )
			return;
		try {
			db.setAutoCommit(false);
			FBUnterkonto oldUnter = db.selectForUpdateFBUnterkonto( unter );	// Das FBUnterkonto auswählen
			if( oldUnter == null )				// Wenn kein FBUnterkonto in der Datenbank gefunden
				throw new ApplicationServerException( 18 );
			if( oldUnter.getGeloescht() )		// Wenn das FBUnterkonto gelöscht ist
				throw new ApplicationServerException( 24 );
			if( !oldUnter.equals( unter ) )		// Wenn die FBUnterkonten [DB-Application] nicht gleich sind
				throw new ApplicationServerException( 27 );
			// wenn die Budgets nicht übereinstimmen
			if( oldUnter.getBudget() != unter.getBudget() )
				throw new ApplicationServerException( 28 );
			
			FBHauptkonto oldHaupt = db.selectForUpdateFBHauptkonto( haupt );	// Das FBHauptkonto auswählen
			if( oldHaupt == null )					// Wenn kein FBHauptkonto in der Datenbank gefunden
				throw new ApplicationServerException( 16 );
			if( oldHaupt.getGeloescht() )			// Wenn das FBHauptkonto gelöscht ist
				throw new ApplicationServerException( 23 );
			if( !oldHaupt.equals( haupt ) )			// Wenn die FBHauptkonten [DB-Application] nicht gleich sind
				throw new ApplicationServerException( 25 );
			
			// Das FBUnterkonto aktualisieren
			unter.setBudget( unter.getBudget() - betrag );	// Betrag gutschreiben
			db.updateFBUnterkonto( unter );			// In der Datenbank aktualisieren
			// Das FBHauptkonto aktualisieren
			haupt.setBudget( haupt.getBudget() + betrag );	// Betrag abbuchen
			db.updateFBHauptkonto( haupt );			// In der Datenbank aktualisieren
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}
	
	/**
	 * Abfrage der ZVKonten mit den dazugehörigen ZVTiteln und ZVUntertiteln.
	 * @return Liste mit den ZVKonten
	 * @author w.flat
	 */
	public ArrayList getZVKonten() throws ApplicationServerException {
		ArrayList zvKonten = db.selectZVKonten();	// Es werden alle ZVKonten ermittelt
		ArrayList zvTitel;		// Liste für die ZVTitel
		
		if( zvKonten == null )	// Keine ZVKonten vorhanden
			return null;

		// Schleife zur Ermittlung der ZVTitel eines ZVKontos
		for( int i = 0; i < zvKonten.size(); i++ ) {
			if( zvKonten.get(i) == null )	// kein ZVKonto
				continue;
			// Abfrage der zugehörigen ZVTitel für das ZVKonto
			((ZVKonto)zvKonten.get(i)).setSubTitel( zvTitel = db.selectZVTitel( (ZVKonto)zvKonten.get(i) ) );
			if( zvTitel == null )		// Keine ZVTitel
				continue;

			// Schleife zur Ermittlung aller ZVUntertitel von einem ZVTitel
			for( int j = 0; j < zvTitel.size(); j++ ) {
				if( zvTitel.get(j) == null )	// kein ZVTitel
					continue;
				// Ermittlung der zugehörigen ZVUntertitel vom ZVTitel
				((ZVTitel)zvTitel.get(j)).setSubUntertitel( db.selectZVUntertitel( (ZVTitel)zvTitel.get(j) ) );
			}
		}

		return zvKonten;	// Rückgabe der ermittelten ZVKonten
	}
	
	/**
	 * Ein neues ZVKonto in die Datenbank einfügen.
	 * @param ZVKonto, welches eingefügt werden soll.
	 * @return kontoId vom eingefügten ZVKonto
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int addZVKonto( ZVKonto zvKonto ) throws ApplicationServerException {
		if( zvKonto == null )		// Wenn kein ZVKonto
			return 0;
		if( db.existsZVKonto( zvKonto ) > 0 )		// Wenn das ZVKonto bereits existiert
			throw new ApplicationServerException( 11 );
		if( !zvKonto.isTGRKonto() ) {			// Ist das ZVKonto ein TitelGruppenKonto
			if( db.existsZVTitel( (ZVTitel)zvKonto.getSubTitel().get(0) ) > 0 )		// Wenn das ZVTitel bereits existiert
				throw new ApplicationServerException( 11 );
		}
		
		try {
			db.setAutoCommit(false);
			int zvKontoId = 0;
			if( (zvKontoId = db.existsDeleteZVKonto( zvKonto )) > 0 ){	// Wenn ein gelöschtes ZVKonto exitiert, dann aktualisieren
				zvKonto.setId( zvKontoId );				// Im neuem ZVKonto id setzen
				db.selectForUpdateZVKonto( zvKonto );	// Das gelöschte ZVKonto zum Aktualiseren auswählen
				if( zvKonto.isTGRKonto() )				// Ist das ZVKonto ein Titel-Gruppen-Konto
					return db.updateZVKonto( zvKonto );	// Dann nur das ZVKonto aktualisieren
				else {		// Sonst muss man auch den ZVTitel aktualisieren oder erstellen
					zvKontoId = db.updateZVKonto( zvKonto );	// Das ZVKonto aktualisieren
					// Wenn ein gelöschtes ZVTitel exitiert, dann aktualisieren
					int zvTitelId = db.existsDeleteZVTitel( (ZVTitel)zvKonto.getSubTitel().get(0) ); 
					if( zvTitelId > 0 ) {
						((ZVTitel)zvKonto.getSubTitel().get(0)).setId( zvTitelId );		// Im neuen ZVTitel, die id setzen
						db.selectForUpdateZVTitel( (ZVTitel)zvKonto.getSubTitel().get(0) );	// ZVTitel zum Aktualisieren auswählen
						db.updateZVTitel( (ZVTitel)zvKonto.getSubTitel().get(0) );		// Den ZVTitel aktualisieren
					} else {
						db.insertZVTitel( (ZVTitel)zvKonto.getSubTitel().get(0) );		// Sonst neuen ZVTitel erstellen
					}
					
					return zvKontoId;		// Rückgabe der ZVKontoId
				}
			}
			
			if( zvKonto.isTGRKonto() )	// Wenn ein TGR-Konto
				return db.insertZVKonto( zvKonto );	// dann nur ZVKonto erstellen
			else {
				zvKontoId = db.insertZVKonto( zvKonto );	// Sonst ZVKonto erstellen
				if( zvKontoId > 0 ) {	// und wenn erstellt wurde
					zvKonto.setId( zvKontoId );		// dann zvKontoId setzen
					db.insertZVTitel( (ZVTitel)zvKonto.getSubTitel().get(0) );	// und dann den zugehörigen ZVTitel erstellen
				}
				
				return zvKontoId;	// Und die ZVKontoId zurückgeben
			}
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}
	
	/**
	 * Abfrage der Id von einem ZVTitel.
	 * @param ZVTitel, welcher abgefragt werden soll
	 * @return ZVTitelId des ZVTitels
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int getZVTitelId( ZVTitel zvTitel ) throws ApplicationServerException {
		return db.existsZVTitel( zvTitel );
	}

	/**
	 * Einen neuen ZVTitel in die Datenbank erstellen.
	 * @param ZVTitel, welcher erstellt werden soll
	 * @return ZVTitelId vom erstellten ZVTitel
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int addZVTitel( ZVTitel zvTitel ) throws ApplicationServerException {
		if( zvTitel == null )		// Wenn kein ZVTitel
			return 0;
		if( db.existsZVTitel( zvTitel ) > 0 )		// Wenn der ZVTitel bereits existiert
			throw new ApplicationServerException( 11 );
			
		try {
			db.setAutoCommit(false);
			int id = 0;
			if( (id = db.existsDeleteZVTitel( zvTitel )) > 0 )	{	// Wenn ein gelöschtes ZVTitel exitiert, dann aktualisieren
				zvTitel.setId( id );		// Id setzen
				db.selectForUpdateZVTitel( zvTitel );	// ZVTitel zum Aktualisieren auswählen
				return db.updateZVTitel( zvTitel );		// ZVTitel Aktualisieren
			}
			
			return db.insertZVTitel( zvTitel );		// Sonst einen neuen ZVTitel erstellen
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}

	/**
	 * Einen neuen ZVUntertitel in die Datenbank erstellen.
	 * @param ZVUntertitel, welcher erstellt werden soll
	 * @return ZVUntertitelId vom eingefügtem ZVUntertitel
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int addZVUntertitel( ZVUntertitel zvUntertitel ) throws ApplicationServerException {
		if( zvUntertitel == null )		// Wenn kein ZVUntertitel
			return 0;
		if( db.existsZVUntertitel( zvUntertitel ) > 0 )		// Wenn der ZVUntertitel bereits existiert
			throw new ApplicationServerException( 11 );
			
		try {
			db.setAutoCommit(false);
			int id = 0;
			// Wenn ein gelöschtes ZVUntertitel exitiert, dann aktualisieren
			if( (id = db.existsDeleteZVUntertitel( zvUntertitel )) > 0 ) {
				zvUntertitel.setId( id );		// dann die ZVUntertitelId setzen
				db.selectForUpdateZVUntertitel( zvUntertitel );	// Zum Aktualisieren auswählen
				return db.updateZVUntertitel( zvUntertitel );	// ZVUntertitel auswählen
			}
			
			return db.insertZVUntertitel( zvUntertitel );	// Sonst neuen ZVUntertitel erstellen
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}

	/**
	 * Ein ZVKonto aus der Datenbank löschen. <br>
	 * Dabei müssen auch alle dazugehörigen ZVTitel und ZVUntertitel gelöscht werden.
	 * @return ZVKontoId vom gelöschten ZVKonto
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int delZVKonto( ZVKonto zvKonto ) throws ApplicationServerException {
		if( zvKonto == null )	// Kein ZVKonto
			return 0;
		
		if( db.countKontenzuordnungen( zvKonto ) > 0 )		// Wenn es Kontenzuordnungen gibt
			throw new ApplicationServerException(21);
		
		ArrayList zvTitel = zvKonto.getSubTitel();
		// Nachsehen ob es beim Löschen von ZVTiteln Fehler entstehen
		if( zvTitel != null ) {		// Gibt es ZVTitel
			for( int i = 0; i < zvTitel.size(); i++ ) {
				if( zvTitel.get(i) == null )	// kein zvTitel
					continue;
				if( db.countActiveBestellungen( (ZVTitel)zvTitel.get(i) ) > 0 )		// Wenn es noch laufende Bestellungen gibt
					throw new ApplicationServerException(20);

				ArrayList zvUntertitel = ((ZVTitel)zvTitel.get(i)).getSubUntertitel();
				// Nachsehen ob es beim Löschen von ZVUntertiteln Fehler entstehen
				if( zvUntertitel != null ) {	// Gibt es ZVUntertitel
					for( int j = 0; j < zvUntertitel.size(); j++ ) {
						if( zvUntertitel.get(j) == null )	// kein ZVUntertitel
							continue;
						// Wenn es noch laufende Bestellungen gibt
						if( db.countActiveBestellungen( (ZVUntertitel)zvUntertitel.get(j) ) > 0 )
							throw new ApplicationServerException(20);
					}	// Ende for zvUntertitel
				}	// Ende if( zvUntertitel != null )
			}	// Ende for zvTitel
		}	// Ende if( zvTitel != null )
		
		try {
			db.setAutoCommit(false);
			boolean delZVKonto = true;	// Variable um festzustellen ob das ZVKonto ganz gelöscht werden sollte
			// Alle ZVTitel und die dazugehörigen ZVUntertitel löschen
			// Dabei wird geschaut ob noch irgendwelche abgeschlossenen Bestellungen und Buchungen gibt
			// Wenn ja dann nur der Flag-gelöscht gesetzt. Wenn nein dann wird ganz gelöscht 
			if( zvTitel != null ) {		// Gibt es ZVTitel
				for( int i = 0; i < zvTitel.size(); i++ ) {
					if( zvTitel.get(i) == null )	// kein zvTitel
						continue;
					boolean delZVTitel = true; 	// Variable um festzustellen ob der ZVTitel ganz gelöscht werden sollte
					ArrayList zvUntertitel = ((ZVTitel)zvTitel.get(i)).getSubUntertitel();
					// Löschen von ZVUntertiteln
					if( zvUntertitel != null ) {	// Gibt es ZVUntertitel
						for( int j = 0; j < zvUntertitel.size(); j++ ) {
							if( zvUntertitel.get(j) == null )	// kein ZVUntertitel
								continue;
							// Wenn es abgeschlossene Bestellungen oder Buchungen gibt
							if( (db.countBestellungen( (ZVUntertitel)zvUntertitel.get(j) ) > 0) ||
												(db.countBuchungen( (ZVUntertitel)zvUntertitel.get(j) ) > 0) ) {
								// ZVUntertitel zum Aktualisieren auswählen
								db.selectForUpdateZVUntertitel( (ZVUntertitel)zvUntertitel.get(j) );
								((ZVUntertitel)zvUntertitel.get(j)).setGeloescht( true );	// Flag-Gelöscht setzen
								db.updateZVUntertitel( (ZVUntertitel)zvUntertitel.get(j) );	// ZVUntertitel aktualisieren
								delZVTitel = false;		// Der ZVTitel darf nicht ganz gelöscht werden
							} else {
								// Sonst den ZVUntertitel aus der Datenbank löschen
								db.deleteZVUntertitel( (ZVUntertitel)zvUntertitel.get(j) );
							}
						}	// Ende for zvUntertitel
					}	// Ende if( zvUntertitel != null )
					
					// Wenn es abgeschlossene Bestellungen oder Buchungen gibt
					if( (db.countBestellungen( (ZVTitel)zvTitel.get(i) ) > 0) ||
														(db.countBuchungen( (ZVTitel)zvTitel.get(i) ) > 0) || !delZVTitel) {
						// ZVTitel zum Aktualisieren auswählen
						db.selectForUpdateZVTitel( (ZVTitel)zvTitel.get(i) );
						((ZVTitel)zvTitel.get(i)).setGeloescht( true );	// Flag-Gelöscht setzen
						db.updateZVTitel( (ZVTitel)zvTitel.get(i) );	// ZVTitel aktualisieren
						delZVKonto = false;		// Das ZVKonto darf nicht ganz gelöscht werden
					} else {
						// Sonst den ZVTitel aus der Datenbank löschen
						db.deleteZVTitel( (ZVTitel)zvTitel.get(i) );
					}
				}	// Ende for zvTitel
			}	// Ende if( zvTitel != null )
			
			// Und zum Schluss das ZVKonto löschen
			if( delZVKonto ) {		// Wenn das ZVKonto ganz gelöscht werden sollte
				if( db.deleteZVKonto( zvKonto ) > 0 )	// Wenn der Löschvorgang erfolgreich war
					return zvKonto.getId();
				else
					return 0;
			} else {				// Sonst nur das Flag-Gelöscht setzen
				db.selectForUpdateZVKonto( zvKonto );		// Das ZVKonto zum Aktualisieren auswählen
				zvKonto.setGeloescht( true );				// Flag-Gelöscht setzen
				return db.updateZVKonto( zvKonto );	// Das ZVKonto aktualisieren und ZVKontoId zurückgeben
			}
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}
	
	/**
	 * Überprüfung ob ein ZVKonto zweckgebunden sein kann. <br>
	 * Dabei wird ermittelt ob mehr als ein ZVKonto zu dem FBKonto einer Kontozuordnung existiert.
	 * @param ZVKonto für welches die Überprüfung durchgeführt werden soll
	 * @return Zahl > 0, wenn das ZVKonto nicht zweckgebunden sein kann. Sonst Zahl = 0.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int getNumberOfKontenzuordnungen( ZVKonto zvKonto ) throws ApplicationServerException {
		return db.countZVKonten( zvKonto );
	}

	/**
	 * Einen ZVTitel in der Datenbank löschen. Dabei müssen auch alle ZVUntertitel gelöscht werden.
	 * @param ZVTitel, welcher gelöscht werden sollte.
	 * @return ZVTitelId vom gelöschten ZVTitel
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int delZVTitel( ZVTitel zvTitel ) throws ApplicationServerException {
		if( zvTitel == null )	// Kein ZVTitel
			return 0;
		
		if( db.countActiveBestellungen( zvTitel ) > 0 )		// Wenn es Kontenzuordnungen gibt
			throw new ApplicationServerException(21);

		ArrayList zvUntertitel = zvTitel.getSubUntertitel();
		// Nachsehen ob es beim Löschen von ZVUntertiteln Fehler entstehen
		if( zvUntertitel != null ) {	// Gibt es ZVUntertitel
			for( int j = 0; j < zvUntertitel.size(); j++ ) {
				if( zvUntertitel.get(j) == null )	// kein ZVUntertitel
					continue;
				// Wenn es noch laufende Bestellungen gibt
				if( db.countActiveBestellungen( (ZVUntertitel)zvUntertitel.get(j) ) > 0 )
					throw new ApplicationServerException(20);
			}	// Ende for zvUntertitel
		}	// Ende if( zvUntertitel != null )
		
		try {
			db.setAutoCommit(false);
			// Löschen von ZVUntertiteln 
			boolean delZVTitel = true; 	// Variable um festzustellen ob der ZVTitel ganz gelöscht werden sollte
			if( zvUntertitel != null ) {	// Gibt es ZVUntertitel
				for( int j = 0; j < zvUntertitel.size(); j++ ) {
					if( zvUntertitel.get(j) == null )	// kein ZVUntertitel
						continue;
					// Wenn es abgeschlossene Bestellungen oder Buchungen gibt
					if( (db.countBestellungen( (ZVUntertitel)zvUntertitel.get(j) ) > 0) ||
													(db.countBuchungen( (ZVUntertitel)zvUntertitel.get(j) ) > 0) ) {
						// ZVUntertitel zum Aktualisieren auswählen
						db.selectForUpdateZVUntertitel( (ZVUntertitel)zvUntertitel.get(j) );
						((ZVUntertitel)zvUntertitel.get(j)).setGeloescht( true );	// Flag-Gelöscht setzen
						db.updateZVUntertitel( (ZVUntertitel)zvUntertitel.get(j) );	// ZVUntertitel aktualisieren
						delZVTitel = false;		// Der ZVTitel darf nicht ganz gelöscht werden
					} else {
						// Sonst den ZVUntertitel aus der Datenbank löschen
						db.deleteZVUntertitel( (ZVUntertitel)zvUntertitel.get(j) );
					}
				}	// Ende for zvUntertitel
			}	// Ende if( zvUntertitel != null )
			
			// Und zum Schluss den ZVTitel löschen
			if( (db.countBestellungen( zvTitel ) > 0) || (db.countBuchungen( zvTitel ) > 0) || !delZVTitel ) {
				db.selectForUpdateZVTitel( zvTitel );	// ZVTitel zum Aktualisieren auswählen
				zvTitel.setGeloescht( true );			// Flag-Gelöscht setzen
				return db.updateZVTitel( zvTitel );	// ZVTitel aktualisieren
			}
			
			// Sonst wird der ZVTitel aus der Datenbank gelöscht
			if( db.deleteZVTitel( zvTitel ) > 0 )	// War der Löschvorgang erfolgreich
				return zvTitel.getId();	// ZVTitelId zurückgeben
			else
				return 0;				// Sonst Rückgabe = 0
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}

	/**
	 * Einen ZVUntertitel in der Datenbank löschen.
	 * @param ZVUntertitel der gelöscht werden sollte.
	 * @return ZVUntertitelId vom gelöschten ZVUntertitel.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int delZVUntertitel( ZVUntertitel zvUntertitel ) throws ApplicationServerException {
		if( zvUntertitel == null )	// Kein ZVTitel
			return 0;
		
		if( db.countActiveBestellungen( zvUntertitel ) > 0 )		// Wenn es Kontenzuordnungen gibt
			throw new ApplicationServerException(21);

		try {
			db.setAutoCommit(false);
			// Wenn es abgeschlossene Bestellungen oder Buchungen gibt
			if( (db.countBestellungen( zvUntertitel ) > 0) || (db.countBuchungen( zvUntertitel ) > 0) ) {
				db.selectForUpdateZVUntertitel( zvUntertitel );		// ZVUntertitel zum Aktualisieren auswählen
				zvUntertitel.setGeloescht( true );					// Flag-Gelöscht setzen
				return db.updateZVUntertitel( zvUntertitel );	// ZVUntertitel aktualisieren
			}
			
			// Sost wird der ZVUntertitel aus der Datenbank gelöscht
			if( db.deleteZVUntertitel( zvUntertitel ) > 0 )		// Wenn der Löschvorgang erfolgreich war
				return zvUntertitel.getId();		// ZVUntertitelId zurückgeben
			else
				return 0;		// Sonst Rückgabe = 0
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}

	/**
	 * Ein ZVKonto in der Datenbank aktualisieren. Es werden auch alle <br>
	 * ZVTitel und ZVUntertitel aktualisiert, wenn die Änderung diese betreffen.
	 * @param ZVKonto, das aktualisiert werden soll.
	 * @return zvKontoId des ZVKontos
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int setZVKonto( ZVKonto zvKonto ) throws ApplicationServerException {
		if( zvKonto == null )		// Wenn kein ZVKonto angegeben
			return 0;
		try {
			db.setAutoCommit(false);
			ZVKonto old = db.selectForUpdateZVKonto( zvKonto );	// Das ZVKonto in der Datenbank zum Aktualisieren auswählen
			if( old == null )			// Wenn kein ZVKonto in der Datenbank gefunden
				throw new ApplicationServerException( 10 );
			if( old.getGeloescht() )	// Wenn ein ZVKonto gelöscht ist
				throw new ApplicationServerException( 29 );
			if( old.equals( zvKonto ) )	// Wenn die ZVKontos gleich sind, dann betrift die Änderung nur das ZVKonto
				return db.updateZVKonto( zvKonto );
			if( db.existsZVKonto( zvKonto ) > 0 )	// Wenn das neue ZVKonto bereits existiert
				throw new ApplicationServerException( 10 );
			
			ArrayList zvTitel = zvKonto.getSubTitel();
			// Nachsehen ob es beim Ändern von ZVTiteln Fehler entstehen
			if( zvTitel != null ) {		// Gibt es ZVTitel
				for( int i = 0; i < zvTitel.size(); i++ ) {
					if( zvTitel.get(i) == null )	// kein zvTitel
						continue;
					// Wenn es Bestellungen oder Buchungen gibt, die diesen ZVTitel verwenden
					if( db.countBestellungen( (ZVTitel)zvTitel.get(i) ) > 0 || db.countBuchungen( (ZVTitel)zvTitel.get(i) ) > 0 )
						throw new ApplicationServerException(41);
					
					ArrayList zvUntertitel = ((ZVTitel)zvTitel.get(i)).getSubUntertitel();
					// Nachsehen ob es beim Ändern von ZVUntertiteln Fehler entstehen
					if( zvUntertitel != null ) {	// Gibt es ZVUntertitel
						for( int j = 0; j < zvUntertitel.size(); j++ ) {
							if( zvUntertitel.get(j) == null )	// kein ZVUntertitel
								continue;
							// Wenn es Bestellungen oder Buchungen gibt, die diesen ZVUntertitel verwenden
							if( db.countBestellungen( (ZVUntertitel)zvUntertitel.get(j) ) > 0 ||
														db.countBuchungen( (ZVUntertitel)zvUntertitel.get(j) ) > 0 )
								throw new ApplicationServerException(41);
						}	// Ende for zvUntertitel
					}	// Ende if( zvUntertitel != null )
				}	// Ende for zvTitel
			}	// Ende if( zvTitel != null )
		
			// Alle ZVTitel und die dazugehörigen ZVUntertitel aktualisieren
			if( zvTitel != null ) {		// Gibt es ZVTitel
				for( int i = 0; i < zvTitel.size(); i++ ) {
					if( zvTitel.get(i) == null )	// kein zvTitel
						continue;
					ArrayList zvUntertitel = ((ZVTitel)zvTitel.get(i)).getSubUntertitel();
					// Die ZVUntertiteln ändern
					if( zvUntertitel != null ) {	// Gibt es ZVUntertitel
						for( int j = 0; j < zvUntertitel.size(); j++ ) {
							if( zvUntertitel.get(j) == null )	// kein ZVUntertitel
								continue;
							db.updateZVUntertitel( (ZVUntertitel)zvUntertitel.get(j) );
						}	// Ende for zvUntertitel
					}	// Ende if( zvUntertitel != null )
					
					db.updateZVTitel( (ZVTitel)zvTitel.get(i) );
				}	// Ende for zvTitel
			}	// Ende if( zvTitel != null )
			
			return db.updateZVKonto( zvKonto );
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}

	/**
	 * Einen ZVTitel in der Datenbank aktualisieren. <br>
	 * Es werden auch die ZVUntertitel aktualisiert, wenn die Änderungen diese betreffen.
	 * @param ZVTitel, der aktualisiert werden soll
	 * @return ZVTitelId des übergebenen ZVTitels
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int setZVTitel( ZVTitel zvTitel ) throws ApplicationServerException {
		if( zvTitel == null )	// Kein ZVTitel angegeben
			return 0;
		try {
			db.setAutoCommit(false);
			ZVTitel old = db.selectForUpdateZVTitel( zvTitel );	// Zum Aktualisieren auswählen
			if( old == null )		// kein ZVTitel vorhanden
				throw new ApplicationServerException( 12 );
			if( old.getGeloescht() )	// Wenn der Titel schon gelöscht ist
				throw new ApplicationServerException( 30 );
			if( old.equals( zvTitel ) )	// Wenn die beiden Titeln gleich sind
				return db.updateZVTitel( zvTitel );
			if( db.existsZVTitel( zvTitel ) > 0 )	// Wenn der ZVTitel bereits existiert
				throw new ApplicationServerException( 13 );
			// Wenn es Bestellungen oder Buchungen gibt, die diesen ZVTitel verwenden
			if( db.countBestellungen(zvTitel) > 0 || db.countBuchungen(zvTitel) > 0 )
				throw new ApplicationServerException(41);
				
			ArrayList zvUntertitel = zvTitel.getSubUntertitel();
			// Nachsehen ob es beim Ändern von ZVUntertiteln Fehler entstehen
			if( zvUntertitel != null ) {	// Gibt es ZVUntertitel
				for( int j = 0; j < zvUntertitel.size(); j++ ) {
					if( zvUntertitel.get(j) == null )	// kein ZVUntertitel
						continue;
					// Wenn es Bestellungen oder Buchungen gibt, die diesen ZVUntertitel verwenden
					if(  db.countBestellungen((ZVUntertitel)zvUntertitel.get(j)) > 0 || 
													db.countBuchungen((ZVUntertitel)zvUntertitel.get(j)) > 0)
						throw new ApplicationServerException(41);
				}	// Ende for zvUntertitel
			}	// Ende if( zvUntertitel != null )
			
			// Ändern von ZVUntertiteln
			if( zvUntertitel != null ) {	// Gibt es ZVUntertitel
				for( int j = 0; j < zvUntertitel.size(); j++ ) {
					if( zvUntertitel.get(j) == null )	// kein ZVUntertitel
						continue;
					db.updateZVUntertitel( (ZVUntertitel)zvUntertitel.get(j) );
				}	// Ende for zvUntertitel
			}	// Ende if( zvUntertitel != null )
	
			return db.updateZVTitel( zvTitel );
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}

	/**
	 * Einen ZVUntertitel in der Datenbank aktualisieren.
	 * @param ZVUntertitel, der aktualisiert werden soll
	 * @return ZVUntertitelId des aktualisierten ZVUntertitels
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int setZVUntertitel( ZVUntertitel zvUntertitel ) throws ApplicationServerException {
		if( zvUntertitel == null )	// Kein ZVUntertitel angegeben
			return 0;
		try {
			db.setAutoCommit(false);
			ZVUntertitel old = db.selectForUpdateZVUntertitel( zvUntertitel );	// Den ZVUntertitel zum aktualisieren auswählen
			if( old == null )	// Kein ZVUntertitel gefunden
				throw new ApplicationServerException( 14 );
			if( old.getGeloescht() )	// Der ZVUntertitel ist bereits gelöscht
				throw new ApplicationServerException( 31 );
			
			if( old.equals( zvUntertitel ) )	// Sind die beiden ZVUntertitel gleich 
				return db.updateZVUntertitel( zvUntertitel );
	
			if( db.existsZVUntertitel( zvUntertitel ) > 0 )	// Wenn der neue ZVUntertitel bereits existiert
				throw new ApplicationServerException( 15 );
			// Wenn es Bestellungen oder Buchungen gibt, die diesen ZVUntertitel verwenden
			if( db.countBestellungen( zvUntertitel ) > 0 || db.countBuchungen( zvUntertitel ) > 0 )
				throw new ApplicationServerException( 20 );
			
			return db.updateZVUntertitel( zvUntertitel );
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}
	
	/**
	 * Budget auf ein ZVKonto buchen.
	 * @param ZVKonto auf das der Betrag gebucht wird.
	 * @param Betrag, der auf das ZVKonto gebucht wird.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public void buche( ZVKonto konto, float betrag ) throws ApplicationServerException {
		if( konto == null )
			return;
		try {
			db.setAutoCommit(false);
			ZVKonto old = db.selectForUpdateZVKonto( konto );	// Das ZVKonto zum Aktualisieren auswählen
			if( old == null )		// Wenn kein ZVKonto in der Datenbank gefunden
				throw new ApplicationServerException( 10 );
			if( old.getGeloescht() )	// Wenn das ZVKonto gelöscht ist
				throw new ApplicationServerException( 29 );
			
			if( !old.equals( konto ) )	// Wenn die ZVKontos nicht gleich sind, dann wurde das Konto verändert
				throw new ApplicationServerException( 35 );
			konto.setTgrBudget( konto.getTgrBudget() + betrag );
			db.updateZVKonto( konto );
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}
	
	/**
	 * Budget auf einen ZVTitel buchen.
	 * @param ZVTitel auf den der Betrag gebucht wird.
	 * @param Betrag, der auf den ZVTitel gebucht wird.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public void buche( ZVTitel konto, float betrag ) throws ApplicationServerException {
		if( konto == null )
			return;
		try {
			db.setAutoCommit(false);
			ZVTitel old = db.selectForUpdateZVTitel( konto );	// Den ZVTitel zu aktualisieren auswählen
			if( old == null )		// Wenn kein ZVTitel in der Datenbank gefunden
				throw new ApplicationServerException( 12 );
			if( old.getGeloescht() )	// Wenn der ZVTitel gelöscht ist
				throw new ApplicationServerException( 30 );
			if( !old.equals( konto ) )	// Wenn die ZVTitel nicht gleich sind, dann wurde das Konto verändert
				throw new ApplicationServerException( 36 );
			konto.setBudget( konto.getBudget() + betrag );
			db.updateZVTitel( konto );
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}
	
	/**
	 * Budget auf einen ZVUntertitel buchen.
	 * @param ZVUntertitel auf den der Betrag gebucht wird.
	 * @param Betrag, der auf den ZVUntertitel gebucht wird.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public void buche( ZVUntertitel konto, float betrag ) throws ApplicationServerException {
		if( konto == null )
			return;
		try {
			db.setAutoCommit(false);
			ZVUntertitel old = db.selectForUpdateZVUntertitel( konto );	// Den ZVUntertitel zu aktualisieren auswählen
			if( old == null )		// Wenn kein ZVUntertitel in der Datenbank gefunden
				throw new ApplicationServerException( 14 );
			if( old.getGeloescht() )	// Wenn der ZVUntertitel gelöscht ist
				throw new ApplicationServerException( 31 );
			
			if( !old.equals( konto ) )	// Wenn die ZVUntertitel nicht gleich sind, dann wurde das Konto verändert
				throw new ApplicationServerException( 37 );
			konto.setBudget( konto.getBudget() + betrag );
			db.updateZVUntertitel( konto );
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}

	

  // Ein Institut in der Datenbank aktualisieren
	public void setInstitute(Institut editedInst, Institut clientInst) throws ApplicationServerException {
		if(editedInst != null && clientInst != null){
			Institut dbInst = db.selectForUpdateInstitute(clientInst);

			// Institut existiert nicht mehr
			if(dbInst == null || dbInst.getGeloescht())
				throw new ApplicationServerException(3);

			//Institut hat sich zwischenzeitlich geändert
			if(!dbInst.equals(clientInst))
				throw new ApplicationServerException(50);

			// Institut oder Kostenstelle schon vorhanden
			if(db.checkInstitute(editedInst) != 0)
				throw new ApplicationServerException(4);

			db.updateInstitute(editedInst);
		}
	}

	public void delInstitute(Institut clientInst) throws ApplicationServerException {
		if(clientInst != null){
			Institut dbInst = db.selectForUpdateInstitute(clientInst);

			if(dbInst == null || dbInst.getGeloescht())	// Institut wurde schon gelöscht
				throw new ApplicationServerException(3);
			if(!dbInst.equals(clientInst))			// Gleichheit der Institute
				throw new ApplicationServerException(50);

			// Institut ist ein Fachbereich
			Fachbereich[] inst = db.selectFachbereiche();
			if(inst[0].getId() == dbInst.getId())
				throw new ApplicationServerException(51);

			// Institut hat FBHauptkonten
			ArrayList konten = db.selectFBHauptkonten(dbInst);
			if(konten.size() > 0)
				throw new ApplicationServerException(52);

			// Institut hat Benutzer
			Benutzer[] benutzer = db.selectUsers(dbInst);
			if(benutzer.length > 0)
				throw new ApplicationServerException(53);

			db.deleteInstitute(clientInst);
		}
	}

	// fügt ein neues Institut hinzu
	public int addInstitute(Institut institut) throws ApplicationServerException {
		if(db.checkInstitute(institut) == 0){
			return db.insertInstitute(institut);
		}else
			throw new ApplicationServerException(4);
	}



	public void setUser(Benutzer editedUser, Benutzer clientUser) throws ApplicationServerException {
		if(editedUser != null && clientUser != null){

			Benutzer dbUser = db.selectForUpdateUser(clientUser);

			// Benuter existiert nicht mehr
			if(dbUser == null || dbUser.getGeloescht())
				throw new ApplicationServerException(2);

			//Benuter hat sich zwischenzeitlich geändert
			if(!dbUser.equals(clientUser))
				throw new ApplicationServerException(55);

			// Benutzername wird in der MySQL geändert
			if(!editedUser.getBenutzername().equals(clientUser.getBenutzername())){
				if(db.checkUserMySQL(editedUser) > 0)		// Benutzername in der MySQL schon vorhanden
					throw new ApplicationServerException(54);

				db.updateUserMySQL(editedUser, dbUser.getBenutzername());
			}

			// Benutername schon vorhanden
			if(db.checkUser(editedUser) > 0)
				throw new ApplicationServerException(5);

			// FBKonto existiert nicht mehr
			if(dbUser.getPrivatKonto() != 0){
				FBUnterkonto privatKonto = db.selectFBKonto(dbUser.getPrivatKonto());
				if(privatKonto == null)
					throw new ApplicationServerException(64);
			}
			db.updateUser(editedUser);
		}
	}


	public void delUser(Benutzer clientUser) throws ApplicationServerException {
		if(clientUser != null){
			Benutzer dbUser = db.selectForUpdateUser(clientUser);

			if(dbUser == null || dbUser.getGeloescht())	// Benutzer wurde schon gelöscht
				throw new ApplicationServerException(2);
			if(!dbUser.equals(clientUser))			// Gleichheit der Benutzer
				throw new ApplicationServerException(55);

			// Benutzer hat aktive Bestellungen
			if(db.countAktiveBestellungen(dbUser) > 0)
				throw new ApplicationServerException(50);

			//Benutzer aus der MySQL-DB löschen
			db.deleteUserMySQL(dbUser);

			// Benutzer hat Bestellungen gemacht
			if(db.countBestellungen(dbUser) > 0)
				db.deleteUser(dbUser);
			else if(db.countBuchungen(dbUser) > 0) // Benutzer hat schon Buchungen getätigt
				db.deleteUser(dbUser);
			else
				db.deleteUserFinal(dbUser);		// Kann definitiv gelöscht werden
				
		  db.commit();
		}
	}

	//Fügt den Benutzer in die MySQL- und FBMittelvewaltungsdatenbank hinzu
	public int addUser(Benutzer benutzer) throws ApplicationServerException {
		if(db.checkUserMySQL(benutzer) > 0)		// benutzer bereits in der MySQL-DB vorhanden
			throw new ApplicationServerException(54);
		
		//	benutzer bereits in der FBMittelvewaltung-DB vorhanden
		if(db.checkUser(benutzer) > 0)
			throw new ApplicationServerException(5);

		//		FBKonto existiert nicht mehr
		if(benutzer.getPrivatKonto() != 0){
			FBUnterkonto privatKonto = db.selectFBKonto(benutzer.getPrivatKonto());
			if(privatKonto == null)
				throw new ApplicationServerException(64);
		}
		db.setAutoCommit(false);
		
		db.insertUserMySQL(benutzer);
		int newID = db.insertUser(benutzer);
		
		db.setAutoCommit(true);
		return newID;
	}


	public Fachbereich[] getFachbereiche() throws ApplicationServerException {
		return db.selectFachbereiche();
	}

	public Fachbereich setFachbereich(Fachbereich editedFB, Fachbereich fb) throws ApplicationServerException {
		Fachbereich dbFB = db.selectForUpdateFachbereich();

		if(dbFB == null)
			throw new ApplicationServerException(67);

		if(!fb.equals(dbFB))
			throw new ApplicationServerException(58);

		db.updateFachbereich(editedFB);
		
		return getFachbereiche()[0];
	}

	public Haushaltsjahr getHaushaltsjahr() throws ApplicationServerException {
		return db.selectHaushaltsjahr();
	}

	public void setHaushaltsjahr(Haushaltsjahr editedHhj, Haushaltsjahr clientHhj) throws ApplicationServerException {
		if(editedHhj != null && clientHhj != null){
			Haushaltsjahr dbHhj = db.selectForUpdateHaushaltsjahr();

			if(dbHhj == null)			// Fachberich existiert nicht mehr
				throw new ApplicationServerException(57);

			if(!dbHhj.equals(clientHhj))		//Fachbereich hat sich zwischenzeitlich geändert
				throw new ApplicationServerException(58);

			db.updateHaushaltsjahr(editedHhj);
		}
	}

	public ResultSet query(String query) {
		try {
			return db.query(query);
		} catch (ApplicationServerException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void update(String query) {
		try {
			db.update(query);
		} catch (ApplicationServerException e) {
			e.printStackTrace();
		}
	}

	public Rolle[] getRollenFull() throws ApplicationServerException {
		return db.selectRollenFull();
	}

	public void addRollenAktivitaet(int rolle, int aktivitaet) throws ApplicationServerException  {
		db.insertRollenAktivitaet(rolle, aktivitaet);
	}

	public void delRollenAktivitaet(int rolle, int aktivitaet) throws ApplicationServerException  {
		db.deleteRollenAktivitaet(rolle, aktivitaet);
	}

	public Aktivitaet[] getAktivitaeten() throws ApplicationServerException {
		return db.selectAktivitaeten();
	}

	public int addRolle(Rolle rolle) throws ApplicationServerException {
		if(db.checkRolle(rolle) == 0){
			return db.insertRolle(rolle);
		}else
			throw new ApplicationServerException(8);
	}

	public void setRolle(Rolle editedRolle, Rolle clientRolle) throws ApplicationServerException {
		if(editedRolle != null && clientRolle != null){
			Rolle dbRolle = db.selectForUpdateRolle(editedRolle);

			if(dbRolle == null)			// Rolle existiert nicht mehr
				throw new ApplicationServerException(7);

			if(!dbRolle.equals(clientRolle))		//Rolle hat sich zwischenzeitlich geändert
				throw new ApplicationServerException(59);

			if(db.checkRolle(editedRolle) > 0)	//Rolle existiert schon
				throw new ApplicationServerException(8);

			db.updateRolle(editedRolle);
		}
	}

	public void delRolle(Rolle rolle) throws ApplicationServerException {
		if(rolle != null){
			Rolle dbRolle = db.selectForUpdateRolle(rolle);

			if(dbRolle == null)			// Rolle existiert nicht mehr
				throw new ApplicationServerException(7);

			if(!dbRolle.equals(rolle))		//Rolle hat sich zwischenzeitlich geändert
				throw new ApplicationServerException(59);

			if(db.selectBenutzerRolle(rolle) > 0)
				throw new ApplicationServerException(9);

			db.deleteRollenAktivitaeten(rolle.getId());
			db.deleteRolle(rolle);
		}
	}

	public Benutzer getUser(String user, String password) throws ApplicationServerException {
		return db.selectUser(user, password);
	}

	public ArrayList getZVKontenOnly() throws ApplicationServerException {
		return db.selectZVKonten();
	}

	/**
	 * gibt Institute mit FBHauptkonten, FBHauptkonten sind mit ihren Kontenzuordnungen
	 * @return Institute mit FBHauptkonten
	 * @throws ApplicationServerException
	 */
	public Institut[] getInstitutZuordnungen() throws ApplicationServerException {
		Institut[] instituts = db.selectInstitutes();	// Es werden alle Institute ermittelt
		ArrayList hauptkonten;

		if( instituts == null )		// Keine Institute vorhanden
			return null;

		// Schleife zur Ermittlung der FBHauptkonten eines Instituts
		for( int i = 0; i < instituts.length; i++ ) {
			if( instituts[i] == null )	// kein Institut
				continue;

			hauptkonten = db.selectFBHauptkonten( instituts[i] );
			for( int j = 0; j < hauptkonten.size(); j++ ) {
				if( hauptkonten.get(j) == null )	// kein FBHauptkonto
					continue;
				// Ermittlung der Kontenzuordnungen vom Hauptkonto
				((FBHauptkonto)hauptkonten.get(j)).setZuordnung( db.selectKontenzuordnungen( ((FBHauptkonto)hauptkonten.get(j)) ) );
			}
			if( hauptkonten == null )
				continue;
			instituts[i].setHauptkonten( hauptkonten );
		}

		return instituts;
	}


	public void setKontenZuordnung(FBHauptkonto fbKonto, Kontenzuordnung clientZuordnung) throws ApplicationServerException {
		// Kontenzuordnung existiert ?
		Kontenzuordnung dbZuordnung = db.selectKontenzuordnung(fbKonto.getId(), clientZuordnung.getZvKonto().getId());

		if(dbZuordnung == null)	// Zuordnung existiert nicht mehr
			throw new ApplicationServerException(60);

		ZVKonto dbZVKonto = db.selectZVKonto(clientZuordnung.getZvKonto().getId());
		if(dbZVKonto == null)		// ZVKonto existiert nicht mehr
			throw new ApplicationServerException(63);

		FBHauptkonto dbFBKonto = db.selectFBHauptkonto(fbKonto.getId());
		if(dbFBKonto == null)	// FBKonto existiert nicht mehr
			throw new ApplicationServerException(64);


		db.updateKontenZuordnung(fbKonto.getId(), clientZuordnung.getZvKonto().getId(), clientZuordnung.getStatus());
	}


	public void addKontenZuordnung(FBHauptkonto fbKonto, ZVKonto zvKonto) throws ApplicationServerException  {
		Kontenzuordnung zuordnung = db.selectKontenzuordnung(fbKonto.getId(), zvKonto.getId());

		if(zuordnung != null)	// Zuordnung existiert schon
			throw new ApplicationServerException(62);

		ZVKonto dbZVKonto = db.selectZVKonto(zvKonto.getId());
		if(dbZVKonto == null)		// ZVKonto existiert nicht mehr
			throw new ApplicationServerException(63);

		FBHauptkonto dbFBKonto = db.selectFBHauptkonto(fbKonto.getId());
		if(dbFBKonto == null)	// FBKonto existiert nicht mehr
			throw new ApplicationServerException(64);

		Kontenzuordnung[] zuordnungen = db.selectKontenzuordnungen(fbKonto);

		if(zuordnungen != null){	// es existieren Zuordnungen
			if(zuordnungen[0].getZvKonto().getZweckgebunden()) 	// nur ein zweckgebundenes ZVKonto
				throw new ApplicationServerException(61);
			if(dbZVKonto.getZweckgebunden())		// ZVKonto ist zweckgebunden ?
				throw new ApplicationServerException(61);
		}
		db.insertKontenZuordnung(fbKonto.getId(), zvKonto.getId());
	}


	public void delKontenZuordnung(FBHauptkonto fbKonto, ZVKonto zvKonto) throws ApplicationServerException  {
		db.deleteKontenZuordnung(fbKonto.getId(), zvKonto.getId());
	}

	public FBUnterkonto getFBKonto(int fbKontoId) throws ApplicationServerException {
		return db.selectFBKonto(fbKontoId);
	}
	
	/**
	 * Abfrage aller Firmen in der Datenbank.
	 * @return Liste mit allen Firmen in der Datenbank.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public ArrayList getFirmen() throws ApplicationServerException {
		return db.selectFirmen();
	}
	
	/**
	 * Eine neue Firma erstellen.
	 * @param Firma, die erstellt werden soll.
	 * @return id der erstellten Firma.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int addFirma( Firma firma ) throws ApplicationServerException {
		if( firma == null )		// Wurde eine Firma angegeben
			return 0;
		if( db.existsFirma( firma ) > 0 )				// Wenn diese Firma bereits existiert 
			throw new ApplicationServerException( 38 );	// dann kann man es nicht mehr erstellen
		try {
			db.setAutoCommit(false);
			int id = 0;		// id der Firma
			if( (id = db.existsDelFirma( firma )) > 0 ) {	// Es gibt eine gelöschte Firma
				firma.setId( id );		// Id der gelöschten Firma ist jetzt id der neuen Firma
				db.selectForUpdateFirma( firma );	// Zum Aktualisieren auswählen
				return db.updateFirma( firma );		// Aktualisieren
			}
			
			return db.insertFirma( firma );			// Sonst neu erstellen
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}
	
	/**
	 * Eine Firma in der Datenbank aktualisieren.
	 * @param Firma, die aktualisiert werden soll.
	 * @return id der aktualisierten Firma.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int setFirma( Firma firma ) throws ApplicationServerException {
		if( firma == null )		// Wurde eine Firma angegeben
			return 0;
		if( db.existsDelFirma( firma ) > 0 )			// Wenn diese Firma bereits gelöscht ist
			throw new ApplicationServerException( 39 );	// dann kann man es nicht aktualisieren
		
		try {
			db.setAutoCommit(false);
			db.selectForUpdateFirma( firma );		// Firma zu aktualisieren auswählen
			return db.updateFirma( firma );			// Firma aktualisieren und Id zurückgeben
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}


	/**
	 * Eine Firma in der Datenbank löschen.
	 * @param Firma, die gelöscht werden soll.
	 * @return id der gelöschten Firma.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int delFirma( Firma firma ) throws ApplicationServerException {
		if( firma == null )					// Wurde eine Firma angegeben
			return 0;
		if( db.countActiveBestellungen( firma ) > 0 )	// Gibt es aktive Bestellungen, die an die angegebene Firma gehen
			throw new ApplicationServerException( 20 );	// dann kann man es nicht löschen
		
		try {
			db.setAutoCommit(false);
			if( db.countInactiveBestellungen( firma ) > 0 ) {	// Wenn es inaktive Bestellungen gibt, dann braucht man die Firma
				firma.setGeloescht( true );						// für Reports und es wird nur der Flag gesetzt
				db.selectForUpdateFirma( firma );				// Firma zum aktualisieren auswählen
				return db.updateFirma( firma );					// Firma aktualisieren
			}
			return db.deleteFirma( firma );			// Sonst wird die Firma aus der Datenbank gelöscht
		} finally {
			db.setAutoCommit(true);
			db.commit();
		}
	}


	/* (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getKostenarten()
	 */
	public Kostenart[] getKostenarten() throws ApplicationServerException {
		return db.selectKostenarten();
	}


	/* (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getUsers(dbObjects.Institut)
	 */
	public Benutzer[] getUsers(Institut institut) throws ApplicationServerException {
		return db.selectUsers(institut);
	}


	/* (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#addBestellung(dbObjects.StandardBestellung)
	 */
	public void addBestellung(StandardBestellung bestellung) throws ApplicationServerException {
		db.setAutoCommit(false);
		
		int newBestellungId = db.insertBestellung(bestellung, 0);
		int newAngebotId = 0;
		
		// fügt die Standardbestellung ein
		bestellung.setId(newBestellungId);
		db.insertStandardBestellung(bestellung);
		
		// Fügt alle Angebote ein
		for(int i = 0; i < bestellung.getAngebote().size(); i++){
			Angebot angebot = (Angebot)bestellung.getAngebote().get(i);
			ArrayList positionen = angebot.getPositionen();
			
			newAngebotId = db.insertAngebot(angebot, newBestellungId, (bestellung.getAuswahl() == (i + 1)) ? true : false);
			
			// fügt alle Positionen ein
			for(int j = 0; j < positionen.size(); j++){
				Position position = (Position)positionen.get(j);
				
				db.insertPosition(position, newAngebotId);
			}
		}
		db.commit();
		
		db.setAutoCommit(true);
	}


	/* (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#addBestellung(dbObjects.ASKBestellung)
	 */
	public void addBestellung(ASKBestellung bestellung) throws ApplicationServerException {
		int newBestellungId = db.insertBestellung(bestellung, 0);
		int newAngebotId = 0;
		
		// fügt die ASKbestellung ein
		bestellung.setId(newBestellungId);
		db.insertASKBestellung(bestellung);
		
		// Fügt das Angebot ein
		Angebot angebot = (Angebot)bestellung.getAngebot();
		ArrayList positionen = angebot.getPositionen();
		
		newAngebotId = db.insertAngebot(angebot, newBestellungId, true);
		
		// fügt alle Positionen ein
		for(int j = 0; j < positionen.size(); j++){
			Position position = (Position)positionen.get(j);
			
			db.insertPosition(position, newAngebotId);
		}
		
		
	}

}