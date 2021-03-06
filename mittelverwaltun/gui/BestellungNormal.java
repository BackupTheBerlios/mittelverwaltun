package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dbObjects.*;

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

/**
 *
 * @author robert
 *
 * Klasse f�r die StandardBestellung wird f�r die Sondierungsphase und die Bestellphase benutzt.
 * Diese Bestellung kann nur ein Admin oder ein Institutsadmin durchf�hren.
 */
public class BestellungNormal extends JInternalFrame implements ActionListener, ItemListener, ZVKontoSelectable, FBKontoSelectable {

  JLabel labKoSt = new JLabel();
  JTextPane tpAuftragGrund = new JTextPane();
  JTextPane jTextPane1 = new JTextPane();
  JPanel oben = new JPanel();
  JLabel jLabel9 = new JLabel();
  JLabel labTitel = new JLabel();
  JLabel labUT = new JLabel();
  BestellungBeilageTable tableAngebote = new BestellungBeilageTable(this);
  JLabel jLabel22 = new JLabel();
  JLabel jLabel8 = new JLabel();
  JLabel jLabel12 = new JLabel();
  JScrollPane jScrollPane2 = new JScrollPane();
  JLabel jLabel13 = new JLabel();
  JLabel jLabel11 = new JLabel();
  JLabel labKapitel = new JLabel();
  JLabel jLabel7 = new JLabel();
  JLabel labBestellNr = new JLabel();
  JTextPane tpVerwendungszweck = new JTextPane();
  JCheckBox cbDrittelMittel = new JCheckBox();
  JLabel jLabel10 = new JLabel();
  JLabel jLabel24 = new JLabel();
  JTextField tfErsatzText = new JTextField();
  JTextField tfInventarNr = new JTextField();
  
  MainFrame frame;
  JButton buTitel = new JButton();
  JLabel labInstitut = new JLabel();
  JComboBox cbInstitut = new JComboBox();
  ButtonGroup buttonGroup1 = new ButtonGroup();
  JRadioButton rbErstbeschaffung = new JRadioButton();
  JRadioButton rbErsatz = new JRadioButton();
  ButtonGroup buttonGroup2 = new ButtonGroup();
  JRadioButton rbAngebotGuenstig = new JRadioButton();
  JRadioButton rbAuftragGrund = new JRadioButton();
  ButtonGroup buttonGroup3 = new ButtonGroup();
  JPanel panelErsatz = new JPanel();
  JPanel panelBeilage = new JPanel();
  JButton buAddAngebot = new JButton(Functions.getAddIcon(getClass()));
  JButton buBeenden = new JButton(Functions.getCloseIcon(this.getClass()));
  JButton buSpeichern = new JButton(Functions.getSaveIcon(this.getClass()));
  JButton buBestellen = new JButton(Functions.getBestellIcon(getClass()));
  JButton buDelete = new JButton(Functions.getDelIcon(getClass()));
  JButton buDrucken = new JButton(Functions.getPrintIcon(getClass()));
	JTextField tfReferenzNr = new JTextField();
  JComboBox cbKostenart = new JComboBox();
  JLabel labInstitut1 = new JLabel();
  JComboBox cbAuftraggeber = new JComboBox();
  JComboBox cbEmpfaenger = new JComboBox();
  JTextPane tpBemerkungen = new JTextPane();
  JLabel labInstitut2 = new JLabel();
  JLabel labBestellNr1 = new JLabel();
  JFormattedTextField tfBestellDatum = new JFormattedTextField(DateFormat.getDateInstance());
  int angebotNr;
  ZVUntertitel zvTitel;
  FBUnterkonto fbKonto;
  StandardBestellung bestellung;
  JTextField tfFBKonto = new JTextField();
  JButton buFBKonto = new JButton();
  JTabbedPane jTabbedPane1 = new JTabbedPane();
  JPanel panelZweck = new JPanel();
  JPanel panelAuswahlgrund = new JPanel();
  JPanel panelBemerkungen = new JPanel();
  JScrollPane jScrollPane3 = new JScrollPane();
  JScrollPane jScrollPane4 = new JScrollPane();
  JScrollPane jScrollPane5 = new JScrollPane();

  public BestellungNormal(MainFrame frame) {
  	this.frame = frame;
		this.setClosable(true);
		this.setIconifiable(true);

		init();
  }

