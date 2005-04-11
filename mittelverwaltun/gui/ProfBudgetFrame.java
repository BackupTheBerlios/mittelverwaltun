/*
 * Created on 11.10.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui;
import javax.swing.*;

import applicationServer.ApplicationServer;
import applicationServer.ApplicationServerException;
import applicationServer.CentralServer;

import dbObjects.Benutzer;
import dbObjects.FBHauptkonto;
import dbObjects.Fachbereich;
import dbObjects.Institut;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Mario
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ProfBudgetFrame extends JInternalFrame implements ActionListener{
	MainFrame frame = null;
	ApplicationServer as = null;

	JButton btChangeSet = new JButton(Functions.getEditIcon(this.getClass()));
	JLabel lbPauschBudget = new JLabel();
	CurrencyTextField tfPauschBudget = new CurrencyTextField(0);
	JScrollPane spInstitute = new JScrollPane();
//	JPanel pnInstitute = new JPanel();
	JPanel pnInstitute = null;
	CurrencyTextField tfBudgetSum = new CurrencyTextField(0);
	CurrencyTextField tfAvBudget = new CurrencyTextField(0);
	JLabel lbBudgetSum = new JLabel();
	JLabel lbAvBudget = new JLabel();
	JPanel pnActions = new JPanel();
	JButton btBuchen = new JButton(Functions.getImportIcon(this.getClass()));
	JButton btCancel = new JButton(Functions.getCloseIcon(this.getClass()));
	JButton btRefresh = new JButton(Functions.getRefreshIcon(this.getClass()));
	ProfBudgetPanel[] budgetPanels = null;

	

	
	public ProfBudgetFrame(MainFrame frame) {
	   this.frame = frame;
	   this.as = frame.applicationServer;
	   	   
	   lbAvBudget.setHorizontalAlignment(SwingConstants.RIGHT);
	   lbAvBudget.setText("Verfügbare Mittel:");
	   lbAvBudget.setBounds(new Rectangle(10, 10, 115, 20));
	   
	   tfAvBudget.setBounds(new Rectangle(130, 10, 120, 20));
	   tfAvBudget.setFont(new java.awt.Font("Dialog", 1, 12));
	   tfAvBudget.setDisabledTextColor(Color.RED);
	   tfAvBudget.setBorder(null);
	   tfAvBudget.setEnabled(false);
	   tfAvBudget.setEditable(false);
	   tfAvBudget.setHorizontalAlignment(SwingConstants.LEFT);
	   
	   btRefresh.setBounds(new Rectangle(254, 8, 130, 25));
	   btRefresh.setText("Aktualisieren");
	   btRefresh.addActionListener(this);
	   btRefresh.setActionCommand("refresh");

	   lbPauschBudget.setHorizontalAlignment(SwingConstants.RIGHT);
	   lbPauschBudget.setText("Pauschalbudget");
	   lbPauschBudget.setBounds(new Rectangle(10, 42, 115, 20));
	   tfPauschBudget.setBounds(new Rectangle(130, 42, 120, 20));
	   tfPauschBudget.setEnabled(false);
	   tfPauschBudget.setEditable(false);
	   tfPauschBudget.setDisabledTextColor(Color.BLACK);
	   tfPauschBudget.setHorizontalAlignment(SwingConstants.RIGHT);
	   
	   btChangeSet.setBounds(new Rectangle(254, 40, 130, 25));
	   btChangeSet.setText("Übernehmen");
	   btChangeSet.addActionListener(this);
	   btChangeSet.setActionCommand("set");

	   spInstitute.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   spInstitute.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	   spInstitute.setBounds(new Rectangle(10, 75, 374, 277));
	   
	   lbBudgetSum.setHorizontalAlignment(SwingConstants.RIGHT);
	   lbBudgetSum.setText("Zu verteilende Mittel");
	   lbBudgetSum.setBounds(new Rectangle(10, 364, 115, 20));
	   tfBudgetSum.setBounds(new Rectangle(130, 364, 120, 20));
	   tfBudgetSum.setEnabled(false);
	   tfBudgetSum.setEditable(false);
	   tfBudgetSum.setDisabledTextColor(Color.BLACK);
	   tfBudgetSum.setHorizontalAlignment(SwingConstants.RIGHT);

	   btBuchen.setBounds(new Rectangle(254, 362, 130, 25));
	   btBuchen.setText("Zuweisen");
	   btBuchen.setActionCommand("execute");
	   btBuchen.addActionListener(this);
	   btCancel.setBounds(new Rectangle(254, 403, 130, 25));
	   btCancel.setText("Beenden");
	   btCancel.addActionListener(this);
	   btCancel.setActionCommand("dispose");

	   pnActions.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	   pnActions.setBounds(new Rectangle(10, 394, 374, 1));
	   pnActions.setLayout(null);
	  	   
	   this.getContentPane().setLayout(null);
	   this.setSize(404,470);
	   this.setClosable(false);
	   this.setTitle("Mittelverteilung nach Professorenbudgets");
	   this.getContentPane().add(btChangeSet, null);
	   this.getContentPane().add(tfPauschBudget, null);
	   this.getContentPane().add(spInstitute, null);
	   this.getContentPane().add(tfAvBudget, null);
	   this.getContentPane().add(lbBudgetSum, null);
	   this.getContentPane().add(lbAvBudget, null);
	   this.getContentPane().add(lbPauschBudget, null);
	   this.getContentPane().add(tfBudgetSum, null);
	   this.getContentPane().add(pnActions, null);
	   this.getContentPane().add(btCancel, null);
	   this.getContentPane().add(btBuchen, null);
	   this.getContentPane().add(btRefresh, null);
	   
	   this.actualize();
	   this.calculateOverallBudget();
		 setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
    
	}

	public void calculateOverallBudget(){
		float oaBudget = 0;
		for (int i=0; i<budgetPanels.length; i++){
			if (budgetPanels[i]!=null)
				oaBudget += budgetPanels[i].getRemmitance();
		}
		
		tfBudgetSum.setValue(new Float(oaBudget));
		if (oaBudget > ((Float)tfAvBudget.getValue()).floatValue()){
			
			tfBudgetSum.setBackground(Color.PINK);		
			tfAvBudget.setBackground(Color.PINK);
			btBuchen.setEnabled(false);
			
		}else{
			tfBudgetSum.setBackground(this.getBackground());
			tfAvBudget.setBackground(this.getBackground());
			btBuchen.setEnabled(true);
		}
		
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("edit")){
			tfPauschBudget.setEnabled(true);
			tfPauschBudget.setEditable(true);
			btChangeSet.setText("Übernehmen");
			btChangeSet.setIcon(Functions.getDownIcon(this.getClass()));
			btChangeSet.setActionCommand("set");
		}else if (cmd.equals("set")){
			tfPauschBudget.setEditable(false);
			tfPauschBudget.setEnabled(false);
			for (int i=0; i<budgetPanels.length; i++){
				if (budgetPanels[i]!=null)
					budgetPanels[i].setStandardBudget(((Float)tfPauschBudget.getValue()).floatValue());
			}
			btChangeSet.setText("Ändern");
			btChangeSet.setIcon(Functions.getEditIcon(this.getClass()));
			btChangeSet.setActionCommand("edit");
		}else if (cmd.equals("overall budget changed")){
			this.calculateOverallBudget();
		}else if (cmd.equals("dispose")){
			this.dispose();
		}else if (cmd.equals("execute")){
			for (int i=0; i<budgetPanels.length; i++){
				if (budgetPanels[i]!=null){
					try {
						as.setAccountBudget(frame.getBenutzer(), budgetPanels[i].getSelectedAccount(), budgetPanels[i].getRemmitance());
						budgetPanels[i].enter();
						try {
							tfAvBudget.setValue(new Float(as.getAvailableNoPurposeBudget()));
					 	} catch (ApplicationServerException exc) {
							tfAvBudget.setValue(new Float(0));
					 	}
					} catch (ApplicationServerException exc) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", exc.getMessage(), exc.getNestedMessage(),MessageDialogs.WARNING_ICON);
						exc.printStackTrace();
					}
					
				}
			}
		}else if (cmd.equals("refresh")){
			this.actualize();
		}
	}

	public static void main(String[] args) {
		JFrame test = new JFrame("Mittelzuweisung nach Professorbudgets Test");
		JDesktopPane desk = new JDesktopPane();
		desk.setDesktopManager(new DefaultDesktopManager());
		test.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		test.setContentPane(desk);
		test.setBounds(100,100,800,700);
		try{
			CentralServer server = (CentralServer)Naming.lookup("//localhost/mittelverwaltung");
//			ApplicationServer applicationServer = server.getMyApplicationServer();
//			PasswordEncrypt pe = new PasswordEncrypt();
//			String psw = pe.encrypt(new String("r.driesner").toString());
//			applicationServer.login("r.driesner", psw);
//			ProfBudgetFrame pdf = new ProfBudgetFrame(applicationServer);
//			desk.add(pdf);
//			test.show();
//			pdf.show();
		}catch(Exception e){
				System.out.println(e);
		}
	}

	private void actualize(){
		
		try {
			Fachbereich[] fbs = as.getFachbereiche();
			if ((fbs != null) && (fbs.length == 1)){
				tfPauschBudget.setValue(new Float(fbs[0].getProfPauschale()));
			}else{
				tfPauschBudget.setValue(new Float(0));
			}
		} catch (ApplicationServerException e) {
			tfPauschBudget.setValue(new Float(0));
			e.printStackTrace();
			MessageDialogs.showDetailMessageDialog(this, "Fehler bei Bestimmung d. Professorenpauschale", e.getMessage(), e.getNestedMessage(),MessageDialogs.WARNING_ICON);
		}
		
	 	try {
			tfAvBudget.setValue(new Float(as.getAvailableNoPurposeBudget()));
	 	} catch (ApplicationServerException e) {
			tfAvBudget.setValue(new Float(0));
			e.printStackTrace();
			MessageDialogs.showDetailMessageDialog(this, "Fehler bei Bestimmung d. verfügbaren Budgets", e.getMessage(), e.getNestedMessage(),MessageDialogs.WARNING_ICON);
		 	
		}
		
	 	try{
			ArrayList pnData = new ArrayList();
			
			Institut[] institutes = as.getInstitutes();
			
			//System.out.println("Institute: "+ institutes.length);
			
			for (int i=0; i<institutes.length; i++){
				
				ArrayList accounts = as.getNoPurposeFBHauptkonten(institutes[i]);	
				//System.out.println("Konten: "+ accounts.size());;
				Benutzer[] users = as.getUsersByRole(institutes[i], 5);
				//System.out.println("Benutzer: " + ((users!=null)? ""+users.length : "null"));
				if ((!accounts.isEmpty()) && (users!=null)){
					Object[] r = {institutes[i], accounts.toArray(), users};
					pnData.add(r);
				}
			}
			
			//System.out.println("Panels: "+ pnData.size());
			if (pnInstitute != null){
				spInstitute.remove(pnInstitute);
			}
			
			pnInstitute = new JPanel();
			pnInstitute.setLayout(new GridLayout(pnData.size(),1));		
			budgetPanels = new ProfBudgetPanel[pnData.size()];
			
			Iterator it = pnData.iterator();
			int i = 0;
			while (it.hasNext()){
				Object[] obj = (Object[])it.next();

				Institut institute = (Institut)obj[0];

				FBHauptkonto[] accounts = new FBHauptkonto[((Object[])obj[1]).length];
				for (int j=0;j<accounts.length;j++)
					accounts[j]=(FBHauptkonto)((Object[])obj[1])[j];

				Benutzer[] users = (Benutzer[])obj[2];
			
				budgetPanels[i] = new ProfBudgetPanel(institute.getBezeichnung(), accounts, users, 0);
				budgetPanels[i].addActionListener(this);
				pnInstitute.add(budgetPanels[i]);
				i++;  	
			}
			
			spInstitute.getViewport().add(pnInstitute, null);
			
	   }catch(ApplicationServerException e){
	   		e.printStackTrace();
	   		MessageDialogs.showDetailMessageDialog(this, "Fehler bei Ermittlung der Professoren bzw. Konten eines Instituts", e.getMessage(), e.getNestedMessage(),MessageDialogs.WARNING_ICON);
		}
	}
}
