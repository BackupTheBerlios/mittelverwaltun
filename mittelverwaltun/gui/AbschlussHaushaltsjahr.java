package gui;

import javax.swing.*;

import applicationServer.ApplicationServerException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;


public class AbschlussHaushaltsjahr extends JInternalFrame implements ActionListener {
  
  // Dialogelemente	
  JPanel pnHeader = new JPanel();
  JPanel pnSeparator = new JPanel();
  JLabel lbHeadline = new JLabel(Functions.getPropertiesIcon(this.getClass()));
  JLabel lbHeadline1B = new JLabel();
  JButton btClose = new JButton(Functions.getCloseIcon(this.getClass()));
  JButton btApply = new JButton(Functions.getImportIcon(this.getClass()));
  JButton btForward = new JButton(Functions.getForwardIcon(this.getClass()));
  JButton btBackward = new JButton(Functions.getBackIcon(this.getClass()));
  JLabel lbHeadline2B = new JLabel();
  JLabel lbHeadline3B = new JLabel();
  JLabel lbHeadline4B = new JLabel();
  JLabel lbHeadline1 = new JLabel();
  JLabel lbHeadline2 = new JLabel();
  JLabel lbHeadline3 = new JLabel();
  JLabel lbHeadline4 = new JLabel();
  JLabel lbHeadline5B = new JLabel();
  JLabel lbHeadline5 = new JLabel();
  JScrollPane spContent1 = new JScrollPane();
  JScrollPane spContent2 = new JScrollPane();
  JScrollPane spContent3 = new JScrollPane();
  JScrollPane spContent4 = new JScrollPane();
  JScrollPane spContent5 = new JScrollPane();
  JTextArea taContent5 = new JTextArea();
  AccountTable taContent1 = null;
  
  
  // Sonstige Attribute
  MainFrame frame = null;
  int layer = 0;

