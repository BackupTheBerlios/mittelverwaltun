package gui;

import javax.swing.*;
import java.awt.event.*;


public class HaushaltsjahrAnlegen extends JInternalFrame implements ActionListener {
	
	JPanel panel = new JPanel();

	JScrollPane scrollZVKonten = new JScrollPane();
	JList listZVKonten = new JList();
  
	ButtonGroup buGroup = new ButtonGroup();
	JRadioButton rbUebernehmen = new JRadioButton( "‹bernehmen" );
	JRadioButton rbBeantragt = new JRadioButton( "Beantragt" );
	JRadioButton rbBeantragtGenehmigt = new JRadioButton( "Beantragt/Genehmigt" );
	JRadioButton rbVerwerfen = new JRadioButton( "Verwerfen" );
  
	JButton buUebernehmen = new JButton( "‹bernehmen" );
	JButton buAnlegen = new JButton( "Abschlieﬂen/Anlegen" );
	JButton buAbbrechen = new JButton( "Abbrechen" );
	JButton buBeenden = new JButton( "Beenden" );

	MainFrame frame;

	public HaushaltsjahrAnlegen( MainFrame frame ) {
	  super( "Haushaltsjahr abschlieﬂen/anlegen" );
	  this.setClosable(true);
	  this.setIconifiable(true);
	  this.getContentPane().setLayout( null );
	  this.frame = frame;
	
	  Setting.setPosAndLoc( this.getContentPane(), panel, 5, 5, 435, 340 );
	  panel.setBorder(BorderFactory.createEtchedBorder());
	  panel.setLayout( null );
	
	  Setting.setPosAndLoc( panel, new JLabel( "ZV-Konten" ), 10, 10, 150, 16 );
	  Setting.setPosAndLoc( panel, scrollZVKonten, 10, 30, 200, 200 );
	  scrollZVKonten.getViewport().add( listZVKonten, null );

	  Setting.setPosAndLoc( panel, rbUebernehmen, 250, 30, 150, 25 );
	  Setting.setPosAndLoc( panel, rbBeantragtGenehmigt, 250, 95, 150, 25 );
	  Setting.setPosAndLoc( panel, rbBeantragt, 250, 150, 150, 25 );
	  Setting.setPosAndLoc( panel, rbVerwerfen, 250, 205, 150, 25 );
	  buGroup.add( rbUebernehmen );
	  rbUebernehmen.setSelected( true );
	  buGroup.add( rbBeantragtGenehmigt );
	  buGroup.add( rbBeantragt );
	  buGroup.add( rbVerwerfen );
		

	  Setting.setPosAndLoc( panel, buUebernehmen, 10, 260, 200, 25 );
	  buUebernehmen.addActionListener( this );
	  Setting.setPosAndLoc( panel, buAnlegen, 220, 260, 200, 25 );
	  buBeenden.addActionListener( this );
	  Setting.setPosAndLoc( panel, buAbbrechen, 10, 300, 200, 25 );
	  buAbbrechen.addActionListener( this );
	  Setting.setPosAndLoc( panel, buBeenden, 220, 300, 200, 25 );
	  buBeenden.addActionListener( this );

		
	  this.setSize( 455, 385 );
	}

	public void actionPerformed(ActionEvent e) {
	  if ( e.getSource() == buAnlegen ) {
	  } else if ( e.getSource() == buUebernehmen ) {
	  } else if ( e.getSource() == buAbbrechen ) {
	  } else if ( e.getSource() == buBeenden ) {
		  this.dispose();
	  }
	}
}
