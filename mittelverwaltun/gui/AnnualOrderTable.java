/*
 * Created on 09.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gui;



import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * @author Mario
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AnnualOrderTable extends JTable {
	AccountTable fbAccountTab, zvAccountTab;
	
	public AnnualOrderTable(ArrayList orders, AccountTable fbAccountTab, AccountTable zvAccountTab){
	
		this.fbAccountTab = fbAccountTab;
		this.zvAccountTab = zvAccountTab;
		
		setModel(new AnnualOrderTableModel(orders, fbAccountTab, zvAccountTab));
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		applyTableRendering();
	
	}

	
	public AnnualOrderTableModel getAnnualOrderTableModel(){
		return (AnnualOrderTableModel)getModel();
	}
	
	private void applyTableRendering(){
		setRowHeight(20);
		setFont(new java.awt.Font("Dialog", 0, 11));
		
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		setDefaultEditor(Float.class, new JTableFloatEditor(0));
		JTableCurrencyRenderer jtcr = new JTableCurrencyRenderer();
		jtcr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		setDefaultRenderer(Float.class, jtcr);
		
		// Datum
		getColumnModel().getColumn(0).setPreferredWidth(65);
		getColumnModel().getColumn(0).setCellRenderer(dtcr);
		//	Typ
		getColumnModel().getColumn(1).setPreferredWidth(105);
		getColumnModel().getColumn(1).setCellRenderer(dtcr);
		//	Phase
		getColumnModel().getColumn(2).setPreferredWidth(65);
		getColumnModel().getColumn(2).setCellRenderer(dtcr);
		//	Besteller
		getColumnModel().getColumn(3).setPreferredWidth(105);		
		//	Auftraggeber
		getColumnModel().getColumn(4).setPreferredWidth(105);
		//	FBKonto
		getColumnModel().getColumn(5).setPreferredWidth(210);
		getColumnModel().getColumn(5).setCellRenderer(new ColoredCellRenderer());
		//	ZVKonto
		getColumnModel().getColumn(6).setPreferredWidth(210);
		getColumnModel().getColumn(6).setCellRenderer(new ColoredCellRenderer());
		//	Bestellwert
		getColumnModel().getColumn(7).setPreferredWidth(75);
		//	Aktion
		getColumnModel().getColumn(8).setPreferredWidth(100);
		getColumnModel().getColumn(8).setCellRenderer(new ActionCellRenderer());
			JComboBox cbAktion = new JComboBox();
		  	cbAktion.addItem("Abschlieﬂen");
		  	cbAktion.addItem("Stornieren");
		  	cbAktion.setFont(new java.awt.Font("Dialog", 1, 11));
		  	cbAktion.setForeground(getForeground());
		  
	  	getColumnModel().getColumn(8).setCellEditor(new DefaultCellEditor(cbAktion));
	}

	public void actualize(){
		for (int i=0; i < getAnnualOrderTableModel().getRowCount(); i++){
			if (fbAccountTab.isAccountBudgetCopied(getAnnualOrderTableModel().getFbAccountTabRow(i)) && zvAccountTab.isAccountBudgetCopied(getAnnualOrderTableModel().getZvAccountTabRow(i)))
				getAnnualOrderTableModel().setValueAt(new String("Portieren"),i,8);
			else
				getAnnualOrderTableModel().setValueAt(new String("Abschlieﬂen"),i,8);			
		}
		
		applyTableRendering();
	}
	
	private static class ColoredCellRenderer implements TableCellRenderer {
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			JLabel label = new JLabel((String)value);
			
			Color green = new Color(204,255,204);
			Color red = new Color(255,204,204);
			Color orange = new Color (255, 255, 204);
			
			label.setOpaque(true);
			label.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			label.setFont(table.getFont());
			label.setForeground(table.getForeground());
			
			if (col == 5){
				if (((AnnualOrderTable)table).fbAccountTab.isAccountCopied(((AnnualOrderTable)table).getAnnualOrderTableModel().getFbAccountTabRow(row))){
					if (((AnnualOrderTable)table).fbAccountTab.isAccountBudgetCopied(((AnnualOrderTable)table).getAnnualOrderTableModel().getFbAccountTabRow(row)))
						label.setBackground(green);
					else
						label.setBackground(orange);
				}else{
					label.setBackground(red);
				}
			}
				
			if (col == 6){
				if (((AnnualOrderTable)table).zvAccountTab.isAccountCopied(((AnnualOrderTable)table).getAnnualOrderTableModel().getZvAccountTabRow(row))){
					if (((AnnualOrderTable)table).zvAccountTab.isAccountBudgetCopied(((AnnualOrderTable)table).getAnnualOrderTableModel().getZvAccountTabRow(row)))
						label.setBackground(green);
					else
						label.setBackground(orange);
				}else{
					label.setBackground(red);
				}
			}	
			
			return label;
		}
	}
	
	private static class ActionCellRenderer implements TableCellRenderer {
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			Color green = new Color(204,255,204);
			Color orange = new Color(255,255,204);
			Color red = new Color(255,204,204);
								
			if (col == 8){
				

				JLabel label = new JLabel((String)value);
					
				label.setOpaque(true);
				label.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
				label.setFont(new java.awt.Font("Dialog", 1, 11));
				label.setForeground(table.getForeground());
				
				if (((String)value).equalsIgnoreCase("Portieren"))
					label.setBackground(green);
				else if (((String)value).equalsIgnoreCase("Abschlieﬂen"))
					label.setBackground(orange);
				else
					label.setBackground(red);
				label.setHorizontalAlignment(SwingConstants.CENTER);
				return label;				
	
			}else return null;
		}
	}	
}