  public AbschlussHaushaltsjahr(MainFrame frame) {
    
  	this.frame = frame;
  	
  	this.setSize(760, 460);
  	this.getContentPane().setLayout(null);

    pnHeader.setBackground(Color.white);
    pnHeader.setForeground(Color.black);
    pnHeader.setBorder(null);
    pnHeader.setBounds(new Rectangle(0, 10, 750, 55));
    pnHeader.setLayout(null);

    lbHeadline.setFont(new java.awt.Font("Dialog", 1, 14));
    lbHeadline.setForeground(Color.gray);
    //lbHeadline.setIcon(null);
    //lbHeadline.setIconTextGap(4);
    lbHeadline.setText("Abschluss Haushaltsjahr");
    lbHeadline.setBounds(new Rectangle(5, 5, 210, 25));

    pnHeader.add(lbHeadline, null);

    lbHeadline1B.setBounds(new Rectangle(5, 30, 150, 20));
    lbHeadline1B.setText("Zentralverwaltungskonten");
    lbHeadline1B.setForeground(Color.darkGray);
    lbHeadline1B.setHorizontalAlignment(SwingConstants.CENTER);
    lbHeadline1B.setFont(new java.awt.Font("Dialog", 1, 12));
    //lbHeadline1B.setVisible(true);

    pnHeader.add(lbHeadline1B, null);

    lbHeadline1.setFont(new java.awt.Font("Dialog", 0, 12));
    lbHeadline1.setForeground(Color.darkGray);
    lbHeadline1.setText("Zentralverwaltungskonten");
    lbHeadline1.setBounds(new Rectangle(5, 30, 150, 20));
    lbHeadline1.setHorizontalAlignment(SwingConstants.CENTER);
    //lbHeadline1.setVisible(false);

    pnHeader.add(lbHeadline1, null);

    lbHeadline2B.setFont(new java.awt.Font("Dialog", 1, 12));
    lbHeadline2B.setForeground(Color.darkGray);
    lbHeadline2B.setHorizontalAlignment(SwingConstants.CENTER);
    lbHeadline2B.setText("Fachbereichskonten");
    lbHeadline2B.setBounds(new Rectangle(158, 30, 118, 20));
    //lbHeadline2B.setVisible(false);

    pnHeader.add(lbHeadline2B, null);

    lbHeadline2.setBounds(new Rectangle(158, 30, 118, 20));
    lbHeadline2.setText("Fachbereichskonten");
    lbHeadline2.setHorizontalAlignment(SwingConstants.CENTER);
    lbHeadline2.setForeground(Color.darkGray);
    lbHeadline2.setFont(new java.awt.Font("Dialog", 0, 12));
    //lbHeadline2.setVisible(true);

    pnHeader.add(lbHeadline2, null);

    lbHeadline3B.setFont(new java.awt.Font("Dialog", 1, 12));
    lbHeadline3B.setForeground(Color.darkGray);
    lbHeadline3B.setHorizontalAlignment(SwingConstants.CENTER);
    lbHeadline3B.setText("Kontenzuordnungen");
    lbHeadline3B.setBounds(new Rectangle(279, 30, 116, 20));
    //lbHeadline3B.setVisible(false);

    pnHeader.add(lbHeadline3B, null);

    lbHeadline3.setBounds(new Rectangle(279, 30, 116, 20));
    lbHeadline3.setText("Kontenzuordnungen");
    lbHeadline3.setForeground(Color.darkGray);
    lbHeadline3.setFont(new java.awt.Font("Dialog", 0, 12));
    lbHeadline3.setHorizontalAlignment(SwingConstants.CENTER);
    //lbHeadline3.setVisible(true);

    pnHeader.add(lbHeadline3, null);

    lbHeadline4B.setBounds(new Rectangle(398, 30, 76, 20));
    lbHeadline4B.setText("Bestellungen");
    lbHeadline4B.setForeground(Color.darkGray);
    lbHeadline4B.setHorizontalAlignment(SwingConstants.CENTER);
    lbHeadline4B.setFont(new java.awt.Font("Dialog", 1, 12));
    //lbHeadline4B.setVisible(false);

    pnHeader.add(lbHeadline4B, null);

    lbHeadline4.setFont(new java.awt.Font("Dialog", 0, 12));
    lbHeadline4.setHorizontalAlignment(SwingConstants.CENTER);
    lbHeadline4.setForeground(Color.darkGray);
    lbHeadline4.setText("Bestellungen");
    lbHeadline4.setBounds(new Rectangle(398, 30, 76, 20));
    //lbHeadline4.setVisible(true);

    pnHeader.add(lbHeadline4, null);

    lbHeadline5B.setFont(new java.awt.Font("Dialog", 1, 12));
    lbHeadline5B.setHorizontalAlignment(SwingConstants.CENTER);
    lbHeadline5B.setForeground(Color.darkGray);
    lbHeadline5B.setText("Abschluss");
    lbHeadline5B.setBounds(new Rectangle(477, 30, 64, 20));
    //lbHeadline5B.setVisible(false);

    pnHeader.add(lbHeadline5B, null);

    lbHeadline5.setBounds(new Rectangle(477, 30, 64, 20));
    lbHeadline5.setText("Abschluss");
    lbHeadline5.setForeground(Color.darkGray);
    lbHeadline5.setHorizontalAlignment(SwingConstants.CENTER);
    lbHeadline5.setFont(new java.awt.Font("Dialog", 0, 12));
    //lbHeadline5.setVisible(true);

    pnHeader.add(lbHeadline5, null);

    spContent1.setBounds(new Rectangle(5, 75, 740, 300));
    try {
		taContent1 = new AccountTable(frame.applicationServer.getOffeneZVKonten(frame.applicationServer.getCurrentHaushaltsjahrId()));
	} catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ApplicationServerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
	spContent1.getViewport().add(taContent1, null);
    spContent2.setBounds(new Rectangle(5, 75, 740, 300));
    
    
    
    //spContent2.setVisible(false);

    spContent3.setBounds(new Rectangle(5, 75, 740, 300));
    //spContent3.setVisible(false);

    spContent4.setBounds(new Rectangle(5, 75, 740, 300));
    //spContent4.setVisible(false);

    spContent5.setBounds(new Rectangle(5, 75, 740, 300));
    //spContent5.setVisible(false);

    taContent5.setFont(new java.awt.Font("Dialog", 1, 11));
    taContent5.setText("\n\tZusammenfassung\n\t==================");
    spContent5.getViewport().add(taContent5, null);

    pnSeparator.setBorder(BorderFactory.createLineBorder(Color.gray));
    pnSeparator.setBounds(new Rectangle(5, 385, 740, 2));

    btClose.setBounds(new Rectangle(620, 395, 125, 25));
    btClose.setFont(new java.awt.Font("Dialog", 1, 11));
    btClose.setActionCommand("dispose");
    btClose.setText("Beenden");
    btClose.addActionListener(this);
    
    btApply.setBounds(new Rectangle(490, 395, 125, 25));
    btApply.setEnabled(false);
    btApply.setFont(new java.awt.Font("Dialog", 1, 11));
    btApply.setActionCommand("execute");
    btApply.setText("Fertigstellen");
    btApply.addActionListener(this);

    btForward.setText("Weiter");
    btForward.setBounds(new Rectangle(385, 395, 100, 25));
    btForward.setFont(new java.awt.Font("Dialog", 1, 11));
    btForward.setActionCommand("forward");
    btForward.addActionListener(this);
    
    btBackward.setFont(new java.awt.Font("Dialog", 1, 11));
    btBackward.setBounds(new Rectangle(280, 395, 100, 25));
    btBackward.setEnabled(false);
    btBackward.setText("Zurück");
    btBackward.setActionCommand("backward");
    btBackward.addActionListener(this);
    
    this.getContentPane().add(pnHeader, null);
    this.getContentPane().add(spContent1, null);
    this.getContentPane().add(spContent2, null);
    this.getContentPane().add(spContent3, null);
    this.getContentPane().add(spContent4, null);
    this.getContentPane().add(spContent5, null);
    this.getContentPane().add(pnSeparator, null);
    this.getContentPane().add(btClose, null);
    this.getContentPane().add(btApply, null);
    this.getContentPane().add(btForward, null);
    this.getContentPane().add(btBackward, null);
    
    this.layer = 1;
    updateView();
  }

