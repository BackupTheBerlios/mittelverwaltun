package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class BestellungStorno extends JInternalFrame implements ActionListener {
	
	JPanel panel = new JPanel(); 
	JScrollPane scrollPane1 = new JScrollPane();
  
	JTable tableBestellung = new JTable(){
		public Class getColumnClass(int column) {
		  return getValueAt(0, column).getClass();
		}

		public Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column
			) {
		  return new JButton("Ändern");
	}

	};

	JLabel labMWST = new JLabel();
	JLabel labSumme = new JLabel();
  
  
	JTextField tfBestellnr = new JTextField();
  
	JButton buSuche = new JButton( "Suche" );
	JButton buBeenden = new JButton( "Beenden" );
	JButton buAendern = new JButton( "Ändern" );
	JButton buStorno = new JButton( "Stornieren" );
  
	MainFrame frame;

	public BestellungStorno( MainFrame frame ) {

	  super( "Bestellung ändern/stornieren" );
	  this.setClosable(true);
	  this.setIconifiable(true);
	  this.getContentPane().setLayout( null );
	  this.frame = frame;
	
	  Setting.setPosAndLoc( this.getContentPane(), panel, 5, 5, 430, 285 );
	  panel.setBorder(BorderFactory.createEtchedBorder());
	  panel.setLayout( null );
	
	  Setting.setPosAndLoc( panel, new JLabel( "Bestellnummer :" ), 10, 10, 100, 16 );
	  Setting.setPosAndLoc( panel, tfBestellnr, 120, 10, 100, 22 );
	
	  Setting.setPosAndLoc( panel, scrollPane1, 10, 40, 210, 200 );
	  scrollPane1.getViewport().add( tableBestellung, null );


	  Setting.setPosAndLoc( panel, new JLabel( "MwSt. :" ), 270, 190, 50, 16 );
	  Setting.setPosAndLoc( panel, labMWST, 320, 190, 100, 16 );
	
	  Setting.setPosAndLoc( panel, new JLabel( "Summe :" ), 270, 220, 50, 16 );
	  Setting.setPosAndLoc( panel, labSumme, 320, 220, 100, 16 );


	  Setting.setPosAndLoc( panel, buBeenden, 10, 250, 120, 25 );
	  buBeenden.addActionListener( this );
	  Setting.setPosAndLoc( panel, buAendern, 155, 250, 120, 25 );
	  buAendern.addActionListener( this );
	  Setting.setPosAndLoc( panel, buStorno, 300, 250, 120, 25 );
	  buStorno.addActionListener( this );

	  this.setSize( 450, 330 );

	}

	public void actionPerformed(ActionEvent e) {
	  if ( e.getSource() == buAendern ) {
	  } else if ( e.getSource() == buStorno ) {
	  } else if ( e.getSource() == buBeenden ) {
		  this.dispose();
	  }
	}
}
