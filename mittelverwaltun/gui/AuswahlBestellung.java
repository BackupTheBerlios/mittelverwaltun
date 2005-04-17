package gui;

import javax.swing.*;

import dbObjects.Benutzer;
import dbObjects.Rolle;


import applicationServer.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.*;
import java.util.ArrayList;


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
	int[] validTypes = {-1, -1, -1};
	
	public AuswahlBestellung(MainFrame frame) {
		this.frame = frame;
		try {
			jbInit();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
  
	private void jbInit() throws Exception {
	    this.setClosable(true);
	   
		this.setSize(800, 290);
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
		this.setFrameIcon(null);
	    this.setTitle("Bestellungsauswahl");
	    this.getContentPane().setLayout(null);
	
		//String[] items = {"kein Filter", "Standardbestellungen", "ASK-Bestellungen", "Ausahlungsanforderungen"};
	
	    cbFilter = new JComboBox();
	    cbFilter.addItem("kein Filter");
	    Rolle r = frame.getActiveRole();
	    if (r.hasAktivitaet(6)){
	    	cbFilter.addItem("Auszahlungsanforderungen");
	    	validTypes[0] = 2;
	    }
	    if (r.hasAktivitaet(7)){
	    	cbFilter.addItem("Standardbestellungen");
	    	validTypes[1] = 0;
	    }
	    if (r.hasAktivitaet(8)){
	    	cbFilter.addItem("ASK-Bestellungen");
	    	validTypes[2] = 1;
	    }
	    cbFilter.setBounds(new Rectangle(15, 12, 180, 27));
	    cbFilter.setFont(new java.awt.Font("Dialog", 1, 11));
	    	    
	    btAktualisieren.setBounds(new Rectangle(200, 12, 130, 27));
	    btAktualisieren.setFont(new java.awt.Font("Dialog", 1, 11));
	    btAktualisieren.setText("Aktualisieren");
	    btAktualisieren.setActionCommand("refresh");
	    btAktualisieren.addActionListener(this);
	    
	    Benutzer b = frame.getBenutzer();
	    ArrayList orders = new ArrayList();
	    if (b != null){
	    	if (b.getSichtbarkeit() == Benutzer.VIEW_FACHBEREICH)
	    		orders = frame.applicationServer.getBestellungen(validTypes);
	    	else if (b.getSichtbarkeit() == Benutzer.VIEW_INSTITUT)
	    		orders = frame.applicationServer.getInstitutsbestellungen(b.getKostenstelle().getId(), validTypes);
	    	else if (b.getSichtbarkeit() == Benutzer.VIEW_PRIVAT)
	    		orders = frame.applicationServer.getKontenbestellungen(b.getPrivatKonto(), validTypes);
	    }
	    tabBestellungen = new OrderTable(this, orders);
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
					}
				else
					try {
						frame.addChild( new AbwicklungBestellungNormal( frame , frame.applicationServer.getStandardBestellung(tabBestellungen.getSelectedOrderID())));
					} catch (ApplicationServerException exception) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", exception.getMessage(), exception.getNestedMessage(), MessageDialogs.ERROR_ICON);
					}
			}else if (tabBestellungen.getSelectedOrderType()==OrderTable.ASK_TYP){
				if(tabBestellungen.getSelectedOrderPhase()==OrderTable.SONDIERUNG)
					try {
						frame.addChild( new BestellungASK( frame , frame.applicationServer.getASKBestellung(tabBestellungen.getSelectedOrderID())));
					} catch (ApplicationServerException exception) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", exception.getMessage(), exception.getNestedMessage(), MessageDialogs.ERROR_ICON);
					}
				else
					try {
						frame.addChild( new AbwicklungBestellungASK( frame , frame.applicationServer.getASKBestellung(tabBestellungen.getSelectedOrderID())));
					} catch (ApplicationServerException exception) {
						//System.out.println(exception.getErrorCode());
						//System.out.println(exception.getNestedMessage());
						MessageDialogs.showDetailMessageDialog(this, "Fehler", exception.getMessage(), exception.getNestedMessage(), MessageDialogs.ERROR_ICON);
					}
			}else if (tabBestellungen.getSelectedOrderType()==OrderTable.ZA_TYP){
				if(tabBestellungen.getSelectedOrderPhase()==OrderTable.ABGESCHLOSSEN)
					try {
						frame.addChild( new BestellungKlein( frame , frame.applicationServer.getKleinbestellung(tabBestellungen.getSelectedOrderID())));
					} catch (ApplicationServerException exception) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", exception.getMessage(), exception.getNestedMessage(), MessageDialogs.ERROR_ICON);
					}
			}
		} else if(e.getActionCommand() == "refresh"){
			String filter = (String)cbFilter.getSelectedItem();
			int[] types = {-1, -1, -1};
			if (filter.equals("kein Filter")){
				for (int i=0; i<validTypes.length; i++)
					types[i] = validTypes[i];
			}else if (filter.equals("Standardbestellungen")){
				types[0] = 0;
			}else if (filter.equals("ASK-Bestellungen")){
				types[0] = 1;
			}else if (filter.equals("Auszahlungsanforderungen")){
				types[0] = 2;
			}
			ArrayList orders = new ArrayList();
			try {
				Benutzer b = frame.getBenutzer();
			    if (b != null){
			    	if (b.getSichtbarkeit() == Benutzer.VIEW_FACHBEREICH)
			    		orders = frame.applicationServer.getBestellungen(types);
			    	else if (b.getSichtbarkeit() == Benutzer.VIEW_INSTITUT)
			    		orders = frame.applicationServer.getInstitutsbestellungen(b.getKostenstelle().getId(), types);
			    	else if (b.getSichtbarkeit() == Benutzer.VIEW_PRIVAT)
			    		orders = frame.applicationServer.getKontenbestellungen(b.getPrivatKonto(), types);
			    }
			} catch (ApplicationServerException e1) {
				e1.printStackTrace();
			}
			tabBestellungen.setOrders(orders);
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