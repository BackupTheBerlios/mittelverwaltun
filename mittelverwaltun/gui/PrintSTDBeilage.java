package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;

import dbObjects.Angebot;
import dbObjects.StandardBestellung;
import dbObjects.ZVTitel;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;


public class PrintSTDBeilage extends JFrame implements Printable {
  JLabel jLabel4 = new JLabel();
  JLabel jLabel8 = new JLabel();
  JLabel labKoSt = new JLabel();
  JLabel jLabel12 = new JLabel();
  JLabel jLabel1 = new JLabel();
  JTextPane jTextPane1 = new JTextPane();
  JLabel jLabel13 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JPanel oben = new JPanel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel9 = new JLabel();
  JLabel labKostenstelle = new JLabel();
  JLabel labInventarNr = new JLabel();
  JLabel labTitel = new JLabel();
  JLabel labUT = new JLabel();
  JLabel labErsatzText = new JLabel();
  JLabel jLabel11 = new JLabel();
  JLabel labKapitel = new JLabel();
  JCheckBox cbReparatur = new JCheckBox();
  JLabel jLabel7 = new JLabel();
  JCheckBox cbVerbrauchsmaterial = new JCheckBox();
  JLabel labBestellNr = new JLabel();
  JPanel jPanel1 = new JPanel();
  JLabel jLabel6 = new JLabel();
  JTextPane tpBegruendung = new JTextPane();
  JCheckBox cbErstbeschaffung = new JCheckBox();
  JCheckBox cbInvestitionen = new JCheckBox();
  JCheckBox cbDrittelMittel = new JCheckBox();
  JCheckBox cbErsatz = new JCheckBox();
  JLabel jLabel10 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JCheckBox cbAngebotGuenstig = new JCheckBox();
  JCheckBox cbAuftragGrund = new JCheckBox();
  JTextPane tpAuftragGrund = new JTextPane();
  JLabel jLabel14 = new JLabel();
  JPanel unten = new JPanel();
  JLabel labAngebotNr = new JLabel();
  JLabel jLabel15 = new JLabel();
  JLabel jLabel21 = new JLabel();
  JLabel jLabel20 = new JLabel();
  JLabel labName = new JLabel();
  JPanel unterschrift = new JPanel();
  JLabel labUnterschrift = new JLabel();
  JLabel jLabel19 = new JLabel();
  JLabel jLabel16 = new JLabel();
  JLabel jLabel18 = new JLabel();
  JLabel jLabel17 = new JLabel();
  JLabel labUnterschrift2 = new JLabel();
  JLabel labDatum = new JLabel();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTable tableBestellung = new JTable();
  JPanel printPanel = new JPanel();
  StandardBestellung order;

  public PrintSTDBeilage(StandardBestellung order) {
  	this.order = order;
  	try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
		createBestellung(order);
    createTable(order.getAngebote());
  }

