package gui;


import java.rmi.Naming;

import dbObjects.Benutzer;

import applicationServer.ApplicationServer;
import applicationServer.CentralServer;
import junit.framework.TestCase;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class BenutzerverwaltungTest extends TestCase {
	Benutzerverwaltung bverw;
	
	public BenutzerverwaltungTest(String arg0) {
		super(arg0);
		try{
			CentralServer server = (CentralServer)Naming.lookup("//localhost/mittelverwaltung");
//			ApplicationServer applicationServer = server.getMyApplicationServer();
//			PasswordEncrypt pe = new PasswordEncrypt();
//			String psw = pe.encrypt(new String("r.driesner").toString());
//			applicationServer.login("r.driesner", psw);
//			this.bverw = new Benutzerverwaltung(applicationServer);
		}catch(Exception e){
				System.out.println(e);
		}	
	}

	public void testAddUser() {
		bverw.tfBenutzername.setText("m.schmitt");
		bverw.tfName.setText("Testuser");
		bverw.tfVorname.setText("Heinrich");
		bverw.tfPasswort.setText("Test");
		bverw.tfPasswortWdhl.setText("Test");
		bverw.tfEMail.setText("testuser@web.de");
		bverw.cbRollen.setSelectedIndex(bverw.cbRollen.getItemCount() - 1);
		bverw.cbInstitut.setSelectedIndex(bverw.cbInstitut.getItemCount() - 1);
//		assertEquals("Application Server: Benutzername existiert schon.", bverw.addUser());
		
		bverw.tfBenutzername.setText("h.testuser");
//		assertEquals("", bverw.addUser());
		
		Benutzer benutzer = (Benutzer)bverw.listModel.getElementAt(bverw.listBenutzer.getSelectedIndex());
		for(int i = 0; i < bverw.listModel.getSize(); i++){
			if(((Benutzer)bverw.listModel.getElementAt(i)).getId() == benutzer.getId()){
				assertTrue(benutzer.equals((Benutzer)bverw.listModel.getElementAt(i)));
				break;
			}
		}	
	}

	public void testChangeUser() {
		//leere Felder Testen
		bverw.tfBenutzername.setText("");
//		assertEquals("Benutzername, Name und Vorname müssen ausgefüllt sein.", bverw.changeUser());
		bverw.buRefresh.doClick();
		
		bverw.tfName.setText("");
//		assertEquals("Benutzername, Name und Vorname müssen ausgefüllt sein.", bverw.changeUser());
		bverw.buRefresh.doClick();
		
		bverw.tfVorname.setText("");
//		assertEquals("Benutzername, Name und Vorname müssen ausgefüllt sein.", bverw.changeUser());
		bverw.buRefresh.doClick();
		
		//gleiche Benutzername nicht erlaubt
		bverw.tfBenutzername.setText("m.schmitt");
//		assertEquals("Application Server: Benutzername existiert schon.", bverw.changeUser());
		bverw.buRefresh.doClick();
		
		//Passwort ändern
		bverw.tfPasswort.setText("TestPasswort");
		bverw.tfPasswortWdhl.setText("TestPasswort2");
//		assertEquals("Passwort und Passwort-Wiederholung sind nicht gleich.", bverw.changeUser());
		
		//Daten ändern
		if(!bverw.listBenutzer.isSelectionEmpty()){
			Benutzer oldBenutzer = (Benutzer)bverw.listModel.getElementAt(bverw.listBenutzer.getSelectedIndex());
			bverw.tfBenutzername.setText(oldBenutzer.getBenutzername() + "Test");
			bverw.tfName.setText(oldBenutzer.getName() + "Test");
			bverw.tfVorname.setText(oldBenutzer.getVorname() + "Test");
			bverw.tfPasswort.setText("");
			bverw.tfPasswortWdhl.setText("");
//			assertEquals("", bverw.changeUser());
			
			Benutzer editBenutzer = (Benutzer)bverw.listModel.getElementAt(bverw.listBenutzer.getSelectedIndex());
			assertEquals(oldBenutzer.getBenutzername() + "Test", editBenutzer.getBenutzername());
			assertEquals(oldBenutzer.getName() + "Test", editBenutzer.getName());
			assertEquals(oldBenutzer.getVorname() + "Test", editBenutzer.getVorname());
			bverw.buRefresh.doClick();
			
			for(int i = 0; i < bverw.listModel.getSize(); i++){
				if(((Benutzer)bverw.listModel.getElementAt(i)).getId() == editBenutzer.getId()){
					assertTrue(editBenutzer.equals((Benutzer)bverw.listModel.getElementAt(i)));
					break;
				}
			}
			
			bverw.tfBenutzername.setText(oldBenutzer.getBenutzername());
			bverw.tfName.setText(oldBenutzer.getName());
			bverw.tfVorname.setText(oldBenutzer.getVorname());
//			assertEquals("", bverw.changeUser());
		}
	}
	
	public void testDelUser() {
		bverw.buRefresh.doClick();
		for(int i = 0; i < bverw.listModel.getSize(); i++){
			bverw.listBenutzer.setSelectedIndex(i);
			if(bverw.tfBenutzername.getText().equals("h.testuser")){
//				assertEquals("", bverw.delUser());
				break;
			}
		}
	}

}
