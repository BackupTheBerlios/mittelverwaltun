/*
 * Created on 08.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gui;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import dbObjects.FBHauptkonto;
import dbObjects.Kontenzuordnung;
/**
 * @author Mario
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MappingsTableModel extends DefaultTableModel {
	
	//AccountTable fbAccounts, zvAccounts;
	ArrayList fbRowReferences = new ArrayList();
	ArrayList zvRowReferences = new ArrayList();
	
	public  MappingsTableModel (ArrayList accounts, AccountTable fbAccounts, AccountTable zvAccounts){
		super();
				
		String[] colheads = {"Fachbereichshauptkonto", "Zentralverwaltungskonto", "Budgetverwendung", "Übernahme"};
		setColumnIdentifiers(colheads);
			
		if (accounts != null){
				
			for(int i = 0; i < accounts.size(); i++){
				
				FBHauptkonto acc = (FBHauptkonto)accounts.get(i);
				
				Kontenzuordnung[] z = acc.getZuordnung();
				
				if (z != null){
					for (int j = 0; j < z.length; j++){
						
						Object[] data = new Object[4];
						
						data[0] = acc.getInstitut().getKostenstelle() + "-" + acc.getHauptkonto() + "   " + acc.getBezeichnung();
						fbRowReferences.add(new Integer(i));
						
						data[1] = z[j].getZvKonto().getKapitel() + (z[j].getZvKonto().getTitelgruppe().equalsIgnoreCase("")? "      " : "/" + z[j].getZvKonto().getTitelgruppe()) + "   " + z[j].getZvKonto().getBezeichnung();
						zvRowReferences.add(new Integer(zvAccounts.getRowOfAccount(z[j].getZvKonto().getId())));
						
						data[2] = (z[j].getZvKonto().getZweckgebunden() ? "zweckgebunden" : "frei verfügbar");
						
						data[3] = new Boolean (false);
						
						addRow(data);
					}
				}
			}
		}		
	}

	public boolean isCellEditable(int rowIndex, int colIndex) {
		return false;
	}

	public Class getColumnClass(int colIndex) {
		return getValueAt(0, colIndex).getClass();
	}
	
	public int getFbAccountTabRow(int rowIndex){
		if ((rowIndex < this.getRowCount())&& (rowIndex >= 0)){
			return ((Integer)fbRowReferences.get(rowIndex)).intValue();
		}else return -1;
	}
	
	public int getZvAccountTabRow(int rowIndex){
		if ((rowIndex < this.getRowCount())&& (rowIndex >= 0)){
			return ((Integer)zvRowReferences.get(rowIndex)).intValue();
		}else return -1;
	}

	public ArrayList getMappedRowIndecesOfFbAccount (int fbRow){
		ArrayList rowList = new ArrayList();
		for (int i=0; i < fbRowReferences.size(); i++){
			if (((Integer)fbRowReferences.get(i)).intValue() == fbRow)
				rowList.add(new Integer(((Integer)zvRowReferences.get(i)).intValue()));
		}
		return rowList;
	}
	
	public ArrayList getMappedRowIndecesOfZvAccount (int zvRow){
		ArrayList rowList = new ArrayList();
		for (int i=0; i < zvRowReferences.size(); i++){
			if (((Integer)zvRowReferences.get(i)).intValue() == zvRow)
				rowList.add(new Integer(((Integer)fbRowReferences.get(i)).intValue()));
		}
		
		return rowList;
	}
	
}
