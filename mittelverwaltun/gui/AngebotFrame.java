package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dbObjects.Angebot;
import dbObjects.FBHauptkonto;
import dbObjects.Firma;
import dbObjects.Position;


public class AngebotFrame extends JDialog implements ActionListener, PropertyChangeListener, ItemListener{
  JScrollPane scrollPositionen = new JScrollPane();
  JButton buSpeichern = new JButton();
  JButton buBeenden = new JButton();
  FBHauptkonto hauptkonto;
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JTextField tfFirma = new JTextField();
  JTextField tfStrasse = new JTextField();
  JTextField tfPLZ = new JTextField();
  JTextField tfOrt = new JTextField();
  JLabel jLabel5 = new JLabel();
  JButton buAddPosition = new JButton();
  PositionenTable tablePositionen;
  JLabel jLabel6 = new JLabel();
  CurrencyTextField tfBestellsumme = new CurrencyTextField();
  JLabel jLabel7 = new JLabel();
  JLabel jLabel8 = new JLabel();
  JLabel labNetto = new JLabel();
  JLabel labMwSt16 = new JLabel();
  JLabel labMwSt7 = new JLabel();
  JLabel jLabel9 = new JLabel();
  JFormattedTextField tfDate = new JFormattedTextField(DateFormat.getDateInstance());
  BestellungNormal frame = null;
  int angebotNr = -1;
  JLabel jLabel10 = new JLabel();
  JLabel jLabel11 = new JLabel();
  JComboBox cbFirmen = new JComboBox();
  JButton buAddFirm = new JButton();


  public AngebotFrame(BestellungNormal frame) {
    super(JOptionPane.getFrameForComponent(frame), "Angebot", false);
    this.frame = frame;

		tablePositionen = new PositionenTable();
		cbFirmen.addItemListener(this);
		
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    tfDate.setValue(new Date(System.currentTimeMillis()));
	
		tablePositionen.addPropertyChangeListener(this);
		buSpeichern.addActionListener( this );
		buAddPosition.addActionListener(this);
		buBeenden.setIcon(Functions.getCloseIcon(getClass()));
		buBeenden.addActionListener( this );
		buAddFirm.addActionListener(this);

		this.setSize(540, 480);
		Point p = frame.getLocation();
	  Point p2 = frame.frame.getLocation();
		loadFirmen();

		setLocation((int)p.getX() + (int)p2.getX() + 50 , (int)p.getY() + (int)p2.getY() + 100 );
  }

	  public AngebotFrame(BestellungNormal frame, Angebot angebot, int angebotNr) {
			super(JOptionPane.getFrameForComponent(null), "Angebot", false);
			this.frame = frame;
			this.angebotNr = angebotNr;
			tablePositionen = new PositionenTable(angebot.getPositionen());
			tablePositionen.addPropertyChangeListener(this);
			cbFirmen.addItemListener(this);

			try {
			  jbInit();
			  pack();
			}
			catch(Exception ex) {
			  ex.printStackTrace();
			}
			
		  buSpeichern.addActionListener( this );
		  buAddPosition.addActionListener(this);
		  buBeenden.setIcon(Functions.getCloseIcon(getClass()));
		  buBeenden.addActionListener( this );
			buAddFirm.addActionListener(this);
			setData(angebot);
		  this.setBounds(100,100,540, 480);
			loadFirmen();
			setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
			
	 }