  private void updateView(){
  	
  	lbHeadline1B.setVisible(layer == 1);
  	lbHeadline1.setVisible(layer != 1);
  	spContent1.setVisible(layer == 1);
  	
  	lbHeadline2B.setVisible(layer == 2);
  	lbHeadline2.setVisible(layer != 2);
  	spContent2.setVisible(layer == 2);
  	
  	lbHeadline3B.setVisible(layer == 3);
  	lbHeadline3.setVisible(layer != 3);
  	spContent3.setVisible(layer == 3);
  	
  	lbHeadline4B.setVisible(layer == 4);
  	lbHeadline4.setVisible(layer != 4);
  	spContent4.setVisible(layer == 4);
  
  	lbHeadline5B.setVisible(layer == 5);
  	lbHeadline5.setVisible(layer != 5);
  	spContent5.setVisible(layer == 5);
  	
  	btApply.setEnabled(layer == 5);
  	btBackward.setEnabled(layer != 1);
  	btForward.setEnabled(layer != 5);
  }
  
  
  public static void main(String[] args) {
    //AbschlussHaushaltsjahr abschlussHaushaltsjahr = new AbschlussHaushaltsjahr();
  }


  public void actionPerformed(ActionEvent e) {
  	String cmd = e.getActionCommand();
  	
  	if (cmd == "backward"){
  		layer--;
  		updateView();
  	}else if (cmd == "forward"){
  		layer++;
  		updateView();
  	}else if (cmd == "execute"){
  		
  	}else if (cmd == "dispose"){
  		this.dispose();
  	}
	
  }

}
