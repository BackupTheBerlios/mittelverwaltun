package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
import java.text.NumberFormat;
import java.util.ArrayList;


public class BestellungKlein extends JInternalFrame implements ActionListener, ItemListener, PropertyChangeListener, ZVKontoSelectable {
	JLabel labFBKonto = new JLabel();
	JComboBox comboFBKonto = new JComboBox();
	JLabel labZVKonto = new JLabel();
	JComboBox comboZVKonto = new JComboBox();
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
	BestellungKleinTable tableBelege = new BestellungKleinTable();
	JButton buAddRow = new JButton();
	JLabel labGesamtText = new JLabel();
	JPanel panelProjekt = new JPanel();
	JTextField tfProjektNr = new JTextField();
	JLabel labProjektNr = new JLabel();
	JTextField tfDatum = new JTextField();
	JLabel labDatum = new JLabel();
	MainFrame frame;

	public BestellungKlein(MainFrame frame) {
		super("Auszahlungsanordnung erstellen");
		this.frame = frame;
		this.setClosable(true);
		this.setIconifiable(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setBounds(50,50,530,555);
		setData();
				
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}

//		labUser.setText(frame.getBenutzer().getName() + ", " + frame.getBenutzer().getVorname());

		buAbbrechen.addActionListener(this);
		buAbbrechen.setIcon(Functions.getCloseIcon(getClass()));
		buBestellen.addActionListener(this);
		buBestellen.setIcon(Functions.getBestellIcon(getClass()));
		buDrucken.addActionListener(this);
		buDrucken.setIcon(Functions.getPrintIcon(getClass()));
		buAddRow.addActionListener(this);
		buAddRow.setIcon(Functions.getExpandIcon(getClass()));
		tableBelege.addPropertyChangeListener(this);
  }


  private void loadInstituts(){
//	  try {
////		TODO Admin durch die Aktivität austauschen
//			if(frame.getBenutzer().getRolle().getBezeichnung().equals("Admin")){
//				Institut[] instituts = frame.getApplicationServer().getInstitutes();
//			
//			  if(instituts != null){
//				  cbInstitut.removeAllItems();
//					 for(int i = 0; i < instituts.length; i++){
//						  cbInstitut.addItem(instituts[i]);
//					 }
//			  }
//			}else{
//				Institut institut = frame.getBenutzer().getKostenstelle();
//				
//				cbInstitut.removeAllItems();
//				cbInstitut.addItem(institut);
//			}
//	  } catch (ApplicationServerException e) {
//		  e.printStackTrace();
//	  }
  }

	private void loadHauptkonten() {
//	  try {
//		  Institut institut = null;
//		  // TODO Admin durch die Aktivität austauschen
//		  if(frame.getBenutzer().getRolle().getBezeichnung().equals("Admin"))
//			  institut = (Institut)cbInstitut.getSelectedItem();
//		  else
//			  institut = frame.getBenutzer().getKostenstelle();
//
//
//		  if(institut != null){
//			  ArrayList hauptKonten = frame.getApplicationServer().getFBHauptkonten(institut);
//
//			  if(hauptKonten != null){
//				  cbKostenstelle.removeAllItems();
//					 for(int i = 0; i < hauptKonten.size(); i++){
//
//						  cbKostenstelle.addItem(hauptKonten.get(i));
//					 }
//			  }
//		  }
//	  } catch (ApplicationServerException e) {
//		  e.printStackTrace();
//	  }
	}

	private void setData(){
		loadInstituts();
		loadHauptkonten();
	}

