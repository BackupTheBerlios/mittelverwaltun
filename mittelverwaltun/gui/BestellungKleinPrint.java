package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;


public class BestellungKleinPrint extends JFrame implements Printable {
  JPanel printPanel = new JPanel();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JPanel oben = new JPanel();
  JLabel jLabel2 = new JLabel();
  JLabel labProjektNr = new JLabel();
  JLabel jLabel9 = new JLabel();
  JLabel labUT = new JLabel();
  JLabel labTitel = new JLabel();
  JLabel jLabel11 = new JLabel();
  JLabel labKapitel = new JLabel();
  JLabel jLabel10 = new JLabel();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTable tableBestellung = new JTable();
  Border border1;
  JLabel jLabel8 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel12 = new JLabel();
  JPanel jPanel4 = new JPanel();
  JLabel jLabel13 = new JLabel();
  JLabel labGesamt = new JLabel();
  JLabel labName = new JLabel();
  JTextPane tpVerwendung = new JTextPane();
  JPanel jPanel3 = new JPanel();
  JLabel jLabel14 = new JLabel();
  JPanel jPanel2 = new JPanel();
  JLabel labUnterschrift = new JLabel();
  JTextPane tpAuszahlung = new JTextPane();
  JLabel jLabel19 = new JLabel();
  JPanel unten = new JPanel();
  JLabel jLabel16 = new JLabel();
  JLabel jLabel18 = new JLabel();
  JLabel jLabel7 = new JLabel();
  JLabel jLabel17 = new JLabel();
  JLabel labKartei = new JLabel();
  JLabel labAuszahlungAn = new JLabel();
  JLabel labTitlVerz = new JLabel();
  JPanel jPanel1 = new JPanel();
  JLabel labDatum = new JLabel();
  JLabel jLabel6 = new JLabel();
  JLabel labKarteiNr = new JLabel();
  JLabel jLabel15 = new JLabel();
  JLabel jLabel5 = new JLabel();

  public BestellungKleinPrint() {
  	createBestellung();
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    createTable();
  }

  public static void main(String[] args) {
    BestellungKleinPrint bestellungKlein2 = new BestellungKleinPrint();
    bestellungKlein2.show();
  }

