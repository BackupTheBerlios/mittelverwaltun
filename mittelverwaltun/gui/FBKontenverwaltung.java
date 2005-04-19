package gui;

import javax.swing.*;
import javax.swing.event.*;

import applicationServer.*;
import dbObjects.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;

/**
 * Frame zur Verwaltung der FBHauptkonten. <br>
 * Es können : <br>
 * 1. Neue FBKonten angelegt werden.<br>
 * 2. FBKonten aktualisiert werden. <br>
 * 3. FBkonten gelöscht werden. <br>
 * @author w.flat
 */
public class FBKontenverwaltung extends JInternalFrame implements ActionListener, TreeSelectionListener {

	JScrollPane scrollKonten = new JScrollPane();
	FBKontenTree treeKonten;
	FBHauptkontoPanel fbHauptkontoPanel = new FBHauptkontoPanel();
	FBUnterkontoPanel fbUnterkontoPanel = new FBUnterkontoPanel();
	JLabel labNoPanel = new JLabel("Keine Vorschau verfügbar !");
	JButton buAnlegen = new JButton("Anlegen");
	JButton buAendern = new JButton("Ändern");
	JButton buLoeschen = new JButton("Löschen");
	JButton buBeenden = new JButton("Beenden");
	JButton buAktualisieren = new JButton("Aktualisieren");
	MainFrame frame;

	
	public static void main(String[] args) {
	  JFrame test = new JFrame("FB-Kontenverwaltung");
	  JDesktopPane desk = new JDesktopPane();
	  desk.setDesktopManager(new DefaultDesktopManager());
	  test.setContentPane(desk);
	  test.setBounds(100,100,540,400);
	  JInternalFrame child = new FBKontenverwaltung( null );
	  
	  test.addWindowListener( new WindowAdapter() {
	  	public void windowClosing(WindowEvent e) {
			super.windowClosing(e);
			e.getWindow().dispose();
			System.exit(0);
		}
	  });
	  
	  desk.add(child);
	  test.setVisible(true);
	  child.show();
	
	}
		
	/**
	 * Erzeugen vom Frame zum Verwalten der FBKonten. 
	 * @param frame = MainFrame in dem das JInternalFrame liegt und welches den ApplicationServer besitzt.
	 * author w.flat
	 */
	public FBKontenverwaltung( MainFrame frame ) {
		super( "FB-Kontenverwaltung" );
		this.setClosable( true );
		this.setIconifiable( true);
		this.getContentPane().setLayout( null );
		this.frame = frame;
		
		scrollKonten.getViewport().add( treeKonten = new FBKontenTree( this, "FBKonten" ), null );
		scrollKonten.setBounds(new Rectangle(10, 10, 280, 235));
		this.getContentPane().add(scrollKonten);
		fbHauptkontoPanel.setBounds(new Rectangle(300, 10, 500, 235));
		this.getContentPane().add(fbHauptkontoPanel);
		fbUnterkontoPanel.setBounds(new Rectangle(300, 10, 500, 235));
		this.getContentPane().add(fbUnterkontoPanel);
		labNoPanel.setBounds(new Rectangle(300, 10, 500, 16));
		this.getContentPane().add(labNoPanel);

		buAktualisieren.setBounds(new Rectangle(10, 255, 150, 25));
		buAktualisieren.addActionListener( this );
		this.getContentPane().add(buAktualisieren);
		buAktualisieren.setIcon(Functions.getRefreshIcon(getClass()));
		buAnlegen.setBounds(new Rectangle(170, 255, 150, 25));
		buAnlegen.addActionListener( this );
		this.getContentPane().add(buAnlegen);
		buAnlegen.setIcon(Functions.getAddIcon(getClass()));
		buAendern.setBounds(new Rectangle(330, 255, 150, 25));
		buAendern.addActionListener( this );
		this.getContentPane().add(buAendern);
		buAendern.setIcon(Functions.getEditIcon(getClass()));
		buLoeschen.setBounds(new Rectangle(490, 255, 150, 25));
		buLoeschen.addActionListener( this );
		this.getContentPane().add(buLoeschen);
		buLoeschen.setIcon(Functions.getDelIcon(getClass()));
		buBeenden.setBounds(new Rectangle(650, 255, 150, 25));
		buBeenden.addActionListener( this );
		this.getContentPane().add(buBeenden);
		buBeenden.setIcon(Functions.getCloseIcon(getClass()));

		loadKonten();
		showPanel();
			
		this.setSize( 818, 318 );
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
    
	}
	
