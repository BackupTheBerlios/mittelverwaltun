package gui;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import dbObjects.ASKBestellung;
import dbObjects.Benutzer;
import dbObjects.FBUnterkonto;
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
import java.rmi.Naming;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;


public class BestellungASK extends JInternalFrame implements ActionListener, TableModelListener, ItemListener, ZVKontoSelectable, FBKontoSelectable {
  JLabel jLabel4 = new JLabel();
  JTextPane tpBemerkungen = new JTextPane();
  JLabel jLabel6 = new JLabel();
  JScrollPane scrollTable = new JScrollPane();
  PositionsTable tableBestellung;
  MainFrame frame;
  JComboBox cbSwBeauftragter = new JComboBox();
  JButton buBeenden = new JButton();
  JButton buDelete = new JButton();
  JButton buDrucken = new JButton();
  JButton buBestellen = new JButton();
  JButton buSpeichern = new JButton();
  CurrencyTextField tfBestellsumme = new CurrencyTextField();
  JLabel jLabel8 = new JLabel();
  JButton buAddPosition = new JButton();
  JLabel labMwSt16 = new JLabel();
  JLabel labMwSt7 = new JLabel();
  JLabel jLabel9 = new JLabel();
  JLabel jLabel7 = new JLabel();
  JLabel labNetto = new JLabel();
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


