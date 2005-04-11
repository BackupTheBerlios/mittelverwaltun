package gui;

import applicationServer.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.DateFormat;

import javax.swing.*;

import dbObjects.Benutzer;
import dbObjects.TmpRolle;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.*;

public class TempRollenFrame extends JInternalFrame implements ActionListener{

  ApplicationServer applicationServer;
  JScrollPane jScrollPane3 = new JScrollPane();
  DefaultListModel liMoBenutzer = new DefaultListModel();
  JList listBenutzer = new JList(liMoBenutzer);
  JLabel jLabel1 = new JLabel();
  JFormattedTextField tfGueltigBis = new JFormattedTextField(DateFormat.getDateInstance());
  JButton buDelTempRolle = new JButton(Functions.getDelIcon(getClass()));
  JButton buSaveTempRolle = new JButton(Functions.getEditIcon(getClass()));
  JButton buAddTempRolle = new JButton(Functions.getAddIcon(getClass()));
  JComboBox cbBenutzer = new JComboBox();
  JPanel jPanel1 = new JPanel();
  TitledBorder titledBorder1;
  JLabel jLabel2 = new JLabel();
  JFormattedTextField tfGueltigBisNeu = new JFormattedTextField(DateFormat.getDateInstance());
  JPanel jPanel2 = new JPanel();
  TitledBorder titledBorder2;
  JButton buRefresh = new JButton();
  MainFrame frame;