	/**
	 * Laden der Konten aus der Datenbank in den Baum
	 */
	void loadKonten() {
		if( frame != null ) {
			try {
				treeKonten.delTree();
				if(frame.getBenutzer().getSichtbarkeit() == Benutzer.VIEW_FACHBEREICH)
				    treeKonten.loadInstituts( frame.getApplicationServer().getInstitutesWithAccounts() );
				else if(frame.getBenutzer().getSichtbarkeit() == Benutzer.VIEW_INSTITUT)
				    treeKonten.loadInstituts( frame.getApplicationServer().getInstituteWithAccounts(
				            						frame.getBenutzer().getKostenstelle(), true) );
			} catch (ApplicationServerException e) {
				System.out.println( e.toString() );
			}
		}
	}
	
	/**
	 * Auswahl des richtigen Panels, das angezeigt werden soll. 
	 */
	void showPanel(){
		if( treeKonten.rootIsSelected() ) {
			fbHauptkontoPanel.setVisible( false );
			fbUnterkontoPanel.setVisible( false );
			labNoPanel.setVisible( true );
		} else if( treeKonten.institutIsSelected() ) {
			fbHauptkontoPanel.setVisible( true );
			fbHauptkontoPanel.setTextfields( treeKonten.getInstitut() );
			fbUnterkontoPanel.setVisible( false );
			labNoPanel.setVisible( false );
		} else if( treeKonten.fbHauptkontoIsSelected() ) {
			fbHauptkontoPanel.setVisible( true );
			fbHauptkontoPanel.setTextfields( treeKonten.getFBHauptkonto() );
			fbUnterkontoPanel.setVisible( false );
			labNoPanel.setVisible( false );
		} else if( treeKonten.fbUnterkontoIsSelected() ) {
			fbHauptkontoPanel.setVisible( false );
			fbUnterkontoPanel.setVisible( true );
			fbUnterkontoPanel.setTextfields( treeKonten.getFBUnterkonto() );
			labNoPanel.setVisible( false );
		}
	}
	
	/**
	 * Ein neues FBHauptkonto in der Datenbank und im Baum erstellen. 
	 * @return String mit Fehlern. Wenn der String leer ist, dann sind keine Fehler aufgetreten.
	 * @throws ApplicationServerException und RemoteException
	 */
	String addFBHauptkonto() throws ApplicationServerException, RemoteException {
		String error = "";
		FBHauptkonto konto = null;
		
		if( !(error += fbHauptkontoPanel.getEditErrorString()).equalsIgnoreCase( "" ) )
			return error;
			
		konto = fbHauptkontoPanel.getNewFBHauptkonto();		// Das neue FNHauptkonto
		konto.setHaushaltsJahrID( frame.getApplicationServer().getCurrentHaushaltsjahrId() );	// Die aktuelle HaushaltsjahrId
		int id = frame.getApplicationServer().addFBHauptkonto( konto );	// In die Datenbank einfügen 
		if( id > 0 ) {		// Wenn es eingefügt wurde
			konto.setId( id );	// Die id in das Konto einfügen
			treeKonten.getInstitut().getHauptkonten().add( konto );	// Das FBHauptkonto dem Institut zufügen
			treeKonten.addNewNode( konto );		// In den Baum einfügen
		} else {
			error += " - Ausnahmefehler ist aufgetreten !\n";
		}
		
		return error;
	}

	/**
	 * Ein neues FBUnterkonto in der Datenbank und im Baum erstellen.
	 * @return String mit Fehlern. Wenn der String leer ist, dann sind keine Fehler aufgetreten.
	 * @throws ApplicationServerException und RemoteException.
	 */
	String addFBUnterkonto() throws ApplicationServerException, RemoteException {
		String error = "";
		FBUnterkonto konto = null;
		
		if( !(error += fbHauptkontoPanel.getAddErrorString()).equalsIgnoreCase( "" ) )
			return error;
			
		konto = fbHauptkontoPanel.getNewFBUnterkonto();		// Das neue FBUnterkonto
		int id = frame.getApplicationServer().addFBUnterkonto( konto );	// Das FBUnterkonto in die Datenbank einfügen
		if( id > 0 ) {		// Wurde eingefügt
			konto.setId( id );		// Die id einfügen
			// Das Konto zu den Unterkonten des FBHauptkontos hinzufügen
			treeKonten.getFBHauptkonto().getUnterkonten().add( konto );
			treeKonten.addNewNode( konto );		// Das neue FBUnterkonto in den Baum einfügen
		} else {
			error += " - Ausnahmefehler ist aufgetreten !\n";
		}
		
		return error;
	}
	
