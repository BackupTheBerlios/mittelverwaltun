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
import javax.swing.JButton;
import javax.swing.ListSelectionModel;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * <p>Table f�r die Klasse Reports zum Anzeigen der Auswertungen<p>
 * 
 * @author robert
 */
public class ReportsTable extends JTable{
		
	private int type = 0;
	private ActionListener actionListener;
	
	/**
	 * Konstruktor f�r ein Report
	 * @param type - Typ des Reports (REPORT_1, .., REPORT_7)
	 * @param report - ArrayList mit den 
	 */
	public ReportsTable(ActionListener listener){
		this.actionListener = listener;
		
		//setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	private void applyTableRendering(){
		setRowHeight(20);
		
		setDefaultEditor(Float.class, new JTableFloatEditor(0));
  	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
  	dtcr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
  	setDefaultRenderer(Integer.class, dtcr);
		
		if(type == Reports.REPORT_1){
			getColumnModel().getColumn(0).setPreferredWidth(45);
		}else if(type == Reports.REPORT_2){
			
		}else if(type == Reports.REPORT_3){
			
		}else if(type == Reports.REPORT_4){
			
		}else if(type == Reports.REPORT_5){
			
		}else if(type == Reports.REPORT_6){
			
		}else if(type == Reports.REPORT_7){
			getColumnModel().getColumn(7).setPreferredWidth(20);
			getColumnModel().getColumn(7).setMaxWidth(20);
			getColumnModel().getColumn(7).setCellEditor(new TableButtonCellEditor(actionListener));
			getColumnModel().getColumn(7).setCellRenderer(new TableButtonCellRenderer());
			
		}else if(type == Reports.REPORT_8){
			
		}
		
		
	}
	
	/**
	 * f�llt die Tabelle mit dem Inhalt der ArrayListe
	 * @param content - ArrayListe mit dem Inhalt f�r den Report
	 */
	public void fillReport(int type, ArrayList content){
		this.type = type;
		ReportsTableModel model = new ReportsTableModel(type, content);
		setModel(model);
		applyTableRendering();
	}
	
	private static class TableButtonCellRenderer implements TableCellRenderer {        
		final JButton button;        
		TableButtonCellRenderer() {            
			button = new JButton(Functions.getRowDeleteIcon(this.getClass()));
			button.setActionCommand("showOrder");
			button.setToolTipText("Bestellung anzeigen");
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
			button = new JButton(Functions.getRowDeleteIcon(this.getClass()));
			button.setActionCommand("showOrder"); 
			button.setToolTipText("Bestellung anzeigen");
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
	
	public int getSelectedOrderID(){
		return ((ReportsTableModel)getModel()).getId(getSelectedRow());
	}
	
	public int getSelectedOrderType(){
		return ((ReportsTableModel)getModel()).getType(getSelectedRow());
	}

	public int getSelectedOrderPhase(){
		return ((ReportsTableModel)getModel()).getPhase(getSelectedRow());
	}
	
	public void setReport(int type, ArrayList orders){
		setModel(new ReportsTableModel(type, orders));
		applyTableRendering();
	}
	
}
