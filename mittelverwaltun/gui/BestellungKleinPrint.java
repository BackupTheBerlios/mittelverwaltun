package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.text.*;
import java.util.*;

import dbObjects.*;


/**
 * Klasse zum Drucken der KleinBestellung = Auszahlungsanordnung
 * @author w.flat
 * 04.03.2005
 */
public class BestellungKleinPrint extends JFrame implements Printable {
	
	JPanel printPanel = new JPanel();
	JLabel labFHMannheim = new JLabel();
	JLabel labProjektNrText = new JLabel();
	JPanel oben = new JPanel();
	JLabel labHochschuleTUG = new JLabel();
	JLabel labProjektNr = new JLabel();
	JLabel labKapitelText = new JLabel();
	JLabel labUT = new JLabel();
	JLabel labTitel = new JLabel();
	JLabel labTitelText = new JLabel();
	JLabel labKapitel = new JLabel();
	JLabel labUntertitelText = new JLabel();
	JScrollPane scrollBestellung = new JScrollPane();
	JTable tableBestellung = new JTable();
	Border border1;
	JLabel labEingetragen = new JLabel();
	JLabel labGesamtText = new JLabel();
	JLabel labLieferung = new JLabel();
	JPanel panelAuszahlungAn = new JPanel();
	JLabel labAuszahlungAnText = new JLabel();
	JLabel labGesamt = new JLabel();
	JLabel labName = new JLabel();
	JTextPane tpVerwendung = new JTextPane();
	JPanel panelLieferung = new JPanel();
	JLabel labTitlVerzText = new JLabel();
	JPanel panelVerwendung = new JPanel();
	JLabel labUnterschrift = new JLabel();
	JTextPane tpAuszahlung = new JTextPane();
	JLabel labRegierungsdirektor = new JLabel();
	JPanel unten = new JPanel();
	JLabel labDatumText = new JLabel();
	JLabel labUnterschriftText = new JLabel();
	JLabel labLabor = new JLabel();
	JLabel labNameText = new JLabel();
	JLabel labKartei = new JLabel();
	JLabel labAuszahlungAn = new JLabel();
	JLabel labTitlVerz = new JLabel();
	JPanel panelKartei = new JPanel();
	JLabel labDatum = new JLabel();
	JLabel labMaterial = new JLabel();
	JLabel labKarteiNr = new JLabel();
	JLabel labAuszahlungsanordnung = new JLabel();
	JLabel labVerwendung = new JLabel();
	KleinBestellung bestellung;
	
	public BestellungKleinPrint(KleinBestellung bestellung) {
		this.bestellung = bestellung;
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
		createBestellung();
		createTable();
	}
	