  public BestellungNormal(MainFrame frame, StandardBestellung bestellung) {
	  this.frame = frame;
	  this.setClosable(true);
	  this.setIconifiable(true);
	  this.bestellung = bestellung;

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
		buAddAngebot.addActionListener(this);
		buDrucken.setEnabled(false);
		buBeenden.addActionListener(this);
		buSpeichern.addActionListener(this);
		buBestellen.addActionListener(this);
		buBestellen.setEnabled(false);
		buFBKonto.addActionListener(this);
		buDelete.addActionListener(this);
		buDelete.setEnabled(false);
		rbErsatz.addActionListener(this);
		rbErstbeschaffung.addActionListener(this);
		rbAngebotGuenstig.addActionListener(this);
		rbAuftragGrund.addActionListener(this);

		tfBestellDatum.setValue(new Date(System.currentTimeMillis()));
		setData();

		panelErsatz.setVisible(false);
		tpAuftragGrund.setVisible(false);
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
  }

  private String checkData(){
  	String error = "";

  	error += (tfReferenzNr.getText().equals("") ? " - ReferenzNr \n" : "");
  	error += (zvTitel == null ? " - zvTitel \n" : "");
		error += (fbKonto == null ? " - fbKonto \n" : "");

  	return error;
  }

  public static void main(String[] args) {
		MainFrame test = new MainFrame("FBMittelverwaltung");
		BestellungNormal bestellung;
	 	try{
		 	CentralServer server = (CentralServer)Naming.lookup("//localhost/mittelverwaltung");
//		 	ApplicationServer applicationServer = server.getMyApplicationServer();
//		 	test.setApplicationServer(applicationServer);
//		 	PasswordEncrypt pe = new PasswordEncrypt();
//		 	String psw = pe.encrypt(new String("a").toString());
//		 	test.setBenutzer(applicationServer.login("test", psw));
//	   	test.setBounds(100,100,800,900);
//		 	test.setExtendedState(Frame.MAXIMIZED_BOTH);
//
//		 	test.setJMenuBar( new MainMenu( test ) );
//		 	StandardBestellung best = applicationServer.getStandardBestellung(9);
//
//			bestellung = new BestellungNormal(test, best);
//		 	test.addChild(bestellung);
//		 	test.show();
//			bestellung.show();
	 }catch(Exception e){
			bestellung = new BestellungNormal(test);
			test.addChild(bestellung);
			test.setVisible(true);
			bestellung.show();
			System.out.println(e);
	 }
  }

  private void clearInputFields(){
  	tfErsatzText.setText("");
  	tfFBKonto.setText("");
  	tfInventarNr.setText("");
  	tfReferenzNr.setText("");
		labKoSt.setText("KoSt: " + ((Institut)cbInstitut.getSelectedItem()).getKostenstelle());
		labKapitel.setText("");
		labTitel.setText("");
		labUT.setText("");
		tpAuftragGrund.setText("");
		tpBemerkungen.setText("");
		tpVerwendungszweck.setText("");
  	bestellung = null;
		fbKonto = null;
		zvTitel = null;
  	angebotNr = 0;
  	buBestellen.setEnabled(false);
  	cbDrittelMittel.setSelected(false);
  	rbAngebotGuenstig.doClick();
  	rbErstbeschaffung.doClick();
  	tableAngebote.removeAll();
  }

