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
 * 
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class ReportsTable extends JTable{
	
	/**
	 * Ein Report, der die Konten der Zentralverwaltung und die zugewiesenen Mittel, die Summe
	 * der Ausgaben und aktuellen Kontostände enthält
	 */
	public static final int REPORT_1 = 1;
	
	/**
	 * Ein Report, nder die Konten der Zentralverwaltung und die zugewiesenen Mittel , die Summe 
	 * der Ausgaben und Verteilungen, so dass der Dekan feststellen kann, wie viel Mittel kann er noch verteilen
	 */
	public static final int REPORT_2 = 2;
	
	/**
	 * Ein Report, der die institutsinternen Konten, die verteilten Mittel, die Summe 
	 * der Ausgaben und den aktuellen Kontostand enthält
	 */
	public static final int REPORT_3 = 3;
	
	/**
	 * Ein Report, der die Ausgaben bei den institutsinternen Konten nach Verwaltungskonten sortiert
	 */
	public static final int REPORT_4 = 4;
	
	/**
	 * Ein Report von jedem Konto aus der Zentralverwaltung mit Ausgaben jedes Instituts
	 * und aktueller Kontostände
	 */
	public static final int REPORT_5 = 5;
	
	/**
	 * Für jedes Institut ein Report mit Ausgaben sortiert nach Verwaltungskonen
	 */
	public static final int REPORT_6 = 6;
	
	/**
	 * Für jedes Instiut ein detaillierter Report über die Einnahmen und Ausgaben mit FBI-Schlüsselnummer,
	 * Hüll-Nr, Datum, welches Konto der Zentralverwaltung belastet wurde, Firma, Beschreibung des bestellten Artikels, 
	 * Besteller, Nutzer, Festlegung bzw. Anordnung, Status der Bestellung
	 */
	public static final int REPORT_7 = 7;
	
	/**
	 * Für jedes Instiut ein Report über die Einnahmen
	 */
	public static final int REPORT_8 = 8;
	
	
	private int type = 0;
	private ActionListener actionListener;
	
	/**
	 * Konstruktor für ein Report
	 * @param type - Typ des Reports (REPORT_1, .., REPORT_7)
	 * @param report - ArrayList mit den 
	 */
	public ReportsTable(int type, ArrayList content, ActionListener listener){
		this.actionListener = listener;
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		applyTableRendering();
	}
	
	public ReportsTable(){
	}
	
	private void applyTableRendering(){
		setRowHeight(20);
		
		setDefaultEditor(Float.class, new JTableFloatEditor(0));
  	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
  	dtcr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
  	setDefaultRenderer(Integer.class, dtcr);
		
		if(type == REPORT_1){
			getColumnModel().getColumn(0).setPreferredWidth(45);
		}else if(type == REPORT_2){
			
		}else if(type == REPORT_3){
			
		}else if(type == REPORT_4){
			
		}else if(type == REPORT_5){
			
		}else if(type == REPORT_6){
			
		}else if(type == REPORT_7){
			
		}
	}
	
	/**
	 * füllt die Tabelle mit dem Inhalt der ArrayListe
	 * @param content - ArrayListe mit dem Inhalt für den Report
	 */
	public void fillReport(ArrayList content){
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
	
}
