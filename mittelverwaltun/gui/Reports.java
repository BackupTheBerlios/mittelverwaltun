package gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import dbObjects.Institut;
import dbObjects.ZVKonto;

import applicationServer.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.Naming;
import java.util.ArrayList;


/**
 * <p>Title: Reports - GUI</p>
 * <p>Description: Frame zum Anzeigen verschiedener Reports</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Robert
 * @version 1.0
 */
public class Reports extends JInternalFrame implements ActionListener, ItemListener{

	/**
	 * Ein Report, der die Konten der Zentralverwaltung und die zugewiesenen Mittel, die Summe
	 * der Ausgaben und aktuellen Kontostände enthält
	 */
	public static final int REPORT_1 = 1;

	/**
	 * Ein Report, nder die Konten der Zentralverwaltung und die zugewiesenen Mittel , die Summe
	 * der Ausgaben und Verteilungen, so dass der Dekan feststellen kann, wie viel Mittel kann er noch verteilen
	 */
	public static final int REPORT_2 = 2;

	/**
	 * Ein Report, der die institutsinternen Konten, die verteilten Mittel, die Summe
	 * der Ausgaben und den aktuellen Kontostand enthält
	 */
	public static final int REPORT_3 = 3;

	/**
	 * Ein Report, der die Ausgaben bei den institutsinternen Konten nach Verwaltungskonten sortiert
	 */
	public static final int REPORT_4 = 4;

	/**
	 * Ein Report von jedem Konto aus der Zentralverwaltung mit Ausgaben jedes Instituts
	 * und aktueller Kontostände
	 */
	public static final int REPORT_5 = 5;

	/**
	 * Für jedes Institut ein Report mit Ausgaben sortiert nach Verwaltungskonten
	 */
	public static final int REPORT_6 = 6;

	/**
	 * Für jedes Instiut ein detaillierter Report über die Ausgaben mit FBI-Schlüsselnummer,
	 * Hüll-Nr, Datum, welches Konto der Zentralverwaltung belastet wurde, Firma, Beschreibung des bestellten Artikels,
	 * Besteller, Nutzer, Festlegung bzw. Anordnung, Status der Bestellung
	 */
	public static final int REPORT_7 = 7;

	/**
	 * Für jedes Instiut ein Report über die Einnahmen
	 * FB-Konto, Einnahmen
	 */
	public static final int REPORT_8 = 8;

	MainFrame frame;

	JButton btBeenden = new JButton(Functions.getCloseIcon(this.getClass()));
	JScrollPane spReport = new JScrollPane();
	JComboBox cbReportFilter;
	JButton btAktualisieren = new JButton(Functions.getFindIcon(this.getClass()));
	ReportsTable tabReport;
  JLabel labInstitut = new JLabel();
  JComboBox cbZVKonten = new JComboBox();
  String[] items = {"Report_1", "Report_2", "Report_3", "Report_4", "Report_5", "Report_6", "Report_7", "Report_8"};
	String[] 	tooltips = {"<html><p>Ein Report, der die Konten der Zentralverwaltung <br>" +
														"und die zugewiesenen Mittel, die Summe der <br>" +
														"Ausgaben und aktuellen Kontostände enthält</p></html>",
												"<html><p>Ein Report, nder die Konten der Zentralverwaltung und die zugewiesenen Mittel , die Summe <br>" +
														"der Ausgaben und Verteilungen, so dass der Dekan feststellen kann, wie viel Mittel kann er noch verteilen</p></html>",
												"<html><p>Ein Report, der die institutsinternen Konten, die verteilten Mittel, die Summe <br>" +
														"der Ausgaben und den aktuellen Kontostand enthält</p></html>",
												"Ein Report, der die Ausgaben bei den institutsinternen Konten nach Verwaltungskonten sortiert",
												"<html><p>Ein Report von jedem Konto aus der Zentralverwaltung mit Ausgaben jedes Instituts <br>" +
														"und aktueller Kontostände</p></html>",
												"Für jedes Institut ein Report mit Ausgaben sortiert nach Verwaltungskonen",
												"<html><p>Für jedes Instiut ein detaillierter Report über die Ausgaben mit FBI-Schlüsselnummer, <br>" +
														"Hüll-Nr, Datum, welches Konto der Zentralverwaltung belastet wurde, Firma, Beschreibung des bestellten Artikels, <br>" +
														"Besteller, Nutzer, Festlegung bzw. Anordnung, Status der Bestellung</p></html>",
												"Für jedes Instiut ein Report über die Einnahmen"};
  JButton btDrucken = new JButton(Functions.getPrintIcon(this.getClass()));
  JComboBox cbInstitut = new JComboBox();
  String filter = "";
  JButton buReportInfo = new JButton(Functions.getInformation24Icon(this.getClass()));