	private void jbInit() throws Exception {
		unten = new JPanel() {
								public void paintComponent(Graphics g) {
									super.paintComponent(g);
									g.drawLine(307, 241, 422, 241);
								}
							};
		oben = new JPanel() {
								public void paintComponent(Graphics g) {
									super.paintComponent(g);
									g.drawLine(314, 23, 369, 23);
									g.drawLine(404, 23, 456, 23);
									g.drawLine(488, 23, 528, 23);
									g.drawLine(443, 48, 528, 48);
								}
		 };
		this.setSize(new Dimension(580, 727));
		border1 = BorderFactory.createEmptyBorder();
		this.getContentPane().setBackground(Color.white);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("Kleinbestellung");
		this.getContentPane().setLayout(null);
		printPanel.setBackground(Color.white);
		printPanel.setBounds(new Rectangle(11, 8, 548, 743));
		printPanel.setLayout(null);
		labFHMannheim.setEnabled(true);
		labFHMannheim.setFont(new java.awt.Font("Dialog", 1, 16));
		labFHMannheim.setText("Fachhochschule Mannheim");
		labFHMannheim.setBounds(new Rectangle(6, 6, 225, 15));
		labProjektNrText.setFont(new java.awt.Font("Dialog", 0, 12));
		labProjektNrText.setText("Projekt-Nr.");
		labProjektNrText.setBounds(new Rectangle(374, 33, 67, 15));
		oben.setBackground(Color.white);
		oben.setBounds(new Rectangle(3, 3, 549, 56));
		oben.setLayout(null);
		labHochschuleTUG.setFont(new java.awt.Font("Dialog", 0, 12));
		labHochschuleTUG.setText("Hochschule für Technik und Gestaltung");
		labHochschuleTUG.setBounds(new Rectangle(6, 27, 230, 15));
		labProjektNr.setFont(new java.awt.Font("Dialog", 0, 12));
		labProjektNr.setText("");
		labProjektNr.setBounds(new Rectangle(443, 33, 85, 15));
		labKapitelText.setFont(new java.awt.Font("Dialog", 0, 12));
		labKapitelText.setText("Kapitel");
		labKapitelText.setBounds(new Rectangle(274, 8, 53, 15));
		labUT.setFont(new java.awt.Font("Dialog", 0, 12));
		labUT.setHorizontalAlignment(SwingConstants.CENTER);
		labUT.setText("56");
		labUT.setBounds(new Rectangle(488, 8, 40, 15));
		labTitel.setFont(new java.awt.Font("Dialog", 0, 12));
		labTitel.setHorizontalAlignment(SwingConstants.CENTER);
		labTitel.setText("64875");
		labTitel.setBounds(new Rectangle(404, 8, 52, 15));
		labTitelText.setFont(new java.awt.Font("Dialog", 0, 12));
		labTitelText.setText("Titel");
		labTitelText.setBounds(new Rectangle(376, 8, 40, 15));
		labKapitel.setFont(new java.awt.Font("Dialog", 0, 12));
		labKapitel.setHorizontalAlignment(SwingConstants.CENTER);
		labKapitel.setText("02436");
		labKapitel.setBounds(new Rectangle(314, 8, 55, 15));
		labUntertitelText.setFont(new java.awt.Font("Dialog", 0, 12));
		labUntertitelText.setText("UT");
		labUntertitelText.setBounds(new Rectangle(466, 8, 33, 15));
		scrollBestellung.getViewport().setBackground(Color.white);
		scrollBestellung.setBorder(border1);
		scrollBestellung.setBounds(new Rectangle(4, 63, 532, 99));
		labEingetragen.setFont(new java.awt.Font("Dialog", 1, 11));
		labEingetragen.setText("eingetragen.");
		labEingetragen.setBounds(new Rectangle(422, 9, 75, 15));
		labGesamtText.setFont(new java.awt.Font("Arial", 1, 12));
		labGesamtText.setText("Gesamt");
		labGesamtText.setBounds(new Rectangle(387, 7, 53, 15));
		labLieferung.setFont(new java.awt.Font("Dialog", 1, 11));
		labLieferung.setText("Für richtige Lieferung und Ausführung");
		labLieferung.setBounds(new Rectangle(6, 5, 221, 15));
		panelAuszahlungAn.setLayout(null);
		panelAuszahlungAn.setBounds(new Rectangle(4, 188, 529, 32));
		panelAuszahlungAn.setBorder(BorderFactory.createLineBorder(Color.black));
		labAuszahlungAnText.setText("");
		labAuszahlungAnText.setFont(new java.awt.Font("Dialog", 1, 11));
		labAuszahlungAnText.setText("Auszahlung an:");
		labAuszahlungAnText.setBounds(new Rectangle(5, 9, 89, 15));
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
		panelLieferung.setLayout(null);
		panelLieferung.setBounds(new Rectangle(4, 135, 529, 54));
		panelLieferung.setBorder(BorderFactory.createLineBorder(Color.black));
		labTitlVerzText.setFont(new java.awt.Font("Dialog", 0, 11));
		labTitlVerzText.setText("Titl. Verz. Nr.:");
		labTitlVerzText.setBounds(new Rectangle(230, 225, 89, 15));
		panelVerwendung.setBorder(BorderFactory.createLineBorder(Color.black));
		panelVerwendung.setBounds(new Rectangle(4, 26, 529, 79));
		panelVerwendung.setLayout(null);
		labUnterschrift.setBorder(null);
		labUnterschrift.setText("");
		labUnterschrift.setBounds(new Rectangle(376, 30, 138, 15));
		tpAuszahlung.setBounds(new Rectangle(228, 269, 268, 248));
		labRegierungsdirektor.setFont(new java.awt.Font("Dialog", 0, 11));
		labRegierungsdirektor.setText("Regierungsdirektor");
		labRegierungsdirektor.setBounds(new Rectangle(275, 547, 147, 15));
		unten.setBackground(Color.white);
		unten.setBounds(new Rectangle(5, 168, 547, 567));
		unten.setLayout(null);
		labDatumText.setFont(new java.awt.Font("Dialog", 1, 11));
		labDatumText.setText("Datum:");
		labDatumText.setBounds(new Rectangle(5, 30, 40, 15));
		labUnterschriftText.setFont(new java.awt.Font("Dialog", 1, 11));
		labUnterschriftText.setText("Unterschrift:");
		labUnterschriftText.setBounds(new Rectangle(299, 30, 79, 15));
		labLabor.setFont(new java.awt.Font("Dialog", 1, 11));
		labLabor.setText("- Labor, Nr.");
		labLabor.setBounds(new Rectangle(244, 9, 68, 15));
		labNameText.setFont(new java.awt.Font("Dialog", 1, 11));
		labNameText.setText("Name:");
		labNameText.setBounds(new Rectangle(136, 30, 40, 15));
		labKartei.setFont(new java.awt.Font("Dialog", 1, 11));
		labKartei.setText("");
		labKartei.setBounds(new Rectangle(147, 9, 88, 15));
		labAuszahlungAn.setFont(new java.awt.Font("Dialog", 1, 11));
		labAuszahlungAn.setText("");
		labAuszahlungAn.setBounds(new Rectangle(93, 9, 277, 15));
		labTitlVerz.setText("");
		labTitlVerz.setBounds(new Rectangle(307, 226, 115, 15));
		panelKartei.setBorder(BorderFactory.createLineBorder(Color.black));
		panelKartei.setBounds(new Rectangle(4, 104, 529, 32));
		panelKartei.setLayout(null);
		labDatum.setFont(new java.awt.Font("Dialog", 1, 11));
		labDatum.setText("06.04.2004");
		labDatum.setBounds(new Rectangle(49, 30, 73, 15));
		labMaterial.setFont(new java.awt.Font("Dialog", 1, 11));
		labMaterial.setText("In Material/Geräte-Kartei");
		labMaterial.setBounds(new Rectangle(4, 9, 140, 15));
		labKarteiNr.setFont(new java.awt.Font("Dialog", 1, 11));
		labKarteiNr.setText("");
		labKarteiNr.setBounds(new Rectangle(314, 9, 99, 15));
		labAuszahlungsanordnung.setFont(new java.awt.Font("Dialog", 1, 14));
		labAuszahlungsanordnung.setText("Auszahlungsanordnung");
		labAuszahlungsanordnung.setBounds(new Rectangle(230, 249, 176, 20));
		labVerwendung.setFont(new java.awt.Font("Dialog", 1, 11));
		labVerwendung.setText("Verwendung oder Begründung");
		labVerwendung.setBounds(new Rectangle(5, 3, 187, 15));
		this.getContentPane().add(printPanel, null);
		oben.add(labFHMannheim, null);
		oben.add(labHochschuleTUG, null);
		oben.add(labTitel, null);
		oben.add(labUT, null);
		oben.add(labKapitelText, null);
		oben.add(labKapitel, null);
		oben.add(labTitelText, null);
		oben.add(labUntertitelText, null);
		oben.add(labProjektNr, null);
		oben.add(labProjektNrText, null);
		scrollBestellung.getViewport().add(tableBestellung, null);
		printPanel.add(scrollBestellung, null);
		printPanel.add(oben, null);
		unten.add(labGesamt, null);
		unten.add(labGesamtText, null);
		unten.add(panelVerwendung, null);
		panelVerwendung.add(labVerwendung, null);
		panelVerwendung.add(tpVerwendung, null);
		unten.add(panelKartei, null);
		panelKartei.add(labEingetragen, null);
		panelKartei.add(labMaterial, null);
		panelKartei.add(labKartei, null);
		panelKartei.add(labLabor, null);
		panelKartei.add(labKarteiNr, null);
		unten.add(panelLieferung, null);
		panelLieferung.add(labUnterschriftText, null);
		panelLieferung.add(labDatum, null);
		panelLieferung.add(labNameText, null);
		panelLieferung.add(labName, null);
		panelLieferung.add(labUnterschrift, null);
		panelLieferung.add(labDatumText, null);
		panelLieferung.add(labLieferung, null);
		unten.add(panelAuszahlungAn, null);
		panelAuszahlungAn.add(labAuszahlungAn, null);
		panelAuszahlungAn.add(labAuszahlungAnText, null);
		unten.add(labTitlVerz, null);
		unten.add(labTitlVerzText, null);
		unten.add(labAuszahlungsanordnung, null);
		unten.add(tpAuszahlung, null);
		unten.add(labRegierungsdirektor, null);
		printPanel.add(unten, null);
	}
	