  private void jbInit() throws Exception {
    border1 = BorderFactory.createEmptyBorder();
    unten = new JPanel(){
			public void paintComponent(Graphics g) {
			  super.paintComponent(g);
			  g.drawLine(307, 241, 422, 241);
			}
		 };
		 
		oben = new JPanel(){
			public void paintComponent(Graphics g) {
			  super.paintComponent(g);
			  g.drawLine(314, 23, 369, 23);
			  g.drawLine(404, 23, 456, 23);
			  g.drawLine(488, 23, 528, 23);
			  g.drawLine(443, 48, 528, 48);
			}
		 };
		this.setSize(new Dimension(580, 727));
    this.getContentPane().setBackground(Color.white);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setTitle("Kleinbestellung");
    this.getContentPane().setLayout(null);
    printPanel.setBackground(Color.white);
    printPanel.setBounds(new Rectangle(11, 8, 548, 743));
    printPanel.setLayout(null);
    jLabel1.setEnabled(true);
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 16));
    jLabel1.setText("Fachhochschule Mannheim");
    jLabel1.setBounds(new Rectangle(6, 6, 225, 15));
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setText("Projekt-Nr.");
    jLabel3.setBounds(new Rectangle(374, 33, 67, 15));
    oben.setBackground(Color.white);
    oben.setBounds(new Rectangle(3, 3, 549, 56));
    oben.setLayout(null);
    jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setText("Hochschule für Technik und Gestaltung");
    jLabel2.setBounds(new Rectangle(6, 27, 230, 15));
    labProjektNr.setFont(new java.awt.Font("Dialog", 0, 12));
    labProjektNr.setText("");
    labProjektNr.setBounds(new Rectangle(443, 33, 85, 15));
    jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel9.setText("Kapitel");
    jLabel9.setBounds(new Rectangle(274, 8, 53, 15));
    labUT.setFont(new java.awt.Font("Dialog", 0, 12));
    labUT.setHorizontalAlignment(SwingConstants.CENTER);
    labUT.setText("56");
    labUT.setBounds(new Rectangle(488, 8, 40, 15));
    labTitel.setFont(new java.awt.Font("Dialog", 0, 12));
    labTitel.setHorizontalAlignment(SwingConstants.CENTER);
    labTitel.setText("64875");
    labTitel.setBounds(new Rectangle(404, 8, 52, 15));
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel11.setText("Titel");
    jLabel11.setBounds(new Rectangle(376, 8, 40, 15));
    labKapitel.setFont(new java.awt.Font("Dialog", 0, 12));
    labKapitel.setHorizontalAlignment(SwingConstants.CENTER);
    labKapitel.setText("02436");
    labKapitel.setBounds(new Rectangle(314, 8, 55, 15));
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setText("UT");
    jLabel10.setBounds(new Rectangle(466, 8, 33, 15));
    jScrollPane1.getViewport().setBackground(Color.white);
    jScrollPane1.setBorder(border1);
    jScrollPane1.setBounds(new Rectangle(4, 63, 532, 99));
    jLabel8.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel8.setText("eingetragen.");
    jLabel8.setBounds(new Rectangle(422, 9, 75, 15));
    jLabel4.setFont(new java.awt.Font("Arial", 1, 12));
    jLabel4.setText("Gesamt");
    jLabel4.setBounds(new Rectangle(387, 7, 53, 15));
    jLabel12.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel12.setText("Für richtige Lieferung und Ausführung");
    jLabel12.setBounds(new Rectangle(6, 5, 221, 15));
    jPanel4.setLayout(null);
    jPanel4.setBounds(new Rectangle(4, 188, 529, 32));
    jPanel4.setBorder(BorderFactory.createLineBorder(Color.black));
    jLabel13.setText("");
    jLabel13.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel13.setText("Auszahlung an:");
    jLabel13.setBounds(new Rectangle(5, 9, 89, 15));
    labGesamt.setFont(new java.awt.Font("Dialog", 1, 12));
    labGesamt.setHorizontalAlignment(SwingConstants.CENTER);
    labGesamt.setBounds(new Rectangle(455, 8, 76, 15));
    labName.setFont(new java.awt.Font("Dialog", 1, 11));
    labName.setText("Schramm");
    labName.setBounds(new Rectangle(185, 30, 98, 15));
    tpVerwendung.setBackground(null);
    tpVerwendung.setFont(new java.awt.Font("Dialog", 1, 11));
    tpVerwendung.setBorder(null);
    tpVerwendung.setEditable(false);
    tpVerwendung.setText("");
    tpVerwendung.setBounds(new Rectangle(3, 21, 519, 51));
    jPanel3.setLayout(null);
    jPanel3.setBounds(new Rectangle(4, 135, 529, 54));
    jPanel3.setBorder(BorderFactory.createLineBorder(Color.black));
    jLabel14.setFont(new java.awt.Font("Dialog", 0, 11));
    jLabel14.setText("Titl. Verz. Nr.:");
    jLabel14.setBounds(new Rectangle(230, 225, 89, 15));
    jPanel2.setBorder(BorderFactory.createLineBorder(Color.black));
    jPanel2.setBounds(new Rectangle(4, 26, 529, 79));
    jPanel2.setLayout(null);
    labUnterschrift.setBorder(null);
    labUnterschrift.setText("");
    labUnterschrift.setBounds(new Rectangle(376, 30, 138, 15));
    tpAuszahlung.setBounds(new Rectangle(228, 269, 268, 248));
    jLabel19.setFont(new java.awt.Font("Dialog", 0, 11));
    jLabel19.setText("Regierungsdirektor");
    jLabel19.setBounds(new Rectangle(275, 547, 147, 15));
    unten.setBackground(Color.white);
    unten.setBounds(new Rectangle(5, 168, 547, 567));
    unten.setLayout(null);
    jLabel16.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel16.setText("Datum:");
    jLabel16.setBounds(new Rectangle(5, 30, 40, 15));
    jLabel18.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel18.setText("Unterschrift:");
    jLabel18.setBounds(new Rectangle(299, 30, 79, 15));
    jLabel7.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel7.setText("- Labor, Nr.");
    jLabel7.setBounds(new Rectangle(244, 9, 68, 15));
    jLabel17.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel17.setText("Name:");
    jLabel17.setBounds(new Rectangle(136, 30, 40, 15));
    labKartei.setFont(new java.awt.Font("Dialog", 1, 11));
    labKartei.setText("");
    labKartei.setBounds(new Rectangle(147, 9, 88, 15));
    labAuszahlungAn.setFont(new java.awt.Font("Dialog", 1, 11));
    labAuszahlungAn.setText("");
    labAuszahlungAn.setBounds(new Rectangle(93, 9, 277, 15));
    labTitlVerz.setText("");
    labTitlVerz.setBounds(new Rectangle(307, 226, 115, 15));
    jPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
    jPanel1.setBounds(new Rectangle(4, 104, 529, 32));
    jPanel1.setLayout(null);
    labDatum.setFont(new java.awt.Font("Dialog", 1, 11));
    labDatum.setText("06.04.2004");
    labDatum.setBounds(new Rectangle(49, 30, 73, 15));
    jLabel6.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel6.setText("In Material/Geräte-Kartei");
    jLabel6.setBounds(new Rectangle(4, 9, 140, 15));
    labKarteiNr.setFont(new java.awt.Font("Dialog", 1, 11));
    labKarteiNr.setText("");
    labKarteiNr.setBounds(new Rectangle(314, 9, 99, 15));
    jLabel15.setFont(new java.awt.Font("Dialog", 1, 14));
    jLabel15.setText("Auszahlungsanordnung");
    jLabel15.setBounds(new Rectangle(230, 249, 176, 15));
    jLabel5.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel5.setText("Verwendung oder Begründung");
    jLabel5.setBounds(new Rectangle(5, 3, 187, 15));
    this.getContentPane().add(printPanel, null);
    oben.add(jLabel1, null);
    oben.add(jLabel2, null);
    oben.add(labTitel, null);
    oben.add(labUT, null);
    oben.add(jLabel9, null);
    oben.add(labKapitel, null);
    oben.add(jLabel11, null);
    oben.add(jLabel10, null);
    oben.add(labProjektNr, null);
    oben.add(jLabel3, null);
		jScrollPane1.getViewport().add(tableBestellung, null);
    printPanel.add(jScrollPane1, null);
    printPanel.add(oben, null);
    unten.add(labGesamt, null);
    unten.add(jLabel4, null);
    unten.add(jPanel2, null);
    jPanel2.add(jLabel5, null);
    jPanel2.add(tpVerwendung, null);
    unten.add(jPanel1, null);
    jPanel1.add(jLabel8, null);
    jPanel1.add(jLabel6, null);
    jPanel1.add(labKartei, null);
    jPanel1.add(jLabel7, null);
    jPanel1.add(labKarteiNr, null);
    unten.add(jPanel3, null);
    jPanel3.add(jLabel18, null);
    jPanel3.add(labDatum, null);
    jPanel3.add(jLabel17, null);
    jPanel3.add(labName, null);
    jPanel3.add(labUnterschrift, null);
    jPanel3.add(jLabel16, null);
    jPanel3.add(jLabel12, null);
    unten.add(jPanel4, null);
    jPanel4.add(labAuszahlungAn, null);
    jPanel4.add(jLabel13, null);
    unten.add(labTitlVerz, null);
    unten.add(jLabel14, null);
    unten.add(jLabel15, null);
    unten.add(tpAuszahlung, null);
    unten.add(jLabel19, null);
    printPanel.add(unten, null);
  }

  private void createBestellung(){
  	labKapitel.setText("");
  	labTitel.setText("");
  	labUT.setText("");
  	labProjektNr.setText("");
  	
  	// Summe Gesamt
  	labGesamt.setText("");
  	
  	tpVerwendung.setText("");
  	labKartei.setText("");
  	labKarteiNr.setText("");
  	
  	labDatum.setText("");
  	labName.setText("");
  	
  	labTitlVerz.setText("");
  	  	
		insertText(tpAuszahlung.getDocument(), "1.  Sachlich richtig und festgestellt \n2.  " +
																						  "Verbuchungsstlle: Kapitel 88888, \n" +
																						"     Titel 88888 für das Kalenderjahr 2004  \n" +
																						  "3.  Die Zahlstelle " +
																						"der Landesoberkasse \n     Nordbaden and der Fachhochschule \n     Mannheim wird " +
																						"hiermit angewiesen, den \n     Betrag von \n     ______________ Euro _____ Cent \n     in Worten:  ________________________" +
																						"\n     _________________________________\n     auszuzahlen und wie oben angegeben zu \n     buchen. \n\nMannheim, den _______________________");
	 }

	 private void createTable(){
		String[] cols = {"Beleg-Nr.", "Firma", "Artikel, Gegenstand", "Preis(€)"};
		String[][] data = new String[4][cols.length];

		for(int i = 0; i < 4; i++){
			data[i][0] = "" + i;
			data[i][1] = "";
			data[i][2] = "";
			data[i][3] = "";
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
		tableBestellung.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
		tableBestellung.getTableHeader().setBackground(new Color(255,255,255));
		tableBestellung.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		render.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		tableBestellung.getColumnModel().getColumn(0).setCellRenderer(render); tableBestellung.getColumnModel().getColumn(0).setMaxWidth(80);
		tableBestellung.getColumnModel().getColumn(1).setMaxWidth(150);
		tableBestellung.getColumnModel().getColumn(2).setCellRenderer(render); tableBestellung.getColumnModel().getColumn(2).setMaxWidth(300);
		tableBestellung.getColumnModel().getColumn(3).setCellRenderer(render); tableBestellung.getColumnModel().getColumn(3).setMaxWidth(100);
		tableBestellung.setGridColor(Color.white);

		tableBestellung.setRowHeight(17);
		tableBestellung.setEnabled(false);

		jScrollPane1.getViewport().add(tableBestellung);
		jScrollPane1.setBounds(new Rectangle(1, 63, 532, (tableBestellung.getModel().getRowCount() * 17)+20));
		unten.setBounds(new Rectangle(0, 63 + (tableBestellung.getModel().getRowCount() *17), 547, 567));
		printPanel.setBounds(4,5,547, oben.getHeight() + tableBestellung.getTableHeader().getHeight() + tableBestellung.getModel().getRowCount() * 30 + unten.getHeight() + 50);

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
		int heightAll = oben.getHeight() +  jScrollPane1.getHeight() + unten.getHeight();
		
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