/*
 * Created on 08.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gui;



import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
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
public class MappingsTable extends JTable {
	
	AccountTable fbAccountTab, zvAccountTab;
	
	public MappingsTable(ArrayList accounts, AccountTable fbAccountTab, AccountTable zvAccountTab){
	
		this.fbAccountTab = fbAccountTab;
		this.zvAccountTab = zvAccountTab;
		
		setModel(new MappingsTableModel(accounts, fbAccountTab, zvAccountTab));
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		applyTableRendering();
	
	}

	
	public MappingsTableModel getMappingsTableModel(){
		return (MappingsTableModel)getModel();
	}
	
	private void applyTableRendering(){
		setRowHeight(20);
		setFont(new java.awt.Font("Dialog", 0, 11));
			
		// FBKonto
		getColumnModel().getColumn(0).setPreferredWidth(250);
		getColumnModel().getColumn(0).setCellRenderer(new ColoredCellRenderer());
		// ZVKonto
		getColumnModel().getColumn(1).setPreferredWidth(250);
		getColumnModel().getColumn(1).setCellRenderer(new ColoredCellRenderer());
		// Typ
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		getColumnModel().getColumn(2).setPreferredWidth(125);
		getColumnModel().getColumn(2).setCellRenderer(dtcr);
		// Übernahme
		getColumnModel().getColumn(3).setPreferredWidth(80);
		getColumnModel().getColumn(3).setCellRenderer(new ColoredBooleanCellRenderer());
	
	}


	public void actualize(){
		for (int i=0; i < getMappingsTableModel().getRowCount(); i++){
			if (fbAccountTab.isAccountCopied(getMappingsTableModel().getFbAccountTabRow(i)) && zvAccountTab.isAccountCopied(getMappingsTableModel().getZvAccountTabRow(i)))
				getMappingsTableModel().setValueAt(new Boolean(true),i,3);
			else
				getMappingsTableModel().setValueAt(new Boolean(false),i,3);
		}
		
		applyTableRendering();
	}
	
	private static class ColoredBooleanCellRenderer implements TableCellRenderer {

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			JCheckBox cb = new JCheckBox();
			cb.setSelected(((Boolean)value).booleanValue());
			cb.setOpaque(true);
			cb.setMargin(new Insets(0,0,0,0));
			cb.setIconTextGap(0);
			cb.setDoubleBuffered(true);
			cb.setHorizontalAlignment(SwingConstants.CENTER);
			//cb.setEnabled(false);
			
			if (cb.isSelected())
				cb.setBackground(new Color(204,255,204));
			else
				cb.setBackground(new Color(255,204,204));
			
			return cb;
		}
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
			
			if (col == 0){
				if (((MappingsTable)table).fbAccountTab.isAccountCopied(((MappingsTable)table).getMappingsTableModel().getFbAccountTabRow(row))){
					if (((MappingsTable)table).fbAccountTab.isAccountBudgetCopied(((MappingsTable)table).getMappingsTableModel().getFbAccountTabRow(row)))
						label.setBackground(green);
					else
						label.setBackground(orange);
				}else{
					label.setBackground(red);
				}
			}
				
			if (col == 1){
				if (((MappingsTable)table).zvAccountTab.isAccountCopied(((MappingsTable)table).getMappingsTableModel().getZvAccountTabRow(row))){
					if (((MappingsTable)table).zvAccountTab.isAccountBudgetCopied(((MappingsTable)table).getMappingsTableModel().getZvAccountTabRow(row)))
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
}
