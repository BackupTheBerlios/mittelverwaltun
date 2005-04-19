/* Created on 19.04.2005. */

package gui.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.netbeans.jemmy.*;
import org.netbeans.jemmy.operators.*;

/**
 * Klasse TestGUI.
 * @author w.flat
 */
public class TestClientGUI extends TestCase {

    public TestClientGUI(String name) {
    	super(name);
    }

	/**
	 * Testen des Reiters "Server"
	 */
	public void testLogin() {
		String url = "localhost";
		String servername = "mittelverwaltung";

		// Frame finden
		JFrameOperator mainFrame = new JFrameOperator("Login Mittelverwaltung");
		// TabbedPane finden
		JTabbedPaneOperator tabPane = new JTabbedPaneOperator(mainFrame);
		
		// Den dritten Reiter aswählen
//		tabPane.setSelectedIndex(1);
//		JTextFieldOperator tfURL = new JTextFieldOperator(tabPane, url);
//		JTextFieldOperator tfServername = new JTextFieldOperator(tabPane, servername);
//
//		tfURL.setText("");
//		new JButtonOperator(tabPane, "Einstellungen Speichern").push();
//		new JDialogOperator(mainFrame, "Error !").close();
//		tfURL.setText(url);
//
//		tfServername.setText("");
//		new JButtonOperator(tabPane, "Einstellungen Speichern").push();
//		new JDialogOperator(mainFrame, "Error !").close();
//		tfServername.setText(servername);
//		
//		new JButtonOperator(tabPane, "Einstellungen Speichern").push();

		// Den ersten Reiter testen
		String benutzer = "w.flat";
		
		tabPane.setSelectedIndex(0);
		JTextFieldOperator tfBenutzer = new JTextFieldOperator(tabPane, benutzer);
		JPasswordFieldOperator tfPasswort = new JPasswordFieldOperator(tabPane, "");
		tfBenutzer.setText("");
		
//		new JButtonOperator(tabPane, "Anmelden").push();
//		new JDialogOperator(mainFrame, "Warnung").close();
//		tfBenutzer.setText(benutzer);

//		new JButtonOperator(tabPane, "Anmelden").push();
//		new JDialogOperator(mainFrame, "Warnung").close();
//		tfBenutzer.setText("");
		tfPasswort.setText(benutzer);

//		new JButtonOperator(tabPane, "Anmelden").push();
//		new JDialogOperator(mainFrame, "Warnung").close();
		tfBenutzer.setText(benutzer);
		
		new JButtonOperator(tabPane, "Anmelden").push();
	}
	
	/**
	 * Testen vom Infofenster. 
	 */
	public void testInfoFenster() {
		// Frame finden
		JFrameOperator mainFrame = new JFrameOperator("Mittelverwaltungsprogramm");
		
		// Menü "Info->Anzigen" frücken
	    new JMenuBarOperator(mainFrame).pushMenu("Info|Anzeigen", "|");
	    JInternalFrameOperator infoFrame = new JInternalFrameOperator(mainFrame, "Information");
	    
	    (new JButtonOperator(infoFrame, "Schließen")).push();
	}
	
