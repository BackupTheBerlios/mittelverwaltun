package gui;

import java.sql.Date;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import dbObjects.FBUnterkonto;
import dbObjects.Firma;
import dbObjects.Institut;
import dbObjects.ZVKonto;

/**
 * <p>TableModel für die Tabelle ReportsTable<p>
 * 
 * @author Robert
 */

public class ReportsTableModel extends DefaultTableModel {
		
	private int type = 0;
	ArrayList identifiers;
		
	public ReportsTableModel (int type, ArrayList content){
		super();
		this.type = type;
		
		if (type == Reports.REPORT_1){
			String[] colheads = {"ZV-Konto", "zugewiesene Mittel", "Ausgaben", "Kontostand"}; 
			setColumnIdentifiers(colheads);
		} else if (type == Reports.REPORT_2){
			String[] colheads = {"ZV-Konto", "zugewiesene Mittel", "Ausgaben", "Verteilungen"}; 
			setColumnIdentifiers(colheads);
		} else if (type == Reports.REPORT_3){
			String[] colheads = {"FB-Konto", "verteilte Mittel", "Ausgaben", "Kontostand"}; 
			setColumnIdentifiers(colheads);
		} else if (type == Reports.REPORT_4){
			String[] colheads = {"FB-Konto", "ZV-Konto", "Ausgaben"}; 
			setColumnIdentifiers(colheads);
		} else if  (type == Reports.REPORT_5){
			String[] colheads = {"ZV-Konto", "Institut", "Ausgaben", "Kontostand"}; 
			setColumnIdentifiers(colheads);
		}	else if (type == Reports.REPORT_6){
			String[] colheads = {"ZV-Konto", "Ausgaben"}; 
			setColumnIdentifiers(colheads);
		} else if (type == Reports.REPORT_7){
			String[] colheads = {"ZV-Konto", "Ausgaben", "FBI-Schlüsselnummer", "Hül-Nr", "Datum", "Firma", "Status", ""}; 
			setColumnIdentifiers(colheads);
		} else if (type == Reports.REPORT_8){
			String[] colheads = {"ZV-Konto", "Einnahmen"}; 
			setColumnIdentifiers(colheads);
		}
		
		fillReport (content);
	}

	
	public void fillReport (ArrayList content){
		
		for(int i = 0; i < content.size(); i++){
			Object[] data = null;
			
			if (type == Reports.REPORT_1 || type == Reports.REPORT_2 || type == Reports.REPORT_3 || type == Reports.REPORT_5)
				data = new Object[4];
			else if(type == Reports.REPORT_4)
				data = new Object[3];
			else if(type == Reports.REPORT_6 || type == Reports.REPORT_8)
				data = new Object[3];
			else if(type == Reports.REPORT_7)
				data = new Object[9];
			
			// eine Zeile des Reports
			ArrayList row = (ArrayList)content.get(i);
			
			if (type == Reports.REPORT_1){
				data[0] = ((ZVKonto)row.get(0)).getBezeichnung();	// ZV-Konto
				data[1] = (Float)row.get(1);											// zugewiesene Mittel
				data[2] = (Float)row.get(2);											// Ausgaben
				data[3] = (Float)row.get(3);											// Kontostand
				
			} else if (type == Reports.REPORT_2){
				data[0] = ((ZVKonto)row.get(0)).getBezeichnung();	// ZV-Konto
				data[1] = (Float)row.get(1);											// zugewiesene Mittel
				data[2] = (Float)row.get(2);											// Ausgaben
				data[3] = (Float)row.get(3);											// Verteilungen
				
			} else if (type == Reports.REPORT_3){
				data[0] = ((FBUnterkonto)row.get(0)).getBezeichnung();// FB-Konto
				data[1] = (Float)row.get(1);													// verteilte Mittel
				data[2] = (Float)row.get(2);													// Ausgaben
				data[3] = (Float)row.get(3);													// Kontostand
				
			} else if (type == Reports.REPORT_4){
				data[0] = ((FBUnterkonto)row.get(0)).getBezeichnung();// FB-Konto
				data[1] = ((ZVKonto)row.get(1)).getBezeichnung();			// ZV-Konto
				data[2] = (Float)row.get(2);													// Ausgaben
				
			} else if (type == Reports.REPORT_5){
				data[0] = ((ZVKonto)row.get(0)).getBezeichnung();	// ZV-Konto
				data[1] = ((Institut)row.get(1)).getBezeichnung();// Institut
				data[2] = (Float)row.get(2);											// Ausgaben
				data[3] = (Float)row.get(3);											// Kontostand
					
			} else if (type == Reports.REPORT_6){
				data[0] = ((ZVKonto)row.get(0)).getBezeichnung();	// ZV-Konto
				data[1] = (Float)row.get(1);											// Ausgaben
						
			} else if (type == Reports.REPORT_7){
				data[0] = ((ZVKonto)row.get(0)).getBezeichnung();	// ZV-Konto
				data[1] = (Float)row.get(1);											// Ausgaben
				data[2] = (String)row.get(2);											// FBI-Schlüsselnummer
				data[3] = (String)row.get(3);										  // Hül-Nr
				data[4] = (Date)row.get(4);												// Datum 
				data[5] = ((Firma)row.get(5)).getName();					// Firma
				data[6] = (String)row.get(6);											// Status
				data[7] = new String("X");												// Button anzeigen
				
				identifiers.add((Integer)row.get(7));
							
			} else if (type == Reports.REPORT_8){
				data[0] = ((ZVKonto)row.get(0)).getBezeichnung();	// ZV-Konto
				data[1] = (Float)row.get(1);											// Einnahmen
							
			}
						
			addRow(data);
		}
	}
	
	public int getId (int row){
	
		if ((row < this.getRowCount())&& (row >= 0))
			return ((Integer)identifiers.get(row)).intValue();
		else return 0;
		
	}
	
	public boolean isCellEditable(int rowIndex, int colIndex) {
		return false;
	}

	public Class getColumnClass(int colIndex) {
		return getValueAt(0, colIndex).getClass();
	}
	
}