/*
 * Created on 01.03.2005
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
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * @author Mario
 *
 * TODO To change the template for this generated type comment go to
 * Window 
 * Preferences - Java - Code Style - Code Templates
 */
public class OrderTable extends JTable{
	
	static final int STD_TYP = 0;
	static final int ASK_TYP = 1;
	static final int ZA_TYP = 2;
	static final int SONDIERUNG = 0;
	static final int ABWICKLUNG = 1;
	static final int ABGESCHLOSSEN = 2;
	
	private ActionListener actionListener;

	public OrderTable(ActionListener listener, ArrayList orders){
		this.actionListener = listener;
		setOrders(orders);
	}

	private static class TableButtonCellRenderer implements TableCellRenderer {        
		final JButton button;        
		
		TableButtonCellRenderer() {            
			button = new JButton(Functions.getOpenIcon(this.getClass()));
			button.setActionCommand("showOrder");
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
			button = new JButton(Functions.getOpenIcon(this.getClass()));
			button.setActionCommand("showOrder");           
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
	
	public void setOrders (ArrayList orders){
		setModel(new OrderTableModel(orders));
		applyTableRendering();
	}
	
	public int getSelectedOrderID(){
		return ((OrderTableModel)getModel()).getId(getSelectedRow());
	}
	
	public int getSelectedOrderType(){
		return ((OrderTableModel)getModel()).getType(getSelectedRow());
	}
	
	public int getSelectedOrderPhase(){
		return ((OrderTableModel)getModel()).getPhase(getSelectedRow());
	}
	
	private void applyTableRendering(){
		setRowHeight(20);
		
		setDefaultEditor(Float.class, new JTableFloatEditor(0));
		JTableCurrencyRenderer jtcr = new JTableCurrencyRenderer();
		jtcr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		setDefaultRenderer(Float.class, jtcr);
		
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		
		getColumnModel().getColumn(0).setCellRenderer(dtcr);
		getColumnModel().getColumn(0).setPreferredWidth(70);
		getColumnModel().getColumn(1).setPreferredWidth(120);
		getColumnModel().getColumn(1).setCellRenderer(dtcr);
		getColumnModel().getColumn(2).setPreferredWidth(90);
		getColumnModel().getColumn(2).setCellRenderer(dtcr);
		getColumnModel().getColumn(3).setPreferredWidth(120);
		getColumnModel().getColumn(4).setPreferredWidth(120);
		getColumnModel().getColumn(5).setPreferredWidth(120);
		getColumnModel().getColumn(6).setPreferredWidth(100);
		getColumnModel().getColumn(7).setPreferredWidth(20);
		getColumnModel().getColumn(7).setCellEditor(new TableButtonCellEditor(actionListener));
		getColumnModel().getColumn(7).setCellRenderer(new TableButtonCellRenderer());
	}
}