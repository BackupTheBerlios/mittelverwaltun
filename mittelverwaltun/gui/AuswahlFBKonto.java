package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import dbObjects.Benutzer;
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
	 * Konstruktor
	 * @param parent
	 * @param modal
	 * @param applicationServer
	 * @param hauptKonto - sollen auch HauptKonten ausgewählt werden können
	 */
  public AuswahlFBKonto(Component parent, MainFrame frame, boolean hauptKonto) {
		super(frame, "FBKonto Auswahl", true);
		this.parent = parent;
		this.applicationServer = frame.getApplicationServer();
		this.hauptKonto = hauptKonto;

		try {
		  jbInit();
		}
		catch(Exception ex) {
		  ex.printStackTrace();
		}
		
		try {
		  int visibility = frame.getBenutzer().getSichtbarkeit();
		  
		  if(visibility == Benutzer.VIEW_FACHBEREICH)
		    treeKonten.loadInstituts( applicationServer.getInstitutesWithAccounts() );
		  else if(visibility == Benutzer.VIEW_INSTITUT)
		    treeKonten.loadInstituts( applicationServer.getInstituteWithAccounts(frame.getBenutzer().getKostenstelle(), true) );
		  else if(visibility == Benutzer.VIEW_PRIVAT){
		    treeKonten.loadInstituts( applicationServer.getPrivatKonto(frame.getBenutzer().getKostenstelle(), 
		            																									 frame.getBenutzer().getPrivatKonto() ) );
		  }		    
		} catch (ApplicationServerException e1) {
		}

	  tfKonto.setEnabled(false);
	  buAuswahl.addActionListener( this );

	  buBeenden.setIcon(Functions.getCloseIcon(getClass()));
	  buBeenden.addActionListener( this );

	  this.setBounds(0,0,460, 320);
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
		this.setVisible(true);
  }
 
  /**
   * Konstruktor
   * @param parent
   * @param institut - Institut von dem nur die FBHaupt- und FBUnterkonten angezeigt werden
   * @param applicationServer
   * @param hauptKonto - sollen auch HauptKonten ausgewählt werden können 
   */
 	public AuswahlFBKonto(Component parent, Institut institut, MainFrame frame, boolean hauptKonto) {
		 super(frame, "FBKonto Auswahl", true);
		 this.parent = parent;
		 this.applicationServer = frame.getApplicationServer();
		 this.hauptKonto = hauptKonto;
		
		 try {
			jbInit();
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

		this.setBounds(50,50,460, 320);
		this.setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
		this.setVisible(true);
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