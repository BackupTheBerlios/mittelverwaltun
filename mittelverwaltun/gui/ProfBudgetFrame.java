

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
import dbObjects.Institut;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Mario
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ProfBudgetFrame extends JInternalFrame implements ActionListener{

	ApplicationServer as = null;

	JButton btChangeSet = new JButton();
	JLabel lbPauschBudget = new JLabel();
	CurrencyTextField tfPauschBudget = new CurrencyTextField(0);
	JScrollPane spInstitute = new JScrollPane();
	JPanel pnInstitute = new JPanel();
	CurrencyTextField tfBudgetSum = new CurrencyTextField(0);
	CurrencyTextField tfAvBudget = new CurrencyTextField(0);
	JLabel lbBudgetSum = new JLabel();
	JLabel lbAvBudget = new JLabel();
	JPanel pnActions = new JPanel();
	JButton btBuchen = new JButton();
	JButton btCancel = new JButton();
	ProfBudgetPanel[] budgetPanels = null;

	public ProfBudgetFrame(ApplicationServer as) {
	   
	   this.as = as;
	   	   
	   lbPauschBudget.setHorizontalAlignment(SwingConstants.RIGHT);
	   lbPauschBudget.setText("Pauschalbudget");
	   lbPauschBudget.setBounds(new Rectangle(10, 12, 115, 20));
	   tfPauschBudget.setValue(new Float(500));
	   tfPauschBudget.setBounds(new Rectangle(130, 12, 120, 20));
	   tfPauschBudget.setEnabled(false);
	   tfPauschBudget.setEditable(false);
	   tfPauschBudget.setDisabledTextColor(Color.BLACK);
	   tfPauschBudget.setHorizontalAlignment(SwingConstants.RIGHT);
	   btChangeSet.setBounds(new Rectangle(254, 10, 110, 25));
	   btChangeSet.setText("Ändern");
	   btChangeSet.addActionListener(this);


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
			
				budgetPanels[i] = new ProfBudgetPanel(institute.getBezeichnung(), accounts, users, 500);
				budgetPanels[i].addActionListener(this);
				pnInstitute.add(budgetPanels[i]);
				i++;  	
			}
			
	   }catch(ApplicationServerException e){
	   		System.out.println(e.getMessage());
	   		System.out.println(e.getNestedMessage());
	   }

	   spInstitute.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	   spInstitute.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	   spInstitute.setBounds(new Rectangle(10, 45, 374, 264));
	   spInstitute.getViewport().add(pnInstitute, null);

	   lbBudgetSum.setHorizontalAlignment(SwingConstants.RIGHT);
	   lbBudgetSum.setText("Zu verteilende Mittel");
	   lbBudgetSum.setBounds(new Rectangle(50, 320, 145, 20));
	   tfBudgetSum.setBounds(new Rectangle(200, 320, 150, 20));
	   tfBudgetSum.setEnabled(false);
	   tfBudgetSum.setEditable(false);
	   tfBudgetSum.setDisabledTextColor(Color.BLACK);
	   tfBudgetSum.setHorizontalAlignment(SwingConstants.RIGHT);

	   lbAvBudget.setHorizontalAlignment(SwingConstants.RIGHT);
	   lbAvBudget.setText("Verfügbare freie Mittel");
	   lbAvBudget.setBounds(new Rectangle(50, 350, 145, 20));
	   try {
			tfAvBudget.setValue(new Float(as.getAvailableNoPurposeBudget()));
		} catch (ApplicationServerException e1) {
			tfAvBudget.setValue(new Float(0));
	   }
	   tfAvBudget.setBounds(new Rectangle(200, 350, 150, 20));
	   tfAvBudget.setEnabled(false);
	   tfAvBudget.setEditable(false);
	   tfAvBudget.setDisabledTextColor(Color.BLACK);
	   tfAvBudget.setHorizontalAlignment(SwingConstants.RIGHT);
	   
	   btBuchen.setBounds(new Rectangle(144, 10, 100, 25));
	   btBuchen.setText("Buchen");
	   btCancel.setBounds(new Rectangle(254, 10, 100, 25));
	   btCancel.setText("Abbrechen");
	   btCancel.addActionListener(this);

	   pnActions.setBorder(BorderFactory.createEtchedBorder());
	   pnActions.setBounds(new Rectangle(10, 380, 374, 45));
	   pnActions.setLayout(null);
	   pnActions.add(btBuchen, null);
	   pnActions.add(btCancel, null);

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
	   
	   this.calculateOverallBudget();
	   
	}

	public void calculateOverallBudget(){
		float oaBudget = 0;
		for (int i=0; i<budgetPanels.length; i++){
			if (budgetPanels[i]!=null)
				oaBudget += budgetPanels[i].getOverallBudget();
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


	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("Ändern")){
			tfPauschBudget.setEnabled(true);
			tfPauschBudget.setEditable(true);
			//tfPauschBudget.setBackground(this.getDesktopPane().getBackground());
			btChangeSet.setText("Übernehmen");
			
		}else if (cmd.equals("Übernehmen")){
			tfPauschBudget.setEditable(false);
			tfPauschBudget.setEnabled(false);
			//tfPauschBudget.setBackground(this.getBackground());
			for (int i=0; i<budgetPanels.length; i++){
				if (budgetPanels[i]!=null)
					budgetPanels[i].setStandardBudget(((Float)tfPauschBudget.getValue()).floatValue());
			}
			btChangeSet.setText("Ändern");
		}else if (cmd.equals("overall budget changed")){
			this.calculateOverallBudget();
		}else if (cmd.equals("Abbrechen")){
			this.dispose();
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
			ApplicationServer applicationServer = server.getMyApplicationServer();
			PasswordEncrypt pe = new PasswordEncrypt();
			String psw = pe.encrypt(new String("r.driesner").toString());
			applicationServer.login("r.driesner", psw);
			ProfBudgetFrame pdf = new ProfBudgetFrame(applicationServer);
			desk.add(pdf);
			test.show();
			pdf.show();
		}catch(Exception e){
				System.out.println(e);
		}
	
	
	}




}
