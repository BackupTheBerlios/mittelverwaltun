/* Created on 18.04.2005 */

package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Klasse InfoFrame. Anzeigen vom Infofenster über die Entwickler.
 * @author w.flat
 */
public class InfoFrame extends JInternalFrame implements ActionListener {
    
    JLabel labFHLogo = null;
    JLabel labMS = new JLabel();
    JLabel labRD = new JLabel();
    JLabel labWF = new JLabel();
    JLabel labVorlesung = new JLabel();
    JLabel labFHMannheim = new JLabel();
    JLabel labDesigned = new JLabel();
    JButton butBeenden = new JButton();
    
    final static int WIDTH = 323;
    final static int HEIGHT = 288;

    /**
     * Konstruktor zum Erstellen des Info-Fensters. 
     */
    public InfoFrame(MainFrame frame) {
		super( "Information" );
		this.setClosable( true );
		this.setResizable(false);
		this.getContentPane().setLayout( null );
        try {
            jbInit();
        } catch(Exception e) {
            e.printStackTrace();
        }
		setLocation((frame.getWidth() - WIDTH) / 2, (frame.getHeight()- HEIGHT)/2);
		this.setSize( WIDTH, HEIGHT );
    }
    
    /**
     * Initialisierung der graphischen Oberfläche. 
     * @throws Exception
     */
    private void jbInit() throws Exception {
        labDesigned.setText("Designed by :");
        labDesigned.setBounds(new Rectangle(20, 150, 100, 16));
        labFHMannheim.setText("FH-Mannheim SS2005");
        labFHMannheim.setBounds(new Rectangle(20, 100, 200, 16));
        labVorlesung.setText("Vorlesung OO2 + DBA");
        labVorlesung.setBounds(new Rectangle(20, 120, 200, 16));
        labWF.setText("Waldemar Flat");
        labWF.setBounds(new Rectangle(120, 170, 150, 15));
        labWF.setRequestFocusEnabled(true);
        labWF.setFont(new java.awt.Font("Dialog", 0, 11));
        labRD.setText("Robert Driesner");
        labRD.setBounds(new Rectangle(120, 150, 150, 15));
        labRD.setFont(new java.awt.Font("Dialog", 0, 11));
        labMS.setText("Mario Schmitt");
        labMS.setBounds(new Rectangle(120, 190, 150, 15));
        labMS.setFont(new java.awt.Font("Dialog", 0, 11));
		labFHLogo = new JLabel(Functions.getFHLogo(getClass()), JLabel.CENTER);
		labFHLogo.setText("");
        labFHLogo.setBounds(new Rectangle(10, 10, 295, 80));
        
        butBeenden.setBounds(new Rectangle(35, 220, 250, 25));
        butBeenden.setText("Schließen");
        butBeenden.addActionListener( this );
        butBeenden.setIcon(Functions.getCloseIcon(getClass()));
        
        this.getContentPane().setLayout(null);
        this.getContentPane().add(labFHLogo, null);
        this.getContentPane().add(labMS, null);
        this.getContentPane().add(labRD, null);
        this.getContentPane().add(labWF, null);
        this.getContentPane().add(labVorlesung, null);
        this.getContentPane().add(labFHMannheim, null);
        this.getContentPane().add(labDesigned, null);
        this.getContentPane().add(butBeenden, null);
    }

    /**
     * Reaktion auf den Beenden-Button.
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == butBeenden) {
            this.dispose();
        }
    }

}
