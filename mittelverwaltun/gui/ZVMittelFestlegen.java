package gui;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import dbObjects.*;
import applicationServer.ApplicationServerException;
import java.awt.Rectangle;
import java.awt.event.*;



public class ZVMittelFestlegen extends JInternalFrame implements ActionListener, TreeSelectionListener {

	JScrollPane scrollKonten = new JScrollPane();
	ZVKontenTree treeKonten;
	JLabel labKontostand = new JLabel();
	CurrencyTextField tfKontostand = new CurrencyTextField(
		Integer.MIN_VALUE,
		Integer.MAX_VALUE);
	JLabel labBetrag = new JLabel();
	CurrencyTextField tfBetrag = new CurrencyTextField(
		Integer.MIN_VALUE,
		Integer.MAX_VALUE);
	JButton buBuchen = new JButton();
	JButton buAktualisieren = new JButton();
	JButton buBeenden = new JButton();
	MainFrame frame;

	
	public ZVMittelFestlegen( MainFrame frame ) {
		super( "ZVMittel festlegen" );
		this.setClosable(true);
		this.setIconifiable(true);
		this.frame = frame;
		try {
			jbInit();
			loadZVKonten();
		} catch( Exception e ) {
		}
		this.setSize( 588, 253 );
	}
	
	/**
	 * Den Betrag des ausgewählten Kontos anzeigen.
	 */
	void showAmount() {
		if( treeKonten.rootIsSelected() ) {					// Wenn der WurzelKnoten ausgewählt ist
			tfKontostand.setValue( new Float( 0 ) );		// dann sind alle Beträge 0
			tfBetrag.setValue( new Float( 0 ) );			// und alle Textfelder gesperrt
			tfBetrag.setEnabled( false );
			buBuchen.setEnabled( false );					// Und der Button Buchen ist auch gesperrt
		} else if( treeKonten.zvKontoIsSelected() ) {		// Ist ein ZVKonto ausgewählt
			if( treeKonten.getZVKonto().isTGRKonto() ) {	// dann muss amn schauen ob es sich um ein TGR-Konto handelt
				tfBetrag.setEnabled( true );				// in das Textfeld Betrag kann ein Betrag eingegeben werden
				buBuchen.setEnabled( true );				// der Betrag kann auch übernommen werden 
			} else {										// Wenn ein Titel-Konto
				tfBetrag.setEnabled( false );				// man kann keinen Betrag buchen
				buBuchen.setEnabled( false );				// und der Button Buchen ist gesperrt
			}
			tfKontostand.setValue( new Float( treeKonten.getZVKonto().getTgrBudget() ) );	// den aktuellen Betrag anzeigen
			tfBetrag.setValue( new Float( 0 ) );			// Der Anfangsbetrag ist immer 0
		} else if( treeKonten.zvTitelIsSelected() ) {		// Wenn ein zvTitel ausgewählt ist
			// Wenn es Untertitel gibt, dann können nur Untertitel bebucht werden
			if( treeKonten.getZVTitel().getSubUntertitel().size() > 0 ) {
				tfKontostand.setValue( new Float( 0 ) );	// dann ist der Kontostand 0
				tfBetrag.setEnabled( false );				// man kann keinen Betrag buchen
				buBuchen.setEnabled( false );				// und der Button Buchen ist gesperrt
			} else {										// Wenn es keine Untertitel gibt
				tfBetrag.setEnabled( true );				// in das Textfeld Betrag kann ein Betrag eingegeben werden
				buBuchen.setEnabled( true );				// der Betrag kann auch übernommen werden 
			}
			tfKontostand.setValue( new Float( treeKonten.getZVTitel().getBudget() ) );	// Kontostand anzeigen
			tfBetrag.setValue( new Float( 0 ) );			// Der Anfangsbetrag ist immer 0
		} else if( treeKonten.zvUntertitelIsSelected() ) {
			tfKontostand.setValue( new Float( treeKonten.getZVUntertitel().getBudget() ) );	// Kontostand anzeigen
			tfBetrag.setEnabled( true );					// in das Textfeld Betrag kann ein Betrag eingegeben werden
			buBuchen.setEnabled( true );					// der Betrag kann auch übernommen werden 
			tfBetrag.setValue( new Float( 0 ) );			// Der Anfangsbetrag ist immer 0
		}
	}
	
	/**
	 * Laden der ZVKonten in den Baum.
	 */
	void loadZVKonten() {
		if( frame != null ) {
			try {
				treeKonten.delTree();		// Den aktuellen Baum löschen
				treeKonten.loadZVKonten( frame.getApplicationServer().getZVKonten() );	// Die ZVKonten holen und einfügen
			} catch (ApplicationServerException e) {
				System.out.println( e.toString() );
			}
		}
	}
	
