/*
 * Created on 14.04.2005
 *
 */
package gui;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.table.DefaultTableModel;
import dbObjects.Haushaltsjahr;

/**
 * @author Mario
 *
 */
public class BudgetYearTableModel extends DefaultTableModel {
	ArrayList years = new ArrayList();
	
	public BudgetYearTableModel (ArrayList years){
		super();
		
		String[] colheads = {"Von", "Bis", "Status", ""};
	    setColumnIdentifiers(colheads);
		
	    if (years != null){
			
	    	this.years = years;
	    	DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMANY);
	    	
	    	for(int i = 0; i < years.size(); i++){
				Haushaltsjahr year = (Haushaltsjahr)years.get(i);
				
				Object[] data = new Object[4];
				data[0] = (year.getVon() != null) ? df.format(year.getVon()) : "";
				data[1] = (year.getBis() != null) ? df.format(year.getBis()) : "";
				data[2] = year.getStatusString();
				data[3] = "";
				
				addRow(data);
			}
			
		}
	}
	
	public int getId (int row){
		if ((row < this.getRowCount())&& (row >= 0))
			return ((Haushaltsjahr)years.get(row)).getId();
		else return 0;
	}
	
	public int getStatus (int row){
		if ((row < this.getRowCount())&& (row >= 0))
			return ((Haushaltsjahr)years.get(row)).getStatus();
		else return -1;
	}
	
	public boolean isCellEditable(int rowIndex, int colIndex) {
		return (colIndex == 3);
	}
	
//	public Class getColumnClass(int colIndex) {
//		return getValueAt(0, colIndex).getClass();
//	}
	
}
