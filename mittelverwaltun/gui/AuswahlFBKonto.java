package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import dbObjects.FBUnterkonto;
import dbObjects.Institut;
import applicationServer.ApplicationServer;
import applicationServer.ApplicationServerException;


public class AuswahlFBKonto extends JDialog implements ActionListener, TreeSelectionListener {
  JScrollPane scrollTree = new JScrollPane();
  FBKontenTree treeKonten;
  JLabel jLabel1 = new JLabel();
  JTextField tfKonto = new JTextField();
  JButton buAuswahl = new JButton();
  JButton buBeenden = new JButton();
  Component parent;
  ApplicationServer applicationServer;
  boolean hauptKonto;


	/**
	 * 
	 * @param parent
	 * @param modal
	 * @param applicationServer
	 * @param hauptKonto - sollen auch HauptKonten ausgewählt werden können
	 */
  public AuswahlFBKonto(Component parent, boolean modal, ApplicationServer applicationServer, boolean hauptKonto) {
		super(JOptionPane.getFrameForComponent(parent), "FBKonto Auswahl", modal);
		this.parent = parent;
		this.applicationServer = applicationServer;
		this.hauptKonto = hauptKonto;

		try {
		  jbInit();
		  pack();
		}
		catch(Exception ex) {
		  ex.printStackTrace();
		}
		
		try {
			treeKonten.loadInstituts( applicationServer.getInstitutesWithAccounts() );
		} catch (ApplicationServerException e1) {
		}

	  tfKonto.setEnabled(false);
	  buAuswahl.addActionListener( this );

	  buBeenden.setIcon(Functions.getCloseIcon(getClass()));
	  buBeenden.addActionListener( this );

	  this.setBounds(0,0,460, 320);
		setLocation((JOptionPane.getFrameForComponent(parent).getWidth()/2) - (getWidth()/2), (JOptionPane.getFrameForComponent(parent).getHeight()/2) - (getHeight()/2));
    
}
 
 	public AuswahlFBKonto(Component parent, Institut institut, boolean modal, ApplicationServer applicationServer, boolean hauptKonto) {
		 super(JOptionPane.getFrameForComponent(parent), "FBKonto Auswahl", modal);
		 this.parent = parent;
		 this.applicationServer = applicationServer;
		 this.hauptKonto = hauptKonto;
		
		 try {
			jbInit();
			pack();
		 }
		 catch(Exception ex) {
			ex.printStackTrace();
		 }
		
		 try {
			 treeKonten.loadInstituts( applicationServer.getInstituteWithAccounts(institut, true) );
		 } catch (ApplicationServerException e1) {
		 } 

		tfKonto.setEnabled(false);
		buAuswahl.addActionListener( this );

		buBeenden.setIcon(Functions.getCloseIcon(getClass()));
		buBeenden.addActionListener( this );

		this.setBounds(0,0,460, 320);
  }
 
  
  private void jbInit() throws Exception {
    this.getContentPane().setLayout(null);
    scrollTree.setBounds(new Rectangle(10, 9, 437, 208));
    jLabel1.setText("FBPrivatKonto:");
    jLabel1.setBounds(new Rectangle(49, 231, 98, 15));
		tfKonto.setText("");
		tfKonto.setBounds(new Rectangle(175, 228, 246, 21));
    buAuswahl.setBounds(new Rectangle(86, 260, 103, 25));
    buAuswahl.setText("Auswählen");
    buBeenden.setBounds(new Rectangle(270, 259, 120, 25));
    buBeenden.setText("Beenden");
    this.getContentPane().add(scrollTree, null);
		scrollTree.getViewport().add(treeKonten = new FBKontenTree( this, "FBKonten" ) , null);
    this.getContentPane().add(buAuswahl, null);
    this.getContentPane().add(buBeenden, null);
    this.getContentPane().add(tfKonto, null);
    this.getContentPane().add(jLabel1, null);
  }

	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == buAuswahl ) {
			setAuswahl();
		}else if(e.getSource() == buBeenden){
			this.dispose();	
		}
	}

	void setAuswahl(){
		if(treeKonten.fbUnterkontoIsSelected() || (treeKonten.fbHauptkontoIsSelected() && hauptKonto)){
			FBUnterkonto fbKonto = (FBUnterkonto)treeKonten.getCurrentNode().getUserObject();
			((FBKontoSelectable)parent).setFBKonto(fbKonto);
			this.dispose();
		}
	}

	public void valueChanged(TreeSelectionEvent e) {
		treeKonten.checkSelection( e );
		if(treeKonten.fbUnterkontoIsSelected() || (treeKonten.fbHauptkontoIsSelected() && hauptKonto)){
			buAuswahl.setEnabled(true);
			FBUnterkonto fbKonto = (FBUnterkonto)treeKonten.getCurrentNode().getUserObject();
			tfKonto.setText(fbKonto.getBezeichnung());
		}else{
			buAuswahl.setEnabled(false);
		}
	}
}