  private void jbInit() throws Exception {
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
    panelAuswahlgrund.add(rbAngebotGuenstig, null);
    panelAuswahlgrund.add(rbAuftragGrund, null);
    panelAuswahlgrund.add(jScrollPane4, null);
    panelBeilage.add(jScrollPane2, null);
    panelBeilage.add(buAddAngebot, null);
    jScrollPane2.getViewport().add(tableAngebote, null);
    jScrollPane4.getViewport().add(tpAuftragGrund, null);
    this.getContentPane().add(buDelete, null);
    this.getContentPane().add(buBestellen, null);
    this.getContentPane().add(buSpeichern, null);
    this.getContentPane().add(buDrucken, null);
    this.getContentPane().add(buBeenden, null);
    this.getContentPane().add(panelBeilage, null);
    panelZweck.add(jScrollPane3, null);
    panelZweck.add(cbDrittelMittel, null);
    panelZweck.add(jTextPane1, null);
    jTabbedPane1.add(panelBemerkungen, "Bemerkungen");
    panelBemerkungen.add(jScrollPane5, null);
    jTabbedPane1.add(panelAuswahlgrund, "Auswahlgrund");
    jScrollPane5.getViewport().add(tpBemerkungen, null);
    jScrollPane3.getViewport().add(tpVerwendungszweck, null);
    jTabbedPane1.add(panelZweck, "Verwendungszweck");
    panelErsatz.add(tfInventarNr, null);
    panelErsatz.add(tfErsatzText, null);
    panelErsatz.add(jLabel8, null);
    panelBeilage.add(jTabbedPane1, null);
    panelBeilage.add(oben, null);
    oben.add(cbAuftraggeber, null);
    oben.add(jLabel24, null);
    oben.add(jLabel10, null);
    oben.add(labTitel, null);
    oben.add(rbErsatz, null);
    oben.add(labKapitel, null);
    oben.add(jLabel12, null);
    oben.add(jLabel13, null);
    oben.add(buFBKonto, null);
    oben.add(tfFBKonto, null);
    oben.add(labKoSt, null);
    oben.add(tfReferenzNr, null);
    oben.add(labBestellNr1, null);
    oben.add(tfBestellDatum, null);
    oben.add(labInstitut1, null);
    oben.add(cbEmpfaenger, null);
    oben.add(labInstitut2, null);
    oben.add(labBestellNr, null);
    oben.add(rbErstbeschaffung, null);
    oben.add(jLabel11, null);
    oben.add(jLabel7, null);
    oben.add(cbKostenart, null);
    oben.add(cbInstitut, null);
    oben.add(jLabel9, null);
    oben.add(labInstitut, null);
    oben.add(labUT, null);
    oben.add(jLabel22, null);
    oben.add(buTitel, null);
    oben.add(panelErsatz, null);
		this.setSize(new Dimension(590, 640));
    labInstitut.setFont(new java.awt.Font("Dialog", 0, 12));
    labInstitut.setText("Institut:");
    labInstitut.setBounds(new Rectangle(7, 106, 50, 15));
    cbInstitut.setBounds(new Rectangle(93, 103, 345, 21));
    buAddAngebot.setBounds(new Rectangle(11, 384, 170, 21));
    buAddAngebot.setText("Angebot hinzuf�gen");
    tpVerwendungszweck.setText("");
    rbErstbeschaffung.setFont(new java.awt.Font("Dialog", 0, 12));
    rbErstbeschaffung.setSelected(true);
    rbErstbeschaffung.setText("Erstbeschaffung");
    rbErstbeschaffung.setBounds(new Rectangle(9, 208, 144, 23));
    rbErsatz.setFont(new java.awt.Font("Dialog", 0, 12));
    rbErsatz.setText("Ersatz f�r:");
    rbErsatz.setBounds(new Rectangle(9, 236, 82, 23));
    rbAngebotGuenstig.setFont(new java.awt.Font("Dialog", 0, 12));
    rbAngebotGuenstig.setSelected(true);
    rbAngebotGuenstig.setText("das preisg�nstigste und wirtschaftlichste Angebot abgegeben hat.");
    rbAngebotGuenstig.setBounds(new Rectangle(2, 5, 418, 23));
    rbAuftragGrund.setFont(new java.awt.Font("Dialog", 0, 12));
    rbAuftragGrund.setText("aus folgenden Grund bevorzugt wurden:");
    rbAuftragGrund.setBounds(new Rectangle(2, 28, 302, 23));
    panelErsatz.setBounds(new Rectangle(90, 230, 461, 35));
    panelErsatz.setLayout(null);
    panelBeilage.setLayout(null);
    buBeenden.setBounds(new Rectangle(465, 578, 112, 25));
    buBeenden.setText("Beenden");
    buSpeichern.setBounds(new Rectangle(118, 578, 112, 25));
    buSpeichern.setText("Speichern");
    buBestellen.setBounds(new Rectangle(2, 578, 112, 25));
    buBestellen.setText("Bestellen");
    tfReferenzNr.setText("");
    tfReferenzNr.setBounds(new Rectangle(69, 10, 92, 21));
    cbKostenart.setBounds(new Rectangle(93, 160, 212, 21));
    labBestellNr.setBorder(null);
    labInstitut1.setBounds(new Rectangle(7, 49, 89, 15));
    labInstitut1.setText("Auftraggeber:");
    labInstitut1.setFont(new java.awt.Font("Dialog", 0, 12));
    cbAuftraggeber.setBounds(new Rectangle(93, 46, 344, 21));
    cbEmpfaenger.setBounds(new Rectangle(93, 74, 343, 21));
    tpBemerkungen.setEditable(true);
    tpBemerkungen.setText("");
    tpBemerkungen.setFont(new java.awt.Font("Dialog", 0, 12));
    labInstitut2.setFont(new java.awt.Font("Dialog", 0, 12));
    labInstitut2.setText("Empf�nger:");
    labInstitut2.setBounds(new Rectangle(7, 77, 89, 15));
    labBestellNr1.setFont(new java.awt.Font("Dialog", 0, 12));
    labBestellNr1.setBounds(new Rectangle(334, 9, 95, 23));
    labBestellNr1.setText(" Bestell-Datum:");
    labBestellNr1.setBorder(null);
    tfBestellDatum.setBounds(new Rectangle(422, 10, 72, 20));
    tfFBKonto.setEditable(false);
    tfFBKonto.setText("");
    tfFBKonto.setBounds(new Rectangle(93, 132, 345, 21));
    buFBKonto.setBounds(new Rectangle(438, 132, 109, 21));
    buFBKonto.setText("FBKonto");
    jTextPane1.setBackground(SystemColor.control);
    jTextPane1.setEditable(false);
    buDelete.setText("L�schen");
    buDelete.setBounds(new Rectangle(234, 578, 112, 25));
    jTabbedPane1.setBounds(new Rectangle(0, 407, 572, 164));
    panelZweck.setLayout(null);
    panelAuswahlgrund.setLayout(null);
    panelBemerkungen.setLayout(null);
    jScrollPane3.setBounds(new Rectangle(3, 44, 558, 69));
    jScrollPane4.setBounds(new Rectangle(17, 51, 545, 84));
    jScrollPane5.setBounds(new Rectangle(7, 7, 554, 127));
    panelBeilage.setBounds(new Rectangle(4, 4, 600, 600));

    this.setTitle("Bestellung");
    this.getContentPane().setLayout(null);
    labKoSt.setText("  KoSt");
    labKoSt.setBounds(new Rectangle(161, 9, 112, 23));
    labKoSt.setFont(new java.awt.Font("Dialog", 0, 12));
    labKoSt.setToolTipText("");
    labKoSt.setBorder(BorderFactory.createLineBorder(Color.black));
    tpAuftragGrund.setFont(new java.awt.Font("Dialog", 0, 12));
    tpAuftragGrund.setText("");
    jTextPane1.setBounds(new Rectangle(3, 2, 558, 40));
    jTextPane1.setText("Begr�ndung f�r die Notwendigkeit der Beschaffung und der zweckentsprechenden " +
    "Verwendung gem�� 4.1 Beschaffungsrichtlinien.");
    jTextPane1.setFont(new java.awt.Font("Dialog", 0, 12));
    oben.setLayout(null);
    oben.setBounds(new Rectangle(11, 0, 563, 290));
    jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel9.setText("Kapitel");
    jLabel9.setBounds(new Rectangle(185, 190, 53, 15));
    labTitel.setFont(new java.awt.Font("Dialog", 0, 12));
    labTitel.setHorizontalAlignment(SwingConstants.CENTER);
    labTitel.setBounds(new Rectangle(311, 190, 52, 15));
    labUT.setFont(new java.awt.Font("Dialog", 0, 12));
    labUT.setHorizontalAlignment(SwingConstants.CENTER);
    labUT.setBounds(new Rectangle(383, 179, 40, 15));
    jLabel22.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel22.setText("Kostenart:");
    jLabel22.setBounds(new Rectangle(7, 163, 124, 15));
    jLabel8.setBounds(new Rectangle(302, 11, 75, 15));
    jLabel8.setText("Inventar-Nr.:");
    jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel12.setBounds(new Rectangle(4, 266, 394, 15));
    jLabel12.setText("Angebote / Preisvergleiche wurden bei folgenden Firmen eingeholt:");
    jLabel12.setFont(new java.awt.Font("Dialog", 1, 12));
    jScrollPane2.setBorder(BorderFactory.createEmptyBorder());
    jScrollPane2.setBorder(null);
    jScrollPane2.setBounds(new Rectangle(11, 289, 548, 93));
    jLabel13.setBounds(new Rectangle(404, 267, 131, 15));
    jLabel13.setText("(Angebote bitte beilegen!)");
    jLabel13.setFont(new java.awt.Font("Dialog", 0, 10));
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel11.setText("Titel");
    jLabel11.setBounds(new Rectangle(286, 190, 40, 15));
    labKapitel.setFont(new java.awt.Font("Dialog", 0, 12));
    labKapitel.setHorizontalAlignment(SwingConstants.CENTER);
    labKapitel.setBounds(new Rectangle(226, 190, 55, 15));
    jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel7.setText("zu belastender Haushaltstitel:");
    jLabel7.setBounds(new Rectangle(9, 190, 185, 15));
    labBestellNr.setText(" BestellNr.");
    labBestellNr.setBounds(new Rectangle(7, 9, 68, 23));
    labBestellNr.setFont(new java.awt.Font("Dialog", 0, 12));
    tpVerwendungszweck.setFont(new java.awt.Font("Dialog", 0, 12));
    cbDrittelMittel.setFont(new java.awt.Font("Dialog", 0, 11));
    cbDrittelMittel.setText("Dritt- / HBFG-Mittel: Die Bestellung entspricht dem vorgelegten Finanzierungs- " +
    "/ Ausstattungsplan");
    cbDrittelMittel.setBounds(new Rectangle(3, 114, 498, 23));
    cbDrittelMittel.setFocusable(false);
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setText("UT");
    jLabel10.setBounds(new Rectangle(365, 190, 33, 15));
    jLabel24.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel24.setText("Kostenstelle:");
    jLabel24.setBounds(new Rectangle(7, 134, 82, 15));
    tfErsatzText.setText("");
		tfErsatzText.setBounds(new Rectangle(-1, 7, 293, 21));
    tfInventarNr.setText("");
		tfInventarNr.setBounds(new Rectangle(377, 8, 77, 21));
    buDrucken.setBounds(new Rectangle(349, 578, 112, 25));
		buDrucken.setText("Vorschau");
    buTitel.setBounds(new Rectangle(438, 186, 109, 21));
    buTitel.setActionCommand("buTitel");
    buTitel.setText("Titelauswahl");
    oben.add(jLabel22, null);
    oben.add(jLabel22, null);
    oben.add(jLabel22, null);
    buttonGroup2.add(rbErstbeschaffung);
    buttonGroup2.add(rbErsatz);
    buttonGroup3.add(rbAngebotGuenstig);
    buttonGroup3.add(rbAuftragGrund);
    oben.add(jLabel22, null);
    oben.add(jLabel22, null);
    oben.add(jLabel22, null);
		jTabbedPane1.setSelectedComponent(panelZweck);
    jTabbedPane1.setSelectedIndex(0);

  }