	/**
	 * Die Felder des Bestell-Formulars beschriften. 
	 */
	private void createBestellung(){
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

		// Alle Labels beschriften
		labKapitel.setText(kapitel);
		labTitel.setText(titel);
		labUT.setText(untertitel);
		labProjektNr.setText(bestellung.getProjektNr());
		labGesamt.setText(NumberFormat.getCurrencyInstance().format(bestellung.getBestellwert()));
		tpVerwendung.setText(bestellung.getVerwendungszweck());
		labKartei.setText(bestellung.getLabor());
		labKarteiNr.setText(bestellung.getKartei());
		labDatum.setText(DateFormat.getDateInstance().format(bestellung.getDatum()));
		labName.setText("");
		labTitlVerz.setText(bestellung.getVerzeichnis());
		labAuszahlungAn.setText(bestellung.getAuftraggeber().toBestellString());

		
		insertText(tpAuszahlung.getDocument(),	"1.  Sachlich richtig und festgestellt \n" +
												"2.  Verbuchungsstelle: Kapitel " + kapitel + ", \n" +
												"     Titel " + titel + " für das Kalenderjahr " + jahr + "  \n" +
												"3.  Die Zahlstelle der Landesoberkasse \n" +												"     Nordbaden and der Fachhochschule \n" +												"     Mannheim wird hiermit angewiesen, den \n" +												"     Betrag von \n" +												"     " + euro + " Euro " + cent + " Cent \n" +												"     in Worten:  _______________________\n" +
												"     ___________________________________\n" +												"     auszuzahlen und wie oben angegeben zu \n" +												"     buchen. \n\n" +												"Mannheim, den _______________________");
	}
	