  public BestellungASK(MainFrame frame) {
		this.frame = frame;
		this.setClosable(true);
		this.setIconifiable(true);

		try {
			tableBestellung = new PositionsTable(PositionsTable.ASK_STANDARD, true, this, new ArrayList(), frame.getApplicationServer().getInstitutes());
		} catch (ApplicationServerException e) {
			// TODO Automatisch erstellter Catch-Block
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
			tableBestellung = new PositionsTable(PositionsTable.ASK_STANDARD, true, this, bestellung.getAngebot().getPositionen(), frame.getApplicationServer().getInstitutes());
		} catch (ApplicationServerException e) {
			// TODO Automatisch erstellter Catch-Block
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
		buDrucken.setIcon(Functions.getPrintIcon(getClass()));
		buTitel.addActionListener(this);
		buAddPosition.addActionListener(tableBestellung);
		buAddPosition.setActionCommand("addPosition");
		buAddPosition.setIcon(Functions.getAddIcon(getClass()));
		buBeenden.addActionListener(this);
		buBeenden.setIcon(Functions.getCloseIcon(this.getClass()));
		buSpeichern.addActionListener(this);
		buSpeichern.setIcon(Functions.getSaveIcon(this.getClass()));
		buBestellen.addActionListener(this);
		buBestellen.setIcon(Functions.getBestellIcon(getClass()));
		buFBKonto.addActionListener(this);
		buDelete.addActionListener(this);
		buDelete.setIcon(Functions.getDelIcon(getClass()));
		setData();

		

//	TODO Admin durch die Aktivität austauschen
		if(!frame.getBenutzer().getRolle().getBezeichnung().equals("Admin")){
			cbInstitut.setVisible(false);
			labInstitut.setVisible(false);
		}

		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
  }

  private void setOrderData(){
  }

  private void loadInstituts(){
	  try {
//		TODO Admin durch die Aktivität austauschen
			if(frame.getBenutzer().getRolle().getBezeichnung().equals("Admin")){
				Institut[] instituts = frame.getApplicationServer().getInstitutes();

			  if(instituts != null){
				  cbInstitut.removeAllItems();
					 for(int i = 0; i < instituts.length; i++){
						  cbInstitut.addItem(instituts[i]);
					 }
			  }
			}else{
				Institut institut = frame.getBenutzer().getKostenstelle();

				cbInstitut.removeAllItems();
				cbInstitut.addItem(institut);
			}
	  } catch (ApplicationServerException e) {
		  e.printStackTrace();
	  }
  }


  private String checkData(){
	  String error = "";

//	  error += (tpAdresse.getText().equals("") ? " - Firma \n" : "");
//	  error += (tpLieferadresse.getText().equals("") ? " - Lieferadresse \n" : "");

	  return error;
	 }

	private void setData(){
	 loadUsers();
	 loadInstituts();
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
		this.setBounds(50,50,620,650);
    cbSwBeauftragter.setBounds(new Rectangle(110, 122, 345, 21));
    buBeenden.setBounds(new Rectangle(481, 559, 112, 25));
    buBeenden.setText("Beenden");
    buDelete.setText("Löschen");
    buDelete.setBounds(new Rectangle(242, 559, 112, 25));
    buDrucken.setBounds(new Rectangle(362, 559, 112, 25));
    buDrucken.setText("Drucken");
    buBestellen.setBounds(new Rectangle(3, 559, 112, 25));
    buBestellen.setText("Bestellen");
    buSpeichern.setBounds(new Rectangle(123, 559, 112, 25));
    buSpeichern.setText("Speichern");
    tfBestellsumme.setBounds(new Rectangle(405, 436, 117, 21));
    jLabel8.setBounds(new Rectangle(288, 396, 79, 15));
    jLabel8.setText("7 % MwSt.");
    jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
    buAddPosition.setText("Position hinzufügen");
    buAddPosition.setBounds(new Rectangle(4, 372, 164, 25));
    labMwSt16.setBounds(new Rectangle(405, 416, 117, 15));
    labMwSt16.setText("");
    labMwSt16.setFont(new java.awt.Font("Dialog", 0, 12));
    labMwSt7.setBounds(new Rectangle(405, 396, 117, 15));
    labMwSt7.setText("");
    labMwSt7.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel9.setBounds(new Rectangle(287, 442, 96, 15));
    jLabel9.setText("Bestellsumme:");
    jLabel9.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel7.setBounds(new Rectangle(287, 375, 115, 15));
    jLabel7.setText("Gesamtnettopreis:");
    jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
    labNetto.setBounds(new Rectangle(405, 375, 117, 15));
    labNetto.setText("");
    labNetto.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setText("16 % MwSt.");
    jLabel10.setBounds(new Rectangle(287, 416, 79, 15));
    labUT.setBounds(new Rectangle(384, 180, 40, 15));
    labUT.setHorizontalAlignment(SwingConstants.CENTER);
    labUT.setFont(new java.awt.Font("Dialog", 0, 12));
    labTitel.setBounds(new Rectangle(312, 180, 52, 15));
    labTitel.setHorizontalAlignment(SwingConstants.CENTER);
    labTitel.setFont(new java.awt.Font("Dialog", 0, 12));
    labInstitut1.setFont(new java.awt.Font("Dialog", 0, 12));
    labInstitut1.setText("Auftraggeber:");
    labInstitut1.setBounds(new Rectangle(8, 42, 89, 15));
    cbEmpfaenger.setBounds(new Rectangle(110, 67, 343, 21));
    cbInstitut.setBounds(new Rectangle(110, 94, 345, 21));
    jLabel11.setBounds(new Rectangle(287, 180, 40, 15));
    jLabel11.setText("Titel");
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    cbAuftraggeber.setBounds(new Rectangle(110, 39, 344, 21));
    labKapitel.setBounds(new Rectangle(227, 180, 55, 15));
    labKapitel.setHorizontalAlignment(SwingConstants.CENTER);
    labKapitel.setFont(new java.awt.Font("Dialog", 0, 12));
    labBestellNr1.setBorder(null);
    labBestellNr1.setText(" Bestell-Datum:");
    labBestellNr1.setBounds(new Rectangle(309, 7, 95, 23));
    labBestellNr1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel12.setBounds(new Rectangle(8, 180, 185, 15));
    jLabel12.setText("zu belastender Haushaltstitel:");
    jLabel12.setFont(new java.awt.Font("Dialog", 0, 12));
    labInstitut2.setBounds(new Rectangle(8, 70, 89, 15));
    labInstitut2.setText("Empfänger:");
    labInstitut2.setFont(new java.awt.Font("Dialog", 0, 12));
    buFBKonto.setText("FBKonto");
    buFBKonto.setBounds(new Rectangle(456, 151, 109, 21));
    buTitel.setText("Titelauswahl");
    buTitel.setActionCommand("buTitel");
    buTitel.setBounds(new Rectangle(456, 176, 109, 21));
    tfFBKonto.setBounds(new Rectangle(110, 151, 345, 21));
    tfFBKonto.setText("");
    tfFBKonto.setEditable(false);
    tfBestellDatum.setBounds(new Rectangle(397, 8, 72, 20));
    jLabel24.setBounds(new Rectangle(8, 152, 82, 15));
    jLabel24.setText("Kostenstelle:");
    jLabel24.setFont(new java.awt.Font("Dialog", 0, 12));
    labInstitut.setBounds(new Rectangle(8, 97, 50, 15));
    labInstitut.setText("Institut:");
    labInstitut.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel13.setBounds(new Rectangle(186, 180, 53, 15));
    jLabel13.setText("Kapitel");
    jLabel13.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel14.setBounds(new Rectangle(366, 180, 33, 15));
    jLabel14.setText("UT");
    jLabel14.setFont(new java.awt.Font("Dialog", 0, 12));
    this.getContentPane().add(tfBestellDatum, null);
    this.getContentPane().add(labBestellNr1, null);
    this.getContentPane().add(labInstitut1, null);
    this.getContentPane().add(cbAuftraggeber, null);
    this.getContentPane().add(labInstitut2, null);
    this.getContentPane().add(cbEmpfaenger, null);
    this.getContentPane().add(labInstitut, null);
    this.getContentPane().add(jLabel12, null);
    this.getContentPane().add(jLabel24, null);
    this.getContentPane().add(tfFBKonto, null);
    this.getContentPane().add(labUT, null);
    this.getContentPane().add(jLabel14, null);
    this.getContentPane().add(labTitel, null);
    this.getContentPane().add(jLabel11, null);
    this.getContentPane().add(labKapitel, null);
    this.getContentPane().add(jLabel13, null);
    this.getContentPane().add(jLabel6, null);
    this.getContentPane().add(cbInstitut, null);
    this.getContentPane().add(cbSwBeauftragter, null);
    this.getContentPane().add(buTitel, null);
    this.getContentPane().add(buFBKonto, null);
    this.getContentPane().add(buAddPosition, null);
    this.getContentPane().add(tfBestellsumme, null);
    this.getContentPane().add(labMwSt16, null);
    this.getContentPane().add(labMwSt7, null);
    this.getContentPane().add(jLabel10, null);
    this.getContentPane().add(jLabel9, null);
    this.getContentPane().add(jLabel7, null);
    this.getContentPane().add(labNetto, null);
    this.getContentPane().add(jLabel8, null);
    this.getContentPane().add(tpBemerkungen, null);
    this.getContentPane().add(jLabel4, null);
    this.getContentPane().add(buDrucken, null);
    this.getContentPane().add(buBestellen, null);
    this.getContentPane().add(buSpeichern, null);
    this.getContentPane().add(buDelete, null);
    this.getContentPane().add(buBeenden, null);
    this.getContentPane().add(scrollTable, null);
		scrollTable.getViewport().add(tableBestellung, null);
    this.setTitle("Bestellung ASK");
    this.getContentPane().setLayout(null);
    jLabel4.setText("363,50");
    jLabel4.setBounds(new Rectangle(484, 9, 79, 15));
    jLabel4.setText("58,16");
    jLabel4.setBounds(new Rectangle(484, 29, 79, 15));
    jLabel4.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel4.setText("Bemerkungen");
    jLabel4.setBounds(new Rectangle(5, 465, 159, 15));
    tpBemerkungen.setEditable(true);
    tpBemerkungen.setText("");
    tpBemerkungen.setFont(new java.awt.Font("Dialog", 0, 12));
    tpBemerkungen.setBounds(new Rectangle(4, 490, 557, 62));
    jLabel6.setBounds(new Rectangle(8, 125, 104, 15));
    jLabel6.setText("SW-Beauftragte/r:");
    jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
    scrollTable.setBounds(new Rectangle(5, 210, 583, 156));
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
	  String error = "";
	  if ( e.getSource() == buFBKonto ) {
		  AuswahlFBKonto fbKontoAuswahl = new AuswahlFBKonto(this, (Institut)cbInstitut.getSelectedItem(), false, frame.getApplicationServer(), true);
		  fbKontoAuswahl.show();
	  }else if ( e.getSource() == buTitel ) {
		  AuswahlZVKonto kontoAuswahl = new AuswahlZVKonto(this, fbKonto, false, frame);
		  kontoAuswahl.show();
	  }else if ( e.getSource() == buDrucken ) {
		  printBestellung();
	  }else if ( e.getSource() == buBestellen ) {
		  error = checkData();
		  if(error.equals("")){
			  bestellen();
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
			  saveBestellung();
		  }else{
			  JOptionPane.showMessageDialog(
						 this,
						 error,
						 "Warnung",
						 JOptionPane.ERROR_MESSAGE);
		  }
	  }else if ( e.getSource() == buBeenden ) {
		  dispose();
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
		if(zvTitel.getZVTitel() != null){
			labKapitel.setText(zvTitel.getZVTitel().getZVKonto().getKapitel());
			labTitel.setText(zvTitel.getTitel());
			labUT.setText(zvTitel.getUntertitel());
			this.zvTitel = zvTitel.getZVTitel();
		}else{
			labKapitel.setText(((ZVTitel)zvTitel).getZVKonto().getKapitel());
			labTitel.setText(zvTitel.getTitel());
			labUT.setText("");
			this.zvTitel = (ZVTitel)zvTitel;
		}
	}

	/* (Kein Javadoc)
	 * @see gui.FBKontoSelectable#setFBKonto(dbObjects.FBUnterkonto)
	 */
	public void setFBKonto(FBUnterkonto fbKonto) {
		this.fbKonto = fbKonto;
		tfFBKonto.setText(fbKonto.toString());
	}

	/* (Kein Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	public void tableChanged(TableModelEvent e) {
		tfBestellsumme.setValue(new Float(((PositionsTableModel)e.getSource()).getOrderSum()));
	}
}