/*
 * Created on 28.02.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gui;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import dbObjects.Institut;
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
	public static final int ASK_STANDARD = 3;
	
	private boolean editable = true;
	private int type;
	private ArrayList identifiers;
		
	public PositionsTableModel (int type, boolean editable, ArrayList positions){
		super();
		this.type = type;
		this.editable = editable;
		
		if (type == STD_ABWICKLUNG){
			String[] colheads = {"Menge", "Artikel", "Einzelpreis", "Mwst", "Rabatt", "Gesamt", "Beglichen", "" }; 
			setColumnIdentifiers(colheads);
		} else if (type == ASK_ABWICKLUNG){
				String[] colheads = {"Menge", "Artikel", "Institut", "Einzelpreis", "Mwst", "Gesamt", "Beglichen", "" }; 
				setColumnIdentifiers(colheads);
				
		} else if (type == ASK_STANDARD){
				String[] colheads = {"Menge", "Artikel", "Institut", "Einzelpreis", "Mwst", "Gesamt", "" }; 
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
			else if(type == ASK_STANDARD)
				data = new Object[7];
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
			
			if(type == ASK_STANDARD){ 
				data[6] = new String("X");
			}else if(type != ANZEIGE ){
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
		}else if (type == ASK_STANDARD){
			Object[] obj = {new Integer(1),"", null, new Float(0), new Float(0.16), new Float(0), new String("X")};
			addRow(obj);
		}else{
			Object[] obj = {new Integer(1),"",new Float(0), new Float(0.16), new Float(0), new Float(0)};
			addRow(obj);
		}
		
		fireTableDataChanged();
		identifiers.add(new Integer(0));
	}
	
	public boolean isCellEditable(int rowIndex, int colIndex) {
		if (editable && ((type == STD_ABWICKLUNG)||(type == ASK_ABWICKLUNG)||(type == ASK_STANDARD)))
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
			  		
			  		}else if (type==ASK_ABWICKLUNG || type==ASK_STANDARD){
			  		
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
		
		sum = ((float)Math.round(sum * 100f))/100f;
		
		return sum;
	}
	
	/**
	 * gibt die Summe der Mehrwertsteuer der Positionen mit 7 %
	 * @return Summe der 7 % Mehrwertsteuer
	 * @author robert
	 */
	public float get7PercentSum(){
	
		float mwst7 = 0.0f;
	
		for (int i=0; i<getRowCount();i++){
			float mwst = 0.0f;
			float preis = 0.0f;
			
			if (type==STD_ABWICKLUNG){
				mwst = ((Float)getValueAt(i, 3)).floatValue();
				preis = ((Float)getValueAt(i, 2)).floatValue();
			}else if (type==ASK_ABWICKLUNG || type==ASK_STANDARD){
				mwst = ((Float)getValueAt(i, 4)).floatValue();
				preis = ((Float)getValueAt(i, 3)).floatValue();
			}
		
			if(mwst == 0.07f){
				int menge = ((Integer)getValueAt(i,0)).intValue();

				mwst7 += (menge * preis * mwst);
			}
		}
	
		return mwst7;
	}
	
	/**
	 * gibt die Summe der Mehrwertsteuer der Positionen mit 7 %
	 * @return Summe der 7 % Mehrwertsteuer
	 * @author robert
	 */
	public float get16PercentSum(){

		float mwst16 = 0.0f;
							
		for (int i=0; i<getRowCount();i++){
			float mwst = 0.0f;
			float preis = 0.0f;
			
			if (type==STD_ABWICKLUNG){
				mwst = ((Float)getValueAt(i, 3)).floatValue();
				preis = ((Float)getValueAt(i, 2)).floatValue();
			}else if (type==ASK_ABWICKLUNG || type==ASK_STANDARD){
				mwst = ((Float)getValueAt(i, 4)).floatValue();
				preis = ((Float)getValueAt(i, 3)).floatValue();
			}
		
			if(mwst == 0.16f){
				int menge = ((Integer)getValueAt(i,0)).intValue();

				mwst16 += (menge * preis * mwst);
			}
		}

		return mwst16;
	}
	
	public float getOrderDebit(){
		
		float debit = 0.0f;
		
		if ((type == STD_ABWICKLUNG)||(type == ASK_ABWICKLUNG)){
			for (int i=0; i<getRowCount();i++){
				if(!((Boolean)getValueAt(i,6)).booleanValue())
					debit += ((Float)getValueAt(i,5)).floatValue();
			}
		}
		
		debit = ((float)Math.round(debit * 100f))/100f;
		
		return debit;
	}

	public ArrayList getOrderPositions(){
		if (type==ANZEIGE){
			ArrayList positions = new ArrayList();
				for (int i=0; i < getRowCount(); i++){
					positions.add( new Position(((Integer)identifiers.get(i)).intValue(), (String)getValueAt(i,1),
												((Float)getValueAt(i,2)).floatValue(), ((Integer)getValueAt(i,0)).intValue(),
												((Float)getValueAt(i,3)).floatValue(), ((Float)getValueAt(i,4)).floatValue())
								);
				}
			return positions;

		}else if (type==STD_ABWICKLUNG){
			ArrayList positions = new ArrayList();
				for (int i=0; i < getRowCount(); i++){
					positions.add( new Position(((Integer)identifiers.get(i)).intValue(), (String)getValueAt(i,1),
												((Float)getValueAt(i,2)).floatValue(), ((Integer)getValueAt(i,0)).intValue(),
												((Float)getValueAt(i,3)).floatValue(), ((Float)getValueAt(i,4)).floatValue(),
												((Boolean)getValueAt(i,6)).booleanValue())
								);
				}
			return positions;
		}else if (type==ASK_ABWICKLUNG){
			ArrayList positions = new ArrayList();
				for (int i=0; i < getRowCount(); i++){
					positions.add( new Position(((Integer)identifiers.get(i)).intValue(), (String)getValueAt(i,1),
												((Float)getValueAt(i,3)).floatValue(), ((Integer)getValueAt(i,0)).intValue(),
												((Float)getValueAt(i,4)).floatValue(), (Institut)getValueAt(i,2),
												((Boolean)getValueAt(i,6)).booleanValue())
								);
				}
			return positions;

		}else if (type==ASK_ABWICKLUNG){
			ArrayList positions = new ArrayList();
				for (int i=0; i < getRowCount(); i++){
					positions.add( new Position(((Integer)identifiers.get(i)).intValue(), (String)getValueAt(i,1),
												((Float)getValueAt(i,3)).floatValue(), ((Integer)getValueAt(i,0)).intValue(),
												((Float)getValueAt(i,4)).floatValue(), (Institut)getValueAt(i,2), false)
								);
				}
			return positions;
	
		}else return null;
	
	}
	
	public void setEditable (boolean editable){
		this.editable = editable;
	}
	
	public boolean isEditable(){
		return editable;
	}
	
	public void payAllPositions(){
		if (editable && ((type == STD_ABWICKLUNG)||(type == ASK_ABWICKLUNG))){
			for (int i=0; i<getRowCount();i++){
				setValueAt(new Boolean(true), i, 6);
			}	
		}
	}
	
}