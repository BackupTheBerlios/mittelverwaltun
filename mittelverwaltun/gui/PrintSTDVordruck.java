package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;

import dbObjects.Angebot;
import dbObjects.StandardBestellung;


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

  public PrintSTDVordruck(StandardBestellung order) {
  	this.order = order;
  	try{
				img = loadImageResource("image","fh-header2.gif");
				if (img != null)
						  logoIcon =  new ImageIcon(img);
	 }catch (IOException e){
		System.out.println("Grafik nicht geladen");
	 };

	 logo = new JPanel(){
		public void paintComponent(Graphics g) {
		  super.paintComponent(g);
			g.drawImage(img, 0, 0, 675, 84, this);
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
		createTable();
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


  private void createTable(){
		  String[] cols = {"Menge", "Artikel/Bestellnummer", "Einzelpreis(€)", "Rabatt", "Gesamtpreis(€)"};
		  String[][] data = new String[1][cols.length];

		  for(int i = 0; i < 1; i++){
			  data[i][0] = "" + i;
			  data[i][1] = "Intel Pro/1000MT Art. Nr. 34960 kjkjk kjk jk jkj kjk jk jk jk jk jk jk kj";
			  data[i][2] = "32,10";
			  data[i][3] = "";
			  data[i][4] = "160,50";
		  }
	  DefaultTableModel tableModel = new DefaultTableModel(data, cols){
			  public boolean isCellEditable(int rowIndex, int columnIndex){
					 return false;
			  }

			  public Class getColumnClass(int col)  {
				  return String.class;
			  }
		  };

		  tableBestellung = new JTable(tableModel);
		  tableBestellung.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
		  tableBestellung.getTableHeader().setBackground(new Color(255,255,255));
		  tableBestellung.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
		  DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		  render.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		  tableBestellung.getColumnModel().getColumn(0).setCellRenderer(render); tableBestellung.getColumnModel().getColumn(0).setMaxWidth(50);
		  tableBestellung.getColumnModel().getColumn(1).setCellRenderer(new MultiLineRenderer());
		  tableBestellung.getColumnModel().getColumn(1).setMaxWidth(450);
		  tableBestellung.getColumnModel().getColumn(2).setCellRenderer(render); tableBestellung.getColumnModel().getColumn(2).setMaxWidth(110);
		  tableBestellung.getColumnModel().getColumn(3).setCellRenderer(render); tableBestellung.getColumnModel().getColumn(3).setMaxWidth(50);
		  tableBestellung.getColumnModel().getColumn(4).setCellRenderer(render); tableBestellung.getColumnModel().getColumn(4).setMaxWidth(120);
		  tableBestellung.setGridColor(new Color(255,255,255));

		  tableBestellung.setRowHeight(30);
		  tableBestellung.setEnabled(false);

			jScrollPane1.getViewport().add(tableBestellung);
			jScrollPane1.setBounds(new Rectangle(0, 285, 547, (tableBestellung.getModel().getRowCount() * 30)+20));
			lieferAnschrift.setBounds(new Rectangle(0, 285 + (tableBestellung.getModel().getRowCount() *30)+20, 547, 248));
			kostenstelle.setBounds(new Rectangle(0, 274 + (tableBestellung.getModel().getRowCount() *30)+280, 547, 127));
			printPanel.setBounds(4,5,547, adresse.getHeight() + tableBestellung.getTableHeader().getHeight() + tableBestellung.getModel().getRowCount() * 30 +
													lieferAnschrift.getHeight() + kostenstelle.getHeight() + 50);

	  }

	  private void createBestellung(StandardBestellung order){

		  Angebot a = (Angebot)order.getAngebote().get(order.getAngenommenesAngebot());


		  // Adresse
		  tpAdresse.setText(a.getAnbieter().getName() + "\n" +
		  									a.getAnbieter().getStrasseNr() + "\n\n" +
		  									a.getAnbieter().getPlz() + " " + a.getAnbieter().getOrt());

		  // Besteller
		  tpBesteller.setText("Fachbereich Informatik \nInstitut für Betriebssysteme \nProf. G. Bengel");

		  
		  // BestellNr.
		  tfBestellNr.setText("Bestellung    Nr.:");

		  // Bestelldaten

		  // Gesamtsummen
		  netto.setText("363,50");
		  labMwSt16.setText("58,16");
		  labGesammt.setText("421,66");


		  // Bemerkungen
		  tpBemerkungen.setText("Angebot: Nr. 095921 vom 20.1.2004");

		  // Lieferanschrift
		  tpLieferadresse.setText("Fachhoschschule Mannheim \nBTS-Labor, Bau 1, H.Lukwata \nWindeckstr. 110 \n68163 Mannheim");

		  // Kostenstelle
		  tpKostenstelle.setText("KostSt.:      \n"	+
														"KostSt-Nr.:   \n" +
														"Kap/Tit/Ut:   \n" +
														"InstitKo.:    \n" +
														"InstitKo-Nr.: \n" +
														"FBI-Nr.:      \n" +
														"Hül-Nr.:      \n");
	  }

  private void jbInit() throws Exception {
    this.setSize(new Dimension(640, 900));
    border1 = BorderFactory.createEmptyBorder();
    this.getContentPane().setBackground(Color.white);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setTitle("Bestellung");
    this.getContentPane().setLayout(null);
    printPanel.setBackground(Color.white);
    printPanel.setBounds(new Rectangle(4, 6, 587, 784));
    printPanel.setLayout(null);
    tpAdresse.setBackground(Color.white);
    tpAdresse.setFont(new java.awt.Font("Dialog", 0, 11));
    tpAdresse.setEditable(false);
    tpAdresse.setText("tpAdresse");
    tpAdresse.setBounds(new Rectangle(3, 120, 287, 87));
    tfFachhochschule.setBounds(new Rectangle(10, 10, 0, 0));
    tfFachhochschule.setText("Fachhoschule Mannheim - WindeckStraße 110 - 68163 Mannheim");
    tfFachhochschule.setBounds(new Rectangle(3, 92, 335, 15));
    adresse.setBounds(new Rectangle(2, 2, 580, 278));
    adresse.setBackground(Color.white);
    adresse.setForeground(Color.black);
    adresse.setLayout(null);
    adresse.setBounds(new Rectangle(3, 3, 580, 285));
    tpBesteller.setEditable(false);
    tpBesteller.setText("tpBesteller");
    tpBesteller.setBounds(new Rectangle(361, 86, 183, 164));
    logo.setBackground(Color.white);
    logo.setBounds(new Rectangle(0, 0, 572, 81));
    logo.setLayout(null);
    tfBestellNr.setFont(new java.awt.Font("Dialog", 1, 14));
    tfBestellNr.setText("Bestellung    Nr.:");
    tfBestellNr.setBounds(new Rectangle(8, 252, 457, 15));
    jLabel4.setText("363,50");
    jLabel4.setBounds(new Rectangle(484, 9, 79, 15));
    jLabel4.setText("58,16");
    jLabel4.setBounds(new Rectangle(484, 29, 79, 15));
    jLabel4.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel4.setText("Bemerkungen");
    jLabel4.setBounds(new Rectangle(2, 82, 159, 15));
    labMwSt16.setFont(new java.awt.Font("Dialog", 0, 12));
    labMwSt16.setHorizontalAlignment(SwingConstants.RIGHT);
    labMwSt16.setBounds(new Rectangle(443, 28, 94, 15));
    jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel2.setHorizontalTextPosition(SwingConstants.LEADING);
    jLabel2.setText("Gesamtnettopreis:");
    jLabel2.setVerticalAlignment(SwingConstants.CENTER);
    jLabel2.setVerticalTextPosition(SwingConstants.CENTER);
    jLabel2.setBounds(new Rectangle(188, 8, 233, 15));
    lieferAnschrift.setLayout(null);
    lieferAnschrift.setBackground(Color.white);
    lieferAnschrift.setBounds(new Rectangle(6, 393, 526, 248));
    tpLieferadresse.setEditable(false);
    tpLieferadresse.setText("Lieferanschrift");
    tpLieferadresse.setBounds(new Rectangle(4, 173, 347, 82));
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel1.setHorizontalTextPosition(SwingConstants.LEADING);
    jLabel1.setText("Bestellsumme inkl. 16 % MwSt.");
    jLabel1.setVerticalAlignment(SwingConstants.CENTER);
    jLabel1.setVerticalTextPosition(SwingConstants.CENTER);
    jLabel1.setBounds(new Rectangle(188, 48, 233, 15));
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel3.setHorizontalTextPosition(SwingConstants.LEADING);
    jLabel3.setText("16% MwSt.");
    jLabel3.setVerticalAlignment(SwingConstants.CENTER);
    jLabel3.setVerticalTextPosition(SwingConstants.CENTER);
    jLabel3.setBounds(new Rectangle(188, 28, 233, 15));
    tpBemerkungen.setFont(new java.awt.Font("Dialog", 0, 12));
    tpBemerkungen.setEditable(false);
    tpBemerkungen.setText("Bemerkungen");
    tpBemerkungen.setBounds(new Rectangle(0, 97, 559, 62));
    labGesammt.setFont(new java.awt.Font("Dialog", 1, 12));
    labGesammt.setHorizontalAlignment(SwingConstants.RIGHT);
    labGesammt.setText("421,66");
    labGesammt.setBounds(new Rectangle(443, 48, 79, 15));
    netto.setFont(new java.awt.Font("Dialog", 0, 12));
    netto.setHorizontalAlignment(SwingConstants.RIGHT);
    netto.setBounds(new Rectangle(443, 8, 79, 15));
    jLabel5.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel5.setText("Lieferanschrift:");
    jLabel5.setBounds(new Rectangle(2, 155, 202, 15));
    jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel6.setText("Unterschrift des Institutsleiters");
    jLabel6.setBounds(new Rectangle(304, 10, 205, 15));
    tpKostenstelle.setFont(new java.awt.Font("Dialog", 0, 12));
    tpKostenstelle.setEditable(false);
    tpKostenstelle.setText("Kostenstelle");
    tpKostenstelle.setBounds(new Rectangle(1, 2, 248, 120));
    kostenstelle.setBackground(Color.white);
    kostenstelle.setBounds(new Rectangle(5, 648, 524, 127));
    kostenstelle.setLayout(null);
    jScrollPane1.getViewport().setBackground(Color.white);
    jScrollPane1.setBorder(border1);
    jScrollPane1.setBounds(new Rectangle(2, 285, 531, 106));
    this.getContentPane().add(printPanel, null);
    adresse.add(tfFachhochschule, null);
    adresse.add(logo, null);
    adresse.add(tfBestellNr, null);
    adresse.add(tfFachhochschule, null);
    adresse.add(tpAdresse, null);
    adresse.add(tpBesteller, null);
    printPanel.add(lieferAnschrift, null);
    lieferAnschrift.add(jLabel4, null);
    lieferAnschrift.add(jLabel5, null);
    lieferAnschrift.add(netto, null);
    lieferAnschrift.add(jLabel2, null);
    lieferAnschrift.add(jLabel3, null);
    lieferAnschrift.add(jLabel1, null);
    lieferAnschrift.add(labGesammt, null);
    lieferAnschrift.add(labMwSt16, null);
    lieferAnschrift.add(tpLieferadresse, null);
    lieferAnschrift.add(tpBemerkungen, null);
    printPanel.add(kostenstelle, null);
    kostenstelle.add(tpKostenstelle, null);
    kostenstelle.add(jLabel6, null);
    printPanel.add(jScrollPane1, null);
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
		int heightAll = adresse.getHeight() +  jScrollPane1.getHeight() + lieferAnschrift.getHeight() + kostenstelle.getHeight();

		int totalNumPages= (int)Math.ceil(heightAll / pageHeight);

	 	if(pageIndex  >= totalNumPages) {
		 	return NO_SUCH_PAGE;
	 	}else{
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			g2d.setClip(0, pageIndex * (int)pageFormat.getImageableHeight(), 560, 740);
			if(totalNumPages > 1)
				g2d.drawString("Seite: "+(pageIndex+1)+" von "+totalNumPages,(int)pageWidth/2-35, 725);//bottom center

			g2d.translate(0f,-pageIndex * pageFormat.getImageableHeight());
			g2d.setClip(0, pageIndex * (int)pageFormat.getImageableHeight()-5, 560, 712);

			printPanel.printAll(g2d);

			return Printable.PAGE_EXISTS;
		}
	}
}