package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import dbObjects.*;

/**
 * @author robert
 */
public class BestellungKleinTable extends JTable {

	/**
	 * Frame mit dem ActionListener.
	 */
	BestellungKlein frame;
	
	/**
	 * Die Liste mit den Firmen f�r die ComboBox.
	 */
	ArrayList firmen;
	
	/**
	 * Die Spalten-Namen
	 */
	private static String[] title = {"Beleg-Nr", "Firma", "Artikel, Gegenstand", "Preis", ""};
	
	/**
	 * Erstellt eine Tabelle mit f�nf Spalten f�r den BestellungKlein-Frame. <br>
	 * Es k�nnen die Belege der Auszahlungsanordnung gespeichert werden.
	 * @param frame = BestellungKlein-Frame als ActionListener
	 * @param firmen = Liste mit Firmen f�r die ComboBox
	 * @author w.flat
	 */
	public BestellungKleinTable(BestellungKlein frame, ArrayList firmen) {
		this.frame = frame;		// ActionListener f�r die L�schen-Buttons
		this.firmen = firmen;	// Firmen f�r die ComboBox
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);		// Keine automatische Gr��en-Anpassung
		
		// TableModel festlegen. Dabei wird bei DefaultTableModel nur die Methoden
		// getColumnClass und isCellEditable �berschrieben
		setModel(new DefaultTableModel(title, 0) {
										public Class getColumnClass(int c) {
											if(c == 0)
												return Integer.class;
											else if(c == 1)
												return JComboBox.class;
											else if(c == 3)
												return Float.class;
											else if(c == 4)
												return JButton.class;
											else
												return JTextField.class;
										}
										
										public boolean isCellEditable(int rowIndex, int columnIndex){
											return columnIndex < 5;
										}
								}
						);
		// CellEditor und CellRenderer f�r Float-Klasse festlegen
		setDefaultEditor(Float.class, new JTableFloatEditor(0));
		setDefaultRenderer(Float.class, new JTableCurrencyRenderer());
		// CellEditor und CellRenderer f�r Integer-Klasse festlegen
		setDefaultEditor(Integer.class, new JTableIntegerEditor(1));
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		setDefaultRenderer(Integer.class, dtcr);
		// Festlegen der Tabellen-Eigenschaften
		updateTableStructure();
	}
	
	/**
	 * Neue Zeile in der Tabelle einf�gen.
	 * @author w.flat
	 */
	public void addRaw() {
		Object[] o = {new Integer(this.getRowCount() + 1), (firmen.get(0)==null ? new Firma():firmen.get(0)), "", new Float(0)};
		((DefaultTableModel)this.getModel()).addRow(o);
		((DefaultTableModel)this.getModel()).fireTableRowsInserted(	((DefaultTableModel)this.getModel()).getRowCount(), 
																	((DefaultTableModel)this.getModel()).getRowCount());
	}
	
	/**
	 * Die aktuelle Spalte l�schen.
	 * @author w.flat
	 */
	public void delPresRaw() {
		((DefaultTableModel)getModel()).removeRow(getSelectedRow());
		setBelegNr();
		((DefaultTableModel)getModel()).fireTableStructureChanged();
		updateTableStructure();
	}
	
	/**
	 * Neue Firmen speichern.
	 * @param firmen = Neue Liste mit Firmen
	 * @author w.flat
	 */
	public void setFirmen(ArrayList firmen) {
		if(firmen == null || this.firmen == firmen)	// Wenn keine Firmen angegeben
			return;									// Gleiche Firmen
		
		this.firmen = firmen;
	}
	
	/**
	 * Beleg-Nr. aktualisieren.
	 * @author w.flat
	 */
	private void setBelegNr() {
		for(int i = 0; i < this.getRowCount(); i++) {
			((DefaultTableModel)this.getModel()).setValueAt(new Integer(i + 1), i, 0);
		}
	}
	
	/**
	 * Die Struktur der Tabelle festlegen.
	 * @author w.flat
	 */
	private void updateTableStructure(){
		// Einstellungen der ersten Spalte = "Beleg-Nr"
		getColumnModel().getColumn(0).setMinWidth(50);
		getColumnModel().getColumn(0).setWidth(60);
		getColumnModel().getColumn(0).setPreferredWidth(60);
		getColumnModel().getColumn(0).setMaxWidth(70);
		// Einstellen der zweiten Spalte = "Firma"
		getColumnModel().getColumn(1).setMinWidth(150);		// Gr��e festlegen
		getColumnModel().getColumn(1).setWidth(150);
		getColumnModel().getColumn(1).setPreferredWidth(150);
		getColumnModel().getColumn(1).setMaxWidth(200);
		JComboBox cb = new JComboBox();		// ComboBox f�r die Firmen
		for(int i = 0; i < firmen.size(); i++) {
			cb.addItem(firmen.get(i));
		}
		getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(cb));		// CellEditor
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();			// CellRenderer
		renderer.setToolTipText("Firmenauswahl");
		getColumnModel().getColumn(1).setCellRenderer(renderer);
		// Einstellen der dritten Spalte = "Artikel"
		getColumnModel().getColumn(2).setMinWidth(190);
		getColumnModel().getColumn(2).setWidth(190);
		getColumnModel().getColumn(2).setPreferredWidth(190);
		getColumnModel().getColumn(2).setMaxWidth(250);
		getColumnModel().getColumn(2).setCellRenderer(new MultiLineRenderer());
		// Einstellen der vierten Spalte = "Preis"
		getColumnModel().getColumn(3).setMinWidth(60);
		getColumnModel().getColumn(3).setWidth(80);
		getColumnModel().getColumn(3).setPreferredWidth(80);
		getColumnModel().getColumn(3).setMaxWidth(100);
		// Einstellen der f�nnften Spalte = "JButton"
		getColumnModel().getColumn(4).setMinWidth(100);
		getColumnModel().getColumn(4).setWidth(100);
		getColumnModel().getColumn(4).setPreferredWidth(100);
		getColumnModel().getColumn(4).setMaxWidth(100);
		getColumnModel().getColumn(4).setCellEditor(new TableButtonCellEditor(frame));
		getColumnModel().getColumn(4).setCellRenderer(new TableButtonCellRenderer());
	}
	
	/**
	 * JButton-Renderer f�r die letzte Spalte.
	 * @author w.flat
	 * 02.03.2005
	 */
	private static class TableButtonCellRenderer implements TableCellRenderer {
		final JButton button;
		
		TableButtonCellRenderer() {
			button = new JButton("L�schen");
		}
		
		public Component getTableCellRendererComponent(	JTable table, Object value, boolean isSelected, 
																boolean hasFocus, int row, int column) {
			return button;
		}
	}
	
	/**
	 * JButton-CellEditor f�r die letzte Spalte.
	 * @author w.flat
	 * 02.03.2005
	 */
	private static class TableButtonCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {        
		final JButton button;
		final ActionListener callback;

		TableButtonCellEditor(ActionListener callback) {            
			button = new JButton("L�schen");
			this.callback = callback;  
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
}
