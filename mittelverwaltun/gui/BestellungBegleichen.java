package gui;

import java.awt.event.*;
import javax.swing.*;


public class BestellungBegleichen extends JInternalFrame implements ActionListener {
	
	JPanel panel = new JPanel();
  
	JTextField tfBestellnr = new JTextField();
	JLabel labBestellsumme = new JLabel();
	JLabel labRestbetrag = new JLabel();
	JTextField tfBetrag = new JTextField();
  
	JButton buSuche = new JButton( "Suche" );
	JButton buAnzeigen = new JButton( "Anzeigen" );
	JButton buBegleichen = new JButton( "Begleichen" );
	JButton buBeenden = new JButton( "Beenden" );

	MainFrame frame;
	
	public BestellungBegleichen( MainFrame frame ) {
	  super( "Bestellung begleichen" );
	  this.setClosable(true);
	  this.setIconifiable(true);
	  this.getContentPane().setLayout( null );
	  this.frame = frame;
	
	  Setting.setPosAndLoc( this.getContentPane(), panel, 5, 5, 350, 165 );
	  panel.setBorder(BorderFactory.createEtchedBorder());
	  panel.setLayout( null );
	
	  Setting.setPosAndLoc( panel, new JLabel( "Bestellnummer :" ), 10, 10, 100, 16 );
	  Setting.setPosAndLoc( panel, tfBestellnr, 120, 10, 100, 22 );
	
	  Setting.setPosAndLoc( panel, new JLabel( "Bestellsumme :" ), 10, 50, 100, 16 );
	  Setting.setPosAndLoc( panel, labBestellsumme, 120, 50, 100, 16 );

	  Setting.setPosAndLoc( panel, new JLabel( "Restbetrag :" ), 10, 90, 100, 16 );
	  Setting.setPosAndLoc( panel, labRestbetrag, 120, 90, 100, 16 );

	  Setting.setPosAndLoc( panel, new JLabel( "Betrag :" ), 10, 130, 100, 16 );
	  Setting.setPosAndLoc( panel, tfBetrag, 120, 130, 100, 22 );
	
	  Setting.setPosAndLoc( panel, buSuche, 240, 10, 100, 25 );
	  buSuche.addActionListener( this );
	  Setting.setPosAndLoc( panel, buAnzeigen, 240, 50, 100, 25 );
	  buAnzeigen.addActionListener( this );
	  Setting.setPosAndLoc( panel, buBegleichen, 240, 90, 100, 25 );
	  buBegleichen.addActionListener( this );
	  Setting.setPosAndLoc( panel, buBeenden, 240, 130, 100, 25 );
	  buBeenden.addActionListener( this );

	  this.setSize( 370, 210 );
	}

	public void actionPerformed(ActionEvent e) {
	  if ( e.getSource() == buSuche ) {
	  } else if ( e.getSource() == buAnzeigen ) {
	  } else if ( e.getSource() == buBegleichen ) {
	  } else if ( e.getSource() == buBeenden ) {
		  this.dispose();
	  }	
	}

}
