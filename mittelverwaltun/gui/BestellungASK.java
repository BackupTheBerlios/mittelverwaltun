package gui;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import dbObjects.ASKBestellung;
import dbObjects.Angebot;
import dbObjects.Benutzer;
import dbObjects.FBUnterkonto;
import dbObjects.Firma;
import dbObjects.Institut;
import dbObjects.ZVTitel;
import dbObjects.ZVUntertitel;

import applicationServer.ApplicationServerException;
import applicationServer.CentralServer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.*;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import javax.swing.border.*;


public class BestellungASK extends JInternalFrame implements ActionListener, TableModelListener, ItemListener, ZVKontoSelectable, FBKontoSelectable {
  JLabel jLabel4 = new JLabel();
  JTextPane tpBemerkungen = new JTextPane();
  JLabel jLabel6 = new JLabel();
  JScrollPane scrollTable = new JScrollPane();
  PositionsTable tableBestellung;
  MainFrame frame;
  JComboBox cbSwBeauftragter = new JComboBox();
  JButton buBeenden = new JButton(Functions.getCloseIcon(this.getClass()));
  JButton buDelete = new JButton(Functions.getDelIcon(getClass()));
  JButton buDrucken = new JButton(Functions.getPrintIcon(getClass()));
  JButton buBestellen = new JButton(Functions.getBestellIcon(getClass()));
  JButton buSpeichern = new JButton(Functions.getSaveIcon(this.getClass()));
  CurrencyTextField tfBestellsumme = new CurrencyTextField();
  JLabel jLabel8 = new JLabel();
  JButton buAddPosition = new JButton(Functions.getAddIcon(getClass()));
  JLabel jLabel9 = new JLabel();
  JLabel jLabel7 = new JLabel();
  JLabel jLabel10 = new JLabel();
  JLabel labUT = new JLabel();
  JLabel labTitel = new JLabel();
  JLabel labInstitut1 = new JLabel();
  JComboBox cbEmpfaenger = new JComboBox();
  JComboBox cbInstitut = new JComboBox();
  JLabel jLabel11 = new JLabel();
  JComboBox cbAuftraggeber = new JComboBox();
  JLabel labKapitel = new JLabel();
  JLabel labBestellNr1 = new JLabel();
  JLabel jLabel12 = new JLabel();
  JLabel labInstitut2 = new JLabel();
  JButton buFBKonto = new JButton();
  JButton buTitel = new JButton();
  JTextField tfFBKonto = new JTextField();
  JFormattedTextField tfBestellDatum = new JFormattedTextField(DateFormat.getDateInstance());
  JLabel jLabel24 = new JLabel();
  JLabel labInstitut = new JLabel();
  JLabel jLabel13 = new JLabel();
  JLabel jLabel14 = new JLabel();
  ZVUntertitel zvTitel;
	FBUnterkonto fbKonto;
	ASKBestellung bestellung;
	Firma firma;
  JLabel labInstitut3 = new JLabel();
  JLabel labFirma = new JLabel();
  CurrencyTextField tfNetto = new CurrencyTextField();
  CurrencyTextField tf7MwSt = new CurrencyTextField();
  CurrencyTextField tf16MwSt = new CurrencyTextField();
  JPanel panelZusatz = new JPanel();
  TitledBorder titledBorderPanel1;
  JScrollPane jScrollPane1 = new JScrollPane();
  JPanel panelAngebot = new JPanel();
  TitledBorder titledBorderPanel2;
  TitledBorder titledBorderPanel3;
  JPanel jPanel1 = new JPanel();
  JLabel labInstitut4 = new JLabel();
  JTextField tfAuftragsnummer = new JTextField();


  public BestellungASK(MainFrame frame) {
		this.frame = frame;
		this.setClosable(true);
		this.setIconifiable(true);

		try {
			tableBestellung = new PositionsTable(PositionsTable.ASK_DURCHFUEHRUNG, true, this, new ArrayList(), frame.getApplicationServer().getInstitutes());
		} catch (ApplicationServerException e) {
			e.printStackTrace();
		}

		init();
		tfBestellDatum.setValue(new Date(System.currentTimeMillis()));
  }

