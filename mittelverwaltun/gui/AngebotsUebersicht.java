package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;

import dbObjects.Angebot;

/**
 * <p>Title: Mittelverwaltung - GUI</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Mario Schmitt
 * @version 1.0
 */

public class AngebotsUebersicht extends JDialog implements ActionListener{

  int index = -1;	
  ArrayList angebote;	
	
  JPanel mainPanel = new JPanel();
  JLabel lbAnbieter = new JLabel();
  JTextField tfAnbieter = new JTextField();
  JFormattedTextField tfDatum = new JFormattedTextField();
  JLabel lbSumme = new JLabel();
  JLabel lbDatum = new JLabel();
  CurrencyTextField tfSumme = new CurrencyTextField();
  JButton btBackward = new JButton(Functions.getBackIcon(this.getClass()));
  JButton btForward = new JButton(Functions.getForwardIcon(this.getClass()));
  JScrollPane spPositionen = new JScrollPane();
  PositionsTable posTable;
  
  
  public AngebotsUebersicht(Component parent, String title, boolean modal, ArrayList angebote) {
    
  	super(JOptionPane.getFrameForComponent(parent), title, modal);
    this.setSize(560,245);
    this.setResizable(false);
    
    this.angebote = angebote;
    
    try {
    	
    	ArrayList positionen = new ArrayList();
    	String anbieter = "";
    	Date datum = null;
			
		
    	if (angebote.size() > 0){
    		index = 0;
    		positionen = ((Angebot)angebote.get(index)).getPositionen();
			anbieter = ((Angebot)angebote.get(index)).getAnbieter().getName();
			datum = ((Angebot)angebote.get(index)).getDatum();
    	}
    	
        mainPanel.setLayout(null);
        mainPanel.setBorder(null);
        mainPanel.setBounds(new Rectangle(0, 0, 560, 245));

        lbAnbieter.setFont(new java.awt.Font("Dialog", 1, 11));
        lbAnbieter.setHorizontalAlignment(SwingConstants.LEFT);
        lbAnbieter.setText("Anbieter:");
        lbAnbieter.setBounds(new Rectangle(15, 15, 55, 25));
        
        tfAnbieter.setFont(new java.awt.Font("Dialog", 1, 12));
        tfAnbieter.setEnabled(false);
        tfAnbieter.setBorder(null);
        tfAnbieter.setDisabledTextColor(Color.black);
        tfAnbieter.setEditable(false);
        tfAnbieter.setText(anbieter);
        tfAnbieter.setBounds(new Rectangle(75, 15, 150, 25));
                
        lbDatum.setFont(new java.awt.Font("Dialog", 0, 11));
        lbDatum.setHorizontalAlignment(SwingConstants.RIGHT);
        lbDatum.setHorizontalTextPosition(SwingConstants.RIGHT);
        lbDatum.setText("Angebot vom");
        lbDatum.setBounds(new Rectangle(330, 15, 145, 25));
	    
        tfDatum.setFont(new java.awt.Font("Dialog", 0, 11));
        tfDatum.setEnabled(false);
        tfDatum.setBorder(null);
        tfDatum.setHorizontalAlignment(SwingConstants.RIGHT);
        tfDatum.setDisabledTextColor(Color.black);
        tfDatum.setEditable(false);
        tfDatum.setValue(datum);
        tfDatum.setBounds(new Rectangle(475, 15, 65, 25));
                
        posTable = new PositionsTable(PositionsTable.ANZEIGE,positionen);
                
        spPositionen.setBorder(null);
        spPositionen.setBounds(new Rectangle(15, 50, 525, 120));
        spPositionen.getViewport().add(posTable, null);

        lbSumme.setFont(new java.awt.Font("Dialog", 1, 11));
	    lbSumme.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbSumme.setText("Angebotssumme (Brutto)");
	    lbSumme.setBounds(new Rectangle(260, 175, 145, 25));
	
	    tfSumme.setFont(new java.awt.Font("Dialog", 1, 12));
	    tfSumme.setBackground(UIManager.getColor("Viewport.background"));
	    tfSumme.setEnabled(false);
	    //tfSumme.setBorder(null);
	    tfSumme.setDisabledTextColor(Color.black);
	    tfSumme.setValue(new Float(posTable.getOrderSum()));
	    tfSumme.setHorizontalAlignment(SwingConstants.RIGHT);
	    tfSumme.setBounds(new Rectangle(410, 175, 130, 25));        
        
        btBackward.setBounds(new Rectangle(15, 175, 50, 25));
        btBackward.setFont(new java.awt.Font("Dialog", 1, 11));
        btBackward.setActionCommand("backward");
        btBackward.addActionListener(this);
        
        btForward.setBounds(new Rectangle(70, 175, 50, 25));
        btForward.setFont(new java.awt.Font("Dialog", 1, 11));
        btForward.setActionCommand("forward");
        btForward.addActionListener(this);

        if (angebote.size() <2 ){
        	 btBackward.setEnabled(false);
        	 btForward.setEnabled(false);
        }
        
        mainPanel.add(lbAnbieter, null);
        mainPanel.add(tfAnbieter, null);
        mainPanel.add(lbDatum, null);
        mainPanel.add(tfDatum, null);
        mainPanel.add(spPositionen, null);
        mainPanel.add(lbSumme, null);
        mainPanel.add(tfSumme, null);
        mainPanel.add(btForward, null);
        mainPanel.add(btBackward, null);

        this.getContentPane().setLayout(null);
        this.getContentPane().add(mainPanel, null);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  
  public int getNextIndexBwd(int index){
  	if (angebote.size()>0){
  		index--;
  	  	if (index < 0) index = angebote.size() - 1;
  	  	return index;
  	} else return -1;
  }

  public int getNextIndexFwd(int index){
  	if (angebote.size()>0){
  		index++;
  	  	if (index >= angebote.size()) index = 0;
  	  	return index;
  	} else return -1;
  }

public void actionPerformed(ActionEvent e) {
	if (e.getActionCommand()=="backward"){
		posTable.setPositions(((Angebot)angebote.get(index = getNextIndexBwd(index))).getPositionen());
		tfAnbieter.setText(((Angebot)angebote.get(index)).getAnbieter().getName());
		tfDatum.setValue(((Angebot)angebote.get(index)).getDatum());
		tfSumme.setValue(new Float(posTable.getOrderSum()));
	}else if (e.getActionCommand()=="forward"){
		posTable.setPositions(((Angebot)angebote.get(index = getNextIndexFwd(index))).getPositionen());
		tfAnbieter.setText(((Angebot)angebote.get(index)).getAnbieter().getName());
		tfDatum.setValue(((Angebot)angebote.get(index)).getDatum());
		tfSumme.setValue(new Float(posTable.getOrderSum()));
	}
	
}
  
}