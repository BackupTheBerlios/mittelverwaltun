package applicationServer;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;

import dbObjects.*;


public class Database implements Serializable {

	private String driver = null;
	private String url = null;
	private String database = null;
	private String defaultPwd = null;
	private Connection con = null;
	private PreparedSqlStatements statements = null;

	public Database (String driver, String url, String database, String defaultPwd){
		this.driver = driver;
		this.url = url;
		this.database = database;
		this.defaultPwd = defaultPwd;
		this.con = null;
		this.statements = null;
	}

	/**
	 * stellt eine Verbindung zur Datenbank her mit dem benutzernamen
	 * @param user - benutzername
	 * @throws ConnectionException
	 * author Mario
	 */
	public void connect (String user) throws ConnectionException{
		try{
			System.out.println("Load database driver...");
			Class.forName(driver);
			System.out.println("Done.");
			System.out.println("Connect to database...");
			con = DriverManager.getConnection("jdbc:" + url + "/" + database, user, defaultPwd);
			System.out.println("Connection established.");
			System.out.println("Set autocommit = false...");
			con.setAutoCommit(false);
			System.out.println("Done.");
//			System.out.println("Set transaction_isolation_level = repeatable_read...");
//			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			System.out.println("Set transaction_isolation_level = read_committed...");
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			System.out.println("Done.");
			System.out.println("Prepare SQL-Statements...");
			statements = new PreparedSqlStatements(con);	//
			System.out.println("Done.");
		}catch (ClassNotFoundException e){
			throw new ConnectionException("Connection Exception: Invalid database driver." + e.getMessage());
		}catch (SQLException e){
			e.printStackTrace();
			throw new ConnectionException("Connection Exception: Connection refused.");
		
		} 
	}

	/**
	 * unterbricht die Verbindung mit der Datenbank
	 * @throws ConnectionException
	 * author Mario
	 */
	public void disconnect() throws ConnectionException{
		try {
			System.out.println("Release SQL-Statements...");
			statements.release();
//			releaseStatements();
			System.out.println("Done.");
			System.out.println("Disconnect database...");
			con.close();
			System.out.println("Done.");
		} catch (SQLException e) {
			throw new ConnectionException(e.getMessage());

		}
	}

	/**
	 * Abfrage aller (nicht abgeschlossenen) FBHauptkonten eines Haushaltsjahres
	 * @return Array mit den ermittelten Hauptkonten (inkl. Unterkonten und Institut)
	 * @throws ApplicationServerException
	 * author m.schmitt
	 */
	public ArrayList selectOffeneFBHauptkonten( int haushaltsjahr ) throws ApplicationServerException {
		ArrayList konten = new ArrayList();	// Liste mit den Hauptkonten

		try{
			Object[] parameters = { new Integer(haushaltsjahr) };	// Parameter setzen
			ResultSet rs = statements.get(61).executeQuery(parameters);	// SQL-Statement mit der Nummer 50 ausf�hren
			rs.last();		// auf die letzte abgefragte Zeile springen

			if ( rs.getRow() > 0 ) {	// Wenn die Zeilen anzahl gr��er als 0	
				rs.beforeFirst();		// Vor die erste Zeile springen

				while( rs.next() ){		// Wenn es noch Zeilen gibt
					// Institutobjekt erzeugen
											//    ID        Bezeichnung       Kostenstelle
					Institut i = new Institut(rs.getInt(3), rs.getString(12), rs.getString(13));
					
					// Neues Kontenobjekt erzeugen
													//  kontoId     haushaltsJahr      bezeichnung 
					FBHauptkonto k = new FBHauptkonto( rs.getInt(1), rs.getInt(2), i, rs.getString(4),
							// hauptkonto      unterkonto        budget         dispolimit     vormerkungen
							rs.getString(5), rs.getString(6), rs.getFloat(7), rs.getFloat(8), rs.getFloat(9),
							// pr�fbedingung         kleinbestellungen               gel�scht
							rs.getString(10), rs.getString(11).equalsIgnoreCase("1"), false );
					
					// Alle Unterkonten ermitteln
					k.setUnterkonten(selectFBUnterkonten(i, k));
					
					// Alle Kontenzuordnungen ermitteln
					k.setZuordnung(selectAllKontenzuordnungen(k));
					
					// Konto zur Ergebnisliste hinzuf�gen
					konten.add( k );
				}
			}

			rs.close();					// Die Abfrage schlie�en
		} catch(SQLException e) {
			//System.out.println(e.getMessage());
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return konten;		// Ermittelten Hauptkonten
	}
	
	
	/**
	 * Abfrage aller FBHauptkonten in der Datenbank, die zum angegebenem Institut geh�ren <br>
	 * und nicht gel�scht sind.
	 * @return Array mit den ermittelten Hauptkonten
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public ArrayList selectFBHauptkonten( Institut institut ) throws ApplicationServerException {
		ArrayList konten = new ArrayList();	// Liste mit den Hauptkonten

		try{
			Object[] parameters = { new Integer(institut.getId()) };	// Parameter setzen
			ResultSet rs = statements.get(50).executeQuery(parameters);	// SQL-Statement mit der Nummer 50 ausf�hren
			rs.last();		// auf die letzte abgefragte Zeile springen

			if ( rs.getRow() > 0 ) {	// Wenn die Zeilen anzahl gr��er als 0	
				rs.beforeFirst();		// Vor die erste Zeile springen

				while( rs.next() ){		// Wenn es noch Zeilen gibt
					// Neues FBHauptkonto erzeugen und die Liste einf�gen
												// kontoId, hausHaltsJahreId, institut, bezeichnung, 
					konten.add( new FBHauptkonto( rs.getInt(1), rs.getInt(2), institut, rs.getString(4),
												// hauptkonto,  unterkonto, budget, dispolimit, vormerkungen
												rs.getString(5), rs.getString(6), rs.getFloat(7), rs.getFloat(8), rs.getFloat(9),
												// pr�fbedingung, kleinbestellungen, gel�scht
												rs.getString(10), rs.getString(11).equalsIgnoreCase("1"), false ) );
				}
			}

			rs.close();					// Die Abfrage schlie�en
		} catch(SQLException e) {
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return konten;		// Ermittelten Hauptkonten
	}

	/**
	 * Abfrage aller FBUnterkonten in der Datenbank, die zum angegebenem Institut und Hauptkonto geh�ren <br>
	 * und nicht gel�scht sind.
	 * @param Institut
	 * @param FBHauptkonto
	 * @return FBUnterkonten
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public ArrayList selectFBUnterkonten( Institut institut, FBHauptkonto hauptkonto ) throws ApplicationServerException {
		ArrayList konten = new ArrayList();		// FBUnterkonten

		try{
			// Parameter an das SQL-Statement
			Object[] parameters = { new Integer(institut.getId()), new Integer(hauptkonto.getId()) };
			// SQL-Statement mit der Nummer 51 ausf�hren 
			ResultSet rs = statements.get(51).executeQuery(parameters);
			rs.last();		// An letzte Zeile springen

			if(rs.getRow() > 0) {		// Wenn die Zeile gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen

				while( rs.next() ){		// Wenn es die n�chste Zeile gibt
					// Neues FBUnterkonto erzeugen und in die Liste einf�gen
												// kontoId, hausHaltsJahrId, institut, bezeichnung
					konten.add( new FBUnterkonto( rs.getInt(1), rs.getInt(2), institut, rs.getString(4),
												// hauptkonto, unterkonto, budget, vormerkungen
												rs.getString(5), rs.getString(6), rs.getFloat(7), rs.getFloat(8),
												// kleinbestellungen, gel�scht
												rs.getString(9).equalsIgnoreCase("1"), false ) );
				}
			}

			rs.close();					// Die Abfrage schlie�en
		} catch (SQLException e){
			
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return konten;		// Ermittelten FBUnterkonten
	}
	
	/**
	 * Abfrage der FBUnterkonten, die ein Benutzer f�r seine Kleinbestellung in einem FBHauptkonto verwenden kann.
	 * @param user = Benutzer, f�r den die Konten ermittelt werden sollen.
	 * @param hauptkonto = FBHauptkonto, von dem die FBUnterkonten ermittelt werden m�ssen.
	 * @return Liste mit den ermittelten FBUnterkonten. 
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public ArrayList selectFBUnterkontenForUser(Benutzer user, FBHauptkonto hauptkonto) throws ApplicationServerException {
		ArrayList konten = new ArrayList();
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = { new Integer(hauptkonto.getInstitut().getId()), hauptkonto.getHauptkonto(), 
									hauptkonto.getHauptkonto(), new Integer(user.getId()) };
			// SQL-Statement mit der Nummer 56 ausf�hren 
			ResultSet rs = statements.get(56).executeQuery(parameters);
			rs.last();		// An letzte Zeile springen
			if(rs.getRow() > 0) {		// Wenn die Zeile gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				while( rs.next() ){		// Wenn es die n�chste Zeile gibt
					// Neues FBUnterkonto erzeugen und in die Liste einf�gen
												// kontoId, hausHaltsJahrId, institut, bezeichnung
					konten.add( new FBUnterkonto( rs.getInt(1), rs.getInt(2), user.getKostenstelle(), rs.getString(4),
												// hauptkonto, unterkonto, budget, vormerkungen
												rs.getString(5), rs.getString(6), rs.getFloat(7), rs.getFloat(9),
												// kleinbestellungen, 
												rs.getString(11).equalsIgnoreCase("1"),
												// gel�scht
												rs.getString(12).equalsIgnoreCase("1") ) );
				}
			}
			rs.close();					// Die Abfrage schlie�en
		} catch (SQLException e) {
			throw new ApplicationServerException( 0, e.getMessage() );
		}

		return konten;		// Ermittelten Konten zur�ckgeben
	}

	/**
	 * Ein neues FBHauptkonto erstellen.
	 * @param FBHauptkonto
	 * @return kontoId, des eingef�gten FBHauptkontos
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int insertFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = {new Integer(konto.getHaushaltsJahrID()), new Integer(konto.getInstitut().getId()),
									konto.getBezeichnung(), konto.getHauptkonto(), konto.getUnterkonto(),
									new Float(konto.getBudget()), new Float(konto.getDispoLimit()),
									new Float(konto.getVormerkungen()), konto.getPruefung(),
									(konto.getKleinbestellungen() ? "1" : "0"), (konto.getGeloescht() ? "1" : "0")};
			// SQL-Statement ausf�hren
			statements.get(52).executeUpdate(parameters);
			
			return existsFBKonto( konto );		// kontoId ermitteln
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	/**
	 * Ein neues FBUnterkonto erstellen.
	 * @param FBUnterkonto, welches erstellt werden soll
	 * @return KontoId, des erstellten FBUnterkontos
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	 public int insertFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {
		 try{
		 	// Parameter, welche an das SQL-Statement �bergeben werden 
			Object[] parameters = {new Integer(konto.getHaushaltsJahrID()), new Integer(konto.getInstitut().getId()),
									konto.getBezeichnung(), konto.getHauptkonto(), konto.getUnterkonto(),
									new Float(konto.getBudget()), new Float(0),
									new Float(konto.getVormerkungen()), "",
									(konto.getKleinbestellungen() ? "1" : "0"), (konto.getGeloescht() ? "1" : "0")};
			// SQL-Statement ausf�hren
		 	statements.get(52).executeUpdate(parameters);
			
			return existsFBKonto( konto );		// kontoId ermitteln
		 } catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		 }
	 }
	 
	/**
	 * Abfrage ob ein FBKonto schon existiert. Dabei ist die InstitutsId, <br>
	 * das Hauptkonto und das Unterkonto entscheidend.
	 * @param FBkonto
	 * @return kontoId des FBKontos
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int existsFBKonto(FBUnterkonto konto) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement festlegen
			Object[] parameters = {new Integer(konto.getInstitut().getId()), konto.getHauptkonto(), konto.getUnterkonto()};
			// SQL-Statement mit der Nummer 53 ausf�hren
			ResultSet rs = statements.get(53).executeQuery(parameters);
			rs.last();	// Auf die letzte Zeile springen
			if( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0, dann existiert das Konto
				rs.beforeFirst();	// Vor die erste Zeile springen
				rs.next();			// N�chste Zeile
				return rs.getInt(1);// Id des FBKontos
			}
		} catch (SQLException e) {
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;		// Sonst ist die Id 0 -> nicht gefunden
	}

	/**
	 * Abfrage ob ein FBKonto schon existiert. Dabei ist die InstitutsId, <br>
	 * das Hauptkonto und das Unterkonto entscheidend.
	 * @param InstitutsId
	 * @param Hauptkonto
	 * @param Unterkonto
	 * @return kontoId des FBKontos
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int existsFBKonto(int institutsId, String hauptkonto, String unterkonto ) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement festlegen
			Object[] parameters = {new Integer(institutsId), hauptkonto, unterkonto};
			// SQL-Statement mit der Nummer 53 ausf�hren
			ResultSet rs = statements.get(53).executeQuery(parameters);
			rs.last();	// Auf die letzte Zeile springen
			if( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0, dann existiert das Konto
				rs.beforeFirst();	// Vor die erste Zeile springen
				rs.next();			// N�chste Zeile
				return rs.getInt(1);// Id des FBKontos
			}
		} catch (SQLException e) {
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;		// Sonst ist die Id 0 -> nicht gefunden
	}
	
	/**
	 * Abfrage ob ein gel�schtes FBKonto schon existiert. <br>
	 * Dabei ist die InstitutsId, das Hauptkonto und das Unterkonto entscheidend.
	 * @param FBkonto
	 * @return kontoId vom gel�schten FBKonto
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int existsDeleteFBKonto(FBUnterkonto konto) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement festlegen
			Object[] parameters = {new Integer(konto.getInstitut().getId()), konto.getHauptkonto(), konto.getUnterkonto()};
			// SQL-Statement mit der Nummer 59 ausf�hren
			ResultSet rs = statements.get(59).executeQuery(parameters);
			rs.last();	// Auf die letzte Zeile springen
			if( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0, dann existiert das Konto
				rs.beforeFirst();	// Vor die erste Zeile springen
				rs.next();			// N�chste Zeile
				return rs.getInt(1);// Id des FBKontos
			}
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;		// Sonst ist die Id 0 -> nicht gefunden
	}
		

	/**
	 * Ein FBKonto in der Datenbank l�schen. Es wird kein Unterschied zwischen Haupt- und Unterkonto gemacht.
	 * @param FBKonto, das gel�scht werden soll
	 * @return Zeilennummer, des gel�schten FBKontos
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int deleteFBKonto( FBUnterkonto konto ) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters ={ new Integer(konto.getId())};
			// SQL-Statement ausf�hren
			return statements.get(54).executeUpdate(parameters);	// Zeilennummer des gel�schten Kontos
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	
	/**
	 * Ein FBHauptkonto zum Aktualisieren ausw�hlen <br>
	 * @param FBKonto
	 * @return FBHauptkonto, welches aktualisiert werden soll
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public FBHauptkonto selectForUpdateFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {
		FBHauptkonto result = null;		// FBHauptkonto, welches aktualisiert werden soll
		
		try{
			// Parameter f�r das SQL-Statement festlegen
			Object[] parameters = {new Integer(konto.getId())};
			// SQL-Statement mit der Nummer 58 ausf�hren
			ResultSet rs = statements.get(58).executeQuery(parameters);
			rs.last();	// Auf die letzte Zeile springen
			if( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0, dann existiert das Konto
				rs.beforeFirst();	// Vor die erste Zeile springen
				rs.next();			// N�chste Zeile
										// kontoId, hausHaltsJahrId, instiut, bezeichnung, 
				result = new FBHauptkonto( konto.getId(), rs.getInt(1), konto.getInstitut(), rs.getString(3), 
											// hauptkonto, unterkonto, budget
											rs.getString(4), rs.getString(5), rs.getFloat(6),
											// dispolimit, vormerkungen, pruefbedingung
											rs.getFloat(7), rs.getFloat(8), rs.getString(9),
											// kleinbestellungen, gel�scht
									 		rs.getString(10).equalsIgnoreCase( "1" ), rs.getString(11).equalsIgnoreCase( "1" ) );
			}
			rs.close();		// Abfrage schlie�en
		} catch ( SQLException e ) {
			throw new ApplicationServerException( 0, e.getMessage() );
		}
		
		return result;		// FBHauptkonto zur�ckgeben
	}
	
	/**
	 * Ein FBUnterkonto zum aktualisieren ausw�hlen
	 * @param FBUnterkonto
	 * @return FBUnterkonto, welches ausgew�hlt wurde
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public FBUnterkonto selectForUpdateFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {
		FBUnterkonto result = null;		// Das FBUnterkonto, welches aktualisiert werden soll
		
		try{
			// Parameter f�r das SQL-Statement festlegen
			Object[] parameters = {new Integer(konto.getId())};
			// SQL-Statement mit der Nummer 58 ausf�hren
			ResultSet rs = statements.get(58).executeQuery(parameters);
			rs.last();	// Auf die letzte Zeile springen
			if( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0, dann existiert das Konto
				rs.beforeFirst();	// Vor die erste Zeile springen
				rs.next();			// N�chste Zeile
										// kontoId, hausHaltsJahrId, instiut, bezeichnung, 
				result = new FBUnterkonto( konto.getId(), rs.getInt(1), konto.getInstitut(), rs.getString(3), 
										// hauptkonto, unterkonto, budget, vormerkungen
										rs.getString(4), rs.getString(5), rs.getFloat(6), rs.getFloat(8), 
										// kleinbestellungen, gel�scht
										rs.getString(10).equalsIgnoreCase( "1" ), rs.getString(11).equalsIgnoreCase( "1" ) );
			}
			rs.close();		// Abfrage schlie�en
		} catch ( SQLException e ){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
		
		return result;		// FBUnterkonto zur�ckgeben
	}
	
	/**
	 * Anzahl der Bestellungen(aktiv und abgeschlossen) ermitteln, <br>
	 * bei denen ein bestimmtes FBKonto angegeben wurde.
	 * @param FBKonto, welches �berpr�ft werden soll
	 * @return Anzahl der Bestellungen bei denen, das FBKonto angegeben ist
	 * author w.flat
	 */
	public int countBestellungen( FBUnterkonto konto )throws ApplicationServerException{
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement ausf�hren
			ResultSet rs = statements.get(212).executeQuery(parameters);
			if(rs.next())	// Gibt es ein Ergebnis
				return rs.getInt(1);	// Anzahl der Bestellungen zur�ckgeben
			else
				return 0;				// Sonst ist die Anzahl = 0
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
	}
	
	/**
	 * Anzahl der aktiven Bestellungen ermitteln, bei denen ein bestimmtes FBKonto angegeben wurde.
	 * @param FBKonto, welches �berpr�ft werden soll
	 * @return Anzahl der nicht abgeschlossenen Bestellungen
	 * author w.flat
	 */
	public int countActiveBestellungen( FBUnterkonto konto )throws ApplicationServerException{
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement ausf�hren
			ResultSet rs = statements.get(213).executeQuery(parameters);
			if(rs.next())	// Wenn es Result gibt
				return rs.getInt(1);	// R�ckgabe Anzahl der Bestellungen
			else
				return 0;				// Sonst wird 0 zur�ckgegeben
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Anzahl der aktiven Benutzer ermitteln, denen ein bestimmtes FBKonto zugeordnet ist.
	 * @param FBKonto, f�r das die �berpr�fung durch genommen wird.
	 * @return Anzahl der Benutzer, die dieses Konto besitzen
	 * author w.flat
	 */
	public int countActiveBenutzer( FBUnterkonto konto )throws ApplicationServerException{
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement ausf�hren
			ResultSet rs = statements.get(31).executeQuery(parameters);
			if(rs.next())	// Gibt es ein Ergebnis
				return rs.getInt(1);	// Dann die Anzahl zur�ckgeben
			else
				return 0;				// Sonst ist die Anzahl = 0
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}

	/**
	 * Anzahl der Benutzer(aktiv und gel�scht) ermitteln, denen ein bestimmtes FBKonto zugeordnet ist.
	 * @param FBKonto, f�r das die �berpr�fung durch genommen wird.
	 * @return Anzahl der Benutzer, die dieses Konto besitzen
	 * author w.flat
	 */
	public int countBenutzer( FBUnterkonto konto )throws ApplicationServerException{
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement ausf�hren
			ResultSet rs = statements.get(32).executeQuery(parameters);
			if(rs.next())	// Gibt es ein Ergebnis
				return rs.getInt(1);	// Dann die Anzahl zur�ckgeben
			else
				return 0;				// Sonst ist die Anzahl = 0
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Anzahl der Buchungen ermitteln, bei denen ein bestimmtes FBKonto benutzt wurde.
	 * @param FBKonto, welches �berpr�ft werden soll
	 * @return Anzahl der Buchingen, bei denen das FBKonto benutzt wurde
	 * author w.flat 
	 */
	public int countBuchungen( FBUnterkonto konto )throws ApplicationServerException{
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement ausf�hren
			ResultSet rs = statements.get(221).executeQuery(parameters);
			if(rs.next())	// Gibt es ein Ergebnis
				return rs.getInt(1);	// Anzahl der Buchungen zur�ckgeben
			else	
				return 0;				// Sonst ist die Anzahl = 0
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Anzahl der Kontenzuordnungen ermitteln, bei denen ein bestimmtes FBKonto angegeben wurde.
	 * @param FBKonto welches �berpr�ft werden soll.
	 * @return Anzahl der Kontenzuordnungen, bei denen das FBKonto angegeben ist.
	 * author w.flat
	 */
	public int countKontenzuordnungen( FBUnterkonto konto )throws ApplicationServerException{
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement ausf�hren
			ResultSet rs = statements.get(236).executeQuery(parameters);
			if(rs.next())		// Wenn es ein Ergebnis gibt
				return rs.getInt(1);	// Anzahl der Kontenzuordnungen zur�ckgeben
			else
				return 0;				// Sonst ist die Anzahl = 0
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Ein FBHauptkonto in der Datenbank aktualisieren.
	 * @param FBHauptkonto
	 * @return kontoId vom Hauptkonto, das aktulisiert wurde
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int updateFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = { new Integer(konto.getHaushaltsJahrID()), new Integer(konto.getInstitut().getId()),
									konto.getBezeichnung(), konto.getHauptkonto(), konto.getUnterkonto(),
									new Float(konto.getBudget()), new Float(konto.getDispoLimit()),
									new Float(konto.getVormerkungen()), konto.getPruefung(),
									(konto.getKleinbestellungen() ? "1" : "0"), 
									(konto.getGeloescht() ? "1" : "0"), new Integer(konto.getId()) };
			// Aktualisierung durchf�hren
			statements.get(55).executeUpdate(parameters);
			// Ermittlung der kontoId
			if( konto.getGeloescht() )	// Wenn das gel�scht wurde
				return existsDeleteFBKonto( konto );
			else						// Sonst bei nicht gel�schten nachsehen 
				return existsFBKonto( konto );
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Ein FBUnterkonto in der Datenbank aktualisieren.
	 * @param FBUnterkonto
	 * @return kontoId vom Unterkonto, das aktulisiert wurde
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int updateFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = { new Integer(konto.getHaushaltsJahrID()), new Integer(konto.getInstitut().getId()),
									konto.getBezeichnung(), konto.getHauptkonto(), konto.getUnterkonto(),
									new Float(konto.getBudget()), new Float(0),
									new Float(konto.getVormerkungen()), new String(""),
									(konto.getKleinbestellungen() ? "1" : "0"), 
									(konto.getGeloescht() ? "1" : "0"), new Integer(konto.getId()) };
			// Aktualisierung durchf�hren
			statements.get(55).executeUpdate(parameters);
			// Ermittlung der kontoId
			if( konto.getGeloescht() )	// Wenn das gel�scht wurde
				return existsDeleteFBKonto( konto );
			else						// Sonst bei nicht gel�schten nachsehen 
				return existsFBKonto( konto );
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	/**
	 * gibt einen Benutzer mit dem �bergebenen Benutzernamen und Passwort zur�ck
	 * @param user - Benutzername in der Datenbank
	 * @param password
	 * @return Benutzer
	 * @throws ApplicationServerException
	 * author robert
	 */
	public Benutzer selectUser(String user, String password) throws ApplicationServerException{
		Benutzer benutzer = null;

		try{
			Object[] parameters = { user, password };
			ResultSet rs = statements.get(170).executeQuery(parameters);

			if (rs.next()){
				benutzer = new Benutzer(rs.getInt(1), rs.getString(2), rs.getString(3),
										new Rolle(rs.getInt(4), rs.getString(5)), new Institut(rs.getInt(6), rs.getString(7), rs.getString(8)),
										rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getInt(13),
										rs.getString(14), rs.getString(15), rs.getString(16), rs.getString(17),!rs.getString(18).equalsIgnoreCase( "0" ),
										rs.getInt(19));
			}else {
				throw new ApplicationServerException(2);
			}

			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(113,e.getMessage());
		}

		return benutzer;
	}

	/**
	 * Liefert einen Benutzer und sperrt diesen Datensatz f�r ein Update
	 * @param benutzer
	 * @return Benutzer aus der Datenbank f�r den Vergleich ob sich zwischenzeitlich was ge�ndert hat
	 * @throws ApplicationServerException
	 * author robert
	 */
	public Benutzer selectForUpdateUser(Benutzer benutzer) throws ApplicationServerException{
		Benutzer b = null;
		try{
			Object[] parameters = { benutzer.getBenutzername(), benutzer.getPasswort()};

			ResultSet rs = statements.get(172).executeQuery(parameters);
			if (rs.next())
				b = new Benutzer(rs.getInt(1), rs.getString(2), rs.getString(3),
							new Rolle(rs.getInt(4), rs.getString(5)), new Institut(rs.getInt(6), rs.getString(7), rs.getString(8)),
							rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getInt(13),
							rs.getString(14), rs.getString(15), rs.getString(16), rs.getString(17),!rs.getString(18).equalsIgnoreCase( "0" ),
							rs.getInt(19));
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(114, e.getMessage());
		}
		return b;
	}

	/**
	 * gibt alle Benutzer im System zur�ck
	 * @return Benutzer-Array
	 * @throws ApplicationServerException
	 * author robert
	 */
	public Benutzer[] selectUsers() throws ApplicationServerException{
		Benutzer[] benutzer = null;

		try{
			ResultSet rs = statements.get(171).executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			if (count > 0){
				benutzer = new Benutzer[count];
				int i = 0;
				while (rs.next()){
					benutzer[i] = new Benutzer(rs.getInt(1), rs.getString(2), rs.getString(3),
																			new Rolle(rs.getInt(13), rs.getString(14)),
																			new Institut(rs.getInt(10), rs.getString(11), rs.getString(12)),
																			rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
																			rs.getInt(9),rs.getString(15), rs.getString(16), rs.getString(17), 
																			rs.getString(18),!rs.getString(19).equalsIgnoreCase( "0" ),
																			rs.getInt(20));
					i++;
				}
			}
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(115, e.getMessage());
		}

		return benutzer;
	}
	
	/**
	 * gibt alle SwBeauftragte des Fachbereichs zur�ck
	 * @return Benutzer-Array mit allen SwBeauftragten
	 * @throws ApplicationServerException
	 * author robert
	 */
	public Benutzer[] selectSwBeauftragte() throws ApplicationServerException{
		Benutzer[] benutzer = null;

		try{
			ResultSet rs = statements.get(22).executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			if (count > 0){
				benutzer = new Benutzer[count];
				int i = 0;
				while (rs.next()){
					benutzer[i] = new Benutzer( rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4));
					i++;
				}
			}
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(95, e.getMessage());
		}

		return benutzer;
	}

	/**
	 * gibt alle tempor�ren Rollen des Empf�ngers zur�ck
	 * @param empfaenger
	 * @return TmpRolle-Array
	 * @throws ApplicationServerException
	 * author Mario
	 */
	public TmpRolle[] selectTempRollen (int empfaenger) throws ApplicationServerException{

		TmpRolle[] tmpRollen = new TmpRolle[0];

		try{
			Object[] parameters = { new Integer(empfaenger) };
			ResultSet rs = statements.get(190).executeQuery(parameters);

			rs.last();
			int rowCount = rs.getRow();
			rs.beforeFirst();

			if (rowCount > 0){
				tmpRollen = new TmpRolle[rowCount];
				int i=0;
				while (rs.next()){
					tmpRollen[i++] = new TmpRolle( rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDate(4) );
				}
			}

			rs.close();

		} catch (SQLException e){
			throw new ApplicationServerException(1,e.getMessage());
		}

		return tmpRollen;
	}

	/**
	 * gibt alle ID der Aktivit�ten einer Rolle zur�ck
	 * @param rollenId - Id der Rolle
	 * @return int-Array mit allen Id�s der Aktivit�ten
	 * @throws ApplicationServerException
	 * author Mario
	 */
	public int[] selectAktivitaeten(int rollenId) throws ApplicationServerException{

		int[] aktivitaeten = null;
		try{

			Object[] parameters = { new Integer(rollenId) };
			ResultSet rs = statements.get(110).executeQuery(parameters);

			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			if (count > 0){
				aktivitaeten = new int[count];
				int i = 0;
				while (rs.next()){
					aktivitaeten[i] = rs.getInt(1);
					i++;
				}
			}
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(1,e.getMessage());
		}

		return aktivitaeten;
	}

	/**
	 * gibt alle Fachbereiche zur�ck. Im Moment nur den Fachbereich Informatik
	 * @return Fachberich-Array
	 * @throws ApplicationServerException
	 * author robert
	 */
	public Fachbereich[] selectFachbereiche() throws ApplicationServerException{
		Fachbereich[] fachbereiche = null;
		try{
			ResultSet rs = statements.get(180).executeQuery();

			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			if (count > 0){
				fachbereiche = new Fachbereich[count];
				int i = 0;
				while (rs.next()){
					fachbereiche[i] = new Fachbereich(rs.getInt(1), rs.getString(2), rs.getFloat(3), rs.getString(4), rs.getString(5),
																						rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9));
					i++;
				}
			}
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}
		return fachbereiche;
	}

	/**
	 * sperrt den Fachbereich bevor es ge�ndert werden kann
	 * @param fachbereich
	 * @param institutsid
	 * @return Fachbereich
	 * @throws ApplicationServerException
	 */
	public Fachbereich selectForUpdateFachbereich() throws ApplicationServerException{
		Fachbereich f = null;
		try{
			ResultSet rs = statements.get(181).executeQuery();

			if(rs.next())
				f = new Fachbereich(rs.getInt(1), rs.getString(2), rs.getFloat(3), rs.getString(4), rs.getString(5),
														rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9));
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}
		return f;
	}

	/**
	 * aktualisiert den �bergebenen Fachberiech in der Datenbank
	 * @param fachbereich
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void updateFachbereich(Fachbereich fachbereich) throws ApplicationServerException{
		if(fachbereich != null){
			try{

				Object[] parameters = { new Integer(fachbereich.getId()), fachbereich.getFbBezeichnung(), new Float(fachbereich.getProfPauschale()), 
																fachbereich.getStrasseHausNr(), fachbereich.getPlzOrt(), fachbereich.getFhBezeichnung(),
																fachbereich.getFhBeschreibung() };
				if(statements.get(40).executeUpdate(parameters) == 0)
					throw new ApplicationServerException(3);

			} catch (SQLException e){
				throw new ApplicationServerException(116, e.getMessage());
			}
		}
	}

	/**
	 * gibt alle Institute des Fachbereichs zur�ck
	 * @return Institut-Array
	 * @throws ApplicationServerException
	 * author robert
	 */
	public Institut[] selectInstitutes() throws ApplicationServerException{

		Institut[] institutes = null;
		try{
			ResultSet rs = statements.get(80).executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			if (count > 0){
				institutes = new Institut[count];
				int i = 0;
				while (rs.next()){
					institutes[i] = new Institut (rs.getInt(1), rs.getString(2), rs.getString(3),
																				new Benutzer(rs.getInt(4),rs.getString(5),rs.getString(6),rs.getString(7)));
					i++;
				}
			}
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(117, e.getMessage());
		}

		return institutes;
	}
	
	/**
	 * gibt ein Institut mit Institutsleiter anhand der id zur�ck
	 * @param id des Instituts
	 * @return Institut
	 * @throws ApplicationServerException
	 * author robert
	 */
	public Institut selectInstitute(int id) throws ApplicationServerException{
		Institut institut = null;
		
		try{
			Object[] parameters = { new Integer(id) };
			ResultSet rs = statements.get(86).executeQuery(parameters);
			if(rs.next())
				institut = new Institut (rs.getInt(1), rs.getString(2), rs.getString(3),
																	new Benutzer(rs.getInt(4),rs.getString(5),rs.getString(6),rs.getString(7)));
			
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 118, e.getMessage() );
		}

		return institut;		
	}

	/**
	 * l�scht das �bergebene Institut
	 * @param institut
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void deleteInstitute(Institut institut) throws ApplicationServerException{
		try{

			Object[] parameters = { new Integer(institut.getId()), institut.getBezeichnung() };
			statements.get(83).executeUpdate(parameters);


		} catch (SQLException e){
			throw new ApplicationServerException(119, e.getMessage());
		}
	}

	/**
	 * f�gt ein neues Institut in die Datenbank hinzu
	 * @param institut
	 * @return Id - neue Id des Instituts
	 * @throws ApplicationServerException
	 * author robert
	 */
	public int insertInstitute(Institut institut) throws ApplicationServerException{
		if(checkInstitute(institut) == 0){
			try{
				Object[] parameters = { institut.getBezeichnung(), new Integer(institut.getKostenstelle()),
																new Integer(institut.getInstitutsleiter().getId()) };
				statements.get(82).executeUpdate(parameters);
				ResultSet rs = statements.get(82).getGeneratedKeys();

				if (rs.next()) {
					return rs.getInt(1);
				}
			} catch (SQLException e){
				throw new ApplicationServerException(120, e.getMessage());
			}
		}else{
			throw new ApplicationServerException(4);
		}
		return 0;
	}

	/**
	 * Gibt die Benutzer eines Instituts zur�ck
	 * @param institut
	 * @return Benutzer-Array
	 * @throws ApplicationServerException
	 * author robert
	 */
	public Benutzer[] selectUsers(Institut institut) throws ApplicationServerException{
		Benutzer[] benutzer = null;
		try{
			Object[] parameters = { new Integer(institut.getId()) };

			ResultSet rs = statements.get(173).executeQuery(parameters);
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			if (count > 0){
				benutzer = new Benutzer[count];
				int i = 0;
				while (rs.next()){
					benutzer[i] = new Benutzer(rs.getInt(1), rs.getString(2), rs.getString(3),
																			new Rolle(rs.getInt(4), rs.getString(5)), 
																			new Institut(rs.getInt(6), rs.getString(7), rs.getString(8)),
																			rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getInt(13),
																			rs.getString(14), rs.getString(15), rs.getString(16), rs.getString(17),
																			!rs.getString(18).equalsIgnoreCase( "0" ), rs.getInt(19));
					i++;
				}
			}
			rs.close();
			return benutzer;
		} catch (SQLException e){
			throw new ApplicationServerException(121, e.getMessage());
		}
	}
	
	/**
	 * gibt einen Benutzer anhand einer userId zur�ck mit allen Attributen
	 * @param userId
	 * @return Benutzer
	 * @throws ApplicationServerException
	 * author robert
	 */
	public Benutzer selectUser(int userId) throws ApplicationServerException{
		Benutzer benutzer = null;

		try{
			Object[] parameters = { new Integer(userId) };
			ResultSet rs = statements.get(174).executeQuery(parameters);

			if (rs.next()){
				benutzer = new Benutzer(rs.getInt(1), rs.getString(2), rs.getString(3),
										new Rolle(rs.getInt(4), rs.getString(5)), new Institut(rs.getInt(6), rs.getString(7), rs.getString(8)),
										rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getInt(13),
										rs.getString(14), rs.getString(15), rs.getString(16), rs.getString(17),!rs.getString(18).equalsIgnoreCase( "0" ),
										rs.getInt(19));
			}else {
				throw new ApplicationServerException(2);
			}

			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(72,e.getMessage());
		}

		return benutzer;
	}

	/**
	 * gibt ein Institut aus der Datenbank und sperrt diesen Datensatz f�r eine Aktualisierung
	 * @param institut
	 * @return Institut aus der Datenbank f�r den Abgleich
	 * @throws ApplicationServerException
	 * author robert
	 */
	public Institut selectForUpdateInstitute(Institut institut) throws ApplicationServerException{
		Institut inst = null;
		try{
			Object[] parameters = { new Integer(institut.getId()) };

			ResultSet rs = statements.get(85).executeQuery(parameters);
			if (rs.next())
				inst = new Institut (rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4));
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(122, e.getMessage());
		}
		return inst;
	}

