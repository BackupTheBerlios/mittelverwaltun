package gui;

import java.rmi.Naming;

import applicationServer.CentralServer;
import junit.framework.TestCase;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class HaushaltsjahrAendernTest extends TestCase {

	HaushaltsjahrAendern hhj;
	
	public HaushaltsjahrAendernTest(String arg0) {
		super(arg0);
		try{
			CentralServer server = (CentralServer)Naming.lookup("//localhost/mittelverwaltung");
//			ApplicationServer applicationServer = server.getMyApplicationServer();
//			PasswordEncrypt pe = new PasswordEncrypt();
//			String psw = pe.encrypt(new String("r.driesner").toString());
//			applicationServer.login("r.driesner", psw);
//			this.hhj = new HaushaltsjahrAendern(applicationServer);
		}catch(Exception e){
				System.out.println(e);
		}	
	}

	public void testChangeHaushaltsjahr() {
		//leere Felder Test
		hhj.tfJahr.setText("");
		assertEquals("Jahr, Von, Bis müssen ausgefüllt werden.",hhj.changeHaushaltsjahr());
		hhj.loadHaushaltsjahr();
		
		
		//Datumsformat testen
		hhj.tfBis.setText("01.01.200456");
		assertEquals("Es wird nur dd.mm.yyyy - Datumsformat akzeptiert.",hhj.changeHaushaltsjahr());
		hhj.loadHaushaltsjahr();
		
		hhj.tfVon.setText("15.05.2005");
		hhj.tfBis.setText("02.02.2004");
		assertEquals("Von-Datum muss vor dem Bis-Datum kommen.",hhj.changeHaushaltsjahr());
		hhj.loadHaushaltsjahr();
		
		assertEquals("",hhj.changeHaushaltsjahr());
	}

}
