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
	private static String[] editTitle = {"Nr","Firma, Ort","Angebot vom","Auftt.-Summe", "Auswahl", "","","Id"};
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
												for(int i = 0; i < getRowCount(); i++){
													super.setValueAt(new Integer(i+1), i, 0);
													super.setValueAt(new Boolean(false), i, 4);
													((Angebot)super.getValueAt(i, 1)).setAngenommen(false);
												}
												getFrame().tfAngebotNr.setText(((Boolean)aValue).booleanValue() == true ? "" + (rowIndex + 1) : "");
												getFrame().angebotNr = ((Boolean)aValue).booleanValue() == true ? (rowIndex + 1) : 0;
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
									angebot.show();
								}
							}        
						};
		updateTableStructur2();
	}
	
	private BestellungNormal getFrame(){
		return frame;
	}
		
//	public BestellungBeilageTable(BestellungNormal frame, Position[] positions){
//		this.frame = frame; 
//		
//		Object[][] data = new Object[positions.length][5];
//		
//		for(int i = 0; i < positions.length; i++){
//			data[i][0] = new Integer(positions[i].getMenge());
//			data[i][1] = positions[i].getArtikel();
//			data[i][2] = new Float(positions[i].getEinzelPreis());
//			data[i][3] = new Float(positions[i].getMwst());
//			data[i][4] = new Float((positions[i].getMenge() * positions[i].getEinzelPreis()));
//			data[i][5] = new String("Speichern");
//			data[i][6] = new String("Löschen");
//			data[i][7] = new Integer(positions[i].getId());
//		}
//
//		al = new ActionListener() {            
//					public void actionPerformed(ActionEvent e) { 
//						if(e.getActionCommand() == "Speichern"){
//							int row = getSelectedRow();
//							Position position = new Position(((Integer)getValueAt(row, 7)).intValue(), 
//															getValueAt(row, 1).toString(), 
//															((Float)getValueAt(row, 2)).floatValue(),
//															((Integer)getValueAt(row, 0)).intValue(), 
//															((Float)getValueAt(row, 3)).floatValue(),
//															((Float)getValueAt(row, 4)).floatValue()
//															);
//							//useCase.updatePosition(position);
//							System.out.println(position);
//						}else if(e.getActionCommand() == "Del"){
//							int answer = JOptionPane.showConfirmDialog(
//										getComponent(0),
//										"Soll die Position " + getSelectedRow() + " mit: \n"
//										+ getValueAt(getSelectedRow(), 1)
//										+ "\ngelöscht werden ? ",
//										"Warnung",
//										JOptionPane.YES_NO_OPTION,
//										JOptionPane.QUESTION_MESSAGE,
//										null);
//							if(answer == 0){
//								//useCase.deletePosition(position);
//								((DefaultTableModel)getModel()).removeRow(getSelectedRow());
//								((DefaultTableModel)getModel()).fireTableStructureChanged();
//								updateTableStructur();
//							}
//						}
//		           
//					}        
//				}; 
//							
//		setModel(new DefaultTableModel(title,0){
//										public Class getColumnClass(int c) {
//											 return getValueAt(0, c).getClass();
//										}
//							
//										public boolean isCellEditable(int rowIndex, int columnIndex){
//										  return columnIndex > 3;
//										}
//										
//								}
//						);
//		setDefaultEditor(Float.class, new JTableFloatEditor(0));
//	  	setDefaultRenderer(Float.class, new JTableCurrencyRenderer());
//	  	setDefaultEditor(Integer.class, new JTableIntegerEditor(1));
//	  	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
//	  	dtcr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
//	  	setDefaultRenderer(Integer.class, dtcr);
//	  	updateTableStructur();
//	  	
//	}
	
	private void updateTableStructur2(){
//		getColumnModel().getColumn(4).setCellEditor(new AbstractCellEditor());
//		getColumnModel().getColumn(4).setCellRenderer(new TableButtonCellRenderer("Del"));
		getColumnModel().getColumn(0).setMaxWidth(40);
		//getColumnModel().getColumn(1).setCellRenderer(new MultiLineRenderer());
		
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
	
//	private void updateTableStructur(){
//		JRadioButton test = new JRadioButton();
//		getColumnModel().getColumn(6).setCellEditor(new TableButtonCellEditor("Speichern", al));
//		getColumnModel().getColumn(6).setCellRenderer(new TableButtonCellRenderer("Speichern"));
//		getColumnModel().getColumn(7).setCellEditor(new TableButtonCellEditor("Löschen", al));
//		getColumnModel().getColumn(7).setCellRenderer(new TableButtonCellRenderer("", "Löschen", Functions.getDelIcon(this.getClass())));
//		getColumnModel().getColumn(0).setMaxWidth(50);
//		getColumnModel().getColumn(2).setMaxWidth(100);
//		getColumnModel().getColumn(3).setMaxWidth(50);
//		getColumnModel().getColumn(4).setMaxWidth(150);
//		getColumnModel().getColumn(5).setMaxWidth(70);
//		getColumnModel().getColumn(5).setMinWidth(70);
//		getColumnModel().getColumn(6).setMaxWidth(60);
//		getColumnModel().getColumn(6).setMinWidth(60);
//		getColumnModel().getColumn(7).setMinWidth(0);
//		getColumnModel().getColumn(7).setMaxWidth(0);
//		getColumnModel().getColumn(7).setWidth(0);
//	}
	
	
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