	/**
	 * Pr�ft ob die �nderung des Instituts oder das Einf�gen gemacht werden kann.
	 * Es wird die Anzahl der Institute zur�ckgegen, mit der gleichen Bezeichnung und Kostenstelle.
	 * @param institut
	 * @return Anzahl der gefundenen Institute ausser das �bergebene Institut - bei 0 ist das Einf�gen kein Problem
	 * @throws ApplicationServerException
	 * author robert
	 */
	public int checkInstitute(Institut institut) throws ApplicationServerException{
		try{
			Object[] parameters = { new Integer(institut.getId()), institut.getBezeichnung(), new Integer(institut.getKostenstelle()) };
			ResultSet rs = statements.get(84).executeQuery(parameters);
			rs.last();
			return rs.getRow();
		} catch (SQLException e){
			throw new ApplicationServerException(123, e.getMessage());
		}
	}

	/**
	 * aktualisiert ein Institut
	 * @param institut
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void updateInstitute(Institut institut) throws ApplicationServerException{
		try{
			Object[] parameters = {institut.getBezeichnung() ,new Integer(institut.getKostenstelle()), 
														 new Integer(institut.getInstitutsleiter().getId()), new Integer(institut.getId())};
				if(statements.get(81).executeUpdate(parameters) == 0)
					throw new ApplicationServerException(3);
		} catch (SQLException e){
			throw new ApplicationServerException(124, e.getMessage());
		}
	}

	/**
	 * Pr�ft ob es schon einen Benutzer mit dem Benutzernamen in der MySQL-Datenbank gibt
	 * @param benutzer
	 * @return Anzahl der Benutzer mit dem Benutzernamen
	 * @throws ApplicationServerException
	 * author robert
	 */
	 public int checkUserMySQL(Benutzer benutzer) throws ApplicationServerException{
		 try{
			 Object[] parameters = {benutzer.getBenutzername() };
			 ResultSet rs = statements.get(93).executeQuery(parameters);

			 rs.last();

		 	 return rs.getRow();
		 } catch (SQLException e){
			 throw new ApplicationServerException(125, e.getMessage());
		 }
	 }

	/**
	 * Pr�ft ob es schon einen Benutzer mit dem Benutzernamen in der Tabelle User gibt
	 * @param benutzer
	 * @return Anzahl der Benutzer mit dem gleichen Benutzernamen ausser der �bergebene Benutzer
	 * @throws ApplicationServerException
	 * author robert
	 */
	 public int checkUser(Benutzer benutzer) throws ApplicationServerException{
		 try{
			 Object[] parameters = { new Integer(benutzer.getId()), benutzer.getBenutzername() };
			 ResultSet rs = statements.get(27).executeQuery(parameters);

			 rs.last();
		 return rs.getRow();
		 } catch (SQLException e){
			 throw new ApplicationServerException(126, e.getMessage());
		 }
	 }

	/**
	 * aktualisiert den Benutzernamen in der MySQL-Datenbank
	 * @param benutzer - aktualisierte Benutzer
	 * @param oldBenutzer - alte Benutzername
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void updateUserMySQL(Benutzer benutzer, String oldBenutzer) throws ApplicationServerException{
		try{
			Object[] param1 ={benutzer.getBenutzername(), oldBenutzer};
			statements.get(94).executeUpdate(param1);
			statements.get(95).executeUpdate(param1);

		} catch (SQLException e){
			throw new ApplicationServerException(127, e.getMessage());
		}
	}

	/**
	 * aktualisiert den �bergebenen Benutzer in der Tabelle User
	 * @param benutzer
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void updateUser(Benutzer benutzer) throws ApplicationServerException{
		try{
			Object[] param2 = {	benutzer.getBenutzername(), new Integer(benutzer.getRolle().getId()), new Integer(benutzer.getKostenstelle().getId()),
													benutzer.getTitel(), benutzer.getName(),  benutzer.getVorname(), benutzer.getEmail(),
													((benutzer.getPrivatKonto() == 0) ? null : new Integer(benutzer.getPrivatKonto())), benutzer.getTelefon(), benutzer.getFax(), 
													benutzer.getBau(), benutzer.getRaum(), (benutzer.getSwBeauftragter() ? "1" : "0"),
													new Integer(benutzer.getSichtbarkeit()),
													new Integer(benutzer.getId())};
			if(statements.get(21).executeUpdate(param2) == 0)
				throw new ApplicationServerException(2);
			

			if(!benutzer.getPasswort().equals("")){
				Object[] param = {benutzer.getPasswort(), new Integer(benutzer.getId()) };
				statements.get(23).executeUpdate(param);
			}
		} catch (SQLException e){
			throw new ApplicationServerException(128, e.getMessage());
		}
	}

	/**
	 * f�gt einen neuen Benutzer in die MySQL-Datenbank hinzu
	 * @param benutzer
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void insertUserMySQL(Benutzer benutzer) throws ApplicationServerException{
		try{
			Object[] param ={benutzer.getBenutzername()};

			statements.get(90).executeUpdate(param);
			statements.get(91).executeUpdate(param);
			statements.get(92).executeUpdate(param);
			
		} catch (SQLException e){
			throw new ApplicationServerException(100, e.getMessage());
		}
	}

	/**
	 * f�gt einen Benutzer in die Tabelle Benutzer ein
	 * @param benutzer
	 * @return Id in der Tabelle Benutzer des eingef�gten Users 
	 * @throws ApplicationServerException
	 * author robert
	 */
	public int insertUser(Benutzer benutzer) throws ApplicationServerException{
		try{
	    
		  Object[] param3 ={benutzer.getBenutzername(), benutzer.getPasswort(), new Integer(benutzer.getRolle().getId()),
												new Integer(benutzer.getKostenstelle().getId()),  benutzer.getTitel(), benutzer.getName(),
												benutzer.getVorname(), benutzer.getEmail(), ((benutzer.getPrivatKonto() == 0) ? null : new Integer(benutzer.getPrivatKonto())), 
												benutzer.getTelefon(), benutzer.getFax(), benutzer.getBau(), benutzer.getRaum(), 
												(benutzer.getSwBeauftragter() ? "1" : "0"), new Integer(benutzer.getSichtbarkeit())};
			statements.get(24).executeUpdate(param3);

			ResultSet rs = statements.get(24).getGeneratedKeys();
			
	    if (rs.next()) {
	        return rs.getInt(1);
	    }else
	        return 0;
		} catch (SQLException e){
			throw new ApplicationServerException(101, e.getMessage());
		}
	}

	/**
	 * l�scht den �bergebenen Benuter aus der MySQL-Datenbank
	 * @param benutzer
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void deleteUserMySQL(Benutzer benutzer) throws ApplicationServerException{
		try{

			Object[] param ={benutzer.getBenutzername()};
			statements.get(96).executeUpdate(param);
			statements.get(97).executeUpdate(param);
		} catch (SQLException e){
			throw new ApplicationServerException(129, e.getMessage());
		}
	}

	/**
	 * L�scht den Benutzer endg�ltig aus der Tabelle Benutzer
	 * @param benutzer
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void deleteUserFinal(Benutzer benutzer) throws ApplicationServerException{
		try{

			Object[] parameters = {new Integer(benutzer.getId()), benutzer.getBenutzername()};
			statements.get(26).executeUpdate(parameters);

		} catch (SQLException e){
			throw new ApplicationServerException(130, e.getMessage());
		}
	}

	/**
	 * L�scht den Benutzer nicht aus der Tabelle sondern setzt das Flag geloescht auf 1
	 * @param benutzer
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void deleteUser(Benutzer benutzer) throws ApplicationServerException{
		try{

			Object[] parameters = {new Integer(benutzer.getId())};
			statements.get(29).executeUpdate(parameters);

		} catch (SQLException e){
			throw new ApplicationServerException(131, e.getMessage());
		}
	}

	/**
	 * stellt alle �nderungen der aktuellen Session zur�ck
	 * @throws ApplicationServerException
	 */
	public void rollback() throws ApplicationServerException{
		try {
			this.con.rollback();
		} catch (SQLException e) {
			throw new ApplicationServerException(163, e.getMessage());
			
		}
	}
	
