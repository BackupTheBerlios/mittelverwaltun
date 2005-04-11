
/*
 * Created on 13.10.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.JTextComponent;

import dbObjects.FBHauptkonto;
import dbObjects.Institut;

import applicationServer.ApplicationServer;
import applicationServer.ApplicationServerException;
import applicationServer.CentralServer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.rmi.*;
import java.text.ParseException;

/**
 * @author Mario
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RemmitanceToFBHauptkontoFrame extends JInternalFrame implements ActionListener, TreeSelectionListener, FocusListener {
	MainFrame frame = null;
	ApplicationServer as = null;
	FBKontenTree treeAccounts = null;

	JButton btRefresh = new JButton(Functions.getRefreshIcon(this.getClass()));
	JScrollPane spAccountTree = new JScrollPane();
	JLabel lbInstitute = new JLabel();
	JLabel lbAccount = new JLabel();
	JLabel lbBalance = new JLabel();
	JLabel lbAvailableResources = new JLabel();
	JButton btAction = new JButton(Functions.getImportIcon(this.getClass()));
	JButton btCancel = new JButton(Functions.getCloseIcon(this.getClass()));
	JLabel lbRemmitance = new JLabel();
	JLabel lbNewBalance = new JLabel();
	JTextField tfInstitute = new JTextField();
	JTextField tfAccount = new JTextField();
	CurrencyTextField tfBalance = new CurrencyTextField();
	CurrencyTextField tfAvailableResources = new CurrencyTextField();
	CurrencyTextField tfRemmitance = new CurrencyTextField();
	CurrencyTextField tfNewBalance = new CurrencyTextField();
	JPanel plInfo = new JPanel();

	FBHauptkonto selectedAccount = null;

	public RemmitanceToFBHauptkontoFrame(MainFrame frame) {
		this.frame = frame;
		this.as = frame.applicationServer;
		
		this.getContentPane().setLayout(null);

		btRefresh.setBounds(new Rectangle(10, 10, 140, 25));
		btRefresh.setText("Aktualisieren");
		btRefresh.setActionCommand("refresh");
		btRefresh.addActionListener(this);
		
		spAccountTree.setBounds(new Rectangle(10, 40, 260, 225));
		spAccountTree.getViewport().add(treeAccounts = new FBKontenTree( this, "Fachbereichshauptkonten" ), null);
		this.loadAccounts();
		
				
		plInfo.setLayout(null);
		plInfo.setBounds(new Rectangle(275, 40, 320, 225));
		plInfo.setBorder(BorderFactory.createEtchedBorder());

		   lbInstitute.setHorizontalAlignment(SwingConstants.RIGHT);
		   lbInstitute.setText("Institut");
		   lbInstitute.setBounds(new Rectangle(10, 10, 110, 20));
		   tfInstitute.setEditable(false);
		   tfInstitute.setBounds(new Rectangle(130, 10, 180, 20));
		   tfInstitute.setHorizontalAlignment(SwingConstants.LEFT);
		   tfInstitute.addFocusListener(this);
		   
		   lbAccount.setHorizontalAlignment(SwingConstants.RIGHT);
		   lbAccount.setText("Konto");
		   lbAccount.setBounds(new Rectangle(10, 40, 110, 20));
		   tfAccount.setEditable(false);
		   tfAccount.setBounds(new Rectangle(130, 40, 180, 20));
		   tfAccount.setHorizontalAlignment(SwingConstants.LEFT);
		   tfAccount.addFocusListener(this);
		   
		   lbBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		   lbBalance.setText("Alter Kontostand");
		   lbBalance.setBounds(new Rectangle(9, 70, 110, 20));
		   tfBalance.setDisabledTextColor(Color.BLACK);
		   tfBalance.setEnabled(false);
		   tfBalance.setEditable(false);
		   tfBalance.setBounds(new Rectangle(130, 70, 180, 20));
		   tfBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		   
		   lbAvailableResources.setHorizontalAlignment(SwingConstants.RIGHT);
		   lbAvailableResources.setText("Verfügbare Mittel");
		   lbAvailableResources.setBounds(new Rectangle(9, 100, 110, 20));
		   tfAvailableResources.setDisabledTextColor(Color.BLACK);
		   tfAvailableResources.setEnabled(false);
		   tfAvailableResources.setEditable(false);
		   tfAvailableResources.setBounds(new Rectangle(130, 100, 180, 20));
		   tfAvailableResources.setHorizontalAlignment(SwingConstants.RIGHT);
		   
		   lbRemmitance.setHorizontalAlignment(SwingConstants.RIGHT);
		   lbRemmitance.setText("Überweisung");
		   lbRemmitance.setBounds(new Rectangle(10, 130, 110, 20));
		   tfRemmitance.setBounds(new Rectangle(130, 130, 180, 20));
		   tfRemmitance.setEnabled(false);
		   tfRemmitance.setEditable(false);
		   tfRemmitance.setHorizontalAlignment(SwingConstants.RIGHT);
		   tfRemmitance.addFocusListener(this);
		   
		   lbNewBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		   lbNewBalance.setText("Neuer Kontostand");
		   lbNewBalance.setBounds(new Rectangle(10, 160, 110, 20));
		   tfNewBalance.setDisabledTextColor(Color.BLACK);
		   tfNewBalance.setEnabled(false);
		   tfNewBalance.setEditable(false);
		   tfNewBalance.setBounds(new Rectangle(130, 160, 180, 20));
		   tfNewBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		   
		   btAction.setBounds(new Rectangle(190, 190, 120, 25));
		   btAction.setText("Zuweisen");
		   btAction.setActionCommand("execute");
		   btAction.addActionListener(this);

		plInfo.add(tfRemmitance, null);
		plInfo.add(lbInstitute, null);
		plInfo.add(tfInstitute, null);
		plInfo.add(lbAccount, null);
		plInfo.add(tfAccount, null);
		plInfo.add(lbBalance, null);
		plInfo.add(tfBalance, null);
		plInfo.add(lbAvailableResources, null);
		plInfo.add(tfAvailableResources, null);
		plInfo.add(lbRemmitance, null);
		plInfo.add(lbNewBalance, null);
		plInfo.add(tfNewBalance, null);
		plInfo.add(btAction, null);

		btCancel.setBounds(new Rectangle(465, 275, 120, 25));
		btCancel.setText("Beenden");
		btCancel.setActionCommand("dispose");
		btCancel.addActionListener(this);

		this.getContentPane().add(spAccountTree, null);
		this.getContentPane().add(plInfo, null);
		this.getContentPane().add(btCancel, null);	
		this.getContentPane().add(btRefresh, null);	

		this.setSize(615,345);
		this.setClosable(false);
		this.setTitle("Mittelverteilung an Fachbereichshauptkonten");
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
    
	}

	void loadAccounts() {
		if( as != null ) {
			try {
				treeAccounts.delTree();
				treeAccounts.loadInstituts( as.getInstitutesWithMainAccounts() );
			} catch (ApplicationServerException e) {
				System.out.println( e.toString() );
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		JFrame test = new JFrame("Mittelzuweisung auf FBHauptkonto Test");
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
			//RemmitanceToFBHauptkontoFrame rf = new RemmitanceToFBHauptkontoFrame(applicationServer);
			//desk.add(rf);
			//test.show();
			//rf.show();
		}catch(Exception e){
				System.out.println(e);
		}
		
	
	
	}

	private void clearTextFields(){
		tfInstitute.setText("");
		tfInstitute.setToolTipText("");
		tfAccount.setText("");
		tfAccount.setToolTipText("");
		tfAvailableResources.setValue(new Float(0));
		tfBalance.setValue(new Float(0));
		tfRemmitance.setValue(new Float(0));
		tfNewBalance.setValue(new Float(0));
	}	

	public void valueChanged(TreeSelectionEvent e) {

		Institut i = null;
		
		treeAccounts.checkSelection(e);
		clearTextFields();
		
		if ((i=treeAccounts.getInstitut())!=null){
			
			tfInstitute.setText(i.getBezeichnung());
			tfInstitute.setToolTipText(i.getBezeichnung());
			tfInstitute.setCaretPosition(0);
			if ((selectedAccount=treeAccounts.getFBHauptkonto())!=null){
				
				tfAccount.setText(selectedAccount.toString());
				tfAccount.setToolTipText(selectedAccount.toString());
				tfAccount.setCaretPosition(0);
				tfBalance.setValue(new Float(selectedAccount.getBudget()));
				tfNewBalance.setValue(new Float(selectedAccount.getBudget()));
				updateAvailableResources();
				
				btAction.setEnabled(true);
				tfRemmitance.setEnabled(true);
				tfRemmitance.setEditable(true);
				
				
			}else{		
				btAction.setEnabled(false);
				tfRemmitance.setEditable(false);
				tfRemmitance.setEnabled(false);
			}
		}else {
			selectedAccount = null;
			btAction.setEnabled(false);
			tfRemmitance.setEditable(false);
			tfRemmitance.setEnabled(false);
			
		}	
	}


	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();		
		
		if (cmd.equals("execute")){
			
			float newBudget = ((Float)tfNewBalance.getValue()).floatValue();
			float oldBudget = ((Float)tfBalance.getValue()).floatValue();
			float remmitance = ((Float)tfRemmitance.getValue()).floatValue();
			
			if (newBudget != oldBudget){
				
				try {
					
					as.setAccountBudget(frame.getBenutzer(), selectedAccount, remmitance);
					selectedAccount.setBudget(newBudget);
					tfBalance.setValue(new Float(newBudget));
					updateAvailableResources();
					tfRemmitance.setValue(new Float(0));
					
				} catch (ApplicationServerException exc) {
					
					if (exc.getErrorCode() != 153){
						
						this.loadAccounts();
					
					}else{
						
						tfBalance.setValue(new Float(selectedAccount.getBudget()));
						tfNewBalance.setValue(new Float(selectedAccount.getBudget()));
						updateAvailableResources();
						tfRemmitance.setValue(new Float(0));
					
					}
					
					MessageDialogs.showDetailMessageDialog(this, "Fehler", exc.getMessage(), exc.getNestedMessage(),MessageDialogs.WARNING_ICON);
				
				}
			}
		}else if (cmd.equals("refresh")){
			loadAccounts();
			clearTextFields();
			
		
		}else if (cmd.equals("dispose")){
			this.dispose();
		}
	}

	private void updateAvailableResources () {
		try {
			tfAvailableResources.setValue(new Float(as.getAvailableBudgetForAccount(selectedAccount)));
		} catch (ApplicationServerException exc1) {
			tfAvailableResources.setValue(new Float(0));
			MessageDialogs.showDetailMessageDialog(this, "Fehler", "Der Betrag verfügbarer Mittel konnte nicht bestimmt\nwerden und wird deshalb auf Null gesetzt.", exc1.getMessage(),MessageDialogs.WARNING_ICON);
		}
		
		tfRemmitance.setInterval(-((Float)tfBalance.getValue()).floatValue(),((Float)tfAvailableResources.getValue()).floatValue());						
	}

	
	public void focusGained(FocusEvent e) {
		if (e.getSource() != tfRemmitance)
			((JTextComponent)(e.getSource())).getCaret().setVisible(true);
	}

	
	public void focusLost(FocusEvent e) {
		if (e.getSource()!= tfRemmitance)
			((JTextComponent)(e.getSource())).getCaret().setVisible(false);
		else{
			try {
				tfRemmitance.commitEdit();
				tfNewBalance.setValue(new Float(((Float)tfBalance.getValue()).floatValue()+((Float)tfRemmitance.getValue()).floatValue()  ));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
	}
	
}