	private void jbInit() throws Exception {
		this.getContentPane().setLayout(null);
		labKontostand.setText("Kontostand");
		labKontostand.setBounds(new Rectangle(320, 10, 100, 15));
		labBetrag.setText("Betrag");
		labBetrag.setBounds(new Rectangle(320, 50, 100, 15));
		tfKontostand.setText("");
		tfKontostand.setBounds(new Rectangle(420, 10, 150, 21));
		tfBetrag.setText("");
		tfBetrag.setBounds(new Rectangle(420, 50, 150, 21));
		buBuchen.setBounds(new Rectangle(320, 100, 250, 25));
		buBuchen.setText("Buchen");
		buAktualisieren.setBounds(new Rectangle(320, 143, 250, 25));
		buAktualisieren.setText("Aktualisieren");
		buBeenden.setBounds(new Rectangle(320, 185, 250, 25));
		buBeenden.setText("Beenden");
		scrollKonten.setBounds(new Rectangle(10, 10, 300, 200));
		this.getContentPane().add(scrollKonten, null);
		this.getContentPane().add(labKontostand, null);
		this.getContentPane().add(tfKontostand, null);
		this.getContentPane().add(labBetrag, null);
		this.getContentPane().add(tfBetrag, null);
		this.getContentPane().add(buBuchen, null);
		this.getContentPane().add(buAktualisieren, null);
		this.getContentPane().add(buBeenden, null);
		scrollKonten.getViewport().add(treeKonten = new ZVKontenTree( this, "ZVKonten" ), null);
		
		this.buAktualisieren.addActionListener( this );
		this.buAktualisieren.setIcon(Functions.getRefreshIcon(getClass()));
		this.buBeenden.addActionListener( this );
		this.buBeenden.setIcon(Functions.getCloseIcon(getClass()));
		this.buBuchen.addActionListener( this );
		this.buBuchen.setIcon(Functions.getEditIcon(getClass()));
		
		this.tfKontostand.setEnabled( false );
	}
	
	/**
	 * Führt die Buchung auf das selektierte ZVKonto durch.
	 * @return error-String
	 */
	String bucheZVKonto() {
		String error = "";
		// Wenn negativer Betrag dann kann man nicht mehr abziehen als auf dem Konto drauf ist
		if( ((Float)tfBetrag.getValue()).floatValue() < 0 ) {
			if( -((Float)tfBetrag.getValue()).floatValue() > treeKonten.getZVKonto().getTgrBudget() )
				error += " - Der Betrag darf nicht größer sein als der Kontostand.\n";
		}
		if( error.equalsIgnoreCase( "" ) ) {
			try {
				ZVKonto copy = (ZVKonto)treeKonten.getZVKonto().clone();
				frame.getApplicationServer().buche( frame.getBenutzer(), copy, ((Float)tfBetrag.getValue()).floatValue() );
				treeKonten.getZVKonto().setTgrBudget( treeKonten.getZVKonto().getTgrBudget() +
														((Float)tfBetrag.getValue()).floatValue() );
				showAmount();
			} catch ( ApplicationServerException e ) {
				error += " - " + e.toString() + "\n";
			}
		}
		
		return error;
	}
	
	/**
	 * Führt die Buchung auf das selektierte ZVTitel durch.
	 * @return error-String
	 */
	String bucheZVTitel() {
		String error = "";
		// Wenn negativer Betrag dann kann man nicht mehr abziehen als auf dem Konto drauf ist
		if( ((Float)tfBetrag.getValue()).floatValue() < 0 ) {
			if( -((Float)tfBetrag.getValue()).floatValue() > treeKonten.getZVTitel().getBudget() )
				error += " - Der Betrag darf nicht größer sein als der Kontostand.\n";
		}
		if( error.equalsIgnoreCase( "" ) ) {
			try {
				ZVTitel copy = (ZVTitel)treeKonten.getZVTitel().clone();
				frame.getApplicationServer().buche( frame.getBenutzer(), copy, ((Float)tfBetrag.getValue()).floatValue() );
				treeKonten.getZVTitel().setBudget( treeKonten.getZVTitel().getBudget() +
														((Float)tfBetrag.getValue()).floatValue() );
				showAmount();
			} catch ( ApplicationServerException e ) {
				error += " - " + e.toString() + "\n";
			}
		}
		
		return error;
	}

	/**
	 * Führt die Buchung auf das selektierte ZVUntertitel durch.
	 * @return error-String
	 */
	String bucheZVUntertitel() {
		String error = "";
		// Wenn negativer Betrag dann kann man nicht mehr abziehen als auf dem Konto drauf ist
		if( ((Float)tfBetrag.getValue()).floatValue() < 0 ) {
			if( -((Float)tfBetrag.getValue()).floatValue() > treeKonten.getZVUntertitel().getBudget() )
				error += " - Der Betrag darf nicht größer sein als der Kontostand.\n";
		}
		if( error.equalsIgnoreCase( "" ) ) {
			try {
				ZVUntertitel copy = (ZVUntertitel)treeKonten.getZVUntertitel().clone();
				frame.getApplicationServer().buche( frame.getBenutzer(), copy, ((Float)tfBetrag.getValue()).floatValue() );
				treeKonten.getZVUntertitel().setBudget( treeKonten.getZVUntertitel().getBudget() +
														((Float)tfBetrag.getValue()).floatValue() );
				showAmount();
			} catch ( ApplicationServerException e ) {
				error += " - " + e.toString() + "\n";
			}
		}
		
		return error;
	}
	
	public void actionPerformed( ActionEvent e ) {
		String error = "";
		if ( e.getSource() == buBuchen ) {
			if( treeKonten.zvKontoIsSelected() ) {				// Ist ein ZVKonto ausgewählt
				error = bucheZVKonto();
			} else if( treeKonten.zvTitelIsSelected() ) {		// Ist ein ZVTitel ausgewählt
				error = bucheZVTitel();
			} else if( treeKonten.zvUntertitelIsSelected() ) {	// Ist ein ZVUntertitel ausgewählt
				error = bucheZVUntertitel();
			}
		} else if ( e.getSource() == buAktualisieren ) {
			loadZVKonten();
		} else if ( e.getSource() == buBeenden ) {
			this.dispose();
		}
		
		if( !error.equalsIgnoreCase( "" ) ) {
			error = "Folgende Fehler sind aufgetreten:\n" + error;
			JOptionPane.showMessageDialog( this, error,	"Fehler !", JOptionPane.ERROR_MESSAGE );
		}
	}


	public void valueChanged(TreeSelectionEvent e) {
		treeKonten.checkSelection( e );
		showAmount();
	}
}

