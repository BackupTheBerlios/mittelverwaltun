package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;

import applicationServer.ApplicationServer;
import applicationServer.ApplicationServerException;

import dbObjects.Angebot;
import dbObjects.Benutzer;
import dbObjects.Fachbereich;
import dbObjects.Position;
import dbObjects.StandardBestellung;
import dbObjects.ZVTitel;


public class PrintSTDVordruck extends JFrame implements Printable{
  JPanel printPanel = new JPanel();
  JTextPane tpAdresse = new JTextPane();
  JLabel tfFachhochschule = new JLabel();
  JPanel adresse = new JPanel();
  JTextPane tpBesteller = new JTextPane();
  JPanel logo = new JPanel();
  JLabel tfBestellNr = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel labMwSt16 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JPanel lieferAnschrift = new JPanel();
  JTextPane tpLieferadresse = new JTextPane();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JTextPane tpBemerkungen = new JTextPane();
  JLabel labGesammt = new JLabel();
  JLabel netto = new JLabel();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JTextPane tpKostenstelle = new JTextPane();
  JPanel kostenstelle = new JPanel();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTable tableBestellung = new JTable();
  Border border1;
  Image img;
	ImageIcon logoIcon = null;
	StandardBestellung order;
	ApplicationServer as;
  JLabel labMwSt7 = new JLabel();
  JLabel jLabel7 = new JLabel();
  JPanel panelSumme = new JPanel();
  JTextPane tpKostenstelle1 = new JTextPane();
  int numPages = 0;

  public PrintSTDVordruck(StandardBestellung order, ApplicationServer as) {
  	this.order = order;
  	this.as = as;
  	try{
				img = loadImageResource("image","fhlogo.gif");
				if (img != null)
						  logoIcon =  new ImageIcon(img);
	 }catch (IOException e){
		System.out.println("Grafik nicht geladen");
	 };

	 logo = new JPanel(){
		public void paintComponent(Graphics g) {
		  super.paintComponent(g);
//			g.drawImage(img, 0, 0, 675, 84, this);
			g.drawImage(img, 0, 0, 295, 80, this);
		}
	 };

		kostenstelle = new JPanel(){
			public void paintComponent(Graphics g) {
			  super.paintComponent(g);
			  g.drawLine(0,0,700,0);
				g.drawLine(this.getWidth()/2, 0, this.getWidth()/2, this.getHeight());
			}
		 };
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
		createBestellung(order);
		createTable(((Angebot)order.getAngebote().get(order.getAngenommenesAngebot())).getPositionen());
  }

  private InputStream getResourceStream (String pkgname, String fname){
		 String resname = "/" + pkgname.replace('.','/')+ "/" + fname;
		 Class clazz = getClass();
		 InputStream is = clazz.getResourceAsStream(resname);
		 return is;
	}

	private Image loadImageResource (String pkgname, String fname)
		throws IOException{
		 Image ret = null;

		 InputStream is = getResourceStream(pkgname, fname);

		 if (is != null){

			byte[] buffer = new byte[0];
			byte[] tmpbuf = new byte[1024];

			while (true){
					  int len = is.read(tmpbuf);
					  if (len<=0){
								 break;
					  }
					  byte[] newbuf = new byte[buffer.length + len];
					  System.arraycopy(buffer, 0, newbuf, 0, buffer.length);
					  System.arraycopy(tmpbuf, 0, newbuf, buffer.length, len);
					  buffer = newbuf;
			}

			// create image
			ret = Toolkit.getDefaultToolkit().createImage(buffer);
			is.close();
		 }

		 return ret;
	  }


