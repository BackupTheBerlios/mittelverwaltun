/*
 * Created on 09.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gui;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import dbObjects.Bestellung;
import dbObjects.FBHauptkonto;
import dbObjects.ZVKonto;
import dbObjects.ZVTitel;

/**
 * @author Mario
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AnnualOrderTableModel extends DefaultTableModel {
	
	// Bestelltypen
	static final int STD_TYP = 0;
	static final int ASK_TYP = 1;
	static final int ZA_TYP = 2;
	
	// Bestellphasen
	static final int SONDIERUNG = 0;
	static final int ABWICKLUNG = 1;
	static final int ABGESCHLOSSEN = 2;
	static final int STORNIERT = 2;
	
	// Aktionen
	static final int UNDEFINED = -1;
	static final int PORTIEREN = 0;
	static final int ABSCHLIESSEN = 1;
	static final int STORNIEREN = 2;
	
	ArrayList fbRowReferences = new ArrayList();
	ArrayList zvRowReferences = new ArrayList();

	ArrayList orders = new ArrayList();
	
	public AnnualOrderTableModel (ArrayList orders, AccountTable fbAccounts, AccountTable zvAccounts){
		super();
		
		String[] colheads = {"Datum", "Typ", "Phase", "Besteller", "Auftraggeber", "Fachbereichshauptkonto", "Zentralverwaltungskonto", "Bestellwert", "Aktion"};
	    
		setColumnIdentifiers(colheads);
		if (orders != null){
			
			this.orders = orders;
			
			for(int i = 0; i < orders.size(); i++){
				Bestellung order = (Bestellung)orders.get(i);
				Object[] data = new Object[9];
				
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
				
				
				if (order.getFbkonto() instanceof FBHauptkonto){
					FBHauptkonto acc = (FBHauptkonto)order.getFbkonto();
					data[5] = acc.getInstitut().getKostenstelle() + "-" + acc.getHauptkonto() + "   " + acc.getBezeichnung();
					fbRowReferences.add(new Integer(fbAccounts.getRowOfAccount(acc.getId())));
				}else{
					data[5] = "";
					fbRowReferences.add(new Integer(-1));
				}
				
				if (order.getZvtitel() instanceof ZVTitel){
					ZVKonto acc = ((ZVTitel)order.getZvtitel()).getZVKonto();
					data[6] = acc.getKapitel() + (acc.getTitelgruppe().equalsIgnoreCase("")? "      " : "/" + acc.getTitelgruppe()) + "   " + acc.getBezeichnung();
					zvRowReferences.add(new Integer(zvAccounts.getRowOfAccount(acc.getId())));
				}else{
					data[6] = "";
					zvRowReferences.add(new Integer(-1));
				}
				data[7] = new Float (order.getBestellwert()); 
				data[8] = new String("");
				
				addRow(data);
				
			}
			
		}
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
		return ((colIndex==8) && !((String)getValueAt(rowIndex, colIndex)).equalsIgnoreCase("Portieren"));
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
	
	public int getAktion(int rowIndex){
		if ((rowIndex < this.getRowCount())&& (rowIndex >= 0)){
			String value = (String)getValueAt(rowIndex,8);
			if (value.equalsIgnoreCase("Portieren"))
				return PORTIEREN;
			else if (value.equalsIgnoreCase("Abschlieﬂen"))
				return ABSCHLIESSEN;
			else if (value.equalsIgnoreCase("Stornieren"))
				return STORNIEREN;
			else return UNDEFINED;
		}else return UNDEFINED;
	}
	
	public ArrayList getOrders(){
		for(int i = 0; i < orders.size(); i++){
			Bestellung order = (Bestellung)orders.get(i);
			if (getAktion(i) == ABSCHLIESSEN)
				order.setPhase('2');
			else if (getAktion(i) == STORNIEREN)
				order.setPhase('3');
		}
		return orders;
	}
	
}	

