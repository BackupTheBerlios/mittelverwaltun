package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import dbObjects.FBUnterkonto;
import dbObjects.Kontenzuordnung;
import dbObjects.ZVKonto;
import dbObjects.ZVUntertitel;
import applicationServer.ApplicationServerException;


public class AuswahlZVKonto extends JDialog implements ActionListener, TreeSelectionListener {
  JScrollPane scrollTree = new JScrollPane();
  ZVKontenTree treeKonten;
  JLabel jLabel1 = new JLabel();
  JTextField tfKonto = new JTextField();
  JButton buAuswahl = new JButton();
  JButton buBeenden = new JButton();
  Component parent;
  MainFrame frame;
  FBUnterkonto fbKonto;


  public AuswahlZVKonto(Component parent, FBUnterkonto fbKonto, boolean modal, MainFrame frame) {
    super(JOptionPane.getFrameForComponent(parent), "ZVKonto Auswahl", modal);
    this.parent = parent;
    this.frame = frame;
    this.fbKonto = fbKonto;
    FBUnterkonto test ;

    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }

		loadZVKonten();
		tfKonto.setEnabled(false);
		buAuswahl.addActionListener( this );

		buBeenden.setIcon(Functions.getCloseIcon(getClass()));
		buBeenden.addActionListener( this );

		this.setBounds(0,0,460, 320);
  }

  /**
	* Laden der ZVKonten in den Baum
	*/
  void loadZVKonten() {
	  if( frame != null ) {
		  try {
			  treeKonten.delTree();
			  ArrayList institute = frame.getApplicationServer().getZVKonten();

			  if(fbKonto != null){
					Kontenzuordnung[] zuordnungen = fbKonto.getZuordnung();

			  	if(zuordnungen != null){
			  		for(int i = 0; i < institute.size(); i++){
					  	ZVKonto zvKonto = (ZVKonto)institute.get(i);
							boolean del = true;

							if(zvKonto != null){
								for(int j = 0; j < zuordnungen.length; j++){
									if(zuordnungen[j].getZvKonto().getId() == zvKonto.getId()){
											del = false;
											break;
									}
								}
								if(del){
										institute.remove(i);
										i--;
								}
							}
			  		}
			  	}else{
			  		institute.clear();
			  	}
			  }
				treeKonten.loadZVKonten( institute );

		  } catch (ApplicationServerException e) {
			  System.out.println( e.toString() );
		  }
	  }
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
		scrollTree.getViewport().add(treeKonten = new ZVKontenTree( this, "ZVKonten" ), null);
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
		if(treeKonten.zvTitelIsSelected() || treeKonten.zvUntertitelIsSelected()){
			ZVUntertitel zvTitel = (ZVUntertitel)treeKonten.getCurrentNode().getUserObject();
			((ZVKontoSelectable)parent).setZVKonto(zvTitel);
			this.dispose();
		}
	}

	public void valueChanged(TreeSelectionEvent e) {
		treeKonten.checkSelection( e );
		if(treeKonten.zvTitelIsSelected() || treeKonten.zvUntertitelIsSelected()){
			buAuswahl.setEnabled(true);
			ZVUntertitel zvTitel = (ZVUntertitel)treeKonten.getCurrentNode().getUserObject();
			tfKonto.setText(zvTitel.getBezeichnung());
		}else{
			buAuswahl.setEnabled(false);
		}
	}
}