  public BestellungASK(MainFrame frame, ASKBestellung bestellung) {
	  this.frame = frame;
	  this.setClosable(true);
	  this.setIconifiable(true);
	  this.bestellung = bestellung;

		try {
			tableBestellung = new PositionsTable(PositionsTable.ASK_DURCHFUEHRUNG, true, this, bestellung.getAngebot().getPositionen(), frame.getApplicationServer().getInstitutes());
		} catch (ApplicationServerException e) {
			e.printStackTrace();
		}

	  init();
	  setOrderData();
	}

  private void init(){
		try {
		jbInit();
	 }
	 catch(Exception e) {
		e.printStackTrace();
	 }
		
		cbInstitut.addItemListener(this);
		buDrucken.addActionListener(this);
		buTitel.addActionListener(this);
		buAddPosition.addActionListener(tableBestellung);
		buAddPosition.setActionCommand("addPosition");
		buBeenden.addActionListener(this);
		buSpeichern.addActionListener(this);
		buBestellen.addActionListener(this);
		buBestellen.setEnabled(false);
		buFBKonto.addActionListener(this);
		buDelete.addActionListener(this);
		buDelete.setEnabled(false);
		buBestellen.setEnabled(false);
		buSpeichern.setEnabled(false);
		setData();


		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
  }

  private void setOrderData(){
		buDelete.setEnabled(true);
		buBestellen.setEnabled(true);
		cbAuftraggeber.setSelectedItem(bestellung.getAuftraggeber());
		cbEmpfaenger.setSelectedItem(bestellung.getEmpfaenger());
		
		if(bestellung.getFbkonto()!=null) cbInstitut.setSelectedItem(bestellung.getFbkonto().getInstitut());
		
		cbSwBeauftragter.setSelectedItem(bestellung.getSwbeauftragter());
		tfBestellDatum.setValue(bestellung.getDatum());
		labFirma.setText("" + bestellung.getAngebot().getAnbieter());
		setFBKonto(bestellung.getFbkonto());
		setZVKonto(bestellung.getZvtitel());
		tpBemerkungen.setText(bestellung.getBemerkung());
		tfAuftragsnummer.setText(bestellung.getReferenznr());

		PositionsTableModel ptm = (PositionsTableModel)tableBestellung.getModel();

		tfNetto.setValue(new Float(ptm.getOrderSum() - ptm.get7PercentSum() - ptm.get16PercentSum()));
		tf7MwSt.setValue(new Float(ptm.get7PercentSum()));
		tf16MwSt.setValue(new Float(ptm.get16PercentSum()));
		tfBestellsumme.setValue(new Float(ptm.getOrderSum()));

		if(ptm.getOrderSum() == 0){
		  buBestellen.setEnabled(false);
			buSpeichern.setEnabled(false);
		}else{
			buBestellen.setEnabled(true);
			buSpeichern.setEnabled(true);
		}
  }

  private void loadInstituts(){
	  try {
			if(frame.getBenutzer().getSichtbarkeit() == Benutzer.VIEW_FACHBEREICH){
				Institut[] instituts = frame.getApplicationServer().getInstitutes();

			  if(instituts != null){
				  cbInstitut.removeAllItems();
					 for(int i = 0; i < instituts.length; i++){
						  cbInstitut.addItem(instituts[i]);
					 }
			  }
			}else if(frame.getBenutzer().getSichtbarkeit() == Benutzer.VIEW_INSTITUT){
				Institut institut = frame.getBenutzer().getKostenstelle();

				cbInstitut.removeAllItems();
				cbInstitut.addItem(institut);
			}
	  } catch (ApplicationServerException e) {
		  e.printStackTrace();
	  }
  }

  /**
   * lädt die ASK-Firma
   *
   */
  private void loadASKFirma(){
  	try {
			firma = frame.getApplicationServer().getASKFirma();
			labFirma.setText("" + firma);
		} catch (ApplicationServerException e) {
			e.printStackTrace();
	}
  }


  private String checkData(){
	  String error = "";

	  error += (tfBestellDatum.getValue() == null ? " - Datum \n" : "");
		error += (tfAuftragsnummer.getText().equals("") ? " - Auftragsnummer \n" : "");
	  error += (fbKonto == null ? " - Kostenstelle \n" : "");
		error += (zvTitel == null ? " - Haushaltstitel \n" : "");

	  return error;
	 }

	private void setData(){
	 loadUsers();
	 loadSwBeauftragte();
	 loadASKFirma();
	 loadInstituts();
 }

