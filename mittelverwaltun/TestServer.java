
import org.netbeans.jemmy.*;
import org.netbeans.jemmy.operators.*;
import javax.swing.*;


public class TestServer implements Scenario {

	/**
	 * Implementierte Scenario-Interface-Methode 
	 */
	public int runIt(Object arg0) {
		try {
			// Zu testende Applikation starten
			// Zu testende Applikation und der Jemmy-Test müssen im gleichen JVM laufen
			// Deswegen wird die zu testende Applikation im Jemmy-Test gestartet
			new ClassReference("applicationServer.Server").startApplication();
			
			// Frame, auf das gewartet wird
			// Wenn etwas mit einer Swing-Komponente machen möchte, muss man diese zuerst finden
			// Bei Frames wird zum Suchen der Frame-Titel verwendet
			JFrameOperator mainFrame = new JFrameOperator("Central Server");
			
			// Wenn man das Fenster gefunden hat, dann hat man den Ausgangspunkt um Komponente zu suchen
			// Button mit dem Text = "Start Registry" drücken 
			// Parameter : auf welchem Frame befindet sich der Button, und Text des Buttons
			JButtonOperator reg = new JButtonOperator(mainFrame, "Start Registry");
			reg.push();
			JButtonOperator ser = new JButtonOperator(mainFrame, "Server registrieren");
			ser.push();
			
			new JButtonOperator(mainFrame, "Benutzer entfernen").push();
			
			new JButtonOperator(mainFrame, ((JButton)ser.getSource()).getText()).push();
			new JButtonOperator(mainFrame, ((JButton)reg.getSource()).getText()).push();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return 0;
	}

	public static void main(String[] args) {
		String[] params = {"TestServer"};		// Name der Klasse, die das Scenario-Interface implementiert
		org.netbeans.jemmy.Test.main(params);	// Starten des Tests
	}
}
