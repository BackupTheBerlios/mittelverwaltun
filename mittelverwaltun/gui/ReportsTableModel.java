package gui;

import java.sql.Date;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

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
		identifiers = new ArrayList();
		
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
			String[] colheads = {"ZV-Konto", "Ausgaben", "FBI-Schlüsselnummer", "Hül-Nr", "Typ", "Datum", "Status", ""}; 
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
				data = new Object[8];
			
			// eine Zeile des Reports
			ArrayList row = (ArrayList)content.get(i);
			
			if (type == Reports.REPORT_1){
				data[0] = (String)row.get(0);							// ZV-Konto
				data[1] = (Float)row.get(1);							// zugewiesene Mittel
				data[2] = (Float)row.get(2);							// Ausgaben
				data[3] = (Float)row.get(3);							// Kontostand
				
			} else if (type == Reports.REPORT_2){
				data[0] = (String)row.get(0);							// ZV-Konto
				data[1] = (Float)row.get(1);							// zugewiesene Mittel
				data[2] = (Float)row.get(2);							// Ausgaben
				data[3] = (Float)row.get(3);							// Verteilungen
				
			} else if (type == Reports.REPORT_3){
				data[0] = (String)row.get(0);							// FB-Konto
				data[1] = (Float)row.get(1);							// verteilte Mittel
				data[2] = (Float)row.get(2);							// Ausgaben
				data[3] = (Float)row.get(3);							// Kontostand
				
			} else if (type == Reports.REPORT_4){
				data[0] = (String)row.get(0);							// FB-Konto
				data[1] = (String)row.get(1);							// ZV-Konto
				data[2] = (Float)row.get(2);							// Ausgaben
				
			} else if (type == Reports.REPORT_5){
				data[0] = (String)row.get(0);							// ZV-Konto
				data[1] = (String)row.get(1);							// Institut
				data[2] = (Float)row.get(2);							// Ausgaben
				data[3] = (Float)row.get(3);							// Kontostand
					
			} else if (type == Reports.REPORT_6){
				data[0] = (String)row.get(0);							// ZV-Konto
				data[1] = (Float)row.get(1);							// Ausgaben
						
			} else if (type == Reports.REPORT_7){
				data[0] = (String)row.get(0);							// ZV-Konto
				data[1] = (Float)row.get(1);							// Ausgaben
				data[2] = (String)row.get(2);							// FBI-Schlüsselnummer
				data[3] = (String)row.get(3);							// Hül-Nr
				
				if (row.get(4).equals("0"))										// Typ
					data[4] = "Standardbestellung";
				else if (row.get(4).equals("1"))
					data[4] = "ASK-Bestellung";
				else if (row.get(4).equals("2"))
					data[4] = "Zahlungsanforderung";
				else data[4] = "n.a.";

				data[5] = ((Date)row.get(5)).toString();								// Datum
				
				if (row.get(6).equals("0"))											// Phase
					data[6] = (String)"Sondierung";
				else if (row.get(6).equals("1"))
					data[6] = (String)"Abwicklung";
				else if (row.get(6).equals("2"))
					data[6] = (String)"Abgeschlossen";
				else if (row.get(6).equals("3"))
					data[6] = (String)"Storniert";
				else data[6] = (String)"unbekannt";
				
				data[7] = new String("X");								// Button anzeigen
				identifiers.add((Integer)row.get(7));			// Id der Bestellung
							
			} else if (type == Reports.REPORT_8){
				data[0] = (String)row.get(0);							// ZV-Konto
				data[1] = (Float)row.get(1);							// Einnahmen
							
			}
						
			addRow(data);
		}
	}
	
	public int getType (int row){
		if(type == Reports.REPORT_7){
			if ((row < this.getRowCount())&& (row >= 0)){
				String type = (String)getValueAt(row, 4);
				if (type.equals("Standardbestellung"))
					return OrderTableModel.STD_TYP;
				else if (type.equals("ASK-Bestellung"))
					return OrderTableModel.ASK_TYP;
				else if (type.equals("Zahlungsanforderung"))
					return OrderTableModel.ZA_TYP;
				else return -1;
			} else return -1;
		}else
			return -1;		
	}
	
	
	public int getPhase (int row){
		if(type == Reports.REPORT_7){
			if ((row < this.getRowCount())&& (row >= 0)){
				String type = (String)getValueAt(row, 6);
				if (type.equals("Sondierung"))
					return OrderTableModel.SONDIERUNG;
				else if (type.equals("Abwicklung"))
					return OrderTableModel.ABWICKLUNG;
				else if (type.equals("Abgeschlossen"))
					return OrderTableModel.ABGESCHLOSSEN;
				else if (type.equals("Storniert"))
					return OrderTableModel.STORNIERT;
				else return -1;
			} else return -1;
		}else
			return -1;
	}
	
	public int getId (int row){
	
		if ((row < this.getRowCount())&& (row >= 0))
			return ((Integer)identifiers.get(row)).intValue();
		else return 0;
		
	}
	
	public boolean isCellEditable(int rowIndex, int colIndex) {
		if(type == Reports.REPORT_7 && colIndex == 7)
			return true;
		else
			return false;
	}

	public Class getColumnClass(int colIndex) {
		System.out.println("" + colIndex);
		return getValueAt(0, colIndex).getClass();
	}
	
}