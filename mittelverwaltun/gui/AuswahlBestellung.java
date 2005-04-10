package gui;

import javax.swing.*;


import applicationServer.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.*;


/**
 * <p>Title: Mittelverwaltung - GUI</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Mario Schmitt
 * @version 1.0
 */

public class AuswahlBestellung extends JInternalFrame implements ActionListener{
	
	
	MainFrame frame;
	
	JButton btBeenden = new JButton(Functions.getCloseIcon(this.getClass()));
	JScrollPane spBestellungen = new JScrollPane();
	JComboBox cbFilter;
	JButton btAktualisieren = new JButton(Functions.getRefreshIcon(this.getClass()));
	OrderTable tabBestellungen;

	public AuswahlBestellung(MainFrame frame) {
		this.frame = frame;
		try {
			jbInit();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
  
	private void jbInit() throws Exception {
	    
		this.setSize(800, 290);
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
    this.setFrameIcon(null);
	    this.setTitle("Bestellungsauswahl");
	    this.getContentPane().setLayout(null);
	
		String[] items = {"kein Filter", "Standardbestellungen", "ASK-Bestellungen", "Ausahlungsanforderungen"};
	
	    cbFilter = new JComboBox(items);
	    cbFilter.setBounds(new Rectangle(15, 12, 180, 27));
	    cbFilter.setFont(new java.awt.Font("Dialog", 1, 11));
	    	    
	    btAktualisieren.setBounds(new Rectangle(200, 12, 130, 27));
	    btAktualisieren.setFont(new java.awt.Font("Dialog", 1, 11));
	    btAktualisieren.setText("Aktualisieren");
	    btAktualisieren.setActionCommand("refresh");
	    btAktualisieren.addActionListener(this);
	    
	    tabBestellungen = new OrderTable(this, frame.applicationServer.getBestellungen());
	    tabBestellungen.setFont(new java.awt.Font("Dialog", 0, 11));
	    	    
	    spBestellungen.setBounds(new Rectangle(15, 49,760, 150));
	    spBestellungen.getViewport().add(tabBestellungen, null);
	
	    
	    btBeenden.setBounds(new Rectangle(655, 209, 120, 27));
	    btBeenden.setFont(new java.awt.Font("Dialog", 1, 11));
	    btBeenden.setActionCommand("dispose");
	    btBeenden.addActionListener(this);
	    btBeenden.setText("Beenden");
	
	    this.getContentPane().add(cbFilter, null);
	    this.getContentPane().add(btAktualisieren, null);
	    this.getContentPane().add(spBestellungen, null);
	    this.getContentPane().add(btBeenden, null);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "showOrder"){
	
			if (tabBestellungen.getSelectedOrderType()==OrderTable.STD_TYP){
				if(tabBestellungen.getSelectedOrderPhase()==OrderTable.SONDIERUNG)
					try {
						frame.addChild( new BestellungNormal( frame , frame.applicationServer.getStandardBestellung(tabBestellungen.getSelectedOrderID())));
					} catch (ApplicationServerException exception) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", exception.getMessage(), exception.getNestedMessage(), MessageDialogs.ERROR_ICON);
					} catch(RemoteException re) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", re.getMessage(), 
																"Fehler bei RMI-Kommunikation", MessageDialogs.ERROR_ICON);
					}
				else
					try {
						frame.addChild( new AbwicklungBestellungNormal( frame , frame.applicationServer.getStandardBestellung(tabBestellungen.getSelectedOrderID())));
					} catch (ApplicationServerException exception) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", exception.getMessage(), exception.getNestedMessage(), MessageDialogs.ERROR_ICON);
					} catch(RemoteException re) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", re.getMessage(), 
																"Fehler bei RMI-Kommunikation", MessageDialogs.ERROR_ICON);
					}
			}else if (tabBestellungen.getSelectedOrderType()==OrderTable.ASK_TYP){
				if(tabBestellungen.getSelectedOrderPhase()==OrderTable.SONDIERUNG)
					try {
						frame.addChild( new BestellungASK( frame , frame.applicationServer.getASKBestellung(tabBestellungen.getSelectedOrderID())));
					} catch (ApplicationServerException exception) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", exception.getMessage(), exception.getNestedMessage(), MessageDialogs.ERROR_ICON);
					} catch(RemoteException re) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", re.getMessage(), 
																"Fehler bei RMI-Kommunikation", MessageDialogs.ERROR_ICON);
					}
				else
					try {
						frame.addChild( new AbwicklungBestellungASK( frame , frame.applicationServer.getASKBestellung(tabBestellungen.getSelectedOrderID())));
					} catch (ApplicationServerException exception) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", exception.getMessage(), exception.getNestedMessage(), MessageDialogs.ERROR_ICON);
					} catch(RemoteException re) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", re.getMessage(), 
																"Fehler bei RMI-Kommunikation", MessageDialogs.ERROR_ICON);
					}
			}else if (tabBestellungen.getSelectedOrderType()==OrderTable.ZA_TYP){
				if(tabBestellungen.getSelectedOrderPhase()==OrderTable.ABGESCHLOSSEN)
					try {
						frame.addChild( new BestellungKlein( frame , frame.applicationServer.getKleinbestellung(tabBestellungen.getSelectedOrderID())));
					} catch (ApplicationServerException exception) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", exception.getMessage(), exception.getNestedMessage(), MessageDialogs.ERROR_ICON);
					} catch(RemoteException re) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", re.getMessage(), 
																"Fehler bei RMI-Kommunikation", MessageDialogs.ERROR_ICON);
					}
			}
		} else if(e.getActionCommand() == "refresh"){
			String filter = (String)cbFilter.getSelectedItem();
			try {
				if (filter.equals("kein Filter")){
					tabBestellungen.setOrders(frame.applicationServer.getBestellungen());
				}else if (filter.equals("Standardbestellungen")){
					tabBestellungen.setOrders(frame.applicationServer.getBestellungen(0));
				}else if (filter.equals("ASK-Bestellungen")){
					tabBestellungen.setOrders(frame.applicationServer.getBestellungen(1));
				}else if (filter.equals("Ausahlungsanforderungen")){
					tabBestellungen.setOrders(frame.applicationServer.getBestellungen(2));
				}
			} catch (ApplicationServerException e1) {
				e1.printStackTrace();
			} catch(RemoteException re) {
				MessageDialogs.showDetailMessageDialog(this, "Fehler", re.getMessage(), 
														"Fehler bei RMI-Kommunikation", MessageDialogs.ERROR_ICON);
			}
		} else if(e.getActionCommand() == "dispose"){
			this.dispose();
		}
	}
	
	public static void main(String[] args) {
		MainFrame test = new MainFrame("FBMittelverwaltung");
		
		try{
			CentralServer server = (CentralServer)Naming.lookup("//localhost/mittelverwaltung");
//			ApplicationServer applicationServer = server.getMyApplicationServer();
//			test.setApplicationServer(applicationServer);
//			PasswordEncrypt pe = new PasswordEncrypt();
//			String psw = pe.encrypt(new String("r.driesner").toString());
//			test.setBenutzer(applicationServer.login("r.driesner", psw));
			test.setBounds(100,100,800,900);
			test.setExtendedState(Frame.MAXIMIZED_BOTH);

			test.setJMenuBar( new MainMenu( test ) );

			AuswahlBestellung ab = new AuswahlBestellung(test);
			test.addChild(ab);
			test.setVisible(true);
			ab.show();
	 }catch(Exception e){
			System.out.println(e);
	 }
  }
}