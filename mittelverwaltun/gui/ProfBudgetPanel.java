
/**
 * @author Mario
 *
 * Folgendes auswählen, um die Schablone für den erstellten Typenkommentar zu ändern:
 * Fenster&gt;Benutzervorgaben&gt;Java&gt;Codegenerierung&gt;Code und Kommentare
 */
package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import dbObjects.Benutzer;
import dbObjects.FBHauptkonto;

public class ProfBudgetPanel extends JPanel implements TableModelListener, ItemListener {

  private JLabel labInstitut = new JLabel();
  private JTextField tfInstitut = new JTextField();
  private JLabel labHauptkto = new JLabel();
  private JComboBox comboHptkto = null;
  private JTable tabProfBudget = null;
  private JScrollPane spProfBudget = new JScrollPane();
  private JLabel labSumme = new JLabel();
  private CurrencyTextField tfSumme = new CurrencyTextField();
  private JLabel labOldBalance = new JLabel();
  private CurrencyTextField tfOldBalance = new CurrencyTextField();
  private JLabel labNewBalance = new JLabel();
  private CurrencyTextField tfNewBalance = new CurrencyTextField();
  private ActionListener actionListener = null;	


  public ProfBudgetPanel(String institut, FBHauptkonto[] konten, Benutzer[] professoren, float dfltBdgt) {
		
		this.setLayout(null);
		this.setMinimumSize(new Dimension(354, 274));
		this.setPreferredSize(new Dimension(354, 274));
		this.setBorder(BorderFactory.createEtchedBorder());
		
		labInstitut.setText("Institut");
		labInstitut.setBounds(new Rectangle(12, 12, 70, 20));
		labInstitut.setHorizontalAlignment(SwingConstants.RIGHT);
				
		tfInstitut.setText(institut);
		tfInstitut.setColumns(100);
		//tfInstitut.setEnabled(false);
		
		tfInstitut.setDisabledTextColor(Color.BLACK);
		tfInstitut.setEditable(false);
		tfInstitut.setBounds(new Rectangle(92, 12, 250, 20));
		//tfInstitut.setBackground(Color.white);
		//tfInstitut.setBorder(BorderFactory.createLoweredBevelBorder());
		
		labHauptkto.setText("Hauptkonto");
		labHauptkto.setBounds(new Rectangle(12, 42, 70, 20));
		labHauptkto.setHorizontalAlignment(SwingConstants.RIGHT);
		
		comboHptkto = new JComboBox(konten);
		comboHptkto.setEditable(false);
		comboHptkto.addItemListener(this);
		
		comboHptkto.setBounds(new Rectangle(92, 42, 250, 20));
		//comboHptkto.setBackground(Color.white);
				
		tabProfBudget = new JTable(new ProfBudgetTableModel(professoren, dfltBdgt));
		tabProfBudget.getModel().addTableModelListener(this);
		JTableCurrencyRenderer jtcr = new JTableCurrencyRenderer();
		jtcr.setHorizontalAlignment(SwingConstants.RIGHT);
		tabProfBudget.setDefaultEditor(Float.class, new JTableFloatEditor(0));
		tabProfBudget.setDefaultRenderer(Float.class, jtcr);
		tabProfBudget.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabProfBudget.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabProfBudget.getColumnModel().getColumn(0).setPreferredWidth(196);	// Menge
		tabProfBudget.getColumnModel().getColumn(1).setPreferredWidth(130);
		
		spProfBudget.getViewport().add(tabProfBudget, null);
		spProfBudget.getViewport().setBackground(Color.white);
		spProfBudget.setBorder(BorderFactory.createLoweredBevelBorder());
		spProfBudget.setBounds(new Rectangle(12, 72, 330, 100));
		
		
		labOldBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		labOldBalance.setText("Alter Kontostand");
		labOldBalance.setBounds(new Rectangle(97, 182, 105, 20));
		tfOldBalance.setEnabled(false);
		tfOldBalance.setEditable(false);
		tfOldBalance.setDisabledTextColor(Color.BLACK);
		tfOldBalance.setBounds(new Rectangle(212, 182, 130, 20));
		tfOldBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		tfOldBalance.setValue(new Float(getSelectedAccount()!=null ? getSelectedAccount().getBudget() : 0));
				
		labSumme.setHorizontalAlignment(SwingConstants.RIGHT);
		labSumme.setText("Überweisung");
		labSumme.setBounds(new Rectangle(97, 212, 105, 20));
		tfSumme.setEnabled(false);
		tfSumme.setEditable(false);
		tfSumme.setDisabledTextColor(Color.BLACK);
		tfSumme.setBounds(new Rectangle(212, 212, 130, 20));
		tfSumme.setHorizontalAlignment(SwingConstants.RIGHT);
		tfSumme.setValue(new Float(((ProfBudgetTableModel)(tabProfBudget.getModel())).calculateOverallBudget()));
		
		labNewBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		labNewBalance.setText("Neuer Kontostand");
		labNewBalance.setBounds(new Rectangle(97, 242, 105, 20));
		tfNewBalance.setEnabled(false);
		tfNewBalance.setEditable(false);
		tfNewBalance.setDisabledTextColor(Color.BLACK);
		tfNewBalance.setBounds(new Rectangle(212, 242, 130, 20));
		tfNewBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		tfNewBalance.setValue(new Float(((Float)tfOldBalance.getValue()).floatValue() + ((Float)tfSumme.getValue()).floatValue()));
				
		this.add(labHauptkto, null);
		this.add(comboHptkto, null);
		this.add(tfInstitut, null);
		this.add(labInstitut, null);
		this.add(spProfBudget, null);
		this.add(labSumme, null);
		this.add(tfSumme, null);
		this.add(labOldBalance, null);
		this.add(tfOldBalance, null);
		this.add(labNewBalance, null);
		this.add(tfNewBalance, null);
   }

