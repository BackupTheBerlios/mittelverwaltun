package applicationServer;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;


import dbObjects.*;


public class Database implements Serializable{

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

	public void connect (String user) throws ConnectionException{
		try{
			System.out.println("Load database driver...");
			Class.forName(driver);
			System.out.println("Done.");
			System.out.println("Connect to database...");
			con = DriverManager.getConnection("jdbc:mysql://" + url + "/" + database, user, defaultPwd);

			//con.setAutoCommit(false);
			//con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

			System.out.println("Connection established.");
			System.out.println("Prepare SQL-Statements...");
			statements = new PreparedSqlStatements(con);	//
			System.out.println("Done.");
		}catch (ClassNotFoundException e){
			throw new ConnectionException("Connection Exception: Invalid database driver.");
		}catch (SQLException e){
			throw new ConnectionException("Connection Exception: Connection refused.");
		}
	}

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
	 * Abfrage aller FBHauptkonten in der Datenbank, die zum angegebenem Institut gehören <br>
	 * und nicht gelöscht sind.
	 * @return Array mit den ermittelten Hauptkonten
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public ArrayList selectFBHauptkonten( Institut institut ) throws ApplicationServerException {
		ArrayList konten = new ArrayList();	// Liste mit den Hauptkonten

		try{
			Object[] parameters = { new Integer(institut.getId()) };	// Parameter setzen
			ResultSet rs = statements.get(50).executeQuery(parameters);	// SQL-Statement mit der Nummer 50 ausführen
			rs.last();		// auf die letzte abgefragte Zeile springen

			if ( rs.getRow() > 0 ) {	// Wenn die Zeilen anzahl größer als 0	
				rs.beforeFirst();		// Vor die erste Zeile springen

				while( rs.next() ){		// Wenn es noch Zeilen gibt
					// Neues FBHauptkonto erzeugen und die Liste einfügen
												// kontoId, hausHaltsJahreId, institut, bezeichnung, 
					konten.add( new FBHauptkonto( rs.getInt(1), rs.getInt(2), institut, rs.getString(4),
												// hauptkonto,  unterkonto, budget, dispolimit, vormerkungen
												rs.getString(5), rs.getString(6), rs.getFloat(7), rs.getFloat(8), rs.getFloat(9),
												// prüfbedingung, kleinbestellungen, gelöscht
												rs.getString(10), rs.getString(11).equalsIgnoreCase("1"), false ) );
				}
			}

			rs.close();					// Die Abfrage schließen
		} catch(SQLException e) {
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return konten;		// Ermittelten Hauptkonten
	}

	/**
	 * Abfrage aller FBUnterkonten in der Datenbank, die zum angegebenem Institut und Hauptkonto gehören <br>
	 * und nicht gelöscht sind.
	 * @param Institut
	 * @param FBHauptkonto
	 * @return FBUnterkonten
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public ArrayList selectFBUnterkonten( Institut institut, FBHauptkonto hauptkonto ) throws ApplicationServerException {
		ArrayList konten = new ArrayList();		// FBUnterkonten

		try{
			// Parameter an das SQL-Statement
			Object[] parameters = { new Integer(institut.getId()), new Integer(hauptkonto.getId()) };
			// SQL-Statement mit der Nummer 51 ausführen 
			ResultSet rs = statements.get(51).executeQuery(parameters);
			rs.last();		// An letzte Zeile springen

			if(rs.getRow() > 0) {		// Wenn die Zeile größer als 0
				rs.beforeFirst();		// Vor die erste Zeile springen

				while( rs.next() ){		// Wenn es die nächste Zeile gibt
					// Neues FBUnterkonto erzeugen und in die Liste einfügen
												// kontoId, hausHaltsJahrId, institut, bezeichnung
					konten.add( new FBUnterkonto( rs.getInt(1), rs.getInt(2), institut, rs.getString(4),
												// hauptkonto, unterkonto, budget, vormerkungen
												rs.getString(5), rs.getString(6), rs.getFloat(7), rs.getFloat(8),
												// kleinbestellungen, gelöscht
												rs.getString(9).equalsIgnoreCase("1"), false ) );
				}
			}

			rs.close();					// Die Abfrage schließen
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return konten;		// Ermittelten FBUnterkonten
	}
	

	/**
	 * Ein neues FBHauptkonto erstellen.
	 * @param FBHauptkonto
	 * @return kontoId, des eingefügten FBHauptkontos
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int insertFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = {new Integer(konto.getHaushaltsJahrID()), new Integer(konto.getInstitut().getId()),
									konto.getBezeichnung(), konto.getHauptkonto(), konto.getUnterkonto(),
									new Float(konto.getBudget()), new Float(konto.getDispoLimit()),
									new Float(konto.getVormerkungen()), konto.getPruefung(),
									(konto.getKleinbestellungen() ? "1" : "0"), (konto.getGeloescht() ? "1" : "0")};
			// SQL-Statement ausführen
			statements.get(52).executeUpdate(parameters);
			
			return existsFBKonto( konto );		// kontoId ermitteln
		} catch (SQLException e){
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	/**
	 * Ein neues FBUnterkonto erstellen.
	 * @param FBUnterkonto, welches erstellt werden soll
	 * @return KontoId, des erstellten FBUnterkontos
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	 public int insertFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {
		 try{
		 	// Parameter, welche an das SQL-Statement übergeben werden 
			Object[] parameters = {new Integer(konto.getHaushaltsJahrID()), new Integer(konto.getInstitut().getId()),
									konto.getBezeichnung(), konto.getHauptkonto(), konto.getUnterkonto(),
									new Float(konto.getBudget()), new Float(0),
									new Float(konto.getVormerkungen()), "",
									(konto.getKleinbestellungen() ? "1" : "0"), (konto.getGeloescht() ? "1" : "0")};
			// SQL-Statement ausführen
		 	statements.get(52).executeUpdate(parameters);
			
			return existsFBKonto( konto );		// kontoId ermitteln
		 } catch (SQLException e){
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		 }
	 }
	 
	/**
	 * Abfrage ob ein FBKonto schon existiert. Dabei ist die InstitutsId, <br>
	 * das Hauptkonto und das Unterkonto entscheidend.
	 * @param FBkonto
	 * @return kontoId des FBKontos
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int existsFBKonto(FBUnterkonto konto) throws ApplicationServerException {
		try{
			// Parameter für das SQL-Statement festlegen
			Object[] parameters = {new Integer(konto.getInstitut().getId()), konto.getHauptkonto(), konto.getUnterkonto()};
			// SQL-Statement mit der Nummer 53 ausführen
			ResultSet rs = statements.get(53).executeQuery(parameters);
			rs.last();	// Auf die letzte Zeile springen
			if( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen größer als 0, dann existiert das Konto
				rs.beforeFirst();	// Vor die erste Zeile springen
				rs.next();			// Nächste Zeile
				return rs.getInt(1);// Id des FBKontos
			}
		} catch (SQLException e) {
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;		// Sonst ist die Id 0 -> nicht gefunden
	}
	
	/**
	 * Abfrage ob ein gelöschtes FBKonto schon existiert. <br>
	 * Dabei ist die InstitutsId, das Hauptkonto und das Unterkonto entscheidend.
	 * @param FBkonto
	 * @return kontoId vom gelöschten FBKonto
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int existsDeleteFBKonto(FBUnterkonto konto) throws ApplicationServerException {
		try{
			// Parameter für das SQL-Statement festlegen
			Object[] parameters = {new Integer(konto.getInstitut().getId()), konto.getHauptkonto(), konto.getUnterkonto()};
			// SQL-Statement mit der Nummer 59 ausführen
			ResultSet rs = statements.get(59).executeQuery(parameters);
			rs.last();	// Auf die letzte Zeile springen
			if( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen größer als 0, dann existiert das Konto
				rs.beforeFirst();	// Vor die erste Zeile springen
				rs.next();			// Nächste Zeile
				return rs.getInt(1);// Id des FBKontos
			}
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;		// Sonst ist die Id 0 -> nicht gefunden
	}
		

	/**
	 * Ein FBKonto in der Datenbank löschen. Es wird kein Unterschied zwischen Haupt- und Unterkonto gemacht.
	 * @param FBKonto, das gelöscht werden soll
	 * @return Zeilennummer, des gelöschten FBKontos
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int deleteFBKonto( FBUnterkonto konto ) throws ApplicationServerException {
		try{
			// Parameter für das SQL-Statement
			Object[] parameters ={ new Integer(konto.getId())};
			// SQL-Statement ausführen
			return statements.get(54).executeUpdate(parameters);	// Zeilennummer des gelöschten Kontos
		} catch (SQLException e){
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	
	/**
	 * Ein FBHauptkonto zum Aktualisieren auswählen <br>
	 * @param FBKonto
	 * @return FBHauptkonto, welches aktualisiert werden soll
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public FBHauptkonto selectForUpdateFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {
		FBHauptkonto result = null;		// FBHauptkonto, welches aktualisiert werden soll
		
		try{
			// Parameter für das SQL-Statement festlegen
			Object[] parameters = {new Integer(konto.getId())};
			// SQL-Statement mit der Nummer 58 ausführen
			ResultSet rs = statements.get(58).executeQuery(parameters);
			rs.last();	// Auf die letzte Zeile springen
			if( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen größer als 0, dann existiert das Konto
				rs.beforeFirst();	// Vor die erste Zeile springen
				rs.next();			// Nächste Zeile
										// kontoId, hausHaltsJahrId, instiut, bezeichnung, 
				result = new FBHauptkonto( konto.getId(), rs.getInt(1), konto.getInstitut(), rs.getString(3), 
											// hauptkonto, unterkonto, budget
											rs.getString(4), rs.getString(5), rs.getFloat(6),
											// dispolimit, vormerkungen, pruefbedingung
											rs.getFloat(7), rs.getFloat(8), rs.getString(9),
											// kleinbestellungen, gelöscht
									 		rs.getString(10).equalsIgnoreCase( "1" ), rs.getString(11).equalsIgnoreCase( "1" ) );
			}
			rs.close();		// Abfrage schließen
		} catch ( SQLException e ) {
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
		
		return result;		// FBHauptkonto zurückgeben
	}
	
	/**
	 * Ein FBUnterkonto zum aktualisieren auswählen
	 * @param FBUnterkonto
	 * @return FBUnterkonto, welches ausgewählt wurde
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public FBUnterkonto selectForUpdateFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {
		FBUnterkonto result = null;		// Das FBUnterkonto, welches aktualisiert werden soll
		
		try{
			// Parameter für das SQL-Statement festlegen
			Object[] parameters = {new Integer(konto.getId())};
			// SQL-Statement mit der Nummer 58 ausführen
			ResultSet rs = statements.get(58).executeQuery(parameters);
			rs.last();	// Auf die letzte Zeile springen
			if( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen größer als 0, dann existiert das Konto
				rs.beforeFirst();	// Vor die erste Zeile springen
				rs.next();			// Nächste Zeile
										// kontoId, hausHaltsJahrId, instiut, bezeichnung, 
				result = new FBUnterkonto( konto.getId(), rs.getInt(1), konto.getInstitut(), rs.getString(3), 
										// hauptkonto, unterkonto, budget, vormerkungen
										rs.getString(4), rs.getString(5), rs.getFloat(6), rs.getFloat(8), 
										// kleinbestellungen, gelöscht
										rs.getString(10).equalsIgnoreCase( "1" ), rs.getString(11).equalsIgnoreCase( "1" ) );
			}
			rs.close();		// Abfrage schließen
		} catch ( SQLException e ){
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
		
		return result;		// FBUnterkonto zurückgeben
	}
	
	/**
	 * Anzahl der Bestellungen(aktiv und abgeschlossen) ermitteln, <br>
	 * bei denen ein bestimmtes FBKonto angegeben wurde.
	 * @param FBKonto, welches überprüft werden soll
	 * @return Anzahl der Bestellungen bei denen, das FBKonto angegeben ist
	 * @author w.flat
	 */
	public int countBestellungen( FBUnterkonto konto )throws ApplicationServerException{
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement ausführen
			ResultSet rs = statements.get(212).executeQuery(parameters);
			if(rs.next())	// Gibt es ein Ergebnis
				return rs.getInt(1);	// Anzahl der Bestellungen zurückgeben
			else
				return 0;				// Sonst ist die Anzahl = 0
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
	}
	
	/**
	 * Anzahl der aktiven Bestellungen ermitteln, bei denen ein bestimmtes FBKonto angegeben wurde.
	 * @param FBKonto, welches überprüft werden soll
	 * @return Anzahl der nicht abgeschlossenen Bestellungen
	 * @author w.flat
	 */
	public int countActiveBestellungen( FBUnterkonto konto )throws ApplicationServerException{
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement ausführen
			ResultSet rs = statements.get(213).executeQuery(parameters);
			if(rs.next())	// Wenn es Result gibt
				return rs.getInt(1);	// Rückgabe Anzahl der Bestellungen
			else
				return 0;				// Sonst wird 0 zurückgegeben
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Anzahl der aktiven Benutzer ermitteln, denen ein bestimmtes FBKonto zugeordnet ist.
	 * @param FBKonto, für das die Überprüfung durch genommen wird.
	 * @return Anzahl der Benutzer, die dieses Konto besitzen
	 * @author w.flat
	 */
	public int countActiveBenutzer( FBUnterkonto konto )throws ApplicationServerException{
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement ausführen
			ResultSet rs = statements.get(31).executeQuery(parameters);
			if(rs.next())	// Gibt es ein Ergebnis
				return rs.getInt(1);	// Dann die Anzahl zurückgeben
			else
				return 0;				// Sonst ist die Anzahl = 0
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}

	/**
	 * Anzahl der Benutzer(aktiv und gelöscht) ermitteln, denen ein bestimmtes FBKonto zugeordnet ist.
	 * @param FBKonto, für das die Überprüfung durch genommen wird.
	 * @return Anzahl der Benutzer, die dieses Konto besitzen
	 * @author w.flat
	 */
	public int countBenutzer( FBUnterkonto konto )throws ApplicationServerException{
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement ausführen
			ResultSet rs = statements.get(32).executeQuery(parameters);
			if(rs.next())	// Gibt es ein Ergebnis
				return rs.getInt(1);	// Dann die Anzahl zurückgeben
			else
				return 0;				// Sonst ist die Anzahl = 0
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Anzahl der Buchungen ermitteln, bei denen ein bestimmtes FBKonto benutzt wurde.
	 * @param FBKonto, welches überprüft werden soll
	 * @return Anzahl der Buchingen, bei denen das FBKonto benutzt wurde
	 * @author w.flat 
	 */
	public int countBuchungen( FBUnterkonto konto )throws ApplicationServerException{
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement ausführen
			ResultSet rs = statements.get(221).executeQuery(parameters);
			if(rs.next())	// Gibt es ein Ergebnis
				return rs.getInt(1);	// Anzahl der Buchungen zurückgeben
			else	
				return 0;				// Sonst ist die Anzahl = 0
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Anzahl der Kontenzuordnungen ermitteln, bei denen ein bestimmtes FBKonto angegeben wurde.
	 * @param FBKonto welches überprüft werden soll.
	 * @return Anzahl der Kontenzuordnungen, bei denen das FBKonto angegeben ist.
	 * @author w.flat
	 */
	public int countKontenzuordnungen( FBUnterkonto konto )throws ApplicationServerException{
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement ausführen
			ResultSet rs = statements.get(236).executeQuery(parameters);
			if(rs.next())		// Wenn es ein Ergebnis gibt
				return rs.getInt(1);	// Anzahl der Kontenzuordnungen zurückgeben
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
	 * @author w.flat
	 */
	public int updateFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = { new Integer(konto.getHaushaltsJahrID()), new Integer(konto.getInstitut().getId()),
									konto.getBezeichnung(), konto.getHauptkonto(), konto.getUnterkonto(),
									new Float(konto.getBudget()), new Float(konto.getDispoLimit()),
									new Float(konto.getVormerkungen()), konto.getPruefung(),
									(konto.getKleinbestellungen() ? "1" : "0"), 
									(konto.getGeloescht() ? "1" : "0"), new Integer(konto.getId()) };
			// Aktualisierung durchführen
			statements.get(55).executeUpdate(parameters);
			// Ermittlung der kontoId
			if( konto.getGeloescht() )	// Wenn das gelöscht wurde
				return existsDeleteFBKonto( konto );
			else						// Sonst bei nicht gelöschten nachsehen 
				return existsFBKonto( konto );
		} catch (SQLException e){
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Ein FBUnterkonto in der Datenbank aktualisieren.
	 * @param FBUnterkonto
	 * @return kontoId vom Unterkonto, das aktulisiert wurde
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int updateFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = { new Integer(konto.getHaushaltsJahrID()), new Integer(konto.getInstitut().getId()),
									konto.getBezeichnung(), konto.getHauptkonto(), konto.getUnterkonto(),
									new Float(konto.getBudget()), new Float(0),
									new Float(konto.getVormerkungen()), new String(""),
									(konto.getKleinbestellungen() ? "1" : "0"), 
									(konto.getGeloescht() ? "1" : "0"), new Integer(konto.getId()) };
			// Aktualisierung durchführen
			statements.get(55).executeUpdate(parameters);
			// Ermittlung der kontoId
			if( konto.getGeloescht() )	// Wenn das gelöscht wurde
				return existsDeleteFBKonto( konto );
			else						// Sonst bei nicht gelöschten nachsehen 
				return existsFBKonto( konto );
		} catch (SQLException e){
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	

	public Benutzer selectUser(String user, String password) throws ApplicationServerException{
		Benutzer benutzer = null;

		try{
			Object[] parameters = { user, password };
			ResultSet rs = statements.get(170).executeQuery(parameters);

			if (rs.next()){
				benutzer = new Benutzer(rs.getInt(1), rs.getString(2), rs.getString(3),
										new Rolle(rs.getInt(4), rs.getString(5)), new Institut(rs.getInt(6), rs.getString(7), rs.getString(8)),
										rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getInt(13),
										rs.getString(14), rs.getString(15), rs.getString(16), rs.getString(17),!rs.getString(18).equalsIgnoreCase( "0" ));
			}else {
				throw new ApplicationServerException(2);
			}

			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(1,e.getMessage());
		}

		return benutzer;
	}

	// Liefert einen Benutzer und sperrt diesen Datensatz für ein Update
	public Benutzer selectForUpdateUser(Benutzer benutzer) throws ApplicationServerException{
		Benutzer b = null;
		try{
			Object[] parameters = { benutzer.getBenutzername(), benutzer.getPasswort()};

			ResultSet rs = statements.get(172).executeQuery(parameters);
			if (rs.next())
				b = new Benutzer(rs.getInt(1), rs.getString(2), rs.getString(3),
							new Rolle(rs.getInt(4), rs.getString(5)), new Institut(rs.getInt(6), rs.getString(7), rs.getString(8)),
							rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getInt(13),
							rs.getString(14), rs.getString(15), rs.getString(16), rs.getString(17),!rs.getString(18).equalsIgnoreCase( "0" ));
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}
		return b;
	}

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
																			rs.getString(18),!rs.getString(19).equalsIgnoreCase( "0" ));
					i++;
				}
			}
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}

		return benutzer;
	}

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
			System.out.println(e.getMessage());
			throw new ApplicationServerException(1,e.getMessage());
		}

		return tmpRollen;
	}

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
			System.out.println(e.getMessage());
			throw new ApplicationServerException(1,e.getMessage());
		}

		return aktivitaeten;
	}

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
	 * sperrt den Fachbereich bevor es geändert werden kann
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

	public void updateFachbereich(Fachbereich fachbereich) throws ApplicationServerException{
		if(fachbereich != null){
			try{

				Object[] parameters = { new Integer(fachbereich.getId()), fachbereich.getFbBezeichnung(), new Float(fachbereich.getProfPauschale()), 
																fachbereich.getStrasseHausNr(), fachbereich.getPlzOrt(), fachbereich.getFhBezeichnung(),
																fachbereich.getFhBeschreibung() };
				if(statements.get(40).executeUpdate(parameters) == 0)
					throw new ApplicationServerException(3);

			} catch (SQLException e){
				throw new ApplicationServerException(1, e.getMessage());
			}
		}
	}

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
			throw new ApplicationServerException(1, e.getMessage());
		}

		return institutes;
	}
	
	/**
	 * gibt ein Institut mit Institutsleiter anhand der id zurück
	 * @param id des Instituts
	 * @return Institut
	 * @throws ApplicationServerException
	 */
	public Institut selectInstitute(int id) throws ApplicationServerException{
		Institut institut = null;
		
		try{
			Object[] parameters = { new Integer(id) };
			ResultSet rs = statements.get(86).executeQuery(parameters);
			if(rs.next())
				institut = new Institut (rs.getInt(1), rs.getString(2), rs.getString(3),
																	new Benutzer(rs.getInt(4),rs.getString(5),rs.getString(6),rs.getString(7)));
			
			rs.close();		// Abfrage schließen
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}

		return institut;		
	}

	public void deleteInstitute(Institut institut) throws ApplicationServerException{
		try{

			Object[] parameters = { new Integer(institut.getId()), institut.getBezeichnung() };
			statements.get(83).executeUpdate(parameters);


		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}
	}


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
				throw new ApplicationServerException(1, e.getMessage());
			}
		}else{
			throw new ApplicationServerException(4);
		}
		return 0;
	}

	// Gibt die Benutzer eines Instituts zurück
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
																			!rs.getString(18).equalsIgnoreCase( "0" ));
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
	 * gibt einen Benutzer anhand einer userId zurück mit allen Attributen
	 * @param userId
	 * @return Benutzer
	 * @throws ApplicationServerException
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
										rs.getString(14), rs.getString(15), rs.getString(16), rs.getString(17),!rs.getString(18).equalsIgnoreCase( "0" ));
			}else {
				throw new ApplicationServerException(2);
			}

			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(72,e.getMessage());
		}

		return benutzer;
	}

	public Institut selectForUpdateInstitute(Institut institut) throws ApplicationServerException{
		Institut inst = null;
		try{
			Object[] parameters = { new Integer(institut.getId()) };

			ResultSet rs = statements.get(85).executeQuery(parameters);
			if (rs.next())
				inst = new Institut (rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4));
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}
		return inst;
	}

	// Prüft ob die Änderung des Instituts oder das Einfügen gemacht werden kann
	public int checkInstitute(Institut institut) throws ApplicationServerException{
		try{
			Object[] parameters = { new Integer(institut.getId()), institut.getBezeichnung(), new Integer(institut.getKostenstelle()) };
			ResultSet rs = statements.get(84).executeQuery(parameters);
			rs.last();
			return rs.getRow();
		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	public void updateInstitute(Institut institut) throws ApplicationServerException{
		try{
			Object[] parameters = {institut.getBezeichnung() ,new Integer(institut.getKostenstelle()), 
														 new Integer(institut.getInstitutsleiter().getId()), new Integer(institut.getId())};
				if(statements.get(81).executeUpdate(parameters) == 0)
					throw new ApplicationServerException(3);
		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	//	Prüft ob es schon einen Benutzer mit dem Benutzernamen gibt
	 public int checkUserMySQL(Benutzer benutzer) throws ApplicationServerException{
		 try{
			 Object[] parameters = {benutzer.getBenutzername() };
			 ResultSet rs = statements.get(93).executeQuery(parameters);

			 rs.last();

		 	 return rs.getRow();
		 } catch (SQLException e){
			 throw new ApplicationServerException(1, e.getMessage());
		 }
	 }


	//	Prüft ob es schon einen Benutzer mit dem Benutzernamen gibt
	 public int checkUser(Benutzer benutzer) throws ApplicationServerException{
		 try{
			 Object[] parameters = { new Integer(benutzer.getId()), benutzer.getBenutzername() };
			 ResultSet rs = statements.get(27).executeQuery(parameters);

			 rs.last();
		 return rs.getRow();
		 } catch (SQLException e){
			 throw new ApplicationServerException(1, e.getMessage());
		 }
	 }

	public void updateUserMySQL(Benutzer benutzer, String oldBenutzer) throws ApplicationServerException{
		try{
			//setAutoCommit();
			Object[] param1 ={benutzer.getBenutzername(), oldBenutzer};
			statements.get(94).executeUpdate(param1);
			statements.get(95).executeUpdate(param1);
			commit();

		} catch (SQLException e){
				rollback();
				throw new ApplicationServerException(1, e.getMessage());
		}
	}

	public void updateUser(Benutzer benutzer) throws ApplicationServerException{
		try{
				if(benutzer.getPrivatKonto() == 0){
					Object[] param2 = {benutzer.getBenutzername(), new Integer(benutzer.getRolle().getId()), new Integer(benutzer.getKostenstelle().getId()),
														benutzer.getTitel(), benutzer.getName(),  benutzer.getVorname(), benutzer.getEmail(),
														benutzer.getTelefon(), benutzer.getFax(), benutzer.getBau(), benutzer.getRaum(), 
														(benutzer.getSwBeauftragter() ? "1" : "0"), new Integer(benutzer.getId())};
					if(statements.get(22).executeUpdate(param2) == 0)
						throw new ApplicationServerException(2);
				}else{
					Object[] param3 = {benutzer.getBenutzername(), new Integer(benutzer.getRolle().getId()), new Integer(benutzer.getKostenstelle().getId()),
															benutzer.getTitel(), benutzer.getName(),  benutzer.getVorname(), benutzer.getEmail(),
															new Integer(benutzer.getPrivatKonto()), benutzer.getTelefon(), benutzer.getFax(), 
															benutzer.getBau(), benutzer.getRaum(), (benutzer.getSwBeauftragter() ? "1" : "0"),
															new Integer(benutzer.getId())};
					if(statements.get(21).executeUpdate(param3) == 0)
						throw new ApplicationServerException(2);
				}

				if(!benutzer.getPasswort().equals("")){
					Object[] param = {benutzer.getPasswort(), new Integer(benutzer.getId()) };
					statements.get(23).executeUpdate(param);
				}
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	public void insertUserMySQL(Benutzer benutzer) throws ApplicationServerException{
		try{
			Object[] param ={benutzer.getBenutzername()};

			//setAutoCommit();
			statements.get(90).executeUpdate(param);
			statements.get(91).executeUpdate(param);
			statements.get(92).executeUpdate(param);
			//commit();

		} catch (SQLException e){
			System.out.println(e.getMessage());
			rollback();
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	/**
	 * fügt einen Benutzer in die Tabelle Benutzer ein
	 * @param benutzer
	 * @return Id in der Tabelle Benutzer des eingefügten Users 
	 * @throws ApplicationServerException
	 */
	public int insertUser(Benutzer benutzer) throws ApplicationServerException{
		try{
			Object[] param ={benutzer.getBenutzername()};

			PreparedStatementWrapper stmt;
	    if(benutzer.getPrivatKonto() == 0){
	        Object[] param2 ={benutzer.getBenutzername(), benutzer.getPasswort(), new Integer(benutzer.getRolle().getId()),
	                          new Integer(benutzer.getKostenstelle().getId()),  benutzer.getTitel(), benutzer.getName(),
	                          benutzer.getVorname(), benutzer.getEmail(), benutzer.getTelefon(), benutzer.getFax(),
	                          benutzer.getBau(), benutzer.getRaum(), (benutzer.getSwBeauftragter() ? "1" : "0")};
	        stmt = statements.get(25);
	        stmt.executeUpdate(param2);
	    }else{
	        Object[] param3 ={benutzer.getBenutzername(), benutzer.getPasswort(), new Integer(benutzer.getRolle().getId()),
	                          new Integer(benutzer.getKostenstelle().getId()),  benutzer.getTitel(), benutzer.getName(),
	                          benutzer.getVorname(), benutzer.getEmail(), new Integer(benutzer.getPrivatKonto()), 
	                          benutzer.getTelefon(), benutzer.getFax(), benutzer.getBau(), benutzer.getRaum(), 
	                          (benutzer.getSwBeauftragter() ? "1" : "0")};
	    		stmt = statements.get(24);
	        stmt.executeUpdate(param3);
	    }

			ResultSet rs = stmt.getGeneratedKeys();
			
	    if (rs.next()) {
	        return rs.getInt(1);
	    }else
	        return 0;
		} catch (SQLException e){
			rollback();
			setAutoCommit(true);
			System.out.println(e.getMessage());
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	public void deleteUserMySQL(Benutzer benutzer) throws ApplicationServerException{
		try{

			Object[] param ={benutzer.getBenutzername()};
			//setAutoCommit();
			statements.get(96).executeUpdate(param);
			statements.get(97).executeUpdate(param);
			//commit();
		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	/**
	 * Löscht den Benutzer endgültig aus der Tabelle Benutzer
	 */
	public void deleteUserFinal(Benutzer benutzer) throws ApplicationServerException{
		try{

			Object[] parameters = {new Integer(benutzer.getId()), benutzer.getBenutzername()};
			statements.get(26).executeUpdate(parameters);

		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	// setzt das Flag geloescht auf 1
	public void deleteUser(Benutzer benutzer) throws ApplicationServerException{
		try{

			Object[] parameters = {new Integer(benutzer.getId())};
			statements.get(29).executeUpdate(parameters);

		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	public void rollback() throws ApplicationServerException{
		try{
			statements.get(2).executeQuery();
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	public void commit() throws ApplicationServerException{
		try{
			statements.get(1).executeQuery();
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	public void setAutoCommit(boolean commit) throws ApplicationServerException{
		try{
			if(commit)
				statements.get(5).executeQuery();
			else
				statements.get(3).executeQuery();
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	public Haushaltsjahr selectHaushaltsjahr() throws ApplicationServerException{
		Haushaltsjahr hhj;
		try{
			ResultSet rs = statements.get(70).executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			rs.next();
			if (count > 0){
				return new Haushaltsjahr(rs.getString(1), rs.getString(2), rs.getString(3));
			}else{
				throw new ApplicationServerException(6);
			}
		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	/**
	 * gibt das aktuelle Haushaltsjahr zurück, d.h. wenn status = 0
	 * danach wird das Haushaltsjahr vor einer Aktualisierung für andere
	 * Benutzer gesperrt (SELECT FOR UPDATE).
	 * @return	Haushaltsjahr
	 * @throws ApplicationServerException
	 */
	public Haushaltsjahr selectForUpdateHaushaltsjahr() throws ApplicationServerException{
		Haushaltsjahr hhj = null;

		try{
			ResultSet rs = statements.get(70).executeQuery();
			if(rs.next())
				hhj = new Haushaltsjahr(rs.getString(1), rs.getString(2), rs.getString(3));
		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}
		return hhj;
	}

	public void updateHaushaltsjahr(Haushaltsjahr hhj) throws ApplicationServerException{
		if(hhj != null){
			try{
				Object[] parameters = {hhj.getBeschreibung(), hhj.getVon(), hhj.getBis()};

				if(statements.get(71).executeUpdate(parameters) == 0)
					throw new ApplicationServerException(6);

			} catch (SQLException e){
				throw new ApplicationServerException(1, e.getMessage());
			}
		}
	}
	
		/**
	 * Abfrage der HaushaltsjahrId vom aktuellem Jahr
	 */
	public int selectHaushaltsjahrId() throws ApplicationServerException{
		Haushaltsjahr hhj;
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
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

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
			throw new ApplicationServerException(1, e.getMessage());
		}

		return rollen;
	}

	public Rolle selectForUpdateRolle(Rolle rolle) throws ApplicationServerException{
		Rolle r = null;
		try{
			Object[] parameters = {new Integer(rolle.getId())};

			ResultSet rs = statements.get(106).executeQuery(parameters);
			if(rs.next())
				r = new Rolle(rs.getInt(1), rs.getString(2));
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}
		return r;
	}

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
			 throw new ApplicationServerException(1,e.getMessage());
		 }

		 return aktivitaeten;
	 }

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
			 System.out.println(e.getMessage());
			 throw new ApplicationServerException(1,e.getMessage());
		 }

		 return aktivitaeten;
	 }


	 public void insertRollenAktivitaet(int rolle, int aktivitaet)  throws ApplicationServerException{
		 try{
			 Object[] parameters = {new Integer(rolle), new Integer(aktivitaet)};
			 statements.get(112).executeUpdate(parameters);
		 } catch (SQLException e){
			 System.out.println(e.getMessage());
			 throw new ApplicationServerException(1,e.getMessage());
		 }
	 }

	 public void deleteRollenAktivitaet(int rolle, int aktivitaet)  throws ApplicationServerException{
		 try{
			Object[] parameters = {new Integer(rolle), new Integer(aktivitaet)};
			statements.get(113).executeUpdate(parameters);
		 } catch (SQLException e){
			 System.out.println(e.getMessage());
			 throw new ApplicationServerException(1,e.getMessage());
		 }
	 }

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
			throw new ApplicationServerException(1, e.getMessage());
		}

		return rollen;
	}

	/**
	 * Abfrage aller ZVKonten in der Datenbank.
	 * @return Liste mit ZVKonten
	 * @author w.flat
	 */
	public ArrayList selectZVKonten() throws ApplicationServerException {
		ArrayList konten = new ArrayList();	// Liste für die ZVKonten

		try{
			// Das SQL-Statement mit der Nummer 120 ausführen
			ResultSet rs = statements.get(120).executeQuery();
			rs.last();	// In die letzte Zeile springen
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen größer als 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				
				while( rs.next() ){		// Solange es nächste Abfragezeile gibt
					konten.add( new ZVKonto( rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5),
										rs.getFloat(6), rs.getFloat(7), rs.getString(8).equalsIgnoreCase( "1" ),
										rs.getString(9).charAt(0), rs.getShort(10) ) );
				}
			}
			rs.close();		// Abfrage schließen
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return konten;		// Liste mit den ZVKonten zurückgeben
	}

	/**
	 * Abfrage aller ZVTitel in der Datenbank die zu einem ZVKonto gehören.
	 * @param ZVKonto, zu dem die ZVTitel ermittelt werden sollen
	 * @return Liste mit den ZVTiteln, die zu dem ZVKonto gehören
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public ArrayList selectZVTitel( ZVKonto zvKonto ) throws ApplicationServerException {
		ArrayList konten = new ArrayList();	// Liste mit den ZVTiteln des ZVKontos

		try{
			// Parameter für das SQL-Statement 
			Object[] parameters = {new Integer(zvKonto.getId())};
			// SQL-Statement ausführen
			ResultSet rs = statements.get(140).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der abgefragten Zeilen größer 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				while( rs.next() ) {	// Solange es Zeilen gibt	
					konten.add( new ZVTitel( rs.getInt(1), zvKonto, rs.getString(3), rs.getString(4), rs.getString(5),
										rs.getFloat(6), rs.getString(7), rs.getString(8) ) );
				}
			}
			rs.close();		// Abfrage schließen
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}

		return konten;		// Ermittelten ZVTitel zurückgeben
	}

	/**
	 * Abfrage aller ZVUntertitel in der Datenbank die zu einem ZVTitel gehören.
	 * @param zvTitel, zu dem die ZVUntertitel ermittelt werden sollen
	 * @return Liste mit den ZVUntertiteln, die zu dem ZVTitel gehören
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public ArrayList selectZVUntertitel( ZVTitel zvTitel ) throws ApplicationServerException {
		ArrayList konten = new ArrayList();		// Liste für die ermittelten ZVUntertitel

		try{
			// Parameter für das SQL-Statement
			Object[] parameters = {new Integer(zvTitel.getZVKonto().getId())};
			// SQL-Statement mit der Nummer 141 ausführen
			ResultSet rs = statements.get(141).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if ( rs.getRow() > 0 ) {	// Gibt es ien Ergebnis
				rs.beforeFirst();		// Vor die erste Zeile springen
				while( rs.next() ) {	// Solange es Abfragezeilen gibt
					// ZVUntertitel erstellen und in die Liste einfügen
					konten.add( new ZVUntertitel( rs.getInt(1), zvTitel, rs.getString(3), rs.getString(4), rs.getString(5),
										rs.getFloat(6), rs.getString(7), rs.getString(8) ) );
				}
			}
			rs.close();		// Abfrage schließen
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}

		return konten;		// Die ermittelten ZVUntertitel zurückgeben
	}

	/**
	 * Ein neues ZVKonto erstellen.
	 * @param ZVKonto, welches erstellt werden soll
	 * @return kontoId vom erstellten ZVKonto
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int insertZVKonto( ZVKonto konto ) throws ApplicationServerException {
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = {new Integer(konto.getHaushaltsJahrId()), konto.getBezeichnung(), konto.getKapitel(),
								konto.getTitelgruppe(), new Float(konto.getTgrBudget()), new Float(konto.getDispoLimit()),
								(konto.getZweckgebunden() ? "1" : "0"), "" + konto.getFreigegeben(),
								"" + konto.getUebernahmeStatus()};
			// SQL-Statement ausführen
			statements.get(121).executeUpdate(parameters);
			
			return existsZVKonto( konto );	// KontoId ermitteln und zurückgeben
		} catch (SQLException e){
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Abfrage ob ein ZVKonto existiert. Dabei ist nur der Kapitel und die Titelgruppe entscheidend.
	 * @param ZVKonto, welches überprüft werden soll
	 * @return kontoId vom übergebenen ZVKonto
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int existsZVKonto( ZVKonto konto ) throws ApplicationServerException {
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = {konto.getKapitel(), konto.getTitelgruppe()};
			// SQL-Statement mit der Nummer 122 ausführen
			ResultSet rs = statements.get(122).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if( rs.getRow() > 0 ){		// Gibt es ein Ergebnis
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// Nächste Zeile
				return rs.getInt(1);	// Rückgabe der kontoId
			}
		} catch (SQLException e){
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;		// Sonst Rückgabe = 0 
	}
	
	/**
	 * Abfrage ob ein gelöschtes ZVKonto existiert. Dabei ist nur der Kapitel und die Titelgruppe entscheidend.
	 * @param ZVKonto, welches überprüft werden soll
	 * @return kontoId vom ZVKonto
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int existsDeleteZVKonto( ZVKonto konto ) throws ApplicationServerException {
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = {konto.getKapitel(), konto.getTitelgruppe()};
			// SQL-Statement mit der Nummer 126 ausführen
			ResultSet rs = statements.get(126).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if( rs.getRow() > 0 ){		// Gibt es ein Ergebnis
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// Nächste Zeile
				return rs.getInt(1);	// Rückgabe der kontoId
			}
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;		// Sonst Rückgabe = 0 
	}

	/**
	 * Abfrage ob ein ZVTitel existiert. Dabei ist die zvKontoId, der Titel und der Untertitel entscheidend.
	 * @param ZVTitel, das abgefragt werden soll
	 * @return titelId vom ZVTitel
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int existsZVTitel( ZVTitel titel ) throws ApplicationServerException {
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = {new Integer(titel.getZVKonto().getId()), titel.getTitel(), titel.getUntertitel()};
			// SQL-Statement mit der Numer 142 ausführen
			ResultSet rs = statements.get(142).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if( rs.getRow() > 0 ){	// Gibt es abgefragte Zeilen
				rs.beforeFirst();	// Vor die erste Zeile springen
				rs.next();			// Nächste abgefragte Zeile
				return rs.getInt(1);	// titelId zurückgeben
			}
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;		// Sonst ist die Rückgabe = 0
	}
	
	/**
	 * Abfrage ob ein gelöschter ZVTitel existiert. Dabei ist die zvKontoId, der Titel und der Untertitel entscheidend.
	 * @param zvTitel, der abgefragt werden soll
	 * @return titelId vom ermittelten ZVTitel
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int existsDeleteZVTitel( ZVTitel titel ) throws ApplicationServerException {
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = {new Integer(titel.getZVKonto().getId()), titel.getTitel(), titel.getUntertitel()};
			// SQL-Statement mit der Numer 147 ausführen
			ResultSet rs = statements.get(147).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if( rs.getRow() > 0 ){	// Gibt es abgefragte Zeilen
				rs.beforeFirst();	// Vor die erste Zeile springen
				rs.next();			// Nächste abgefragte Zeile
				return rs.getInt(1);
			}
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;		// Sonst ist die Rückgabe = 0
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
	 * Abfrage ob ein gelöschter ZVUntertitel existiert. Dabei ist die zvKontoId, der Titel und der Untertitel entscheidend.
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
	 * @param ZVTitel/ZVUntertitel, der überprüft werden sollte
	 * @return Anzahl der ermittelten Bestellungen
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int countBestellungen( ZVUntertitel konto )throws ApplicationServerException{
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement mit der Nummer 214 ausführen
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
	 * @param ZVTitel/ZVUntertitel, für welches die Überprüfung durchgeführt werden soll.
	 * @return Anzahl der nicht abgeschlossenen Bestellungen mit diesem ZVTitel/ZVUntertitel.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int countActiveBestellungen( ZVUntertitel konto )throws ApplicationServerException{
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement mit der Nummer 215 ausführen
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
	 * @param ZVTitel/ZVUntertitel, für welches die Überprüfung durchgeführt werden soll.
	 * @return Anzahl der Buchungen mit diesem ZVTitel/ZVUntertitel.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int countBuchungen( ZVUntertitel konto )throws ApplicationServerException{
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement mit der Nummer 222 ausführen
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
	 * @author w.flat
	 */
	public int countKontenzuordnungen( ZVKonto konto )throws ApplicationServerException{
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement mit der Nummer 235 auführen
			ResultSet rs = statements.get(235).executeQuery(parameters);
			if(rs.next())	// Gibt es ein Ergebnis
				return rs.getInt(1);	// Anzahl der Kontenzuordnungen für dieses ZVKonto
			else
				return 0;				// Sonst ist die Anzahl = 0
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
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
	public int countZVKonten( ZVKonto konto )throws ApplicationServerException{
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = { new Integer(konto.getId()) };
			// SQL-Statement mit der Nummer 237 auführen
			ResultSet rs = statements.get(237).executeQuery(parameters);
			if(rs.next())	// Gibt es ein Ergebnis
				return rs.getInt(1);	// Ermittelte Zahl zurückgeben
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
			// Parameter für das SQL-Statement
			Object[] parameters ={new Integer(konto.getZVKonto().getId()), konto.getBezeichnung(), konto.getTitel(),
								konto.getUntertitel(), new Float(konto.getBudget()), konto.getBemerkung(), konto.getBedingung()};
			// SQL-Statement mit der Nummer 143 ausführen
			statements.get(143).executeUpdate(parameters);
			return existsZVTitel( konto );		// ZVTitelId ermitteln und zurückgeben
		} catch (SQLException e){
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Ein neuen ZVUntertitel zu einem ZVTitel erstellen erstellen.
	 * @param ZVUntertitel welcher erstellt werden soll
	 * @return ZVUntertitelId vom erstellten ZVUntertitel
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int insertZVUntertitel( ZVUntertitel konto ) throws ApplicationServerException {
		try{
			// Parameter für das SQL-Statement
			Object[] parameters ={new Integer(konto.getZVTitel().getZVKonto().getId()), konto.getBezeichnung(), konto.getTitel(),
								konto.getUntertitel(), new Float(konto.getBudget()), konto.getBemerkung(), konto.getBedingung()};
			// SQL-Statement mit der Nummer 143 ausführen
			statements.get(143).executeUpdate(parameters);
			return existsZVUntertitel( konto );		// ZVUntertitelId ermitteln und zurückgeben
		} catch (SQLException e){
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Ein ZVKonto in der Datenbank löschen.
	 * @param ZVKonto das gelöscht werden soll.
	 * @return Die gelöschte Zeile in der ZVKonto-Tabelle
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int deleteZVKonto( ZVKonto konto ) throws ApplicationServerException {
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = {new Integer(konto.getId())};
			// SQL-Statement mit der Nummer 123 ausführen
			return statements.get(123).executeUpdate(parameters);	// gelöschte Zeile zurückgeben
		} catch (SQLException e){
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Einen ZVTitel in der Datenbank löschen.
	 * @param ZVTitel das gelöscht werden soll.
	 * @return Die gelöschte Zeile in der ZVTitel-Tabelle
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int deleteZVTitel( ZVUntertitel titel ) throws ApplicationServerException {
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = {new Integer(titel.getId())};
			// SQL-Statement mit der Nummer 144 ausführen
			return statements.get(144).executeUpdate(parameters);	// gelöschte Zeile zurückgeben
		} catch (SQLException e){
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Einen ZVUntertitel in der Datenbank löschen.
	 * @param ZVUntertitel das gelöscht werden soll.
	 * @return Die gelöschte Zeile in der ZVTitel-Tabelle
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int deleteZVUntertitel( ZVUntertitel titel ) throws ApplicationServerException {
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = {new Integer(titel.getId())};
			// SQL-Statement mit der Nummer 144 ausführen
			return statements.get(144).executeUpdate(parameters);	// gelöschte Zeile zurückgeben
		} catch (SQLException e){
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}

	}

	/**
	 * Ein ZVKonto in der Datenbank aktualisieren.
	 * @param ZVKonto, welches aktualisiert werden soll.
	 * @return zvKontoId vom aktualisierten ZVKonto
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int updateZVKonto( ZVKonto konto ) throws ApplicationServerException {
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = {new Integer(konto.getHaushaltsJahrId()),  konto.getBezeichnung(), konto.getKapitel(),
							konto.getTitelgruppe(), new Float(konto.getTgrBudget()), new Float(konto.getDispoLimit()),
							(konto.getZweckgebunden() ? "1" : "0"), ""+konto.getFreigegeben(), ""+konto.getUebernahmeStatus(),
							(konto.getGeloescht() ? "1" : "0"), new Integer(konto.getId())};
			// SQL-Statement mit der Nummer ausführen 
			statements.get(124).executeUpdate(parameters);
			if( konto.getGeloescht() )		// Wenn das gelöschtes Konto
				return existsDeleteZVKonto( konto );	// Dann zvKontoId vom gelöschten ZVKonto
			else
				return existsZVKonto( konto );			// Sonst von aktiven ZVKonto
		} catch (SQLException e){
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Einen ZVTitel in der Datenbank aktualisieren.
	 * @param ZVTitel, welcher aktualisiert werden soll
	 * @return zvTitelId des aktualisierten ZVTitels
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int updateZVTitel( ZVTitel titel ) throws ApplicationServerException {
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = {new Integer(titel.getZVKonto().getId()), titel.getBezeichnung(), titel.getTitel(),
							titel.getUntertitel(), new Float(titel.getBudget()), titel.getBemerkung(),
							titel.getBedingung(), (titel.getGeloescht() ? "1" : "0"), new Integer(titel.getId())};
			// SQL-Statement mit der Nummer 145 ausführen
			statements.get(145).executeUpdate(parameters);
			if( titel.getGeloescht() )		// Wenn gelöschter ZVTitel
				return existsDeleteZVTitel( titel );	// Dann ZVTitelId vom gelöschten ZVTitel
			else
				return existsZVTitel( titel );			// Sonst vom aktiven ZVTitel
		} catch (SQLException e){
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Einen ZVUntertitel in der Datenbank aktualisieren.
	 * @param ZVUntertitel welcher aktualisiert werden soll
	 * @return ZVUntertitelId vom aktualisierten ZVUntertitel
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int updateZVUntertitel( ZVUntertitel titel ) throws ApplicationServerException {
		try{
			// Parameter an das SQL-Statement
			Object[] parameters = {new Integer( titel.getZVTitel().getZVKonto().getId()), titel.getBezeichnung(), titel.getTitel(),
							titel.getUntertitel(), new Float(titel.getBudget()), titel.getBemerkung(),
							titel.getBedingung(), (titel.getGeloescht() ? "1" : "0"), new Integer(titel.getId())};
			// SQL-Statement mit der Nummer 145 ausführen
			statements.get(145).executeUpdate(parameters);
			if( titel.getGeloescht() )		// Wenn gelöschter ZVTitel
				return existsDeleteZVUntertitel( titel );	// Dann ZVUntertitelId vom gelöschten ZVUntertitel
			else
				return existsZVUntertitel( titel );			// Sonst vom aktiven ZVUntertitel
		} catch (SQLException e){
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	
	/**
	 * Ein ZVKonto zum Aktualisieren auswählen.
	 * @param ZVKonto, welches ausgewählt werden soll.
	 * @return ZVKonto, das ausgewählt wurde
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public ZVKonto selectForUpdateZVKonto( ZVKonto zvKonto ) throws ApplicationServerException {
		ZVKonto result = null;		// Das ausgewählte ZVKonto
		try {
			// Parameter für das SQL-Statement
			Object[] parameters = {new Integer( zvKonto.getId() )};
			// SQL-Statement mit der Nummer 125 ausführen
			ResultSet rs = statements.get(125).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if ( rs.getRow() > 0 ) {	// Gibt es abgefragte Zeilen
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// Nächste Abfrage-Zeile
				// Neues ZVKonto erstellen
				result = new ZVKonto( zvKonto.getId(), rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
									rs.getFloat(5), rs.getFloat(6), !rs.getString(7).equalsIgnoreCase( "0" ),
									rs.getString(8).charAt(0), rs.getShort(9), !rs.getString(10).equalsIgnoreCase( "0" ) );
			}
			rs.close();		// Abfrage schließen
		} catch(SQLException e) {
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
		
		return result;		// ZVKonto zurückgeben
	}
	
	/**
	 * Einen ZVTitel zum Aktualisieren auswählen.
	 * @param ZVTitel, der aktualisiert werden soll
	 * @return ZVTitel der aktualisiert werden soll
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public ZVTitel selectForUpdateZVTitel( ZVTitel zvTitel ) throws ApplicationServerException {
		ZVTitel result = null;		// Der ausgewählte ZVTitel
		try {
			// Parameter für das SQL-Statement
			Object[] parameters = {new Integer( zvTitel.getId() )};
			// SQL-Statement mit der Nummer 146 ausführen
			ResultSet rs = statements.get(146).executeQuery(parameters);
			rs.last();		// In die letzte Abfragezeile springen 
			if ( rs.getRow() > 0 ) {	// Gibt es ein Ergebnis
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// Die Nächste Zeile
				// Neuen ZVTitel erstellen
				result = new ZVTitel( zvTitel.getId(), zvTitel.getZVKonto(), rs.getString(2), rs.getString(3),rs.getString(4),
									rs.getFloat(5), rs.getString(6), rs.getString(7), !rs.getString(8).equalsIgnoreCase( "0" ) );
			}
			rs.close();		// Abfrage schließen
		} catch(SQLException e) {
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
		
		return result;		// ZVtitel zurückgeben
	}
	
	/**
	 * einen ZVTitle aus ZVKontenTitle mit der entsprechenden id zurückgeben
	 * @param id des ZVTitels
	 * @return ZVTitel
	 * @throws ApplicationServerException
	 */
	public ZVTitel selectZVTitel( int id ) throws ApplicationServerException {
		ZVTitel result = null;		// Der ausgewählte ZVTitel
		try {
			Object[] parameters = {new Integer( id )};
			ResultSet rs = statements.get(148).executeQuery(parameters);
			
			rs.last();		// In die letzte Abfragezeile springen 
			if ( rs.getRow() > 0 ) {	// Gibt es ein Ergebnis
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// Die Nächste Zeile
				// Neuen ZVTitel erstellen
				result = new ZVTitel( id, null, rs.getString(2), rs.getString(3),rs.getString(4),
									rs.getFloat(5), rs.getString(6), rs.getString(7), !rs.getString(8).equalsIgnoreCase( "0" ) );
			}
			rs.close();		// Abfrage schließen
		} catch(SQLException e) {
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	
		return result;		// ZVtitel zurückgeben
	}
	
	/**
	 * Einen ZVUntertitel zum Aktualisieren auswählen
	 * @param ZVUntertitel, der aktualisiert werden soll
	 * @return ZVUntertitel der aktualisiert werden soll
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public ZVUntertitel selectForUpdateZVUntertitel( ZVUntertitel zvUntertitel ) throws ApplicationServerException {
		ZVUntertitel result = null;		// Der ausgewählte ZVUntertitel
		try {
			// Parameter für das SQL-Statement
			Object[] parameters = {new Integer( zvUntertitel.getId() )};
			// SQL-Statement mit der Nummer 146 ausführen
			ResultSet rs = statements.get(146).executeQuery(parameters);
			rs.last();		// In die letzte Abfragezeile springen 
			if ( rs.getRow() > 0 ) {	// Gibt es ein Ergebnis
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// Die Nächste Zeile
				// Neuen ZVUntertitel erstellen
				result = new ZVUntertitel( zvUntertitel.getId(), zvUntertitel.getZVTitel(), rs.getString(2), rs.getString(3),
					rs.getString(4), rs.getFloat(5), rs.getString(6), rs.getString(7), !rs.getString(8).equalsIgnoreCase( "0" ) );
			}
			rs.close();		// Abfrage schließen
		} catch(SQLException e) {
			rollback();		// Um die Änderungen rückgängig zu machen
			throw new ApplicationServerException( 0, e.getMessage() );
		}
		
		return result;		// ZVtitel zurückgeben
	}
	

	public ResultSet query(String query) throws ApplicationServerException{
	 try{
		 PreparedStatement stmt = con.prepareStatement(query);
	 		return stmt.executeQuery();
	 } catch (SQLException e){
		 throw new ApplicationServerException(1, e.getMessage());
	 }
  }

  public void update(String query) throws ApplicationServerException{
	   try{
		   PreparedStatement stmt = con.prepareStatement(query);
			 stmt.executeUpdate();
	   } catch (SQLException e){
		   throw new ApplicationServerException(1, e.getMessage());
	   }
   }

   // prüft ob die Bezeichnung der Rolle schon mal in der Daten vorhanden ist
  public int checkRolle(Rolle rolle) throws ApplicationServerException{
  	try{
	  	Object[] parameters = { new Integer(rolle.getId()), rolle.getBezeichnung()};
			ResultSet rs = statements.get(101).executeQuery(parameters);
			rs.last();

		  return rs.getRow();
	  } catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
	  }
  }

  public int insertRolle(Rolle rolle) throws ApplicationServerException{
		try{
		   Object[] parameters = { new Integer(rolle.getId()), rolle.getBezeichnung()};
		   statements.get(102).executeUpdate(parameters);
		   ResultSet rs = statements.get(102).getGeneratedKeys();
		   if (rs.next()) {
			   return rs.getInt(1);
		   }
   } catch (SQLException e){
	   throw new ApplicationServerException(1, e.getMessage());
   }
   return 0;
	}

	public void updateRolle(Rolle rolle) throws ApplicationServerException{
		try{
			Object[] parameters = {rolle.getBezeichnung(), new Integer(rolle.getId())};
				if(statements.get(103).executeUpdate(parameters) == 0)
					throw new ApplicationServerException(7);
		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	public void deleteRolle(Rolle rolle) throws ApplicationServerException{
	  try{
		Object[] parameters = { new Integer(rolle.getId()), rolle.getBezeichnung() };
		   if(statements.get(104).executeUpdate(parameters) == 0)
					throw new ApplicationServerException(7);
		   } catch (SQLException e){
			   throw new ApplicationServerException(1, e.getMessage());
		   }
	}

	public void deleteRollenAktivitaeten(int rolle)  throws ApplicationServerException{
		 try{
			Object[] parameters = {new Integer(rolle)};
			statements.get(114).executeUpdate(parameters);
		 } catch (SQLException e){
			 System.out.println(e.getMessage());
			 throw new ApplicationServerException(1,e.getMessage());
		 }
	 }

	// Gibt die Anzahl der Benutzer die dieser Rolle zugeordnet sind
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

	// Gibt die Anzahl der Bestellungen eines Benutzers an
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

 	//	Gibt die Anzahl der Bestellungen eines Benutzers an
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
	  * gibt die Kontenzuordnungen zu einem FBHauptkonto zurück
	  * @param FBHauptkonto
	  * @return Kontenzuordnungen[]
	  * @throws ApplicationServerException
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
	 * gibt die Kontenzuordnugn mit der fbKontoId und zvKontoId
	 * @param fbKontoId
	 * @param zvKontoId
	 * @return
	 * @throws ApplicationServerException
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

	public void insertKontenZuordnung(int fbKontoId, int zvKontoId)throws ApplicationServerException{
		try{
			Object[] parameters = { new Integer(zvKontoId), new Integer(fbKontoId) };
			statements.get(232).executeUpdate(parameters);
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	public void deleteKontenZuordnung(int fbKontoId, int zvKontoId)throws ApplicationServerException{
		try{
			Object[] parameters = { new Integer(zvKontoId), new Integer(fbKontoId) };
			statements.get(231).executeUpdate(parameters);
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	public void updateKontenZuordnung(int fbKontoId, int zvKontoId, short status)throws ApplicationServerException{
		try{
			Object[] parameters = {new Integer(status), new Integer(fbKontoId), new Integer(zvKontoId) };
			statements.get(230).executeUpdate(parameters);
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException(1, e.getMessage());
		}
	}


	//	Abfrage eines ZVKontos in der Datenbank
	public ZVKonto selectZVKonto(int zvKontoId) throws ApplicationServerException {
		ZVKonto konto = null;

		try{
			Object[] parameters = { new Integer(zvKontoId)};
			ResultSet rs = statements.get(139).executeQuery(parameters);

			if ( rs.next() ){
				konto = new ZVKonto( rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5),
									rs.getFloat(6), rs.getFloat(7), !rs.getString(8).equalsIgnoreCase( "0" ),
									rs.getString(9).charAt(0), rs.getShort(10) );
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
	 * Abfrage eines FBUnterkontos mit der angegebenen Id.
	 */
	public FBUnterkonto selectFBKonto(int fbKontoid) throws ApplicationServerException {
		FBUnterkonto konto = null;

		try{
			Object[] parameters = { new Integer(fbKontoid) };
			ResultSet rs = statements.get(69).executeQuery(parameters);

			if( rs.next() ){
				konto = new FBUnterkonto( rs.getInt(1), rs.getInt(2), null, rs.getString(4), rs.getString(5),
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
	 * Abfrage aller nicht gelöschter Firmen.
	 * @return ArrayListe mit nicht gelöschten Firmen.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public ArrayList selectFirmen() throws ApplicationServerException {
		ArrayList firmen = new ArrayList();		// Liste mit den Firmen
		try{
			// SQL-Statement mit der Nummer 240 ausführen
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
			rs.close();		// Abfrage schließen
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return firmen;		// Die Firmen zurückgeben
	}
	
	/**
	 * gibt eine Firma anhand der id zurück
	 * @param id der Firma
	 * @return Firma
	 * @throws ApplicationServerException
	 */
	public Firma selectFirma(int id) throws ApplicationServerException {
		Firma firma = null;		
		try{
			Object[] parameters = { new Integer(id) };
			ResultSet rs = statements.get(248).executeQuery(parameters);
			rs.last();		
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeile > 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				firma = new Firma( rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
											rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
											rs.getString(10), rs.getString(11).equalsIgnoreCase( "1" ), 
											rs.getString(12).equalsIgnoreCase( "1" ) );
			}
			rs.close();		// Abfrage schließen
		} catch (SQLException e){
			throw new ApplicationServerException( 75, e.getMessage() );
		}
	
		return firma;		// Die Firmen zurückgeben
	}
	
	/**
	 * Abfrage aller gelöschter Firmen.
	 * @return ArrayList mit gelöschten Firmen.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public ArrayList selectDelFirmen() throws ApplicationServerException {
		ArrayList firmen = new ArrayList();		// Liste für die gelöschten Firmen
		try {
			// SQL-Statement mit der Nummer 241 ausführen
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

			rs.close();		// Abfrage schließen
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return firmen;		// Die Firmen zurückgeben
	}
	
	/**
	 * Eine Firma zum aktualisieren auswählen.
	 * @param Firma, die ausgewählt werden soll.
	 * @return Firma, die ausgewählt wurde.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public Firma selectForUpdateFirma( Firma firma ) throws ApplicationServerException {
		Firma result = null;
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = {new Integer(firma.getId())};
			// SQL-Statement mit der Nummer 242 ausführen
			ResultSet rs = statements.get(242).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeile > 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// Die nächste Zeile
				result = new Firma( rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
									rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
									rs.getString(10), rs.getString(11).equalsIgnoreCase( "1" ),
									rs.getString(12).equalsIgnoreCase( "1" ) );
			}
			rs.close();			// Abfrage schließen
		} catch (SQLException e){
			rollback();
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return result;		// Die Firma zurückgeben
	}
	
	/**
	 * Abfrage ob eine nicht gelöschte Firma schon existiert. <br>
	 * Dabei ist der Namen, Strasse, Strassen-Nr, PLZ, Ort entscheidend.
	 * @param Firma, die überprüft werden soll.
	 * @return id von der Firma.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int existsFirma( Firma firma ) throws ApplicationServerException {
		try { 
			// Parameter für das SQL-Statement
			Object[] parameters = {new String(firma.getName()), new String(firma.getStrasseNr()),
									new String(firma.getPlz()), new String(firma.getOrt())};
			// SQL-Satement mit der Nummer 243 ausführen
			ResultSet rs = statements.get(243).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if( rs.getRow() > 0 ){		// Ist die Anzahl der Zeile > 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// Die nächste Zeile
				return rs.getInt(1);	// Id der Firma
			}
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;
	}
	
	/**
	 * Abfrage ob eine gelöschte Firma schon existiert. <br>
	 * Dabei ist der Namen, Strasse, Strassen-Nr, PLZ, Ort entscheidend.	 
	 * @param Firma, die überprüft werden soll.
	 * @return id von der Firma
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int existsDelFirma( Firma firma ) throws ApplicationServerException {
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = {new String(firma.getName()), new String(firma.getStrasseNr()),
									new String(firma.getPlz()), new String(firma.getOrt())};
			// SQL-Satement mit der Nummer 244 ausführen
			ResultSet rs = statements.get(244).executeQuery(parameters);
			rs.last();		// In die letzte Zeile springen
			if( rs.getRow() > 0 ){		// Ist die Anzahl der Zeile > 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				rs.next();				// Die nächste Zeile
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
	 * @author w.flat
	 */
	public int updateFirma( Firma firma ) throws ApplicationServerException {
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = { firma.getName(), firma.getStrasseNr(), firma.getPlz(), firma.getOrt(), firma.getKundenNr(),
									firma.getTelNr(), firma.getFaxNr(), firma.getEMail(), firma.getWWW(), 
									new String(firma.getASK() ? "1" : "0"),
									new String(firma.getGeloescht() ? "1" : "0"), new Integer( firma.getId() ) };
			// SQL-Satement mit der Nummer 245 ausführen
			statements.get(245).executeUpdate(parameters);
			if( firma.getGeloescht() )		// Wenn gelöschte Firma
				return existsDelFirma( firma );	// Id von der gelöschten Firma
			else
				return existsFirma( firma );	// Id von der nicht gelöschten Firma
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	/**
	 * Ein neue Firma erstellen.
	 * @param Firma, die erstellt werden soll.
	 * @return id der erstellten Firma.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	 public int insertFirma( Firma firma ) throws ApplicationServerException {
		 try{
			// Parameter für das SQL-Statement
			Object[] parameters = { firma.getName(), firma.getStrasseNr(), firma.getPlz(), firma.getOrt(), firma.getKundenNr(),
									firma.getTelNr(), firma.getFaxNr(), firma.getEMail(), firma.getWWW(), 
									new String( firma.getASK() ? "1" : "0" ), new String( firma.getGeloescht() ? "1" : "0" ) };
			// SQL-Satement mit der Nummer 246 ausführen
			statements.get(246).executeUpdate(parameters);
			return existsFirma( firma );	// Id von der Firma
		} catch (SQLException e){
			rollback();
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	/**
	 * Eine Firma in der Datenbank löschen.
	 * @param Firma, die gelöscht werden soll.
	 * @return Zeilenindex von der Firma, die gelöscht wurde.
	 * @throws ApplicationServerException
	 * 
	 */
	public int deleteFirma( Firma firma ) throws ApplicationServerException {
		try{
			// Parameter für das SQL-Statement
			Object[] parameters = {new Integer(firma.getId())};
			// SQL-Satement mit der Nummer 247 ausführen
			statements.get(247).executeUpdate(parameters);
			return firma.getId();		// FirmenId zurückgeben
		} catch (SQLException e){
			rollback();
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	/**
	 * Anzahl der Belege, bei denen eine bestimmte Firma angegeben wurde.
	 * @param Firma, die überprüft werden soll.
	 * @return Anzahl der Belege.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int countBelege( Firma firma )throws ApplicationServerException{
		 try{
			// Parameter für das SQL-Statement
			Object[] parameters = { new Integer(firma.getId()) };
			// SQL-Satement mit der Nummer 275 ausführen
			ResultSet rs = statements.get(275).executeQuery(parameters);
		   if(rs.next())		// Gibt es ein Ergebnis
			 return rs.getInt(1);	// Anzahl zurückgeben
		   else
				return 0;			// Sonst ist die Anzahl = 0
		 } catch (SQLException e){
		   throw new ApplicationServerException(1, e.getMessage());
	   }
	}
	
	/**
	 * Anzahl der Angebote, bei denen eine bestimmte Firma angegeben wurde.
	 * @param Firma, die überprüft werden soll.
	 * @return Anzahl der Angebote.
	 * @throws ApplicationServerException
	 * @author w.flat
	 */
	public int countAngebote( Firma firma )throws ApplicationServerException{
		 try{
			// Parameter für das SQL-Statement
			Object[] parameters = { new Integer(firma.getId()) };
			// SQL-Satement mit der Nummer 262 ausführen
			ResultSet rs = statements.get(262).executeQuery(parameters);
		   if(rs.next())		// Gibt es ein Ergebnis
			 return rs.getInt(1);	// Anzahl zurückgeben
		   else
				return 0;			// Sonst ist die Anzahl = 0
		 } catch (SQLException e){
		   throw new ApplicationServerException(1, e.getMessage());
	   }
	}


	 // Gibt die Benutzer eines Instituts mit einer bestimmten Rolle zurück
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
	 public FBHauptkonto selectForUpdateFBHauptkonto(int kontoID) throws ApplicationServerException {
		 FBHauptkonto konto = null;

		 try{
			 Object[] parameters = { new Integer(kontoID) };
			 ResultSet rs = statements.get(58).executeQuery(parameters);

			 if( rs.next() ){
				 konto = new FBHauptkonto( kontoID, rs.getInt(1), new Institut(rs.getInt(2),null,null), rs.getString(3),rs.getString(4), rs.getString(5),
											 rs.getFloat(6), rs.getFloat(7), rs.getString(8), !rs.getString(9).equalsIgnoreCase( "0" ) );
			 }

			 rs.close();
		 } catch (SQLException e){
			 System.out.println(e.getMessage());
			 throw new ApplicationServerException( 1, e.getMessage() );
		 }
		 return konto;
	 }
	 /**
	  * Abfrage aller FBHauptkonten in der Datenbank, die zum angegebenem Institut gehören.
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
	  * @author robert
	  * 
	  * gibt alle Kostenarten aus der SQL-Tabelle Kostenarten
	  * @return Kostenart Array
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
	 * fügt eine Bestellung in die Tabelle Bestellungen ein und liefert die erzeugte Id für die Bestellung zurück
	 * @param bestellung - Standard-,ASK- und KleinBestellung
	 * @param typ - gibt den Typ der Bestellung an 0 = Standard, 1 = ASK, 2 = Klein
	 * @return Id der Bestellung in der Tabelle Bestellungen
	 * @throws ApplicationServerException
	 */ 
	public int insertBestellung(Bestellung bestellung, int typ) throws ApplicationServerException{
		if(bestellung != null){
			try{
				float vb = 0f; // Verbindlichkeiten
				if(typ != 2 && bestellung.getPhase() != 0)
					vb = bestellung.getBestellwert();
				
				Object[] parameters = { new Integer(bestellung.getBesteller().getId()), new Integer(bestellung.getAuftraggeber().getId()),
																new Integer(bestellung.getEmpfaenger().getId()), bestellung.getReferenznr(), "" + typ,
																"" + bestellung.getPhase(), bestellung.getDatum(), new Integer(bestellung.getZvtitel().getId()), 
																new Integer(bestellung.getFbkonto().getId()), new Float(bestellung.getBestellwert()), new Float(vb)};
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
	 * fügt eine StandardBestellung in die Tabelle ASK_Standard_Bestellungen ein
	 * @param bestellung - Standardbestellung
	 * @return
	 * @throws ApplicationServerException
	 */
	public void insertStandardBestellung(StandardBestellung bestellung) throws ApplicationServerException{
		if(bestellung != null){
			try{
				Object[] parameters = { new Integer(bestellung.getId()), bestellung.getBemerkung(),
																new Integer(bestellung.getKostenart().getId()), (bestellung.getErsatzbeschaffung() ? "1" : "0"),
																bestellung.getErsatzbeschreibung(), bestellung.getInventarNr(), bestellung.getVerwendungszweck(),
																(bestellung.getPlanvorgabe() ? "1" : "0"), bestellung.getBegruendung()};
				statements.get(250).executeUpdate(parameters);
				
			} catch (SQLException e){
				//rollback();
				setAutoCommit(true);
				throw new ApplicationServerException(66, e.getMessage());
			}
		}else{
			setAutoCommit(true);
			throw new ApplicationServerException(66);
		}
	}
	
	/**
	 * gibt eine StandardBestellung mit der bestellerId zurück. Angebote und Auswahl sind nicht gesetzt
	 * @param bestellId der StandardBestellung
	 * @return Standardbestellung
	 * @throws ApplicationServerException
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
																						rs.getString("referenzNr"), rs.getDate("datum"), besteller, rs.getString("phase").charAt(0), auftraggeber,
																						empfaenger, zvTitel, fbkonto, rs.getInt("bestellwert") );
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
	 * fügt eine ASKBestellung in die Tabelle ASK_Standard_Bestellungen ein
	 * @param bestellung - ASKbestellung
	 * @param angebotId - Id des dazugehörigen Angebots
	 * @return
	 * @throws ApplicationServerException
	 */
	public void insertASKBestellung(ASKBestellung bestellung) throws ApplicationServerException{
		if(bestellung != null){
			try{
				Object[] parameters = {new Integer(bestellung.getId()), 
																bestellung.getBemerkung(), new Integer(bestellung.getSwbeauftragter().getId())};
				statements.get(251).executeUpdate(parameters);
			
			} catch (SQLException e){
				throw new ApplicationServerException(69, e.getMessage());
			}
		}else{
			throw new ApplicationServerException(69);
		}
	}
	
	/**
	 * fügt ein Angebot in die Tabelle Angebote ein und liefert eine Id zurück
	 * @param angebot
	 * @param bestellungId - Id der eingefügten Bestellung
	 * @param angenommen - sagt aus ob das Angebot ausgewählt wurde
	 * @return Id des Angebots
	 * @throws ApplicationServerException
	 */
	public int insertAngebot(Angebot angebot, int bestellungId, boolean angenommen) throws ApplicationServerException{
		if(angebot != null){
			try{
				Object[] parameters = { new Integer(bestellungId), new Integer(angebot.getAnbieter().getId()),
																angebot.getDatum(), (angenommen ? "1" : "0") };
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
	 * fügt eine Position in die Tabelle Positionen ein
	 * @param position 
	 * @param angebotId - Id des zugehörigen Angbots
	 * @throws ApplicationServerException
	 */
	public void insertPosition(Position position, int angebotId) throws ApplicationServerException{
		if(position != null){
			try{
				Object[] parameters = { new Integer(angebotId), new Integer(position.getInstitut().getId()), new Integer(position.getMenge()),
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
	 * gibt eine ArrayListe aller Angebote zu einer Bestellung anhand der bestellId zurück
	 * @param bestellId - Id der Bestellung
	 * @return ArrayListe von Angeboten
	 * @throws ApplicationServerException
	 */
	public ArrayList selectAngebote(int bestellId) throws ApplicationServerException {
		ArrayList angebote = new ArrayList();	// Liste für die ZVKonten

		try{
			Object[] parameters = { new Integer(bestellId) };
			ResultSet rs = statements.get(261).executeQuery(parameters);
			rs.last();	
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen größer als 0
				rs.beforeFirst();		// Vor die erste Zeile springen
			
				while( rs.next() ){		// Solange es nächste Abfragezeile gibt
					
					Firma anbieter = new Firma( rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
																			rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
																			rs.getString(10), rs.getString(11).equalsIgnoreCase( "1" ), 
																			rs.getString(12).equalsIgnoreCase( "1" ) );
					angebote.add( new Angebot( rs.getInt(13), null, rs.getDate(14), anbieter, !rs.getString(15).equalsIgnoreCase("0")));
				}
			}
			rs.close();		// Abfrage schließen
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
	 */
	public ArrayList selectPositionen(int angebotId) throws ApplicationServerException {
		ArrayList positionen = new ArrayList();	// Liste für die ZVKonten
		
		try{
			Object[] parameters = { new Integer(angebotId) };
			ResultSet rs = statements.get(266).executeQuery(parameters);
			rs.last();	
			if ( rs.getRow() > 0 ) {	// Ist die Anzahl der Zeilen größer als 0
				rs.beforeFirst();		// Vor die erste Zeile springen
				while( rs.next() ){		// Solange es nächste Abfragezeile gibt 
					Institut institut = selectInstitute(rs.getInt("institut"));
				  positionen.add( new Position(rs.getInt(1), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getFloat(6),
				  														 rs.getFloat(7), institut, !rs.getString(8).equalsIgnoreCase("0")));
				}
			}
			rs.close();		// Abfrage schließen
		} catch (SQLException e){
			throw new ApplicationServerException( 74, e.getMessage() );
		}

		return positionen;
	}
	
	/**
	 * prüft ob die ReferenzNr der Bestellung schon vorhanden ist
	 * @param referenzNr der Bestellung
	 * @return Anzahl der Bestellungen mit dieser ReferenzNr
	 * @throws ApplicationServerException
	 */
	public int checkReferenzNr(String referenzNr) throws ApplicationServerException{
	  try{
		  Object[] parameters = { referenzNr };
			  ResultSet rs = statements.get(218).executeQuery(parameters);
			  rs.last();
	
			 return rs.getRow();
		 } catch (SQLException e){
			  throw new ApplicationServerException(77, e.getMessage());
		 }
 	}
}