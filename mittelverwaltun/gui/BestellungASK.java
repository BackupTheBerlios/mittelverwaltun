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


public class BestellungASK extends JInternalFrame implements ActionListener, PropertyChangeListener, ItemListener, ZVKontoSelectable {
  JTabbedPane jTabbedPane1 = new JTabbedPane();
  JPanel panelBestellung = new JPanel();
  JPanel panelBeilage = new JPanel();
  JLabel jLabel4 = new JLabel();
  JTextPane tpLieferadresse = new JTextPane();
  JLabel label2 = new JLabel();
  JTextPane tpAdresse = new JTextPane();
  JTextPane tpBemerkungen = new JTextPane();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel8 = new JLabel();
  JLabel labFachbereich = new JLabel();
  JLabel labUT = new JLabel();
  JLabel labTitel = new JLabel();
  JLabel jLabel6 = new JLabel();
  JLabel labKapitel = new JLabel();
  JLabel jLabel7 = new JLabel();
  JLabel jLabel9 = new JLabel();
  JLabel jLabel10 = new JLabel();
  JScrollPane scrollTable = new JScrollPane();
  BestellungASKTable tableBestellung;
  JButton buAddPosition = new JButton();
  JLabel jLabel13 = new JLabel();
  JLabel labMwSt = new JLabel();
  JLabel jLabel14 = new JLabel();
  JLabel jLabel15 = new JLabel();
  JLabel labNetto = new JLabel();
  JLabel labBestellsumme = new JLabel();
  JButton buBeenden = new JButton();
  JButton buDrucken = new JButton();
  JButton buBestellen = new JButton();
  JButton buSpeichern = new JButton();
  JButton buTitel = new JButton();
  JComboBox cbKostenstelle = new JComboBox();
  JComboBox cbInstitut = new JComboBox();
  JLabel jLabel24 = new JLabel();
  JLabel labInstitut = new JLabel();
  Institut[] instituts = null;
  MainFrame frame;
  JTextField tfSoftwarebeauftragter = new JTextField();

  public BestellungASK(MainFrame frame) {
		this.frame = frame;
		this.setClosable(true);
		this.setIconifiable(true);

		buDrucken.addActionListener(this);
		buTitel.addActionListener(this);
		buBeenden.addActionListener(this);
		buBeenden.setIcon(Functions.getCloseIcon(this.getClass()));
		buSpeichern.addActionListener(this);
		buBestellen.addActionListener(this);
		buAddPosition.addActionListener(this);
		cbKostenstelle.addItemListener(this);
		cbKostenstelle.addPropertyChangeListener(this);
		cbInstitut.addItemListener(this);
		cbInstitut.addPropertyChangeListener(this);
		setData();

		tableBestellung = new BestellungASKTable(instituts);
		tableBestellung.addPropertyChangeListener(this);

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
		
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
  }
  
  

