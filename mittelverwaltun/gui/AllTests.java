package gui;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author robert
 *
 * Folgendes ausw�hlen, um die Schablone f�r den erstellten Typenkommentar zu �ndern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for gui");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(BenutzerverwaltungTest.class));
		suite.addTest(new TestSuite(HaushaltsjahrAendernTest.class));
		//$JUnit-END$
		return suite;
	}
}
