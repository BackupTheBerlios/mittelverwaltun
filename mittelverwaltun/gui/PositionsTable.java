/*
 * Created on 28.02.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gui;


import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * @author Mario
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PositionsTable extends JTable implements ActionListener {
	
	public static final int ANZEIGE = 0;
	public static final int STD_ABWICKLUNG = 1;
	
	int type = 0;
	TableModelListener tml = null;
	
	public PositionsTable(int type, TableModelListener tml, ArrayList positions){
		
		this.type = type;
		this.tml = tml;
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		PositionsTableModel model = new PositionsTableModel(type, positions);
		model.addTableModelListener(tml);
		setModel(model);
		
		applyTableRendering();
	}

	public PositionsTable(int type, ArrayList positions){
		
		PositionsTableModel model = new PositionsTableModel(type, positions);
		setModel(model);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		applyTableRendering();
	}
	
	private static class TableButtonCellRenderer implements TableCellRenderer {        
		final JButton button;        
		TableButtonCellRenderer() {            
			button = new JButton(Functions.getDelIcon(this.getClass()));
			button.setActionCommand("deletePosition");
			button.setMargin(new Insets(2,2,2,2));         
		}        
	
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {            
			return button;        
		}    
	}    
				
	private static class TableButtonCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {        
		
		final JButton button;        
		final ActionListener callback;
		        
		TableButtonCellEditor(ActionListener callback) {            
			button = new JButton(Functions.getDelIcon(this.getClass()));
			button.setActionCommand("deletePosition");           
			this.callback = callback;  
			button.setMargin(new Insets(2,2,2,2));         
			button.addActionListener(this);        
		} 
		
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {            
			return button;        
		}        
		
		public Object getCellEditorValue() {            
			return null;        
		}        
		
		public void actionPerformed(ActionEvent e) {            
			button.getParent().requestFocus();
			callback.actionPerformed(e);        
		}    
	}
	
	private void applyTableRendering(){
		
		setDefaultEditor(Float.class, new JTableFloatEditor(0));
	  	setDefaultRenderer(Float.class, new JTableCurrencyRenderer());
	  	
	  	setDefaultEditor(Integer.class, new JTableIntegerEditor(1));
	  	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
	  	dtcr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	  	setDefaultRenderer(Integer.class, dtcr);
		
	  	JComboBox cb = new JComboBox();
		cb.addItem(new Float(0.07));
		cb.addItem(new Float(0.16));
		getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(cb));
		getColumnModel().getColumn(3).setCellRenderer(new JTablePercentRenderer());
		
		if (type == 1){
			getColumnModel().getColumn(7).setCellEditor(new TableButtonCellEditor(this));
			getColumnModel().getColumn(7).setCellRenderer(new TableButtonCellRenderer());
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "deletePosition"){
			int answer = JOptionPane.showConfirmDialog(
						getComponent(0),
						"Soll die Position " + (getSelectedRow() + 1) + " mit: \n"
						+ getValueAt(getSelectedRow(), 1)
						+ "\ngelöscht werden ? ",
						"Warnung",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null);
			if(answer == 0){
				PositionsTableModel model = (PositionsTableModel)getModel();
				model.removePosition(getSelectedRow());
				model.fireTableStructureChanged();
				applyTableRendering();
			}
		} else if (e.getActionCommand() == "addPosition"){
			PositionsTableModel model = (PositionsTableModel)getModel();
			model.addPosition();
			//model.fireTableStructureChanged();
			//applyTableRendering();
		}
	}
	
	public ArrayList getPositions(){
		return null;
	}
	
	public void setPositions(ArrayList positions){
		PositionsTableModel model = new PositionsTableModel(type, positions);
		if (this.tml != null) model.addTableModelListener(tml);
		setModel(model);
		applyTableRendering();
	}
	
	public float getOrderSum(){
		
		PositionsTableModel model = (PositionsTableModel)getModel();
		
		return model.getOrderSum();
	}
	
	public float getOrderDebit(){
		
		PositionsTableModel model = (PositionsTableModel)getModel();
		
		return model.getOrderDebit();
	}
}
