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

		statements = new PreparedStatementWrapper[275];
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
		{//6
			statements[i++] = null;
		}
		{//7
			statements[i++] = null;
		}
		{//8
			statements[i++] = null;
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
		{//20			(0)
			ps = con.prepareStatement("SELECT * FROM Benutzer WHERE id = ? AND geloescht = '0'");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//21			(1)
			ps = con.prepareStatement("UPDATE Benutzer " +
										 "SET " +
												"benutzername = ?, " +
												"rollenId = ?, " +
												"institutsId = ?, " +
												"titel = ?, " +
												"name = ?, " +
												"vorname = ?, " +
												"email = ?, " +
												"privatKontoId = ?, telefon = ?, fax = ?, bau = ?, raum = ?, swBeauftragter = ?" +
									   "WHERE id = ?");
			int[] param = {Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
										 Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
										 Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//22			(2)
			ps = con.prepareStatement("UPDATE Benutzer " +
										 "SET " +
												"benutzername = ?, " +
												"rollenId = ?, " +
												"institutsId = ?, " +
												"titel = ?, " +
												"name = ?, " +
												"vorname = ?, " +
												"email = ?, " +
												"privatKontoId = NULL, telefon = ?, fax = ?, bau = ?, raum = ?, swBeauftragter = ?" +
									   "WHERE id = ?");
			int[] param = {Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
										 Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
										 Types.VARCHAR, Types.VARCHAR, Types.INTEGER };
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//23			(3)
			ps = con.prepareStatement("UPDATE Benutzer " +
										 "SET passwort = ? " +
									   "WHERE id = ?");
			int[] param = {Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//24			(4)
			ps = con.prepareStatement(	"INSERT " +
										  "INTO Benutzer " +
												"(benutzername, passwort, rollenId, institutsId, titel, " +
												"name, vorname, email, privatKontoId, telefon, fax, bau, raum, swBeauftragter)" +
										"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
										Statement.RETURN_GENERATED_KEYS);
			int[] param = {Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR,
										 Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, 
										 Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//25			(5)
			ps = con.prepareStatement(	"INSERT " +
										  "INTO Benutzer " +
												"(benutzername, passwort, rollenId, institutsId, titel, " +
												"name, vorname, email, telefon, fax, bau, raum, swBeauftragter) " +
										"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
										Statement.RETURN_GENERATED_KEYS);
			int[] param = {Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR,
										 Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
										 Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}

		{//26			(6)
			ps = con.prepareStatement("DELETE " +
										"FROM Benutzer " +"" +
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
										"FROM FBKonten a, Benutzer b " +
										"WHERE a.id = ? " +
										"AND a.id = b.privatKontoId AND b.geloescht != '0'" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//32
			/**
			 * Anzahl der Benutzer(aktiv und gelöscht), die ein bestimmtes FBKonto besitzen.
			 * @author w.flat
			 */
			ps = con.prepareStatement( "SELECT COUNT(b.privatKontoId) " +
										"FROM FBKonten a, Benutzer b " +
										"WHERE a.id = ? " +
										"AND a.id = b.privatKontoId" );
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
			ps = con.prepareStatement("SELECT id, haushaltsjahrId, institutsId, bezeichnung, "+
										"hauptkonto, unterkonto, budget, dispoLimit, vormerkungen, " +										"pruefBedingung, kleinbestellungen " +
										"FROM FBKonten " +
										"WHERE institutsID = ? " +
										"AND unterkonto = \"0000\" " +
										"AND geloescht = \"0\"");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//51			(10)
			/**
			 * Abfrage aller FBUnterkonten, die zu einem bestimmten Institut und einem <br> 
			 * bestimmten FBHauptkonto angehören und nicht gelöscht sind. 
			 * @author w.flat
			 */
			ps = con.prepareStatement("SELECT a.id, a.haushaltsjahrId, a.institutsId, a.bezeichnung, " +
										"a.hauptkonto, a.unterkonto, a.budget, a.vormerkungen, a.kleinbestellungen " +
										"FROM FBKonten a, FBKonten b " +
										"WHERE a.institutsID = ? " +
										"AND a.institutsID = b.institutsID " +
										"AND b.id = ? " +
										"AND a.hauptkonto = b.hauptkonto " +
										"AND NOT (a.unterkonto = \"0000\") " +
										"AND a.geloescht = \"0\"");
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//52			(11)
			/**
			 * Einfügen eines FBKontos in die Datenbank. 
			 * @author w.flat
			 */
			ps = con.prepareStatement("INSERT " +
										"INTO FBKonten " +
											 "(haushaltsjahrId, institutsID, bezeichnung, hauptkonto, " +
											 "unterkonto, budget, dispoLimit, vormerkungen, pruefBedingung," +											 "kleinbestellungen, geloescht) " +
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
			ps = con.prepareStatement("SELECT id " +
										"FROM FBKonten "+
										"WHERE institutsID = ? " +
										"AND hauptkonto = ? " +
										"AND unterkonto = ? AND geloescht = '0'");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//54			(13)
			ps = con.prepareStatement("DELETE " +
										"FROM FBKonten " +
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
			statements[i++] = null;
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
			ps = con.prepareStatement("SELECT id " +
										"FROM FBKonten "+
										"WHERE institutsID = ? " +
										"AND hauptkonto = ? " +
										"AND unterkonto = ? AND geloescht = '1'");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//60
			//CHANGE PreparedSqlStatements: Start
			ps = con.prepareStatement( "SELECT " +
											  "distinct fbk.id, fbk.haushaltsjahrId, fbk.institutsId, fbk.bezeichnung, " +
												  "fbk.hauptkonto, fbk.unterkonto, fbk.budget, fbk.dispoLimit, fbk.pruefBedingung " +
											 "FROM FBKonten fbk, Kontenzuordnung kz, ZVKonten zvk " +
										"WHERE fbk.institutsID = ? " +
										  "AND fbk.unterkonto = \"0000\" " +
										  "AND fbk.geloescht = \"0\" " +
										  "AND fbk.id = kz.fbkontoid " +
										  "AND kz.zvkontoid = zvk.id " +
										  "AND zvk.zweckgebunden = \"0\" ");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
//		CHANGE PreparedSqlStatements: End
		}
		{//61
			statements[i++] = null;
		}
		{//62
			statements[i++] = null;
		}
		{//63
			statements[i++] = null;
		}
		{//64
			statements[i++] = null;
		}
		{//65
			statements[i++] = null;
		}
		{//66
			statements[i++] = null;
		}
		{//67
			statements[i++] = null;
		}
		{//68
			statements[i++] = null;
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
			ps = con.prepareStatement( "SELECT beschreibung, " +
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
		{//74
			statements[i++] = null;
		}
		{//75
			statements[i++] = null;
		}
		{//76
			statements[i++] = null;
		}
		{//77
			statements[i++] = null;
		}
		{//78
			statements[i++] = null;
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
			ps = con.prepareStatement("SELECT a.id, a.bezeichnung, a.kostenstelle, a.institutsleiter, " +
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
			ps = con.prepareStatement( "GRANT RELOAD ON * . * TO ?@'%' IDENTIFIED BY 'mittelverwaltung' " +
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
			ps = con.prepareStatement("GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES, LOCK TABLES ON `fbmittelverwaltung` . * TO ?@'%' WITH GRANT OPTION ");
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
			ps = con.prepareStatement( "SELECT id, haushaltsjahrid, bezeichnung, " +
											"kapitel, titelgruppe, tgrBudget, " +
											"dispoLimit, zweckgebunden, freigegeben, " +
											"uebernahmestatus " +
										"FROM ZVKonten " +
										"WHERE geloescht = \"0\"" );
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//121			(42)
			/**
			 * Neues ZVKonto erstellen.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"INSERT " +
										"INTO ZVKonten " +
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
			 * Abfrage eines ZVKontos mit einem bestimmten Kapitel <br>
			 * und einer bestimmter Titelgruppe und welches nicht gelöscht ist.
			 * @author w.flat 
			 */
			ps = con.prepareStatement(	"SELECT id " +
										"FROM ZVKonten " +
										"WHERE kapitel = ? " +
											"AND titelgruppe = ? AND geloescht = '0'" );
			int[] param = {Types.VARCHAR, Types.VARCHAR};
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
											"geloescht = ? " +
										"WHERE id = ?");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.FLOAT, Types.FLOAT,
							Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//125
			ps = con.prepareStatement( "SELECT haushaltsjahrid, bezeichnung, " +
											  "kapitel, titelgruppe, tgrBudget, " +
											  "dispoLimit, zweckgebunden, freigegeben, " +
											  "uebernahmestatus, geloescht " +
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
			ps = con.prepareStatement(	"SELECT id " +
										"FROM ZVKonten " +
										"WHERE kapitel = ? " +
											"AND titelgruppe = ? AND geloescht != '0'" );
			int[] param = {Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//127
			statements[i++] = null;
		}
		{//128
			statements[i++] = null;
		}
		{//129
			statements[i++] = null;
		}
		{//130
			statements[i++] = null;
		}
		{//131
			statements[i++] = null;
		}
		{//132
			statements[i++] = null;
		}
		{//133
			statements[i++] = null;
		}
		{//134
			statements[i++] = null;
		}
		{//135
			statements[i++] = null;
		}
		{//136
			statements[i++] = null;
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
																	  "uebernahmestatus " +
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
											"titel, untertitel, budget, " +
											"bemerkung, pruefBedingung " +
										"FROM ZVKontenTitel " +
										"WHERE zvKontoId = ? "+
											"AND untertitel = \"\" " +
											"AND geloescht = \"0\"");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//141		(41
			/**
			 * Abfrage der nicht gelöschten ZVUntertitel von einem bestimmten ZVTitel.
			 * @author w.flat
			 */
			ps = con.prepareStatement( "SELECT id, zvKontoID, bezeichnung, " +
											"titel, untertitel, budget, " +
											"bemerkung, pruefBedingung " +
										"FROM ZVKontenTitel " +
										"WHERE zvKontoId = ? " +
											"AND NOT (untertitel = \"\") "+
											"AND geloescht = \"0\"");
			int[] param = {Types.INTEGER};
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
										"WHERE zvKontoID = ? " +
											"AND titel = ? " +
											"AND untertitel = ? AND geloescht = '0'" );
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//143		(46)
			/**
			 * Einen ZVTitel erstellen.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"INSERT " +
										"INTO ZVKontenTitel " +
											"(zvKontoID, bezeichnung, titel, " +
											"untertitel, budget, bemerkung, pruefBedingung) " +
										"VALUES (?,?,?,?,?,?,?)");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.FLOAT, Types.VARCHAR, Types.VARCHAR};
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
											"untertitel = ?, budget = ?, bemerkung = ?, " +
											"pruefBedingung = ?, geloescht = ? " +
										"WHERE id = ?");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.FLOAT, Types.VARCHAR,
							Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//146
			/**
			 * Abfrage des ZVTitels mit einer bestimmter titelId.
			 * @author w.flat 
			 */
			ps = con.prepareStatement( "SELECT zvKontoID, bezeichnung, " +
											 "titel, untertitel, budget, " +
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
											 "titel, untertitel, budget, " +
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
			ps = con.prepareStatement( "SELECT a.id " +
										"FROM ZVKonten a, ZVKontenTitel b " +
									   "WHERE a.kapitel = ? " +
										 "AND a.titelgruppe = \"\" " +
										 "AND b.titel = ? " +
										 "AND a.id = b.zvKontoID " +
										 "AND b.untertitel = \"\" AND a.geloescht = '0'");
			int[] param = {Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		//CHANGE PreparedSqlStatements: Start
		{//161
			ps = con.prepareStatement( "SELECT SUM(t.budget), k.tgrbudget " +
										 "FROM ZVKontentitel t, ZVKonten k " +
										"WHERE t.zvkontoid = k.id " +
										  "AND k.zweckgebunden = \"0\" " +
										  "AND t.geloescht = \"0\" " +
										  "AND k.geloescht = \"0\" " +
										"GROUP BY k.id");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
//CHANGE PreparedSqlStatements: End
//CHANGE PreparedSqlStatements: Start
		{//162
			ps = con.prepareStatement( "SELECT SUM(t.budget), k.tgrbudget " +
										 "FROM ZVKontentitel t, ZVKonten k " +
										"WHERE t.zvkontoid = ? " +
										  "AND k.zweckgebunden = \"0\" " +
										  "AND t.geloescht = \"0\" " +
										  "AND k.geloescht = \"0\" " +
										"GROUP BY k.id");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
//CHANGE PreparedSqlStatements: End
//CHANGE PreparedSqlStatements: Start
		{//163
			ps = con.prepareStatement( "SELECT DISTINCT fb2.id, fb2.budget " +
										 "FROM kontenzuordnung z, zvkonten zv, fbkonten fb1, fbkonten fb2 " +
										"WHERE z.zvkontoid = zv.id " +
										  "AND zv.zweckgebunden = \"0\" " +
										  "AND zv.geloescht = \"0\" " +
										  "AND z.fbkontoid = fb1.id " +
										  "AND fb1.geloescht = \"0\" " +
										  "AND fb1.haushaltsjahrid = fb2.haushaltsjahrid " +
										  "AND fb1.institutsid = fb2.institutsid " +
										  "AND fb1.hauptkonto = fb2.hauptkonto " +
										  "AND fb2.geloescht=\"0\" ");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
//CHANGE PreparedSqlStatements: End
//CHANGE PreparedSqlStatements: Start
		{//164
			ps = con.prepareStatement( "SELECT DISTINCT fb2.id, fb2.budget " +
										 "FROM kontenzuordnung z, fbkonten fb1, fbkonten fb2 " +
										"WHERE z.zvkontoid = ? " +
										  "AND z.fbkontoid = fb1.id " +
										  "AND fb1.geloescht = \"0\" " +
										  "AND fb1.haushaltsjahrid = fb2.haushaltsjahrid " +
										  "AND fb1.institutsid = fb2.institutsid " +
										  "AND fb1.hauptkonto = fb2.hauptkonto " +
										  "AND fb2.geloescht=\"0\" ");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
//CHANGE PreparedSqlStatements: End
		{//165
			statements[i++] = null;
		}
		{//166
			statements[i++] = null;
		}
		{//167
			statements[i++] = null;
		}
		{//168
			statements[i++] = null;
		}
		{//169
			statements[i++] = null;
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
											  "b.telefon, b.fax, b.bau, b.raum, b.swBeauftragter " +
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
																			"b.telefon, b.fax, b.bau, b.raum, b.swBeauftragter " +
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
																		"b.telefon, b.fax, b.bau, b.raum, b.swBeauftragter " +
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
																	  "b.telefon, b.fax, b.bau, b.raum, b.swBeauftragter " +
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
											  "b.telefon, b.fax, b.bau, b.raum, b.swBeauftragter " +
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
		/* Tabelle: Bestellungen				  */
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
										"FROM FBKonten a, Bestellungen b " +
										"WHERE a.id = ? " +
										"AND a.id = b.fbKonto" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//213
			/**
			 * Ermittlung der Anzahl der aktiven Bestellung, bei denen eine bestimmtes FBKonto angegeben wurde.
			 * @author w.flat 
			 */
			ps = con.prepareStatement( "SELECT COUNT(b.id) " +
										"FROM FBKonten a, Bestellungen b " +
										"WHERE a.id = ? " +
										"AND a.id = b.fbKonto " +
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
										"FROM ZVKontenTitel a, Bestellungen b " +
										"WHERE a.id = ? " +
										"AND a.id = b.zvTitel" );
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
										"FROM ZVKontenTitel a, Bestellungen b " +
										"WHERE a.id = ? " +
										"AND a.id = b.zvTitel " +
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
		
		{//218 
			statements[i++] = null;
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
		{//220 Anzahl der Buchunen zu einem Benutzer
			ps = con.prepareStatement( "SELECT COUNT(b.betrag) " +
										 "FROM Benutzer a, Buchungen b " +
										"WHERE a.id = ? " +
										"AND a.id = b.benutzerid " );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//221
			/**
			 * Ermittlung der Anzahl der Buchungen bei denen ein bestimmtes FBKonto verwendet wurde.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"SELECT COUNT(b.bestellungId) " +
										"FROM FBKonten a, Buchungen b " +
										"WHERE a.id = ? " +
										"AND (a.id = b.fbKontoId1 OR a.id = b.fbKontoId2)" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//222
			/**
			 * Anzahl der Buchungen bei denen ein bestimmter ZVTitel angegeben wurde.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"SELECT COUNT(b.bestellungId) " +
										"FROM ZVKontenTitel a, Buchungen b " +
										"WHERE a.id = ? " +
										"AND (a.id = b.zvTitelId1 OR a.id = b.zvTitelId2)" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//223
			statements[i++] = null;
		}
		{//224
			statements[i++] = null;
		}
		{//225
			statements[i++] = null;
		}
		{//226
			statements[i++] = null;
		}
		{//227
			statements[i++] = null;
		}
		{//228
			statements[i++] = null;
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
			ps = con.prepareStatement( "SELECT b.status, a.id, a.bezeichnung, a.kapitel, a.titelgruppe, a.zweckgebunden " +
																 "FROM ZVKonten a, Kontenzuordnung b, haushaltsjahre c " +
																 "WHERE a.haushaltsjahrid = c.id " +
																		"AND a.geloescht = '0' " +
																		"AND b.fbKontoId = ? " +
																		"AND b.zvKontoId = a.id " +
																		"AND c.status = 0"	 );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//234 Kontenzuordnung
			ps = con.prepareStatement( "SELECT b.status, a.id, a.bezeichnung, a.kapitel, a.titelgruppe, a.zweckgebunden " +
																 "FROM ZVKonten a, Kontenzuordnung b, haushaltsjahre c " +
																 "WHERE a.haushaltsjahrid = c.id " +
																		"AND a.geloescht = '0' " +
																		"AND b.fbKontoId = ? " +
																		"AND b.zvKontoId = ? " +
																		"AND b.zvKontoId = a.id " +
																		"AND c.status = 0"	 );
			int[] param = {Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//235
			/**
			 * Ermittlung der Anzahl der Kontenzuordnungen, <br>
			 * bei denen eine bestimmtes ZVKonto angegeben ist.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"SELECT COUNT(a.id) " +
										"FROM ZVKonten a, Kontenzuordnung b " +
										"WHERE a.id = ? " +
										"AND a.id = b.zvKontoId" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//236
			/**
			 * Ermittlung der Kontenzuordnungen, bei denen ein bestimmtes FBKonto angegeben ist.
			 * @author w.flat
			 */
			ps = con.prepareStatement( "SELECT COUNT(a.id) " +
										"FROM FBKonten a, Kontenzuordnung b " +
										"WHERE a.id = ? " +
										"AND a.id = b.fbKontoId" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//237
			/**
			 * Überprüfung ob ein ZVKonto zweckgebunden sein kann. <br>
			 * Dabei wird ermittelt ob mehr als ein ZVKonto zu dem FBKonto einer Kontozuordnung existiert.
			 * @author w.flat 
			 */
			ps = con.prepareStatement( 	"SELECT (COUNT(b.zvKontoId) - COUNT(DISTINCT b.fbKontoId)) " +
										"FROM Kontenzuordnung a, Kontenzuordnung b " +
										"WHERE a.zvKontoId = ? " +
										"AND a.fbKontoId = b.fbKontoId" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//238
			statements[i++] = null;
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
		{//245 Eine Firma in der Datenbank aktualisieren
			/**
			 * Eine Firma in der Datenbank aktualisieren.
			 * @author w.flat
			 */
			ps = con.prepareStatement( 	"UPDATE firmen " +
										"SET name = ?, strassenr = ?, plz = ?, ort = ?, kundennr = ?, " +
											"telnr = ?, faxnr = ?, email = ?, www = ?, ask = ?, geloescht = ? " +
										"WHERE id = ?" );
			int[] param = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
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
							Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
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
		{//249
			statements[i++] = null;
		}
		/******************************************/
		/* Tabelle: ASK_Standard_Bestellungen 	  */
		/* Indizes: 250-259                       */
		/******************************************/
		{//250 fügt eine StandardBestellung in die ASK_Standard_Bestellungen Tabelle ein
			ps = con.prepareStatement("INSERT " +
																"INTO ASK_Standard_Bestellungen " +
																	 "( id, bemerkungen, kostenart, ersatzbeschaffung, ersatzbeschreibung, " +
																	 "ersatzInventarNr, verwendungszweck, planvorgabe, begruendung) " +
															  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.INTEGER,  Types.VARCHAR,  Types.VARCHAR,
										 Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//251 fügt eine ASKBestellung in die ASK_Standard_Bestellungen Tabelle ein
			ps = con.prepareStatement("INSERT " +
																"INTO ASK_Standard_Bestellungen " +
																	 "( id, bemerkungen, swBeauftragter) " +
															  "VALUES (?, ?, ?)");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.INTEGER};
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
		/* Tabelle: Angebote 	                    */
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
		{//261 gibt die Angebote zur eine Bestellung mit bestellId zurück
			ps = con.prepareStatement("SELECT id, anbieter, datum, angenommen" +
																"FROM Angebote " +
															  "WHERE bestellung = ?");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//262
			statements[i++] = null;
		}
		{//263
			statements[i++] = null;
		}
		{//264
			statements[i++] = null;
		}
		/******************************************/
		/* Tabelle: Positionen                 	  */
		/* Indizes: 260-264                       */
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
			ps = con.prepareStatement("SELECT id, institut, artikel, einzelPreis, menge, mwSt, rabatt, beglichen" +
																"FROM Positionen " +
															  "WHERE angebot = ?");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//267
			statements[i++] = null;
		}
		{//268
			statements[i++] = null;
		}
		{//269
			statements[i++] = null;
		}
		/**************************************************/
		/* Join: Bestellungen, ASK_Standard_Bestellungen  */
		/* Indizes: 270-274					                      */
		/**************************************************/
		{//270 gibt die StandardBestellung mit der zugehörigen Id zurück. Es werden nur BenutzerId ermittelt
			ps = con.prepareStatement("SELECT " +																		"k.id, k.beschreibung," +																		"b.ersatzbeschaffung, b.ersatzbeschreibung, b.ersatzInventarNr, " +																		"b.verwendungszweck, b.planvorgabe, b.begruendung, b.bemerkungen, " +
																		"a.besteller, a.auftraggeber, a.empfaenger, " +																		"a.referenzNr, a.huelNr, a.phase, a.datum, a.zvTitel, a.fbKonto, a.bestellwert " +
																"FROM Bestellungen a, ASK_Standard_Bestellungen b, Kostenarten k " +
															  "WHERE a.id = ? " +															  		"AND a.id = b.id " +															  		"AND a.typ = '0' " +															  		"AND a.geloescht = '0' " +															  		"AND k.id = b.kostenart");
			int[] param = {	Types.INTEGER };
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//271
			statements[i++] = null;
		}
		{//272
			statements[i++] = null;
		}
		{//273
			statements[i++] = null;
		}
		{//274
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