  private void saveOrder( int phase){
		ASKBestellung editedOrder = null;

		java.util.Date datum = (java.util.Date)tfBestellDatum.getValue();
		Date sqlDate = new Date(datum.getTime());

  	if(bestellung == null){
			Angebot a = new Angebot(tableBestellung.getOrderPositions(), sqlDate, firma, true);

			editedOrder = new ASKBestellung(tfAuftragsnummer.getText(), null, sqlDate, (Benutzer)frame.getBenutzer(),
																		  (Benutzer)cbAuftraggeber.getSelectedItem(), (Benutzer)cbEmpfaenger.getSelectedItem(),
																		  zvTitel, fbKonto, ((Float)tfBestellsumme.getValue()).floatValue(), 0, '0', a, tpBemerkungen.getText(),
																			(Benutzer)cbSwBeauftragter.getSelectedItem());
  	}else{
			editedOrder = (ASKBestellung)bestellung.clone();
		  editedOrder.getAngebot().setPositionen(tableBestellung.getOrderPositions());
		  editedOrder.setBestellwert(tableBestellung.getOrderSum());
		  editedOrder.setAuftraggeber((Benutzer)cbAuftraggeber.getSelectedItem());
		  editedOrder.setSwbeauftragter((Benutzer)cbSwBeauftragter.getSelectedItem());
		  editedOrder.setBemerkung(tpBemerkungen.getText());
		  editedOrder.setReferenznr(tfAuftragsnummer.getText());
		  editedOrder.setDatum(sqlDate);
		  editedOrder.setEmpfaenger((Benutzer)cbEmpfaenger.getSelectedItem());
		  editedOrder.setFbkonto(fbKonto);
		  editedOrder.setZvtitel(zvTitel);
  	}

  	if(phase == 1){
			float betrag = tableBestellung.getOrderSum();
			editedOrder.setPhase('1');
			editedOrder.setVerbindlichkeiten(betrag);
			editedOrder.setBestellwert(betrag);
			editedOrder.getZvtitel().setVormerkungen(betrag);
			editedOrder.getFbkonto().setVormerkungen(betrag);
  	}

		int id = 0;

		try {
			if(bestellung == null)
				id = frame.getApplicationServer().addBestellung(editedOrder);
			else{
				id = editedOrder.getId();
				frame.getApplicationServer().setBestellung(frame.getBenutzer(), bestellung, editedOrder, true);
			}
			
			buDelete.setEnabled(true);
			if(phase == 1){
				buBestellen.setEnabled(false);
			}
				
			bestellung = frame.getApplicationServer().getASKBestellung(id);
		} catch (ApplicationServerException e) {
				MessageDialogs.showDetailMessageDialog(this, "Fehler", e.getMessage(), e.getNestedMessage(), MessageDialogs.ERROR_ICON);
				e.printStackTrace();
		}
 }

 	private void clearInputFields(){
 		tableBestellung.removeAll();
	 	labKapitel.setText("");
		labTitel.setText("");
		labUT.setText("");
		tpBemerkungen.setText("");
		bestellung = null;
		fbKonto = null;
		zvTitel = null;
	 	buBestellen.setEnabled(false);
	}

	/**
	 * lädt User für die Auswahl des Auftraggebers und Empfängers. Falls der User ein Institutssdmin ist,
	 * werden nur die Benutzer des jeweiligen Instituts geladen. Ist der User ein Admin werden alle User
	 * geladen.
	 */
	private void loadUsers(){
		try{
			Benutzer[] users = null;
			if(frame.getBenutzer().getSichtbarkeit() == Benutzer.VIEW_FACHBEREICH)
				users = frame.getApplicationServer().getUsers();
			else if(frame.getBenutzer().getSichtbarkeit() == Benutzer.VIEW_INSTITUT)
				users = frame.getApplicationServer().getUsers(frame.getBenutzer().getKostenstelle());

			  if(users != null){
				  cbAuftraggeber.removeAllItems();
					cbEmpfaenger.removeAllItems();
					 for(int i = 0; i < users.length; i++){
						cbAuftraggeber.addItem(users[i]);
						cbEmpfaenger.addItem(users[i]);
					 }
					cbAuftraggeber.setSelectedItem(frame.getBenutzer());
					cbEmpfaenger.setSelectedItem(frame.getBenutzer());
			  }
		}catch(Exception e){
			 System.out.println(e);
		}

	}