  private void jbInit() throws Exception {
		this.setSize(new Dimension(564, 785));
    oben = new JPanel(){
			public void paintComponent(Graphics g) {
			  super.paintComponent(g);
			  g.drawLine(341, 90, 493, 90);
			  g.drawLine(249, 166, 304, 166);
			  g.drawLine(249, 166, 304, 166);
			  g.drawLine(368, 166, 420, 166);
			  g.drawLine(470, 166, 510, 166);
			  g.drawLine(87, 220, 389, 220);
			  g.drawLine(473, 220, 554, 220);
			}
		};
    tpAuftragGrund = new JTextPane(){
			public void paintComponent(Graphics g) {
			  super.paintComponent(g);
			  g.drawLine(240, 20, 600, 20);
			  g.drawLine(0, 45, 600, 45);
			}
		};
    unten = new JPanel(){
			public void paintComponent(Graphics g) {
			  super.paintComponent(g);
			  g.drawLine(183, 22, 224, 22);
			}
		 };
    unterschrift = new JPanel(){
      public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(52, 20, 120, 20);
		  	g.drawLine(191, 20, 289, 20);
				g.drawLine(382, 20, 555, 20);
				g.drawLine(159, 60, 332, 60);
      }
    };
    oben.setLayout(null);
    oben.setBounds(new Rectangle(2, 2, 549, 434));
    oben.setBackground(Color.white);
    jLabel3.setBounds(new Rectangle(9, 75, 351, 15));
    jLabel3.setText("Die bei der Bestellung aufgeführten Gegenstände werden für");
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel13.setBounds(new Rectangle(406, 412, 131, 15));
    jLabel13.setText("(Angebote bitte beilegen!)");
    jLabel13.setFont(new java.awt.Font("Dialog", 0, 10));
    jTextPane1.setBounds(new Rectangle(8, 5, 531, 40));
    jTextPane1.setText("Begründung für die Notwendigkeit der Beschaffung und der zweckentsprechenden " +
    "Verwendung gemäß 4.1 Beschaffungsrichtlinien.");
    jTextPane1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel1.setBounds(new Rectangle(30, 5, 513, 15));
    jLabel1.setText("Fachhoschschule Mannheim - Hochschule für Technik und Gestaltung");
    jLabel1.setFont(new java.awt.Font("Dialog", 1, 15));
    jLabel12.setBounds(new Rectangle(6, 411, 394, 15));
    jLabel12.setText("Angebote / Preisvergleiche wurden bei folgenden Firmen eingeholt:");
    jLabel12.setFont(new java.awt.Font("Dialog", 1, 12));
    labKoSt.setText("  KoSt");
    labKoSt.setBounds(new Rectangle(432, 36, 112, 23));
    labKoSt.setFont(new java.awt.Font("Dialog", 0, 12));
    labKoSt.setToolTipText("");
    labKoSt.setBorder(BorderFactory.createLineBorder(Color.black));
    jLabel8.setBounds(new Rectangle(394, 205, 75, 15));
    jLabel8.setText("Inventar-Nr.:");
    jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel4.setBounds(new Rectangle(494, 76, 56, 15));
    jLabel4.setText("beschaft.");
    jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
    this.getContentPane().setLayout(null);
    jLabel2.setFont(new java.awt.Font("Dialog", 3, 20));
    jLabel2.setText("Beilage zur Bestellung");
    jLabel2.setBounds(new Rectangle(23, 31, 234, 31));
    jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel9.setText("Kapitel");
    jLabel9.setBounds(new Rectangle(206, 151, 53, 15));
    labKostenstelle.setFont(new java.awt.Font("Dialog", 0, 12));
    labKostenstelle.setHorizontalAlignment(SwingConstants.CENTER);
    labKostenstelle.setHorizontalTextPosition(SwingConstants.CENTER);
    labKostenstelle.setText("");
    labKostenstelle.setBounds(new Rectangle(346, 75, 144, 15));
    labInventarNr.setFont(new java.awt.Font("Dialog", 0, 12));
    labInventarNr.setBorder(null);
    labInventarNr.setText("");
    labInventarNr.setBounds(new Rectangle(473, 205, 81, 15));
    labTitel.setFont(new java.awt.Font("Dialog", 0, 12));
    labTitel.setHorizontalAlignment(SwingConstants.CENTER);
    labTitel.setText("");
    labTitel.setBounds(new Rectangle(368, 151, 52, 15));
    labUT.setFont(new java.awt.Font("Dialog", 0, 12));
    labUT.setHorizontalAlignment(SwingConstants.CENTER);
    labUT.setText("");
    labUT.setBounds(new Rectangle(470, 151, 40, 15));
    labErsatzText.setFont(new java.awt.Font("Dialog", 0, 12));
    labErsatzText.setBorder(null);
    labErsatzText.setText("");
    labErsatzText.setBounds(new Rectangle(87, 205, 302, 15));
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel11.setText("Titel");
    jLabel11.setBounds(new Rectangle(339, 151, 40, 15));
    labKapitel.setFont(new java.awt.Font("Dialog", 0, 12));
    labKapitel.setHorizontalAlignment(SwingConstants.CENTER);
    labKapitel.setText("");
    labKapitel.setBounds(new Rectangle(249, 151, 55, 15));
    cbReparatur.setBackground(Color.white);
    cbReparatur.setFont(new java.awt.Font("Dialog", 0, 12));
    cbReparatur.setText("Reparatur");
    cbReparatur.setBounds(new Rectangle(294, 125, 83, 23));
    cbReparatur.setFocusable(false);
    jLabel7.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel7.setText("zu belastender Haushaltstitel:");
    jLabel7.setBounds(new Rectangle(9, 151, 185, 15));
    cbVerbrauchsmaterial.setBackground(Color.white);
    cbVerbrauchsmaterial.setFont(new java.awt.Font("Dialog", 0, 12));
    cbVerbrauchsmaterial.setText("Verbrauchersmaterial");
    cbVerbrauchsmaterial.setBounds(new Rectangle(394, 125, 157, 23));
    cbVerbrauchsmaterial.setFocusable(false);
    labBestellNr.setText(" Best.");
    labBestellNr.setBounds(new Rectangle(304, 36, 129, 23));
    labBestellNr.setFont(new java.awt.Font("Dialog", 0, 12));
    labBestellNr.setBorder(BorderFactory.createLineBorder(Color.black));
    jPanel1.setBackground(Color.white);
    jPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
    jPanel1.setBounds(new Rectangle(0, 238, 544, 168));
    jPanel1.setLayout(null);
    jLabel6.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel6.setText("Kostenart:");
    jLabel6.setBounds(new Rectangle(9, 118, 124, 15));
    tpBegruendung.setFont(new java.awt.Font("Dialog", 0, 12));
    tpBegruendung.setText("");
    tpBegruendung.setBounds(new Rectangle(12, 46, 527, 90));
    cbErstbeschaffung.setBackground(Color.white);
    cbErstbeschaffung.setFont(new java.awt.Font("Dialog", 0, 12));
    cbErstbeschaffung.setText("Erstbeschaffung");
    cbErstbeschaffung.setBounds(new Rectangle(4, 177, 120, 23));
    cbErstbeschaffung.setFocusable(false);
    cbInvestitionen.setBackground(Color.white);
    cbInvestitionen.setFont(new java.awt.Font("Dialog", 0, 12));
    cbInvestitionen.setText("Investitionen");
    cbInvestitionen.setBounds(new Rectangle(172, 124, 104, 23));
    cbInvestitionen.setFocusable(false);
    cbDrittelMittel.setBackground(Color.white);
    cbDrittelMittel.setFont(new java.awt.Font("Dialog", 0, 11));
    cbDrittelMittel.setText("Dritt- / HBFG-Mittel: Die Bestellung entspricht dem vorgelegten Finanzierungs- " +
    "/ Ausstattungsplan");
    cbDrittelMittel.setBounds(new Rectangle(3, 140, 498, 23));
    cbDrittelMittel.setFocusable(false);
    cbErsatz.setBackground(Color.white);
    cbErsatz.setFont(new java.awt.Font("Dialog", 0, 12));
    cbErsatz.setText("Ersatz für:");
    cbErsatz.setBounds(new Rectangle(4, 201, 83, 23));
    cbErsatz.setFocusable(false);
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel10.setText("UT");
    jLabel10.setBounds(new Rectangle(448, 151, 33, 15));
    jLabel5.setFont(new java.awt.Font("Dialog", 0, 9));
    jLabel5.setText("(Kostenstelle)");
    jLabel5.setBounds(new Rectangle(386, 92, 68, 15));
    cbAngebotGuenstig.setBackground(Color.white);
    cbAngebotGuenstig.setFont(new java.awt.Font("Dialog", 0, 12));
    cbAngebotGuenstig.setText("das preisgünstigste und wirtschaftlichste Angebot abgegeben hat.");
    cbAngebotGuenstig.setBounds(new Rectangle(4, 28, 394, 23));
    cbAngebotGuenstig.setFocusable(false);
    cbAuftragGrund.setBackground(Color.white);
    cbAuftragGrund.setText("");
    cbAuftragGrund.setBounds(new Rectangle(4, 53, 24, 23));
    cbAuftragGrund.setFocusable(false);
    tpAuftragGrund.setFont(new java.awt.Font("Dialog", 0, 12));
    tpAuftragGrund.setText("aus folgenden Grund bevorzugt wurden:");
    tpAuftragGrund.setBounds(new Rectangle(25, 51, 516, 51));
    jLabel14.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel14.setText("Der Auftrag wird der oben unter");
    jLabel14.setBounds(new Rectangle(7, 8, 179, 15));
    unten.setBackground(Color.white);
    unten.setBounds(new Rectangle(1, 530, 545, 107));
    unten.setLayout(null);
    labAngebotNr.setFont(new java.awt.Font("Dialog", 0, 12));
    labAngebotNr.setBorder(null);
    labAngebotNr.setHorizontalAlignment(SwingConstants.CENTER);
    labAngebotNr.setText("");
    labAngebotNr.setBounds(new Rectangle(188, 8, 41, 15));
    jLabel15.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel15.setText("genannten Firma erteilt, da diese Firma");
    jLabel15.setBounds(new Rectangle(240, 8, 221, 15));
    jLabel21.setFont(new java.awt.Font("Dialog", 0, 10));
    jLabel21.setRequestFocusEnabled(true);
    jLabel21.setText("(Datum, Unterschift)");
    jLabel21.setBounds(new Rectangle(191, 61, 109, 15));
    jLabel20.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel20.setText("Beschaffungsbeauftragter:");
    jLabel20.setBounds(new Rectangle(5, 47, 154, 15));
    labName.setFont(new java.awt.Font("Dialog", 0, 12));
    labName.setText("");
    labName.setBounds(new Rectangle(191, 7, 98, 15));
    unterschrift.setBackground(Color.white);
    unterschrift.setBounds(new Rectangle(-2, 689, 550, 78));
    unterschrift.setLayout(null);
    labUnterschrift.setBorder(null);
    labUnterschrift.setText("");
    labUnterschrift.setBounds(new Rectangle(382, 7, 151, 15));
    jLabel19.setFont(new java.awt.Font("Dialog", 0, 10));
    jLabel19.setText("(Leiter der Hochschuleinrichtung, Projektleiter etc.)");
    jLabel19.setBounds(new Rectangle(313, 21, 249, 15));
    jLabel16.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel16.setText("Datum:");
    jLabel16.setBounds(new Rectangle(6, 7, 40, 15));
    jLabel18.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel18.setText("Unterschrift:");
    jLabel18.setBounds(new Rectangle(305, 7, 79, 15));
    jLabel17.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel17.setText("Name:");
    jLabel17.setBounds(new Rectangle(142, 7, 40, 15));
    labUnterschrift2.setBounds(new Rectangle(159, 47, 173, 15));
    labUnterschrift2.setText("");
    labDatum.setFont(new java.awt.Font("Dialog", 0, 12));
    labDatum.setBounds(new Rectangle(55, 7, 73, 15));
    jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		jScrollPane1.setBorder(BorderFactory.createEmptyBorder());
    jScrollPane1.getViewport().setBackground(Color.white);
    jScrollPane1.setBorder(null);
    jScrollPane1.setBounds(new Rectangle(3, 433, 548, 93));
    this.getContentPane().setBackground(Color.white);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setTitle("Beilage zur Bestellung");
    printPanel.setBackground(Color.white);
		printPanel.setBounds(new Rectangle(3, 4, 562, 772));
		printPanel.setLayout(null);
    oben.add(jLabel1, null);
    oben.add(labKoSt, null);
    oben.add(labBestellNr, null);
    oben.add(labKostenstelle, null);
    oben.add(jLabel5, null);
    oben.add(cbReparatur, null);
    oben.add(cbInvestitionen, null);
    oben.add(jLabel6, null);
    oben.add(jLabel7, null);
    oben.add(jLabel9, null);
    oben.add(jLabel11, null);
    oben.add(jLabel10, null);
    oben.add(labErsatzText, null);
    oben.add(labInventarNr, null);
    oben.add(jLabel8, null);
    oben.add(labKapitel, null);
    oben.add(labTitel, null);
    oben.add(labUT, null);
    oben.add(jPanel1, null);
    jPanel1.add(jTextPane1, null);
    jPanel1.add(tpBegruendung, null);
    jPanel1.add(cbDrittelMittel, null);
    oben.add(jLabel2, null);
    oben.add(jLabel3, null);
    oben.add(cbErstbeschaffung, null);
    oben.add(cbErsatz, null);
    oben.add(jLabel4, null);
    oben.add(cbVerbrauchsmaterial, null);
    oben.add(jLabel13, null);
    oben.add(jLabel12, null);
    printPanel.add(unterschrift, null);
    unten.add(cbAngebotGuenstig, null);
    unten.add(cbAuftragGrund, null);
    unten.add(jLabel14, null);
    unten.add(labAngebotNr, null);
    unten.add(jLabel15, null);
    unten.add(tpAuftragGrund, null);
    unterschrift.add(jLabel16, null);
    unterschrift.add(labDatum, null);
    unterschrift.add(jLabel17, null);
    unterschrift.add(labName, null);
    unterschrift.add(labUnterschrift, null);
    unterschrift.add(jLabel18, null);
    unterschrift.add(jLabel20, null);
    unterschrift.add(labUnterschrift2, null);
    unterschrift.add(jLabel21, null);
    unterschrift.add(jLabel19, null);
		printPanel.add(jScrollPane1, null);
    printPanel.add(unten, null);
    jScrollPane1.getViewport().add(tableBestellung, null);
		printPanel.add(oben, null);
    this.getContentPane().add(printPanel, null);
  }

  private void createTable(ArrayList angebote){
		 String[] cols = {"Nr.", "Firma, Ort", "Angebot vom", "Auftr.-Summe inkl. MwSt."};
		 Object[][] data = new Object[4][cols.length];

		 for(int i = 0; i < angebote.size(); i++){
		 	 Angebot a = (Angebot)angebote.get(i);
			 data[i][0] = new Integer(i+1);
			 data[i][1] = a.getAnbieter().toString();
			 data[i][2] = a.getDatum();
			 data[i][3] = new Float(a.getSumme());
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
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		tableBestellung.setDefaultRenderer(Integer.class, dtcr);

		tableBestellung.getColumnModel().getColumn(0).setMaxWidth(20);
 		tableBestellung.getColumnModel().getColumn(1).setMaxWidth(300);
 		tableBestellung.getColumnModel().getColumn(2).setMaxWidth(100);
 		tableBestellung.getColumnModel().getColumn(3).setMaxWidth(170);
		 //tableBestellung.setGridColor(new Color(255,255,255));

		 //tableBestellung.setRowHeight(40);
		 tableBestellung.setEnabled(false);

			jScrollPane1.getViewport().add(tableBestellung);
			jScrollPane1.setBounds(new Rectangle(0, 440, 548, (tableBestellung.getModel().getRowCount() * 15)+20));
			unten.setBounds(new Rectangle(0, 380 + (tableBestellung.getModel().getRowCount() *30)+20, 547, 107));
//			unterschrift.setBounds(new Rectangle(0, 405 + (tableBestellung.getModel().getRowCount() *30) + 107, 537, 78));
//			printPanel.setBounds(4,5,547, oben.getHeight() + tableBestellung.getTableHeader().getHeight() + tableBestellung.getModel().getRowCount() * 30 + unten.getHeight() + 60);
			printPanel.setBounds(4,5,547, 800);

	 }

	 private void insertText(Document doc, String string) {
		try {
			doc.insertString(doc.getLength(), string, null);
		}catch(Throwable t) {
			t.printStackTrace();
		}
  }

  private void createBestellung(StandardBestellung order){
	  labBestellNr.setText("Best. " + order.getReferenznr());
	  labKoSt.setText(" Kost: " + order.getFbkonto().getInstitut().getKostenstelle());
	  labKostenstelle.setText(order.getFbkonto().getInstitut().getBezeichnung());

	  // Kostenart
	  switch(order.getKostenart().getId()){
		  case 1: {
							  cbInvestitionen.setSelected(true);
							  break;
						  }
		  case 2: {
							  cbReparatur.setSelected(true);
							  break;
						  }
		  case 3: {
							  cbVerbrauchsmaterial.setSelected(true);
							  break;
						  }
	  }

	  // Haushaltstitel
	  labKapitel.setText(order.getZvtitel().getZVTitel() != null ? order.getZvtitel().getZVTitel().getZVKonto().getKapitel() : ((ZVTitel)order.getZvtitel()).getZVKonto().getKapitel());
	  labTitel.setText(order.getZvtitel().getTitel());
	  labUT.setText(order.getZvtitel().getUntertitel());

	  // Erstbeschaffung
	  if(!order.getErsatzbeschaffung()){
		  cbErstbeschaffung.setSelected(true);
	  }else{
		  cbErsatz.setSelected(true);
		  labErsatzText.setText(order.getErsatzbeschreibung());
		  labInventarNr.setText(order.getInventarNr());
	  }

	  // Begründung
	  tpBegruendung.setText(order.getVerwendungszweck());
	  cbDrittelMittel.setSelected(order.getPlanvorgabe());

	  // Auftrag
	  labAngebotNr.setText("" + (1 + order.getAngenommenesAngebot()));

	  // Entscheidung für
	 if(order.getBegruendung().equals(""))
		  cbAngebotGuenstig.setSelected(true);
	  else{
		  cbAuftragGrund.setSelected(true);
		  tpAuftragGrund.setText("aus folgenden Grund bevorzugt wurden:     " + order.getBegruendung());
	  }

	  //Rest
	  labDatum.setText("" + order.getDatum());
	  labName.setText(order.getAuftraggeber().getName());
  }

	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		Graphics2D g2d = (Graphics2D) graphics;

		double pageHeight = pageFormat.getImageableHeight();
		double pageWidth = pageFormat.getImageableWidth();

		// Height of all components
		int heightAll =  printPanel.getHeight();

		int totalNumPages= (int)Math.ceil(heightAll / pageHeight);

//		if(pageIndex  >= totalNumPages) {
//			return NO_SUCH_PAGE;
//		}else{
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
//			g2d.setClip(0, pageIndex * (int)pageFormat.getImageableHeight(), 560, 900);
//			if(totalNumPages > 1)
//				g2d.drawString("Seite: " + (pageIndex+1) + " von " + totalNumPages,( int)pageWidth/2 - 35, 790);//bottom center

			g2d.translate(0f, 0);
			//g2d.translate(0f, pageIndex * pageFormat.getImageableHeight());
//			g2d.setClip(0, pageIndex * (int)pageFormat.getImageableHeight() - 5, 560, 780);
			g2d.setClip(0, 0, 560, 780);

			printPanel.printAll(g2d);

			return Printable.PAGE_EXISTS;
//		}
	}
}