  	private void createTable(ArrayList positionen){
		  String[] cols = {"Menge", "Artikel/Bestellnummer", "Einzelpreis(€)", "MwSt.", "Rabatt", "Gesamtpreis(€)"};
		  Object[][] data = new Object[positionen.size()][cols.length];

		  for(int i = 0; i < positionen.size(); i++){
		  	Position p = (Position)positionen.get(i);
			  data[i][0] = new Integer(p.getMenge());
			  data[i][1] = p.getArtikel();
			  data[i][2] = new Float(p.getEinzelPreis());
				data[i][3] = new Float(p.getMwst());
			  data[i][4] = new Float(p.getRabatt());
			  data[i][5] = new Float(p.getGesamtpreis());
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

			tableBestellung.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));

			tableBestellung.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
			tableBestellung.getTableHeader().setBorder(BorderFactory.createEtchedBorder(new Color(0,0,0),new Color(225,225,225) ));
			tableBestellung.getTableHeader().setBackground(new Color(225,225,225));
			tableBestellung.getTableHeader().setPreferredSize(new Dimension(545, 30));

			tableBestellung.setDefaultRenderer(Float.class, new JTableCurrencyRenderer());
			tableBestellung.setDefaultRenderer(String.class, new MultiLineRenderer());

		  DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
			dtcr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			tableBestellung.setDefaultRenderer(Integer.class, dtcr);
			tableBestellung.getColumnModel().getColumn(3).setCellRenderer(new JTablePercentRenderer());

		  tableBestellung.getColumnModel().getColumn(0).setMaxWidth(50);
		  tableBestellung.getColumnModel().getColumn(1).setMaxWidth(300);
		  tableBestellung.getColumnModel().getColumn(2).setMaxWidth(110);
			tableBestellung.getColumnModel().getColumn(3).setMaxWidth(50);
		  tableBestellung.getColumnModel().getColumn(4).setMaxWidth(50);
		  tableBestellung.getColumnModel().getColumn(5).setMaxWidth(120);
		  tableBestellung.setGridColor(new Color(255,255,255));

		  tableBestellung.setRowHeight(30);
		  tableBestellung.setEnabled(false);

			jScrollPane1.getViewport().add(tableBestellung);
			jScrollPane1.setBounds(new Rectangle(3, 240, 547, (tableBestellung.getModel().getRowCount() * 30)+35));

			int heightAll = adresse.getHeight() + tableBestellung.getTableHeader().getHeight() + tableBestellung.getModel().getRowCount() * 30 +
											panelSumme.getHeight() + lieferAnschrift.getHeight() + kostenstelle.getHeight() + 50;
			numPages = (heightAll / 800) + 1;