	/**
	 * Ein FBHauptkonto in der Datenbank und im Baum aktualisieren. 
	 * @return String mit Fehlern. Wenn der String leer ist, dann sind keine Fehler aufgetreten.
	 * @throws ApplicationServerException und RemoteException.
	 */
	String setFBHauptkonto() throws ApplicationServerException, RemoteException {
		String error = "";
		FBHauptkonto konto = null;
		
		if( !(error += fbHauptkontoPanel.getEditErrorString()).equalsIgnoreCase( "" ) )
			return error;
		
		konto = fbHauptkontoPanel.getEditFBHauptkonto();	// Das aktualisierte FBHauptkonto
		FBHauptkonto copy = (FBHauptkonto)treeKonten.getFBHauptkonto().cloneWhole();	// Eine Kopie erstellen
		copy.setFBHauptkonto( konto );		// Die Kopie aktualisieren
		if( frame.getApplicationServer().setFBHauptkonto( copy ) == konto.getId() ){	// In der Datenbank aktualisieren
			treeKonten.getFBHauptkonto().setFBHauptkonto( konto );		// Im Baum aktualisieren
			treeKonten.refreshCurrentNode();		// Im Baum neu anzeigen
		} else {
			error += " - Ausnahmefehler ist aufgetreten !\n";
		}
		
		return error;
	}
	
	/**
	 * Ein FBUnterkonto in der Datenbank und im Baum aktualisieren. 
	 * @return String mit Fehlern. Wenn der String leer ist, dann sind keine Fehler aufgetreten.
	 * @throws ApplicationServerException und RemoteException.
	 */
	String setFBUnterkonto() throws ApplicationServerException, RemoteException {
		String error = "";
		FBUnterkonto konto = null;
		
		if( !(error += fbUnterkontoPanel.getEditErrorString()).equalsIgnoreCase( "" ) )
			return error;
		
		konto = fbUnterkontoPanel.getEditFBUnterkonto();		// Das aktualisierte FBUnterkonto
		FBUnterkonto copy = (FBUnterkonto)treeKonten.getFBUnterkonto().clone();	// Eine Kopie vom vorhandenem Unterkonto erstellen
		copy.setFBUnterkonto( konto );		// Die Kopie aktualisieren
		if( frame.getApplicationServer().setFBUnterkonto( copy ) == konto.getId() ){	// In der Datenbank aktualisieren 
			treeKonten.getFBUnterkonto().setFBUnterkonto( konto );	// Im Baum aktualisieren
			treeKonten.refreshCurrentNode();	// Neu anzeigen
		} else {
			error += " - Ausnahmefehler ist aufgetreten !\n";
		}
		
		return error;
	}
	
	/**
	 * Ein FBHauptkonto in der Datenbank und im Baum löschen.
	 * @throws ApplicationServerException und RemoteException.
	 */
	String delFBHauptkonto() throws ApplicationServerException, RemoteException {
		String error = "";
		if( frame.getApplicationServer().delFBHauptkonto( treeKonten.getFBHauptkonto() ) > 0 ) {
			treeKonten.getInstitut().getHauptkonten().remove( treeKonten.getFBHauptkonto() );
			treeKonten.delNode();
		} else {
			error += " - Ausnahmefehler ist aufgetreten !\n";
		}
		
		return error;
	}
	
	/**
	 * Ein FBUnterkonto in der Datenbank und im Baum aktualisieren. 
	 * @throws ApplicationServerException und RemoteException.
	 */
	String delFBUnterkonto() throws ApplicationServerException, RemoteException {
		String error = "";
		if( frame.getApplicationServer().delFBUnterkonto( treeKonten.getFBUnterkonto() )  > 0 ) {
			treeKonten.getFBHauptkonto().getUnterkonten().remove( treeKonten.getFBUnterkonto() );
			treeKonten.delNode();
		} else {
			error += " - Ausnahmefehler ist aufgetreten !\n";
		}
			
		return error;
	}
	
	/**
	 * Erzeugen einer Nachricht wenn man auf den "Anlegen"-Button gedrückt hat.
	 * @return Die erzeugte Nachricht
	 */
	String getAddMessage() {
		if( treeKonten.institutIsSelected() )
			return "Wollen Sie das FBHauptkonto\n" + fbHauptkontoPanel.getNewFBHauptkonto().toString() + "\nwirklich anlegen ?";
		else if( treeKonten.fbHauptkontoIsSelected() )
			return "Wollen Sie das FBUnterkonto\n" + fbHauptkontoPanel.getNewFBUnterkonto().toString() + "\nwirklich anlegen ?";
		else
			return "";
	}
	
	/**
	 * Erzeugen einer Nachricht wenn man auf den "Löschen"-Button gedrückt hat.
	 * @return Die erzeugte Nachricht
	 */
	String getDelMessage() {
		if( treeKonten.fbHauptkontoIsSelected() )
			return "Wollen Sie das FBHauptkonto\n" + treeKonten.getFBHauptkonto().toString() +
					"\nmit allen Unterkonten wirklich löschen ?";
		else if( treeKonten.fbUnterkontoIsSelected() )
			return "Wollen Sie das FBUnterkonto\n" + treeKonten.getFBUnterkonto().toString() + "\nwirklich löschen ?";
		else
			return "";
	}
	
