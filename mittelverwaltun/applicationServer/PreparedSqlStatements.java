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

		statements = new PreparedStatementWrapper[250];
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
		{//3
			ps = con.prepareStatement( "SET AUTOCOMMIT = 0");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//4
			ps = con.prepareStatement( "BEGIN");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
//		Ende Robert 08.09.2004
		{//5
			statements[i++] = null;
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
												"privatKontoId = ?, telefon = ?, fax = ?, bau = ?, raum = ?, softwarebeauftragter = ?" +
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
												"privatKontoId = NULL, telefon = ?, fax = ?, bau = ?, raum = ?, softwarebeauftragter = ?" +
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
												"name, vorname, email, privatKontoId, telefon, fax, bau, raum, softwarebeauftragter)" +
										"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
										Statement.RETURN_GENERATED_KEYS);
			int[] param = {Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.VARCHAR,
							Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER };
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//25			(5)
			ps = con.prepareStatement(	"INSERT " +
										  "INTO Benutzer " +
												"(benutzername, passwort, rollenId, institutsId, titel, " +
												"name, vorname, email, telefon, fax, bau, raum, softwarebeauftragter) " +
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
		{//28 Gibt die Benutzer eines Instituts zurück
			ps = con.prepareStatement("SELECT b.benutzername, b.passwort, b.vorname, b.name " +
										"FROM Benutzer b, Institute a " +
									   "WHERE a.id = ? AND a.id = b.institutsid AND b.geloescht = '0'");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
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
			ps = con.prepareStatement( "SELECT COUNT(b.privatKontoId) " +
										"FROM FBKonten a, Benutzer b " +
										"WHERE a.id = ? " +
										"AND a.id = b.privatKontoId AND b.geloescht != '0'" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//32
			statements[i++] = null;
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
											  "profPauschale = ? " +
										"WHERE institutsId = ?");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.FLOAT, Types.INTEGER};
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
			ps = con.prepareStatement("SELECT id, haushaltsjahrId, institutsId, bezeichnung, "+
											 "hauptkonto, unterkonto, budget, dispoLimit, pruefBedingung " +
										"FROM FBKonten " +
									   "WHERE institutsID = ? " +
									     "AND unterkonto = \"0000\" " +
									     "AND geloescht = \"0\"");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//51			(10)
			ps = con.prepareStatement("SELECT a.id, a.haushaltsjahrId, a.institutsId, a.bezeichnung, " +
											 "a.hauptkonto, a.unterkonto, a.budget " +
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
			ps = con.prepareStatement("INSERT " +
										"INTO FBKonten " +
											 "(haushaltsjahrId, institutsID, bezeichnung, hauptkonto, " +
											 "unterkonto, budget, dispoLimit, pruefBedingung, geloescht) " +
									  "VALUES (?,?,?,?,?,?,?,?,?)");
			int[] param = {Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.FLOAT, Types.FLOAT, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//53			(12)
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
			ps = con.prepareStatement("UPDATE FBKonten " +
										 "SET haushaltsjahrId = ?, "+
										 	 "institutsId = ?, " +
										 	 "bezeichnung = ?, " +
											 "hauptkonto = ?, " +
											 "unterkonto = ?, " +
											 "budget = ?, " +
											 "dispoLimit = ?, " +
											 "pruefBedingung = ?, " +
											 "geloescht = ? " +
									   "WHERE id = ?");
			int[] param = {Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
							Types.FLOAT, Types.FLOAT, Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//56			(37)
			ps = con.prepareStatement( "INSERT " +
										 "INTO FBKonten " +
											  "(haushaltsjahrId, institutsID, bezeichnung, hauptkonto, " +
											  "unterkonto, budget, geloescht) " +
									   "VALUES (?,?,?,?,?,?,?)"	 );
			int[] param = {Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.FLOAT, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//57			(38)
			ps = con.prepareStatement( "UPDATE FBKonten " +
										  "SET haushaltsjahrId = ?, institutsId = ?, bezeichnung = ?, " +
											  "hauptkonto = ?, unterkonto = ?, budget = ?, geloescht = ? " +
										"WHERE id = ?"	 );
			int[] param = {Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.FLOAT, Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//58
			ps = con.prepareStatement( "SELECT " +
										"haushaltsjahrId , institutsId, bezeichnung, " +
										"hauptkonto, unterkonto, budget, dispoLimit, pruefBedingung, geloescht " +
										"FROM FBKonten WHERE id = ? FOR UPDATE");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//59
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
		{//80			(17)
			ps = con.prepareStatement("SELECT * FROM Institute");
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//81			(18)
			ps = con.prepareStatement("UPDATE Institute " +
										 "SET bezeichnung = ?, kostenstelle = ? " +
									   "WHERE id = ?");
			int[] param = {Types.VARCHAR, Types.INTEGER, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}

		{//82			(19)
			ps = con.prepareStatement("INSERT " +
										"INTO Institute " +
											 "(bezeichnung ,kostenstelle) " +
									  "VALUES (?, ?)",
									  Statement.RETURN_GENERATED_KEYS);
			int[] param = {Types.VARCHAR, Types.INTEGER};
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
			ps = con.prepareStatement("SELECT * " +
										"FROM Institute " +
									   "WHERE id = ? FOR UPDATE");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//86
			statements[i++] = null;
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
			ps = con.prepareStatement( "SELECT id, haushaltsjahrid, bezeichnung, " +
											  "kapitel, titelgruppe, tgrBudget, " +
											  "dispoLimit, zweckgebunden, freigegeben, " +
											  "uebernahmestatus " +
										 "FROM ZVKonten " +
										"WHERE geloescht = \"0\""	 );
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//121			(42)
			ps = con.prepareStatement( "INSERT " +
										 "INTO ZVKonten " +
											  "(haushaltsjahrid, bezeichnung, kapitel, " +
											  "titelgruppe, tgrBudget, dispoLimit, "+
											  "zweckgebunden, freigegeben, uebernahmestatus) " +
									   "VALUES (?,?,?,?,?,?,?,?,?)");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.FLOAT, Types.FLOAT,
							Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//122			(43)
			ps = con.prepareStatement("SELECT id " +
										"FROM ZVKonten " +
									   "WHERE kapitel = ? " +
										 "AND titelgruppe = ? AND geloescht = '0'");
			int[] param = {Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//123			(47)
			ps = con.prepareStatement("DELETE FROM ZVKonten WHERE id = ?");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//124			(49)
			ps = con.prepareStatement( "UPDATE ZVKonten " +
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
			ps = con.prepareStatement("SELECT id " +
										"FROM ZVKonten " +
									   "WHERE kapitel = ? " +
										 "AND titelgruppe = ? AND geloescht != '0'");
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
		{//141		(41)
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
			ps = con.prepareStatement( "SELECT id " +
										 "FROM ZVKontenTitel " +
										"WHERE zvKontoID = ? " +
										  "AND titel = ? " +
										  "AND untertitel = ? AND geloescht = '0'");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//143		(46)
			ps = con.prepareStatement( "INSERT " +
										 "INTO ZVKontenTitel " +
											  "(zvKontoID, bezeichnung, titel, " +
											  "untertitel, budget, bemerkung, pruefBedingung) " +
									   "VALUES (?,?,?,?,?,?,?)");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.FLOAT, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//144		(48)
			ps = con.prepareStatement("DELETE FROM ZVKontenTitel WHERE id = ?");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//145		(50)
			ps = con.prepareStatement( "UPDATE ZVKontenTitel " +
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
			ps = con.prepareStatement( "SELECT zvKontoID, bezeichnung, " +
											  "titel, untertitel, budget, " +
											  "bemerkung, pruefBedingung, geloescht " +
										"FROM ZVKontenTitel " +
										"WHERE id = ? FOR UPDATE");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//147
			ps = con.prepareStatement( "SELECT id " +
										 "FROM ZVKontenTitel " +
										"WHERE zvKontoID = ? " +
										  "AND titel = ? " +
										  "AND untertitel = ? AND geloescht != '0'");
			int[] param = {Types.INTEGER, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//148
			statements[i++] = null;
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
		{//170		(24)
			ps = con.prepareStatement( "SELECT b.id, b.benutzername, b.passwort, " +
											  "r.id, r.bezeichnung, " +
											  "i.id, i.bezeichnung, i.kostenstelle, " +
											  "b.titel, b.name, b.vorname, b.email, b.privatKontoId, " +
											  "b.telefon, b.fax, b.bau, b.raum, b.softwarebeauftragter " +
										"FROM Benutzer b, Institute i, Rollen r " +
									   "WHERE b.institutsId = i.id " +
									     "AND b.rollenId = r.id " +
									     "AND b.benutzername = ? " +
									     "AND b.passwort = ? " +
									     "AND b.geloescht = 0 ");
			int[] param = {Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//171		(25)
			ps = con.prepareStatement( "SELECT b.id, b.benutzername, b.passwort, b.rollenId, " +
															   		  "b.titel, b.name, b.vorname, b.email, b.privatKontoId, "+
															   		  "i.id, i.bezeichnung, i.kostenstelle, " +
															   		  "r.id, r.bezeichnung, " +
																			"b.telefon, b.fax, b.bau, b.raum, b.softwarebeauftragter " +
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
																		"b.telefon, b.fax, b.bau, b.raum, b.softwarebeauftragter " +
																"FROM Benutzer b, Institute i, Rollen r " +
															   "WHERE b.institutsId = i.id " +
																 "AND b.rollenId = r.id " +
																 "AND b.benutzername = ? " +
																 "AND b.passwort = ? " +
																 "AND b.geloescht = 0 FOR UPDATE");
			int[] param = {Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//173
			statements[i++] = null;
		}
		{//174
			statements[i++] = null;
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
											  "b.bezeichnung, b.kostenstelle " +
										 "FROM Fachbereiche a, Institute b " +
										"WHERE a.institutsid = b.id" );
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//181 SELECT FOR UPDATE
			ps = con.prepareStatement( "SELECT a.institutsId, a.bezeichnung AS hochschule, a.profPauschale, " +
											  "b.bezeichnung, b.kostenstelle " +
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
		{//210
			ps = con.prepareStatement( "SELECT COUNT(b.id) " +
										 "FROM Benutzer a, Bestellungen b " +
										"WHERE a.id = ? " +
										"AND a.id = b.bestellerid " );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//211
			ps = con.prepareStatement( "SELECT COUNT(b.id) " +
										"FROM Benutzer a, Bestellungen b " +
										"WHERE a.id = ? " +
										"AND a.id = b.bestellerid " +
										"AND b.status != '0'" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		
		{//212
			ps = con.prepareStatement( "SELECT COUNT(b.id) " +
										"FROM FBKonten a, Bestellungen b " +
										"WHERE a.id = ? " +
										"AND a.id = b.fbKontoId" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//213
			ps = con.prepareStatement( "SELECT COUNT(b.id) " +
										"FROM FBKonten a, Bestellungen b " +
										"WHERE a.id = ? " +
										"AND a.id = b.fbKontoId " +
										"AND b.status != '0'" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//214
			ps = con.prepareStatement( "SELECT COUNT(b.id) " +
										"FROM ZVKontenTitel a, Bestellungen b " +
										"WHERE a.id = ? " +
										"AND a.id = b.zvTitelId" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//215
			ps = con.prepareStatement( "SELECT COUNT(b.id) " +
										"FROM ZVKontenTitel a, Bestellungen b " +
										"WHERE a.id = ? " +
										"AND a.id = b.zvTitelId " +
										"AND b.status != '0'" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//216 Active Bestellungen, die die angegebene Firma beinhalten
			ps = con.prepareStatement( "SELECT COUNT(b.id) " +
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
		{//219
			statements[i++] = null;
		}

		/******************************************/
		/* Tabelle: Buchungen   								  */
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
			ps = con.prepareStatement( "SELECT COUNT(b.bestellungId) " +
										 "FROM FBKonten a, Buchungen b " +
										"WHERE a.id = ? " +
										"AND (a.id = b.fbKontoId1 OR a.id = b.fbKontoId2)" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//222
			ps = con.prepareStatement( "SELECT COUNT(b.bestellungId) " +
										 "FROM ZVKontenTitel a, Buchungen b " +
										"WHERE a.id = ? " +
										"AND a.id = b.zvTitelId" );
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
			ps = con.prepareStatement( "SELECT COUNT(a.id) " +
										 "FROM ZVKonten a, Kontenzuordnung b " +
										"WHERE a.id = ? " +
										"AND a.id = b.zvKontoId" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//236
			ps = con.prepareStatement( "SELECT COUNT(a.id) " +
										 "FROM FBKonten a, Kontenzuordnung b " +
										"WHERE a.id = ? " +
										"AND a.id = b.fbKontoId" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//237
			ps = con.prepareStatement( "SELECT (COUNT(b.zvKontoId) - COUNT(DISTINCT b.fbKontoId)) " +
										 "FROM Kontenzuordnung a, Kontenzuordnung b " +
										"WHERE a.zvKontoId = ? " +
										"AND a.fbKontoId = b.fbKontoId" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//238
			statements[i++] = null;
		}
		{//239
			statements[i++] = null;
		}
		
		/******************************************/
		/* Tabelle: Buchungen   				  */
		/* Indizes: 240-249                       */
		/******************************************/
		{//240 Alle Firmen von der Tabelle abfragen, die nicht gelöscht sind 
			ps = con.prepareStatement( "SELECT id, name, strassenr, plz, ort, kundennr, telnr, faxnr, email, www, geloescht " +
										 "FROM firmen WHERE geloescht = '0'" );
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//241 Alle Firmen von der Tabelle abfragen, die gelöscht sind 
			ps = con.prepareStatement( "SELECT id, name, strassenr, plz, ort, kundennr, telnr, faxnr, email, www, geloescht " +
										 "FROM firmen WHERE geloescht != '0'" );
			statements[i++] = new PreparedStatementWrapper(ps);
		}
		{//242 Eine Firma zum Aktualisieren abfragen
			ps = con.prepareStatement( "SELECT id, name, strassenr, plz, ort, kundennr, telnr, faxnr, email, www, geloescht " +
										 "FROM firmen WHERE id = ? FOR UPDATE" );
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//243 Abfrage ob eine nicht gelöschte Firma existiert
			ps = con.prepareStatement( "SELECT id FROM firmen " +
										 "WHERE name = ? AND strassenr = ? AND plz = ? AND ort = ? AND geloescht = '0'" );
			int[] param = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//244 Abfrage ob eine gelöschte Firma existiert
			ps = con.prepareStatement( "SELECT id FROM firmen " +										 "WHERE name = ? AND strassenr = ? AND plz = ? AND ort = ? AND geloescht != '0'" );
			int[] param = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//245 Eine Firma in der Datenbank aktualisieren
			ps = con.prepareStatement( "UPDATE firmen " +
										"SET name = ?, strassenr = ?, plz = ?, ort = ?, kundennr = ?, " +
										"telnr = ?, faxnr = ?, email = ?, www = ?, geloescht = ? " +
										"WHERE id = ?" );
			int[] param = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//246 Eine Firma in die Datenbank einfügen
			ps = con.prepareStatement( "INSERT INTO firmen " +
										"(name, strassenr, plz, ort, kundennr, telnr, faxnr, email, www, geloescht) " +										"VALUES (?,?,?,?,?,?,?,?,?,?)" );
			int[] param = {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//247 Eine Firma in der Datenbank löschen
			ps = con.prepareStatement("DELETE FROM firmen WHERE id = ?");
			int[] param = {Types.INTEGER};
			statements[i++] = new PreparedStatementWrapper(ps, param);
		}
		{//248
			statements[i++] = null;
		}
		{//249
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


