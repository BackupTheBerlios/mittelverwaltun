package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import dbObjects.Angebot;
import dbObjects.FBHauptkonto;
import dbObjects.Firma;
import dbObjects.Position;
import javax.swing.border.*;


public class AngebotFrame extends JDialog implements TableModelListener, ActionListener, ItemListener{
  JScrollPane scrollPositionen = new JScrollPane();
  JButton buSpeichern = new JButton(Functions.getSaveIcon(getClass()));
  JButton buBeenden = new JButton(Functions.getCloseIcon(getClass()));
  FBHauptkonto hauptkonto;
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JTextField tfFirma = new JTextField();
  JTextField tfStrasse = new JTextField();
  JTextField tfPLZ = new JTextField();
  JTextField tfOrt = new JTextField();
  JButton buAddPosition = new JButton(Functions.getAddIcon(getClass()));
  PositionsTable tablePositionen;
  JLabel jLabel9 = new JLabel();
  JFormattedTextField tfDate = new JFormattedTextField(DateFormat.getDateInstance());
  BestellungNormal frame = null;
  int angebotNr = -1;
  JLabel jLabel11 = new JLabel();
  JComboBox cbFirmen = new JComboBox();
  JButton buAddFirm = new JButton();
  Angebot angebot;
  CurrencyTextField tfBestellsumme = new CurrencyTextField();
  JLabel jLabel12 = new JLabel();
  CurrencyTextField tf16MwSt = new CurrencyTextField();
  CurrencyTextField tf7MwSt = new CurrencyTextField();
  JLabel jLabel13 = new JLabel();
  JLabel jLabel14 = new JLabel();
  JLabel jLabel15 = new JLabel();
  CurrencyTextField tfNetto = new CurrencyTextField();
  JPanel jPanel1 = new JPanel();
  TitledBorder titledBorder1;
  JPanel jPanel2 = new JPanel();
  TitledBorder titledBorder2;


  public AngebotFrame(BestellungNormal frame) {
    super(JOptionPane.getFrameForComponent(frame), "Angebot", false);
    this.frame = frame;

		tablePositionen = new PositionsTable(PositionsTable.STD_DURCHFUEHRUNG, true, this, new ArrayList());
    tfDate.setValue(new Date(System.currentTimeMillis()));

    init();
  }

  public AngebotFrame(BestellungNormal frame, Angebot angebot, int angebotNr) {
		super(JOptionPane.getFrameForComponent(null), "Angebot", false);
		this.frame = frame;
		this.angebot = angebot;
		this.angebotNr = angebotNr;
		if(((Position)angebot.getPositionen().get(0)).getMwst() != 0)
			tablePositionen = new PositionsTable(PositionsTable.STD_DURCHFUEHRUNG, true, this, angebot.getPositionen());
		else{
			tablePositionen = new PositionsTable(PositionsTable.STD_DURCHFUEHRUNG, true, this, new ArrayList());
		}

		init();
	  setData(angebot);
	  
		if(((Position)angebot.getPositionen().get(0)).getMwst() != 0)
			tfBestellsumme.setEditable(false);
 }

	private void init(){
		try {
			jbInit();
			pack();
		 }
		 catch(Exception ex) {
			ex.printStackTrace();
		 }

		cbFirmen.addItemListener(this);
		buSpeichern.addActionListener( this );
	  buAddPosition.addActionListener(tablePositionen);
	  buAddPosition.setActionCommand("addPosition");
	  buBeenden.addActionListener( this );
		buAddFirm.addActionListener(this);
		loadFirmen();

		this.setSize(570, 540);
		Point p = frame.getLocation();
	  Point p2 = frame.frame.getLocation();
	  setLocation((int)p.getX() + (int)p2.getX() + 50 , (int)p.getY() + (int)p2.getY() + 100 );

	}

	private void setData(Angebot angebot){
 	  cbFirmen.setSelectedItem(angebot.getAnbieter());
 		tfDate.setValue(angebot.getDatum());
		
		PositionsTableModel ptm = (PositionsTableModel)tablePositionen.getModel();

		tfNetto.setValue(new Float(ptm.getOrderSum() - ptm.get7PercentSum() - ptm.get16PercentSum()));
		tf7MwSt.setValue(new Float(ptm.get7PercentSum()));
		tf16MwSt.setValue(new Float(ptm.get16PercentSum()));
		tfBestellsumme.setValue(new Float(angebot.getSumme()));
	}

	private String checkData(){
		String error = "";

		error += (tfDate.getText().equals("") ? "- Datum \n" : "");
		error += (tfBestellsumme.getText().equals("") ? "- Bestellsumme \n" : "");

		return error;
	}

