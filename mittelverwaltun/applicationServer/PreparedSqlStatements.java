/*
 * Created on 01.09.2004
 */
package applicationServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Types;


/**
 */
public class PreparedSqlStatements {

	private PreparedStatementWrapper[] statements;

	public PreparedSqlStatements (Connection con) throws SQLException{
		PreparedStatement ps;

		statements = new PreparedStatementWrapper[360];
		int i = 0;

		/**************************************/
		/* Tabelle: Allgemeine SQL-Statements */
		/* Indizes: 0-9	                      */
		/**************************************/
		{//0				(34)
			ps = con.prepareStatement( "FLUSH PRIVILEGES");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//1
			ps = con.prepareStatement( "COMMIT");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//2
			ps = con.prepareStatement( "ROLLBACK");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//3 setzt autocommit auf 0 (ist für ein Rollback gedacht, damit die geänderten Daten auch rückgängig gemacht werden können
			ps = con.prepareStatement( "SET AUTOCOMMIT = 0");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//4
			ps = con.prepareStatement( "BEGIN");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//5 setzt autocommit auf 1
			ps = con.prepareStatement( "SET AUTOCOMMIT = 1");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//6 gibt den Session user zurück
			ps = con.prepareStatement( "SELECT SESSION_USER()");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//7 Locks für Haushaltsjahresabschluss
			ps = con.prepareStatement(
					"LOCK TABLES " +
					    "zvkonten_tmp WRITE, " +
						"zvkonten WRITE, " +
						"zvkonten AS zk WRITE, " +
						"zvkonten AS zk1 WRITE, " +
						"zvkonten AS zk2 WRITE, " +
						"zvkontentitel WRITE, " +
						"zvkontentitel AS zt WRITE, " +
						"zvkontentitel AS zt1 WRITE, " +
						"zvkontentitel AS zt2 WRITE, " +
						"zvkontentitel AS zt3 WRITE, " +
						"zvkontentitel AS zt4 WRITE, " +
						"zvkontentitel AS ztold WRITE, " +
						"zvkontentitel AS ztnew WRITE, " +
						"fbkonten WRITE, " +
						"fbkonten AS fk WRITE, " +
						"fbkonten AS fk1 WRITE, " +
						"fbkonten AS fk2 WRITE, " +
						"fbkonten AS fkmain WRITE, " +
						"fbkonten AS fkold WRITE, " +
						"fbkonten AS fknew WRITE, " +
						"kontenzuordnung WRITE, " +
						"kontenzuordnung AS kz WRITE, " +
						"kontenzuordnung AS kz1 WRITE, " +
						"kontenzuordnung AS kz2 WRITE, " +
						"haushaltsjahre WRITE, " +
						"haushaltsjahre AS h WRITE, " +
						"haushaltsjahre AS old WRITE, " +
						"haushaltsjahre AS new WRITE");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//8 Unlock
			ps = con.prepareStatement("UNLOCK TABLES");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//9
			statements[i++] = null;
		}

		/**************************************/
		/* Tabelle: Aktivitaeten              */
		/* Indizes: 10-19                     */
		/**************************************/
		{//10
			ps = con.prepareStatement( "SELECT * " +
										 "FROM Aktivitaeten "+
									 "ORDER BY bezeichnung " );
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//11
			statements[i++] = null;
		}
		{//12
			statements[i++] = null;
		}
		{//13
			statements[i++] = null;
		}
		{//14
			statements[i++] = null;
		}
		{//15
			statements[i++] = null;
		}
		{//16
			statements[i++] = null;
		}
		{//17
			statements[i++] = null;
		}
		{//18
			statements[i++] = null;
		}
		{//19
			statements[i++] = null;
		}


		/**************************************/
		/* Tabelle: Benutzer                  */
		/* Indizes: 20-39                     */
		/**************************************/
		{//20			
			ps = con.prepareStatement("SELECT * FROM Benutzer WHERE id = ? AND geloescht = '0'");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//21	aktualisiert einen Benutzer anhand der Id, keine Aktualisierung des Passworts
			ps = con.prepareStatement("UPDATE Benutzer " +
										 "SET " +
												"benutzername = ?, " +
												"rollenId = ?, " +
												"institutsId = ?, " +
												"titel = ?, " +
												"name = ?, " +
												"vorname = ?, " +
												"email = ?, " +
												"privatKontoId = ?, telefon = ?, fax = ?, bau = ?, raum = ?, swBeauftragter = ?, sichtbarkeit = ? " +
									   "WHERE id = ?");
			int[] param = {Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
										 Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
										 Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//22	gibt alle Software-Beauftragte des Fachbereichs zurück
			ps = con.prepareStatement( "SELECT id, benutzername, name, vorname " +
																"FROM Benutzer " +
																"WHERE swBeauftragter = '1' " +																	"AND geloescht = 0 " );
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//23	
			ps = con.prepareStatement("UPDATE Benutzer " +
										 "SET passwort = ? " +
									   "WHERE id = ?");
			int[] param = {Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//24	
			ps = con.prepareStatement(	"INSERT " +
										  "INTO Benutzer " +
												"(benutzername, passwort, rollenId, institutsId, titel, " +
												"name, vorname, email, privatKontoId, telefon, fax, bau, raum, swBeauftragter, sichtbarkeit)" +
										"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
										Statement.RETURN_GENERATED_KEYS);
			int[] param = {Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR,
										 Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, 
										 Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//25	
			statements[i++] = null;
		}

		{//26			(6)
			ps = con.prepareStatement("DELETE " +
										"FROM Benutzer " +
									   "WHERE id = ? " +
										 "AND benutzername = ?");
			int[] param = {Types.INTEGER, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//27			(7)
				ps = con.prepareStatement("SELECT * " +
											"FROM Benutzer " +
										   "WHERE id != ? " +
										   	 "AND benutzername = ? " +
												 "AND geloescht = '0'");
				int[] param = {Types.INTEGER, Types.VARCHAR};
				statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//28 
			statements[i++] = null;
		}
		{//29
			ps = con.prepareStatement("UPDATE Benutzer " +
										 "SET geloescht = 1 " +
									   "WHERE id = ?");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//30 Gibt die Benutzer eines Instituts zurück mit einer bestimmten Rolle zurück
			ps = con.prepareStatement("SELECT b.benutzername, b.passwort, b.vorname, b.name " +
										"FROM Benutzer b, Institute a " +
										"WHERE a.id = ? AND a.id = b.institutsid AND b.geloescht = '0' AND b.rollenid = ?");
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//31
			/**
			 * Anzahl der nicht gelöschten Benutzer, die ein bestimmtes FBKonto besitzen.
			 * @author w.flat
			 */
			ps = con.prepareStatement( "SELECT COUNT(b.privatKontoId) " +
										"FROM FBKonten fk, Benutzer b " +
										"WHERE fk.id = ? " +
										"AND fk.id = b.privatKontoId AND b.geloescht != '0'" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//32
			/**
			 * Anzahl der Benutzer(aktiv und gelöscht), die ein bestimmtes FBKonto besitzen.
			 * @author w.flat
			 */
			ps = con.prepareStatement( "SELECT COUNT(b.privatKontoId) " +
										"FROM FBKonten fk, Benutzer b " +
										"WHERE fk.id = ? " +
										"AND fk.id = b.privatKontoId" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//33
			statements[i++] = null;
		}
		{//34
			statements[i++] = null;
		}
		{//35
			statements[i++] = null;
		}
		{//36
			statements[i++] = null;
		}
		{//37
			statements[i++] = null;
		}
		{//38
			statements[i++] = null;
		}
		{//39
			statements[i++] = null;
		}

		/**************************************/
		/* Tabelle: Fachbereiche              */
		/* Indizes: 40-49                     */
		/**************************************/
		{//40			(8)
			ps = con.prepareStatement( "UPDATE Fachbereiche " +
										  "SET institutsid = ?, " +
											  "bezeichnung = ?, " +
											  "profPauschale = ?, " +
												"anschrift_1 = ?, " +
												"anschrift_2 = ?, " +
												"hochschule_1 = ?, " +
												"hochschule_2 = ? ");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.FLOAT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//41
			statements[i++] = null;
		}
		{//42
			statements[i++] = null;
		}
		{//43
			statements[i++] = null;
		}
		{//44
			statements[i++] = null;
		}
		{//45
			statements[i++] = null;
		}
		{//46
			statements[i++] = null;
		}
		{//47
			statements[i++] = null;
		}
		{//48
			statements[i++] = null;
		}
		{//49
			statements[i++] = null;
		}
		/**************************************/
		/* Tabelle: FBKonten                  */
		/* Indizes: 50-69				 	  */
		/**************************************/
		{//50			(9)
			/**
			 * Abfrage aller Hauptkonten, die zu einem bestimmten Institut angehören <br>
			 * und nicht gelöscht sind. 
	 		 * @author w.flat
			 */
			ps = con.prepareStatement("SELECT DISTINCT(fk.id), fk.haushaltsjahrId, fk.institutsId, fk.bezeichnung, "+
										"fk.hauptkonto, fk.unterkonto, fk.budget, fk.dispoLimit, fk.vormerkungen, " +										"fk.pruefBedingung, fk.kleinbestellungen " +
										"FROM FBKonten fk, Haushaltsjahre h " +
										"WHERE fk.institutsID = ? AND h.status = '0' AND fk.haushaltsjahrId = h.id " +
										"AND fk.unterkonto = '0000' AND fk.geloescht = '0'");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//51			(10)
			/**
			 * Abfrage aller FBUnterkonten, die zu einem bestimmten Institut und einem <br> 
			 * bestimmten FBHauptkonto angehören und nicht gelöscht sind. 
			 * @author w.flat
			 */
			ps = con.prepareStatement("SELECT DISTINCT(fk1.id), fk1.haushaltsjahrId, fk1.institutsId, fk1.bezeichnung, " +
										"fk1.hauptkonto, fk1.unterkonto, fk1.budget, fk1.vormerkungen, fk1.kleinbestellungen " +
										"FROM FBKonten fk1, FBKonten fk2, Haushaltsjahre h " +
										"WHERE fk1.institutsID = ? AND fk1.institutsID = fk2.institutsID " +
										"AND fk2.id = ? AND fk1.hauptkonto = fk2.hauptkonto AND fk1.unterkonto != '0000' " +
										"AND h.id = fk1.haushaltsjahrId AND h.id = fk2.haushaltsjahrId AND h.status = '0' " +
										"AND fk1.geloescht = '0'");
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//52			(11)
			/**
			 * Einfügen eines FBKontos in die Datenbank. 
			 * @author w.flat
			 */
			ps = con.prepareStatement("INSERT INTO FBKonten " +
										 "(haushaltsjahrId, institutsID, bezeichnung, hauptkonto, " +
										 "unterkonto, budget, dispoLimit, vormerkungen, pruefBedingung," +										 "kleinbestellungen, geloescht) " +
									  "VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			int[] param = {Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.FLOAT, Types.FLOAT, Types.FLOAT, Types.VARCHAR,
							Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//53			(12)
			/**
			 * Abfrage der FBKontoId, von einem bestimmten FBKonto und welches nicht gelöscht ist. 
			 * @author w.flat
			 */
			ps = con.prepareStatement("SELECT fk.id FROM FBKonten fk, Haushaltsjahre h "+
										"WHERE fk.institutsID = ? AND fk.hauptkonto = ? AND fk.unterkonto = ? " +
										"AND h.id = fk.haushaltsjahrId AND h.status = '0' AND fk.geloescht = '0'");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//54			(13)
			ps = con.prepareStatement("DELETE FROM FBKonten " +
									   "WHERE id = ? ");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//55			(14)
			/**
			 * Aktualisieren eines FBKontos in der Datenbank. 
			 * @author w.flat
			 */
			ps = con.prepareStatement("UPDATE FBKonten " +
										"SET haushaltsjahrId = ?, "+
											"institutsId = ?, " +
											"bezeichnung = ?, " +
											"hauptkonto = ?, " +
											"unterkonto = ?, " +
											"budget = ?, " +
											"dispoLimit = ?, " +
											"vormerkungen = ?, " +
											"pruefBedingung = ?, " +
											"kleinbestellungen = ?, " +
											"geloescht = ? " +
										"WHERE id = ?");
			int[] param = {Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.FLOAT,
							Types.FLOAT, Types.FLOAT, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//56			(37)
			/**
			 * Abfrage der FBKonten die im bestimmten Institut sind und für Kleinbestellungen freigegeben sind. <br>
			 * Diese Konten dürfen außer einem bestimmten Benutzer nicht zugeordnet sein.
			 * @author w.flat
			 */
			ps = con.prepareStatement(	"SELECT DISTINCT(fk.id), fk.haushaltsjahrId, fk.institutsId, fk.bezeichnung, " + 
											"fk.hauptkonto, fk.unterkonto, fk.budget, fk.dispoLimit, fk.vormerkungen, " + 											"fk.pruefBedingung, fk.kleinbestellungen, fk.geloescht " + 
										"FROM FBKonten fk, Haushaltsjahre h " + 
										"WHERE fk.institutsId = ? AND fk.hauptkonto = ? AND fk.kleinbestellungen = '1' AND " + 											"fk.geloescht = '0' AND fk.unterkonto != '0000' AND " +
											"h.id = fk.haushaltsjahrId AND h.status = '0' AND " +
											"fk.id NOT IN (SELECT DISTINCT(fk1.id) " + 														"FROM FBKonten fk1, Benutzer b " + 
														"WHERE fk1.hauptkonto = ? AND fk1.unterkonto != \"0000\" AND " + 															"fk1.institutsId = b.institutsId AND " + 
															"fk1.kleinbestellungen = '1' AND " + 															"fk1.id = b.privatKontoId AND " + 
															"b.id != ? AND fk1.geloescht = '0')" ); 
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//57			(38)
			statements[i++] = null;
		}
		{//58
			/**
			 * Abfrage eines bestimmten FBKontos zum Aktualisieren. 
			 * @author w.flat
			 */
			ps = con.prepareStatement( "SELECT " +
										"haushaltsjahrId , institutsId, bezeichnung, " +
										"hauptkonto, unterkonto, budget, dispoLimit, vormerkungen, " +										"pruefBedingung, kleinbestellungen, geloescht " +
										"FROM FBKonten WHERE id = ? FOR UPDATE");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//59
			/**
			 * Abfrage eines bestimmten FBKontos und welches gelöscht ist. 
			 * @author w.flat
			 */
			ps = con.prepareStatement(	"SELECT fk.id FROM FBKonten fk, Haushaltsjahre h "+
											"WHERE fk.institutsID = ? AND fk.hauptkonto = ? AND fk.unterkonto = ? " +
											"AND h.id = fk.haushaltsjahrId AND h.status = '0' AND fk.geloescht = '1'");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//60
			ps = con.prepareStatement( "SELECT " +
											  "distinct fk.id, fk.haushaltsjahrId, fk.institutsId, fk.bezeichnung, " +
												  "fk.hauptkonto, fk.unterkonto, fk.budget, fk.dispoLimit, fk.pruefBedingung " +
											 "FROM FBKonten fk, Kontenzuordnung kz, ZVKonten zk, Haushaltsjahre h " +
										"WHERE fk.institutsID = ? " +
										  "AND fk.unterkonto = \"0000\" " +
										  "AND fk.geloescht = \"0\" " +
										  "AND fk.id = kz.fbkontoid " +
										  "AND kz.zvkontoid = zk.id " +
										  "AND zk.zweckgebunden = \"0\" " +
										  "AND fk.haushaltsjahrid = h.id " +
										  "AND h.status = '0'");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//61
			/**
			 * Abfrage aller (nicht abgeschlossenen) FBHauptkonten eines Haushaltsjahres 
	 		 * @author m.schmitt
			 */
			ps = con.prepareStatement("SELECT fk.id, fk.haushaltsjahrId, i.id, fk.bezeichnung, "+
										"fk.hauptkonto, fk.unterkonto, fk.budget, fk.dispoLimit, fk.vormerkungen, " +
										"fk.pruefBedingung, fk.kleinbestellungen, i.bezeichnung, i.kostenstelle " +
										"FROM FBKonten fk, Institute i " +
										"WHERE fk.haushaltsjahrID = ? " +
										"AND fk.institutsID = i. id " +
										"AND fk.unterkonto = \"0000\" " +
										"AND fk.geloescht = \"0\" " +
										"ORDER BY i.kostenstelle, fk.hauptkonto");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//62 Temporäre FBKontentabelle mit den Konten eines bestimmten Haushaltsjahres erstellen
			ps = con.prepareStatement("CREATE TABLE fbkonten_tmp "+
											       "SELECT * " + 
												     "FROM fbkonten "+
													"WHERE haushaltsjahrid = ?"
									  );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//63 Löscht temporäre FBKonten-Tabelle
			ps = con.prepareStatement("DROP TABLE IF EXISTS fbkonten_tmp");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//64 Portiert die Konten eines bestimmten Hauptkontos inklusive Budgets und Vormerkungen auf ein neues Haushaltsjahr
			ps = con.prepareStatement(
					"INSERT INTO fbkonten " +
								"(haushaltsjahrid, institutsid, hauptkonto, unterkonto, bezeichnung, " +
								" budget, vormerkungen, dispolimit, pruefbedingung, kleinbestellungen, " + 
								" geloescht)" +
						"SELECT   ?, f2.institutsid, f2.hauptkonto, f2.unterkonto, f2.bezeichnung, " +
								" f2.budget, f2.vormerkungen, f2.dispolimit, f2.pruefbedingung, f2.kleinbestellungen, " + 
								" f2.geloescht " +
						  "FROM   fbkonten_tmp f1, fbkonten_tmp f2 " +
						 "WHERE   f1.id = ? " +
						   "AND   f1.institutsid = f2.institutsid " +
						   "AND   f1.hauptkonto = f2.hauptkonto " +
			  		  "ORDER BY   f2.hauptkonto, f2.unterkonto");
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//65 Portiert die Konten eines bestimmten Hauptkontos ohne Budgets und Vormerkungen auf ein neues Haushaltsjahr
			ps = con.prepareStatement(
					"INSERT INTO fbkonten " +
								"(haushaltsjahrid, institutsid, hauptkonto, unterkonto, bezeichnung, " +
								" budget, vormerkungen, dispolimit, pruefbedingung, kleinbestellungen, " + 
								" geloescht)" +
						"SELECT   ?, f2.institutsid, f2.hauptkonto, f2.unterkonto, f2.bezeichnung, " +
								" 0, 0, f2.dispolimit, f2.pruefbedingung, f2.kleinbestellungen, " + 
								" f2.geloescht " +
						  "FROM   fbkonten_tmp f1, fbkonten_tmp f2 " +
						 "WHERE   f1.id = ? " +
						   "AND   f1.institutsid = f2.institutsid " +
						   "AND   f1.hauptkonto = f2.hauptkonto " +
					  "ORDER BY   f2.hauptkonto, f2.unterkonto");
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//66 Ermittelt der ID eines FB-Kontos dessen ID in einem anderen (zu übergebenden) Haushaltsjahr
			ps = con.prepareStatement(
					"SELECT fk2.id " +
					  "FROM FBKonten fk1, FBKonten fk2 " +
					 "WHERE fk1.id = ? " +
					   "AND fk1.institutsid = fk2.institutsid " +
					   "AND fk1.hauptkonto = fk2.hauptkonto " +
					   "AND fk1.unterkonto = fk2.unterkonto " +
					   "AND fk2.haushaltsjahrid = ? " +
					   "AND fk1.geloescht = '0' " +
					   "AND fk2.geloescht = '0' ");
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//67
			statements[i++] = null;
		}
		{//68 Gibt eine FBHauptkonto für ein PrivatKonto zurück
	    ps = con.prepareStatement("SELECT fk.id, fk.haushaltsjahrId, fk.institutsId, fk.bezeichnung, "+
																	 "fk.hauptkonto, fk.unterkonto, fk.budget, fk.dispoLimit, fk.pruefBedingung " +
																"FROM FBKonten fk, Haushaltsjahre h " +
																"WHERE fk.hauptkonto = ? " +
																	"AND fk.institutsId = ? " +
																	"AND fk.unterkonto = \"0000\" " +
																	"AND h.status = 0 " +
																  "AND fk.geloescht = '0'");
			int[] param = {Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//69 gibt ein FBKonto zurück
			ps = con.prepareStatement("SELECT id, haushaltsjahrId, institutsId, bezeichnung, "+
											 "hauptkonto, unterkonto, budget, dispoLimit, pruefBedingung " +
										"FROM FBKonten " +
										"WHERE id = ? " +
										  "AND geloescht = '0'");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}

		/**************************************/
		/* Tabelle: Haushaltsjahre            */
		/* Indizes: 70-79                     */
		/**************************************/
		{//70				(15)
			ps = con.prepareStatement( "SELECT " +
											  "beschreibung, " +
											  "DATE_FORMAT(beginn,'%d.%m.%Y'), " +
											  "DATE_FORMAT(ende,'%d.%m.%Y') " +
										 "FROM Haushaltsjahre " +
										"WHERE status = 0");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//71				(16)
			ps = con.prepareStatement( "UPDATE Haushaltsjahre " +
										  "SET beschreibung = ?, " +
											  "beginn = STR_TO_DATE(?,'%d.%m.%Y'), " +
											  "ende = STR_TO_DATE(?,'%d.%m.%Y') " +
										"WHERE status = 0");
			int[] param = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//72	SELECT FOR UPDATE
			ps = con.prepareStatement( "SELECT beschreibung, " +
											  "DATE_FORMAT(beginn,'%d.%m.%Y'), " +
											  "DATE_FORMAT(ende,'%d.%m.%Y') " +
										 "FROM Haushaltsjahre " +
										"WHERE status = 0 FOR UPDATE");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//73
			ps = con.prepareStatement( "SELECT id " +
										 "FROM Haushaltsjahre " +
										"WHERE status = 0");
			statements[i++] = new PreparedStatementWrapper(ps);
		}		
		{//74 Fügt ein neues Haushaltsjahr ein
			ps = con.prepareStatement(
					"INSERT INTO Haushaltsjahre " +
								"(beginn, status) " +
						 "VALUES (?,?)");
			int[] param = {Types.DATE, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//75 Aktualisiert den Status eines Haushaltsjahres
			ps = con.prepareStatement(
					"UPDATE Haushaltsjahre " +
					   "SET status = ? " +
					 "WHERE id = ? ");
			int[] param = {Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//76 Aktualisiert das Ende eines Haushaltsjahres
			ps = con.prepareStatement(
					"UPDATE Haushaltsjahre " +
					   "SET ende = ? " +
					 "WHERE id = ? ");
			int[] param = {Types.DATE, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//77
			ps = con.prepareStatement(
					"SELECT id, beginn, ende, status " +
					  "FROM Haushaltsjahre " +
					 "ORDER BY beginn DESC");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//78 Ermittelt anhand der ID des vorigen Haushaltsjahres die ID des folgenden Haushaltsjahres
			ps = con.prepareStatement(
					"SELECT new.id " +
					  "FROM Haushaltsjahre old, Haushaltsjahre new " +
					 "WHERE old.id = ? " +
					  "AND old.ende = new.beginn " +
					  "AND new.status = '0'");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//79
			statements[i++] = null;
		}
		/**************************************/
		/* Tabelle: Institute                 */
		/* Indizes: 80-89					  */
		/**************************************/
		{//80		gibt alle Institute zurück
			ps = con.prepareStatement("SELECT a.id, a.bezeichnung, a.kostenstelle, a.institutsleiter, " +
																			 "b.id, b.benutzername, b.name, b.vorname " +
																"FROM institute a, Benutzer b " + 
																"WHERE b.id = a.institutsleiter");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//81			(18)
			ps = con.prepareStatement("UPDATE Institute " +
										 "SET bezeichnung = ?, kostenstelle = ? , institutsleiter = ? " +
									   "WHERE id = ?");
			int[] param = {Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}

		{//82			(19)
			ps = con.prepareStatement("INSERT " +
										"INTO Institute " +
											 "(bezeichnung ,kostenstelle, institutsleiter) " +
									  "VALUES (?, ?, ?)",
									  Statement.RETURN_GENERATED_KEYS);
			int[] param = {Types.VARCHAR, Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//83			(20)
			ps = con.prepareStatement("DELETE " +
										"FROM Institute " +
									   "WHERE id = ? " +
									     "AND bezeichnung = ?");
			int[] param = {Types.INTEGER, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//84	Prüft ob es schon so ein Institut gibt mit der Bezeichnung oder Kostenstelle
			ps = con.prepareStatement("SELECT * " +
										"FROM Institute " +
									   "WHERE id != ? " +
									   	 "AND (bezeichnung = ? OR kostenstelle = ?)");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//85  SELECT FOR UPDATE
			ps = con.prepareStatement("SELECT id, bezeichnung, kostenstelle, geloescht " +
										"FROM Institute " +
									   "WHERE id = ? FOR UPDATE");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//86 gibt ein Institut mit Institutsleiter anhand der id zurück
			ps = con.prepareStatement("SELECT a.id, a.bezeichnung, a.kostenstelle, " +
																			 "b.id, b.benutzername, b.name, b.vorname " +
																"FROM institute a, Benutzer b " + 
																"WHERE b.id = a.institutsleiter " +																		"AND a.id = ?");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//87
			statements[i++] = null;
		}
		{//88
			statements[i++] = null;
		}
		{//89
			statements[i++] = null;
		}

		/**************************************/
		/* Tabelle: mysql.user                */
		/* Indizes: 90-99					  */
		/**************************************/
		{//90			(33)
			ps = con.prepareStatement( "GRANT RELOAD ON * . * TO ?@'%' IDENTIFIED BY '" + ApplicationServer.APPL_DB_PSWD + "' " +
																	"WITH GRANT OPTION MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0; ");
			int[] param = {Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//91			(35)
			ps = con.prepareStatement("GRANT SELECT, INSERT, UPDATE, DELETE ON `mysql` . * TO ?@'%'; ");
			int[] param = {Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//92			(36)
			ps = con.prepareStatement("GRANT ALL PRIVILEGES ON `" + ApplicationServer.APPL_DB_NAME + "` . * TO ?@'%' WITH GRANT OPTION ");
			int[] param = {Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//93
			ps = con.prepareStatement( "SELECT * FROM mysql.user WHERE `user` = ? "	);
			int[] param = {Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//94
			ps = con.prepareStatement("UPDATE mysql.user SET `user` = ?  WHERE `user` = ? ");
			int[] param = {Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//95
			ps = con.prepareStatement("UPDATE mysql.db SET `user` = ?  WHERE `user` = ? ");
			int[] param = {Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//96
			ps = con.prepareStatement("DELETE FROM mysql.user WHERE `User` = ? AND `Host` = '%';");
			int[] param = {Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//97
			ps = con.prepareStatement("DELETE FROM mysql.db WHERE `User` = ? AND `Host` = '%';");
			int[] param = {Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//98
			statements[i++] = null;
		}
		{//99
			statements[i++] = null;
		}

		/**************************************/
		/* Tabelle: Rollen                    */
		/* Indizes: 100-109					  */
		/**************************************/
		{//100			(22)
			ps = con.prepareStatement( "SELECT * " +
										 "FROM Rollen");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//101
			ps = con.prepareStatement("SELECT * " +
										"FROM Rollen " +
									   "WHERE id != ? " +
										 "AND bezeichnung = ?");
			int[] param = {Types.INTEGER, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//102
			ps = con.prepareStatement("INSERT " +
										"INTO Rollen " +
											 "(id ,bezeichnung) " +
									  "VALUES (?, ?)",
									  Statement.RETURN_GENERATED_KEYS);
			int[] param = {Types.INTEGER, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//103
			ps = con.prepareStatement("UPDATE Rollen " +
										 "SET bezeichnung = ?" +
									   "WHERE id = ?");
			int[] param = {Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//104
			ps = con.prepareStatement("DELETE " +
										"FROM Rollen " +
									   "WHERE id = ? " +
										 "AND bezeichnung = ?");
			int[] param = {Types.INTEGER, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//105
			ps = con.prepareStatement("SELECT * " +
										"FROM Rollen a, Benutzer b " +
									   "WHERE a.id = ? " +
										 "AND a.id = b.rollenId");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//106
			ps = con.prepareStatement( "SELECT * " +
										 "FROM Rollen " +
										 "WHERE id = ? FOR UPDATE");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//107
			statements[i++] = null;
		}
		{//108
			statements[i++] = null;
		}
		{//109
			statements[i++] = null;
		}

		/**************************************/
		/* Tabelle: Rollenaktivitaeten        */
		/* Indizes: 110-119					  */
		/**************************************/
		{//110			(23)
			ps = con.prepareStatement( "SELECT aktivitaetsID " +
										 "FROM Rollenaktivitaeten "+
										"WHERE rollenId = ? " +
									 "ORDER BY aktivitaetsID ");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//111			(28)
			ps = con.prepareStatement( "SELECT * " +
										 "FROM Rollenaktivitaeten " +
										"WHERE rollenId = ? " +
									 "ORDER BY aktivitaetsID " );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//112			(31)
			ps = con.prepareStatement( "INSERT " +
										 "INTO Rollenaktivitaeten " +
											  "(rollenId, aktivitaetsID) " +
									   "VALUES (?, ?)" );
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//113			(32)
			ps = con.prepareStatement( "DELETE "+
										 "FROM Rollenaktivitaeten " +
										"WHERE rollenId = ? " +
										 "AND aktivitaetsID = ?" );
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//114
			ps = con.prepareStatement("DELETE " +
										"FROM Rollenaktivitaeten " +
									   "WHERE rollenId = ?");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//115
			statements[i++] = null;
		}
		{//116
			statements[i++] = null;
		}
		{//117
			statements[i++] = null;
		}
		{//118
			statements[i++] = null;
		}
		{//119
			statements[i++] = null;
		}

		/**************************************/
		/* Tabelle: ZVKonten                  */
		/* Indizes 120-139					  */
		/**************************************/
		{//120			(39)
			/**
			 * Abfrage aller nicht gelöschten ZVKonten in der Datenbank.
			 * @author w.flat
			 */
			ps = con.prepareStatement( "SELECT DISTINCT(zk.id), zk.haushaltsjahrId, zk.bezeichnung, " +
											"zk.kapitel, zk.titelgruppe, zk.tgrBudget, " +
											"zk.dispoLimit, zk.zweckgebunden, zk.freigegeben, " +
											"zk.uebernahmestatus, zk.portiert, zk.abgeschlossen " +
										"FROM ZVKonten zk, Haushaltsjahre h " +
										"WHERE h.status = '0' AND zk.haushaltsjahrId = h.id AND zk.geloescht = '0'" );
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//121			(42)
			/**
			 * Neues ZVKonto erstellen.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"INSERT INTO ZVKonten " +
											"(haushaltsjahrid, bezeichnung, kapitel, " +
											"titelgruppe, tgrBudget, dispoLimit, "+
											"zweckgebunden, freigegeben, uebernahmestatus) " +
										"VALUES (?,?,?,?,?,?,?,?,?)" );
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.FLOAT, Types.FLOAT,
							Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//122			(43)
			/**
			 * Abfrage eines ZVKontos im aktuellen Haushaltsjahr mit einem bestimmten Kapitel
			 * und einer bestimmter Titelgruppe (Titelgruppenkonto) oder Titel (Titelkonto)und 
			 * welches nicht gelöscht oder abgeschlossen ist.
			 * @author w.flat 
			 */
			ps = con.prepareStatement(	"SELECT zk.id " +
										  "FROM zvkonten zk, zvkontentitel zt, haushaltsjahre h " +
										 "WHERE zk.kapitel = ? " +
										   "AND zk.titelgruppe = '' " +
										   "AND zk.id = zt.zvkontoid " +
										   "AND zt.titel = ? " +
										   "AND zt.untertitel = '' " +
										   "AND zk.haushaltsjahrid = h.id " +
										   "AND h.status = '0' " +
										   "AND zt.geloescht = '0' " +
										   "AND zk.geloescht = '0' " +
										   "AND zk.abgeschlossen = '0' " +
										 "UNION " +
										"SELECT zk.id " +
										  "FROM zvkonten zk, haushaltsjahre h " +
										 "WHERE zk.kapitel = ? " +
										   "AND zk.titelgruppe = ? " +
										   "AND NOT zk.titelgruppe = '' " +
										   "AND zk.haushaltsjahrid = h.id " +
										   "AND h.status = '0' " +
										   "AND zk.geloescht = '0' " +
										   "AND zk.abgeschlossen = '0'");
			//               Kapitel       Titel            Kapitel      Titelgruppe
			int[] param = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//123			(47)
			/**
			 * Ein ZVKonto mit der bestimmten ZVKontoId löschen.
			 * @author w.flat
			 */
			ps = con.prepareStatement("DELETE FROM ZVKonten WHERE id = ?");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//124			(49)
			/**
			 * Das ZVKonto mit einer bestimmten kontoId aktualisieren.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"UPDATE ZVKonten " +
										"SET haushaltsjahrid = ?, bezeichnung = ?, kapitel = ?, " +
											"titelgruppe = ?, tgrBudget = ?, dispoLimit = ?, " +
											"zweckgebunden = ?, freigegeben = ?, uebernahmestatus = ?, " +
											"portiert = ?,  abgeschlossen = ?, geloescht = ? " +
										"WHERE id = ?");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.FLOAT, Types.FLOAT,
							Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
							Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//125
			ps = con.prepareStatement( "SELECT haushaltsjahrid, bezeichnung, " +
											  "kapitel, titelgruppe, tgrBudget, " +
											  "dispoLimit, zweckgebunden, freigegeben, " +
											  "uebernahmestatus, portiert, abgeschlossen, geloescht " +
										 	"FROM ZVKonten " +
											"WHERE id = ? FOR UPDATE");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//126
			/**
			 * Abfrage eines ZVKontos mit bestimmtem Kapitel <br>
			 * und bestimmter Titelgruppe und welches gelöscht ist.
			 * @author w.flat
			 */
			ps = con.prepareStatement(	"SELECT zk.id " +
								  "FROM zvkonten zk, zvkontentitel zt, haushaltsjahre h " +
								 "WHERE zk.kapitel = ? " +
								   "AND zk.titelgruppe = '' " +
								   "AND zk.id = zt.zvkontoid " +
								   "AND zt.titel = ? " +
								   "AND zt.untertitel = '' " +
								   "AND zk.haushaltsjahrid = h.id " +
								   "AND h.status = '0' " +
								   "AND zt.geloescht = '0' " +
								   "AND zk.geloescht = '1' " +
								 "UNION " +
								"SELECT k.id " +
								  "FROM zvkonten zk, haushaltsjahre h " +
								 "WHERE zk.kapitel = ? " +
								   "AND zk.titelgruppe = ? " +
								   "AND NOT zk.titelgruppe = '' " +
								   "AND zk.haushaltsjahrid = h.id " +
								   "AND h.status = '0' " +
								   "AND zk.geloescht = '1' " );
			//               Kapitel       Titel            Kapitel      Titelgruppe
			int[] param = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//127			
			/**
			 * Abfrage aller nicht abgeschlossenen ZVKonten eines Haushaltsjahres.
			 * @author m.schmitt
			 */
			ps = con.prepareStatement( "SELECT id, haushaltsjahrid, bezeichnung, " +
											"kapitel, titelgruppe, tgrBudget, " +
											"dispoLimit, zweckgebunden, freigegeben, " +
											"uebernahmestatus, portiert, abgeschlossen " +
										"FROM ZVKonten " +
										"WHERE haushaltsjahrid = ? " +
										  "AND abgeschlossen = \"0\"" +
										  "AND geloescht = \"0\" "+ 
										  "ORDER BY kapitel, titelgruppe");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//128 Wurde das spezifizierte Konto bereits portiert?
			ps = con.prepareStatement("SELECT id " +
					 					"FROM zvkonten " +
									    "WHERE id = ? " +
									      "AND portiert = '1' " +
										  "AND geloescht = '0'");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//129 Portiert ein ZVKonto inklusive Budget
			ps = con.prepareStatement(
					 "INSERT INTO zvkonten " +
								 "(	haushaltsjahrid, kapitel, titelgruppe, tgrbudget, " +
								 "	bezeichnung, zweckgebunden, uebernahmestatus, " +
								 "	portiert, abgeschlossen, dispolimit, geloescht ) " +
					 "SELECT ?, kapitel, titelgruppe, tgrbudget, " +
					        "bezeichnung, zweckgebunden, '0', " +
							"'0', '0', dispolimit, '0' " +
					   "FROM zvkonten_tmp " +
					  "WHERE id = ?");
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//130 Portiert ein ZVKonto ohne Budget
			ps = con.prepareStatement(
					 "INSERT INTO zvkonten " +
								 "(	haushaltsjahrid, kapitel, titelgruppe, tgrbudget, " +
								 "	bezeichnung, zweckgebunden, uebernahmestatus, " +
								 "	portiert, abgeschlossen, dispolimit, geloescht ) " +
					 "SELECT ?, kapitel, titelgruppe, 0, " +
					        "bezeichnung, zweckgebunden, '0', " +
							"'0', '0', dispolimit, '0' " +
					   "FROM zvkonten_tmp " +
					  "WHERE id = ?");
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//131 Portiert die Titel eines ZVKontos mit Budgetübernahme auf eine neue Konto-Id
			ps = con.prepareStatement(
					"INSERT INTO zvkontentitel " + 
								"(zvkontoid, titel, untertitel, bezeichnung, " +
								" budget, vormerkungen, bemerkung, pruefbedingung, geloescht) " +
					"SELECT	?, titel, untertitel, bezeichnung, " +
							"budget, vormerkungen, bemerkung, pruefbedingung, geloescht " +
					  "FROM zvkontentitel_tmp " +
					 "WHERE zvkontoid = ?" );
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//132 Portiert die Titel eines ZVKontos ohne Budgetübernahme auf eine neue Konto-Id
			ps = con.prepareStatement(
					"INSERT INTO zvkontentitel " + 
								"(zvkontoid, titel, untertitel, bezeichnung, " +
								" budget, vormerkungen, bemerkung, pruefbedingung, geloescht) " +
					"SELECT	?, titel, untertitel, bezeichnung, " +
							"0, 0, bemerkung, pruefbedingung, geloescht " +
					  "FROM zvkontentitel_tmp " +
					 "WHERE zvkontoid = ?" );
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//133 Ermittelt die IDs der portierten Titel eines bereits portierten Konten
			// und die auf den Ursprungstiteln vorhandene Budgets.
			// Wurde ein portierter Titel gelöscht, so ist dessen ID = 0
			
			ps = con.prepareStatement(
					 "SELECT zt1.id, IF(zt2.id is NULL, 0, zt2.id), " + 
						   	"zt1.budget " + 
					   "FROM ZVKontentitel zt1 " + 
				  "LEFT JOIN ZVkontentitel zt2 " +
				         "ON zt1.titel = zt2.titel " +
                        "AND zt1.untertitel = zt2.untertitel " +
			          "WHERE zt1.zvkontoid = ? " + // alte ID
					    "AND zt2.zvkontoid = ? " + // neue ID
						"AND zt2.geloescht = '0'");
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//134 Addiert zu übergebenden Betrag auf Titelbudget
			ps = con.prepareStatement(
					"UPDATE zvkontentitel " +
					   "SET budget = budget + ? " +
					 "WHERE id = ?");
			int[] param = {Types.FLOAT, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//135 Addiert zu übergebenden Betrag auf Titelgruppenbudget
			ps = con.prepareStatement(
					"UPDATE zvkonten " +
					   "SET tgrbudget = tgrbudget + ? " +
					 "WHERE id = ?");
			int[] param = {Types.FLOAT, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//136 Ermittelt anhand der ID eines ZV-Titels dessen ID in einem anderen (zu übergebenden) Haushaltsjahr (ID)
			ps = con.prepareStatement(
					"SELECT zt4.id " +
					  "FROM zvkontentitel zt1, zvkontentitel zt2, zvkonten zk1, " +
					       "zvkontentitel zt3, zvkontentitel zt4, zvkonten zk2 " +
				     "WHERE zt1.id = ? " +
					   "AND zt1.zvkontoid = zk1.id " +
					   "AND zk1.titelgruppe = '' " +
					   "AND zk1.id = zt2.zvkontoid " +
					   "AND zt2.titel = zt1.titel " +
					   "AND zt2.untertitel = '' " +
					   "AND zt3.untertitel = zt2.untertitel " +
					   "AND zt3.titel = zt2.titel " +
					   "AND zt3.zvkontoid = zk2.id "+ 
					   "AND zk2.titelgruppe = zk1.titelgruppe " +
					   "AND zk2.kapitel = zk1.kapitel " +
					   "AND zk2.haushaltsjahrid = ? " +
					   "AND zk2.id = zt4.zvkontoid " +
					   "AND zt4.titel = zt1.titel " +
					   "AND zt4.untertitel = zt1.untertitel " +
					   "AND zt1.geloescht = '0' " +
					   "AND zt2.geloescht = '0' " +
					   "AND zk1.geloescht = '0' " +
					   "AND zt3.geloescht = '0' " +
					   "AND zt4.geloescht = '0' " +
					   "AND zk2.geloescht = '0' " +
					 "UNION " +
					"SELECT t2.id " +
					  "FROM zvkonten zk1, zvkontentitel zt1, " +
					       "zvkonten zk2, zvkontentitel zt2 " +
					 "WHERE zt1.id = ? " +
					   "AND zt1.zvkontoid = zk1.id " +
					   "AND NOT zk1.titelgruppe = '' " +
					   "AND zk2.kapitel = zk1.kapitel " +
					   "AND zk2.titelgruppe = zk1.titelgruppe " +
					   "AND zk2.haushaltsjahrid = ? " +
					   "AND zk2.id = zt2.zvkontoid " +
					   "AND zt2.titel = zt1.titel " +
					   "AND zt2.untertitel = zt1.untertitel " +
					   "AND zt1.geloescht = '0' " +
					   "AND zt2.geloescht = '0' " +
					   "AND zk1.geloescht = '0' " +
					   "AND zk2.geloescht = '0'");
			int[] param = {Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//137
			statements[i++] = null;
		}
		{//138
			statements[i++] = null;
		}
		{//139
			ps = con.prepareStatement( "SELECT id, haushaltsjahrid, bezeichnung, " +
																	  "kapitel, titelgruppe, tgrBudget, " +
																	  "dispoLimit, zweckgebunden, freigegeben, " +
																	  "uebernahmestatus, portiert, abgeschlossen " +
																 "FROM ZVKonten " +
																	"WHERE geloescht = '0'" +
																		"AND id = ?");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}

		/**************************************/
		/* Tabelle: ZVKontentitel             */
		/* Indizes 140-159					  */
		/**************************************/

		{//140		(40)
			/**
			 * Abrage der ZVTitel, die zu einem bestimmten ZVKonto gehören und nicht gelöscht sind.
			 * @author w.flat
			 */
			ps = con.prepareStatement( "SELECT id, zvKontoID, bezeichnung, " +
											"titel, untertitel, budget, vormerkungen, " +
											"bemerkung, pruefBedingung, geloescht " +
										"FROM ZVKontenTitel " +
										"WHERE zvKontoId = ? AND untertitel = '' AND geloescht = '0'");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//141		(41
			/**
			 * Abfrage der nicht gelöschten ZVUntertitel von einem bestimmten ZVTitel.
			 * @author w.flat
			 */
			ps = con.prepareStatement( "SELECT id, zvKontoID, bezeichnung, " +
											"titel, untertitel, budget, vormerkungen, " +
											"bemerkung, pruefBedingung, geloescht " +
										"FROM ZVKontenTitel " +
										"WHERE zvKontoId = ? AND titel = ? AND untertitel != '' AND geloescht = '0'");
			int[] param = {Types.INTEGER, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//142		(45)
			/**
			 * Abfrage der titelId von einem ZVTitel, der eine bestimmte zvKontoId <br>
			 * einen bestimmten zvTitel und Untertitel und nicht gelöscht ist.
			 * @author w.flat
			 */
			ps = con.prepareStatement( "SELECT id " +
										"FROM ZVKontenTitel " +
										"WHERE zvKontoID = ? AND titel = ? AND untertitel = ? AND geloescht = '0'" );
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//143		(46)
			/**
			 * Einen ZVTitel erstellen.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"INSERT INTO ZVKontenTitel " +
											"(zvKontoID, bezeichnung, titel, untertitel, " +
											"budget, vormerkungen, bemerkung, pruefBedingung) " +
										"VALUES (?,?,?,?,?,?,?,?)");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, 
							Types.FLOAT, Types.FLOAT, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//144		(48)
			/**
			 * Ein ZVTitel mit bestimmten ZVTitelId löschen.
			 * @author w.flat
			 */
			ps = con.prepareStatement("DELETE FROM ZVKontenTitel WHERE id = ?");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//145		(50)
			/**
			 * Aktualisieren des ZVTitels mit einer bestimmter titelId.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"UPDATE ZVKontenTitel " +
										"SET zvKontoID = ?, bezeichnung = ?, titel = ?, " +
											"untertitel = ?, budget = ?, vormerkungen = ?, bemerkung = ?, " +
											"pruefBedingung = ?, geloescht = ? " +
										"WHERE id = ?");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.FLOAT, Types.FLOAT, Types.VARCHAR,
							Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//146
			/**
			 * Abfrage des ZVTitels mit einer bestimmter titelId.
			 * @author w.flat 
			 */
			ps = con.prepareStatement( "SELECT zvKontoID, bezeichnung, " +
											 "titel, untertitel, budget, vormerkungen, " +
											 "bemerkung, pruefBedingung, geloescht " +
										"FROM ZVKontenTitel " +
										"WHERE id = ? FOR UPDATE" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//147
			/**
			 * Abfrage der titelId von einem ZVTitel, der eine bestimmte zvKontoId <br>
			 * einen bestimmten zvTitel und Untertitel hat und gelöscht ist.
			 * @author w.flat
			 */
			ps = con.prepareStatement( "SELECT id " +
										"FROM ZVKontenTitel " +
										"WHERE zvKontoID = ? " +
											"AND titel = ? " +
											"AND untertitel = ? AND geloescht != '0'" );
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//148
			/**
			 * Abfrage des ZVTitels mit einer bestimmter titelId.
			 * @author w.flat 
			 */
			ps = con.prepareStatement( "SELECT zvKontoID, bezeichnung, " +
											 "titel, untertitel, budget, vormerkungen, " +
											 "bemerkung, pruefBedingung, geloescht " +
										"FROM ZVKontenTitel " +
										"WHERE id = ? " );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//149
			statements[i++] = null;
		}
		{//150
			statements[i++] = null;
		}
		{//151
			statements[i++] = null;
		}
		{//152
			statements[i++] = null;
		}
		{//153
			statements[i++] = null;
		}
		{//154
			statements[i++] = null;
		}
		{//155
			statements[i++] = null;
		}
		{//156
			statements[i++] = null;
		}
		{//157
			statements[i++] = null;
		}
		{//158
			statements[i++] = null;
		}
		{//159
			statements[i++] = null;
		}
		/**************************************/
		/* JOIN: ZVKonten, ZVKontentitel      */
		/* Indizes: 160-169					  */
		/**************************************/
		
		{//160		(44)
			ps = con.prepareStatement( "SELECT zk.id " +
										"FROM ZVKonten zk, ZVKontenTitel zt " +
									   "WHERE zk.kapitel = ? " +
										 "AND zk.titelgruppe = \"\" " +
										 "AND zt.titel = ? " +
										 "AND zk.id = zt.zvKontoID " +
										 "AND zt.untertitel = \"\" AND zk.geloescht = '0'");
			int[] param = {Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//161
			ps = con.prepareStatement( "SELECT SUM(zt.budget), zk.tgrbudget " +
										 "FROM ZVKontentitel zt, ZVKonten zk, Haushaltsjahre h " +
										"WHERE zt.zvkontoid = zk.id " +
										  "AND zk.zweckgebunden = \"0\" " +
										  "AND zt.geloescht = \"0\" " +
										  "AND zk.geloescht = \"0\" " +
										  "AND zk.haushaltsjahrid = h.id " +
										  "AND h.status = '0' " +
										"GROUP BY k.id");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//162
			ps = con.prepareStatement( "SELECT SUM(zt.budget), zk.tgrbudget " +
										 "FROM ZVKontentitel zt, ZVKonten zk " +
										"WHERE zt.zvkontoid = ? " +
										  "AND zt.zvkontoid = zk.id " +
										 // "AND k.zweckgebunden = \"0\" " +
										  "AND zt.geloescht = \"0\" " +
										  "AND zk.geloescht = \"0\" " +
										"GROUP BY k.id");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//163
			ps = con.prepareStatement( "SELECT DISTINCT fk2.id, fk2.budget " +
										 "FROM kontenzuordnung kz, zvkonten zk, fbkonten fk1, fbkonten fk2, haushaltsjahre h " +
										"WHERE kz.zvkontoid = zk.id " +
										  "AND zk.zweckgebunden = \"0\" " +
										  "AND zk.geloescht = \"0\" " +
										  "AND kz.fbkontoid = fk1.id " +
										  "AND fk1.geloescht = \"0\" " +
										  "AND fk1.haushaltsjahrid = fk2.haushaltsjahrid " +
										  "AND fk1.institutsid = fk2.institutsid " +
										  "AND fk1.hauptkonto = fk2.hauptkonto " +
										  "AND fk2.geloescht=\"0\" " +
										  "AND fk1.haushaltsjahrid = h.id " +
										  "AND h.status = '0'");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//164
			ps = con.prepareStatement( "SELECT DISTINCT fk2.id, fk2.budget " +
										 "FROM kontenzuordnung kz, fbkonten fk1, fbkonten fk2 " +
										"WHERE kz.zvkontoid = ? " +
										  "AND kz.fbkontoid = fk1.id " +
										  "AND fk1.geloescht = \"0\" " +
										  "AND fk1.haushaltsjahrid = fk2.haushaltsjahrid " +
										  "AND fk1.institutsid = fk2.institutsid " +
										  "AND fk1.hauptkonto = fk2.hauptkonto " +
										  "AND fk2.geloescht=\"0\" ");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//165
			ps = con.prepareStatement(	"SELECT zk.tgrbudget - SUM( IF(zt.budget < zt.vormerkungen, zt.vormerkungen - zt.budget, 0) ) " +
										  "FROM zvkonten zk, zvkontentitel zt " +
										 "WHERE zk.id = ? " +  
										   "AND zk.id = zt.zvkontoid " +
										   "AND zk.geloescht=\"0\" " +
										   "AND zt.geloescht=\"0\" " +
										"GROUP BY zk.id" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//166 Temporäre ZVKontentabelle mit den Konten eines bestimmten Haushaltsjahres erstellen
			ps = con.prepareStatement("CREATE TABLE zvkonten_tmp "+
											       "SELECT * " + 
												     "FROM zvkonten "+
													"WHERE haushaltsjahrid = ?"
									  );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//167 Temporäre ZVKontentiteltabelle mit den Titeln eines bestimmten Haushaltsjahres erstellen
			ps = con.prepareStatement("CREATE TABLE zvkontentitel_tmp " + 
					                               "SELECT zt.* "+
												     "FROM zvkontentitel zt, zvkonten zk " +
													"WHERE zt.zvkontoid = zk.id " +
													  "AND zk.haushaltsjahrid = ?");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//168 Löscht temporäre ZVKonten-Tabelle
			ps = con.prepareStatement("DROP TABLE IF EXISTS zvkonten_tmp");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//169 Löscht temporäre ZVKontentitel-Tabelle
			ps = con.prepareStatement("DROP TABLE IF EXISTS zvkontentitel_tmp");
			statements[i++] = new PreparedStatementWrapper(ps);
		}


		/**************************************/
		/* Join: Benutzer, Institute, Rollen  */
		/* Indizes: 170-179					  */
		/**************************************/
		{//170	gibt den Benutzer anhand von: benutzername, passwort
			ps = con.prepareStatement( "SELECT b.id, b.benutzername, b.passwort, " +
											  "r.id, r.bezeichnung, " +
											  "i.id, i.bezeichnung, i.kostenstelle, " +
											  "b.titel, b.name, b.vorname, b.email, b.privatKontoId, " +
											  "b.telefon, b.fax, b.bau, b.raum, b.swBeauftragter, b.sichtbarkeit " +
										"FROM Benutzer b, Institute i, Rollen r " +
									   "WHERE b.institutsId = i.id " +
									     "AND b.rollenId = r.id " +
									     "AND b.benutzername = ? " +
									     "AND b.passwort = ? " +
									     "AND b.geloescht = 0 ");
			int[] param = {Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//171 gibt alle Benutzer zurück
			ps = con.prepareStatement( "SELECT b.id, b.benutzername, b.passwort, b.rollenId, " +
															   		  "b.titel, b.name, b.vorname, b.email, b.privatKontoId, "+
															   		  "i.id, i.bezeichnung, i.kostenstelle, " +
															   		  "r.id, r.bezeichnung, " +
																			"b.telefon, b.fax, b.bau, b.raum, b.swBeauftragter, b.sichtbarkeit " +
																"FROM Benutzer b, Institute i, Rollen r " +
															   "WHERE b.institutsId = i.id " +
																 "AND b.geloescht = 0 " +
																 "AND b.rollenId = r.id" );
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//172
			ps = con.prepareStatement( "SELECT b.id, b.benutzername, b.passwort, " +
																	  "r.id, r.bezeichnung, " +
																	  "i.id, i.bezeichnung, i.kostenstelle, " +
																	  "b.titel, b.name, b.vorname, b.email, b.privatKontoId, " +
																		"b.telefon, b.fax, b.bau, b.raum, b.swBeauftragter, b.sichtbarkeit " +
																"FROM Benutzer b, Institute i, Rollen r " +
															   "WHERE b.institutsId = i.id " +
																 "AND b.rollenId = r.id " +
																 "AND b.benutzername = ? " +
																 "AND b.passwort = ? " +
																 "AND b.geloescht = 0 FOR UPDATE");
			int[] param = {Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//173 gibt alle Benutzer eines Instituts zurück
			ps = con.prepareStatement( "SELECT b.id, b.benutzername, b.passwort, " +
																	  "r.id, r.bezeichnung, " +
																	  "i.id, i.bezeichnung, i.kostenstelle, " +
																	  "b.titel, b.name, b.vorname, b.email, b.privatKontoId, " +
																	  "b.telefon, b.fax, b.bau, b.raum, b.swBeauftragter, b.sichtbarkeit " +
																"FROM Benutzer b, Institute i, Rollen r " +
															   "WHERE b.institutsId = i.id " +
															     "AND i.id = ? " +
															     "AND b.rollenId = r.id " +
															     "AND b.geloescht = 0 " );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//174 gibt den Benutzer anhand einer userId zurück mit allen Objekten
			ps = con.prepareStatement( "SELECT b.id, b.benutzername, b.passwort, " +
											  "r.id, r.bezeichnung, " +
											  "i.id, i.bezeichnung, i.kostenstelle, " +
											  "b.titel, b.name, b.vorname, b.email, b.privatKontoId, " +
											  "b.telefon, b.fax, b.bau, b.raum, b.swBeauftragter, b.sichtbarkeit " +
										"FROM Benutzer b, Institute i, Rollen r " +
										"WHERE b.id = ? " +											"AND b.institutsId = i.id " +
										  "AND b.rollenId = r.id " +
										  "AND b.geloescht = '0' ");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//175
			statements[i++] = null;
		}
		{//176
			statements[i++] = null;
		}
		{//177
			statements[i++] = null;
		}
		{//178
			statements[i++] = null;
		}
		{//179
			statements[i++] = null;
		}
		/**************************************/
		/* Join: Fachbereiche, Institute      */
		/* Indizes: 180-189                   */
		/**************************************/
		{//180			(26)
			ps = con.prepareStatement( "SELECT a.institutsId, a.bezeichnung AS hochschule, a.profPauschale, " +
											  "b.bezeichnung, b.kostenstelle, a.anschrift_1, a.anschrift_2, a.hochschule_1, a.hochschule_2 " +
										 "FROM Fachbereiche a, Institute b " +
										"WHERE a.institutsid = b.id" );
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//181 SELECT FOR UPDATE
			ps = con.prepareStatement( "SELECT a.institutsId, a.bezeichnung AS hochschule, a.profPauschale, " +
											  "b.bezeichnung, b.kostenstelle, a.anschrift_1, a.anschrift_2, a.hochschule_1, a.hochschule_2 " +
										 "FROM Fachbereiche a, Institute b " +
										"WHERE a.institutsid = b.id FOR UPDATE" );
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//182
			statements[i++] = null;
		}
		{//183
			statements[i++] = null;
		}
		{//184
			statements[i++] = null;
		}
		{//185
			statements[i++] = null;
		}
		{//186
			statements[i++] = null;
		}
		{//187
			statements[i++] = null;
		}
		{//188
			statements[i++] = null;
		}
		{//189
			statements[i++] = null;
		}
		/**************************************/
		/* Join: Benutzer, Rollen, Temprollen */
		/* Indizes: 190-199                   */
		/**************************************/
		{//190			(27)
			ps = con.prepareStatement( "SELECT r.id, r.bezeichnung, " +
											  "tr.besitzer, tr.gueltigBis " +
									  	 "FROM Benutzer b, Rollen r , TempRollen tr " +
									  	"WHERE tr.empfaenger = ? " +
										  "AND tr.besitzer = b.id " +
										  "AND b.rollenId = r.id " );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//191
			statements[i++] = null;
		}
		{//192
			statements[i++] = null;
		}
		{//193
			statements[i++] = null;
		}
		{//194
			statements[i++] = null;
		}
		{//195
			statements[i++] = null;
		}
		{//196
			statements[i++] = null;
		}
		{//197
			statements[i++] = null;
		}
		{//198
			statements[i++] = null;
		}
		{//199
			statements[i++] = null;
		}
		/******************************************/
		/* Join: Rollenaktivitaeten, Aktivitaeten */
		/* Indizes: 200-209                   		*/
		/******************************************/
		{//200
			ps = con.prepareStatement( "SELECT b.* " +
										 "FROM Rollenaktivitaeten  a, Aktivitaeten b " +
										"WHERE a.rollenId = ? " +
										  "AND a.aktivitaetsId = b.id " +
									 "ORDER BY aktivitaetsID " );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//201
			statements[i++] = null;
		}
		{//202
			statements[i++] = null;
		}
		{//203
			statements[i++] = null;
		}
		{//204
			statements[i++] = null;
		}
		{//205
			statements[i++] = null;
		}
		{//206
			statements[i++] = null;
		}
		{//207
			statements[i++] = null;
		}
		{//208
			statements[i++] = null;
		}
		{//209
			statements[i++] = null;
		}

		/******************************************/
		/* Tabelle: Bestellungen	1			  */
		/* Indizes: 210-219                       */
		/******************************************/
		{//210 gibt Anzahl der Bestellungen eines Benutzers
			ps = con.prepareStatement( "SELECT COUNT(b.id) " +
										 "FROM Benutzer a, Bestellungen b " +
										"WHERE a.id = ? " +
										"AND a.id = b.besteller " );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//211 gibt Anzahl der aktiven Bestellungen eines Benutzers
			ps = con.prepareStatement( "SELECT COUNT(b.id) " +
										"FROM Benutzer a, Bestellungen b " +
										"WHERE a.id = ? " +
										"AND a.id = b.besteller " +
										"AND b.phase != '2'" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//212
			/**
			 * Anzahl der Bestellungen(aktiv und abgeschlossen) bei denen <br>
			 * ein bestimmtes FBKonto angegeben ist.
			 * @author w.flat
			 */
			ps = con.prepareStatement( "SELECT COUNT(b.id) " +
										"FROM FBKonten fk, Bestellungen b " +
										"WHERE fk.id = ? " +
										"AND fk.id = b.fbKonto" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//213
			/**
			 * Ermittlung der Anzahl der aktiven Bestellung, bei denen eine bestimmtes FBKonto angegeben wurde.
			 * @author w.flat 
			 */
			ps = con.prepareStatement( "SELECT COUNT(b.id) " +
										"FROM FBKonten fk, Bestellungen b " +
										"WHERE fk.id = ? " +
										"AND fk.id = b.fbKonto " +
										"AND b.phase != '2'" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//214
			/**
			 * Abfrage der Anzahl der abgeschlossenen und aktiven Bestellungen, bei denen <br>
			 * ein bestimmter ZVTitel verwendet wurde.
			 * @author w.flat
			 */
			ps = con.prepareStatement( "SELECT COUNT(b.id) " +
										"FROM ZVKontenTitel zt, Bestellungen b " +
										"WHERE zt.id = ? " +
										"AND zt.id = b.zvTitel" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//215
			/**
			 * Abfrage der Anzahl der nicht abgeschlossenen Bestellungen, bei denen <br>
			 * ein bestimmter ZVTitel verwendet wurde.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"SELECT COUNT(b.id) " +
										"FROM ZVKontenTitel zt, Bestellungen b " +
										"WHERE zt.id = ? " +
										"AND zt.id = b.zvTitel " +
										"AND b.phase != '2'" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//216
			/**
			 * Active Bestellungen, die die angegebene Firma beinhalten.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"SELECT COUNT(b.id) " +
										"FROM firmen a, Bestellungen b " +
										"WHERE a.id = ? " +
										"AND a.id = b.lieferant " +
										"AND b.status = '0'" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//217 Inactive Bestellungen, die die angegebene Firma beinhalten
			ps = con.prepareStatement( "SELECT COUNT(b.id) " +
										"FROM firmen a, Bestellungen b " +
										"WHERE a.id = ? " +
										"AND a.id = b.lieferant " +
										"AND b.status != '0'" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//218 gibt die Anzahl der Bestellungen mit der ReferenzNr
			ps = con.prepareStatement( "SELECT COUNT(id) " +
																	 "FROM Bestellungen " +
																"WHERE referenzNr = ? " );
			int[] param = {Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//219 fügt eine Bestellung in die Bestellungen Tabelle ein und liefert eine Id
			ps = con.prepareStatement("INSERT " +
																"INTO Bestellungen " +
																	 "(besteller, auftraggeber, empfaenger, referenzNr, typ, phase, " +																	 "datum, zvTitel, fbKonto, bestellwert, verbindlichkeiten) " +
															  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
															  Statement.RETURN_GENERATED_KEYS);
			int[] param = {Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.VARCHAR,  Types.VARCHAR,  Types.VARCHAR,
										 Types.DATE, Types.INTEGER, Types.INTEGER, Types.FLOAT, Types.FLOAT};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}

		/******************************************/
		/* Tabelle: Buchungen   				  */
		/* Indizes: 220-229                       */
		/******************************************/
		{//220 Anzahl der Buchungen zu einem Benutzer
			ps = con.prepareStatement( "SELECT COUNT(b.betragZvKonto1) " +
										 "FROM Benutzer a, Buchungen b " +
										"WHERE a.id = ? " +
										"AND a.id = b.benutzer " );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//221
			/**
			 * Ermittlung der Anzahl der Buchungen bei denen ein bestimmtes FBKonto verwendet wurde.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"SELECT COUNT(b.bestellung) " +
										"FROM FBKonten fk, Buchungen b " +
										"WHERE fk.id = ? " +
										"AND (fk.id = b.fbKonto1 OR fk.id = b.fbKonto2)" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//222
			/**
			 * Anzahl der Buchungen bei denen ein bestimmter ZVTitel angegeben wurde.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"SELECT COUNT(b.bestellung) " +
										"FROM ZVKontenTitel zt, Buchungen b " +
										"WHERE zt.id = ? " +
										"AND (zt.id = b.zvTitel1 OR zt.id = b.zvTitel2)" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//223 fügt eine neue Buchung ein
			ps = con.prepareStatement("INSERT " +
																"INTO Buchungen " +
																	 "(timeStamp, benutzer, typ, beschreibung, bestellung, zvKonto1, betragZvKonto1, zvKonto2, betragZvKonto2, " +																	 "zvTitel1, betragZvTitel1, zvTitel2, betragZvTitel2, " +																	 "fbKonto1, betragFbKonto1, fbKonto2, betragFbKonto2 ) " +
															  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
															  Statement.RETURN_GENERATED_KEYS);
			int[] param = {	Types.TIMESTAMP, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.FLOAT, Types.INTEGER, Types.FLOAT,
										 	Types.INTEGER, Types.FLOAT, Types.INTEGER, Types.FLOAT, 
											Types.INTEGER, Types.FLOAT, Types.INTEGER, Types.FLOAT	};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//224
			ps = con.prepareStatement(	"SELECT SUM( betragZVKonto1 ) " +
					  "FROM buchungen " +
                  "WHERE bestellung = ? " +
					"AND typ = '9'"); 
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//225
			/**
			 * Buchung von einer bestimmter Bestellung mit einem bestimmten Typ.
			 * @author w.flat
			 */
			ps = con.prepareStatement(	"SELECT timeStamp, benutzer, typ, beschreibung, bestellung, zvKonto1, betragZvKonto1, " +
											"zvTitel1, betragZvTitel1, zvTitel2, betragZvTitel2, " +
											"fbKonto1, betragFbKonto1, fbKonto2, betragFbKonto2 " +
					  					"FROM buchungen " +
				  						"WHERE bestellung = ? AND typ = ? " ); 
			int[] param = {Types.INTEGER, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//226 Führt ein Update der Konten-/Titel_IDs der Buchungen zu einer Bestellung durch
			ps = con.prepareStatement(
					"UPDATE Buchungen " +
					   "SET zvtitel1 = IF(zvtitel1 = ?, ?, zvtitel1), " +
					       "zvtitel2 = IF(zvtitel2 = ?, ?, zvtitel2), " +
						   "fbkonto1 = IF(fbkonto1 = ?, ?, fbkonto1), " +
						   "fbkonto2 = IF(fbkonto2 = ?, ?, fbkonto2) " +
					 "WHERE bestellung = ?");
						//   alter Titel   neuer Titel
			int[] param = { Types.INTEGER, Types.INTEGER,
						//   alter Titel   neuer Titel
							Types.INTEGER, Types.INTEGER,
						//   altes Konto   neues Konto
							Types.INTEGER, Types.INTEGER,
						//   altes Konto   neues Konto
							Types.INTEGER, Types.INTEGER,
						//    Bestellung
							Types.INTEGER };
			
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//227 Fügt alle Mittelübernahmebuchungen für ZV-Titel bei Portierung eines ZV-Kontos ein 
			ps = con.prepareStatement(
					"INSERT INTO Buchungen " +
								"(timestamp, benutzer, typ, beschreibung, " +
								" zvtitel1, betragZvTitel1, zvtitel2, betragZvTitel2 ) " +
						 "SELECT " +
						 		"?, ?, '3', '', " +
								"ztold.id, -ztnew.budget, ztnew.id, ztnew.budget " +
						   "FROM zvkontentitel ztold, zvkontentitel ztnew " +
						  "WHERE ztold.zvkontoid = ? " +
						    "AND ztnew.zvkontoid = ? " +
							"AND ztold.titel = ztnew.titel " +
							"AND ztold.untertitel = ztnew.untertitel " +
							"AND ztnew.budget > 0 " );
			int[] param = {Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//228 Fügt alle Mittelübernahmebuchungen bei Portierung eines FB-Hauptkontos ein 
			ps = con.prepareStatement(
					"INSERT INTO Buchungen " +
								"(timestamp, benutzer, typ, beschreibung, " +
								" fbkonto1, betragfbkonto1, fbkonto2, betragfbkonto2) " +
						 "SELECT " + 
						 		"?, ?, '4', '', " +
								"fkold.id, -fknew.budget, fknew.id, fknew.budget " +
						  "FROM fbkonten fkmain, fbkonten fkold, fbkonten fknew " +
						 "WHERE fkmain.id = ? " +
						   "AND fkmain.haushaltsjahrid = fkold.haushaltsjahrid " +
						   "AND fkmain.institutsid = fkold.institutsid " +
						   "AND fkmain.hauptkonto = fkold.hauptkonto " +
						   "AND fkold.institutsid = fknew.institutsid " +
						   "AND fkold.hauptkonto = fknew.hauptkonto " +
						   "AND fkold.unterkonto = fknew.unterkonto " +
						   "AND fknew.haushaltsjahrid = ? " +
						   "AND fknew.budget > 0");
			int[] param = {Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//229
			statements[i++] = null;
		}

		/******************************************/
		/* Tabelle: Kontenzuordnung   						*/
		/* Indizes: 230-239                       */
		/******************************************/
		{//230 Kontenzuordnung ändern
			ps = con.prepareStatement( "UPDATE Kontenzuordnung " +
										 "SET status = ? " +
										"WHERE fbKontoId = ? " +
										"AND zvKontoId = ? " );
			int[] param = {Types.INTEGER, Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//231 Kontenzuordnung löschen
			ps = con.prepareStatement( "DELETE FROM Kontenzuordnung " +
																	"WHERE zvKontoId = ? " +
																		"AND fbKontoId = ? " );
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//232 Kontenzuordnung einfügen
			ps = con.prepareStatement( "INSERT INTO Kontenzuordnung(zvKontoId, fbKontoId, status) " +
										 							"VALUES(?, ?, 0) " );
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//233  Kontenzuordnungen
			ps = con.prepareStatement( "SELECT kz.status, zk.id, zk.bezeichnung, zk.kapitel, zk.titelgruppe, zk.zweckgebunden " +
																 "FROM ZVKonten zk, Kontenzuordnung kz, haushaltsjahre h " +
																 "WHERE zk.haushaltsjahrid = h.id " +
																		"AND zk.geloescht = '0' " +
																		"AND kz.fbKontoId = ? " +
																		"AND kz.zvKontoId = zk.id " +
																		"AND h.status = 0"	 );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//234 Kontenzuordnung
			ps = con.prepareStatement( "SELECT kz.status, zk.id, zk.bezeichnung, zk.kapitel, zk.titelgruppe, zk.zweckgebunden " +
																 "FROM ZVKonten zk, Kontenzuordnung kz, haushaltsjahre h " +
																 "WHERE zk.haushaltsjahrid = h.id " +
																		"AND zk.geloescht = '0' " +
																		"AND kz.fbKontoId = ? " +
																		"AND kz.zvKontoId = ? " +
																		"AND kz.zvKontoId = zk.id " +
																		"AND h.status = 0"	 );
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//235
			/**
			 * Ermittlung der Anzahl der Kontenzuordnungen, <br>
			 * bei denen eine bestimmtes ZVKonto angegeben ist.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"SELECT COUNT(zk.id) " +
										"FROM ZVKonten zk, Kontenzuordnung kz " +
										"WHERE zk.id = ? " +
										"AND zk.id = kz.zvKontoId" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//236
			/**
			 * Ermittlung der Kontenzuordnungen, bei denen ein bestimmtes FBKonto angegeben ist.
			 * @author w.flat
			 */
			ps = con.prepareStatement( "SELECT COUNT(fk.id) " +
										"FROM FBKonten fk, Kontenzuordnung kz " +
										"WHERE fk.id = ? " +
										"AND fk.id = kz.fbKontoId" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//237
			/**
			 * Überprüfung ob ein ZVKonto zweckgebunden sein kann. <br>
			 * Dabei wird ermittelt ob mehr als ein ZVKonto zu dem FBKonto einer Kontozuordnung existiert.
			 * @author w.flat 
			 */
			ps = con.prepareStatement( 	"SELECT (COUNT(kz2.zvKontoId) - COUNT(DISTINCT kz2.fbKontoId)) " +
										"FROM Kontenzuordnung kz1, Kontenzuordnung kz2 " +
										"WHERE kz1.zvKontoId = ? " +
										"AND kz1.fbKontoId = kz2.fbKontoId" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//238  Kontenzuordnungen
			ps = con.prepareStatement( "SELECT kz.status, zk.id, zk.bezeichnung, zk.kapitel, zk.titelgruppe, zk.zweckgebunden " +
																 "FROM ZVKonten zk, Kontenzuordnung kz " +
																 "WHERE zk.geloescht = '0' " +
																   "AND kz.fbKontoId = ? " +
																   "AND kz.zvKontoId = zk.id " +
																 "ORDER BY zk.kapitel, zk.titelgruppe");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//239	gibt alle Kostenarten zurück mit id, beschreibung
			ps = con.prepareStatement( "SELECT * FROM kostenarten");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		
		/******************************************/
		/* Tabelle: Firmen		   				  */
		/* Indizes: 240-249                       */
		/******************************************/
		{//240
			/**
			 * Abfrage aller Firmen, die nicht gelöscht sind.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"SELECT id, name, strassenr, plz, ort, kundennr, " +											"telnr, faxnr, email, www, ask, geloescht " +
										"FROM firmen WHERE geloescht = '0'" );
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//241
			/**
			 * Abfrage aller Firmen, die gelöscht sind.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"SELECT id, name, strassenr, plz, ort, kundennr, " +											"telnr, faxnr, email, www, ask, geloescht " +
										"FROM firmen WHERE geloescht != '0'" );
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//242 
			/**
			 * Eine Firma zum Aktualisieren abfragen.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"SELECT id, name, strassenr, plz, ort, " +											"kundennr, telnr, faxnr, email, www, ask, geloescht " +
										"FROM firmen WHERE id = ? FOR UPDATE" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//243 
			/**
			 * Abfrage ob eine nicht gelöschte Firma existiert.
			 * @author w.flat
			 */
			ps = con.prepareStatement(	"SELECT id FROM firmen " +
										"WHERE name = ? AND strassenr = ? AND plz = ? AND ort = ? AND geloescht = '0'" );
			int[] param = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//244 
			/**
			 * Abfrage ob eine gelöschte Firma existiert.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"SELECT id FROM firmen " +										"WHERE name = ? AND strassenr = ? AND plz = ? AND ort = ? AND geloescht != '0'" );
			int[] param = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//245
			/**
			 * Eine Firma in der Datenbank aktualisieren.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"UPDATE firmen " +
										"SET name = ?, strassenr = ?, plz = ?, ort = ?, kundennr = ?, " +
											"telnr = ?, faxnr = ?, email = ?, www = ?, ask = ?, geloescht = ? " +
										"WHERE id = ?" );
			int[] param = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
					Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//246 
			/**
			 * Eine Firma in die Datenbank einfügen.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"INSERT INTO firmen " +
										"(name, strassenr, plz, ort, kundennr, telnr, faxnr, email, www, ask, geloescht) " +										"VALUES (?,?,?,?,?,?,?,?,?,?,?)" );
			int[] param = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//247
			/**
			 * Eine Firma in die Datenbank löschen.
			 * @author w.flat
			 */
			ps = con.prepareStatement("DELETE FROM firmen WHERE id = ?");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//248 gibt eine Firma anhand der id zurück
			ps = con.prepareStatement( 	"SELECT id, name, strassenr, plz, ort, kundennr, " +
											"telnr, faxnr, email, www, ask, geloescht " +
										"FROM firmen WHERE id = ? AND geloescht = '0'" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//249 gibt eine Firma für die ASK-Bestellung zurück
			ps = con.prepareStatement( 	"SELECT id, name, strassenr, plz, ort, kundennr, " +
											"telnr, faxnr, email, www, ask, geloescht " +
										"FROM firmen WHERE ask = '1' AND geloescht = '0'" );
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		/******************************************/
		/* Tabelle: ASK_Standard_Bestellungen 	  */
		/* Indizes: 250-259                       */
		/******************************************/
		{//250 fügt eine StandardBestellung in die ASK_Standard_Bestellungen Tabelle ein
			ps = con.prepareStatement("INSERT " +
																"INTO ASK_Standard_Bestellungen " +
																	 "( id, bemerkungen, swBeauftragter, kostenart, ersatzbeschaffung, ersatzbeschreibung, " +
																	 "ersatzInventarNr, verwendungszweck, planvorgabe, begruendung) " +
															  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER,  Types.VARCHAR,  Types.VARCHAR,
										 Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//251 löscht eine ASK- bzw. StandardBestellung anhand der Id
			ps = con.prepareStatement("DELETE FROM ASK_Standard_Bestellungen WHERE id = ? ");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//252
			statements[i++] = null;
		}
		{//253
			statements[i++] = null;
		}
		{//254
			statements[i++] = null;
		}
		{//255
			statements[i++] = null;
		}
		{//256
			statements[i++] = null;
		}
		{//257
			statements[i++] = null;
		}
		{//258
			statements[i++] = null;
		}
		{//259
			statements[i++] = null;
		}
		/******************************************/
		/* Tabelle: Angebote 1 	                  */
		/* Indizes: 260-264                       */
		/******************************************/
		{//260 fügt ein Angebot in die Tabelle Angebot ein
			ps = con.prepareStatement("INSERT " +
																"INTO Angebote " +
																	 "( bestellung, anbieter, datum, angenommen) " +
															  "VALUES (?, ?, ?, ?)",
																Statement.RETURN_GENERATED_KEYS);
			int[] param = {Types.INTEGER, Types.INTEGER, Types.DATE, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//261 gibt die Angebote zur einer Bestellung mit bestellId und vollständiger Firma zurück
			ps = con.prepareStatement("SELECT f.id, f.name, f.strassenr, f.plz, f.ort, f.kundennr, " +
																			 "f.telnr, f.faxnr, f.email, f.www, f.ask, f.geloescht, " +																			 "a.id AS angebotId, a.datum, a.angenommen " +
																"FROM Angebote a, Firmen f " +
															  "WHERE a.bestellung = ? " +															  		"AND a.anbieter = f.id " +															  		"AND f.geloescht = '0'");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//262 Anzahl der Angebote bei denen eine bestimmte Firma verwendet wurde.
			ps = con.prepareStatement(	"SELECT COUNT(a.id)" +
										"FROM Angebote a, Firmen b " +
										"WHERE b.id = ? AND a.anbieter = b.id" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//263 löscht alle Angebote zu einer Bestellung
			ps = con.prepareStatement("DELETE FROM Angebote WHERE bestellung = ? ");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//264 aktualisiert ein Angebot anhand der Id
			ps = con.prepareStatement("UPDATE Angebote " +
																"SET anbieter = ?, datum = ?, angenommen = ? " +
																"WHERE id = ?" );
			int[] param = {Types.INTEGER, Types.DATE, Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		/******************************************/
		/* Tabelle: Positionen 1               	  */
		/* Indizes: 265-269                       */
		/******************************************/
		{//265 fügt eine Positon in die Tabelle Positionen ein
			ps = con.prepareStatement("INSERT " +
																"INTO Positionen " +
																	 "( angebot, institut, menge, artikel, einzelPreis, mwSt, rabatt) " +
															  "VALUES (?, ?, ?, ?, ?, ?, ?)");
			int[] param = {Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.FLOAT, Types.FLOAT, Types.FLOAT};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//266 gibt alle Positionen zu einem Angebot anhand der angebotId zurück
			ps = con.prepareStatement("SELECT id, institut, artikel, einzelPreis, menge, mwSt, rabatt, beglichen " +
																"FROM Positionen " +
															  "WHERE angebot = ?");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//267 löscht alle Positionen von allen Angeboten einer Bestellung
			ps = con.prepareStatement("DELETE FROM Positionen WHERE angebot IN (SELECT id FROM angebote WHERE bestellung = ?)");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//268 aktualisiert eine Position anhand der Id
			ps = con.prepareStatement("UPDATE Positionen " +
																"SET institut = ?, menge = ?, artikel = ?, einzelPreis = ?, mwSt = ?, rabatt = ?, beglichen = ? " +
																"WHERE id = ? " );
			int[] param = {Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.FLOAT, Types.FLOAT, Types.FLOAT, Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//269 löscht eine Position anhand der Id
			ps = con.prepareStatement("DELETE FROM Positionen WHERE id = ? ");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		/***************************************************/
		/* Join: Bestellungen, ASK_Standard_Bestellungen 1 */
		/* Indizes: 270-274					              			   */
		/***************************************************/
		{//270 gibt die StandardBestellung mit der zugehörigen Id zurück. Es werden nur BenutzerId ermittelt
			ps = con.prepareStatement("SELECT " +																		"k.id, k.beschreibung," +																		"b.ersatzbeschaffung, b.ersatzbeschreibung, b.ersatzInventarNr, " +																		"b.verwendungszweck, b.planvorgabe, b.begruendung, b.bemerkungen, " +
																		"a.besteller, a.auftraggeber, a.empfaenger, " +																		"a.referenzNr, a.huelNr, a.phase, a.huelNr, a.datum, a.zvTitel, a.fbKonto, a.bestellwert, a.verbindlichkeiten , " +																		"b.swBeauftragter " +
																"FROM Bestellungen a, ASK_Standard_Bestellungen b, Kostenarten k " +
															  "WHERE a.id = ? " +															  		"AND a.id = b.id " +															  		"AND a.typ = '0' " +															  		"AND a.geloescht = '0' " +															  		"AND k.id = b.kostenart");
			int[] param = {	Types.INTEGER };
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//271 aktualisiert die ASK- bzw. StandardBestellung
			ps = con.prepareStatement("UPDATE Bestellungen a, ASK_Standard_Bestellungen b " +																"SET a.geloescht = ?, a.besteller = ?, a.auftraggeber = ?, a.empfaenger = ?, a.referenzNr = ?, a.huelNr = ?, a.phase = ?, " +
																		"a.datum = ?, a.zvTitel = ?, a.fbKonto = ?, a.bestellwert = ?, a.verbindlichkeiten = ?, " +																		"b.bemerkungen = ?, b.kostenart = ?, b.ersatzbeschaffung = ?, b.ersatzbeschreibung = ?, b.ersatzInventarNr = ?, " +
																		"b.verwendungszweck = ?, b.planvorgabe = ?, b.begruendung = ?, b.swBeauftragter = ? " +
																"WHERE a.id = ? " +
																	"AND a.id = b.id " +
																	"AND a.geloescht = '0' ");
			int[] param = {	Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR,
											Types.DATE, Types.INTEGER, Types.INTEGER, Types.FLOAT, Types.FLOAT,
											Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
											Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER };
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//272
			ps = con.prepareStatement(	"SELECT " + 
											"o.id, o.datum, o.typ, o.phase, " +
											"u1.name, u1.vorname, " +
											"u2.name, u2.vorname, " +
											"u3.name, u3.vorname, " +
											"o.bestellwert, " +
											"o.verbindlichkeiten " +
										"FROM bestellungen o, benutzer u1, benutzer u2, benutzer u3 " +
										"WHERE o.typ IN (?, ?, ?) " +
										  "AND o.besteller = u1.id " +
										  "AND o.auftraggeber = u2.id " +
										  "AND o.empfaenger = u3.id " +
										  "AND o.geloescht = '0' " +
										  "ORDER BY datum DESC");
			int[] param = {	Types.INTEGER, Types.INTEGER, Types.INTEGER }; // {typ1, typ2, typ3}
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//273
			statements[i++] = null;
		}
		{//274 select for update für eine StandardBestellung
			ps = con.prepareStatement("SELECT " +
																		"k.id, k.beschreibung," +
																		"b.ersatzbeschaffung, b.ersatzbeschreibung, b.ersatzInventarNr, " +
																		"b.verwendungszweck, b.planvorgabe, b.begruendung, b.bemerkungen, " +
																		"a.besteller, a.auftraggeber, a.empfaenger, " +
																		"a.referenzNr, a.huelNr, a.phase, a.huelNr, a.datum, a.zvTitel, a.fbKonto, a.bestellwert, a.verbindlichkeiten, " +																		"b.swBeauftragter " +
																"FROM Bestellungen a, ASK_Standard_Bestellungen b, Kostenarten k " +
															  "WHERE a.id = ? " +
																	"AND a.id = b.id " +
																	"AND a.typ = ? " +
																	"AND a.geloescht = '0' " +
																	"AND k.id = b.kostenart FOR UPDATE");
			int[] param = {	Types.INTEGER, Types.VARCHAR };
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		/**********************/
		/* Tabelle: Belege    */
		/* Indizes: 275-279	  */
		/**********************/
		{//275
			/**
			 * Anzahl der Belege bei denen eine bestimmte Firma verwendet wurde.
			 * @author w.flat
			 */
			ps = con.prepareStatement(	"SELECT COUNT(a.id)" +
										"FROM Belege a, Firmen b " +
										"WHERE b.id = ? AND a.firma = b.id" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//276
			/**
			 * Ein Beleg in die Tabelle einfügen. 
			 * @author w.flat
			 */
			ps = con.prepareStatement(	"INSERT INTO belege " +
										"(bestellung, nr, firma, artikel, belegsumme) " +
										"VALUES (?,?,?,?,?)");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.FLOAT};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//277
			/**
			 * Abfrage aller Belege einer angegebenen Bestellung. 
			 * @author w.flat
			 */
			ps = con.prepareStatement(	"SELECT id, bestellung, nr, firma, artikel, belegsumme " +
										"FROM belege " +
										"WHERE bestellung = ?" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//278
			/**
			 * Löschen aller Belege einer angegebenen Bestellung. 
			 * @author w.flat
			 */
			ps = con.prepareStatement(	"DELETE FROM belege WHERE bestellung = ?" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//279
			/**
			 * Abfrage der Id eines bestimmten Belegs einer bestimmten Bestellung und bestimmten Beleg-Nummer. 
			 * @author w.flat
			 */
			ps = con.prepareStatement(	"SELECT id " +
										"FROM belege " +
										"WHERE bestellung = ? AND nr = ?" );
			int[] param = {Types.INTEGER, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		/******************************************/
		/* Tabelle: Positionen 2               	  */
		/* Indizes: 280-284                       */
		/******************************************/
		{//280 löscht alle Positionen eines Angebots
			ps = con.prepareStatement("DELETE FROM Positionen WHERE angebot = ? ");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//281 Select for update für eine Position
			ps = con.prepareStatement("SELECT id, institut, artikel, einzelPreis, menge, mwSt, rabatt, beglichen " +
																"FROM Positionen " +
															  "WHERE angebot = ? FOR UPDATE");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//282
			statements[i++] = null;
		}
		{//283
			statements[i++] = null;
		}
		{//284
			statements[i++] = null;
		}
		/******************************************/
		/* Tabelle: Angebote 2 	                  */
		/* Indizes: 285-289                       */
		/******************************************/
		{//285 löscht ein Angebot anhand der Id
			ps = con.prepareStatement("DELETE FROM Angebote WHERE id = ? ");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//286 select for update der Angebote einer Bestellung
			ps = con.prepareStatement("SELECT f.id, f.name, f.strassenr, f.plz, f.ort, f.kundennr, " +
																			 "f.telnr, f.faxnr, f.email, f.www, f.ask, f.geloescht, " +
																			 "a.id AS angebotId, a.datum, a.angenommen " +
																"FROM Angebote a, Firmen f " +
															  "WHERE a.bestellung = ? " +
																	"AND a.anbieter = f.id " +
																	"AND f.geloescht = '0' FOR UPDATE");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//287
			statements[i++] = null;
		}
		{//288
			statements[i++] = null;
		}
		{//289
			statements[i++] = null;
		}
		/******************************************/
		/* Tabelle: Kleinbestellungen             */
		/* Indizes: 290-299                       */
		/******************************************/
		{//290 
			/**
			 * Eine Kleinbestellung einfügen. 
			 * @author w.flat
			 */
			ps = con.prepareStatement(	"INSERT INTO kleinbestellungen " +
										"(id, projektNr, verwendungszweck, labor, kartei, verzeichnis) " +
										"VALUES (?,?,?,?,?,?)" );
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//291
			/**
			 * Kleinbestellung aktualisieren. 
			 * @author w.flat
			 */
			ps = con.prepareStatement(	"UPDATE Bestellungen a, Kleinbestellungen b " +
										"SET a.besteller = ?, a.auftraggeber = ?, a.empfaenger = ?, a.referenzNr = ?, " +											"a.huelNr = ?, a.phase = ?, a.datum = ?, a.zvTitel = ?, a.fbKonto = ?, " +											"a.bestellwert = ?, a.verbindlichkeiten = ?, a.geloescht = ?, " +
											"b.projektNr = ?, b.verwendungszweck = ?, b.labor = ?, b.kartei = ?, " +											"b.verzeichnis = ? " +
										"WHERE a.id = ? AND a.id = b.id AND a.geloescht = '0' ");
			int[] param = {	Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.VARCHAR, 
								Types.VARCHAR,  Types.VARCHAR, Types.DATE, Types.INTEGER, Types.INTEGER, 
								Types.FLOAT, Types.FLOAT, Types.VARCHAR, 
								Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, 
								Types.VARCHAR, 
								Types.INTEGER };
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//292
			/**
			 * Alle nicht gelöschten Kleinbestellung abfragen. 
			 * @author w.flat
			 */
			ps = con.prepareStatement(	"SELECT a.id, a.besteller, a.empfaenger, a.datum, a.zvTitel, a.fbKonto, " +
											"a.bestellwert, " +
											"b.projektNr, b.verwendungszweck, b.labor, b.kartei, b.verzeichnis " +
										"FROM Bestellungen a, Kleinbestellungen b " + 
										"WHERE a.id = b.id AND a.geloescht = '0' ");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//293
			/**
			 * Alle gelöschten Kleinbestellung abfragen. 
			 * @author w.flat
			 */
			ps = con.prepareStatement(	"SELECT a.id, a.besteller, a.empfaenger, a.datum, a.zvTitel, a.fbKonto, " +
											"a.bestellwert, " +
											"b.projektNr, b.verwendungszweck, b.labor, b.kartei, b.verzeichnis " +
										"FROM Bestellungen a, Kleinbestellungen b " + 
										"WHERE a.id = b.id AND a.geloescht = '1' ");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//294
			/**
			 * Eine Kleinbestellung zu Aktualisieren auswählen. 
			 * @author w.flat
			 */
			ps = con.prepareStatement(	"SELECT * FROM Bestellungen a, Kleinbestellungen b " + 
										"WHERE a.id = b.id AND a.id = ? FOR UPDATE");
			int[] param = {	Types.INTEGER };
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//295 
			/**
			 * Eine Kleinbestellung mit bestimmter Id selektieren. 
			 * @author w.flat
			 */
			ps = con.prepareStatement(	"SELECT a.id, a.besteller, a.empfaenger, a.datum, a.zvTitel, a.fbKonto, " +
											"a.bestellwert, a.phase, " +
											"b.projektNr, b.verwendungszweck, b.labor, b.kartei, b.verzeichnis " +
										"FROM Bestellungen a, Kleinbestellungen b " + 
										"WHERE a.id = b.id AND a.id = ? ");
			int[] param = {	Types.INTEGER };
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//296
			statements[i++] = null;
		}
		{//297
			statements[i++] = null;
		}
		{//298
			statements[i++] = null;
		}
		{//299
			statements[i++] = null;
		}
		/******************************************/
		/* Tabelle: Bestellungen 2            		*/
		/* Indizes: 300-309                       */
		/******************************************/
		{//300 löscht eine Bestellung aus der Tabelle Bestellungen anhand der Id
			ps = con.prepareStatement("DELETE FROM Bestellungen WHERE id = ? ");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//301 ermittelt alle Bestellungen in Sondierungs- oder Abwicklungsphase eines Haushaltsjahres
			ps = con.prepareStatement(
						"SELECT " + 
							   "o.id, o.datum, o.typ, o.phase, " + 
							   "u1.name, u1. vorname, " + 
							   "u2.name, u2.vorname, " +
							   "zt.id, zk.id, zk.bezeichnung, zk.kapitel, zk.titelgruppe, zk.zweckgebunden, " +
							   "fk2.id, fk2.bezeichnung, i.bezeichnung, i.kostenstelle, fk2.hauptkonto, " +
							   "o.bestellwert " +
						  "FROM bestellungen o, benutzer u1, benutzer u2, zvkontentitel zt, " +
						  	   "zvkonten zk, fbkonten fk1, fbkonten fk2, institute i " +
						 "WHERE "+
						       "(o.phase = '0' OR o.phase = '1') " +
						   "AND o.geloescht = '0' "+
						   "AND o.besteller = u1.id "+
						   "AND o.auftraggeber = u2.id "+
						   "AND o.zvtitel = zt.id "+
						   "AND zt.zvkontoid = zk.id "+
						   "AND o.fbkonto = fk1.id "+
						   "AND fk1.haushaltsjahrid = fk2.haushaltsjahrid "+
						   "AND fk1.institutsid = fk2.institutsid "+
						   "AND fk1.hauptkonto = fk2.hauptkonto "+
						   "AND fk2.unterkonto = \'0000\' "+
						   "AND fk2.institutsid = i.id "+
						   "AND zk.geloescht = \'0\' "+
						   "AND fk2.geloescht = \'0\' "+
						   "AND fk2.haushaltsjahrid = zk.haushaltsjahrid "+
						   "AND zk.haushaltsjahrid = ?"
					);
			
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//302 Ändert ZV-Titel und FB-Konto einer Bestellung
			ps = con.prepareStatement(
					"UPDATE Bestellungen " + 
					   "SET zvtitel = ?, " +
					       "fbkonto = ? " +
					 "WHERE id = ?");
			int[] param = {Types.INTEGER, Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//303 
			statements[i++] = null;
		}
		{//304 Ermittelt alle Bestellunen die über FB-Konten eines bestimmten Instituts abgewickelt werden
			ps = con.prepareStatement(	"SELECT " + 
											"o.id, o.datum, o.typ, o.phase, " +
											"u1.name, u1.vorname, " +
											"u2.name, u2.vorname, " +
											"u3.name, u3.vorname, " +
											"o.bestellwert, " +
											"o.verbindlichkeiten " +
										"FROM bestellungen o, benutzer u1, benutzer u2, benutzer u3, fbkonten fk " +
										"WHERE o.besteller = u1.id " +
										  "AND o.typ IN (?, ?, ?) " +
										  "AND o.auftraggeber = u2.id " +
										  "AND o.empfaenger = u3.id " +
										  "AND o.geloescht = '0' " +
										  "AND o.fbkonto = fk.id " +
										  "AND fk.institutsid = ? " +
										  "ORDER BY datum DESC");
			int[] param = {Types.INTEGER, Types.INTEGER, Types.INTEGER,	Types.INTEGER }; //{typ1, typ2, typ3, institutsid}
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//305 
			statements[i++] = null;
		}
		{//306 Ermittelt alle Bestellungen die über ein bestimtes FB-Konto abgewickelt werden
			ps = con.prepareStatement(	"SELECT " + 
											"o.id, o.datum, o.typ, o.phase, " +
											"u1.name, u1.vorname, " +
											"u2.name, u2.vorname, " +
											"u3.name, u3.vorname, " +
											"o.bestellwert, " +
											"o.verbindlichkeiten " +
										"FROM bestellungen o, benutzer u1, benutzer u2, benutzer u3 " +
										"WHERE o.besteller = u1.id " +
										  "AND o.typ IN (?, ?, ?) " +
										  "AND o.auftraggeber = u2.id " +
										  "AND o.empfaenger = u3.id " +
										  "AND o.geloescht = '0' " +
										  "AND o.fbkonto = ? " +
										  "ORDER BY datum DESC");
			int[] param = {Types.INTEGER, Types.INTEGER, Types.INTEGER,	Types.INTEGER }; //{typ1, typ2, typ3, fbkontoid}
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//307
			statements[i++] = null;
		}
		{//308
			statements[i++] = null;
		}
		{//309
			statements[i++] = null;
		}
		/*****************************************************/
		/* Join: FBKonten, ZVKontentitel, Kontenzuordnungen  */
		/* Indizes: 310-324					                 */
		/*****************************************************/
		{//310 Addition eine Betrags auf die Vormerkungen
			ps = con.prepareStatement("UPDATE FBKonten fk, ZVKontentitel zt " +
																"SET fk.vormerkungen = (fk.vormerkungen + ?), zt.vormerkungen = (zt.vormerkungen + ?) " +
																"WHERE fk.id = ? " +
																	"AND zt.id = ? " +
																	"AND fk.geloescht = '0' " +																	"AND zt.geloescht = '0' ");
			int[] param = {	Types.FLOAT, Types.FLOAT, Types.INTEGER, Types.INTEGER };
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//311
			ps = con.prepareStatement(	"UPDATE FBKonten fk, ZVKonten zk, ZVKontentitel zt " +	
										   "SET " +
										       "fk.vormerkungen = (fk.vormerkungen + ?), " + 
										       "fk.budget = (fk.budget + ?), " +
										       "zk.tgrbudget = (zk.tgrbudget + ?), " +
										       "zt.vormerkungen = (zt.vormerkungen + ?), " +
										       "zt.budget = (zt.budget + ?) " +
										 "WHERE fk.id = ? " +
										   "AND zk.id = ? " +
										   "AND zt.id = ? " +
										   "AND fk.geloescht = '0' " +
										   "AND zk.geloescht = '0' " +
										   "AND zt.geloescht = '0'");
			int[] param = {	Types.FLOAT, Types.FLOAT, Types.FLOAT, Types.FLOAT, Types.FLOAT, Types.INTEGER, Types.INTEGER, Types.INTEGER };
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//312
			/**
			 * Aktualisieren der Beträge auf dem ZVTitel und auf dem FBKonto, bei der Stornierung einer
			 * Kleinbestellung.
			 * @author w.flat
			 */
			ps = con.prepareStatement(	"UPDATE FBKonten fk, ZVKontentitel zt, ZVKonten zk " +	
										   "SET " +
												"zk.tgrBudget = (zk.tgrBudget + ?), " +
												"zt.budget = (zt.budget + ?), " +
											   "fk.budget = (fk.budget + ?) " +
										 "WHERE zk.id = ? AND zt.id = ? AND fk.id = ? " +
										   "AND fk.geloescht = '0' AND zk.geloescht = '0' AND zt.geloescht = '0'" );
			int[] param = {	Types.FLOAT, Types.FLOAT, Types.FLOAT, Types.INTEGER, Types.INTEGER, Types.INTEGER };
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//313 Portiert die Kontenzuordnungen eines Haushaltsjahres in eine temporäre Tabelle.
			// Die Konten-IDs werden bereits umgesetzt.
			ps = con.prepareStatement(
					"CREATE TABLE kontenzuordnung_tmp  " +
						"SELECT zk2.id zvkontoid, fk2.id fbkontoid, kz.status " +
						  "FROM kontenzuordnung kz, " +
						       "fbkonten fk1, fbkonten fk2, " +
							   "zvkonten zk1, zvkonten zk2 " +
						 "WHERE kz.fbkontoid = fk1.id " +
						   "AND fk1.haushaltsjahrid = ? " +
						   "AND fk2.haushaltsjahrid = ? " +
						   "AND fk1.institutsid = fk2.institutsid " +
						   "AND fk1.hauptkonto = fk2.hauptkonto " +
						   "AND fk1.unterkonto = fk2.unterkonto " +
						   "AND kz.zvkontoid = zk1.id " +
						   "AND zk1.haushaltsjahrid = fk1.haushaltsjahrid " +
						   "AND zk2.haushaltsjahrid = fk2.haushaltsjahrid " +
						   "AND zk2.kapitel = zk1.kapitel " +
						   "AND NOT zk1.titelgruppe = '' " +
						   "AND zk2.titelgruppe = zk1.titelgruppe " +
						   "AND fk1.geloescht = '0' " +
						   "AND fk2.geloescht = '0' " +
						   "AND zk1.geloescht = '0' " +
						   "AND zk2.geloescht = '0' " +
					    "UNION " +
						"SELECT zk2.id zvkontoid, fk2.id fbkontoid, kz.status " +
						  "FROM kontenzuordnung kz, " +
						       "zvkonten zk1, zvkontentitel zt1, " +
							   "zvkonten zk2, zvkontentitel zt2, " +
							   "fbkonten fk1, fbkonten fk2 " +
					     "WHERE zk1.id = kz.zvkontoid " +
						   "AND zk1.id = zt1.zvkontoid " +
						   "AND zk1.haushaltsjahrid = ? " +
						   "AND zk1.titelgruppe = '' " +
						   "AND zt1.untertitel = ''" +
						   "AND zk2.id = zt2.zvkontoid " +
						   "AND zk2.haushaltsjahrid = ? " +
						   "AND zk2.titelgruppe = '' " +
						   "AND zt2.untertitel = '' " +
						   "AND zk1.kapitel = zk2.kapitel " +
						   "AND zt1.titel = zt2.titel " +
						   "AND fk1.id = kz.fbkontoid " +
						   "AND fk1.haushaltsjahrid = zk1.haushaltsjahrid " +
						   "AND fk2.haushaltsjahrid = zk2.haushaltsjahrid " +
						   "AND fk1.institutsid = fk2.institutsid " +
						   "AND fk1.hauptkonto = fk2.hauptkonto " +
						   "AND fk1.unterkonto = fk2.unterkonto " +
						   "AND zk1.geloescht = '0' " +
						   "AND zt1.geloescht = '0' " +
						   "AND zk2.geloescht = '0' " +
						   "AND zt2.geloescht = '0' " +
						   "AND fk1.geloescht = '0' " +
						   "AND fk2.geloescht = '0'");
			int[] param = {	Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER };
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//314 Fügt alle Datensätze der temporären Kontenzuordnungstabelle in die persistente Kontenzuordnungstabelle ein
			ps = con.prepareStatement("INSERT INTO kontenzuordnung SELECT * FROM kontenzuordnung_tmp");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//315 Löscht temporäre Kontenzuordnungstabelle
			ps = con.prepareStatement("DROP TABLE IF EXISTS kontenzuordnung_tmp");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//316
			statements[i++] = null;
		}
		{//317
			statements[i++] = null;
		}
		{//318
			statements[i++] = null;
		}
		{//319
			statements[i++] = null;
		}
		{//320 
			statements[i++] = null;
		}
		{//321
			statements[i++] = null;
		}
		{//322
			statements[i++] = null;
		}
		{//323
			statements[i++] = null;
		}
		{//324
			statements[i++] = null;
		}
		/***************************************************/
		/* Join: Bestellungen, ASK_Standard_Bestellungen 2 */
		/* Indizes: 325-334					                       */
		/***************************************************/
		{//325 gibt die ASKBestellung anhand der Id zurück, es werden nur die ID zurückgegeben
			ps = con.prepareStatement("SELECT " +
																		"b.bemerkungen, b.swBeauftragter, " +
																		"a.besteller, a.auftraggeber, a.empfaenger, " +
																		"a.referenzNr, a.huelNr, a.phase, a.huelNr, a.datum, a.zvTitel, " +																		"a.fbKonto, a.bestellwert, a.verbindlichkeiten " +
																"FROM Bestellungen a, ASK_Standard_Bestellungen b " +
															  "WHERE a.id = ? " +
																	"AND a.id = b.id " +
																	"AND a.typ = '1' " +
																	"AND a.geloescht = '0' ");
			int[] param = {	Types.INTEGER };
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//326 select for update für eine ASKBestellung
			ps = con.prepareStatement("SELECT " +
																		"b.bemerkungen, b.swBeauftragter, " +
																		"a.besteller, a.auftraggeber, a.empfaenger, " +
																		"a.referenzNr, a.huelNr, a.phase, a.huelNr, a.datum, a.zvTitel, " +
																		"a.fbKonto, a.bestellwert, a.verbindlichkeiten " +
																"FROM Bestellungen a, ASK_Standard_Bestellungen b " +
															  "WHERE a.id = ? " +
																	"AND a.id = b.id " +
																	"AND a.typ = '1' " +
																	"AND a.geloescht = '0' FOR UPDATE");
			int[] param = {	Types.INTEGER };
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//327
			statements[i++] = null;
		}
		{//328
			statements[i++] = null;
		}
		{//329
			statements[i++] = null;
		}
		{//330 
			statements[i++] = null;
		}
		{//331
			statements[i++] = null;
		}
		{//332
			statements[i++] = null;
		}
		{//333
			statements[i++] = null;
		}
		{//334
			statements[i++] = null;
		}
		/**************************/
		/* Reports, Logs					*/
		/* Indizes: 335-349				*/
		/**************************/
		{//335 Report 7 für alle Institute siehe gui.Reports.java
			ps = con.prepareStatement("SELECT " +
																		"i.bezeichnung AS instiut, " +																		"zk.bezeichnung AS zvKonto, (be.bestellwert - be.verbindlichkeiten) AS ausgaben, " +
																		"be.referenzNr , be.huelNr , be.typ, be.datum, be.phase, be.id " +
																"FROM " +																	"Bestellungen be, ZVKontentitel zt, ZVKonten zk, " +																	"Institute i, FBKonten fk, Haushaltsjahre h " +
															  "WHERE i.id = fk.institutsId  " +
																	"AND be.fbKonto = fk.id " +																	"AND be.zvTitel = zt.id " +																	"AND (be.datum BETWEEN ? AND ?) " +
																	"AND zt.zvKontoId = zk.id " +																	"AND fk.geloescht = '0' " +
																	"AND zk.geloescht = '0' " +
																	"AND zt.geloescht = '0' " +
																	"AND h.id = fk.haushaltsjahrId " +
																	"AND h.id = zk.haushaltsjahrId " +
																	"AND h.status = 0 " +
																	"AND be.phase != 0 " +
																	"AND be.geloescht = '0'" +																"ORDER BY i.bezeichnung");
			int[] param = {	Types.DATE, Types.DATE };													
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//336 Report 8 für alle Institute: FB-Konto, ZV-Konto, Einnahmen siehe gui.Reports.java
			ps = con.prepareStatement("SELECT " +
																		"i.bezeichnung AS institut, fk.bezeichnung AS fbKonto, SUM(b.betragFbKonto1) " +
																"FROM " +
																	"Institute i, FBKonten fk, Buchungen b, Haushaltsjahre h " +
															  "WHERE i.id = fk.institutsId  " +
																	"AND b.fbKonto1 = fk.id " +
																	"AND (b.timestamp BETWEEN ? AND ?) " +
																	"AND fk.geloescht = '0' " +
																	"AND h.id = fk.haushaltsjahrId " +
																	"AND h.status = 0 " +
																	"AND b.typ = '5' " +																"GROUP BY i.bezeichnung, fk.bezeichnung");
			int[] param = {	Types.TIMESTAMP, Types.TIMESTAMP };													
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//337 Report 6 siehe gui.Reports.java
			ps = con.prepareStatement("SELECT " +
																		"i.bezeichnung AS institut, " +																		"zk.bezeichnung AS zvKonto, " +																		"SUM(COALESCE((be.bestellwert - be.verbindlichkeiten),0)) AS ausgaben " +
																"FROM " +
																	"ZVKontentitel zt, ZVKonten zk, " +
																	"Institute i, FBKonten fk, Haushaltsjahre h " +																"LEFT JOIN Bestellungen be " +																	"ON be.phase != 0 " +
																	"AND (be.datum BETWEEN ? AND ?) " +
																	"AND be.geloescht = '0' " +
																	"AND be.fbKonto = fk.id " +
																	"AND be.zvTitel = zt.id " +
															  "WHERE i.id = fk.institutsId  " +
																	"AND zt.zvKontoId = zk.id " +
																	"AND fk.geloescht = '0' " +
																	"AND zk.geloescht = '0' " +
																	"AND zt.geloescht = '0' " +
																	"AND h.id = fk.haushaltsjahrId " +
																	"AND h.id = zk.haushaltsjahrId " +
																	"AND h.status = 0 " +
																"GROUP BY i.bezeichnung, zk.bezeichnung");
			int[] param = {	Types.DATE, Types.DATE };													
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//338 Report 5 siehe gui.Reports.java
			ps = con.prepareStatement("SELECT " +
																		"zk.bezeichnung AS zvKonto, " +
																		"i.bezeichnung AS institut, " +
																		"SUM(COALESCE(bu.betragFbKonto1,0)) AS ausgaben, " +
																		"( SELECT SUM(budget) " +																			"FROM FBKonten " +																			"WHERE institutsId = i.id " +																			"AND geloescht = '0') AS kontostand " +
																"FROM " +
																	"ZVKontentitel zt, ZVKonten zk, " +
																	"Institute i, FBKonten fk, Haushaltsjahre h " +																"LEFT JOIN Buchungen bu " +																	"ON bu.typ > 8 " +
																	"AND (bu.timestamp BETWEEN ? AND ?) " +
																	"AND bu.fbKonto1 = fk.id " +
																	"AND bu.zvTitel1 = zt.id " +
															  "WHERE i.id = fk.institutsId  " +
																	"AND zt.zvKontoId = zk.id " +
																	"AND fk.geloescht = '0' " +
																	"AND zk.geloescht = '0' " +
																	"AND zt.geloescht = '0' " +
																	"AND h.id = fk.haushaltsjahrId " +
																	"AND h.id = zk.haushaltsjahrId " +
																	"AND h.status = 0 " +
																"GROUP BY zk.bezeichnung, i.bezeichnung");
			int[] param = {	Types.TIMESTAMP, Types.TIMESTAMP };													
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//339 Report 4 siehe gui.Reports.java
			/**
			 * Ein Report, der die Ausgaben bei den institutsinternen Konten nach Verwaltungskonten sortiert
			 */
			ps = con.prepareStatement("SELECT " +
																		"fk.bezeichnung AS fbKonto, " +
																		"zk.bezeichnung AS zvKonto, " +
																		"SUM(bu.betragFbKonto1) AS ausgaben " +
																"FROM " +
																	"Buchungen bu, ZVKontentitel zt, ZVKonten zk, " +
																	"FBKonten fk, Haushaltsjahre h " +
															  "WHERE zt.zvKontoId = zk.id " +															  	"AND bu.typ > 8 " +
																	"AND (bu.timestamp BETWEEN ? AND ?) " +
																	"AND bu.fbKonto1 = fk.id " +
																	"AND bu.zvTitel1 = zt.id " +
																	"AND fk.geloescht = '0' " +
																	"AND zk.geloescht = '0' " +
																	"AND zt.geloescht = '0' " +
																	"AND h.id = fk.haushaltsjahrId " +
																	"AND h.id = zk.haushaltsjahrId " +
																	"AND h.status = 0 " +
																"GROUP BY fk.bezeichnung, zk.bezeichnung");
			int[] param = {	Types.TIMESTAMP, Types.TIMESTAMP };													
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//340 Report 3 siehe gui.Reports.java
			/**
			 * Ein Report, der die institutsinternen Konten, die verteilten Mittel, die Summe
			 * der Ausgaben und den aktuellen Kontostand enthält
			 */
			ps = con.prepareStatement("SELECT " +																		"i.bezeichnung AS institut, " +
																		"fk.bezeichnung AS fbKonto, " +																		"(SELECT SUM(betragFbKonto1) FROM Buchungen WHERE fbKonto1 = fk.id AND typ = '5' AND (timestamp BETWEEN ? AND ?)) AS einnahmen, " +
																		"SUM(COALESCE(bu.betragFbKonto1,0)) AS ausgaben, " +																		"fk.budget AS kontostand " +
																"FROM " +
																	"Institute i, ZVKontentitel zt, ZVKonten zk, " +
																	"FBKonten fk, Haushaltsjahre h " +
															  "LEFT JOIN Buchungen bu " +
																	"ON bu.typ > 8 " +
																	"AND (bu.timestamp BETWEEN ? AND ?) " +
																	"AND bu.fbKonto1 = fk.id " +
																	"AND bu.zvTitel1 = zt.id " +
															  "WHERE i.id = fk.institutsId " +															  	"AND zt.zvKontoId = zk.id " +
																	"AND fk.geloescht = '0' " +
																	"AND zk.geloescht = '0' " +
																	"AND zt.geloescht = '0' " +
																	"AND h.id = fk.haushaltsjahrId " +
																	"AND h.id = zk.haushaltsjahrId " +
																	"AND h.status = 0 " +
																"GROUP BY i.bezeichnung, fk.bezeichnung");
			int[] param = {	Types.TIMESTAMP, Types.TIMESTAMP, Types.TIMESTAMP, Types.TIMESTAMP };													
			statements[i++] = new PreparedStatementWrapper(ps,param);
		}
		{//341 Report 2 siehe gui.Reports.java
			/**
			 * Ein Report, der die Konten der Zentralverwaltung und die zugewiesenen Mittel , die Summe
			 * der Ausgaben und Verteilungen, so dass der Dekan feststellen kann, wie viel Mittel kann er noch verteilen
			 */
		    ps = con.prepareStatement("SELECT " +
								"zvk.id AS zvKonto, " +
								"( SELECT SUM(betragZvKonto1 + betragZvTitel1) " +
									"FROM Buchungen " +
									"WHERE typ IN (1, 2) " +
										"AND (timestamp BETWEEN ? AND ?) " +
										"AND ( zvKonto1 = zvk.id " +
													"OR zvTitel1 IN " +
																	"( SELECT id " +
																		"FROM ZVKontentitel " +
																		"WHERE zvKontoId = zvk.id " +
																			"AND geloescht = '0'" +
																	")" +
												")" +
								") AS mittel, " +
								"(zvk.tgrBudget + (SELECT SUM(budget) " +
																	"FROM ZVKontentitel " +
																	"WHERE zvKontoId  = zvk.id " +
																		"AND geloescht = '0')" +
								") AS kontostand " +
						"FROM " +
							"ZVKonten zvk, Haushaltsjahre h " +
					  "WHERE h.id = zvk.haushaltsjahrId " +
							"AND h.status = 0 " +
							"AND zvk.geloescht = '0'");
		    
				int[] param = {	Types.TIMESTAMP, Types.TIMESTAMP };													
				statements[i++] = new PreparedStatementWrapper(ps,param);
		}
		{//342 Report 1 siehe gui.Reports.java
			/**
			 * Ein Report, der die Konten der Zentralverwaltung und die zugewiesenen Mittel, die Summe
			 * der Ausgaben und aktuellen Kontostände enthält
			 */
			ps = con.prepareStatement("SELECT " +
																		"zk.bezeichnung AS zvKonto, " +
																		"( SELECT SUM(betragZvKonto1 + betragZvTitel1) " +
																			"FROM Buchungen " +
																			"WHERE typ IN (1, 2) " +
																				"AND (timestamp BETWEEN ? AND ?) " +
																				"AND ( zvKonto1 = zk.id " +
																							"OR zvTitel1 IN " +
																											"( SELECT id " +
																												"FROM ZVKontentitel " +
																												"WHERE zvKontoId = zk.id " +																													"AND geloescht = '0'" +
																											")" +
																						")" +
																		") AS mittel, " +
																		"( SELECT SUM(betragZvKonto1 + betragZvTitel1) " +																			"FROM Buchungen " +																			"WHERE typ > 8 " +																				"AND (timestamp BETWEEN ? AND ?) " +
																				"AND ( zvKonto1 = zk.id " +																							"OR zvTitel1 IN " +																											"( SELECT id " +																												"FROM ZVKontentitel " +																												"WHERE zvKontoId = zk.id " +																													"AND geloescht = '0'" +																											")" +																						")" +																		") AS ausgaben, " +
																		"(zk.tgrBudget + (SELECT SUM(budget) " +																											"FROM ZVKontentitel " +																											"WHERE zvKontoId  = zk.id " +																												"AND geloescht = '0')" +																		") AS kontostand " +
																"FROM " +
																	"ZVKonten zk, Haushaltsjahre h " +
															  "WHERE h.id = zk.haushaltsjahrId " +
																	"AND h.status = 0 " +																	"AND zk.geloescht = '0'");
			int[] param = {	Types.TIMESTAMP, Types.TIMESTAMP, Types.TIMESTAMP, Types.TIMESTAMP };													
			statements[i++] = new PreparedStatementWrapper(ps,param);
		}
		{//343 gibt alle Logs in einem Zeitabschnitt zurück
			ps = con.prepareStatement("SELECT " +
																		"timestamp, benutzerId, typ, beschreibung " +
																"FROM " +
																	"Logs " +
															  "WHERE timestamp BETWEEN ? AND ? " +															  "ORDER BY typ");
			int[] param = {	Types.TIMESTAMP, Types.TIMESTAMP };													
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//344 fügt eine neue Log ein
			ps = con.prepareStatement(	"INSERT INTO logs " +
																		"(timestamp, benutzerId, typ, beschreibung) " +
																	"VALUES ( NOW(), " +																						"(SELECT id FROM Benutzer WHERE CONVERT(benutzername USING latin1) = CONVERT(substring_index(SESSION_USER(),'@',1) USING latin1)), " +																						"?, ?)" );
			int[] param = {Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//345 
			statements[i++] = null;
		}
		{//346
			statements[i++] = null;
		}
		{//347
			statements[i++] = null;
		}
		{//348
			statements[i++] = null;
		}
		{//349
			statements[i++] = null;
		}
		/**********************************/
		/* Tabelle: Temprollen  */
		/* Indizes: 350-359					      */
		/**********************************/
		{//350 fügt eine neue temp. Rolle hinzu
			ps = con.prepareStatement( "INSERT INTO Temprollen(empfaenger, besitzer, gueltigBis) " +
																	"VALUES(?, ?, ?) " );
			int[] param = {Types.INTEGER, Types.INTEGER, Types.DATE};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//351 löscht eine temp. Rolle
			ps = con.prepareStatement("DELETE FROM Temprollen " +
																"WHERE empfaenger = ? " +
																	"AND besitzer = ? " );
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//352 aktualisiert das Datum einer TempRolle
			ps = con.prepareStatement( "UPDATE Temprollen " +
								 "SET gueltigBis = ? " +
								"WHERE empfaenger = ? " +
								"AND besitzer = ? " );
			int[] param = {Types.DATE, Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//353 gibt die Benutzer die eine TempRolle vom Besitzer erhalten haben
			ps = con.prepareStatement("SELECT b.id, b.benutzername, b.name, b.vorname, tr.gueltigBis " +
																"FROM Benutzer b, TempRollen tr " +
																"WHERE tr.besitzer = ? " +
																	"AND b.id = tr.empfaenger " +
																	"AND b.geloescht = '0'");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//354 löscht alte TmpRollen
			ps = con.prepareStatement("DELETE FROM Temprollen " +
															"WHERE gueltigBis < NOW() " );
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//355 löscht alle TmpRollen eines Benutzers
			ps = con.prepareStatement("DELETE FROM Temprollen " +
																"WHERE empfaenger = ? " +
																	"OR besitzer = ? " );
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//356
			statements[i++] = null;
		}
		{//357
			statements[i++] = null;
		}
		{//358
			statements[i++] = null;
		}
		{//359
			statements[i++] = null;
		}
	}

	public void release() throws SQLException{
		for (int i=0; i<statements.length;i++){
			if (statements[i] != null){
				statements[i].close();
			}
		}
	}

	public PreparedStatementWrapper get (int stmt){
		return statements[stmt];
	}

	public static void main(String[] args) {

	}
}


