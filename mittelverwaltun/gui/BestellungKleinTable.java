package gui;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import dbObjects.Position;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class BestellungKleinTable extends JTable {
	ActionListener al;
	private static String[] title = {"Beleg-Nr","Firma","Artikel, Gegenstand","Preis",""};
	private static String[] editTitle = {"Beleg-Nr","Firma","Artikel, Gegenstand","Preis","","","Id"};
  private Object[][] data = {{new Integer(1),"","", new Float(0), new String("Löschen")}};
	
	public BestellungKleinTable(){
		setModel(new DefaultTableModel(data, title){
										public Class getColumnClass(int c) {
											 return getValueAt(0, c).getClass();
										}
							
										public boolean isCellEditable(int rowIndex, int columnIndex){
										  return columnIndex < 5;
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
								if(e.getActionCommand() == "Löschen"){
									((DefaultTableModel)getModel()).removeRow(getSelectedRow());
									((DefaultTableModel)getModel()).fireTableStructureChanged();
									updateTableStructur2();
								}
		           
							}        
						};
		updateTableStructur2();
	}
		
	public BestellungKleinTable(Position[] positions){
		Object[][] data = new Object[positions.length][5];
		
//		for(int i = 0; i < positions.length; i++){
//			data[i][0] = new Integer(positions[i].getMenge());
//			data[i][1] = positions[i].getBeschreibung();
//			data[i][2] = new Float(positions[i].getEinzelPreis());
//			data[i][3] = new Float(positions[i].getMwst());
//			data[i][4] = new Float((positions[i].getMenge() * positions[i].getEinzelPreis()));
//			data[i][5] = new String("Speichern");
//			data[i][6] = new String("Löschen");
//			data[i][7] = new Integer(positions[i].getId());
//		}

		al = new ActionListener() {            
					public void actionPerformed(ActionEvent e) { 
						if(e.getActionCommand() == "Speichern"){
							int row = getSelectedRow();
							Position position = new Position(((Integer)getValueAt(row, 7)).intValue(), 
															getValueAt(row, 1).toString(), 
															((Float)getValueAt(row, 2)).floatValue(),
															((Integer)getValueAt(row, 0)).intValue(), 
															((Float)getValueAt(row, 3)).floatValue(),
															((Float)getValueAt(row, 4)).floatValue()
															);
							//useCase.updatePosition(position);
							System.out.println(position);
						}else if(e.getActionCommand() == "Löschen"){
							int answer = JOptionPane.showConfirmDialog(
										getComponent(0),
										"Soll die Position " + getSelectedRow() + " mit: \n"
										+ getValueAt(getSelectedRow(), 1)
										+ "\ngelöscht werden ? ",
										"Warnung",
										JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE,
										null);
							if(answer == 0){
								//useCase.deletePosition(position);
								((DefaultTableModel)getModel()).removeRow(getSelectedRow());
								((DefaultTableModel)getModel()).fireTableStructureChanged();
								updateTableStructur();
							}
						}
		           
					}        
				}; 
							
		setModel(new DefaultTableModel(data, title){
										public Class getColumnClass(int c) {
											 return getValueAt(0, c).getClass();
										}
							
										public boolean isCellEditable(int rowIndex, int columnIndex){
										  return columnIndex < 5;
										}
								}
						);
		setDefaultEditor(Float.class, new JTableFloatEditor(0));
	  	setDefaultRenderer(Float.class, new JTableCurrencyRenderer());
	  	setDefaultEditor(Integer.class, new JTableIntegerEditor(1));
	  	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
	  	dtcr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	  	setDefaultRenderer(Integer.class, dtcr);
	  	updateTableStructur();
	  	
	}
	
	private void updateTableStructur2(){
		getColumnModel().getColumn(4).setCellEditor(new TableButtonCellEditor("Löschen", al));
		getColumnModel().getColumn(4).setCellRenderer(new TableButtonCellRenderer("Löschen"));
		getColumnModel().getColumn(0).setMaxWidth(50);
		getColumnModel().getColumn(2).setCellRenderer(new MultiLineRenderer());
		getColumnModel().getColumn(3).setMaxWidth(50);
		getColumnModel().getColumn(4).setMaxWidth(150);
		getColumnModel().getColumn(4).setMaxWidth(70);
		getColumnModel().getColumn(4).setMinWidth(70);
	}
	
	private void updateTableStructur(){
		getColumnModel().getColumn(5).setCellEditor(new TableButtonCellEditor("Speichern", al));
		getColumnModel().getColumn(5).setCellRenderer(new TableButtonCellRenderer("Speichern"));
		getColumnModel().getColumn(6).setCellEditor(new TableButtonCellEditor("Löschen", al));
		getColumnModel().getColumn(6).setCellRenderer(new TableButtonCellRenderer("Löschen"));
		getColumnModel().getColumn(0).setMaxWidth(50);
		getColumnModel().getColumn(2).setMaxWidth(100);
		getColumnModel().getColumn(3).setMaxWidth(50);
		getColumnModel().getColumn(4).setMaxWidth(150);
		getColumnModel().getColumn(5).setMaxWidth(70);
		getColumnModel().getColumn(5).setMinWidth(70);
		getColumnModel().getColumn(6).setMaxWidth(60);
		getColumnModel().getColumn(6).setMinWidth(60);
		getColumnModel().getColumn(7).setMinWidth(0);
		getColumnModel().getColumn(7).setMaxWidth(0);
		getColumnModel().getColumn(7).setWidth(0);
	}
	
	
	private static class TableButtonCellRenderer implements TableCellRenderer {        
		final JButton button;        
		TableButtonCellRenderer(String buttonLabel) {            
			button = new JButton(buttonLabel);  
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
