package gui;

import javax.swing.*;
import java.awt.event.*;


public class MittelUebertrag extends JInternalFrame implements ActionListener {
	
	JPanel panel = new JPanel();

	JScrollPane scrollKonten = new JScrollPane();
	JList listKonten = new JList();

	JTextField tfBezeichnung = new JTextField();
	JTextField tfKapitel = new JTextField();
	JTextField tfTitelgruppe = new JTextField();
	JTextField tfTitel = new JTextField();
	JTextField tfUntertitel = new JTextField();
	JTextField tfBetrag = new JTextField();
 
	ButtonGroup buGroup = new ButtonGroup();
	JRadioButton rbAlles = new JRadioButton( "Alles" );
	JRadioButton rbTeilbetrag = new JRadioButton( "Teilbetrag" );
	JTextField tfTeilbetrag = new JTextField();
 
	JButton buUebernehmen = new JButton( "Übernehmen" );
	JButton buVerwerfen = new JButton( "Verwerfen" );
	JButton buAbbrechen = new JButton( "Abbrechen" );
	JButton buBeenden = new JButton( "Beenden" );
  
	MainFrame frame;

	public MittelUebertrag( MainFrame frame ) {
	  super( "Mittelübertrag aus vergangenem Jahr" );
	  this.setClosable(true);
	  this.setIconifiable(true);
	  this.getContentPane().setLayout( null );
	  this.frame = frame;
	
	  Setting.setPosAndLoc( this.getContentPane(), panel, 5, 5, 530, 280 );
	  panel.setBorder(BorderFactory.createEtchedBorder());
	  panel.setLayout( null );
	
	  Setting.setPosAndLoc( panel, new JLabel( "ZV-Konten" ), 10, 10, 150, 16 );
	  Setting.setPosAndLoc( panel, scrollKonten, 10, 30, 150, 150 );
	  scrollKonten.getViewport().add( listKonten, null );

	  Setting.setPosAndLoc( panel, rbAlles, 250, 30, 150, 25 );
	  Setting.setPosAndLoc( panel, rbTeilbetrag, 250, 95, 150, 25 );
	  Setting.setPosAndLoc( panel, tfTeilbetrag, 250, 150, 150, 25 );
	  buGroup.add(rbAlles);
	  rbAlles.setSelected(true);
	  buGroup.add(rbTeilbetrag);
	
	  Setting.setPosAndLoc( panel, new JLabel( "Bezeichnung" ), 170, 30, 80, 16 );
	  Setting.setPosAndLoc( panel, tfBezeichnung, 250, 30, 100, 22 );
	
	  Setting.setPosAndLoc( panel, new JLabel( "Kapitel" ), 170, 62, 80, 16 );
	  Setting.setPosAndLoc( panel, tfKapitel, 250, 62, 100, 22 );
	
	  Setting.setPosAndLoc( panel, new JLabel( "ZV-Titelgruppe" ), 170, 94, 80, 16 );
	  Setting.setPosAndLoc( panel, tfTitelgruppe, 250, 94, 100, 22 );
	
	  Setting.setPosAndLoc( panel, new JLabel( "Titel" ), 170, 126, 80, 16 );
	  Setting.setPosAndLoc( panel, tfTitel, 250, 126, 100, 22 );
	
	  Setting.setPosAndLoc( panel, new JLabel( "Untertitel" ), 170, 158, 80, 16 );
	  Setting.setPosAndLoc( panel, tfUntertitel, 250, 158, 100, 22 );

	  Setting.setPosAndLoc( panel, new JLabel( "Betrag" ), 370, 30, 50, 16 );
	  Setting.setPosAndLoc( panel, tfBetrag, 420, 30, 100, 22 );
	
	  Setting.setPosAndLoc( panel, rbTeilbetrag, 370, 104, 150, 25 );
	  rbTeilbetrag.addActionListener( this );
	  Setting.setPosAndLoc( panel, rbAlles, 370, 75, 150, 25 );
	  rbAlles.addActionListener( this );
	  Setting.setPosAndLoc( panel, tfTeilbetrag, 370, 158, 150, 22 );
	  buGroup.add(rbAlles);
	  buGroup.add(rbTeilbetrag);
	  rbAlles.setSelected(true);
	  tfTeilbetrag.setEnabled(false);

	  Setting.setPosAndLoc( panel, buVerwerfen, 10, 200, 200, 25 );
	  buVerwerfen.addActionListener( this );
	  Setting.setPosAndLoc( panel, buUebernehmen, 320, 200, 200, 25 );
	  buUebernehmen.addActionListener( this );
	  Setting.setPosAndLoc( panel, buAbbrechen, 10, 240, 200, 25 );
	  buAbbrechen.addActionListener( this );
	  Setting.setPosAndLoc( panel, buBeenden, 320, 240, 200, 25 );
	  buBeenden.addActionListener( this );
		
	  this.setSize( 550, 325 );

	}

	public void actionPerformed(ActionEvent e) {
	  if ( e.getSource() == rbTeilbetrag ) {
		  tfTeilbetrag.setEnabled(true);
	  } else if ( e.getSource() == rbAlles ) {
		  tfTeilbetrag.setEnabled(false);
	  } else if ( e.getSource() == buVerwerfen ) {
	  } else if ( e.getSource() == buUebernehmen ) {
	  } else if ( e.getSource() == buAbbrechen ) {
	  } else if ( e.getSource() == buBeenden ) {
		  this.dispose();
	  }
	}

}
