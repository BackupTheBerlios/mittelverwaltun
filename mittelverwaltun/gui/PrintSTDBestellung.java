package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.modules.gui.base.PreviewInternalFrame;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.report.util.WaitingImageObserver;

import applicationServer.ApplicationServerException;

import dbObjects.*;

import java.awt.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


public class PrintSTDBestellung extends JFrame /* implements Printable */ {
  
  StandardBestellung order;
  MainFrame frame;
  DefaultTableModel dtm;

  public PrintSTDBestellung(StandardBestellung order, MainFrame frame) {
	this.order = order;
	this.frame = frame;
  	
	 createTableModel(order.getAngebote());
	 printReport();
  }


  private void createTableModel(ArrayList angebote){
		ArrayList positionen = ((Angebot)order.getAngebote().get(order.getAngenommenesAngebot())).getPositionen();
		
		String[] cols = {"Menge", "Artikel/Bestellnummer", "Einzelpreis", "MwSt", "Rabatt", "Gesamtpreis"};
		Object[][] data = new Object[positionen.size()][cols.length];
	
		for(int i = 0; i < positionen.size(); i++){
		  Position p = (Position)positionen.get(i);
			data[i][0] = new Integer(p.getMenge());
			data[i][1] = p.getArtikel();
			data[i][2] = NumberFormat.getCurrencyInstance().format(p.getEinzelPreis());
			data[i][3] = NumberFormat.getPercentInstance().format(p.getMwst());
			data[i][4] = NumberFormat.getCurrencyInstance().format(p.getRabatt());
			data[i][5] = NumberFormat.getCurrencyInstance().format(p.getGesamtpreis());
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


  private void createOrder(JFreeReport report){
		report.setProperty("bestNr", order.getReferenznr());
		report.setPropertyMarked("bestNr", true);
		report.setProperty("koSt", order.getFbkonto().getInstitut().getKostenstelle());
		report.setPropertyMarked("koSt", true);
		report.setProperty("kostenstelle", order.getFbkonto().getInstitut().getBezeichnung());
		report.setPropertyMarked("kostenstelle", true);
		report.setProperty("investitionen", " ");
		report.setPropertyMarked("investitionen", true);
		report.setProperty("reparatur", " ");
		report.setPropertyMarked("reparatur", true);
		report.setProperty("verbrauchsmaterial", " ");
		report.setPropertyMarked("verbrauchsmaterial", true);
		
	  // Kostenart
	  switch(order.getKostenart().getId()){
		  case 1: {
								report.setProperty("investitionen", "X");
							  break;
						  }
		  case 2: {
								report.setProperty("reparatur", "X");
							  break;
						  }
		  case 3: {
								report.setProperty("verbrauchsmaterial", "X");
							  break;
						  }
	  }

	  // Haushaltstitel
		report.setProperty("kapitel", order.getZvtitel().getZVTitel() != null ? order.getZvtitel().getZVTitel().getZVKonto().getKapitel() : ((ZVTitel)order.getZvtitel()).getZVKonto().getKapitel());
		report.setPropertyMarked("kapitel", true);
		report.setProperty("titel", order.getZvtitel().getTitel());
		report.setPropertyMarked("titel", true);
		report.setProperty("ut", order.getZvtitel().getUntertitel());
		report.setPropertyMarked("ut", true);

	  // Erstbeschaffung
		report.setProperty("erstbeschaffung", " ");
		report.setPropertyMarked("erstbeschaffung", true);
		report.setProperty("ersatzX", " ");
		report.setPropertyMarked("ersatzX", true);
		
	  if(!order.getErsatzbeschaffung()){
			report.setProperty("erstbeschaffung", "X");
	  }else{
			report.setProperty("ersatzX", "X");
			report.setProperty("ersatz", order.getErsatzbeschreibung());
			report.setPropertyMarked("ersatz", true);
			report.setProperty("inventarNr", order.getInventarNr());
			report.setPropertyMarked("inventarNr", true);
	  }

	  // Begründung
		report.setProperty("begruendung", order.getVerwendungszweck());
		report.setPropertyMarked("begruendung", true);
		report.setProperty("drittMittel", " ");
		report.setPropertyMarked("drittMittel", true);
		
		if(order.getPlanvorgabe())
			report.setProperty("drittMittel", "X");

	  // Auftrag
		for(int i = 0; i < 6; i++){
			
			if(i < order.getAngebote().size()){
				Angebot a = (Angebot)order.getAngebote().get(i);
				
				report.setProperty("nr" + (1+i), "" + (i+1));
				report.setProperty("firma" + (1+i), a.getAnbieter().toString());
				report.setProperty("angebot" + (1+i), DateFormat.getDateInstance().format(a.getDatum()));
				report.setProperty("summe" + (1+i), NumberFormat.getCurrencyInstance().format(a.getSumme()));
			}else{
				report.setProperty("nr" + (1+i), " ");
				report.setProperty("firma" + (1+i), " ");
				report.setProperty("angebot" + (1+i), " ");
				report.setProperty("summe" + (1+i), " ");
			}
			
			report.setPropertyMarked("nr" + (1+i), true);
			report.setPropertyMarked("firma" + (1+i), true);
			report.setPropertyMarked("angebot" + (1+i), true);
			report.setPropertyMarked("summe" + (1+i), true);
		 }
		 
		report.setProperty("auftragNr", "" + (1 + order.getAngenommenesAngebot()));
		report.setPropertyMarked("auftragNr", true);

	  // Entscheidung für
		report.setProperty("preisX", " ");
		report.setPropertyMarked("preisX", true);
		report.setProperty("grundX", " ");
		report.setPropertyMarked("grundX", true);
		report.setProperty("grund", " ");
		report.setPropertyMarked("grund", true);
		
		if(order.getBegruendung().equals(""))
			report.setProperty("preisX", "X");
	  else{
			report.setProperty("grundX", "X");
			report.setProperty("grund", "aus folgenden Grund bevorzugt wurden: " + order.getBegruendung());
	  }

	  //Rest
		report.setProperty("datum",  DateFormat.getDateInstance().format(order.getDatum()));
		report.setPropertyMarked("datum", true);
		report.setProperty("name",  order.getAuftraggeber().getName());
		report.setPropertyMarked("name", true);

		// Bestellungsseite
		//	FH-Logo einfügen
		final URL imageURL = getClass().getResource("../image/fhlogoklein.gif");
		final Image image = Toolkit.getDefaultToolkit().createImage(imageURL);
		final WaitingImageObserver obs = new WaitingImageObserver(image);
		obs.waitImageLoaded();
		report.setProperty("logo", image);
		report.setPropertyMarked("logo", true);
		
		Angebot a = (Angebot)order.getAngebote().get(order.getAngenommenesAngebot());


		// Adresse
		report.setProperty("adresse", a.getAnbieter().getName() + "\n" +
																	a.getAnbieter().getStrasseNr() + "\n\n" +
																	a.getAnbieter().getPlz() + " " + a.getAnbieter().getOrt());
		report.setPropertyMarked("adresse", true);
		
		Fachbereich[] fbs = null;
		try {
			fbs = frame.getApplicationServer().getFachbereiche();
			// Besteller
			Benutzer il = order.getFbkonto().getInstitut().getInstitutsleiter(); // Institutsleiter
			report.setProperty("besteller", fbs[0].getFbBezeichnung() + "\n" +
																			order.getFbkonto().getInstitut().getBezeichnung() + "\n" +
																			il.getTitel() + " " + il.getVorname() + " " + il.getName() + "\n\n" +
																			"Tel.:    (0621) " + il.getTelefon() + "\n" +
																			"Fax:            " + il.getFax() + "\n" +
																			"Datum:    " + DateFormat.getDateInstance().format(order.getDatum()) + "\n" +
																			"UstldNr.: DE811630438");
			report.setPropertyMarked("besteller", true);
			
		} catch (ApplicationServerException e) {
			e.printStackTrace();
		} 

	  // BestellNr.
		report.setProperty("bestellNr", order.getReferenznr());
		report.setPropertyMarked("bestellNr", true);
		
	  // Bestelldaten

	  // Gesamtsummen
		report.setProperty("netto", NumberFormat.getCurrencyInstance().format((order.getBestellwert() - order.get7PercentSum() - order.get16PercentSum())));
		report.setPropertyMarked("netto", true);
		report.setProperty("mwst7", NumberFormat.getCurrencyInstance().format(order.get7PercentSum()));
		report.setPropertyMarked("mwst7", true);
		report.setProperty("mwst16", NumberFormat.getCurrencyInstance().format(order.get16PercentSum()));
		report.setPropertyMarked("mwst16", true);
		report.setProperty("gesamt", NumberFormat.getCurrencyInstance().format(order.getBestellwert()));
		report.setPropertyMarked("gesamt", true);
		
	  // Bemerkungen
		report.setProperty("bemerkungen", order.getBemerkung());
		report.setPropertyMarked("bemerkungen", true);
		
	  // Lieferanschrift
		report.setProperty("lieferanschrift", fbs[0].getFhBezeichnung() + "\n" +
																					fbs[0].getFbBezeichnung() + "\n" +
																					"   z.Hd. " + order.getEmpfaenger().getVorname() + " " + order.getEmpfaenger().getName() + "\n" +
																					"   Bau " + order.getEmpfaenger().getBau() + ", " + "Raum " + order.getEmpfaenger().getRaum() + "\n" +
																					fbs[0].getStrasseHausNr() + "\n" +
																					fbs[0].getPlzOrt());
		report.setPropertyMarked("lieferanschrift", true);
		
  // Kostenstelle
		report.setProperty("kostenstelle1", "KostSt.:" + "\n" +
																				"KostSt-Nr.:" + "\n" +
																				"Kap/Tit/Ut:" + "\n" +
																				"InstitKo.:" + "\n" +
																				"InstitKo-Nr.:" + "\n" +
																				"FBI-Nr.:" + "\n" +
																				"Hül-Nr.:");
		report.setPropertyMarked("kostenstelle1", true);
		report.setProperty("kostenstelle2", order.getFbkonto().getInstitut().getBezeichnung() + "\n" +
																				order.getFbkonto().getInstitut().getKostenstelle() + "\n" +
																				(order.getZvtitel().getZVTitel() != null ? order.getZvtitel().getZVTitel().getZVKonto().getKapitel() : ((ZVTitel)order.getZvtitel()).getZVKonto().getKapitel()) + "/" +
																													 order.getZvtitel().getTitel() + "/" +
																													 order.getZvtitel().getUntertitel() + "\n" +
																				order.getFbkonto().getBezeichnung() + "\n" +
																				order.getFbkonto().getInstitut().getKostenstelle() + " " +
																													 order.getFbkonto().getHauptkonto() + " " +
																													 order.getFbkonto().getUnterkonto() + "\n" +
																				order.getReferenznr() + "\n" +
																				order.getHuel());
		report.setPropertyMarked("kostenstelle2", true);
  }
  
  private void printReport(){
	  final URL in = getClass().getResource("/xml/standardBestellung.xml");
//	  System.setProperty("org.jfree.report.targets.G2OutputTarget.isBuggyFRC","true");
		
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
}