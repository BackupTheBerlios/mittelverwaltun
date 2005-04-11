package gui;

import applicationServer.*;

import java.rmi.Naming;
import java.sql.Date;
import java.text.DateFormat;

import javax.swing.*;

import dbObjects.Benutzer;
import dbObjects.TmpRolle;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TempRollenFrame extends JInternalFrame implements ActionListener, ListSelectionListener{

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
  JButton buRefresh = new JButton(Functions.getRefreshIcon(getClass()));
  MainFrame frame;
  JButton buClose = new JButton(Functions.getCloseIcon(getClass()));
  TmpRolle[] tmpRollen;

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
		buRefresh.addActionListener( this );
		buClose.addActionListener( this );
		tfGueltigBisNeu.setValue(new Date(System.currentTimeMillis()));
		
		listBenutzer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listBenutzer.addListSelectionListener(this);
		
		loadTempRolleBenutzer();
		loadUsers();

		this.setBounds(0,0,445, 340);
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));

  }

  /**
   * lädt alle TempRollen mit Benutzern, die die Rolle von dem Besitzer erhalten haben
   */
  private void loadTempRolleBenutzer(){
		try{
			tmpRollen = applicationServer.getTempRolleUsers(frame.getBenutzer().getId());

			liMoBenutzer.clear();
			if(tmpRollen != null){
				for(int i = 0; i < tmpRollen.length; i++){
					liMoBenutzer.addElement(tmpRollen[i]);
			  }
			  listBenutzer.setSelectedIndex(0);
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}


	private void addTempRollenBenutzer()  throws ApplicationServerException{
		if(tfGueltigBisNeu.getValue() == null){
			throw new ApplicationServerException(1, "Das Datum muss ausgefüllt sein.");
		}else{
			java.util.Date bisUtil = (java.util.Date)tfGueltigBisNeu.getValue();
			Date bis = new Date(bisUtil.getTime());
			
			if(!bis.after(new Date(System.currentTimeMillis())))
				throw new ApplicationServerException(0, "Das Datum muss in der Zukunft liegen.");

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
		  Benutzer[] users = frame.getApplicationServer().getUsers();
  		
		  if(users != null){
			  cbBenutzer.removeAllItems();
				for(int i = 0; i < users.length; i++){
				 	if(!frame.getBenutzer().equals(users[i])){
				 		boolean consistsUser = false;
				 		for(int j = 0; j < liMoBenutzer.getSize(); j++){
				 			Benutzer user = ((TmpRolle)liMoBenutzer.getElementAt(j)).getEmpfaenger();
				 			if(user.equals(users[i]))
				 				consistsUser = true;
				 		}
				 		
				 		if(!consistsUser)
				 			cbBenutzer.addItem(users[i]);
				 	}
				 		
				}
				cbBenutzer.setSelectedItem(frame.getBenutzer());
		  }
		}catch(Exception e){
			 System.out.println(e);
		}
	}

	private void setTempRollenBenutzer() throws ApplicationServerException{
		if(!listBenutzer.isSelectionEmpty()){

			TmpRolle currTmpRolle = (TmpRolle)liMoBenutzer.getElementAt(listBenutzer.getSelectedIndex());
			java.util.Date bisUtil = (java.util.Date)tfGueltigBis.getValue();
			Date bis = new Date(bisUtil.getTime());
			
			if(!bis.after(new Date(System.currentTimeMillis())))
				throw new ApplicationServerException(0, "Das Datum muss in der Zukunft liegen.");

			currTmpRolle.setGueltigBis(bis);

			applicationServer.setTempRolle(currTmpRolle);
			liMoBenutzer.setElementAt(currTmpRolle, listBenutzer.getSelectedIndex());

		}else
			throw new ApplicationServerException(0, "Es ist keine Rolle selektiert !");
	}

	private void delTempRollenRolle() throws ApplicationServerException{
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
		loadUsers();
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
    jScrollPane3.setBounds(new Rectangle(7, 23, 274, 177));
    jLabel1.setText("Gültig bis:");
    jLabel1.setBounds(new Rectangle(291, 23, 97, 15));
    tfGueltigBis.setBounds(new Rectangle(291, 41, 122, 21));
    buDelTempRolle.setBounds(new Rectangle(291, 105, 122, 25));
    buDelTempRolle.setText("Löschen");
    buSaveTempRolle.setBounds(new Rectangle(291, 70, 122, 25));
    buSaveTempRolle.setText("Ändern");
    buAddTempRolle.setBounds(new Rectangle(301, 51, 111, 25));
    buAddTempRolle.setText("Einfügen");
    cbBenutzer.setBounds(new Rectangle(8, 22, 404, 25));
    jPanel1.setBorder(titledBorder1);
    jPanel1.setBounds(new Rectangle(3, 8, 427, 84));
    jPanel1.setLayout(null);
    jLabel2.setBounds(new Rectangle(8, 56, 78, 15));
    jLabel2.setText("Gültig bis:");
    tfGueltigBisNeu.setBounds(new Rectangle(98, 53, 111, 21));
    jPanel2.setBorder(titledBorder2);
    jPanel2.setBounds(new Rectangle(5, 96, 427, 209));
    jPanel2.setLayout(null);
    buRefresh.setText("Refresh");
    buRefresh.setBounds(new Rectangle(291, 140, 122, 25));
    buClose.setBounds(new Rectangle(291, 175, 122, 25));
    buClose.setText("Beenden");
    jPanel2.add(jLabel1, null);
    jPanel2.add(tfGueltigBis, null);
    jPanel2.add(buSaveTempRolle, null);
    jPanel2.add(buDelTempRolle, null);
    jPanel2.add(buRefresh, null);
    jPanel2.add(jScrollPane3, null);
    jPanel2.add(buClose, null);
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
  	MainFrame test = new MainFrame("FBMittelverwaltung");
	 	try{
		 	CentralServer server = (CentralServer)Naming.lookup("//192.168.1.1/mittelverwaltung");
		 	ApplicationServer applicationServer = server.getMyApplicationServer();
		 	test.setApplicationServer(applicationServer);
		 	PasswordEncrypt pe = new PasswordEncrypt();
		 	String psw = pe.encrypt(new String("r.driesner").toString());
		 	test.setBenutzer(applicationServer.login("r.driesner", psw));
	   	test.setBounds(100,100,800,900);
		 	test.setExtendedState(Frame.MAXIMIZED_BOTH);

		 	test.setJMenuBar( new MainMenu( test ) );
		 	TempRollenFrame rollenVerwaltung = new TempRollenFrame(test);
		 	test.addChild(rollenVerwaltung);
		 	test.setVisible(true);
		 	rollenVerwaltung.show();
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
			}else if( e.getSource() == buRefresh ) {
				loadTempRolleBenutzer();
				loadUsers();
			}else if( e.getSource() == buClose ) {
				dispose();
			}
		} catch (ApplicationServerException e1) {
			MessageDialogs.showDetailMessageDialog(this, "Fehler", e1.getMessage(), e1.getNestedMessage(), MessageDialogs.ERROR_ICON);
		}
	}

	public void valueChanged(ListSelectionEvent arg0) {
		if(!listBenutzer.isSelectionEmpty()){
			TmpRolle rolle = (TmpRolle)liMoBenutzer.getElementAt(listBenutzer.getSelectedIndex());
			tfGueltigBis.setValue(rolle.getGueltigBis());
		}
	}
}