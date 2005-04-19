/* Created on 18.04.2005 */

package applicationServer.test;

import javax.swing.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.netbeans.jemmy.*;
import org.netbeans.jemmy.operators.*;

public class TestServerGUI extends TestCase {
	

    public TestServerGUI(String name) {
    	super(name);
    }
    
	/**
	 * Testen des Reiters "Server"
	 */
	public void testTabServer() {
		String driver = "com.mysql.jdbc.Driver";
		String url = "mysql://localhost";
		String dbname = "fbmittelverwaltung";
		String passwort = "mittelverwaltung";
		String servername = "mittelverwaltung";
		String path = "C:\\Programme\\Java\\jdk1.5.0\\jre\\bin\\rmiregistry.exe";

		// Frame finden
		JFrameOperator mainFrame = new JFrameOperator("FB-Mittelverwaltung Server");
		// TabbedPane finden
		JTabbedPaneOperator tabPane = new JTabbedPaneOperator(mainFrame);
		
		// Den dritten Reiter aswählen
		tabPane.setSelectedIndex(2);
		JTextFieldOperator tfDriver = new JTextFieldOperator(tabPane, driver);
		JTextFieldOperator tfURL = new JTextFieldOperator(tabPane, url);
		JTextFieldOperator tfDBName = new JTextFieldOperator(tabPane, dbname);
		JTextFieldOperator tfPasswort1 = new JPasswordFieldOperator(tabPane, passwort);
		tfPasswort1.setText("");
		JTextFieldOperator tfPasswort2 = new JPasswordFieldOperator(tabPane, passwort);
		tfPasswort1.setText(passwort);
		JTextFieldOperator tfServername = new JTextFieldOperator(tabPane, servername);
		JTextFieldOperator tfPath = new JTextFieldOperator(tabPane, path);
		
		tfDriver.setText("");
		new JButtonOperator(tabPane, "Einstellungen Speichern").push();
		new JDialogOperator(mainFrame, "Fehler !").close();
		tfDriver.setText(driver);

		tfURL.setText("");
		new JButtonOperator(tabPane, "Einstellungen Speichern").push();
		new JDialogOperator(mainFrame, "Fehler !").close();
		tfURL.setText(url);

		tfDBName.setText("");
		new JButtonOperator(tabPane, "Einstellungen Speichern").push();
		new JDialogOperator(mainFrame, "Fehler !").close();
		tfDBName.setText(dbname);

		tfPasswort1.setText("");
		new JButtonOperator(tabPane, "Einstellungen Speichern").push();
		new JDialogOperator(mainFrame, "Fehler !").close();
		tfPasswort1.setText(passwort);

		tfPasswort2.setText("");
		new JButtonOperator(tabPane, "Einstellungen Speichern").push();
		new JDialogOperator(mainFrame, "Fehler !").close();
		tfPasswort2.setText(passwort);
		
		tfPasswort1.setText("ghg");
		tfPasswort2.setText("abc");
		new JButtonOperator(tabPane, "Einstellungen Speichern").push();
		new JDialogOperator(mainFrame, "Fehler !").close();
		tfPasswort1.setText(passwort);
		tfPasswort2.setText(passwort);

		tfServername.setText("");
		new JButtonOperator(tabPane, "Einstellungen Speichern").push();
		new JDialogOperator(mainFrame, "Fehler !").close();
		tfServername.setText(servername);

		tfPath.setText("");
		new JButtonOperator(tabPane, "Einstellungen Speichern").push();
		new JDialogOperator(mainFrame, "Fehler !").close();
		tfPath.setText(path);
		
		new JButtonOperator(tabPane, "Einstellungen Speichern").push();

		// Den ersten Reiter auswählen
		tabPane.setSelectedIndex(0);
		JButtonOperator reg = new JButtonOperator(tabPane, "RMI-Registry Starten");
		reg.push();
		JButtonOperator ser = new JButtonOperator(tabPane, "Central-Server Starten");
		ser.push();

		new JButtonOperator(tabPane, ((JButton)ser.getSource()).getText()).push();
		new JButtonOperator(tabPane, ((JButton)reg.getSource()).getText()).push();
	}

    public static Test suite() {
    	//start the application first
    	try {
    		new ClassReference("applicationServer.Server").startApplication();

            // increase timeouts values to see what's going on
            // otherwise everything's happened very fast
            JemmyProperties.getCurrentTimeouts().loadDebugTimeouts();	
    	} catch(Exception e) {
    	    e.printStackTrace();
    	}

    	// create suite
    	TestSuite suite = new TestSuite();
    	suite.addTest(new TestServerGUI("testTabServer"));

    	return(suite);
    }

}