package gui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import dbObjects.*;
import applicationServer.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.beans.*;
import java.rmi.*;
import java.sql.Date;
import java.text.*;


public class BestellungKlein extends JInternalFrame implements ActionListener, ItemListener, 
																PropertyChangeListener, ZVKontoSelectable {
	
	JLabel labTextKostenstelle = new JLabel();
	JLabel labTextKapitel = new JLabel();
	JButton buBestellen = new JButton();
	JButton buDrucken = new JButton();
	JButton buAbbrechen = new JButton();
	JComboBox comboBenutzer = new JComboBox();
	JLabel labAuszahlungAn = new JLabel();
	JPanel panelVerwendung = new JPanel();
	JLabel labBegruendung = new JLabel();
	JTextPane tpVerwendung = new JTextPane();
	JPanel panelKartei = new JPanel();
	JLabel labMaterial = new JLabel();
	JLabel labLaborNr = new JLabel();
	JTextField tfKartei = new JTextField();
	JLabel labEingetragen = new JLabel();
	JTextField tfKarteiNr = new JTextField();
	JPanel panelTitelVerzNr = new JPanel();
	JTextField tfTitelVerzNr = new JTextField();
	JLabel labTitelVerzNr = new JLabel();
	JPanel panelTable = new JPanel();
	JScrollPane scrollBelege = new JScrollPane();
	JLabel labGesamt = new JLabel();
	BestellungKleinTable tableBelege;
	JButton buAddRow = new JButton();
	JLabel labGesamtText = new JLabel();
	JPanel panelProjekt = new JPanel();
	JTextField tfProjektNr = new JTextField();
	JLabel labProjektNr = new JLabel();
	JFormattedTextField tfDatum = new JFormattedTextField(DateFormat.getDateInstance());
	JLabel labDatum = new JLabel();
	JLabel labKostenstelle = new JLabel();
	JLabel labTextHauptkonto = new JLabel();
	JLabel labHauptkonto = new JLabel();
	JLabel labTextUnterkonto = new JLabel();
	JLabel labUnterkonto = new JLabel();
	JButton butFBKontoAuswahl = new JButton();
	JLabel labKapitel = new JLabel();
	JLabel labTextTitel = new JLabel();
	JLabel labTitel = new JLabel();
	JLabel labTextUntertitel = new JLabel();
	JLabel labUntertitel = new JLabel();
	JButton butZVTitelAuswahl = new JButton();
	JButton butStornieren = new JButton();
	MainFrame frame;
	FBUnterkonto fbKonto;
	ZVUntertitel zvTitel;
	KleinBestellung bestellung;

	/**
	 * Konstruktor zum Durchühren einer neun Auszahlungsanordnung. 
	 * @param frame = MainFrame in dem das JInternalFrame liegt und welches den ApplicationServer besitzt.
	 * @author w.flat
	 */
	public BestellungKlein(MainFrame frame) {
		super("Auszahlungsanordnung Erstellen");
		this.frame = frame;
		this.setClosable(true);
		this.setIconifiable(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setBounds(50,50,630,555);
		
		try {
			tableBelege = new BestellungKleinTable(this, frame.getApplicationServer().getFirmen());
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}

		buAbbrechen.addActionListener(this);
		buAbbrechen.setIcon(Functions.getCloseIcon(getClass()));
		buBestellen.addActionListener(this);
		buBestellen.setIcon(Functions.getBestellIcon(getClass()));
		buDrucken.addActionListener(this);
		buDrucken.setIcon(Functions.getPrintIcon(getClass()));
		buAddRow.addActionListener(this);
		buAddRow.setIcon(Functions.getExpandIcon(getClass()));
		butFBKontoAuswahl.addActionListener(this);
		butFBKontoAuswahl.setIcon(Functions.getFindIcon(getClass()));
		butZVTitelAuswahl.addActionListener(this);
		butZVTitelAuswahl.setIcon(Functions.getFindIcon(getClass()));

		butStornieren.setVisible(false);
		tfDatum.setValue(new Date(System.currentTimeMillis()));
		
		loadUsers();
	}
	
	/**
	 * Konstruktor zum Anzeigen und Stornieren einer Auszahlunganordnung. 
	 * @param frame = welches den ApplicationServer besitzt. 
	 * @param bestellung = KleinBestellung die angezeigt werden soll und welche storniert werden kann. 
	 * @author w.flat
	 */
	public BestellungKlein(MainFrame frame, KleinBestellung bestellung) {
		super("Auszahlungsanordnung Anzeigen/Stornieren");
		this.frame = frame;
		
		try{
			this.bestellung = (KleinBestellung)frame.getApplicationServer().getKleinbestellungen().get(0);
		} catch(Exception e) {
			this.bestellung = null;
		}
		
		this.setClosable(true);
		this.setIconifiable(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setBounds(50,50,630,555);
		
		try {
			tableBelege = new BestellungKleinTable(this.bestellung.getBelege());
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}

		buAbbrechen.addActionListener(this);
		buAbbrechen.setIcon(Functions.getCloseIcon(getClass()));
		butStornieren.addActionListener(this);
		butStornieren.setIcon(Functions.getDelIcon(getClass()));
		buDrucken.addActionListener(this);
		buDrucken.setIcon(Functions.getPrintIcon(getClass()));
		buAddRow.addActionListener(this);
		buAddRow.setIcon(Functions.getExpandIcon(getClass()));
		buAddRow.setEnabled(false);
		butFBKontoAuswahl.addActionListener(this);
		butFBKontoAuswahl.setIcon(Functions.getFindIcon(getClass()));
		butFBKontoAuswahl.setEnabled(false);
		butZVTitelAuswahl.addActionListener(this);
		butZVTitelAuswahl.setIcon(Functions.getFindIcon(getClass()));
		butZVTitelAuswahl.setEnabled(false);
		buBestellen.setVisible(false);
		
		// Beschriften der Labels
		setFBKonto(this.bestellung.getFbkonto());
		setZVKonto(this.bestellung.getZvtitel());
		labGesamt.setText(NumberFormat.getCurrencyInstance().format(this.bestellung.getBestellwert()));
		// Beschriften der TextFelder
		comboBenutzer.insertItemAt(this.bestellung.getAuftraggeber(), 0);
		comboBenutzer.setSelectedIndex(0);
		comboBenutzer.setEnabled(false);
		tfProjektNr.setText(this.bestellung.getProjektNr());
		tfProjektNr.setEnabled(false);
		tfDatum.setValue(this.bestellung.getDatum());
		tfDatum.setEnabled(false);
		tpVerwendung.setText(this.bestellung.getVerwendungszweck());
		tpVerwendung.setEnabled(false);
		tfKartei.setText(this.bestellung.getLabor());
		tfKartei.setEnabled(false);
		tfKarteiNr.setText(this.bestellung.getKartei());
		tfKarteiNr.setEnabled(false);
		tfTitelVerzNr.setText(this.bestellung.getVerzeichnis());
		tfTitelVerzNr.setEnabled(false);
	}

	/**
	 * Methode zum Laden aller möglichen User, die zur Zeit die Kleinbestellung durchführen können.
	 * @author w.flat
	 */
	private void loadUsers() {
		comboBenutzer.removeAllItems();		// Alle Einträge in der ComboBox löschen
		if(frame == null)	// Kein Frame vorhanden
			return;
		if(frame.getBenutzer() == null)		// Benutzer nicht vorhanden
			return;
		
		try {
			if(frame.getBenutzer().getRolle().getBezeichnung().equalsIgnoreCase("Admin")) {
				// Benutzer für die man die Kleinbestellung durchführen kann
				Benutzer[] users = frame.getApplicationServer().getUsers();
				for(int i = 0; i < users.length; i++) {
					if(users[i] == null)	// Kein Benutzer vorhanden
						continue;
					comboBenutzer.addItem(users[i]);				// Neuen Benutzer in die ComboBox einfügen
					if(users[i].equals(frame.getBenutzer()))		// Den Benutzer selektieren
						comboBenutzer.setSelectedItem(users[i]);
				}
			} else if(frame.getBenutzer().getRolle().getBezeichnung().equalsIgnoreCase("Institutsadmin")) {
				// Benutzer für die man die Kleinbestellung durchführen kann
				Benutzer[] users = frame.getApplicationServer().getUsers(frame.getBenutzer().getKostenstelle());
				for(int i = 0; i < users.length; i++) {
					if(users[i] == null)	// Kein Benutzer vorhanden
						continue;
					comboBenutzer.addItem(users[i]);				// Neuen Benutzer in die ComboBox einfügen
					if(users[i].equals(frame.getBenutzer()))		// Den Benutzer selektieren
						comboBenutzer.setSelectedItem(users[i]);
				}
			} else {
				comboBenutzer.addItem(frame.getBenutzer());			// Neuen Benutzer in die ComboBox einfügen
				comboBenutzer.setSelectedItem(frame.getBenutzer());	// Den Benutzer selektieren
			}
		} catch(ApplicationServerException e) {
		}
	}
	
	/**
	 * Methode zum Löschen der FBKonto-Labels und ZVTitel-Labels.
	 * @author w.flat
	 */
	private void clearLabels() {
		labKostenstelle.setText("");
		labHauptkonto.setText("");
		labUnterkonto.setText("");
		labKapitel.setText("");
		labTitel.setText("");
		labUntertitel.setText("");
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
			 test.setBounds(100,100,700,700);
			 test.setExtendedState(Frame.MAXIMIZED_BOTH);

			 test.setJMenuBar( new MainMenu( test ) );
			 BestellungKlein bestellungKlein = new BestellungKlein(test);
			 test.addChild(bestellungKlein);
			 test.show();
			 bestellungKlein.show();
		 }catch(Exception e){
				 System.out.println(e);
		 }
	}
	
	/**
	 * Initialisierung der Graphischen Oberfläche. 
	 * @throws Exception
	 * @author w.flat
	 */
	private void jbInit() throws Exception {
		labTextKostenstelle.setText("Kostenstelle");
		labTextKostenstelle.setBounds(new Rectangle(10, 40, 80, 15));
		this.getContentPane().setLayout(null);
		labTextKapitel.setText("Kapitel");
		labTextKapitel.setBounds(new Rectangle(10, 70, 80, 15));
		buBestellen.setBounds(new Rectangle(10, 490, 150, 25));
		buBestellen.setText("Bestellen");
		buDrucken.setBounds(new Rectangle(235, 490, 150, 25));
		buDrucken.setText("Drucken");
		buAbbrechen.setBounds(new Rectangle(460, 490, 150, 25));
		buAbbrechen.setText("Abbrechen");
		labAuszahlungAn.setText("Auszahlung an");
		labAuszahlungAn.setBounds(new Rectangle(10, 10, 120, 15));
		panelVerwendung.setBorder(BorderFactory.createLineBorder(Color.black));
		panelVerwendung.setBounds(new Rectangle(10, 335, 600, 80));
		panelVerwendung.setLayout(null);
		labBegruendung.setText("Verwendung oder Begründung");
		labBegruendung.setBounds(new Rectangle(6, 6, 200, 15));
		panelKartei.setBorder(BorderFactory.createLineBorder(Color.black));
		panelKartei.setBounds(new Rectangle(10, 414, 600, 30));
		panelKartei.setLayout(null);
		labMaterial.setText("In Material/Geräte-Kartei");
		labMaterial.setBounds(new Rectangle(6, 6, 160, 15));
		labLaborNr.setAlignmentX((float) 0.0);
		labLaborNr.setText(" -Labor, Nr.");
		labLaborNr.setBounds(new Rectangle(281, 6, 80, 15));
		tfKartei.setText("");
		tfKartei.setBounds(new Rectangle(171, 5, 100, 21));
		labEingetragen.setText("eingetragen.");
		labEingetragen.setBounds(new Rectangle(481, 6, 90, 15));
		tfKarteiNr.setText("");
		tfKarteiNr.setBounds(new Rectangle(371, 5, 100, 21));
		panelTitelVerzNr.setBorder(BorderFactory.createLineBorder(Color.black));
		panelTitelVerzNr.setBounds(new Rectangle(10, 443, 600, 30));
		panelTitelVerzNr.setLayout(null);
		tfTitelVerzNr.setText("");
		tfTitelVerzNr.setBounds(new Rectangle(131, 5, 150, 21));
		labTitelVerzNr.setText("Titl. Verz. Nr.");
		labTitelVerzNr.setBounds(new Rectangle(6, 7, 120, 15));
		panelTable.setBorder(BorderFactory.createLineBorder(Color.black));
		panelTable.setBounds(new Rectangle(10, 140, 600, 200));
		panelTable.setLayout(null);
		labGesamt.setText("");
		labGesamt.setBounds(new Rectangle(451, 166, 140, 15));
		buAddRow.setBounds(new Rectangle(6, 166, 180, 22));
		buAddRow.setText("Zeile hinzufügen");
		labGesamtText.setText("Gesamt");
		labGesamtText.setBounds(new Rectangle(371, 166, 80, 15));
		panelProjekt.setBorder(BorderFactory.createLineBorder(Color.black));
		panelProjekt.setBounds(new Rectangle(10, 102, 600, 40));
		panelProjekt.setLayout(null);
		tfProjektNr.setText("");
		tfProjektNr.setBounds(new Rectangle(86, 9, 150, 21));
		labProjektNr.setText("Projekt-Nr.");
		labProjektNr.setBounds(new Rectangle(6, 11, 80, 15));
		tfDatum.setText("");
		tfDatum.setBounds(new Rectangle(441, 9, 150, 21));
		labDatum.setText("Datum");
		labDatum.setBounds(new Rectangle(361, 11, 80, 15));
		labKostenstelle.setText("");
		labKostenstelle.setBounds(new Rectangle(90, 40, 60, 15));
		labTextHauptkonto.setText("Hauptkonto");
		labTextHauptkonto.setBounds(new Rectangle(160, 40, 80, 15));
		labHauptkonto.setText("");
		labHauptkonto.setBounds(new Rectangle(240, 40, 40, 15));
		labTextUnterkonto.setText("Unterkonto");
		labTextUnterkonto.setBounds(new Rectangle(290, 40, 80, 15));
		labUnterkonto.setText("");
		labUnterkonto.setBounds(new Rectangle(370, 40, 60, 15));
		butFBKontoAuswahl.setBounds(new Rectangle(440, 40, 170, 25));
		butFBKontoAuswahl.setText("FBKonto-Auswahl");
		labKapitel.setText("");
		labKapitel.setBounds(new Rectangle(90, 70, 60, 15));
		labTextTitel.setText("Titel");
		labTextTitel.setBounds(new Rectangle(160, 70, 80, 15));
		labTitel.setText("");
		labTitel.setBounds(new Rectangle(240, 70, 60, 15));
		labTextUntertitel.setText("Untertitel");
		labTextUntertitel.setBounds(new Rectangle(310, 70, 80, 15));
		labUntertitel.setText("");
		labUntertitel.setBounds(new Rectangle(390, 70, 40, 15));
		butZVTitelAuswahl.setBounds(new Rectangle(440, 70, 170, 25));
		butZVTitelAuswahl.setText("ZVTitel-Auswahl");
		butStornieren.setBounds(new Rectangle(10, 490, 150, 25));
		butStornieren.setOpaque(true);
		butStornieren.setText("Stornieren");
		scrollBelege.setBounds(new Rectangle(5, 5, 590, 150));
		tpVerwendung.setBounds(new Rectangle(6, 26, 590, 50));
		comboBenutzer.setBounds(new Rectangle(130, 10, 310, 21));
		this.getContentPane().add(labTextKostenstelle, null);
		this.getContentPane().add(labTextKapitel, null);
		this.getContentPane().add(buBestellen, null);
		this.getContentPane().add(buDrucken, null);
		this.getContentPane().add(buAbbrechen, null);
		this.getContentPane().add(comboBenutzer, null);
		this.getContentPane().add(labAuszahlungAn, null);
		this.getContentPane().add(panelVerwendung, null);
		panelVerwendung.add(labBegruendung, null);
		panelVerwendung.add(tpVerwendung, null);
		this.getContentPane().add(panelKartei, null);
		panelKartei.add(labMaterial, null);
		panelKartei.add(labLaborNr, null);
		panelKartei.add(tfKartei, null);
		panelKartei.add(labEingetragen, null);
		panelKartei.add(tfKarteiNr, null);
		this.getContentPane().add(panelTitelVerzNr, null);
		panelTitelVerzNr.add(tfTitelVerzNr, null);
		panelTitelVerzNr.add(labTitelVerzNr, null);
		this.getContentPane().add(panelTable, null);
		panelTable.add(scrollBelege, null);
		panelTable.add(labGesamt, null);
		panelTable.add(buAddRow, null);
		panelTable.add(labGesamtText, null);
		this.getContentPane().add(panelProjekt, null);
		panelProjekt.add(tfProjektNr, null);
		panelProjekt.add(labProjektNr, null);
		panelProjekt.add(tfDatum, null);
		panelProjekt.add(labDatum, null);
		this.getContentPane().add(labKostenstelle, null);
		this.getContentPane().add(labTextHauptkonto, null);
		this.getContentPane().add(labHauptkonto, null);
		this.getContentPane().add(labTextUnterkonto, null);
		this.getContentPane().add(labUnterkonto, null);
		this.getContentPane().add(butFBKontoAuswahl, null);
		this.getContentPane().add(labKapitel, null);
		this.getContentPane().add(labTextTitel, null);
		this.getContentPane().add(labTitel, null);
		this.getContentPane().add(labTextUntertitel, null);
		this.getContentPane().add(labUntertitel, null);
		this.getContentPane().add(butZVTitelAuswahl, null);
		this.getContentPane().add(butStornieren, null);

		scrollBelege.getViewport().add(tableBelege, null);		
		comboBenutzer.addItemListener(this);
	}
	
	/**
	 * Reaktion auf die Button-Ereignise.
	 * @author w.flat
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equalsIgnoreCase("Löschen")) {
			int answer = JOptionPane.showConfirmDialog(
						getComponent(0),
						"Soll der Beleg mit der Beleg-Nr. = "
						+ ((DefaultTableModel)tableBelege.getModel()).getValueAt(tableBelege.getSelectedRow(), 0)
						+ "\ngelöscht werden ? ",
						"Warnung",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null);
			if(answer == 0){
				tableBelege.delPresRaw();
			}
		} else if( e.getSource() == butZVTitelAuswahl ) {
			AuswahlZVKonto kontoAuswahl = new AuswahlZVKonto(this, fbKonto, true, frame);
			kontoAuswahl.show();
		} else if( e.getSource() == butFBKontoAuswahl ) {
			FBKontoAuswahlDialog dialog = new FBKontoAuswahlDialog(frame, this, true, (Benutzer)comboBenutzer.getSelectedItem());
		} else if( e.getSource() == buAddRow ) {
			tableBelege.addRaw();
		} else if ( e.getSource() == buDrucken ) {
			String error = getErrorString();
			if(!error.equalsIgnoreCase("")) {	// Wenn Fehler im Formular aufgetreten sind
				error = "Folgende Fehler sind aufgetreten:\n" + error;
				// Fehlermeldung ausgeben
				JOptionPane.showMessageDialog( this, error,	"Fehler !", JOptionPane.ERROR_MESSAGE );
				return;		// Srung aus der Methode
			}
			// Wenn keine Fehler beim Formular, dann Drucken
			PrinterJob pJob = PrinterJob.getPrinterJob();
			pJob.setJobName("Kleinbestellung");

			PageFormat pf = new PageFormat();
			Paper paper = pf.getPaper() ;
			paper.setImageableArea(35,90,560,712) ;
			paper.setSize(595,842);
			pf.setPaper(paper);
			BestellungKleinPrint printFrame = new BestellungKleinPrint(getKleinBestellung());
			printFrame.show();
			printFrame.setVisible(false);

			Book book = new Book();
			book.append(printFrame, pf);

			if(pJob.printDialog()){
				try{
					pJob.setPrintable(printFrame, pf);
					pJob.print();
				}catch(PrinterException pexc){
					System.out.println("Fehler beim Drucken");
				}
			}
			pJob.cancel();
			if(pJob.isCancelled())
				printFrame.dispose();
		} else if ( e.getSource() == buAbbrechen ) {
			this.dispose();
		} else if( e.getSource() == buBestellen ) {
			String error = getErrorString();
			if(!error.equalsIgnoreCase("")) {		// Fehler im Bestell-Formular
				error = "Folgende Fehler sind aufgetreten:\n" + error;
				JOptionPane.showMessageDialog( this, error,	"Fehler !", JOptionPane.ERROR_MESSAGE );
			} else {	// Sonst Bestellung durchführen
				bestellung = getKleinBestellung();
				try {
					bestellung.setId(frame.getApplicationServer().addKleinbestellung(bestellung));
					buBestellen.setEnabled(false);
				} catch(ApplicationServerException exc) {
					error = "Fehler bein Generieren der Bestellung:\n" + exc.toString();
					JOptionPane.showMessageDialog( this, error,	"Fehler !", JOptionPane.ERROR_MESSAGE );
				}
			}
		} else if( e.getSource() == butStornieren ) {
			int answer = JOptionPane.showConfirmDialog(
						getComponent(0),
						"Soll die Bestellung wirklich gelöscht werden ? ",
						"Warnung",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null);
			if(answer == 0) {	// Die Bestellung löschen
				try {
					frame.getApplicationServer().delKleinbestellung(bestellung);
					butStornieren.setEnabled(false);
					buDrucken.setEnabled(false);
				} catch(ApplicationServerException exc) {
					String error = "Fehler beim Löschen der Bestellung:\n" + exc.toString();
					JOptionPane.showMessageDialog( this, error,	"Fehler !", JOptionPane.ERROR_MESSAGE );
				}
			}			
		}
	}
	
	/**
	 * Eine Kleinbestellung generieren aus dem Formular. 
	 * @return Kleinbestellung, die generiert wurde. 
	 * @author w.flat
	 */
	private KleinBestellung getKleinBestellung() {
	    java.util.Date datum = (java.util.Date)tfDatum.getValue();
		Date sqlDate = new Date(datum.getTime());

		return new KleinBestellung(0, sqlDate, frame.getBenutzer(), (Benutzer)comboBenutzer.getSelectedItem(), 
									zvTitel, fbKonto, tableBelege.getSum(), tfProjektNr.getText(), tpVerwendung.getText(),
									tfKartei.getText(), tfKarteiNr.getText(), tfTitelVerzNr.getText(), tableBelege.getBelege() );
	}

	/**
	 * Wenn in der Combo-Box-Benutzer anderer Eintrag ausgewählt wurde. 
	 * @author w.flat
	 */
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == comboBenutzer) {
			clearLabels();
		}
	}
	
	/**
	 * Ermitteln aller Fehler beim Bestellungsformular. 
	 * @return String mit Fehlern
	 * @author w.flat
	 */
	private String getErrorString() {
		String error = "";
		
		if(tfProjektNr.getText().equalsIgnoreCase("")) {
			error += " - Die Projektnummer darf nicht leer sein.\n";
		}
		if(tfKartei.getText().equalsIgnoreCase("")) {
			error += " - Die Laborbezeichnung darf nicht leer sein.\n";
		}
		if(tfKarteiNr.getText().equalsIgnoreCase("")) {
			error += " - Die Karteinummer darf nicht leer sein.\n";
		}
		if(tfTitelVerzNr.getText().equalsIgnoreCase("")) {
			error += " - Die Titl.Verz.Nr. darf nicht leer sein.\n";
		}
		if(tpVerwendung.getText().equalsIgnoreCase("")) {
			error += " - Das Verwendungsfeld darf nicht leer sein.\n";
		}
		if(fbKonto == null) {
			error += " - Es wurde kein FBKonto angegeben.\n";
		}
		if(zvTitel == null) {
			error += " - Es wurde kein ZVTitel angegeben.\n";
		}
		error += tableBelege.getErrorString();

		return error;
	}

	/**
	 * Das FBKonto für die Bestellung festlegen.  
	 * @author w.flat
	 */
	public void setFBKonto(FBUnterkonto fbKonto) {
		clearLabels();
		this.fbKonto = fbKonto;
		this.zvTitel = null;
		labKostenstelle.setText(this.fbKonto.getInstitut().getKostenstelle());
		labHauptkonto.setText(this.fbKonto.getHauptkonto());
		labUnterkonto.setText(this.fbKonto.getUnterkonto());
	}

	/**
	 * Den ZVTitel für die Bestellung festlegen. 
	 * @author w.flat
	 */
	public void setZVKonto(ZVUntertitel zvTitel) {
		this.zvTitel = zvTitel;
		if(zvTitel instanceof ZVTitel)
			labKapitel.setText(((ZVTitel)this.zvTitel).getZVKonto().getKapitel());
		else
			labKapitel.setText(this.zvTitel.getZVTitel().getZVKonto().getKapitel());
		labTitel.setText(this.zvTitel.getTitel());
		labUntertitel.setText(this.zvTitel.getUntertitel());
	}

	/**
	 * Reaktion auf die Veränderungen im Betrag Feld. 
	 * @author w.flat
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getSource() == tableBelege) {
			labGesamt.setText(NumberFormat.getCurrencyInstance().format(tableBelege.getSum()));
		}
	}
}

/**
 * Dialog zur Auswahl, eines FBKontos für eine Kleinbestellung. 
 * @author w.flat
 * 03.03.2005
 */
