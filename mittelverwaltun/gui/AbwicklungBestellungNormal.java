package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;


import javax.swing.border.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import applicationServer.ApplicationServer;
import applicationServer.CentralServer;
import dbObjects.Angebot;
import dbObjects.Fachbereich;
import dbObjects.StandardBestellung;

/**
 * <p>Title: Mittelverwaltung - GUI</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Mario Schmitt
 * @version 1.0
 */

public class AbwicklungBestellungNormal extends JInternalFrame implements TableModelListener, ActionListener{
  
  MainFrame frame;
  ApplicationServer as;
  StandardBestellung origin;
  
  // Dialogelemente	
  JTextField tfReferenzNr = new JTextField();
  JTextFieldExt tfHuelNr = new JTextFieldExt(6);
  JLabel lbHuelNr = new JLabel();
  JLabel lbReferenzNr = new JLabel();
  JLabel lbKonto = new JLabel();
  JLabel lbZvTitel = new JLabel();
  JLabel lbKapitel = new JLabel();
  JLabel lbTitel = new JLabel();
  JLabel lbUntertitel = new JLabel();
  JTextField tfKonto = new JTextField();
  JTextField tfKapitel = new JTextField();
  JTextField tfTitel = new JTextField();
  JTextField tfUntertitel = new JTextField();
  JButton btAngebote = new JButton();
  JLabel lbBesteller = new JLabel();
  JLabel lbAuftraggeber = new JLabel();
  JTextField tfBesteller = new JTextField();
  JTextField tfAuftraggeber = new JTextField();
  JButton btAbschließen = new JButton();
  JButton btSpeichern = new JButton();
  JButton btDrucken = new JButton();
  JButton btBeenden = new JButton(Functions.getCloseIcon(this.getClass()));
  JLabel lbDatum = new JLabel();
  JFormattedTextField tfDatum = new JFormattedTextField();
  JTabbedPane tabbedZusatzinfos = new JTabbedPane();
  JTextPane tpVerwendungszweck = new JTextPane();
  JTextPane tpBegruendung = new JTextPane();
  JTextPane tpBemerkungen = new JTextPane();
  JTextPane tpAnschrift = new JTextPane();
  JTextPane tpErsatz = new JTextPane();
  TitledBorder titledBorder;
  JPanel panelBasisinfos = new JPanel();
  TitledBorder titledBorderPanel1;
  JPanel panelAngebot = new JPanel();
  TitledBorder titledBorderPanel2;
  JLabel lbFirma = new JLabel();
  JTextField tfFirma = new JTextField();
  JButton btFirma = new JButton();
  JScrollPane spPositionen = new JScrollPane();
  PositionsTable tabPositionen;
  JButton btNeuePosition = new JButton(Functions.getAddIcon(this.getClass()));
  JLabel lbSumme = new JLabel();
  CurrencyTextField tfSumme = new CurrencyTextField();
  JLabel lbVerbindlichkeiten = new JLabel();
  CurrencyTextField tfVerbindlichkeiten = new CurrencyTextField();