			if(heightAll > 800){
				panelSumme.setBounds(new Rectangle(179, 275 + tableBestellung.getTableHeader().getHeight() + tableBestellung.getModel().getRowCount() * 30, 351, 99));
				lieferAnschrift.setBounds(new Rectangle(2, (heightAll / 800) * 800 + 472, 526, 185));
				kostenstelle.setBounds(new Rectangle(0, (heightAll / 800) * 800 + 670, 524, 127));
				int t = (heightAll / 800) * 800 + 800;
				printPanel.setBounds(4,5,547, t);
			}else{
				panelSumme.setBounds(new Rectangle(179, 240 + 35 + tableBestellung.getTableHeader().getHeight() + tableBestellung.getModel().getRowCount() * 30, 351, 99));
				printPanel.setBounds(4,5,547, 850);
			}
	  }

	  public int getNumPages(){
	  	return numPages;
	  }

	  private void createBestellung(StandardBestellung order){

		  Angebot a = (Angebot)order.getAngebote().get(order.getAngenommenesAngebot());


		  // Adresse
		  tpAdresse.setText(a.getAnbieter().getName() + "\n" +
		  									a.getAnbieter().getStrasseNr() + "\n\n" +
		  									a.getAnbieter().getPlz() + " " + a.getAnbieter().getOrt());


			Fachbereich[] fbs = null;
			try {
				fbs = as.getFachbereiche();
//				Besteller
				Benutzer il = order.getFbkonto().getInstitut().getInstitutsleiter(); // Institutsleiter
			  tpBesteller.setText(	fbs[0].getFbBezeichnung() + "\n" +
															order.getFbkonto().getInstitut().getBezeichnung() + "\n" +
															il.getTitel() + " " + il.getVorname() + " " + il.getName() + "\n\n" +
															"Tel.:    (0621) " + il.getTelefon() + "\n" +
															"Fax:            " + il.getFax() + "\n" +
															"Datum:    " + order.getDatum() + "\n" +
															"UstldNr.: DE811630438");

			} catch (ApplicationServerException e) {
				e.printStackTrace();
			}

		  // BestellNr.
		  tfBestellNr.setText("Bestellung    Nr.:" + order.getReferenznr());

		  // Bestelldaten

		  // Gesamtsummen
		  netto.setText(NumberFormat.getCurrencyInstance().format((order.getBestellwert() - order.get7PercentSum() - order.get16PercentSum())));
			labMwSt7.setText(NumberFormat.getCurrencyInstance().format(order.get7PercentSum()));
		  labMwSt16.setText(NumberFormat.getCurrencyInstance().format(order.get16PercentSum()));
		  labGesammt.setText(NumberFormat.getCurrencyInstance().format(order.getBestellwert()));



		  // Bemerkungen
		  tpBemerkungen.setText(order.getBemerkung());

		  // Lieferanschrift
		  if ((fbs != null) && (fbs.length > 0))
					tpLieferadresse.setText(fbs[0].getFhBezeichnung() + "\n" +
											fbs[0].getFbBezeichnung() + "\n" +
											"   z.Hd. " + order.getEmpfaenger().getVorname() + " " + order.getEmpfaenger().getName() + "\n" +
											"   Bau " + order.getEmpfaenger().getBau() + ", " + "Raum " + order.getEmpfaenger().getRaum() + "\n" +
											fbs[0].getStrasseHausNr() + "\n" +
											fbs[0].getPlzOrt());
			else tpLieferadresse.setText("");


		  // Kostenstelle
			tpKostenstelle1.setText("KostSt.:" + "\n" +
															"KostSt-Nr.:" + "\n" +
															"Kap/Tit/Ut:" + "\n" +
															"InstitKo.:" + "\n" +
															"InstitKo-Nr.:" + "\n" +
															"FBI-Nr.:" + "\n" +
															"Hül-Nr.:");
		  tpKostenstelle.setText(	order.getFbkonto().getInstitut().getBezeichnung() + "\n" +
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
	  }

  private void jbInit() throws Exception {
    this.setSize(new Dimension(640, 900));
    border1 = BorderFactory.createEmptyBorder();
    this.getContentPane().setBackground(Color.white);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setTitle("Bestellung");
    this.getContentPane().setLayout(null);
    printPanel.setBackground(Color.white);
    printPanel.setBounds(new Rectangle(4, 6, 587, 800));
    printPanel.setLayout(null);
    tpAdresse.setBackground(Color.white);
    tpAdresse.setFont(new java.awt.Font("Dialog", 0, 11));
    tpAdresse.setEditable(false);
    tpAdresse.setText("tpAdresse");
    tpAdresse.setBounds(new Rectangle(3, 120, 287, 87));
    tfFachhochschule.setBounds(new Rectangle(10, 10, 0, 0));
    tfFachhochschule.setFont(new java.awt.Font("Dialog", 0, 10));
    tfFachhochschule.setText("Fachhoschule Mannheim - WindeckStraße 110 - 68163 Mannheim");
    tfFachhochschule.setBounds(new Rectangle(3, 92, 335, 15));
    adresse.setBounds(new Rectangle(2, 2, 580, 278));
    adresse.setBackground(Color.white);
    adresse.setForeground(Color.black);
    adresse.setLayout(null);
    adresse.setBounds(new Rectangle(3, 3, 580, 236));
    tpBesteller.setEditable(false);
    tpBesteller.setText("tpBesteller");
    tpBesteller.setBounds(new Rectangle(361, 86, 183, 164));
    logo.setBackground(Color.white);
    logo.setBounds(new Rectangle(0, 0, 572, 81));
    logo.setLayout(null);
    tfBestellNr.setFont(new java.awt.Font("Dialog", 1, 14));
    tfBestellNr.setText("Bestellung    Nr.:");
    tfBestellNr.setBounds(new Rectangle(5, 214, 457, 15));
    jLabel4.setText("363,50");
    jLabel4.setBounds(new Rectangle(484, 9, 79, 15));
    jLabel4.setText("58,16");
    jLabel4.setBounds(new Rectangle(484, 29, 79, 15));
    jLabel4.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel4.setText("Bemerkungen");
    jLabel4.setBounds(new Rectangle(2, 8, 159, 15));
    labMwSt16.setFont(new java.awt.Font("Dialog", 0, 12));
    labMwSt16.setHorizontalAlignment(SwingConstants.RIGHT);
    labMwSt16.setBounds(new Rectangle(259, 53, 79, 15));
    jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel2.setHorizontalTextPosition(SwingConstants.LEADING);
    jLabel2.setText("Gesamtnettopreis:");
    jLabel2.setVerticalAlignment(SwingConstants.CENTER);
    jLabel2.setVerticalTextPosition(SwingConstants.CENTER);
    jLabel2.setBounds(new Rectangle(4, 12, 233, 15));
    lieferAnschrift.setLayout(null);
    lieferAnschrift.setBackground(Color.white);
    lieferAnschrift.setBounds(new Rectangle(2, 472, 526, 185));
    tpLieferadresse.setEditable(false);
    tpLieferadresse.setText("Lieferanschrift");
    tpLieferadresse.setBounds(new Rectangle(4, 99, 559, 95));
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel1.setHorizontalTextPosition(SwingConstants.LEADING);
    jLabel1.setText("Bestellsumme inkl. 16 % MwSt.");
    jLabel1.setVerticalAlignment(SwingConstants.CENTER);
    jLabel1.setVerticalTextPosition(SwingConstants.CENTER);
    jLabel1.setBounds(new Rectangle(4, 73, 233, 15));
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel3.setHorizontalTextPosition(SwingConstants.LEADING);
    jLabel3.setText("16% MwSt.");
    jLabel3.setVerticalAlignment(SwingConstants.CENTER);
    jLabel3.setVerticalTextPosition(SwingConstants.CENTER);
    jLabel3.setBounds(new Rectangle(4, 53, 233, 15));
    tpBemerkungen.setFont(new java.awt.Font("Dialog", 0, 12));
    tpBemerkungen.setEditable(false);
    tpBemerkungen.setText("Bemerkungen");
    tpBemerkungen.setBounds(new Rectangle(0, 23, 559, 54));
    labGesammt.setFont(new java.awt.Font("Dialog", 1, 12));
    labGesammt.setHorizontalAlignment(SwingConstants.RIGHT);
    labGesammt.setText("421,66");
    labGesammt.setBounds(new Rectangle(259, 73, 79, 15));
    netto.setFont(new java.awt.Font("Dialog", 0, 12));
    netto.setHorizontalAlignment(SwingConstants.RIGHT);
    netto.setBounds(new Rectangle(259, 12, 79, 15));
    jLabel5.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel5.setText("Lieferanschrift:");
    jLabel5.setBounds(new Rectangle(2, 81, 202, 15));
    jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel6.setText("Unterschrift des Institutsleiters");
    jLabel6.setBounds(new Rectangle(304, 10, 205, 15));
    tpKostenstelle.setFont(new java.awt.Font("Dialog", 0, 12));
    tpKostenstelle.setEditable(false);
    tpKostenstelle.setBounds(new Rectangle(117, 2, 132, 120));
    kostenstelle.setBackground(Color.white);
    kostenstelle.setBounds(new Rectangle(0, 670, 524, 127));
    kostenstelle.setLayout(null);
    jScrollPane1.getViewport().setBackground(Color.white);
    jScrollPane1.setBorder(border1);
    jScrollPane1.setBounds(new Rectangle(-2, 235, 580, 137));
    labMwSt7.setBounds(new Rectangle(259, 32, 79, 15));
    labMwSt7.setHorizontalAlignment(SwingConstants.RIGHT);
    labMwSt7.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel7.setBounds(new Rectangle(4, 32, 233, 15));
    jLabel7.setVerticalTextPosition(SwingConstants.CENTER);
    jLabel7.setVerticalAlignment(SwingConstants.CENTER);
    jLabel7.setText("7% MwSt.");
    jLabel7.setHorizontalTextPosition(SwingConstants.LEADING);
    jLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
    panelSumme.setBackground(Color.white);
		panelSumme.setBounds(new Rectangle(175, 367, 351, 99));
		panelSumme.setLayout(null);
    tpKostenstelle1.setBounds(new Rectangle(10, 2, 104, 120));
    tpKostenstelle1.setText("Kostenstelle");
    tpKostenstelle1.setEditable(false);
    tpKostenstelle1.setFont(new java.awt.Font("Dialog", 0, 12));
    this.getContentPane().add(printPanel, null);
    adresse.add(tfFachhochschule, null);
    adresse.add(logo, null);
    adresse.add(tfFachhochschule, null);
    adresse.add(tpAdresse, null);
    adresse.add(tpBesteller, null);
    adresse.add(tfBestellNr, null);
		panelSumme.add(labGesammt, null);
		panelSumme.add(jLabel2, null);
		panelSumme.add(jLabel7, null);
		panelSumme.add(jLabel3, null);
		panelSumme.add(netto, null);
		panelSumme.add(labMwSt7, null);
		panelSumme.add(labMwSt16, null);
		panelSumme.add(jLabel1, null);
    printPanel.add(kostenstelle, null);
    lieferAnschrift.add(tpBemerkungen, null);
    lieferAnschrift.add(jLabel4, null);
    lieferAnschrift.add(jLabel5, null);
    lieferAnschrift.add(tpLieferadresse, null);
    printPanel.add(panelSumme, null);
    kostenstelle.add(tpKostenstelle, null);
    kostenstelle.add(jLabel6, null);
    kostenstelle.add(tpKostenstelle1, null);
    printPanel.add(jScrollPane1, null);
    printPanel.add(lieferAnschrift, null);
    jScrollPane1.getViewport().add(tableBestellung, null);
    printPanel.add(adresse, null);
    lieferAnschrift.add(jLabel4, null);
  }

  private void insertText(Document doc, String string) {
	 try {
		 doc.insertString(doc.getLength(), string, null);
	 }catch(Throwable t) {
		 t.printStackTrace();

	 }
 }

	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		Graphics2D g2d = (Graphics2D) graphics;

		double pageHeight = pageFormat.getImageableHeight();
		double pageWidth = pageFormat.getImageableWidth();

		// Height of all components
		int heightAll = printPanel.getHeight();

		int totalNumPages= (int)Math.ceil(heightAll / pageHeight);

	 	if(pageIndex  >= totalNumPages) {
		 	return NO_SUCH_PAGE;
	 	}else{
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
//			g2d.setClip(0, pageIndex * (int)pageFormat.getImageableHeight(), 560, 900);
//			if(totalNumPages > 1)
//				g2d.drawString("Seite: " + (pageIndex+1) + " von " + totalNumPages,( int)pageWidth/2 - 35, 790);//bottom center

			g2d.translate(0f, -pageIndex * pageFormat.getImageableHeight());
			g2d.setClip(0, pageIndex * (int)pageFormat.getImageableHeight() - 5, 560, 780);

			printPanel.printAll(g2d);

			return Printable.PAGE_EXISTS;
		}
	}
}