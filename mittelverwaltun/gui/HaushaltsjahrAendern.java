package gui;

import javax.swing.*;

import dbObjects.Haushaltsjahr;

import applicationServer.ApplicationServer;
import applicationServer.ApplicationServerException;
import applicationServer.CentralServer;

import java.awt.event.*;
import java.rmi.Naming;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HaushaltsjahrAendern extends JInternalFrame implements ActionListener {
	
	ApplicationServer applicationServer;
	JPanel panel = new JPanel();
	JTextField tfJahr = new JTextField();
	DateTextField tfVon = new DateTextField();
	DateTextField tfBis = new DateTextField();
	JButton buUebernehmen = new JButton( "Ändern" );
	JButton buBeenden = new JButton( "Beenden" );
  Haushaltsjahr haushaltsjahr = null;
	
	
	public HaushaltsjahrAendern(ApplicationServer applicationServer) {
		super( "Haushaltsjahr ändern" );
		this.setClosable(true);
		this.setIconifiable(true);
		this.getContentPane().setLayout( null );
		this.applicationServer = applicationServer;
		
		Setting.setPosAndLoc( this.getContentPane(), panel, 5, 5, 270, 130 );
		panel.setBorder(BorderFactory.createEtchedBorder());
		panel.setLayout( null );		
		
		Setting.setPosAndLoc( panel, new JLabel( "Jahr" ), 10, 10, 50, 16 );
		Setting.setPosAndLoc( panel, tfJahr, 40, 10, 80, 22 );

		Setting.setPosAndLoc( panel, new JLabel( "Von" ), 10, 50, 50, 16 );
		Setting.setPosAndLoc( panel, tfVon, 40, 50, 80, 22 );
		
		Setting.setPosAndLoc( panel, new JLabel( "Bis" ), 150, 50, 50, 16 );
		Setting.setPosAndLoc( panel, tfBis, 180, 50, 80, 22 );
		
		
		Setting.setPosAndLoc( panel, buUebernehmen, 10, 90, 110, 25 );
		buUebernehmen.setIcon(Functions.getEditIcon(getClass()));
		buUebernehmen.addActionListener( this );
		Setting.setPosAndLoc( panel, buBeenden, 150, 90, 110, 25 );
		buBeenden.setIcon(Functions.getCloseIcon(getClass()));
		buBeenden.addActionListener( this );
		
		loadHaushaltsjahr();
		this.setSize( 290, 170 );
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
			ApplicationServer applicationServer = server.getMyApplicationServer();
			PasswordEncrypt pe = new PasswordEncrypt();
			String psw = pe.encrypt(new String("r.driesner").toString());
			applicationServer.login("r.driesner", psw);
			HaushaltsjahrAendern hhj = new HaushaltsjahrAendern(applicationServer);
			desk.add(hhj);
			test.show();
			hhj.show();
		}catch(Exception e){
				System.out.println(e);
		}
  }
}
