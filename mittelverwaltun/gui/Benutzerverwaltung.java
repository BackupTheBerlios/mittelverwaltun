package gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import applicationServer.*;
import dbObjects.*;

import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Benutzerverwaltung extends JInternalFrame implements ActionListener, ListSelectionListener, FBKontoSelectable {

  ApplicationServer applicationServer;
  MainFrame frame = null;
  JLabel jLabel1 = new JLabel();
  JScrollPane jScrollPane1 = new JScrollPane();
  DefaultListModel listModel = new DefaultListModel();
  JList listBenutzer = new JList(listModel);
  JButton buAnlegen = new JButton();
  JButton buAendern = new JButton();
  JButton buLoeschen = new JButton();
  JButton buBeenden = new JButton();
  JButton buKontoAuswahl = new JButton();
  JLabel jLabel2 = new JLabel();
  JTextField tfBenutzername = new JTextField();
  JLabel jLabel3 = new JLabel();
  JPasswordField tfPasswort = new JPasswordField();
  JPasswordField tfPasswortWdhl = new JPasswordField();
  JTextField tfName = new JTextField();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel7 = new JLabel();
  JLabel jLabel8 = new JLabel();
  JLabel jLabel9 = new JLabel();
  JLabel jLabel10 = new JLabel();
  JTextField tfVorname = new JTextField();
  JTextField tfTitel = new JTextField();
  JTextField tfEMail = new JTextField();
  JTextField tfKonto = new JTextField();
  JComboBox cbInstitut = new JComboBox();
  JComboBox cbRollen = new JComboBox();
  JLabel jLabel11 = new JLabel();
  JButton buRefresh = new JButton();
  int privatKonto;
  JButton buKontoEntfernen = new JButton();
  JTextField tfTelefon = new JTextField();
  JTextField tfFax = new JTextField();
  JLabel jLabel12 = new JLabel();
  JLabel jLabel13 = new JLabel();
  JTextField tfBau = new JTextField();
  JTextField tfRaum = new JTextField();
  JLabel jLabel14 = new JLabel();
  JLabel jLabel15 = new JLabel();
  JCheckBox cbSoftBeauftragter = new JCheckBox();

  public Benutzerverwaltung(MainFrame frame){
		super( "Account ändern" );
		this.setClosable(true);
		this.setIconifiable(true);
		this.frame = frame;
		this.applicationServer = frame.getApplicationServer();

		try {
		  jbInit();
		}
		catch(Exception e) {
		  e.printStackTrace();
		}
		buRefresh.setBorder(null);
		buRefresh.setIcon(Functions.getRefreshIcon(getClass()));

		buAnlegen.setIcon(Functions.getAddIcon(getClass()));

		buAendern.setIcon(Functions.getEditIcon(getClass()));
		buAendern.addActionListener( this );

		buLoeschen.setIcon(Functions.getDelIcon(getClass()));

		buBeenden.setIcon(Functions.getCloseIcon(getClass()));
		buBeenden.addActionListener( this );

		buRefresh.setEnabled(false);
		buLoeschen.setEnabled(false);
		buAnlegen.setEnabled(false);
		cbInstitut.setEnabled(false);
		cbRollen.setEnabled(false);
		buKontoAuswahl.setEnabled(false);
		buKontoEntfernen.setEnabled(false);
		tfBenutzername.setEnabled(false);
		tfKonto.setEnabled(false);
		listBenutzer.addListSelectionListener(this);
		listBenutzer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		loadUsers();
		loadInstituts();
		loadRollen();

		this.setBounds(0,0,799, 400);
  }

  public Benutzerverwaltung(ApplicationServer applicationServer){
    super( "Benutzerverwaltung" );
    this.applicationServer = applicationServer;
    this.setClosable(true);
    this.setIconifiable(true);

    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

		tfKonto.setEnabled(false);

		buRefresh.setBorder(null);
    buRefresh.setIcon(Functions.getRefreshIcon(getClass()));
		buRefresh.addActionListener( this );

    buKontoAuswahl.addActionListener( this );
		buKontoEntfernen.addActionListener( this );

    buAnlegen.setIcon(Functions.getAddIcon(getClass()));
    buAnlegen.addActionListener( this );

    buAendern.setIcon(Functions.getEditIcon(getClass()));
    buAendern.addActionListener( this );

    buLoeschen.setIcon(Functions.getDelIcon(getClass()));
    buLoeschen.addActionListener( this );

    buBeenden.setIcon(Functions.getCloseIcon(getClass()));
    buBeenden.addActionListener( this );
    listBenutzer.addListSelectionListener(this);
    listBenutzer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		loadUsers();
		loadInstituts();
		loadRollen();
    this.setBounds(0,0,799, 400);
  }

  private void jbInit() throws Exception {
    jLabel1.setPreferredSize(new Dimension(100, 16));
    jLabel1.setText("Benutzername(*)");
    jLabel1.setBounds(new Rectangle(236, 29, 116, 15));
    this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.setTitle("Benutzerverwaltung");
    this.getContentPane().setLayout(null);
    jScrollPane1.setBounds(new Rectangle(6, 31, 217, 327));
    buAnlegen.setBounds(new Rectangle(229, 335, 110, 25));
    buAnlegen.setText("Anlegen");
    buAendern.setBounds(new Rectangle(377, 335, 110, 25));
    buAendern.setText("Ändern");
    buLoeschen.setBounds(new Rectangle(526, 335, 110, 25));
    buLoeschen.setText("Löschen");
    buBeenden.setBounds(new Rectangle(674, 335, 110, 25));
    buBeenden.setText("Beenden");
    buKontoAuswahl.setBounds(new Rectangle(516, 289, 115, 25));
    buKontoAuswahl.setText("auswählen");
    jLabel2.setBounds(new Rectangle(236, 61, 116, 15));
    jLabel2.setText("Passwort(*)");
    jLabel2.setPreferredSize(new Dimension(100, 16));
    tfBenutzername.setText("");
    tfBenutzername.setBounds(new Rectangle(334, 25, 165, 22));
    jLabel3.setPreferredSize(new Dimension(100, 16));
    jLabel3.setText("Passwort-Wdhl(*)");
    jLabel3.setBounds(new Rectangle(512, 61, 116, 15));
    tfPasswort.setBounds(new Rectangle(334, 57, 165, 22));
    tfPasswort.setText("");
    tfPasswortWdhl.setBounds(new Rectangle(619, 57, 165, 22));
    tfPasswortWdhl.setText("");
    tfName.setBounds(new Rectangle(334, 89, 165, 22));
    tfName.setText("");
    jLabel4.setPreferredSize(new Dimension(100, 16));
    jLabel4.setText("Name(*)");
    jLabel4.setBounds(new Rectangle(236, 93, 116, 15));
    jLabel5.setPreferredSize(new Dimension(100, 16));
    jLabel5.setText("Vorname(*)");
    jLabel5.setBounds(new Rectangle(512, 93, 116, 15));
    jLabel6.setPreferredSize(new Dimension(100, 16));
    jLabel6.setText("Titel");
    jLabel6.setBounds(new Rectangle(236, 125, 116, 15));
    jLabel7.setPreferredSize(new Dimension(100, 16));
    jLabel7.setText("Email");
    jLabel7.setBounds(new Rectangle(512, 126, 116, 15));
    jLabel8.setPreferredSize(new Dimension(100, 16));
    jLabel8.setText("Institut(*)");
    jLabel8.setBounds(new Rectangle(236, 262, 116, 15));
    jLabel9.setPreferredSize(new Dimension(100, 16));
    jLabel9.setText("Rollen(*)");
    jLabel9.setBounds(new Rectangle(512, 262, 116, 15));
    jLabel10.setPreferredSize(new Dimension(100, 16));
    jLabel10.setText("Privatkonto");
    jLabel10.setBounds(new Rectangle(236, 294, 116, 15));
    tfVorname.setText("");
    tfVorname.setBounds(new Rectangle(619, 89, 165, 22));
    tfTitel.setText("");
    tfTitel.setBounds(new Rectangle(334, 122, 165, 22));
    tfEMail.setText("");
    tfEMail.setBounds(new Rectangle(619, 122, 165, 22));
    tfKonto.setText("");
    tfKonto.setBounds(new Rectangle(341, 290, 165, 22));
    cbInstitut.setBounds(new Rectangle(341, 258, 165, 22));
    cbRollen.setBounds(new Rectangle(619, 258, 165, 22));
    jLabel11.setBounds(new Rectangle(6, 8, 116, 15));
    jLabel11.setText("Benutzer");
    jLabel11.setPreferredSize(new Dimension(100, 16));
    buRefresh.setBounds(new Rectangle(134, 3, 20, 20));
    buRefresh.setToolTipText("Aktualisieren");
    buRefresh.setText("");
    buKontoEntfernen.setBounds(new Rectangle(669, 289, 115, 25));
    buKontoEntfernen.setText("entziehen");
    tfTelefon.setBounds(new Rectangle(334, 154, 165, 22));
    tfTelefon.setText("");
    tfFax.setBounds(new Rectangle(619, 154, 165, 22));
    tfFax.setText("");
    jLabel12.setBounds(new Rectangle(236, 158, 116, 15));
    jLabel12.setText("Telefon");
    jLabel12.setPreferredSize(new Dimension(100, 16));
    jLabel13.setBounds(new Rectangle(512, 158, 116, 15));
    jLabel13.setText("Fax");
    jLabel13.setPreferredSize(new Dimension(100, 16));
    tfBau.setBounds(new Rectangle(334, 186, 165, 22));
    tfBau.setText("");
    tfRaum.setBounds(new Rectangle(619, 186, 165, 22));
    tfRaum.setText("");
    jLabel14.setBounds(new Rectangle(236, 190, 116, 15));
    jLabel14.setText("Bau");
    jLabel14.setPreferredSize(new Dimension(100, 16));
    jLabel15.setBounds(new Rectangle(512, 190, 116, 15));
    jLabel15.setText("Raum");
    jLabel15.setPreferredSize(new Dimension(100, 16));
    cbSoftBeauftragter.setText("Softwarebeauftragter");
    cbSoftBeauftragter.setBounds(new Rectangle(236, 222, 182, 23));
    this.getContentPane().add(jLabel1, null);
    this.getContentPane().add(jLabel5, null);
    this.getContentPane().add(jLabel2, null);
    this.getContentPane().add(tfPasswortWdhl, null);
    this.getContentPane().add(jLabel7, null);
    this.getContentPane().add(tfVorname, null);
    this.getContentPane().add(tfEMail, null);
    this.getContentPane().add(jLabel3, null);
		this.getContentPane().add(buRefresh, null);
    this.getContentPane().add(jLabel6, null);
    this.getContentPane().add(jLabel4, null);
    this.getContentPane().add(jLabel11, null);
    this.getContentPane().add(tfFax, null);
    this.getContentPane().add(jLabel12, null);
    this.getContentPane().add(jLabel13, null);
    this.getContentPane().add(jLabel14, null);
    this.getContentPane().add(jLabel15, null);
    this.getContentPane().add(tfRaum, null);
    this.getContentPane().add(buBeenden, null);
    this.getContentPane().add(buLoeschen, null);
    this.getContentPane().add(jLabel10, null);
    this.getContentPane().add(buAendern, null);
    this.getContentPane().add(cbRollen, null);
    this.getContentPane().add(buAnlegen, null);
    this.getContentPane().add(cbInstitut, null);
    this.getContentPane().add(jLabel9, null);
    this.getContentPane().add(tfKonto, null);
    this.getContentPane().add(buKontoAuswahl, null);
    this.getContentPane().add(buKontoEntfernen, null);
    this.getContentPane().add(jLabel8, null);
    this.getContentPane().add(cbSoftBeauftragter, null);
    this.getContentPane().add(tfBenutzername, null);
    this.getContentPane().add(tfPasswort, null);
    this.getContentPane().add(tfName, null);
    this.getContentPane().add(tfTitel, null);
    this.getContentPane().add(tfTelefon, null);
    this.getContentPane().add(tfBau, null);
    this.getContentPane().add(jScrollPane1, null);
    jScrollPane1.getViewport().add(listBenutzer, null);
  }

  private void loadUsers(){
	  try{
			if(frame != null){
		  	Benutzer benutzer = applicationServer.getUser(frame.getBenutzer().getBenutzername(), frame.getBenutzer().getPasswort());
				listModel.addElement(benutzer);
				listBenutzer.setSelectedIndex(0);
		  }else{
		  	Benutzer[] benutzer = applicationServer.getUsers();
				if(benutzer != null){
					listModel.clear();
				  for(int i = 0; i < benutzer.length; i++){
					  listModel.addElement(benutzer[i]);
				  }
				  listBenutzer.setSelectedIndex(0);
				}
		  }
	  }catch(Exception e){
		  System.out.println(e);
	  }
  }

  private void loadInstituts(){
	  try{
		  Institut[] institute = applicationServer.getInstitutes();

			if(institute != null){
				cbInstitut.removeAllItems();
				  for(int i = 0; i < institute.length; i++){
					  cbInstitut.addItem(institute[i]);
				  }
			}
	  }catch(Exception e){
		  System.out.println(e);
	  }

  }

  private void loadRollen(){
	  try{
		  Rolle[] rollen = applicationServer.getRollen();

			if(rollen != null){
				cbRollen.removeAllItems();
			  for(int i = 0; i < rollen.length; i++){
				  cbRollen.addItem(rollen[i]);
			  }
			}
	  }catch(Exception e){
		  System.out.println(e);
	  }
  }

  protected void addUser() throws ApplicationServerException, RemoteException{
		if(listBenutzer.isSelectionEmpty())
			throw new ApplicationServerException(0, "Es ist kein Benutzer selektiert !");
		else if(tfBenutzername.getText().equals("") || tfName.getText().equals("") || tfVorname.getText().equals("") || (tfPasswort.getPassword().length == 0))
			throw new ApplicationServerException(0, "Benutzername, Passwort, Name und Vorname müssen ausgefüllt sein.");
		else{
			if((new String(tfPasswort.getPassword())).equals(new String(tfPasswortWdhl.getPassword()))){
				PasswordEncrypt pe = new PasswordEncrypt();
				String psw = pe.encrypt(new String(tfPasswort.getPassword()).toString());

			  Benutzer benutzer = new Benutzer(tfBenutzername.getText(), psw, (Rolle)cbRollen.getSelectedItem(),
																				(Institut)cbInstitut.getSelectedItem(), tfTitel.getText(), tfName.getText(),
																				tfVorname.getText(), tfEMail.getText(), privatKonto, tfTelefon.getText(),
																				tfFax.getText(), tfBau.getText(), tfRaum.getText(), cbSoftBeauftragter.isSelected());
				int key = applicationServer.addUser(benutzer);
				benutzer.setId(key);
			  listModel.addElement(benutzer);
			  listBenutzer.setSelectedIndex(listModel.getSize() - 1);
			}else
				throw new ApplicationServerException(0, "Passwort und Passwort-Wiederholung sind nicht gleich.");
		}
  }

	protected void delUser() throws ApplicationServerException, RemoteException{
		Benutzer currBenutzer = (Benutzer)listModel.getElementAt(listBenutzer.getSelectedIndex());
		applicationServer.delUser(currBenutzer);
		listModel.remove(listBenutzer.getSelectedIndex());
	}


	public String setPrivatKonto(int fbKontoId){
		if(listBenutzer.isSelectionEmpty()){
			return "Es ist kein Benutzer selektiert ";
		}else{
			privatKonto = fbKontoId;
			return "";
		}
	}


  protected void changeUser() throws ApplicationServerException, RemoteException{
	  if(listBenutzer.isSelectionEmpty()){
			throw new ApplicationServerException(0, "Es ist kein Benutzer selektiert !");
		}else if(tfBenutzername.getText().equals("") || tfName.getText().equals("") || tfVorname.getText().equals("")){
			throw new ApplicationServerException(0, "Benutzername, Name und Vorname müssen ausgefüllt sein.");
	  }else{
		  String passwort = "";
		  if(!(tfPasswort.getPassword().length == 0)){
			  if((new String(tfPasswort.getPassword())).equals(new String(tfPasswortWdhl.getPassword()))){
					PasswordEncrypt pe = new PasswordEncrypt();
					passwort = pe.encrypt(new String(tfPasswort.getPassword()).toString());
				}else{
					throw new ApplicationServerException(0, "Passwort und Passwort-Wiederholung sind nicht gleich.");
			  }
		  }
	  	Benutzer currBenutzer = (Benutzer)listModel.getElementAt(listBenutzer.getSelectedIndex());
			if(passwort.equals(""))
				passwort = currBenutzer.getPasswort();

			Benutzer benutzer = new Benutzer(currBenutzer.getId(), tfBenutzername.getText(), passwort, (Rolle)cbRollen.getSelectedItem(),
																			(Institut)cbInstitut.getSelectedItem(), tfTitel.getText(), tfName.getText(),
																			tfVorname.getText(), tfEMail.getText(), privatKonto, tfTelefon.getText(),
																			tfFax.getText(), tfBau.getText(), tfRaum.getText(), cbSoftBeauftragter.isSelected());
			applicationServer.setUser(benutzer, currBenutzer);
			listModel.setElementAt(benutzer, listBenutzer.getSelectedIndex());
			if(frame != null)
				frame.setBenutzer(benutzer);
	  }
  }

  public void actionPerformed(ActionEvent e) {
	  try{
		  if ( e.getSource() == buAnlegen ) {
			  addUser();
		  } else if ( e.getSource() == buAendern ) {
			  changeUser();
		  } else if ( e.getSource() == buLoeschen ) {
				if(listBenutzer.isSelectionEmpty()){
					throw new ApplicationServerException(0, "Es ist kein Institut selektiert !");
			  }else{
				  if(JOptionPane.showConfirmDialog(this, "Wollen Sie wirklich löschen ?", "Löschen", 
				  																JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
					  delUser();
						loadUsers();
				  }
			  }
		  } else if ( e.getSource() == buRefresh ) {
				loadInstituts();
				loadRollen();
				loadUsers();
			} else if ( e.getSource() == buKontoAuswahl ) {
				AuswahlFBKonto kontoAuswahl = new AuswahlFBKonto(this, (Institut)cbInstitut.getSelectedItem(), false, applicationServer, false);
				kontoAuswahl.setVisible(true);
			} else if ( e.getSource() == buKontoEntfernen ) {
				privatKonto = 0;
				tfKonto.setText("");
			} else if ( e.getSource() == buBeenden ) {
			  this.dispose();
		  }
	  }catch(ApplicationServerException ex){
			MessageDialogs.showDetailMessageDialog(this, "Fehler", ex.getMessage(), ex.getNestedMessage(), MessageDialogs.ERROR_ICON);
	  } catch(RemoteException re) {
		  MessageDialogs.showDetailMessageDialog(this, "Fehler", re.getMessage(), 
												  "Fehler bei RMI-Kommunikation", MessageDialogs.ERROR_ICON);
	  }
 }

	public void valueChanged(ListSelectionEvent e) {
		if(!listBenutzer.isSelectionEmpty()){
			Benutzer benutzer = (Benutzer)listModel.getElementAt(listBenutzer.getSelectedIndex());

			tfBenutzername.setText(benutzer.getBenutzername());
			tfName.setText(benutzer.getName());
			tfVorname.setText(benutzer.getVorname());
			tfEMail.setText(benutzer.getEmail());
			tfTelefon.setText(benutzer.getTelefon());
			tfFax.setText(benutzer.getFax());
			tfBau.setText(benutzer.getBau());
			tfRaum.setText(benutzer.getRaum());
			cbSoftBeauftragter.setSelected(benutzer.getSwBeauftragter());
			
			if(benutzer.getPrivatKonto() != 0){
				try {
					FBUnterkonto fbKonto = applicationServer.getFBKonto(benutzer.getPrivatKonto());
					if(fbKonto != null)
						tfKonto.setText(fbKonto.getBezeichnung());
					privatKonto = benutzer.getPrivatKonto();
				} catch (ApplicationServerException e1) {
					System.out.println(e1.getMessage());
				}
			}else{
				tfKonto.setText("");
				privatKonto = 0;
			}
			tfTitel.setText(benutzer.getTitel());
			tfPasswort.setText("");
			tfPasswortWdhl.setText("");

			cbInstitut.setSelectedItem(benutzer.getKostenstelle());
			cbRollen.setSelectedItem(benutzer.getRolle());
		}

	}

	public static void main(String[] args) {
		JFrame test = new JFrame("Benutzerverwaltung Test");
		JDesktopPane desk = new JDesktopPane();
		desk.setDesktopManager(new DefaultDesktopManager());
		test.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		test.setContentPane(desk);
		test.setBounds(100,100,800,700);
		try{
			CentralServer server = (CentralServer)Naming.lookup("//localhost/mittelverwaltung");
//			ApplicationServer applicationServer = server.getMyApplicationServer();
//			PasswordEncrypt pe = new PasswordEncrypt();
//			String psw = pe.encrypt(new String("r.driesner").toString());
//			applicationServer.login("r.driesner", psw);
//			Benutzerverwaltung benutzerVerwaltung = new Benutzerverwaltung(applicationServer);
//			desk.add(benutzerVerwaltung);
//			test.show();
//			benutzerVerwaltung.show();
		}catch(Exception e){
				System.out.println(e);
		}
  }

	/* (Kein Javadoc)
	 * @see gui.FBKontoSelectable#setFBKonto(dbObjects.FBUnterkonto)
	 */
	public void setFBKonto(FBUnterkonto fbKonto) {
		if(fbKonto != null){
			setPrivatKonto(fbKonto.getId());
			tfKonto.setText(fbKonto.getBezeichnung());
		}
	}
}

