package gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import applicationServer.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;


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
	JScrollPane spReport = new JScrollPane();
	JComboBox cbReportFilter;
	JButton btAktualisieren = new JButton(Functions.getRefreshIcon(this.getClass()));
	ReportsTable tabReport;
  JLabel labInstitut = new JLabel();
  JComboBox cbInstitut = new JComboBox();
  String[] items = {"Report_1", "Report_2", "Report_3", "Report_4", "Report_5", "Report_6", "Report_7", "Report_8"};
	String[] 	tooltips = {"<html><p>Ein Report, der die Konten der Zentralverwaltung <br>" +														"und die zugewiesenen Mittel, die Summe der <br>" +														"Ausgaben und aktuellen Kontostände enthält</p></html>",
												"<html><p>Ein Report, nder die Konten der Zentralverwaltung und die zugewiesenen Mittel , die Summe <br>" +														"der Ausgaben und Verteilungen, so dass der Dekan feststellen kann, wie viel Mittel kann er noch verteilen</p></html>", 
												"<html><p>Ein Report, der die institutsinternen Konten, die verteilten Mittel, die Summe <br>" +														"der Ausgaben und den aktuellen Kontostand enthält</p></html>", 
												"Ein Report, der die Ausgaben bei den institutsinternen Konten nach Verwaltungskonten sortiert",
												"<html><p>Ein Report von jedem Konto aus der Zentralverwaltung mit Ausgaben jedes Instituts <br>" +														"und aktueller Kontostände</p></html>",
												"Für jedes Institut ein Report mit Ausgaben sortiert nach Verwaltungskonen",
												"<html><p>Für jedes Instiut ein detaillierter Report über die Einnahmen und Ausgaben mit FBI-Schlüsselnummer, <br>" +														"Hüll-Nr, Datum, welches Konto der Zentralverwaltung belastet wurde, Firma, Beschreibung des bestellten Artikels, <br>" +														"Besteller, Nutzer, Festlegung bzw. Anordnung, Status der Bestellung</p></html>",
												"Für jedes Instiut ein Report über die Einnahmen"};

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
    this.setTitle("Reports");
    this.getContentPane().setLayout(null);
		
    cbReportFilter = new JComboBox(items);
    cbReportFilter.setBounds(new Rectangle(15, 12, 180, 27));
    cbReportFilter.setFont(new java.awt.Font("Dialog", 1, 11));
	  cbReportFilter.setRenderer(new MyComboBoxRenderer());
    
    btAktualisieren.setBounds(new Rectangle(410, 12, 113, 27));
    btAktualisieren.setFont(new java.awt.Font("Dialog", 1, 11));
    btAktualisieren.setText("Anzeigen");
    btAktualisieren.setActionCommand("refresh");
    btAktualisieren.addActionListener(this);

    tabReport = new ReportsTable();
    tabReport.setFont(new java.awt.Font("Dialog", 0, 11));

    spReport.setBounds(new Rectangle(15, 49,760, 150));
    labInstitut.setFont(new java.awt.Font("Dialog", 1, 11));
    labInstitut.setText("Institut:");
    labInstitut.setBounds(new Rectangle(211, 12, 61, 27));
    cbInstitut.setBounds(new Rectangle(258, 12, 129, 27));
    spReport.getViewport().add(tabReport, null);


    btBeenden.setBounds(new Rectangle(655, 209, 120, 27));
    btBeenden.setFont(new java.awt.Font("Dialog", 1, 11));
    btBeenden.setActionCommand("dispose");
    btBeenden.addActionListener(this);
    btBeenden.setText("Beenden");

    this.getContentPane().add(spReport, null);
    this.getContentPane().add(btBeenden, null);
    this.getContentPane().add(cbReportFilter, null);
    this.getContentPane().add(cbInstitut, null);
    this.getContentPane().add(labInstitut, null);
    this.getContentPane().add(btAktualisieren, null);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "showOrder"){


		} else if(e.getActionCommand() == "refresh"){

		} else if(e.getActionCommand() == "dispose"){
			this.dispose();
		}
	}
	
	public static void main(String[] args) {
		MainFrame test = new MainFrame("FBMittelverwaltung");
		Reports report;
		try{
			CentralServer server = (CentralServer)Naming.lookup("//localhost/mittelverwaltung");
			ApplicationServer applicationServer = server.getMyApplicationServer();
			test.setApplicationServer(applicationServer);
			PasswordEncrypt pe = new PasswordEncrypt();
			String psw = pe.encrypt(new String("a").toString());
			test.setBenutzer(applicationServer.login("test", psw));
			test.setBounds(100,100,800,900);
			test.setExtendedState(Frame.MAXIMIZED_BOTH);

			test.setJMenuBar( new MainMenu( test ) );
			
			report = new Reports(test);
			test.addChild(report);
			test.show();
			report.show();
	 }catch(Exception e){
			System.out.println(e);
	 }
  }
  
  class MyComboBoxRenderer extends BasicComboBoxRenderer {
	  public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		  if (isSelected) {
			  setBackground(list.getSelectionBackground());
			  setForeground(list.getSelectionForeground()); 
			  if (-1 < index) {
			  	list.setToolTipText(tooltips[index]);
			  }
		  } else {
			  setBackground(list.getBackground());
			  setForeground(list.getForeground());
		  } 
		  setFont(list.getFont());
		  setText((value == null) ? "" : value.toString()); 
		  
	  	return this;
	  } 
  }
}