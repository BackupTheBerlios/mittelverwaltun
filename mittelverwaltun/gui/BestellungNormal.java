package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dbObjects.Angebot;
import dbObjects.FBHauptkonto;
import dbObjects.Institut;
import dbObjects.ZVTitel;
import dbObjects.ZVUntertitel;

import applicationServer.ApplicationServer;
import applicationServer.ApplicationServerException;
import applicationServer.CentralServer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.Naming;
import java.util.ArrayList;


public class BestellungNormal extends JInternalFrame implements ActionListener, ItemListener, ZVKontoSelectable, PropertyChangeListener{

	JTabbedPane jTabbedPane1 = new JTabbedPane();
  JLabel jLabel4 = new JLabel();
  JTextPane tpAdresse = new JTextPane();
  JPanel bestellungPanel = new JPanel();
  JTextPane tpLieferadresse = new JTextPane();
  JTextPane tpBemerkungen = new JTextPane();
  JPanel panelBestellung = new JPanel();
  JLabel jLabel5 = new JLabel();
  JLabel labKoSt = new JLabel();
  JTextPane tpAuftragGrund = new JTextPane();
  JTextPane jTextPane1 = new JTextPane();
  JPanel beilagePanel = new JPanel();
  JPanel oben = new JPanel();
  JLabel jLabel14 = new JLabel();
  JLabel jLabel9 = new JLabel();
  JLabel labTitel = new JLabel();
  JLabel labUT = new JLabel();
  BestellungBeilageTable tableAngebote = new BestellungBeilageTable(this);
  JPanel jPanel1 = new JPanel();
  JLabel jLabel22 = new JLabel();
  JLabel jLabel8 = new JLabel();
  JLabel jLabel12 = new JLabel();
  JScrollPane jScrollPane2 = new JScrollPane();
  JLabel jLabel13 = new JLabel();
  JPanel unten = new JPanel();
  JLabel jLabel11 = new JLabel();
  JLabel labKapitel = new JLabel();
  JLabel jLabel7 = new JLabel();
  JLabel labBestellNr = new JLabel();
  JTextPane tpBegruendung = new JTextPane();
  JCheckBox cbDrittelMittel = new JCheckBox();
  JLabel jLabel15 = new JLabel();
  JLabel jLabel10 = new JLabel();
  JLabel jLabel23 = new JLabel();
  JLabel jLabel24 = new JLabel();
  JLabel jLabel25 = new JLabel();
  JTextField tfErsatzText = new JTextField();
  JTextField tfInventarNr = new JTextField();
  JTextField tfAngebotNr = new JTextField();
  JButton buDrucken = new JButton();

  MainFrame frame;
  JButton buTitel = new JButton();
  JLabel label2 = new JLabel();
  JComboBox cbKostenstelle = new JComboBox();
  JLabel labInstitut = new JLabel();
  JComboBox cbInstitut = new JComboBox();
  JButton buAddAngebot = new JButton();
  ArrayList angebote = new ArrayList();
  JRadioButton rbInvestitionen = new JRadioButton();
  JRadioButton rbReparatur = new JRadioButton();
  JRadioButton rbVerbrauchersmaterial = new JRadioButton();
  ButtonGroup buttonGroup1 = new ButtonGroup();
  JRadioButton rbErstbeschaffung = new JRadioButton();
  JRadioButton rbErsatz = new JRadioButton();
  ButtonGroup buttonGroup2 = new ButtonGroup();
  JRadioButton rbAngebotGuenstig = new JRadioButton();
  JRadioButton rbAuftragGrund = new JRadioButton();
  ButtonGroup buttonGroup3 = new ButtonGroup();
  JPanel panelErsatz = new JPanel();
  JPanel panelBeilage = new JPanel();
  JScrollPane scrollBeilage = new JScrollPane();
  JButton buBeenden = new JButton();
  JButton buSpeichern = new JButton();
  JButton buBestellen = new JButton();

