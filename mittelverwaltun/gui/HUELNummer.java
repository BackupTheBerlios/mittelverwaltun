package gui;

import javax.swing.*;
import java.awt.event.*;



public class HUELNummer extends JInternalFrame implements ActionListener {
	
	JPanel panel = new JPanel();

  JTextField tfBestellnr = new JTextField();
  JTextField tfHuelnr = new JTextField();
  
  JButton buSuchen = new JButton( "Suchen" );
  JButton buSpeichern = new JButton( "Speichern" );
  JButton buBeenden = new JButton( "Beenden" );
  
  MainFrame frame;

  public HUELNummer( MainFrame frame ) {  	
	super( "HÜL-Nummer eingeben" );
	this.setClosable(true);
	this.setIconifiable(true);
	this.getContentPane().setLayout( null );
	this.frame = frame;
	
	Setting.setPosAndLoc( this.getContentPane(), panel, 5, 5, 340, 125 );
	panel.setBorder(BorderFactory.createEtchedBorder());
	panel.setLayout( null );
	
	Setting.setPosAndLoc( panel, new JLabel( "Bestellnummer :" ), 10, 10, 100, 16 );
	Setting.setPosAndLoc( panel, tfBestellnr, 110, 10, 100, 22 );
	
	Setting.setPosAndLoc( panel, new JLabel( "HÜL-Nummer :" ), 10, 40, 100, 16 );
	Setting.setPosAndLoc( panel, tfHuelnr, 110, 40, 100, 22 );
	
	Setting.setPosAndLoc( panel, buSuchen, 230, 10, 100, 25 );
	buSuchen.addActionListener( this );
	Setting.setPosAndLoc( panel, buSpeichern, 230, 50, 100, 25 );
	buSpeichern.addActionListener( this );
	Setting.setPosAndLoc( panel, buBeenden, 230, 90, 100, 25 );
	buBeenden.addActionListener( this );

	this.setSize( 360, 170 );
  }


  public void actionPerformed(ActionEvent e) {
	this.dispose();
  }
}
