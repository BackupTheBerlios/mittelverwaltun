package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.rmi.Naming;

import javax.swing.border.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.text.JTextComponent;

import applicationServer.ApplicationServer;
import applicationServer.ApplicationServerException;
import applicationServer.CentralServer;
import dbObjects.Angebot;
import dbObjects.Fachbereich;
import dbObjects.StandardBestellung;
import dbObjects.ZVTitel;

/**
 * <p>Title: Mittelverwaltung - GUI</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Mario Schmitt
 * @version 1.0
 */

public class AbwicklungBestellungNormal extends JInternalFrame implements TableModelListener, ActionListener, FocusListener{
  
  MainFrame frame;
  ApplicationServer as;
  StandardBestellung origin;
  
  // Dialogelemente
  JTextField tfPhase = new JTextField();
  JLabel lbPhase = new JLabel();
  JTextField tfReferenzNr = new JTextField();
  JTextFieldExt tfHuelNr = new JTextFieldExt(6);
  JLabel lbHuelNr = new JLabel();
  JLabel lbReferenzNr = new JLabel();
  JLabel lbKonto = new JLabel();
  JLabel lbZvTitel = new JLabel();
  JLabel lbKapitel = new JLabel();
  JLabel lbTitel = new JLabel();
  JLabel lbUntertitel = new JLabel();
  JLabel lbKostenart = new JLabel();
  JTextField tfKonto = new JTextField();
  JTextField tfKapitel = new JTextField();
  JTextField tfTitel = new JTextField();
  JTextField tfUntertitel = new JTextField();
  JTextField tfKostenart = new JTextField();
  JButton btAngebote = new JButton(Functions.getCopyIcon(this.getClass()));
  JLabel lbBesteller = new JLabel();
  JLabel lbAuftraggeber = new JLabel();
  JTextField tfBesteller = new JTextField();
  JTextField tfAuftraggeber = new JTextField();
  JButton btAbschlieﬂen = new JButton(Functions.getPropertiesIcon(this.getClass()));
  JButton btSpeichern = new JButton(Functions.getSaveIcon(this.getClass()));
  JButton btDrucken = new JButton(Functions.getPrintIcon(this.getClass()));
  JButton btBeenden = new JButton(Functions.getCloseIcon(this.getClass()));
  JButton btStorno = new JButton(Functions.getStopIcon(this.getClass()));
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
  JButton btFirma = new JButton(Functions.getZoomIcon(this.getClass()));
  JScrollPane spPositionen = new JScrollPane();
  PositionsTable tabPositionen;
  JButton btNeuePosition = new JButton(Functions.getRowInsertAfterIcon(this.getClass()));
  JLabel lbSumme = new JLabel();
  CurrencyTextField tfSumme = new CurrencyTextField();
  JLabel lbVerbindlichkeiten = new JLabel();
  CurrencyTextField tfVerbindlichkeiten = new CurrencyTextField();

