package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * <p>Title: Reports - GUI</p>
 * <p>Description: Frame zum Anzeigen verschiedener Reports</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Robert
 * @version 1.0
 */
public class Reports extends JInternalFrame implements ActionListener{
	
	
	MainFrame frame;
	
	JButton btBeenden = new JButton(Functions.getCloseIcon(this.getClass()));
	JScrollPane spBestellungen = new JScrollPane();
	JComboBox cbFilter;
	JButton btAktualisieren = new JButton(Functions.getRefreshIcon(this.getClass()));
	ReportsTable tabBestellungen;

	public Reports(MainFrame frame) {
		this.frame = frame;
		try {
			jbInit();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
  
	private void jbInit() throws Exception {
	    
		this.setSize(800, 290);
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
	    
	    tabBestellungen = new ReportsTable();
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
	
			
		} else if(e.getActionCommand() == "refresh"){
			
		} else if(e.getActionCommand() == "dispose"){
			this.dispose();
		}
	}
}