   	public void tableChanged(TableModelEvent arg0) {
		tfSumme.setValue(new Float(((ProfBudgetTableModel)(tabProfBudget.getModel())).calculateOverallBudget()));
		tfNewBalance.setValue(new Float(((Float)tfOldBalance.getValue()).floatValue() + ((Float)tfSumme.getValue()).floatValue()));
		if (actionListener != null){
			actionListener.actionPerformed(new ActionEvent(this,0,"overall budget changed"));
		}
	}

	public void setStandardBudget (float budget){
		((ProfBudgetTableModel)(tabProfBudget.getModel())).setStandardBudget(budget);		
		tfSumme.setValue(new Float(((ProfBudgetTableModel)(tabProfBudget.getModel())).calculateOverallBudget()));
		tfNewBalance.setValue(new Float(((Float)tfOldBalance.getValue()).floatValue() + ((Float)tfSumme.getValue()).floatValue()));
		if (actionListener != null){
			actionListener.actionPerformed(new ActionEvent(this,0,"overall budget changed"));
		}
	}

	public FBHauptkonto getSelectedAccount(){
		if (comboHptkto.getSelectedIndex() != -1)
			return (FBHauptkonto)comboHptkto.getSelectedItem();
		else return null;
	}

	public float getRemmitance(){
		return ((Float)tfSumme.getValue()).floatValue();
	}

	public void enter(){
		getSelectedAccount().setBudget(((Float)tfNewBalance.getValue()).floatValue());
		tfOldBalance.setValue(new Float(((Float)tfNewBalance.getValue()).floatValue()));
		setStandardBudget(0);
	}
	
	public void addActionListener (ActionListener al){
		this.actionListener = al;
	}

	public static void main(String[] args) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		tfOldBalance.setValue(new Float(getSelectedAccount()!=null ? getSelectedAccount().getBudget() : 0));
		//tfSumme.setValue(new Float(((ProfBudgetTableModel)(tabProfBudget.getModel())).calculateOverallBudget()));
		tfNewBalance.setValue(new Float(((Float)tfOldBalance.getValue()).floatValue() + ((Float)tfSumme.getValue()).floatValue()));
	}
}
