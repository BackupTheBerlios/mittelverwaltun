/*
 * File : applicationServer.test.TestServer.java
 * Created on 14.04.2005 
 */

package applicationServer.test;

import gui.*;
import dbObjects.*;

import java.net.InetAddress;
import java.rmi.Naming;
import java.sql.Date;
import java.util.ArrayList;
import applicationServer.ApplicationServer;
import applicationServer.CentralServer;

import junit.framework.*;

/**
 * Klasse TestServer. Zum Testen des ApplicationServers. 
 * @author w.flat
 */
public class TestServer extends TestCase {

	/**
	 * Host des Servers.
	 */
	final static private String HOST = "localhost";
	
	/**
	 * Name des Servers.
	 */
	final static private String NAME = "mitteltest";
	
	/**
	 * CentralServer, wo man sich anmeldet. 
	 */
	CentralServer centralServer = null;
	
	/**
	 * ApplicationServer, mit dem man arbeitet.
	 */
	ApplicationServer applicationServer = null;
	
	Exception exc = null;
	
	/**
	 * Flag ob der CentralServer und ApplicationServer gestartetet wurde oder nicht.
	 */
	static private boolean START = true;
	
	/**
	 * TestCase erstellen.
	 * @param name = Name vom TestCase.
	 */
	public TestServer(String name) {
		super(name);
	}
	