class FBKontoAuswahlDialog extends JDialog implements ActionListener, TreeSelectionListener {
	JPanel panel1 = new JPanel();
	JScrollPane scrollFirmen = new JScrollPane();
	FBKontenTree treeKonten;
	JButton butSelect = new JButton();
	JButton butAbbrechen = new JButton();
	BestellungKlein intFrame;

	/**
	 * Dialog für die Auswahl eines FBKontos, für eine Kleinbestellung. 
	 * @param frame = welches blockiert werden soll. 
	 * @param intFrame = Frame, welches die Methode setFBKonto hat. 
	 * @param modal = Flag ob der frame blockiert werden soll.
	 * @param user = für den die FBKonten geladen werden sollen. 
	 * @author w.flat
	 */
	public FBKontoAuswahlDialog(MainFrame frame, BestellungKlein intFrame, boolean modal, Benutzer user) {
		super(frame, "FBKonto Auswahl", modal);
		this.intFrame = intFrame;
		
		try {
			treeKonten = new FBKontenTree(this, "FBKonten für Kleinbestellungen");
			jbInit();
		}
		catch(Exception ex) {
		  ex.printStackTrace();
		}
		
		butAbbrechen.addActionListener(this);
		butAbbrechen.setIcon(Functions.getCloseIcon(getClass()));
		butSelect.addActionListener(this);
		butSelect.setIcon(Functions.getAddIcon(getClass()));
		butSelect.setEnabled(false);
		
		try { 
			treeKonten.loadInstituts(frame.getApplicationServer().getFBKontenForUser(user));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		this.setBounds(50, 50, 332, 285);
		this.show();
	}
	
	/**
	 * Anzeigen des Dialogs und laden der Konten in den Baum. 
	 * @param inst = Institut mit den FBKonten, die ausgewählt werden können. 
	 * @author w.flat
	 */
	private void showFBKontoAuswahl(Institut[] inst) {
		this.show();
		treeKonten.loadInstituts(inst);
	}

	/**
	 * Initialisierung des Dialogs. 
	 * @throws Exception
	 * @author w.flat
	 */ 
	private void jbInit() throws Exception {
		panel1.setLayout(null);
		panel1.setBorder(BorderFactory.createEtchedBorder());
		butSelect.setBounds(new Rectangle(12, 222, 140, 25));
		butSelect.setText("Auswählen");
		butAbbrechen.setBounds(new Rectangle(172, 222, 140, 25));
		butAbbrechen.setToolTipText("");
		butAbbrechen.setText("Abbrechen");
		scrollFirmen.setBounds(new Rectangle(12, 12, 300, 200));
		getContentPane().add(panel1, BorderLayout.CENTER);
		panel1.add(scrollFirmen, null);
		scrollFirmen.getViewport().add(treeKonten, null);
		panel1.add(butSelect, null);
		panel1.add(butAbbrechen, null);
	}
	
	/**
	 * Überprüfung, ob ein FBKonto ausgewählt wurde, bei dem das Flag-Kleinbestellungen gesetzt ist. 
	 * @author w.flat
	 */
	private void checkKonto() {
		if(treeKonten.fbHauptkontoIsSelected()) {
			butSelect.setEnabled(treeKonten.getFBHauptkonto().getKleinbestellungen());
		} else if(treeKonten.fbUnterkontoIsSelected()) {
			butSelect.setEnabled(treeKonten.getFBUnterkonto().getKleinbestellungen());
		} else {
			butSelect.setEnabled(false);
		}
	}
	
	/**
	 * Reaktion auf die Button-Ereignise.
	 * @author w.flat
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == butSelect) {
			if(treeKonten.fbHauptkontoIsSelected()) {
				intFrame.setFBKonto(treeKonten.getFBHauptkonto());
			} else if(treeKonten.fbUnterkontoIsSelected()) {
				intFrame.setFBKonto(treeKonten.getFBUnterkonto());
			}
			this.dispose();
		} else if(e.getSource() == butAbbrechen) {
			this.dispose();
		}
	}
	
	/**
	 * Reaktion auf die Baum-Ereignise.
	 * @author w.flat
	 */
	public void valueChanged(TreeSelectionEvent e) {
		treeKonten.checkSelection( e );
		checkKonto();		
	}
}

