
/**
 * @author Mario
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
package gui;

import javax.swing.table.AbstractTableModel;
import dbObjects.Benutzer;


public class ProfBudgetTableModel extends AbstractTableModel {
	private String[] header = {"Professor", "Budget"};
	private Object[][] data = {{"", new Float(0)}};
	
	public ProfBudgetTableModel(Benutzer[] professoren, float dfltBdgt){
		data = new Object[professoren.length][2];
		for (int i=0; i<professoren.length; i++){
			data[i][0] = professoren[i].getName() + ", " + professoren[i].getVorname();	
			data[i][1] = new Float(dfltBdgt);
		}
	}

	public int getRowCount() {
		return data.length;
	}

	public int getColumnCount() {
		return 2;
	}
	
	public Class getColumnClass(int col){
		if((col >= 0) && (col<getColumnCount())) 
			return data[0][col].getClass();
		else return null; 
	}

	public String getColumnName(int col){
		if((col >= 0) && (col<getColumnCount())) 
			return header[col];
		else return null; 	
	}

	public boolean isCellEditable(int row, int col){
		return ((col==1) && (row >= 0) && (row<getRowCount()));
	}
	
	public Object getValueAt(int row, int col) {
		if((row >= 0) && (row<getRowCount()) && (col >= 0) && (col<getColumnCount())) 
			return data[row][col];
		else return null;
	}

	public void setValueAt (Object value, int row, int col){
		if( /*(value.getClass().getName()=="Float") && */(col==1) && (row >= 0) && (row<getRowCount()) ){
			data[row][col]=value;
		}
					
		this.fireTableCellUpdated(row,col);
	}

	

	public void setStandardBudget(float budget){
		for (int i=0; i<getRowCount();i++){
			data[i][1] = new Float(budget);
		}
		this.fireTableDataChanged();
	}

	public float calculateOverallBudget(){
		float sum = 0;
		for (int i=0; i<data.length; i++){
			Float v = (Float)data[i][1];
			sum += v.floatValue();
		}
		return sum;
	}


	public static void main(String[] args) {
	}
}
