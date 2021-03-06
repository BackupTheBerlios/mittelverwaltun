/*
 * Created on 07.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gui;


import java.awt.Component;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * @author Mario
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AccountTable extends JTable {
	
	// AccountTable-Typen
	static final int FB_KONTEN = 2;
	static final int ZV_KONTEN = 1;
	
	private int typ;
	
	public AccountTable(int typ, ArrayList accounts){
		
		this.typ = typ;
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setAccounts(accounts);
	
	}

	
	public AccountTableModel getAccountTableModel(){
		return (AccountTableModel)getModel();
	}
	

	
	public void setAccounts (ArrayList accounts){
		setModel(new AccountTableModel(typ, accounts));
		applyTableRendering();
	}
	
	public int getSelectedAccountID(){
		return ((AccountTableModel)getModel()).getId(getSelectedRow());
	}
	
//	public int getSelectedAccountStatus(){
//		return ((AccountTableModel)getModel()).getStatus(getSelectedRow());
//	}
	
	public int getTyp(){
		return typ;
	}
	
	private void applyTableRendering(){
		setRowHeight(20);
		setFont(new java.awt.Font("Dialog", 0, 11));
		setDefaultEditor(Float.class, new JTableFloatEditor(0));
		JTableCurrencyRenderer jtcr = new JTableCurrencyRenderer();
		jtcr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		setDefaultRenderer(Float.class, jtcr);
		setDefaultRenderer(Boolean.class, new ColoredBooleanCellRenderer());
		
		
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		
		// Portieren
		getColumnModel().getColumn(0).setPreferredWidth(60);
		// Kapitel oder Kostenstelle
		getColumnModel().getColumn(1).setPreferredWidth(typ == ZV_KONTEN? 50 : 80);
		getColumnModel().getColumn(1).setCellRenderer(dtcr);
		// Titelgruppe oder Hauptkonto
		getColumnModel().getColumn(2).setPreferredWidth(70);
		getColumnModel().getColumn(2).setCellRenderer(dtcr);
		// Bezeichnung
		getColumnModel().getColumn(3).setPreferredWidth(200);
		// Gesamtbudget
		getColumnModel().getColumn(4).setPreferredWidth(100);
		// Offene Bestellungen
		getColumnModel().getColumn(5).setPreferredWidth(120);
		getColumnModel().getColumn(5).setCellRenderer(dtcr);
		// Vormerkungen
		getColumnModel().getColumn(6).setPreferredWidth(100);
		// Übernahmestatus
		
		if (typ == ZV_KONTEN){
			getColumnModel().getColumn(7).setPreferredWidth(110);
			getColumnModel().getColumn(7).setCellRenderer(dtcr);
		  	JComboBox cbStatus = new JComboBox();
		  	cbStatus.addItem("keine");
		  	cbStatus.addItem("beantragt");
		  	cbStatus.addItem("bewilligt");
		  	cbStatus.setFont(this.getFont());
		  	DefaultTableCellRenderer colStatusRenderer = new DefaultTableCellRenderer();
		  	colStatusRenderer.setBackground(cbStatus.getBackground());
		  	getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(cbStatus));
		  	getColumnModel().getColumn(7).setCellRenderer(colStatusRenderer);
		}
		
		// Übernehmen
		getColumnModel().getColumn(9 - typ).setPreferredWidth(80);
		
		// Status
		if (typ==ZV_KONTEN) {
			DefaultTableCellRenderer cr9 = new DefaultTableCellRenderer();
			cr9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			//cr9.setFont(new java.awt.Font("Dialog", 1, 11));
			getColumnModel().getColumn(9).setPreferredWidth(150);
			getColumnModel().getColumn(9).setCellRenderer(cr9);
		}
		//getColumnModel().getColumn(7).setCellEditor(new TableButtonCellEditor(actionListener));
		//getColumnModel().getColumn(7).setCellRenderer(new TableButtonCellRenderer());
	}

	private static class ColoredBooleanCellRenderer implements TableCellRenderer {

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			JCheckBox cb = new JCheckBox();
			cb.setSelected(((Boolean)value).booleanValue());
			cb.setOpaque(true);
			cb.setMargin(new Insets(0,0,0,0));
			cb.setIconTextGap(0);
			cb.setDoubleBuffered(true);
			cb.setHorizontalAlignment(SwingConstants.CENTER);
			//cb.setBackground(Color.BLUE);
			AccountTableModel atm = ((AccountTable)table).getAccountTableModel();
			cb.setEnabled(atm.isCellEditable(row, col));
			return cb;
		}
	}
	
	public void tableChanged (TableModelEvent e){
		super.tableChanged(e);
		
		if ((e.getColumn() == 0)&& !(((Boolean)getAccountTableModel().getValueAt(e.getFirstRow(),e.getColumn()))).booleanValue()){
			getAccountTableModel().setUebernehmen(e.getFirstRow(), false);
			
		}
		
		if ((typ==ZV_KONTEN) && (e.getColumn() == 7)&& (getAccountTableModel().getStatus(e.getFirstRow())!= 3) && (getAccountTableModel().getUebernehmen(e.getFirstRow()))){
			getAccountTableModel().setUebernehmen(e.getFirstRow(), false);
		}
		
		if ((typ == ZV_KONTEN) && ((e.getColumn() == 8)||(e.getColumn() == 0)||(e.getColumn() == 7))){
			if ((getAccountTableModel().getStatus(e.getFirstRow()) == 0) || ((getAccountTableModel().getStatus(e.getFirstRow()) == 2) && getAccountTableModel().getUebernehmen(e.getFirstRow()))){
				getAccountTableModel().setValueAt(new String("Abschließen"), e.getFirstRow(), 9);
			}else{
				getAccountTableModel().setValueAt(new String("in Bearbeitung"), e.getFirstRow(), 9);
			}
		}
			
		
		if ((e.getFirstRow() >= 0)&&(e.getColumn()>=0)){
			applyTableRendering();
		}
	}
	
	public int getRowOfAccount(int accId){
		return getAccountTableModel().getRowOfAccount(accId);
	}
	
	public boolean isAccountCopied(int row){
		return getAccountTableModel().getPortieren(row);
	}
	
	public boolean isAccountBudgetCopied(int row){
		return getAccountTableModel().getUebernehmen(row);
	}
	
	public ArrayList getAccounts(){
		return getAccountTableModel().getAccounts();
	}
	
}