package gui;

import java.rmi.Naming;

import dbObjects.Institut;

import applicationServer.ApplicationServer;
import applicationServer.CentralServer;
import junit.framework.TestCase;

/**
 * @author robert
 *
 * Folgendes ausw�hlen, um die Schablone f�r den erstellten Typenkommentar zu �ndern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class InstitutverwaltungTest extends TestCase {
	Institutverwaltung inst;
	
	public InstitutverwaltungTest(String arg0) {
		super(arg0);
		try{
			CentralServer server = (CentralServer)Naming.lookup("//localhost/mittelverwaltung");
			ApplicationServer applicationServer = server.getMyApplicationServer();
			PasswordEncrypt pe = new PasswordEncrypt();
			String psw = pe.encrypt(new String("r.driesner").toString());
			applicationServer.login("r.driesner", psw);
			this.inst = new Institutverwaltung(applicationServer);
		}catch(Exception e){
				System.out.println(e);
		}	
	}
	
	
	
	public void testInstitutverwaltung(){
		//selektieren der Institute
		Institut currInstitut = (Institut)inst.listModel.getElementAt(2);
		inst.listInstitute.setSelectedIndex(2);
		assertEquals(currInstitut.getBezeichnung(), inst.tfBezeichnung.getText());
		assertEquals(currInstitut.getKostenstelle(), inst.tfKostenstelle.getText());
		
		//leere Felder einf�gen
		inst.tfBezeichnung.setText("");
		inst.tfKostenstelle.setText("");
		assertEquals("Bezeichnung und Kostenstelle m�ssen ausgef�llt sein.", inst.addInstitut());
		
		//Testdaten einf�gen
		inst.tfBezeichnung.setText("Test-Institut");
		inst.tfKostenstelle.setText("160002");
		assertEquals("", inst.addInstitut());
		currInstitut = (Institut)inst.listModel.getElementAt(inst.listInstitute.getSelectedIndex());
		assertEquals("Test-Institut", currInstitut.getBezeichnung());
		assertEquals("160002", currInstitut.getKostenstelle());
		
		//doppelte Daten einf�gen
		inst.tfBezeichnung.setText("Test-Institut");
		inst.tfKostenstelle.setText("160002");
		assertEquals("Application Server: Bezeichnung oder Kostenstelle existiert schon.", inst.addInstitut());
		
		//ver�ndern der Daten
		inst.tfBezeichnung.setText("Test-Institut2");
		inst.tfKostenstelle.setText("160004");
		assertEquals("", inst.changeInstitut());
		currInstitut = (Institut)inst.listModel.getElementAt(inst.listInstitute.getSelectedIndex());
		assertEquals("Test-Institut2", currInstitut.getBezeichnung());
		assertEquals("160004", currInstitut.getKostenstelle());
		
		//ver�ndern der Daten indem doppelte Kostenstelle entsteht
		inst.tfBezeichnung.setText("Test-Institut2");
		inst.tfKostenstelle.setText("160000");
		assertEquals("Application Server: Bezeichnung oder Kostenstelle existiert schon.", inst.changeInstitut());
		
		//l�schen
		assertEquals("" ,inst.delInstitut());
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