	void insertZVKonto() {
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

	private void jbInit() throws Exception {
		labFBKonto.setText("FBKonto");
		labFBKonto.setBounds(new Rectangle(10, 40, 120, 15));
		this.getContentPane().setLayout(null);
		labZVKonto.setText("ZVKonto");
		labZVKonto.setBounds(new Rectangle(10, 70, 120, 15));
		buBestellen.setBounds(new Rectangle(10, 490, 120, 25));
		buBestellen.setText("Bestellen");
		buDrucken.setBounds(new Rectangle(200, 490, 120, 25));
		buDrucken.setText("Drucken");
		buAbbrechen.setBounds(new Rectangle(390, 490, 120, 25));
		buAbbrechen.setText("Abbrechen");
		labAuszahlungAn.setText("Auszahlung an");
		labAuszahlungAn.setBounds(new Rectangle(10, 10, 120, 15));
		panelVerwendung.setBorder(BorderFactory.createLineBorder(Color.black));
		panelVerwendung.setBounds(new Rectangle(10, 335, 500, 80));
		panelVerwendung.setLayout(null);
		labBegruendung.setText("Verwendung oder Begründung");
		labBegruendung.setBounds(new Rectangle(6, 6, 200, 15));
		panelKartei.setBorder(BorderFactory.createLineBorder(Color.black));
		panelKartei.setBounds(new Rectangle(10, 414, 500, 30));
		panelKartei.setLayout(null);
		labMaterial.setText("In Material/Geräte-Kartei");
		labMaterial.setBounds(new Rectangle(6, 6, 145, 15));
		labLaborNr.setText("Labor, Nr.");
		labLaborNr.setBounds(new Rectangle(241, 6, 70, 15));
		tfKartei.setText("");
		tfKartei.setBounds(new Rectangle(151, 5, 85, 21));
		labEingetragen.setText("eingetragen.");
		labEingetragen.setBounds(new Rectangle(401, 6, 90, 15));
		tfKarteiNr.setText("");
		tfKarteiNr.setBounds(new Rectangle(311, 5, 85, 21));
		panelTitelVerzNr.setBorder(BorderFactory.createLineBorder(Color.black));
		panelTitelVerzNr.setBounds(new Rectangle(10, 443, 500, 30));
		panelTitelVerzNr.setLayout(null);
		tfTitelVerzNr.setText("");
		tfTitelVerzNr.setBounds(new Rectangle(116, 5, 150, 21));
		labTitelVerzNr.setText("Titel. Verz. Nr.");
		labTitelVerzNr.setBounds(new Rectangle(6, 7, 110, 15));
		panelTable.setBorder(BorderFactory.createLineBorder(Color.black));
		panelTable.setBounds(new Rectangle(10, 140, 500, 200));
		panelTable.setLayout(null);
		labGesamt.setText("");
		labGesamt.setBounds(new Rectangle(351, 166, 140, 15));
		buAddRow.setBounds(new Rectangle(6, 166, 180, 22));
		buAddRow.setText("Zeile hinzufügen");
		labGesamtText.setText("Gesamt");
		labGesamtText.setBounds(new Rectangle(271, 166, 80, 15));
		panelProjekt.setBorder(BorderFactory.createLineBorder(Color.black));
		panelProjekt.setBounds(new Rectangle(10, 102, 500, 40));
		panelProjekt.setLayout(null);
		tfProjektNr.setText("");
		tfProjektNr.setBounds(new Rectangle(86, 9, 150, 21));
		labProjektNr.setText("Projekt-Nr.");
		labProjektNr.setBounds(new Rectangle(6, 11, 80, 15));
		tfDatum.setText("");
		tfDatum.setBounds(new Rectangle(341, 9, 150, 21));
		labDatum.setText("Datum");
		labDatum.setBounds(new Rectangle(261, 11, 80, 15));
		scrollBelege.setBounds(new Rectangle(5, 5, 490, 150));
		tpVerwendung.setBounds(new Rectangle(6, 26, 490, 50));
		comboZVKonto.setBounds(new Rectangle(130, 70, 380, 21));
		comboFBKonto.setBounds(new Rectangle(130, 40, 380, 21));
		comboBenutzer.setBounds(new Rectangle(130, 10, 380, 21));
		this.getContentPane().add(labFBKonto, null);
		this.getContentPane().add(comboFBKonto, null);
		this.getContentPane().add(labZVKonto, null);
		this.getContentPane().add(comboZVKonto, null);
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
		scrollBelege.add(tableBelege, null);
	}
	
	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == buAddRow ) {
			DefaultTableModel dtm = (DefaultTableModel)tableBelege.getModel();
			Object[] o = {new Integer(1),"","", new Float(0)};

			dtm.addRow(o);
			dtm.fireTableRowsInserted(dtm.getRowCount(),dtm.getRowCount());

		} else if ( e.getSource() == buDrucken ) {
			PrinterJob pJob = PrinterJob.getPrinterJob();
			pJob.setJobName("Kleinbestellung");

			PageFormat pf = new PageFormat();
			Paper paper = pf.getPaper() ;
			paper.setImageableArea(35,90,560,712) ;
			paper.setSize(595,842);
			pf.setPaper(paper);
			BestellungKleinPrint printFrame = new BestellungKleinPrint();
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
		}else if ( e.getSource() == buAbbrechen ) {
			this.dispose();
		}
	}

	public void setZVKonto(ZVUntertitel zvTitel) {

	}

	public void propertyChange(PropertyChangeEvent e) {
//		if(e.getSource() == cbKostenstelle){
//			FBHauptkonto kostenstelle = (FBHauptkonto)cbKostenstelle.getSelectedItem();
//
//			if(kostenstelle != null){
//				labKapitel.setText("");
//				labTitel.setText("");
//				labUT.setText("");
//			}
//		}else if(e.getSource() == cbInstitut){
//			loadHauptkonten();
//		}else if(e.getSource() == table){
//			DefaultTableModel dtm = (DefaultTableModel)table.getModel();
//		  float sum = 0;
//		  float price = 0;
//
//		  for(int i = 0; i < table.getRowCount(); i++){
//				price = ((Float)dtm.getValueAt(i, 3)).floatValue();
//				sum += price;
//		  }
//		  labGesamt.setText(NumberFormat.getCurrencyInstance().format(sum));
//		}
	}


	public void itemStateChanged(ItemEvent e) {
//		if(e.getSource() == cbKostenstelle){
//			FBHauptkonto kostenstelle = (FBHauptkonto)cbKostenstelle.getSelectedItem();
//			labUser.setText("Hallo" + frame.getBenutzer().getName() + ", " + frame.getBenutzer().getVorname());
//			if(kostenstelle != null){
//				labKapitel.setText("");
//				labTitel.setText("");
//				labUT.setText("");
//			}
//		}else if(e.getSource() == cbInstitut){
//			loadHauptkonten();
//		}
	}
}