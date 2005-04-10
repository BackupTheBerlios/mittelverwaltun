package gui;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;


import dbObjects.Angebot;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class BestellungBeilageTable extends JTable {
	ActionListener al;
	private static String[] title = {"Nr","Firma, Ort","Angebot vom","Auftt.-Summe", "Auswahl", "",""};
  private Object[][] data = {{new Integer(1),"","", new Float(0),new Boolean(false), new Boolean(false), new String(""), new String("Show")}};
  private ArrayList angebote = new ArrayList();
  BestellungNormal frame = null;
	
	public BestellungBeilageTable(BestellungNormal frame){
		this.frame = frame;
		
		setModel(new DefaultTableModel(title,0){
										public Class getColumnClass(int c) {
											 return getValueAt(0, c).getClass();
										}
							
										public boolean isCellEditable(int rowIndex, int columnIndex){
										  return columnIndex > 3;
										}
										public void setValueAt(Object aValue, int rowIndex, int columnIndex){
										  if(columnIndex  == 4){
												getFrame().buBestellen.setEnabled(false);
												for(int i = 0; i < getRowCount(); i++){
													super.setValueAt(new Integer(i+1), i, 0);
													super.setValueAt(new Boolean(false), i, 4);
													((Angebot)super.getValueAt(i, 1)).setAngenommen(false);
												}
												
												if(((Boolean)aValue).booleanValue() == true){
													getFrame().angebotNr = (rowIndex + 1);
													getFrame().buBestellen.setEnabled(true);
												}else{
													getFrame().angebotNr = 0;
												}
												((Angebot)super.getValueAt(rowIndex, 1)).setAngenommen(((Boolean)aValue).booleanValue());
										  }
										  super.setValueAt(aValue,rowIndex,columnIndex);
										  fireTableRowsUpdated(rowIndex,rowIndex);
										}
								}
						);
		setDefaultEditor(Float.class, new JTableFloatEditor(0));
		setDefaultRenderer(Float.class, new JTableCurrencyRenderer());
		setDefaultEditor(Integer.class, new JTableIntegerEditor(1));
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		setDefaultRenderer(Integer.class, dtcr);
		al = new ActionListener() {            
							public void actionPerformed(ActionEvent e) { 
								if(e.getActionCommand() == "Del"){
									int answer = JOptionPane.showConfirmDialog(
																			getComponent(0),
																			"Soll das Angebot " + (getSelectedRow() + 1) + " : \n"
																			+ getValueAt(getSelectedRow(), 1)
																			+ "\ngelöscht werden ? ",
																			"Warnung",
																			JOptionPane.YES_NO_OPTION,
																			JOptionPane.QUESTION_MESSAGE,
																			null);
									if(answer == 0){
										((DefaultTableModel)getModel()).removeRow(getSelectedRow());
										for(int i = 0; i < getRowCount(); i++){
											setValueAt(new Integer(i+1), i, 0);
											setValueAt(new Boolean(false), i, 4);
										}
										((DefaultTableModel)getModel()).fireTableStructureChanged();
										updateTableStructur2();
									}
								}else if(e.getActionCommand() == "Show"){
									DefaultTableModel dtm = (DefaultTableModel)getModel();
									AngebotFrame angebot = new AngebotFrame(getFrame(), (Angebot)getValueAt(getSelectedRow(), 1), getSelectedRow());
									angebot.setVisible(true);
								}
							}        
						};
		updateTableStructur2();
	}
	
	private BestellungNormal getFrame(){
		return frame;
	}

	
	private void updateTableStructur2(){
		getColumnModel().getColumn(0).setMaxWidth(40);
		getColumnModel().getColumn(2).setMinWidth(80);
		getColumnModel().getColumn(2).setMaxWidth(80);
		
		getColumnModel().getColumn(3).setMinWidth(80);
		getColumnModel().getColumn(3).setMaxWidth(80);
		
		
		getColumnModel().getColumn(5).setCellEditor(new TableButtonCellEditor("Del", "Del", Functions.getDelIcon(this.getClass()), al));
		getColumnModel().getColumn(5).setCellRenderer(new TableButtonCellRenderer("Del", "Del", Functions.getDelIcon(this.getClass())));
		getColumnModel().getColumn(5).setMaxWidth(35);
		getColumnModel().getColumn(5).setMinWidth(35);
		
		getColumnModel().getColumn(6).setCellEditor(new TableButtonCellEditor("Show", al));
		getColumnModel().getColumn(6).setCellRenderer(new TableButtonCellRenderer("Show"));
		getColumnModel().getColumn(6).setMaxWidth(50);
		getColumnModel().getColumn(6).setMinWidth(50);
	}
	
	
	private static class TableButtonCellRenderer implements TableCellRenderer {        
		final JButton button;        
		
		TableButtonCellRenderer(String buttonLabel) {            
			button = new JButton(buttonLabel);  
			button.setMargin(new Insets(2,2,2,2));         
		}
		 
		TableButtonCellRenderer(String command, String toolTip, ImageIcon icon) {            
			button = new JButton(icon);
			button.setActionCommand(command);
			button.setToolTipText(toolTip);  
			button.setMargin(new Insets(2,2,2,2));         
		}        
	
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {            
			return button;        
		}    
	}    
				
	private static class TableButtonCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {        
		final JButton button;        
		final ActionListener callback;
		        
		TableButtonCellEditor(String buttonLabel, ActionListener callback) {            
			button = new JButton(buttonLabel);            
			this.callback = callback;  
			button.setMargin(new Insets(2,2,2,2));         
			button.addActionListener(this);        
		}     
		
		TableButtonCellEditor(String command, String toolTip, ImageIcon icon, ActionListener callback) {            
			button = new JButton(icon);            
			this.callback = callback; 
			button.setActionCommand(command);
			button.setToolTipText(toolTip); 
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
}
