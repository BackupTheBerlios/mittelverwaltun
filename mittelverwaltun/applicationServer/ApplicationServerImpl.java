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
	 * Ermittlung der HaushaltsjahrId vom aktuellem Jahr
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
	 */
	public ArrayList getFBHauptkonten( Institut institut ) throws ApplicationServerException {
		ArrayList hauptkonten;

		hauptkonten = db.selectFBHauptkonten( institut );
		for( int j = 0; j < hauptkonten.size(); j++ ) {
			if( hauptkonten.get(j) == null )	// kein FBHauptkonto
				continue;
			// Ermittlung der Kontenzuordnungen vom Hauptkonto
			((FBHauptkonto)hauptkonten.get(j)).setZuordnung( db.selectKontenzuordnungen( ((FBHauptkonto)hauptkonten.get(j)) ) );
		}
		return hauptkonten;
	}

	/**
	 * Abfrage von Unterkonten eines bestimmten Instituts von einem bestimmten Hauptkonto.
	 */
	public ArrayList getFBUnterkonten( Institut institut, FBHauptkonto hauptkonto ) throws ApplicationServerException {
		return db.selectFBUnterkonten( institut, hauptkonto );
	}
	
	/**
	 * Ein neues FBHauptkonto erstellen.
	 * @return id des eingefügten Hauptkontos
	 * @throws ApplicationServerException
	 */
	public int addFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {
		if( konto == null )
			return 0;
		if( db.existsFBKonto( konto ) > 0 )				// Wenn dieses FBHauptkonto bereits existiert 
			throw new ApplicationServerException( 17 );	// dann kann man es nicht mehr erstellen
		
		int id = 0;
		if( (id = db.existsDeleteFBKonto( konto )) > 0 ){
			konto.setId( id );
			db.selectForUpdateFBHauptkonto( konto );
			int hkId = db.updateFBHauptkonto( konto );
			db.commit();
			return hkId;
		}
		
		return db.insertFBHauptkonto( konto );
	}

	/**
	 * Ein neues FBUnterkonto erstellen
	 * @return id des eingefügten Unterkontos
	 * @throws ApplicationServerException
	 */
	public int addFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {
		if( konto == null )
			return 0;
		if( db.existsFBKonto( konto ) > 0 )		// Wenn ein FBUnterkonto bereits existiert
			throw new ApplicationServerException( 19 );
		
		int id = 0;
		if( (id = db.existsDeleteFBKonto( konto )) > 0 ){
			konto.setId( id );
			db.selectForUpdateFBUnterkonto( konto );
			int ukId = db.updateFBUnterkonto( konto );
			db.commit();
			return ukId;
		}
		
		return db.insertFBUnterkonto( konto );
	}

	/**
	 * Ein FBHauptkonto löschen. Dabei werden auch die Unterkonten gelöscht.
	 * @return id des gelöschten Hauptkontos
	 * @throws ApplicationServerException
	 */
	public int delFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {
		if( konto == null )		// Kein FBHauptkonto
			return 0;
		
		if( db.countActiveBestellungen( konto ) > 0 )		// Wenn Bestellungen noch nicht abgeschlossen sind 
			throw new ApplicationServerException( 20 );
		if( db.countKontenzuordnungen( konto ) > 0 )		// Wenn Kontenzuordnungen existieren
			throw new ApplicationServerException( 21 );
	
		ArrayList temp = ((FBHauptkonto)konto).getUnterkonten();
		// Nachsehen ob sich bei den Unterkonten Fehler ergeben
		if( temp != null ) {		// Es gibt Unterkonten
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
				// Wenn es abgeschlossene Bestellungen noch gibt oder Buchungen, die man zur Statistik braucht,
				// dann wird nur das Flag gesetzt, sonst wird ganz gelöscht
				if( (db.countBestellungen( (FBUnterkonto)temp.get(i) ) > 0) ||		
																(db.countBuchungen( (FBUnterkonto)temp.get(i) ) > 0) ) {
					db.selectForUpdateFBUnterkonto( (FBUnterkonto)temp.get(i) );	// Zum Aktualisieren rausholen
					((FBUnterkonto)temp.get(i)).setGeloescht( true );		// Flag gelöscht setzen
					db.updateFBUnterkonto( (FBUnterkonto)temp.get(i) );		// Und dann aktualisieren
					delFBHauptkonto = false;
				} else {
					db.deleteFBKonto( (FBUnterkonto)temp.get(i) );			// Sonst ganz löschen
				}
			}
		}
		
		// Es gibt abgeschlossene Bestellungen noch oder Buchungen
		if( (db.countBestellungen( konto ) > 0) || (db.countBuchungen( konto ) > 0) || !delFBHauptkonto ) {
			db.selectForUpdateFBHauptkonto( konto );
			konto.setGeloescht( true );
			return db.updateFBHauptkonto( konto );
		}
		// Sonst wird das Hauptkonto ganz gelöscht
		if( db.deleteFBKonto( konto ) > 0)
			return konto.getId();
		else
			return 0;
	}
	
	/**
	 * Ein FBUnterkonto löschen.
	 * @return id des gelöschten Unterkontos
	 * @throws ApplicationServerException
	 */
	public int delFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {
		if( konto == null )		// Kein FBUnterkonto
			return 0;
		
		if( db.countActiveBestellungen( konto ) > 0 )		// Wenn Bestellungen noch nicht abgeschlossen sind 
			throw new ApplicationServerException( 20 );
		if( db.countActiveBenutzer( konto ) >  0 )			// Unterkonto ist einem Benutzer zugeordnet
			throw new ApplicationServerException( 22 );
		
		// Es gibt abgeschlossene Bestellungen noch oder Buchungen
		if( (db.countBestellungen( konto ) > 0) || (db.countBuchungen( konto ) > 0) ) {
			db.selectForUpdateFBUnterkonto( konto );
			konto.setGeloescht( true );
			return db.updateFBUnterkonto( konto );
		}
		
		if( db.deleteFBKonto( konto ) > 0)
			return konto.getId();
		else
			return 0;
	}

	/**
	 * Ein FBHauptkonto aktualisieren.
	 * @return id des aktualisierten FBHauptkontos
	 * @throws ApplicationServerException
	 */
	public int setFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {
		if( konto == null )		// Kein Konto angegeben
			return 0;
		
		FBHauptkonto old = db.selectForUpdateFBHauptkonto( konto );	// Zum aktualisieren auswählen
		if( old == null )		// Kein FBHauptonto gefunden
			throw new ApplicationServerException( 16 );
		if( old.getGeloescht() )	// Das FBHauptonto gelöscht
			throw new ApplicationServerException( 23 );
		
		if( old.equals( konto ) )
			return db.updateFBHauptkonto( konto );
			
		if( db.existsFBKonto( konto ) > 0 )			// Das neue Konto existiert bereits
			throw new ApplicationServerException( 17 );
			
		if( db.countActiveBestellungen( konto ) > 0 )	// gibt es Bestellungen beim Hauptkonto
			throw new ApplicationServerException( 20 );
		
		ArrayList temp = ((FBHauptkonto)konto).getUnterkonten();
		// Nachsehen ob sich bei den Unterkonten Fehler ergeben
		if( temp != null ) {		// Es gibt Unterkonten
			for( int i = 0; i < temp.size(); i++ ) {
				if( temp.get(i) == null )	// Kein Unterkonto
					continue;
				if( db.countActiveBestellungen( (FBUnterkonto)temp.get(i) ) > 0 )	// gibt es Bestellungen 
					throw new ApplicationServerException( 20 );
			}
		}
		
		// Alle Unterkonten ändern 
		if( temp != null ) {		// Es gibt Unterkonten
			for( int i = 0; i < temp.size(); i++ ) {
				if( temp.get(i) == null )	// Kein Unterkonto
					continue;
				db.updateFBUnterkonto( (FBUnterkonto)temp.get(i) );
			}
		}
		
		return db.updateFBHauptkonto( konto );
	}

	/**
	 * Ein FBUnterkonto aktualisieren.
	 * @return id des aktualisierten FBUnterkontos
	 * @throws ApplicationServerException
	 */
	public int setFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {
		if( konto == null )		// Kein Konto angegeben
			return 0;
		
		FBUnterkonto old = db.selectForUpdateFBUnterkonto( konto );	// Zum aktualisieren auswählen
		if( old == null )		// Kein FBUnterkonto gefunden
			throw new ApplicationServerException( 18 );
		if( old.getGeloescht() )	// Das FBUnterkonto gelöscht
			throw new ApplicationServerException( 24 );
		
		if( old.equals( konto ) )		// Wenn die Konten gleich sind
			return db.updateFBUnterkonto( konto );
			
		if( db.existsFBKonto( konto ) > 0 )			// Das neue Konto existiert bereits
			throw new ApplicationServerException( 19 );
			
		if( db.countActiveBestellungen( konto ) > 0 )	// gibt es Bestellungen beim Unterkonto
			throw new ApplicationServerException( 20 );

		return db.updateFBUnterkonto( konto );
	}
	
	/**
	 * Budget von einem FBHauptkonto auf ein FBUnterkonto buchen.
	 * @param FBHauptkonto von
	 * @param FBUnterkonto nach
	 * @param float Betrag
	 */
	public void buche( FBHauptkonto haupt, FBUnterkonto unter, float betrag ) throws ApplicationServerException {
		if( haupt == null || unter == null )
			return;
		FBHauptkonto oldHaupt = db.selectForUpdateFBHauptkonto( haupt );	// Das FBHauptkonto auswählen
		if( oldHaupt == null )				// Wenn kein FBHauptkonto in der Datenbank gefunden
			throw new ApplicationServerException( 16 );
		if( oldHaupt.getGeloescht() )		// Wenn das FBHauptkonto gelöscht ist
			throw new ApplicationServerException( 23 );
		if( !oldHaupt.equals( haupt ) )		// Wenn die FBHauptkonten nicht gleich sind, dann wurde das Konto verändert
			throw new ApplicationServerException( 25 );
		// wenn die Budgets oder Dispolimits nicht übereinstimmen
		if( haupt.getBudget() != oldHaupt.getBudget() || haupt.getDispoLimit() != oldHaupt.getDispoLimit() )
			throw new ApplicationServerException( 26 );
		haupt.setBudget( haupt.getBudget() - betrag );
		db.updateFBHauptkonto( haupt );
		
		FBUnterkonto oldUnter = db.selectForUpdateFBUnterkonto( unter );		// Das FBUnterkonto auswählen
		if( oldUnter == null ) {			// Wenn kein FBUnterkonto in der Datenbank gefunden
			db.updateFBHauptkonto( oldHaupt );
			throw new ApplicationServerException( 18 );
		}
		if( oldUnter.getGeloescht() ) {		// Wenn das FBUnterkonto gelöscht ist
			db.updateFBHauptkonto( oldHaupt );
			throw new ApplicationServerException( 24 );
		}
		if( !oldUnter.equals( unter ) ) {		// Wenn die FBUnterkonten nicht gleich sind, dann wurde das Konto verändert
			db.updateFBHauptkonto( oldHaupt );
			throw new ApplicationServerException( 27 );
		}
		unter.setBudget( unter.getBudget() + betrag );
		db.updateFBUnterkonto( unter );
	}
	
	/**
	 * Budget von einem FBHauptkonto auf ein anderes FBHauptkonto buchen.
	 * @param FBHauptkonto von
	 * @param FBHauptkonto nach
	 * @param float Betrag
	 */
	public void buche( FBHauptkonto from, FBHauptkonto to, float betrag ) throws ApplicationServerException {
		if( from == null || to == null )
			return;
		FBHauptkonto oldFrom = db.selectForUpdateFBHauptkonto( from );	// Das FBHauptkonto auswählen
		if( oldFrom == null )				// Wenn kein FBHauptkonto in der Datenbank gefunden
			throw new ApplicationServerException( 16 );
		if( oldFrom.getGeloescht() )		// Wenn das FBHauptkonto gelöscht ist
			throw new ApplicationServerException( 23 );
		if( !oldFrom.equals( from ) )		// Wenn die FBHauptkonten nicht gleich sind, dann wurde das Konto verändert
			throw new ApplicationServerException( 25 );
		// wenn die Budgets oder Dispolimits nicht übereinstimmen
		if( oldFrom.getBudget() != from.getBudget() || oldFrom.getDispoLimit() != from.getDispoLimit() )
			throw new ApplicationServerException( 26 );
		from.setBudget( from.getBudget() - betrag );
		db.updateFBHauptkonto( from );
		
		FBHauptkonto oldTo = db.selectForUpdateFBHauptkonto( to );	// Das FBHauptkonto auswählen
		if( oldTo == null )	{			// Wenn kein FBHauptkonto in der Datenbank gefunden
			db.updateFBHauptkonto( oldFrom );
			throw new ApplicationServerException( 16 );
		}
		if( oldTo.getGeloescht() ) {	// Wenn das FBHauptkonto gelöscht ist
			db.updateFBHauptkonto( oldFrom );
			throw new ApplicationServerException( 23 );
		}
		if( !oldTo.equals( to ) ) {		// Wenn die FBHauptkonten nicht gleich sind, dann wurde das Konto verändert
			db.updateFBHauptkonto( oldFrom );
			throw new ApplicationServerException( 25 );
		}
		to.setBudget( to.getBudget() + betrag );
		db.updateFBHauptkonto( to );
	}
	
	/**
	 * Budget von einem FBUnterkonto auf ein anderes FBHauptkonto buchen.
	 * @param FBUnterkonto von
	 * @param FBHauptkonto nach
	 * @param float Betrag
	 */
	public void buche( FBUnterkonto unter, FBHauptkonto haupt, float betrag ) throws ApplicationServerException {
		if( unter == null || haupt == null )
			return;
		FBUnterkonto oldUnter = db.selectForUpdateFBUnterkonto( unter );	// Das FBUnterkonto auswählen
		if( oldUnter == null )				// Wenn kein FBUnterkonto in der Datenbank gefunden
			throw new ApplicationServerException( 18 );
		if( oldUnter.getGeloescht() )		// Wenn das FBUnterkonto gelöscht ist
			throw new ApplicationServerException( 24 );
		if( !oldUnter.equals( unter ) )		// Wenn die FBUnterkonten nicht gleich sind, dann wurde das Konto verändert
			throw new ApplicationServerException( 27 );
		// wenn die Budgets nicht übereinstimmen
		if( oldUnter.getBudget() != unter.getBudget() )
			throw new ApplicationServerException( 28 );
		unter.setBudget( unter.getBudget() - betrag );
		db.updateFBUnterkonto( unter );
		
		FBHauptkonto oldHaupt = db.selectForUpdateFBHauptkonto( haupt );	// Das FBHauptkonto auswählen
		if( oldHaupt == null ) {				// Wenn kein FBHauptkonto in der Datenbank gefunden
			db.updateFBUnterkonto( oldUnter );
			throw new ApplicationServerException( 16 );
		}
		if( oldHaupt.getGeloescht() ) {		// Wenn das FBHauptkonto gelöscht ist
			db.updateFBUnterkonto( oldUnter );
			throw new ApplicationServerException( 23 );
		}
		if( !oldHaupt.equals( haupt ) )	{	// Wenn die FBHauptkonten nicht gleich sind, dann wurde das Konto verändert
			db.updateFBUnterkonto( oldUnter );
			throw new ApplicationServerException( 25 );
		}
		haupt.setBudget( haupt.getBudget() + betrag );
		db.updateFBHauptkonto( haupt );
	}
	
	/**
	 * Abfrage der ZVKonten mit den dazugehörigen ZVTiteln und ZVUntertiteln.
	 */
	public ArrayList getZVKonten() throws ApplicationServerException {
		ArrayList zvKonten = db.selectZVKonten();	// Es werden alle ZVKonten ermittelt
		ArrayList zvTitel;
		
		if( zvKonten == null )	// Keine ZVKonten
			return null;

		// Schleife zur Ermittlung der ZVTitel eines ZVKontos
		for( int i = 0; i < zvKonten.size(); i++ ) {
			if( zvKonten.get(i) == null )	// kein ZVKonto
				continue;

			((ZVKonto)zvKonten.get(i)).setSubTitel( zvTitel = db.selectZVTitel( (ZVKonto)zvKonten.get(i) ) );
			if( zvTitel == null )		// Keine ZVTitel
				continue;

			// Schleife zur Ermittlung aller ZVUntertitel von einem ZVTitel
			for( int j = 0; j < zvTitel.size(); j++ ) {
				if( zvTitel.get(j) == null )	// kein ZVTitel
					continue;
				// Ermittlung der ZVUntertitel vom ZVTitel
				((ZVTitel)zvTitel.get(j)).setSubUntertitel( db.selectZVUntertitel( (ZVTitel)zvTitel.get(j) ) );
			}
		}

		return zvKonten;
	}
	
	/**
	 * Ein neues ZVKonto in die Datenbank einfügen.
	 * @return id vom eingefügtem ZVKonto
	 * @throws ApplicationServerException
	 */
	public int addZVKonto( ZVKonto zvKonto ) throws ApplicationServerException {
		if( zvKonto == null )		// Wenn kein ZVKonto
			return 0;
		if( db.existsZVKonto( zvKonto ) > 0 )		// Wenn das ZVKonto bereits existiert
			throw new ApplicationServerException( 11 );
		if( !zvKonto.isTGRKonto() ){
			if( db.existsZVTitel( (ZVTitel)zvKonto.getSubTitel().get(0) ) > 0 )		// Wenn das ZVTitel bereits existiert
				throw new ApplicationServerException( 11 );
		}
		
		int zvKontoId = 0;
		if( (zvKontoId = db.existsDeleteZVKonto( zvKonto )) > 0 ){	// Wenn ein gelöschtes ZVKonto exitiert, dann aktualisieren
			zvKonto.setId( zvKontoId );
			db.selectForUpdateZVKonto( zvKonto );
			if( zvKonto.isTGRKonto() )
				return db.updateZVKonto( zvKonto );	// Dann nur das ZVKonto aktualisieren
			else {		// Sonst muss man auch den ZVTitel aktualisieren oder erstellen
				zvKontoId = db.updateZVKonto( zvKonto );
				// Wenn ein gelöschtes ZVTitel exitiert, dann aktualisieren
				int zvTitelId = db.existsDeleteZVTitel( (ZVTitel)zvKonto.getSubTitel().get(0) ); 
				if( zvTitelId > 0 ) {
					((ZVTitel)zvKonto.getSubTitel().get(0)).setId( zvTitelId );
					db.selectForUpdateZVTitel( (ZVTitel)zvKonto.getSubTitel().get(0) );
					db.updateZVTitel( (ZVTitel)zvKonto.getSubTitel().get(0) );
				} else {
					db.insertZVTitel( (ZVTitel)zvKonto.getSubTitel().get(0) );
				}
				
				return zvKontoId;
			}
		}
		
		if( zvKonto.isTGRKonto() )
			return db.insertZVKonto( zvKonto );
		else {
			zvKontoId = db.insertZVKonto( zvKonto );
			if( zvKontoId > 0 ) {
				zvKonto.setId( zvKontoId );
				db.insertZVTitel( (ZVTitel)zvKonto.getSubTitel().get(0) );
			}
			
			return zvKontoId;
		}
	}
	
	/**
	 * Abfrage der Id von einem ZVTitel.
	 * @param zvTitel
	 * @return id des ZVTitels
	 * @throws ApplicationServerException
	 */
	public int getZVTitelId( ZVTitel zvTitel ) throws ApplicationServerException {
		return db.existsZVTitel( zvTitel );
	}

	/**
	 * Einen neuen ZVTitel in die Datenbank einfügen.
	 * @return id vom eingefügtem ZVTitel
	 * @throws ApplicationServerException
	 */
	public int addZVTitel( ZVTitel zvTitel ) throws ApplicationServerException {
		if( zvTitel == null )		// Wenn kein ZVTitel
			return 0;
		if( db.existsZVTitel( zvTitel ) > 0 )		// Wenn der ZVTitel bereits existiert
			throw new ApplicationServerException( 11 );
			
		int id = 0;
		if( (id = db.existsDeleteZVTitel( zvTitel )) > 0 )	{	// Wenn ein gelöschtes ZVTitel exitiert, dann aktualisieren
			zvTitel.setId( id );
			db.selectForUpdateZVTitel( zvTitel );
			return db.updateZVTitel( zvTitel );
		}
		
		return db.insertZVTitel( zvTitel );
	}

	/**
	 * Einen neuen ZVUntertitel in die Datenbank einfügen.
	 * @return id vom eingefügtem ZVUntertitel
	 * @throws ApplicationServerException
	 */
	public int addZVUntertitel( ZVUntertitel zvUntertitel ) throws ApplicationServerException {
		if( zvUntertitel == null )		// Wenn kein ZVUntertitel
			return 0;
		if( db.existsZVUntertitel( zvUntertitel ) > 0 )		// Wenn der ZVUntertitel bereits existiert
			throw new ApplicationServerException( 11 );
			
		int id = 0;
		// Wenn ein gelöschtes ZVUntertitel exitiert, dann aktualisieren
		if( (id = db.existsDeleteZVUntertitel( zvUntertitel )) > 0 ) {
			zvUntertitel.setId( id );
			db.selectForUpdateZVUntertitel( zvUntertitel );
			return db.updateZVUntertitel( zvUntertitel );
		}
		
		return db.insertZVUntertitel( zvUntertitel );
	}

	/**
	 * Ein ZVKonto aus der Datenbank löschen. Dabei müssen auch alle dazugehörigen ZVTitel und ZVUntertitel gelöscht werden.
	 * @return id vom gelöschtem ZVKonto
	 * @throws ApplicationServerException
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
							db.selectForUpdateZVUntertitel( (ZVUntertitel)zvUntertitel.get(j) );
							((ZVUntertitel)zvUntertitel.get(j)).setGeloescht( true );
							db.updateZVUntertitel( (ZVUntertitel)zvUntertitel.get(j) );
							delZVTitel = false;
						} else {
							db.deleteZVUntertitel( (ZVUntertitel)zvUntertitel.get(j) );
						}
					}	// Ende for zvUntertitel
				}	// Ende if( zvUntertitel != null )
				
				// Wenn es abgeschlossene Bestellungen oder Buchungen gibt
				if( (db.countBestellungen( (ZVTitel)zvTitel.get(i) ) > 0) ||
													(db.countBuchungen( (ZVTitel)zvTitel.get(i) ) > 0) || !delZVTitel) {
					db.selectForUpdateZVTitel( (ZVTitel)zvTitel.get(i) );
					((ZVTitel)zvTitel.get(i)).setGeloescht( true );
					db.updateZVTitel( (ZVTitel)zvTitel.get(i) );
					delZVKonto = false;
				} else {
					db.deleteZVTitel( (ZVTitel)zvTitel.get(i) );
				}
			}	// Ende for zvTitel
		}	// Ende if( zvTitel != null )
		
		// Und zum Schluss das ZVKonto löschen
		if( delZVKonto ) {
			if( db.deleteZVKonto( zvKonto ) > 0 )
				return zvKonto.getId();
			else
				return 0;
		} else {
			db.selectForUpdateZVKonto( zvKonto );
			zvKonto.setGeloescht( true );
			return db.updateZVKonto( zvKonto );
		}
	}
	
	
	/**
	 * Anzahl der Kontenzuordnungen von einem ZVKonto ermtteln
	 * @param zvKonto
	 * @return anzahl der Kontenzuordnungen
	 * @throws ApplicationServerException
	 */
	public int getNumberOfKontenzuordnungen( ZVKonto zvKonto ) throws ApplicationServerException {
		return db.countZVKonten( zvKonto );
	}

	/**
	 * Einen ZVTitel in der Datenbank löschen. Dabei müssen auch alle ZVUntertitel gelöscht werden.
	 * @return id vom gelöschtem ZVTitel
	 * @throws ApplicationServerException
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
		
		// Löschen von ZVUntertiteln 
		boolean delZVTitel = true; 	// Variable um festzustellen ob der ZVTitel ganz gelöscht werden sollte
		if( zvUntertitel != null ) {	// Gibt es ZVUntertitel
			for( int j = 0; j < zvUntertitel.size(); j++ ) {
				if( zvUntertitel.get(j) == null )	// kein ZVUntertitel
					continue;
				// Wenn es abgeschlossene Bestellungen oder Buchungen gibt
				if( (db.countBestellungen( (ZVUntertitel)zvUntertitel.get(j) ) > 0) ||
												(db.countBuchungen( (ZVUntertitel)zvUntertitel.get(j) ) > 0) ) {
					db.selectForUpdateZVUntertitel( (ZVUntertitel)zvUntertitel.get(j) );
					((ZVUntertitel)zvUntertitel.get(j)).setGeloescht( true );
					db.updateZVUntertitel( (ZVUntertitel)zvUntertitel.get(j) );
					delZVTitel = false;
				} else {
					db.deleteZVUntertitel( (ZVUntertitel)zvUntertitel.get(j) );
				}
			}	// Ende for zvUntertitel
		}	// Ende if( zvUntertitel != null )
		
		// Und zum Schluss den ZVTitel löschen
		if( (db.countBestellungen( zvTitel ) > 0) || (db.countBuchungen( zvTitel ) > 0) || !delZVTitel ) {
			db.selectForUpdateZVTitel( zvTitel );
			zvTitel.setGeloescht( true );
			return db.updateZVTitel( zvTitel );
		}
		
		if( db.deleteZVTitel( zvTitel ) > 0 )
			return zvTitel.getId();
		else
			return 0;
	}

	/**
	 * Einen ZVUntertitel in der Datenbank löschen.
	 * @return id vom gelöschtem ZVUntertitel
	 * @throws ApplicationServerException
	 */
	public int delZVUntertitel( ZVUntertitel zvUntertitel ) throws ApplicationServerException {
		if( zvUntertitel == null )	// Kein ZVTitel
			return 0;
		
		if( db.countActiveBestellungen( zvUntertitel ) > 0 )		// Wenn es Kontenzuordnungen gibt
			throw new ApplicationServerException(21);

		// Wenn es abgeschlossene Bestellungen oder Buchungen gibt
		if( (db.countBestellungen( zvUntertitel ) > 0) || (db.countBuchungen( zvUntertitel ) > 0) ) {
			db.selectForUpdateZVUntertitel( zvUntertitel );
			zvUntertitel.setGeloescht( true );
			return db.updateZVUntertitel( zvUntertitel );
		}
		
		if( db.deleteZVUntertitel( zvUntertitel ) > 0 )
			return zvUntertitel.getId();
		else
			return 0;
	}

	/**
	 * Ein ZVKonto in der Datenbank aktualisieren. Es werden auch alle ZVTitel und ZVUntertitel aktualisiert,
	 * wenn die Änderung diese betreffen.
	 * @return id des ZVKontos
	 * @throws ApplicationServerException
	 */
	public int setZVKonto( ZVKonto zvKonto ) throws ApplicationServerException {
		if( zvKonto == null )	// Wenn kein ZVKonto angegeben
			return 0;
		ZVKonto old = db.selectForUpdateZVKonto( zvKonto );	// Das ZVKonto in der Datenbank zum Aktualisieren auswählen
		if( old == null )		// Wenn kein ZVKonto in der Datenbank gefunden
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
				if( db.countActiveBestellungen( (ZVTitel)zvTitel.get(i) ) > 0 )		// Wenn es noch laufende Bestellungen gibt
					throw new ApplicationServerException(20);

				ArrayList zvUntertitel = ((ZVTitel)zvTitel.get(i)).getSubUntertitel();
				// Nachsehen ob es beim Ändern von ZVUntertiteln Fehler entstehen
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
		
		// Alle ZVTitel und die dazugehörigen ZVUntertitel ändern
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
	}

	/**
	 * Einen ZVTitel in der Datenbank aktualisieren. Es werden auch die ZVUntertitel aktualisiert,
	 * wenn die Änderungen diese betreffen.
	 * @return id des ZVTitels
	 * @throws ApplicationServerException
	 */
	public int setZVTitel( ZVTitel zvTitel ) throws ApplicationServerException {
		if( zvTitel == null )	// Kein ZVTitel angegeben
			return 0;
		ZVTitel old = db.selectForUpdateZVTitel( zvTitel );	// Zum Aktualisieren auswählen
		if( old == null )		// kein ZVTitel vorhanden
			throw new ApplicationServerException( 12 );
		if( old.getGeloescht() )	// Wenn der Titel schon gelöscht ist
			throw new ApplicationServerException( 30 );
		
		if( old.equals( zvTitel ) )	// Wenn die beiden Titeln gleich sind
			return db.updateZVTitel( zvTitel );
		
		if( db.existsZVTitel( zvTitel ) > 0 )	// Wenn der ZVTitel bereits existiert
			throw new ApplicationServerException( 13 );
		if( db.countActiveBestellungen( zvTitel ) > 0 )	// Es gibt noch laufende Bestellungen
			throw new ApplicationServerException( 20 );
			
		ArrayList zvUntertitel = zvTitel.getSubUntertitel();
		// Nachsehen ob es beim Ändern von ZVUntertiteln Fehler entstehen
		if( zvUntertitel != null ) {	// Gibt es ZVUntertitel
			for( int j = 0; j < zvUntertitel.size(); j++ ) {
				if( zvUntertitel.get(j) == null )	// kein ZVUntertitel
					continue;
				// Wenn es noch laufende Bestellungen gibt
				if( db.countActiveBestellungen( (ZVUntertitel)zvUntertitel.get(j) ) > 0 )
					throw new ApplicationServerException(20);
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
	}

	/**
	 * Einen ZVUntertitel in der Datenbank aktualisieren.
	 * @return id des aktualisierten ZVUntertitels
	 * @throws ApplicationServerException
	 */
	public int setZVUntertitel( ZVUntertitel zvUntertitel ) throws ApplicationServerException {
		if( zvUntertitel == null )	// Kein ZVUntertitel angegeben
			return 0;
		ZVUntertitel old = db.selectForUpdateZVUntertitel( zvUntertitel );	// Den ZVUntertitel zum aktualisieren auswählen
		
		if( old == null )	// Kein ZVUntertitel gefunden
			throw new ApplicationServerException( 14 );
		if( old.getGeloescht() )	// Der ZVUntertitel ist bereits gelöscht
			throw new ApplicationServerException( 31 );
		
		if( old.equals( zvUntertitel ) )	// Sind die beiden ZVUntertitel gleich 
			return db.updateZVUntertitel( zvUntertitel );

		if( db.existsZVUntertitel( zvUntertitel ) > 0 )	// Wenn der neue ZVUntertitel bereits existiert
			throw new ApplicationServerException( 15 );
		
		if( db.countActiveBestellungen( zvUntertitel ) > 0 )	// Wenn es noch laufende Bestellungen gibt
			throw new ApplicationServerException( 20 );
		
		return db.updateZVUntertitel( zvUntertitel );
	}
	
	/**
	 * Budget auf ein ZVKonto buchen.
	 * @param ZVKonto, Betrag
	 */
	public void buche( ZVKonto konto, float betrag ) throws ApplicationServerException {
		if( konto == null )
			return;
		ZVKonto old = db.selectForUpdateZVKonto( konto );	// Das ZVKonto zum Aktualisieren auswählen
		if( old == null )		// Wenn kein ZVKonto in der Datenbank gefunden
			throw new ApplicationServerException( 10 );
		if( old.getGeloescht() )	// Wenn das ZVKonto gelöscht ist
			throw new ApplicationServerException( 29 );
		
		if( !old.equals( konto ) )	// Wenn die ZVKontos nicht gleich sind, dann wurde das Konto verändert
			throw new ApplicationServerException( 35 );
		konto.setTgrBudget( konto.getTgrBudget() + betrag );
		db.updateZVKonto( konto );
	}
	
	/**
	 * Budget auf einen ZVTitel buchen.
	 * @param ZVTitel, Betrag
	 */
	public void buche( ZVTitel konto, float betrag ) throws ApplicationServerException {
		if( konto == null )
			return;
		ZVTitel old = db.selectForUpdateZVTitel( konto );	// Den ZVTitel zu aktualisieren auswählen
		if( old == null )		// Wenn kein ZVTitel in der Datenbank gefunden
			throw new ApplicationServerException( 12 );
		if( old.getGeloescht() )	// Wenn der ZVTitel gelöscht ist
			throw new ApplicationServerException( 30 );
		
		if( !old.equals( konto ) )	// Wenn die ZVTitel nicht gleich sind, dann wurde das Konto verändert
			throw new ApplicationServerException( 36 );
		konto.setBudget( konto.getBudget() + betrag );
		db.updateZVTitel( konto );
	}
	
	/**
	 * Budget auf einen ZVUntertitel buchen.
	 * @param ZVTitel, Betrag
	 */
	public void buche( ZVUntertitel konto, float betrag ) throws ApplicationServerException {
		if( konto == null )
			return;
		ZVUntertitel old = db.selectForUpdateZVUntertitel( konto );	// Den ZVUntertitel zu aktualisieren auswählen
		if( old == null )		// Wenn kein ZVUntertitel in der Datenbank gefunden
			throw new ApplicationServerException( 14 );
		if( old.getGeloescht() )	// Wenn der ZVUntertitel gelöscht ist
			throw new ApplicationServerException( 31 );
		
		if( !old.equals( konto ) )	// Wenn die ZVUntertitel nicht gleich sind, dann wurde das Konto verändert
			throw new ApplicationServerException( 37 );
		konto.setBudget( konto.getBudget() + betrag );
		db.updateZVUntertitel( konto );
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
	 */
	public ArrayList getFirmen() throws ApplicationServerException {
		return db.selectFirmen();
	}
	
	/**
	 * Eine neue Firma erstellen.
	 * @return id der erstellten Firma.
	 * @throws ApplicationServerException
	 */
	public int addFirma( Firma firma ) throws ApplicationServerException {
		if( firma == null )		// Wurde eine Firma angegeben
			return 0;
		if( db.existsFirma( firma ) > 0 )				// Wenn diese Firma bereits existiert 
			throw new ApplicationServerException( 38 );	// dann kann man es nicht mehr erstellen
		
		int id = 0;		// id der Firma
		if( (id = db.existsDelFirma( firma )) > 0 ) {	// Es gibt eine gelöschte Firma
			firma.setId( id );		// Id der gelöschten Firma ist jetzt id der neuen Firma
			db.selectForUpdateFirma( firma );	// Zum Aktualisieren auswählen
			return db.updateFirma( firma );		// Aktualisieren
		}
		
		return db.insertFirma( firma );			// Sonst neu erstellen
	}
	
	/**
	 * Eine Firma in der Datenbank aktualisieren.
	 * @return id der aktualisierten Firma.
	 * @throws ApplicationServerException
	 */
	public int setFirma( Firma firma ) throws ApplicationServerException {
		if( firma == null )		// Wurde eine Firma angegeben
			return 0;
		if( db.existsDelFirma( firma ) > 0 )			// Wenn diese Firma bereits gelöscht ist
			throw new ApplicationServerException( 39 );	// dann kann man es nicht aktualisieren
				
		db.selectForUpdateFirma( firma );		// Firma zu aktualisieren auswählen
		
		return db.updateFirma( firma );
	}


	/**
	 * Eine Firma in der Datenbank löschen.
	 * @return id der gelöschten Firma.
	 * @throws ApplicationServerException
	 */
	public int delFirma( Firma firma ) throws ApplicationServerException {
		if( firma == null )					// Wurde eine Firma angegeben
			return 0;
		if( db.countActiveBestellungen( firma ) > 0 )	// Gibt es aktive Bestellungen, die an die angegebene Firma gehen
			throw new ApplicationServerException( 20 );	// dann kann man es nicht löschen
		
		if( db.countInactiveBestellungen( firma ) > 0 ) {	// Wenn es inaktive Bestellungen gibt, dann braucht man die Firma
			firma.setGeloescht( true );						// für Reports und es wird nur der Flag gesetzt
			db.selectForUpdateFirma( firma );				// Firma zum aktualisieren auswählen
			return db.updateFirma( firma );					// Firma logisch löschen
		}
		
		return db.deleteFirma( firma );			// Sonst wird physisch gelöscht
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

}