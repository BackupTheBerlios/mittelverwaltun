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
			String[] colheads = {"ZVKonto", "Institut", "Institut-Ausgaben", "Institut-Kontostand"};
			setColumnIdentifiers(colheads);
		}	else if (type == Reports.REPORT_6){
			String[] colheads = {"Instiut", "ZV-Konto", "Ausgaben"}; 
			setColumnIdentifiers(colheads);
		} else if (type == Reports.REPORT_7){
			String[] colheads = {"Instiut", "ZV-Konto", "Ausgaben", "FBI-Schlüsselnummer", "Hül-Nr", "Typ", "Datum", "Status", ""}; 
			setColumnIdentifiers(colheads);
		} else if (type == Reports.REPORT_8){
			String[] colheads = {"Instiut", "FB-Konto", "Einnahmen"}; 
			setColumnIdentifiers(colheads);
		}
		
		fillReport (content);
	}

	
	public void fillReport (ArrayList content){
		
		for(int i = 0; i < content.size(); i++){
			Object[] data = null;
			
			if (type == Reports.REPORT_1 || type == Reports.REPORT_2 || type == Reports.REPORT_3 || type == Reports.REPORT_5)
				data = new Object[4];
			else if(type == Reports.REPORT_4 || type == Reports.REPORT_8)
				data = new Object[3];
			else if(type == Reports.REPORT_6 )
				data = new Object[3];
			else if(type == Reports.REPORT_7)
				data = new Object[9];
			
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
				data[0] = (String)row.get(0);							// Institut
				data[1] = (String)row.get(1);							// Institut
				data[2] = (Float)row.get(2);							// Ausgaben
				data[3] = (Float)row.get(3);							// Kontostand
					
			} else if (type == Reports.REPORT_6){
				data[0] = (String)row.get(0);							// Institut
				data[1] = (String)row.get(1);							// ZV-Konto
				data[2] = (Float)row.get(2);							// Ausgaben
						
			} else if (type == Reports.REPORT_7){
				data[0] = (String)row.get(0);							// Institut
				data[1] = (String)row.get(1);							// ZV-Konto
				data[2] = (Float)row.get(2);							// Ausgaben
				data[3] = (String)row.get(3);							// FBI-Schlüsselnummer
				data[4] = (String)row.get(4);							// Hül-Nr
				
				if (row.get(5).equals("0"))										// Typ
					data[5] = "Standardbestellung";
				else if (row.get(5).equals("1"))
					data[5] = "ASK-Bestellung";
				else if (row.get(5).equals("2"))
					data[5] = "Zahlungsanforderung";
				else data[5] = "n.a.";

				data[6] = ((Date)row.get(6)).toString();								// Datum
				
				if (row.get(7).equals("0"))											// Phase
					data[7] = (String)"Sondierung";
				else if (row.get(7).equals("1"))
					data[7] = (String)"Abwicklung";
				else if (row.get(7).equals("2"))
					data[7] = (String)"Abgeschlossen";
				else if (row.get(7).equals("3"))
					data[7] = (String)"Storniert";
				else data[7] = (String)"unbekannt";
				
				data[8] = new String("X");								// Button anzeigen
				identifiers.add((Integer)row.get(8));			// Id der Bestellung
							
			} else if (type == Reports.REPORT_8){
				data[0] = (String)row.get(0);							// Institut
				data[1] = (String)row.get(1);							// FB-Konto
				data[2] = (Float)row.get(2);							// Einnahmen
							
			}
						
			addRow(data);
		}
	}
	
	public int getType (int row){
		if(type == Reports.REPORT_7){
			if ((row < this.getRowCount())&& (row >= 0)){
				String type = (String)getValueAt(row, 5);
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
				String type = (String)getValueAt(row, 7);
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
		if(type == Reports.REPORT_7 && colIndex == 8)
			return true;
		else
			return false;
	}

	public Class getColumnClass(int colIndex) {
		System.out.println("" + colIndex);
		return getValueAt(0, colIndex).getClass();
	}
	
}