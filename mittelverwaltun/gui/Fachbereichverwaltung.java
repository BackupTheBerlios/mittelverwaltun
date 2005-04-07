package gui;

import javax.swing.*;
import applicationServer.*;

import dbObjects.Fachbereich;
import dbObjects.Institut;
import java.rmi.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;

public class Fachbereichverwaltung extends JInternalFrame implements ActionListener{
  JPanel jPanel1 = new JPanel();
  JLabel jLabel1 = new JLabel();
  JButton buRefresh = new JButton();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel7 = new JLabel();
  JTextField tfFBBezeichnung = new JTextField();
  CurrencyTextField tfProfBudget = new CurrencyTextField(0);
  JTextField tfPlzOrt = new JTextField();
  JTextField tfStrasseHausnr = new JTextField();
  JTextField tfFHBezeichnung = new JTextField();
  JTextField tfFHBeschreibung = new JTextField();
  JLabel jLabel8 = new JLabel();
  JComboBox cbInstitut = new JComboBox();
  JButton buAendern = new JButton();
  JButton buBeenden = new JButton();

  ApplicationServer applicationServer;
	Container contentPane;
	Fachbereich fachbereich;
  JLabel jLabel9 = new JLabel();


  public Fachbereichverwaltung(MainFrame frame) {
		 try {
			jbInit();
		 }
		 catch(Exception e) {
			e.printStackTrace();
		 }
		this.setClosable(true);
		this.setIconifiable(true);
		this.getContentPane().setLayout( null );
		this.applicationServer = frame.getApplicationServer();

		buRefresh.setBorder(null);
		buRefresh.setIcon(Functions.getRefreshIcon(getClass()));
		buRefresh.setToolTipText("Aktualisieren");
		buRefresh.addActionListener( this );

		buAendern.setIcon(Functions.getEditIcon(getClass()));
		buAendern.addActionListener( this );

		buBeenden.setIcon(Functions.getCloseIcon(getClass()));
		buBeenden.addActionListener( this );

		loadFachbereiche();
		loadData();
		loadInstituts();
		this.setSize( 395, 350 );
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
    
  }

  private void loadFachbereiche(){
	  try{
		 Fachbereich[] fachbereiche = applicationServer.getFachbereiche();

		 fachbereich = fachbereiche[0];

		 
	 }catch(Exception e){
		 System.out.println(e);
	 }
  }
  
  private void loadData(){
  	
		 tfFBBezeichnung.setText(fachbereich.getFbBezeichnung());
		 tfProfBudget.setValue(new Float(fachbereich.getProfPauschale()));
		 tfStrasseHausnr.setText(fachbereich.getStrasseHausNr());
		 tfPlzOrt.setText(fachbereich.getPlzOrt());
		 tfFHBezeichnung.setText(fachbereich.getFhBezeichnung());
		 tfFHBeschreibung.setText(fachbereich.getFhBeschreibung());
  }

  private void loadInstituts(){
		 try{
			 Institut[] institute = applicationServer.getInstitutes();

			  if(institute != null){
				  cbInstitut.removeAllItems();
					 for(int i = 0; i < institute.length; i++){
						 cbInstitut.addItem(institute[i]);
					 }
					Institut institut = new Institut(fachbereich.getId(), fachbereich.getBezeichnung(), fachbereich.getKostenstelle());
					cbInstitut.setSelectedItem(institut);
			  }
		 }catch(Exception e){
			 System.out.println(e);
		 }
	 }

	protected String changeFachbereich(){
		Number budget = null;
		try {
			budget = NumberFormat.getCurrencyInstance().parse(tfProfBudget.getText());
		} catch (ParseException e) {
			return e.getMessage();
		}
		Fachbereich fachbereichNew = new Fachbereich(fachbereich.getId(), tfFBBezeichnung.getText(), budget.floatValue(),
																								 tfStrasseHausnr.getText(),	tfPlzOrt.getText(), tfFHBezeichnung.getText(),
																								 tfFHBeschreibung.getText());
		try{
			int institutsId = ((Institut)cbInstitut.getSelectedItem()).getId();
			fachbereichNew.setId(institutsId);
			fachbereich = applicationServer.setFachbereich(fachbereichNew, fachbereich);
			
			return "";
		}catch(Exception e){
			return e.getMessage();
		}
	}

	 public static void main(String[] args) {
		  JFrame test = new JFrame("Fachbereichverwaltung Test");
		  JDesktopPane desk = new JDesktopPane();
		  desk.setDesktopManager(new DefaultDesktopManager());
		  test.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		  test.setContentPane(desk);
		  test.setBounds(100,100,800,700);
		  try{
			  CentralServer server = (CentralServer)Naming.lookup("//localhost/mittelverwaltung");
//			  ApplicationServer applicationServer = server.getMyApplicationServer();
//			  PasswordEncrypt pe = new PasswordEncrypt();
//			  String psw = pe.encrypt(new String("r.driesner").toString());
//			  applicationServer.login("r.driesner", psw);
//			  Fachbereichverwaltung fachbereichverwaltung = new Fachbereichverwaltung(applicationServer);
//			  desk.add(fachbereichverwaltung);
//			  test.show();
//			  fachbereichverwaltung.show();
		  }catch(Exception e){
				  System.out.println(e);
		  }
	 }

