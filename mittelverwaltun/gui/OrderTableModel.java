/*
 * Created on 01.03.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gui;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import dbObjects.Bestellung;

/**
 * @author Mario
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OrderTableModel extends DefaultTableModel {
	
	static final int STD_TYP = 0;
	static final int ASK_TYP = 1;
	static final int ZA_TYP = 2;
	static final int SONDIERUNG = 0;
	static final int ABWICKLUNG = 1;
	static final int ABGESCHLOSSEN = 2;
	static final int STORNIERT = 2;
	
	ArrayList identifiers;
		
	public OrderTableModel (ArrayList orders){
		super();
		identifiers = new ArrayList();
		
		String[] colheads = {"Datum", "Typ", "Phase", "Besteller", "Auftraggeber", "Empfänger", "Bestellwert", ""};
	    
		setColumnIdentifiers(colheads);
		if (orders != null){
			for(int i = 0; i < orders.size(); i++){
				Bestellung order = (Bestellung)orders.get(i);
				Object[] data = new Object[8];
				
				data[0] = order.getDatum();
				
				if (order.getTyp()== '0')
					data[1] = "Standardbestellung";
				else if (order.getTyp()== '1')
					data[1] = "ASK-Bestellung";
				else if (order.getTyp()== '2')
					data[1] = "Zahlungsanforderung";
				else data[1] = "n.a.";
				
				if (order.getPhase()== '0')
					data[2] = "Sondierung";
				else if (order.getPhase()== '1')
					data[2] = "Abwicklung";
				else if (order.getPhase()== '2')
					data[2] = "Abgeschlossen";
				else if (order.getPhase()== '3')
					data[2] = "Storniert";
				else data[2] = "unbekannt";
							
				data[3] = order.getBesteller().getName() + ", " + order.getBesteller().getVorname();
				data[4] = order.getAuftraggeber().getName() + ", " + order.getAuftraggeber().getVorname();
				data[5] = order.getEmpfaenger().getName() + ", " + order.getEmpfaenger().getVorname();
				
				data[6] = new Float (order.getBestellwert()); 
				data[7] = new String("show");
				
				addRow(data);
				identifiers.add(new Integer(order.getId()));
			}
			
		}
	}

	public int getId (int row){
		
		if ((row < this.getRowCount())&& (row >= 0))
			return ((Integer)identifiers.get(row)).intValue();
		else return 0;
			
	}
	
	public int getType (int row){
		
		if ((row < this.getRowCount())&& (row >= 0)){
			String type = (String)getValueAt(row, 1);
			if (type.equals("Standardbestellung"))
				return STD_TYP;
			else if (type.equals("ASK-Bestellung"))
				return ASK_TYP;
			else if (type.equals("Zahlungsanforderung"))
				return ZA_TYP;
			else return -1;
		} else return -1;
			
	}
	
	public int getPhase (int row){
		
		if ((row < this.getRowCount())&& (row >= 0)){
			String type = (String)getValueAt(row, 2);
			if (type.equals("Sondierung"))
				return SONDIERUNG;
			else if (type.equals("Abwicklung"))
				return ABWICKLUNG;
			else if (type.equals("Abgeschlossen"))
				return ABGESCHLOSSEN;
			else if (type.equals("Storniert"))
				return STORNIERT;
			else return -1;
		} else return -1;
			
	}
	
	public boolean isCellEditable(int rowIndex, int colIndex) {
		return colIndex == 7;
	}

	public Class getColumnClass(int colIndex) {
		return getValueAt(0, colIndex).getClass();
	}
}