	/**
	 * lädt alle Softwarebeauftragte
	 *
	 */
	private void loadSwBeauftragte(){
		try{
			Benutzer[] users = frame.getApplicationServer().getSwBeauftragte();

		  if(users != null){
			  cbSwBeauftragter.removeAllItems();
				 for(int i = 0; i < users.length; i++){
					cbSwBeauftragter.addItem(users[i]);
				 }
		  }
		}catch(Exception e){
			 System.out.println(e);
		}

	}

  public static void main(String[] args) {
		MainFrame test = new MainFrame("FBMittelverwaltung");
		BestellungASK bestellung;
		try{
			CentralServer server = (CentralServer)Naming.lookup("//localhost/mittelverwaltung");
//			ApplicationServer applicationServer = server.getMyApplicationServer();
//			test.setApplicationServer(applicationServer);
//			PasswordEncrypt pe = new PasswordEncrypt();
//			String psw = pe.encrypt(new String("a").toString());
//			test.setBenutzer(applicationServer.login("test", psw));
//			test.setBounds(100,100,800,900);
//			test.setExtendedState(Frame.MAXIMIZED_BOTH);
//			test.setJMenuBar( new MainMenu( test ) );
//
//			ASKBestellung best = applicationServer.getASKBestellung(8);
//			bestellung = new BestellungASK(test, best);
//			test.addChild(bestellung);
//
//			test.show();
//			bestellung.show();
		}catch(Exception e){
			bestellung = new BestellungASK(test);
			test.addChild(bestellung);
			test.setVisible(true);
			bestellung.show();
			System.out.println(e);
		}
	 }

