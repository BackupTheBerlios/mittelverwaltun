/*
 * Created on 07.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gui;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import dbObjects.FBHauptkonto;
import dbObjects.Kontenzuordnung;
import dbObjects.ZVKonto;

/**
 * @author Mario
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AccountTableModel extends DefaultTableModel {
	
	// AccountTableModel-Typen
	static final int FB_KONTEN = 2;
	static final int ZV_KONTEN = 1;

	// Übernahme-Stati
	
	static final short KEINE = 0;
	static final short BEANTRAGT = 1;
	static final short BEWILLIGT = 2;
	static final short UEBERNOMMEN = 3;
	
	int typ;
	ArrayList accounts;
	
public  AccountTableModel (int typ, ArrayList accounts){
	super();
	
	this.typ = typ;
	this.accounts = accounts;
	
	if (typ == ZV_KONTEN){
	
		
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
				
				if(acc.getUebernahmeStatus() == 0)
					data[9] = "Abschließen";
				else
					data[9] = "in Bearbeitung";
				
				addRow(data);
			}
		}
	}

	if (typ == FB_KONTEN){
		String[] colheads = {"Portieren", "Kostenstelle", "Hauptkonto", "Bezeichnung", "Gesamtbudget", "Offene Bestellungen", "Vormerkungen", /*"Budgetübernahme", */"Übernehmen"};
	    setColumnIdentifiers(colheads);
		
	    if (accounts != null){
			
			for(int i = 0; i < accounts.size(); i++){
				
				FBHauptkonto acc = (FBHauptkonto)accounts.get(i);
				Object[] data = new Object[8];
				
				data[0] = new Boolean(false);
				data[1] = acc.getInstitut().getKostenstelle();
				data[2] = acc.getHauptkonto();
				data[3] = acc.getBezeichnung();
				data[4] = new Float(acc.getBudget());
				data[5] = new Integer(0);
				data[6] = new Float(acc.getVormerkungen());
			
				data[7] = new Boolean(false);
				
				//data[8] = "";
				
				addRow(data);
				
			}
		}		
	}
}

public int getId (int row){
	
	if (((typ == FB_KONTEN)||(typ == ZV_KONTEN))&&(row < this.getRowCount())&& (row >= 0))
		return ((ZVKonto)accounts.get(row)).getId();
	else return 0;
		
}


public int getStatus (int row){
	
	if ((typ == ZV_KONTEN) && (row < this.getRowCount())&& (row >= 0)){
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
		return ((Boolean)getValueAt(row, 0)).booleanValue();
		 
	} else return false;	
}


public boolean getAbgeschlossen(int row){
	if ((row < this.getRowCount())&& (row >= 0)){
		if (typ == ZV_KONTEN)
			return ((String)getValueAt(row, 9)).equalsIgnoreCase("Abschließen");
		else return true;
		 
	} else return false;	
}


public void setPortieren (int row, boolean value){
	if ((row < this.getRowCount())&& (row >= 0)){
		setValueAt(new Boolean(value), row, 0 );
	}
}

public void setAllePortieren (){
	for (int i=0; i < this.getRowCount(); i++){
		setPortieren(i, true);
	}
}

public void setAlleUebernehmen (){
	for (int i=0; i < this.getRowCount(); i++){
		if (getPortieren(i) && ((typ == FB_KONTEN)||(getStatus(i)==BEWILLIGT)))
			setUebernehmen(i, true);
	}
}


public void setUebernehmen (int row, boolean value){
	if ((row < this.getRowCount())&& (row >= 0)){
		setValueAt(new Boolean(value), row, 9 - typ );
	}
}

public boolean getUebernehmen(int row){
	if ((row < this.getRowCount())&& (row >= 0)){
		return ((Boolean)getValueAt(row, 9 - typ )).booleanValue();
	} else return false;	
}


public boolean isCellEditable(int rowIndex, int colIndex) {
	if ((rowIndex < this.getRowCount())&& (rowIndex >= 0))
		if (typ == ZV_KONTEN)
			return ((colIndex == 0) && (!((ZVKonto)accounts.get(rowIndex)).isPortiert()))||(colIndex == 7)||((colIndex == 8) && (getStatus(rowIndex)==BEWILLIGT) && getPortieren(rowIndex));
		else if (typ == FB_KONTEN)
			return ((colIndex == 0) || ((colIndex == 7) && getPortieren(rowIndex)));
		else return false;
	else return false;
}

public Class getColumnClass(int colIndex) {
	return getValueAt(0, colIndex).getClass();
}


public int getRowOfAccount(int accId){
	int row = -1;
	
	for (int i=0; i<accounts.size(); i++){
		int id = 0;
		
		if (typ==ZV_KONTEN){
			id = ((ZVKonto)accounts.get(i)).getId();
		}else if (typ == FB_KONTEN){
			id = ((FBHauptkonto)accounts.get(i)).getId();
		}
		
		if (id == accId){
			row = i;
			break;
		}
	}
	
	return row;
}

	public int getTyp(){
		return typ;
	}

	public Object getAccount(int row){
		if ((row < this.getRowCount())&& (row >= 0))
			return accounts.get(row);
		else
			return null;
	}
	
	
	public float getNoPurposeBudget(){
		float budget = 0.0f;
		if (typ == ZV_KONTEN ){
			for (int i = 0; i < accounts.size(); i++){
				ZVKonto acc = (ZVKonto)accounts.get(i);
				if (!acc.isZweckgebunden() && getUebernehmen(i))
					budget += acc.getBudget();
			}
		}else if (typ == FB_KONTEN){
			for (int i = 0; i < accounts.size(); i++){
				FBHauptkonto acc = (FBHauptkonto)accounts.get(i);
				Kontenzuordnung[] z = acc.getZuordnung();
				if ((z != null)&&(z.length > 0)&&(!z[0].getZvKonto().getZweckgebunden())&&getUebernehmen(i))
					budget += acc.getBudget();
			}
		}
		return budget;
	}

	public ArrayList getAccounts(){
		
		
		if (typ == ZV_KONTEN){
			for (int i=0; i < accounts.size(); i++){
				
				((ZVKonto)accounts.get(i)).setPortiert(getPortieren(i));
				((ZVKonto)accounts.get(i)).setAbgeschlossen(getAbgeschlossen(i));
				((ZVKonto)accounts.get(i)).setUebernahmeStatus((short)getStatus(i));
			
			}
			return accounts;
		}else if(typ == FB_KONTEN){
			for (int i=0; i < accounts.size(); i++){
				
				((FBHauptkonto)accounts.get(i)).setPortieren(getPortieren(i));
				((FBHauptkonto)accounts.get(i)).setBudgetUebernehmen(getUebernehmen(i));
			
			}
			return accounts;
		}else return null;
	}
}