	/**
	 * Testen der Institutsverwaltung. 
	 */
	public void testInstitut() {
		// Frame finden
		JFrameOperator mainFrame = new JFrameOperator("Mittelverwaltungsprogramm");
		
		// Menü "Info->Anzigen" frücken
	    new JMenuBarOperator(mainFrame).pushMenu("Verwaltung|Institute", "|");
	    JInternalFrameOperator instFrame = new JInternalFrameOperator(mainFrame, "Institutsverwaltung");
	    
	    // ButtonsFinden
		JButtonOperator butAnlegen = new JButtonOperator(instFrame, "Anlegen");
		JButtonOperator butAendern = new JButtonOperator(instFrame, "Ändern");
		JButtonOperator butLoeschen = new JButtonOperator(instFrame, "Löschen");
		JButtonOperator butBeenden = new JButtonOperator(instFrame, "Beenden");
		
		JTextFieldOperator tfBezeichnung = new JTextFieldOperator(instFrame, "FBI allgemein");
		JTextFieldOperator tfKostenstelle = new JTextFieldOperator(instFrame, "160000");
		JComboBoxOperator cbLeiter = new JComboBoxOperator(instFrame);
		
		// Gleiches Institut versuchen anzulegen
		butAnlegen.push();
		new JDialogOperator(mainFrame, "Warnung").close();
		
		// Neues Insitut anlegen
		tfBezeichnung.setText("MM-Institut");
		tfKostenstelle.setText("123456");
		cbLeiter.selectItem(1);
		butAnlegen.push();

		// Das Insitut verändern
		tfBezeichnung.setText("MMI");
		tfKostenstelle.setText("123457");
		butAendern.push();

		// Das Institut wieder löschen
		butLoeschen.push();
		new JButtonOperator(new JDialogOperator(mainFrame, "Löschen"), "Ja").push();
		
		// Institutsverwaltung wieder schließen
		butBeenden.push();
		
	}
	
	/**
	 * Testen vom Benutzer. 
	 */
	public void testBenutzer() {
		// Frame finden
		JFrameOperator mainFrame = new JFrameOperator("Mittelverwaltungsprogramm");
		
		// Menü "Info->Anzigen" frücken
	    new JMenuBarOperator(mainFrame).pushMenu("Verwaltung|Benutzer", "|");
	    JInternalFrameOperator benFrame = new JInternalFrameOperator(mainFrame, "Benutzerverwaltung");
		
	    // ButtonsFinden
		JButtonOperator butAnlegen = new JButtonOperator(benFrame, "Anlegen");
		JButtonOperator butAendern = new JButtonOperator(benFrame, "Ändern");
		JButtonOperator butLoeschen = new JButtonOperator(benFrame, "Löschen");
		JButtonOperator butBeenden = new JButtonOperator(benFrame, "Beenden");
		JTextFieldOperator tfName = new JTextFieldOperator(benFrame, 8);
		JTextFieldOperator tfVorname = new JTextFieldOperator(benFrame, 1);
		JTextFieldOperator tfBenutzername = new JTextFieldOperator(benFrame, 6);
		JTextFieldOperator tfPasswort1 = new JTextFieldOperator(benFrame, 7);
		JTextFieldOperator tfPasswort2 = new JTextFieldOperator(benFrame, 0);

		// Gleichen Benutzer anlegen
		butAnlegen.push();
		new JDialogOperator(mainFrame, "Fehler").close();
		
		// Neuen Benutzer anlegen
		tfBenutzername.setText("t.test");
		tfName.setText("Test");
		tfVorname.setText("Test");
		tfPasswort1.setText("123");
		tfPasswort2.setText("321");
		butAnlegen.push();
		new JDialogOperator(mainFrame, "Fehler").close();
		tfPasswort1.setText("Test");
		tfPasswort2.setText("Test");
		butAnlegen.push();
		
		// Benutzer aktualisieren
		tfVorname.setText("User");
		butAendern.push();
		
		// Benutzer löschen
		butLoeschen.push();
		new JButtonOperator(new JDialogOperator(mainFrame, "Löschen"), "Ja").push();
		
		// Benutzerverwaltung schließen
		butBeenden.push();
	}
	