	/**
	 * commitet alle �nderungen die in der Session ablaufen
	 * @throws ApplicationServerException
	 */
	public void commit() throws ApplicationServerException{
		try {
			this.con.commit();
		} catch (SQLException e) {
			throw new ApplicationServerException(164, e.getMessage());
			
		}
	}

//	public void setAutoCommit(boolean commit) throws ApplicationServerException{
//		try{
//			if(commit)
//				statements.get(5).executeQuery();
//			else
//				statements.get(3).executeQuery();
//		} catch (SQLException e){
//			System.out.println(e.getMessage());
//			throw new ApplicationServerException(1, e.getMessage());
//		}
//	}

	/**
	 * gibt das aktuelle Haushaltsjahr zur�ck
	 * @throws ApplicationServerException
	 */
//	public Haushaltsjahr selectHaushaltsjahr() throws ApplicationServerException{
//		Haushaltsjahr hhj;
//		try{
//			ResultSet rs = statements.get(70).executeQuery();
//			rs.last();
//			int count = rs.getRow();
//			rs.beforeFirst();
//			rs.next();
//			if (count > 0){
//				return new Haushaltsjahr(rs.getString(1), rs.getString(2), rs.getString(3));
//			}else{
//				throw new ApplicationServerException(6);
//			}
//		} catch (SQLException e){
//			throw new ApplicationServerException(132, e.getMessage());
//		}
//	}

	/**
	 * gibt das aktuelle Haushaltsjahr zur�ck, d.h. wenn status = 0
	 * danach wird das Haushaltsjahr vor einer Aktualisierung f�r andere
	 * Benutzer gesperrt (SELECT FOR UPDATE).
	 * @return	Haushaltsjahr
	 * @throws ApplicationServerException
	 */
//	public Haushaltsjahr selectForUpdateHaushaltsjahr() throws ApplicationServerException{
//		Haushaltsjahr hhj = null;
//
//		try{
//			ResultSet rs = statements.get(70).executeQuery();
//			if(rs.next())
//				hhj = new Haushaltsjahr(rs.getString(1), rs.getString(2), rs.getString(3));
//		} catch (SQLException e){
//			throw new ApplicationServerException(133, e.getMessage());
//		}
//		return hhj;
//	}

	/**
	 * aktualisiert das Haushaltsjahr
	 * @param hhj
	 * @throws ApplicationServerException
	 */
//	public void updateHaushaltsjahr(Haushaltsjahr hhj) throws ApplicationServerException{
//		if(hhj != null){
//			try{
//				Object[] parameters = {hhj.getBeschreibung(), hhj.getVon(), hhj.getBis()};
//
//				if(statements.get(71).executeUpdate(parameters) == 0)
//					throw new ApplicationServerException(6);
//
//			} catch (SQLException e){
//				throw new ApplicationServerException(134, e.getMessage());
//			}
//		}
//	}
	
	/**
	 * Abfrage der HaushaltsjahrId vom aktuellem Jahr
	 */
	public int selectHaushaltsjahrId() throws ApplicationServerException{
		
		try{
			ResultSet rs = statements.get(73).executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			rs.next();
			if (count > 0){
				return rs.getInt(1);
			}else{
				throw new ApplicationServerException(6);
			}
		} catch (SQLException e){
			throw new ApplicationServerException(135, e.getMessage());
		}
	}

	/**
	 * Abfrage der ID des folgenden Haushaltsjahres
	 */
	public int selectFollowingHaushaltsjahrId(int preYear) throws ApplicationServerException{
		
		try{
			Object[] parameters = {new Integer(preYear)};
			ResultSet rs = statements.get(78).executeQuery(parameters);
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			rs.next();
			if (count > 0){
				return rs.getInt(1);
			}else{
				return 0;
			}
		} catch (SQLException e){
			throw new ApplicationServerException(191, e.getMessage());
		}
	}
	
	
	/**
	 * gibt alle Rollen zur�ck
	 * @return Rolle-Array
	 * @throws ApplicationServerException
	 */
	public Rolle[] selectRollen() throws ApplicationServerException{
		Rolle[] rollen = null;
		try{
			ResultSet rs = statements.get(100).executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			if (count > 0){
				rollen = new Rolle[count];
				int i = 0;
				while (rs.next()){
					rollen[i] = new Rolle(rs.getInt(1), rs.getString(2));
					i++;
				}
			}
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(136, e.getMessage());
		}

		return rollen;
	}

	/**
	 * gibt eine Rolle f�r den Abgleich zur�ck und sperrt diesen Datensatz
	 * @param rolle
	 * @return Rolle
	 * @throws ApplicationServerException
	 */
	public Rolle selectForUpdateRolle(Rolle rolle) throws ApplicationServerException{
		Rolle r = null;
		try{
			Object[] parameters = {new Integer(rolle.getId())};

			ResultSet rs = statements.get(106).executeQuery(parameters);
			if(rs.next())
				r = new Rolle(rs.getInt(1), rs.getString(2));
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(137, e.getMessage());
		}
		return r;
	}

	/**
	 * gibt alle Aktivit�ten zu einer Rolle zur�ck
	 * @param rollenId - Id der Rolle
	 * @return Aktivitaet-Array
	 * @throws ApplicationServerException
	 * author robert
	 */
	public Aktivitaet[] selectAktivitaetenFull(int rollenId) throws ApplicationServerException{
 		Aktivitaet[] aktivitaeten = null;
	 	try{
		 	Object[] parameters ={new Integer(rollenId)};
		 	ResultSet rs = statements.get(200).executeQuery(parameters);
		 	rs.last();
		 	int count = rs.getRow();
		 	rs.beforeFirst();
		 	if (count > 0){
			 	aktivitaeten = new Aktivitaet[count];
			 	int i = 0;
			 	while (rs.next()){
				 	aktivitaeten[i] = new Aktivitaet(rs.getInt(1), rs.getString(2),  rs.getString(3));
				 	i++;
			 	}
		 	}
		 	rs.close();
	 	} catch (SQLException e){
		 	System.out.println(e.getMessage());
		 	throw new ApplicationServerException(138,e.getMessage());
	 	}

 		return aktivitaeten;
	}

	/**
	 * gibt alle Aktivit�ten zur�ck
	 * @return Aktiviaet-Array
	 * @throws ApplicationServerException
	 * author robert
	 */
	public Aktivitaet[] selectAktivitaeten() throws ApplicationServerException{
		Aktivitaet[] aktivitaeten = null;
		try{
			ResultSet rs = statements.get(10).executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			if (count > 0){
				aktivitaeten = new Aktivitaet[count];
				int i = 0;
				while (rs.next()){
					aktivitaeten[i] = new Aktivitaet(rs.getInt(1), rs.getString(2),  rs.getString(3));
					i++;
				}
			}
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(139,e.getMessage());
		}

		return aktivitaeten;
	}

	/**
	 * f�gt eine neue Zurordnung einer Aktivit�t zu einer Rolle
	 * @param rolle - Id der Rolle
	 * @param aktivitaet - Id der Aktivit�t
	 * @throws ApplicationServerException
	 * author robert
	 */
	 public void insertRollenAktivitaet(int rolle, int aktivitaet)  throws ApplicationServerException{
		 try{
			 Object[] parameters = {new Integer(rolle), new Integer(aktivitaet)};
			 statements.get(112).executeUpdate(parameters);
		 } catch (SQLException e){
			 throw new ApplicationServerException(140,e.getMessage());
		 }
	 }

	/**
	 * l�scht eine Zuordnung einer Aktivit�t zu einer Rolle
	 * @param rolle - Id der Rolle
	 * @param aktivitaet - Id der Aktivit�t
	 * @throws ApplicationServerException
	 * author robert
	 */
	 public void deleteRollenAktivitaet(int rolle, int aktivitaet)  throws ApplicationServerException{
		 try{
			Object[] parameters = {new Integer(rolle), new Integer(aktivitaet)};
			statements.get(113).executeUpdate(parameters);
		 } catch (SQLException e){
			 throw new ApplicationServerException(141,e.getMessage());
		 }
	 }

	/**
	 * gibt alle Rollen mit allen Aktivit�ten zur�ck
	 * @return Rolle-Array
	 * @throws ApplicationServerException
	 * author robert
	 */
	public Rolle[] selectRollenFull() throws ApplicationServerException{
		Rolle[] rollen = null;
		try{
			ResultSet rs = statements.get(100).executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			if (count > 0){
				rollen = new Rolle[count];
				int i = 0;
				while (rs.next()){
					Aktivitaet[] aktivitaeten = selectAktivitaetenFull(rs.getInt(1));
					rollen[i] = new Rolle(rs.getInt(1), rs.getString(2), aktivitaeten);
					i++;
				}
			}
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(142, e.getMessage());
		}

		return rollen;
	}