	 private void setData(Angebot angebot){
	 	  cbFirmen.setSelectedItem(angebot.getAnbieter());
	 		tfDate.setValue(angebot.getDatum());
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
    this.getContentPane().setLayout(null);
    scrollPositionen.setBounds(new Rectangle(9, 130, 525, 178));
    buSpeichern.setBounds(new Rectangle(112, 410, 103, 25));
    buSpeichern.setText("Speichern");
    buBeenden.setBounds(new Rectangle(280, 409, 120, 25));
    buBeenden.setText("Beenden");
    jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel1.setVerifyInputWhenFocusTarget(true);
    jLabel1.setText("Firma:");
    jLabel1.setBounds(new Rectangle(12, 42, 54, 15));
    jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel2.setText("Straße und Nr.:");
    jLabel2.setBounds(new Rectangle(12, 68, 90, 15));
    jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel3.setText("PLZ:");
    jLabel3.setBounds(new Rectangle(12, 93, 34, 15));
    jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel4.setText("Ort:");
    jLabel4.setBounds(new Rectangle(104, 93, 34, 15));
    tfFirma.setFont(new java.awt.Font("Dialog", 0, 12));
    tfFirma.setEditable(false);
    tfFirma.setText("");
    tfFirma.setBounds(new Rectangle(104, 36, 232, 21));
    tfStrasse.setFont(new java.awt.Font("Dialog", 0, 12));
    tfStrasse.setEditable(false);
    tfStrasse.setText("");
    tfStrasse.setBounds(new Rectangle(104, 62, 232, 21));
    tfPLZ.setFont(new java.awt.Font("Dialog", 0, 12));
    tfPLZ.setToolTipText("");
    tfPLZ.setEditable(false);
    tfPLZ.setText("");
    tfPLZ.setBounds(new Rectangle(43, 87, 49, 21));
    tfOrt.setFont(new java.awt.Font("Dialog", 0, 12));
    tfOrt.setEditable(false);
    tfOrt.setText("");
    tfOrt.setBounds(new Rectangle(135, 87, 203, 21));
    jLabel5.setText("Positionen");
    jLabel5.setBounds(new Rectangle(11, 111, 85, 15));
    buAddPosition.setBounds(new Rectangle(9, 313, 164, 25));
    buAddPosition.setText("Position hinzufügen");
    jLabel6.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel6.setText("Bestellsumme:");
    jLabel6.setBounds(new Rectangle(212, 384, 96, 15));
//    tfBestellsumme.setText("");
    tfBestellsumme.setBounds(new Rectangle(330, 378, 117, 21));
    jLabel7.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel7.setText("Gesamtnettopreis:");
    jLabel7.setBounds(new Rectangle(212, 317, 115, 15));
    jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel8.setText("7 % MwSt.");
    jLabel8.setBounds(new Rectangle(213, 338, 79, 15));
    labNetto.setFont(new java.awt.Font("Dialog", 0, 12));
    labNetto.setText("");
    labNetto.setBounds(new Rectangle(330, 317, 117, 15));
		labMwSt7.setFont(new java.awt.Font("Dialog", 0, 12));
		labMwSt7.setText("");
		labMwSt7.setBounds(new Rectangle(330, 338, 117, 15));
    labMwSt16.setFont(new java.awt.Font("Dialog", 0, 12));
    labMwSt16.setText("");
    labMwSt16.setBounds(new Rectangle(330, 358, 117, 15));
    jLabel9.setText("Angebot vom:(Datum)");
    jLabel9.setBounds(new Rectangle(402, 75, 127, 15));
    tfDate.setBounds(new Rectangle(400, 95, 105, 21));
    jLabel10.setBounds(new Rectangle(212, 358, 79, 15));
    jLabel10.setText("16 % MwSt.");
    jLabel10.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel11.setBounds(new Rectangle(12, 12, 107, 15));
    jLabel11.setText("Firmenauswahl:");
    jLabel11.setVerifyInputWhenFocusTarget(true);
    jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
    cbFirmen.setBounds(new Rectangle(114, 9, 251, 21));
    buAddFirm.setBounds(new Rectangle(412, 5, 99, 25));
    buAddFirm.setText("add Firm");
    scrollPositionen.getViewport().add(tablePositionen, null);
    this.getContentPane().add(buAddPosition, null);
    this.getContentPane().add(jLabel5, null);
    this.getContentPane().add(scrollPositionen, null);
    this.getContentPane().add(labNetto, null);
    this.getContentPane().add(jLabel7, null);
    this.getContentPane().add(buBeenden, null);
    this.getContentPane().add(buSpeichern, null);
    this.getContentPane().add(tfBestellsumme, null);
    this.getContentPane().add(jLabel6, null);
    this.getContentPane().add(jLabel8, null);
    this.getContentPane().add(labMwSt7, null);
    this.getContentPane().add(jLabel10, null);
    this.getContentPane().add(labMwSt16, null);
    this.getContentPane().add(tfDate, null);
    this.getContentPane().add(jLabel9, null);
    this.getContentPane().add(tfOrt, null);
    this.getContentPane().add(jLabel1, null);
    this.getContentPane().add(tfFirma, null);
    this.getContentPane().add(jLabel2, null);
    this.getContentPane().add(tfStrasse, null);
    this.getContentPane().add(jLabel3, null);
    this.getContentPane().add(tfPLZ, null);
    this.getContentPane().add(jLabel4, null);
    this.getContentPane().add(jLabel11, null);
    this.getContentPane().add(cbFirmen, null);
    this.getContentPane().add(buAddFirm, null);
  }