	/**
	 * Erzeugen einer Nachricht wenn man auf den "Ändern"-Button gedrückt hat.
	 * @return Die erzeugte Nachricht
	 */
	String getEditMessage() {
		if( treeKonten.fbHauptkontoIsSelected() )
			return "Wollen Sie das FBHauptkonto\n" + treeKonten.getFBHauptkonto().toString() +
					"\nmit allen Unterkonten wirklich ändern ?";
		else if( treeKonten.fbUnterkontoIsSelected() )
			return "Wollen Sie das FBUnterkonto\n" + treeKonten.getFBUnterkonto().toString() + "\nwirklich ändern ?";
		else
			return "";
	}
	
	/**
	 * Reaktion auf die Button-Ereignisse.
	 */
	public void actionPerformed(ActionEvent e) {
		String error = "";

		if ( e.getSource() == buAnlegen ) {			// Button "Anlegen" wurde gedrückt
			if( frame == null || treeKonten.rootIsSelected() )
				return;
			if( treeKonten.fbUnterkontoIsSelected() ){
				error += " - Ein FBUnterkonto kann keine weiteren Konten enthalten.\n";
			} else {
				if( JOptionPane.showConfirmDialog( this, getAddMessage(), "Anlegen ?", JOptionPane.YES_NO_OPTION,
																	JOptionPane.QUESTION_MESSAGE  ) == JOptionPane.YES_OPTION ){
					try{
						if( treeKonten.institutIsSelected() )
							error += addFBHauptkonto();
						else if( treeKonten.fbHauptkontoIsSelected() )
							error += addFBUnterkonto();
					} catch( ApplicationServerException exception ) {
						error += " - " + exception.toString();
					} catch(RemoteException re) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", re.getMessage(), 
																"Fehler bei RMI-Kommunikation", MessageDialogs.ERROR_ICON);
					}
				}
			}
		} else if ( e.getSource() == buLoeschen ) {		// Button "Löschen" wurde gedrückt
			if( frame == null || treeKonten.rootIsSelected() )
				return;
			if( treeKonten.institutIsSelected() ){
				error += " - Ein Institut kann nicht gelöscht werden.\n";
			} else {
				if( JOptionPane.showConfirmDialog( this, getDelMessage(), "Löschen ?", JOptionPane.YES_NO_OPTION,
																	JOptionPane.QUESTION_MESSAGE  ) == JOptionPane.YES_OPTION ){
					try{
						if( treeKonten.fbHauptkontoIsSelected() )
							error += delFBHauptkonto();
						else if( treeKonten.fbUnterkontoIsSelected() )
							error += delFBUnterkonto();
					} catch( ApplicationServerException exception ) {
						error += " - " + exception.toString();
					} catch(RemoteException re) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", re.getMessage(), 
																"Fehler bei RMI-Kommunikation", MessageDialogs.ERROR_ICON);
					}
				}
			}
		} else if ( e.getSource() == buAendern ) {		// Button "Ändern" wurde gedrückt
			if( frame == null || treeKonten.rootIsSelected() )
				return;
			if( treeKonten.institutIsSelected() ){
				error += " - Ein Institut kann nicht geändert werden.\n";
			} else {
				if( JOptionPane.showConfirmDialog( this, getEditMessage(), "Ändern ?", JOptionPane.YES_NO_OPTION,
																	JOptionPane.QUESTION_MESSAGE  ) == JOptionPane.YES_OPTION ){
					try{
						if( treeKonten.fbHauptkontoIsSelected() )
							error += setFBHauptkonto();
						else if( treeKonten.fbUnterkontoIsSelected() )
							error += setFBUnterkonto();
					} catch( ApplicationServerException exception ) {
						error += " - " + exception.toString();
					} catch(RemoteException re) {
						MessageDialogs.showDetailMessageDialog(this, "Fehler", re.getMessage(), 
																"Fehler bei RMI-Kommunikation", MessageDialogs.ERROR_ICON);
					}
				}
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
	 * Reaktion auf die Änderungen im Baum. 
	 */
	public void valueChanged( TreeSelectionEvent e ) {
		treeKonten.checkSelection( e );
		showPanel();
	}
}

/**
 * Panel zum Anzeigen aller Textfelder, die gebraucht werden um ein FBHauptkonto oder ein Institut anzuzeigen, <br>
 * ein FBHauptkonto, FBUnterkonto zu erstellen und ein FBHauptkonto zu aktualisieren. <br>
 * Es werden auch alle Funktion implementiert, die dazu benötigt werden.
 * @author w.flat
 */
class FBHauptkontoPanel extends JPanel implements ActionListener {

	FBHauptkonto konto; // FBHauptkonto, das übergeben wurde
	Institut institut;
	JTextField tfBezeichnung = new JTextField();
	JLabel labBezeichnung = new JLabel();
	JLabel labKostenstelle = new JLabel();
	JLabel labHauptkonto = new JLabel();
	JLabel labUnterkonto = new JLabel();
	IntTextField tfKostenstelle = new IntTextField(6);
	IntTextField tfHauptkonto = new IntTextField(2);
	IntTextField tfUnterkonto = new IntTextField(4);
	JLabel labBudget = new JLabel();
	CurrencyTextField tfBudget = new CurrencyTextField(	Integer.MIN_VALUE, Integer.MAX_VALUE );
	JLabel labDispolimit = new JLabel();
	CurrencyTextField tfDispolimit = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	JCheckBox checkPruefbedingung = new JCheckBox();
	ButtonGroup groupPruefbedingung = new ButtonGroup();
	JRadioButton rbBis = new JRadioButton("Bis", true);
	JRadioButton rbAb = new JRadioButton("Ab", false);
	CurrencyTextField tfPruefbedingung = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	JCheckBox checkKleinbestellungen = new JCheckBox();
	JLabel labVormerkungen = new JLabel();
	CurrencyTextField tfVormerkungen = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );

	/**
	 * Das FBHauptkontoPanel Erzeugen.
	 */
	public FBHauptkontoPanel() {
	  try {
		jbInit();
	  }
	  catch(Exception ex) {
		ex.printStackTrace();
	  }
	}
	
	/**
	 * Initialisieren der graphischen Oberfläche. 
	 * @throws Exception
	 */
	void jbInit() throws Exception {
	  this.setLayout(null);
	  tfBezeichnung.setToolTipText("");
	  tfBezeichnung.setToolTipText("");
	  tfBezeichnung.setText("");
	  tfBezeichnung.setBounds(new Rectangle(92, 12, 405, 21));
	  labBezeichnung.setText("Bezeichnung");
	  labBezeichnung.setBounds(new Rectangle(12, 12, 80, 15));
	  labKostenstelle.setText("Kostenstelle");
	  labKostenstelle.setBounds(new Rectangle(12, 47, 80, 15));
	  labHauptkonto.setText("Hauptkonto");
	  labHauptkonto.setBounds(new Rectangle(187, 47, 80, 15));
	  labUnterkonto.setText("Unterkonto");
	  labUnterkonto.setBounds(new Rectangle(357, 47, 80, 15));
	  tfHauptkonto.setRequestFocusEnabled(true);
	  tfHauptkonto.setText("");
	  tfHauptkonto.setBounds(new Rectangle(267, 47, 65, 21));
	  tfKostenstelle.setText("");
	  tfKostenstelle.setBounds(new Rectangle(92, 47, 65, 21));
	  tfKostenstelle.setEnabled( false );
	  tfUnterkonto.setText("");
	  tfUnterkonto.setBounds(new Rectangle(432, 47, 65, 21));
	  labBudget.setText("Budget");
	  labBudget.setBounds(new Rectangle(12, 82, 80, 15));
	  labDispolimit.setText("Dispolimit");
	  labDispolimit.setBounds(new Rectangle(257, 82, 95, 15));
	  checkPruefbedingung.setText("Prüfbedingung");
	  checkPruefbedingung.setBounds(new Rectangle(12, 152, 120, 23));
	  tfBudget.setText("");
	  tfBudget.setBounds(new Rectangle(92, 82, 140, 21));
	  tfBudget.setEnabled( false );
	  tfDispolimit.setText("");
	  tfDispolimit.setBounds(new Rectangle(357, 82, 140, 21));
	  tfPruefbedingung.setText("");
	  tfPruefbedingung.setBounds(new Rectangle(357, 152, 140, 21));
	  checkKleinbestellungen.setActionCommand("jCheckBox1");
	  checkKleinbestellungen.setText("Kleinbestellungen");
	  checkKleinbestellungen.setBounds(new Rectangle(12, 117, 140, 23));
	  labVormerkungen.setText("Vormerkungen");
	  labVormerkungen.setBounds(new Rectangle(257, 117, 95, 15));
	  tfVormerkungen.setText("");
	  tfVormerkungen.setBounds(new Rectangle(357, 117, 140, 21));
	  rbAb.setBounds(new Rectangle(242, 152, 60, 23));
	  rbBis.setBounds(new Rectangle(162, 152, 60, 23));
	  
	  this.add(labBezeichnung, null);
	  this.add(tfBezeichnung, null);
	  this.add(labKostenstelle, null);
	  this.add(labUnterkonto, null);
	  this.add(labHauptkonto, null);
	  this.add(tfKostenstelle, null);
	  this.add(tfHauptkonto, null);
	  this.add(tfUnterkonto, null);
	  this.add(labBudget, null);
	  this.add(tfBudget, null);
	  this.add(labDispolimit, null);
	  this.add(tfDispolimit, null);
	  this.add(checkPruefbedingung, null);
	  this.add(rbBis, null);
	  this.add(rbAb, null);
	  this.add(tfPruefbedingung, null);
	  this.add(checkKleinbestellungen, null);
	  this.add(labVormerkungen, null);
	  this.add(tfVormerkungen, null);
	  tfVormerkungen.setEnabled(false);
	  checkPruefbedingung.addActionListener( this );
	  rbBis.addActionListener( this );
	  rbAb.addActionListener( this );
	  groupPruefbedingung.add( rbBis );
	  groupPruefbedingung.add( rbAb );
	  this.setBorder(BorderFactory.createEtchedBorder());
	}

	/**
	 * Die Textfelder mit Daten des übergebenden Instituts fühlen. 
	 * @param institut = Institut von dem die Daten übernommen werden. 
	 */
	public void setTextfields( Institut institut ) {
		if( (this.institut = institut) == null )		// Wenn kein Institut angegeben
			return;

		tfBezeichnung.setText( institut.getBezeichnung() );
		tfKostenstelle.setValue( institut.getKostenstelle() );
		tfHauptkonto.setValue( "" );
		tfUnterkonto.setValue( "" );
		tfBudget.setValue( new Float( 0 ) );
		tfDispolimit.setValue( new Float( 0 ) );
		checkPruefbedingung.setSelected( false );
		rbBis.setSelected( true );
		rbAb.setSelected( false );
		tfPruefbedingung.setValue( new Float( 0 ) );
		tfVormerkungen.setValue( new Float( 0 ) );
		checkKleinbestellungen.setSelected( false );
		
		actionPerformed( new ActionEvent( checkPruefbedingung, 0, "" ) );
	}
	
	/**
	 * Die Textfelder mit Daten des übergebenden FBHauptkontos fühlen. 
	 * @param konto = FBHauptkonto von dem die Daten übernommen werden. 
	 */
	public void setTextfields( FBHauptkonto konto ) {
		if( (this.konto = konto) == null )		// Wenn kein FBHauptkonto angegeben
			return;

		tfBezeichnung.setText( konto.getBezeichnung() );
		tfKostenstelle.setValue( konto.getInstitut().getKostenstelle() );
		tfHauptkonto.setValue( konto.getHauptkonto() );
		tfUnterkonto.setValue( konto.getUnterkonto() );
		tfBudget.setValue( new Float( konto.getBudget() ) );
		tfDispolimit.setValue( new Float( konto.getDispoLimit() ) );
		checkPruefbedingung.setSelected( konto.isPruefungActive() );
		rbBis.setSelected( konto.isPruefungBis() );
		rbAb.setSelected( !konto.isPruefungBis() );
		tfPruefbedingung.setValue( new Float( konto.getPruefsumme() ) );
		tfVormerkungen.setValue( new Float( konto.getVormerkungen() ) );
		checkKleinbestellungen.setSelected(konto.getKleinbestellungen());
		actionPerformed( new ActionEvent( checkPruefbedingung, 0, "" ) );
	}
	
	/**
	 * Ermitlung der Fehler die beim Erstellen eines neuen FBUnterkontos aufgetreten sind. 
	 * @return String mit Fehlern. Wenn leerer String, dann keine Fehler aufgetreten. 
	 */
	public String getAddErrorString() {
		String error = "";
		if( tfBezeichnung.getText().equalsIgnoreCase( "" ) )
			error += " - Die Bezeichnung darf nicht leer sein.\n";
		if( !tfHauptkonto.getText().equalsIgnoreCase( konto.getHauptkonto() ) )
			error += " - Das Hauptkonto darf nicht verändert werden.\n";
		if( tfUnterkonto.getText().equalsIgnoreCase( "0000" ) )
			error += " - Das Unterkonto darf nicht '0000' sein.\n";
		
		return error;
	}
	
	/**
	 * Ermittlung der Fehler die beim Aktualisieren eines FBHauptkontos aufgetreten sind.
	 * @return String mit Fehlern. Wenn leerer String, dann keine Fehler aufgetreten. 
	 */
	public String getEditErrorString() {
		String error = "";
		if( tfBezeichnung.getText().equalsIgnoreCase( "" ) )
			error += " - Die Bezeichnung darf nicht leer sein.\n";
		if( !tfUnterkonto.getText().equalsIgnoreCase( "0000" ) )
			error += " - Das Unterkonto muß '0000' sein.\n";
		
		return error;
	}
	
	/**
	 * Erstellen eines aktualisierten FBHauptkontos. 
	 * @return FBHauptkonto, welches aus den Daten des Panels erstellt wurde. 
	 */
	public FBHauptkonto getEditFBHauptkonto(){
		if( konto == null )
			return null;
		
		return new FBHauptkonto( konto.getId(), konto.getHaushaltsJahrID(), konto.getInstitut(), tfBezeichnung.getText(),
					tfHauptkonto.getText(), tfUnterkonto.getText(), konto.getBudget(), 
					Float.parseFloat( tfDispolimit.getValue().toString() ), konto.getVormerkungen(),
					FBHauptkonto.getPruefung( checkPruefbedingung.isSelected(), rbBis.isSelected(), tfPruefbedingung.getValue() ),
					checkKleinbestellungen.isSelected(), konto.getGeloescht() );
	}
	
	/**
	 * Erstellen eines neuen FBHauptkontos zu einem Institut.
	 * @return FBHauptkonto, welches aus den Daten des Panels erstellt wurde. 
	 */
	public FBHauptkonto getNewFBHauptkonto(){
		if( institut == null )
			return null;
		
		return new FBHauptkonto( 0, 0, institut, tfBezeichnung.getText(), tfHauptkonto.getText(), tfUnterkonto.getText(),
					0, Float.parseFloat( tfDispolimit.getValue().toString() ), 0, 
					FBHauptkonto.getPruefung( checkPruefbedingung.isSelected(), rbBis.isSelected(), tfPruefbedingung.getValue() ),
					checkKleinbestellungen.isSelected(), false );
	}
	
	/**
	 * Erstellen eines neuen FBUnterkontos zu einem FBHauptkonto.
	 * @return FBUnterkonto, welches aus den Daten des Panels erstellt wurde. 
	 */
	public FBUnterkonto getNewFBUnterkonto(){
		if( konto == null )
			return null;
		
		return new FBUnterkonto( 0, konto.getHaushaltsJahrID(), konto.getInstitut(), tfBezeichnung.getText(),
								konto.getHauptkonto(), tfUnterkonto.getText(), 0, 0, checkKleinbestellungen.isSelected(), false );
	}

	/**
	 * Hier erfolgt die Reaktion wenn eines der RadioButtons oder CheckBox gedrückt wurde. 
	 */
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == checkPruefbedingung ) {
			if( checkPruefbedingung.isSelected() ) {
				rbBis.setVisible( true );
				rbAb.setVisible( true );
				tfPruefbedingung.setVisible( true );
			} else {
				rbBis.setVisible( false );
				rbAb.setVisible( false );
				tfPruefbedingung.setVisible( false );
			}
		} else if( e.getSource() == rbBis ) {
		} else if( e.getSource() == rbAb ) {
		}
	}
}

