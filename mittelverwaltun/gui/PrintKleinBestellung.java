package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.gui.base.PreviewInternalFrame;
import org.jfree.report.modules.parser.base.ReportGenerator;

import java.net.URL;
import java.text.*;
import java.util.*;

import dbObjects.*;


/**
 * Klasse zum Drucken der KleinBestellung = Auszahlungsanordnung
 * @author w.flat
 * 04.03.2005
 */
public class PrintKleinBestellung extends JFrame {
	
	KleinBestellung bestellung;
	MainFrame frame;
	DefaultTableModel dtm;
	
	public PrintKleinBestellung(KleinBestellung bestellung, MainFrame frame) {
		this.bestellung = bestellung;
		this.frame = frame;
		
		createTableModel();
		printReport();
	}
	
	/**
	 * Die Felder des Bestell-Formulars beschriften. 
	 */
	private void createOrder(JFreeReport report){
		// Den Text der Felder vorbereiten
		String kapitel = "";
		if(bestellung.getZvtitel() instanceof ZVTitel)
			kapitel = ((ZVTitel)bestellung.getZvtitel()).getZVKonto().getKapitel();
		else
			kapitel = bestellung.getZvtitel().getZVTitel().getZVKonto().getKapitel();
		String titel = bestellung.getZvtitel().getTitel();
		String untertitel = bestellung.getZvtitel().getUntertitel();
		Float summe = new Float(bestellung.getBestellwert());
		String euro = "" + summe.intValue();
		String cent = "" + (int)((summe.floatValue() - summe.intValue()) * 100);
		if(cent.length() <= 1)
			cent = "0" + cent;
		GregorianCalendar cal = new GregorianCalendar();
		cal.setGregorianChange(bestellung.getDatum());
		String jahr = "" + cal.get(Calendar.YEAR);
		
		report.setProperty("kapitel", kapitel);
		report.setPropertyMarked("kapitel", true);
		report.setProperty("titel", titel);
		report.setPropertyMarked("titel", true);
		report.setProperty("ut", untertitel);
		report.setPropertyMarked("ut", true);
		report.setProperty("projektNr", bestellung.getProjektNr());
		report.setPropertyMarked("projektNr", true);
		report.setProperty("gesamt", NumberFormat.getCurrencyInstance().format(bestellung.getBestellwert()));
		report.setPropertyMarked("gesamt", true);
		report.setProperty("verwendung", bestellung.getVerwendungszweck());
		report.setPropertyMarked("verwendung", true);
		report.setProperty("kartei", bestellung.getLabor());
		report.setPropertyMarked("kartei", true);
		report.setProperty("laborNr", bestellung.getKartei());
		report.setPropertyMarked("laborNr", true);
		report.setProperty("laborNr", bestellung.getKartei());
		report.setPropertyMarked("laborNr", true);
		report.setProperty("datum", DateFormat.getDateInstance().format(bestellung.getDatum()));
		report.setPropertyMarked("datum", true);
		report.setProperty("auszahlung", bestellung.getAuftraggeber().toBestellString());
		report.setPropertyMarked("auszahlung", true);
		report.setProperty("titlVerzNr", bestellung.getVerzeichnis());
		report.setPropertyMarked("titlVerzNr", true);
		report.setProperty("jahr", jahr);
		report.setPropertyMarked("jahr", true);
		report.setProperty("euro", euro);
		report.setPropertyMarked("euro", true);
		report.setProperty("cent", cent);
		report.setPropertyMarked("cent", true);
		
	}
	
	/**
	 * Die Tabelle der Belege generieren. 
	 */
	private void createTableModel(){
		// Die Namen der Spalten 
		String[] cols = {"Beleg-Nr.", "Firma", "Artikel, Gegenstand", "Preis"};
		// Daten-Array : Zeilen = Anzahl der Belege, Spalten = Anzahl der Spaltennamen
		String[][] data = new String[bestellung.getBelege().size()][cols.length];
		
		// Das Daten-Array fühlen
		Beleg beleg;
		for(int i = 0; i < bestellung.getBelege().size(); i++){
			beleg = (Beleg)(bestellung.getBelege().get(i));
			data[i][0] = "" + beleg.getNummer();
			data[i][1] = beleg.getFirma().toString();
			data[i][2] = beleg.getArtikel();
			data[i][3] = NumberFormat.getCurrencyInstance().format(beleg.getSumme());
		}
		
		// Das TableModel definieren. Die Spalten sind nicht editierbar und haben alle die String-Klasse
		dtm = new DefaultTableModel(data, cols) {
			public boolean isCellEditable(int rowIndex, int columnIndex){
				return false;
			}
			public Class getColumnClass(int col)  {
				return String.class;
			}
		};


	}

	/**
	* Reads the report from the specified template file.
	* @param templateURL  the template location.
	*
	* @return a report.
	*/
  private JFreeReport parseReport(final URL templateURL)
  {

	 JFreeReport result = null;
	 final ReportGenerator generator = ReportGenerator.getInstance();
	 try
	 {
	 	result = generator.parseReport(templateURL);
	 }
	 catch (Exception e)
	 {
		System.out.println("Failed to parse the report definition");
	 }
	 return result;

  }
	
	private void printReport(){
		final URL in = getClass().getResource("/xml/kleinBestellung.xml");
		
		if (in == null){
			System.out.println("xml not found");
			return;
		}
		JCheckBox test = new JCheckBox();
		test.setSelected(true);
		test.setBackground(Color.WHITE);

		JFreeReport report = new JFreeReport();

		report = parseReport(in);
		report.setData(dtm);
		createOrder(report);
	
		try {
			final PreviewInternalFrame preview = new PreviewInternalFrame(report);
			preview.getBase().setToolbarFloatable(true);
			preview.setPreferredSize(new Dimension(700,600));
			preview.setClosable(true);
			preview.setResizable(true);
			preview.pack();
			frame.addChild(preview);
			preview.setVisible(true);

		} catch (ReportProcessingException e1) {
			e1.printStackTrace();
		}
	}
	

}
