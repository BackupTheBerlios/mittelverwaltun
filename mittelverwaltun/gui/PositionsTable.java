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
import javax.swing.ListSelectionModel;

import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import dbObjects.Institut;

/**
 * @author Mario
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PositionsTable extends JTable implements ActionListener {
	
	public static final int ANZEIGE = 0;
	public static final int STD_ABWICKLUNG = 1;
	public static final int ASK_ABWICKLUNG = 2;
	public static final int ASK_DURCHFUEHRUNG = 3;
	public static final int STD_DURCHFUEHRUNG = 4;
	
	private boolean editable = true;
	private int type = 0;
	private TableModelListener tml = null;
	private Institut[] institutes = null;
	
	public PositionsTable(int type, boolean editable, TableModelListener tml, ArrayList positions){
		this.editable = editable;
		this.type = type;
		this.tml = tml;
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		PositionsTableModel model = new PositionsTableModel(type, editable, positions);
		model.addTableModelListener(tml);
		setModel(model);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		applyTableRendering();
	}

	public PositionsTable(int type, boolean editable, TableModelListener tml, ArrayList positions, Institut[] institutes){
		this.editable = editable;
		this.type = type;
		this.tml = tml;
		this.institutes = institutes;
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		PositionsTableModel model = new PositionsTableModel(type, editable, positions);
		model.addTableModelListener(tml);
		setModel(model);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		applyTableRendering();
	}
	
	public PositionsTable(int type, boolean editable, ArrayList positions){
		this.editable = editable;
		this.type = type;
		PositionsTableModel model = new PositionsTableModel(type, editable, positions);
		setModel(model);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		applyTableRendering();
	}
	
	private static class TableButtonCellRenderer implements TableCellRenderer {        
		final JButton button;        
		TableButtonCellRenderer() {            
			button = new JButton(Functions.getRowDeleteIcon(this.getClass()));
			button.setActionCommand("deletePosition");
			button.setToolTipText("Position löschen");
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
			button.setActionCommand("deletePosition"); 
			button.setToolTipText("Position löschen");
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
		
			setRowHeight(20);
		
			setDefaultEditor(Float.class, new JTableFloatEditor(0));
	  	setDefaultRenderer(Float.class, new JTableCurrencyRenderer());
	  	
	  	setDefaultEditor(Integer.class, new JTableIntegerEditor(1));
	  	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
	  	dtcr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	  	setDefaultRenderer(Integer.class, dtcr);
		
	  	getColumnModel().getColumn(0).setPreferredWidth(45);	// Menge
	  	getColumnModel().getColumn(1).setPreferredWidth(127);	// Artikelbezeichnung	
	  	
	  	if (type == PositionsTable.ASK_ABWICKLUNG || type == PositionsTable.ASK_DURCHFUEHRUNG){
	 		
	  		getColumnModel().getColumn(2).setPreferredWidth(130);	// Institut
	  		JComboBox cbInstitutes = new JComboBox();
	  		if (institutes != null){
	  			for(int i=0; i<institutes.length;i++){
	  				//System.out.println(institutes[i]);
	  				cbInstitutes.addItem(institutes[i]);
	  			}
	  				
	  		}
	  		getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(cbInstitutes));
	  		getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer());
	  		
	  		getColumnModel().getColumn(3).setPreferredWidth(90);	// Einzelpreis	
		  	
		  	getColumnModel().getColumn(4).setPreferredWidth(40);	// Mehrwertsteuer
		  	JComboBox mwst = new JComboBox();
				mwst.addItem(new Float(0.07));
				mwst.addItem(new Float(0.16));
				getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(mwst));
				getColumnModel().getColumn(4).setCellRenderer(new JTablePercentRenderer());
				
				getColumnModel().getColumn(5).setPreferredWidth(90);	// Gesamt	  		
	  	
	  	}else{
	  		getColumnModel().getColumn(2).setPreferredWidth(100);	// Einzelpreis	
		  	
		  	getColumnModel().getColumn(3).setPreferredWidth(45);	// Mehrwertsteuer
		  	JComboBox mwst = new JComboBox();
				mwst.addItem(new Float(0.07));
				mwst.addItem(new Float(0.16));
				getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(mwst));
				getColumnModel().getColumn(3).setCellRenderer(new JTablePercentRenderer());
				
				getColumnModel().getColumn(4).setPreferredWidth(100);	// Rabatt
				getColumnModel().getColumn(5).setPreferredWidth(100);	// Gesamt
	  	}
	  	
		if(type == PositionsTable.ASK_DURCHFUEHRUNG || type == PositionsTable.STD_DURCHFUEHRUNG){ 
			getColumnModel().getColumn(6).setPreferredWidth(25);
			getColumnModel().getColumn(6).setCellEditor(new TableButtonCellEditor(this));
			getColumnModel().getColumn(6).setCellRenderer(new TableButtonCellRenderer());
		}else if(type != PositionsTable.ANZEIGE){
			getColumnModel().getColumn(6).setPreferredWidth(70);
			getColumnModel().getColumn(7).setPreferredWidth(25);
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
	
	public void setPositions(ArrayList positions){
		PositionsTableModel model = new PositionsTableModel(type, editable, positions);
		if (this.tml != null) model.addTableModelListener(tml);
		setModel(model);
		applyTableRendering();
	}
	
	public float getOrderSum(){
		
		PositionsTableModel model = (PositionsTableModel)getModel();
		
		return model.getOrderSum();
	}
	
	public float get7PercentSum(){
		PositionsTableModel model = (PositionsTableModel)getModel();
		
		return model.get7PercentSum();
	}
	
	public float get16PercentSum(){
		PositionsTableModel model = (PositionsTableModel)getModel();
	
		return model.get16PercentSum();
	}
	
	public float getOrderDebit(){
		
		PositionsTableModel model = (PositionsTableModel)getModel();
		
		return model.getOrderDebit();
	}
	
	public ArrayList getOrderPositions(){
		PositionsTableModel model = (PositionsTableModel)getModel();
		return model.getOrderPositions();
	}
	
	public void setEditable(boolean editable){
		((PositionsTableModel)getModel()).setEditable(editable);
	}
	
	public void payAllPositions(){
		((PositionsTableModel)getModel()).payAllPositions();
	}
	
	public void oweAllPositions(){
		((PositionsTableModel)getModel()).oweAllPositions();
	}
	
	public boolean isEditable(){
		return ((PositionsTableModel)getModel()).isEditable();
	}
}
