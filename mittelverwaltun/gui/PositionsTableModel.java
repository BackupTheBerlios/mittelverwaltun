/*
 * Created on 28.02.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gui;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;
import dbObjects.Position;

/**
 * @author Mario
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class PositionsTableModel extends DefaultTableModel {
	
	ArrayList identifiers;
		
	public PositionsTableModel (ArrayList positions){
		super();
		identifiers = new ArrayList();
		
		String[] colheads = {"Menge", "Artikel", "Einzelpreis", "Mwst", "Rabatt", "Gesamt", "Beglichen", "" }; 
		setColumnIdentifiers(colheads);
							
		for(int i = 0; i < positions.size(); i++){
			Position position = (Position)positions.get(i);
			Object[] data = new Object[8];
			
			data[0] = new Integer(position.getMenge());
			data[1] = position.getArtikel();
			data[2] = new Float(position.getEinzelPreis());
			data[3] = new Float(position.getMwst());
			data[4] = new Float(position.getRabatt());
			data[5] = new Float(position.getGesamtpreis());
			data[6] = new Boolean(position.getBeglichen()); 
			data[7] = new String("X");
			
			addRow(data);
			identifiers.add(new Integer(position.getId()));
			
		}
	}

	public void removePosition(int index){
		removeRow(index);
		fireTableDataChanged();
		identifiers.remove(index);
	}
	
	public void addPosition(){
		
		Object[] obj = {new Integer(1),"",new Float(0), new Float(0.16), new Float(0), new Float(0), new Boolean(false), new String("X")};
		addRow(obj);
		fireTableDataChanged();
		identifiers.add(new Integer(0));
	}
	
	public boolean isCellEditable(int rowIndex, int colIndex) {
		return colIndex != 5;
	}

	public Class getColumnClass(int colIndex) {
		return getValueAt(0, colIndex).getClass();
	}

	public void setValueAt(Object obj, int rowIndex, int colIndex) {
		
		  super.setValueAt(obj,rowIndex,colIndex);
		  
		  if(colIndex  < getColumnCount() -1){
			 try{
				int menge = ((Integer)getValueAt(rowIndex,0)).intValue();
				float preis = ((Float)getValueAt(rowIndex, 2)).floatValue();
				float mwst = ((Float)getValueAt(rowIndex, 3)).floatValue();
				float rabatt = ((Float)getValueAt(rowIndex, 4)).floatValue();
			    super.setValueAt(new Float(menge * preis + (menge * preis * mwst) - rabatt), rowIndex, 5); 
			 }catch(Exception e){
			 	super.setValueAt(new Float(0), rowIndex, 5);
			 }
		  }
	}
	
	public float getOrderSum(){
		
		float sum = 0.0f;
		
		for (int i=0; i<getRowCount();i++){
			sum += ((Float)getValueAt(i,5)).floatValue();
		}
		
		return sum;
	}
	
	public float getOrderDebit(){
		
		float debit = 0.0f;
		
		for (int i=0; i<getRowCount();i++){
			if(!((Boolean)getValueAt(i,6)).booleanValue())
				debit += ((Float)getValueAt(i,5)).floatValue();
		}
		
		return debit;
	}
	
}
//
//DefaultTableModel model;
//ArrayList identifiers;
//int test = 0;
//
//public PositionsTableModel (ArrayList positions){
//	identifiers = new ArrayList();
//	
//	String[] colheads = {"Menge", "Artikel", "Einzelpreis", "Mwst", "Rabatt", "Gesamt", "Beglichen", "" }; 
//	Object[][] data = new Object[positions.size()][8];
//	
//	for(int i = 0; i < positions.size(); i++){
//		
//		Position position = (Position)positions.get(i);
//		data[i][0] = new Integer(position.getMenge());
//		data[i][1] = position.getArtikel();
//		data[i][2] = new Float(position.getEinzelPreis());
//		data[i][3] = new Float(position.getMwst());
//		data[i][4] = new Float(position.getRabatt());
//		data[i][5] = new Float(position.getGesamtpreis());
//		data[i][6] = new Boolean(position.getBeglichen()); 
//		data[i][7] = new String("X");
//		identifiers.add(new Integer(position.getId()));
//		
//	}
//	
//	model = new DefaultTableModel(data, colheads);
//}
//
//public void removePosition(int index){
//	model.removeRow(index);
//	model.fireTableDataChanged();
//	identifiers.remove(index);
//}
//
//public void addPosition(){
//	
//	Object[] obj = {new Integer(1),"",new Float(0), new Float(0.16), new Float(0), new Float(0), new Boolean(false), new String("X")};
//	model.addRow(obj);
//	model.fireTableDataChanged();
//	identifiers.add(new Integer(0));
//}
//
//public int getColumnCount() {
//	return model.getColumnCount();
//}
//
//public int getRowCount() {
//	return model.getRowCount();
//}
//
//public boolean isCellEditable(int rowIndex, int colIndex) {
//	return colIndex != 5;
//}
//
//public Class getColumnClass(int colIndex) {
//	return model.getValueAt(0, colIndex).getClass();
//}
//
//public Object getValueAt(int rowIndex, int colIndex) {
//	return model.getValueAt(rowIndex, colIndex);
//}
//
//public void setValueAt(Object obj, int rowIndex, int colIndex) {
//	
//	  model.setValueAt(obj,rowIndex,colIndex);
//	  
//	  if(colIndex  < getColumnCount() -1){
//		 try{
//			int menge = ((Integer)getValueAt(rowIndex,0)).intValue();
//			float preis = ((Float)getValueAt(rowIndex, 2)).floatValue();
//			float mwst = ((Float)getValueAt(rowIndex, 3)).floatValue();
//			float rabatt = ((Float)getValueAt(rowIndex, 4)).floatValue();
//		    model.setValueAt(new Float(menge * preis + (menge * preis * mwst) - rabatt), rowIndex, 5); 
//		 }catch(Exception e){
//		 	model.setValueAt(new Float(0), rowIndex, 5);
//		 }
//	  }
//	  
//	  //model.fireTableRowsUpdated(rowIndex,rowIndex);
//	  model.fireTableDataChanged();
//	  
//}
//
//public String getColumnName(int colIndex) {
//	return model.getColumnName(colIndex);
//}
//
//public void addTableModelListener(TableModelListener listener) {
//	model.addTableModelListener(listener);		
//}
//
//public void removeTableModelListener(TableModelListener listener) {
//	model.removeTableModelListener(listener);
//	
//}
//
//public void fireTableStructureChanged(){
//	model.fireTableStructureChanged();
//}
//
//
//}
