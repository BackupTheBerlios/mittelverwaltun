package gui;

import javax.swing.*;
import javax.swing.event.*;

import applicationServer.*;
import dbObjects.*;
import java.awt.*;
import java.awt.event.*;


/**
 * 
 */
public class FBKontenverwaltung extends JInternalFrame implements ActionListener, TreeSelectionListener {

	JScrollPane scrollKonten = new JScrollPane();
	FBKontenTree treeKonten;
	FBHauptkontoPanel fbHauptkontoPanel = new FBHauptkontoPanel();
	FBUnterkontoPanel fbUnterkontoPanel = new FBUnterkontoPanel();
	JLabel labNoPanel = new JLabel("Keine Vorschau verf�gbar !");
	JButton buAnlegen = new JButton("Anlegen");
	JButton buAendern = new JButton("�ndern");
	JButton buLoeschen = new JButton("L�schen");
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
	  test.show();
	  child.show();
	
	}
		

	public FBKontenverwaltung( MainFrame frame ) {
		super( "FB-Kontenverwaltung" );
		this.setClosable( true );
		this.setIconifiable( true);
		this.getContentPane().setLayout( null );
		this.frame = frame;
		
		scrollKonten.getViewport().add( treeKonten = new FBKontenTree( this, "FBKonten" ), null );
		scrollKonten.setBounds(new Rectangle(10, 10, 280, 200));
		this.getContentPane().add(scrollKonten);
		fbHauptkontoPanel.setBounds(new Rectangle(300, 10, 500, 200));
		this.getContentPane().add(fbHauptkontoPanel);
		fbUnterkontoPanel.setBounds(new Rectangle(300, 10, 500, 200));
		this.getContentPane().add(fbUnterkontoPanel);
		labNoPanel.setBounds(new Rectangle(300, 10, 500, 16));
		this.getContentPane().add(labNoPanel);

		buAktualisieren.setBounds(new Rectangle(10, 220, 150, 25));
		buAktualisieren.addActionListener( this );
		this.getContentPane().add(buAktualisieren);
		buAktualisieren.setIcon(Functions.getRefreshIcon(getClass()));
		buAnlegen.setBounds(new Rectangle(170, 220, 150, 25));
		buAnlegen.addActionListener( this );
		this.getContentPane().add(buAnlegen);
		buAnlegen.setIcon(Functions.getAddIcon(getClass()));
		buAendern.setBounds(new Rectangle(330, 220, 150, 25));
		buAendern.addActionListener( this );
		this.getContentPane().add(buAendern);
		buAendern.setIcon(Functions.getEditIcon(getClass()));
		buLoeschen.setBounds(new Rectangle(490, 220, 150, 25));
		buLoeschen.addActionListener( this );
		this.getContentPane().add(buLoeschen);
		buLoeschen.setIcon(Functions.getDelIcon(getClass()));
		buBeenden.setBounds(new Rectangle(650, 220, 150, 25));
		buBeenden.addActionListener( this );
		this.getContentPane().add(buBeenden);
		buBeenden.setIcon(Functions.getCloseIcon(getClass()));

		loadKonten();
		showPanel();
			
		this.setSize( 818, 288 );
	}
	
	/**
	 * Laden der Konten aus der Datenbank in den Baum
	 */
	void loadKonten() {
		if( frame != null ) {
			try {
				treeKonten.delTree();
				treeKonten.loadInstituts( frame.getApplicationServer().getInstitutesWithAccounts() );
			} catch (ApplicationServerException e) {
				System.out.println( e.toString() );
			}
		}
	}
	
	/**
	 * Auswahl des richtigen Panels, das angezeigt wird
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
	 * Ein neues FBHauptkonto in der Datenbank und im Baum erstellen. Wirft ApplicationServerException.
	 * R�ckgabe ist ein String mit Fehlern. Wenn der String leer ist, dann sind keine Fehler aufgetreten.
	 */
	String addFBHauptkonto() throws ApplicationServerException {
		String error = "";
		FBHauptkonto konto = null;
		
		if( !(error += fbHauptkontoPanel.getEditErrorString()).equalsIgnoreCase( "" ) )
			return error;
			
		konto = fbHauptkontoPanel.getNewFBHauptkonto();		// Das neue FNHauptkonto
		konto.setHaushaltsJahrID( frame.getApplicationServer().getCurrentHaushaltsjahrId() );	// Die aktuelle HaushaltsjahrId
		int id = frame.getApplicationServer().addFBHauptkonto( konto );	// In die Datenbank einf�gen 
		if( id > 0 ) {		// Wenn es eingef�gt wurde
			konto.setId( id );	// Die id in das Konto einf�gen
			treeKonten.getInstitut().getHauptkonten().add( konto );	// Das FBHauptkonto dem Institut zuf�gen
			treeKonten.addNewNode( konto );		// In den Baum einf�gen
		} else {
			error += " - Ausnahmefehler ist aufgetreten !\n";
		}
		
		return error;
	}

	/**
	 * Ein neues FBUnterkonto in der Datenbank und im Baum erstellen. Wirft ApplicationServerException.
	 * R�ckgabe ist ein String mit Fehlern. Wenn der String leer ist, dann sind keine Fehler aufgetreten.
	 */
	String addFBUnterkonto() throws ApplicationServerException {
		String error = "";
		FBUnterkonto konto = null;
		
		if( !(error += fbHauptkontoPanel.getAddErrorString()).equalsIgnoreCase( "" ) )
			return error;
			
		konto = fbHauptkontoPanel.getNewFBUnterkonto();		// Das neue FBUnterkonto
		int id = frame.getApplicationServer().addFBUnterkonto( konto );	// Das FBUnterkonto in die Datenbank einf�gen
		if( id > 0 ) {		// Wurde eingef�gt
			konto.setId( id );		// Die id einf�gen
			// Das Konto zu den Unterkonten des FBHauptkontos hinzuf�gen
			treeKonten.getFBHauptkonto().getUnterkonten().add( konto );
			treeKonten.addNewNode( konto );		// Das neue FBUnterkonto in den Baum einf�gen
		} else {
			error += " - Ausnahmefehler ist aufgetreten !\n";
		}
		
		return error;
	}
	
	/**
	 * Ein FBHauptkonto in der Datenbank und im Baum aktualisieren. Wirft ApplicationServerException.
	 * R�ckgabe ist ein String mit Fehlern. Wenn der String leer ist, dann sind keine Fehler aufgetreten.
	 */
	String setFBHauptkonto() throws ApplicationServerException {
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
	 * Ein FBUnterkonto in der Datenbank und im Baum aktualisieren. Wirft ApplicationServerException.
	 * R�ckgabe ist ein String mit Fehlern. Wenn der String leer ist, dann sind keine Fehler aufgetreten.
	 */
	String setFBUnterkonto() throws ApplicationServerException {
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
	 * Ein FBHauptkonto in der Datenbank und im Baum l�schen. Wirft ApplicationServerException.
	 */
	String delFBHauptkonto() throws ApplicationServerException {
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
	 * Ein FBUnterkonto in der Datenbank und im Baum aktualisieren. Wirft ApplicationServerException.
	 */
	String delFBUnterkonto() throws ApplicationServerException {
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
	 * Erzeugen einer Nachricht wenn man auf den "Anlegen"-Button gedr�ckt hat.
	 * @return die erzeugte Nachricht
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
	 * Erzeugen einer Nachricht wenn man auf den "L�schen"-Button gedr�ckt hat.
	 * @return die erzeugte Nachricht
	 */
	String getDelMessage() {
		if( treeKonten.fbHauptkontoIsSelected() )
			return "Wollen Sie das FBHauptkonto\n" + treeKonten.getFBHauptkonto().toString() +
					"\nmit allen Unterkonten wirklich l�schen ?";
		else if( treeKonten.fbUnterkontoIsSelected() )
			return "Wollen Sie das FBUnterkonto\n" + treeKonten.getFBUnterkonto().toString() + "\nwirklich l�schen ?";
		else
			return "";
	}
	
	/**
	 * Erzeugen einer Nachricht wenn man auf den "�ndern"-Button gedr�ckt hat.
	 * @return die erzeugte Nachricht
	 */
	String getEditMessage() {
		if( treeKonten.fbHauptkontoIsSelected() )
			return "Wollen Sie das FBHauptkonto\n" + treeKonten.getFBHauptkonto().toString() +
					"\nmit allen Unterkonten wirklich �ndern ?";
		else if( treeKonten.fbUnterkontoIsSelected() )
			return "Wollen Sie das FBUnterkonto\n" + treeKonten.getFBUnterkonto().toString() + "\nwirklich �ndern ?";
		else
			return "";
	}
	
	/**
	 * Reaktion auf die Kn�pfe
	 */
	public void actionPerformed(ActionEvent e) {
		String error = "";

		if ( e.getSource() == buAnlegen ) {			// Button "Anlegen" wurde gedr�ckt
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
					}
				}
			}
		} else if ( e.getSource() == buLoeschen ) {		// Button "L�schen" wurde gedr�ckt
			if( frame == null || treeKonten.rootIsSelected() )
				return;
			if( treeKonten.institutIsSelected() ){
				error += " - Ein Institut kann nicht gel�scht werden.\n";
			} else {
				if( JOptionPane.showConfirmDialog( this, getDelMessage(), "L�schen ?", JOptionPane.YES_NO_OPTION,
																	JOptionPane.QUESTION_MESSAGE  ) == JOptionPane.YES_OPTION ){
					try{
						if( treeKonten.fbHauptkontoIsSelected() )
							error += delFBHauptkonto();
						else if( treeKonten.fbUnterkontoIsSelected() )
							error += delFBUnterkonto();
					} catch( ApplicationServerException exception ) {
						error += " - " + exception.toString();
					}
				}
			}
		} else if ( e.getSource() == buAendern ) {		// Button "�ndern" wurde gedr�ckt
			if( frame == null || treeKonten.rootIsSelected() )
				return;
			if( treeKonten.institutIsSelected() ){
				error += " - Ein Institut kann nicht ge�ndert werden.\n";
			} else {
				if( JOptionPane.showConfirmDialog( this, getEditMessage(), "�ndern ?", JOptionPane.YES_NO_OPTION,
																	JOptionPane.QUESTION_MESSAGE  ) == JOptionPane.YES_OPTION ){
					try{
						if( treeKonten.fbHauptkontoIsSelected() )
							error += setFBHauptkonto();
						else if( treeKonten.fbUnterkontoIsSelected() )
							error += setFBUnterkonto();
					} catch( ApplicationServerException exception ) {
						error += " - " + exception.toString();
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
	
	public void valueChanged( TreeSelectionEvent e ) {
		treeKonten.checkSelection( e );
		showPanel();
	}
}

/**
 * Panel zum Anzeigen aller Textfelder, die gebraucht werden um ein FBHauptkonto oder ein Institut anzuzeigen,
 * ein FBHauptkonto, FBUnterkonto zu erstellen und ein FBHauptkonto zu aktualisieren.
 * Es werden auch alle Funktion implementiert, die dazu ben�tigt werden.
 */
class FBHauptkontoPanel extends JPanel implements ActionListener {

	FBHauptkonto konto; // FBHauptkonto, das �bergeben wurde
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

	public FBHauptkontoPanel() {
	  try {
		jbInit();
	  }
	  catch(Exception ex) {
		ex.printStackTrace();
	  }
	}
	void jbInit() throws Exception {
	  this.setLayout(null);
	  tfBezeichnung.setToolTipText("");
	  tfBezeichnung.setText("");
	  tfBezeichnung.setBounds(new Rectangle(90, 10, 405, 21));
	  labBezeichnung.setText("Bezeichnung");
	  labBezeichnung.setBounds(new Rectangle(10, 10, 80, 15));
	  labKostenstelle.setText("Kostenstelle");
	  labKostenstelle.setBounds(new Rectangle(10, 45, 80, 15));
	  labHauptkonto.setText("Hauptkonto");
	  labHauptkonto.setBounds(new Rectangle(185, 45, 80, 15));
	  labUnterkonto.setText("Unterkonto");
	  labUnterkonto.setBounds(new Rectangle(350, 45, 80, 15));
	  tfHauptkonto.setRequestFocusEnabled(true);
	  tfHauptkonto.setText("");
	  tfHauptkonto.setBounds(new Rectangle(265, 45, 65, 21));
	  tfKostenstelle.setText("");
	  tfKostenstelle.setEnabled( false );
	  tfKostenstelle.setBounds(new Rectangle(90, 45, 65, 21));
	  tfUnterkonto.setText("");
	  tfUnterkonto.setBounds(new Rectangle(430, 45, 65, 21));
	  labBudget.setText("Budget");
	  labBudget.setBounds(new Rectangle(10, 80, 80, 15));
	  labDispolimit.setText("Dispolimit");
	  labDispolimit.setBounds(new Rectangle(275, 80, 80, 15));
	  checkPruefbedingung.setText("Pr�fbedingung");
	  checkPruefbedingung.setBounds(new Rectangle(10, 120, 120, 23));
	  rbBis.setBounds(new Rectangle(160, 120, 60, 23));
	  rbAb.setBounds(new Rectangle(240, 120, 60, 23));
	  tfBudget.setText("");
	  tfBudget.setEnabled( false );
	  tfBudget.setBounds(new Rectangle(90, 80, 140, 21));
	  tfDispolimit.setText("");
	  tfDispolimit.setBounds(new Rectangle(355, 80, 140, 21));
	  tfPruefbedingung.setText("");
	  tfPruefbedingung.setBounds(new Rectangle(355, 120, 140, 21));
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
	  checkPruefbedingung.addActionListener( this );
	  rbBis.addActionListener( this );
	  rbAb.addActionListener( this );
	  groupPruefbedingung.add( rbBis );
	  groupPruefbedingung.add( rbAb );
	  this.setBorder(BorderFactory.createEtchedBorder());
	}

	/**
	 * Die Textfelder mit Daten des �bergebenden Instituts f�hlen
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
		
		actionPerformed( new ActionEvent( checkPruefbedingung, 0, "" ) );
	}
	
	/**
	 * Die Textfelder mit Daten des �bergebenden FBHauptkontos f�hlen
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
		
		actionPerformed( new ActionEvent( checkPruefbedingung, 0, "" ) );
	}
	
	/**
	 * Ermitlung der Fehler die beim Erstellen eines neuen FBUnterkontos aufgetreten sind
	 */
	public String getAddErrorString() {
		String error = "";
		if( tfBezeichnung.getText().equalsIgnoreCase( "" ) )
			error += " - Die Bezeichnung darf nicht leer sein.\n";
		if( !tfHauptkonto.getText().equalsIgnoreCase( konto.getHauptkonto() ) )
			error += " - Das Hauptkonto darf nicht ver�ndert werden.\n";
		if( tfUnterkonto.getText().equalsIgnoreCase( "0000" ) )
			error += " - Das Unterkonto darf nicht '0000' sein.\n";
		
		return error;
	}
	
	/**
	 * Ermittlung der Fehler die beim Aktualisieren eines FBHauptkontos aufgetreten sind
	 */
	public String getEditErrorString() {
		String error = "";
		if( tfBezeichnung.getText().equalsIgnoreCase( "" ) )
			error += " - Die Bezeichnung darf nicht leer sein.\n";
		if( !tfUnterkonto.getText().equalsIgnoreCase( "0000" ) )
			error += " - Das Unterkonto mu� '0000' sein.\n";
		
		return error;
	}
	
	/**
	 * Erstellen eines aktualisierten FBHauptkontos
	 */
	public FBHauptkonto getEditFBHauptkonto(){
		if( konto == null )
			return null;
		
		return new FBHauptkonto( konto.getId(), konto.getHaushaltsJahrID(), konto.getInstitut(), tfBezeichnung.getText(),
					tfHauptkonto.getText(), tfUnterkonto.getText(), konto.getBudget(),
					Float.parseFloat( tfDispolimit.getValue().toString() ),
					FBHauptkonto.getPruefung( checkPruefbedingung.isSelected(), rbBis.isSelected(), tfPruefbedingung.getValue() ) );
	}
	
	/**
	 * Erstellen eines neuen FBHauptkontos zu einem Institut
	 */
	public FBHauptkonto getNewFBHauptkonto(){
		if( institut == null )
			return null;
		
		return new FBHauptkonto( 0, 0, institut, tfBezeichnung.getText(), tfHauptkonto.getText(), tfUnterkonto.getText(),
					0, Float.parseFloat( tfDispolimit.getValue().toString() ),
					FBHauptkonto.getPruefung( checkPruefbedingung.isSelected(), rbBis.isSelected(), tfPruefbedingung.getValue() ) );
	}
	
	/**
	 * Erstellen eines neuen FBUnterkontos zu einem FBHauptkonto
	 */
	public FBUnterkonto getNewFBUnterkonto(){
		if( konto == null )
			return null;
		
		return new FBUnterkonto( 0, konto.getHaushaltsJahrID(), konto.getInstitut(), tfBezeichnung.getText(),
								konto.getHauptkonto(), tfUnterkonto.getText(), 0 );
	}

	/**
	 * Hier erfolgt die Reaktion wenn eines der RadioButtons oder CheckBox gedr�ckt wurde
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
 * Panel zum Anzeigen aller Textfelder, die gebraucht werden
 * wenn ein FBUnterkonto anzuzeigen. Es werden auch alle
 * Funktion implementiert, die dazu ben�tigt werden.
 */
class FBUnterkontoPanel extends JPanel {

	FBUnterkonto konto; // Das FBUnterkonto, das �bergeben wurde
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

	
	public FBUnterkontoPanel() {
	  try {
		jbInit();
	  }
	  catch(Exception ex) {
		ex.printStackTrace();
	  }
	}
	void jbInit() throws Exception {
	  this.setLayout(null);
	  tfBezeichnung.setToolTipText("");
	  tfBezeichnung.setText("");
	  tfBezeichnung.setBounds(new Rectangle(90, 10, 405, 21));
	  labBezeichnung.setText("Bezeichnung");
	  labBezeichnung.setBounds(new Rectangle(10, 10, 80, 15));
	  labKostenstelle.setText("Kostenstelle");
	  labKostenstelle.setBounds(new Rectangle(10, 45, 80, 15));
	  labHauptkonto.setText("Hauptkonto");
	  labHauptkonto.setBounds(new Rectangle(185, 45, 80, 15));
	  labUnterkonto.setText("Unterkonto");
	  labUnterkonto.setBounds(new Rectangle(350, 45, 80, 15));
	  tfHauptkonto.setRequestFocusEnabled(true);
	  tfHauptkonto.setText("");
	  tfHauptkonto.setEnabled( false );
	  tfHauptkonto.setBounds(new Rectangle(265, 45, 65, 21));
	  tfKostenstelle.setText("");
	  tfKostenstelle.setEnabled( false );
	  tfKostenstelle.setBounds(new Rectangle(90, 45, 65, 21));
	  tfUnterkonto.setText("");
	  tfUnterkonto.setBounds(new Rectangle(430, 45, 65, 21));
	  labBudget.setText("Budget");
	  labBudget.setBounds(new Rectangle(10, 80, 80, 15));
	  tfBudget.setText("");
	  tfBudget.setEnabled( false );
	  tfBudget.setBounds(new Rectangle(90, 80, 140, 21));
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
	  this.setBorder(BorderFactory.createEtchedBorder());
	}
	
	/**
	 * Die Textfelder mit Daten des �bergebenden FBUnterkontos f�hlen
	 */
	public void setTextfields( FBUnterkonto konto ) {
		if( (this.konto = konto) == null )		// Wenn kein FBUnterkonto angegeben
			return;
		
		tfBezeichnung.setText( konto.getBezeichnung() );
		tfKostenstelle.setValue( konto.getInstitut().getKostenstelle() );
		tfHauptkonto.setValue( konto.getHauptkonto() );
		tfUnterkonto.setValue( konto.getUnterkonto() );
		tfBudget.setValue( new Float( konto.getBudget() ) );
	}
	
	/**
	 * Ermittlung der Fehler die beim Aktualisieren eines FBUnterkontos aufgetreten sind
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
	 * Erstellen eines aktualisierten FBUnterkontos
	 */
	public FBUnterkonto getEditFBUnterkonto(){
		if( konto == null )
			return null;
		
		return new FBUnterkonto( konto.getId(), konto.getHaushaltsJahrID(), konto.getInstitut(), tfBezeichnung.getText(),
								konto.getHauptkonto(), tfUnterkonto.getText(), konto.getBudget() );
	}
}

