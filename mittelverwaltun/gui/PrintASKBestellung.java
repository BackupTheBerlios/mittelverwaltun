package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.net.URL;
import java.util.ArrayList;


public class PrintASKBestellung extends JFrame /*implements Printable*/ {
  JLabel jLabel4 = new JLabel();
  JLabel labKostenstelle = new JLabel();
  JLabel labKoSt = new JLabel();
  JLabel jLabel8 = new JLabel();
  JLabel labUT = new JLabel();
  JLabel labTitel = new JLabel();
  JTextPane tpFirmaAnschrift = new JTextPane();
  JLabel jLabel1 = new JLabel();
  JTextPane tpAnschrift = new JTextPane();
  JLabel labKapitel = new JLabel();
  JLabel jLabel7 = new JLabel();
  JPanel oben = new JPanel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JLabel labSoftwarebeauftragter = new JLabel();
  JLabel jLabel9 = new JLabel();
  JLabel jLabel10 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel111 = new JLabel();
  JLabel jLabel19 = new JLabel();
  JLabel jLabel12 = new JLabel();
  JPanel unten = new JPanel();
  JLabel labUnterschrift1 = new JLabel();
  JLabel labDatum1 = new JLabel();
  JLabel jLabel16 = new JLabel();
  JLabel jLabel11 = new JLabel();
  JLabel jLabel13 = new JLabel();
  JLabel jLabel18 = new JLabel();
  JLabel jLabel17 = new JLabel();
  JLabel labDatum = new JLabel();
  JLabel jLabel14 = new JLabel();
  JLabel jLabel110 = new JLabel();
  JTextPane jTextPane3 = new JTextPane();
  JPanel jPanel2 = new JPanel();
  JLabel jLabel15 = new JLabel();
  JLabel labUnterschrift = new JLabel();
  JTable tableBestellung = new JTable();
	JScrollPane jScrollPane1 = new JScrollPane(tableBestellung);
  JPanel printPanel = new JPanel();
  ASKBestellung order;
  ApplicationServer as;
  MainFrame frame;
  JLabel labFirma = new JLabel();
  JLabel labAuftragsNr = new JLabel();

  public PrintASKBestellung(ASKBestellung order, MainFrame frame) {
  	this.order = order;
  	this.frame = frame;
  	this.as = frame.getApplicationServer();
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
		createTable(order.getAngebot().getPositionen());
//		show();
//		setVisible(false);
		
//		PrinterJob pJob = PrinterJob.getPrinterJob();
//
//	  PageFormat pf = new PageFormat();
//	  Paper paper = pf.getPaper() ;
//	  paper.setImageableArea(25,30,560,800) ;
//	  paper.setSize(595,842);
//	  pf.setPaper(paper);
//
//	  pJob.setJobName("ASK Bestellung");
//	  if(pJob.printDialog()){
//		  try{
//			  pJob.setPrintable(this, pf);
//			  pJob.print();
//		  }catch(PrinterException pexc){
//			  System.out.println("Fehler beim Drucken");
//		  }
//	  }
//	  pJob.cancel();
//	  if(pJob.isCancelled()){
//			this.dispose();
//	  }

		printReport();
  }