	/**
	 * Abfrage aller ZVKonten in der Datenbank.
	 * @return Liste mit ZVKonten
	 * author w.flat
	 */
	public ArrayList selectZVKonten() throws ApplicationServerException {
		ArrayList konten = new ArrayList();	// Liste f�r die ZVKonten

		try{
			// Das SQL-Statement mit der Nummer 120 ausf�hren
			ResultSet rs = statements.get(120).executeQuery();
			rs.last();	// In die letzte Zeile springen
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				
				while( rs.next() ){		// Solange es n�chste Abfragezeile gibt
					konten.add( new ZVKonto( rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5),
										rs.getFloat(6), rs.getFloat(7), rs.getString(8).equalsIgnoreCase( "1" ),
										rs.getString(9).charAt(0), rs.getShort(10),  rs.getString(11).equalsIgnoreCase( "1" ), rs.getString(12).equalsIgnoreCase( "1" )) );
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return konten;		// Liste mit den ZVKonten zur�ckgeben
	}

	/**
	 * Abfrage aller nicht abgeschlossener ZVKonten einer Haushaltsjahres.
	 * @return Liste mit ZVKonten
	 * author m.schmitt
	 */
	public ArrayList selectOffeneZVKonten(int haushaltsjahr) throws ApplicationServerException {
		ArrayList konten = new ArrayList();	// Liste f�r die ZVKonten

		try{
			// Das SQL-Statement mit der Nummer 127 ausf�hren
			Object[] parameters = {new Integer(haushaltsjahr)};
			ResultSet rs = statements.get(127).executeQuery(parameters);
			rs.last();	// In die letzte Zeile springen
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				
				while( rs.next() ){		// Solange es n�chste Abfragezeile gibt
					konten.add( new ZVKonto( rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5),
										rs.getFloat(6), rs.getFloat(7), rs.getString(8).equalsIgnoreCase( "1" ),
										rs.getString(9).charAt(0), rs.getShort(10),  rs.getString(11).equalsIgnoreCase( "1" ), rs.getString(12).equalsIgnoreCase( "1" )) );
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return konten;		// Liste mit den ZVKonten zur�ckgeben
	}
	
	/**
	 * Abfrage aller ZVTitel in der Datenbank die zu einem ZVKonto geh�ren.
	 * @param ZVKonto, zu dem die ZVTitel ermittelt werden sollen
	 * @return Liste mit den ZVTiteln, die zu dem ZVKonto geh�ren
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public ArrayList selectZVTitel( ZVKonto zvKonto ) throws ApplicationServerException {
		ArrayList konten = new ArrayList();	// Liste mit den ZVTiteln des ZVKontos

		try{
			// Parameter f�r das SQL-Statement 
			Object[] parameters = {new Integer(zvKonto.getId())};
			// SQL-Statement ausf�hren
			ResultSet rs = statements.get(140).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der abgefragten Zeilen gr��er 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				while( rs.next() ) {	// Solange es Zeilen gibt	
					konten.add( new ZVTitel( rs.getInt(1), zvKonto, rs.getString(3), rs.getString(4), rs.getString(5),
												rs.getFloat(6), rs.getFloat(7), rs.getString(8), rs.getString(9),
												rs.getString(10).equalsIgnoreCase("1") ) );
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}

		return konten;		// Ermittelten ZVTitel zur�ckgeben
	}

	/**
	 * Abfrage aller ZVUntertitel in der Datenbank die zu einem ZVTitel geh�ren.
	 * @param zvTitel, zu dem die ZVUntertitel ermittelt werden sollen
	 * @return Liste mit den ZVUntertiteln, die zu dem ZVTitel geh�ren
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public ArrayList selectZVUntertitel( ZVTitel zvTitel ) throws ApplicationServerException {
		ArrayList konten = new ArrayList();		// Liste f�r die ermittelten ZVUntertitel

		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = {new Integer(zvTitel.getZVKonto().getId()), zvTitel.getTitel()};
			// SQL-Statement mit der Nummer 141 ausf�hren
			ResultSet rs = statements.get(141).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if ( rs.getRow() > 0 ) {	// Gibt es ien Ergebnis
				rs.beforeFirst();		// Vor die erste Zeile springen
				while( rs.next() ) {	// Solange es Abfragezeilen gibt
					// ZVUntertitel erstellen und in die Liste einf�gen
					konten.add( new ZVUntertitel( rs.getInt(1), zvTitel, rs.getString(3), rs.getString(4), rs.getString(5),
										rs.getFloat(6), rs.getFloat(7), rs.getString(8), rs.getString(9), 
										rs.getString(10).equalsIgnoreCase("1") ) );
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}

		return konten;		// Die ermittelten ZVUntertitel zur�ckgeben
	}

	/**
	 * Ein neues ZVKonto erstellen.
	 * @param ZVKonto, welches erstellt werden soll
	 * @return kontoId vom erstellten ZVKonto
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int insertZVKonto( ZVKonto konto ) throws ApplicationServerException {
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = {new Integer(konto.getHaushaltsJahrId()), konto.getBezeichnung(), konto.getKapitel(),
								konto.getTitelgruppe(), new Float(konto.getTgrBudget()), new Float(konto.getDispoLimit()),
								(konto.getZweckgebunden() ? "1" : "0"), "" + konto.getFreigegeben(),
								"" + konto.getUebernahmeStatus()};
			// SQL-Statement ausf�hren
			statements.get(121).executeUpdate(parameters);
			ResultSet rs = statements.get(121).getGeneratedKeys();
			if (rs.next()) {
				   return rs.getInt(1);
			}else return 0;	// KontoId ermitteln und zur�ckgeben
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Abfrage ob ein ZVKonto existiert. 
	 * Notwendig:
	 *    - Titelkonto: Kapitel des ZVKontos und Titel des (einzigen) ZV-Titels
	 *    - Titelgruppenkonto: Kapitel und Titelgruppe des ZV-Kontos.
	 * @param ZVKonto, welches �berpr�ft werden soll
	 * @return kontoId vom �bergebenen ZVKonto
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int existsZVKonto( ZVKonto konto ) throws ApplicationServerException {
		try{
			
			// Parameter f�r das SQL-Statement
			Object[] parameters = {konto.getKapitel(),"" , konto.getKapitel(), konto.getTitelgruppe()};
						
			if (!konto.isTGRKonto()){
				parameters[1] = ((ZVTitel)konto.getSubTitel().get(0)).getTitel();
			}
			
			// SQL-Statement mit der Nummer 122 ausf�hren
			ResultSet rs = statements.get(122).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if( rs.getRow() > 0 ){		// Gibt es ein Ergebnis
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// N�chste Zeile
				return rs.getInt(1);	// R�ckgabe der kontoId
			}
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;		// Sonst R�ckgabe = 0 
	}
	
	/**
	 * Abfrage ob ein gel�schtes ZVKonto existiert. Dabei ist nur der Kapitel und die Titelgruppe entscheidend.
	 * @param ZVKonto, welches �berpr�ft werden soll
	 * @return kontoId vom ZVKonto
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int existsDeleteZVKonto( ZVKonto konto ) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = {konto.getKapitel(),"" , konto.getKapitel(), konto.getTitelgruppe()};
						
			if (!konto.isTGRKonto()){
				parameters[1] = ((ZVTitel)konto.getSubTitel().get(0)).getTitel();
			}
			// SQL-Statement mit der Nummer 126 ausf�hren
			ResultSet rs = statements.get(126).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if( rs.getRow() > 0 ){		// Gibt es ein Ergebnis
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// N�chste Zeile
				return rs.getInt(1);	// R�ckgabe der kontoId
			}
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;		// Sonst R�ckgabe = 0 
	}

	/**
	 * Abfrage ob ein ZVTitel existiert. Dabei ist die zvKontoId, der Titel und der Untertitel entscheidend.
	 * @param ZVTitel, das abgefragt werden soll
	 * @return titelId vom ZVTitel
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int existsZVTitel( ZVTitel titel ) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = {new Integer(titel.getZVKonto().getId()), titel.getTitel(), titel.getUntertitel()};
			// SQL-Statement mit der Numer 142 ausf�hren
			ResultSet rs = statements.get(142).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if( rs.getRow() > 0 ){	// Gibt es abgefragte Zeilen
				rs.beforeFirst();	// Vor die erste Zeile springen
				rs.next();			// N�chste abgefragte Zeile
				return rs.getInt(1);	// titelId zur�ckgeben
			}
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;		// Sonst ist die R�ckgabe = 0
	}
	
	/**
	 * Abfrage ob ein gel�schter ZVTitel existiert. Dabei ist die zvKontoId, der Titel und der Untertitel entscheidend.
	 * @param zvTitel, der abgefragt werden soll
	 * @return titelId vom ermittelten ZVTitel
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int existsDeleteZVTitel( ZVTitel titel ) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = {new Integer(titel.getZVKonto().getId()), titel.getTitel(), titel.getUntertitel()};
			// SQL-Statement mit der Numer 147 ausf�hren
			ResultSet rs = statements.get(147).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if( rs.getRow() > 0 ){	// Gibt es abgefragte Zeilen
				rs.beforeFirst();	// Vor die erste Zeile springen
				rs.next();			// N�chste abgefragte Zeile
				return rs.getInt(1);
			}
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;		// Sonst ist die R�ckgabe = 0
	}

	/**
	 * Abfrage ob ein ZVUntertitel existiert. Dabei ist die zvKontoId, der Titel und der Untertitel entscheidend.
	 * @param titel
	 * @return id vom ZVUntertitel
	 * @throws ApplicationServerException
	 */
	public int existsZVUntertitel( ZVUntertitel titel ) throws ApplicationServerException {
		try{
			Object[] parameters = {new Integer(titel.getZVTitel().getZVKonto().getId()), titel.getTitel(),titel.getUntertitel()};
			ResultSet rs = statements.get(142).executeQuery(parameters);
			rs.last();
			if( rs.getRow() > 0 ){
				rs.beforeFirst();
				rs.next();
				return rs.getInt(1);
			}
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;
	}
	
	/**
	 * Abfrage ob ein gel�schter ZVUntertitel existiert. Dabei ist die zvKontoId, der Titel und der Untertitel entscheidend.
	 * @param titel
	 * @return id vom ZVUntertitel
	 * @throws ApplicationServerException
	 */
	public int existsDeleteZVUntertitel( ZVUntertitel titel ) throws ApplicationServerException {
		try{
			Object[] parameters = {new Integer(titel.getZVTitel().getZVKonto().getId()), titel.getTitel(),titel.getUntertitel()};
			ResultSet rs = statements.get(147).executeQuery(parameters);
			rs.last();
			if( rs.getRow() > 0 ){
				rs.beforeFirst();
				rs.next();
				return rs.getInt(1);
			}
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;
	}
	
	/**
	 * Anzahl der Bestellungen(aktive oder abgeschlossene) ermitteln, <br>
	 * bei denen ein bestimmter ZVTitel angegeben wurde.
	 * @param ZVTitel/ZVUntertitel, der �berpr�ft werden sollte
	 * @return Anzahl der ermittelten Bestellungen
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int countBestellungen( ZVUntertitel konto )throws ApplicationServerException{
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement mit der Nummer 214 ausf�hren
			ResultSet rs = statements.get(214).executeQuery(parameters);
			if(rs.next())	// Gibt es ein Ergebnis
				return rs.getInt(1);	// Anzahl der Bestellungen
			else
				return 0;				// Sonst ist die Anzahl = 0
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Anzahl der aktiven Bestellungen ermitteln, bei denen ein bestimmter ZVTitel angegeben wurde.
	 * @param ZVTitel/ZVUntertitel, f�r welches die �berpr�fung durchgef�hrt werden soll.
	 * @return Anzahl der nicht abgeschlossenen Bestellungen mit diesem ZVTitel/ZVUntertitel.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int countActiveBestellungen( ZVUntertitel konto )throws ApplicationServerException{
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement mit der Nummer 215 ausf�hren
			ResultSet rs = statements.get(215).executeQuery(parameters);
			if(rs.next())	// Gibt es ein Ergebnis
				return rs.getInt(1);	// Anzahl der Bestellungen
			else
				return 0;				// Sonst ist die Anzahl = 0
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
	}
	
	/**
	 * Anzahl der Buchungen ermitteln, bei denen ein bestimmter ZVTitel angegeben wurde.
	 * @param ZVTitel/ZVUntertitel, f�r welches die �berpr�fung durchgef�hrt werden soll.
	 * @return Anzahl der Buchungen mit diesem ZVTitel/ZVUntertitel.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int countBuchungen( ZVUntertitel konto )throws ApplicationServerException{
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement mit der Nummer 222 ausf�hren
			ResultSet rs = statements.get(222).executeQuery(parameters);
			if(rs.next())	// Gibt es ein Ergebnis
				return rs.getInt(1);	// Anzahl der Buchungen
			else
				return 0;				// Sonst ist die Anzahl = 0
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Anzahl der Kontenzuordnungen ermitteln, bei denen ein bestimmtes ZVKonto angegeben wurde.
	 * @param ZVKonto, welches abgefragt werden soll
	 * @return Anzahl der Kontenzuordnungen, bei denen das ZVKonto angegeben ist
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int countKontenzuordnungen( ZVKonto konto )throws ApplicationServerException{
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement mit der Nummer 235 auf�hren
			ResultSet rs = statements.get(235).executeQuery(parameters);
			if(rs.next())	// Gibt es ein Ergebnis
				return rs.getInt(1);	// Anzahl der Kontenzuordnungen f�r dieses ZVKonto
			else
				return 0;				// Sonst ist die Anzahl = 0
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * �berpr�fung ob ein ZVKonto zweckgebunden sein kann. <br>
	 * Dabei wird ermittelt ob mehr als ein ZVKonto zu dem FBKonto einer Kontozuordnung existiert.
	 * @param ZVKonto f�r welches die �berpr�fung durchgef�hrt werden soll
	 * @return Zahl > 0, wenn das ZVKonto nicht zweckgebunden sein kann. Sonst Zahl = 0.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int countZVKonten( ZVKonto konto )throws ApplicationServerException{
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement mit der Nummer 237 auf�hren
			ResultSet rs = statements.get(237).executeQuery(parameters);
			if(rs.next())	// Gibt es ein Ergebnis
				return rs.getInt(1);	// Ermittelte Zahl zur�ckgeben
			else
				return 0;				// Sonst ist die Zahl = 0
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}


	/**
	 * Ein neuen ZVTitel zu einem ZVKonto erstellen erstellen.
	 * @param ZVTitel, welcher erstellt werden soll
	 * @return ZVTitelId vom erstellten ZVTitel
	 * @throws ApplicationServerException
	 */
	public int insertZVTitel( ZVTitel konto ) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters ={new Integer(konto.getZVKonto().getId()), konto.getBezeichnung(), konto.getTitel(),
									konto.getUntertitel(), new Float(konto.getBudget()), new Float(konto.getVormerkungen()),
									konto.getBemerkung(), konto.getBedingung()};
			// SQL-Statement mit der Nummer 143 ausf�hren
			statements.get(143).executeUpdate(parameters);
			return existsZVTitel( konto );		// ZVTitelId ermitteln und zur�ckgeben
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Ein neuen ZVUntertitel zu einem ZVTitel erstellen erstellen.
	 * @param ZVUntertitel welcher erstellt werden soll
	 * @return ZVUntertitelId vom erstellten ZVUntertitel
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int insertZVUntertitel( ZVUntertitel konto ) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters ={new Integer(konto.getZVTitel().getZVKonto().getId()), konto.getBezeichnung(), konto.getTitel(),
								konto.getUntertitel(), new Float(konto.getBudget()), new Float(konto.getVormerkungen()), 
								konto.getBemerkung(), konto.getBedingung()};
			// SQL-Statement mit der Nummer 143 ausf�hren
			statements.get(143).executeUpdate(parameters);
			return existsZVUntertitel( konto );		// ZVUntertitelId ermitteln und zur�ckgeben
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Ein ZVKonto in der Datenbank l�schen.
	 * @param ZVKonto das gel�scht werden soll.
	 * @return Die gel�schte Zeile in der ZVKonto-Tabelle
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int deleteZVKonto( ZVKonto konto ) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = {new Integer(konto.getId())};
			// SQL-Statement mit der Nummer 123 ausf�hren
			return statements.get(123).executeUpdate(parameters);	// gel�schte Zeile zur�ckgeben
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Einen ZVTitel in der Datenbank l�schen.
	 * @param ZVTitel das gel�scht werden soll.
	 * @return Die gel�schte Zeile in der ZVTitel-Tabelle
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int deleteZVTitel( ZVUntertitel titel ) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = {new Integer(titel.getId())};
			// SQL-Statement mit der Nummer 144 ausf�hren
			return statements.get(144).executeUpdate(parameters);	// gel�schte Zeile zur�ckgeben
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Einen ZVUntertitel in der Datenbank l�schen.
	 * @param ZVUntertitel das gel�scht werden soll.
	 * @return Die gel�schte Zeile in der ZVTitel-Tabelle
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int deleteZVUntertitel( ZVUntertitel titel ) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = {new Integer(titel.getId())};
			// SQL-Statement mit der Nummer 144 ausf�hren
			return statements.get(144).executeUpdate(parameters);	// gel�schte Zeile zur�ckgeben
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}

	}

	/**
	 * Ein ZVKonto in der Datenbank aktualisieren.
	 * @param ZVKonto, welches aktualisiert werden soll.
	 * @return zvKontoId vom aktualisierten ZVKonto
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int updateZVKonto( ZVKonto konto ) throws ApplicationServerException {
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = {new Integer(konto.getHaushaltsJahrId()),  konto.getBezeichnung(), konto.getKapitel(),
							konto.getTitelgruppe(), new Float(konto.getTgrBudget()), new Float(konto.getDispoLimit()),
							(konto.getZweckgebunden() ? "1" : "0"), ""+konto.getFreigegeben(), ""+konto.getUebernahmeStatus(),
							(konto.isPortiert() ? "1" : "0"),(konto.isAbgeschlossen() ? "1" : "0"),(konto.getGeloescht() ? "1" : "0"), new Integer(konto.getId())};
			// SQL-Statement mit der Nummer ausf�hren 
			statements.get(124).executeUpdate(parameters);
			if( konto.getGeloescht() )		// Wenn das gel�schtes Konto
				return existsDeleteZVKonto( konto );	// Dann zvKontoId vom gel�schten ZVKonto
			else
				return existsZVKonto( konto );			// Sonst von aktiven ZVKonto
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Einen ZVTitel in der Datenbank aktualisieren.
	 * @param ZVTitel, welcher aktualisiert werden soll
	 * @return zvTitelId des aktualisierten ZVTitels
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int updateZVTitel( ZVTitel titel ) throws ApplicationServerException {
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = {new Integer(titel.getZVKonto().getId()), titel.getBezeichnung(), titel.getTitel(),
							titel.getUntertitel(), new Float(titel.getBudget()), new Float(titel.getVormerkungen()), 
							titel.getBemerkung(), titel.getBedingung(), 
							(titel.getGeloescht() ? "1" : "0"), new Integer(titel.getId())};
			// SQL-Statement mit der Nummer 145 ausf�hren
			statements.get(145).executeUpdate(parameters);
			if( titel.getGeloescht() )		// Wenn gel�schter ZVTitel
				return existsDeleteZVTitel( titel );	// Dann ZVTitelId vom gel�schten ZVTitel
			else
				return existsZVTitel( titel );			// Sonst vom aktiven ZVTitel
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Einen ZVUntertitel in der Datenbank aktualisieren.
	 * @param ZVUntertitel welcher aktualisiert werden soll
	 * @return ZVUntertitelId vom aktualisierten ZVUntertitel
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int updateZVUntertitel( ZVUntertitel titel ) throws ApplicationServerException {
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = {new Integer( titel.getZVTitel().getZVKonto().getId()), titel.getBezeichnung(), titel.getTitel(),
							titel.getUntertitel(), new Float(titel.getBudget()), new Float(titel.getVormerkungen()), 
							titel.getBemerkung(), titel.getBedingung(),
							(titel.getGeloescht() ? "1" : "0"), new Integer(titel.getId())};
			// SQL-Statement mit der Nummer 145 ausf�hren
			statements.get(145).executeUpdate(parameters);
			if( titel.getGeloescht() )		// Wenn gel�schter ZVTitel
				return existsDeleteZVUntertitel( titel );	// Dann ZVUntertitelId vom gel�schten ZVUntertitel
			else
				return existsZVUntertitel( titel );			// Sonst vom aktiven ZVUntertitel
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	
	/**
	 * Ein ZVKonto zum Aktualisieren ausw�hlen.
	 * @param ZVKonto, welches ausgew�hlt werden soll.
	 * @return ZVKonto, das ausgew�hlt wurde
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public ZVKonto selectForUpdateZVKonto( ZVKonto zvKonto ) throws ApplicationServerException {
		ZVKonto result = null;		// Das ausgew�hlte ZVKonto
		try {
			// Parameter f�r das SQL-Statement
			Object[] parameters = {new Integer( zvKonto.getId() )};
			// SQL-Statement mit der Nummer 125 ausf�hren
			ResultSet rs = statements.get(125).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if ( rs.getRow() > 0 ) {	// Gibt es abgefragte Zeilen
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// N�chste Abfrage-Zeile
				// Neues ZVKonto erstellen
				result = new ZVKonto( zvKonto.getId(), rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
									rs.getFloat(5), rs.getFloat(6), !rs.getString(7).equalsIgnoreCase( "0" ),
									rs.getString(8).charAt(0), rs.getShort(9), rs.getString(10).equalsIgnoreCase( "1" ), rs.getString(11).equalsIgnoreCase( "1" ), !rs.getString(12).equalsIgnoreCase( "0" ) );
			}
			rs.close();		// Abfrage schlie�en
		} catch(SQLException e) {
			throw new ApplicationServerException( 0, e.getMessage() );
		}
		
		return result;		// ZVKonto zur�ckgeben
	}
	
	/**
	 * Einen ZVTitel zum Aktualisieren ausw�hlen.
	 * @param ZVTitel, der aktualisiert werden soll
	 * @return ZVTitel der aktualisiert werden soll
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public ZVTitel selectForUpdateZVTitel( ZVTitel zvTitel ) throws ApplicationServerException {
		ZVTitel result = null;		// Der ausgew�hlte ZVTitel
		try {
			// Parameter f�r das SQL-Statement
			Object[] parameters = {new Integer( zvTitel.getId() )};
			// SQL-Statement mit der Nummer 146 ausf�hren
			ResultSet rs = statements.get(146).executeQuery(parameters);
			rs.last();		// In die letzte Abfragezeile springen 
			if ( rs.getRow() > 0 ) {	// Gibt es ein Ergebnis
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// Die N�chste Zeile
				// Neuen ZVTitel erstellen
				result = new ZVTitel( zvTitel.getId(), zvTitel.getZVKonto(), rs.getString(2), rs.getString(3), rs.getString(4),
									rs.getFloat(5), rs.getFloat(6), rs.getString(7), rs.getString(8), 
									rs.getString(9).equalsIgnoreCase("1") );
			}
			rs.close();		// Abfrage schlie�en
		} catch(SQLException e) {
			throw new ApplicationServerException( 0, e.getMessage() );
		}
		
		return result;		// ZVtitel zur�ckgeben
	}
	
	/**
	 * Einen ZVTitel mit der angegebenen zvTitelId ermitteln.
	 * @param Id des ZVTitels
	 * @return Ermittelter ZVTitel. 
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public ZVTitel selectZVTitel( int id ) throws ApplicationServerException {
		ZVTitel result = null;		// Der ausgew�hlte ZVTitel
		try {
			// Parameter f�r das SQL-Statement
			Object[] parameters = {new Integer( id )};
			// SQL-Statement mit der Nummer 148 ausf�hren
			ResultSet rs = statements.get(148).executeQuery(parameters);
			rs.last();		// In die letzte Abfragezeile springen 
			if ( rs.getRow() > 0 ) {	// Gibt es ein Ergebnis
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// Die N�chste Zeile
				// Neuen ZVTitel erstellen
				ZVKonto zvKonto = selectZVKonto(rs.getInt(1));
				result = new ZVTitel( id, zvKonto, rs.getString(2), rs.getString(3),rs.getString(4),
									rs.getFloat(5), rs.getString(6), rs.getString(7), rs.getString(8).equalsIgnoreCase( "1" ) );
			}
			rs.close();		// Abfrage schlie�en
		} catch(SQLException e) {
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	
		return result;		// ZVtitel zur�ckgeben
	}
		
	/**
	 * Einen ZVUntertitel zum Aktualisieren ausw�hlen
	 * @param ZVUntertitel, der aktualisiert werden soll
	 * @return ZVUntertitel der aktualisiert werden soll
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public ZVUntertitel selectForUpdateZVUntertitel( ZVUntertitel zvUntertitel ) throws ApplicationServerException {
		ZVUntertitel result = null;		// Der ausgew�hlte ZVUntertitel
		try {
			// Parameter f�r das SQL-Statement
			Object[] parameters = {new Integer( zvUntertitel.getId() )};
			// SQL-Statement mit der Nummer 146 ausf�hren
			ResultSet rs = statements.get(146).executeQuery(parameters);
			rs.last();		// In die letzte Abfragezeile springen 
			if ( rs.getRow() > 0 ) {	// Gibt es ein Ergebnis
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// Die N�chste Zeile
				// Neuen ZVUntertitel erstellen
				result = new ZVUntertitel( zvUntertitel.getId(), zvUntertitel.getZVTitel(), rs.getString(2), rs.getString(3),
											rs.getString(4), rs.getFloat(5), rs.getFloat(6), rs.getString(7), rs.getString(8), 
											rs.getString(9).equalsIgnoreCase("1") );
			}
			rs.close();		// Abfrage schlie�en
		} catch(SQLException e) {
			throw new ApplicationServerException( 0, e.getMessage() );
		}
		
		return result;		// ZVtitel zur�ckgeben
	}

  /**
   * pr�ft ob die Bezeichnung der Rolle schon mal in der Daten vorhanden ist
   * @param rolle
   * @return Anzahl der gleichen Bezeichnungen einer Rollen ausser der �bergebenen Rolle
   * @throws ApplicationServerException
   * author robert
   */
  public int checkRolle(Rolle rolle) throws ApplicationServerException{
  	try{
	  	Object[] parameters = { new Integer(rolle.getId()), rolle.getBezeichnung()};
			ResultSet rs = statements.get(101).executeQuery(parameters);
			rs.last();

		  return rs.getRow();
	  } catch (SQLException e){
			throw new ApplicationServerException(143, e.getMessage());
	  }
  }

	/**
	 * f�gt eine neue Rolle hinzu
	 * @param rolle
	 * @return Id - neue Id der Rolle
	 * @throws ApplicationServerException
	 * author robert
	 */
  public int insertRolle(Rolle rolle) throws ApplicationServerException{
		try{
		   Object[] parameters = { new Integer(rolle.getId()), rolle.getBezeichnung()};
		   statements.get(102).executeUpdate(parameters);
		   ResultSet rs = statements.get(102).getGeneratedKeys();
		   if (rs.next()) {
			   return rs.getInt(1);
		   }
   } catch (SQLException e){
	   throw new ApplicationServerException(144, e.getMessage());
   }
   return 0;
	}

	/**
	 * aktualisiert eine Rolle
	 * @param rolle
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void updateRolle(Rolle rolle) throws ApplicationServerException{
		try{
			Object[] parameters = {rolle.getBezeichnung(), new Integer(rolle.getId())};
				if(statements.get(103).executeUpdate(parameters) == 0)
					throw new ApplicationServerException(7);
		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	/**
	 * l�scht eine Rolle
	 * @param rolle
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void deleteRolle(Rolle rolle) throws ApplicationServerException{
	  try{
		Object[] parameters = { new Integer(rolle.getId()), rolle.getBezeichnung() };
		   if(statements.get(104).executeUpdate(parameters) == 0)
					throw new ApplicationServerException(7);
		   } catch (SQLException e){
			   throw new ApplicationServerException(1, e.getMessage());
		   }
	}

	/**
	 * l�scht alle Zuordnungen der Aktivit�ten zu einer Rolle
	 * @param rolle - Id der Rolle
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void deleteRollenAktivitaeten(int rolle)  throws ApplicationServerException{
		 try{
			Object[] parameters = {new Integer(rolle)};
			statements.get(114).executeUpdate(parameters);
		 } catch (SQLException e){
			 System.out.println(e.getMessage());
			 throw new ApplicationServerException(1,e.getMessage());
		 }
	 }

	/**
	 * Gibt die Anzahl der Benutzer die dieser Rolle zugeordnet sind
	 * @param rolle
	 * @return Anzahl der Benutzer die dieser Rolle zugeordnet
	 * @throws ApplicationServerException
	 */
	public int selectBenutzerRolle(Rolle rolle) throws ApplicationServerException{
	  try{
		  Object[] parameters = { new Integer(rolle.getId())};
			  ResultSet rs = statements.get(105).executeQuery(parameters);
			  rs.last();

			return rs.getRow();
		} catch (SQLException e){
			  throw new ApplicationServerException(1, e.getMessage());
		}
	}

	/**
	 * Gibt die Anzahl der Bestellungen eines Benutzers an
	 * @param benutzer
	 * @return Anzahl der Bestellungen
	 * @throws ApplicationServerException
	 */
	public int countBestellungen(Benutzer benutzer)throws ApplicationServerException{
		try{
		  Object[] parameters = { new Integer(benutzer.getId())};
			ResultSet rs = statements.get(210).executeQuery(parameters);
			if(rs.next())
				return rs.getInt(1);
			else
			  return 0;
	  } catch (SQLException e){
		  throw new ApplicationServerException(1, e.getMessage());
	  }
	}

 	/**
 	 * Gibt die Anzahl der Bestellungen eines Benutzers an
 	 * @param benutzer
 	 * @return Anzahl der Bestellungen
 	 * @throws ApplicationServerException
 	 * author w.flat
 	 */
 	public int countAktiveBestellungen(Benutzer benutzer)throws ApplicationServerException{
		 try{
		   Object[] parameters = { new Integer(benutzer.getId())};
			 ResultSet rs = statements.get(211).executeQuery(parameters);
		   if(rs.next())
		   	 return rs.getInt(1);
		   else
		    	return 0;

		 } catch (SQLException e){
		   throw new ApplicationServerException(1, e.getMessage());
	   }
	}

	/**
	 * Gibt die Anzahl der Bestellungen eines Benutzers an
	 * @param benutzer
	 * @return Anzahl der Bestellungen eines Benutzers
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int countBuchungen(Benutzer benutzer)throws ApplicationServerException{
		 try{
		   Object[] parameters = { new Integer(benutzer.getId())};
			 ResultSet rs = statements.get(220).executeQuery(parameters);
				if(rs.next())
				  return rs.getInt(1);
				else
					 return 0;
	   } catch (SQLException e){
		   throw new ApplicationServerException(1, e.getMessage());
	   }
	 }

	 /**
	  * gibt die nicht gesperrten Kontenzuordnungen zu einem FBHauptkonto zur�ck
	  * @param FBHauptkonto
	  * @return Kontenzuordnungen[]
	  * @throws ApplicationServerException
	  * author robert
	  */
	 public Kontenzuordnung[] selectKontenzuordnungen(FBHauptkonto konto)throws ApplicationServerException{
		Kontenzuordnung[] zuordnung = null;
	 	try{
			Object[] parameters = { new Integer(konto.getId())};

			ResultSet rs = statements.get(233).executeQuery(parameters);
			rs.last();

			if (rs.getRow() > 0){
				zuordnung = new Kontenzuordnung[rs.getRow()];
				rs.beforeFirst();

				int i = 0;
				while (rs.next()){
					ZVKonto zvKonto = new ZVKonto(rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), !rs.getString(6).equalsIgnoreCase( "0" ));
					zuordnung[i++] = new Kontenzuordnung( rs.getShort(1), zvKonto );
				}
			}
			rs.close();
		} catch (SQLException e){
			System.out.println( e.getMessage());
			throw new ApplicationServerException(1, e.getMessage());
		}
	 	return zuordnung;
	}

	 /**
	  * Gibt alle Kontenzuordnungen zu einem FBHauptkonto zur�ck
	  * @param FBHauptkonto
	  * @return Kontenzuordnungen[]
	  * @throws ApplicationServerException
	  * author mario
	  */
	 public Kontenzuordnung[] selectAllKontenzuordnungen(FBHauptkonto konto)throws ApplicationServerException{
		Kontenzuordnung[] zuordnung = null;
	 	try{
	 		
	 		
			Object[] parameters = { new Integer(konto.getId())};
			
			ResultSet rs = statements.get(238).executeQuery(parameters);
			
			
			rs.last();

			if (rs.getRow() > 0){
				zuordnung = new Kontenzuordnung[rs.getRow()];
				rs.beforeFirst();

				int i = 0;
				while (rs.next()){
					ZVKonto zvKonto = new ZVKonto(rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), !rs.getString(6).equalsIgnoreCase( "0" ));
					zuordnung[i++] = new Kontenzuordnung( rs.getShort(1), zvKonto );
				}
			}
			rs.close();
		} catch (SQLException e){
			//System.out.println( e.getMessage());
			throw new ApplicationServerException(1, e.getMessage());
		}
	 	return zuordnung;
	}
	 
	 
	/**
	 * gibt die Kontenzuordnugn mit der fbKontoId und zvKontoId
	 * @param fbKontoId
	 * @param zvKontoId
	 * @return Kontenzuordnung
	 * @throws ApplicationServerException
	 * author robert
	 */
	public Kontenzuordnung selectKontenzuordnung(int fbKontoId, int zvKontoId)throws ApplicationServerException{
		Kontenzuordnung zuordnung = null;
		try{
			Object[] parameters = { new Integer(fbKontoId), new Integer(zvKontoId)};

			ResultSet rs = statements.get(234).executeQuery(parameters);

			if(rs.next()){
				ZVKonto zvKonto = new ZVKonto(rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), !rs.getString(6).equalsIgnoreCase( "0" ));
				zuordnung = new Kontenzuordnung( rs.getShort(1), zvKonto );
			}

			rs.close();
		} catch (SQLException e){
			System.out.println( e.getMessage());
			throw new ApplicationServerException(1, e.getMessage());
		}
		return zuordnung;
	}

	/**
	 * f�gt eine neue Kontenzuordnung hinzu
	 * @param fbKontoId - Id des FBKontos
	 * @param zvKontoId - Id des ZVKontos
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void insertKontenZuordnung(int fbKontoId, int zvKontoId)throws ApplicationServerException{
		try{
			Object[] parameters = { new Integer(zvKontoId), new Integer(fbKontoId) };
			statements.get(232).executeUpdate(parameters);
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	/**
	 * l�scht eine Kontenzuordnung
	 * @param fbKontoId - Id des FBKontos
	 * @param zvKontoId - Id des ZVKontos
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void deleteKontenZuordnung(int fbKontoId, int zvKontoId)throws ApplicationServerException{
		try{
			Object[] parameters = { new Integer(zvKontoId), new Integer(fbKontoId) };
			statements.get(231).executeUpdate(parameters);
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	/**
	 * aktualisiert eine Kontenzuordnung
	 * @param fbKontoId - Id des FBKontos
	 * @param zvKontoId - Id des ZVKontos
	 * @param status
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void updateKontenZuordnung(int fbKontoId, int zvKontoId, short status)throws ApplicationServerException{
		try{
			Object[] parameters = {new Integer(status), new Integer(fbKontoId), new Integer(zvKontoId) };
			statements.get(230).executeUpdate(parameters);
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException(1, e.getMessage());
		}
	}


	/**
	 * Abfrage eines ZVKontos in der Datenbank
	 * @param zvKontoId - Id des ZVKontos
	 * @return ZVKonto
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public ZVKonto selectZVKonto(int zvKontoId) throws ApplicationServerException {
		ZVKonto konto = null;

		try{
			Object[] parameters = { new Integer(zvKontoId)};
			ResultSet rs = statements.get(139).executeQuery(parameters);

			if ( rs.next() ){
				konto = new ZVKonto( rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5),
									rs.getFloat(6), rs.getFloat(7), !rs.getString(8).equalsIgnoreCase( "0" ),
									rs.getString(9).charAt(0), rs.getShort(10), rs.getString(11).equalsIgnoreCase( "1" ), rs.getString(12).equalsIgnoreCase( "1" ) );
			}

			rs.close();
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		return konto;
	}

	/**
	 * Abfrage eines FBHauptkontos mit der angegebenen Id.
	 * @param fbHauptKontoid - Id des FBHauptkontos
	 * @return FBHauptkonto
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public FBHauptkonto selectFBHauptkonto(int fbHauptKontoid) throws ApplicationServerException {
		FBHauptkonto konto = null;

		try{
			Object[] parameters = { new Integer(fbHauptKontoid) };
			ResultSet rs = statements.get(69).executeQuery(parameters);

			if( rs.next() ){
				konto = new FBHauptkonto( rs.getInt(1), rs.getInt(2), null, rs.getString(4), rs.getString(5),
											rs.getString(6), rs.getFloat(7), rs.getFloat(8), rs.getString(9) );
			}

			rs.close();
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		return konto;
	}
	
	/**
	 * Abfrage eines FBHauptkontos f�r ein PrivatKonto.
	 * @param hauptkonto - String des FBHauptkontos
	 * @param institut - Id des Instituts
	 * @return FBHauptkonto
	 * @throws ApplicationServerException
	 * author robert
	 */
	public FBHauptkonto selectFBHauptkonto(String hauptkonto, int institut) throws ApplicationServerException {
			FBHauptkonto konto = null;

			try{
				Object[] parameters = { hauptkonto, new Integer(institut) };
				ResultSet rs = statements.get(68).executeQuery(parameters);

				if( rs.next() ){
					konto = new FBHauptkonto( rs.getInt(1), rs.getInt(2), null, rs.getString(4), rs.getString(5),
												rs.getString(6), rs.getFloat(7), rs.getFloat(8), rs.getString(9) );
				}

				rs.close();
			} catch (SQLException e){
				System.out.println(e.getMessage());
				throw new ApplicationServerException( 1, e.getMessage() );
			}
			return konto;
		}

	/**
	 * Abfrage eines FBUnterkontos mit der angegebenen Id.
	 * @param fbKontoid - Id des FBUnterkontos
	 * @return FBUnterkonto
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public FBUnterkonto selectFBKonto(int fbKontoid) throws ApplicationServerException {
		FBUnterkonto konto = null;

		try{
			Object[] parameters = { new Integer(fbKontoid) };
			ResultSet rs = statements.get(69).executeQuery(parameters);

			if( rs.next() ){
				Institut institut = selectInstitute(rs.getInt(3));
				konto = new FBUnterkonto( rs.getInt(1), rs.getInt(2), institut, rs.getString(4), rs.getString(5),
											rs.getString(6), rs.getFloat(7));
			}

			rs.close();
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		return konto;
	}
	
	
	/**
	 * Abfrage aller nicht gel�schter Firmen.
	 * @return ArrayListe mit nicht gel�schten Firmen.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public ArrayList selectFirmen() throws ApplicationServerException {
		ArrayList firmen = new ArrayList();		// Liste mit den Firmen
		try{
			// SQL-Statement mit der Nummer 240 ausf�hren
			ResultSet rs = statements.get(240).executeQuery();
			rs.last();		// In die letzte Zeile springen
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeile > 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				while( rs.next() ){		// Solange es noch Abfrage-Zeilen gibt
					firmen.add( new Firma( rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
											rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
											rs.getString(10), rs.getString(11).equalsIgnoreCase( "1" ), 
											rs.getString(12).equalsIgnoreCase( "1" ) ) );
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return firmen;		// Die Firmen zur�ckgeben
	}
	
	/**
	 * gibt eine Firma anhand der id zur�ck
	 * @param id der Firma
	 * @return Firma
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public Firma selectFirma(int id) throws ApplicationServerException {
		Firma firma = null;		
		try{
			Object[] parameters = { new Integer(id) };
			ResultSet rs = statements.get(248).executeQuery(parameters);
			
			if ( rs.next()) {	
				firma = new Firma( rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
											rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
											rs.getString(10), rs.getString(11).equalsIgnoreCase( "1" ), 
											rs.getString(12).equalsIgnoreCase( "1" ) );
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 75, e.getMessage() );
		}
	
		return firma;		// Die Firmen zur�ckgeben
	}
	
	/**
	 * gibt die Firma f�r eine ASK-Bestellung zur�ck
	 * @return ASK-Firma
	 * @throws ApplicationServerException
	 * author robert
	 */
	public Firma selectASKFirma() throws ApplicationServerException {
		Firma firma = null;		
		try{
			ResultSet rs = statements.get(249).executeQuery();
		
			if ( rs.next()) {	
				firma = new Firma( rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
											rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
											rs.getString(10), rs.getString(11).equalsIgnoreCase( "1" ), 
											rs.getString(12).equalsIgnoreCase( "1" ) );
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 96, e.getMessage() );
		}

		return firma;		// Die Firmen zur�ckgeben
	}
	
	/**
	 * Abfrage aller gel�schter Firmen.
	 * @return ArrayList mit gel�schten Firmen.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public ArrayList selectDelFirmen() throws ApplicationServerException {
		ArrayList firmen = new ArrayList();		// Liste f�r die gel�schten Firmen
		try {
			// SQL-Statement mit der Nummer 241 ausf�hren
			ResultSet rs = statements.get(241).executeQuery();
			rs.last();		// In die letzte Zeile springen
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeile > 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				while( rs.next() ){		// Solange es noch Abfrage-Zeilen gibt
					firmen.add( new Firma( rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
											rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
											rs.getString(10), rs.getString(11).equalsIgnoreCase( "1" ),
											rs.getString(12).equalsIgnoreCase( "1" ) ) );
				}
			}

			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return firmen;		// Die Firmen zur�ckgeben
	}
	
	/**
	 * Eine Firma zum aktualisieren ausw�hlen.
	 * @param Firma, die ausgew�hlt werden soll.
	 * @return Firma, die ausgew�hlt wurde.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public Firma selectForUpdateFirma( Firma firma ) throws ApplicationServerException {
		Firma result = null;
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = {new Integer(firma.getId())};
			// SQL-Statement mit der Nummer 242 ausf�hren
			ResultSet rs = statements.get(242).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeile > 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// Die n�chste Zeile
				result = new Firma( rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
									rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
									rs.getString(10), rs.getString(11).equalsIgnoreCase( "1" ),
									rs.getString(12).equalsIgnoreCase( "1" ) );
			}
			rs.close();			// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return result;		// Die Firma zur�ckgeben
	}
	
	/**
	 * Abfrage ob eine nicht gel�schte Firma schon existiert. <br>
	 * Dabei ist der Namen, Strasse, Strassen-Nr, PLZ, Ort entscheidend.
	 * @param Firma, die �berpr�ft werden soll.
	 * @return id von der Firma.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int existsFirma( Firma firma ) throws ApplicationServerException {
		try { 
			// Parameter f�r das SQL-Statement
			Object[] parameters = {new String(firma.getName()), new String(firma.getStrasseNr()),
									new String(firma.getPlz()), new String(firma.getOrt())};
			// SQL-Satement mit der Nummer 243 ausf�hren
			ResultSet rs = statements.get(243).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if( rs.getRow() > 0 ){		// Ist die Anzahl der Zeile > 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// Die n�chste Zeile
				return rs.getInt(1);	// Id der Firma
			}
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;
	}
	
	/**
	 * Abfrage ob eine gel�schte Firma schon existiert. <br>
	 * Dabei ist der Namen, Strasse, Strassen-Nr, PLZ, Ort entscheidend.	 
	 * @param Firma, die �berpr�ft werden soll.
	 * @return id von der Firma
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int existsDelFirma( Firma firma ) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = {new String(firma.getName()), new String(firma.getStrasseNr()),
									new String(firma.getPlz()), new String(firma.getOrt())};
			// SQL-Satement mit der Nummer 244 ausf�hren
			ResultSet rs = statements.get(244).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if( rs.getRow() > 0 ){		// Ist die Anzahl der Zeile > 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// Die n�chste Zeile
				return rs.getInt(1);	// Id der Firma
			}
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;
	}

	/**
	 * Eine Firma in der Datenbank aktualisieren.
	 * @param Firma, die aktualisiert werden soll.
	 * @return id von der Firma, die aktulisiert wurde.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int updateFirma( Firma firma ) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = { firma.getName(), firma.getStrasseNr(), firma.getPlz(), firma.getOrt(), firma.getKundenNr(),
									firma.getTelNr(), firma.getFaxNr(), firma.getEMail(), firma.getWWW(), 
									new String(firma.getASK() ? "1" : "0"),
									new String(firma.getGeloescht() ? "1" : "0"), new Integer( firma.getId() ) };
			// SQL-Satement mit der Nummer 245 ausf�hren
			statements.get(245).executeUpdate(parameters);
			if( firma.getGeloescht() )		// Wenn gel�schte Firma
				return existsDelFirma( firma );	// Id von der gel�schten Firma
			else
				return existsFirma( firma );	// Id von der nicht gel�schten Firma
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	/**
	 * Ein neue Firma erstellen.
	 * @param Firma, die erstellt werden soll.
	 * @return id der erstellten Firma.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	 public int insertFirma( Firma firma ) throws ApplicationServerException {
		 try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = { firma.getName(), firma.getStrasseNr(), firma.getPlz(), firma.getOrt(), firma.getKundenNr(),
									firma.getTelNr(), firma.getFaxNr(), firma.getEMail(), firma.getWWW(), 
									new String( firma.getASK() ? "1" : "0" ), new String( firma.getGeloescht() ? "1" : "0" ) };
			// SQL-Satement mit der Nummer 246 ausf�hren
			statements.get(246).executeUpdate(parameters);
			return existsFirma( firma );	// Id von der Firma
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	/**
	 * Eine Firma in der Datenbank l�schen.
	 * @param Firma, die gel�scht werden soll.
	 * @return Zeilenindex von der Firma, die gel�scht wurde.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int deleteFirma( Firma firma ) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = {new Integer(firma.getId())};
			// SQL-Satement mit der Nummer 247 ausf�hren
			statements.get(247).executeUpdate(parameters);
			return firma.getId();		// FirmenId zur�ckgeben
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	/**
	 * Anzahl der Belege, bei denen eine bestimmte Firma angegeben wurde.
	 * @param Firma, die �berpr�ft werden soll.
	 * @return Anzahl der Belege.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int countBelege( Firma firma )throws ApplicationServerException{
		 try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = { new Integer(firma.getId()) };
			// SQL-Satement mit der Nummer 275 ausf�hren
			ResultSet rs = statements.get(275).executeQuery(parameters);
		   if(rs.next())		// Gibt es ein Ergebnis
			 return rs.getInt(1);	// Anzahl zur�ckgeben
		   else
				return 0;			// Sonst ist die Anzahl = 0
		 } catch (SQLException e){
		   throw new ApplicationServerException(1, e.getMessage());
	   }
	}
	
	/**
	 * Anzahl der Angebote, bei denen eine bestimmte Firma angegeben wurde.
	 * @param Firma, die �berpr�ft werden soll.
	 * @return Anzahl der Angebote.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int countAngebote( Firma firma )throws ApplicationServerException{
		 try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = { new Integer(firma.getId()) };
			// SQL-Satement mit der Nummer 262 ausf�hren
			ResultSet rs = statements.get(262).executeQuery(parameters);
		   if(rs.next())		// Gibt es ein Ergebnis
			 return rs.getInt(1);	// Anzahl zur�ckgeben
		   else
				return 0;			// Sonst ist die Anzahl = 0
		 } catch (SQLException e){
		   throw new ApplicationServerException(1, e.getMessage());
	   }
	}

	 /**
	  * Gibt die Benutzer eines Instituts mit einer bestimmten Rolle zur�ck
	  * @param institut
	  * @param rollenId - Id der Rolle
	  * @return Benutzer-Array
	  * @throws ApplicationServerException
	  * author Mario
	  */
	 public Benutzer[] selectUsersByRole(Institut institut, int rollenId) throws ApplicationServerException{
		 Benutzer[] benutzer = null;
		 try{
			 Object[] parameters = { new Integer(institut.getId()), new Integer(rollenId) };
	
			 ResultSet rs = statements.get(30).executeQuery(parameters);
			 rs.last();
			 int count = rs.getRow();
			 rs.beforeFirst();
			 if (count > 0){
				 benutzer = new Benutzer[count];
				 int i = 0;
				 while (rs.next()){
					 benutzer[i] = new Benutzer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
					 i++;
				 }
			 }
			 rs.close();
			 return benutzer;
		 } catch (SQLException e){
			 throw new ApplicationServerException(1, e.getMessage());
		 }
	 }

	 /**
	   * Ermittelt die Summe der Budgets aller zweckungebundenen ZV-Konten
	   * @return zweckungebundenes ZV-Budget
	   * @throws ApplicationServerException
	   * author Mario
	   */	 	
	 public float selectNoPurposeZVBudgetSum() throws ApplicationServerException{
		 float result = 0;

		 try{
			 ResultSet rs = statements.get(161).executeQuery();
			 while(rs.next()){
				 result += rs.getFloat(1) + rs.getFloat(2);
			 }
		 } catch (SQLException e){
			 throw new ApplicationServerException(150, e.getMessage());
		 }

		 return result;
	 }

	/**
	 * Ermittelt die Summe der Budgets aller FB-Konten die zweckungebundenen ZV-Konten
	 * zugeordnet sind.
	 * @return zweckungebundenes FB-Budget
	 * @throws ApplicationServerException
	 * author Mario
	 */	 	 
	 public float selectNoPurposeFBBudgetSum() throws ApplicationServerException{
		 float result = 0;

		 try{
			 ResultSet rs = statements.get(163).executeQuery();
			 while(rs.next()){
				 result += rs.getFloat(2);
			 }
		 } catch (SQLException e){
			 throw new ApplicationServerException(150, e.getMessage());
		 }

		 return result;
	 }
	 
	/**
	 * Ermittelt das Gesamtbudget des �bergebenen ZV-Kontos
	 * (= Budget aller Titel + Budget Titelgruppe)
	 * @return Gesamtbudget
	 * @throws ApplicationServerException
	 * author Mario
	 */	 
	 public float selectTotalAccountBudget(ZVKonto acc) throws ApplicationServerException{
		 float result = 0;
		 Object[] parameters = {new Integer(acc.getId())};
		 try{
			 ResultSet rs = statements.get(162).executeQuery(parameters);
			 while(rs.next()){
				 result += rs.getFloat(1) + rs.getFloat(2);
			 }
		 } catch (SQLException e){
		 		
			 throw new ApplicationServerException(151, e.getMessage());
		 }

		 return result;
	 }
	 
	/**
	 * Ermittelt die Summe der Budgets aller FB-Konten die dem �bergebenen ZV-Konto
	 * durch die Tabelle Kontenzuordnungen zugeordnet sind.
	 * => bei zweckgebundenen ZV-Konten ist das Ergebnis auch die Summe des
	 * bereits verteilten Budgets des ZV-Kontos!!!
	 * @return Budgetsumme aller zugeordneten FB-Konten
	 * @throws ApplicationServerException
	 * author Mario
	 */	 
	 public float selectDistributedAccountBudget(ZVKonto acc) throws ApplicationServerException{
		 float result = 0;
		 Object[] parameters = {new Integer(acc.getId())};
		 try{
			 ResultSet rs = statements.get(164).executeQuery(parameters);
			 while(rs.next()){
				 result += rs.getFloat(2);
			 }
		 } catch (SQLException e){
			 throw new ApplicationServerException(157, e.getMessage());
		 }

		 return result;
	 }
	 
	 /**
	  * gibt ein FBHauptkonto mit der �bergebenen Id zur�ck und sperrt diesen Datensatz f�r die Aktualisierung
	  * @param kontoID - Id des FBHauptkontos
	  * @return FBHauptkonto
	  * @throws ApplicationServerException
	  * author w.flat
	  */
	 public FBHauptkonto selectForUpdateFBHauptkonto(int kontoID) throws ApplicationServerException {
		 FBHauptkonto konto = null;

		 try{
			 Object[] parameters = { new Integer(kontoID) };
			 ResultSet rs = statements.get(58).executeQuery(parameters);

			 if( rs.next() ){
				 konto = new FBHauptkonto( kontoID, rs.getInt(1), new Institut(rs.getInt(2),null,null), rs.getString(3), rs.getString(4), rs.getString(5),
				 		rs.getFloat(6), rs.getFloat(7), rs.getFloat(8), rs.getString(9), !rs.getString(10).equalsIgnoreCase( "0" ), !rs.getString(11).equalsIgnoreCase( "0" ) );
			 }

			 rs.close();
		 } catch (SQLException e){
			 System.out.println(e.getMessage());
			 throw new ApplicationServerException( 1, e.getMessage() );
		 }
		 return konto;
	 }
	 
	 /**
	  * Abfrage aller FBHauptkonten in der Datenbank, die zum angegebenem Institut geh�ren.
	  * @param institut
	  * @return ArrayListe mit FBHauptkonten
	  * @throws ApplicationServerException
	  */
	 public ArrayList selectNoPurposeFBHauptkonten( Institut institut ) throws ApplicationServerException {
		 ArrayList konten = new ArrayList();

		 try{
			 Object[] parameters = { new Integer(institut.getId()) };
			 ResultSet rs = statements.get(60).executeQuery(parameters);
			 rs.last();

			 if ( rs.getRow() > 0 ){
				 rs.beforeFirst();

				 int i = 0;
				 while( rs.next() ){
					 konten.add( new FBHauptkonto( rs.getInt(1), rs.getInt(2), institut, rs.getString(4), rs.getString(5),
												 rs.getString(6), rs.getFloat(7), rs.getFloat(8), rs.getString(9) ) );
				 }
			 }

			 rs.close();
		 } catch (SQLException e){
			 throw new ApplicationServerException( 1, e.getMessage() );
		 }
		 return konten;
	 }
	 
	 /**
	  * gibt alle Kostenarten aus der SQL-Tabelle Kostenarten zur�ck
	  * @return Kostenart-Array
	  * author robert
	  */
	 public Kostenart[] selectKostenarten() throws ApplicationServerException{
			Kostenart[] kostenarten = null;
			try{
				ResultSet rs = statements.get(239).executeQuery();
				rs.last();
				int count = rs.getRow();
				rs.beforeFirst();
				if (count > 0){
					kostenarten = new Kostenart[count];
					int i = 0;
					while (rs.next()){
						kostenarten[i] = new Kostenart(rs.getInt(1), rs.getString(2));
						i++;
					}
				}
				rs.close();
			} catch (SQLException e){
				throw new ApplicationServerException(1, e.getMessage());
			}
		 	return kostenarten;
	 }
	
	/**
	 * f�gt eine Bestellung in die Tabelle Bestellungen ein und liefert die erzeugte Id f�r die Bestellung zur�ck
	 * @param bestellung - Standard-,ASK- und KleinBestellung
	 * @param typ - gibt den Typ der Bestellung an 0 = Standard, 1 = ASK, 2 = Klein
	 * @return Id der Bestellung in der Tabelle Bestellungen
	 * @throws ApplicationServerException
	 * author robert
	 */ 
	public int insertBestellung(Bestellung bestellung) throws ApplicationServerException{
		if(bestellung != null){
			try{
				Object[] parameters = { new Integer(bestellung.getBesteller().getId()), new Integer(bestellung.getAuftraggeber().getId()),
																new Integer(bestellung.getEmpfaenger().getId()), bestellung.getReferenznr(), "" + bestellung.getTyp(),
																"" + bestellung.getPhase(), bestellung.getDatum(), new Integer(bestellung.getZvtitel().getId()), 
																new Integer(bestellung.getFbkonto().getId()), new Float(bestellung.getBestellwert()), new Float(bestellung.getVerbindlichkeiten())};
				statements.get(219).executeUpdate(parameters);
				ResultSet rs = statements.get(219).getGeneratedKeys();

				if (rs.next()) {
					return rs.getInt(1);
				}
			} catch (SQLException e){
				throw new ApplicationServerException(65, e.getMessage());
			}
		}else{
			throw new ApplicationServerException(65);
		}
		return 0;
	}
	
	/**
	 * f�gt eine StandardBestellung in die Tabelle ASK_Standard_Bestellungen ein
	 * @param bestellung - Standardbestellung
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void insertStandardBestellung(StandardBestellung bestellung) throws ApplicationServerException{
		if(bestellung != null){
			try{
				Object[] parameters = { new Integer(bestellung.getId()), bestellung.getBemerkung(), null,
																new Integer(bestellung.getKostenart().getId()), (bestellung.getErsatzbeschaffung() ? "1" : "0"),
																bestellung.getErsatzbeschreibung(), bestellung.getInventarNr(), bestellung.getVerwendungszweck(),
																(bestellung.getPlanvorgabe() ? "1" : "0"), bestellung.getBegruendung()};
				statements.get(250).executeUpdate(parameters);
				
			} catch (SQLException e){
				throw new ApplicationServerException(66, e.getMessage());
			}
		}else{
			throw new ApplicationServerException(66);
		}
	}
	
	/**
	 * gibt eine StandardBestellung mit der bestellerId zur�ck. Angebote und Auswahl sind nicht gesetzt
	 * @param bestellId der StandardBestellung
	 * @return Standardbestellung
	 * @throws ApplicationServerException
	 * author robert
	 */
	public StandardBestellung selectStandardBestellung(int bestellId) throws ApplicationServerException{
		StandardBestellung bestellung = null;

		try{
			Object[] parameters = { new Integer(bestellId) };
			ResultSet rs = statements.get(270).executeQuery(parameters);

			if (rs.next()){
				Kostenart kostenart = new Kostenart(rs.getInt(1), rs.getString(2));
				Benutzer besteller = selectUser(rs.getInt("besteller"));
				Benutzer auftraggeber = selectUser(rs.getInt("auftraggeber"));
				Benutzer empfaenger = selectUser(rs.getInt("empfaenger"));
				ZVTitel zvTitel = selectZVTitel(rs.getInt("zvTitel"));
				FBUnterkonto fbkonto = selectFBKonto(rs.getInt("fbKonto"));
				
				bestellung = new StandardBestellung(kostenart, !rs.getString("ersatzbeschaffung").equalsIgnoreCase("0"), rs.getString("ersatzbeschreibung"),
																						rs.getString("ersatzInventarNr"), rs.getString("verwendungszweck"), !rs.getString("planvorgabe").equalsIgnoreCase("0"), 
																						rs.getString("begruendung"), rs.getString("bemerkungen"), bestellId, 
																						rs.getString("referenzNr"), rs.getDate("datum"), besteller, rs.getString("phase").charAt(0), rs.getString("huelNr"), auftraggeber,
																						empfaenger, zvTitel, fbkonto, rs.getFloat("bestellwert"), rs.getFloat("verbindlichkeiten") );
			}else {
				throw new ApplicationServerException(70);
			}

			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(71,e.getMessage());
		}

		return bestellung;
	}
	
	/**
	 * gibt eine ASKBestellung mit der bestellerId zur�ck. Angebote und Auswahl sind nicht gesetzt
	 * @param bestellId der ASKBestellung
	 * @return ASKbestellung
	 * @throws ApplicationServerException
	 * author robert
	 */
	public ASKBestellung selectASKBestellung(int bestellId) throws ApplicationServerException{
		ASKBestellung bestellung = null;

		try{
			Object[] parameters = { new Integer(bestellId) };
			ResultSet rs = statements.get(325).executeQuery(parameters);

			if (rs.next()){
				Benutzer besteller = selectUser(rs.getInt("besteller"));
				Benutzer auftraggeber = selectUser(rs.getInt("auftraggeber"));
				Benutzer empfaenger = selectUser(rs.getInt("empfaenger"));
				Benutzer swBeauftragter = selectUser(rs.getInt("swBeauftragter"));
				ZVTitel zvTitel = selectZVTitel(rs.getInt("zvTitel"));
				FBUnterkonto fbkonto = selectFBKonto(rs.getInt("fbKonto"));
			
				bestellung = new ASKBestellung(	bestellId, rs.getString("referenzNr"), rs.getString("huelNr"), rs.getDate("datum"), besteller,
																			 	auftraggeber, empfaenger, zvTitel, fbkonto, rs.getFloat("bestellwert"), rs.getFloat("verbindlichkeiten"),
																				rs.getString("phase").charAt(0), null, rs.getString("bemerkungen"), swBeauftragter);
				
			}else {
				throw new ApplicationServerException(70);
			}

			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(97,e.getMessage());
		}

		return bestellung;
	}
	
	/**
	 * w�hlt eine StandardBestellung zum Aktualisieren
	 * @param bestellId
	 * @return StandardBestellung
	 * @throws ApplicationServerException
	 */
	public StandardBestellung selectForUpdateStandardBestellung(int bestellId) throws ApplicationServerException{
		StandardBestellung bestellung = null;

		try{
			Object[] parameters = { new Integer(bestellId), "0" };
			ResultSet rs = statements.get(274).executeQuery(parameters);

			if (rs.next()){
				Kostenart kostenart = new Kostenart(rs.getInt(1), rs.getString(2));
				Benutzer besteller = selectUser(rs.getInt("besteller"));
				Benutzer auftraggeber = selectUser(rs.getInt("auftraggeber"));
				Benutzer empfaenger = selectUser(rs.getInt("empfaenger"));
				ZVTitel zvTitel = selectZVTitel(rs.getInt("zvTitel"));
				FBUnterkonto fbkonto = selectFBKonto(rs.getInt("fbKonto"));
			
				bestellung = new StandardBestellung(kostenart, !rs.getString("ersatzbeschaffung").equalsIgnoreCase("0"), rs.getString("ersatzbeschreibung"),
																						rs.getString("ersatzInventarNr"), rs.getString("verwendungszweck"), !rs.getString("planvorgabe").equalsIgnoreCase("0"), 
																						rs.getString("begruendung"), rs.getString("bemerkungen"), bestellId, 
																						rs.getString("referenzNr"), rs.getDate("datum"), besteller, rs.getString("phase").charAt(0), rs.getString("huelNr"), auftraggeber,
																						empfaenger, zvTitel, fbkonto, rs.getFloat("bestellwert"), rs.getFloat("verbindlichkeiten") );
			}else {
				throw new ApplicationServerException(70);
			}

			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(71,e.getMessage());
		}

		return bestellung;
	}
	
	/**
	 * w�hlt eine ASKBestellung zum Aktualisieren
	 * @param bestellId
	 * @return ASKBestellung
	 * @throws ApplicationServerException
	 */
	public ASKBestellung selectForUpdateASKBestellung(int bestellId) throws ApplicationServerException{
		ASKBestellung bestellung = null;

		try{
			Object[] parameters = { new Integer(bestellId) };
			ResultSet rs = statements.get(326).executeQuery(parameters);

			if (rs.next()){
				Benutzer besteller = selectUser(rs.getInt("besteller"));
				Benutzer auftraggeber = selectUser(rs.getInt("auftraggeber"));
				Benutzer empfaenger = selectUser(rs.getInt("empfaenger"));
				Benutzer swBeauftragter = selectUser(rs.getInt("swBeauftragter"));
				ZVTitel zvTitel = selectZVTitel(rs.getInt("zvTitel"));
				FBUnterkonto fbkonto = selectFBKonto(rs.getInt("fbKonto"));
			
				bestellung = new ASKBestellung(	bestellId, rs.getString("referenzNr"), rs.getString("huelNr"), rs.getDate("datum"), besteller,
																				auftraggeber, empfaenger, zvTitel, fbkonto, rs.getFloat("bestellwert"), rs.getFloat("verbindlichkeiten"),
																				rs.getString("phase").charAt(0), null, rs.getString("bemerkungen"), swBeauftragter);
			}else {
				throw new ApplicationServerException(70);
			}

			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(93,e.getMessage());
		}

		return bestellung;
	}
	
	/**
	 * f�gt eine ASKBestellung in die Tabelle ASK_Standard_Bestellungen ein
	 * @param bestellung - ASKbestellung
	 * @param angebotId - Id des dazugeh�rigen Angebots
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void insertASKBestellung(ASKBestellung bestellung) throws ApplicationServerException{
		if(bestellung != null){
			try{
				Object[] parameters = { new Integer(bestellung.getId()), bestellung.getBemerkung(),  new Integer(bestellung.getSwbeauftragter().getId()),
																null, null, null, null, null, null, null};
				statements.get(250).executeUpdate(parameters);
				
			} catch (SQLException e){
				throw new ApplicationServerException(69, e.getMessage());
			}
		}else{
			throw new ApplicationServerException(69);
		}
	}
	
	/**
	 * f�gt ein Angebot in die Tabelle Angebote ein und liefert eine Id zur�ck
	 * @param angebot
	 * @param bestellungId - Id der eingef�gten Bestellung
	 * @param angenommen - sagt aus ob das Angebot ausgew�hlt wurde
	 * @return Id des Angebots
	 * @throws ApplicationServerException
	 * author robert
	 */
	public int insertAngebot(Angebot angebot, int bestellungId) throws ApplicationServerException{
		if(angebot != null){
			try{
				Object[] parameters = { new Integer(bestellungId), new Integer(angebot.getAnbieter().getId()),
																angebot.getDatum(), (angebot.getAngenommen() ? "1" : "0") };
				statements.get(260).executeUpdate(parameters);
				ResultSet rs = statements.get(260).getGeneratedKeys();

				if (rs.next()) {
					return rs.getInt(1);
				}
			} catch (SQLException e){
				throw new ApplicationServerException(67, e.getMessage());
			}
		}else{
			throw new ApplicationServerException(67);
		}
		return 0;
	}

	/**
	 * f�gt eine Position in die Tabelle Positionen ein
	 * @param position 
	 * @param angebotId - Id des zugeh�rigen Angbots
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void insertPosition(Position position, int angebotId) throws ApplicationServerException{
		if(position != null){
			try{
				Object[] parameters = { new Integer(angebotId), ((position.getInstitut() == null) ? null : new Integer(position.getInstitut().getId())), new Integer(position.getMenge()),
																position.getArtikel(), new Float(position.getEinzelPreis()), new Float(position.getMwst()),
																new Float(position.getRabatt()) };
				statements.get(265).executeUpdate(parameters);
		
			} catch (SQLException e){
				throw new ApplicationServerException(68, e.getMessage());
			}
		}else{
			throw new ApplicationServerException(68);
		}
	}
	
	/**
	 * gibt eine ArrayListe aller Angebote zu einer Bestellung anhand der bestellId zur�ck
	 * @param bestellId - Id der Bestellung
	 * @return ArrayListe von Angeboten
	 * @throws ApplicationServerException
	 * author robert
	 */
	public ArrayList selectAngebote(int bestellId) throws ApplicationServerException {
		ArrayList angebote = new ArrayList();	// Liste f�r die ZVKonten

		try{
			Object[] parameters = { new Integer(bestellId) };
			ResultSet rs = statements.get(261).executeQuery(parameters);
			rs.last();	
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen
			
				while( rs.next() ){		// Solange es n�chste Abfragezeile gibt
					
					Firma anbieter = new Firma( rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
																			rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
																			rs.getString(10), rs.getString(11).equalsIgnoreCase( "1" ), 
																			rs.getString(12).equalsIgnoreCase( "1" ) );
					angebote.add( new Angebot( rs.getInt(13), null, rs.getDate(14), anbieter, !rs.getString(15).equalsIgnoreCase("0")));
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 73, e.getMessage() );
		}
	
		return angebote;
	}
	
	/**
	 * gibt eine ArrayList von Positionen eines Angebots anhand der angebotId
	 * @param angebotId - Id des Angebots
	 * @return ArrayList von Positionen
	 * @throws ApplicationServerException
	 * author robert
	 */
	public ArrayList selectPositionen(int angebotId) throws ApplicationServerException {
		ArrayList positionen = new ArrayList();	// Liste f�r die ZVKonten
		
		try{
			Object[] parameters = { new Integer(angebotId) };
			ResultSet rs = statements.get(266).executeQuery(parameters);
			rs.last();	
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				while( rs.next() ){		// Solange es n�chste Abfragezeile gibt 
					Institut institut = selectInstitute(rs.getInt("institut"));
				  positionen.add( new Position(rs.getInt(1), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getFloat(6),
				  														 rs.getFloat(7), institut, !rs.getString(8).equalsIgnoreCase("0")));
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 74, e.getMessage() );
		}

		return positionen;
	}
	
	/**
	 * pr�ft ob die ReferenzNr der Bestellung schon vorhanden ist
	 * @param referenzNr der Bestellung
	 * @return Anzahl der Bestellungen mit dieser ReferenzNr
	 * @throws ApplicationServerException
	 * author robert
	 */
	public int checkReferenzNr(String referenzNr) throws ApplicationServerException{
	  try{
		  Object[] parameters = { referenzNr };
		    ResultSet rs = statements.get(218).executeQuery(parameters);
			  rs.last();
	
			 return rs.getInt(1);
		 } catch (SQLException e){
			  throw new ApplicationServerException(77, e.getMessage());
		 }
 	}
 
	/**
	 * gibt in der Datenbank vorhandene Bestelungen zur�ck
	 * @param filter (=> Filterung anhand des Bestellungstyps
	 * @return ArrayListe von Bestellungen
	 * @throws ApplicationServerException
	 * author Mario
	 */
	public ArrayList selectBestellungen(int filter) throws ApplicationServerException {
		ArrayList bestellungen = new ArrayList();

		try{
			ResultSet rs;
			if ((filter >= 0)&&(filter<=2)){
				Object[] parameters = { new Integer(filter) };
				rs = statements.get(272).executeQuery(parameters);
			}else{
				rs = statements.get(273).executeQuery();
			}
						
			rs.last();	
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen
			
				while( rs.next() ){		// Solange es n�chste Abfragezeile gibt
					bestellungen.add( new Bestellung(	rs.getInt(1), rs.getDate(2), rs.getString(3).charAt(0), rs.getString(4).charAt(0), 
																						new Benutzer(rs.getString(5),rs.getString(6)), 
																						new Benutzer(rs.getString(7),rs.getString(8)), 
																						new Benutzer(rs.getString(9),rs.getString(10)), 
																						rs.getFloat(11), rs.getFloat(12)));
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 158, e.getMessage() );
		}
	
		return bestellungen;
	}
	
	/**
	 * ermittelt die Bestellungen in Sondierungs- oder Abwicklungsphase eines Haushaltsjahres
	 * @param ID des Haushaltsjahres
	 * @return ArrayListe von Bestellungen
	 * @throws ApplicationServerException
	 * author Mario
	 */
	public ArrayList selectOffeneBestellungen(int haushaltsjahr) throws ApplicationServerException {
		ArrayList bestellungen = new ArrayList();

		try{
			ResultSet rs;
			Object[] parameters = { new Integer(haushaltsjahr) };
			rs = statements.get(301).executeQuery(parameters);
			
						
			rs.last();	
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen
			
				while( rs.next() ){		// Solange es n�chste Abfragezeile gibt
	
													//  Name              Vorname
					Benutzer besteller = new Benutzer(rs.getString(5), rs.getString(6));
													//      Name               Vorname
					Benutzer auftraggeber = new Benutzer(rs.getString(7), rs.getString(8));
															               // ID          Bezeichnung         Kapitel          Titelgruppe      Zweckgebunden
					ZVTitel titel = new ZVTitel(rs.getInt(9), new ZVKonto(rs.getInt(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14).equalsIgnoreCase("1")));
					                                      //  ID           Bezeichnung                   Bezeichnung        Kostenstelle        Hauptkonto
					FBHauptkonto konto = new FBHauptkonto(rs.getInt(15), rs.getString(16), new Institut(rs.getString(17),  rs.getString(18)), rs.getString(19));
														// ID      Datum                Typ                       Phase
					Bestellung b = new Bestellung(	rs.getInt(1), rs.getDate(2), rs.getString(3).charAt(0), rs.getString(4).charAt(0), 
																						// Bestellwert
													besteller, auftraggeber, titel, konto, rs.getFloat(20) );
							
					bestellungen.add( b );
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 165, e.getMessage() );
		}
	
		return bestellungen;
	}
	
 	/**
 	 * aktualisiert eine StandardBestellung angand der bestellId
 	 * @param b - Standardbestellung
 	 * @throws ApplicationServerException
 	 * author robert
 	 */
	public void updateStandardBestellung(StandardBestellung b) throws ApplicationServerException{
		if(b != null){
			try{
				
				Object[] parameters = { (b.getGeloescht() ? "1" : "0"), new Integer(b.getBesteller().getId()), new Integer(b.getAuftraggeber().getId()), new Integer(b.getEmpfaenger().getId()), b.getReferenznr(), b.getHuel(), "" + b.getPhase(),
																b.getDatum(), new Integer(b.getZvtitel().getId()), new Integer(b.getFbkonto().getId()), new Float(b.getBestellwert()), new Float(b.getVerbindlichkeiten()),
																b.getBemerkung(), new Integer(b.getKostenart().getId()), (b.getErsatzbeschaffung() ? "1" : "0"), b.getErsatzbeschreibung(), b.getInventarNr(),
																b.getVerwendungszweck(), (b.getPlanvorgabe() ? "1" : "0"), b.getBegruendung(), null, new Integer(b.getId()) };
				if(statements.get(271).executeUpdate(parameters) == 0)
					throw new ApplicationServerException(78);

			} catch (SQLException e){
				throw new ApplicationServerException(79, e.getMessage());
			}
		}
	}
	
	/**
	 * aktualisiert eine ASKBestellung angand der bestellId
	 * @param b - ASKBestellung
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void updateASKBestellung(ASKBestellung b) throws ApplicationServerException{
		if(b != null){
			try{
			
				Object[] parameters = { (b.getGeloescht() ? "1" : "0"), new Integer(b.getBesteller().getId()), new Integer(b.getAuftraggeber().getId()), new Integer(b.getEmpfaenger().getId()), b.getReferenznr(), b.getHuel(), "" + b.getPhase(),
																b.getDatum(), new Integer(b.getZvtitel().getId()), new Integer(b.getFbkonto().getId()), new Float(b.getBestellwert()), new Float(b.getVerbindlichkeiten()),
																b.getBemerkung(), null, null, null, null, null, null, null, new Integer(b.getSwbeauftragter().getId()), new Integer(b.getId()) };
				if(statements.get(271).executeUpdate(parameters) == 0)
					throw new ApplicationServerException(78);

			} catch (SQLException e){
				throw new ApplicationServerException(94, e.getMessage());
			}
		}
	}
	
	/**
	 * l�scht alle Angebote zu einer Bestellung. Zugeh�rige Positionen m�ssen vorher
	 * gel�scht werden.
	 * @param bestellId
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void deleteAngebote( int bestellId ) throws ApplicationServerException {
		try{
			Object[] parameters = {new Integer(bestellId)};
			statements.get(263).executeUpdate(parameters);
		} catch (SQLException e){
			throw new ApplicationServerException( 80, e.getMessage() );
		}
	}
	
	/**
	 * l�scht ein Angebot anhand der Id. VORSICHT: vorher dazugeh�rige Positionen l�schen
	 * @param angebotId Id des Angebots
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void deleteAngebot( int angebotId ) throws ApplicationServerException {
		try{
			Object[] parameters = {new Integer(angebotId)};
			statements.get(285).executeUpdate(parameters);
		} catch (SQLException e){
			throw new ApplicationServerException( 87, e.getMessage() );
		}
	}
	
	/**
	 * l�scht alle Positionen aller Angebote einer Bestellung
	 * @param bestellId
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void deletePositions(int bestellId) throws ApplicationServerException {
		try{
			Object[] parameters = {new Integer(bestellId)};
			statements.get(267).executeUpdate(parameters);
		} catch (SQLException e){
			throw new ApplicationServerException( 86, e.getMessage() );
		}
	}
	
	/**
	 * l�scht alle Positionen eines Angebots
	 * @param angebotId - Id des Angebots
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void deleteOfferPositions(int angebotId) throws ApplicationServerException {
		try{
			Object[] parameters = {new Integer(angebotId)};
			statements.get(280).executeUpdate(parameters);
		} catch (SQLException e){
			throw new ApplicationServerException( 86, e.getMessage() );
		}
	}
	
	/**
	 * l�scht eine Position anhand der Id
	 * @param id der Position
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void deletePosition(int id) throws ApplicationServerException {
		try{
			Object[] parameters = {new Integer(id)};
			statements.get(269).executeUpdate(parameters);
		} catch (SQLException e){
			throw new ApplicationServerException( 88, e.getMessage() );
		}
	}
	
	/**
	 * aktualisiert ein Angebot
	 * @param angebot
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void updateAngebot(Angebot angebot) throws ApplicationServerException{
		if(angebot != null){
			try{
				Object[] parameters = { new Integer(angebot.getAnbieter().getId()), angebot.getDatum(), 
																(angebot.getAngenommen() ? "1" : "0"), new Integer(angebot.getId()) };
				if(statements.get(264).executeUpdate(parameters) == 0)
					throw new ApplicationServerException(82);

			} catch (SQLException e){
				throw new ApplicationServerException(81, e.getMessage());
			}
		}
	}
	
	/**
	 * aktualisiert eine Position
	 * @param position
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void updatePosition(Position p) throws ApplicationServerException{
		if(p != null){
			try{
				Object[] parameters = { ((p.getInstitut() == null) ? null : new Integer(p.getInstitut().getId())), new Integer(p.getMenge()), p.getArtikel(),
																new Float(p.getEinzelPreis()), new Float(p.getMwst()), new Float(p.getRabatt()),
																(p.getBeglichen() ? "1" : "0"), new Integer(p.getId()) };
				if(statements.get(268).executeUpdate(parameters) == 0)
					throw new ApplicationServerException(83);

			} catch (SQLException e){
				throw new ApplicationServerException(84, e.getMessage());
			}
		}
	}
	
	/**
	 * Einen Beleg in die Datenbank einf�gen.
	 * @param bestellung = Id der Bestellung, zu der dieser Beleg geh�rt. 
	 * @param beleg = Beleg der eingef�gt werden soll.
	 * @return Id des eingef�gten Belges.  
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int insertBeleg(int bestellung, Beleg beleg) throws ApplicationServerException {
		if(beleg == null)
			return 0;
		try {
			// Prameter f�r das SQL-Statement
			Object[] parameters = {new Integer(bestellung), "" + beleg.getNummer(), new Integer(beleg.getFirma().getId()), 
									beleg.getArtikel(), new Float(beleg.getSumme())};
			// SQL-Statement mit der Nummer 276 ausf�hren
			statements.get(276).executeUpdate(parameters);
			ResultSet rs = statements.get(276).getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} catch(SQLException e) {
			throw new ApplicationServerException(1, e.getMessage());
		}
	} 
	
	/**
	 * Abfrage der Id eines bestimmten Belegs. <br>
	 * Beleg von einer bestimmten Bestellung und mit einer bestimmten Beleg-Nummer.
	 * @param bestellung = Id der Bestellung, zu der dieser Beleg geh�rt. 
	 * @param beleg = Beleg der �berpr�ft werden soll. 
	 * @return Id des Belegs. 
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int existsBeleg(int bestellung, Beleg beleg) throws ApplicationServerException {
		if(beleg == null)
			return 0;
		try {
			// Prameter f�r das SQL-Statement
			Object[] parameters = {new Integer(bestellung), "" + beleg.getNummer()};
			// SQL-Statement mit der Nummer 279 ausf�hren
			ResultSet rs = statements.get(279).executeQuery(parameters);
			rs.last();	// Auf die letzte Zeile springen
			if( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0, dann existiert der Beleg
				rs.beforeFirst();	// Vor die erste Zeile springen
				rs.next();			// N�chste Zeile
				return rs.getInt(1);// Id des Belegs
			}
			return 0;
		} catch(SQLException e) {
			throw new ApplicationServerException(1, e.getMessage());
		}
	} 

	/**
	 * L�schen aller Belege einer bestimmten Bestellung. 
	 * @param bestellung = Id der Bestellung, von der die Belege gel�scht werden sollen. 
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int deleteBelege(int bestellung) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters ={ new Integer(bestellung)};
			// SQL-Statement ausf�hren
			return statements.get(278).executeUpdate(parameters);	// Zeilenanzahl der gel�schten Belege
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	} 

	/**
	 * Abfrage aller Belege einer bestimmten Bestellung. 
	 * @param bestellung = Beleg der eingef�gt werden soll.  
	 * @return Id des Belegs. 
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public ArrayList selectBelege(int bestellung) throws ApplicationServerException {
		ArrayList belege = new ArrayList();		// Liste mit Belegen
		try {
			// Prameter f�r das SQL-Statement
			Object[] parameters = {new Integer(bestellung)};
			// SQL-Statement mit der Nummer 277 ausf�hren
			ResultSet rs = statements.get(277).executeQuery(parameters);
			rs.last();	// Auf die letzte Zeile springen
			if( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0, dann existiert der Beleg
				rs.beforeFirst();	// Vor die erste Zeile springen
				while( rs.next() ){		// Wenn es noch Zeilen gibt
					// Neuen Beleg erzeugen und die Liste einf�gen
					belege.add( new Beleg( rs.getInt(1), (new Integer(rs.getString(3))).intValue(), 
												selectFirma(rs.getInt(4)), rs.getString(5), rs.getFloat(6) ) );
				}
			}
			rs.close();					// Die Abfrage schlie�en
		} catch(SQLException e) {
			throw new ApplicationServerException(1, e.getMessage());
		}
		
		return belege;
	} 
	
	/**
	 * Abfrage von allen nicht gel�schten Kleinbestellungen. 
	 * @return Liste mit Kleinbestellungen. 
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public ArrayList selectKleinbestellungen() throws ApplicationServerException {
		ArrayList bestellungen = new ArrayList();		// Liste mit Bestellungen
		KleinBestellung temp;
		try {
			// SQL-Statement mit der Nummer 292 ausf�hren
			ResultSet rs = statements.get(292).executeQuery();
			rs.last();	// Auf die letzte Zeile springen
			if( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0, dann existiert der Beleg
				rs.beforeFirst();	// Vor die erste Zeile springen
				while( rs.next() ){		// Wenn es noch Zeilen gibt
					// Neuen Beleg erzeugen und die Liste einf�gen
					bestellungen.add( temp = new KleinBestellung( rs.getInt(1), rs.getDate(4), selectUser(rs.getInt(2)), 
												selectUser(rs.getInt(3)), selectZVTitel(rs.getInt(5)), 
												selectFBKonto(rs.getInt(6)), rs.getFloat(7), rs.getString(8),
												rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), null ) );
				}
			}
			rs.close();					// Die Abfrage schlie�en
		} catch(SQLException e) {
			throw new ApplicationServerException(1, e.getMessage());
		}
		
		return bestellungen;
	} 

	/**
	 * Abfrage von allen gel�schten Kleinbestellungen. 
	 * @return Liste mit Kleinbestellungen. 
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public ArrayList selectDelKleinbestellungen() throws ApplicationServerException {
		ArrayList bestellungen = new ArrayList();		// Liste mit Bestellungen
		KleinBestellung temp;
		
		try {
			// SQL-Statement mit der Nummer 293 ausf�hren
			ResultSet rs = statements.get(293).executeQuery();
			rs.last();	// Auf die letzte Zeile springen
			if( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0, dann existiert der Beleg
				rs.beforeFirst();	// Vor die erste Zeile springen
				while( rs.next() ){		// Wenn es noch Zeilen gibt
					// Neuen Beleg erzeugen und die Liste einf�gen
					bestellungen.add( temp = new KleinBestellung( rs.getInt(1), rs.getDate(4), selectUser(rs.getInt(2)), 
												selectUser(rs.getInt(3)), selectZVTitel(rs.getInt(6)), 
												selectFBKonto(rs.getInt(5)), rs.getFloat(7), rs.getString(8),
												rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), null ) );
					temp.setGeloescht(true);
				}
			}
			rs.close();					// Die Abfrage schlie�en
		} catch(SQLException e) {
			throw new ApplicationServerException(1, e.getMessage());
		}
		
		return bestellungen;
	} 
	
	/**
	 * Eine Kleinbestellung in der Datenbank aktualisieren.
	 * @param Kleinbestellung, die aktualisiert werden soll.
	 * @return Id von der Kleinbestellung, die aktulisiert wurde.
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public int updateKleinbestellung( KleinBestellung bestellung ) throws ApplicationServerException {
		try{
			// Parameter f�r das SQL-Statement
			Object[] parameters = { new Integer(bestellung.getBesteller().getId()), 
									new Integer(bestellung.getAuftraggeber().getId()), 
									new Integer(bestellung.getEmpfaenger().getId()), 
									bestellung.getReferenznr(), bestellung.getHuel(), new String("" + bestellung.getPhase()), 
									bestellung.getDatum(), new Integer(bestellung.getZvtitel().getId()), 
									new Integer(bestellung.getFbkonto().getId()),
									new Float(bestellung.getBestellwert()), new Float(0), 
									new String(bestellung.getGeloescht() ? "1" : "0"), bestellung.getProjektNr(), 
									bestellung.getVerwendungszweck(), bestellung.getLabor(), bestellung.getKartei(), 
									bestellung.getVerzeichnis(), new Integer(bestellung.getId()) };
			// SQL-Satement mit der Nummer 291 ausf�hren
			statements.get(291).executeUpdate(parameters);
			return bestellung.getId();	// Id von der aktualisierung KleinBestellung
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	/**
	 * Eine neue KleinBestellung erstellen.
	 * @param KleinBestellung, die eingef�gt werden soll. 
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public void insertKleinbestellung( KleinBestellung bestellung ) throws ApplicationServerException {
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = {new Integer(bestellung.getId()), bestellung.getProjektNr(),
									bestellung.getVerwendungszweck(), bestellung.getLabor(), 
									bestellung.getKartei(), bestellung.getVerzeichnis()};
			// SQL-Statement ausf�hren
			statements.get(290).executeUpdate(parameters);
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Eine KleinBestellung zum Aktualisieren ausw�hlen.
	 * @param KleinBestellung, die aktualisiert werden soll. 
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public void selectForUpdateKleinbestellung( KleinBestellung bestellung ) throws ApplicationServerException {
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = {new Integer(bestellung.getId())};
			// SQL-Statement ausf�hren
			statements.get(294).executeQuery(parameters);
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	/**
	 * Eine Kleinbestellung mit einer bestimmter Id abfragen. 
	 * @param Id des Kontos. 
	 * @return Kleinbestellung die abgefragt wurde. 
	 * @throws ApplicationServerException
	 * author w.flat
	 */	
	public KleinBestellung selectKleinbestellung( int id ) throws ApplicationServerException {
		KleinBestellung bestellung = null;
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = {new Integer(id)};
			// SQL-Statement mit der Nummer 295 ausf�hren
			ResultSet rs = statements.get(295).executeQuery(parameters);
			rs.last();	// Auf die letzte Zeile springen
			if( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0, dann existiert der Beleg
				rs.beforeFirst();	// Vor die erste Zeile springen
				rs.next();
				// Neuen Beleg erzeugen und die Liste einf�gen
				bestellung = new KleinBestellung( rs.getInt(1), rs.getDate(4), selectUser(rs.getInt(2)), 
											selectUser(rs.getInt(3)), selectZVTitel(rs.getInt(5)), 
											selectFBKonto(rs.getInt(6)), rs.getFloat(7), rs.getString(9),
											rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), 
											selectBelege(rs.getInt(1)) );
				bestellung.setPhase(rs.getString(8).charAt(0));
			}
			rs.close();					// Die Abfrage schlie�en
			return bestellung;
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	/**
	 * Positionen zum Aktualisieren ausw�hlen
	 * @param angebotId - Id des Angebots
	 * @return ArrayList mit Positionen
	 * @throws ApplicationServerException
	 * author robert
	 */
	public ArrayList selectForUpdatePositionen(int angebotId) throws ApplicationServerException {
		ArrayList positionen = new ArrayList();	// Liste f�r die ZVKonten
		
		try{
			Object[] parameters = { new Integer(angebotId) };
			ResultSet rs = statements.get(281).executeQuery(parameters);
			rs.last();	
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				while( rs.next() ){		// Solange es n�chste Abfragezeile gibt 
					Institut institut = selectInstitute(rs.getInt("institut"));
				  positionen.add( new Position(rs.getInt(1), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getFloat(6),
																		 rs.getFloat(7), institut, !rs.getString(8).equalsIgnoreCase("0")));
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 74, e.getMessage() );
		}

		return positionen;
	}
	
	/**
	 * Angebote zum Aktualisieren ausw�hlen
	 * @param bestellId
	 * @return ArrayList mit Angeboten
	 * @throws ApplicationServerException
	 * author robert
	 */
	public ArrayList selectForUpdateAngebote(int bestellId) throws ApplicationServerException {
		ArrayList angebote = new ArrayList();	// Liste f�r die ZVKonten

		try{
			Object[] parameters = { new Integer(bestellId) };
			ResultSet rs = statements.get(286).executeQuery(parameters);
			rs.last();	
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen
			
				while( rs.next() ){		// Solange es n�chste Abfragezeile gibt
					
					Firma anbieter = new Firma( rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
																			rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
																			rs.getString(10), rs.getString(11).equalsIgnoreCase( "1" ), 
																			rs.getString(12).equalsIgnoreCase( "1" ) );
					angebote.add( new Angebot( rs.getInt(13), null, rs.getDate(14), anbieter, !rs.getString(15).equalsIgnoreCase("0")));
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 73, e.getMessage() );
		}
	
		return angebote;
	}
	
	/**
	 * l�scht eine Bestellung aus der Tabelle Bestellungen. 
	 * VORSICHT ! Vorher die Bestellungen ASK, Standard bzw. Klein l�schen.
	 * @param bestellId - Id der Bestellung
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void deleteBestellung(int bestellId) throws ApplicationServerException{
		try{
			Object[] parameters = { new Integer(bestellId) };
			statements.get(300).executeUpdate(parameters);

		} catch (SQLException e){
			throw new ApplicationServerException(91, e.getMessage());
		}
	}
	
	/**
	 * l�scht eine ASK- bzw. StandardBestellung aus der Tabelle ASK_Standard_Bestellungen.
	 * VORSICHT ! Vorher alle Angebote l�schen.
	 * @param bestellId - Id der Bestellung
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void deleteASK_Standard_Bestellung(int bestellId) throws ApplicationServerException{
		try{
			Object[] parameters = { new Integer(bestellId) };
			statements.get(251).executeUpdate(parameters);

		} catch (SQLException e){
			throw new ApplicationServerException(92, e.getMessage());
		}
	}
	
	/**
	 * f�gt eine neue Buchung ein
	 * @param b - Buchung
	 * @throws ApplicationServerException
	 */
	public void insertBuchung(Buchung b) throws ApplicationServerException{
		try{
			Object[] parameters = { 
					b.getTimestamp(),
					new Integer(b.getBenutzer().getId()), 
					"" + b.getTyp(), 
					b.getBeschreibung(),
					(b.getBestellung()== null) ? null : new Integer(b.getBestellung().getId()),
					(b.getZvKonto1() == null) ? null : new Integer(b.getZvKonto1().getId()), 
					new Float(b.getBetragZvKonto1()),
					(b.getZvKonto2() == null) ? null : new Integer(b.getZvKonto2().getId()), 
					new Float(b.getBetragZvKonto2()), 		
					(b.getZvTitel1() == null) ? null : new Integer(b.getZvTitel1().getId()), 
					new Float(b.getBetragZvTitel1()), 
					(b.getZvTitel2() == null) ? null : new Integer(b.getZvTitel2().getId()), 
					new Float(b.getBetragZvTitel2()),
					(b.getFbKonto1() == null) ? null : new Integer(b.getFbKonto1().getId()), 
					new Float(b.getBetragFbKonto1()),
					(b.getFbKonto2() == null) ? null : new Integer(b.getFbKonto2().getId()), 
					new Float(b.getBetragFbKonto2()) };
			statements.get(223).executeUpdate(parameters);
			
		} catch (SQLException e){
			throw new ApplicationServerException(110, e.getMessage());
		}
	}

	/**
	 * setzt den Summand auf die existierenden Vormerkungen des jeweiligen FBKontos bzw.
	 * ZVTitels
	 * @param fbKonto
	 * @param zvTitel
	 * @param summand
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void updateVormerkungen(FBUnterkonto fbKonto, ZVUntertitel zvTitel, float summand) throws ApplicationServerException{
		try{
			Object[] parameters = { new Float(summand), new Float(summand), new Integer(fbKonto.getId()), new Integer(zvTitel.getId()) };
			if(statements.get(310).executeUpdate(parameters) == 0)
				throw new ApplicationServerException(99);

		} catch (SQLException e){
			throw new ApplicationServerException(98, e.getMessage());
		}
	}
	
	public float getAvailableTgrBudget(int zvKontoID) throws ApplicationServerException{
		float budget = 0.00f;
		
		try{
			Object[] parameters = { new Integer(zvKontoID) };
			ResultSet rs = statements.get(165).executeQuery(parameters);
			rs.last();	
			if ( rs.getRow() > 0 ) {// Konto vorhanden
				budget = rs.getFloat(1);
			}
			rs.close();// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 159, e.getMessage() );
		}
		return budget;
	}

	public float getTgrExpensesForOrder (int orderID) throws ApplicationServerException{
		float expenses = 0.00f;
		
		try{
			Object[] parameters = { new Integer(orderID) };
			ResultSet rs = statements.get(224).executeQuery(parameters);
			rs.last();	
			if ( rs.getRow() > 0 ) {// Bestellung vorhanden
				expenses = -rs.getFloat(1);
			}
			rs.close();// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 159, e.getMessage() );
		}
		return expenses;
	}
	
	public void updateAccountStates(ZVUntertitel titel, float tgrEntry, float titelEntry, FBUnterkonto konto, float accEntry) throws ApplicationServerException{
		int zvKontoID;
		if(titel instanceof ZVTitel)
			zvKontoID = ((ZVTitel)titel).getZVKonto().getId();
		else
			zvKontoID = titel.getZVTitel().getZVKonto().getId();
		
		try{
			Object[] parameters = { new Float(accEntry), new Float(accEntry), new Float(tgrEntry), new Float(tgrEntry + titelEntry), new Float(titelEntry), 
									new Integer(konto.getId()), new Integer(zvKontoID), new Integer(titel.getId()) };
			if(statements.get(311).executeUpdate(parameters) == 0)
				throw new ApplicationServerException(99);

		} catch (SQLException e){
			throw new ApplicationServerException(98, e.getMessage());
		}
	}
	
	/**
	 * gibt eine ArrayList von ArrayList f�r den Report7 zur�ck. N�here Infos in gui.Reports Klasse
	 * @return Report 7 als ArrayListe
	 * @throws ApplicationServerException
	 */
	public ArrayList selectReport7(Date von, Date bis) throws ApplicationServerException {
		ArrayList report = new ArrayList();	// Liste f�r die ZVKonten

		try{
			Object[] parameters = {von, bis};
			ResultSet rs = statements.get(335).executeQuery(parameters); 
			rs.last();	
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen
		
				while( rs.next() ){		// Solange es n�chste Abfragezeile gibt
					ArrayList row = new ArrayList();
					row.add(rs.getString(1)); 						// Institut
					row.add(rs.getString(2)); 						// ZV-Konto
					row.add(new Float(rs.getFloat(3)));		// Ausgaben
					row.add((rs.getString(4) == null ? "" : rs.getString(4)));							// FBI-Schl�sselnummer
					row.add((rs.getString(5) == null ? "" : rs.getString(5)));							// H�l-Nr
					row.add(rs.getString(6));							// Typ
					row.add(rs.getDate(7));								// Datum
					row.add(rs.getString(8));							// Status
					row.add(new Integer(rs.getInt(9)));		// Id Bestellung
					
					report.add( row );
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 102, e.getMessage() );
		}

		return report;
	}
	
	/**
	 * gibt eine ArrayList von ArrayList f�r den Report8 zur�ck. N�here Infos in gui.Reports Klasse
	 * @return Report 8 als ArrayListe
	 * @throws ApplicationServerException
	 */
	public ArrayList selectReport8(Date von, Date bis) throws ApplicationServerException {
		ArrayList report = new ArrayList();	// Liste f�r die ZVKonten

		try{
			Object[] parameters = {new Timestamp(von.getTime()), new Timestamp(bis.getTime())};
			ResultSet rs = statements.get(336).executeQuery(parameters); 
			rs.last();	
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen
	
				while( rs.next() ){		// Solange es n�chste Abfragezeile gibt
					ArrayList row = new ArrayList();
					row.add(rs.getString(1)); 						// Institut
					row.add(rs.getString(2)); 						// FB-Konto
					row.add(new Float(rs.getFloat(3)));		// Einnahmen
					
					report.add( row );
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 103, e.getMessage() );
		}

		return report;
	}
	
	/**
	 * gibt eine ArrayList von ArrayList f�r den Report6 zur�ck. N�here Infos in gui.Reports Klasse
	 * @return Report 6 als ArrayListe
	 * @throws ApplicationServerException
	 */
	public ArrayList selectReport6(Date von, Date bis) throws ApplicationServerException {
		ArrayList report = new ArrayList();	// Liste f�r die ZVKonten

		try{
			Object[] parameters = {von, bis};
			ResultSet rs = statements.get(337).executeQuery(parameters); 
			rs.last();	
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen

				while( rs.next() ){		// Solange es n�chste Abfragezeile gibt
					ArrayList row = new ArrayList();
					row.add(rs.getString(1)); 						// Institut
					row.add(rs.getString(2)); 						// ZV-Konto
					row.add(new Float(rs.getFloat(3)));		// Ausgaben
				
					report.add( row );
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 104, e.getMessage() );
		}

		return report;
	}
	
	/**
	 * gibt eine ArrayList von ArrayList f�r den Report5 zur�ck. N�here Infos in gui.Reports Klasse
	 * @return Report 5 als ArrayListe
	 * @throws ApplicationServerException
	 */
	public ArrayList selectReport5(Date von, Date bis) throws ApplicationServerException {
		ArrayList report = new ArrayList();	// Liste f�r die ZVKonten

		try{
			Object[] parameters = {new Timestamp(von.getTime()), new Timestamp(bis.getTime())};
			ResultSet rs = statements.get(338).executeQuery(parameters); 
			rs.last();	
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen

				while( rs.next() ){		// Solange es n�chste Abfragezeile gibt
					ArrayList row = new ArrayList();
					row.add(rs.getString(1)); 						// zvKonto
					row.add(rs.getString(2)); 						// institut
					row.add(new Float(rs.getFloat(3)));		// ausgaben
					row.add(new Float(rs.getFloat(4)));		// kontostand
			
					report.add( row );
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 105, e.getMessage() );
		}

		return report;
	}
	
	/**
	 * gibt eine ArrayList von ArrayList f�r den Report4 zur�ck. N�here Infos in gui.Reports Klasse
	 * @return Report 4 als ArrayListe
	 * @throws ApplicationServerException
	 */
	public ArrayList selectReport4(Date von, Date bis) throws ApplicationServerException {
		ArrayList report = new ArrayList();	// Liste f�r die ZVKonten

		try{
			Object[] parameters = {new Timestamp(von.getTime()), new Timestamp(bis.getTime())};
			ResultSet rs = statements.get(339).executeQuery(parameters); 
			rs.last();	
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen

				while( rs.next() ){		// Solange es n�chste Abfragezeile gibt
					ArrayList row = new ArrayList();
					row.add(rs.getString(1)); 						// FB-Konto
					row.add(rs.getString(2)); 						// ZV-Konto
					row.add(new Float(rs.getFloat(3)));		// Ausgaben (FB-Konto)
		
					report.add( row );
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 106, e.getMessage() );
		}

		return report;
	}
	
	/**
	 * gibt eine ArrayList von ArrayList f�r den Report3 zur�ck. N�here Infos in gui.Reports Klasse
	 * @return Report 3 als ArrayListe
	 * @throws ApplicationServerException
	 */
	public ArrayList selectReport3(Date von, Date bis) throws ApplicationServerException {
		ArrayList report = new ArrayList();	// Liste f�r die ZVKonten

		try{
			Object[] parameters = {new Timestamp(von.getTime()), new Timestamp(bis.getTime()), new Timestamp(von.getTime()), new Timestamp(bis.getTime())};
			ResultSet rs = statements.get(340).executeQuery(parameters); 
			rs.last();	
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen

				while( rs.next() ){		// Solange es n�chste Abfragezeile gibt
					ArrayList row = new ArrayList();
					row.add(rs.getString(1)); 						// Institut
					row.add(rs.getString(2)); 						// FB-Konto
					row.add(new Float(rs.getFloat(3))); 	// verteilte Mittel
					row.add(new Float(rs.getFloat(4)));		// Ausgaben 
					row.add(new Float(rs.getFloat(5)));		// Kontostand
	
					report.add( row );
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 107, e.getMessage() );
		}

		return report;
	}
	
	/**
	 * gibt eine ArrayList von ArrayList f�r den Report2 zur�ck. N�here Infos in gui.Reports Klasse
	 * @return Report 2 als ArrayListe
	 * @throws ApplicationServerException
	 */
	public ArrayList selectReport2(Date von, Date bis) throws ApplicationServerException {
		ArrayList report = new ArrayList();	// Liste f�r die ZVKonten

//		try{
//			ResultSet rs = statements.get(342).executeQuery(); 
//			rs.last();	
//			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0
//				rs.beforeFirst();		// Vor die erste Zeile springen
//
//				while( rs.next() ){		// Solange es n�chste Abfragezeile gibt
//					ArrayList row = new ArrayList();
////					row.add(rs.getString(1)); 						// Institut
////					row.add(rs.getString(2)); 						// FB-Konto
////					row.add(new Float(rs.getFloat(3))); 	// verteilte Mittel
////					row.add(new Float(rs.getFloat(4)));		// Ausgaben 
////					row.add(new Float(rs.getFloat(5)));		// Kontostand
//
//					report.add( row );
//				}
//			}
//			rs.close();		// Abfrage schlie�en
//		} catch (SQLException e){
//			throw new ApplicationServerException( 108, e.getMessage() );
//		}

		return report;
	}
	
	/**
	 * gibt eine ArrayList von ArrayList f�r den Report1 zur�ck. N�here Infos in gui.Reports Klasse
	 * @return Report 1 als ArrayListe
	 * @throws ApplicationServerException
	 */
	public ArrayList selectReport1(Date von, Date bis) throws ApplicationServerException {
		ArrayList report = new ArrayList();	// Liste f�r die ZVKonten

		try{
			Object[] parameters = {new Timestamp(von.getTime()), new Timestamp(bis.getTime()), new Timestamp(von.getTime()), new Timestamp(bis.getTime())};
			ResultSet rs = statements.get(342).executeQuery(parameters); 
			rs.last();	
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen

				while( rs.next() ){		// Solange es n�chste Abfragezeile gibt
					ArrayList row = new ArrayList();
					row.add(rs.getString(1)); 						// ZV-Konto
					row.add(new Float(rs.getFloat(2))); 	// zugewiesene Mittel
					row.add(new Float(rs.getFloat(3)));		// Ausgaben 
					row.add(new Float(rs.getFloat(4)));		// Kontostand

					report.add( row );
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException( 109, e.getMessage() );
		}

		return report;
	}

	/**
	 * Aktualisieren der Betr�ge auf dem ZVTitel und FBKonto bei der Stornierung einer Kleinbestellung. 
	 * @param titel = Der Titel der bei der Bestellung angegeben wurde. 
	 * @param konto = FBkonto, das bei der Bestellung angegeben wurde. 
	 * @param betrag = Der Betrag, den der ZVTitel und das FBKonto bekommt. 
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public void updateAccountStates(ZVKonto zvKonto, ZVUntertitel zvTitel, FBUnterkonto fbKonto, 
							float betragZvKonto, float betragZvTitel, float betragFbKonto) throws ApplicationServerException{
		try{
			Object[] parameters = { new Float(betragZvKonto), new Float(betragZvTitel), new Float(betragFbKonto), 
									new Integer(zvKonto.getId()), new Integer(zvTitel.getId()), new Integer(fbKonto.getId()) };
			if(statements.get(312).executeUpdate(parameters) == 0)
				throw new ApplicationServerException(99);

		} catch (SQLException e){
			throw new ApplicationServerException(98, e.getMessage());
		}
	}

	/**
	 * Abfrage einer Buchung von einer bestimmten Bestellung mit einem bestimmten Typ. 
	 * @param bestellungsId = Id von der Bestellung, zu der die Buchung geh�rt. 
	 * @param typ = Der Typ der Buchung. 
	 * @return Buchung, die ermittelt wurde
	 * @throws ApplicationServerException
	 * author w.flat
	 */
	public Buchung selectBuchung(int bestellungsId, String typ) throws ApplicationServerException{
		Buchung buchung = null;
		try{
			Object[] parameters = { new Integer(bestellungsId), new String(typ) };
			ResultSet res = statements.get(225).executeQuery(parameters);
			res.last();
			if(res.getRow() > 0) {
				buchung = new Buchung(selectUser(res.getInt(2)), res.getString(3), selectKleinbestellung(res.getInt(5)),
										selectZVKonto(res.getInt(6)), res.getFloat(7), 
										selectZVTitel(res.getInt(8)), res.getFloat(9), 
										selectFBKonto(res.getInt(12)), res.getFloat(13) );
			}
			return buchung;
		} catch (SQLException e){
			throw new ApplicationServerException(98, e.getMessage());
		}
	}
	
	/**
	 * gibt eine alle Logs aus der Tabelle Logs in einem bestimmten Zeitraum
	 * @param von - Datum f�r den Startpunkt
	 * @param bis - Datum f�r den Endpunkt
	 * @return Arraylist mit allen Logs-Eintr�gen
	 * @throws ApplicationServerException
	 */
	public ArrayList selectLogList(Date von, Date bis) throws ApplicationServerException{
		ArrayList list = new ArrayList();
		try{
			Object[] parameters = { new Timestamp(von.getTime()), new Timestamp(bis.getTime()) };
			ResultSet rs = statements.get(343).executeQuery(parameters);
			rs.last();	
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen gr��er als 0
				rs.beforeFirst();		// Vor die erste Zeile springen
	
				while( rs.next() ){		// Solange es n�chste Abfragezeile gibt
					ArrayList row = new ArrayList();
					Benutzer b = selectUser(rs.getInt(2));
					
					row.add(new Date(rs.getTimestamp(1).getTime())); 		// Datum
					row.add(b.getName() + ", " + b.getVorname()); 								// Benutzer
					row.add(rs.getString(3));		// Typ
					row.add(rs.getString(4));		// Beschreibung
					
					list.add( row );
				}
			}
			rs.close();		// Abfrage schlie�en
		} catch (SQLException e){
			throw new ApplicationServerException(111, e.getMessage());
		}
		return list;
	}
	
	/**
	 * f�gt eine neue Log in die Tabelle Logs ein
	 * @param type - 0 = Add, 1 = Set, 2 = Del, 3 = buche
	 * @param beschreibung - (Text der �nderung)
	 * @throws ApplicationServerException
	 */
	public void insertLog(int type, String beschreibung) throws ApplicationServerException{
		try{
			Object[] parameters = {"" + type, beschreibung };
			statements.get(344).executeUpdate(parameters);
		
		} catch (SQLException e){
			throw new ApplicationServerException(112, e.getMessage());
		}
	}

	/**
	 * Portiert die ZVKonten eines bestimmten Haushaltsjahres in eine tempor�re Tabelle
	 * @param haushaltsjahr = ID
	 * @throws ApplicationServerException
	 */
	public void createAsSelectTempZvKontenTab (int haushaltsjahr) throws ApplicationServerException{
		try{
			Object[] parameters = {new Integer (haushaltsjahr) };
			statements.get(166).executeUpdate(parameters);
		
		} catch (SQLException e){
			throw new ApplicationServerException(166, e.getMessage());
		}
	}

	/**
	 * Portiert die ZVKontentitel eines bestimmten Haushaltsjahres in eine tempor�re Tabelle
	 * @param haushaltsjahr = ID
	 * @throws ApplicationServerException
	 */
	public void createAsSelectTempZvKontentitelTab (int haushaltsjahr) throws ApplicationServerException{
		try{
			Object[] parameters = {new Integer (haushaltsjahr) };
			statements.get(167).executeUpdate(parameters);
		
		} catch (SQLException e){
			throw new ApplicationServerException(167, e.getMessage());
		}
	}

	public boolean existsPortedZvAccount(int id) throws ApplicationServerException{
		try{
			// Parameter f�r das SQL-Statement festlegen
			Object[] parameters = {new Integer(id)};
			// SQL-Statement ausf�hren
			ResultSet rs = statements.get(128).executeQuery(parameters);
			rs.last();	// Auf die letzte Zeile springen
			if( rs.getRow() > 0 ) {	
				return true;
			}else return false;
		} catch (SQLException e) {
			throw new ApplicationServerException(168, e.getMessage());
		}
	
	}
	
	public int insertAsSelectZvKonto (int acc, int year, boolean wBudget) throws ApplicationServerException{
		try{
			PreparedStatementWrapper ps;
			ResultSet rs;
			
			Object[] parameters = { new Integer(year), new Integer(acc)};
			
			if (wBudget){
				ps = statements.get(129);
			}else ps = statements.get(130);
			
			ps.executeUpdate(parameters);
			rs = ps.getGeneratedKeys();
			
			if (rs.next()) {
				return rs.getInt(1);
			}else return 0;
			
	   } catch (SQLException e){
		   throw new ApplicationServerException(171, e.getMessage());
	   }
	}

	public int insertAsSelectZvKontentitel (int newAccID, int oldAccID, boolean wBudget) throws ApplicationServerException{
		try{
			PreparedStatementWrapper ps;
		
			Object[] parameters = { new Integer(newAccID), new Integer(oldAccID)};
			
			if (wBudget){
				ps = statements.get(131);
			}else ps = statements.get(132);
			
			return ps.executeUpdate(parameters);

	   } catch (SQLException e){
		   throw new ApplicationServerException(172, e.getMessage());
	   }
	}
	
	
	public void dropTmpZvKontenTab () throws ApplicationServerException{
		try{
			
			statements.get(168).executeUpdate();
				
	   } catch (SQLException e){
		   throw new ApplicationServerException(169, e.getMessage());
	   }		
	}
	
	public void dropTmpZvKontentitelTab () throws ApplicationServerException{
		try{
			
			statements.get(169).executeUpdate();
				
	   } catch (SQLException e){
		   throw new ApplicationServerException(170, e.getMessage());
	   }		
	}
	
	public float updateZvTitelBudgetTakeovers (Benutzer b, int oldAccID, int newAccID) throws ApplicationServerException{
		try{
			float budget = 0.0f; 
			
			Object[] parameters = { new Integer(oldAccID), new Integer(newAccID)};
			
			// Ermittle neue Titel-IDs und offene alte Budgets
			ResultSet rs = statements.get(133).executeQuery(parameters);

			rs.last();	
			if ( rs.getRow() > 0 ) {	
				
				rs.beforeFirst();		
												
				while( rs.next() ){
					if (rs.getInt(2) == 0){ //Falls portierter ZVKontentitel nicht mehr existiert ...
						budget += rs.getFloat(3); // summiere nicht �bernommenes Budget
					}else if (rs.getFloat(3) > 0){// sonst buche Budget auf neuen Titel wenn Budget > 0
						Object[] p = {new Float(rs.getFloat(3)), new Integer(rs.getInt(2))};
						// Aktualisiere Titelbudget
						statements.get(134).executeUpdate(p); 
						// F�ge Buchung ein
						insertBuchung(new Buchung(b, "3", new ZVUntertitel(rs.getInt(1)), -rs.getFloat(3), new ZVUntertitel(rs.getInt(2)), rs.getFloat(3))); 
					}
				}
			}
			rs.close();		
			
			return budget;
			
		} catch (SQLException e){
			throw new ApplicationServerException(174, e.getMessage());
		}	
	}

	public void updateZvTgrBudget (int accID, float budget) throws ApplicationServerException{
		try{
						
			Object[] parameters = { new Float(budget), new Integer(accID)};
			statements.get(135).executeUpdate(parameters);
			
		} catch (SQLException e){
			throw new ApplicationServerException(173, e.getMessage());
		}	
	}

	/**
	 * Portiert die FBKonten eines bestimmten Haushaltsjahres in eine tempor�re Tabelle
	 * @param haushaltsjahr = ID
	 * @throws ApplicationServerException
	 */
	public void createAsSelectTempFbKontenTab (int haushaltsjahr) throws ApplicationServerException{
		try{
			Object[] parameters = {new Integer (haushaltsjahr) };
			statements.get(62).executeUpdate(parameters);
		
		} catch (SQLException e){
			throw new ApplicationServerException(175, e.getMessage());
		}
	}

	public void dropTmpFbKontenTab () throws ApplicationServerException{
		try{
			
			statements.get(63).executeUpdate();
				
	   } catch (SQLException e){
		   throw new ApplicationServerException(176, e.getMessage());
	   }		
	}

	public int insertAsSelectFbKonto (int acc, int year, boolean wBudget) throws ApplicationServerException{
		try{
			PreparedStatementWrapper ps;
			ResultSet rs;
			
			Object[] parameters = { new Integer(year), new Integer(acc)};
			
			if (wBudget){
				ps = statements.get(64);
			}else ps = statements.get(65);
			
			ps.executeUpdate(parameters);
			rs = ps.getGeneratedKeys();
			
			if (rs.next()) { //erster zur�ckgelieferter Schl�ssel = neue Hauptkonto-ID
				return rs.getInt(1);
			}else return 0;
			
	   } catch (SQLException e){
		   throw new ApplicationServerException(177, e.getMessage());
	   }
	}
	
	/**
	 * f�gt eine tempor�re Rolle f�r eine Benutzer hinzu.
	 * author robert
	 * @param tmpRolle - neue TempRolle des Benutzers
	 * @throws ApplicationServerException
	 */
	public void insertTempRolle(TmpRolle tmpRolle)throws ApplicationServerException{
		try{
			Object[] parameters = { new Integer(tmpRolle.getEmpfaenger().getId()), new Integer(tmpRolle.getBesitzer()), tmpRolle.getGueltigBis()};
			statements.get(350).executeUpdate(parameters);
		} catch (SQLException e){
			throw new ApplicationServerException(145, e.getMessage());
		}
	}
	
	/**
	 * l�scht eine tempor�re Rolle f�r eine Benutzer.
	 * author robert
	 * @param tmpRolle - temp. Rolle
	 * @throws ApplicationServerException
	 */
	public void deleteTempRolle(TmpRolle tmpRolle)throws ApplicationServerException{
		try{
			Object[] parameters = { new Integer(tmpRolle.getEmpfaenger().getId()), new Integer(tmpRolle.getBesitzer()) };
			statements.get(351).executeUpdate(parameters);
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException(146, e.getMessage());
		}
	}
	
	/**
	 * l�scht alle TmpRollen die zu einem Benutzer zugeordnet sind und die die er selbst vergeben hat
	 * @param empfaenger - Id des Benutzer der die TmpRollen erh�lt
	 * @throws ApplicationServerException
	 */
	public void deleteUserTempRolle(int empfaenger)throws ApplicationServerException{
		try{
			Object[] parameters = { new Integer(empfaenger)};
			statements.get(355).executeUpdate(parameters);
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException(149, e.getMessage());
		}
	}
	
	/**
	 * l�scht alle alten TmpRollen
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void deleteOldTempRolles()throws ApplicationServerException{
		try{
			statements.get(354).executeUpdate();
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException(148, e.getMessage());
		}
	}
	
	/**
	 * das Datum einer TempRolle aktualisieren
	 * @param tmpRolle - TempRolle
	 * @throws ApplicationServerException
	 * author robert
	 */
	public void updateTempRolle(TmpRolle tmpRolle)throws ApplicationServerException{
		try{
			Object[] parameters = {tmpRolle.getGueltigBis(), new Integer(tmpRolle.getEmpfaenger().getId()), new Integer(tmpRolle.getBesitzer()) };
			statements.get(352).executeUpdate(parameters);
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException(147, e.getMessage());
		}
	}
	
	/**
	 * gibt alle TempRollen mit Benutzern, die die Rolle von dem Besitzer erhalten haben
	 * @param besitzer - Id des Benutzers der die TempRolle vergeben hat
	 * @return Benutzer-Array
	 * @throws ApplicationServerException
	 * author robert
	 */
	public TmpRolle[] selectTempRolleUsers(int besitzer) throws ApplicationServerException{
		TmpRolle[] tmpRolle = null;

		try{
			Object[] parameters = {new Integer(besitzer) };
			ResultSet rs = statements.get(353).executeQuery(parameters);
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			if (count > 0){
				tmpRolle = new TmpRolle[count];
				int i = 0;
				while (rs.next()){
					tmpRolle[i] = new TmpRolle(0, besitzer, new Benutzer(rs.getInt(1), rs.getString(2),  rs.getString(3),  rs.getString(4)),
																		 rs.getDate(5));
					i++;
				}
			}
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(115, e.getMessage());
		}

		return tmpRolle;
	}

	public void createAsSelectTempKontenzuordnungTab (int oldYear, int newYear) throws ApplicationServerException{
		try{
			Object[] parameters = {new Integer (oldYear), new Integer (newYear), new Integer (oldYear), new Integer (newYear) };
			statements.get(313).executeUpdate(parameters);
		
		} catch (SQLException e){
			throw new ApplicationServerException(178, e.getMessage());
		}
	}

	public void dropTmpKontenzuordnungTab () throws ApplicationServerException{
		try{
			
			statements.get(315).executeUpdate();
				
	   } catch (SQLException e){
		   throw new ApplicationServerException(179, e.getMessage());
	   }		
	}

	public int insertAsSelectKontenzuordnungen () throws ApplicationServerException{
		try{
			
			return statements.get(314).executeUpdate();
				
	   } catch (SQLException e){
		   throw new ApplicationServerException(180, e.getMessage());
	   }		
	}

	public int selectZvTitelIdInYear (int title, int year) throws ApplicationServerException{
		try{
			
			Object[] parameters = { new Integer(title), new Integer(year), new Integer(title), new Integer(year)};
			
			ResultSet rs = statements.get(136).executeQuery(parameters);
			
			int result = 0;
			if (rs.next()) { //erster zur�ckgelieferter Schl�ssel = neue ZV-Titel-ID
				result = rs.getInt(1);
			}
			rs.close();
			return result;
			
	   } catch (SQLException e){
		   throw new ApplicationServerException(181, e.getMessage());
	   }		
	}
	
	public int selectFbKontoIdInYear (int acc, int year) throws ApplicationServerException{
		try{
			
			Object[] parameters = { new Integer(acc), new Integer(year)};
			
			ResultSet rs = statements.get(66).executeQuery(parameters);
			
			int result = 0;
			if (rs.next()) { //erster zur�ckgelieferter Schl�ssel = neue ZV-Titel-ID
				result =  rs.getInt(1);
			}
			rs.close();
			return result;
			
	   } catch (SQLException e){
		   throw new ApplicationServerException(182, e.getMessage());
	   }		
	}

	public int updateOrderAccounts (int order, int zvAcc, int fbAcc) throws ApplicationServerException{
		try{
			
			Object[] parameters = { new Integer(zvAcc), (fbAcc > 0) ? new Integer(fbAcc) : null, new Integer(order)};
			return statements.get(302).executeUpdate(parameters);
						
	   } catch (SQLException e){
		   throw new ApplicationServerException(183, e.getMessage());
	   }				
	}

	public int insertHaushaltsjahr (java.sql.Date beginn, char status) throws ApplicationServerException{
		
		try {
			Object[] parameters = {beginn, "3"};
			statements.get(74).executeUpdate(parameters);
			
			ResultSet rs = statements.get(74).getGeneratedKeys();
			
			int result = 0;
			if (rs.next()) result =  rs.getInt(1);
			rs.close();
			return result;
		} catch (SQLException e) {
			throw new ApplicationServerException(184, e.getMessage());
		}
		
	}
	
	public int updateHaushaltsjahrStatus (int id, char status) throws ApplicationServerException{
		try {
			Object[] parameters = {""+status, new Integer(id)};
			return statements.get(75).executeUpdate(parameters);
		} catch (SQLException e) {
			throw new ApplicationServerException(185, e.getMessage());
		}
	}

	public int updateHaushaltsjahrEnde (int id, java.sql.Date ende) throws ApplicationServerException{
		try {
			Object[] parameters = {ende, new Integer(id)};
			return statements.get(76).executeUpdate(parameters);
		} catch (SQLException e) {
			throw new ApplicationServerException(186, e.getMessage());
		}
	}
	
	public ArrayList selectHaushaltsjahre() throws ApplicationServerException{
		try {
			ArrayList years = new ArrayList();
			ResultSet rs = statements.get(77).executeQuery();
			
			rs.last();	
			if ( rs.getRow() > 0 ) {	
				rs.beforeFirst();		
				while( rs.next() ){		
					Haushaltsjahr year = new Haushaltsjahr(rs.getInt(1), rs.getDate(2), rs.getDate(3), rs.getInt(4));
					years.add(year);
				}
			}
			rs.close();		
			return years;
		} catch (SQLException e) {
			throw new ApplicationServerException(187, e.getMessage());
		}		
	}
	
	public int updateBestellungsbuchungen (int order, int oldTitle, int newTitle, int oldAcc, int newAcc) throws ApplicationServerException{
		try{
		
		Object[] parameters = { new Integer(oldTitle), new Integer(newTitle),
								new Integer(oldTitle), new Integer(newTitle),
								new Integer(oldAcc), new Integer(newAcc),
								new Integer(oldAcc), new Integer(newAcc),
								new Integer(order)};
		
		return statements.get(226).executeUpdate(parameters);
		} catch (SQLException e){
			throw new ApplicationServerException(188, e.getMessage());
		}
	}
	
	public int insertAsSelectBuchungenZvTitelMitteluebernahme(Benutzer user, int oldAcc, int newAcc) throws ApplicationServerException{
		try{
			Object[] parameters = {new Timestamp(System.currentTimeMillis()), new Integer(user.getId()), new Integer(oldAcc), new Integer(newAcc)};
			return statements.get(227).executeUpdate(parameters);
		} catch (SQLException e){
			throw new ApplicationServerException(189, e.getMessage());
		}		
	}

	public int insertAsSelectBuchungenFBKontoMitteluebernahme(Benutzer user, int oldAcc, int newYear) throws ApplicationServerException{
		try{
			Object[] parameters = {new Timestamp(System.currentTimeMillis()), new Integer(user.getId()), new Integer(oldAcc), new Integer(newYear)};
			return statements.get(228).executeUpdate(parameters);
		} catch (SQLException e){
			throw new ApplicationServerException(190, e.getMessage());
		}		
	}
	
}