/**
 * Panel zum Anzeigen aller Textfelder, die gebraucht werden <br>
 * wenn ein FBUnterkonto anzuzeigen. Es werden auch alle <br>
 * Funktion implementiert, die dazu benötigt werden. 
 * @author w.flat
 */
class FBUnterkontoPanel extends JPanel {

	FBUnterkonto konto; // Das FBUnterkonto, das übergeben wurde
	JTextField tfBezeichnung = new JTextField();
	JLabel labBezeichnung = new JLabel();
	JLabel labKostenstelle = new JLabel();
	JLabel labHauptkonto = new JLabel();
	JLabel labUnterkonto = new JLabel();
	IntTextField tfKostenstelle = new IntTextField(6);
	IntTextField tfHauptkonto = new IntTextField(2);
	IntTextField tfUnterkonto = new IntTextField(4);
	JLabel labBudget = new JLabel();
	CurrencyTextField tfBudget = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	JLabel labVormerkungen = new JLabel();
	CurrencyTextField tfVormerkungen = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	JCheckBox checkKleinbestellungen = new JCheckBox();
	
	/**
	 * Erzeugen des FBUnterkontoPanels.
	 */
	public FBUnterkontoPanel() {
	  try {
		jbInit();
	  }
	  catch(Exception ex) {
		ex.printStackTrace();
	  }
	}
	