  public AbwicklungBestellungNormal(MainFrame frame, StandardBestellung b) {
  	this.frame = frame;
    this.as = frame.applicationServer;
    this.origin = b;
  	
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    this.setSize(675,671);
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
    this.setTitle("Standardbestellung");
    this.getContentPane().setLayout(null);

    titledBorderPanel1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158)),"Allgemeine Informationen");
    titledBorderPanel1.setTitleFont(new java.awt.Font("Dialog", 1, 11));

    panelBasisinfos.setBorder(titledBorderPanel1);
    panelBasisinfos.setBounds(new Rectangle(15, 5, 500, 208));
    panelBasisinfos.setLayout(null);

	    lbDatum.setBounds(new Rectangle(10, 22, 95, 21));
	    lbDatum.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbDatum.setText("Datum");
	    lbDatum.setFont(new java.awt.Font("Dialog", 1, 11));
	
	    tfDatum.setBounds(new Rectangle(110, 22, 81, 21));
	    tfDatum.setValue(origin.getDatum());
	    tfDatum.setColumns(10);
	    tfDatum.setEditable(false);
	    	    
	    lbPhase.setFont(new java.awt.Font("Dialog", 1, 11));
	    lbPhase.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbPhase.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbPhase.setText("Status:");
	    lbPhase.setBounds(new Rectangle(288, 22, 105, 21));
	
	    tfPhase.setFont(new java.awt.Font("Dialog", 1, 12));
	    tfPhase.setEditable(false);
	    tfPhase.setBorder(null);
	    tfPhase.setBounds(new Rectangle(397, 22, 90, 21));
	    
	    lbHuelNr.setFont(new java.awt.Font("Dialog", 1, 11));
	    lbHuelNr.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbHuelNr.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbHuelNr.setText("H‹L-Nummer");
	    lbHuelNr.setBounds(new Rectangle(10, 53, 95, 21));
	
	    tfHuelNr.setEnabled(true);
	    tfHuelNr.setText(origin.getHuel());
	    tfHuelNr.setBounds(new Rectangle(110, 53, 130, 21));
	
	    lbReferenzNr.setFont(new java.awt.Font("Dialog", 1, 11));
	    lbReferenzNr.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbReferenzNr.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbReferenzNr.setText("Referenz-Nummer");
	    lbReferenzNr.setBounds(new Rectangle(248, 53, 105, 21));
	
	    tfReferenzNr.setEditable(false);
	    tfReferenzNr.setText(origin.getReferenznr());
	    tfReferenzNr.setCaretPosition(0);
	    tfReferenzNr.addFocusListener(this);
	    tfReferenzNr.setColumns(6);
	    tfReferenzNr.setBounds(new Rectangle(357, 53, 130, 21));
	
	    lbBesteller.setBounds(new Rectangle(10, 84, 95, 21));
	    lbBesteller.setText("Besteller");
	    lbBesteller.setFont(new java.awt.Font("Dialog", 1, 11));
	    lbBesteller.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbBesteller.setHorizontalTextPosition(SwingConstants.RIGHT);
	
	    tfBesteller.setEditable(false);
	    tfBesteller.setText(origin.getBesteller().getName() + ", " + origin.getBesteller().getVorname());
	    tfBesteller.setToolTipText(origin.getBesteller().getName() + ", " + origin.getBesteller().getVorname());
	    tfBesteller.setCaretPosition(0);
	    tfBesteller.addFocusListener(this);
	    tfBesteller.setBounds(new Rectangle(110, 84, 130, 21));
	
	    lbAuftraggeber.setBounds(new Rectangle(248, 84, 105, 21));
	    lbAuftraggeber.setText("Auftraggeber");
	    lbAuftraggeber.setFont(new java.awt.Font("Dialog", 1, 11));
	    lbAuftraggeber.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbAuftraggeber.setHorizontalTextPosition(SwingConstants.RIGHT);
	
	    tfAuftraggeber.setEditable(false);
	    tfAuftraggeber.setText(origin.getAuftraggeber().getName() + ", " + origin.getAuftraggeber().getVorname());
	    tfAuftraggeber.setToolTipText(origin.getAuftraggeber().getName() + ", " + origin.getAuftraggeber().getVorname());
	    tfAuftraggeber.setCaretPosition(0);
	    tfAuftraggeber.addFocusListener(this);
	    tfAuftraggeber.setBounds(new Rectangle(357, 84, 130, 21));
	    
	    lbKonto.setFont(new java.awt.Font("Dialog", 1, 11));
	    lbKonto.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbKonto.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbKonto.setText("Kostenstelle");
	    lbKonto.setBounds(new Rectangle(10, 115, 95, 21));
	
	    tfKonto.setEditable(false);
	    tfKonto.setText(origin.getFbkonto().toString());
	    tfKonto.setToolTipText(origin.getFbkonto().getBezeichnung() + ", Kostenstelle: " + origin.getFbkonto().getInstitut().getKostenstelle() + ", Hauptkonto: "+origin.getFbkonto().getHauptkonto() + ", Unterkonto: "+origin.getFbkonto().getUnterkonto());
	    tfKonto.setCaretPosition(0);
	    tfKonto.addFocusListener(this);
	    tfKonto.setBounds(new Rectangle(110, 115, 377, 21));

	    lbZvTitel.setFont(new java.awt.Font("Dialog", 1, 11));
	    lbZvTitel.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbZvTitel.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbZvTitel.setText("Haushaltstitel");
	    lbZvTitel.setBounds(new Rectangle(10, 146, 95, 21));
	
	    lbKapitel.setFont(new java.awt.Font("Dialog", 0, 11));
	    lbKapitel.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbKapitel.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbKapitel.setText("Kapitel");
	    lbKapitel.setBounds(new Rectangle(110, 146, 35, 21));
	
	    tfKapitel.setBounds(new Rectangle(150, 146, 70, 21));
	    String titelToolTip = origin.getZvtitel().getBezeichnung() + ", Kapitel: ";
	    if(origin.getZvtitel() instanceof ZVTitel){
	    	tfKapitel.setText(((ZVTitel)origin.getZvtitel()).getZVKonto().getKapitel());
	    	titelToolTip += ((ZVTitel)origin.getZvtitel()).getZVKonto().getKapitel();
	    }else{
			tfKapitel.setText(origin.getZvtitel().getZVTitel().getZVKonto().getKapitel());
			titelToolTip += ((ZVTitel)origin.getZvtitel()).getZVKonto().getKapitel();
	    }
	    tfKapitel.setCaretPosition(0);
	    tfKapitel.addFocusListener(this);
		tfKapitel.setEditable(false);
	    	
	    lbTitel.setFont(new java.awt.Font("Dialog", 0, 11));
	    lbTitel.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbTitel.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbTitel.setText("Titel");
	    lbTitel.setBounds(new Rectangle(225, 146, 20, 21));
	
	    tfTitel.setBounds(new Rectangle(250, 146, 70, 21));
	    tfTitel.setText(origin.getZvtitel().getTitel());
	    tfTitel.setEditable(false);
	    tfTitel.setCaretPosition(0);
	    tfTitel.addFocusListener(this);
	    titelToolTip += ", Titel: " + origin.getZvtitel().getTitel();
	   	
	    lbUntertitel.setFont(new java.awt.Font("Dialog", 0, 11));    
	    lbUntertitel.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbUntertitel.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbUntertitel.setText("Unterttitel");
	    lbUntertitel.setBounds(new Rectangle(325, 146, 50, 21));
	
	    tfUntertitel.setBounds(new Rectangle(380, 146, 70, 21));
	    tfUntertitel.setText(origin.getZvtitel().getUntertitel().equals("")?"00":origin.getZvtitel().getUntertitel());
	    tfUntertitel.setEditable(false);
	    tfUntertitel.setCaretPosition(0);
	    tfUntertitel.addFocusListener(this);
	    titelToolTip += ", Untertitel: " + (origin.getZvtitel().getUntertitel().equals("")?"00":origin.getZvtitel().getUntertitel());
	    
	    tfKapitel.setToolTipText(titelToolTip);
	    tfTitel.setToolTipText(titelToolTip);
	    tfUntertitel.setToolTipText(titelToolTip);
	    
	    lbKostenart.setFont(new java.awt.Font("Dialog", 1, 11));
	    lbKostenart.setHorizontalAlignment(SwingConstants.RIGHT);
	    
	    lbKostenart.setText("Kostenart");
	    lbKostenart.setBounds(new Rectangle(10, 177, 95, 21));
	
	    tfKostenart.setBounds(new Rectangle(110, 177, 210, 21));
	    //tfKostenart.setDisabledTextColor(Color.black);
	    tfKostenart.setText(origin.getKostenart().getBeschreibung());
	    //tfKostenart.setEnabled(false);
	    tfKostenart.setEditable(false);
	    tfKostenart.setCaretPosition(0);
	    tfKostenart.addFocusListener(this);
	    //tfKostenart.setBackground(UIManager.getColor("Viewport.background"));
	    
    panelBasisinfos.add(tfDatum, null);
    panelBasisinfos.add(lbPhase, null);
    panelBasisinfos.add(tfPhase, null);
    panelBasisinfos.add(lbHuelNr, null);
    panelBasisinfos.add(tfHuelNr, null);
    panelBasisinfos.add(lbReferenzNr, null);
    panelBasisinfos.add(tfReferenzNr, null);
    panelBasisinfos.add(tfBesteller, null);
    panelBasisinfos.add(lbBesteller, null);
    panelBasisinfos.add(tfAuftraggeber, null);
    panelBasisinfos.add(lbAuftraggeber, null);
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
    panelBasisinfos.add(lbKostenart, null);
    panelBasisinfos.add(tfKostenart, null);

    titledBorder = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158)),"Zus‰tzliche Bestellungsdetails");
    titledBorder.setTitleFont(new java.awt.Font("Dialog", 1, 11));
    
    tabbedZusatzinfos.setBorder(titledBorder);
    tabbedZusatzinfos.setBounds(new Rectangle(15, 218, 640, 100));
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
								"z.Hd. " + origin.getEmpfaenger().getVorname() + " " + origin.getEmpfaenger().getName() + "\n" +
								((((origin.getEmpfaenger().getBau()!=null) && !origin.getEmpfaenger().getBau().equalsIgnoreCase(""))) ? "   Bau " + origin.getEmpfaenger().getBau() + (((origin.getEmpfaenger().getRaum()!=null)&&(!origin.getEmpfaenger().getRaum().equalsIgnoreCase(""))) ? ", " + "Raum " + origin.getEmpfaenger().getRaum() : "") + "\n"  : "") +
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
    panelAngebot.setBounds(new Rectangle(15,323, 640, 309));
    panelAngebot.setLayout(null);
    
    Angebot angebot = (Angebot)origin.getAngebote().get(origin.getAngenommenesAngebot());
    
	    lbFirma.setBounds(new Rectangle(9, 25, 54, 21));
	    lbFirma.setText("Anbieter");
	    lbFirma.setHorizontalTextPosition(SwingConstants.RIGHT);
	    lbFirma.setHorizontalAlignment(SwingConstants.RIGHT);
	    lbFirma.setFont(new java.awt.Font("Dialog", 1, 11));
	
	    tfFirma.setBounds(new Rectangle(66, 25, 185, 21));
	    tfFirma.setText(angebot.getAnbieter().getName());
	    tfFirma.setToolTipText(angebot.getAnbieter().getName() + ", " + angebot.getAnbieter().getOrt());
	    tfFirma.setEditable(false);
	    tfFirma.setCaretPosition(0);
	    tfFirma.addFocusListener(this);
	    	
	    btFirma.setText("Details");
	    btFirma.setFont(new java.awt.Font("Dialog", 1, 11));
	    btFirma.setBounds(new Rectangle(255, 23, 100, 27));
	    btFirma.setActionCommand("details");
	    btFirma.addActionListener(this);
	    
	    btAngebote.setBounds(new Rectangle(470, 23, 160, 27));
	    btAngebote.setFont(new java.awt.Font("Dialog", 1, 11));
	    btAngebote.setText("Angebotsvergleich");
	    btAngebote.setActionCommand("showOffers");
	    btAngebote.addActionListener(this);
	
	    tabPositionen = new PositionsTable(PositionsTable.STD_ABWICKLUNG, false, this, angebot.getPositionen());
	       
	    spPositionen.getViewport().add(tabPositionen, null);
		spPositionen.setBounds(new Rectangle(10, 58, 620, 180));
	   
	    btNeuePosition.setText("Position hinzuf¸gen");
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
	    tfSumme.setEditable(false);
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
	    tfVerbindlichkeiten.setEditable(false);
	    tfVerbindlichkeiten.setEnabled(false);
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

    btAbschlieﬂen.setBounds(new Rectangle(525, 25, 125, 27));
    btAbschlieﬂen.setFont(new java.awt.Font("Dialog", 1, 11));
    btAbschlieﬂen.setText("Abschlieﬂen");
    btAbschlieﬂen.setToolTipText("Alle offenen Positionen begleichen, Abwicklung abschlieﬂen und Bestellung speichern");
    btAbschlieﬂen.setActionCommand("completeOrder");
    btAbschlieﬂen.addActionListener(this);
    
    btSpeichern.setBounds(new Rectangle(525, 60, 125, 27));
    btSpeichern.setFont(new java.awt.Font("Dialog", 1, 11));
    btSpeichern.setText("Speichern");
    btSpeichern.setToolTipText("Bestellung speichern");
    btSpeichern.setActionCommand("saveOrder");
    btSpeichern.addActionListener(this);

    btStorno.setBounds(new Rectangle(525, 95, 125, 27));
    btStorno.setFont(new java.awt.Font("Dialog", 1, 11));
    btStorno.setText("Storno");
    btStorno.setToolTipText("Bestellung stornieren");
    btStorno.setActionCommand("revokeOrder");
    btStorno.addActionListener(this);
    
    btDrucken.setBounds(new Rectangle(525, 130, 125, 27));
    btDrucken.setFont(new java.awt.Font("Dialog", 1, 11));
    btDrucken.setText("Vorschau");
		btDrucken.setActionCommand("print");
    btDrucken.setToolTipText("Bestellung drucken");
		btDrucken.addActionListener(this);

    btBeenden.setBounds(new Rectangle(525, 165, 125, 27));
    btBeenden.setFont(new java.awt.Font("Dialog", 1, 11));
    btBeenden.setText("Beenden");
    btBeenden.setActionCommand("dispose");
    btBeenden.addActionListener(this);

    this.getContentPane().add(panelBasisinfos, null);
    this.getContentPane().add(tabbedZusatzinfos, null);
    this.getContentPane().add(panelAngebot, null);

    this.getContentPane().add(btAbschlieﬂen, null);
    this.getContentPane().add(btDrucken, null);
    this.getContentPane().add(btBeenden, null);
    this.getContentPane().add(btSpeichern, null);
    this.getContentPane().add(btStorno, null);
    
    updateComponentEnabling();
    updatePhase();
    
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
//			AbwicklungBestellungNormal iFrame= new AbwicklungBestellungNormal(applicationServer, bestellung);
//			desk.add(iFrame);
//			test.show();
//			iFrame.show();
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
	
	private void printOrder(){
		PrintSTDBestellung bestellung = new PrintSTDBestellung(origin, frame);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "details"){
			FirmenDetails dialog = new FirmenDetails(this, "Visitenkarte", true, ((Angebot)origin.getAngebote().get(origin.getAngenommenesAngebot())).getAnbieter());
			dialog.show();
		}else if (e.getActionCommand() == "print"){
			printOrder();
		}else if (e.getActionCommand() == "dispose"){
			this.dispose();
		}else if (e.getActionCommand() == "showOffers"){
			AngebotsUebersicht dialog = new AngebotsUebersicht(this, "Angebotsvergleich", true, origin.getAngebote());
			dialog.show();
		}else if (e.getActionCommand() == "saveOrder"){
			saveOrder();
		}else if (e.getActionCommand() == "completeOrder"){
			completeOrder();
		}else if (e.getActionCommand() == "revokeOrder"){
			revokeOrder();
		}
	}

	private void saveOrder(){
		
		StandardBestellung tempOrigin = (StandardBestellung)origin.clone();
		StandardBestellung editedOrder = (StandardBestellung)origin.clone();
		((Angebot)editedOrder.getAngebote().get(editedOrder.getAngenommenesAngebot())).setPositionen(tabPositionen.getOrderPositions());
		editedOrder.setHuel(this.tfHuelNr.getText());
		editedOrder.setBestellwert(tabPositionen.getOrderSum());
		editedOrder.setVerbindlichkeiten(tabPositionen.getOrderDebit());
		
		try {
			//StandardBestellung copy = (StandardBestellung)editedOrder.clone();
			as.setBestellung(frame.getBenutzer(), tempOrigin, editedOrder);
			origin = as.getStandardBestellung(origin.getId());
			updateComponentEnabling();
			updatePhase();
		} catch (ApplicationServerException e) {
				MessageDialogs.showDetailMessageDialog(this, "Fehler", e.getMessage(), e.getNestedMessage(), MessageDialogs.ERROR_ICON);
				//e.printStackTrace();
		}
	}
	
	private void completeOrder(){
		
//		TODO: Test => mehr als eine Position in Angebot, Bestellsumme > 0
		
		tabPositionen.payAllPositions();
		
		StandardBestellung editedOrder = (StandardBestellung)origin.clone();
		((Angebot)editedOrder.getAngebote().get(editedOrder.getAngenommenesAngebot())).setPositionen(tabPositionen.getOrderPositions());
		editedOrder.setPhase('2');
		editedOrder.setHuel(this.tfHuelNr.getText());
		editedOrder.setBestellwert(tabPositionen.getOrderSum());
		editedOrder.setVerbindlichkeiten(tabPositionen.getOrderDebit());
		
		try {
			//StandardBestellung copy = (StandardBestellung)editedOrder.clone();
			as.setBestellung(frame.getBenutzer(), origin, editedOrder);
			origin = as.getStandardBestellung(origin.getId());
			updateComponentEnabling();
			updatePhase();
		} catch (ApplicationServerException e) {
				MessageDialogs.showDetailMessageDialog(this, "Fehler", e.getMessage(), e.getNestedMessage(), MessageDialogs.ERROR_ICON);
				//e.printStackTrace();
		}
	}
	
	private void revokeOrder(){
		
		tabPositionen.oweAllPositions();
		
		StandardBestellung editedOrder = (StandardBestellung)origin.clone();
		((Angebot)editedOrder.getAngebote().get(editedOrder.getAngenommenesAngebot())).setPositionen(tabPositionen.getOrderPositions());
		editedOrder.setPhase('3');
		editedOrder.setHuel(this.tfHuelNr.getText());
		editedOrder.setBestellwert(tabPositionen.getOrderSum());
		editedOrder.setVerbindlichkeiten(tabPositionen.getOrderDebit());
		
		try {
			//StandardBestellung copy = (StandardBestellung)editedOrder.clone();
			as.setBestellung(frame.getBenutzer(), origin, editedOrder);
			origin = as.getStandardBestellung(origin.getId());
			updateComponentEnabling();
			updatePhase();
		} catch (ApplicationServerException e) {
				MessageDialogs.showDetailMessageDialog(this, "Fehler", e.getMessage(), e.getNestedMessage(), MessageDialogs.ERROR_ICON);
				//e.printStackTrace();
		}
	}
	
	private void updateComponentEnabling(){
		
		boolean enable = origin.getPhase()=='1';
		
		tfHuelNr.setEnabled(enable);
		if (tfHuelNr.isEnabled())
			tfHuelNr.setBackground(Color.WHITE);
		else
			tfHuelNr.setBackground(UIManager.getColor("Viewport.background"));
		
		tabPositionen.setEditable(enable);
		
		btNeuePosition.setEnabled(enable);
		
		btAbschlieﬂen.setEnabled(enable);
		
		btSpeichern.setEnabled(enable);
		
		btStorno.setEnabled(origin.getPhase() != '3');
	}
	
	private void updatePhase(){
		if (origin.getPhase()=='1'){
			tfPhase.setForeground(Color.GREEN);
			tfPhase.setText("Abwicklung");
		} else if (origin.getPhase()=='2'){
			tfPhase.setForeground(Color.ORANGE);
			tfPhase.setText("Abgeschlossen");
		} else if (origin.getPhase()=='3'){
			tfPhase.setForeground(Color.RED);
			tfPhase.setText("Storniert");
		}  
	}

	public void focusGained(FocusEvent e) {
		((JTextComponent)(e.getSource())).getCaret().setVisible(true);
	}

	public void focusLost(FocusEvent e) {
		((JTextComponent)(e.getSource())).getCaret().setVisible(false);
	}
}