  private void jbInit() throws Exception {
    titledBorderPanel1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158)),"Allgemeine Informationen");
    titledBorderPanel2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158)),"Angebot");
    titledBorderPanel3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158)),"Allgemeine Informationen");
    this.setBounds(50,50,605,635);
    cbSwBeauftragter.setBounds(new Rectangle(108, 107, 194, 21));
    buBeenden.setBounds(new Rectangle(479, 576, 112, 25));
    buBeenden.setText("Beenden");
    buDelete.setText("Löschen");
    buDelete.setBounds(new Rectangle(242, 576, 112, 25));
    buDrucken.setBounds(new Rectangle(360, 576, 112, 25));
    buDrucken.setText("Vorschau");
    buBestellen.setBounds(new Rectangle(4, 576, 112, 25));
    buBestellen.setText("Bestellen");
    buSpeichern.setBounds(new Rectangle(123, 576, 112, 25));
    buSpeichern.setText("Speichern");
    tfBestellsumme.setEditable(false);
    tfBestellsumme.setEnabled(false);
    tfBestellsumme.setFont(new java.awt.Font("SansSerif", 1, 12));
    tfBestellsumme.setBounds(new Rectangle(433, 216, 117, 21));
    jLabel8.setBounds(new Rectangle(317, 169, 79, 15));
    jLabel8.setText("7 % MwSt.");
    jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
    buAddPosition.setText("Position hinzufügen");
    buAddPosition.setBounds(new Rectangle(15, 143, 179, 25));
    jLabel9.setBounds(new Rectangle(317, 219, 96, 15));
    jLabel9.setText("Bestellsumme:");
    jLabel9.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel7.setBounds(new Rectangle(317, 145, 115, 15));
    jLabel7.setText("Gesamtnettopreis:");
    jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setText("16 % MwSt.");
    jLabel10.setBounds(new Rectangle(317, 193, 79, 15));
    labUT.setBounds(new Rectangle(396, 194, 40, 15));
    labUT.setHorizontalAlignment(SwingConstants.CENTER);
    labUT.setFont(new java.awt.Font("Dialog", 0, 12));
    labTitel.setBounds(new Rectangle(324, 194, 52, 15));
    labTitel.setHorizontalAlignment(SwingConstants.CENTER);
    labTitel.setFont(new java.awt.Font("Dialog", 0, 12));
    labInstitut1.setFont(new java.awt.Font("Dialog", 0, 12));
    labInstitut1.setText("Auftraggeber:");
    labInstitut1.setBounds(new Rectangle(11, 80, 89, 15));
    cbEmpfaenger.setBounds(new Rectangle(375, 79, 194, 21));
    cbInstitut.setBounds(new Rectangle(108, 135, 460, 21));
    jLabel11.setBounds(new Rectangle(299, 194, 40, 15));
    jLabel11.setText("Titel");
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    cbAuftraggeber.setBounds(new Rectangle(108, 79, 194, 21));
    labKapitel.setBounds(new Rectangle(239, 194, 55, 15));
    labKapitel.setHorizontalAlignment(SwingConstants.CENTER);
    labKapitel.setFont(new java.awt.Font("Dialog", 0, 12));
    labBestellNr1.setBorder(null);
    labBestellNr1.setText(" Bestell-Datum:");
    labBestellNr1.setBounds(new Rectangle(409, 19, 95, 23));
    labBestellNr1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel12.setBounds(new Rectangle(11, 194, 185, 15));
    jLabel12.setText("zu belastender Haushaltstitel:");
    jLabel12.setFont(new java.awt.Font("Dialog", 0, 12));
    labInstitut2.setBounds(new Rectangle(309, 82, 89, 15));
    labInstitut2.setText("Empfänger:");
    labInstitut2.setFont(new java.awt.Font("Dialog", 0, 12));
    buFBKonto.setText("FBKonto");
    buFBKonto.setBounds(new Rectangle(459, 165, 109, 21));
    buTitel.setText("Titelauswahl");
    buTitel.setActionCommand("buTitel");
    buTitel.setBounds(new Rectangle(459, 191, 109, 21));
    tfFBKonto.setBounds(new Rectangle(108, 165, 342, 21));
    tfFBKonto.setText("");
    tfFBKonto.setEditable(false);
    tfBestellDatum.setBounds(new Rectangle(497, 20, 72, 20));
    jLabel24.setBounds(new Rectangle(11, 166, 82, 15));
    jLabel24.setText("Kostenstelle:");
    jLabel24.setFont(new java.awt.Font("Dialog", 0, 12));
    labInstitut.setBounds(new Rectangle(11, 137, 50, 15));
    labInstitut.setText("Institut:");
    labInstitut.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel13.setBounds(new Rectangle(198, 194, 53, 15));
    jLabel13.setText("Kapitel");
    jLabel13.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel14.setBounds(new Rectangle(378, 194, 33, 15));
    jLabel14.setText("UT");
    jLabel14.setFont(new java.awt.Font("Dialog", 0, 12));
    labInstitut3.setBounds(new Rectangle(11, 52, 63, 15));
    labInstitut3.setText("Anbieter:");
    labInstitut3.setFont(new java.awt.Font("Dialog", 0, 12));
    labFirma.setFont(new java.awt.Font("Dialog", 0, 12));
    labFirma.setText("");
    labFirma.setBounds(new Rectangle(108, 51, 460, 15));
    tfNetto.setEditable(false);
    tfNetto.setEnabled(false);
    tfNetto.setBounds(new Rectangle(433, 142, 117, 21));
    tf7MwSt.setEditable(false);
    tf7MwSt.setEnabled(false);
    tf7MwSt.setBounds(new Rectangle(433, 166, 117, 21));
    tf16MwSt.setEditable(false);
    tf16MwSt.setEnabled(false);
    tf16MwSt.setBounds(new Rectangle(433, 190, 117, 21));
    panelZusatz.setBorder(titledBorderPanel1);
    panelZusatz.setBounds(new Rectangle(4, 229, 587, 101));
    panelZusatz.setLayout(null);
    titledBorderPanel1.setTitle("Zusatzinformationen");
    titledBorderPanel1.setTitleFont(new java.awt.Font("Dialog", 1, 11));
    jScrollPane1.setBounds(new Rectangle(14, 42, 561, 51));
    panelAngebot.setBorder(titledBorderPanel2);
    panelAngebot.setBounds(new Rectangle(4, 331, 587, 243));
    panelAngebot.setLayout(null);
    titledBorderPanel2.setTitle("Angebot");
    titledBorderPanel2.setTitleFont(new java.awt.Font("Dialog", 1, 11));
    titledBorderPanel3.setTitle("Allgemeine Informationen");
    titledBorderPanel3.setTitleFont(new java.awt.Font("Dialog", 1, 11));
    jPanel1.setBorder(titledBorderPanel3);
    jPanel1.setBounds(new Rectangle(4, 3, 587, 220));
    jPanel1.setLayout(null);
    labInstitut4.setFont(new java.awt.Font("Dialog", 0, 12));
    labInstitut4.setText("Auftragsnummer:");
    labInstitut4.setBounds(new Rectangle(11, 23, 110, 15));
    tfAuftragsnummer.setText("");
    tfAuftragsnummer.setBounds(new Rectangle(108, 20, 130, 21));
    panelZusatz.add(jScrollPane1, null);
    panelZusatz.add(jLabel4, null);
    jScrollPane1.getViewport().add(tpBemerkungen, null);
    this.getContentPane().add(panelAngebot, null);
    this.getContentPane().add(buBeenden, null);
    this.getContentPane().add(jPanel1, null);
    this.setTitle("Bestellung ASK");
    this.getContentPane().setLayout(null);
    jLabel4.setText("363,50");
    jLabel4.setBounds(new Rectangle(484, 9, 79, 15));
    jLabel4.setText("58,16");
    jLabel4.setBounds(new Rectangle(484, 29, 79, 15));
    jLabel4.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel4.setText("Bemerkungen");
    jLabel4.setBounds(new Rectangle(18, 21, 95, 15));
    tpBemerkungen.setEditable(true);
    tpBemerkungen.setText("");
    tpBemerkungen.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel6.setBounds(new Rectangle(11, 109, 104, 15));
    jLabel6.setText("SW-Beauftragte/r:");
    jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
    scrollTable.setBounds(new Rectangle(15, 18, 556, 123));
    panelAngebot.add(buAddPosition, null);
    panelAngebot.add(scrollTable, null);
    panelAngebot.add(tfBestellsumme, null);
    panelAngebot.add(jLabel7, null);
    panelAngebot.add(tfNetto, null);
    panelAngebot.add(jLabel8, null);
    panelAngebot.add(tf7MwSt, null);
    panelAngebot.add(jLabel10, null);
    panelAngebot.add(tf16MwSt, null);
    panelAngebot.add(jLabel9, null);
    scrollTable.getViewport().add(tableBestellung, null);
    this.getContentPane().add(buBestellen, null);
    this.getContentPane().add(buSpeichern, null);
    this.getContentPane().add(buDelete, null);
    this.getContentPane().add(buDrucken, null);
    this.getContentPane().add(panelZusatz, null);
    jPanel1.add(buTitel, null);
    jPanel1.add(tfBestellDatum, null);
    jPanel1.add(jLabel13, null);
    jPanel1.add(labKapitel, null);
    jPanel1.add(jLabel11, null);
    jPanel1.add(labTitel, null);
    jPanel1.add(jLabel14, null);
    jPanel1.add(labUT, null);
    jPanel1.add(cbAuftraggeber, null);
    jPanel1.add(labInstitut2, null);
    jPanel1.add(cbEmpfaenger, null);
    jPanel1.add(jLabel12, null);
    jPanel1.add(jLabel6, null);
    jPanel1.add(labInstitut1, null);
    jPanel1.add(labInstitut, null);
    jPanel1.add(jLabel24, null);
    jPanel1.add(tfFBKonto, null);
    jPanel1.add(cbSwBeauftragter, null);
    jPanel1.add(cbInstitut, null);
    jPanel1.add(labInstitut4, null);
    jPanel1.add(labFirma, null);
    jPanel1.add(labInstitut3, null);
    jPanel1.add(tfAuftragsnummer, null);
    jPanel1.add(labBestellNr1, null);
    jPanel1.add(buFBKonto, null);
  }

  private void printBestellung(){
	  PrintASKBestellung printOrder = new PrintASKBestellung(this.bestellung, frame);
  }

  public void actionPerformed(ActionEvent e) {
	  String error = "";
	  if ( e.getSource() == buFBKonto ) {
		  AuswahlFBKonto fbKontoAuswahl = new AuswahlFBKonto(this, (Institut)cbInstitut.getSelectedItem(), frame.getApplicationServer(), true);
			fbKontoAuswahl.setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
		  fbKontoAuswahl.setVisible(true);
	  }else if ( e.getSource() == buTitel ) {
		  AuswahlZVKonto kontoAuswahl = new AuswahlZVKonto(this, fbKonto, false, frame);
			kontoAuswahl.setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
		  kontoAuswahl.setVisible(true);
	  }else if ( e.getSource() == buDrucken ) {
		  printBestellung();
	  }else if ( e.getSource() == buBestellen ) {
		  error = checkData();
		  if(error.equals("")){
				saveOrder(1);
		  }else{
			  JOptionPane.showMessageDialog(
						 this,
						 error,
						 "Warnung",
						 JOptionPane.ERROR_MESSAGE);
		  }
  	}else if ( e.getSource() == buSpeichern ) {
		  error = checkData();
		  if(error.equals("")){
			  saveOrder(0);
		  }else{
			  JOptionPane.showMessageDialog(
						 this,
						 error,
						 "Warnung",
						 JOptionPane.ERROR_MESSAGE);
		  }
	  }else if ( e.getSource() == buDelete ) {
			int answer = JOptionPane.showConfirmDialog(
						getComponent(0), "Soll die Bestellung wirklich gelöscht werden ? ", "Warnung",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
			if(answer == 0){
				delOrder();
			}
		}else if ( e.getSource() == buBeenden ) {
		  dispose();
	  }
  }

  private void delOrder(){
	  try {
		  if(bestellung != null)
			  frame.getApplicationServer().delBestellung(bestellung);
			  clearInputFields();
	  } catch (ApplicationServerException e) {
		  MessageDialogs.showDetailMessageDialog(this, "Warnung", e.getMessage(), e.getNestedMessage(), MessageDialogs.WARNING_ICON);
		  e.printStackTrace();
	  }
  }


	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == cbInstitut){
			fbKonto = null;
			zvTitel = null;
			tfFBKonto.setText("");
			labKapitel.setText("");
			labTitel.setText("");
			labUT.setText("");
		}
	}

	public void setZVKonto(ZVUntertitel zvTitel) {
		if (zvTitel != null){
			if(zvTitel.getZVTitel() != null){
				labKapitel.setText(zvTitel.getZVTitel().getZVKonto().getKapitel());
				labTitel.setText(zvTitel.getTitel());
				labUT.setText(zvTitel.getUntertitel());
				this.zvTitel = zvTitel.getZVTitel();
			}else{
				labKapitel.setText(((ZVTitel)zvTitel).getZVKonto().getKapitel());
				labTitel.setText(zvTitel.getTitel());
				labUT.setText("");
				this.zvTitel = zvTitel;
			}
		}else this.zvTitel = null;
	}	
	