	/**
	 * Initialisierung der graphischen Oberfläche. 
	 * @throws Exception
	 */
	void jbInit() throws Exception {
	  this.setLayout(null);
	  tfBezeichnung.setToolTipText("");
	  tfBezeichnung.setText("");
	  tfBezeichnung.setBounds(new Rectangle(92, 12, 405, 21));
	  labBezeichnung.setText("Bezeichnung");
	  labBezeichnung.setBounds(new Rectangle(12, 12, 80, 15));
	  labKostenstelle.setText("Kostenstelle");
	  labKostenstelle.setBounds(new Rectangle(12, 47, 80, 15));
	  labHauptkonto.setText("Hauptkonto");
	  labHauptkonto.setBounds(new Rectangle(187, 47, 80, 15));
	  labUnterkonto.setText("Unterkonto");
	  labUnterkonto.setBounds(new Rectangle(352, 47, 80, 15));
	  tfHauptkonto.setRequestFocusEnabled(true);
	  tfHauptkonto.setText("");
	  tfHauptkonto.setBounds(new Rectangle(267, 47, 65, 21));
	  tfHauptkonto.setEnabled( false );
	  tfKostenstelle.setText("");
	  tfKostenstelle.setBounds(new Rectangle(92, 47, 65, 21));
	  tfKostenstelle.setEnabled( false );
	  tfUnterkonto.setText("");
	  tfUnterkonto.setBounds(new Rectangle(432, 47, 65, 21));
	  labBudget.setText("Budget");
	  labBudget.setBounds(new Rectangle(12, 82, 80, 15));
	  tfBudget.setText("");
	  tfBudget.setBounds(new Rectangle(92, 82, 140, 21));
	  tfBudget.setEnabled( false );
	  labVormerkungen.setText("Vormerkungen");
	  labVormerkungen.setBounds(new Rectangle(257, 82, 95, 15));
	  tfVormerkungen.setText("");
	  tfVormerkungen.setBounds(new Rectangle(352, 82, 145, 21));
	  checkKleinbestellungen.setText("Kleinbestellungen");
	  checkKleinbestellungen.setBounds(new Rectangle(12, 117, 140, 23));
	  this.add(labBezeichnung, null);
	  this.add(tfBezeichnung, null);
	  this.add(labKostenstelle, null);
	  this.add(labUnterkonto, null);
	  this.add(labHauptkonto, null);
	  this.add(tfKostenstelle, null);
	  this.add(tfHauptkonto, null);
	  this.add(tfUnterkonto, null);
	  this.add(labBudget, null);
	  this.add(tfBudget, null);
	  this.add(labVormerkungen, null);
	  this.add(tfVormerkungen, null);
	  tfVormerkungen.setEnabled(false);
	  this.add(checkKleinbestellungen, null);
	  this.setBorder(BorderFactory.createEtchedBorder());
	}
	
