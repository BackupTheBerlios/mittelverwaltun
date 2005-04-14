package gui;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import dbObjects.FBHauptkonto;
import dbObjects.Kontenzuordnung;
import dbObjects.ZVKonto;

import applicationServer.ApplicationServerException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class AbschlussHaushaltsjahr extends JInternalFrame implements ActionListener, TableModelListener {
  
  // Panels
  JPanel pnHeader = new JPanel();
  JPanel pnSeparator = new JPanel();
  // Buttons
  JButton btClose = new JButton(Functions.getCloseIcon(this.getClass()));
  JButton btApply = new JButton(Functions.getImportIcon(this.getClass()));
  JButton btForward = new JButton(Functions.getForwardIcon(this.getClass()));
  JButton btBackward = new JButton(Functions.getBackIcon(this.getClass()));
  JButton btTakeOverAcc = new JButton(Functions.getExportIcon(this.getClass()));
  JButton btTakeOverBudget = new JButton (Functions.getExportIcon(this.getClass()));
  // TextFields
  CurrencyTextField tfZvBudget = new CurrencyTextField(0.0f);
  CurrencyTextField tfFbBudget = new CurrencyTextField(0.0f);
  // Labels
  JLabel lbHeadline = new JLabel(Functions.getPropertiesIcon(this.getClass()));
  JLabel lbHeadline1B = new JLabel();
  JLabel lbHeadline2B = new JLabel();
  JLabel lbHeadline3B = new JLabel();
  JLabel lbHeadline4B = new JLabel();
  JLabel lbHeadline1 = new JLabel();
  JLabel lbHeadline2 = new JLabel();
  JLabel lbHeadline3 = new JLabel();
  JLabel lbHeadline4 = new JLabel();
  JLabel lbHeadline5B = new JLabel();
  JLabel lbHeadline5 = new JLabel();
  JLabel lbZvBudget = new JLabel();
  JLabel lbFbBudget = new JLabel();
  // Scrollpanes
  JScrollPane spContent1 = new JScrollPane();
  JScrollPane spContent2 = new JScrollPane();
  JScrollPane spContent3 = new JScrollPane();
  JScrollPane spContent4 = new JScrollPane();
  JScrollPane spContent5 = new JScrollPane();
  // Contentbereiche
  JTextArea taContent5 = new JTextArea();
  AccountTable atContent1 = null;
  AccountTable atContent2 = null;
  MappingsTable mtContent3 = null;
  AnnualOrderTable aotContent4 = null;
  
  // Sonstige Attribute
  MainFrame frame = null;
  int layer = 0;

  public AbschlussHaushaltsjahr(MainFrame frame) {
    
  	this.frame = frame;
  	
  	this.setSize(835, 488);
  	this.getContentPane().setLayout(null);

    pnHeader.setBackground(Color.white);
    pnHeader.setForeground(Color.black);
    pnHeader.setBorder(null);
    pnHeader.setBounds(new Rectangle(0, 10, 825, 55));
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

    spContent1.setBounds(new Rectangle(5, 75, 815, 270));
    try {
		atContent1 = new AccountTable(AccountTable.ZV_KONTEN, frame.applicationServer.getOffeneZVKonten(frame.applicationServer.getCurrentHaushaltsjahrId()));
		atContent1.getAccountTableModel().addTableModelListener(this);
		spContent1.getViewport().add(atContent1, null);
    } catch (ApplicationServerException ae) {
		// TODO Auto-generated catch block
		ae.printStackTrace();
	}
    
    
    ArrayList fbKonten = new ArrayList();
    spContent2.setBounds(new Rectangle(5, 75, 815, 270));
    spContent3.setBounds(new Rectangle(58, 75, 709, 300));
	
    try {
		fbKonten = frame.applicationServer.getOffeneFBHauptkonten(frame.applicationServer.getCurrentHaushaltsjahrId());
		
		atContent2 = new AccountTable(AccountTable.FB_KONTEN, fbKonten);
	    atContent2.getAccountTableModel().addTableModelListener(this);
	    spContent2.getViewport().add(atContent2, null);
	    
	    mtContent3 = new MappingsTable(fbKonten, atContent2, atContent1);
	    spContent3.getViewport().add(mtContent3, null);
	} catch (ApplicationServerException ae) {
		ae.printStackTrace();
	}
    
    spContent4.setBounds(new Rectangle(5, 75, 815, 300));
    try {
		aotContent4 = new AnnualOrderTable(frame.applicationServer.getOffeneBestellungen(frame.applicationServer.getCurrentHaushaltsjahrId()), atContent2, atContent1);
		spContent4.getViewport().add(aotContent4, null);
    } catch (ApplicationServerException ae) {
		ae.printStackTrace();
	}
    
    spContent5.setBounds(new Rectangle(5, 75, 815, 300));
    taContent5.setFont(new java.awt.Font("Dialog", 1, 11));
    spContent5.getViewport().add(taContent5, null);

    btTakeOverAcc.setBounds(new Rectangle(5, 350, 180, 25));
    btTakeOverAcc.setFont(new java.awt.Font("Dialog", 1, 11));
    btTakeOverAcc.setActionCommand("takeOverAccounts");
    btTakeOverAcc.setText("Alle Konten portieren");
    btTakeOverAcc.addActionListener(this);
 
    btTakeOverBudget.setBounds(new Rectangle(190, 350, 200, 25));
    btTakeOverBudget.setFont(new java.awt.Font("Dialog", 1, 11));
    btTakeOverBudget.setActionCommand("takeOverBudgets");
    btTakeOverBudget.setText("Alle Budgets übernehmen");
    btTakeOverBudget.addActionListener(this);
  
    lbZvBudget.setBounds(new Rectangle(220, 380, 200, 20));
    lbZvBudget.setText("Zweckungebundenes ZV-Budget");
    lbZvBudget.setFont(new java.awt.Font("Dialog", 1, 11));
    lbZvBudget.setHorizontalAlignment(SwingConstants.RIGHT);
    
    tfZvBudget.setBounds(new Rectangle(425, 380, 100, 20));
    tfZvBudget.setValue(new Float(0));
    tfZvBudget.setFont(new java.awt.Font("Dialog", 1, 11));
    tfZvBudget.setHorizontalAlignment(SwingConstants.RIGHT);
    
    lbFbBudget.setBounds(new Rectangle(530, 380, 185, 20));
    lbFbBudget.setText("Zweckungebundenes FB-Budget");
    lbFbBudget.setFont(new java.awt.Font("Dialog", 1, 11));
    lbFbBudget.setHorizontalAlignment(SwingConstants.RIGHT);
 
    tfFbBudget.setBounds(new Rectangle(720, 380, 100, 20));
    tfFbBudget.setValue(new Float(0));
    tfFbBudget.setFont(new java.awt.Font("Dialog", 1, 11));
    tfFbBudget.setHorizontalAlignment(SwingConstants.RIGHT);
    
    pnSeparator.setBorder(BorderFactory.createLineBorder(Color.gray));
    pnSeparator.setBounds(new Rectangle(5, 413, 815, 2));

    btClose.setBounds(new Rectangle(695, 423, 125, 25));
    btClose.setFont(new java.awt.Font("Dialog", 1, 11));
    btClose.setActionCommand("dispose");
    btClose.setText("Beenden");
    btClose.addActionListener(this);
    
    btApply.setBounds(new Rectangle(565, 423, 125, 25));
    btApply.setEnabled(false);
    btApply.setFont(new java.awt.Font("Dialog", 1, 11));
    btApply.setActionCommand("execute");
    btApply.setText("Fertigstellen");
    btApply.addActionListener(this);

    btForward.setText("Weiter");
    btForward.setBounds(new Rectangle(460, 423, 100, 25));
    btForward.setFont(new java.awt.Font("Dialog", 1, 11));
    btForward.setActionCommand("forward");
    btForward.addActionListener(this);
    
    btBackward.setFont(new java.awt.Font("Dialog", 1, 11));
    btBackward.setBounds(new Rectangle(355, 423, 100, 25));
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
    this.getContentPane().add(btTakeOverAcc);
    this.getContentPane().add(btTakeOverBudget);
    this.getContentPane().add(pnSeparator, null);
    this.getContentPane().add(btClose, null);
    this.getContentPane().add(btApply, null);
    this.getContentPane().add(btForward, null);
    this.getContentPane().add(btBackward, null);
    this.getContentPane().add(lbFbBudget);
    this.getContentPane().add(tfFbBudget);
    this.getContentPane().add(lbZvBudget);
    this.getContentPane().add(tfZvBudget);
    
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
  	
  	lbFbBudget.setVisible(layer < 3);
  	tfFbBudget.setVisible(layer < 3);
 	lbZvBudget.setVisible(layer < 3);
  	tfZvBudget.setVisible(layer < 3);  	
  	
  	btTakeOverAcc.setVisible(layer < 3);
  	btTakeOverBudget.setVisible(layer < 3);
  	btApply.setEnabled(layer == 4);
  	btBackward.setEnabled((layer > 1)&&(layer < 5));
  	btForward.setEnabled(layer < 4);
  	
  	if (((Float)tfFbBudget.getValue()).floatValue() > ((Float)tfZvBudget.getValue()).floatValue()){
  		tfFbBudget.setBackground(new Color(255, 204, 204));
  		tfZvBudget.setBackground(new Color(255, 204, 204));
  	}else{
  		tfFbBudget.setBackground(Color.WHITE);
  		tfZvBudget.setBackground(Color.WHITE);
  	}
  	
  	mtContent3.actualize();
  	aotContent4.actualize();
  	
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
  		try {
			int[] cnts = frame.getApplicationServer().finishBudgetYear(frame.getBenutzer(), frame.getApplicationServer().getCurrentHaushaltsjahrId(), aotContent4.getOrders(), atContent1.getAccounts(), atContent2.getAccounts(), true);
		
			taContent5.setText(
				"\n\t" +
				"Zusammenfassung\n\t"+
				"==================\n\t" +
				"> Abgeschlossene Bestellungen: " + cnts[0] + "\n\t" +
				"> Stornierte Bestellungen: " + cnts[1] + "\n\t" +
				"> Portierte Bestellungen: " + cnts[2] + "\n\t" +
				"> Portierte Zentralverwaltungskonten: " + cnts[3] + "\n\t" +
				"> Portierte Zentralverwaltungstitel: " + cnts[4] + "\n\t" +
				"> Übernommene Zentralverwaltungsbudgets: " + cnts[5] + "\n\t" +
				"> Abgeschlossene Zentralverwaltungskonten: " + cnts[6] + "\n\t" +
				"> Portierte Fachbereichskonten: " + cnts[7] + "\n\t" +
				"> Übernommene Fachbereichsbudgets: " + cnts[8] + "\n\t" +
				"> Abgeschlossene Fachbereichskonten: alle\n\t" +
				"> Portierte Kontenzuordnungen: " + cnts[9]
			);

  		} catch (ApplicationServerException ase) {
			System.out.println(ase.getNestedMessage());
			ase.printStackTrace();
			taContent5.setText(
					"\n\t" +
					"Fehler bei Haushaltsjahreabschluss\n\t"+
					"==================================\n\t"+
					"Message from server: " + ase.getMessage() + "\n\t" +
					"Nested message: " + ase.getNestedMessage()
					);
		}
  		
  		layer++;
  		updateView();
  	
  	}else if (cmd == "dispose"){
  		this.dispose();
  	}else if (cmd == "takeOverAccounts"){
  		if (layer == 1){
  			atContent1.getAccountTableModel().setAllePortieren();
  			updateView();
  		}else if (layer == 2){
  			atContent2.getAccountTableModel().setAllePortieren();
  			updateView(); 			
  		}
   	}else if (cmd == "takeOverBudgets"){
  		if (layer == 1){
  			atContent1.getAccountTableModel().setAlleUebernehmen();
  			updateView();
  		}else if (layer == 2){
  			atContent2.getAccountTableModel().setAlleUebernehmen();
  			updateView(); 			
  		}
   	}
  }

	public void tableChanged(TableModelEvent e) {
		
		int row = e.getFirstRow(), col = e.getColumn();
		
		AccountTableModel source, zvTableModel, fbTableModel;
		source = (AccountTableModel)e.getSource();
		zvTableModel = atContent1.getAccountTableModel();
		fbTableModel = atContent2.getAccountTableModel();
		
		
		float oldZvBudget = ((Float)tfZvBudget.getValue()).floatValue();
		float newZvBudget = zvTableModel.getNoPurposeBudget();
		float oldFbBudget = ((Float)tfFbBudget.getValue()).floatValue();
		float newFbBudget = fbTableModel.getNoPurposeBudget();
		tfZvBudget.setValue(new Float(newZvBudget));
		tfFbBudget.setValue(new Float(newFbBudget));
		
		if (((oldZvBudget - oldFbBudget)>= 0) && ((newZvBudget - newFbBudget)< 0))
			MessageDialogs.showInfoMessageDialog(this, "Hinweis", "Um einen Haushaltsjahresabschluss durchführen zu\nkönnen, muss die Summe des übernommenen zweckunge-\nbundenen Zentralverwaltungsbudget größer oder gleich der\nSumme des übernommenen zweckungebundenen Fach-\nbereichsbudgets sein.");
		
		tfZvBudget.setValue(new Float(newZvBudget));
		tfFbBudget.setValue(new Float(newFbBudget));
		
		if (col == 9 - source.getTyp()){ // Spalte "Übernehmen"
			
			if (source.equals(zvTableModel) && !zvTableModel.getUebernehmen(row)){ // ZV-Kontoübernahmen wurde deselektiert
				ZVKonto acc = ((ZVKonto)zvTableModel.getAccount(row));
				if (acc.getZweckgebunden()){
					ArrayList rowIndeces = mtContent3.getMappingsTableModel().getMappedRowIndecesOfZvAccount(row);
					for (int i=0; i<rowIndeces.size(); i++){
						fbTableModel.setUebernehmen(((Integer)rowIndeces.get(i)).intValue(), false);
					}
					//MessageDialogs.showInfoMessageDialog(this, "Hinweis", "Die Budgets der diesem zweckgebundenen Zentral-\nverwaltungskonto zugeordneten Fachbereichskonten können \nnicht übernommen werden.");
				}
			
			}else if (source.equals(fbTableModel) && fbTableModel.getUebernehmen(row)){ // FB-Kontenübernahme wurde selektiert
								
				FBHauptkonto acc = ((FBHauptkonto)fbTableModel.getAccount(row));
				Kontenzuordnung[] z = acc.getZuordnung();
				
				if ((z != null)&& (z.length > 0) && (z[0].getZvKonto().getZweckgebunden())){
					int accRow = zvTableModel.getRowOfAccount(z[0].getZvKonto().getId());
					if (!zvTableModel.getUebernehmen(accRow)){
						fbTableModel.setUebernehmen(e.getFirstRow(), false);
						MessageDialogs.showInfoMessageDialog(this, "Hinweis", "Um das Budget dieses Fachbereichkontos zu übernehmen,\nmuss auch das Budget des zugehörigen zweckgebundenen\nZentralverwaltungskontos übernommen werden.");
					}
				}
			}
		}
		
		updateView();
		
	}  	
}
