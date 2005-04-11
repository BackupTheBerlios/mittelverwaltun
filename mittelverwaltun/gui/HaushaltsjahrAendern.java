package gui;

import dbObjects.*;
import applicationServer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.text.*;
import java.util.Date;


public class HaushaltsjahrAendern extends JInternalFrame implements ActionListener {
	
	JPanel panelFrame = new JPanel();
	JLabel labJahr = new JLabel();
	JTextField tfJahr = new JTextField();
	JLabel labVon = new JLabel();
	JTextField tfVon = new JTextField();
	JLabel labBis = new JLabel();
	JTextField tfBis = new JTextField();
	JButton buUebernehmen = new JButton();
	JButton buBeenden = new JButton();

	ApplicationServer applicationServer;
	Haushaltsjahr haushaltsjahr;
	
	
	public HaushaltsjahrAendern(ApplicationServer applicationServer) {
		super( "Haushaltsjahr ändern" );
		this.setClosable(true);
		this.setIconifiable(true);
		this.applicationServer = applicationServer;
		
		try {
		  jbInit();
		}
		catch(Exception e) {
		  e.printStackTrace();
		}
		
		buUebernehmen.setIcon(Functions.getEditIcon(getClass()));
		buUebernehmen.addActionListener( this );
		buBeenden.setIcon(Functions.getCloseIcon(getClass()));
		buBeenden.addActionListener( this );
		
		loadHaushaltsjahr();
		this.setSize( 330, 170 );
	}
	
	/**
	 * Initialisierung der graphischen Oberfläche. 
	 * @throws Exception
	 * @author w.flat
	 */
	private void jbInit() throws Exception {
		panelFrame.setLayout(null);
		panelFrame.setBorder(BorderFactory.createEtchedBorder());
		labJahr.setText("Jahr");
		labJahr.setBounds(new Rectangle(12, 12, 50, 15));
		tfJahr.setText("");
		tfJahr.setBounds(new Rectangle(62, 12, 80, 21));
		labVon.setPreferredSize(new Dimension(34, 15));
		labVon.setText("Von");
		labVon.setBounds(new Rectangle(12, 52, 50, 15));
		tfVon.setText("");
		tfVon.setBounds(new Rectangle(62, 52, 80, 21));
		labBis.setText("Bis");
		labBis.setBounds(new Rectangle(152, 52, 50, 15));
		tfBis.setPreferredSize(new Dimension(57, 21));
		tfBis.setText("");
		tfBis.setBounds(new Rectangle(202, 52, 80, 21));
		buUebernehmen.setBounds(new Rectangle(12, 92, 130, 25));
		buUebernehmen.setText("Übernehmen");
		buBeenden.setBounds(new Rectangle(152, 92, 130, 25));
		buBeenden.setText("Beenden");
		this.getContentPane().add(panelFrame, BorderLayout.CENTER);
		panelFrame.add(labJahr, null);
		panelFrame.add(tfJahr, null);
		panelFrame.add(labVon, null);
		panelFrame.add(tfVon, null);
		panelFrame.add(labBis, null);
		panelFrame.add(tfBis, null);
		panelFrame.add(buUebernehmen, null);
		panelFrame.add(buBeenden, null);
	}
	
	protected void loadHaushaltsjahr(){
		try {
			Haushaltsjahr hhj = applicationServer.getHaushaltsjahr();
			
			this.haushaltsjahr = hhj;
			
			tfJahr.setText(hhj.getBeschreibung());
			tfVon.setText(hhj.getVon());
			tfBis.setText(hhj.getBis());
		}	catch (ApplicationServerException e) {
			System.out.println(e.getMessage());
		}
	}
	
	protected String changeHaushaltsjahr(){
		if(tfBis.getText().length() == 10 && tfVon.getText().length() == 10){
			if(tfJahr.getText().length() == 0 || tfBis.getText().length() == 0 || tfVon.getText().length() == 0){
				return "Jahr, Von, Bis müssen ausgefüllt werden.";
			}else{
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				try {
					Date d1 = dateFormat.parse(tfVon.getText());
					Date d2 = dateFormat.parse(tfBis.getText());
					
					if(d1.compareTo(d2) == -1){
						Haushaltsjahr hhj = new Haushaltsjahr(tfJahr.getText(), tfVon.getText(), tfBis.getText());
						try {
							applicationServer.setHaushaltsjahr(hhj, this.haushaltsjahr);
						} catch (ApplicationServerException e) {
							return e.getMessage();
						}
					}else{
						return "Von-Datum muss vor dem Bis-Datum kommen.";
					}
				} catch (ParseException e1) {
					return "DateFormat Fehler";
				}
			}
		}else{
			return "Es wird nur dd.mm.yyyy - Datumsformat akzeptiert.";
		}
		return "";
	}

	public void actionPerformed(ActionEvent e) {
		String error = "";
		if ( e.getSource() == buUebernehmen ) {
			error = changeHaushaltsjahr();
		} else if ( e.getSource() == buBeenden ) {
			this.dispose();
		}
		if(!error.equals("")){
			JOptionPane.showMessageDialog(
				 this,
				 error,
				 "Warnung",
				 JOptionPane.ERROR_MESSAGE);
			loadHaushaltsjahr();
	  }
	}
	
	public static void main(String[] args) {
		JFrame test = new JFrame("Haushaltsjahr ändern Test");
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
//			HaushaltsjahrAendern hhj = new HaushaltsjahrAendern(applicationServer);
//			desk.add(hhj);
//			test.show();
//			hhj.show();
		}catch(Exception e){
				System.out.println(e);
		}
  }
}