	/**
	 * Die Textfelder mit Daten des übergebenden FBUnterkontos fühlen. 
	 * @param konto = FBUnterkonto von dem die Daten übernommen werden. 
	 */
	public void setTextfields( FBUnterkonto konto ) {
		if( (this.konto = konto) == null )		// Wenn kein FBUnterkonto angegeben
			return;
		
		tfBezeichnung.setText( konto.getBezeichnung() );
		tfKostenstelle.setValue( konto.getInstitut().getKostenstelle() );
		tfHauptkonto.setValue( konto.getHauptkonto() );
		tfUnterkonto.setValue( konto.getUnterkonto() );
		tfBudget.setValue( new Float( konto.getBudget() ) );
		tfVormerkungen.setValue( new Float( konto.getVormerkungen() ) );
		checkKleinbestellungen.setSelected(konto.getKleinbestellungen());
	}
	
	/**
	 * Ermittlung der Fehler die beim Aktualisieren eines FBUnterkontos aufgetreten sind
	 * @return String mit Fehlern. Wenn leerer String, dann keine Fehler aufgetreten. 
	 */
	public String getEditErrorString() {
		String error = "";
		if( tfBezeichnung.getText().equalsIgnoreCase( "" ) )
			error += " - Die Bezeichnung darf nicht leer sein.\n";
		if( tfUnterkonto.getText().equalsIgnoreCase( "0000" ) )
			error += " - Das Unterkonto darf nicht '0000' sein.\n";
		
		return error;
	}
	
	/**
	 * Erstellen eines aktualisierten FBUnterkontos.
	 * @return FBUnterkonto welches aus den Daten des Panels erstellt wurde. 
	 */
	public FBUnterkonto getEditFBUnterkonto(){
		if( konto == null )
			return null;
		
		return new FBUnterkonto( konto.getId(), konto.getHaushaltsJahrID(), konto.getInstitut(), tfBezeichnung.getText(),
									konto.getHauptkonto(), tfUnterkonto.getText(), konto.getBudget(), 
									konto.getVormerkungen(), checkKleinbestellungen.isSelected(), konto.getGeloescht() );
	}
}

