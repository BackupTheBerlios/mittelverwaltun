/*
 * Created on 14.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * @author Mario
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BudgetYearTable extends JTable {
	private ActionListener actionListener;

	public BudgetYearTable(ActionListener listener, ArrayList years){
		this.actionListener = listener;
		setYears(years);
	}

	public void setYears (ArrayList years){
		setModel(new BudgetYearTableModel(years));
		applyTableRendering();
	}
	
	public int getSelectedYearID(){
		return ((BudgetYearTableModel)getModel()).getId(getSelectedRow());
	}
	
	public int getSelectedYearStatus(){
		return ((BudgetYearTableModel)getModel()).getStatus(getSelectedRow());
	}
	
	public int getYearStatus(int row){
		return ((BudgetYearTableModel)getModel()).getStatus(row);
	}
	
	private void applyTableRendering(){
		setRowHeight(20);
		
		setFont(new java.awt.Font("Dialog", 0, 11));
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		TextCellRenderer tcr = new TextCellRenderer();
		
		getColumnModel().getColumn(0).setPreferredWidth(97);
		getColumnModel().getColumn(0).setCellRenderer(tcr);
		getColumnModel().getColumn(1).setCellRenderer(tcr);
		getColumnModel().getColumn(1).setPreferredWidth(97);
		getColumnModel().getColumn(2).setCellRenderer(tcr);
		getColumnModel().getColumn(2).setPreferredWidth(120);
		getColumnModel().getColumn(3).setPreferredWidth(100);
		getColumnModel().getColumn(3).setCellEditor(new ActionCellEditor(actionListener));
		getColumnModel().getColumn(3).setCellRenderer(new ActionCellRenderer());
	}

	private static class TextCellRenderer implements TableCellRenderer {        
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {            
			if (column < 3){
				
				JLabel label = new JLabel((String)value);
				label.setOpaque(true);
				label.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
				label.setHorizontalAlignment(SwingConstants.CENTER);
				
				if (row == table.getSelectedRow())
					label.setBackground(table.getSelectionBackground());
				else
					label.setBackground(table.getBackground());
				
				if (((BudgetYearTable)table).getYearStatus(row)==0){
					label.setFont(new java.awt.Font("Dialog", 1, 11));
				}else{
					label.setFont(new java.awt.Font("Dialog", 0, 11));
				}
				
				return label;
				
			}else return null;
		}    
	}    
		
	private static class ActionCellRenderer implements TableCellRenderer {        
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {            
			if (column == 3){
				if (((BudgetYearTable)table).getYearStatus(row)<2) {
					JButton button = new JButton(Functions.getKeyIcon(this.getClass()));
					button.setText("Abschließen");
					button.setFont(new java.awt.Font("Dialog", 0, 10));
					button.setActionCommand("finishYear");
					button.setToolTipText("Haushaltsjahr abschließen");
					button.setMargin(new Insets(1,1,1,1));
					//button.addActionListener(this);
					return button;
				}else{	
					JLabel label = new JLabel("");
					label.setOpaque(true);
					label.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
					
					if (row == table.getSelectedRow())
						label.setBackground(table.getSelectionBackground());
					else
					label.setBackground(new Color(210,225,245));
					
					return label;
				}
			}else return null;
		}    
	}    
				
	private static class ActionCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {        
		
		final ActionListener callback;
		        
		ActionCellEditor(ActionListener callback) {            
			this.callback = callback;
		} 
		
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {            
			if (column == 3){
				if (((BudgetYearTable)table).getYearStatus(row)<2) {
					JButton button = new JButton(Functions.getKeyIcon(this.getClass()));
					button.setText("Abschließen");
					button.setActionCommand("finishYear");
					button.setToolTipText("Haushaltsjahr abschließen");
					button.setFont(new java.awt.Font("Dialog", 0, 10));
					button.setMargin(new Insets(1,1,1,1));
					button.addActionListener(this);
					return button;
				}else{	
					JLabel label = new JLabel("");
					label.setOpaque(true);
					label.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
					if (row == table.getSelectedRow())
						label.setBackground(table.getSelectionBackground());
					else
					label.setBackground(new Color(210,225,245));
					return label;
				}
			}else return null;
		}            
		       
		
		public Object getCellEditorValue() {            
			return null;        
		}        
		
		public void actionPerformed(ActionEvent e) {            
			((JButton)e.getSource()).getParent().requestFocus();
			callback.actionPerformed(e);        
		}    
	}
}
