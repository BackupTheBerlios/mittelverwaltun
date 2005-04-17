package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import dbObjects.*;
import applicationServer.*;

/**
 * InternalFrame zum Umbuchen der FBKonten. <br>
 * 1. Es ist möglich von einem FBHauptkonto auf ein anderes FBHauptkonto das Budget umzubuchen. <br>
 * 2. Es ist möglich von einem FBHauptkonto auf eins seiner FBUnterkonten das Budget umzubuchen. <br>
 * 3. Es ist möglich von einem FBUnterkonto auf das übergeordnete FBHauptkonto das Budget umzubuchen. <br>
 * @author w.flat
 */
public class FBKontenUmbuchen extends JInternalFrame implements ActionListener, TreeSelectionListener, ListSelectionListener {
	
	JScrollPane scrollVon = new JScrollPane();
	FBKontenTree treeKonten;
	JScrollPane scrollHaupt = new JScrollPane();
	DefaultListModel listModelHaupt = new DefaultListModel();
	JList listHaupt = new JList(listModelHaupt);
	JScrollPane scrollUnter = new JScrollPane();
	DefaultListModel listModelUnter = new DefaultListModel();
	JList listUnter = new JList(listModelUnter);
	JLabel labVon = new JLabel();
	JLabel labNach = new JLabel();
	JLabel labKontostandVon = new JLabel();
	CurrencyTextField tfKontostandVon = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	JLabel labKontostandNach = new JLabel();
	CurrencyTextField tfKontostandNach = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	JLabel labBetrag = new JLabel();
	CurrencyTextField tfBetrag = new CurrencyTextField( 0, Integer.MAX_VALUE );
	JButton buBuchen = new JButton();
	JButton buAktualisieren = new JButton();
	JButton buBeenden = new JButton();
	JLabel labHauptkonten = new JLabel();
	JLabel labUnterkonten = new JLabel();
	MainFrame frame;

