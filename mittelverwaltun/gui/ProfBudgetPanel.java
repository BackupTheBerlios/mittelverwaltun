
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

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import dbObjects.Benutzer;
import dbObjects.FBHauptkonto;

public class ProfBudgetPanel extends JPanel implements TableModelListener {

  private JLabel labInstitut = new JLabel();
  private JTextField tfInstitut = new JTextField();
  private JLabel labHauptkto = new JLabel();
  private JComboBox comboHptkto = null;
  private JTable tabProfBudget = null;
  private JScrollPane spProfBudget = new JScrollPane();
  private JLabel labSumme = new JLabel();
  private CurTextField tfSumme = new CurTextField();
  private ActionListener actionListener = null;	


  public ProfBudgetPanel(String institut, FBHauptkonto[] konten, Benutzer[] professoren, float dfltBdgt) {
		
		this.setLayout(null);
		this.setMinimumSize(new Dimension(354, 264));
		this.setPreferredSize(new Dimension(354, 264));
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
		
		comboHptkto.setBounds(new Rectangle(92, 42, 250, 20));
		//comboHptkto.setBackground(Color.white);
				
		tabProfBudget = new JTable(new ProfBudgetTableModel(professoren, dfltBdgt));
		tabProfBudget.getModel().addTableModelListener(this);
		tabProfBudget.setDefaultEditor(Float.class, new JTableFloatEditor(0));
		tabProfBudget.setDefaultRenderer(Float.class, new JTableCurrencyRenderer());
				
		spProfBudget.getViewport().add(tabProfBudget, null);
		spProfBudget.getViewport().setBackground(Color.white);
		spProfBudget.setBorder(BorderFactory.createLoweredBevelBorder());
		spProfBudget.setBounds(new Rectangle(92, 72, 250, 150));
		
		labSumme.setHorizontalAlignment(SwingConstants.RIGHT);
		labSumme.setText("Summe");
		labSumme.setBounds(new Rectangle(92, 232, 85, 20));
		//tfSumme.setBackground(Color.white);
		tfSumme.setEnabled(false);
		tfSumme.setDisabledTextColor(Color.BLACK);
		tfSumme.setBounds(new Rectangle(187, 232, 155, 20));
		tfSumme.setHorizontalAlignment(SwingConstants.RIGHT);
		tfSumme.setAmount(((ProfBudgetTableModel)(tabProfBudget.getModel())).calculateOverallBudget());
		
		this.add(labHauptkto, null);
		this.add(comboHptkto, null);
		this.add(tfInstitut, null);
		this.add(labInstitut, null);
		this.add(spProfBudget, null);
		this.add(labSumme, null);
		this.add(tfSumme, null);
   }

   	public void tableChanged(TableModelEvent arg0) {
		tfSumme.setAmount(((ProfBudgetTableModel)(tabProfBudget.getModel())).calculateOverallBudget());
		if (actionListener != null){
			actionListener.actionPerformed(new ActionEvent(this,0,"overall budget changed"));
		}
	}

	public void setStandardBudget (float budget){
		((ProfBudgetTableModel)(tabProfBudget.getModel())).setStandardBudget(budget);		
		tfSumme.setAmount(((ProfBudgetTableModel)(tabProfBudget.getModel())).calculateOverallBudget());
		if (actionListener != null){
			actionListener.actionPerformed(new ActionEvent(this,0,"overall budget changed"));
		}
	}

	public FBHauptkonto getSelectedAccount(){
		if (comboHptkto.getSelectedIndex() != -1)
			return (FBHauptkonto)comboHptkto.getSelectedItem();
		else return null;
	}

	public float getOverallBudget(){
		return tfSumme.getAmount();
	}

	public void addActionListener (ActionListener al){
		this.actionListener = al;
	}

	public static void main(String[] args) {
	}
}
