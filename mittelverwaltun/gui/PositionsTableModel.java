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
	
	public static final int ANZEIGE = 0;
	public static final int STD_ABWICKLUNG = 1;
	public static final int ASK_ABWICKLUNG = 2;
	 	
	int type;
	ArrayList identifiers;
		
	public PositionsTableModel (int type, ArrayList positions){
		
		super();
		this.type = type;
		
		if (type == STD_ABWICKLUNG){
			String[] colheads = {"Menge", "Artikel", "Einzelpreis", "Mwst", "Rabatt", "Gesamt", "Beglichen", "" }; 
			setColumnIdentifiers(colheads);
		} else if (type == ASK_ABWICKLUNG){
				String[] colheads = {"Menge", "Artikel", "Institut", "Einzelpreis", "Mwst", "Gesamt", "Beglichen", "" }; 
				setColumnIdentifiers(colheads);
				
		} else {
			String[] colheads = {"Menge", "Artikel", "Einzelpreis", "Mwst", "Rabatt", "Gesamt" }; 
			setColumnIdentifiers(colheads);
		}
		
		setPositions (positions);
	}

	
	public void setPositions (ArrayList positions){
		identifiers = new ArrayList();
		
		for(int i = 0; i < positions.size(); i++){
			Object[] data;
			
			if ((type == STD_ABWICKLUNG)||(type == ASK_ABWICKLUNG))
				data = new Object[8];
			else //default type = 0
				data = new Object[6];
			
			Position position = (Position)positions.get(i);
			
			
			data[0] = new Integer(position.getMenge());
			data[1] = position.getArtikel();
			
			if (type == ASK_ABWICKLUNG){
				data[2] = position.getInstitut();
				data[3] = new Float(position.getEinzelPreis());
				data[4] = new Float(position.getMwst());
				data[5] = new Float(position.getGesamtpreis());
			}else{
				data[2] = new Float(position.getEinzelPreis());
				data[3] = new Float(position.getMwst());
				data[4] = new Float(position.getRabatt());
				data[5] = new Float(position.getGesamtpreis());
			}
			
			if (type != ANZEIGE){
				data[6] = new Boolean(position.getBeglichen()); 
				data[7] = new String("X");
			}
						
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
		
		if (type == STD_ABWICKLUNG){
			Object[] obj = {new Integer(1),"",new Float(0), new Float(0.16), new Float(0), new Float(0), new Boolean(false), new String("X")};
			addRow(obj);
		}else if (type == ASK_ABWICKLUNG){
			Object[] obj = {new Integer(1),"", null, new Float(0), new Float(0.16), new Float(0), new Boolean(false), new String("X")};
			addRow(obj);
		}else{
			Object[] obj = {new Integer(1),"",new Float(0), new Float(0.16), new Float(0), new Float(0)};
			addRow(obj);
		}
		
		fireTableDataChanged();
		identifiers.add(new Integer(0));
	}
	
	public boolean isCellEditable(int rowIndex, int colIndex) {
		if ((type == STD_ABWICKLUNG)||(type == ASK_ABWICKLUNG))
			return colIndex != 5;
		else
			return false;
	}

	public Class getColumnClass(int colIndex) {
		return getValueAt(0, colIndex).getClass();
	}

	public void setValueAt(Object obj, int rowIndex, int colIndex) {
		
		  super.setValueAt(obj,rowIndex,colIndex);
		   	if(colIndex  < getColumnCount() - 1){
			
			  	try{
			  		
			  		if (type==STD_ABWICKLUNG){
			  			
			  			int menge = ((Integer)getValueAt(rowIndex,0)).intValue();
						float preis = ((Float)getValueAt(rowIndex, 2)).floatValue();
						float mwst = ((Float)getValueAt(rowIndex, 3)).floatValue();
						float rabatt = ((Float)getValueAt(rowIndex, 4)).floatValue();
					    
						super.setValueAt(new Float(menge * preis + (menge * preis * mwst) - rabatt), rowIndex, 5);
			  		
			  		}else if (type==ASK_ABWICKLUNG){
			  		
			  			int menge = ((Integer)getValueAt(rowIndex,0)).intValue();
						float preis = ((Float)getValueAt(rowIndex, 3)).floatValue();
						float mwst = ((Float)getValueAt(rowIndex, 4)).floatValue();
											    
						super.setValueAt(new Float(menge * preis + (menge * preis * mwst)), rowIndex, 5);
			  		
			  		}
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
		
		if ((type == STD_ABWICKLUNG)||(type == ASK_ABWICKLUNG)){
			for (int i=0; i<getRowCount();i++){
				if(!((Boolean)getValueAt(i,6)).booleanValue())
					debit += ((Float)getValueAt(i,5)).floatValue();
			}
		}
		return debit;
	}

}