  public BestellungNormal(MainFrame frame) {
  	this.frame = frame;
		this.setClosable(true);
		this.setIconifiable(true);

		buDrucken.addActionListener(this);
		buTitel.addActionListener(this);
		buAddAngebot.addActionListener(this);
		buBeenden.addActionListener(this);
		buBeenden.setIcon(Functions.getCloseIcon(this.getClass()));
		buSpeichern.addActionListener(this);
		buBestellen.addActionListener(this);

		cbKostenstelle.addItemListener(this);
		cbKostenstelle.addPropertyChangeListener(this);
		cbInstitut.addItemListener(this);
		cbInstitut.addPropertyChangeListener(this);
		rbErsatz.addActionListener(this);
		rbErstbeschaffung.addActionListener(this);
		rbAngebotGuenstig.addActionListener(this);
		rbAuftragGrund.addActionListener(this);
		setData();


    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }



//	TODO Admin durch die Aktivität austauschen
		if(!frame.getBenutzer().getRolle().getBezeichnung().equals("Admin")){
			cbInstitut.setVisible(false);
			labInstitut.setVisible(false);
		}
		panelErsatz.setVisible(false);
		tpAuftragGrund.setVisible(false);
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
  }

  private String checkData(){
  	String error = "";

  	error += (tpAdresse.getText().equals("") ? " - Firma \n" : "");
  	error += (tpLieferadresse.getText().equals("") ? " - Lieferadresse \n" : "");

  	return error;
  }

  public Angebot getAngebot(int row){
  	return (Angebot)angebote.get(row);
  }

  public static void main(String[] args) {
		 MainFrame test = new MainFrame("FBMittelverwaltung");
		 try{
			 CentralServer server = (CentralServer)Naming.lookup("//localhost/mittelverwaltung");
			 ApplicationServer applicationServer = server.getMyApplicationServer();
			 test.setApplicationServer(applicationServer);
			 PasswordEncrypt pe = new PasswordEncrypt();
			 String psw = pe.encrypt(new String("r.driesner").toString());
			 test.setBenutzer(applicationServer.login("r.driesner", psw));
		   test.setBounds(100,100,800,900);
			 test.setExtendedState(Frame.MAXIMIZED_BOTH);
			 
			 test.setJMenuBar( new MainMenu( test ) );
			 BestellungNormal bestellung = new BestellungNormal(test);
			 test.addChild(bestellung);
			 test.show();
				bestellung.show();
		 }catch(Exception e){
				 System.out.println(e);
		 }
  }

  private void jbInit() throws Exception {
		this.setSize(640,550);
		tpLieferadresse.setEditable(true);
    tpLieferadresse.setText("");
    tpBemerkungen.setEditable(true);
    tpBemerkungen.setText("");
    cbKostenstelle.setBounds(new Rectangle(84, 88, 345, 21));
    labInstitut.setFont(new java.awt.Font("Dialog", 0, 12));
    labInstitut.setText("Institut:");
    labInstitut.setBounds(new Rectangle(9, 70, 50, 15));
    cbInstitut.setBounds(new Rectangle(84, 64, 345, 21));
    buAddAngebot.setBounds(new Rectangle(1, 0, 148, 21));
    buAddAngebot.setText("Angebot hinzufügen");
    tpBegruendung.setText("");
    rbInvestitionen.setFont(new java.awt.Font("Dialog", 0, 12));
    rbInvestitionen.setSelected(true);
    rbInvestitionen.setText("Investitionen");
    rbInvestitionen.setBounds(new Rectangle(78, 114, 112, 23));
    rbReparatur.setFont(new java.awt.Font("Dialog", 0, 12));
    rbReparatur.setText("Reparatur");
    rbReparatur.setBounds(new Rectangle(196, 114, 91, 23));
    rbVerbrauchersmaterial.setFont(new java.awt.Font("Dialog", 0, 12));
    rbVerbrauchersmaterial.setText("Verbrauchersmaterial");
    rbVerbrauchersmaterial.setBounds(new Rectangle(303, 114, 185, 23));
    rbErstbeschaffung.setFont(new java.awt.Font("Dialog", 0, 12));
    rbErstbeschaffung.setSelected(true);
    rbErstbeschaffung.setText("Erstbeschaffung");
    rbErstbeschaffung.setBounds(new Rectangle(9, 172, 144, 23));
    rbErsatz.setFont(new java.awt.Font("Dialog", 0, 12));
    rbErsatz.setText("Ersatz für:");
    rbErsatz.setBounds(new Rectangle(9, 200, 82, 23));
    rbAngebotGuenstig.setFont(new java.awt.Font("Dialog", 0, 12));
    rbAngebotGuenstig.setSelected(true);
    rbAngebotGuenstig.setText("das preisgünstigste und wirtschaftlichste Angebot abgegeben hat.");
    rbAngebotGuenstig.setBounds(new Rectangle(7, 52, 418, 23));
    rbAuftragGrund.setFont(new java.awt.Font("Dialog", 0, 12));
    rbAuftragGrund.setText("aus folgenden Grund bevorzugt wurden:");
    rbAuftragGrund.setBounds(new Rectangle(7, 75, 302, 23));
    panelErsatz.setBounds(new Rectangle(90, 187, 461, 35));
    panelErsatz.setLayout(null);
    panelBeilage.setLayout(null);
    scrollBeilage.setBounds(new Rectangle(1, 4, 609, 430));
    tfAngebotNr.setEnabled(false);
    tfAngebotNr.setEditable(false);
    buBeenden.setBounds(new Rectangle(514, 484, 112, 25));
    buBeenden.setText("Beenden");
    buSpeichern.setBounds(new Rectangle(174, 484, 112, 25));
    buSpeichern.setText("Speichern");
    buBestellen.setBounds(new Rectangle(4, 484, 112, 25));
    buBestellen.setText("Bestellen");
    unten.add(tpAuftragGrund, null);
    unten.add(jLabel14, null);
    unten.add(tfAngebotNr, null);
    unten.add(jLabel15, null);
    unten.add(buAddAngebot, null);
    unten.add(rbAuftragGrund, null);
    unten.add(rbAngebotGuenstig, null);
    jPanel1.add(jTextPane1, null);
    jPanel1.add(tpBegruendung, null);
    jPanel1.add(cbDrittelMittel, null);
    panelBeilage.add(jScrollPane2, null);
    panelBeilage.add(unten, null);
    jScrollPane2.getViewport().add(tableAngebote, null);
    oben.add(panelErsatz, null);
    panelBestellung.add(tpBemerkungen, null);
    panelBestellung.add(label2, null);
    panelBestellung.add(tpAdresse, null);
    panelBestellung.add(jLabel4, null);
    panelBestellung.add(jLabel5, null);
    panelBestellung.add(tpLieferadresse, null);
    this.getContentPane().add(buSpeichern, null);
    this.getContentPane().add(buDrucken, null);
    this.getContentPane().add(buBeenden, null);
    this.getContentPane().add(buBestellen, null);
    this.getContentPane().add(jTabbedPane1, null);

    this.setTitle("Bestellung");
    this.getContentPane().setLayout(null);
    jTabbedPane1.setBounds(new Rectangle(4, 9, 623, 468));
    jLabel4.setText("363,50");
    jLabel4.setBounds(new Rectangle(484, 9, 79, 15));
    jLabel4.setText("58,16");
    jLabel4.setBounds(new Rectangle(484, 29, 79, 15));
    jLabel4.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel4.setText("Bemerkungen");
    jLabel4.setBounds(new Rectangle(2, 116, 159, 15));
    tpAdresse.setBackground(Color.white);
    tpAdresse.setEditable(true);
    tpAdresse.setText("");
    tpAdresse.setBounds(new Rectangle(2, 23, 287, 87));
    bestellungPanel.setLayout(null);
    tpLieferadresse.setBounds(new Rectangle(2, 215, 572, 82));
    tpBemerkungen.setFont(new java.awt.Font("Dialog", 0, 12));
    tpBemerkungen.setBounds(new Rectangle(2, 131, 572, 62));
    panelBestellung.setBounds(new Rectangle(2, 2, 580, 278));
    panelBestellung.setForeground(Color.black);
    panelBestellung.setLayout(null);
    panelBestellung.setBounds(new Rectangle(3, 3, 580, 313));
    jLabel5.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel5.setText("Lieferanschrift:");
    jLabel5.setBounds(new Rectangle(2, 197, 202, 15));
    labKoSt.setText("  KoSt");
    labKoSt.setBounds(new Rectangle(432, 36, 112, 23));
    labKoSt.setFont(new java.awt.Font("Dialog", 0, 12));
    labKoSt.setToolTipText("");
    labKoSt.setBorder(BorderFactory.createLineBorder(Color.black));
    tpAuftragGrund.setFont(new java.awt.Font("Dialog", 0, 12));
    tpAuftragGrund.setText("");
    tpAuftragGrund.setBounds(new Rectangle(25, 97, 516, 51));
    jTextPane1.setBounds(new Rectangle(8, 5, 531, 40));
    jTextPane1.setText("Begründung für die Notwendigkeit der Beschaffung und der zweckentsprechenden " +
    "Verwendung gemäß 4.1 Beschaffungsrichtlinien.");
    jTextPane1.setFont(new java.awt.Font("Dialog", 0, 12));
    beilagePanel.setBounds(new Rectangle(3, 4, 562, 728));
		beilagePanel.setLayout(null);
    oben.setLayout(null);
    oben.setBounds(new Rectangle(14, 0, 549, 434));
    jLabel14.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel14.setText("Der Auftrag wird der oben unter");
    jLabel14.setBounds(new Rectangle(7, 29, 179, 15));
    jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel9.setText("Kapitel");
    jLabel9.setBounds(new Rectangle(185, 151, 53, 15));
    labTitel.setFont(new java.awt.Font("Dialog", 0, 12));
    labTitel.setHorizontalAlignment(SwingConstants.CENTER);
    labTitel.setBounds(new Rectangle(311, 151, 52, 15));
    labUT.setFont(new java.awt.Font("Dialog", 0, 12));
    labUT.setHorizontalAlignment(SwingConstants.CENTER);
    labUT.setBounds(new Rectangle(383, 151, 40, 15));
    jPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
    jPanel1.setBounds(new Rectangle(0, 238, 544, 168));
    jPanel1.setLayout(null);
    jLabel22.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel22.setText("Kostenart:");
    jLabel22.setBounds(new Rectangle(9, 118, 124, 15));
    jLabel8.setBounds(new Rectangle(304, 13, 75, 15));
    jLabel8.setText("Inventar-Nr.:");
    jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel12.setBounds(new Rectangle(6, 411, 394, 15));
    jLabel12.setText("Angebote / Preisvergleiche wurden bei folgenden Firmen eingeholt:");
    jLabel12.setFont(new java.awt.Font("Dialog", 1, 12));
    jScrollPane2.setBorder(BorderFactory.createEmptyBorder());
    jScrollPane2.setBorder(null);
    jScrollPane2.setBounds(new Rectangle(12, 433, 548, 93));
    jLabel13.setBounds(new Rectangle(406, 412, 131, 15));
    jLabel13.setText("(Angebote bitte beilegen!)");
    jLabel13.setFont(new java.awt.Font("Dialog", 0, 10));
    unten.setBounds(new Rectangle(12, 527, 545, 132));
    unten.setLayout(null);
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel11.setText("Titel");
    jLabel11.setBounds(new Rectangle(286, 151, 40, 15));
    labKapitel.setFont(new java.awt.Font("Dialog", 0, 12));
    labKapitel.setHorizontalAlignment(SwingConstants.CENTER);
    labKapitel.setBounds(new Rectangle(226, 151, 55, 15));
    jLabel7.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel7.setText("zu belastender Haushaltstitel:");
    jLabel7.setBounds(new Rectangle(9, 151, 185, 15));
    labBestellNr.setText(" Best.");
    labBestellNr.setBounds(new Rectangle(303, 36, 129, 23));
    labBestellNr.setFont(new java.awt.Font("Dialog", 0, 12));
    labBestellNr.setBorder(BorderFactory.createLineBorder(Color.black));
    tpBegruendung.setFont(new java.awt.Font("Dialog", 0, 12));
    tpBegruendung.setBounds(new Rectangle(8, 46, 531, 90));
    cbDrittelMittel.setFont(new java.awt.Font("Dialog", 0, 11));
    cbDrittelMittel.setText("Dritt- / HBFG-Mittel: Die Bestellung entspricht dem vorgelegten Finanzierungs- " +
    "/ Ausstattungsplan");
    cbDrittelMittel.setBounds(new Rectangle(3, 140, 498, 23));
    cbDrittelMittel.setFocusable(false);
    jLabel15.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel15.setText("genannten Firma erteilt, da diese Firma");
    jLabel15.setBounds(new Rectangle(240, 29, 221, 15));
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setText("UT");
    jLabel10.setBounds(new Rectangle(365, 151, 33, 15));
    jLabel23.setFont(new java.awt.Font("Dialog", 1, 15));
    jLabel23.setText("Fachhoschschule Mannheim - Hochschule für Technik und Gestaltung");
    jLabel23.setBounds(new Rectangle(20, 8, 513, 15));
    jLabel24.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel24.setText("Kostenstelle:");
    jLabel24.setBounds(new Rectangle(9, 94, 82, 15));
    jLabel25.setBounds(new Rectangle(13, 34, 234, 31));
    jLabel25.setText("Beilage zur Bestellung");
    jLabel25.setFont(new java.awt.Font("Dialog", 3, 20));
    tfErsatzText.setText("");
		tfErsatzText.setBounds(new Rectangle(1, 8, 293, 21));
    tfInventarNr.setText("");
		tfInventarNr.setBounds(new Rectangle(379, 8, 77, 21));
    tfAngebotNr.setText("");
		tfAngebotNr.setBounds(new Rectangle(185, 23, 47, 21));
    buDrucken.setBounds(new Rectangle(344, 484, 112, 25));
		buDrucken.setText("Drucken");
    buTitel.setBounds(new Rectangle(422, 147, 110, 20));
    buTitel.setActionCommand("buTitel");
    buTitel.setText("Titelauswahl");
		label2.setFont(new java.awt.Font("Dialog", 1, 12));
		label2.setText("Firma");
		label2.setBounds(new Rectangle(2, 4, 84, 15));
    oben.add(jLabel22, null);
    oben.add(jLabel22, null);
    oben.add(jLabel22, null);
    oben.add(jLabel22, null);
    oben.add(jLabel22, null);
    jTabbedPane1.add(beilagePanel, "Beilage zur Bestellung");
    beilagePanel.add(scrollBeilage, null);
    jTabbedPane1.add(bestellungPanel, "Bestellung");
    bestellungPanel.add(panelBestellung, null);
    scrollBeilage.getViewport().add(panelBeilage, null);
    buttonGroup1.add(rbInvestitionen);
    buttonGroup1.add(rbReparatur);
    buttonGroup1.add(rbVerbrauchersmaterial);
    buttonGroup2.add(rbErstbeschaffung);
    buttonGroup2.add(rbErsatz);
    buttonGroup3.add(rbAngebotGuenstig);
    buttonGroup3.add(rbAuftragGrund);
    panelErsatz.add(tfInventarNr, null);
    panelErsatz.add(tfErsatzText, null);
    panelErsatz.add(jLabel8, null);
    oben.add(rbInvestitionen, null);
    oben.add(rbReparatur, null);
    oben.add(rbVerbrauchersmaterial, null);
    oben.add(rbErstbeschaffung, null);
    oben.add(rbErsatz, null);
    oben.add(jLabel22, null);
    oben.add(cbKostenstelle, null);
    oben.add(jLabel24, null);
    oben.add(labInstitut, null);
    oben.add(cbInstitut, null);
    oben.add(jLabel13, null);
    oben.add(jLabel12, null);
    oben.add(labKoSt, null);
    oben.add(jLabel23, null);
    oben.add(jLabel25, null);
    oben.add(labBestellNr, null);
    oben.add(jLabel9, null);
    oben.add(labKapitel, null);
    oben.add(jLabel11, null);
    oben.add(labTitel, null);
    oben.add(jLabel10, null);
    oben.add(labUT, null);
    oben.add(buTitel, null);
    oben.add(jLabel7, null);
    oben.add(jPanel1, null);
    oben.add(jLabel22, null);
    panelBeilage.add(oben, null);
		panelBeilage.setBounds(new Rectangle(617, 156, 548, 678));
		panelBeilage.setSize(548, 678);
		panelBeilage.setPreferredSize(new Dimension(548, 678));

  }
	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == rbErsatz ) {
			panelErsatz.setVisible(true);
		}else if ( e.getSource() == rbErstbeschaffung ) {
			panelErsatz.setVisible(false);
		}else if ( e.getSource() == rbAngebotGuenstig ) {
			tpAuftragGrund.setVisible(false);
		}else if ( e.getSource() == rbAuftragGrund ) {
			tpAuftragGrund.setVisible(true);
		}else if ( e.getSource() == buTitel ) {
			AuswahlZVKonto kontoAuswahl;
			kontoAuswahl = new AuswahlZVKonto(this, (FBHauptkonto)cbKostenstelle.getSelectedItem(), false, frame);
			kontoAuswahl.show();
		}else if ( e.getSource() == buAddAngebot ) {
			DefaultTableModel dtm = (DefaultTableModel)tableAngebote.getModel();
			AngebotFrame angebot = new AngebotFrame(this);
			angebot.show();
		}else if ( e.getSource() == buDrucken ) {
			printBestellung();
		}else if ( e.getSource() == buBestellen ) {
			bestellen();
		}else if ( e.getSource() == buSpeichern ) {
			saveBestellung();
		}else if ( e.getSource() == buBeenden ) {
			dispose();
		}
	}

	private void saveBestellung(){

	}

	private void bestellen(){

	}

	private void printBestellung(){
		PrinterJob pJob = PrinterJob.getPrinterJob();

		PageFormat pf = new PageFormat();
		Paper paper = pf.getPaper() ;
		paper.setImageableArea(35,90,560,712) ;
		paper.setSize(595,842);
		pf.setPaper(paper);
		BestellungVordruckPrint bestellung = new BestellungVordruckPrint();
		bestellung.show();
		bestellung.setVisible(false);
		BestellungBeilagePrint beilage = new BestellungBeilagePrint();
		beilage.show();
		beilage.setVisible(false);

		pJob.setJobName("Bestellung");
		Book book = new Book();
		book.append(bestellung, pf);
		if(pJob.printDialog()){
			try{
				pJob.setPageable(book);
				pJob.print();
			}catch(PrinterException pexc){
				System.out.println("Fehler beim Drucken");
			}
		}
		pJob.cancel();
		if(pJob.isCancelled()){
			bestellung.dispose();
		}

		pJob.setJobName("Beilage zur Bestellung");
		book = new Book();
		book.append(beilage, pf);
		if(pJob.printDialog()){
			try{
				pJob.setPageable(book);
				pJob.print();
			}catch(PrinterException pexc){
				System.out.println("Fehler beim Drucken");
			}
		}
		pJob.cancel();
		if(pJob.isCancelled()){
			beilage.dispose();
		}
	}

	public void setZVKonto(ZVUntertitel zvTitel) {
		if(zvTitel.getZVTitel() != null){
			labKapitel.setText(zvTitel.getZVTitel().getZVKonto().getKapitel());
			labTitel.setText(zvTitel.getTitel());
			labUT.setText(zvTitel.getUntertitel());
		}else{
			labKapitel.setText(((ZVTitel)zvTitel).getZVKonto().getKapitel());
			labTitel.setText(zvTitel.getTitel());
			labUT.setText("");
		}
	}

	private void loadInstituts(){
		try {
			Institut[] instituts = frame.getApplicationServer().getInstitutes();

			if(instituts != null){
				cbInstitut.removeAllItems();
				  for(int i = 0; i < instituts.length; i++){
						cbInstitut.addItem(instituts[i]);
				  }
			}
		} catch (ApplicationServerException e) {
			e.printStackTrace();
		}
	}

	private void loadHauptkonten(){
		try {
			Institut institut = null;
			// TODO Admin durch die Aktivität austauschen
			if(frame.getBenutzer().getRolle().getBezeichnung().equals("Admin"))
				institut = (Institut)cbInstitut.getSelectedItem();
			else
				institut = frame.getBenutzer().getKostenstelle();


			if(institut != null){
				ArrayList hauptKonten = frame.getApplicationServer().getFBHauptkonten(institut);

				if(hauptKonten != null){
					cbKostenstelle.removeAllItems();
					  for(int i = 0; i < hauptKonten.size(); i++){

							cbKostenstelle.addItem(hauptKonten.get(i));
					  }
				}
			}
		} catch (ApplicationServerException e) {
			e.printStackTrace();
		}
	}

	private void setData(){
//		TODO Admin durch die Aktivität austauschen
		if(frame.getBenutzer().getRolle().getBezeichnung().equals("Admin"))
			loadInstituts();

		loadHauptkonten();
	}


	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == cbKostenstelle){
			FBHauptkonto kostenstelle = (FBHauptkonto)cbKostenstelle.getSelectedItem();

			if(kostenstelle != null){
				labKoSt.setText("KoSt: " + kostenstelle.getInstitut().getKostenstelle());
				labKapitel.setText("");
				labTitel.setText("");
				labUT.setText("");
			}
		}else if(e.getSource() == cbInstitut){
			loadHauptkonten();
		}
	}

	public void insertAngebot(Angebot angebot, int angebotNr){
		DefaultTableModel dtm = (DefaultTableModel)tableAngebote.getModel();
		Object[] o = {new Integer(dtm.getRowCount()+1), angebot, angebot.getDatum(), new Float(angebot.getSumme()), new Boolean(false)};

		if(angebotNr == -1)
			dtm.addRow(o);
		else{
			dtm.removeRow(angebotNr);
			dtm.insertRow(angebotNr, o);
		}


		dtm.fireTableRowsInserted(dtm.getRowCount(),dtm.getRowCount());
	}

	public void propertyChange(PropertyChangeEvent e) {
		if(e.getSource() == cbKostenstelle){
			FBHauptkonto kostenstelle = (FBHauptkonto)cbKostenstelle.getSelectedItem();

			if(kostenstelle != null){
				labKoSt.setText("KoSt: " + kostenstelle.getInstitut().getKostenstelle());
				labKapitel.setText("");
				labTitel.setText("");
				labUT.setText("");
			}
		}else if(e.getSource() == cbInstitut){
			loadHauptkonten();
		}

	}

}