	/**
	 * Testen von FBKonten. 
	 */
	public void testFBKonten() {
		// Frame finden
		JFrameOperator mainFrame = new JFrameOperator("Mittelverwaltungsprogramm");
		
		// Menü "Info->Anzigen" frücken
	    new JMenuBarOperator(mainFrame).pushMenu("Verwaltung|Konten|Fachbereichskonten", "|");
	    JInternalFrameOperator benFrame = new JInternalFrameOperator(mainFrame, "FB-Kontenverwaltung");
		
	    // ButtonsFinden
		JButtonOperator butAnlegen = new JButtonOperator(benFrame, "Anlegen");
		JButtonOperator butAendern = new JButtonOperator(benFrame, "Ändern");
		JButtonOperator butLoeschen = new JButtonOperator(benFrame, "Löschen");
		JButtonOperator butBeenden = new JButtonOperator(benFrame, "Beenden");

		// Einen Institut anlegen
		butAnlegen.push();
		
		// Tree-Operator holen
		JTreeOperator tree = new JTreeOperator(mainFrame);
	    tree.clickMouse();
	    tree.expandRow(0);
	    tree.selectRow(0);
	    tree.expandRow(1);
	    tree.selectRow(1);
	    
		JTextFieldOperator tfBezeichnung = new JTextFieldOperator(benFrame, 0);
		JTextFieldOperator tfHaupt = new JTextFieldOperator(benFrame, 2);
		JTextFieldOperator tfUnter = new JTextFieldOperator(benFrame, 3);
	    
	    // Gleiches FBKonto anlegen
	    butAnlegen.push();
	    new JButtonOperator(new JDialogOperator(mainFrame, "Anlegen ?"), "Ja").push();
	    new JDialogOperator(mainFrame, "Fehler !").close();
	    
	    // Neues FBHauptkonto anlegen mit Unterkonto != "0000"
		tfBezeichnung.setText("FBI 2");
		tfHaupt.setText("01");
		tfUnter.setText("0001");
	    butAnlegen.push();
	    new JButtonOperator(new JDialogOperator(mainFrame, "Anlegen ?"), "Ja").push();
	    new JDialogOperator(mainFrame, "Fehler !").close();
	    
	    // Neues FBHauptkonto erfolgreich anlegen
		tfUnter.setText("0000");
	    butAnlegen.push();
	    new JButtonOperator(new JDialogOperator(mainFrame, "Anlegen ?"), "Ja").push();
	    
	    // Unterkonto zu dem Hauptkonto anlegen mit Unterkonto == 0000.
	    tree.expandRow(3);
	    tree.selectRow(3);
		tfBezeichnung.setText("FBI 2 Unter");
		tfHaupt.setText("01");
		tfUnter.setText("0000");
	    butAnlegen.push();
	    new JButtonOperator(new JDialogOperator(mainFrame, "Anlegen ?"), "Ja").push();
	    new JDialogOperator(mainFrame, "Fehler !").close();
	    
	    // Neues FBUnterkonto erfolgreich anlegen
		tfUnter.setText("0001");
	    butAnlegen.push();
	    new JButtonOperator(new JDialogOperator(mainFrame, "Anlegen ?"), "Ja").push();
	    
	    // Hauptkonto aktualisieren.
	    tree.selectRow(3);
		tfBezeichnung.setText("FBI 2 Akt.");
		tfHaupt.setText("02");
		tfUnter.setText("0000");
	    butAendern.push();
	    new JButtonOperator(new JDialogOperator(mainFrame, "Ändern ?"), "Ja").push();
	    
	    // Überprüfen ob auch das FBUnterkonto geändert wurde
	    tree.selectRow(4);
	    new JTextFieldOperator(benFrame, "02");
	    
	    // Das Hauptkonto löschen
	    tree.selectRow(3);
	    butLoeschen.push();
	    new JButtonOperator(new JDialogOperator(mainFrame, "Löschen ?"), "Ja").push();
		
		// FB-Kontenverwaltung schließen
		butBeenden.push();
	}
	
	/**
	 * Dn GUI-Test erstellen. 
	 * @return Die erstellte TestSuite. 
	 */
    public static Test suite() {
    	//start the application first
    	try {
    		new ClassReference("gui.Start").startApplication();

            // increase timeouts values to see what's going on
            // otherwise everything's happened very fast
            JemmyProperties.getCurrentTimeouts().loadDebugTimeouts();	
    	} catch(Exception e) {
    	    e.printStackTrace();
    	}

    	// create suite
    	TestSuite suite = new TestSuite();
    	suite.addTest(new TestClientGUI("testLogin"));
    	
    	suite.addTest(new TestClientGUI("testInstitut"));
    	suite.addTest(new TestClientGUI("testBenutzer"));
    	suite.addTest(new TestClientGUI("testFBKonten"));
      	
    	suite.addTest(new TestClientGUI("testInfoFenster"));

    	return(suite);
    }
}
