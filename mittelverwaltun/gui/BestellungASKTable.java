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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import dbObjects.Institut;
import dbObjects.Position;

/**
 * @author robert
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
public class BestellungASKTable extends JTable {
	ActionListener al;
	private static String[] title = {"Menge","Produkt","Einzelpreis","Gesamtpreis","Instiut",""};
	private static String[] editTitle = {"Menge","Produkt","Einzelpreis","Gesamtpreis","Instiut","","","Id"};
  private Object[][] data = {{new Integer(1),"",new Float(0), new Float(0), "", new String("Del")}};
  private Institut[] institute;
	
	public BestellungASKTable(Institut[] institute){
		this.institute = institute;
		
		setModel(new DefaultTableModel(title, 0){
										public Class getColumnClass(int c) {
											 return getValueAt(0, c).getClass();
										}
							
										public boolean isCellEditable(int rowIndex, int columnIndex){
										  return true;
										}
										
										public void setValueAt(Object aValue, int rowIndex, int columnIndex)
											{
											  super.setValueAt(aValue,rowIndex,columnIndex);
											  if(columnIndex  < getColumnCount() -1){
												 try{
													int menge = ((Integer)getValueAt(rowIndex,0)).intValue();
													float price = ((Float)getValueAt(rowIndex, 2)).floatValue();
												 super.setValueAt(new Float(menge * price), rowIndex, 3);
												 }catch(Exception e){
												 super.setValueAt(new Integer(0), rowIndex, 3);
												 }
											  }
											  fireTableRowsUpdated(rowIndex,rowIndex);
											}
								}
						);
		setDefaultEditor(Float.class, new JTableFloatEditor(0));
		setDefaultRenderer(Float.class, new JTableCurrencyRenderer());
		setDefaultEditor(Integer.class, new JTableIntegerEditor(1));
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		
		setUpInstitutColumn(getColumnModel().getColumn(4));
		
		setDefaultRenderer(Integer.class, dtcr);
		al = new ActionListener() {            
							public void actionPerformed(ActionEvent e) { 
								if(e.getActionCommand() == "Del"){
									int answer = JOptionPane.showConfirmDialog(
																												getComponent(0),
																												"Soll die Position " + (getSelectedRow() + 1) + " : \n"
																												+ getValueAt(getSelectedRow(), 1)
																												+ "\ngelöscht werden ? ",
																												"Warnung",
																												JOptionPane.YES_NO_OPTION,
																												JOptionPane.QUESTION_MESSAGE,
																												null);
									if(answer == 0){
										((DefaultTableModel)getModel()).removeRow(getSelectedRow());
										((DefaultTableModel)getModel()).fireTableStructureChanged();
										updateTableStructur2();
									}
								}
							}        
						};
		updateTableStructur2();
	}
		
	public BestellungASKTable(ArrayList positions){
		Object[][] data = new Object[positions.size()][8];
		
		for(int i = 0; i < positions.size(); i++){
			Position position = (Position)positions.get(i);
			data[i][0] = new Integer(position.getMenge());
			data[i][1] = position.getBeschreibung();
			data[i][2] = new Float(position.getEinzelPreis());
			data[i][3] = new Float((position.getMenge() * position.getEinzelPreis()));
			data[i][4] = new String("");
			data[i][5] = new String("Save");
			data[i][6] = new String("Del");
			data[i][7] = new Integer(position.getId());
		}

		al = new ActionListener() {            
					public void actionPerformed(ActionEvent e) { 
						if(e.getActionCommand() == "Save"){
							int row = getSelectedRow();
							Position position = new Position(((Integer)getValueAt(row, 7)).intValue(), 
															getValueAt(row, 1).toString(), 
															((Float)getValueAt(row, 2)).floatValue(),
															((Integer)getValueAt(row, 0)).intValue(), 
															((Float)getValueAt(row, 3)).floatValue(),
															((Float)getValueAt(row, 4)).floatValue()
															);
							//useCase.updatePosition(position);
						}else if(e.getActionCommand() == "Del"){
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
							
		setModel(new DefaultTableModel(data, editTitle){
									public Class getColumnClass(int c) {
										 return getValueAt(0, c).getClass();
									}
												
									public boolean isCellEditable(int rowIndex, int columnIndex){
									  return columnIndex < 7;
									}
															
									public void setValueAt(Object aValue, int rowIndex, int columnIndex)
										{
										  super.setValueAt(aValue,rowIndex,columnIndex);
										  if(columnIndex  < getColumnCount() -3){
											 try{
												int menge = ((Integer)getValueAt(rowIndex,0)).intValue();
												float price = ((Float)getValueAt(rowIndex, 2)).floatValue();
												float rabat = ((Float)getValueAt(rowIndex, 3)).floatValue();
											 super.setValueAt(new Float(menge * price - rabat), rowIndex, 4);
											 }catch(Exception e){
											 super.setValueAt(new Integer(0), rowIndex, 4);
											 }
										  }
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
  	updateTableStructur();
	  	
	}
	
	public void setUpInstitutColumn(TableColumn column) {
		JComboBox comboBox = new JComboBox();
		
		for(int i = 0; i < institute.length; i++){
			comboBox.addItem(institute[i]);
		}
		
		column.setCellEditor(new DefaultCellEditor(comboBox));

		//Set up tool tips for the institut cells.
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setToolTipText("Wählen Sie ein Institut aus");
		column.setCellRenderer(renderer);
	}
	
	private void updateTableStructur2(){
		getColumnModel().getColumn(5).setCellEditor(new TableButtonCellEditor("Del", al));
		getColumnModel().getColumn(5).setCellRenderer(new TableButtonCellRenderer("Del"));
		getColumnModel().getColumn(5).setMaxWidth(50);
		getColumnModel().getColumn(5).setMinWidth(50);
		
		getColumnModel().getColumn(0).setMaxWidth(50);
		getColumnModel().getColumn(3).setMaxWidth(80);
//		getColumnModel().getColumn(4).setMaxWidth(120);
		
		setUpInstitutColumn(getColumnModel().getColumn(4));
	}
	
	private void updateTableStructur(){
		getColumnModel().getColumn(5).setCellEditor(new TableButtonCellEditor("Save", al));
		getColumnModel().getColumn(5).setCellRenderer(new TableButtonCellRenderer("Save"));
		getColumnModel().getColumn(6).setCellEditor(new TableButtonCellEditor("Del", al));
		getColumnModel().getColumn(6).setCellRenderer(new TableButtonCellRenderer("Del"));
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