	public Reports(MainFrame frame) {
		this.frame = frame;
		try {
			jbInit();
		}catch(Exception e) {
			e.printStackTrace();
		}
		loadInstituts();
		loadZVKonten();
	}

	private void jbInit() throws Exception {

		this.setSize(800, 290);
		this.setFrameIcon(null);
    this.setTitle("Reports");
    this.getContentPane().setLayout(null);

    cbReportFilter = new JComboBox(items);
    cbReportFilter.setBounds(new Rectangle(15, 12, 91, 27));
    cbReportFilter.setFont(new java.awt.Font("Dialog", 1, 11));
	  cbReportFilter.setRenderer(new MyComboBoxRenderer());
	  cbReportFilter.setToolTipText(tooltips[0]);
	  cbReportFilter.setActionCommand("selectReport");
	  cbReportFilter.addItemListener(this);

    btAktualisieren.setBounds(new Rectangle(658, 12, 113, 27));
    btAktualisieren.setFont(new java.awt.Font("Dialog", 1, 11));
    btAktualisieren.setText("Anzeigen");
    btAktualisieren.setActionCommand("showReport");
    btAktualisieren.addActionListener(this);

    tabReport = new ReportsTable(this);
    tabReport.setFont(new java.awt.Font("Dialog", 0, 11));

    spReport.setBounds(new Rectangle(15, 49,760, 150));
    labInstitut.setFont(new java.awt.Font("Dialog", 1, 11));
    labInstitut.setText("Institut:");
    labInstitut.setBounds(new Rectangle(211, 12, 61, 27));
    labInstitut.setVisible(false);
		cbZVKonten.setBounds(new Rectangle(278, 12, 363, 27));
		cbZVKonten.setVisible(false);
		cbZVKonten.setActionCommand("selectZVKonto");
		cbZVKonten.addItemListener(this);
		btDrucken.setText("Drucken");
    btDrucken.addActionListener(this);
    btDrucken.setActionCommand("dispose");
    btDrucken.setFont(new java.awt.Font("Dialog", 1, 11));
    btDrucken.setBounds(new Rectangle(512, 209, 120, 27));


    btBeenden.setBounds(new Rectangle(655, 209, 120, 27));
    btBeenden.setFont(new java.awt.Font("Dialog", 1, 11));
    btBeenden.setActionCommand("dispose");
    btBeenden.addActionListener(this);
    btBeenden.setText("Beenden");

    cbInstitut.setBounds(new Rectangle(278, 12, 363, 27));
    cbInstitut.setVisible(false);
	 	cbInstitut.setActionCommand("selectInstitut");
	 	cbInstitut.addItemListener(this);
    buReportInfo.setBounds(new Rectangle(110, 12, 28, 27));
    buReportInfo.setText("");
	 	buReportInfo.setActionCommand("showReportInfo");
		buReportInfo.addActionListener(this);

    this.getContentPane().add(spReport, null);
    spReport.getViewport().add(tabReport, null);
    this.getContentPane().add(btBeenden, null);
	 	this.getContentPane().add(cbZVKonten, null);
    this.getContentPane().add(labInstitut, null);
    this.getContentPane().add(btDrucken, null);
    this.getContentPane().add(btAktualisieren, null);
    this.getContentPane().add(cbInstitut, null);
    this.getContentPane().add(cbReportFilter, null);
    this.getContentPane().add(buReportInfo, null);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "showOrder"){
			if (tabReport.getSelectedOrderType()==OrderTable.STD_TYP){
				if(tabReport.getSelectedOrderPhase()!=OrderTable.SONDIERUNG)
					try {
						frame.addChild( new AbwicklungBestellungNormal( frame , frame.applicationServer.getStandardBestellung(tabReport.getSelectedOrderID())));
					} catch (ApplicationServerException exception) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", exception.getMessage(), exception.getNestedMessage(), MessageDialogs.ERROR_ICON);
					}
			}else if (tabReport.getSelectedOrderType()==OrderTable.ASK_TYP){
				if(tabReport.getSelectedOrderPhase()!=OrderTable.SONDIERUNG)
					try {
						frame.addChild( new AbwicklungBestellungASK( frame , frame.applicationServer.getASKBestellung(tabReport.getSelectedOrderID())));
					} catch (ApplicationServerException exception) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", exception.getMessage(), exception.getNestedMessage(), MessageDialogs.ERROR_ICON);
					}
			}else if (tabReport.getSelectedOrderType()==OrderTable.ZA_TYP){
				if(tabReport.getSelectedOrderPhase()==OrderTable.ABGESCHLOSSEN)
					try {
						frame.addChild( new BestellungKlein( frame , frame.applicationServer.getKleinbestellung(tabReport.getSelectedOrderID())));
					} catch (ApplicationServerException exception) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", exception.getMessage(), exception.getNestedMessage(), MessageDialogs.ERROR_ICON);
					}
			}
		} else if(e.getActionCommand() == "showReport"){
			try {
				if(cbReportFilter.getSelectedItem() == "Report_3"){
							ArrayList content = frame.getApplicationServer().getReport(REPORT_3);
							tabReport.fillReport(REPORT_3, filter, content);
				} else if(cbReportFilter.getSelectedItem() == "Report_4"){
							ArrayList content = frame.getApplicationServer().getReport(REPORT_4);
							tabReport.fillReport(REPORT_4, "", content);
				} else if(cbReportFilter.getSelectedItem() == "Report_5"){
					if(cbZVKonten.getSelectedItem() != null){
							ArrayList content = frame.getApplicationServer().getReport(REPORT_5);
							tabReport.fillReport(REPORT_5, filter, content);
					}
				} else if(cbReportFilter.getSelectedItem() == "Report_6"){
					if(cbInstitut.getSelectedItem() != null){
							ArrayList content = frame.getApplicationServer().getReport(REPORT_6);
							tabReport.fillReport(REPORT_6, filter, content);
					}
				} else if(cbReportFilter.getSelectedItem() == "Report_7"){
					if(cbInstitut.getSelectedItem() != null){
							ArrayList content = frame.getApplicationServer().getReport(REPORT_7);
							tabReport.fillReport(REPORT_7, filter, content);
					}
				} else if(cbReportFilter.getSelectedItem() == "Report_8"){
					if(cbInstitut.getSelectedItem() != null){
							ArrayList content = frame.getApplicationServer().getReport(REPORT_8);
							tabReport.fillReport(REPORT_8, filter, content);
					}
				}
			} catch (ApplicationServerException exception) {
				MessageDialogs.showDetailMessageDialog(this, "Fehler", exception.getMessage(), exception.getNestedMessage(), MessageDialogs.ERROR_ICON);
			}
		} else if(e.getActionCommand() == "showReportInfo"){
			;
		} else if(e.getActionCommand() == "dispose"){
			this.dispose();
		}
	}

	private void loadInstituts(){
	  try {
//		TODO Admin durch die Aktivität austauschen
			if(frame.getBenutzer().getRolle().getBezeichnung().equals("Admin")){
				Institut[] instituts = frame.getApplicationServer().getInstitutes();

			  if(instituts != null){
				  cbInstitut.removeAllItems();
				  Institut institut = new Institut(0, "alle", "00000");
				  cbInstitut.addItem(institut);
				  for(int i = 0; i < instituts.length; i++){
					  cbInstitut.addItem(instituts[i]);
				  }
			  }
			}else{
				Institut institut = frame.getBenutzer().getKostenstelle();

				cbInstitut.removeAllItems();
				cbInstitut.addItem(institut);
			}
			labInstitut.setText("Institute");
	  } catch (ApplicationServerException e) {
		  e.printStackTrace();
	  }
  }

  private void loadZVKonten(){
		try {
			ArrayList zvKonten = frame.getApplicationServer().getZVKonten();
			  if(zvKonten.size() > 0){
				  cbZVKonten.removeAllItems();
				  ZVKonto zvKonto = new ZVKonto("alle", "", "", 0);
				  cbZVKonten.addItem(zvKonto);
					for(int i = 0; i < zvKonten.size(); i++){
						cbZVKonten.addItem((ZVKonto)zvKonten.get(i));
					}
			 }
			 labInstitut.setText("ZVKonto");
		} catch (ApplicationServerException e) {
			e.printStackTrace();
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
			String psw = pe.encrypt(new String("r.driesner").toString());
			test.setBenutzer(applicationServer.login("r.driesner", psw));
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

	public void itemStateChanged(ItemEvent e) {
		String r = (String)cbReportFilter.getSelectedItem();
		int report = 0;

		if(r == "Report_1"){
			cbReportFilter.setToolTipText(tooltips[0]);
			report = REPORT_1;
		}else if(r == "Report_2"){
			cbReportFilter.setToolTipText(tooltips[1]);
			report = REPORT_2;
		}else if(r == "Report_3"){
			cbReportFilter.setToolTipText(tooltips[2]);
			report = REPORT_3;
		}else if(r == "Report_4"){
			cbReportFilter.setToolTipText(tooltips[3]);
			report = REPORT_4;
		}else if(r == "Report_5"){	
			cbReportFilter.setToolTipText(tooltips[4]);
			report = REPORT_5;
		}else if(r == "Report_6"){
			cbReportFilter.setToolTipText(tooltips[5]);
			report = REPORT_6;
		}else if(r == "Report_7"){
			cbReportFilter.setToolTipText(tooltips[6]);
			report = REPORT_7;
		}else if(r == "Report_8"){
			cbReportFilter.setToolTipText(tooltips[7]);
			report = REPORT_8;
		}
		
		if(e.getSource() == cbReportFilter){
			tabReport.fillReport(0,"", new ArrayList());
			filter = "";
			if(r == "Report_3" || r == "Report_6" || r == "Report_7" || r == "Report_8"){
				labInstitut.setText("Institute");
				labInstitut.setVisible(true);
				cbInstitut.setVisible(true);
				cbZVKonten.setVisible(false);
			}else if(r == "Report_5"){
				labInstitut.setText("ZVKonto");
				labInstitut.setVisible(true);
				cbZVKonten.setVisible(true);
				cbInstitut.setVisible(false);
			}else{
				labInstitut.setVisible(false);
				cbInstitut.setVisible(false);
			}
		}else if(e.getSource() == cbInstitut){
			Institut i = (Institut)cbInstitut.getSelectedItem();

			if(i.getBezeichnung().equals("alle"))
				this.filter = "";
			else
				this.filter = i.getBezeichnung();

			if(report == tabReport.getReportType())
				tabReport.filterView(this.filter);

		}else if(e.getSource() == cbZVKonten){
			ZVKonto zvk = (ZVKonto)cbZVKonten.getSelectedItem();

			if(zvk.getBezeichnung().equals("alle"))
				this.filter = "";
			else
				this.filter = zvk.getBezeichnung();

			if(report == tabReport.getReportType())
				tabReport.filterView(this.filter);
		}
	}
}