//	public void setZVKonto(ZVUntertitel zvTitel) {
//		if(zvTitel.getZVTitel() != null){
//			labKapitel.setText(zvTitel.getZVTitel().getZVKonto().getKapitel());
//			labTitel.setText(zvTitel.getTitel());
//			labUT.setText(zvTitel.getUntertitel());
//			this.zvTitel = zvTitel.getZVTitel();
//		}else{
//			labKapitel.setText(((ZVTitel)zvTitel).getZVKonto().getKapitel());
//			labTitel.setText(zvTitel.getTitel());
//			labUT.setText("");
//			this.zvTitel = (ZVTitel)zvTitel;
//		}
//	}

	public void setFBKonto(FBUnterkonto fbKonto) {
		this.fbKonto = fbKonto;
		if (fbKonto != null){
			tfFBKonto.setText(fbKonto.toString());
			buBestellen.setSelected(true);
		}
	}
	
//	public void setFBKonto(FBUnterkonto fbKonto) {
//		this.fbKonto = fbKonto;
//		tfFBKonto.setText(fbKonto.toString());
//	}

	public void tableChanged(TableModelEvent e) {
		PositionsTableModel ptm = (PositionsTableModel)e.getSource();

		tfNetto.setValue(new Float(ptm.getOrderSum() - ptm.get7PercentSum() - ptm.get16PercentSum()));
		tf7MwSt.setValue(new Float(ptm.get7PercentSum()));
		tf16MwSt.setValue(new Float(ptm.get16PercentSum()));
		tfBestellsumme.setValue(new Float(ptm.getOrderSum()));

		if(ptm.getOrderSum() == 0){
		  buBestellen.setEnabled(false);
		 	buSpeichern.setEnabled(false);
		}else{
			buBestellen.setEnabled(true);
			buSpeichern.setEnabled(true);
		}
	}
}