  private void jbInit() throws Exception {
		oben = new JPanel(){
			public void paintComponent(Graphics g) {
			  super.paintComponent(g);
			  g.drawLine(95, 203, 193, 203);
				g.drawLine(311, 203, 541, 203);
				g.drawLine(146, 229, 307, 229);
				g.drawLine(224, 255, 273, 255);
				g.drawLine(323, 255, 380, 255);
				g.drawLine(404, 255, 444, 255);
			}
		 };

		unten = new JPanel(){
			public void paintComponent(Graphics g) {
			  super.paintComponent(g);
			  g.drawLine(59, 27, 190, 27);
				g.drawLine(290, 27, 467, 27);
				g.drawLine(56, 170, 187, 170);
				g.drawLine(287, 170, 464, 170);
			}
		 };

    this.getContentPane().setBackground(Color.white);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setSize(new Dimension(557, 727));
    oben.setLayout(null);
    oben.setBounds(new Rectangle(1, 1, 547, 265));
    oben.setBackground(Color.white);
    jLabel7.setBounds(new Rectangle(313, 214, 168, 15));
    jLabel7.setText("des Fachbereichs Informatik");
    jLabel7.setFont(new java.awt.Font("Dialog", 0, 11));
    labKapitel.setBounds(new Rectangle(224, 240, 49, 15));
    labKapitel.setText(order.getZvtitel().getZVTitel() != null ? order.getZvtitel().getZVTitel().getZVKonto().getKapitel() : ((ZVTitel)order.getZvtitel()).getZVKonto().getKapitel());
    labKapitel.setFont(new java.awt.Font("Dialog", 0, 11));
		tpAnschrift.setBounds(new Rectangle(14, 35, 304, 66));
		tpAnschrift.setFont(new java.awt.Font("Dialog", 1, 11));
		Fachbereich[] fbs = as.getFachbereiche();
		if ((fbs != null) && (fbs.length > 0))
			tpAnschrift.setText(fbs[0].getFbBezeichnung() + "\n" +
													fbs[0].getStrasseHausNr() + "\n" +
													fbs[0].getPlzOrt());
		else
		 	tpAnschrift.setText("");
    jLabel1.setBounds(new Rectangle(14, 7, 280, 25));
    jLabel1.setText("Fachhochschule Mannheim");
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 16));
		tpFirmaAnschrift.setBounds(new Rectangle(14, 133, 344, 50));
		tpFirmaAnschrift.setFont(new java.awt.Font("Dialog", 0, 11));
		tpFirmaAnschrift.setText(	order.getAngebot().getAnbieter().getStrasseNr() + "\n" +
															order.getAngebot().getAnbieter().getPlz() + " " + order.getAngebot().getAnbieter().getOrt() + "\n" +
															"Fax: " + order.getAngebot().getAnbieter().getFaxNr());
    labTitel.setBounds(new Rectangle(323, 240, 57, 15));
    labTitel.setText(order.getZvtitel().getTitel());
    labTitel.setFont(new java.awt.Font("Dialog", 0, 11));
    labUT.setBounds(new Rectangle(404, 240, 40, 15));
    labUT.setText(order.getZvtitel().getUntertitel());
    labUT.setFont(new java.awt.Font("Dialog", 0, 11));
    jLabel8.setBounds(new Rectangle(14, 240, 212, 15));
    jLabel8.setText("zu belastender Haushaltstitel: Kapitel:");
    jLabel8.setFont(new java.awt.Font("Dialog", 0, 11));
    labKoSt.setBounds(new Rectangle(311, 188, 230, 15));
    labKoSt.setText(order.getFbkonto().getInstitut().getKostenstelle());
    labKoSt.setFont(new java.awt.Font("Dialog", 0, 11));
    labKostenstelle.setBounds(new Rectangle(95, 188, 98, 15));
    labKostenstelle.setText(order.getFbkonto().getInstitut().getBezeichnung());
    labKostenstelle.setFont(new java.awt.Font("Dialog", 0, 11));
    jLabel4.setBounds(new Rectangle(14, 188, 78, 15));
    jLabel4.setText("Kostenstelle:");
    jLabel4.setFont(new java.awt.Font("Dialog", 0, 11));
    this.setTitle("Drucktest");
    this.getContentPane().setLayout(null);
    jLabel2.setFont(new java.awt.Font("Dialog", 3, 14));
    jLabel2.setText("Beilage zur Bestellung bei");
    jLabel2.setBounds(new Rectangle(15, 111, 202, 23));
    jLabel6.setFont(new java.awt.Font("Dialog", 0, 11));
    jLabel6.setText("Software-beauftragte/r:");
    jLabel6.setBounds(new Rectangle(14, 214, 135, 15));
    labSoftwarebeauftragter.setFont(new java.awt.Font("Dialog", 0, 11));
    labSoftwarebeauftragter.setText(order.getSwbeauftragter().getName() + ", " + order.getSwbeauftragter().getVorname());
    labSoftwarebeauftragter.setBounds(new Rectangle(146, 214, 161, 15));
    jLabel9.setFont(new java.awt.Font("Dialog", 0, 11));
    jLabel9.setText("Titel:");
    jLabel9.setBounds(new Rectangle(286, 240, 33, 15));
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 11));
    jLabel10.setText("UT:");
    jLabel10.setBounds(new Rectangle(384, 240, 21, 15));
    jLabel5.setFont(new java.awt.Font("Dialog", 0, 11));
    jLabel5.setText("KostenstelleNr.:");
    jLabel5.setBounds(new Rectangle(217, 188, 97, 15));
    jLabel111.setBounds(new Rectangle(9, 155, 40, 15));
    jLabel111.setText("Datum:");
    jLabel111.setFont(new java.awt.Font("Dialog", 0, 11));
    jLabel19.setFont(new java.awt.Font("Dialog", 0, 11));
    jLabel19.setText("Bestellung durchgeführt und obige Leistungen erhalten");
    jLabel19.setBounds(new Rectangle(8, 120, 315, 15));
    jLabel12.setFont(new java.awt.Font("Dialog", 0, 11));
    jLabel12.setText("Unterschrift:");
    jLabel12.setBounds(new Rectangle(209, 12, 77, 15));
    unten.setBackground(Color.white);
    unten.setBounds(new Rectangle(0, 542, 547, 168));
    unten.setLayout(null);
    labUnterschrift1.setBounds(new Rectangle(287, 155, 177, 15));
    labUnterschrift1.setText("");
    labUnterschrift1.setFont(new java.awt.Font("Dialog", 0, 11));
    labDatum1.setBounds(new Rectangle(56, 155, 131, 15));
    labDatum1.setFont(new java.awt.Font("Dialog", 0, 11));
    labDatum1.setText("");
    jLabel16.setBorder(BorderFactory.createLineBorder(Color.black));
    jLabel16.setText("");
    jLabel16.setBounds(new Rectangle(355, 18, 182, 41));
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 11));
    jLabel11.setText("Datum:");
    jLabel11.setBounds(new Rectangle(12, 12, 40, 15));
    jLabel13.setFont(new java.awt.Font("Dialog", 0, 11));
    jLabel13.setText("Ist von der Technischen Betriebsleitung auszufüllen!");
    jLabel13.setBounds(new Rectangle(5, 0, 328, 15));
    jLabel18.setFont(new java.awt.Font("Dialog", 0, 10));
    jLabel18.setText("Unterschrift");
    jLabel18.setBounds(new Rectangle(362, 39, 68, 15));
    jLabel17.setFont(new java.awt.Font("Dialog", 0, 10));
    jLabel17.setText("Datum");
    jLabel17.setBounds(new Rectangle(189, 40, 40, 15));
    labDatum.setFont(new java.awt.Font("Dialog", 0, 11));
    labDatum.setBounds(new Rectangle(59, 12, 131, 15));
    jLabel14.setBorder(BorderFactory.createLineBorder(Color.black));
    jLabel14.setText("");
    jLabel14.setBounds(new Rectangle(3, 18, 177, 41));
    jLabel110.setBounds(new Rectangle(206, 155, 77, 15));
    jLabel110.setText("Unterschrift:");
    jLabel110.setFont(new java.awt.Font("Dialog", 0, 11));
    jTextPane3.setBackground(new Color(225, 225, 225));
    jTextPane3.setFont(new java.awt.Font("Dialog", 0, 10));
    jTextPane3.setText("Auftrags- nummer");
    jTextPane3.setBounds(new Rectangle(7, 22, 52, 34));
    jPanel2.setBackground(new Color(225, 225, 225));
    jPanel2.setBounds(new Rectangle(7, 38, 539, 64));
    jPanel2.setLayout(null);
    jLabel15.setBorder(BorderFactory.createLineBorder(Color.black));
    jLabel15.setText("");
    jLabel15.setBounds(new Rectangle(179, 18, 177, 41));
    labUnterschrift.setFont(new java.awt.Font("Dialog", 0, 11));
    labUnterschrift.setText("");
    labUnterschrift.setBounds(new Rectangle(290, 12, 177, 15));
		printPanel.setBackground(Color.white);
		labFirma.setBounds(new Rectangle(222, 111, 321, 23));
		labFirma.setFont(new java.awt.Font("Dialog", 3, 16));
		labFirma.setText(order.getAngebot().getAnbieter().getName());
		labAuftragsNr.setFont(new java.awt.Font("Dialog", 0, 11));
    labAuftragsNr.setText(order.getReferenznr());
		labAuftragsNr.setBounds(new Rectangle(60, 26, 112, 27));
    jScrollPane1.getViewport().add(tableBestellung);
    jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    jScrollPane1.getViewport().setBackground(Color.white);
    jScrollPane1.setBorder(BorderFactory.createEmptyBorder());
    jScrollPane1.setBounds(new Rectangle(0, 270, 547, 266));
    tableBestellung.setFont(new java.awt.Font("Dialog", 0, 11));
    printPanel.setBounds(new Rectangle(4, 5, 547, 714));
		printPanel.setLayout(null);
    jPanel2.add(jLabel13, null);
    jPanel2.add(jLabel18, null);
    jPanel2.add(jLabel17, null);
    jPanel2.add(jTextPane3, null);
    jPanel2.add(jLabel15, null);
    jPanel2.add(jLabel16, null);
    jPanel2.add(jLabel14, null);
    jPanel2.add(labAuftragsNr, null);
    unten.add(jLabel19, null);
    unten.add(jLabel110, null);
    unten.add(jLabel111, null);
    unten.add(labDatum1, null);
    unten.add(labUnterschrift1, null);
    unten.add(jLabel11, null);
    unten.add(labDatum, null);
    unten.add(jLabel12, null);
    unten.add(labUnterschrift, null);
    unten.add(jPanel2, null);
    oben.add(jLabel1, null);
    oben.add(tpAnschrift, null);
    oben.add(jLabel4, null);
    oben.add(labKostenstelle, null);
    oben.add(jLabel5, null);
    oben.add(labKoSt, null);
    oben.add(jLabel6, null);
    oben.add(labSoftwarebeauftragter, null);
    oben.add(jLabel7, null);
    oben.add(jLabel8, null);
    oben.add(labKapitel, null);
    oben.add(jLabel9, null);
    oben.add(labTitel, null);
    oben.add(labUT, null);
    oben.add(jLabel2, null);
    printPanel.add(jScrollPane1, null);
    printPanel.add(unten, null);
    printPanel.add(oben, null);
    this.getContentPane().add(printPanel, null);
    oben.add(tpFirmaAnschrift, null);
    oben.add(labFirma, null);
    oben.add(jLabel10, null);

  }

  private void createTable(ArrayList positions){
		String[] cols = {"Menge", "Produkt", "Einzelpreis", "Gesamtpreis", "für Institut"};
		Object[][] data = new Object[positions.size()][cols.length];

		for(int i = 0; i < positions.size(); i++){
			Position p = (Position)positions.get(i);

			data[i][0] = new Integer(p.getMenge());
			data[i][1] = p.getArtikel();
			data[i][2] = new Float(p.getEinzelPreis() + (p.getEinzelPreis() * p.getMwst()));
			data[i][3] = new Float(p.getGesamtpreis());
			data[i][4] = p.getInstitut().getBezeichnung();
		}
	  DefaultTableModel tableModel = new DefaultTableModel(data, cols){
				public boolean isCellEditable(int rowIndex, int columnIndex){
					  return false;
				}

				public Class getColumnClass(int col)  {
					return getValueAt(0, col).getClass();
				}
			};

		tableBestellung = new JTable(tableModel);
		tableBestellung.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
		tableBestellung.getTableHeader().setBorder(BorderFactory.createEtchedBorder(new Color(0,0,0),new Color(225,225,225) ));
		tableBestellung.getTableHeader().setBackground(new Color(225,225,225));
		tableBestellung.getTableHeader().setPreferredSize(new Dimension(545, 30));

		tableBestellung.setDefaultRenderer(Float.class, new JTableCurrencyRenderer());
		tableBestellung.setDefaultRenderer(String.class, new MultiLineRenderer());

	  DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		tableBestellung.setDefaultRenderer(Integer.class, dtcr);

		tableBestellung.getColumnModel().getColumn(0).setMaxWidth(50);
		tableBestellung.getColumnModel().getColumn(1).setMaxWidth(250);
		tableBestellung.getColumnModel().getColumn(2).setMaxWidth(80);
		tableBestellung.setFont(new Font("Arial", Font.PLAIN, 11));
		tableBestellung.getColumnModel().getColumn(3).setMaxWidth(100);
		tableBestellung.getColumnModel().getColumn(4).setMaxWidth(100);

		tableBestellung.setGridColor(Color.white);
		tableBestellung.setRowHeight(30);
		tableBestellung.setEnabled(false);

		jScrollPane1.getViewport().add(tableBestellung);
		jScrollPane1.setBounds(new Rectangle(0, 268, 547, (tableBestellung.getModel().getRowCount() * 30)+35));

		int heightAll = oben.getHeight() + tableBestellung.getTableHeader().getHeight() + tableBestellung.getModel().getRowCount() * 30 + unten.getHeight() + 50;


		if(heightAll > 800){
			unten.setBounds(new Rectangle(0, (heightAll / 800) * 800 + 600, 547, 200));
			int t = (heightAll / 800) * 800 + 800;
			printPanel.setBounds(4,5,547, t);
		}else{
			unten.setBounds(new Rectangle(0, 600, 547, 200));
			printPanel.setBounds(4,5,547, 800);
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
	
	private void printReport(){
		final URL in = getClass().getResource("/xml/askBestellung.xml");
		System.setProperty("org.jfree.report.targets.G2OutputTarget.isBuggyFRC","true");
		
		if (in == null){
			System.out.println("xml not found");
			return;
		}
		JCheckBox test = new JCheckBox();
		test.setSelected(true);
		test.setBackground(Color.WHITE);

		JFreeReport report = new JFreeReport();
	
		report = parseReport(in);
		report.setData(tableBestellung.getModel());
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
		final URL imageURL = getClass().getResource("../image/fhlogoklein.gif");
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
			// TODO Automatisch erstellter Catch-Block
			e.printStackTrace();
		}
	}


//	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
//		Graphics2D g2d = (Graphics2D) graphics;
//
//		double pageHeight = pageFormat.getImageableHeight();
//		double pageWidth = pageFormat.getImageableWidth();
//
//		// Height of all components
////		int heightAll = oben.getHeight() +  jScrollPane1.getHeight() + unten.getHeight();
//		int heightAll = printPanel.getHeight();
//
//		int totalNumPages= (int)Math.ceil(heightAll / pageHeight);
//
//		if(pageIndex  >= totalNumPages) {
//			return NO_SUCH_PAGE;
//		}else{
//			
//			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
////			g2d.setClip(0, pageIndex * (int)pageFormat.getImageableHeight(), 560, 900);
////			if(totalNumPages > 1)
////				g2d.drawString("Seite: " + (pageIndex+1) + " von " + totalNumPages,( int)pageWidth/2 - 35, 790);//bottom center
//
//			g2d.translate(0f, -pageIndex * pageFormat.getImageableHeight());
//			g2d.setClip(0, pageIndex * (int)pageFormat.getImageableHeight() - 5, 560, 780);
//
//			printPanel.printAll(g2d);
//
//			return Printable.PAGE_EXISTS;
//		}
//	}
}