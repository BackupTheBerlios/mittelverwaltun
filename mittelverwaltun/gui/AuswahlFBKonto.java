package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import dbObjects.FBHauptkonto;
import dbObjects.FBUnterkonto;
import applicationServer.ApplicationServerException;


public class AuswahlFBKonto extends JDialog implements ActionListener, TreeSelectionListener {
  JScrollPane scrollTree = new JScrollPane();
  FBKontenTree treeKonten;
  JLabel jLabel1 = new JLabel();
  JTextField tfPrivatKonto = new JTextField();
  JButton buAuswahl = new JButton();
  JButton buBeenden = new JButton();
  Benutzerverwaltung benutzerverwaltung;
  

  public AuswahlFBKonto(Frame frame, String title, boolean modal, Benutzerverwaltung benutzerverwaltung) {
    super(frame, title, modal);
    this.benutzerverwaltung = benutzerverwaltung;
    
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    
		try {
			treeKonten.loadInstituts( benutzerverwaltung.applicationServer.getInstitutesWithAccounts() );
		} catch (ApplicationServerException e1) {
		}
		
		tfPrivatKonto.setEnabled(false);
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
    tfPrivatKonto.setText("");
    tfPrivatKonto.setBounds(new Rectangle(175, 228, 246, 21));
    buAuswahl.setBounds(new Rectangle(86, 260, 103, 25));
    buAuswahl.setText("Auswählen");
    buBeenden.setBounds(new Rectangle(270, 259, 120, 25));
    buBeenden.setText("Beenden");
    this.getContentPane().add(scrollTree, null);
		scrollTree.getViewport().add(treeKonten = new FBKontenTree( this, "FBKonten" ) , null);
    this.getContentPane().add(buAuswahl, null);
    this.getContentPane().add(buBeenden, null);
    this.getContentPane().add(tfPrivatKonto, null);
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
		String error = "";
		if( treeKonten.fbHauptkontoIsSelected() ){
			FBHauptkonto fbKonto = ( (FBHauptkonto)treeKonten.getCurrentNode().getUserObject() );
			if(fbKonto != null){
				error = benutzerverwaltung.setPrivatKonto(fbKonto.getId());
				if(error.equals(""))
					benutzerverwaltung.tfKonto.setText(fbKonto.getBezeichnung());
			}
				
		}else if(treeKonten.fbUnterkontoIsSelected()){
			FBUnterkonto fbKonto = ( (FBUnterkonto)treeKonten.getCurrentNode().getUserObject() );
			if(fbKonto != null){
				error = benutzerverwaltung.setPrivatKonto(fbKonto.getId());
				if(error.equals(""))
					benutzerverwaltung.tfKonto.setText(fbKonto.getBezeichnung());	
			}
		}	
		if(!error.equals(""))
			JOptionPane.showMessageDialog(
					this,
					error,
					"Warnung",
					JOptionPane.ERROR_MESSAGE);
		else
			this.dispose();
	}
	
	public void valueChanged(TreeSelectionEvent e) {
		treeKonten.checkSelection( e );
		if( treeKonten.fbHauptkontoIsSelected() ){
			FBHauptkonto fbKonto = ( (FBHauptkonto)treeKonten.getCurrentNode().getUserObject() );
			if(fbKonto != null)
				tfPrivatKonto.setText(fbKonto.getBezeichnung());	
		}else if(treeKonten.fbUnterkontoIsSelected()){
			FBUnterkonto fbKonto = ( (FBUnterkonto)treeKonten.getCurrentNode().getUserObject() );
			if(fbKonto != null)
				tfPrivatKonto.setText(fbKonto.getBezeichnung());	
		}
		
	}
}