	/**
	 * Erzeugen vom Frame zum Umbuchen der FBMittel. 
	 * @param frame = MainFrame in dem das JInternalFrame liegt und welches den ApplicationServer besitzt.
	 * author w.flat
	 */
	public FBKontenUmbuchen( MainFrame frame ) {
		super( "FBKonten umbuchen" );
		this.setClosable( true );
		this.setIconifiable( true);
		this.frame = frame;

		try {
			jbInit();
			loadKonten();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
		
		this.setSize( 540, 398 );
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
    
	}
	
	/**
	 * Laden der Konten aus der Datenbank in den Baum.
	 */
	void loadKonten() {
		if( frame != null ) {
			try {
				treeKonten.delTree();		// Den aktuellen Baum löschen
				Benutzer b = frame.getBenutzer();
				if (b != null){
					if (b.getSichtbarkeit() == Benutzer.VIEW_FACHBEREICH){
						// Alle Konten holen
						treeKonten.loadInstituts( frame.getApplicationServer().getInstitutesWithAccounts() );	
					}else if (b.getSichtbarkeit() == Benutzer.VIEW_INSTITUT){
						// Nur Konten des Institutes des angemeldeten Benutzers holen
						treeKonten.loadInstituts( frame.getApplicationServer().getInstituteWithAccounts(b.getKostenstelle(), true) );	
					}
				}
				
			} catch (ApplicationServerException e) {
				System.out.println( e.toString() );
			}
		}
	}
	
	/**
	 * Anzeigen vom Kontostand des ausgewählten Kontos, von dem abgebucht wird.
	 * Und anschließend laden aller FBKonten, die einen Betrag erhalten können.
	 */
	void showVonAmount() {
		listModelHaupt.clear();					// Zuerst werden die Listen gelöscht
		listModelUnter.clear();					// Zuerst werden die Listen gelöscht
		tfBetrag.setValue( new Float( 0 ) );	// Der Betrag der überwiesen wird ist am Anfang immer 0
		if( treeKonten.institutIsSelected() || treeKonten.rootIsSelected() ) {	// Wenn kein Konto ausgewählt
			tfKontostandNach.setValue( new Float( 0 ) );	// Überall den Betrag 0 eintragen
			tfKontostandVon.setValue( new Float( 0 ) );
			buBuchen.setEnabled( false );					// Man keine Buchung durchführen
			tfBetrag.setEnabled( false );
		} else if( treeKonten.fbHauptkontoIsSelected() ) {
			// Den Kontostand vom ausfewähltem Konto anzeigen
			tfKontostandVon.setValue( new Float( treeKonten.getFBHauptkonto().getBudget() ) );
			loadHauptUnter();						// Alle bebuchbaren Konten in die Liste laden
			if( listModelHaupt.size() > 0 ) {		// Wenn es HauptKonten gibt
				listHaupt.setSelectedIndex( 0 );	// Dann das erste Konto markieren
				buBuchen.setEnabled( true );		// und man kann etwas buchen
				tfBetrag.setEnabled( true );
			} else if( listModelUnter.size() > 0 ) {	// Wenn es Unterkonten gibt
				listUnter.setSelectedIndex( 0 );	// Dann das erste Konto markieren
				buBuchen.setEnabled( true );		// und man kann etwas buchen
				tfBetrag.setEnabled( true );
			} else {
				buBuchen.setEnabled( false );		// Sonst kann man nichts buchen
				tfBetrag.setEnabled( false );	
			}
		// FBUnterkonto ausgewählt, dann kann man nur auf das übergeordnete FBHauptkonto buchen
		} else if( treeKonten.fbUnterkontoIsSelected() ) {
			tfKontostandVon.setValue( new Float( treeKonten.getFBUnterkonto().getBudget() ) );	// Budget vom Unterkonto
			listModelHaupt.addElement( treeKonten.getFBHauptkonto() );		// Hauptkonten-Liste enthält nur ein Element
			listHaupt.setSelectedIndex( 0 );								// dieses Element wird selektiert
			buBuchen.setEnabled( true );									// und man kann Buchung durchführen
			tfBetrag.setEnabled( true );
		}
	}
		
	/**
	 * Laden aller FBhauptkonten und FBUnterkonten, die vom ausgewältem FBHauptkonto im Baum Geld bekommen können.
	 * Dies sind die Unterkonten von diesem Hauptkonto und die anderen FBHauptkonten außer dem ausgewältem im Baum.
	 */
	void loadHauptUnter() {
		ArrayList temp;							// temporäre Liste für die Haupt- und Unterkonten
		ArrayList instituts = treeKonten.getInstituts();				// Alle Institute aus dem Baum holen
		FBHauptkonto selected = treeKonten.getFBHauptkonto();			// Das selektierte FBHauptkonto
		for( int i = 0; i < instituts.size(); i++ ) {
			temp = ((Institut)instituts.get( i )).getHauptkonten();		// Die Hauptkonten eines Instituts
			for( int j = 0; j < temp.size(); j++ ) {
				if( ((FBHauptkonto)temp.get( j )) == selected )			// Wenn das selektierte Konto,
					continue;											// dann nicht in die Liste aufnehmen
				listModelHaupt.addElement( temp.get( j ) );				// Sonst einfügen des Kontos in die Hauptkonten-Liste
			}
		}
		temp = treeKonten.getFBHauptkonto().getUnterkonten();			// Dann alle Uterkonten in die Unterkonten-Liste
		for( int i = 0; i < temp.size(); i++ ) {						// übernehmen
			listModelUnter.addElement( temp.get( i ) );
		}
	}
	
	/**
	 * Initialisieren der graphischen Oberfläche. 
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		this.getContentPane().setLayout(null);
		labVon.setFont(new java.awt.Font("SansSerif", 0, 13));
		labVon.setForeground(Color.blue);
		labVon.setText("Von");
		labVon.setBounds(new Rectangle(10, 10, 100, 18));
		labNach.setFont(new java.awt.Font("SansSerif", 0, 13));
		labNach.setForeground(Color.blue);
		labNach.setText("Nach");
		labNach.setBounds(new Rectangle(270, 10, 100, 18));
		labKontostandVon.setText("Kontostand");
		labKontostandVon.setBounds(new Rectangle(10, 250, 100, 15));
		labKontostandNach.setText("Kontostand");
		labKontostandNach.setBounds(new Rectangle(270, 250, 100, 15));
		labBetrag.setText("Betrag");
		labBetrag.setBounds(new Rectangle(10, 290, 100, 15));
		buBuchen.setBounds(new Rectangle(270, 288, 250, 25));
		buBuchen.setText("Buchen");
		buAktualisieren.setBounds(new Rectangle(10, 330, 250, 25));
		buAktualisieren.setText("Aktualisieren");
		buBeenden.setBounds(new Rectangle(270, 330, 250, 25));
		buBeenden.setText("Beenden");
		tfKontostandNach.setText("");
		tfKontostandNach.setBounds(new Rectangle(370, 250, 150, 21));
		tfKontostandVon.setText("");
		tfKontostandVon.setBounds(new Rectangle(110, 250, 150, 21));
		tfBetrag.setText("");
		tfBetrag.setBounds(new Rectangle(110, 290, 150, 21));
		labHauptkonten.setText("Hauptkonten");
		labHauptkonten.setBounds(new Rectangle(270, 30, 250, 15));
		this.setLocale(java.util.Locale.getDefault());
		labUnterkonten.setText("Unterkonten");
		labUnterkonten.setBounds(new Rectangle(270, 135, 250, 15));
		scrollUnter.setBounds(new Rectangle(270, 155, 250, 75));
		scrollVon.setBounds(new Rectangle(10, 30, 250, 200));
		scrollHaupt.setBounds(new Rectangle(270, 50, 250, 75));
		this.getContentPane().add(scrollVon, null);
		this.getContentPane().add(labVon, null);
		this.getContentPane().add(labNach, null);
		this.getContentPane().add(labKontostandVon, null);
		this.getContentPane().add(tfKontostandVon, null);
		this.getContentPane().add(labKontostandNach, null);
		this.getContentPane().add(tfKontostandNach, null);
		this.getContentPane().add(labBetrag, null);
		this.getContentPane().add(tfBetrag, null);
		this.getContentPane().add(buBuchen, null);
		this.getContentPane().add(buAktualisieren, null);
		this.getContentPane().add(buBeenden, null);
		this.getContentPane().add(scrollHaupt, null);
		this.getContentPane().add(labHauptkonten, null);
		scrollHaupt.getViewport().add(listHaupt, null);
		scrollVon.getViewport().add(treeKonten = new FBKontenTree( this, "FBKonten" ), null);
		this.getContentPane().add(labUnterkonten, null);
		this.getContentPane().add(scrollUnter, null);
		scrollUnter.getViewport().add(listUnter, null);
		
		tfKontostandNach.setEnabled( false );
		tfKontostandVon.setEnabled( false );
		buAktualisieren.addActionListener( this );
		buBuchen.addActionListener( this );
		buBeenden.addActionListener( this );
		buAktualisieren.setIcon(Functions.getRefreshIcon(getClass()));
		buBuchen.setIcon(Functions.getEditIcon(getClass()));
		buBeenden.setIcon(Functions.getCloseIcon(getClass()));
		listHaupt.addListSelectionListener( this );
		listUnter.addListSelectionListener( this );
		listHaupt.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		listUnter.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
	}
	
	/**
	 * Die Buchung von einem FBHauptkonto auf ein anderes FBHauptkonto durchführen.
	 * @return error-String
	 */
	String bucheHauptHaupt() {
		String error = "";
		
		FBHauptkonto from = treeKonten.getFBHauptkonto();
		FBHauptkonto to = (FBHauptkonto)listModelHaupt.get( listHaupt.getSelectedIndex() );
		float amount = ((Float)tfBetrag.getValue()).floatValue(); 
		if( (from.getBudget() + from.getDispoLimit()) - amount < 0 )
			error += " - Der Betrag ist zu hoch.\n";
		if( error.equalsIgnoreCase( "" ) ) {
			try {
				frame.getApplicationServer().buche( frame.getBenutzer(), (FBHauptkonto)from.clone(), 
																			(FBHauptkonto)to.clone(), amount );
				from.setBudget( from.getBudget() - amount );
				to.setBudget( to.getBudget() + amount );
				tfKontostandVon.setValue( new Float( from.getBudget() ) );
				tfKontostandNach.setValue( new Float( to.getBudget() ) );
				tfBetrag.setValue( new Float( 0 ) );
			} catch( ApplicationServerException e ) {
				error += " - " + e.toString() + ".\n";
			}
		}
		
		return error;
	}
	
	/**
	 * Die Buchung von einem FBHauptkonto auf ein FBUnterkonto durchführen.
	 * @return error-String
	 */
	String bucheHauptUnter() {
		String error = "";
		
		FBHauptkonto from = treeKonten.getFBHauptkonto();
		FBUnterkonto to = (FBUnterkonto)listModelUnter.get( listUnter.getSelectedIndex() );
		float amount = ((Float)tfBetrag.getValue()).floatValue(); 
		if( (from.getBudget() + from.getDispoLimit()) - amount < 0 )
			error += " - Der Betrag ist zu hoch.\n";
		if( error.equalsIgnoreCase( "" ) ) {
			try {
				frame.getApplicationServer().buche( frame.getBenutzer(), (FBHauptkonto)from.clone(), 
																			(FBUnterkonto)to.clone(), amount );
				from.setBudget( from.getBudget() - amount );
				to.setBudget( to.getBudget() + amount );
				tfKontostandVon.setValue( new Float( from.getBudget() ) );
				tfKontostandNach.setValue( new Float( to.getBudget() ) );
				tfBetrag.setValue( new Float( 0 ) );
			} catch( ApplicationServerException e ) {
				error += " - " + e.toString() + ".\n";
			}
		}
		
		return error;
	}

	/**
	 * Die Buchung von einem FBUnterkonto auf ein FBHauptkonto durchführen.
	 * @return error-String
	 */
	String bucheUnterHaupt() {
		String error = "";
		
		FBUnterkonto from = treeKonten.getFBUnterkonto();
		FBHauptkonto to = (FBHauptkonto)listModelHaupt.get( listHaupt.getSelectedIndex() );
		float amount = ((Float)tfBetrag.getValue()).floatValue(); 
		if( from.getBudget() - amount < 0 )
			error += " - Der Betrag ist zu hoch.\n";
		if( error.equalsIgnoreCase( "" ) ) {
			try {
				frame.getApplicationServer().buche( frame.getBenutzer(), (FBUnterkonto)from.clone(), 
																			(FBHauptkonto)to.clone(), amount );
				from.setBudget( from.getBudget() - amount );
				to.setBudget( to.getBudget() + amount );
				tfKontostandVon.setValue( new Float( from.getBudget() ) );
				tfKontostandNach.setValue( new Float( to.getBudget() ) );
				tfBetrag.setValue( new Float( 0 ) );
			} catch( ApplicationServerException e ) {
				error += " - " + e.toString() + ".\n";
			}
		}
		
		return error;
	}

	/**
	 * Verarbeitung der Knopfereignisse. 
	 */
	public void actionPerformed(ActionEvent e) {
		String error = "";
		if ( e.getSource() == buBuchen ) {
			if( treeKonten.fbHauptkontoIsSelected() ) {
				if( listHaupt.getSelectedIndex() >= 0 ) {
					error += bucheHauptHaupt();
				} else {
					error += bucheHauptUnter();
				}
			} else if( treeKonten.fbUnterkontoIsSelected() ) {
				error += bucheUnterHaupt();
			}
		} else if ( e.getSource() == buAktualisieren ) {
			loadKonten();
		} else if ( e.getSource() == buBeenden ) {
			this.dispose();
		}
		
		if( !error.equalsIgnoreCase( "" ) ) {
			error = "Folgende Fehler sind aufgetreten:\n" + error;
			JOptionPane.showMessageDialog( this, error,	"Fehler !", JOptionPane.ERROR_MESSAGE );
		}
	}

	/**
	 * Verarbeitung der Änderungen im Baum.
	 */
	public void valueChanged(TreeSelectionEvent e) {
		treeKonten.checkSelection( e );
		showVonAmount();
	}

	/**
	 * Verarbeitung der Veränderungen in den Listen.
	 */
	public void valueChanged(ListSelectionEvent e) {
		if( e.getSource() == listHaupt ) {
			if( listHaupt.getSelectedIndex() >= 0 ) {
				listUnter.clearSelection();
				tfKontostandNach.setValue(
								new Float( ((FBHauptkonto)listModelHaupt.get( listHaupt.getSelectedIndex() )).getBudget() ) );
			}
		} else {
			if( listUnter.getSelectedIndex() >= 0 ) {
				listHaupt.clearSelection();
				tfKontostandNach.setValue(
								new Float( ((FBUnterkonto)listModelUnter.get( listUnter.getSelectedIndex() )).getBudget() ) );
			}
		}
	}

}
