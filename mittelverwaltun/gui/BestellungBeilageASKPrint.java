package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;


public class BestellungBeilageASKPrint extends JFrame implements Printable {
  JLabel jLabel4 = new JLabel();
  JLabel labKostenstelle = new JLabel();
  JLabel labKoSt = new JLabel();
  JLabel labFachbereich = new JLabel();
  JLabel jLabel8 = new JLabel();
  JLabel labUT = new JLabel();
  JLabel labTitel = new JLabel();
  JTextPane jTextPane2 = new JTextPane();
  JLabel jLabel1 = new JLabel();
  JTextPane jTextPane1 = new JTextPane();
  JLabel labKapitel = new JLabel();
  JLabel jLabel3 = new JLabel();
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

  public BestellungBeilageASKPrint() {
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
    BestellungBeilageASKPrint printTest = new BestellungBeilageASKPrint();
    printTest.show();
  }

  private void jbInit() throws Exception {
		oben = new JPanel(){
			public void paintComponent(Graphics g) {
			  super.paintComponent(g);
			  g.drawLine(95, 203, 193, 203);
				g.drawLine(311, 203, 541, 203);
				g.drawLine(146, 229, 307, 229);
				g.drawLine(416, 229, 541, 229);
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
    jLabel7.setBounds(new Rectangle(313, 214, 106, 15));
    jLabel7.setText("des Fachbereichs");
    jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setBounds(new Rectangle(332, 160, 136, 15));
    jLabel3.setText("Fax: 0721 / 96458-99");
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    labKapitel.setBounds(new Rectangle(224, 240, 49, 15));
    labKapitel.setText("33333");
    labKapitel.setFont(new java.awt.Font("Dialog", 0, 12));
    jTextPane1.setBounds(new Rectangle(14, 35, 231, 66));
    jTextPane1.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel1.setBounds(new Rectangle(14, 7, 280, 25));
    jLabel1.setText("Fachhochschule Mannheim");
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 18));
    jTextPane2.setBounds(new Rectangle(14, 140, 147, 50));
    jTextPane2.setFont(new java.awt.Font("Dialog", 0, 12));
    labTitel.setBounds(new Rectangle(323, 240, 57, 15));
    labTitel.setText("88888");
    labTitel.setFont(new java.awt.Font("Dialog", 0, 12));
    labUT.setBounds(new Rectangle(404, 240, 40, 15));
    labUT.setText("34");
    labUT.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel8.setBounds(new Rectangle(14, 240, 212, 15));
    jLabel8.setText("zu belastender Haushaltstitel: Kapitel:");
    jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
    labFachbereich.setBounds(new Rectangle(416, 214, 125, 15));
    labFachbereich.setText("");
    labFachbereich.setFont(new java.awt.Font("Dialog", 0, 12));
    labKoSt.setBounds(new Rectangle(311, 188, 230, 15));
    labKoSt.setText("");
    labKoSt.setFont(new java.awt.Font("Dialog", 0, 12));
    labKostenstelle.setBounds(new Rectangle(95, 188, 98, 15));
    labKostenstelle.setText("");
    labKostenstelle.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel4.setBounds(new Rectangle(14, 188, 78, 15));
    jLabel4.setText("Kostenstelle:");
    jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
    this.setTitle("Drucktest");
    this.getContentPane().setLayout(null);
    jLabel2.setFont(new java.awt.Font("Dialog", 3, 16));
    jLabel2.setText("Beilage zur Bestellung bei ASKnet AG");
    jLabel2.setBounds(new Rectangle(15, 111, 301, 23));
    jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel6.setText("Software-beauftragte/r:");
    jLabel6.setBounds(new Rectangle(14, 214, 135, 15));
    labSoftwarebeauftragter.setFont(new java.awt.Font("Dialog", 0, 12));
    labSoftwarebeauftragter.setText("");
    labSoftwarebeauftragter.setBounds(new Rectangle(146, 214, 161, 15));
    jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel9.setText("Titel:");
    jLabel9.setBounds(new Rectangle(286, 240, 33, 15));
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setText("UT:");
    jLabel10.setBounds(new Rectangle(375, 240, 28, 15));
    jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel5.setText("KostenstelleNr.:");
    jLabel5.setBounds(new Rectangle(217, 188, 97, 15));
    jLabel111.setBounds(new Rectangle(9, 155, 40, 15));
    jLabel111.setText("Datum:");
    jLabel111.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel19.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel19.setText("Bestellung durchgeführt und obige Leistungen erhalten");
    jLabel19.setBounds(new Rectangle(8, 120, 315, 15));
    jLabel12.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel12.setText("Unterschrift:");
    jLabel12.setBounds(new Rectangle(209, 12, 77, 15));
    unten.setBackground(Color.white);
    unten.setBounds(new Rectangle(0, 542, 547, 168));
    unten.setLayout(null);
    labUnterschrift1.setBounds(new Rectangle(287, 155, 177, 15));
    labUnterschrift1.setText("");
    labUnterschrift1.setFont(new java.awt.Font("Dialog", 0, 12));
    labDatum1.setBounds(new Rectangle(56, 155, 131, 15));
    labDatum1.setText("01.01.2004");
    labDatum1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel16.setBorder(BorderFactory.createLineBorder(Color.black));
    jLabel16.setText("");
    jLabel16.setBounds(new Rectangle(355, 18, 182, 41));
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel11.setText("Datum:");
    jLabel11.setBounds(new Rectangle(12, 12, 40, 15));
    jLabel13.setText("Ist von der Technischen Betriebsleitung auszufüllen!");
    jLabel13.setBounds(new Rectangle(5, 0, 268, 15));
    jLabel18.setFont(new java.awt.Font("Dialog", 0, 10));
    jLabel18.setText("Unterschrift");
    jLabel18.setBounds(new Rectangle(362, 39, 68, 15));
    jLabel17.setFont(new java.awt.Font("Dialog", 0, 10));
    jLabel17.setText("Datum");
    jLabel17.setBounds(new Rectangle(189, 40, 40, 15));
    labDatum.setFont(new java.awt.Font("Dialog", 0, 12));
    labDatum.setText("01.01.2004");
    labDatum.setBounds(new Rectangle(59, 12, 131, 15));
    jLabel14.setBorder(BorderFactory.createLineBorder(Color.black));
    jLabel14.setText("");
    jLabel14.setBounds(new Rectangle(3, 18, 177, 41));
    jLabel110.setBounds(new Rectangle(206, 155, 77, 15));
    jLabel110.setText("Unterschrift:");
    jLabel110.setFont(new java.awt.Font("Dialog", 0, 12));
    jTextPane3.setBackground(Color.lightGray);
    jTextPane3.setFont(new java.awt.Font("Dialog", 0, 10));
    jTextPane3.setText("Auftrags- nummer");
    jTextPane3.setBounds(new Rectangle(7, 22, 59, 34));
    jPanel2.setBackground(Color.lightGray);
    jPanel2.setBounds(new Rectangle(7, 38, 539, 64));
    jPanel2.setLayout(null);
    jLabel15.setBorder(BorderFactory.createLineBorder(Color.black));
    jLabel15.setText("");
    jLabel15.setBounds(new Rectangle(179, 18, 177, 41));
    labUnterschrift.setFont(new java.awt.Font("Dialog", 0, 12));
    labUnterschrift.setText("");
    labUnterschrift.setBounds(new Rectangle(290, 12, 177, 15));
		printPanel.setBackground(Color.white);
    jScrollPane1.getViewport().add(tableBestellung);
    jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    jScrollPane1.getViewport().setBackground(Color.white);
    jScrollPane1.setBorder(BorderFactory.createEmptyBorder());
    jScrollPane1.setBounds(new Rectangle(0, 270, 547, 266));
    tableBestellung.setFont(new java.awt.Font("Dialog", 0, 10));
    printPanel.setBounds(new Rectangle(4, 5, 547, 714));
		printPanel.setLayout(null);
    jPanel2.add(jLabel13, null);
    jPanel2.add(jLabel18, null);
    jPanel2.add(jLabel17, null);
    jPanel2.add(jTextPane3, null);
    jPanel2.add(jLabel14, null);
    jPanel2.add(jLabel15, null);
    jPanel2.add(jLabel16, null);
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
    oben.add(jTextPane1, null);
    oben.add(jTextPane2, null);
    oben.add(jLabel3, null);
    oben.add(jLabel4, null);
    oben.add(labKostenstelle, null);
    oben.add(jLabel5, null);
    oben.add(labKoSt, null);
    oben.add(jLabel6, null);
    oben.add(labSoftwarebeauftragter, null);
    oben.add(labFachbereich, null);
    oben.add(jLabel7, null);
    oben.add(jLabel8, null);
    oben.add(labKapitel, null);
    oben.add(jLabel9, null);
    oben.add(labTitel, null);
    oben.add(jLabel10, null);
    oben.add(labUT, null);
    oben.add(jLabel2, null);
    printPanel.add(jScrollPane1, null);
    printPanel.add(unten, null);
    printPanel.add(oben, null);
    this.getContentPane().add(printPanel, null);

  }

