package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InstBudgetUmbuchen extends JInternalFrame implements ActionListener {
	JPanel panel = new JPanel();
	JLabel labTemp = new JLabel();
	
	JScrollPane scrollInstituteVon = new JScrollPane();
	JList listInstituteVon = new JList();
  
	JScrollPane scrollInstituteNach = new JScrollPane();
	JList listInstituteNach = new JList();

	JComboBox cbHauptkontenNach = new JComboBox();
	JComboBox cbUnterkontenNach = new JComboBox();
  
	JComboBox cbHauptkontenVon = new JComboBox();
	JComboBox cbUnterkontenVon = new JComboBox();  


	JTextField tfFBKontoVon = new JTextField();
	JTextField tfFBKontoNach = new JTextField();
	JTextField tfBetrag = new JTextField();
  
	JButton buBeenden = new JButton( "Beenden" );
	JButton buUmbuchen = new JButton( "Umbuchen" );
  
	MainFrame frame;

	public InstBudgetUmbuchen( MainFrame frame ) {
		super( "Institutsbudget umbuchen" );
		this.setClosable(true);
		this.setIconifiable(true);
		this.getContentPane().setLayout( null );
		this.frame = frame;
	
		Setting.setPosAndLoc( this.getContentPane(), panel, 5, 5, 450, 455 );
		panel.setBorder(BorderFactory.createEtchedBorder());
		panel.setLayout( null );
	
		Setting.setPosAndLoc( panel, labTemp = new JLabel( "Von :" ), 10, 10, 100, 16 );
		labTemp.setFont( new Font("Dialog", 1, 14) );
		Setting.setPosAndLoc( panel, new JLabel( "Institute" ), 10, 30, 100, 16 );
		Setting.setPosAndLoc( panel, scrollInstituteVon, 10, 60, 200, 120 );
		scrollInstituteVon.getViewport().add( listInstituteVon, null );
	
	
		Setting.setPosAndLoc( panel, labTemp = new JLabel( "Nach :" ), 240, 10, 100, 16 );
		labTemp.setFont( new Font("Dialog", 1, 14) );
		Setting.setPosAndLoc( panel, new JLabel( "Institute" ), 240, 30, 100, 16 );
		Setting.setPosAndLoc( panel, scrollInstituteNach, 240, 60, 200, 120 );
		scrollInstituteNach.getViewport().add( listInstituteNach, null );
	

		Setting.setPosAndLoc( panel, new JLabel( "Hauptkonten" ), 10, 200, 100, 16 );
		Setting.setPosAndLoc( panel, cbHauptkontenVon, 10, 220, 150, 22 );
		Setting.setPosAndLoc( panel, new JLabel( "Unterkonten" ), 10, 250, 100, 16 );
		Setting.setPosAndLoc( panel, cbUnterkontenVon, 10, 270, 150, 22 );

	
		Setting.setPosAndLoc( panel, new JLabel( "Hauptkonten" ), 240, 200, 100, 16 );
		Setting.setPosAndLoc( panel, cbHauptkontenNach, 240, 220, 150, 22 );
		Setting.setPosAndLoc( panel, new JLabel( "Unterkonten" ), 240, 250, 100, 16 );	
		Setting.setPosAndLoc( panel, cbUnterkontenNach, 240, 270, 150, 22 );
	
	
	
		Setting.setPosAndLoc( panel, labTemp = new JLabel( "Von :" ), 10, 310, 100, 16 );
		labTemp.setFont(new java.awt.Font("Dialog", 1, 14));
		Setting.setPosAndLoc( panel, labTemp = new JLabel( "Nach :" ), 240, 310, 100, 16 );
		labTemp.setFont(new java.awt.Font("Dialog", 1, 14));
		
		Setting.setPosAndLoc( panel, new JLabel( "FB-Konto :" ), 10, 340, 90, 16 );
		Setting.setPosAndLoc( panel, tfFBKontoVon, 110, 340, 100, 22 );	
		Setting.setPosAndLoc( panel, new JLabel( "FB-Konto :" ), 240, 340, 90, 16 );
		Setting.setPosAndLoc( panel, tfFBKontoNach, 340, 340, 100, 22 );
	
		Setting.setPosAndLoc( panel, new JLabel( "Betrag :" ), 10, 380, 100, 16 );
		Setting.setPosAndLoc( panel, tfBetrag, 110, 380, 330, 22 );	
	
		Setting.setPosAndLoc( panel, buUmbuchen, 10, 420, 200, 25 );
		buUmbuchen.addActionListener( this );
		Setting.setPosAndLoc( panel, buBeenden, 240, 420, 200, 25 );
		buBeenden.addActionListener( this );
	
		setSize( 470, 500 );
	}

	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == buUmbuchen ) {
		} else if ( e.getSource() == buBeenden ) {
		  this.dispose();
		}
	}

}
