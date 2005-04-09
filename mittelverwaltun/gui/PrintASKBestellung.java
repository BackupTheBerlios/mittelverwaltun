package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.gui.base.PreviewInternalFrame;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.util.WaitingImageObserver;

import applicationServer.ApplicationServer;
import applicationServer.ApplicationServerException;

import dbObjects.ASKBestellung;
import dbObjects.Fachbereich;
import dbObjects.Position;
import dbObjects.ZVTitel;

import java.awt.*;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.rmi.*;


public class PrintASKBestellung extends JFrame {
  ASKBestellung order;
  ApplicationServer as;
  MainFrame frame;
  DefaultTableModel dtm;

  public PrintASKBestellung(ASKBestellung order, MainFrame frame) {
  	this.order = order;
  	this.frame = frame;
  	this.as = frame.getApplicationServer();
  	
		createTableModel(order.getAngebot().getPositionen());
		printReport();
  }
  
	/**
	 * erstellt das TableModel für den Report
	 * @param positions - Positionen
	 */
  private void createTableModel(ArrayList positions){
		String[] cols = {"Menge", "Produkt", "Einzelpreis", "Gesamtpreis", "für Institut"};
		Object[][] data = new Object[positions.size()][cols.length];

		for(int i = 0; i < positions.size(); i++){
			Position p = (Position)positions.get(i);

			data[i][0] = new Integer(p.getMenge());
			data[i][1] = p.getArtikel();
			data[i][2] = NumberFormat.getCurrencyInstance().format(p.getEinzelPreis() + (p.getEinzelPreis() * p.getMwst()));
			data[i][3] = NumberFormat.getCurrencyInstance().format(p.getGesamtpreis());
			data[i][4] = p.getInstitut().getBezeichnung();
		}
	  dtm = new DefaultTableModel(data, cols){
				public boolean isCellEditable(int rowIndex, int columnIndex){
					  return false;
				}

				public Class getColumnClass(int col)  {
					return getValueAt(0, col).getClass();
				}
			};
	}
	
	/**
	* Reads the report from the specified template file.
	*
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
		final URL in = getClass().getResource("/xml/askBestellung.xml");
//		System.setProperty("org.jfree.report.targets.G2OutputTarget.isBuggyFRC","true");
		
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
	
	private void createOrder(JFreeReport report){
		// FH-Logo einfügen
		final URL imageURL = getClass().getResource("/image/fhlogoklein.gif");
		final Image image = Toolkit.getDefaultToolkit().createImage(imageURL);
		final WaitingImageObserver obs = new WaitingImageObserver(image);
		obs.waitImageLoaded();
		report.setProperty("logo", image);
		report.setPropertyMarked("logo", true);
		
		try {
			// FH-Anschrift
			Fachbereich[] fbs = as.getFachbereiche();
			report.setProperty("fhAnschrift", fbs[0].getFbBezeichnung() + "\n" +
																				fbs[0].getStrasseHausNr() + "\n" +
																				fbs[0].getPlzOrt());
			report.setPropertyMarked("fhAnschrift", true);
			
			// Firmen Name
			report.setProperty("firma", order.getAngebot().getAnbieter().getName());
			report.setPropertyMarked("firma", true);
			
			// Firmen-Anschrift
			report.setProperty("firmaAnschrift", order.getAngebot().getAnbieter().getStrasseNr() + "\n" +
																					 order.getAngebot().getAnbieter().getPlz() + " " + 
																					 order.getAngebot().getAnbieter().getOrt() + "\n" +
																					 "Fax: " + order.getAngebot().getAnbieter().getFaxNr());
			report.setPropertyMarked("firmaAnschrift", true);
		
			report.setProperty("kostenstelle", order.getFbkonto().getInstitut().getBezeichnung());
			report.setPropertyMarked("kostenstelle", true);
			report.setProperty("kostenstelleNr", order.getFbkonto().getInstitut().getKostenstelle());
			report.setPropertyMarked("kostenstelleNr", true);
			report.setProperty("swBeauftragter", order.getSwbeauftragter().getName() + ", " + order.getSwbeauftragter().getVorname());
			report.setPropertyMarked("swBeauftragter", true);
			report.setProperty("kapitel", order.getZvtitel().getZVTitel() != null ? order.getZvtitel().getZVTitel().getZVKonto().getKapitel() : ((ZVTitel)order.getZvtitel()).getZVKonto().getKapitel());
			report.setPropertyMarked("kapitel", true);
			report.setProperty("titel", order.getZvtitel().getTitel());
			report.setPropertyMarked("titel", true);
			report.setProperty("ut", order.getZvtitel().getUntertitel());
			report.setPropertyMarked("ut", true);
			report.setProperty("auftragsNr", order.getReferenznr());
			report.setPropertyMarked("auftragsNr", true);
			
		} catch (ApplicationServerException e) {
			e.printStackTrace();
		} catch(RemoteException re) {
			re.printStackTrace();
		}
	}
}