  private void createBestellung(){
  	insertText(jTextPane1.getDocument(), "Hochschule für Technik und Gestaltung \nWindeckstraße 110 \n68163 Mannheim");
		insertText(jTextPane2.getDocument(), "Vincenz-Prießnutz-Str. 3 \n76131 Karlsruhe");

	  labKostenstelle.setText("");
	  labKoSt.setText("");

	  labSoftwarebeauftragter.setText("");
	  labFachbereich.setText("");

	  labKapitel.setText("");
	  labTitel.setText("");
	  labUT.setText("");

	  labDatum.setText("");
	  labUnterschrift.setText("");
		  labDatum1.setText("");
		  labUnterschrift1.setText("");
	}

  private void createTable(){
		String[] cols = {"Menge", "Produkt", "Einzelpreis", "Gesamtpreis", "für Institut"};
		String[][] data = new String[8][cols.length];

		for(int i = 0; i < 8; i++){
			data[i][0] = "" + i;
			data[i][1] = "Bechtle ÖA direkt, Neckersulm";
			data[i][2] = "91,87";
			data[i][3] = "91,87";
			data[i][4] = "FBI";
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
		tableBestellung.getTableHeader().setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		render.setVerticalAlignment(DefaultTableCellRenderer.TOP);
		render.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		tableBestellung.getColumnModel().getColumn(0).setCellRenderer(new MultiLineRenderer()); tableBestellung.getColumnModel().getColumn(0).setMaxWidth(50);
		tableBestellung.getColumnModel().getColumn(1).setCellRenderer(new MultiLineRenderer());
		tableBestellung.getColumnModel().getColumn(1).setMaxWidth(300);
		tableBestellung.getColumnModel().getColumn(2).setCellRenderer(render); tableBestellung.getColumnModel().getColumn(2).setMaxWidth(80);
		tableBestellung.getColumnModel().getColumn(3).setCellRenderer(render); tableBestellung.getColumnModel().getColumn(3).setMaxWidth(100);
		tableBestellung.getColumnModel().getColumn(4).setCellRenderer(new MultiLineRenderer()); tableBestellung.getColumnModel().getColumn(4).setMaxWidth(120);

		tableBestellung.setGridColor(Color.white);
		tableBestellung.setRowHeight(30);
		tableBestellung.setEnabled(false);

		jScrollPane1.getViewport().add(tableBestellung);
		jScrollPane1.setBounds(new Rectangle(0, 268, 547, (tableBestellung.getModel().getRowCount() * 30)+20));
		unten.setBounds(new Rectangle(0, 274 + (tableBestellung.getModel().getRowCount() *30)+20, 547, 200));
		printPanel.setBounds(4,5,547, oben.getHeight() + tableBestellung.getTableHeader().getHeight() + tableBestellung.getModel().getRowCount() * 30 + unten.getHeight() + 50);

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

	private void insertText(Document doc, String string) {
		try {
			doc.insertString(doc.getLength(), string, null);
		}catch(Throwable t) {
			t.printStackTrace();
		}
  }
}