	/**
	 * Die Komplete Datenbank testen.
	 */
	public void testAll() {
		if(!START)		// Fehler bei Kommunikation-Aufbau
			return;
		
		int tempInt = 0;		// Variable zum Speichern z.B. einer Id beim Testen
		String tempStr = "";	// Variable zum Speichern eines Strings beim Testen
		int hausId = 0;
		try {
		    hausId = applicationServer.getCurrentHaushaltsjahrId();
		} catch(Exception e) { fail("Abfrage HaushaltsjahrId."); }
				
		/**
		 * Einfügen eines Benutzers 
		 */ 
		Benutzer ben1 = new Benutzer(	"t.test", (new PasswordEncrypt()).encrypt("t.test"), new Rolle(1), 
		        						new Institut(100, "", ""), "", "Test", "User", "test@web.de", 0, "0621/132455",
		        						"0621/132455", "Bau 1", "Raum 01212", true, Benutzer.VIEW_FACHBEREICH);
		try {	// Versuch einen User anzulegen beim Institut, dass nicht vorhanden ist. 
			applicationServer.addUser(ben1);
			fail("Nicht existierender Institut beim Einfügen eines Benutzers.");
		} catch(Exception e) {}
		// Versuch einen User anzulegen mit dem Privatkonto, dass nicht vorhanden ist.
		ben1.setKostenstelle(new Institut(1, "", ""));
		ben1.setPrivatKonto(1);
		try {	
			applicationServer.addUser(ben1);
			fail("Nicht existierendes FBKonto beim Einfügen eines Benutzers.");
		} catch(Exception e) {}
		// Einen Benutzer erfolgreich anlegen
		ben1.setPrivatKonto(0);
		try {
			ben1.setId(applicationServer.addUser(ben1));
		} catch(Exception e) { exc = e; fail("Erfolgreich einen Benutzer anlegen."); }
		try {		// Benutzer abfragen
		    ben1 = applicationServer.getUser("t.test", ben1.getPasswort());
		} catch(Exception e) { exc = e; fail("Benutzer abfragen."); }

		/**
		 * Einfügen eines Insituts.
		 */
		Institut inst1 = new Institut(0, "FBI allgemein", "160000", ben1);
		try {		// Anlegen eines Instituts, das schon existiert
			applicationServer.addInstitute(inst1);
			fail("Institut, das schon existiert anlegen.");
		} catch(Exception e) { }
		// Erfolgreich anlegen eines Instituts
		inst1.setBezeichnung("GAW Institut");
		inst1.setKostenstelle("160001");
		try {		
			inst1.setId(applicationServer.addInstitute(inst1));
		} catch(Exception e) { exc = e; fail("Erfolgreich einen Institut anlegen."); }
		try {
		    Institut[] tis = applicationServer.getInstitutes();
		    inst1 = null;
		    for(int i = 0; i < tis.length; i++) {
		        if(tis[i].getKostenstelle().equals("160001")) {
		            inst1 = tis[i];
		            break;
		        }
		    }
		    if(inst1 == null)
		        throw new Exception();
		} catch(Exception e) { exc = e; fail("Institute abfragen."); }
		
		/**
		 * Firma Anlegen. 
		 */
		Firma firm1 = new Firma( 0, "Meine Firma", "Strasse 1", "12345", "Ort", "256625", "0621/123456", "0621/123456", 
								"mail@mail.de", "www.page.de", false, false );
		try {			// Erfolgreich eine Firma einfügen
			firm1.setId(applicationServer.addFirma(firm1));
		} catch(Exception e) { exc = e; fail("Erfolgreich eine Firma anlegen."); }
		try {			// Gleiche Firma noch einmal einfügen
			applicationServer.addFirma(firm1);
			fail("Gleiche Firma noch einmal anlegen.");
		} catch(Exception e) { }
		try {
		    firm1.setName("Firma");
		    applicationServer.setFirma(firm1);
		} catch(Exception e) { exc = e; fail("Firma aktualisieren."); };
		try {
		    ArrayList lf = applicationServer.getFirmen();
		    firm1 = null;
		    for(int i = 0; i < lf.size(); i++) {
		        if(((Firma)lf.get(i)).getName().equals("Firma")) {
		            firm1 = (Firma)lf.get(i);
		            break;
		        }
		    }
		    if(firm1 == null)
		        throw new Exception();
		} catch(Exception e) { exc = e; fail("Firma abfragen."); };
		
		/**
		 * FBKonten Anlegen.
		 */
		FBHauptkonto haupt1 = new FBHauptkonto( 0, hausId, inst1, "Haupt 1", "01", "0000",
												0.0f, 0.0f, 0.0f, "", false, false );
		FBUnterkonto unter1 = new FBUnterkonto(0, hausId, inst1, "Unter 1", "02", "0001", 0.0f, 0.0f, true, false);
		tempInt = inst1.getId();
		inst1.setId(0);
		try {			// FBHauptkonto mit falschem Institut einfügen
			applicationServer.addFBHauptkonto(haupt1);
			fail("FBHauptkonto mit falschem Institut einfügen.");
		} catch(Exception e) { }
		inst1.setId(tempInt);
		try {			// Erfolgreich ein FBHauptkonto anlegen
		    haupt1.setId(applicationServer.addFBHauptkonto(haupt1));
		} catch(Exception e) { exc = e; fail("Erfolgreich ein FBHauptkonto anlegen."); }
		try {			// Gleiches FBHauptkonto anlegen
		    applicationServer.addFBHauptkonto(haupt1);
		    fail("Gleiches FBHauptkonto anlegen.");
		} catch(Exception e) { }
		try {			// FBUnterkonto mit falschem Hauptkonto einfügen
			applicationServer.addFBUnterkonto(unter1);
			fail("FBUnterkonto mit falschem Hauptkonto einfügen.");
		} catch(Exception e) { }
		unter1.setHauptkonto("01");
		try {			// Erfolgreich ein FBUnterkonto anlegen
		    unter1.setId(applicationServer.addFBUnterkonto(unter1));
		} catch(Exception e) { exc = e; fail("Erfolgreich ein FBUnterkonto anlegen."); }		
		try {			// Gleiches FBUnterkonto anlegen
		    applicationServer.addFBUnterkonto(unter1);
		    fail("Gleiches FBUnterkonto anlegen.");
		} catch(Exception e) { }
		haupt1.getUnterkonten().add(unter1);
		try {
		    haupt1.setHauptkonto("02");
		    applicationServer.setFBHauptkonto(haupt1);
		} catch(Exception e) { exc = e; fail("Erfolgreich ein FBUnterkonto aktualisieren."); };
		try {			// Abfragen der FBKonten und vergleichen des Budgets
		    haupt1 = (FBHauptkonto)applicationServer.getFBHauptkonten(inst1).get(0);
		    haupt1.setUnterkonten(applicationServer.getFBUnterkonten(inst1, haupt1));
		    unter1 = (FBUnterkonto)haupt1.getUnterkonten().get(0);
		} catch(Exception e) { fail("Abfragen der FBKonten."); }
		assertTrue("FBHauptkonto aktualisiert", haupt1.getHauptkonto().equals("02"));
		assertTrue("FBUnterkonto aktualisiert", unter1.getHauptkonto().equals("02"));
		
		/**
		 * ZVKonten anlegen.
		 */
		ZVKonto zvk1 = new ZVKonto( 0, hausId, "ZVKonto 1", "02436", "75", 0.0f, 0.0f, false, '1', (short)0, false, false );
		ZVTitel zvt1 = new ZVTitel( 0, zvk1, "ZVTitel 1", "24875", "", 0.0f, "", "" );
		ZVUntertitel zvut1 = new ZVUntertitel( 0, zvt1, "ZVUntertitel 1", "24870", "00", 0.0f, 0.0f, "", "", false );
		zvk1.getSubTitel().add(zvt1);
		zvt1.getSubUntertitel().add(zvut1);
		try {			// Erfolgreich ein ZVKonto anlegen
		    zvk1.setId(applicationServer.addZVKonto(zvk1));
		} catch(Exception e) { fail("Erfolgreich ein ZVKonto anlegen."); }		
		try {			// Gleiches ZVKonto anlegen
		    applicationServer.addZVKonto(zvk1);
		    fail("Gleiches ZVKonto anlegen.");
		} catch(Exception e) { }
		tempInt = zvk1.getId();
		zvk1.setId(0);
		try {			// Einen ZVTitel ohne gültigen ZVKonto anlegen
		    applicationServer.addZVTitel(zvt1);
		    fail("ZVTitel ohne gültigen ZVKonto anlegen.");
		} catch(Exception e) { }
		zvk1.setId(tempInt);
		try {			// Erfolgreich einen ZVTitel anlegen
		    zvt1.setId(applicationServer.addZVTitel(zvt1));
		} catch(Exception e) { exc = e; fail("Erfolgreich einen ZVTitel anlegen."); }		
		try {			// Gleichen ZVTitel anlegen
		    applicationServer.addZVTitel(zvt1);
		    fail("Gleichen ZVTitel anlegen.");
		} catch(Exception e) { }
		try {			// Einen ZVUntertitel ohne gültigen ZVTitel anlegen
		    applicationServer.addZVUntertitel(zvut1);
		    fail("ZVUntertitel ohne gültigen ZVTitel anlegen.");
		} catch(Exception e) { }
		zvut1.setTitel("24875");
		try {			// Erfolgreich einen ZVUntertitel anlegen
		    zvut1.setId(applicationServer.addZVUntertitel(zvut1));
		} catch(Exception e) { exc = e; fail("Erfolgreich einen ZVUntertitel anlegen."); }		
		try {			// Gleichen ZVUntertitel anlegen
		    applicationServer.addZVUntertitel(zvut1);
		    fail("Gleichen ZVUntertitel anlegen.");
		} catch(Exception e) { }
		try {
		    zvk1.setTitelgruppe("78");
		    applicationServer.setZVKonto(zvk1, true);
		} catch(Exception e) { exc = e; fail("ZVKonto aktualisieren."); }
		try {		// Abfragen vom ZVKonto
		    zvk1 = ((ZVKonto)applicationServer.getZVKonten().get(0));
		    zvt1 = (ZVTitel)zvk1.getSubTitel().get(0);
		    zvut1 = (ZVUntertitel)((ZVTitel)zvk1.getSubTitel().get(0)).getSubUntertitel().get(0);
		} catch(Exception e) { exc = e; fail("Abfragen vom ZVKonto"); }
		assertTrue("Aktualisieren vom ZVKonto", zvk1.getTitelgruppe().equals("78"));
		assertTrue("Aktualisieren vom ZVTitel", zvt1.getTGR().equals("78"));
		assertTrue("Aktualisieren vom ZVUntertitel", zvut1.getTGR().equals("78"));
		
		/**
		 * Kontenzuordnungen anlegen.
		 */
		Kontenzuordnung kz1 = new Kontenzuordnung((short)0, zvk1);
		
		tempInt = zvk1.getId();
		zvk1.setId(0);
		try {			// Kontenzuordnung mit flascher ZVKontoId anlegen
		    applicationServer.addKontenZuordnung(haupt1, zvk1);
		    fail("Kontenzuordnung mit flascher ZVKontoId anlegen.");
		} catch(Exception e) { }
		zvk1.setId(tempInt);
		tempInt = haupt1.getId();
		haupt1.setId(0);
		try {			// Kontenzuordnung mit flascher FBKontoId anlegen
		    applicationServer.addKontenZuordnung(haupt1, zvk1);
		    fail("Kontenzuordnung mit flascher FBKontoId anlegen.");
		} catch(Exception e) { }
		haupt1.setId(tempInt);
		try {			// Erfolgreich eine Kontenzuordnung anlegen
		    applicationServer.addKontenZuordnung(haupt1, zvk1);
		} catch(Exception e) { exc = e; fail("Erfolgreich eine Kontenzuordnung anlegen."); }		
		try {			// Gleiche Kontenzuordnung anlegen
		    applicationServer.addKontenZuordnung(haupt1, zvk1);
		    fail("Gleiche Kontenzuordnung anlegen.");
		} catch(Exception e) { }
		
		/**
		 * Buchungen für das ZVKonto durchführen.
		 */
		tempInt = zvk1.getId();
		zvk1.setId(0);
		try {			// Buchung mit veränderter ZVKontoId
		    applicationServer.buche(ben1, zvk1, 10000.0f);
		    fail("Buchung mit veränderter ZVKontoId.");
		} catch(Exception e) { }
		zvk1.setId(tempInt);
		tempStr = zvk1.getKapitel();
		zvk1.setKapitel("02345");
		try {			// Buchung mit verändertem Kapitel
		    applicationServer.buche(ben1, zvk1, 10000.0f);
		    fail("Buchung mit verändertem Kapitel.");
		} catch(Exception e) { }
		zvk1.setKapitel(tempStr);
		try {			// Erfolgreiche Buchung auf ein ZVKonto mit positivem Betrag
		    applicationServer.buche(ben1, zvk1, 10000.0f);
		    zvk1.setTgrBudget(10000);
		} catch(Exception e) { exc = e; fail("Erfolgreiche Buchung auf ein ZVKonto mit positivem Betrag."); }
		try {			// Erfolgreiche Buchung auf ein ZVKonto mit negativem Betrag
		    applicationServer.buche(ben1, zvk1, -5000.0f);
		    zvk1.setTgrBudget(5000);
		} catch(Exception e) { exc = e; fail("Erfolgreiche Buchung auf ein ZVKonto mit negativem Betrag."); }
		try {			// Erfolgreiche Buchung auf einen ZVTitel mit positivem Betrag
		    applicationServer.buche(ben1, zvt1, 1200.0f);
		    zvt1.setBudget(1200);
		} catch(Exception e) { exc = e; fail("Erfolgreiche Buchung auf einen ZVTitel mit positivem Betrag."); }
		try {			// Erfolgreiche Buchung auf ein ZVTitel mit negativem Betrag
		    applicationServer.buche(ben1, zvt1, -200.0f);
		    zvt1.setBudget(1000);
		} catch(Exception e) { exc = e; fail("Erfolgreiche Buchung auf einen ZVTitel mit negativem Betrag."); }
		try {			// Erfolgreiche Buchung auf einen ZVUntertitel mit positivem Betrag
		    applicationServer.buche(ben1, zvut1, 1700.0f);
		    zvut1.setBudget(1700);
		} catch(Exception e) { exc = e; fail("Erfolgreiche Buchung auf einen ZVUntertitel mit positivem Betrag."); }
		try {			// Erfolgreiche Buchung auf ein ZVTitel mit negativem Betrag
		    applicationServer.buche(ben1, zvut1, -500.0f);
		    zvut1.setBudget(1200);
		} catch(Exception e) { exc = e; fail("Erfolgreiche Buchung auf einen ZVUntertitel mit negativem Betrag."); }
		
		try {		// Abfragen vom ZVKonto
		    zvk1 = ((ZVKonto)applicationServer.getZVKonten().get(0));
		    zvt1 = (ZVTitel)zvk1.getSubTitel().get(0);
		    zvut1 = (ZVUntertitel)((ZVTitel)zvk1.getSubTitel().get(0)).getSubUntertitel().get(0);
		} catch(Exception e) { exc = e; fail("Abfragen vom ZVKonto"); }
		assertTrue("Betrag auf dem ZVKonto", zvk1.getTgrBudget() == 5000.0f);
		assertTrue("Betrag auf dem ZVTitel", zvt1.getBudget() == 1000.0f);
		assertTrue("Betrag auf dem ZVUntertitel", zvut1.getBudget() == 1200.0f);
		
		/**
		 * Buchung auf einen FBHauptkonto.
		 */
		tempInt = haupt1.getId();
		haupt1.setId(0);
		try {		// FBKonto mit falscher Id
		    applicationServer.setAccountBudget(ben1, haupt1, 5000.0f);
		    fail("Buchung auf ein FBHauptkonto mit falscher Id.");
		} catch(Exception e) { }
		haupt1.setId(tempInt);
		haupt1.setBudget(1000.0f);
		try {		// FBKonto mit falschem Betrag
		    applicationServer.setAccountBudget(ben1, haupt1, 5000.0f);
		    fail("Buchung auf ein FBHauptkonto mit falschem Betrag.");
		} catch(Exception e) { }
		haupt1.setBudget(0.0f);
		try {			// Erfolgreiche Buchung auf ein FBHauptkonto mit positivem Betrag
		    applicationServer.setAccountBudget(ben1, haupt1, 5000.0f);
			haupt1.setBudget(5000.0f);
		} catch(Exception e) { exc = e; fail("Erfolgreiche Buchung auf ein FBHauptkonto mit positivem Betrag."); }
		try {			// Erfolgreiche Buchung auf ein FBHauptkonto mit negativem Betrag
		    applicationServer.setAccountBudget(ben1, haupt1, -1000.0f);
			haupt1.setBudget(4000.0f);
		} catch(Exception e) { exc = e; fail("Erfolgreiche Buchung auf ein FBHauptkonto mit negativem Betrag."); }
		
		try {			// Abfragen der FBKonten und vergleichen des Budgets
		    haupt1 = (FBHauptkonto)applicationServer.getFBHauptkonten(inst1).get(0);
		    haupt1.setUnterkonten(applicationServer.getFBUnterkonten(inst1, haupt1));
		    unter1 = (FBUnterkonto)haupt1.getUnterkonten().get(0);
		} catch(Exception e) { exc = e; fail("Abfragen der FBKonten."); }
		assertTrue("Betrag auf dem FBHauptkonto", haupt1.getBudget() == 4000.0f);
		assertTrue("Betrag auf dem FBUnterkonto", unter1.getBudget() == 0.0f);
		
		/**
		 * Umbuchen zwischen zwei FBHauptkonten
		 */
		tempInt = haupt1.getId();
		haupt1.setId(0);
		try {		// FBKonto mit falscher Id
		    applicationServer.buche(ben1, haupt1, haupt1, 1000.0f);
		    fail("Buchung von einem auf ein anderes FBHauptkonto mit falscher Id.");
		} catch(Exception e) { }
		haupt1.setId(tempInt);
		haupt1.setBudget(1000.0f);
		try {		// FBKonto mit falschem Betrag
		    applicationServer.buche(ben1, haupt1, haupt1, 1000.0f);
		    fail("Buchung von einem auf ein anderes FBHauptkonto mit falschem Betrag.");
		} catch(Exception e) { }
		haupt1.setBudget(4000.0f);
		try {			// Erfolgreiche Buchung von einem auf ein anderes FBHauptkonto
		    applicationServer.buche(ben1, haupt1, haupt1, 1000.0f);
		} catch(Exception e) { exc = e; fail("Erfolgreiche Buchung von einem auf ein anderes FBHauptkonto."); }
		try {			// Abfragen der FBKonten und vergleichen des Budgets
		    FBHauptkonto th = (FBHauptkonto)applicationServer.getFBHauptkonten(inst1).get(0);
		    assertTrue("Betrag beim Umbuchen zwischen zwei FBkonten.", th.getBudget() == 4000.0f);
		} catch(Exception e) { exc = e; fail("Abfragen der FBKonten."); }
		
		/**
		 * Umbuchen zwischen FBHauptkonto und FBUnterkonto
		 */
		try {			// Erfolgreiche Buchung von einem FBHauptkonto auf ein FBUnterkonto
		    applicationServer.buche(ben1, haupt1, unter1, 1500.0f);
			haupt1.setBudget(2500.0f);
			unter1.setBudget(1500.0f);
		} catch(Exception e) { exc = e; fail("Erfolgreiche Buchung von einem FBHauptkonto auf ein FBUnterkonto."); }
		try {			// Erfolgreiche Buchung von einem FBUnterkonto auf ein FBHauptkonto
		    applicationServer.buche(ben1, unter1, haupt1, 200.0f);
			haupt1.setBudget(2700.0f);
			unter1.setBudget(1300.0f);
		} catch(Exception e) { exc = e; fail("Erfolgreiche Buchung von einem FBUnterkonto auf ein FBHauptkonto."); }
		try {			// Abfragen der FBKonten und vergleichen des Budgets
		    haupt1 = (FBHauptkonto)applicationServer.getFBHauptkonten(inst1).get(0);
		    haupt1.setUnterkonten(applicationServer.getFBUnterkonten(inst1, haupt1));
		    unter1 = (FBUnterkonto)haupt1.getUnterkonten().get(0);
		} catch(Exception e) { exc = e; fail("Abfragen der FBKonten."); }
		assertTrue("Betrag auf FBHauptkonto beim Umbuchen zwischen FBHauptkonto und FBUnterkonto", haupt1.getBudget() == 2700.0f);
		assertTrue("Betrag auf FBUnterkonto beim Umbuchen zwischen FBHauptkonto und FBUnterkonto", unter1.getBudget() == 1300.0f);
		
		/**
		 * Kleinbestellung durchführen
		 */
		ArrayList list = new ArrayList();
		list.add(new Beleg(0, 1, firm1, "Test-Artikel", 1200.0f)); 
		KleinBestellung klein1 = new KleinBestellung(0, new Date((new java.util.Date()).getTime()), ben1, ben1, 
		        									zvt1, unter1, 1200.0f, "01234", "Test Kleinbestellung", 
		        									"01234", "GAW", "01234", new ArrayList(list) );
		try {	// Kleinbestellung durchführen
		    applicationServer.addKleinbestellung(klein1);
		} catch(Exception e) { exc = e; fail("Kleinbestellung duchführen."); };
		try {		// Abfragen vom ZVKonto und FBKonten
		    zvk1 = ((ZVKonto)applicationServer.getZVKonten().get(0));
		    zvt1 = (ZVTitel)zvk1.getSubTitel().get(0);
		    zvut1 = (ZVUntertitel)((ZVTitel)zvk1.getSubTitel().get(0)).getSubUntertitel().get(0);
		    haupt1 = (FBHauptkonto)applicationServer.getFBHauptkonten(inst1).get(0);
		    haupt1.setUnterkonten(applicationServer.getFBUnterkonten(inst1, haupt1));
		    unter1 = (FBUnterkonto)haupt1.getUnterkonten().get(0);
		} catch(Exception e) { fail("Abfragen vom ZVKonto"); }
		assertTrue("Betrag auf dem ZVKonto bei Kleinbestellung durchführen", zvk1.getTgrBudget() == 4800.0f);
		assertTrue("Betrag auf dem ZVTitel bei Kleinbestellung durchführen", zvt1.getBudget() == 0.0f);
		assertTrue("Betrag auf dem ZVUntertitel bei Kleinbestellung durchführen", zvut1.getBudget() == 1200.0f);
		assertTrue("Betrag auf FBHauptkonto bei Kleinbestellung durchführen", haupt1.getBudget() == 2700.0f);
		assertTrue("Betrag auf FBUnterkonto bei Kleinbestellung durchführen", unter1.getBudget() == 100.0f);
		try {	// Kleinbestellung löschen
		    applicationServer.delKleinbestellung(klein1);
		} catch(Exception e) { fail("Kleinbestellung duchführen."); };
		try {		// Abfragen vom ZVKonto und FBKonten
		    zvk1 = ((ZVKonto)applicationServer.getZVKonten().get(0));
		    zvt1 = (ZVTitel)zvk1.getSubTitel().get(0);
		    zvut1 = (ZVUntertitel)((ZVTitel)zvk1.getSubTitel().get(0)).getSubUntertitel().get(0);
		    haupt1 = (FBHauptkonto)applicationServer.getFBHauptkonten(inst1).get(0);
		    haupt1.setUnterkonten(applicationServer.getFBUnterkonten(inst1, haupt1));
		    unter1 = (FBUnterkonto)haupt1.getUnterkonten().get(0);
		} catch(Exception e) { exc = e; fail("Abfragen vom ZVKonto"); }
		assertTrue("Betrag auf dem ZVKonto bei Kleinbestellung stornieren", zvk1.getTgrBudget() == 5000.0f);
		assertTrue("Betrag auf dem ZVTitel bei Kleinbestellung stornieren", zvt1.getBudget() == 1000.0f);
		assertTrue("Betrag auf dem ZVUntertitel bei Kleinbestellung stornieren", zvut1.getBudget() == 1200.0f);
		assertTrue("Betrag auf FBHauptkonto bei Kleinbestellung stornieren", haupt1.getBudget() == 2700.0f);
		assertTrue("Betrag auf FBUnterkonto bei Kleinbestellung stornieren", unter1.getBudget() == 1300.0f);
		
		/**
		 * ASK-Bestellung durchführen. 
		 */
		ArrayList positions = new ArrayList();
		positions.add(new Position(1, "Pos 1", 500.0f, 2, 0.16f, inst1, false));
		positions.add(new Position(2, "Pos 2", 250.0f, 3, 0.16f, inst1, false));
		Firma firm2 = null;
		try {
		    firm2 = applicationServer.getASKFirma();
		} catch(Exception e) { exc = e; fail("ASK-Firma abfragen."); };
		Angebot angebot1 = new Angebot(positions, new Date((new java.util.Date()).getTime()), firm2, true);
		ASKBestellung ask1 = new ASKBestellung("01234", null, new Date((new java.util.Date()).getTime()), ben1,
		        				ben1, ben1, zvut1, haupt1, 1750.0f, 0, '1', angebot1, "Keine Bemerkungen.", ben1);
		try { // ASK-Bestellung durchführen
		    ask1.setVerbindlichkeiten(1750.0f);
		    ask1.getZvtitel().setVormerkungen(1750.0f);
		    ask1.getFbkonto().setVormerkungen(1750.0f);
		    ask1.setId(applicationServer.addBestellung(ask1));
		    ask1 = applicationServer.getASKBestellung(ask1.getId());
		} catch(Exception e) { exc = e; fail("ASK-Bestellung duchführen."); };
		try { // ASK-Bestellung abwickeln
		    ASKBestellung ask2 = (ASKBestellung)ask1.clone();
		    ask2.setPhase('1');
		    ask2.setVerbindlichkeiten(0.0f);
		    ask2.getZvtitel().setVormerkungen(0.0f);
		    ask2.getFbkonto().setVormerkungen(0.0f);
		    ask2.setBestellwert(1750.0f);
		    for(int i = 0; i < ask2.getAngebot().getPositionen().size(); i++) {
		        ((Position)ask2.getAngebot().getPositionen().get(0)).setBeglichen(true);
		    }
		    applicationServer.setBestellung(ben1, ask1, ask2, true);
		} catch(Exception e) { exc = e; fail("ASK-Bestellung abwickeln."); };
		try {		// Abfragen vom ZVKonto und FBKonten
		    zvk1 = ((ZVKonto)applicationServer.getZVKonten().get(0));
		    zvt1 = (ZVTitel)zvk1.getSubTitel().get(0);
		    zvut1 = (ZVUntertitel)((ZVTitel)zvk1.getSubTitel().get(0)).getSubUntertitel().get(0);
		    haupt1 = (FBHauptkonto)applicationServer.getFBHauptkonten(inst1).get(0);
		    haupt1.setUnterkonten(applicationServer.getFBUnterkonten(inst1, haupt1));
		    unter1 = (FBUnterkonto)haupt1.getUnterkonten().get(0);
		} catch(Exception e) { exc = e; fail("Abfragen vom ZVKonto"); }
		assertTrue("Betrag auf dem ZVKonto bei ASK-Bestellung abwickeln", zvk1.getTgrBudget() == 4450.0f);
		assertTrue("Betrag auf dem ZVTitel bei ASK-Bestellung abwickeln", zvt1.getBudget() == 1000.0f);
		assertTrue("Betrag auf dem ZVUntertitel bei ASK-Bestellung abwickeln", zvut1.getBudget() == 0.0f);
		assertTrue("Betrag auf FBHauptkonto bei ASK-Bestellung abwickeln", haupt1.getBudget() == 950.0f);
		assertTrue("Betrag auf FBUnterkonto bei ASK-Bestellung abwickeln", unter1.getBudget() == 1300.0f);
		
		
//		try {
//			applicationServer.delUser(ben1);
//		} catch(Exception e) { exc = e; fail("Erfolgreich einen Benutzer löschen."); }
		/**
		 * 
		 */
	}
	
