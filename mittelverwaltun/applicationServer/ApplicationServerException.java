package applicationServer;

public class ApplicationServerException extends Exception {

	private static int NOM = 200;
	private static String[] messages = null;

	private int errorCode;

	public ApplicationServerException(int errorCode){
		super();

		if (messages == null) initializeErrorMessages();

		if ((errorCode >= 1) && (errorCode < messages.length))
			this.errorCode = errorCode;
		else this.errorCode = 0;
	}

	public ApplicationServerException(int errorCode, String message) {
		super(message);

		if (messages == null) initializeErrorMessages();
		if ((errorCode >= 1) && (errorCode < messages.length))
			this.errorCode = errorCode;
		else this.errorCode = 0;
	}

	public String getMessage(){
		return messages[errorCode] + " (" + errorCode + ")";
//		if(errorCode == 0)
//			return super.getMessage() + " (" + errorCode + ")";
//		else
//			return messages[errorCode] + " (" + errorCode + ")";
	}

	public String getNestedMessage(){
		return super.getMessage();
	}

	public int getErrorCode(){
		return errorCode;
	}

	public static void initializeErrorMessages(){

		messages = new String[NOM];

		for(int i = 0; i < messages.length; i++){
			messages[i] = null;
		}

//	Exception Anfang WALDEMAR von 0-49

		messages[0] = "Application Server Error: keine Fehlermeldung vorhanden.";
		messages[1] = "Application Server Error: Fehler bei Datenbankzugriff.";
		messages[2] = "Application Server Error: Benutzer existiert nicht mehr in der DB.";
		messages[3] = "Application Server Error: Institut existiert nicht mehr.";
		messages[4] = "Application Server Error: Institut bzw. Kostenstelle bereits vorhanden.";
		messages[5] = "Application Server Error: Benutzername in der DB bereits vergeben.";
		messages[6] = "Application Server Error: Es existiert kein aktives Haushaltsjahr.";
		messages[7] = "Application Server Error: Rolle existiert nicht mehr.";
		messages[8] = "Application Server Error: Rollenname bereits vergeben.";
		messages[9] = "Application Server Error: Rolle wird noch von Benutzern verwendet.";
		messages[10] = "Application Server Error: Das ZVKonto existiert nicht.";
		messages[11] = "Application Server Error: Das ZVKonto existiert bereits.";
		messages[12] = "Application Server Error: Der ZVTitel existiert nicht.";
		messages[13] = "Application Server Error: Der ZVTitel existiert bereits.";
		messages[14] = "Application Server Error: Der ZVUntertitel existiert nicht.";
		messages[15] = "Application Server Error: Der ZVUntertitel existiert bereits.";
		messages[16] = "Application Server Error: Das FBHauptkonto existiert nicht.";
		messages[17] = "Application Server Error: Das FBHauptkonto existiert bereits.";
		messages[18] = "Application Server Error: Das FBUnterkonto existiert nicht.";
		messages[19] = "Application Server Error: Das FBUnterkonto existiert bereits.";
		messages[20] = "Application Server Error: Bestellungen noch nicht abgeschlossen.";
		messages[21] = "Application Server Error: Kontenzuordnungen noch vorhanden.";
		messages[22] = "Application Server Error: Konto ist einem Benutzer zugeordnet.";
		messages[23] = "Application Server Error: Das FBHauptkonto ist gelöscht.";
		messages[24] = "Application Server Error: Das FBUnterkonto ist gelöscht.";
		messages[25] = "Application Server Error: Die FBHauptkonten unterscheiden sich [DB - Application].";
		messages[26] = "Application Server Error: Die FBHauptkonten-Budgets unterscheiden sich [DB - Application].";
		messages[27] = "Application Server Error: Die FBUnterkonten unterscheiden sich [DB - Application].";
		messages[28] = "Application Server Error: Die FBUnterkonten-Budgets unterscheiden sich [DB - Application].";
		messages[29] = "Application Server Error: Das ZVKonto ist gelöscht.";
		messages[30] = "Application Server Error: Der ZVTitel ist gelöscht.";
		messages[31] = "Application Server Error: Der ZVUntertitel ist gelöscht.";
		messages[32] = "Application Server Error: Die ZVKonten-Budgets unterscheiden sich [DB - Application].";
		messages[33] = "Application Server Error: Die ZVTitel-Budgets unterscheiden sich [DB - Application].";
		messages[34] = "Application Server Error: Die ZVUntertitel-Budgets unterscheiden sich [DB - Application].";
		messages[35] = "Application Server Error: Die ZVKonten unterscheiden sich [DB - Application].";
		messages[36] = "Application Server Error: Die ZVTitel unterscheiden sich [DB - Application].";
		messages[37] = "Application Server Error: Die ZVUntertitel unterscheiden sich [DB - Application].";
		messages[38] = "Application Server Error: Die Firma existiert bereits.";
		messages[39] = "Application Server Error: Die Firma ist bereits gelöscht.";
		messages[40] = "Application Server Error: Das FBKonto kann nicht verändert werden." +						"Es gibt Buchungen, Bestellungen oder Benutzer, die dieses Konto verwenden.";
		messages[41] = "Application Server Error: Der ZVTitel/ZVUntertitel kann nicht verändert werden." +
						"Es gibt Buchungen oder Bestellungen, die diesen ZVTitel/ZVUntertitel verwenden.";
		messages[42] = "Application Server Error: Die Firma kann nicht gelöscht werden, " +							"da sie in Belegen oder Angeboten verwendet wird.";

//		Exception Ende WALDEMAR

//		Exception Anfang ROBERT von 50-99

		messages[50] = "Application Server Error: Institut wurde in der Zwischenzeit verändert.";
		messages[51] = "Application Server Error: Institut ist ein Fachbereich.";
		messages[52] = "Application Server Error: Dem Institut sind FBHauptkonten zugeordnet.";
		messages[53] = "Application Server Error: Dem Institut sind Benutzer zugeordnet.";
		messages[54] = "Application Server Error: Benutzername in der MySQL-DB bereits vergeben.";
		messages[55] = "Application Server Error: Benutzer wurde in der Zwischenzeit verändert.";
		messages[56] = "Application Server Error: Benutzer hat noch aktive Bestellungen.";

		messages[57] = "Application Server Error: Fachbereich existiert nicht mehr.";
		messages[58] = "Application Server Error: Fachbereich wurde in der Zwischenzeit verändert.";
		messages[59] = "Application Server Error: Rolle wurde in der Zwischenzeit verändert.";

		messages[60] = "Application Server Error: Kontenzuordnung existiert nicht mehr.";
		messages[61] = "Application Server Error: Dem FBKonto darf nur ein \nzweckgebundenes ZVKonto zugeordnet werden.";
		messages[62] = "Application Server Error: Kontenzuordnung existiert schon.";

		messages[63] = "Application Server Error: ZVKonto existiert nicht mehr.";
		messages[64] = "Application Server Error: Das Fachbereichskonto existiert nicht mehr.";
		
		messages[65] = "Application Server Error: Fehler bei Datenbankzugriff - Database.insertBestellung.";
		messages[66] = "Application Server Error: Fehler bei Datenbankzugriff - Database.insertStandardBestellung.";
		messages[67] = "Application Server Error: Fehler bei Datenbankzugriff - Database.insertAngebot.";
		messages[68] = "Application Server Error: Fehler bei Datenbankzugriff - Database.insertPosition.";
		messages[69] = "Application Server Error: Fehler bei Datenbankzugriff - Database.insertASKBestellung.";
		messages[70] = "Application Server Error: Die Bestellung existiert nicht mehr";
		messages[71] = "Application Server Error: Fehler bei Datenbankzugriff - Database.selectStandardBestellung";
		messages[72] = "Application Server Error: Fehler bei Datenbankzugriff - Database.selectUser(userId)";
		messages[73] = "Application Server Error: Fehler bei Datenbankzugriff - Database.selectAngebote";
		messages[74] = "Application Server Error: Fehler bei Datenbankzugriff - Database.selectPositionen";
		messages[75] = "Application Server Error: Fehler bei Datenbankzugriff - Database.selectFirma";
		messages[76] = "Application Server Error: Die Bestellung hat sich zwischenzeitlich geändert";
		messages[77] = "Application Server Error: Fehler bei Datenbankzugriff - Database.checkReferenzNr";
		messages[78] = "Application Server Error: Die ReferenzNr der Bestellung ist schon vergeben";
		messages[78] = "Application Server Error: Bestellung existiert nicht mehr";
		messages[79] = "Application Server Error: Fehler bei Datenbankzugriff - Database.updateStandardBestellung";
		messages[80] = "Application Server Error: Fehler bei Datenbankzugriff - Database.deleteAngebote";
		messages[81] = "Application Server Error: Fehler bei Datenbankzugriff - Database.updateAngebot";
		messages[82] = "Application Server Error: Angebot existiert nicht mehr";
		messages[83] = "Application Server Error: Position existiert nicht mehr";
		messages[84] = "Application Server Error: Fehler bei Datenbankzugriff - Database.updatePosition";
		messages[85] = "Application Server Error: Fehler bei Datenbankzugriff - Database.deleteOfferPositions";
		messages[86] = "Application Server Error: Fehler bei Datenbankzugriff - Database.deletePositions";
		messages[87] = "Application Server Error: Fehler bei Datenbankzugriff - Database.deleteAngebot";
		messages[88] = "Application Server Error: Fehler bei Datenbankzugriff - Database.deletePosition";
		messages[89] = "Application Server Error: Position hat sich zwischenzeitlich geändert";
		messages[90] = "Application Server Error: Fehler bei Datenbankzugriff - Database.selectForUpdatePosition";
		messages[91] = "Application Server Error: Fehler bei Datenbankzugriff - Database.deleteBestellung";
		messages[92] = "Application Server Error: Fehler bei Datenbankzugriff - Database.deleteASK_Standard_Bestellung";
		messages[93] = "Application Server Error: Fehler bei Datenbankzugriff - Database.selectForUpdateASKBestellung";
		messages[94] = "Application Server Error: Fehler bei Datenbankzugriff - Database.updateASKBestellung";
		messages[95] = "Application Server Error: Fehler bei Datenbankzugriff - Database.selectSwBeauftragte";
		messages[96] = "Application Server Error: Fehler bei Datenbankzugriff - Database.selectASKFirma";
		messages[97] = "Application Server Error: Fehler bei Datenbankzugriff - Database.selectASKBestellung";
		messages[98] = "Application Server Error: Fehler bei Datenbankzugriff - Database.updateVormerkungen";
		messages[99] = "Application Server Error: FBKonto bzw. ZVTitel existiert nicht";
			
//		Exception Ende ROBERT von 50-99

//		Exception Anfang WALDEMAR von 100-149

//		Exception Ende WALDEMAR von 100-149


//		Exception Anfang Mario 150
		messages[150] = "Application Server Error: Fehler bei Datenbankzugriff - Database.selectNoPurposeZVBudgetSum.";
		messages[151] = "Application Server Error: Fehler bei Datenbankzugriff - Database.selecTotalAccountBudget.";
		messages[152] = "Application Server Error: Das Fachbereichskonto wurde in der Zwischenzeit gelöscht.";
		messages[153] = "Application Server Error: Das Kontobudget wurde in der Zwischenzeit verändert.";
		messages[154] = "Application Server Error: Das Fachbereichskonto konnte nicht identifiziert werden.";
		messages[155] = "Application Server Error: Die Budgetänderung konnte nicht durchgeführt werden.";
		messages[156] = "Application Server Error: Fehler bei Datenbankzugriff - Database.selectNoPurposeFBBudgetSum.";
		messages[157] = "Application Server Error: Fehler bei Datenbankzugriff - Database.selectDistributedAccountBudget";
		messages[158] = "Application Server Error: Fehler bei Datenbankzugriff - Database.selectBestellungen";
//Exception Ende Mario 199
	}


}