	/**
	 * lädt alle Firmen in die Combobox der Firmenauswahl ein
	 *
	 */
	private void loadFirmen(){
	  try{
		  ArrayList firmen = frame.frame.getApplicationServer().getFirmen();

			if(firmen != null){
				cbFirmen.removeAllItems();
				for(int i = 0; i < firmen.size(); i++){
					cbFirmen.addItem(firmen.get(i));
				}
			}
	  }catch(Exception e){
		  System.out.println(e);
	  }
  }

  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(165, 163, 151)),"Positionen");
    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(165, 163, 151)),"Anbieter");
    this.getContentPane().setLayout(null);
    scrollPositionen.setBounds(new Rectangle(11, 22, 537, 178));
    buSpeichern.setBounds(new Rectangle(142, 481, 120, 25));
    buSpeichern.setText("Speichern");
    buBeenden.setBounds(new Rectangle(283, 481, 120, 25));
    buBeenden.setText("Beenden");
    jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel1.setVerifyInputWhenFocusTarget(true);
    jLabel1.setText("Firma:");
    jLabel1.setBounds(new Rectangle(11, 52, 54, 15));
    jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setText("Straße und Nr.:");
    jLabel2.setBounds(new Rectangle(11, 80, 90, 15));
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setText("PLZ:");
    jLabel3.setBounds(new Rectangle(11, 107, 34, 15));
    jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel4.setText("Ort:");
    jLabel4.setBounds(new Rectangle(92, 107, 34, 15));
    tfFirma.setFont(new java.awt.Font("Dialog", 0, 12));
    tfFirma.setEditable(false);
    tfFirma.setText("");
    tfFirma.setBounds(new Rectangle(114, 49, 427, 21));
    tfStrasse.setFont(new java.awt.Font("Dialog", 0, 12));
    tfStrasse.setEditable(false);
    tfStrasse.setText("");
    tfStrasse.setBounds(new Rectangle(114, 77, 427, 21));
    tfPLZ.setFont(new java.awt.Font("Dialog", 0, 12));
    tfPLZ.setToolTipText("");
    tfPLZ.setEditable(false);
    tfPLZ.setText("");
    tfPLZ.setBounds(new Rectangle(40, 104, 49, 21));
    tfOrt.setFont(new java.awt.Font("Dialog", 0, 12));
    tfOrt.setEditable(false);
    tfOrt.setText("");
    tfOrt.setBounds(new Rectangle(114, 104, 427, 21));
    buAddPosition.setBounds(new Rectangle(11, 201, 164, 25));
    buAddPosition.setText("Position hinzufügen");
    jLabel9.setFont(new java.awt.Font("Dialog", 1, 11));
    jLabel9.setText("Angebot vom:(Datum)");
    jLabel9.setBounds(new Rectangle(10, 151, 127, 15));
    tfDate.setBounds(new Rectangle(140, 148, 105, 21));
    jLabel11.setBounds(new Rectangle(11, 25, 107, 15));
    jLabel11.setText("Firmenauswahl:");
    jLabel11.setVerifyInputWhenFocusTarget(true);
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    cbFirmen.setBounds(new Rectangle(114, 22, 322, 21));
    buAddFirm.setBounds(new Rectangle(443, 22, 99, 21));
    buAddFirm.setText("add Firm");
		tfBestellsumme.setBounds(new Rectangle(405, 277, 117, 21));
		tfBestellsumme.setFont(new java.awt.Font("SansSerif", 1, 12));
    jLabel12.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel12.setText("7 % MwSt.");
    jLabel12.setBounds(new Rectangle(289, 230, 79, 15));
    tf16MwSt.setBounds(new Rectangle(405, 251, 117, 21));
    tf16MwSt.setEnabled(false);
    tf16MwSt.setEditable(false);
    tf7MwSt.setBounds(new Rectangle(405, 227, 117, 21));
    tf7MwSt.setEnabled(false);
    tf7MwSt.setEditable(false);
    jLabel13.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel13.setText("Gesamtnettopreis:");
    jLabel13.setBounds(new Rectangle(289, 206, 115, 15));
    jLabel14.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel14.setText("Bestellsumme:");
    jLabel14.setBounds(new Rectangle(289, 280, 96, 15));
    jLabel15.setBounds(new Rectangle(289, 254, 79, 15));
    jLabel15.setText("16 % MwSt.");
    jLabel15.setFont(new java.awt.Font("Dialog", 0, 12));
    tfNetto.setBounds(new Rectangle(405, 203, 117, 21));
    tfNetto.setEnabled(false);
    tfNetto.setEditable(false);
    jPanel1.setBorder(titledBorder1);
    jPanel1.setBounds(new Rectangle(7, 171, 556, 306));
    jPanel1.setLayout(null);
    jPanel2.setBorder(titledBorder2);
    jPanel2.setBounds(new Rectangle(8, 8, 556, 134));
    jPanel2.setLayout(null);
    jPanel1.add(scrollPositionen, null);
    jPanel1.add(tfBestellsumme, null);
    jPanel1.add(jLabel13, null);
    jPanel1.add(tfNetto, null);
    jPanel1.add(jLabel12, null);
    jPanel1.add(tf7MwSt, null);
    jPanel1.add(jLabel15, null);
    jPanel1.add(tf16MwSt, null);
    jPanel1.add(jLabel14, null);
    jPanel1.add(buAddPosition, null);
    this.getContentPane().add(jLabel9, null);
    this.getContentPane().add(buBeenden, null);
    this.getContentPane().add(buSpeichern, null);
    scrollPositionen.getViewport().add(tablePositionen, null);
    jPanel2.add(cbFirmen, null);
    jPanel2.add(jLabel1, null);
    jPanel2.add(tfFirma, null);
    jPanel2.add(jLabel2, null);
    jPanel2.add(tfStrasse, null);
    jPanel2.add(tfPLZ, null);
    jPanel2.add(jLabel11, null);
    jPanel2.add(jLabel3, null);
    jPanel2.add(tfOrt, null);
    jPanel2.add(jLabel4, null);
    jPanel2.add(buAddFirm, null);
    this.getContentPane().add(tfDate, null);
    this.getContentPane().add(jPanel1, null);
    this.getContentPane().add(jPanel2, null);
  }

	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == buSpeichern ) {
			setAngebot();
		}else if ( e.getSource() == buAddPosition ) {
//			DefaultTableModel dtm = (DefaultTableModel)tablePositionen.getModel();
//			Object[] o = {new Integer(1),"",new Float(0), new Float(0.16), new Float(0), new Float(0),"Del",new Position()};
//
//			dtm.addRow(o);
//			dtm.fireTableRowsInserted(dtm.getRowCount(),dtm.getRowCount());

		}else if(e.getSource() == buAddFirm){
			frame.frame.addChild( new Firmenverwaltung(frame.frame) );
		}else if(e.getSource() == buBeenden){
			this.dispose();
		}
	}

	void setAngebot() {
		ArrayList positionen = new ArrayList();
		PositionsTableModel dtm = (PositionsTableModel)tablePositionen.getModel();
		String emptyFields = checkData();

		if(!emptyFields.equals(""))
			JOptionPane.showConfirmDialog( 	getComponent(0),
																		  "Es müssen folgende Felder ausgefüllt werden : \n" + emptyFields,
																		  "Warnung",
																		  JOptionPane.CANCEL_OPTION,
																		  JOptionPane.ERROR_MESSAGE,
																		  null);
		else{
//			for(int i = 0; i < tablePositionen.getRowCount(); i++){
//				Position position = (Position)(dtm.getValueAt(i, 7));
//
//				position.setArtikel("" + (dtm.getValueAt(i, 1)));
//				position.setEinzelPreis(((Float)dtm.getValueAt(i, 2)).floatValue());
//				position.setMenge(((Integer)dtm.getValueAt(i, 0)).intValue());
//				position.setMwst(((Float)dtm.getValueAt(i, 3)).floatValue());
//				position.setRabatt(((Float)dtm.getValueAt(i, 4)).floatValue());
//
//				positionen.add(position);
//			}
			positionen = tablePositionen.getOrderPositions();

			if(positionen.size() == 0){
				if(angebot != null){
					// die Bestellsumme neu setzen
					Position pos = (Position)angebot.getPositionen().get(0);
					if(pos.getMwst() != 0){
						Position p = new Position();
						p.setMenge(1);
						p.setArtikel("");
						p.setEinzelPreis(((Float)(tfBestellsumme.getValue())).floatValue());
						positionen.add(p);
					}else{
						pos.setEinzelPreis(((Float)(tfBestellsumme.getValue())).floatValue());
						positionen.add((Position)(angebot.getPositionen().get(0)));
					}
				}else{
					Position pos = new Position();
					pos.setMenge(1);
					pos.setArtikel("");
					pos.setEinzelPreis(((Float)(tfBestellsumme.getValue())).floatValue());
					positionen.add(pos);
				}
			}

			java.util.Date datum = (java.util.Date)tfDate.getValue();
			Date sqlDate = new Date(datum.getTime());

			if(angebot == null){
				angebot = new Angebot(positionen, sqlDate, (Firma)cbFirmen.getSelectedItem(), false);
			}else{
				angebot.setDatum(sqlDate);
				angebot.setAnbieter((Firma)cbFirmen.getSelectedItem());
				angebot.setPositionen(positionen);
			}

			frame.insertAngebot(angebot, angebotNr);
			this.dispose();
		}
	}

	/* (Kein Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == cbFirmen){
			Firma firma = (Firma)cbFirmen.getSelectedItem();

			if(firma != null){
				tfFirma.setText(firma.getName());
				tfStrasse.setText(firma.getStrasseNr());
				tfPLZ.setText(firma.getPlz());
				tfOrt.setText(firma.getOrt());
			}
		}
	}

	/* (Kein Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	public void tableChanged(TableModelEvent e) {
		PositionsTableModel ptm = (PositionsTableModel)e.getSource();

		tfNetto.setValue(new Float(ptm.getOrderSum() - ptm.get7PercentSum() - ptm.get16PercentSum()));
		tf7MwSt.setValue(new Float(ptm.get7PercentSum()));
		tf16MwSt.setValue(new Float(ptm.get16PercentSum()));
		tfBestellsumme.setValue(new Float(ptm.getOrderSum()));

		if(ptm.getOrderSum() > 0){
			tfBestellsumme.setEditable(false);
		}else
			tfBestellsumme.setEditable(true);
	}

}