  public AbwicklungBestellungNormal(/*MainFrame frame*/ ApplicationServer as, StandardBestellung b) {
  	//  this.frame = frame;
    this.as = as;
    this.origin = b;
  	
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    this.setSize(675,640);
    this.setTitle("Abwicklung Standardbestellung");
    this.getContentPane().setLayout(null);

    titledBorderPanel1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158)),"Allgemeine Informationen");
    titledBorderPanel1.setTitleFont(new java.awt.Font("Dialog", 1, 11));

    panelBasisinfos.setBorder(titledBorderPanel1);
    panelBasisinfos.setBounds(new Rectangle(15, 5, 500, 177));
    panelBasisinfos.setLayout(null);

	    lbDatum.setBounds(new Rectangle(57, 146, 39, 21));
	    lbDatum.setText("Datum");
	    lbDatum.setFont(new java.awt.Font("Dialog", 1, 11));
	
	    tfDatum.setBounds(new Rectangle(101, 146, 81, 21));
	    tfDatum.setDisabledTextColor(Color.black);
	    tfDatum.setValue(origin.getDatum());
	    tfDatum.setColumns(10);
	    tfDatum.setEnabled(false);
	    tfDatum.setBackground(UIManager.getColor("Viewport.background"));
	
	    lbHuelNr.setFont(new java.awt.Font("Dialog", 1, 11));
	    lbHuelNr.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbHuelNr.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbHuelNr.setText("HÜL-Nummer");
	    lbHuelNr.setBounds(new Rectangle(6, 22, 90, 21));
	
	    tfHuelNr.setEnabled(true);
	    tfHuelNr.setDisabledTextColor(Color.black);
	    tfHuelNr.setText(origin.getHuel());
	    tfHuelNr.setBounds(new Rectangle(101, 22, 120, 21));
	
	    lbReferenzNr.setFont(new java.awt.Font("Dialog", 1, 11));
	    lbReferenzNr.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbReferenzNr.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbReferenzNr.setText("Referenz-Nummer");
	    lbReferenzNr.setBounds(new Rectangle(229, 22, 105, 21));
	
	    tfReferenzNr.setBackground(UIManager.getColor("Viewport.background"));
	    tfReferenzNr.setEnabled(false);
	    tfReferenzNr.setDisabledTextColor(Color.black);
	    tfReferenzNr.setText(origin.getReferenznr());
	    tfReferenzNr.setColumns(6);
	    tfReferenzNr.setBounds(new Rectangle(338, 22, 120, 21));
	
	    lbBesteller.setBounds(new Rectangle(6, 53, 90, 21));
	    lbBesteller.setText("Besteller");
	    lbBesteller.setFont(new java.awt.Font("Dialog", 1, 11));
	    lbBesteller.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbBesteller.setHorizontalTextPosition(SwingConstants.RIGHT);
	
	    tfBesteller.setBackground(UIManager.getColor("Viewport.background"));
	    tfBesteller.setEnabled(false);
	    tfBesteller.setText(origin.getBesteller().getName() + ", " + origin.getBesteller().getVorname());
	    tfBesteller.setDisabledTextColor(Color.black);
	    tfBesteller.setBounds(new Rectangle(101, 53, 120, 21));
	
	    lbAuftraggeber.setBounds(new Rectangle(229, 53, 105, 21));
	    lbAuftraggeber.setText("Auftraggeber");
	    lbAuftraggeber.setFont(new java.awt.Font("Dialog", 1, 11));
	    lbAuftraggeber.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbAuftraggeber.setHorizontalTextPosition(SwingConstants.RIGHT);
	
	    tfAuftraggeber.setBackground(UIManager.getColor("Viewport.background"));
	    tfAuftraggeber.setEnabled(false);
	    tfAuftraggeber.setText(origin.getAuftraggeber().getName() + ", " + origin.getAuftraggeber().getVorname());
	    tfAuftraggeber.setDisabledTextColor(Color.black);
	    tfAuftraggeber.setBounds(new Rectangle(338, 53, 120, 21));
	
	    lbKonto.setFont(new java.awt.Font("Dialog", 1, 11));
	    lbKonto.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbKonto.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbKonto.setText("Kostenstelle");
	    lbKonto.setBounds(new Rectangle(6, 84, 90, 21));
	
	    tfKonto.setBackground(UIManager.getColor("Viewport.background"));
	    tfKonto.setEnabled(false);
	    tfKonto.setDisabledTextColor(Color.black);
	    tfKonto.setText(origin.getFbkonto().toString());
	    tfKonto.setBounds(new Rectangle(101, 84, 200, 21));
	
	    lbZvTitel.setFont(new java.awt.Font("Dialog", 1, 11));
	    lbZvTitel.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbZvTitel.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbZvTitel.setText("Haushaltstitel");
	    lbZvTitel.setBounds(new Rectangle(6, 115, 90, 21));
	
	    lbKapitel.setFont(new java.awt.Font("Dialog", 0, 11));
	    lbKapitel.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbKapitel.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbKapitel.setText("Kapitel");
	    lbKapitel.setBounds(new Rectangle(101, 115, 35, 21));
	
	    tfKapitel.setBounds(new Rectangle(141, 115, 70, 21));
	    tfKapitel.setDisabledTextColor(Color.black);
	    tfKapitel.setText(origin.getZvtitel().getZVKonto().getKapitel());
	    tfKapitel.setEnabled(false);
	    tfKapitel.setBackground(UIManager.getColor("Viewport.background"));
	
	    lbTitel.setFont(new java.awt.Font("Dialog", 0, 11));
	    lbTitel.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbTitel.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbTitel.setText("Titel");
	    lbTitel.setBounds(new Rectangle(216, 115, 20, 21));
	
	    tfTitel.setBounds(new Rectangle(241, 115, 70, 21));
	    tfTitel.setDisabledTextColor(Color.black);
	    tfTitel.setText(origin.getZvtitel().getTitel());
	    tfTitel.setEnabled(false);
	    tfTitel.setBackground(UIManager.getColor("Viewport.background"));
	
	    lbUntertitel.setFont(new java.awt.Font("Dialog", 0, 11));    
	    lbUntertitel.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbUntertitel.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbUntertitel.setText("Unterttitel");
	    lbUntertitel.setBounds(new Rectangle(316, 115, 50, 21));
	
	    tfUntertitel.setBounds(new Rectangle(371, 115, 70, 21));
	    tfUntertitel.setDisabledTextColor(Color.black);
	    tfUntertitel.setText(origin.getZvtitel().getUntertitel().equals("")?"00":origin.getZvtitel().getUntertitel());
	    tfUntertitel.setEnabled(false);
	    tfUntertitel.setBackground(UIManager.getColor("Viewport.background"));

    panelBasisinfos.add(tfDatum, null);
    panelBasisinfos.add(lbHuelNr, null);
    panelBasisinfos.add(tfHuelNr, null);
    panelBasisinfos.add(lbReferenzNr, null);
    panelBasisinfos.add(tfReferenzNr, null);
    panelBasisinfos.add(tfAuftraggeber, null);
    panelBasisinfos.add(lbAuftraggeber, null);
    panelBasisinfos.add(tfBesteller, null);
    panelBasisinfos.add(lbBesteller, null);
    panelBasisinfos.add(lbKonto, null);
    panelBasisinfos.add(tfKonto, null);
    panelBasisinfos.add(lbZvTitel, null);
    panelBasisinfos.add(lbKapitel, null);
    panelBasisinfos.add(tfKapitel, null);
    panelBasisinfos.add(lbTitel, null);
    panelBasisinfos.add(tfTitel, null);
    panelBasisinfos.add(lbUntertitel, null);
    panelBasisinfos.add(tfUntertitel, null);
    panelBasisinfos.add(lbDatum, null);

    titledBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158)),"Zusätzliche Bestellungsdetails");
    titledBorder.setTitleFont(new java.awt.Font("Dialog", 1, 11));
    
    tabbedZusatzinfos.setBorder(titledBorder);
    tabbedZusatzinfos.setBounds(new Rectangle(15, 187, 640, 100));
    tabbedZusatzinfos.setFont(new java.awt.Font("Dialog", 0, 11));
    
    	tpVerwendungszweck.setFont(new java.awt.Font("Dialog", 0, 11));
    	tpVerwendungszweck.setEnabled(false);
	    tpVerwendungszweck.setEditable(false);
	    tpVerwendungszweck.setText(origin.getVerwendungszweck());
	
	    tpBegruendung.setFont(new java.awt.Font("Dialog", 0, 11));
	    tpBegruendung.setText(origin.getBegruendung());
	    tpBegruendung.setEditable(false);
	    tpBegruendung.setEnabled(false);
	
	    tpBemerkungen.setFont(new java.awt.Font("Dialog", 0, 11));
	    tpBemerkungen.setText(origin.getBemerkung());
	    tpBemerkungen.setEditable(false);
	    tpBemerkungen.setEnabled(false);
		
	    tpAnschrift.setFont(new java.awt.Font("Dialog", 0, 11));
	    Fachbereich[] fbs = as.getFachbereiche();
	    if ((fbs != null) && (fbs.length > 0))
	       	tpAnschrift.setText(fbs[0].getFhBezeichnung() + "\n" +
	    						fbs[0].getFbBezeichnung() + "\n" +
								"   z.Hd. " + origin.getEmpfaenger().getVorname() + " " + origin.getEmpfaenger().getName() + "\n" +
								"   Bau " + origin.getEmpfaenger().getBau() + ", " + "Raum " + origin.getEmpfaenger().getRaum() + "\n" +
								fbs[0].getStrasseHausNr() + "\n" +
								fbs[0].getPlzOrt());
	    else tpAnschrift.setText("");
	    
	    tpAnschrift.setEditable(false);
	    tpAnschrift.setEnabled(false);
	
	    tpErsatz.setFont(new java.awt.Font("Dialog", 0, 11));
	    if (origin.getErsatzbeschaffung())
	    	tpErsatz.setText("Ersatzbeschreibung: " + origin.getErsatzbeschreibung() + "\n" +
	    					 "Inventarnummer: " + origin.getInventarNr());
	    else
	    	tpErsatz.setText("");
	    tpErsatz.setEditable(false);
	    tpErsatz.setEnabled(false);

    tabbedZusatzinfos.add(new JScrollPane(tpAnschrift), "Lieferanschrift");
    tabbedZusatzinfos.add(new JScrollPane(tpVerwendungszweck), "Verwendungszweck");
    tabbedZusatzinfos.add(new JScrollPane(tpBegruendung), "Angebotswahl");
    tabbedZusatzinfos.add(new JScrollPane(tpBemerkungen), "Bemerkungen");
    if (origin.getErsatzbeschaffung()) 
    	tabbedZusatzinfos.add(tpErsatz, "Ersatzbeschaffung");

    titledBorderPanel2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158)),"Angebotsdetails");
    titledBorderPanel2.setTitleFont(new java.awt.Font("Dialog", 1, 11));

    panelAngebot.setBorder(titledBorderPanel2);
    panelAngebot.setBounds(new Rectangle(15,292, 640, 309));
    panelAngebot.setLayout(null);
    
    Angebot angebot = (Angebot)origin.getAngebote().get(origin.getAngenommenesAngebot());
    
	    lbFirma.setBounds(new Rectangle(9, 25, 54, 21));
	    lbFirma.setText("Anbieter");
	    lbFirma.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbFirma.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbFirma.setFont(new java.awt.Font("Dialog", 1, 11));
	
	    tfFirma.setBounds(new Rectangle(66, 25, 185, 21));
	    tfFirma.setText(angebot.getAnbieter().getName());
	    tfFirma.setColumns(0);
	    tfFirma.setDisabledTextColor(Color.black);
	    tfFirma.setEnabled(false);
	    tfFirma.setBackground(UIManager.getColor("Viewport.background"));
	
	    btFirma.setText("Details");
	    btFirma.setFont(new java.awt.Font("Dialog", 1, 11));
	    btFirma.setBounds(new Rectangle(255, 23, 83, 27));
	    btFirma.setActionCommand("details");
	    btFirma.addActionListener(this);
	    
	    btAngebote.setBounds(new Rectangle(446, 23, 184, 27));
	    btAngebote.setFont(new java.awt.Font("Dialog", 1, 11));
	    btAngebote.setText("Angebotsvergleich");
	    btAngebote.setActionCommand("showOffers");
	    btAngebote.addActionListener(this);
	
	    tabPositionen = new PositionsTable(PositionsTable.STD_ABWICKLUNG, this, angebot.getPositionen());
	       
	    spPositionen.getViewport().add(tabPositionen, null);
		spPositionen.setBounds(new Rectangle(10, 58, 620, 180));
	   
	    btNeuePosition.setText("Position hinzufügen");
	    btNeuePosition.setFont(new java.awt.Font("Dialog", 1, 11));
	    btNeuePosition.setBounds(new Rectangle(9, 242, 165, 27));
	    btNeuePosition.setActionCommand("addPosition");
	    btNeuePosition.addActionListener(tabPositionen);
	    
	    lbSumme.setFont(new java.awt.Font("Dialog", 1, 11));
	    lbSumme.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbSumme.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbSumme.setText("Bestellsumme (Brutto)");
	    lbSumme.setBounds(new Rectangle(334, 242, 138, 21));
	
	    tfSumme.setFont(new java.awt.Font("Dialog", 1, 12));
	    tfSumme.setBackground(UIManager.getColor("Viewport.background"));
	    tfSumme.setEnabled(false);
	    tfSumme.setDisabledTextColor(Color.black);
	    tfSumme.setValue(new Float(tabPositionen.getOrderSum()));
	    tfSumme.setHorizontalAlignment(SwingConstants.RIGHT);
	    tfSumme.setBounds(new Rectangle(483, 242, 147, 21));
	    
	    lbVerbindlichkeiten.setFont(new java.awt.Font("Dialog", 1, 11));
	    lbVerbindlichkeiten.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbVerbindlichkeiten.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbVerbindlichkeiten.setText("Verbindlichkeiten");
	    lbVerbindlichkeiten.setBounds(new Rectangle(334, 271, 138, 21));
	
	    tfVerbindlichkeiten.setFont(new java.awt.Font("Dialog", 1, 12));
	    tfVerbindlichkeiten.setBackground(UIManager.getColor("Viewport.background"));
	    tfVerbindlichkeiten.setEnabled(false);
	    //tfVerbindlichkeiten.setDisabledTextColor(Color.black);
	    tfVerbindlichkeiten.setValue(new Float(tabPositionen.getOrderDebit()));
	    if (((Float)tfVerbindlichkeiten.getValue()).floatValue() > 0)
	    	tfVerbindlichkeiten.setDisabledTextColor(Color.RED);
	    else
	    	tfVerbindlichkeiten.setDisabledTextColor(Color.BLACK);
	    tfVerbindlichkeiten.setHorizontalAlignment(SwingConstants.RIGHT);
	    tfVerbindlichkeiten.setBounds(new Rectangle(483, 271, 147, 21));

    panelAngebot.add(btNeuePosition, null);
    panelAngebot.add(tfSumme, null);
    panelAngebot.add(lbSumme, null);
    panelAngebot.add(tfVerbindlichkeiten, null);
    panelAngebot.add(lbVerbindlichkeiten, null);
    panelAngebot.add(lbFirma, null);
    panelAngebot.add(tfFirma, null);
    panelAngebot.add(btFirma, null);
    panelAngebot.add(btAngebote, null);
    panelAngebot.add(spPositionen, null);

    btAbschließen.setBounds(new Rectangle(535, 70, 115, 27));
    btAbschließen.setFont(new java.awt.Font("Dialog", 1, 11));
    btAbschließen.setText("Abschließen");

    btSpeichern.setBounds(new Rectangle(535, 35, 115, 27));
    btSpeichern.setFont(new java.awt.Font("Dialog", 1, 11));
    btSpeichern.setText("Speichern");

    btDrucken.setBounds(new Rectangle(535, 107, 115, 27));
    btDrucken.setFont(new java.awt.Font("Dialog", 1, 11));
    btDrucken.setText("Drucken");

    btBeenden.setBounds(new Rectangle(535, 143, 115, 27));
    btBeenden.setFont(new java.awt.Font("Dialog", 1, 11));
    btBeenden.setText("Beenden");
    btBeenden.setActionCommand("dispose");
    btBeenden.addActionListener(this);

    this.getContentPane().add(panelBasisinfos, null);
    this.getContentPane().add(tabbedZusatzinfos, null);
    this.getContentPane().add(panelAngebot, null);

    this.getContentPane().add(btAbschließen, null);
    this.getContentPane().add(btDrucken, null);
    this.getContentPane().add(btBeenden, null);
    this.getContentPane().add(btSpeichern, null);
    
   
  }
  
  
	public static void main(String[] args) {
		JFrame test = new JFrame("Abwicklung Standardbestellung");
		JDesktopPane desk = new JDesktopPane();
		desk.setDesktopManager(new DefaultDesktopManager());
		test.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		test.setContentPane(desk);
		test.setBounds(100,100,800,800);
		try{
			CentralServer server = (CentralServer)Naming.lookup("//localhost/mittelverwaltung");
			ApplicationServer applicationServer = server.getMyApplicationServer();
			PasswordEncrypt pe = new PasswordEncrypt();
			String psw = pe.encrypt(new String("m.schmitt").toString());
			applicationServer.login("m.schmitt", psw);
			StandardBestellung bestellung = applicationServer.getStandardBestellung(1);
			AbwicklungBestellungNormal iFrame= new AbwicklungBestellungNormal(applicationServer, bestellung);
			desk.add(iFrame);
			test.show();
			iFrame.show();
		}catch(Exception e){
				System.out.println(e);
		}
	
	
	}

	public void tableChanged(TableModelEvent e) {
		tfSumme.setValue(new Float(((PositionsTableModel)e.getSource()).getOrderSum()));
		tfVerbindlichkeiten.setValue(new Float(((PositionsTableModel)e.getSource()).getOrderDebit()));
		if (((Float)tfVerbindlichkeiten.getValue()).floatValue() > 0)
	    	tfVerbindlichkeiten.setDisabledTextColor(Color.RED);
	    else
	    	tfVerbindlichkeiten.setDisabledTextColor(Color.BLACK);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "details"){
			FirmenDetails dialog = new FirmenDetails(this, "Visitenkarte", true, ((Angebot)origin.getAngebote().get(origin.getAngenommenesAngebot())).getAnbieter());
			dialog.show();
		}else if (e.getActionCommand() == "dispose"){
			this.dispose();
		}else if (e.getActionCommand() == "showOffers"){
			AngebotsUebersicht dialog = new AngebotsUebersicht(this, "Angebotsvergleich", true, origin.getAngebote());
			dialog.show();
		}
			
		
	}

}