	/**
	 * Herstellen der Verbindung zum Server. 
	 */
	public void setUp() {
		try{
			centralServer = (CentralServer)Naming.lookup("//" + HOST + "/" + NAME);
			applicationServer = centralServer.getMyApplicationServer(	InetAddress.getLocalHost().getHostName(), 
																		InetAddress.getLocalHost().getHostAddress());
			if(applicationServer == null)
				throw new Exception("Der ApplicationServer konnte nicht gestartet werden.");
			// Benutzer abfragen
			Benutzer benutzer = applicationServer.login("admin", (new PasswordEncrypt()).encrypt("admin"));
			// Dem CentralServer den Namen des Users übertragen
			centralServer.addBenutzerNameToUser(applicationServer.getId(), benutzer.getBenutzername());
		} catch(Exception exc) {
			exc.printStackTrace();
			START = false;
		}
		
	}
	
	/**
	 * Abbau der Verbindung. 
	 */
	public void tearDown() {
		if(!START)
			return;
		try {
			centralServer.delUser( applicationServer.getId() );
		} catch(Exception e1) {
		}
		try {
			centralServer.delUser( applicationServer.getId() );
		} catch(Exception e1) {
		}
		
		if(exc != null)
		    exc.printStackTrace();
	}

	public static void main(String[] args) {
        junit.swingui.TestRunner.run(TestServer.class);
	}
}
