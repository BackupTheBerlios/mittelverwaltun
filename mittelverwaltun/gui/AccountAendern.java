package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class AccountAendern extends JInternalFrame implements ActionListener {

	JTextField tfName = new JTextField();
	JTextField tfVorname = new JTextField();
	JTextField tfBenutzername = new JTextField();

	JTextField tfEMail = new JTextField();
	JTextField tfKonto = new JTextField();	
	JTextField tfInstitut = new JTextField();

	JTextField tfAltes = new JTextField();
	JTextField tfNeues2 = new JTextField();
	JTextField tfNeues1 = new JTextField();

	JButton buBeenden = new JButton();
	JButton buUebernehmen = new JButton();

	Container contentPane;
	MainFrame frame;

	public AccountAendern( MainFrame frame ) {
		super( "Account ändern" );
		this.getContentPane().setLayout( null );
		this.setClosable(true);
		this.setIconifiable( true );
		contentPane = this.getContentPane();
		this.frame = frame;
	  	
	  	// Labels erste Spalte anzeigen
		Setting.setPosAndLoc( contentPane, new JLabel( "Name" ), 10, 10, 90, 16 );
		Setting.setPosAndLoc( contentPane, new JLabel( "Vorname" ), 10, 50, 90, 16 );
		Setting.setPosAndLoc( contentPane, new JLabel( "Benutzername" ), 10, 90, 90, 16 );
		// dazugehörigen Textfelder
		Setting.setPosAndLoc( contentPane, tfName = new JTextField(), 110, 10, 100, 22 );
		Setting.setPosAndLoc( contentPane, tfVorname = new JTextField(), 110, 50, 100, 22 );
		Setting.setPosAndLoc( contentPane, tfBenutzername = new JTextField(), 110, 90, 100, 22 );
		
		// Labels zweite Spalte
		Setting.setPosAndLoc( contentPane, new JLabel( "E-Mail" ), 230, 10, 90, 16 );
		Setting.setPosAndLoc( contentPane, new JLabel( "Konto" ), 230, 50, 90, 16 );
		Setting.setPosAndLoc( contentPane, new JLabel( "Institut" ), 230, 90, 90, 16 );
		// dazugehörigen Textfelder
		Setting.setPosAndLoc( contentPane, tfEMail = new JTextField(), 330, 10, 100, 22 );
		Setting.setPosAndLoc( contentPane, tfKonto = new JTextField(), 330, 50, 100, 22 );
		Setting.setPosAndLoc( contentPane, tfInstitut = new JTextField(), 330, 90, 100, 22 );
		
		// Labels vom Passwort
		Setting.setPosAndLoc( contentPane, new JLabel( "Passwort" ), 10, 140, 80, 16 );	
		Setting.setPosAndLoc( contentPane, new JLabel( "Altes" ), 90, 140, 60, 16 );
		Setting.setPosAndLoc( contentPane, new JLabel( "Neues" ), 90, 180, 60, 16 );
		// dazugehörigen Textfelder
		Setting.setPosAndLoc( contentPane, tfAltes = new JTextField(), 150, 140, 130, 22 );
		Setting.setPosAndLoc( contentPane, tfNeues1 = new JTextField(), 150, 180, 130, 22 );
		Setting.setPosAndLoc( contentPane, tfNeues2 = new JTextField(), 300, 180, 130, 22 );


		Setting.setPosAndLoc( contentPane, buUebernehmen = new JButton( "Übernehmen" ), 10, 230, 150, 25 );
		buUebernehmen.addActionListener( this );
		Setting.setPosAndLoc( contentPane, buBeenden = new JButton( "Beenden" ), 280, 230, 150, 25 );
		buBeenden.addActionListener( this );
		
		this.setSize( 450, 300);

	}
	
	
	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == buUebernehmen ) {
		} else if ( e.getSource() == buBeenden ) {
			this.dispose();
		}
	}
}