	public void actionPerformed(ActionEvent e) {
		if ( e.getSource() == buSpeichern ) {
			setAngebot();
		}else if ( e.getSource() == buAddPosition ) {
			DefaultTableModel dtm = (DefaultTableModel)tablePositionen.getModel();
			Object[] o = {new Integer(1),"",new Float(0), new Float(0.16), new Float(0), new Float(0)};

			dtm.addRow(o);
			dtm.fireTableRowsInserted(dtm.getRowCount(),dtm.getRowCount());

		}else if(e.getSource() == buAddFirm){
			frame.frame.addChild( new Firmenverwaltung(frame.frame) );
		}else if(e.getSource() == buBeenden){
			this.dispose();
		}
	}

	void setAngebot() {
		ArrayList positionen = new ArrayList();
		DefaultTableModel dtm = (DefaultTableModel)tablePositionen.getModel();
		float sum = 0;
		String emptyFields = checkData();

		if(!emptyFields.equals(""))
			JOptionPane.showConfirmDialog( 	getComponent(0),
																		  "Es müssen folgende Felder ausgefüllt werden : \n" + emptyFields,
																		  "Warnung",
																		  JOptionPane.CANCEL_OPTION,
																		  JOptionPane.ERROR_MESSAGE,
																		  null);
		else{
			for(int i = 0; i < tablePositionen.getRowCount(); i++){
					Position position = new Position(		(String)(dtm.getValueAt(i, 1)),
																							((Float)dtm.getValueAt(i, 2)).floatValue(),
																							((Integer)dtm.getValueAt(i, 0)).intValue(),
																							((Float)dtm.getValueAt(i, 3)).floatValue(),
																							((Float)dtm.getValueAt(i, 4)).floatValue(),
																							frame.frame.getBenutzer().getKostenstelle());
					positionen.add(position);
			 }
			 	
				Angebot angebot = null;
				Object o = tfDate.getValue();
				
				java.util.Date datum = (java.util.Date)tfDate.getValue();
				Date sqlDate = new Date(datum.getTime());
				
				if(positionen.size() == 0)
					angebot = new Angebot(positionen, sqlDate, (Firma)cbFirmen.getSelectedItem(), ((Float)(tfBestellsumme.getValue())).floatValue());
				else
					angebot = new Angebot(positionen, sqlDate, (Firma)cbFirmen.getSelectedItem());
					
				frame.insertAngebot(angebot, angebotNr);
				this.dispose();
		}
	}




	public void propertyChange(PropertyChangeEvent e) {
		if(e.getSource() == tablePositionen){
			DefaultTableModel dtm = (DefaultTableModel)tablePositionen.getModel();
		  float sum = 0;
		  float gesamt = 0;
		  float netto = 0;
		  float mwst = 0;
		  float mwst7 = 0;
		  float mwst16 = 0;

		  for(int i = 0; i < tablePositionen.getRowCount(); i++){
			gesamt = ((Float)dtm.getValueAt(i, 5)).floatValue();
				mwst = ((Float)dtm.getValueAt(i, 3)).floatValue();
				sum += gesamt;
				if(mwst == 0.16f)
					mwst16 += (gesamt / 116) * 16;
				else
					mwst7 += (gesamt / 107) * 7;
		  }

		  netto = sum - mwst16 - mwst7;
		  labNetto.setText(NumberFormat.getCurrencyInstance().format(netto));
		  labMwSt7.setText(NumberFormat.getCurrencyInstance().format(mwst7));
		  labMwSt16.setText(NumberFormat.getCurrencyInstance().format(mwst16));
		  tfBestellsumme.setValue(new Float(sum));
		  if(tablePositionen.getRowCount() == 0)
				tfBestellsumme.setEditable(true);
		  else
				tfBestellsumme.setEditable(false);
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

}