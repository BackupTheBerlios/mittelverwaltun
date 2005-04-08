/*
 * Created on 07.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gui;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import dbObjects.ZVKonto;

/**
 * @author Mario
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AccountTableModel extends DefaultTableModel {
	
	// AccountTableModel-Typen
	//static final int FB_KONTEN = 2;
	static final int ZV_KONTEN = 1;

	// Übernahme-Stati
	
	static final short KEINE = 0;
	static final short BEANTRAGT = 1;
	static final short BEWILLIGT = 2;
	static final short UEBERNOMMEN = 3;
		
	ArrayList accounts;
	
public  AccountTableModel (ArrayList accounts){
	super();
	
	this.accounts = accounts;
	String[] colheads = {"Portieren", "Kapitel", "Titelgruppe", "Bezeichnung", "Gesamtbudget", "Offene Bestellungen", "Vormerkungen", "Budgetübernahme", "Übernehmen", "Status"};
    setColumnIdentifiers(colheads);
	
    if (accounts != null){
		
		for(int i = 0; i < accounts.size(); i++){
			
			ZVKonto acc = (ZVKonto)accounts.get(i);
			Object[] data = new Object[10];
			
			data[0] = new Boolean(acc.isPortiert());
			data[1] = acc.getKapitel();
			data[2] = acc.getTitelgruppe();
			data[3] = acc.getBezeichnung();
			data[4] = new Float(acc.getBudget());
			data[5] = new Integer(0);
			data[6] = new Float(acc.getVormerkungen());
			
			
			switch (acc.getUebernahmeStatus()){
				case KEINE: 		data[7] = "keine";
									break;
				case BEANTRAGT: 	data[7] = "beantragt";
									break;
				case BEWILLIGT: 	data[7] = "bewilligt";
									break;
				case UEBERNOMMEN: 	data[7] = "übernommen";
									break;
				default: 			data[7] = "n.a.";
			}
			
			data[8] = new Boolean(false);
			
			data[9] = "";
			
			addRow(data);
			
		}
		
	}
}

public int getId (int row){
	
	if ((row < this.getRowCount())&& (row >= 0))
		return ((ZVKonto)accounts.get(row)).getId();
	else return 0;
		
}


public int getStatus (int row){
	
	if ((row < this.getRowCount())&& (row >= 0)){
		String status = (String)getValueAt(row, 7);
		if (status.equals("keine"))
			return KEINE;
		else if (status.equals("beantragt"))
			return BEANTRAGT;
		else if (status.equals("bewilligt"))
			return BEWILLIGT;
		else if (status.equals("übernommen"))
			return UEBERNOMMEN;
		else return -1;
	} else return -1;
		
}

public boolean getPortieren(int row){
	if ((row < this.getRowCount())&& (row >= 0)){
		Boolean portieren = (Boolean)getValueAt(row, 0);
		return portieren.booleanValue();
	} else return false;	
}

public boolean isCellEditable(int rowIndex, int colIndex) {
	if ((rowIndex < this.getRowCount())&& (rowIndex >= 0))		
		return ((colIndex == 0) && (!((ZVKonto)accounts.get(rowIndex)).isPortiert()))||(colIndex == 7)||((colIndex == 8) && (getStatus(rowIndex)==BEWILLIGT) && getPortieren(rowIndex));
	else return false;
}

public Class getColumnClass(int colIndex) {
	return getValueAt(0, colIndex).getClass();
}

}