	/**
	 * Die Tabelle der Belege generieren. 
	 */
	private void createTable(){
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
		DefaultTableModel tableModel = new DefaultTableModel(data, cols) {
			public boolean isCellEditable(int rowIndex, int columnIndex){
				return false;
			}
			public Class getColumnClass(int col)  {
				return String.class;
			}
		};

		tableBestellung = new JTable(tableModel);
		// Die Schriftart festlegen
		tableBestellung.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
		tableBestellung.getTableHeader().setBackground(new Color(255,255,255));
		tableBestellung.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// Der Renderer für alle Spalten
		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		render.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		tableBestellung.getColumnModel().getColumn(0).setCellRenderer(render);
		tableBestellung.getColumnModel().getColumn(0).setMaxWidth(80);
		tableBestellung.getColumnModel().getColumn(1).setMaxWidth(150);
		tableBestellung.getColumnModel().getColumn(2).setCellRenderer(render);
		tableBestellung.getColumnModel().getColumn(2).setMaxWidth(300);
		tableBestellung.getColumnModel().getColumn(3).setCellRenderer(render); 
		tableBestellung.getColumnModel().getColumn(3).setMaxWidth(100);
		tableBestellung.setGridColor(Color.white);
		tableBestellung.setRowHeight(17);
		tableBestellung.setEnabled(false);

		// Scroll-Pane bearbeiten
		scrollBestellung.getViewport().add(tableBestellung);
		scrollBestellung.setBounds(new Rectangle(1, 63, 532, (tableBestellung.getModel().getRowCount() * 17) + 20));
		unten.setBounds(new Rectangle(0, 63 + ((tableBestellung.getModel().getRowCount() + 1) *17), 547, 567));
		printPanel.setBounds(4,5,547, oben.getHeight() + tableBestellung.getTableHeader().getHeight() + 
										tableBestellung.getModel().getRowCount() * 30 + unten.getHeight() + 50);

	}
	
	/**
	 * Den Text in das Document z.B. der Text-Pane reinschreiben. 
	 * @param doc = Dokument in welches der Text reingeschrieben wird.
	 * @param string = Der Text der reingeschrieben wird. 
	 */	
	private void insertText(Document doc, String string) {
		try {
			doc.insertString(doc.getLength(), string, null);
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	/**
	 * Drucken der Bestellung. 
	 */
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		Graphics2D g2d = (Graphics2D) graphics;
		
		double pageHeight = pageFormat.getImageableHeight();
		double pageWidth = pageFormat.getImageableWidth();

		// Height of all components
		int heightAll = oben.getHeight() +  scrollBestellung.getHeight() + unten.getHeight();
		
		int totalNumPages = (int)Math.ceil(heightAll / pageHeight);

		if(pageIndex  >= totalNumPages) {
			return NO_SUCH_PAGE;
		} else {
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			g2d.setClip(0, pageIndex * (int)pageFormat.getImageableHeight(), 560, 740);
			if(totalNumPages > 1)
				g2d.drawString("Seite: " + (pageIndex+1) + " von " + totalNumPages,( int)pageWidth/2 - 35, 725);//bottom center

			g2d.translate(0f, -pageIndex * pageFormat.getImageableHeight());
			g2d.setClip(0, pageIndex * (int)pageFormat.getImageableHeight() - 5, 560, 712);

			printPanel.printAll(g2d);

			return Printable.PAGE_EXISTS;
		}
	}
}