	public void actionPerformed(ActionEvent e) {
		String error = "";
		if ( e.getSource() == rbErsatz ) {
			panelErsatz.setVisible(true);
		}else if ( e.getSource() == rbErstbeschaffung ) {
			panelErsatz.setVisible(false);
		}else if ( e.getSource() == rbAngebotGuenstig ) {
			tpAuftragGrund.setVisible(false);
		}else if ( e.getSource() == rbAuftragGrund ) {
			tpAuftragGrund.setVisible(true);
		}else if ( e.getSource() == buFBKonto ) {
		  if(cbInstitut.getSelectedItem() != null){
				AuswahlFBKonto fbKontoAuswahl = new AuswahlFBKonto(this, (Institut)cbInstitut.getSelectedItem(), frame, true);
		  }else{
		    JOptionPane.showMessageDialog( this,"Kein Institut selektiert !","Warnung",JOptionPane.ERROR_MESSAGE);
		  }
		}else if ( e.getSource() == buTitel ) {
	    if(fbKonto != null){
        AuswahlZVKonto kontoAuswahl = new AuswahlZVKonto(this, fbKonto, true, frame);
	    }else
	      JOptionPane.showMessageDialog( this,"Kein FBKonto selektiert !","Warnung",JOptionPane.ERROR_MESSAGE);
		}else if ( e.getSource() == buAddAngebot ) {
			DefaultTableModel dtm = (DefaultTableModel)tableAngebote.getModel();
			AngebotFrame angebot = new AngebotFrame(this);
			angebot.setVisible(true);
		}else if ( e.getSource() == buDrucken ) {
			printBestellung();
		}else if ( e.getSource() == buBestellen ) {
			error = checkData();
			if(error.equals("")){
				saveOrder(1);;
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
						getComponent(0), "Soll die Bestellung wirklich gel�scht werden ? ", "Warnung",
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

	/**
	 * speichert bzw. bestellt eine Bestellung
	 * @param phase - 0 nur speichern, 1 bestellen
	 */
	private void saveOrder(int phase){
		DefaultTableModel dtm = (DefaultTableModel)tableAngebote.getModel();
		ArrayList angebote = new ArrayList();

		for(int i = 0; i < dtm.getRowCount(); i++){
			Angebot angebot = (Angebot)dtm.getValueAt(i, 1);

			// ein Angebot hat mindestens eine Position auch wenn er nur die Summe hat
			if(angebot.getPositionen().size() == 0){
				ArrayList positionen = new ArrayList();
				Position position = new Position("", angebot.getSumme(), 1, 0, 0, bestellung.getFbkonto().getInstitut());
				positionen.add(position);
				angebot.setPositionen(positionen);
			}
			angebot.setAngenommen(((Boolean)(dtm.getValueAt(i, 4))).booleanValue());
			angebote.add(angebot);
		}

		java.util.Date datum = (java.util.Date)tfBestellDatum.getValue();
		Date sqlDate = new Date(datum.getTime());

		StandardBestellung newBestellung = new StandardBestellung(angebote, (Kostenart)cbKostenart.getSelectedItem(),
																				rbErsatz.isSelected(), tfErsatzText.getText(), tfInventarNr.getText(), tpVerwendungszweck.getText(),
																				cbDrittelMittel.isSelected(), tpAuftragGrund.getText(), tpBemerkungen.getText(),
																				tfReferenzNr.getText(), sqlDate, frame.getBenutzer(),
																				'0', "", (Benutzer)cbAuftraggeber.getSelectedItem(), (Benutzer)cbEmpfaenger.getSelectedItem(),
																				zvTitel, fbKonto, 0, 0);
		if(phase == 1){
			newBestellung.setPhase('1');
			float betrag = ((Angebot)newBestellung.getAngebote().get(newBestellung.getAngenommenesAngebot())).getSumme();
			newBestellung.setVerbindlichkeiten(betrag);
			newBestellung.setBestellwert(betrag);
			newBestellung.getZvtitel().setVormerkungen(betrag);
			newBestellung.getFbkonto().setVormerkungen(betrag);
		}

		int id = 0;
		try {
			if(bestellung != null){
				newBestellung.setId(bestellung.getId());
				newBestellung.setBesteller(bestellung.getBesteller());
				newBestellung.setHuel(bestellung.getHuel());
				id = bestellung.getId();
				frame.getApplicationServer().setBestellung(frame.getBenutzer(), bestellung, newBestellung, true);
			}else{
				id = frame.getApplicationServer().addBestellung(newBestellung);
			}

			if(phase == 1){
				buDrucken.setEnabled(true);
				buDelete.setEnabled(false);
				buBestellen.setEnabled(false);
				buSpeichern.setEnabled(false);
			}
			bestellung = frame.applicationServer.getStandardBestellung(id);
		} catch (ApplicationServerException e) {
				MessageDialogs.showDetailMessageDialog(this, "Warnung", e.getMessage(), e.getNestedMessage(), MessageDialogs.WARNING_ICON);
				e.printStackTrace();
		}
	}

	private void printBestellung(){
		PrintSTDBestellung beilage = new PrintSTDBestellung(this.bestellung, frame);
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


	/**
	 * l�dt User f�r die Auswahl des Auftraggebers und Empf�ngers. Falls der User ein Institutssdmin ist,
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
	 * l�dt alle Kostenarten in die Combobox
	 * @author robert
	 */
	private void loadKostenart(){
		try {
			Kostenart[] kostenarten = frame.getApplicationServer().getKostenarten();

			if(kostenarten != null){
				cbKostenart.removeAllItems();
			  for(int i = 0; i < kostenarten.length; i++){
					cbKostenart.addItem(kostenarten[i]);
			  }
			}
		} catch (ApplicationServerException e) {
			e.printStackTrace();
		}
	}

	private void setOrderData(){
		buDelete.setEnabled(true);
		tfReferenzNr.setText(bestellung.getReferenznr());
		tfBestellDatum.setValue(bestellung.getDatum());
		cbAuftraggeber.setSelectedItem(bestellung.getAuftraggeber());
		cbEmpfaenger.setSelectedItem(bestellung.getEmpfaenger());
		
		if(bestellung.getFbkonto()!=null) cbInstitut.setSelectedItem(bestellung.getFbkonto().getInstitut());

		if(bestellung.getErsatzbeschaffung())
			rbErsatz.doClick();
		else
		 	rbErstbeschaffung.doClick();
		tfInventarNr.setText(bestellung.getInventarNr());
		tfErsatzText.setText(bestellung.getErsatzbeschreibung());
		if(rbErsatz.isSelected()){
			panelErsatz.setVisible(true);
		}
		tpVerwendungszweck.setText(bestellung.getVerwendungszweck());
		cbDrittelMittel.setSelected(bestellung.getPlanvorgabe());


		ArrayList angebote = bestellung.getAngebote();
		for(int i = 0; i < angebote.size(); i++){
			Angebot angebot = (Angebot)((Angebot)angebote.get(i)).clone();

			if(angebot.getAngenommen()){
				angebotNr = i+1;
				buBestellen.setEnabled(true);
			}
			insertAngebot(angebot, -1);
		}
		
		if(bestellung.getPhase() != '0')
			buDrucken.setEnabled(true);

		rbAuftragGrund.setSelected(!bestellung.getBegruendung().equals(""));
		if(rbAuftragGrund.isSelected())
			tpAuftragGrund.setVisible(true);
		tpAuftragGrund.setText(bestellung.getBegruendung());
		tpBemerkungen.setText(bestellung.getBemerkung());
		setFBKonto(bestellung.getFbkonto());
		setZVKonto(bestellung.getZvtitel());
	}

	private void setData(){
		loadUsers();
		loadInstituts();
		loadKostenart();
	}


	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == cbInstitut){
			fbKonto = null;
			buBestellen.setSelected(false);
			zvTitel = null;
			tfFBKonto.setText("");
			labKoSt.setText("KoSt: " + ((Institut)cbInstitut.getSelectedItem()).getKostenstelle());
			labKapitel.setText("");
			labTitel.setText("");
			labUT.setText("");
		}
	}

	/**
	 * f�gt ein neues oder tauscht ein Angebot in der Angebotstabelle aus
	 * @param angebot das eingef�gt werden soll
	 * @param angebotNr gibt an, an welcher Stelle das Angebot ausgetauscht werden soll, -1 -> neues Anbebot,
	 * 				sonst Position des Anbegots in der Angebotstabelle
	 */
	public void insertAngebot(Angebot angebot, int angebotNr){
		DefaultTableModel dtm = (DefaultTableModel)tableAngebote.getModel();

		if(angebotNr == -1){
			Object[] o = {new Integer(dtm.getRowCount()+1), angebot, angebot.getDatum(), new Float(angebot.getSumme()), new Boolean(angebot.getAngenommen())};
			dtm.addRow(o);
		}else{
			Object[] o = {new Integer(angebotNr + 1), angebot, angebot.getDatum(), new Float(angebot.getSumme()), new Boolean(angebot.getAngenommen())};

			dtm.insertRow(angebotNr, o);
			dtm.removeRow(angebotNr + 1);
		}

		dtm.fireTableRowsInserted(dtm.getRowCount(),dtm.getRowCount());
	}

	/* (Kein Javadoc)
	 * @see gui.FBKontoSelectable#setFBKonto(dbObjects.FBUnterkonto)
	 */
	public void setFBKonto(FBUnterkonto fbKonto) {
		this.fbKonto = fbKonto;
		if (fbKonto != null){
			tfFBKonto.setText(fbKonto.toString());
			buBestellen.setSelected(true);
		}
	}

}