  private void loadInstituts(){
	  try {
		  instituts = frame.getApplicationServer().getInstitutes();

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

  private String checkData(){
	  String error = "";
	
	  error += (tpAdresse.getText().equals("") ? " - Firma \n" : "");
	  error += (tpLieferadresse.getText().equals("") ? " - Lieferadresse \n" : "");
	
	  return error;
	 }

  private void setData(){
//		  TODO Admin durch die Aktivität austauschen
	  if(frame.getBenutzer().getRolle().getBezeichnung().equals("Admin"))
		  loadInstituts();

	  loadHauptkonten();
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
			BestellungASK bestellung = new BestellungASK(test);
			test.addChild(bestellung);

			test.show();
			bestellung.show();
		}catch(Exception e){
				System.out.println(e);
		}
	 }

  private void jbInit() throws Exception {
		this.setBounds(50,50,640,480);
		buBeenden.setBounds(new Rectangle(513, 412, 112, 25));
    buBeenden.setText("Beenden");
    buDrucken.setBounds(new Rectangle(343, 412, 112, 25));
    buDrucken.setText("Drucken");
    buBestellen.setBounds(new Rectangle(2, 412, 112, 25));
    buBestellen.setText("Bestellen");
    buSpeichern.setBounds(new Rectangle(172, 412, 112, 25));
    buSpeichern.setText("Speichern");
    buTitel.setBounds(new Rectangle(429, 90, 114, 19));
    buTitel.setFont(new java.awt.Font("Dialog", 0, 11));
    buTitel.setText("auswählen");
    labKapitel.setText("");
    labTitel.setText("");
    labUT.setText("");
    cbKostenstelle.setBounds(new Rectangle(84, 34, 345, 21));
    cbInstitut.setBounds(new Rectangle(84, 8, 345, 21));
    jLabel24.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel24.setText("Kostenstelle:");
    jLabel24.setBounds(new Rectangle(9, 40, 82, 15));
    labInstitut.setFont(new java.awt.Font("Dialog", 0, 12));
    labInstitut.setText("Institut:");
    labInstitut.setBounds(new Rectangle(9, 9, 50, 15));
    tfSoftwarebeauftragter.setText("");
    tfSoftwarebeauftragter.setBounds(new Rectangle(135, 61, 166, 21));
    panelBestellung.add(tpAdresse, null);
    panelBestellung.add(label2, null);
    panelBestellung.add(jLabel4, null);
    panelBestellung.add(tpBemerkungen, null);
    panelBestellung.add(jLabel5, null);
    panelBestellung.add(tpLieferadresse, null);
    panelBeilage.add(jLabel13, null);
    panelBeilage.add(jLabel6, null);
    panelBeilage.add(jLabel7, null);
    panelBeilage.add(labFachbereich, null);
    panelBeilage.add(jLabel8, null);
    panelBeilage.add(jLabel9, null);
    panelBeilage.add(labTitel, null);
    panelBeilage.add(jLabel10, null);
    panelBeilage.add(labUT, null);
    panelBeilage.add(buTitel, null);
    panelBeilage.add(scrollTable, null);
    panelBeilage.add(buAddPosition, null);
    panelBeilage.add(jLabel15, null);
    panelBeilage.add(labNetto, null);
    panelBeilage.add(labMwSt, null);
    panelBeilage.add(jLabel14, null);
    panelBeilage.add(labBestellsumme, null);
    panelBeilage.add(cbKostenstelle, null);
    panelBeilage.add(jLabel24, null);
    panelBeilage.add(cbInstitut, null);
    panelBeilage.add(labInstitut, null);
    panelBeilage.add(tfSoftwarebeauftragter, null);
    panelBeilage.add(labKapitel, null);
    this.getContentPane().add(buSpeichern, null);
    this.getContentPane().add(buDrucken, null);
    this.getContentPane().add(buBeenden, null);
    scrollTable.getViewport().add(tableBestellung, null);
    jTabbedPane1.add(panelBestellung, "Bestellung");
    jTabbedPane1.add(panelBeilage, "Beilage ASK zur Bestellung");
    this.getContentPane().add(buBestellen, null);
    this.getContentPane().add(jTabbedPane1, null);
    this.setTitle("Bestellung ASK");
    this.getContentPane().setLayout(null);
    jTabbedPane1.setBounds(new Rectangle(5, 4, 620, 402));
    panelBestellung.setLayout(null);
    panelBeilage.setLayout(null);
    jLabel4.setText("363,50");
    jLabel4.setBounds(new Rectangle(484, 9, 79, 15));
    jLabel4.setText("58,16");
    jLabel4.setBounds(new Rectangle(484, 29, 79, 15));
    jLabel4.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel4.setText("Bemerkungen");
    jLabel4.setBounds(new Rectangle(4, 114, 159, 15));
    tpLieferadresse.setEditable(true);
    tpLieferadresse.setText("");
    tpLieferadresse.setBounds(new Rectangle(4, 213, 572, 82));
    label2.setFont(new java.awt.Font("Dialog", 1, 12));
    label2.setText("Firma");
    label2.setBounds(new Rectangle(4, 2, 84, 15));
    tpAdresse.setBackground(Color.white);
    tpAdresse.setEditable(true);
    tpAdresse.setText("");
    tpAdresse.setBounds(new Rectangle(4, 21, 287, 87));
    tpBemerkungen.setEditable(true);
    tpBemerkungen.setText("");
    tpBemerkungen.setFont(new java.awt.Font("Dialog", 0, 12));
    tpBemerkungen.setBounds(new Rectangle(4, 129, 572, 62));
    jLabel5.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel5.setText("Lieferanschrift:");
    jLabel5.setBounds(new Rectangle(4, 195, 202, 15));
    jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel8.setText("zu belastender Haushaltstitel:    Kapitel:");
    jLabel8.setBounds(new Rectangle(9, 93, 219, 15));
    labFachbereich.setFont(new java.awt.Font("Dialog", 0, 12));
    labFachbereich.setText("Informatik");
    labFachbereich.setBounds(new Rectangle(411, 67, 125, 15));
    labUT.setFont(new java.awt.Font("Dialog", 0, 12));
    labUT.setBounds(new Rectangle(399, 93, 40, 15));
    labTitel.setFont(new java.awt.Font("Dialog", 0, 12));
    labTitel.setBounds(new Rectangle(318, 93, 57, 15));
    jLabel6.setBounds(new Rectangle(9, 67, 135, 15));
    jLabel6.setText("Software-beauftragte/r:");
    jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
    labKapitel.setFont(new java.awt.Font("Dialog", 0, 12));
    labKapitel.setBounds(new Rectangle(228, 93, 49, 15));
    jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel7.setText("des Fachbereichs");
    jLabel7.setBounds(new Rectangle(308, 67, 106, 15));
    jLabel9.setBounds(new Rectangle(281, 93, 33, 15));
    jLabel9.setText("Titel:");
    jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setBounds(new Rectangle(370, 93, 28, 15));
    jLabel10.setText("UT:");
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    scrollTable.setBounds(new Rectangle(9, 121, 598, 156));
    buAddPosition.setBounds(new Rectangle(9, 284, 164, 25));
    buAddPosition.setText("Position hinzufügen");
    jLabel13.setBounds(new Rectangle(290, 309, 79, 15));
    jLabel13.setText("16 % MwSt.");
    jLabel13.setFont(new java.awt.Font("Dialog", 0, 12));
    labMwSt.setBounds(new Rectangle(408, 309, 117, 15));
    labMwSt.setText("");
    labMwSt.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel14.setBounds(new Rectangle(290, 338, 96, 15));
    jLabel14.setText("Bestellsumme:");
    jLabel14.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel15.setBounds(new Rectangle(290, 286, 115, 15));
    jLabel15.setText("Gesamtnettopreis:");
    jLabel15.setFont(new java.awt.Font("Dialog", 0, 12));
    labNetto.setBounds(new Rectangle(408, 286, 117, 15));
    labNetto.setText("");
    labNetto.setFont(new java.awt.Font("Dialog", 0, 12));
    labBestellsumme.setFont(new java.awt.Font("Dialog", 0, 12));
    labBestellsumme.setText("");
    labBestellsumme.setBounds(new Rectangle(408, 338, 117, 15));
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
	  BestellungBeilageASKPrint beilage = new BestellungBeilageASKPrint();
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

	  pJob.setJobName("Beilage ASK zur Bestellung");
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

	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == buTitel ) {
			AuswahlZVKonto kontoAuswahl;
			kontoAuswahl = new AuswahlZVKonto(this, (FBHauptkonto)cbKostenstelle.getSelectedItem(), false, frame);
			kontoAuswahl.show();
		}else if ( e.getSource() == buAddPosition ) {
			DefaultTableModel dtm = (DefaultTableModel)tableBestellung.getModel();
			Object[] o = {new Integer(1),"", new Float(0), new Float(0)};

			dtm.addRow(o);
			dtm.fireTableRowsInserted(dtm.getRowCount(),dtm.getRowCount());
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

	public void propertyChange(PropertyChangeEvent e) {
		if(e.getSource() == cbKostenstelle){
			FBHauptkonto kostenstelle = (FBHauptkonto)cbKostenstelle.getSelectedItem();

			if(kostenstelle != null){
				labKapitel.setText("");
				labTitel.setText("");
				labUT.setText("");
			}
		}else if(e.getSource() == cbInstitut){
			loadHauptkonten();
		}else if(e.getSource() == tableBestellung){
			DefaultTableModel dtm = (DefaultTableModel)tableBestellung.getModel();
		  float sum = 0;
		  float netto = 0;
		  float mwst = 0;

		  for(int i = 0; i < tableBestellung.getRowCount(); i++){
				netto += ((Float)dtm.getValueAt(i, 3)).floatValue();
		  }
		  mwst = (netto * 0.16f);
		  sum = netto + mwst;
		  labNetto.setText(NumberFormat.getCurrencyInstance().format(netto));
		  labMwSt.setText(NumberFormat.getCurrencyInstance().format(mwst));
		  labBestellsumme.setText(NumberFormat.getCurrencyInstance().format(sum));
		}
	}

	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == cbKostenstelle){
			FBHauptkonto kostenstelle = (FBHauptkonto)cbKostenstelle.getSelectedItem();

			if(kostenstelle != null){
				labKapitel.setText("");
				labTitel.setText("");
				labUT.setText("");
			}
		}else if(e.getSource() == cbInstitut){
			loadHauptkonten();
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
}