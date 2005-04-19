package gui;

import javax.swing.*;

import applicationServer.ApplicationServerException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuswahlHaushaltsjahr extends JInternalFrame implements ActionListener {
	  
	MainFrame frame = null;
	
	JButton btRefresh = new JButton(Functions.getRefreshIcon(this.getClass()));
	JButton btCancel = new JButton(Functions.getCloseIcon(this.getClass()));
	JPanel pnSeparator = new JPanel();
	JScrollPane spContent = new JScrollPane();
	BudgetYearTable budgetYearTab;
	
  public AuswahlHaushaltsjahr(MainFrame frame) {
		
  		this.frame = frame;
  		
  		this.setClosable(true);
  	    this.setIconifiable(true);
		this.getContentPane().setLayout(null);
		this.setSize(440,235);
		this.setTitle ("‹bersicht Haushaltsjahre");
		this.setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
		btRefresh.setBounds(new Rectangle(5, 5, 130, 25));
		btRefresh.setText("Aktualisieren");
		btRefresh.setFont(new java.awt.Font("Dialog", 1, 11));
		btRefresh.setActionCommand("refresh");
		btRefresh.addActionListener(this);
		
		spContent.setBounds(new Rectangle(5, 35, 420, 125));

		
	  try {
			budgetYearTab = new BudgetYearTable(this, frame.getApplicationServer().getHaushaltsjahre());
			spContent.getViewport().add(budgetYearTab, null);
		} catch (ApplicationServerException e) {
			e.printStackTrace();
		}
		
		
		pnSeparator.setBorder(BorderFactory.createLineBorder(Color.gray));
    pnSeparator.setBounds(new Rectangle(5, 165, 420, 2));
	
    btCancel.setBounds(new Rectangle(305, 172, 120, 25));
    btCancel.setText("Beenden");
    btCancel.setFont(new java.awt.Font("Dialog", 1, 11));
    btCancel.setActionCommand("dispose");
    btCancel.addActionListener(this);
		
		
		this.getContentPane().add(spContent, null);
		this.getContentPane().add(pnSeparator, null);
		this.getContentPane().add(btCancel, null);
		this.getContentPane().add(btRefresh, null);

		this.setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
  }

	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand()=="refresh"){
			try {
				budgetYearTab.setYears(frame.getApplicationServer().getHaushaltsjahre());
			} catch (ApplicationServerException e1) {
				e1.printStackTrace();
			}
		}else if (e.getActionCommand()=="dispose"){
			this.dispose();
		}else if (e.getActionCommand()=="finishYear"){
			if (budgetYearTab.getSelectedYearStatus() == 0) // aktuelles Haushaltsjahr abschlieﬂen
				frame.addChild( new AbschlussHaushaltsjahr( frame, budgetYearTab.getSelectedYearID(), true ) );
			else if (budgetYearTab.getSelectedYearStatus() == 1) // inaktives Haushaltsjahr vollst‰ndig abschlieﬂen
				frame.addChild( new AbschlussHaushaltsjahr( frame, budgetYearTab.getSelectedYearID(), false ) );
			
		}
	}

}