  private void jbInit() throws Exception {
	 this.setTitle("Fachbereichverwaltung");
	 jLabel9.setBounds(new Rectangle(8, 212, 95, 15));
    jLabel9.setText("Bezeichnung");
    jLabel9.setFont(new java.awt.Font("Dialog", 1, 12));
    jPanel1.add(buRefresh, null);
	 jPanel1.add(jLabel1, null);
	 jPanel1.add(jLabel2, null);
	 jPanel1.add(jLabel4, null);
	 jPanel1.add(jLabel5, null);
	 jPanel1.add(tfProfBudget, null);
	 jPanel1.add(tfStrasseHausnr, null);
	 jPanel1.add(tfPlzOrt, null);
	 jPanel1.add(jLabel3, null);
	 jPanel1.add(tfFHBezeichnung, null);
	 jPanel1.add(tfFHBeschreibung, null);
	 jPanel1.add(jLabel8, null);
	 jPanel1.add(cbInstitut, null);
	 jPanel1.add(tfFBBezeichnung, null);
	 jPanel1.add(buAendern, null);
	 jPanel1.add(buBeenden, null);
    jPanel1.add(jLabel6, null);
    jPanel1.add(jLabel7, null);
    jPanel1.add(jLabel9, null);
	 cbInstitut.setBounds(new Rectangle(146, 155, 199, 21));
	 buAendern.setBounds(new Rectangle(35, 264, 128, 25));
	 buAendern.setText("Ändern");
	 buBeenden.setBounds(new Rectangle(202, 264, 126, 25));
	 buBeenden.setText("Beenden");
	 this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	 this.getContentPane().setLayout(null);
	 jPanel1.setBorder(BorderFactory.createEtchedBorder());
	 jPanel1.setBounds(new Rectangle(6, 7, 372, 301));
	 jPanel1.setLayout(null);
	 jLabel1.setFont(new java.awt.Font("Dialog", 1, 12));
	 jLabel1.setText("Fachbereich");
	 jLabel1.setBounds(new Rectangle(8, 10, 95, 15));
	 buRefresh.setBounds(new Rectangle(96, 6, 21, 21));
	 buRefresh.setText("");
	 jLabel2.setBounds(new Rectangle(8, 40, 85, 15));
	 jLabel2.setText("Bezeichnung");
	 jLabel2.setFont(new java.awt.Font("Dialog", 1, 12));
	 jLabel3.setBounds(new Rectangle(8, 69, 122, 15));
	 jLabel3.setText("Professor Pauschale");
	 jLabel3.setFont(new java.awt.Font("Dialog", 1, 12));
	 jLabel4.setBounds(new Rectangle(8, 99, 95, 15));
	 jLabel4.setText("Straße Hausnr.");
	 jLabel4.setFont(new java.awt.Font("Dialog", 1, 12));
	 jLabel5.setBounds(new Rectangle(8, 128, 95, 15));
	 jLabel5.setText("PLZ Ort");
	 jLabel5.setFont(new java.awt.Font("Dialog", 1, 12));
	 jLabel6.setBounds(new Rectangle(8, 187, 95, 15));
	 jLabel6.setText("Hochschule");
	 jLabel6.setFont(new java.awt.Font("Dialog", 1, 12));
	 jLabel7.setFont(new java.awt.Font("Dialog", 1, 12));
	 jLabel7.setText("Beschreibung");
	 jLabel7.setBounds(new Rectangle(8, 236, 95, 15));
	 tfFBBezeichnung.setText("");
	 tfFBBezeichnung.setBounds(new Rectangle(146, 37, 199, 21));
	 tfProfBudget.setBounds(new Rectangle(146, 67, 199, 21));
	 tfPlzOrt.setText("");
	 tfPlzOrt.setBounds(new Rectangle(146, 126, 199, 21));
	 tfStrasseHausnr.setText("");
	 tfStrasseHausnr.setBounds(new Rectangle(146, 96, 199, 21));
	 tfFHBezeichnung.setText("");
	 tfFHBezeichnung.setBounds(new Rectangle(146, 209, 199, 21));
	 tfFHBeschreibung.setBounds(new Rectangle(146, 233, 199, 21));
	 tfFHBeschreibung.setText("");
	 jLabel8.setFont(new java.awt.Font("Dialog", 1, 12));
	 jLabel8.setText("Institut");
	 jLabel8.setBounds(new Rectangle(8, 158, 95, 15));
	 this.getContentPane().add(jPanel1, null);
  }

	public void actionPerformed( ActionEvent e ) {
		String error = "";

		if ( e.getSource() == buAendern ) {
			error = changeFachbereich();
		} else if ( e.getSource() == buRefresh ) {
			loadFachbereiche();
			loadData();
			loadInstituts();
		} else if ( e.getSource() == buBeenden ) {
			this.dispose();
		}
		if(!error.equals("")){
			JOptionPane.showMessageDialog(
				 this,
				 error,
				 "Warnung",
				 JOptionPane.ERROR_MESSAGE);
			loadFachbereiche();
		}
	}
}