  public TempRollenFrame(MainFrame frame) {
		super( "TempRollenverwaltung" );
		this.frame = frame;
		this.applicationServer = frame.getApplicationServer();
		this.setClosable(true);
		this.setIconifiable(true);

    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
		buAddTempRolle.addActionListener( this );
		buDelTempRolle.addActionListener( this );
		buSaveTempRolle.addActionListener( this );

		loadUsers();
		loadTempRolleBenutzer();

		this.setBounds(0,0,500, 500);
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));

  }

  /**
   * lädt alle TempRollen mit Benutzern, die die Rolle von dem Besitzer erhalten haben
   */
  private void loadTempRolleBenutzer(){
		try{
			TmpRolle[] tmpRollen = applicationServer.getTempRolleUsers(frame.getBenutzer().getId());

			liMoBenutzer.clear();
			for(int i = 0; i < tmpRollen.length; i++){
				liMoBenutzer.addElement(tmpRollen[i]);
		  }
		  listBenutzer.setSelectedIndex(0);
		}catch(Exception e){
			System.out.println(e);
		}
	}


	private void addTempRollenBenutzer()  throws ApplicationServerException{
		if(tfGueltigBis.getText().equals("")){
			throw new ApplicationServerException(1, "Das Datum muss ausgefüllt sein.");
		}else{
			java.util.Date bisUtil = (java.util.Date)tfGueltigBisNeu.getValue();
			Date bis = new Date(bisUtil.getTime());

			TmpRolle tmpRolle = new TmpRolle(0, frame.getBenutzer().getId(), (Benutzer)cbBenutzer.getSelectedItem(),
																				bis);
			applicationServer.addTempRolle(tmpRolle);
			liMoBenutzer.addElement(tmpRolle);
			listBenutzer.setSelectedIndex(liMoBenutzer.getSize() - 1);
			listBenutzer.ensureIndexIsVisible(liMoBenutzer.getSize() - 1);
		}
	}

	/**
	 * lädt User für die Auswahl des Auftraggebers und Empfängers. Falls der User ein Institutssdmin ist,
	 * werden nur die Benutzer des jeweiligen Instituts geladen. Ist der User ein Admin werden alle User
	 * geladen.
	 */
	private void loadUsers(){
		try{
			Benutzer[] users = null;
			//		TODO Admin durch die Aktivität austauschen
		  if(frame.getBenutzer().getRolle().getBezeichnung().equals("Admin"))
				users = frame.getApplicationServer().getUsers();
			else
				users = frame.getApplicationServer().getUsers(frame.getBenutzer().getKostenstelle());

			  if(users != null){
				  cbBenutzer.removeAllItems();
					 for(int i = 0; i < users.length; i++){
					 	cbBenutzer.addItem(users[i]);
					 }
					 cbBenutzer.setSelectedItem(frame.getBenutzer());
			  }
		}catch(Exception e){
			 System.out.println(e);
		}
	}

	private void setTempRollenBenutzer() throws ApplicationServerException, RemoteException{
		if(!listBenutzer.isSelectionEmpty()){

			TmpRolle currTmpRolle = (TmpRolle)liMoBenutzer.getElementAt(listBenutzer.getSelectedIndex());
			currTmpRolle.setGueltigBis((Date)tfGueltigBis.getValue());

			applicationServer.setTempRolle(currTmpRolle);
			liMoBenutzer.setElementAt(currTmpRolle, listBenutzer.getSelectedIndex());

		}else
			throw new ApplicationServerException(0, "Es ist keine Rolle selektiert !");
	}

	private void delTempRollenRolle() throws ApplicationServerException, RemoteException{
		TmpRolle currRolle = (TmpRolle)liMoBenutzer.getElementAt(listBenutzer.getSelectedIndex());
		applicationServer.delTempRolle(currRolle);

		int index = listBenutzer.getSelectedIndex();
		liMoBenutzer.remove(listBenutzer.getSelectedIndex());

		if(index <= liMoBenutzer.getSize()){
			if(index == 0)
				listBenutzer.setSelectedIndex(index);
			else
				listBenutzer.setSelectedIndex(index-1);
		}
	}

	private void reload(){
		loadTempRolleBenutzer();
	}

  private void jbInit() throws Exception {
		titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(165, 163, 151)),"eigene Rolle einem Benutzer zuordnen");
    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(165, 163, 151)),"zugeordnete Benutzer");
    this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.getContentPane().setLayout(null);
    this.setTitle("TempRollenverwaltung");
    jScrollPane3.setBounds(new Rectangle(7, 23, 274, 142));
    jLabel1.setText("Gültig bis:");
    jLabel1.setBounds(new Rectangle(291, 23, 50, 15));
    tfGueltigBis.setBounds(new Rectangle(291, 41, 122, 21));
    buDelTempRolle.setBounds(new Rectangle(291, 105, 122, 25));
    buDelTempRolle.setText("Löschen");
    buSaveTempRolle.setBounds(new Rectangle(291, 70, 122, 25));
    buSaveTempRolle.setText("Speichern");
    buAddTempRolle.setBounds(new Rectangle(301, 51, 111, 25));
    buAddTempRolle.setText("Einfügen");
    cbBenutzer.setBounds(new Rectangle(8, 22, 404, 25));
    jPanel1.setBorder(titledBorder1);
    jPanel1.setBounds(new Rectangle(3, 8, 427, 84));
    jPanel1.setLayout(null);
    jLabel2.setBounds(new Rectangle(8, 56, 50, 15));
    jLabel2.setText("Gültig bis:");
    tfGueltigBisNeu.setBounds(new Rectangle(64, 53, 111, 21));
    jPanel2.setBorder(titledBorder2);
    jPanel2.setBounds(new Rectangle(5, 96, 427, 174));
    jPanel2.setLayout(null);
    buRefresh.setText("Aktualisieren");
    buRefresh.setBounds(new Rectangle(291, 140, 122, 25));
    jPanel2.add(jLabel1, null);
    jPanel2.add(tfGueltigBis, null);
    jPanel2.add(buSaveTempRolle, null);
    jPanel2.add(buDelTempRolle, null);
    jPanel2.add(buRefresh, null);
    jPanel2.add(jScrollPane3, null);
    jScrollPane3.getViewport().add(listBenutzer, null);
    jPanel2.add(jScrollPane3, null);
    this.getContentPane().add(jPanel1, null);
    jPanel1.add(cbBenutzer, null);
    jPanel1.add(jLabel2, null);
    jPanel1.add(buAddTempRolle, null);
    jPanel1.add(tfGueltigBisNeu, null);
    this.getContentPane().add(jPanel2, null);
  }

  public static void main(String[] args) {
	  JFrame test = new JFrame("Rollenverwaltung Test");
	  JDesktopPane desk = new JDesktopPane();
	  desk.setDesktopManager(new DefaultDesktopManager());
	  test.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	  test.setContentPane(desk);
	  test.setBounds(100,100,800,700);
	  try{
			  CentralServer server = (CentralServer)Naming.lookup("//localhost/mittelverwaltung");
//			  ApplicationServer applicationServer = server.getMyApplicationServer();
//			  PasswordEncrypt pe = new PasswordEncrypt();
//			  String psw = pe.encrypt(new String("r.driesner").toString());
//			  applicationServer.login("r.driesner", psw);
//				RollenAktivitaetenverwaltung rollenVerwaltung = new RollenAktivitaetenverwaltung(applicationServer);
//			  desk.add(rollenVerwaltung);
//			  test.show();
//				rollenVerwaltung.show();
	  }catch(Exception e){
					  System.out.println(e);
	  }
	}

	public void actionPerformed(ActionEvent e){
		try {
			if ( e.getSource() == buAddTempRolle ) {
				addTempRollenBenutzer();
			}else if( e.getSource() == buDelTempRolle ) {
				if(!liMoBenutzer.isEmpty() && !listBenutzer.isSelectionEmpty()){
					delTempRollenRolle();
				}else{
					JOptionPane.showMessageDialog(
						  this,
						  "Ein Benutzer muss selektiert sein !",
						  "Warnung",
						  JOptionPane.ERROR_MESSAGE);
				}
			}else if( e.getSource() == buSaveTempRolle ) {
				setTempRollenBenutzer();
			}
		} catch (ApplicationServerException e1) {
			JOptionPane.showMessageDialog(
							  this,
							  e1.getMessage(),
							  "Warnung",
							  JOptionPane.ERROR_MESSAGE);
		} catch (RemoteException exc){
		JOptionPane.showMessageDialog( this, exc.getMessage(),	"RemoteException !", JOptionPane.ERROR_MESSAGE );
	}
	}
}