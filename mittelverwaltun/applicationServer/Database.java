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
	 * Abfrage aller FBHauptkonten in der Datenbank, die zum angegebenem Institut gehören.
	 */
	public ArrayList selectFBHauptkonten( Institut institut ) throws ApplicationServerException {
		ArrayList konten = new ArrayList();

		try{
			Object[] parameters = { new Integer(institut.getId()) };
			ResultSet rs = statements.get(50).executeQuery(parameters);
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
	 * Abfrage aller FBUnterkonten in der Datenbank, die zum angegebenem Institut und Hauptkonto gehören.
	 * @param institut
	 * @param hauptkonto
	 * @return
	 * @throws ApplicationServerException
	 */
	public ArrayList selectFBUnterkonten( Institut institut, FBHauptkonto hauptkonto ) throws ApplicationServerException {
		ArrayList konten = new ArrayList();

		try{
			Object[] parameters = { new Integer(institut.getId()), new Integer(hauptkonto.getId()) };
			ResultSet rs = statements.get(51).executeQuery(parameters);

			rs.last();

			if ( rs.getRow() > 0 ){
				rs.beforeFirst();

				int i = 0;
				while( rs.next() ){
					konten.add( new FBUnterkonto( rs.getInt(1), rs.getInt(2), institut, rs.getString(4),
												  rs.getString(5), rs.getString(6), rs.getFloat(7) ) );
				}
			}

			rs.close();

		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		return konten;
	}
	

	/**
	 * Ein neues FBHauptkonto erstellen.
	 * @param konto
	 * @return
	 * @throws ApplicationServerException
	 */
	public int insertFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {
		try{
			Object[] parameters = {new Integer(konto.getHaushaltsJahrID()), new Integer(konto.getInstitut().getId()), konto.getBezeichnung(),
									konto.getHauptkonto(), konto.getUnterkonto(), new Float(konto.getBudget()), new Float(konto.getDispoLimit()),
									konto.getPruefung(), (konto.getGeloescht() ? "1" : "0")};
			statements.get(52).executeUpdate(parameters);
			
			return existsFBKonto( konto );
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	/**
	 * Ein neues Unterkonto erstellen.
	 * @param konto
	 * @return
	 * @throws ApplicationServerException
	 */
	 public int insertFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {
		 try{
		 	Object[] parameters = {new Integer(konto.getHaushaltsJahrID()), new Integer(konto.getInstitut().getId()),
									konto.getBezeichnung(), konto.getHauptkonto(), konto.getUnterkonto(),
									new Float(konto.getBudget()), (konto.getGeloescht() ? "1" : "0")};

		 	statements.get(56).executeUpdate(parameters);
			
			return existsFBKonto( konto );
		 } catch (SQLException e){
			 throw new ApplicationServerException( 0, e.getMessage() );
		 }
	 }
	 
	/**
	 * Abfrage ob ein FBKonto schon existiert. Dabei ist die InstitutsId, das Hauptkonto und das Unterkonto entscheidend.
	 * @param konto
	 * @return id vom FBKonto
	 * @throws ApplicationServerException
	 */
	public int existsFBKonto( FBUnterkonto konto ) throws ApplicationServerException {
		try{
			Object[] parameters = {new Integer(konto.getInstitut().getId()), konto.getHauptkonto(), konto.getUnterkonto()};
			ResultSet rs = statements.get(53).executeQuery(parameters);
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
	 * Abfrage ob ein gelöschtes FBKonto schon existiert.
	 * Dabei ist die InstitutsId, das Hauptkonto und das Unterkonto entscheidend.
	 * @param konto
	 * @return id vom gelöschtem Konto
	 * @throws ApplicationServerException
	 */
	public int existsDeleteFBKonto( FBUnterkonto konto ) throws ApplicationServerException {
		try{
			Object[] parameters = {new Integer(konto.getInstitut().getId()), konto.getHauptkonto(), konto.getUnterkonto()};
			ResultSet rs = statements.get(59).executeQuery(parameters);
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
	 * Ein FBKonto in der Datenbank löschen. Es wird kein Unterschied zwischen Haupt- und Unterkonto gemacht.
	 * @param konto
	 * @return
	 * @throws ApplicationServerException
	 */
	public int deleteFBKonto( FBUnterkonto konto ) throws ApplicationServerException {

		try{
			Object[] parameters ={ new Integer(konto.getId())};
			
			return statements.get(54).executeUpdate(parameters);
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	
	/**
	 * Ein FBHauptkonto zum aktualisieren auswählen
	 * @param konto
	 * @return
	 * @throws ApplicationServerException
	 */
	public FBHauptkonto selectForUpdateFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {
		FBHauptkonto result = null;
		
		try{
			Object[] parameters = {new Integer(konto.getId())};
			ResultSet rs = statements.get(58).executeQuery(parameters);
			rs.last();

			if ( rs.getRow() > 0 ){
				rs.beforeFirst();
				rs.next();
				
				result = new FBHauptkonto( konto.getId(), rs.getInt(1), konto.getInstitut(), rs.getString(3), rs.getString(4),
											rs.getString(5), rs.getFloat(6), rs.getFloat(7), rs.getString(8),
									 		!rs.getString(9).equalsIgnoreCase( "0" ) );
			}
			rs.close();
		} catch ( SQLException e ){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
		
		return result;
	}
	
	/**
	 * Ein FBUnterkonto zum aktualisieren auswählen
	 * @param konto
	 * @return
	 * @throws ApplicationServerException
	 */
	public FBUnterkonto selectForUpdateFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {
		FBUnterkonto result = null;
		
		try{
			Object[] parameters = {new Integer(konto.getId())};
			ResultSet rs = statements.get(58).executeQuery(parameters);
			rs.last();

			if ( rs.getRow() > 0 ){
				rs.beforeFirst();
				rs.next();
				
				result = new FBUnterkonto( konto.getId(), rs.getInt(1), konto.getInstitut(), rs.getString(3), rs.getString(4),
											rs.getString(5), rs.getFloat(6), !rs.getString(9).equalsIgnoreCase( "0" ) );
			}
			rs.close();
		} catch ( SQLException e ){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
		
		return result;
	}
	
	/**
	 * Anzahl der Bestellungen ermitteln, bei denen ein bestimmtes FBKonto angegeben wurde
	 */
	public int countBestellungen( FBUnterkonto konto )throws ApplicationServerException{
		try{
		  Object[] parameters = { new Integer(konto.getId()) };
			ResultSet rs = statements.get(212).executeQuery(parameters);
			if(rs.next())
				return rs.getInt(1);
			else
				return 0;
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Anzahl der aktiven Bestellungen ermitteln, bei denen ein bestimmtes FBKonto angegeben wurde
	 */
	public int countActiveBestellungen( FBUnterkonto konto )throws ApplicationServerException{
		try{
		  Object[] parameters = { new Integer(konto.getId()) };
			ResultSet rs = statements.get(213).executeQuery(parameters);
			if(rs.next())
				return rs.getInt(1);
			else
				return 0;
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Anzahl der aktiven Benutzer ermitteln, denen ein bestimmtes FBKonto zugeordnet ist
	 */
	public int countActiveBenutzer( FBUnterkonto konto )throws ApplicationServerException{
		try{
		  Object[] parameters = { new Integer(konto.getId()) };
			ResultSet rs = statements.get(31).executeQuery(parameters);
			if(rs.next())
				return rs.getInt(1);
			else
				return 0;
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Anzahl der Buchungen ermitteln, bei denen ein bestimmtes FBKonto benutzt wurde
	 */
	public int countBuchungen( FBUnterkonto konto )throws ApplicationServerException{
		try{
		  Object[] parameters = { new Integer(konto.getId()) };
			ResultSet rs = statements.get(221).executeQuery(parameters);
			if(rs.next())
				return rs.getInt(1);
			else
				return 0;
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Anzahl der Kontenzuordnungen ermitteln, bei denen ein bestimmtes FBKonto angegeben wurde
	 */
	public int countKontenzuordnungen( FBUnterkonto konto )throws ApplicationServerException{
		try{
		  Object[] parameters = { new Integer(konto.getId()) };
			ResultSet rs = statements.get(236).executeQuery(parameters);
			if(rs.next())
				return rs.getInt(1);
			else
				return 0;
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Ein FBHauptkonto in der Datenbank aktualisieren.
	 * @param konto
	 * @return id vom Hauptkonto, das aktulisiert wurde
	 * @throws ApplicationServerException
	 */
	public int updateFBHauptkonto( FBHauptkonto konto ) throws ApplicationServerException {

		try{
			Object[] parameters = { new Integer(konto.getHaushaltsJahrID()), new Integer(konto.getInstitut().getId()),
									konto.getBezeichnung(), konto.getHauptkonto(), konto.getUnterkonto(),
									new Float(konto.getBudget()), new Float(konto.getDispoLimit()), konto.getPruefung(),
									(konto.getGeloescht() ? "1" : "0"), new Integer(konto.getId()) };
			statements.get(55).executeUpdate(parameters);
			
			if( konto.getGeloescht() )
				return existsDeleteFBKonto( konto );
			else
				return existsFBKonto( konto );
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Ein FBUnterkonto in der Datenbank aktualisieren.
	 * @param konto
	 * @return id vom Unterkonto, das aktulisiert wurde
	 * @throws ApplicationServerException
	 */
	public int updateFBUnterkonto( FBUnterkonto konto ) throws ApplicationServerException {

		try{

			Object[] parameters = { new Integer(konto.getHaushaltsJahrID()), new Integer(konto.getInstitut().getId()),
									konto.getBezeichnung(), konto.getHauptkonto(), konto.getUnterkonto(),
									new Float(konto.getBudget()), (konto.getGeloescht() ? "1" : "0"), new Integer(konto.getId()) };
			statements.get(57).executeUpdate(parameters);
			
			if( konto.getGeloescht() )
				return existsDeleteFBKonto( konto );
			else
				return existsFBKonto( konto );
		} catch (SQLException e){
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
					fachbereiche[i] = new Fachbereich(rs.getInt(1), rs.getString(2), rs.getFloat(3), rs.getString(4), rs.getString(5));
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
	public Fachbereich selectForUpdateFachbereich(Fachbereich fachbereich, int institutsid) throws ApplicationServerException{
		Fachbereich f = null;
		try{
			ResultSet rs = statements.get(181).executeQuery();

			if(rs.next())
				f = new Fachbereich(rs.getInt(1), rs.getString(2), rs.getFloat(3), rs.getString(4), rs.getString(5));
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}
		return f;
	}

	public void updateFachbereich(Fachbereich fachbereich, int institutsid) throws ApplicationServerException{
		if(fachbereich != null){
			try{

				Object[] parameters = { new Integer(institutsid), fachbereich.getHochschule(), new Float(fachbereich.getProfPauschale()), new Integer(fachbereich.getId()) };
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
					institutes[i] = new Institut (rs.getInt(1), rs.getString(2), rs.getString(3));
					i++;
				}
			}
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}

		return institutes;
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
				Object[] parameters = { institut.getBezeichnung(), new Integer(institut.getKostenstelle()) };
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

			ResultSet rs = statements.get(28).executeQuery(parameters);
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			if (count > 0){
				benutzer = new Benutzer[count];
				int i = 0;
				while (rs.next()){
					benutzer[i] = new Benutzer(rs.getString(1), rs.getString(2), rs.getString(3),rs.getString(4));
					i++;
				}
			}
			rs.close();
			return benutzer;
		} catch (SQLException e){
			throw new ApplicationServerException(1, e.getMessage());
		}
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
			Object[] parameters = {institut.getBezeichnung() ,new Integer(institut.getKostenstelle()), new Integer(institut.getId())};
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
			setAutoCommit();
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
														(benutzer.getSoftBeauftragter() ? "1" : "0"), new Integer(benutzer.getId())};
					if(statements.get(22).executeUpdate(param2) == 0)
						throw new ApplicationServerException(2);
				}else{
					Object[] param3 = {benutzer.getBenutzername(), new Integer(benutzer.getRolle().getId()), new Integer(benutzer.getKostenstelle().getId()),
															benutzer.getTitel(), benutzer.getName(),  benutzer.getVorname(), benutzer.getEmail(),
															new Integer(benutzer.getPrivatKonto()), benutzer.getTelefon(), benutzer.getFax(), 
															benutzer.getBau(), benutzer.getRaum(), (benutzer.getSoftBeauftragter() ? "1" : "0"),
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

			setAutoCommit();
			statements.get(90).executeUpdate(param);
			statements.get(91).executeUpdate(param);
			statements.get(92).executeUpdate(param);
			commit();

		} catch (SQLException e){
			System.out.println(e.getMessage());
			rollback();
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	public int insertUser(Benutzer benutzer) throws ApplicationServerException{
		try{
			Object[] param ={benutzer.getBenutzername()};

			PreparedStatementWrapper stmt;
	    if(benutzer.getPrivatKonto() == 0){
	        Object[] param2 ={benutzer.getBenutzername(), benutzer.getPasswort(), new Integer(benutzer.getRolle().getId()),
	                          new Integer(benutzer.getKostenstelle().getId()),  benutzer.getTitel(), benutzer.getName(),
	                          benutzer.getVorname(), benutzer.getEmail(), benutzer.getTelefon(), benutzer.getFax(),
	                          benutzer.getBau(), benutzer.getRaum(), (benutzer.getSoftBeauftragter() ? "1" : "0")};
	        stmt = statements.get(25);
	        stmt.executeUpdate(param2);
	    }else{
	        Object[] param3 ={benutzer.getBenutzername(), benutzer.getPasswort(), new Integer(benutzer.getRolle().getId()),
	                          new Integer(benutzer.getKostenstelle().getId()),  benutzer.getTitel(), benutzer.getName(),
	                          benutzer.getVorname(), benutzer.getEmail(), new Integer(benutzer.getPrivatKonto()), 
	                          benutzer.getTelefon(), benutzer.getFax(), benutzer.getBau(), benutzer.getRaum(), 
	                          (benutzer.getSoftBeauftragter() ? "1" : "0")};
	    		stmt = statements.get(24);
	        stmt.executeUpdate(param3);
	    }

			ResultSet rs = stmt.getGeneratedKeys();

	    if (rs.next()) {
	        return rs.getInt(1);
	    }else
	        return 0;
		} catch (SQLException e){
			System.out.println(e.getMessage());
			throw new ApplicationServerException(1, e.getMessage());
		}
	}

	public void deleteUserMySQL(Benutzer benutzer) throws ApplicationServerException{
		try{

			Object[] param ={benutzer.getBenutzername()};
			setAutoCommit();
			statements.get(96).executeUpdate(param);
			statements.get(97).executeUpdate(param);
			commit();
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

	public void setAutoCommit() throws ApplicationServerException{
		try{
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
	 * Abfrage aller ZVKonten in der Datenbank
	 */
	public ArrayList selectZVKonten() throws ApplicationServerException {
		ArrayList konten = new ArrayList();

		try{
			ResultSet rs = statements.get(120).executeQuery();
			rs.last();
			
			if ( rs.getRow() > 0 ){
				rs.beforeFirst();

				int i = 0;
				while( rs.next() ){
					konten.add( new ZVKonto( rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5),
										rs.getFloat(6), rs.getFloat(7), !rs.getString(8).equalsIgnoreCase( "0" ),
										rs.getString(9).charAt(0), rs.getShort(10) ) );
				}
			}

			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return konten;
	}

	/**
	 * Abfrage aller ZVTitel in der Datenbank die zu einem ZVKonto gehören
	 * @param zvKonto
	 * @return
	 * @throws ApplicationServerException
	 */
	public ArrayList selectZVTitel( ZVKonto zvKonto ) throws ApplicationServerException {
		ArrayList konten = new ArrayList();

		try{
			Object[] parameters = {new Integer(zvKonto.getId())};
			ResultSet rs = statements.get(140).executeQuery(parameters);
			rs.last();

			if ( rs.getRow() > 0 ){
				rs.beforeFirst();
				int i = 0;

				while( rs.next() ){
					konten.add( new ZVTitel( rs.getInt(1), zvKonto, rs.getString(3), rs.getString(4), rs.getString(5),
										rs.getFloat(6), rs.getString(7), rs.getString(8) ) );
				}
			}
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}

		return konten;
	}

	/**
	 * Abfrage aller ZVUntertitel in der Datenbank die zu einem ZVKonto gehören
	 * @param zvTitel
	 * @return
	 * @throws ApplicationServerException
	 */
	public ArrayList selectZVUntertitel( ZVTitel zvTitel ) throws ApplicationServerException {
		ArrayList konten = new ArrayList();

		try{

			Object[] parameters = {new Integer(zvTitel.getZVKonto().getId())};
			ResultSet rs = statements.get(141).executeQuery(parameters);
			rs.last();

			if ( rs.getRow() > 0 ){
				rs.beforeFirst();
				int i = 0;

				while( rs.next() ){
					konten.add( new ZVUntertitel( rs.getInt(1), zvTitel, rs.getString(3), rs.getString(4), rs.getString(5),
										rs.getFloat(6), rs.getString(7), rs.getString(8) ) );
				}
			}
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}

		return konten;
	}
	


	/**
	 * Ein neues ZVKonto erstellen
	 * @param konto
	 * @return
	 * @throws ApplicationServerException
	 */
	public int insertZVKonto( ZVKonto konto ) throws ApplicationServerException {

		try{
			Object[] parameters = {new Integer(konto.getHaushaltsJahrId()), konto.getBezeichnung(), konto.getKapitel(),
									konto.getTitelgruppe(), new Float(konto.getTgrBudget()), new Float(konto.getDispoLimit()),
									(konto.getZweckgebunden() ? "1" : "0"), "" + konto.getFreigegeben(), "" + konto.getUebernahmeStatus()};
			
			statements.get(121).executeUpdate(parameters);
			
			return existsZVKonto( konto );
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Abfrage ob ein ZVKonto existiert. Dabei ist nur der Kapitel und die Titelgruppe entscheidend.
	 * @param konto
	 * @return id vom ZVKonto
	 * @throws ApplicationServerException
	 */
	public int existsZVKonto( ZVKonto konto ) throws ApplicationServerException {
		try{
			ResultSet rs;
//			if( konto.isTGRKonto() ){
				Object[] parameters = {konto.getKapitel(), konto.getTitelgruppe()};
				rs = statements.get(122).executeQuery(parameters);
//			} else {
//				Object[] parameters = {konto.getKapitel(), ((ZVTitel)konto.getSubTitel().get(0)).getTitel()};
//				rs = statements.get(160).executeQuery(parameters);
//			}
			rs.last();
			if( rs.getRow() > 0 ){
				rs.beforeFirst();
				rs.next();
				int res = rs.getInt(1);
				return res;
			}
		} catch (SQLException e){
			throw new ApplicationServerException(0, e.getMessage());
		}
		
		return 0;
	}
	
	/**
	 * Abfrage ob ein gelöschtes ZVKonto existiert. Dabei ist nur der Kapitel und die Titelgruppe entscheidend.
	 * @param konto
	 * @return id vom ZVKonto
	 * @throws ApplicationServerException
	 */
	public int existsDeleteZVKonto( ZVKonto konto ) throws ApplicationServerException {
		try{
			ResultSet rs;
//			if( konto.isTGRKonto() ){
				Object[] parameters = {konto.getKapitel(), konto.getTitelgruppe()};
				rs = statements.get(126).executeQuery(parameters);
//			} else {
//				Object[] parameters = {konto.getKapitel(), ((ZVTitel)konto.getSubTitel().get(0)).getTitel()};
//				rs = statements.get(169).executeQuery(parameters);
//			}
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
	 * Abfrage ob ein ZVTitel existiert. Dabei ist die zvKontoId, der Titel und der Untertitel entscheidend.
	 * @param titel
	 * @return id vom ZVTitel
	 * @throws ApplicationServerException
	 */
	public int existsZVTitel( ZVTitel titel ) throws ApplicationServerException {
		try{
			Object[] parameters = {new Integer(titel.getZVKonto().getId()), titel.getTitel(), titel.getUntertitel()};
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
	 * Abfrage ob ein gelöschter ZVTitel existiert. Dabei ist die zvKontoId, der Titel und der Untertitel entscheidend.
	 * @param titel
	 * @return id vom ZVTitel
	 * @throws ApplicationServerException
	 */
	public int existsDeleteZVTitel( ZVTitel titel ) throws ApplicationServerException {
		try{
			Object[] parameters = {new Integer(titel.getZVKonto().getId()), titel.getTitel(), titel.getUntertitel()};
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
	 * Anzahl der Bestellungen ermitteln, bei denen ein bestimmter ZVTitel angegeben wurde
	 */
	public int countBestellungen( ZVUntertitel konto )throws ApplicationServerException{
		try{
		  Object[] parameters = { new Integer(konto.getId()) };
			ResultSet rs = statements.get(214).executeQuery(parameters);
			if(rs.next())
				return rs.getInt(1);
			else
				return 0;
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Anzahl der aktiven Bestellungen ermitteln, bei denen ein bestimmter ZVTitel angegeben wurde
	 */
	public int countActiveBestellungen( ZVUntertitel konto )throws ApplicationServerException{
		try{
		  Object[] parameters = { new Integer(konto.getId()) };
			ResultSet rs = statements.get(215).executeQuery(parameters);
			if(rs.next())
				return rs.getInt(1);
			else
				return 0;
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Anzahl der Buchungen ermitteln, bei denen ein bestimmter ZVTitel angegeben wurde
	 */
	public int countBuchungen( ZVUntertitel konto )throws ApplicationServerException{
		try{
		  Object[] parameters = { new Integer(konto.getId()) };
			ResultSet rs = statements.get(222).executeQuery(parameters);
			if(rs.next())
				return rs.getInt(1);
			else
				return 0;
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Anzahl der Kontenzuordnungen ermitteln, bei denen ein bestimmtes ZVKonto angegeben wurde
	 */
	public int countKontenzuordnungen( ZVKonto konto )throws ApplicationServerException{
		try{
		  Object[] parameters = { new Integer(konto.getId()) };
			ResultSet rs = statements.get(235).executeQuery(parameters);
			if(rs.next())
				return rs.getInt(1);
			else
				return 0;
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}
	
	/**
	 * Anzahl der Kontenzuordnungen ermitteln, bei denen ein bestimmtes ZVKonto angegeben wurde
	 */
	public int countZVKonten( ZVKonto konto )throws ApplicationServerException{
		try{
		  Object[] parameters = { new Integer(konto.getId()) };
			ResultSet rs = statements.get(237).executeQuery(parameters);
			if(rs.next())
				return rs.getInt(1);
			else
				return 0;
	  } catch (SQLException e){
		  throw new ApplicationServerException(0, e.getMessage());
	  }
	}


	/**
	 * Ein neuen ZVTitel zu einem ZVKonto erstellen erstellen.
	 * @param konto
	 * @return
	 * @throws ApplicationServerException
	 */
	public int insertZVTitel( ZVTitel konto ) throws ApplicationServerException {
		try{
			Object[] parameters ={new Integer(konto.getZVKonto().getId()), konto.getBezeichnung(), konto.getTitel(),
								konto.getUntertitel(), new Float(konto.getBudget()), konto.getBemerkung(), konto.getBedingung()};
			statements.get(143).executeUpdate(parameters);

			return existsZVTitel( konto );
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Ein neuen ZVUntertitel zu einem ZVTitel erstellen erstellen.
	 * @param konto
	 * @return
	 * @throws ApplicationServerException
	 */
	public int insertZVUntertitel( ZVUntertitel konto ) throws ApplicationServerException {
		try{
			Object[] parameters ={new Integer(konto.getZVTitel().getZVKonto().getId()), konto.getBezeichnung(), konto.getTitel(),
								konto.getUntertitel(), new Float(konto.getBudget()), konto.getBemerkung(), konto.getBedingung()};
			statements.get(143).executeUpdate(parameters);

			return existsZVUntertitel( konto );
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Ein ZVKonto in der Datenbank löschen.
	 * @param konto
	 * @return
	 * @throws ApplicationServerException
	 */
	public int deleteZVKonto( ZVKonto konto ) throws ApplicationServerException {

		try{
			Object[] parameters = {new Integer(konto.getId())};
			return statements.get(123).executeUpdate(parameters);
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Einen ZVTitel in der Datenbank löschen.
	 * @param titel
	 * @return
	 * @throws ApplicationServerException
	 */
	public int deleteZVTitel( ZVUntertitel titel ) throws ApplicationServerException {

		try{
			Object[] parameters = {new Integer(titel.getId())};
			return statements.get(144).executeUpdate(parameters);
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Einen ZVUntertitel in der Datenbank löschen.
	 * @param titel
	 * @return
	 * @throws ApplicationServerException
	 */
	public int deleteZVUntertitel( ZVUntertitel titel ) throws ApplicationServerException {

		try{
			Object[] parameters = {new Integer(titel.getId())};
			return statements.get(144).executeUpdate(parameters);
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}

	}

	/**
	 * Ein ZVKonto in der Datenbank aktualisieren.
	 * @param konto
	 * @return
	 * @throws ApplicationServerException
	 */
	public int updateZVKonto( ZVKonto konto ) throws ApplicationServerException {
		// Verarbeitung des von sql-anweisungen und des ergebnisses
		try{
			Object[] parameters = {new Integer(konto.getHaushaltsJahrId()),  konto.getBezeichnung(), konto.getKapitel(),
							konto.getTitelgruppe(), new Float(konto.getTgrBudget()), new Float(konto.getDispoLimit()),
							(konto.getZweckgebunden() ? "1" : "0"), ""+konto.getFreigegeben(), ""+konto.getUebernahmeStatus(),
							(konto.getGeloescht() ? "1" : "0"), new Integer(konto.getId())};

			statements.get(124).executeUpdate(parameters);
			if( konto.getGeloescht() )
				return existsDeleteZVKonto( konto );
			else
				return existsZVKonto( konto );
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Einen ZVTitel in der Datenbank aktualisieren.
	 * @param titel
	 * @return
	 * @throws ApplicationServerException
	 */
	public int updateZVTitel( ZVTitel titel ) throws ApplicationServerException {
		// Verarbeitung des von sql-anweisungen und des ergebnisses
		try{
			Object[] parameters = {new Integer(titel.getZVKonto().getId()), titel.getBezeichnung(), titel.getTitel(),
							titel.getUntertitel(), new Float(titel.getBudget()), titel.getBemerkung(),
							titel.getBedingung(), (titel.getGeloescht() ? "1" : "0"), new Integer(titel.getId())};

			statements.get(145).executeUpdate(parameters);
			if( titel.getGeloescht() )
				return existsDeleteZVTitel( titel );
			else
				return existsZVTitel( titel );
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}

	/**
	 * Einen ZVUntertitel in der Datenbank aktualisieren.
	 * @param titel
	 * @return
	 * @throws ApplicationServerException
	 */
	public int updateZVUntertitel( ZVUntertitel titel ) throws ApplicationServerException {
		// Verarbeitung des von sql-anweisungen und des ergebnisses
		try{
			Object[] parameters = {new Integer( titel.getZVTitel().getZVKonto().getId()), titel.getBezeichnung(), titel.getTitel(),
							titel.getUntertitel(), new Float(titel.getBudget()), titel.getBemerkung(),
							titel.getBedingung(), (titel.getGeloescht() ? "1" : "0"), new Integer(titel.getId())};

			statements.get(145).executeUpdate(parameters);
			if( titel.getGeloescht() )
				return existsDeleteZVUntertitel( titel );
			else
				return existsZVUntertitel( titel );
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	
	/**
	 * Ein ZVKonto zum Aktualisieren auswählen
	 */
	public ZVKonto selectForUpdateZVKonto( ZVKonto zvKonto ) throws ApplicationServerException {
		ZVKonto result = null;
		
		try {
			Object[] parameters = {new Integer( zvKonto.getId() )};
			ResultSet rs = statements.get(125).executeQuery(parameters);
			rs.last();

			if ( rs.getRow() > 0 ){
				rs.beforeFirst();
				rs.next();

				result = new ZVKonto( zvKonto.getId(), rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
									rs.getFloat(5), rs.getFloat(6), !rs.getString(7).equalsIgnoreCase( "0" ),
									rs.getString(8).charAt(0), rs.getShort(9), !rs.getString(10).equalsIgnoreCase( "0" ) );
			}
			rs.close();

		} catch(SQLException e) {
			throw new ApplicationServerException( 0, e.getMessage() );
		}
		
		return result;
	}
	
	/**
	 * Einen ZVTitel zum Aktualisieren auswählen
	 */
	public ZVTitel selectForUpdateZVTitel( ZVTitel zvTitel ) throws ApplicationServerException {
		ZVTitel result = null;
		
		try {
			Object[] parameters = {new Integer( zvTitel.getId() )};
			ResultSet rs = statements.get(146).executeQuery(parameters);
			rs.last();

			if ( rs.getRow() > 0 ){
				rs.beforeFirst();
				rs.next();

				result = new ZVTitel( zvTitel.getId(), zvTitel.getZVKonto(), rs.getString(2), rs.getString(3),rs.getString(4),
									rs.getFloat(5), rs.getString(6), rs.getString(7), !rs.getString(8).equalsIgnoreCase( "0" ) );
			}
			rs.close();
		} catch(SQLException e) {
			throw new ApplicationServerException( 0, e.getMessage() );
		}
		
		return result;
	}
	
	/**
	 * Einen ZVUntertitel zum Aktualisieren auswählen
	 */
	public ZVUntertitel selectForUpdateZVUntertitel( ZVUntertitel zvUntertitel ) throws ApplicationServerException {
		ZVUntertitel result = null;
		
		try {
			Object[] parameters = {new Integer( zvUntertitel.getId() )};
			ResultSet rs = statements.get(146).executeQuery(parameters);
			rs.last();

			if ( rs.getRow() > 0 ){
				rs.beforeFirst();
				rs.next();

				result = new ZVUntertitel( zvUntertitel.getId(), zvUntertitel.getZVTitel(), rs.getString(2), rs.getString(3),
					rs.getString(4), rs.getFloat(5), rs.getString(6), rs.getString(7), !rs.getString(8).equalsIgnoreCase( "0" ) );
			}
			rs.close();
		} catch(SQLException e) {
			throw new ApplicationServerException( 0, e.getMessage() );
		}
		
		return result;
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
	 * @return ArrayList mit nicht gelöschten Firmen.
	 * @throws ApplicationServerException
	 */
	public ArrayList selectFirmen() throws ApplicationServerException {
		ArrayList firmen = new ArrayList();

		try{
			ResultSet rs = statements.get(240).executeQuery();
			rs.last();

			if ( rs.getRow() > 0 ){
				rs.beforeFirst();

				while( rs.next() ){
					firmen.add( new Firma( rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
											rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
											rs.getString(10), !rs.getString(11).equalsIgnoreCase( "0" ) ) );
				}
			}

			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return firmen;
	}
	
	/**
	 * Abfrage aller gelöschter Firmen.
	 * @return ArrayList mit gelöschten Firmen.
	 * @throws ApplicationServerException
	 */
	public ArrayList selectDelFirmen() throws ApplicationServerException {
		ArrayList firmen = new ArrayList();

		try{
			ResultSet rs = statements.get(241).executeQuery();
			rs.last();

			if ( rs.getRow() > 0 ){
				rs.beforeFirst();

				while( rs.next() ){
					firmen.add( new Firma( rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
											rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
											rs.getString(10), !rs.getString(11).equalsIgnoreCase( "0" ) ) );
				}
			}

			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return firmen;
	}
	
	/**
	 * Eine Firma zum aktualisieren auswählen.
	 * @return Firma, die ausgewählt wurde.
	 * @throws ApplicationServerException
	 */
	public Firma selectForUpdateFirma( Firma firma ) throws ApplicationServerException {
		Firma result = null;

		try{
			Object[] parameters = {new Integer(firma.getId())};
			ResultSet rs = statements.get(242).executeQuery(parameters);
			rs.last();

			if ( rs.getRow() > 0 ){
				rs.beforeFirst();
				rs.next();
				
				result = new Firma( rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
									rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
									rs.getString(10), !rs.getString(11).equalsIgnoreCase( "0" ) );
			}
			
			rs.close();
		} catch (SQLException e){
			throw new ApplicationServerException( 1, e.getMessage() );
		}
		
		return result;
	}
	
	/**
	 * Abfrage ob eine nicht gelöschte Firma schon existiert.
	 * Dabei ist der Namen, Strasse, PLZ, Ort entscheidend. 
	 * @return id von der Firma
	 * @throws ApplicationServerException
	 */
	public int existsFirma( Firma firma ) throws ApplicationServerException {
		try{
			Object[] parameters = {new String(firma.getName()), new String(firma.getStrasseNr()),
									new String(firma.getPlz()), new String(firma.getOrt())};
			ResultSet rs = statements.get(243).executeQuery(parameters);
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
	 * Abfrage ob eine gelöschte Firma schon existiert.
	 * Dabei ist der Namen, Strasse, PLZ, Ort entscheidend. 
	 * @return id von der Firma
	 * @throws ApplicationServerException
	 */
	public int existsDelFirma( Firma firma ) throws ApplicationServerException {
		try{
			Object[] parameters = {new String(firma.getName()), new String(firma.getStrasseNr()),
									new String(firma.getPlz()), new String(firma.getOrt())};
			ResultSet rs = statements.get(244).executeQuery(parameters);
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
	 * Eine Firma in der Datenbank aktualisieren.
	 * @param Firma
	 * @return id von der Firma, die aktulisiert wurde
	 * @throws ApplicationServerException
	 */
	public int updateFirma( Firma firma ) throws ApplicationServerException {
		try{
			Object[] parameters = { firma.getName(), firma.getStrasseNr(), firma.getPlz(), firma.getOrt(), firma.getKundenNr(),
									firma.getTelNr(), firma.getFaxNr(), firma.getEMail(), firma.getWWW(), 
									new String(firma.getGeloescht() ? "1" : "0"), new Integer( firma.getId() ) };
			statements.get(245).executeUpdate(parameters);
			
			if( firma.getGeloescht() )
				return existsDelFirma( firma );
			else
				return existsFirma( firma );
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	/**
	 * Ein neue Firma erstellen.
	 * @param neue Firma.
	 * @return id der erstellten Firma.
	 * @throws ApplicationServerException
	 */
	 public int insertFirma( Firma firma ) throws ApplicationServerException {
		 try{
			Object[] parameters = { firma.getName(), firma.getStrasseNr(), firma.getPlz(), firma.getOrt(), firma.getKundenNr(),
									firma.getTelNr(), firma.getFaxNr(), firma.getEMail(), firma.getWWW(), 
									new String( firma.getGeloescht() ? "1" : "0" ) };

			statements.get(246).executeUpdate(parameters);
			
			return existsFirma( firma );
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}
	}
	
	/**
	 * Eine Firma in der Datenbank löschen.
	 * @param firma, die gelöscht werden soll.
	 * @return Zeilenindex von der Firma, die gelöscht wurde.
	 * @throws ApplicationServerException
	 */
	public int deleteFirma( Firma firma ) throws ApplicationServerException {

		try{
			Object[] parameters = {new Integer(firma.getId())};
			return statements.get(247).executeUpdate(parameters);
		} catch (SQLException e){
			throw new ApplicationServerException( 0, e.getMessage() );
		}

	}
	
	/**
	 * Anzahl der aktiven Bestellungen, die bei einer bestimmten Firma bestellt werden.
	 * @param die Firma.
	 * @return anzahl der Bestellungen.
	 */
	public int countActiveBestellungen( Firma firma )throws ApplicationServerException{
		 try{
		   Object[] parameters = { new Integer(firma.getId()) };
			 ResultSet rs = statements.get(216).executeQuery(parameters);
		   if(rs.next())
			 return rs.getInt(1);
		   else
				return 0;

		 } catch (SQLException e){
		   throw new ApplicationServerException(1, e.getMessage());
	   }
	}
	
	/**
	 * Anzahl der inaktiven Bestellungen, die bei einer bestimmten Firma bestellt werden.
	 * @param die Firma.
	 * @return anzahl der Bestellungen.
	 */
	public int countInactiveBestellungen( Firma firma )throws ApplicationServerException{
		 try{
		   Object[] parameters = { new Integer(firma.getId()) };
			 ResultSet rs = statements.get(216).executeQuery(parameters);
		   if(rs.next())
			 return rs.getInt(1);
		   else
				return 0;

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

}