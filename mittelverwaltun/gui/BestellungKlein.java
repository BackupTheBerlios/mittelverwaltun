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
  JLabel labUT = new JLabel();
  JLabel labTitel = new JLabel();
  JLabel jLabel11 = new JLabel();
  JLabel labKapitel = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel9 = new JLabel();
  JLabel jLabel10 = new JLabel();
  JPanel oben = new JPanel();
  JButton buKontoAuswahl = new JButton();
  JTextField tfProjektNr = new JTextField();
  JLabel jLabel8 = new JLabel();
  JPanel jPanel4 = new JPanel();
  JLabel jLabel13 = new JLabel();
  JTextPane tpVerwendung = new JTextPane();
  JLabel jLabel14 = new JLabel();
  JPanel jPanel2 = new JPanel();
  JPanel unten = new JPanel();
  JLabel jLabel7 = new JLabel();
  JPanel jPanel1 = new JPanel();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel5 = new JLabel();
  BestellungKleinTable table = new BestellungKleinTable();
  JScrollPane scrollTable = new JScrollPane();
  JLabel jLabel1 = new JLabel();
  JLabel labGesamt = new JLabel();
  JButton buAbbrechen = new JButton();
  JButton buBestellen = new JButton();
  JButton buDrucken = new JButton();
  JTextField tfTitlVerz = new JTextField();
  JTextField tfKartei = new JTextField();
  JTextField tfKarteiNr = new JTextField();

  MainFrame frame;
  JButton buAddRow = new JButton();
  JComboBox cbKostenstelle = new JComboBox();
  JComboBox cbInstitut = new JComboBox();
  JLabel jLabel24 = new JLabel();
  JLabel labInstitut = new JLabel();
  JLabel labUser = new JLabel();

  public BestellungKlein(MainFrame frame) {
		super( "Kleinbestellung erstellen" );
		this.frame = frame;
		this.setClosable(true);
		this.setIconifiable(true);
		this.getContentPane().setLayout( null );
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setData();
		try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

		labUser.setText(frame.getBenutzer().getName() + ", " + frame.getBenutzer().getVorname());

		buAbbrechen.addActionListener(this);
		buBestellen.addActionListener(this);
		buDrucken.addActionListener(this);
		buKontoAuswahl.addActionListener(this);
		buAddRow.addActionListener(this);

		if(!frame.getBenutzer().getRolle().getBezeichnung().equals("Admin")){
			cbInstitut.setVisible(false);
			labInstitut.setVisible(false);
		}
		table.addPropertyChangeListener(this);
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
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
	  loadInstituts();
	  loadHauptkonten();
  }

  void insertZVKonto(){

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
    this.setBounds(50,50,550,550);
    oben.setLayout(null);
    oben.setBounds(new Rectangle(6, 49, 549, 223));
    scrollTable.setBounds(new Rectangle(3, 61, 530, 128));
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel1.setText("Gesamt");
    jLabel1.setBounds(new Rectangle(343, 198, 51, 15));
    labGesamt.setFont(new java.awt.Font("Dialog", 1, 12));
    labGesamt.setText("");
    labGesamt.setBounds(new Rectangle(406, 198, 79, 15));
    buAbbrechen.setBounds(new Rectangle(378, 197, 117, 25));
    buAbbrechen.setText("Abbrechen");
    buBestellen.setBounds(new Rectangle(24, 197, 117, 25));
    buBestellen.setText("Bestellen");
    buDrucken.setBounds(new Rectangle(201, 197, 117, 25));
    buDrucken.setText("Drucken");
    tfTitlVerz.setText("");
    tfTitlVerz.setBounds(new Rectangle(84, 157, 128, 21));
    tfKartei.setText("");
    tfKartei.setBounds(new Rectangle(148, 3, 94, 21));
    tfKarteiNr.setText("");
    tfKarteiNr.setBounds(new Rectangle(313, 3, 101, 21));
    buAddRow.setBounds(new Rectangle(4, 192, 127, 21));
    buAddRow.setText("Zeile hinzufügen");
    labKapitel.setText("");
    labTitel.setText("");
    jLabel10.setBounds(new Rectangle(199, 9, 33, 15));
    jLabel10.setText("UT");
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel9.setBounds(new Rectangle(7, 9, 53, 15));
    jLabel9.setText("Kapitel");
    jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setBounds(new Rectangle(5, 37, 67, 15));
    jLabel3.setText("Projekt-Nr.");
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    labKapitel.setBounds(new Rectangle(47, 9, 55, 15));
    labKapitel.setHorizontalAlignment(SwingConstants.CENTER);
    labKapitel.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel11.setBounds(new Rectangle(109, 9, 40, 15));
    jLabel11.setText("Titel");
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    labTitel.setBounds(new Rectangle(137, 9, 52, 15));
    labTitel.setHorizontalAlignment(SwingConstants.CENTER);
    labTitel.setFont(new java.awt.Font("Dialog", 0, 12));
    labUT.setBounds(new Rectangle(221, 9, 40, 15));
    labUT.setHorizontalAlignment(SwingConstants.CENTER);
    labUT.setFont(new java.awt.Font("Dialog", 0, 12));
    this.setTitle("Kleinbestellung");
    this.getContentPane().setLayout(null);
		buKontoAuswahl.setBounds(new Rectangle(278, 5, 176, 25));
		buKontoAuswahl.setText("ZVKonto auswählen");
    tfProjektNr.setBounds(new Rectangle(75, 32, 57, 21));
    tfProjektNr.setText("");
    tfProjektNr.setBounds(new Rectangle(78, 31, 123, 21));
    jLabel8.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel8.setText("eingetragen.");
    jLabel8.setBounds(new Rectangle(422, 9, 75, 15));
    jPanel4.setLayout(null);
    jPanel4.setBounds(new Rectangle(4, 117, 529, 32));
    jPanel4.setBorder(BorderFactory.createLineBorder(Color.black));
    jLabel13.setText("");
    jLabel13.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel13.setText("Auszahlung an:");
    jLabel13.setBounds(new Rectangle(5, 9, 89, 15));
    tpVerwendung.setFont(new java.awt.Font("Dialog", 1, 11));
    tpVerwendung.setBorder(null);
    tpVerwendung.setText("");
    tpVerwendung.setBounds(new Rectangle(3, 21, 519, 51));
    jLabel14.setFont(new java.awt.Font("Dialog", 0, 11));
    jLabel14.setText("Titl. Verz. Nr.:");
    jLabel14.setBounds(new Rectangle(5, 164, 89, 15));
    jPanel2.setBorder(BorderFactory.createLineBorder(Color.black));
    jPanel2.setBounds(new Rectangle(4, 8, 529, 79));
    jPanel2.setLayout(null);
    unten.setBounds(new Rectangle(4, 276, 547, 229));
    unten.setLayout(null);
    jLabel7.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel7.setText("- Labor, Nr.");
    jLabel7.setBounds(new Rectangle(244, 9, 68, 15));
    jPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
    jPanel1.setBounds(new Rectangle(4, 86, 529, 32));
    jPanel1.setLayout(null);
    jLabel6.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel6.setText("In Material/Geräte-Kartei");
    jLabel6.setBounds(new Rectangle(4, 9, 140, 15));
    jLabel5.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel5.setText("Verwendung oder Begründung");
    jLabel5.setBounds(new Rectangle(5, 3, 187, 15));
    cbKostenstelle.setBounds(new Rectangle(88, 27, 345, 21));
    cbInstitut.setBounds(new Rectangle(88, 3, 345, 21));
    jLabel24.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel24.setText("Kostenstelle:");
    jLabel24.setBounds(new Rectangle(13, 33, 82, 15));
    labInstitut.setFont(new java.awt.Font("Dialog", 0, 12));
    labInstitut.setText("Institut:");
    labInstitut.setBounds(new Rectangle(13, 9, 50, 15));
    labUser.setBounds(new Rectangle(99, 9, 296, 15));
    labUser.setText("Auszahlung an:");
    labUser.setFont(new java.awt.Font("Dialog", 1, 11));
    jPanel2.add(jLabel5, null);
    jPanel2.add(tpVerwendung, null);
    this.getContentPane().add(oben, null);
    jPanel1.add(jLabel8, null);
    jPanel1.add(jLabel6, null);
    jPanel1.add(jLabel7, null);
    jPanel1.add(tfKartei, null);
    jPanel1.add(tfKarteiNr, null);
    unten.add(jPanel2, null);
    jPanel4.add(jLabel13, null);
    jPanel4.add(labUser, null);
    this.getContentPane().add(cbInstitut, null);
    this.getContentPane().add(cbKostenstelle, null);
    this.getContentPane().add(jLabel24, null);
    this.getContentPane().add(labInstitut, null);
    unten.add(jLabel14, null);
    unten.add(buBestellen, null);
    unten.add(buDrucken, null);
    unten.add(tfTitlVerz, null);
    unten.add(buAbbrechen, null);
    unten.add(jPanel1, null);
    unten.add(jPanel4, null);
    oben.add(tfProjektNr, null);
    oben.add(jLabel9, null);
    oben.add(labKapitel, null);
    oben.add(jLabel11, null);
    oben.add(labTitel, null);
    oben.add(jLabel10, null);
    oben.add(labUT, null);
    oben.add(buKontoAuswahl, null);
    oben.add(jLabel3, null);
    oben.add(scrollTable, null);
    oben.add(jLabel1, null);
    oben.add(labGesamt, null);
    oben.add(buAddRow, null);
    scrollTable.getViewport().add(table, null);
    oben.add(tfProjektNr, null);
    this.getContentPane().add(unten, null);

  }
	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == buKontoAuswahl ) {
			AuswahlZVKonto kontoAuswahl = new AuswahlZVKonto(this, (FBHauptkonto)cbKostenstelle.getSelectedItem(), false, frame);
			kontoAuswahl.show();
		}else if ( e.getSource() == buAddRow ) {
			DefaultTableModel dtm = (DefaultTableModel)table.getModel();
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
		//zvKonto.getKapitel() + "/" + getTitel()
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
		}else if(e.getSource() == table){
			DefaultTableModel dtm = (DefaultTableModel)table.getModel();
		  float sum = 0;
		  float price = 0;

		  for(int i = 0; i < table.getRowCount(); i++){
				price = ((Float)dtm.getValueAt(i, 3)).floatValue();
				sum += price;
		  }
		  labGesamt.setText(NumberFormat.getCurrencyInstance().format(sum));
		}
	}


	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == cbKostenstelle){
			FBHauptkonto kostenstelle = (FBHauptkonto)cbKostenstelle.getSelectedItem();
			labUser.setText("Hallo" + frame.getBenutzer().getName() + ", " + frame.getBenutzer().getVorname());
			if(kostenstelle != null){
				labKapitel.setText("");
				labTitel.setText("");
				labUT.setText("");
			}
		}else if(e.getSource() == cbInstitut){
			loadHauptkonten();
		}
	}
}