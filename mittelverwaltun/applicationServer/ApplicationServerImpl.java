package applicationServer;

import java.io.Serializable;
import java.sql.Date;
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
	 * @param id = Neue ID-Nummer des Servers.
	 * author w.flat
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Abfrage der id.
	 * @return ID-Nummer des Servers.
	 * author w.flat
	 */
	public int getId() {
		return id;
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#login(java.lang.String, java.lang.String)
	 */
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

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#logout()
	 */
	public void logout()throws ConnectionException{
		db.disconnect();
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getUsers()
	 */
	public Benutzer[] getUsers () throws ApplicationServerException{
		return db.selectUsers();
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getInstitutes()
	 */
	public Institut[] getInstitutes () throws ApplicationServerException{
		return db.selectInstitutes();
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getRollen()
	 */
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
						Kontenzuordnung[] zuordnung = db.selectKontenzuordnungen( ((FBHauptkonto)hauptkonten.get(j)));
						((FBHauptkonto)hauptkonten.get(j)).setZuordnung( zuordnung );
						
						ArrayList unterkonten = db.selectFBUnterkonten( instituts[i], (FBHauptkonto)hauptkonten.get(j));
						// die Kontenzuordnungen der FBHauptkonten werden übernommen
						for(int k = 0; k < unterkonten.size(); k++)
							((FBUnterkonto)(unterkonten.get(k))).setZuordnung(zuordnung);
							
					 ((FBHauptkonto)hauptkonten.get(j)).setUnterkonten(unterkonten);
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
	 
	 /*
	  *  (Kein Javadoc)
	  * @see applicationServer.ApplicationServer#getNoPurposeFBHauptkonten(dbObjects.Institut)
	  */
	 public ArrayList getNoPurposeFBHauptkonten( Institut institut ) throws ApplicationServerException {
		 //return db.selectFBHauptkonten( institut );
		 return db.selectNoPurposeFBHauptkonten( institut );
	 }
	 
	 /**
	  * Budget eines FBHauptkontos aktualisieren.
	  */
	 public void setAccountBudget ( Benutzer b, FBHauptkonto acc, float remmitance ) throws ApplicationServerException{
		try{
			 //TODO: Test auf maximal zuweisungsfähigen betrag?!
			 FBHauptkonto accOld = db.selectForUpdateFBHauptkonto(acc.getId());
	
			 if (accOld == null){
				 throw new ApplicationServerException(64);
				 //Konto existiert nicht mehr 64
			 }else if (accOld.getGeloescht()){
				 throw new ApplicationServerException(152);
				 //Konto wurde geloescht 152
			 }else if (acc.is(accOld)){
				 if (acc.getBudget() == accOld.getBudget()){
					 accOld.setBudget(accOld.getBudget() + remmitance);
					 if(db.updateFBHauptkonto(accOld)==0){
						 throw new ApplicationServerException(155);
					 }
					 bucheMittelverteilung(b, accOld, remmitance);
				 }else{
					 acc.setBudget(accOld.getBudget());
					 throw new ApplicationServerException(153);
					 //Budget wurde zwischenzeitlich aktualisiert 153
				 }
			 }else{
				 throw new ApplicationServerException(154);
				 // Konten stimmen nicht überein
			 }
			
				db.insertLog(1, "Budget eines FBHauptkontos aktualisiert \n" +
												"Benutzer: " + b + "\n" +
												"FBHauptkonto: " + acc + "\n" +
												"Betrag: " +  remmitance);
			
			 db.commit();
		}catch(ApplicationServerException e){
			db.rollback();
			throw e;
		}
	 }
	 
	 /*
	  *  (Kein Javadoc)
	  * @see applicationServer.ApplicationServer#getUsersByRole(dbObjects.Institut, int)
	  */
	 public Benutzer[] getUsersByRole(Institut i, int rollenId) throws ApplicationServerException {
		 return db.selectUsersByRole(i, rollenId);
	 }
	 
	/**
	 * Ermittelt den größtmöglichen Betrag der dem übergebenen FB-Hauptkonto
	 * zugewiesen werden kann
	 * @return der zuweisungsfähige Betrag
	 * @throws ApplicationServerException
	 * author Mario
	 */
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
				return getAvailableNoPurposeBudget();
			}
		} else return 0;
	}
	
	/**
	 * Ermittelt den Betrag des noch nicht an FB-Konten verteilten Budgets des übergebenen ZV-Kontos   
	 * VORSICHT: Liefert nur korrekte Ergebnisse für _zweckgebundenen_ ZV-Konten!!!
	 * @return der Betrag des verteilungsfähigen Budgets
	 * @throws ApplicationServerException
	 * author Mario
	 */
	public float getAvailableAccountBudget (ZVKonto account) throws ApplicationServerException{
		return db.selectTotalAccountBudget(account) - db.selectDistributedAccountBudget(account);
	}
	
	/**
	 * Ermittelt den Betrag des noch nicht an FB-Konten verteilten Budgets 
	 * über alle _zweckungebundenen_ ZV-Konten
	 * @return der Betrag des verteilungsfähigen zweckungebundenen Budgets
	 * @throws ApplicationServerException
	 * author Mario
	 */	
	public float getAvailableNoPurposeBudget () throws ApplicationServerException{
		return (db.selectNoPurposeZVBudgetSum() - db.selectNoPurposeFBBudgetSum());
	}
	
	/**
	 * Abfrage von Hauptkonten eines bestimmten Insituts.
	 * @param institut = Institut von dem die KOnten abgefragt werden.
	 * @return Liste FBHauptkonten, die zu einem bestimmten Institut angehören.
	 * @throws ApplicationServerException
	 * author w.flat
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
	 * @param institut = Institut, welchem die FBKonten zugeordnet sind.
	 * @param hauptkonto = FBHauptkonto, welchem dei FBUnterkonten zugeordnet sind.
	 * @return FBUnterkonten
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public ArrayList getFBUnterkonten( Institut institut, FBHauptkonto hauptkonto ) throws ApplicationServerException {
		return db.selectFBUnterkonten( institut, hauptkonto );	// Die ermittelten Konten zurückgeben
	}

	/**
	 * Abfrage der FBKonten, die ein Benutzer für seine Kleinbestellung verwenden kann.
	 * @param user = Benutzer, für den die Konten ermittelt werden sollen.
	 * @return Institut-Array(1) mit den ermittelten Konten. 
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public Institut[] getFBKontenForUser(Benutzer user) throws ApplicationServerException {
		if(user == null)		// Wenn kein User angegeben
			return null;
		// Institut mit Hauptkonten
		Institut[] inst = getInstituteWithAccounts(user.getKostenstelle(), false);
		ArrayList hauptkonten = inst[0].getHauptkonten();	// Die Hauptkonten eines Instituts
		FBHauptkonto hauptkonto;
		for(int i = 0; i < hauptkonten.size(); i++) {
			if((hauptkonto = (FBHauptkonto)hauptkonten.get(i)) == null)	// Kein FBHauptkonto vorhanden
				continue;
			hauptkonto.setUnterkonten(db.selectFBUnterkontenForUser(user, hauptkonto));
		}
		int temp = 0;
		// Löschen der nicht benötigten FBHauptkonten
		while(temp < hauptkonten.size()) {
			if((hauptkonto = (FBHauptkonto)hauptkonten.get(temp)) == null) {	// Kein FBHauptkonto vorhanden
				hauptkonten.remove(temp);		// Dieses Hauptkonto löschen
				continue;
			}
			// Wenn das FBHauptkonto für Kleinbestellungen freigegeben ist oder 
			// das FBHauptkonto FBUnterkonten enthält, dann wird das FBHauptkonto nicht gelöscht
			if(hauptkonto.getKleinbestellungen() || hauptkonto.getUnterkonten().size() > 0) {
				temp++;
			} else {
				hauptkonten.remove(temp);
			}
		}
		// Kontenzuordnungen ermitteln
		for(int i = 0; i < hauptkonten.size(); i++) {
			hauptkonto = (FBHauptkonto)hauptkonten.get(i);
			Kontenzuordnung[] zuordnungen = db.selectKontenzuordnungen(hauptkonto);
			hauptkonto.setZuordnung(zuordnungen);
			for(int j = 0; j < hauptkonto.getUnterkonten().size(); j++) {
				((FBUnterkonto)hauptkonto.getUnterkonten().get(i)).setZuordnung(zuordnungen);
			}
		}

		return inst;
	}
	
	/**
	 * Ein neues FBHauptkonto erstellen.
	 * @param FBHauptkonto, das erstellt werden soll.
	 * @return kontoId des eingefügten Hauptkontos
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int addFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {
		if( konto == null )		// Kein konto übergeben
			return 0;

		try {
			if( db.existsFBKonto(konto) > 0 )				// Wenn dieses FBHauptkonto bereits existiert 
				throw new ApplicationServerException( 17 );	// dann kann man es nicht mehr erstellen
		
			int id = 0;
			if( (id = db.existsDeleteFBKonto( konto )) > 0 ) {	// Existiert schon ein gelöschtes FBKonto
				konto.setId( id );		// KontoId beim Konto setzen
				db.selectForUpdateFBHauptkonto( konto );	// Zum Aktualisieren auswählen
				return db.updateFBHauptkonto( konto );
			}
			db.insertLog(0, "FBHauptKonto (Id: " + konto.getId() + " hinzugefügt: \n" +
											"FBHauptKonto: " + konto );
			
			return db.insertFBHauptkonto( konto );			// Sonst ein neues FBHauptkonto erstellen
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}
	}

	/**
	 * Ein neues FBUnterkonto erstellen.
	 * @param FBUnterkonto, das erstellt werden soll.
	 * @return kontoId des eingefügten Unterkontos
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int addFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {
		if( konto == null )		// Wenn kein Konto übergeben wurde
			return 0;
		try {
			if( db.existsFBKonto( konto ) > 0 )		// Wenn ein FBUnterkonto bereits existiert
				throw new ApplicationServerException( 19 );
			
			int id = 0;
			if( (id = db.existsDeleteFBKonto( konto )) > 0 ){	// Gibt es ein solches gelöschtes Konto
				konto.setId( id );		// Id des gelöschten Kontos an das neue Konto übergeben
				db.selectForUpdateFBUnterkonto( konto );	// Das Konto zum Aktualiseren auswählen
				return db.updateFBUnterkonto( konto );	// Das Konto aktualisieren
			}
			db.insertLog(0, "FBUnterKonto (Id: " + konto.getId() + " hinzugefügt: \n" +
											"FBUnterKonto: " + konto );
			
			return db.insertFBUnterkonto( konto );			// Sonst ein neues FBUnterkonto erstellen
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}
	}

	/**
	 * Ein FBHauptkonto löschen. Dabei werden auch die Unterkonten gelöscht.
	 * @param FBHauptkonto, das gelöscht werden soll.
	 * @return kontoId des gelöschten FBHauptkontos
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int delFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {
		if( konto == null )		// Kein FBHauptkonto
			return 0;
		
		try {
			if( db.countActiveBestellungen( konto ) > 0 )		// Wenn Bestellungen noch nicht abgeschlossen sind 
				throw new ApplicationServerException( 20 );
			if( db.countKontenzuordnungen( konto ) > 0 )		// Wenn Kontenzuordnungen existieren
				throw new ApplicationServerException( 21 );
		
			String logText = "";
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
			
			db.insertLog(2, "FBHauptKonto (Id: " + konto.getId() + " gelöscht: \n" +
											"FBHauptKonto: " + konto );
			
			// Sonst wird das Hauptkonto ganz gelöscht
			if( db.deleteFBKonto( konto ) > 0)
				return konto.getId();
			else
				return 0;
				
			
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}
	}
	
	/**
	 * Ein FBUnterkonto löschen.
	 * @param FBUnterkonto, das gelöscht werden soll.
	 * @return kontoId des gelöschten FBUnterkontos
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int delFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {
		if( konto == null )		// Kein FBUnterkonto
			return 0;
		
		try {
			if( db.countActiveBestellungen( konto ) > 0 )		// Wenn Bestellungen noch nicht abgeschlossen sind 
				throw new ApplicationServerException( 20 );
			if( db.countActiveBenutzer( konto ) >  0 )			// Unterkonto ist einem Benutzer zugeordnet
				throw new ApplicationServerException( 22 );
				
			// Es gibt abgeschlossene Bestellungen noch oder Buchungen oder Benutzer
			if( (db.countBestellungen( konto ) > 0) || (db.countBuchungen( konto ) > 0) || (db.countBenutzer( konto ) > 0) ) {
				db.selectForUpdateFBUnterkonto( konto );	// Das FBUnterkonto zum Aktualisieren auswählen
				konto.setGeloescht( true );					// Flag gellöscht setzen
				return db.updateFBUnterkonto( konto );		// Das FBUnterkonto in der Datenbank aktualisieren
			}
			db.insertLog(2, "FBUnterkonto (Id: " + konto.getId() + " gelöscht: \n" +
											"FBUnterkonto: " + konto );
			
			// Sonst wird das FBUnterkonto ganz gelöscht
			if( db.deleteFBKonto( konto ) > 0)
				return konto.getId();
			else
				return 0;
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}
	}

	/**
	 * Ein FBHauptkonto aktualisieren.
	 * @param FBHauptkonto, welches aktualisiert werden soll
	 * @return kontoId des aktualisierten FBHauptkontos
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int setFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {
		if( konto == null )		// Kein Konto angegeben
			return 0;
		
		try {
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
			
			db.insertLog(1, "FBHauptKonto (Id: " + konto.getId() + " aktualisiert: \n" +
											"FBHauptKonto: " + konto );
			
			return db.updateFBHauptkonto( konto );	// Sonst kann das FBHauptkonto aktualisiert werden
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();			
		}
	}

	/**
	 * Ein FBUnterkonto aktualisieren.
	 * @param FBUnterkonto, welches aktualisiert werden soll
	 * @return kontoId des aktualisierten FBUnterkontos
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int setFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {
		if( konto == null )		// Kein Konto angegeben
			return 0;
		try {
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
			db.insertLog(1, "FBUnterkonto (Id: " + konto.getId() + " aktualisiert: \n" +
											"FBUnterkonto: " + konto );
			
			return db.updateFBUnterkonto( konto );		// Sonst kann das FBUnterkonto aktualisiert werden
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}
	}
	
	/**
	 * Budget von einem FBHauptkonto auf ein FBUnterkonto buchen.
	 * @param benutzer = Benutzer, der die Buchung durchgeführt hat. 
	 * @param FBHauptkonto, von dem der Betrag abgebucht wird.
	 * @param FBUnterkonto, das den abgebuchten Betrag erhält.
	 * @param Betrag, der vom FBHauptkonto abgebucht wird und welchen das FBUnterkonto erhält.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public void buche( Benutzer benutzer, FBHauptkonto haupt, FBUnterkonto unter, float betrag ) throws ApplicationServerException {
		if( haupt == null || unter == null )	// Ein Konto wurde nicht angegeben
			return;
		try {
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
			bucheUmverteilung(benutzer, haupt, unter, betrag);
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.insertLog(3, "Budget von einem FBHauptkonto auf ein FBUnterkonto gebucht \n" +
											"Benutzer: " + benutzer + "\n" +
											"Von FBHauptkonto: " +  haupt + "\n" +
											"Nach FBUnterkonto: " + unter + "\n" +
											"Betrag: " +  betrag);
			db.commit();
		}
	}
	
	/**
	 * Budget von einem FBHauptkonto auf ein anderes FBHauptkonto buchen.
	 * @param benutzer = Benutzer, der die Buchung durchgeführt hat. 
	 * @param FBHauptkonto, von dem der Betrag abgebucht wird.
	 * @param FBHauptkonto, das den abgebuchten Betrag erhält.
	 * @param Betrag, der vom ersten FBHauptkonto abgebucht wird und welchen das zweite FBHauptkonto erhält.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public void buche( Benutzer benutzer, FBHauptkonto from, FBHauptkonto to, float betrag ) throws ApplicationServerException {
		if( from == null || to == null )
			return;
		try {
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
			bucheUmverteilung(benutzer, from, to, betrag);
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.insertLog(3, "Budget von einem FBHauptkonto auf ein anderes FBHauptkonto gebucht \n" +
											"Benutzer: " + benutzer + "\n" +
											"Von FBHauptkonto: " +  from + "\n" +											"Nach FBHauptkonto: " + to + "\n" +											"Betrag: " +  betrag);
			db.commit();
		}
	}
	
	/**
	 * Budget von einem FBUnterkonto auf ein FBHauptkonto buchen.
	 * @param benutzer = Benutzer, der die Buchung durchgeführt hat. 
	 * @param FBUnterkonto, von dem der Betrag abgebucht wird.
	 * @param FBHauptkonto, das den abgebuchten Betrag erhält.
	 * @param Betrag, der von dem FBUnterkonto abgebucht wird und welchen das FBHauptkonto erhält.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public void buche( Benutzer benutzer, FBUnterkonto unter, FBHauptkonto haupt, float betrag ) throws ApplicationServerException {
		if( unter == null || haupt == null )
			return;
		try {
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
			bucheUmverteilung(benutzer, unter, haupt, betrag);
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.insertLog(3, "Budget von einem FBUnterkonto auf ein FBHauptkonto gebucht \n" +
											"Benutzer: " + benutzer + "\n" +
											"Von FBUnterkonto: " +  unter + "\n" +
											"Nach FBHauptkonto: " + haupt + "\n" +
											"Betrag: " +  betrag);
			db.commit();
		}
	}
	
	/**
	 * Abfrage der ZVKonten mit den dazugehörigen ZVTiteln und ZVUntertiteln.
	 * @return Liste mit den ZVKonten
	 * author w.flat
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
	 * author w.flat
	 */
	public int addZVKonto( ZVKonto zvKonto ) throws ApplicationServerException {
		if( zvKonto == null )		// Wenn kein ZVKonto
			return 0;
		try {
			if( db.existsZVKonto( zvKonto ) > 0 )		// Wenn das ZVKonto bereits existiert
				throw new ApplicationServerException( 11 );
			if( !zvKonto.isTGRKonto() ) {			// Ist das ZVKonto ein TitelGruppenKonto
				if( db.existsZVTitel( (ZVTitel)zvKonto.getSubTitel().get(0) ) > 0 )		// Wenn das ZVTitel bereits existiert
					throw new ApplicationServerException( 11 );
			}
			
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
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.insertLog(0, "ZVKonto hinzugefügt: \n" +
											"ZVKonto: " +  zvKonto );
			db.commit();
		}
	}
	
	/**
	 * Abfrage der Id von einem ZVTitel.
	 * @param ZVTitel, welcher abgefragt werden soll
	 * @return ZVTitelId des ZVTitels
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int getZVTitelId( ZVTitel zvTitel ) throws ApplicationServerException {
		return db.existsZVTitel( zvTitel );
	}

	/**
	 * Einen neuen ZVTitel in die Datenbank erstellen.
	 * @param ZVTitel, welcher erstellt werden soll
	 * @return ZVTitelId vom erstellten ZVTitel
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int addZVTitel( ZVTitel zvTitel ) throws ApplicationServerException {
		if( zvTitel == null )		// Wenn kein ZVTitel
			return 0;
		try {
			if( db.existsZVTitel( zvTitel ) > 0 )		// Wenn der ZVTitel bereits existiert
				throw new ApplicationServerException( 11 );
			
			int id = 0;
			if( (id = db.existsDeleteZVTitel( zvTitel )) > 0 )	{	// Wenn ein gelöschtes ZVTitel exitiert, dann aktualisieren
				zvTitel.setId( id );		// Id setzen
				db.selectForUpdateZVTitel( zvTitel );	// ZVTitel zum Aktualisieren auswählen
				return db.updateZVTitel( zvTitel );		// ZVTitel Aktualisieren
			}
			
			return db.insertZVTitel( zvTitel );		// Sonst einen neuen ZVTitel erstellen
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.insertLog(0, "ZVTitel hinzugefügt: \n" +
											"ZVTitel: " +  zvTitel );
			db.commit();
		}
	}

	/**
	 * Einen neuen ZVUntertitel in die Datenbank erstellen.
	 * @param ZVUntertitel, welcher erstellt werden soll
	 * @return ZVUntertitelId vom eingefügtem ZVUntertitel
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int addZVUntertitel( ZVUntertitel zvUntertitel ) throws ApplicationServerException {
		if( zvUntertitel == null )		// Wenn kein ZVUntertitel
			return 0;
		try {
			if( db.existsZVUntertitel( zvUntertitel ) > 0 )		// Wenn der ZVUntertitel bereits existiert
				throw new ApplicationServerException( 11 );
			
			int id = 0;
			// Wenn ein gelöschtes ZVUntertitel exitiert, dann aktualisieren
			if( (id = db.existsDeleteZVUntertitel( zvUntertitel )) > 0 ) {
				zvUntertitel.setId( id );		// dann die ZVUntertitelId setzen
				db.selectForUpdateZVUntertitel( zvUntertitel );	// Zum Aktualisieren auswählen
				return db.updateZVUntertitel( zvUntertitel );	// ZVUntertitel auswählen
			}
			
			return db.insertZVUntertitel( zvUntertitel );	// Sonst neuen ZVUntertitel erstellen
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.insertLog(0, "ZVUntertitel hinzugefügt: \n" +
											"ZVUntertitel: " +  zvUntertitel );
			db.commit();
		}
	}

	/**
	 * Ein ZVKonto aus der Datenbank löschen. <br>
	 * Dabei müssen auch alle dazugehörigen ZVTitel und ZVUntertitel gelöscht werden.
	 * @return ZVKontoId vom gelöschten ZVKonto
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int delZVKonto( ZVKonto zvKonto ) throws ApplicationServerException {
		if( zvKonto == null )	// Kein ZVKonto
			return 0;
		
		try {
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
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.insertLog(2, "ZVKonto (Id: " + zvKonto.getId() + ") gelöscht: \n" +
											"ZVKonto: " +  zvKonto );
			db.commit();
		}
	}
	
	/**
	 * Überprüfung ob ein ZVKonto zweckgebunden sein kann. <br>
	 * Dabei wird ermittelt ob mehr als ein ZVKonto zu dem FBKonto einer Kontozuordnung existiert.
	 * @param ZVKonto für welches die Überprüfung durchgeführt werden soll
	 * @return Zahl > 0, wenn das ZVKonto nicht zweckgebunden sein kann. Sonst Zahl = 0.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int getNumberOfKontenzuordnungen( ZVKonto zvKonto ) throws ApplicationServerException {
		return db.countZVKonten( zvKonto );
	}

	/**
	 * Einen ZVTitel in der Datenbank löschen. Dabei müssen auch alle ZVUntertitel gelöscht werden.
	 * @param ZVTitel, welcher gelöscht werden sollte.
	 * @return ZVTitelId vom gelöschten ZVTitel
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int delZVTitel( ZVTitel zvTitel ) throws ApplicationServerException {
		if( zvTitel == null )	// Kein ZVTitel
			return 0;
		
		try {
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
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.insertLog(2, "ZVTitel (Id: " + zvTitel.getId() + ") gelöscht: \n" +
											"ZVTitel: " +  zvTitel );
			db.commit();
		}
	}

	/**
	 * Einen ZVUntertitel in der Datenbank löschen.
	 * @param ZVUntertitel der gelöscht werden sollte.
	 * @return ZVUntertitelId vom gelöschten ZVUntertitel.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int delZVUntertitel( ZVUntertitel zvUntertitel ) throws ApplicationServerException {
		if( zvUntertitel == null )	// Kein ZVTitel
			return 0;
		
		try {
			if( db.countActiveBestellungen( zvUntertitel ) > 0 )		// Wenn es Kontenzuordnungen gibt
				throw new ApplicationServerException(21);

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
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.insertLog(2, "ZVUntertitel (Id: " + zvUntertitel.getId() + ") gelöscht: \n" +
											"ZVUntertitel: " +  zvUntertitel );
			db.commit();
		}
	}

	/**
	 * Ein ZVKonto in der Datenbank aktualisieren. Es werden auch alle <br>
	 * ZVTitel und ZVUntertitel aktualisiert, wenn die Änderung diese betreffen.
	 * @param ZVKonto, das aktualisiert werden soll.
	 * @return zvKontoId des ZVKontos
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int setZVKonto( ZVKonto zvKonto ) throws ApplicationServerException {
		if( zvKonto == null )		// Wenn kein ZVKonto angegeben
			return 0;
		try {
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
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.insertLog(1, "ZVKonto (Id: " + zvKonto.getId() + ") aktualisiert: \n" +
											"ZVKonto: " +  zvKonto );
			db.commit();
		}
	}

	/**
	 * Einen ZVTitel in der Datenbank aktualisieren. <br>
	 * Es werden auch die ZVUntertitel aktualisiert, wenn die Änderungen diese betreffen.
	 * @param ZVTitel, der aktualisiert werden soll
	 * @return ZVTitelId des übergebenen ZVTitels
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int setZVTitel( ZVTitel zvTitel ) throws ApplicationServerException {
		if( zvTitel == null )	// Kein ZVTitel angegeben
			return 0;
		try {
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
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.insertLog(1, "ZVTitel (Id: " + zvTitel.getId() + ") aktualisiert: \n" +
											"ZVTitel: " +  zvTitel );
			db.commit();
		}
	}

	/**
	 * Einen ZVUntertitel in der Datenbank aktualisieren.
	 * @param ZVUntertitel, der aktualisiert werden soll
	 * @return ZVUntertitelId des aktualisierten ZVUntertitels
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int setZVUntertitel( ZVUntertitel zvUntertitel ) throws ApplicationServerException {
		if( zvUntertitel == null )	// Kein ZVUntertitel angegeben
			return 0;
		try {
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
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.insertLog(1, "ZVUntertitel (Id: " + zvUntertitel.getId() + ") aktualisiert: \n" +
											"ZVUntertitel: " +  zvUntertitel );
			db.commit();
		}
	}
	
	/**
	 * Budget auf ein ZVKonto buchen.
	 * @param benutzer = Benutzer, der die Buchung durchgeführt hat. 
	 * @param ZVKonto auf das der Betrag gebucht wird.
	 * @param Betrag, der auf das ZVKonto gebucht wird.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public void buche( Benutzer benutzer, ZVKonto konto, float betrag ) throws ApplicationServerException {
		if( konto == null )
			return;
		try {
			ZVKonto old = db.selectForUpdateZVKonto( konto );	// Das ZVKonto zum Aktualisieren auswählen
			if( old == null )		// Wenn kein ZVKonto in der Datenbank gefunden
				throw new ApplicationServerException( 10 );
			if( old.getGeloescht() )	// Wenn das ZVKonto gelöscht ist
				throw new ApplicationServerException( 29 );
			
			if( !old.equals( konto ) )	// Wenn die ZVKontos nicht gleich sind, dann wurde das Konto verändert
				throw new ApplicationServerException( 35 );
			konto.setTgrBudget( konto.getTgrBudget() + betrag );
			db.updateZVKonto( konto );
			bucheMittelzuweisungZVTitelgruppe(benutzer, konto, betrag);
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.insertLog(3, "Budget auf ein ZVKonto gebucht \n" +
											"Benutzer: " + benutzer + "\n" +
											"ZVKonto: " +  konto + "\n" +
											"Betrag: " +  betrag);
			db.commit();
		}
	}
	
	/**
	 * Budget auf einen ZVTitel buchen.
	 * @param benutzer = Benutzer, der die Buchung durchgeführt hat. 
	 * @param ZVTitel auf den der Betrag gebucht wird.
	 * @param Betrag, der auf den ZVTitel gebucht wird.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public void buche( Benutzer benutzer, ZVTitel konto, float betrag ) throws ApplicationServerException {
		if( konto == null )
			return;
		try {
			ZVTitel old = db.selectForUpdateZVTitel( konto );	// Den ZVTitel zu aktualisieren auswählen
			if( old == null )		// Wenn kein ZVTitel in der Datenbank gefunden
				throw new ApplicationServerException( 12 );
			if( old.getGeloescht() )	// Wenn der ZVTitel gelöscht ist
				throw new ApplicationServerException( 30 );
			if( !old.equals( konto ) )	// Wenn die ZVTitel nicht gleich sind, dann wurde das Konto verändert
				throw new ApplicationServerException( 36 );
			konto.setBudget( konto.getBudget() + betrag );
			db.updateZVTitel( konto );
			bucheMittelzuweisungZVTitel(benutzer, konto, betrag);
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.insertLog(3, "Budget auf einen ZVTitel gebucht \n" +
											"Benutzer: " + benutzer + "\n" +
											"ZVTitel: " +  konto + "\n" +
											"Betrag: " +  betrag);
			db.commit();
		}
	}
	
	/**
	 * Budget auf einen ZVUntertitel buchen.
	 * @param benutzer = Benutzer, der die Buchung durchgeführt hat. 
	 * @param ZVUntertitel auf den der Betrag gebucht wird.
	 * @param Betrag, der auf den ZVUntertitel gebucht wird.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public void buche( Benutzer benutzer, ZVUntertitel konto, float betrag ) throws ApplicationServerException {
		if( konto == null )
			return;
		try {
			ZVUntertitel old = db.selectForUpdateZVUntertitel( konto );	// Den ZVUntertitel zu aktualisieren auswählen
			if( old == null )		// Wenn kein ZVUntertitel in der Datenbank gefunden
				throw new ApplicationServerException( 14 );
			if( old.getGeloescht() )	// Wenn der ZVUntertitel gelöscht ist
				throw new ApplicationServerException( 31 );
			
			if( !old.equals( konto ) )	// Wenn die ZVUntertitel nicht gleich sind, dann wurde das Konto verändert
				throw new ApplicationServerException( 37 );
			konto.setBudget( konto.getBudget() + betrag );
			db.updateZVUntertitel( konto );
			bucheMittelzuweisungZVTitel(benutzer, konto, betrag);
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.insertLog(3, "Budget auf einen ZVUntertitel gebucht \n" +
											"Benutzer: " + benutzer + "\n" +
											"ZVUntertitel: " +  konto + "\n" +
											"Betrag: " +  betrag);
			db.commit();
		}
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#setInstitute(dbObjects.Institut, dbObjects.Institut)
	 */
	public void setInstitute(Institut editedInst, Institut clientInst) throws ApplicationServerException {
		if(editedInst != null && clientInst != null){
			try{	
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
				db.insertLog(1, "Institut (Id: " + editedInst.getId() + " aktualisiert: \n" +
												"Institut: " + editedInst );
			
			} catch(ApplicationServerException e) {
				db.rollback();
				throw e;
			} finally {
				db.commit();
			}
		}
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#delInstitute(dbObjects.Institut)
	 */
	public void delInstitute(Institut clientInst) throws ApplicationServerException {
		if(clientInst != null){
			try{	
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
				if(benutzer != null && benutzer.length > 0)
					throw new ApplicationServerException(53);
	
				db.deleteInstitute(clientInst);
				db.insertLog(2, "Institut (Id: " + clientInst.getId() + " gelöscht: \n" +
												"Institut: " + clientInst );
			
			} catch(ApplicationServerException e) {
				db.rollback();
				throw e;
			} finally {
				db.commit();
			}
		}
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#addInstitute(dbObjects.Institut)
	 */
	public int addInstitute(Institut institut) throws ApplicationServerException {
		try{
			db.insertLog(0, "Institut (Id: " + institut.getId() + " hinzugefügt: \n" +
											"Institut: " + institut );
			
			if(db.checkInstitute(institut) == 0){
				return db.insertInstitute(institut);
			}else
				throw new ApplicationServerException(4);
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#setUser(dbObjects.Benutzer, dbObjects.Benutzer)
	 */
	public void setUser(Benutzer editedUser, Benutzer clientUser) throws ApplicationServerException {
		if(editedUser != null && clientUser != null){
			try{
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
				db.insertLog(1, "Benutzer (Id: " + editedUser.getId() +") aktualisiert: \n" +
												"Benutzer: " +  editedUser );
			
			} catch(ApplicationServerException e) {
				db.rollback();
				throw e;
			} finally {
				db.commit();
			}
		}
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#delUser(dbObjects.Benutzer)
	 */
	public void delUser(Benutzer clientUser) throws ApplicationServerException {
		if(clientUser != null){
			try{	
				Benutzer dbUser = db.selectForUpdateUser(clientUser);
	
				if(dbUser == null || dbUser.getGeloescht())	// Benutzer wurde schon gelöscht
					throw new ApplicationServerException(2);
				if(!dbUser.equals(clientUser))			// Gleichheit der Benutzer
					throw new ApplicationServerException(55);
	
				// Benutzer hat aktive Bestellungen
				if(db.countAktiveBestellungen(dbUser) > 0)
					throw new ApplicationServerException(50);
	
				
				// Benutzer hat Bestellungen gemacht
				if(db.countBestellungen(dbUser) > 0)
					db.deleteUser(dbUser);
				else if(db.countBuchungen(dbUser) > 0) // Benutzer hat schon Buchungen getätigt
					db.deleteUser(dbUser);
				else
					db.deleteUserFinal(dbUser);		// Kann definitiv gelöscht werden
					
				// Benutzer aus der MySQL-DB löschen
				db.deleteUserMySQL(dbUser);
				
				db.insertLog(2, "Benutzer (Id: " + clientUser.getId() +") gelöscht: \n" +
												"Benutzer: " +  clientUser );
			
			} catch(ApplicationServerException e) {
				 db.rollback();
				 throw e;
			 } finally {
				 db.commit();
			 }
		}
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#addUser(dbObjects.Benutzer)
	 */
	public int addUser(Benutzer benutzer) throws ApplicationServerException {
		try{	
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
			
			int newID = db.insertUser(benutzer);
			db.insertUserMySQL(benutzer);
			db.insertLog(0, "Benutzer (Id: " + newID +") hinzugefügt: \n" +
											"Benutzer: " +  benutzer );
			
			return newID;
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getFachbereiche()
	 */
	public Fachbereich[] getFachbereiche() throws ApplicationServerException {
		return db.selectFachbereiche();
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#setFachbereich(dbObjects.Fachbereich, dbObjects.Fachbereich)
	 */
	public Fachbereich setFachbereich(Fachbereich editedFB, Fachbereich fb) throws ApplicationServerException {
		try{	
			Fachbereich dbFB = db.selectForUpdateFachbereich();
	
			if(dbFB == null)
				throw new ApplicationServerException(67);
	
			if(!fb.equals(dbFB))
				throw new ApplicationServerException(58);
	
			db.updateFachbereich(editedFB);
			
			return getFachbereiche()[0];
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getHaushaltsjahr()
	 */
	public Haushaltsjahr getHaushaltsjahr() throws ApplicationServerException {
		return db.selectHaushaltsjahr();
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#setHaushaltsjahr(dbObjects.Haushaltsjahr, dbObjects.Haushaltsjahr)
	 */
	public void setHaushaltsjahr(Haushaltsjahr editedHhj, Haushaltsjahr clientHhj) throws ApplicationServerException {
		if(editedHhj != null && clientHhj != null){
			try{	
				Haushaltsjahr dbHhj = db.selectForUpdateHaushaltsjahr();
	
				if(dbHhj == null)			// Fachberich existiert nicht mehr
					throw new ApplicationServerException(57);
	
				if(!dbHhj.equals(clientHhj))		//Fachbereich hat sich zwischenzeitlich geändert
					throw new ApplicationServerException(58);
	
				db.updateHaushaltsjahr(editedHhj);
			} catch(ApplicationServerException e) {
				db.rollback();
				throw e;
			} finally {
				db.insertLog(1, "Haushaltsjahr aktualisiert" );
				db.commit();
			}
		}
	}
	
	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getRollenFull()
	 */
	public Rolle[] getRollenFull() throws ApplicationServerException {
		return db.selectRollenFull();
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#addRollenAktivitaet(int, int)
	 */
	public void addRollenAktivitaet(int rolle, int aktivitaet) throws ApplicationServerException  {
		try{	
			db.insertRollenAktivitaet(rolle, aktivitaet);
			db.insertLog(0, "RollenAktivitaet hinzugefügt: \n" +
											"Rolle (Id: " + rolle + ") \n" +											"Aktivität (Id: " + aktivitaet + ")" );
			
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#delRollenAktivitaet(int, int)
	 */
	public void delRollenAktivitaet(int rolle, int aktivitaet) throws ApplicationServerException  {
		try{	
			db.deleteRollenAktivitaet(rolle, aktivitaet);
			db.insertLog(2, "RollenAktivitaet gelöscht: \n" +
											"Rolle (Id: " + rolle + ") \n" +
											"Aktivität (Id: " + aktivitaet + ")" );
			
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getAktivitaeten()
	 */
	public Aktivitaet[] getAktivitaeten() throws ApplicationServerException {
		return db.selectAktivitaeten();
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#addRolle(dbObjects.Rolle)
	 */
	public int addRolle(Rolle rolle) throws ApplicationServerException {
		if(db.checkRolle(rolle) == 0){
			try{
				db.insertLog(0, "Rolle (Id:" + rolle.getId() + ") hinzugefügt: \n" +
												"Rolle: " + rolle.getBezeichnung() );
			
				return db.insertRolle(rolle);
			} catch(ApplicationServerException e) {
				db.rollback();
				throw e;
			} finally {
				db.commit();
			}
		}else
			throw new ApplicationServerException(8);
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#setRolle(dbObjects.Rolle, dbObjects.Rolle)
	 */
	public void setRolle(Rolle editedRolle, Rolle clientRolle) throws ApplicationServerException {
		if(editedRolle != null && clientRolle != null){
			try{	
				Rolle dbRolle = db.selectForUpdateRolle(editedRolle);
	
				if(dbRolle == null)			// Rolle existiert nicht mehr
					throw new ApplicationServerException(7);
	
				if(!dbRolle.equals(clientRolle))		//Rolle hat sich zwischenzeitlich geändert
					throw new ApplicationServerException(59);
	
				if(db.checkRolle(editedRolle) > 0)	//Rolle existiert schon
					throw new ApplicationServerException(8);
	
				db.updateRolle(editedRolle);
				db.insertLog(1, "Rolle (Id:" + clientRolle.getId() + ") aktualisiert: \n" +
												"Rolle: " + editedRolle.getBezeichnung() );
			
			} catch(ApplicationServerException e) {
				db.rollback();
				throw e;
			} finally {
				db.commit();
			}
		}
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#delRolle(dbObjects.Rolle)
	 */
	public void delRolle(Rolle rolle) throws ApplicationServerException {
		if(rolle != null){
			try{	
				Rolle dbRolle = db.selectForUpdateRolle(rolle);
	
				if(dbRolle == null)			// Rolle existiert nicht mehr
					throw new ApplicationServerException(7);
	
				if(!dbRolle.equals(rolle))		//Rolle hat sich zwischenzeitlich geändert
					throw new ApplicationServerException(59);
	
				if(db.selectBenutzerRolle(rolle) > 0)
					throw new ApplicationServerException(9);
	
				db.deleteRollenAktivitaeten(rolle.getId());
				db.deleteRolle(rolle);
				db.insertLog(2, "Rolle (Id:" + rolle.getId() + ") gelöscht: \n" +
												"Rolle: " + rolle.getBezeichnung() );
			
			} catch(ApplicationServerException e) {
				db.rollback();
				throw e;
			} finally {
				db.commit();
			}
		}
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getUser(java.lang.String, java.lang.String)
	 */
	public Benutzer getUser(String user, String password) throws ApplicationServerException {
		return db.selectUser(user, password);
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getZVKontenOnly()
	 */
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

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#setKontenZuordnung(dbObjects.FBHauptkonto, dbObjects.Kontenzuordnung)
	 */
	public void setKontenZuordnung(FBHauptkonto fbKonto, Kontenzuordnung clientZuordnung) throws ApplicationServerException {
		try{	
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
			db.insertLog(1, "KontenZuordnung aktualisiert: \n" +
											"FBHauptkonto: " + fbKonto );
			
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#addKontenZuordnung(dbObjects.FBHauptkonto, dbObjects.ZVKonto)
	 */
	public void addKontenZuordnung(FBHauptkonto fbKonto, ZVKonto zvKonto) throws ApplicationServerException  {
		try{	
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
			db.insertLog(0, "KontenZuordnung hinzugefügt: \n" +
											"FBHauptkonto: " + fbKonto + "\n" +											"ZVKonto: " + zvKonto );
			
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}	
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#delKontenZuordnung(dbObjects.FBHauptkonto, dbObjects.ZVKonto)
	 */
	public void delKontenZuordnung(FBHauptkonto fbKonto, ZVKonto zvKonto) throws ApplicationServerException  {
		try{	
			db.deleteKontenZuordnung(fbKonto.getId(), zvKonto.getId());
			db.insertLog(2, "KontenZuordnung gelöscht: \n" +
											"FBHauptkonto: " + fbKonto + "\n" +
											"ZVKonto: " + zvKonto );
			
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getFBKonto(int)
	 */
	public FBUnterkonto getFBKonto(int fbKontoId) throws ApplicationServerException {
		return db.selectFBKonto(fbKontoId);
	}
	
	/**
	 * Abfrage aller Firmen in der Datenbank.
	 * @return Liste mit allen Firmen in der Datenbank.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public ArrayList getFirmen() throws ApplicationServerException {
		return db.selectFirmen();
	}
	
	/**
	 * Eine neue Firma erstellen.
	 * @param Firma, die erstellt werden soll.
	 * @return id der erstellten Firma.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int addFirma( Firma firma ) throws ApplicationServerException {
		if( firma == null )		// Wurde eine Firma angegeben
			return 0;
		try {
			if( db.existsFirma( firma ) > 0 )				// Wenn diese Firma bereits existiert 
				throw new ApplicationServerException( 38 );	// dann kann man es nicht mehr erstellen
			int id = 0;		// id der Firma
			if( (id = db.existsDelFirma( firma )) > 0 ) {	// Es gibt eine gelöschte Firma
				firma.setId( id );		// Id der gelöschten Firma ist jetzt id der neuen Firma
				db.selectForUpdateFirma( firma );	// Zum Aktualisieren auswählen
				return db.updateFirma( firma );		// Aktualisieren
			}
			db.insertLog(0, "Firma (Id: " + firma.getId() + " hinzugefügt: \n" +
											"Firma: " + firma );
			
			return db.insertFirma( firma );			// Sonst neu erstellen
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}
	}
	
	/**
	 * Eine Firma in der Datenbank aktualisieren.
	 * @param Firma, die aktualisiert werden soll.
	 * @return id der aktualisierten Firma.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int setFirma( Firma firma ) throws ApplicationServerException {
		if( firma == null )		// Wurde eine Firma angegeben
			return 0;
		try {
			if( db.existsDelFirma( firma ) > 0 )			// Wenn diese Firma bereits gelöscht ist
				throw new ApplicationServerException( 39 );	// dann kann man es nicht aktualisieren
		
			db.selectForUpdateFirma( firma );		// Firma zu aktualisieren auswählen
			
			db.insertLog(1, "Firma (Id: " + firma.getId() + " aktualisiert: \n" +
											"Firma: " + firma );
			
			return db.updateFirma( firma );			// Firma aktualisieren und Id zurückgeben
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}
	}


	/**
	 * Eine Firma in der Datenbank löschen.
	 * @param Firma, die gelöscht werden soll.
	 * @return id der gelöschten Firma.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int delFirma( Firma firma ) throws ApplicationServerException {
		if( firma == null )					// Wurde eine Firma angegeben
			return 0;
		try {
			// Gibt es Belege oder Angebote, die an die angegebene Firma gehen
			if( db.countBelege( firma ) > 0 || db.countAngebote( firma ) > 0 )
				throw new ApplicationServerException( 42 );		// dann kann man es nicht löschen
		
			db.insertLog(2, "Firma (Id: " + firma.getId() + " gelöscht: \n" +
											"Firma: " + firma );
			
			return db.deleteFirma( firma );			// Sonst wird die Firma aus der Datenbank gelöscht
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
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
	public int addBestellung(StandardBestellung bestellung) throws ApplicationServerException {
		try{	
			// ReferenzNr prüfen
			System.out.println(bestellung.getReferenznr());
			int c = db.checkReferenzNr(bestellung.getReferenznr());
			System.out.println("count="+c);
			if(c > 0)
				throw new ApplicationServerException( 78 ); // ReferenzNr existiert schon
			
			int newBestellungId = db.insertBestellung(bestellung);
			int newAngebotId = 0;
			
			// fügt die Standardbestellung ein
			bestellung.setId(newBestellungId);
			db.insertStandardBestellung(bestellung);
			
			// Fügt alle Angebote ein
			for(int i = 0; i < bestellung.getAngebote().size(); i++){
				Angebot angebot = (Angebot)bestellung.getAngebote().get(i);
				ArrayList positionen = angebot.getPositionen();
				
				newAngebotId = db.insertAngebot(angebot, newBestellungId);
				
				// fügt alle Positionen ein
				for(int j = 0; j < positionen.size(); j++){
					Position position = (Position)positionen.get(j);
					
					db.insertPosition(position, newAngebotId);
				}
			}
			
			if(bestellung.getPhase() == '1'){
				// Vormerkungen bei FBKonto und ZVTitel setzen
				db.updateVormerkungen(bestellung.getFbkonto(), bestellung.getZvtitel(), bestellung.getFbkonto().getVormerkungen());
					// ? Vormerkungen Buchen ?
			}
			db.insertLog(0, "Standard-Bestellung (Id: " + bestellung.getId() + " hinzugefügt: \n" +
											"Besteller: " + bestellung.getBesteller().getName() + ", " + bestellung.getBesteller().getVorname() + "\n" +
											"Bestelldatum: " + bestellung.getDatum().toString() );
			
			db.commit();
			return newBestellungId;
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		}
	}

	/* (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#addBestellung(dbObjects.ASKBestellung)
	 */
	public int addBestellung(ASKBestellung bestellung) throws ApplicationServerException {
		try{
			int newBestellungId = db.insertBestellung(bestellung);
			int newAngebotId = 0;
			
			// fügt die ASKbestellung ein
			bestellung.setId(newBestellungId);
			db.insertASKBestellung(bestellung);
			
			// Fügt das Angebot ein
			Angebot angebot = (Angebot)bestellung.getAngebot();
			ArrayList positionen = angebot.getPositionen();
			
			newAngebotId = db.insertAngebot(angebot, newBestellungId);
			
			// fügt alle Positionen ein
			for(int j = 0; j < positionen.size(); j++){
				Position position = (Position)positionen.get(j);
				
				db.insertPosition(position, newAngebotId);
			}
			
			if(bestellung.getPhase() == '1'){
				// Vormerkungen bei FBKonto und ZVTitel setzen
				db.updateVormerkungen(bestellung.getFbkonto(), bestellung.getZvtitel(), bestellung.getFbkonto().getVormerkungen());
					// ? Vormerkungen Buchen ?
			}
			db.insertLog(0, "ASK-Bestellung (Id: " + bestellung.getId() + " hinzugefügt: \n" +											"Besteller: " + bestellung.getBesteller().getName() + ", " + bestellung.getBesteller().getVorname() + "\n" +
											"Bestelldatum: " + bestellung.getDatum().toString() );
			db.commit();
	
			return newBestellungId;
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		}
	}

	/* (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#setBestellung(dbObjects.StandardBestellung, dbObjects.StandardBestellung)
	 */
	public void setBestellung(Benutzer benutzer, StandardBestellung original, StandardBestellung edited) throws ApplicationServerException {
		
		try{
		
			// original StandardBestellung in der Datenbank
			StandardBestellung dbOriginal = db.selectForUpdateStandardBestellung(original.getId());
		
			ArrayList angebote = db.selectForUpdateAngebote(original.getId());
	
			for(int i = 0; i < angebote.size(); i++){
				Angebot angebot = (Angebot)angebote.get(i);
	
				angebot.setPositionen(db.selectForUpdatePositionen(angebot.getId())); // Positionen zu Angeboten hinzufügen
			}
			dbOriginal.setAngebote(angebote); // Angebote hinzufügen
			
			String logText = "";
		
			// die Bestellung hat sich zwischenzeitlich geändert
			if(!original.equals(dbOriginal))
				throw new ApplicationServerException( 76 );
				
			// die Referenznummer der Bestellung hat sich geändert
			if(!(original.getReferenznr().equals(edited.getReferenznr())))
				if(db.checkReferenzNr(edited.getReferenznr()) > 0)
					throw new ApplicationServerException( 78 ); // ReferenzNr existiert schon
			
				
			if(original.getPhase() == '0'){
				db.updateStandardBestellung(edited);
				actualizeAngebote(original.getAngebote(), edited.getAngebote(), edited.getId());
				logText += ", Angebot aktualisiert";
				
				if(edited.getPhase() == '1'){
					//	Vormerkungen bei FBKonto und ZVTitel setzen
					db.updateVormerkungen(edited.getFbkonto(), edited.getZvtitel(), edited.getFbkonto().getVormerkungen());
					bucheBestellungsaenderung(benutzer,edited,edited.getZvtitel(), edited.getFbkonto(), edited.getFbkonto().getVormerkungen());
					logText += ", Vormerkungen aktualisert und Bestellung geändert beim Konto:\n" +
										 "FB-Konto: " + edited.getFbkonto() + "\n" +
										 "ZV-Titel: " + edited.getZvtitel();
					
				}
			}else if((original.getPhase() == '1') || ((original.getPhase() == '2') && (edited.getPhase() == '3'))){
				db.updateStandardBestellung(edited);
				// TODO nur ein Angebot aktualisieren
				actualizeAngebote(original.getAngebote(), edited.getAngebote(), edited.getId());
	
				// Bestimme Bestellwertdifferenz
				float dBestellwert;
				if (edited.getPhase() == '3')
					dBestellwert = -original.getBestellwert();
				else dBestellwert = edited.getBestellwert() - original.getBestellwert();
				
				// Bestimme Zahlungsdifferenz
				float dZahlung = ( edited.getBestellwert() - edited.getVerbindlichkeiten()) - (original.getBestellwert() - original.getVerbindlichkeiten());
			
				// Aktualisiere Kontenstände
				if (dBestellwert != 0){ // => Bestellungsänderung
					
					//	Bestimme verfügbares ZV-Budget
					float availableZvBudget = 0.00f;
					if(original.getZvtitel() instanceof ZVTitel)
						availableZvBudget = db.getAvailableTgrBudget(((ZVTitel)original.getZvtitel()).getZVKonto().getId());
					else
						availableZvBudget = db.getAvailableTgrBudget(original.getZvtitel().getZVTitel().getZVKonto().getId());
					
					if (original.getZvtitel().getBudget() > original.getZvtitel().getVormerkungen())
						availableZvBudget += (original.getZvtitel().getBudget() - original.getZvtitel().getVormerkungen());
					// Bestimme verfügbares FB-Budget
					float availableFbBudget = original.getFbkonto().getBudget() - original.getFbkonto().getVormerkungen();
					
					if ((dBestellwert > availableZvBudget)||(dBestellwert > availableFbBudget)){
						throw new ApplicationServerException( 162 );
					}
					else {
						db.updateVormerkungen(original.getFbkonto(), original.getZvtitel(), dBestellwert);
						logText += ", Vormerkungen aktualisert und Bestellung ";
						if (edited.getPhase() != '3'){
							bucheBestellungsaenderung(benutzer, original, original.getZvtitel(), original.getFbkonto(), dBestellwert);
							logText += "geändert beim Konto:\n" +
												 "FB-Konto: " + edited.getFbkonto() + "\n" +
												 "ZV-Titel: " + edited.getZvtitel();
						}else {
							bucheStornoVormerkungen(benutzer, original, original.getZvtitel(), original.getFbkonto(), dBestellwert);
							logText += "storniert ";
						} 
					}
				}
				
				if (dZahlung != 0){ // => Positionen wurden beglichen oder 'zurückgezahlt'
					float tgrEntry, titelEntry;
					if (dZahlung < 0){
						float tgrExpenses = db.getTgrExpensesForOrder(original.getId());
						if (tgrExpenses > 0){
							tgrEntry = (tgrExpenses + dZahlung) > 0 ? -dZahlung : tgrExpenses;
							titelEntry = -(tgrEntry + dZahlung);
							
						}else{
							tgrEntry = 0;
							titelEntry = -dZahlung;
						}
					}else{
						float availableRessources = original.getZvtitel().getBudget() - original.getZvtitel().getVormerkungen();
						if (availableRessources > 0){
							titelEntry = availableRessources < dZahlung ? -availableRessources : -dZahlung;
							tgrEntry = availableRessources < dZahlung ? availableRessources-dZahlung : 0;
						}else{
							tgrEntry = -dZahlung;
							titelEntry = 0;
						}
					}
					
					db.updateAccountStates(original.getZvtitel(), tgrEntry, titelEntry, original.getFbkonto(), -dZahlung);
					ZVKonto zvk;
					if(original.getZvtitel() instanceof ZVTitel)
						zvk = ((ZVTitel)original.getZvtitel()).getZVKonto();
					else
						zvk = original.getZvtitel().getZVTitel().getZVKonto();
					
					if (edited.getPhase() != '3'){
						bucheBestellungsbegleichung(benutzer, original, zvk, tgrEntry, original.getZvtitel(), titelEntry, original.getFbkonto(), -dZahlung); 
						logText += "\nBestellung beglichen ";
					}else {
						bucheStornoZahlungen(benutzer, original, zvk, tgrEntry, original.getZvtitel(), titelEntry, original.getFbkonto(), -dZahlung);
						logText += "\nBestellungszahlungen storniert ";
					} 
				}
			}
			db.insertLog(1, "Standard-Bestellung (Id:" + original.getId() + " geändert: \n" +
											"Besteller: " + original.getBesteller().getName() + ", " + original.getBesteller().getVorname() + "\n" +
											"Bestelldatum: " + original.getDatum().toString() + "n" +
											 logText);
			
			db.commit();
		}catch (ApplicationServerException e){
			db.rollback();
			throw e;
		}
	}
	
	/**
	 * aktualisiert die Angebote einer Bestellung. Dazu gehört auch löschen, hinzufügen und ändern
	 * der Angebote.
	 * @param oldOffers
	 * @param newOffers
	 * @param bestellId
	 * @throws ApplicationServerException
	 */
	private void actualizeAngebote(ArrayList oldOffers, ArrayList newOffers, int bestellId) throws ApplicationServerException {
		if(!(oldOffers.equals(newOffers))){
			
			while((oldOffers.size() > 0) || (newOffers.size() > 0)){
				Angebot oldOffer = null;
				Angebot newOffer = null;
				
				if(oldOffers.size() > 0)
					oldOffer = (Angebot)oldOffers.get(0); // altes Angebot
	
				if(newOffers.size() > 0)
					newOffer = (Angebot)newOffers.get(0); // neues Angebot
					
				if(oldOffer == null){
					if(newOffer.getId() == 0){ // neues Angebot
						int offerId = db.insertAngebot(newOffer, bestellId);
						
						for(int j = 0; j < newOffer.getPositionen().size(); j++){ // Positionen einfügen
							Position p = (Position)newOffer.getPositionen().get(j);
							db.insertPosition(p, offerId);
						}
						newOffers.remove(newOffer);
					}
				}else{
					if(newOffer == null){ // altes Angebot löschen
						db.deleteOfferPositions(oldOffer.getId());
						db.deleteAngebot(oldOffer.getId());
						oldOffers.remove(oldOffer);
					}else{
						if(newOffer.getId() == 0){ // neues Angebot
							int offerId = db.insertAngebot(newOffer, bestellId);
							
							for(int j = 0; j < newOffer.getPositionen().size(); j++){ // Positionen einfügen
								Position p = (Position)newOffer.getPositionen().get(j);
								db.insertPosition(p, offerId);
							}
							newOffers.remove(newOffer);
						}else{
							if(oldOffer.getId() != newOffer.getId()){
								db.deleteOfferPositions(oldOffer.getId());
								db.deleteAngebot(oldOffer.getId());
								oldOffers.remove(oldOffer);
							}else{
								actualizeAngebot(oldOffer, newOffer, bestellId);
								oldOffers.remove(oldOffer);
								newOffers.remove(newOffer);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * aktualisiert ein vorhandenes Angebot in der Datenbank. Dazu gehört auch löschen, hinzufügen und ändern
	 * der Positionen.
	 * @param oldOffer - altes Angebot
	 * @param newOffer - neues Angebot
	 * @param bestellId - Id der Bestellung
	 * @throws ApplicationServerException
	 * author robert
	 */
	private void actualizeAngebot(Angebot oldOffer, Angebot newOffer, int bestellId) throws ApplicationServerException {
		if(!(oldOffer.equals(newOffer))){ // Angebot hat sich geändert
			
			db.updateAngebot(newOffer); // aktualisiert nur die Tabelle Angebote
			
			ArrayList oldPs = oldOffer.getPositionen(); // alte Positionen
			ArrayList newPs = newOffer.getPositionen(); // neue Positionen
			
			while((oldPs.size() > 0) || (newPs.size() > 0)){
				Position oldP = null;
				Position newP = null;

				if(oldPs.size() > 0)
					oldP = (Position)oldPs.get(0); // alte Position

				if(newPs.size() > 0)
					newP = (Position)newPs.get(0); // neue Position
	
				if(oldP == null){
					if(newP.getId() == 0){ // neues Angebot
						db.insertPosition(newP, newOffer.getId());
						newPs.remove(newP);
					}
				}else{
					if(newP == null){ // altes Angebot löschen
						db.deletePosition(oldP.getId());
						oldPs.remove(oldP);
					}else{
						if(newP.getId() == 0){ // neues Angebot
							db.insertPosition(newP, newOffer.getId());
							newPs.remove(newP);
						}else{
							if(oldP.getId() != newP.getId()){
								db.deletePosition(oldP.getId());
								oldPs.remove(oldP);
							}else{
								if(!oldP.equals(newP)){ // Position geändert
									db.updatePosition(newP); // aktualisieren
								}
								oldPs.remove(oldP);
								newPs.remove(newP);
							}
						}
					}
				}
			}
		}
	}

	/* (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#setBestellung(dbObjects.ASKBestellung, dbObjects.ASKBestellung)
	 */
	public void setBestellung(Benutzer benutzer, ASKBestellung original, ASKBestellung edited) throws ApplicationServerException {
		try{	
			// original ASKBestellung in der Datenbank
			ASKBestellung dbOriginal = db.selectForUpdateASKBestellung(original.getId());
		
			ArrayList angebote = db.selectForUpdateAngebote(original.getId());
	
			Angebot angebot = (Angebot)angebote.get(0);
			
			angebot.setPositionen(db.selectForUpdatePositionen(angebot.getId())); // Positionen zu Angeboten hinzufügen
			
			dbOriginal.setAngebot(angebot); // Angebote hinzufügen
			
			String logText = "";
		
			// die Bestellung hat sich zwischenzeitlich geändert
			if(!original.equals(dbOriginal))
				throw new ApplicationServerException( 76 );
				
			if(original.getPhase() == '0'){
				db.updateASKBestellung(edited);
				actualizeAngebot(original.getAngebot(), edited.getAngebot(), edited.getId());
				logText += ", Angebot aktualisiert";
				
				// es wurde auf den Button Bestellen gedrückt
				if(edited.getPhase() == '1'){
					// Vormerkungen bei FBKonto und ZVTitel setzen
					db.updateVormerkungen(edited.getFbkonto(), edited.getZvtitel(), edited.getFbkonto().getVormerkungen());
					logText += ", Vormerkungen aktualisert und Bestellung geändert beim Konto:\n" +										 "FB-Konto: " + edited.getFbkonto() + "\n" +										 "ZV-Titel: " + edited.getZvtitel();
					bucheBestellungsaenderung(benutzer,edited,edited.getZvtitel(), edited.getFbkonto(), edited.getFbkonto().getVormerkungen());
				}
			
			}else if((original.getPhase() == '1') || ((original.getPhase() == '2') && (edited.getPhase() == '3'))){
				db.updateASKBestellung(edited);
				actualizeAngebot(original.getAngebot(), edited.getAngebot(), edited.getId());
	
				// Bestimme Bestellwertdifferenz
				float dBestellwert;
				if (edited.getPhase() == '3')
					dBestellwert = -original.getBestellwert();
				else dBestellwert = edited.getBestellwert() - original.getBestellwert();
				System.out.println("dBestellwert = "+dBestellwert);
				// Bestimme Zahlungsdifferenz
				float dZahlung = ( edited.getBestellwert() - edited.getVerbindlichkeiten()) - (original.getBestellwert() - original.getVerbindlichkeiten());
				System.out.println("dZahlung = " + dZahlung);
				// Aktualisiere Kontenstände
				if (dBestellwert != 0){ // => Bestellungsänderung
					
					//	Bestimme verfügbares ZV-Budget
					float availableZvBudget = 0.00f;
					if(original.getZvtitel() instanceof ZVTitel)
						availableZvBudget = db.getAvailableTgrBudget(((ZVTitel)original.getZvtitel()).getZVKonto().getId());
					else
						availableZvBudget = db.getAvailableTgrBudget(original.getZvtitel().getZVTitel().getZVKonto().getId());
					
					if (original.getZvtitel().getBudget() > original.getZvtitel().getVormerkungen())
						availableZvBudget += (original.getZvtitel().getBudget() - original.getZvtitel().getVormerkungen());
					System.out.println("availableZvBudget = " + availableZvBudget);
					// Bestimme verfügbares FB-Budget
					float availableFbBudget = original.getFbkonto().getBudget() - original.getFbkonto().getVormerkungen();
					
					if ((dBestellwert > availableZvBudget)||(dBestellwert > availableFbBudget)){
						System.out.println("keine Deckung");//Error keine Deckung => rollback
						throw new ApplicationServerException( 162 );
					}
					else {
						db.updateVormerkungen(original.getFbkonto(), original.getZvtitel(), dBestellwert);
						System.out.println("db.updateVormerkungen(original.getFbkonto(), original.getZvtitel(), " + dBestellwert + ");");
						logText += ", Vormerkungen aktualisert und Bestellung ";
						if (edited.getPhase() != '3'){
							bucheBestellungsaenderung(benutzer, original, original.getZvtitel(), original.getFbkonto(), dBestellwert);
							logText += "geändert beim Konto:\n" +
												 "FB-Konto: " + edited.getFbkonto() + "\n" +
												 "ZV-Titel: " + edited.getZvtitel();
						}else {
							bucheStornoVormerkungen(benutzer, original, original.getZvtitel(), original.getFbkonto(), dBestellwert);
							logText += "storniert ";
						} 
					}
				}
			
				if (dZahlung != 0){ // => Positionen wurden beglichen oder 'zurückgezahlt'
					float tgrEntry, titelEntry;
					if (dZahlung < 0){
						float tgrExpenses = db.getTgrExpensesForOrder(original.getId());
						System.out.println("tgrExpenses: " + tgrExpenses);
						if (tgrExpenses > 0){
							tgrEntry = (tgrExpenses + dZahlung) > 0 ? -dZahlung : tgrExpenses;
							titelEntry = -(tgrEntry + dZahlung);
							
						}else{
							tgrEntry = 0;
							titelEntry = -dZahlung;
						}
					}else{
						float availableRessources = original.getZvtitel().getBudget() - original.getZvtitel().getVormerkungen();
						if (availableRessources > 0){
							titelEntry = availableRessources < dZahlung ? -availableRessources : -dZahlung;
							tgrEntry = availableRessources < dZahlung ? availableRessources-dZahlung : 0;
						}else{
							tgrEntry = -dZahlung;
							titelEntry = 0;
						}
					}
					
					db.updateAccountStates(original.getZvtitel(), tgrEntry, titelEntry, original.getFbkonto(), -dZahlung);
					System.out.println("db.updateAccountStates(original.getZvtitel(), " + tgrEntry + ", " + titelEntry + ", original.getFbkonto(), "+ (-dZahlung) + ");");
					
					ZVKonto zvk;
					if(original.getZvtitel() instanceof ZVTitel)
						zvk = ((ZVTitel)original.getZvtitel()).getZVKonto();
					else
						zvk = original.getZvtitel().getZVTitel().getZVKonto();
					
					if (edited.getPhase() != '3'){
						bucheBestellungsbegleichung(benutzer, original, zvk, tgrEntry, original.getZvtitel(), titelEntry, original.getFbkonto(), -dZahlung);
						logText += "\nBestellung beglichen ";
					}else{
						bucheStornoZahlungen(benutzer, original, zvk, tgrEntry, original.getZvtitel(), titelEntry, original.getFbkonto(), -dZahlung);
						logText += "\nBestellungszahlungen storniert ";
					}
				}
			}
			
			db.insertLog(1, "ASK-Bestellung (Id:" + original.getId() + " geändert: \n" +
											"Besteller: " + original.getBesteller().getName() + ", " + original.getBesteller().getVorname() + "\n" +
											"Bestelldatum: " + original.getDatum().toString() + "n" +
											 logText);
			
			db.commit();
		}catch (ApplicationServerException e){
			db.rollback();
			throw e;
		}
	
	}
	
	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getStandardBestellung(int)
	 */
	public StandardBestellung getStandardBestellung(int id) throws ApplicationServerException {
		StandardBestellung bestellung = db.selectStandardBestellung(id);
		
		ArrayList angebote = db.selectAngebote(id);
		
		for(int i = 0; i < angebote.size(); i++){
			Angebot angebot = (Angebot)angebote.get(i);
			
			angebot.setPositionen(db.selectPositionen(angebot.getId())); // Positionen zu Angeboten hinzufügen
		}
		
		bestellung.setAngebote(angebote); // Angebote hinzufügen
		
		return bestellung;
	}
	
	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getASKBestellung(int)
	 */
	public ASKBestellung getASKBestellung(int id) throws ApplicationServerException {
		ASKBestellung bestellung = db.selectASKBestellung(id);
		
		ArrayList angebote = db.selectAngebote(id);

		Angebot angebot = (Angebot)angebote.get(0);
		
		angebot.setPositionen(db.selectPositionen(angebot.getId())); // Positionen zu Angeboten hinzufügen
		
		bestellung.setAngebot(angebot); // Angebote hinzufügen

		return bestellung;
	}
	
	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getBestellungen(int)
	 */
	public ArrayList getBestellungen(int filter) throws ApplicationServerException{
		return db.selectBestellungen(filter);
	}

	/*
	 *  (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getBestellungen()
	 */
	public ArrayList getBestellungen() throws ApplicationServerException{
		return db.selectBestellungen(-1);
	}
	
	/* (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getInstituteWithAccounts(dbObjects.Institut, boolean)
	 */
	public Institut[] getInstituteWithAccounts(Institut institute, boolean subAccountsIncluded) throws ApplicationServerException {
		Institut[] instituts = {institute};
		
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
					  Kontenzuordnung[] zuordnung = db.selectKontenzuordnungen( ((FBHauptkonto)hauptkonten.get(j)));
					  ((FBHauptkonto)hauptkonten.get(j)).setZuordnung( zuordnung );
						
					  ArrayList unterkonten = db.selectFBUnterkonten( instituts[i], (FBHauptkonto)hauptkonten.get(j));
					  // die Kontenzuordnungen der FBHauptkonten werden übernommen
					  for(int k = 0; k < unterkonten.size(); k++)
						  ((FBUnterkonto)(unterkonten.get(k))).setZuordnung(zuordnung);
							
						((FBHauptkonto)hauptkonten.get(j)).setUnterkonten(unterkonten);
				 }
			 }
		 }

		 return instituts;
	}
	
	/**
	 * Mittelzuweisung an einen ZV-Kontentitel (Typ 1)
	 * @throws ApplicationServerException
	 */
	private void bucheMittelzuweisungZVTitel(Benutzer b, ZVUntertitel t, float buchung) throws ApplicationServerException {
		db.insertBuchung(new Buchung(b, "1", t, buchung));
	}
	
	/**
	 * Mittelzuweisung an die Titelgruppe eines ZV-Kontos (Typ 2)
	 * @throws ApplicationServerException
	 */
	private void bucheMittelzuweisungZVTitelgruppe(Benutzer b, ZVKonto k, float buchung) throws ApplicationServerException {
		db.insertBuchung(new Buchung(b, "2", k, buchung));
	}
	
	/**
	 * Mittelübernahme aus Geschäftsjahr für ZV-Kontentitel (Typ 3)
	 * @throws ApplicationServerException
	 */
	private void bucheZVMitteluebernahme(Benutzer b, ZVUntertitel von, ZVUntertitel nach, float betrag) throws ApplicationServerException {
		db.insertBuchung(new Buchung(b, "3", von, -betrag, nach, betrag));
	}
	
	/**
	 * Mittelübernahme aus Geschäftsjahr für FB-Konten (Typ 4)
	 * @throws ApplicationServerException
	 */
	private void bucheFBMitteluebernahme(Benutzer b, FBUnterkonto von, FBUnterkonto nach, float betrag) throws ApplicationServerException {
		db.insertBuchung(new Buchung(b, "4", von, -betrag, nach, betrag));
	}
	
	/**
	 * Mittelverteilung von ZV-Budget (zweckgebundenes / -ungebundenes) an FB-Konten (Typ 5)
	 * @throws ApplicationServerException
	 */
	private void bucheMittelverteilung(Benutzer b, FBUnterkonto k, float buchung) throws ApplicationServerException {
		db.insertBuchung(new Buchung(b, "5", k, buchung));
	}
	
	/**
	 * Mittelumverteilung zwischen FB-Konten (Typ 6)
	 * @throws ApplicationServerException
	 */
	private void bucheUmverteilung (Benutzer b, FBUnterkonto von, FBUnterkonto nach, float betrag) throws ApplicationServerException {
		db.insertBuchung(new Buchung(b, "6", von, -betrag, nach, betrag));
	}
	
//	/**
//	 * Vormerkung für Bestellung  (Typ 7)
//	 * ACHTUNG: evtl. überflüssig da durch Typ 8 abgedeckt
//	 * @throws ApplicationServerException
//	 */
//	private void bucheVormerkungen(Bestellung b, ZVUntertitel t, FBUnterkonto k, float buchung) throws ApplicationServerException {
//		
//	}
	
	/**
	 * Bestellungsänderung = Änderung der Vormerkung !!!(Typ 8)
	 * @throws ApplicationServerException
	 */
	private void bucheBestellungsaenderung(Benutzer benutzer, Bestellung bestellung, ZVUntertitel t, FBUnterkonto k, float buchung) throws ApplicationServerException {
		db.insertBuchung(new Buchung(benutzer, "8", bestellung, t, k, buchung));
	}
	
	/**
	 * Begleichung einer oder meherer Positionen einer Bestellung (Typ 9)
	 * = Änderung der Budgets und Vormerkungen
	 * @throws ApplicationServerException
	 */
	private void bucheBestellungsbegleichung(Benutzer benutzer, Bestellung bestellung, ZVKonto zvk, float tgrBuchung, ZVUntertitel t, float titelBuchung, FBUnterkonto fbk, float kontoBuchung) throws ApplicationServerException {
		db.insertBuchung(new Buchung(benutzer, "9", bestellung, zvk, tgrBuchung, t, titelBuchung, fbk, kontoBuchung));
	}
	
	/**
	 * Stornierung einer Bestellung Typ 10
	 * @throws ApplicationServerException
	 */
	private void bucheStornoVormerkungen(Benutzer benutzer, Bestellung bestellung, ZVUntertitel t, FBUnterkonto k, float buchung) throws ApplicationServerException {
		db.insertBuchung(new Buchung(benutzer, "10", bestellung, t, k, buchung));
	}

	/**
	 * Stornierung einer Bestellung Typ 11
	 * @throws ApplicationServerException
	 */
	private void bucheStornoZahlungen(Benutzer benutzer, Bestellung bestellung, ZVKonto zvk, float tgrBuchung, ZVUntertitel t, float titelBuchung, FBUnterkonto fbk, float kontoBuchung) throws ApplicationServerException {
		db.insertBuchung(new Buchung(benutzer, "11", bestellung, zvk, tgrBuchung, t, titelBuchung, fbk, kontoBuchung));
	}

	/**
	 * Eine Kleinbestellung in die Datenbank einfügen.
	 * @param bestellung = Kleinbestellung, die erstellt werden soll. 
	 * @return Id der eingefügten Bestellung. 
	 * @throws ApplicationServerException
	 * author w.flat
	 */	
	public int addKleinbestellung(KleinBestellung bestellung) throws ApplicationServerException {
		if(bestellung == null)		// keine Bestellung angegeben
			return 0;
		try {
			// Bestellung einfügen in der Bestellungs-Tabelle und Id speichern
			bestellung.setId(db.insertBestellung(bestellung));
			// Bestellung in der Kleinbestellung-Tabelle speichern
			db.insertKleinbestellung(bestellung);
			// Belege speichern
			for(int i = 0; i < bestellung.getBelege().size(); i++) {
				db.insertBeleg(bestellung.getId(), (Beleg)bestellung.getBelege().get(i));
			}
			//	Bestimme verfügbares ZV-Budget
			float availableZvBudget = 0.00f;
			if(bestellung.getZvtitel() instanceof ZVTitel)
				availableZvBudget = db.getAvailableTgrBudget(((ZVTitel)bestellung.getZvtitel()).getZVKonto().getId());
			else
				availableZvBudget = db.getAvailableTgrBudget(bestellung.getZvtitel().getZVTitel().getZVKonto().getId());
			if (bestellung.getZvtitel().getBudget() > bestellung.getZvtitel().getVormerkungen())
				availableZvBudget += (bestellung.getZvtitel().getBudget() - bestellung.getZvtitel().getVormerkungen());
			
			// Bestimme verfügbares FB-Budget
			float availableFbBudget = bestellung.getFbkonto().getBudget() - bestellung.getFbkonto().getVormerkungen();
				
			if ((bestellung.getBestellwert() > availableZvBudget)||(bestellung.getBestellwert() > availableFbBudget)){
				throw new ApplicationServerException( 162 );
			} else {
				db.updateVormerkungen(bestellung.getFbkonto(), bestellung.getZvtitel(), bestellung.getBestellwert());
				bucheBestellungsaenderung(bestellung.getBesteller(), bestellung, bestellung.getZvtitel(), 
										bestellung.getFbkonto(), bestellung.getBestellwert());
			}
			
			float tgrEntry, titelEntry;
			float availableRessources = bestellung.getZvtitel().getBudget() - bestellung.getZvtitel().getVormerkungen();
			if (availableRessources > 0){
				titelEntry = availableRessources < bestellung.getBestellwert() ? 	-availableRessources : 
																					-bestellung.getBestellwert();
				tgrEntry = availableRessources < bestellung.getBestellwert() ? availableRessources-bestellung.getBestellwert() : 0;
			}else{
				tgrEntry = -bestellung.getBestellwert();
				titelEntry = 0;
			}
				
			db.updateAccountStates(bestellung.getZvtitel(), tgrEntry, titelEntry, bestellung.getFbkonto(), 
																					-bestellung.getBestellwert());
			ZVKonto zvk;
			if(bestellung.getZvtitel() instanceof ZVTitel)
				zvk = ((ZVTitel)bestellung.getZvtitel()).getZVKonto();
			else
				zvk = bestellung.getZvtitel().getZVTitel().getZVKonto();
				
			bucheBestellungsbegleichung(bestellung.getBesteller(), bestellung, zvk, tgrEntry, bestellung.getZvtitel(), 
										titelEntry, bestellung.getFbkonto(), -bestellung.getBestellwert());

			db.insertLog(0, "Klein-Bestellung (Id: " + bestellung.getId() + " hinzugefügt: \n" +
											"Besteller: " + bestellung.getBesteller().getName() + ", " + bestellung.getBesteller().getVorname() + "\n" +
											"Bestelldatum: " + bestellung.getDatum().toString()+ "\n" +
											"FB-Konto: " +  bestellung.getFbkonto() + "\n" +
											"ZV-Titel: " + bestellung.getZvtitel() );
			
			return bestellung.getId();
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}		
	}

	/**
	 * Alle Kleinbestellung auswählen.
	 * @return Liste mit Bestellungen. 
	 * @throws ApplicationServerException
	 * author w.flat
	 */	
	public ArrayList getKleinbestellungen() throws ApplicationServerException {
		ArrayList bestellungen = db.selectKleinbestellungen();	// Liste mit Bestellungen
		KleinBestellung temp;		// temporäres Objekt zum Auswaählen der Belege
		for(int i = 0; i < bestellungen.size(); i++) {
			temp = (KleinBestellung)bestellungen.get(i);
			temp.setBelege(db.selectBelege(temp.getId()));	// Belege der Bestelllung ermitteln
		}
		return bestellungen;
	}

	/**
	 * Alle gelöschten Kleinbestellung auswählen.
	 * @return Liste mit gelöschten Bestellungen. 
	 * @throws ApplicationServerException
	 * author w.flat
	 */	
	public ArrayList getDelKleinbestellungen() throws ApplicationServerException {
		ArrayList bestellungen = db.selectDelKleinbestellungen();	// Liste mit Bestellungen
		KleinBestellung temp;		// temporäres Objekt zum Auswaählen der Belege
		for(int i = 0; i < bestellungen.size(); i++) {
			temp = (KleinBestellung)bestellungen.get(i);
			temp.setBelege(db.selectBelege(temp.getId()));	// Belege der Bestelllung ermitteln
		}
		return bestellungen;
	}
	
	/**
	 * Eine Kleinbestellung löschen. 
	 * @param Kleinbestellung, die gelöscht werden soll. 
	 * @return Id der gelöschten Bestellung. 
	 * @throws ApplicationServerException
	 * author w.flat
	 */	
	public int delKleinbestellung(KleinBestellung bestellung) throws ApplicationServerException {
		if(bestellung == null)		// keine Bestellung angegeben
			return 0;
		try {
			bestellung.setPhase('3');
			db.selectForUpdateKleinbestellung(bestellung);	// Zum Aktualisieren auswählen
			db.updateKleinbestellung(bestellung);		// Bestellung aktualisieren(löschen)
			// Abfrage der Buchung zur Ermittlung der Beträge, die abgebucht wurden 
			Buchung old = db.selectBuchung(bestellung.getId(), "9");
			if(old == null)		// Keine Buchung gefunden
				throw new ApplicationServerException(43);
			db.updateAccountStates(old.getZvKonto(), old.getZvTitel1(), old.getFbKonto1(), 
									-old.getBetragZvKonto(), -old.getBetragZvTitel1(), -old.getBetragFbKonto1());
			ZVKonto zvk;
			if(bestellung.getZvtitel() instanceof ZVTitel)
				zvk = ((ZVTitel)bestellung.getZvtitel()).getZVKonto();
			else
				zvk = bestellung.getZvtitel().getZVTitel().getZVKonto();
			bucheStornoZahlungen(bestellung.getBesteller(), bestellung, zvk, -old.getBetragZvKonto(), bestellung.getZvtitel(), 
									-old.getBetragZvTitel1(), bestellung.getFbkonto(), -old.getBetragFbKonto1());
			
			db.insertLog(2, "Klein-Bestellung (Id: " + bestellung.getId() + " gelöscht: \n" +
											"Besteller: " + bestellung.getBesteller().getName() + ", " + bestellung.getBesteller().getVorname() + "\n" +
											"Bestelldatum: " + bestellung.getDatum().toString() + "\n" +											"Stornozahlungen an:\n" +											"FB-Konto: " +  bestellung.getFbkonto() + "\n" +											"ZV-Titel: " + bestellung.getZvtitel());
			
			return bestellung.getId();
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} catch(Exception ex) {
			db.rollback();
			throw new ApplicationServerException(0);
		} finally {
			db.commit();
		}		
	}
	
	/**
	 * Eine Kleinbestellung mit einer bestimmter Id abfragen. 
	 * @param Id des Kontos. 
	 * @return Kleinbestellung die abgefragt wurde. 
	 * @throws ApplicationServerException
	 * author w.flat
	 */	
	public KleinBestellung getKleinbestellung(int id) throws ApplicationServerException {
		try {
			return db.selectKleinbestellung(id);
		} finally {
			db.commit();
		}		
	}

	/* (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#delBestellung(dbObjects.StandardBestellung)
	 */
	public void delBestellung(StandardBestellung delOrder) throws ApplicationServerException {
		try{	
			// original StandardBestellung in der Datenbank
			StandardBestellung dbOriginal = db.selectForUpdateStandardBestellung(delOrder.getId());
	
			ArrayList angebote = db.selectForUpdateAngebote(delOrder.getId());
	
			for(int i = 0; i < angebote.size(); i++){
				Angebot angebot = (Angebot)angebote.get(i);
	
				angebot.setPositionen(db.selectForUpdatePositionen(angebot.getId())); // Positionen zu Angeboten hinzufügen
			}
			dbOriginal.setAngebote(angebote); // Angebote hinzufügen
		
			// die Bestellung hat sich zwischenzeitlich geändert
			if(!delOrder.equals(dbOriginal))
				throw new ApplicationServerException( 76 );
			
			if(delOrder.getPhase() == '0'){
				// alle Position aller Angebote der Bestellung löschen
				db.deletePositions(delOrder.getId());
				// alle Angebote der Bestellung löschen
				db.deleteAngebote(delOrder.getId());
				// die StandardBestellung löschen
				db.deleteASK_Standard_Bestellung(delOrder.getId());
				// die Bestellung löschen
				db.deleteBestellung(delOrder.getId());
			}else{
				delOrder.setGeloescht(true);
				db.updateStandardBestellung(delOrder); // Flag gelöscht setzen
			}
			db.insertLog(2, "Standard-Bestellung gelöscht: \n" +
											"Besteller: " + delOrder.getBesteller().getName() + ", " + delOrder.getBesteller().getVorname() + "\n" +
											"Bestelldatum: " + delOrder.getDatum().toString() );
			
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}
	}


	/* (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#delBestellung(dbObjects.ASKBestellung)
	 */
	public void delBestellung(ASKBestellung delOrder) throws ApplicationServerException {
		try{	
			// original StandardBestellung in der Datenbank
			ASKBestellung dbOriginal = db.selectForUpdateASKBestellung(delOrder.getId());
	
			ArrayList angebote = db.selectForUpdateAngebote(delOrder.getId());
	
			Angebot angebot = (Angebot)angebote.get(0);
			
			angebot.setPositionen(db.selectForUpdatePositionen(angebot.getId())); // Positionen zu Angeboten hinzufügen
			
			dbOriginal.setAngebot(angebot); // Angebote hinzufügen
		
			// die Bestellung hat sich zwischenzeitlich geändert
			if(!delOrder.equals(dbOriginal))
				throw new ApplicationServerException( 76 );
			
			if(delOrder.getPhase() == '0'){
				// alle Position des Angebots der Bestellung löschen
				db.deletePositions(delOrder.getId());
				// das Angebot löschen
				db.deleteAngebote(delOrder.getId());
				// die StandardBestellung löschen
				db.deleteASK_Standard_Bestellung(delOrder.getId());
				// die Bestellung löschen
				db.deleteBestellung(delOrder.getId());
			}else{
				delOrder.setGeloescht(true);
				db.updateASKBestellung(delOrder); // Flag gelöscht setzen
			}
			
			db.insertLog(2, "ASK-Bestellung gelöscht: \n" +
											"Besteller: " + delOrder.getBesteller().getName() + ", " + delOrder.getBesteller().getVorname() + "\n" +
											"Bestelldatum: " + delOrder.getDatum().toString() );
			
		} catch(ApplicationServerException e) {
			db.rollback();
			throw e;
		} finally {
			db.commit();
		}
	}

	/* (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getSwBeauftragte()
	 */
	public Benutzer[] getSwBeauftragte() throws ApplicationServerException {
		return db.selectSwBeauftragte();
	}

	/* (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getASKFirma()
	 */
	public Firma getASKFirma() throws ApplicationServerException {
		return db.selectASKFirma();
	}

	/* (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getReport(int, dbObjects.Institut)
	 */
	public ArrayList getReport(int typ, Date von, Date bis) throws ApplicationServerException {
		
		if(typ == 1){
			return db.selectReport1(von, bis);
		}if(typ == 2){
			return db.selectReport2(von, bis);
		}if(typ == 3){
			return db.selectReport3(von, bis);
		}else if(typ == 4){
			return db.selectReport4(von, bis);
		}else if(typ == 5){
			return db.selectReport5(von, bis);
		}else if(typ == 6){
			return db.selectReport6(von, bis);
		}else if(typ == 7){
			return db.selectReport7(von, bis);
		}else if(typ == 8){
			return db.selectReport8(von, bis);
		}
		
		return null;
	}

	/* (Kein Javadoc)
	 * @see applicationServer.ApplicationServer#getLogList(java.sql.Date, java.sql.Date)
	 */
	public ArrayList getLogList(Date von, Date bis) throws ApplicationServerException {
		return db.selectLogList(von, bis);
	}
}


