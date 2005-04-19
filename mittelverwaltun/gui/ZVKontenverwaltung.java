package gui;

import javax.swing.*;
import javax.swing.event.*;

import applicationServer.*;
import dbObjects.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;

/**
 * Frame zum Verwalten der ZVKonten, ZVTitel und ZVUntertitel.<br>
 * Es werden folgende Funktionen bereigestellt: <br>
 * 1. Erzeugen vom neuen ZVKonten, ZVtiteln und ZVUntertiteln. <br>
 * 2. Aktualisieren vom vorhandenen ZVKonten, ZVtiteln und ZVUntertiteln. <br>
 * 3. Löschen vom vorhandenen ZVKonten, ZVtiteln und ZVUntertiteln.
 * @author w.flat
 */
public class ZVKontenverwaltung extends JInternalFrame implements ActionListener, TreeSelectionListener {

	JScrollPane scrollZVKonten = new JScrollPane();
	ZVKontenTree treeKonten;
	RootPanel rootPanel = new RootPanel();
	TGRZVKontoPanel tgrKontoPanel = new TGRZVKontoPanel();
	TitelZVKontoPanel titelKontoPanel = new TitelZVKontoPanel();
	TGRZVKontoTitelPanel tgrKontoTitelPanel = new TGRZVKontoTitelPanel();
	TitelZVKontoTitelPanel titelKontoTitelPanel = new TitelZVKontoTitelPanel();
	TGRZVKontoUntertitelPanel tgrKontoUntertitelPanel = new TGRZVKontoUntertitelPanel();
	TitelZVKontoUntertitelPanel titelKontoUntertitelPanel = new TitelZVKontoUntertitelPanel();
	JButton buAnlegen = new JButton("Anlegen");
	JButton buAendern = new JButton("Ändern");
	JButton buLoeschen = new JButton("Löschen");
	JButton buBeenden = new JButton("Beenden");
	JButton buAktualisieren = new JButton("Aktualisieren");
	MainFrame frame;

	/**
	 * Erzeugen von Frame zur ZVKontenverwaltung. 
	 * @param frame = MainFrame in dem sich das InternalFrame befindet, und das den ApplicationServer besitzt. 
	 */
	public ZVKontenverwaltung( MainFrame frame ) {
		super( "ZV-Kontenverwaltung" );
		this.setClosable( true );
		this.setIconifiable( true);
		this.getContentPane().setLayout( null );
		this.frame = frame;
		
		scrollZVKonten.getViewport().add( treeKonten = new ZVKontenTree( this, "ZVKonten" ), null );
		scrollZVKonten.setBounds(new Rectangle(10, 10, 280, 245));
		this.getContentPane().add(scrollZVKonten);
		rootPanel.setBounds(new Rectangle(300, 10, 500, 245));
		this.getContentPane().add(rootPanel);
		tgrKontoPanel.setBounds(new Rectangle(300, 10, 500, 245));
		this.getContentPane().add(tgrKontoPanel);
		titelKontoPanel.setBounds(new Rectangle(300, 10, 500, 245));
		this.getContentPane().add(titelKontoPanel);
		titelKontoTitelPanel.setBounds(new Rectangle(300, 10, 500, 245));
		this.getContentPane().add(titelKontoTitelPanel);
		titelKontoUntertitelPanel.setBounds(new Rectangle(300, 10, 500, 245));
		this.getContentPane().add(titelKontoUntertitelPanel);
		tgrKontoTitelPanel.setBounds(new Rectangle(300, 10, 500, 245));
		this.getContentPane().add(tgrKontoTitelPanel);
		tgrKontoUntertitelPanel.setBounds(new Rectangle(300, 10, 500, 245));
		this.getContentPane().add(tgrKontoUntertitelPanel);
		
		// Unten die Buttons
		buAktualisieren.setBounds(new Rectangle(10, 265, 150, 25));
		buAktualisieren.addActionListener( this );
		this.getContentPane().add(buAktualisieren);
		buAktualisieren.setIcon(Functions.getRefreshIcon(getClass()));
		buAnlegen.setBounds(new Rectangle(170, 265, 150, 25));
		buAnlegen.addActionListener( this );
		this.getContentPane().add(buAnlegen);
		buAnlegen.setIcon(Functions.getAddIcon(getClass()));
		buAendern.setBounds(new Rectangle(330, 265, 150, 25));
		buAendern.addActionListener( this );
		this.getContentPane().add(buAendern);
		buAendern.setIcon(Functions.getEditIcon(getClass()));
		buLoeschen.setBounds(new Rectangle(490, 265, 150, 25));
		buLoeschen.addActionListener( this );
		this.getContentPane().add(buLoeschen);
		buLoeschen.setIcon(Functions.getDelIcon(getClass()));
		buBeenden.setBounds(new Rectangle(650, 265, 150, 25));
		buBeenden.addActionListener( this );
		this.getContentPane().add(buBeenden);
		buBeenden.setIcon(Functions.getCloseIcon(getClass()));
		
		loadZVKonten();
		showPanel();

		this.setSize( 818, 333 );
		setLocation((frame.getWidth()/2) - (getWidth()/2), (frame.getHeight()/2) - (getHeight()/2));
    
	}


	public static void main(String[] args) {
	  JFrame test = new JFrame("ZVKontenverwaltung");
	  JDesktopPane desk = new JDesktopPane();
	  desk.setDesktopManager(new DefaultDesktopManager());
	  test.setContentPane(desk);
	  test.setBounds(100,100,800,350);
	  JInternalFrame child = new ZVKontenverwaltung( null );

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
	 * Laden der ZVKonten in den Baum. 
	 */
	void loadZVKonten() {
		if( frame != null ) {
			try {
				treeKonten.delTree();
				if(frame.getBenutzer().getSichtbarkeit() == Benutzer.VIEW_FACHBEREICH)
				    treeKonten.loadZVKonten( frame.getApplicationServer().getZVKonten() );
				else {
				    buAendern.setEnabled(false);
				    buAnlegen.setEnabled(false);
				    buLoeschen.setEnabled(false);
				}
			} catch (ApplicationServerException e) {
				System.out.println( e.toString() );
			}
		}
	}
	
	/**
	 * Anzeigen vom richtigen Panel und des ausgewählten Kontos im Panel. 
	 */
	void showPanel() {
		if( treeKonten.rootIsSelected() ){
			rootPanel.setVisible( true );
			rootPanel.setTextfields();
			tgrKontoPanel.setVisible( false );
			titelKontoPanel.setVisible( false );
			tgrKontoTitelPanel.setVisible( false );
			titelKontoTitelPanel.setVisible( false );
			tgrKontoUntertitelPanel.setVisible( false );
			titelKontoUntertitelPanel.setVisible( false );
		} else if( treeKonten.zvKontoIsSelected() ) {
			rootPanel.setVisible( false );
			tgrKontoTitelPanel.setVisible( false );
			titelKontoTitelPanel.setVisible( false );
			tgrKontoUntertitelPanel.setVisible( false );
			titelKontoUntertitelPanel.setVisible( false );
			if( treeKonten.getZVKonto().isTGRKonto() ) {
				tgrKontoPanel.setVisible( true );
				tgrKontoPanel.setTextfields( treeKonten.getZVKonto(), frame );
				titelKontoPanel.setVisible( false );
			} else {
				tgrKontoPanel.setVisible( false );
				titelKontoPanel.setVisible( true );
				titelKontoPanel.setTextfields( treeKonten.getZVKonto(), frame );
			}
		} else if( treeKonten.zvTitelIsSelected() ) {
			rootPanel.setVisible( false );
			tgrKontoPanel.setVisible( false );
			titelKontoPanel.setVisible( false );
			tgrKontoUntertitelPanel.setVisible( false );
			titelKontoUntertitelPanel.setVisible( false );
			if( treeKonten.getZVKonto().isTGRKonto() ) {
				tgrKontoTitelPanel.setVisible( true );
				tgrKontoTitelPanel.setTextfields( treeKonten.getZVTitel() );
				titelKontoTitelPanel.setVisible( false );
			} else {
				tgrKontoTitelPanel.setVisible( false );
				titelKontoTitelPanel.setVisible( true );
				titelKontoTitelPanel.setTextfields( treeKonten.getZVTitel() );
			}
		} else if( treeKonten.zvUntertitelIsSelected() ) {
			rootPanel.setVisible( false );
			tgrKontoPanel.setVisible( false );
			titelKontoPanel.setVisible( false );
			tgrKontoTitelPanel.setVisible( false );
			titelKontoTitelPanel.setVisible( false );
			if( treeKonten.getZVKonto().isTGRKonto() ) {
				tgrKontoUntertitelPanel.setVisible( true );
				tgrKontoUntertitelPanel.setTextfields( treeKonten.getZVUntertitel() );
				titelKontoUntertitelPanel.setVisible( false );
			} else {
				tgrKontoUntertitelPanel.setVisible( false );
				titelKontoUntertitelPanel.setVisible( true );
				titelKontoUntertitelPanel.setTextfields( treeKonten.getZVUntertitel() );
			}
		}
	}
	
	/**
	 * Ein ZVKonto in der Datenbank und in dem Baum aktualisieren. 
	 * @return String mit Fehlern. Wenn keine Fehler, dann ist der String leer.
	 * @throws ApplicationServerException, RemoteException.
	 */
	String setZVKonto() throws ApplicationServerException, RemoteException {
		ZVKonto zvKonto;
		String error = "";
		if( treeKonten.getZVKonto().isTGRKonto() ){
			if( (error += tgrKontoPanel.getEditErrorString()).equalsIgnoreCase( "" ) )
				zvKonto = tgrKontoPanel.getEditZVKonto();
			else
				return error;
		} else {
			if( (error += titelKontoPanel.getEditErrorString()).equalsIgnoreCase( "" ) )
				zvKonto = titelKontoPanel.getEditZVKonto();
			else
				return error;
		}
		
		ZVKonto copy = (ZVKonto)treeKonten.getZVKonto().cloneWhole();	// Eine Kopie des vorhandenen ZVKontos erstellen
		copy.setZVKonto( zvKonto );										// Die Kopie aktualisieren
		if( frame.getApplicationServer().setZVKonto( copy, true ) == zvKonto.getId() ) {	// In der Datenbank aktualisieren
			treeKonten.getZVKonto().setZVKonto( zvKonto );		// Im Baum aktualisieren
			treeKonten.refreshCurrentNode();		// Neu anzeigen
		} else {
			error += " - Ausnahmefehler ist aufgetreten !\n";
		}
		
		return error;
	}
	
	/**
	 * Einen ZVTitel in der Datenbank und in dem Baum aktualisieren.
	 * @return String mit Fehlern. Wenn keine Fehler, dann ist der String leer.
	 * @throws ApplicationServerException, RemoteException.
	 */
	String setZVTitel() throws ApplicationServerException, RemoteException {
		ZVTitel zvTitel;
		String error = "";
		if( treeKonten.getZVKonto().isTGRKonto() ){
			if( (error += tgrKontoTitelPanel.getEditErrorString()).equalsIgnoreCase( "" ) )
				zvTitel = tgrKontoTitelPanel.getEditZVTitel();
			else
				return error;
		} else {
			if( (error += titelKontoTitelPanel.getEditErrorString()).equalsIgnoreCase( "" ) )
				zvTitel = titelKontoTitelPanel.getEditZVTitel();
			else
				return error;
		}
		
		ZVTitel copy = (ZVTitel)treeKonten.getZVTitel().cloneWhole();	// Eine Kopie des vorhandenen ZVTitels erstellen
		copy.setZVKonto( (ZVKonto)(((ZVTitel)treeKonten.getZVTitel()).getZVKonto().clone()) );
		copy.setZVTitel( zvTitel );		// Die Kopie aktualisieren
		if( frame.getApplicationServer().setZVTitel( copy ) == zvTitel.getId() ) {	// In der Datenbank aktualisieren
			treeKonten.getZVTitel().setZVTitel( zvTitel );		// Im Baum aktualisieren
			treeKonten.refreshCurrentNode();		// Neu anzeigen
		} else {
			error += " - Ausnahmefehler ist aufgetreten !\n";
		}
		
		return error;
	}
	
	/**
	 * Einen ZVUntertitel in der Datenbank und in dem Baum aktualisieren. 
	 * @return String mit Fehlern. Wenn keine Fehler, dann ist der String leer.
	 * @throws ApplicationServerException, RemoteException.
	 */
	String setZVUntertitel() throws ApplicationServerException, RemoteException {
		ZVUntertitel zvUntertitel;
		String error = "";
		if( treeKonten.getZVKonto().isTGRKonto() ){
			if( (error += tgrKontoUntertitelPanel.getEditErrorString()).equalsIgnoreCase( "" ) )
				zvUntertitel = tgrKontoUntertitelPanel.getEditZVUntertitel();
			else
				return error;
		} else {
			if( (error += titelKontoUntertitelPanel.getEditErrorString()).equalsIgnoreCase( "" ) )
				zvUntertitel = titelKontoUntertitelPanel.getEditZVUntertitel();
			else
				return error;
		}
		
		ZVUntertitel copy = (ZVUntertitel)treeKonten.getZVUntertitel().clone();	// Eine Kopie erstellen
		copy.setZVUntertitel( zvUntertitel );		// Kopie aktualisieren
		if( frame.getApplicationServer().setZVUntertitel( copy ) == zvUntertitel.getId() ) {	// In der DB aktualisieren
			treeKonten.getZVUntertitel().setZVUntertitel( zvUntertitel );	// Im Baum aktualisieren
			treeKonten.refreshCurrentNode();	// Neu anzeigen
		} else {
			error += " - Ausnahmefehler ist aufgetreten !\n";
		}
		
		return error;
	}
	
	/**
	 * Ein ZVKonto in der Datenbank und in dem Baum einfügen.
	 * @return String mit Fehlern. Wenn keine Fehler, dann ist der String leer.
	 * @throws ApplicationServerException, RemoteException.
	 */
	String addZVKonto() throws ApplicationServerException, RemoteException {
		ZVKonto zvKonto;
		String error = "";
		
		if( (error += rootPanel.getAddErrorString()).equalsIgnoreCase( "" ) )
			zvKonto = rootPanel.getNewZVKonto();
		else
			return error;
		
		zvKonto.setHaushaltsJahrId( frame.getApplicationServer().getCurrentHaushaltsjahrId() );
		int zvKontoId = frame.getApplicationServer().addZVKonto( zvKonto );
		if( zvKontoId > 0 ) {
			zvKonto.setId( zvKontoId );
			treeKonten.addNewNode( zvKonto );
			if( !zvKonto.isTGRKonto() ) {
				int zvTitelId = frame.getApplicationServer().getZVTitelId( (ZVTitel)zvKonto.getSubTitel().get(0) );
				if( zvTitelId > 0 ) {
					((ZVTitel)zvKonto.getSubTitel().get(0)).setId( zvTitelId );
					treeKonten.addNewNode( zvKonto.getSubTitel().get(0) );
				} else {
					error += " - Ausnahmefehler ist aufgetreten !\n";
				}
			}
		} else {
			error += " - Ausnahmefehler ist aufgetreten !\n";
		}
		
		return error;
	}
	
	/**
	 * Einen ZVTitel in der Datenbank und in dem Baum einfügen.
	 * @return String mit Fehlern. Wenn keine Fehler, dann ist der String leer.
	 * @throws ApplicationServerException, RemoteException.
	 */
	String addZVTitel() throws ApplicationServerException, RemoteException {
		ZVTitel zvTitel;
		String error = "";
		if( treeKonten.getZVKonto().isTGRKonto() ){
			if( (error += tgrKontoPanel.getAddErrorString()).equalsIgnoreCase( "" ) )
				zvTitel = tgrKontoPanel.getNewZVTitel();
			else
				return error;
		} else {
			error += " - Ein Kapitel/Titel-Konto kann keine zwei Titel haben.\n"; 
			return error;
		}
		
		int zvTitelId = frame.getApplicationServer().addZVTitel( zvTitel );
		if( zvTitelId > 0 ) {
			zvTitel.setId( zvTitelId );
			treeKonten.getZVKonto().getSubTitel().add( zvTitel );
			treeKonten.addNewNode( zvTitel );
		} else {
			error += " - Ausnahmefehler ist aufgetreten !\n";
		}
		
		return error;
	}
	
	/**
	 * Einen ZVUntertitel in der Datenbank und in dem Baum aktualisieren. 
	 * @return String mit Fehlern. Wenn keine Fehler, dann ist der String leer.
	 * @throws ApplicationServerException, RemoteException.
	 */
	String addZVUntertitel() throws ApplicationServerException, RemoteException {
		ZVUntertitel zvUntertitel;
		String error = "";
		if( treeKonten.getZVKonto().isTGRKonto() ){
			if( (error += tgrKontoTitelPanel.getAddErrorString()).equalsIgnoreCase( "" ) )
				zvUntertitel = tgrKontoTitelPanel.getNewZVUntertitel();
			else
				return error;
		} else {
			if( (error += titelKontoTitelPanel.getAddErrorString()).equalsIgnoreCase( "" ) )
				zvUntertitel = titelKontoTitelPanel.getNewZVUntertitel();
			else
				return error;
		}
		
		int zvUntertitelId = frame.getApplicationServer().addZVUntertitel( zvUntertitel );
		if( zvUntertitelId > 0 ) {
			zvUntertitel.setId( zvUntertitelId );
			treeKonten.getZVTitel().getAllUntertitel().add( zvUntertitel );
			treeKonten.addNewNode( zvUntertitel );
		} else {
			error += " - Ausnahmefehler ist aufgetreten !\n";
		}
		
		return error;
	}

	/**
	 * Ein ZVKonto in der Datenbank und im Baum löschen.
	 * @return String mit Fehlern. Wenn keine Fehler, dann ist der String leer.
	 * @throws ApplicationServerException, RemoteException.
	 */
	String delZVKonto() throws ApplicationServerException, RemoteException {
		String error = "";
		if( frame.getApplicationServer().delZVKonto( treeKonten.getZVKonto() ) == treeKonten.getZVKonto().getId() ) {
			treeKonten.delNode();
		} else {
			error += " - Ausnahmefehler ist aufgetreten !\n";
		}
		
		return error;
	}
	
	/**
	 * Einen ZVTitel in der Datenbank und in dem Baum löschen.
	 * @return String mit Fehlern. Wenn keine Fehler, dann ist der String leer.
	 * @throws ApplicationServerException, RemoteException.
	 */
	String delZVTitel() throws ApplicationServerException, RemoteException {
		String error = "";
		if( frame.getApplicationServer().delZVTitel( treeKonten.getZVTitel() ) == treeKonten.getZVTitel().getId() ) {
			treeKonten.getZVKonto().getSubTitel().remove( treeKonten.getZVTitel() );
			treeKonten.delNode();
		} else {
			error += " - Ausnahmefehler ist aufgetreten !\n";
		}
		
		return error;
	}
	
	/**
	 * Einen ZVUntertitel in der Datenbank und in dem Baum löschen. 
	 * @return String mit Fehlern. Wenn keine Fehler, dann ist der String leer.
	 * @throws ApplicationServerException, RemoteException.
	 */
	String delZVUntertitel() throws ApplicationServerException, RemoteException {
		String error = "";
		if( frame.getApplicationServer().delZVUntertitel(treeKonten.getZVUntertitel()) == treeKonten.getZVUntertitel().getId() ) {
			treeKonten.getZVTitel().getAllUntertitel().remove( treeKonten.getZVUntertitel() );
			treeKonten.delNode();
		} else {
			error += " - Ausnahmefehler ist aufgetreten !\n";
		}
		
		return error;
	}
	
	/**
	 * Eine Nachricht erzeugen wenn man auf den "Anlegen"-Button gedrückt hat.
	 * @return Die erzeugte Nachricht.
	 */
	String getAddMessage() {
		if( treeKonten.rootIsSelected() ) {
			return "Wollen Sie das ZVKonto\n" + rootPanel.getNewZVKonto().toString() + "\nwirklich anlegen ?";
		} else if( treeKonten.zvKontoIsSelected() ) {
			return "Wollen Sie den ZVTitel\n" + tgrKontoPanel.getNewZVTitel().toString() + "\nwirklich anlegen ?";
		} else if( treeKonten.zvTitelIsSelected() ) {
			if( treeKonten.getZVKonto().isTGRKonto() ){
				return "Wollen Sie den ZVUntertitel\n" + tgrKontoTitelPanel.getNewZVUntertitel().toString() +
						"\nwirklich anlegen ?";
			} else {
				return "Wollen Sie den ZVUntertitel\n" + titelKontoTitelPanel.getNewZVUntertitel().toString() +
						"\nwirklich anlegen ?";
			} 
		} else {
			return "";
		}
	}
	
	/**
	 * Eine Nachricht erzeugen wenn man auf den "Ändern"-Button gedrückt hat.
	 * @return Die erzeugte Nachricht. 
	 */
	String getEditMessage() {
		if( treeKonten.zvKontoIsSelected() ) {
			if( treeKonten.getZVKonto().isTGRKonto() ){
				return "Wollen Sie das ZVKonto\n" + tgrKontoPanel.getEditZVKonto().toString() +
						"\nmit allen ZVTiteln und ZVUntertiteln wirklich ändern ?";
			} else {
				return "Wollen Sie das ZVKonto\n" + titelKontoPanel.getEditZVKonto().toString() +
						"\nmit allen ZVTiteln und ZVUntertiteln wirklich ändern ?";
			}
		} else if( treeKonten.zvTitelIsSelected() ) {
			if( treeKonten.getZVKonto().isTGRKonto() ){
				return "Wollen Sie den ZVTitel\n" + tgrKontoTitelPanel.getEditZVTitel().toString() +
						"\nmit allen ZVUntertiteln wirklich ändern ?";
			} else {
				return "Wollen Sie den ZVTitel\n" + titelKontoTitelPanel.getEditZVTitel().toString() +
						"\nmit allen ZVUntertiteln wirklich ändern ?";
			}
		} else if( treeKonten.zvUntertitelIsSelected() ) {
			if( treeKonten.getZVKonto().isTGRKonto() ){
				return "Wollen Sie den ZVUntertitel\n" + tgrKontoUntertitelPanel.getEditZVUntertitel().toString() +
						"\nwirklich ändern ?";
			} else {
				return "Wollen Sie den ZVUntertitel\n" + titelKontoUntertitelPanel.getEditZVUntertitel().toString() +
						"\nwirklich ändern ?";
			}
		} else {
			return "";
		}
	}
	
	/**
	 * Eine Nachricht erzeugen wenn man auf den "Ändern"-Button gedrückt hat. 
	 * @return Die erzeugte Nachricht. 
	 */
	String getDelMessage() {
		if( treeKonten.zvKontoIsSelected() ) {
			return "Wollen Sie das ZVKonto\n" + treeKonten.getZVKonto().toString() +
						"\nmit allen ZVTiteln und ZVUntertiteln wirklich löschen ?";
		} else if( treeKonten.zvTitelIsSelected() ) {
			return "Wollen Sie den ZVTitel\n" + treeKonten.getZVTitel().toString() +
						"\nmit allen ZVUntertiteln wirklich löschen ?";
		} else if( treeKonten.zvUntertitelIsSelected() ) {
			return "Wollen Sie den ZVUntertitel\n" + treeKonten.getZVUntertitel().toString() +
						"\nwirklich löschen ?";
		} else {
			return "";
		}
	}

	/**
	 * Behandlung der Button-Ereignisse.
	 */
	public void actionPerformed(ActionEvent e) {
		String error = "";
		
		if ( e.getSource() == buAnlegen ) {
			if( frame == null )
				return;
			if( treeKonten.zvUntertitelIsSelected() ){
				error += " - Ein ZVUntertitel kann keine weiteren Konten enthalten.\n";
			} else if( treeKonten.zvKontoIsSelected() ) {
				if( !treeKonten.getZVKonto().isTGRKonto() ) {
					error += " - Ein 'Kapitel/Titel'-Konto kann keine zwei ZVTitel enthalten.\n";
				}
			}
			if( error.equalsIgnoreCase( "" ) ) {
				if( JOptionPane.showConfirmDialog( this, getAddMessage(), "Anlegen ?", JOptionPane.YES_NO_OPTION,
																JOptionPane.QUESTION_MESSAGE  ) == JOptionPane.YES_OPTION ){
					try {
						if( treeKonten.rootIsSelected() ) {
							error += addZVKonto();
						} else if( treeKonten.zvKontoIsSelected() ) {
							error += addZVTitel();
						} else if( treeKonten.zvTitelIsSelected() ) {
							error += addZVUntertitel();
						}			
					} catch (ApplicationServerException exception) {
						error += " - " + exception.toString();
					} catch(RemoteException re) {
						error += " - Fehler bei RMI-Kommunikation.";
					}
				}
			}
		} else if ( e.getSource() == buAendern ) {
			if( frame == null || treeKonten.rootIsSelected() )
				return;
				
			if( JOptionPane.showConfirmDialog( this, getEditMessage(), "Ändern ?", JOptionPane.YES_NO_OPTION,
																	JOptionPane.QUESTION_MESSAGE  ) == JOptionPane.YES_OPTION ){
				try {
					if( treeKonten.zvKontoIsSelected() ) {
						error += setZVKonto();
					} else if( treeKonten.zvTitelIsSelected() ) {
						error += setZVTitel();
					} else if( treeKonten.zvUntertitelIsSelected() ) {
						error += setZVUntertitel();
					}
				} catch (ApplicationServerException exception) {
					error += " - " + exception.toString();
				} catch(RemoteException re) {
					error += " - Fehler bei RMI-Kommunikation.";
				}
			}
		} else if ( e.getSource() == buAktualisieren ) {
			loadZVKonten();
		} else if ( e.getSource() == buLoeschen ) {
			if( frame == null || treeKonten.rootIsSelected() )
				return;
			if( treeKonten.zvTitelIsSelected() ) {
				if( !treeKonten.getZVKonto().isTGRKonto() ) {
					error += "Bei einem 'Kapitel/Titel'-Konto dürfen Sie den Titel nicht löschen !\n";
				}
			}
			if( error.equalsIgnoreCase( "" ) ) {
				if( JOptionPane.showConfirmDialog( this, getDelMessage(), "Löschen ?", JOptionPane.YES_NO_OPTION,
																	JOptionPane.QUESTION_MESSAGE  ) == JOptionPane.YES_OPTION ){
					try {
						if( treeKonten.zvKontoIsSelected() ) {
							delZVKonto();
						} else if( treeKonten.zvTitelIsSelected() ) {
							delZVTitel();
						} else if( treeKonten.zvUntertitelIsSelected() ) {
							delZVUntertitel();
						}			
					} catch (ApplicationServerException exception) {
						error += " - " + exception.toString();
					} catch(RemoteException re) {
						error += " - Fehler bei RMI-Kommunikation.";
					}
				}
			}
		} else if ( e.getSource() == buBeenden ) {
			this.dispose();
		}
		
		if( !error.equalsIgnoreCase( "" ) ) {
			error = "Folgende Fehler sind aufgetreten:\n" + error;
			JOptionPane.showMessageDialog( this, error,	"Fehler !", JOptionPane.ERROR_MESSAGE );
		}
	}

	/**
	 * Verarbeitung der Ereignisse im Baum. 
	 */
	public void valueChanged(TreeSelectionEvent e) {
		treeKonten.checkSelection( e );
		showPanel();
	}
}

/**
 * Panel zum Anzeigen aller Textfelder, die gebraucht werden wenn der root-Knoten im Baum ausgewählt wurde.<br>
 * Hier kann man ein neues ZVKonto erstellen.<br>
 * Es werden auch alle Funktion implementiert, die dazu notwendig sind.<br>
 * @author w.flat
 */
class RootPanel extends JPanel implements ActionListener {

	JLabel labBezeichnung = new JLabel();
	JTextField tfBezeichnung = new JTextField();
	JLabel labKapitel = new JLabel();
	JLabel labTGR = new JLabel();
	IntTextField tfKapitel = new IntTextField(5);
	IntTextField tfTGR = new IntTextField(2);
	IntTextField tfTitel = new IntTextField(5);
	JLabel labTitel = new JLabel();
	JCheckBox checkZweckgebunden = new JCheckBox();
	JCheckBox checkFreigegeben = new JCheckBox();
	JLabel labDispolimit = new JLabel();
	CurrencyTextField tfDispolimit = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	JRadioButton rbKapitelTGR = new JRadioButton( "Kapitel/Titelgruppe-Konto", true );
	JRadioButton rbKapitelTitel = new JRadioButton( "Kapitel/Titel-Konto", false );
	ButtonGroup group = new ButtonGroup();

	/**
	 * Erzeugung des <code>RootPanel</code>s.
	 */
	public RootPanel() {
	  try {
		jbInit();
	  }
	  catch(Exception ex) {
		ex.printStackTrace();
	  }
	  
	  setTextfields();
	}
	
	/**
	 * Initialisieren der graphischen Oberfläche.
	 * @throws Exception
	 */
	void jbInit() throws Exception {
		labBezeichnung.setText("Bezeichnung");
		labBezeichnung.setBounds(new Rectangle(12, 12, 90, 15));
		this.setLayout(null);
		labKapitel.setText("Kapitel");
		labKapitel.setBounds(new Rectangle(12, 47, 90, 15));
		labTGR.setText("Titelgruppe");
		labTGR.setBounds(new Rectangle(322, 47, 90, 15));
		labTitel.setText("Titel");
		labTitel.setBounds(new Rectangle(322, 47, 90, 15));
		tfBezeichnung.setText("");
		tfBezeichnung.setBounds(new Rectangle(102, 10, 390, 21));
		tfKapitel.setText("");
		tfKapitel.setBounds(new Rectangle(102, 47, 80, 21));
		tfTGR.setText("");
		tfTGR.setBounds(new Rectangle(412, 47, 80, 21));
		tfTitel.setText("");
		tfTitel.setBounds(new Rectangle(412, 47, 80, 21));
		checkZweckgebunden.setText("Zweckgebunden");
		checkZweckgebunden.setBounds(new Rectangle(12, 82, 140, 20));
		labDispolimit.setText("Dispolimit");
		labDispolimit.setBounds(new Rectangle(12, 112, 90, 15));
		tfDispolimit.setBounds(new Rectangle(102, 112, 150, 21));
		rbKapitelTGR.setText("Kapitel/Titelgruppe-Konto");
		rbKapitelTGR.setBounds(new Rectangle(12, 147, 190, 23));
		rbKapitelTitel.setText("Kapitel/Titel-Konto");
		rbKapitelTitel.setBounds(new Rectangle(302, 147, 190, 23));
		this.setBorder(BorderFactory.createEtchedBorder());
		this.setMaximumSize(new Dimension(400, 183));
		this.setMinimumSize(new Dimension(400, 183));
		this.setOpaque(true);
		this.setPreferredSize(new Dimension(400, 183));
		checkFreigegeben.setBounds(new Rectangle(322, 82, 130, 21));
		checkFreigegeben.setText( "Freigegeben" );
		this.add(labBezeichnung, null);
		this.add(labKapitel, null);
		this.add(tfBezeichnung, null);
		this.add(tfKapitel, null);
		this.add(tfTGR, null);
		this.add(tfTitel, null);
		this.add(labTitel, null);
		this.add(labTGR, null);
		this.add(checkZweckgebunden, null);
		this.add(checkFreigegeben, null);
		this.add(labDispolimit, null);
		this.add(tfDispolimit, null);
		this.add(rbKapitelTGR, null);
		this.add(rbKapitelTitel, null);	  group.add( rbKapitelTGR );
	  group.add( rbKapitelTitel );
	  rbKapitelTGR.addActionListener( this );
	  rbKapitelTitel.addActionListener( this );
	}
		
	/**
	 * Alle Textfelder in Ausgangsposition bringen. 
	 */
	public void setTextfields(){
		tfBezeichnung.setText( "" );
		tfKapitel.setValue( "" );
		tfTGR.setValue( "" );
		tfTitel.setValue( "" );
		tfDispolimit.setValue( new Float( 0 ) );
		if( rbKapitelTGR.isSelected() )
			actionPerformed( new ActionEvent( rbKapitelTGR, 0, "" ) );
		else
			actionPerformed( new ActionEvent( rbKapitelTitel, 0, "" ) );
	}
	
	/**
	 * Ermittlung der Fehler, die beim Erstellen eines ZVKontos entstanden sind.
	 * @return String mit Fehlern. Wenn String leer, dann sind keine Fehler aufgetreten. 
	 */
	public String getAddErrorString() {
		String error = "";
		if( tfBezeichnung.getText().equalsIgnoreCase( "" ) )
			error += " - Die Bezeichnung darf nicht leer sein.\n";
		
		return error;
	}
	
	/**
	 * Erstellen eines neuen ZVKontos. 
	 * @return Neues ZVKonto, das aus den Daten des Panels erzeugt wurde. 
	 */
	public ZVKonto getNewZVKonto() {
		ZVKonto zvKonto;
		if( rbKapitelTGR.isSelected() ){
			zvKonto = new ZVKonto( 0, 0, tfBezeichnung.getText(), tfKapitel.getText(), tfTGR.getText(), 0.0f,
									Float.parseFloat( tfDispolimit.getValue().toString() ), checkZweckgebunden.isSelected(),
									checkFreigegeben.isSelected() ? '1' : '0', (short)0, false, false );
		} else {
			zvKonto = new ZVKonto( 0, 0, tfBezeichnung.getText(), tfKapitel.getText(), "", 0.0f,
									Float.parseFloat( tfDispolimit.getValue().toString() ), checkZweckgebunden.isSelected(),
									checkFreigegeben.isSelected() ? '1' : '0', (short)0, false, false );
			zvKonto.getSubTitel().add( new ZVTitel( 0, zvKonto, tfBezeichnung.getText(), tfTitel.getText(), "", 0.0f, "", "" ) );
		}
		
		return zvKonto;
	}

	/**
	 * Hier erfolgt die Reaktion wenn eines der RadioButtons gedrückt wurde. 
	 */
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == rbKapitelTGR ) {
			tfTGR.setVisible( true );
			labTGR.setVisible( true );
			tfTitel.setVisible( false );
			labTitel.setVisible( false );
		} else if( e.getSource() == rbKapitelTitel ) {
			tfTGR.setVisible( false );
			labTGR.setVisible( false );
			tfTitel.setVisible( true );
			labTitel.setVisible( true );
		}
	}
}

/**
 * Panel zum Anzeigen aller Textfelder, die gebraucht werden wenn ein TGRKonto-Knoten im Baum ausgewählt wurde. <br>
 * Es dient auch zum Aktualisieren eines ZVKontos und zum Erstellen eines neuen ZVTitels zum ausgewählten ZVKonto.<br>
 * Es werden auch alle Funktion implementiert, die dazu notwendig sind.<br>
 * @author w.flat
 */
class TGRZVKontoPanel extends JPanel implements ActionListener {

	ZVKonto zvKonto;
	MainFrame frame;
	JLabel labBezeichnung = new JLabel();
	JTextField tfBezeichnung = new JTextField();
	JLabel labKapitel = new JLabel();
	IntTextField tfKapitel = new IntTextField(5);
	JLabel labTGR = new JLabel();
	IntTextField tfTGR = new IntTextField(2);
	JCheckBox checkZweckgebunden = new JCheckBox();
	JLabel labDispolimit = new JLabel();
	CurrencyTextField tfDispolimit = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	JCheckBox checkFreigegeben = new JCheckBox();
	JLabel labBudget = new JLabel();
	CurrencyTextField tfBudget = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	JLabel labLimit = new JLabel();
	JLabel labKategorie = new JLabel();
	IntTextField tfKategorie = new IntTextField(3);
	JLabel labBemerkung = new JLabel();
	JTextField tfBemerkung = new JTextField();
	JCheckBox checkPruefbedingung = new JCheckBox();
	ButtonGroup groupPruefbedingung = new ButtonGroup();
	JRadioButton rbBis = new JRadioButton("Bis", true);
	JRadioButton rbAb = new JRadioButton("Ab", false);
	CurrencyTextField tfPruefbedingung = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );

	/**
	 * Das <code>TGRZVKontoPanel</code> ertsellen. 
	 */
	public TGRZVKontoPanel() {
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
		labBezeichnung.setText("Bezeichnung");
		labBezeichnung.setBounds(new Rectangle(12, 12, 90, 15));
		this.setLayout(null);
		tfBezeichnung.setText("");
		tfBezeichnung.setBounds(new Rectangle(102, 12, 390, 21));
		labKapitel.setText("Kapitel");
		labKapitel.setBounds(new Rectangle(12, 47, 90, 15));
		labTGR.setText("Titelgruppe");
		labTGR.setBounds(new Rectangle(322, 47, 90, 15));
		tfKapitel.setText("");
		tfKapitel.setBounds(new Rectangle(102, 47, 80, 21));
		tfTGR.setText("");
		tfTGR.setBounds(new Rectangle(412, 47, 80, 21));
		checkZweckgebunden.setText("Zweckgebunden");
		checkZweckgebunden.setBounds(new Rectangle(12, 82, 140, 20));
		labDispolimit.setText("Dispolimit");
		labDispolimit.setBounds(new Rectangle(12, 112, 80, 15));
		tfDispolimit.setText("");
		tfDispolimit.setBounds(new Rectangle(92, 112, 100, 21));
		labBudget.setText("Budget");
		labBudget.setBounds(new Rectangle(312, 112, 80, 15));
		tfBudget.setText("");
		tfBudget.setBounds(new Rectangle(392, 112, 100, 21));
		labLimit.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
		labLimit.setForeground(Color.blue);
		labLimit.setText("Zusätzliche Angaben zum neuem Titel");
		labLimit.setBounds(new Rectangle(12, 152, 300, 16));
		this.setFont(new java.awt.Font("SansSerif", 0, 12));
		this.setBorder(BorderFactory.createEtchedBorder());
		labKategorie.setText("Kategorie");
		labKategorie.setBounds(new Rectangle(12, 182, 80, 15));
		tfKategorie.setText("");
		tfKategorie.setBounds(new Rectangle(92, 182, 80, 21));
		labBemerkung.setText("Bemerkung");
		labBemerkung.setBounds(new Rectangle(182, 182, 80, 15));
		tfBemerkung.setText("");
		tfBemerkung.setBounds(new Rectangle(262, 182, 230, 21));
		checkPruefbedingung.setSelected(false);
		checkPruefbedingung.setText("Prüfbedingung");
		checkPruefbedingung.setBounds(new Rectangle(12, 212, 140, 22));
		rbBis.setText("Bis");
		rbBis.setBounds(new Rectangle(202, 212, 60, 22));
		rbAb.setText("Ab");
		rbAb.setBounds(new Rectangle(262, 212, 60, 22));
		tfPruefbedingung.setBounds(new Rectangle(372, 212, 120, 21));
		checkFreigegeben.setBounds(new Rectangle(322, 82, 130, 21));
		checkFreigegeben.setText( "Freigegeben" );
		this.add(labBezeichnung, null);
		this.add(tfBezeichnung, null);
		this.add(labKapitel, null);
		this.add(tfKapitel, null);
		this.add(labTGR, null);
		this.add(tfTGR, null);
		this.add(checkZweckgebunden, null);
		this.add(checkFreigegeben, null);
		this.add(labDispolimit, null);
		this.add(labBudget, null);
		this.add(tfBudget, null);
		this.add(labLimit, null);
		this.add(labKategorie, null);
		this.add(tfKategorie, null);
		this.add(labBemerkung, null);
		this.add(tfBemerkung, null);
		this.add(checkPruefbedingung, null);
		this.add(rbBis, null);
		this.add(rbAb, null);
		this.add(tfPruefbedingung, null);
		this.add(tfDispolimit, null);
		
		checkPruefbedingung.addActionListener( this );
		rbBis.addActionListener( this );
		rbAb.addActionListener( this );
		groupPruefbedingung.add( rbBis );
		groupPruefbedingung.add( rbAb );
		tfBudget.setEnabled( false );
		checkZweckgebunden.addActionListener( this );		
	}

	/**
	 * Die Textfelder mit Daten des übergebenden ZVKontos fühlen. 
	 * @param frame = MainFrame mit dem ApplicationServer. 
	 * @param zvKonto = Das ZVKonto, welches angezeigt werden soll. 
	 */
	public void setTextfields( ZVKonto zvKonto, MainFrame frame ){
		if( (this.zvKonto = zvKonto) == null || (this.frame = frame) == null )	// Kein ZVkonto angegeben
			return;
		
		tfBezeichnung.setText( zvKonto.getBezeichnung() );
		tfKapitel.setValue( zvKonto.getKapitel() );
		tfTGR.setValue( zvKonto.getTitelgruppe() );
		checkZweckgebunden.setSelected( zvKonto.getZweckgebunden() );
		checkFreigegeben.setSelected( zvKonto.getFreigegeben() == '1' );
		tfDispolimit.setValue( new Float( zvKonto.getDispoLimit() ) );
		tfBudget.setValue( new Float( zvKonto.getTgrBudget() ) );
		tfKategorie.setValue( "" );
		tfBemerkung.setText( "" );
		tfPruefbedingung.setValue( new Float( 0 ) );
		actionPerformed( new ActionEvent( checkPruefbedingung, 0, "" ) );
	}
	
	/**
	 * Ermittlung der Fehler die beim Aktualisieren eines ZVKontos entstanden sind.  
	 * @return String mit Fehlern. Wenn String leer, dann sind keine Fehler aufgetreten. 
	 */
	public String getEditErrorString() {
		String error = "";
		if( tfBezeichnung.getText().equalsIgnoreCase( "" ) )
			error += " - Die Bezeichnung darf nicht leer sein.\n";
		
		return error;
	}
	
	/**
	 * Ermittlung der Fehler die beim Erstellen eines neuen ZVTitels zu dem ausgewählten ZVKonto entstanden sind.  
	 * @return String mit Fehlern. Wenn String leer, dann sind keine Fehler aufgetreten. 
	 */
	public String getAddErrorString() {
		if( zvKonto == null )
			return new String( " - Es wurde kein ZVKonto angegeben.\n" );
		
		String error = "";
		if( tfBezeichnung.getText().equalsIgnoreCase( "" ) )
			error += " - Die Bezeichnung darf nicht leer sein.\n";
		if( !tfKapitel.getText().equalsIgnoreCase( zvKonto.getKapitel() ) )
			error += " - Das Kapitel darf nicht verändert werden.\n";
		if( !tfTGR.getText().equalsIgnoreCase( zvKonto.getTitelgruppe() ) )
			error += " - Die Titelgruppe darf nicht verändert werden.\n";
		
		return error;
	}
	
	/**
	 * Estellen eines aktualisierten ZVKontos. 
	 * @return ZVKonto, das aus den Daten im Panel erzeugt wurde. 
	 */
	public ZVKonto getEditZVKonto(){
		if( zvKonto == null )		// Es wurde kein ZVKonto angegeben
			return null;
		
		return new ZVKonto( zvKonto.getId(), zvKonto.getHaushaltsJahrId(), tfBezeichnung.getText(), tfKapitel.getText(),
							tfTGR.getText(), zvKonto.getTgrBudget(), Float.parseFloat( tfDispolimit.getValue().toString() ),
							checkZweckgebunden.isSelected(), checkFreigegeben.isSelected() ? '1' : '0',
							zvKonto.getUebernahmeStatus(), zvKonto.isPortiert(), zvKonto.isAbgeschlossen() ); 
	}
	
	/**
	 * Estellen eines neuen ZVTitels zum angegebenem ZVKonto. 
	 * @return ZVKonto, das aus den Daten im Panel erzeugt wurde. 
	 */
	public ZVTitel getNewZVTitel(){
		if( zvKonto == null )		// Es wurde kein ZVKonto angegeben
			return null;
		
		return new ZVTitel( 0, zvKonto, tfBezeichnung.getText(), tfKategorie.getText() + tfTGR.getText(), "", 0.0f,
						tfBemerkung.getText(),
						ZVTitel.getPruefung( checkPruefbedingung.isSelected(), rbBis.isSelected(), tfPruefbedingung.getValue() ) );
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
		} else if( e.getSource() == checkZweckgebunden ) {
			if( frame == null || zvKonto == null )
				return;
			if( checkZweckgebunden.isSelected() ) {
				try {
					if( frame.getApplicationServer().getNumberOfKontenzuordnungen( zvKonto ) > 0 ) {
						JOptionPane.showMessageDialog( this,
							"Das Konto kann nicht zweckgebunden sein, da es mehrere Kontenzuordnungen gibt.",
							"Zweckgebunden !", JOptionPane.INFORMATION_MESSAGE );
						checkZweckgebunden.setSelected( false );
					}
				} catch (ApplicationServerException e1) {
				}
			}
		}
	}
}


/**
 * Panel zum Anzeigen aller Textfelder, die gebraucht werden, wenn ein TitelZVKonto-Knoten im Baum ausgewählt wurde.<br>
 * Hier kann man das vorhandene ZVKonto aktualisieren.<br>
 * Es werden auch alle Funktion implementiert, die dazu benötigt werden.
 * @author w.flat
 */
class TitelZVKontoPanel extends JPanel implements ActionListener {

	ZVKonto zvKonto;
	MainFrame frame;
	JTextField tfBezeichnung = new JTextField();
	JLabel labBudget = new JLabel();
	JLabel labDispolimit = new JLabel();
	IntTextField tfKapitel = new IntTextField(5);
	CurrencyTextField tfBudget = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	JLabel labKapitel = new JLabel();
	JCheckBox checkZweckgebunden = new JCheckBox();
	CurrencyTextField tfDispolimit = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	JLabel labBezeichnung = new JLabel();
	JCheckBox checkFreigegeben = new JCheckBox();

	/**
	 * Erstellen vom <code>TitelZVKontoPanel</code>.
	 */
	public TitelZVKontoPanel() {
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
	  labBezeichnung.setText("Bezeichnung");
	  labBezeichnung.setBounds(new Rectangle(12, 12, 90, 15));
	  tfDispolimit.setText("");
	  tfDispolimit.setBounds(new Rectangle(92, 112, 100, 21));
	  checkZweckgebunden.setText("Zweckgebunden");
	  checkZweckgebunden.setBounds(new Rectangle(12, 82, 140, 20));
	  labKapitel.setText("Kapitel");
	  labKapitel.setBounds(new Rectangle(12, 47, 90, 15));
	  tfBudget.setText("");
	  tfBudget.setBounds(new Rectangle(392, 112, 100, 21));
	  tfKapitel.setText("");
	  tfKapitel.setBounds(new Rectangle(102, 47, 80, 21));
	  labDispolimit.setText("Dispolimit");
	  labDispolimit.setBounds(new Rectangle(12, 112, 80, 15));
	  labBudget.setText("Budget");
	  labBudget.setBounds(new Rectangle(312, 112, 80, 15));
	  tfBezeichnung.setText("");
	  tfBezeichnung.setBounds(new Rectangle(102, 12, 390, 21));
	  this.setLayout(null);
	  this.setBorder(BorderFactory.createEtchedBorder());
	  checkFreigegeben.setBounds(new Rectangle(322, 82, 130, 21));
	  checkFreigegeben.setText( "Freigegeben" );
	  this.add(tfBezeichnung, null);
	  this.add(labBudget, null);
	  this.add(labDispolimit, null);
	  this.add(tfKapitel, null);
	  this.add(tfBudget, null);
	  this.add(labKapitel, null);
	  this.add(checkZweckgebunden, null);
	  this.add(tfDispolimit, null);
	  this.add(labBezeichnung, null);
	  this.add(checkFreigegeben, null);
	  
	  tfBudget.setEnabled( false );
	  checkZweckgebunden.addActionListener( this );
	}
	
	/**
	 * Textfelder mit Daten des ZVKontos fühlen. 
	 * @param frame = MainFrame, welches den ApplicationServer hat. 
	 * @param zvKonto = Das angezegt werden soll. 
	 */
	public void setTextfields( ZVKonto zvKonto, MainFrame frame ){
		if( (this.zvKonto = zvKonto) == null || (this.frame = frame) == null )
			return;
		
		tfBezeichnung.setText( zvKonto.getBezeichnung() );
		tfKapitel.setValue( zvKonto.getKapitel() );
		checkZweckgebunden.setSelected( zvKonto.getZweckgebunden() );
		checkFreigegeben.setSelected( zvKonto.getFreigegeben() == '1' );
		tfDispolimit.setValue( new Float( zvKonto.getDispoLimit() ) );
		tfBudget.setValue( new Float( zvKonto.getTgrBudget() ) );
	}

	/**
	 * Ermittlung der Fehler, die beim Aktualisieren eines ZVKontos aufgetreten sind. 
	 * @return String mit Fehlern. Wenn String leer, dann sind keine Fehler aufgetreten. 
	 */
	public String getEditErrorString() {
		String error = "";
		if( tfBezeichnung.getText().equalsIgnoreCase( "" ) )
			error += " - Die Bezeichnung darf nicht leer sein.\n";
			
		return error;
	}
	
	/**
	 * Ein aktualisiertes ZVKonto erstellen.
	 * @return ZVKonto, das aus den Daten im Panel erzeugt wurde. 
	 */
	public ZVKonto getEditZVKonto(){
		return new ZVKonto( zvKonto.getId(), zvKonto.getHaushaltsJahrId(), tfBezeichnung.getText(), tfKapitel.getText(),
					"", zvKonto.getTgrBudget(), Float.parseFloat( tfDispolimit.getValue().toString() ),
					checkZweckgebunden.isSelected(), checkFreigegeben.isSelected() ? '1' : '0',
					zvKonto.getUebernahmeStatus(), zvKonto.isPortiert(), zvKonto.isAbgeschlossen() );
	}
	
	/**
	 * Reaktion auf die 'Zweckgebunden'-CheckBox. 
	 */
	public void actionPerformed(ActionEvent e) {
	if( e.getSource() == checkZweckgebunden ) {
		if( frame == null || zvKonto == null )
			return;
		if( checkZweckgebunden.isSelected() ) {
			try {
				if( frame.getApplicationServer().getNumberOfKontenzuordnungen( zvKonto ) > 0 ) {
					JOptionPane.showMessageDialog( this,
						"Das Konto kann nicht zweckgebunden sein, da es mehrere Kontenzuordnungen gibt.",
						"Zweckgebunden !", JOptionPane.INFORMATION_MESSAGE );
					checkZweckgebunden.setSelected( false );
				}
			} catch (ApplicationServerException e1) {
			}
		}
	}
	}
}

/**
 * Panel zum Anzeigen aller Textfelder, die gebraucht werden wenn ein Titel-Knoten <br>
 * von einem TitelZVKonto im Baum ausgewählt wurde.<br>
 * Hier kann man den vorhandenen ZVTitel aktualisieren und einen neuen ZVUntertitel zum ZVTitel erzeugen.<br>
 * Es werden auch alle Funktion implementiert, die dazu benötigt werden.
 * @author w.flat
 */
class TitelZVKontoTitelPanel extends JPanel implements ActionListener {

	ZVTitel zvTitel;
	JLabel labKapitel = new JLabel();
	JLabel labBezeichnung = new JLabel();
	JTextField tfBezeichnung = new JTextField();
	IntTextField tfKapitel = new IntTextField(5);
	JLabel labTitel = new JLabel();
	IntTextField tfTitel = new IntTextField(5);
	JLabel labBemerkung = new JLabel();
	JLabel labBudget = new JLabel();
	CurrencyTextField tfBudget = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	JTextField tfBemerkung = new JTextField();
	ButtonGroup groupPruefbedingung = new ButtonGroup();
	JRadioButton rbBis = new JRadioButton( "Bis", true );
	JRadioButton rbAb = new JRadioButton( "Ab", false );
	CurrencyTextField tfPruefbedingung = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	JCheckBox checkPruefbedingung = new JCheckBox();
	JLabel labLimit = new JLabel();
	JLabel labUntertitel = new JLabel();
	IntTextField tfUntertitel = new IntTextField(2);
	JLabel labVormerkungen = new JLabel();
	CurrencyTextField tfVormerkungen = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );

	/**
	 * Erzeugen vom <code>TitelZVKontoTitelPanel</code>.
	 */
	public TitelZVKontoTitelPanel() {
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
		tfKapitel.setText("");
		tfKapitel.setBounds(new Rectangle(102, 47, 80, 21));
		tfBezeichnung.setText("");
		tfBezeichnung.setBounds(new Rectangle(102, 12, 390, 21));
		labBezeichnung.setText("Bezeichnung");
		labBezeichnung.setBounds(new Rectangle(12, 12, 90, 15));
		labKapitel.setText("Kapitel");
		labKapitel.setBounds(new Rectangle(12, 47, 90, 15));
		this.setLayout(null);
		labTitel.setText("Titel");
		labTitel.setBounds(new Rectangle(322, 47, 90, 15));
		labBemerkung.setText("Bemerkung");
		labBemerkung.setBounds(new Rectangle(10, 112, 90, 15));
		labBudget.setText("Budget");
		labBudget.setBounds(new Rectangle(12, 82, 90, 15));
		tfBudget.setBounds(new Rectangle(102, 82, 100, 21));
		tfBemerkung.setText("");
		tfBemerkung.setBounds(new Rectangle(102, 112, 210, 21));
		rbBis.setText("Bis");
		rbBis.setBounds(new Rectangle(202, 142, 60, 22));
		rbAb.setText("Ab");
		rbAb.setBounds(new Rectangle(262, 142, 60, 22));
		tfPruefbedingung.setBounds(new Rectangle(372, 142, 120, 21));
		checkPruefbedingung.setSelected(false);
		checkPruefbedingung.setText("Prüfbedingung");
		checkPruefbedingung.setBounds(new Rectangle(8, 142, 140, 22));
		labLimit.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
		labLimit.setForeground(Color.blue);
		labLimit.setToolTipText("");
		labLimit.setText("Zusätzliche Angaben zum neuem Untertitel");
		labLimit.setBounds(new Rectangle(12, 182, 300, 16));
		labUntertitel.setText("Untertitel");
		labUntertitel.setBounds(new Rectangle(12, 212, 90, 15));
		tfUntertitel.setText("");
		tfUntertitel.setBounds(new Rectangle(102, 212, 80, 21));
		this.setBorder(BorderFactory.createEtchedBorder());
		labVormerkungen.setText("Vormerkungen");
		labVormerkungen.setBounds(new Rectangle(302, 82, 90, 15));
		tfVormerkungen.setBounds(new Rectangle(392, 82, 100, 21));
		tfTitel.setBounds(new Rectangle(412, 47, 80, 21));
		this.add(labBezeichnung, null);
		this.add(tfBezeichnung, null);
		this.add(labKapitel, null);
		this.add(tfKapitel, null);
		this.add(labTitel, null);
		this.add(tfTitel, null);
		this.add(labBudget, null);
		this.add(tfBudget, null);
		this.add(checkPruefbedingung, null);
		this.add(rbBis, null);
		this.add(rbAb, null);
		this.add(tfPruefbedingung, null);
		this.add(labLimit, null);
		this.add(labUntertitel, null);
		this.add(tfUntertitel, null);
		this.add(labBemerkung, null);
		this.add(tfBemerkung, null);
		this.add(labVormerkungen, null);
		this.add(tfVormerkungen, null);
		tfVormerkungen.setEnabled(false);
		
		tfKapitel.setEnabled( false );
		tfBudget.setEnabled( false );
		checkPruefbedingung.addActionListener( this );
		rbBis.addActionListener( this );
		rbAb.addActionListener( this );
		groupPruefbedingung.add( rbBis );
		groupPruefbedingung.add( rbAb );	  
	}
		
	/**
	 * Textfelder mit Daten des ZVTitels fühlen. 
	 * @param zvTitel = Von dem die Daten übernommen werden. 
	 */
	public void setTextfields( ZVTitel zvTitel ){
		if( (this.zvTitel = zvTitel) == null )	// Kein ZVTitel angegeben
			return;
		
		tfBezeichnung.setText( zvTitel.getBezeichnung() );
		tfKapitel.setValue( zvTitel.getZVKonto().getKapitel() );
		tfTitel.setValue( zvTitel.getTitel() );
		tfBemerkung.setText( zvTitel.getBemerkung() );
		tfBudget.setValue( new Float( zvTitel.getBudget() ) );
		checkPruefbedingung.setSelected( zvTitel.isPruefungActive() );
		rbBis.setSelected( zvTitel.isPruefungBis() );
		rbAb.setSelected( !zvTitel.isPruefungBis() );
		tfPruefbedingung.setValue( new Float( zvTitel.getPruefsumme() ) );
		tfUntertitel.setValue( "" );
		tfVormerkungen.setValue( new Float( zvTitel.getVormerkungen() ) );
		
		actionPerformed( new ActionEvent( checkPruefbedingung, 0, "" ) );
	}
	
	/**
	 * Ermittlung der Fehler, die beim Aktualisieren des ZVTitels entstanden sind.
	 * @return String mit Fehlern. Wenn String leer, dann sind keine Fehler aufgetreten. 
	 */
	public String getEditErrorString() {
		if( zvTitel == null )
			return "";
		
		String error = "";
		if( tfBezeichnung.getText().equalsIgnoreCase( "" ) )
			error += " - Die Bezeichnung darf nicht leer sein.\n";
		if( !tfBezeichnung.getText().equalsIgnoreCase( zvTitel.getBezeichnung() ) )
			error += " - Die Bezeichnung bei einem 'Kapitel/Titel'-Konto muss bei dem ZVKonto geändert werden.\n";
		
		return error;
	}
	
	/**
	 * Ermittlung der Fehler, die beim Erstellen eines neuen ZVUntertitels zum angegebenem ZVTitel entstanden sind.
	 * @return String mit Fehlern. Wenn String leer, dann sind keine Fehler aufgetreten. 
	 */	
	public String getAddErrorString() {
		if( zvTitel == null )
			return new String( " - Es wurde kein ZVTitel angegeben.\n" );
		
		String error = "";
		if( tfBezeichnung.getText().equalsIgnoreCase( "" ) )
			error += " - Die Bezeichnung darf nicht leer sein.\n";
		if( !tfTitel.getText().equalsIgnoreCase( zvTitel.getTitel() ) )
			error += " - Der Titel darf nicht verändert werden.\n";
		
		return error;
	}
	
	/**
	 * Einen aktualisierten ZVTitel erstellen.
	 * @return ZVTitel, das aus den Daten des Panels erzeugt wurde. 
	 */
	public ZVTitel getEditZVTitel() {
		if( zvTitel == null )
			return null;
		
		return new ZVTitel( zvTitel.getId(), zvTitel.getZVKonto(), tfBezeichnung.getText(), tfTitel.getText(), "",
						zvTitel.getBudget(), zvTitel.getVormerkungen(), tfBemerkung.getText(),
						ZVTitel.getPruefung( checkPruefbedingung.isSelected(), rbBis.isSelected(), tfPruefbedingung.getValue() ),
						zvTitel.getGeloescht() );
	}
	
	/**
	 * Einen neuen ZVUntertitel zum angegebenem ZVTitel erzeugen.
	 * @return ZVUntertitel, das aus den Daten des Panels erzeugt wurde. 
	 */
	public ZVUntertitel getNewZVUntertitel() {
		if( zvTitel == null )
			return null;
		return new ZVUntertitel( 0, zvTitel, tfBezeichnung.getText(), tfTitel.getText(), tfUntertitel.getText(),
		        		0.0f, 0.0f, tfBemerkung.getText(),
						ZVTitel.getPruefung( checkPruefbedingung.isSelected(), rbBis.isSelected(), tfPruefbedingung.getValue() ),
						false );
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
 * Panel zum Anzeigen aller Textfelder, die gebraucht werden, wenn ein Untertitel-Knoten von einem TitelZVKonto<br>
 * im Baum ausgewählt wurde. Hier kann man den ZVUntertitel aktualisieren.<br>
 * Es werden auch alle Funktion implementiert, die dazu benötigt werden.
 * @author w.flat
 */
class TitelZVKontoUntertitelPanel extends JPanel implements ActionListener {

	ZVUntertitel zvUntertitel;
	JLabel labTitel = new JLabel();
	JLabel labUntertitel = new JLabel();
	JLabel labKapitel = new JLabel();
	JLabel labBemerkung = new JLabel();
	JLabel labBezeichnung = new JLabel();
	JTextField tfBezeichnung = new JTextField();
	IntTextField tfTitel = new IntTextField(5);
	JLabel labBudget = new JLabel();
	IntTextField tfKapitel = new IntTextField(5);
	CurrencyTextField tfBudget = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	CurrencyTextField tfPruefbedingung = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	IntTextField tfUntertitel = new IntTextField(2);
	JCheckBox checkPruefbedingung = new JCheckBox();
	JTextField tfBemerkung = new JTextField();
	ButtonGroup groupPruefbedingung = new ButtonGroup();
	JRadioButton rbBis = new JRadioButton( "Bis", true );
	JRadioButton rbAb = new JRadioButton( "Ab", false );
	JLabel labVormerkungen = new JLabel();
	CurrencyTextField tfVormerkungen = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );

	/**
	 * Erzeugen vom <code>TitelZVKontoUntertitelPanel</code>.
	 */
	public TitelZVKontoUntertitelPanel() {
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
		tfBemerkung.setText("");
		tfBemerkung.setBounds(new Rectangle(102, 112, 210, 21));
		checkPruefbedingung.setText("Prüfbedingung");
		checkPruefbedingung.setBounds(new Rectangle(12, 142, 140, 22));
		checkPruefbedingung.setSelected(false);
		tfUntertitel.setText("");
		tfUntertitel.setBounds(new Rectangle(412, 47, 80, 21));
		tfPruefbedingung.setBounds(new Rectangle(372, 142, 120, 21));
		tfBudget.setBounds(new Rectangle(102, 82, 100, 21));
		tfKapitel.setText("");
		tfKapitel.setBounds(new Rectangle(412, 12, 80, 21));
		rbAb.setText("Ab");
		rbAb.setBounds(new Rectangle(282, 142, 60, 22));
		labBudget.setText("Budget");
		labBudget.setBounds(new Rectangle(12, 82, 90, 15));
		tfBezeichnung.setText("");
		tfBezeichnung.setBounds(new Rectangle(102, 12, 210, 21));
		labBezeichnung.setText("Bezeichnung");
		labBezeichnung.setBounds(new Rectangle(12, 12, 90, 15));
		labBemerkung.setText("Bemerkung");
		labBemerkung.setBounds(new Rectangle(12, 112, 80, 15));
		labKapitel.setText("Kapitel");
		labKapitel.setBounds(new Rectangle(322, 12, 90, 15));
		labUntertitel.setText("Untertitel");
		labUntertitel.setBounds(new Rectangle(322, 47, 90, 15));
		labTitel.setText("Titel");
		labTitel.setBounds(new Rectangle(12, 47, 90, 15));
		rbBis.setText("Bis");
		rbBis.setBounds(new Rectangle(202, 142, 60, 22));
		this.setBorder(BorderFactory.createEtchedBorder());
		this.setLayout(null);
		labVormerkungen.setText("Vormerkungen");
		labVormerkungen.setBounds(new Rectangle(292, 82, 90, 15));
		tfVormerkungen.setBounds(new Rectangle(392, 82, 100, 21));
		tfTitel.setBounds(new Rectangle(102, 47, 80, 21));
		this.add(labBezeichnung, null);
		this.add(tfBezeichnung, null);
		this.add(tfKapitel, null);
		this.add(labKapitel, null);
		this.add(labTitel, null);
		this.add(tfTitel, null);
		this.add(labUntertitel, null);
		this.add(tfUntertitel, null);
		this.add(labBudget, null);
		this.add(tfBudget, null);
		this.add(checkPruefbedingung, null);
		this.add(rbBis, null);
		this.add(rbAb, null);
		this.add(tfPruefbedingung, null);
		this.add(labBemerkung, null);
		this.add(tfBemerkung, null);
		this.add(labVormerkungen, null);
		this.add(tfVormerkungen, null);
		tfVormerkungen.setEnabled(false);
		
		tfKapitel.setEnabled( false );
		tfTitel.setEnabled( false );
		tfBudget.setEnabled( false );
		checkPruefbedingung.addActionListener( this );
		rbBis.addActionListener( this );
		rbAb.addActionListener( this );
		groupPruefbedingung.add( rbBis );
		groupPruefbedingung.add( rbAb );
	}
	
	/**
	 * Textfelder mit Daten des ZVUntertitels fühlen.
	 * @param zvUntertitel = ZVUNtertitel, von dem die Daten übernommen werden. 
	 */
	public void setTextfields( ZVUntertitel zvUntertitel ){
		if( (this.zvUntertitel = zvUntertitel) == null )	// Kein ZVTitel angegeben
			return;
		
		tfBezeichnung.setText( zvUntertitel.getBezeichnung() );
		tfKapitel.setValue( zvUntertitel.getZVTitel().getZVKonto().getKapitel() );
		tfTitel.setValue( zvUntertitel.getTitel() );
		tfUntertitel.setValue( zvUntertitel.getUntertitel() );
		tfBemerkung.setText( zvUntertitel.getBemerkung() );
		tfBudget.setValue( new Float( zvUntertitel.getBudget() ) );
		checkPruefbedingung.setSelected( zvUntertitel.isPruefungActive() );
		rbBis.setSelected( zvUntertitel.isPruefungBis() );
		rbAb.setSelected( !zvUntertitel.isPruefungBis() );
		tfPruefbedingung.setValue( new Float( zvUntertitel.getPruefsumme() ) );
		tfVormerkungen.setValue(new Float(zvUntertitel.getVormerkungen()));
		
		actionPerformed( new ActionEvent( checkPruefbedingung, 0, "" ) );
	}
	
	/**
	 * Ermittlung der Fehler, die beim Aktualisieren des ZVUntertitels entstanden sind.
	 * @return String mit Fehlern. Wenn String leer, dann sind keine Fehler aufgetreten. 
	 */
	public String getEditErrorString() {
		if( zvUntertitel == null )
			return new String( " - Es wurde kein ZVUntertitel angegeben.\n" );
		
		String error = "";
		if( tfBezeichnung.getText().equalsIgnoreCase( "" ) )
			error += " - Die Bezeichnung darf nicht leer sein.\n";
		
		return error; 
	}
	
	/**
	 * Erstellen des aktualisierten ZVUntertitels. 
	 * @return ZVUntertitel, das aus den Daten des Panels erzeugt wurde. 
	 */
	public ZVUntertitel getEditZVUntertitel() {
		if( zvUntertitel == null )
			return null;
		
		return new ZVUntertitel( zvUntertitel.getId(), zvUntertitel.getZVTitel(), tfBezeichnung.getText(),
					zvUntertitel.getZVTitel().getTitel(), tfUntertitel.getText(), zvUntertitel.getBudget(),
					zvUntertitel.getVormerkungen(), tfBemerkung.getText(),
					ZVTitel.getPruefung( checkPruefbedingung.isSelected(), rbBis.isSelected(), tfPruefbedingung.getValue() ), 
					zvUntertitel.getGeloescht() );

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
 * Panel zum Anzeigen aller Textfelder, die gebraucht werden, wenn ein Titel-Knoten <br>
 * von einem TGRZVKonto im Baum ausgewählt wurde.<br>
 * Hier kann man den vorhandenen ZVTitel aktualisieren und einen ZVUntertitel zum ZVTitel erstellen.<br>
 * Es werden auch alle Funktion implementiert, die dazu benötigt werden.
 * @author w.flat
 */
class TGRZVKontoTitelPanel extends JPanel implements ActionListener {

	ZVTitel zvTitel;
	JLabel labTGR = new JLabel();
	JLabel labKapitel = new JLabel();
	JLabel labBezeichnung = new JLabel();
	JTextField tfBezeichnung = new JTextField();
	IntTextField tfKapitel = new IntTextField(5);
	IntTextField tfKategorie = new IntTextField(3);
	IntTextField tfTitelgruppe = new IntTextField(2);
	JLabel labKategorie = new JLabel();
	JLabel labBudget = new JLabel();
	CurrencyTextField tfBudget = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	JLabel labBemerkung = new JLabel();
	JTextField tfBemerkung = new JTextField();
	ButtonGroup groupPruefbedingung = new ButtonGroup();
	JRadioButton rbBis = new JRadioButton( "Bis", true );
	JRadioButton rbAb = new JRadioButton( "Ab", false );
	CurrencyTextField tfPruefbedingung = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	JCheckBox checkPruefbedingung = new JCheckBox();
	JLabel labLimit = new JLabel();
	JLabel labUntertitel = new JLabel();
	IntTextField tfUntertitel = new IntTextField(2);
	JLabel labVormerkungen = new JLabel();
	CurrencyTextField tfVormerkungen = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );

	/**
	 * Erzeugen vom <code>TGRZVKontoTitelPanel</code>.
	 */	
	public TGRZVKontoTitelPanel() {
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
		tfKapitel.setText("");
		tfKapitel.setBounds(new Rectangle(412, 12, 80, 21));
		tfTitelgruppe.setText("");
		tfTitelgruppe.setBounds(new Rectangle(412, 47, 80, 21));
		tfBezeichnung.setText("");
		tfBezeichnung.setBounds(new Rectangle(102, 12, 210, 21));
		labBezeichnung.setText("Bezeichnung");
		labBezeichnung.setBounds(new Rectangle(12, 12, 90, 15));
		labKapitel.setText("Kapitel");
		labKapitel.setBounds(new Rectangle(322, 12, 90, 15));
		labTGR.setText("Titelgruppe");
		labTGR.setBounds(new Rectangle(322, 47, 90, 15));
		this.setLayout(null);
		tfKategorie.setText("");
		tfKategorie.setBounds(new Rectangle(102, 47, 80, 21));
		labKategorie.setText("Kategorie");
		labKategorie.setBounds(new Rectangle(12, 47, 90, 15));
		labBudget.setText("Budget");
		labBudget.setBounds(new Rectangle(12, 82, 90, 15));
		tfBudget.setBounds(new Rectangle(102, 82, 100, 21));
		this.setBorder(BorderFactory.createEtchedBorder());
		this.setOpaque(true);
		labBemerkung.setText("Bemerkung");
		labBemerkung.setBounds(new Rectangle(12, 112, 80, 15));
		tfBemerkung.setText("");
		tfBemerkung.setBounds(new Rectangle(102, 112, 210, 21));
		rbBis.setText("Bis");
		rbBis.setBounds(new Rectangle(202, 142, 60, 22));
		rbAb.setText("Ab");
		rbAb.setBounds(new Rectangle(262, 142, 60, 22));
		tfPruefbedingung.setBounds(new Rectangle(372, 142, 120, 21));
		checkPruefbedingung.setSelected(false);
		checkPruefbedingung.setText("Prüfbedingung");
		checkPruefbedingung.setBounds(new Rectangle(12, 142, 140, 22));
		labLimit.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
		labLimit.setForeground(Color.blue);
		labLimit.setText("Zusätzliche Angaben zum neuem Untertitel");
		labLimit.setBounds(new Rectangle(12, 182, 300, 16));
		labUntertitel.setText("Untertitel");
		labUntertitel.setBounds(new Rectangle(12, 212, 90, 15));
		tfUntertitel.setText("");
		tfUntertitel.setBounds(new Rectangle(102, 212, 80, 21));
		labVormerkungen.setText("Vormerkungen");
		labVormerkungen.setBounds(new Rectangle(292, 82, 90, 15));
		tfVormerkungen.setBounds(new Rectangle(392, 82, 100, 21));
		this.add(labTGR, null);
		this.add(labKapitel, null);
		this.add(labBezeichnung, null);
		this.add(tfBezeichnung, null);
		this.add(tfTitelgruppe, null);
		this.add(tfKapitel, null);
		this.add(tfKategorie, null);
		this.add(labKategorie, null);
		this.add(labBudget, null);
		this.add(tfBudget, null);
		this.add(labBemerkung, null);
		this.add(tfBemerkung, null);
		this.add(rbBis, null);
		this.add(rbAb, null);
		this.add(tfPruefbedingung, null);
		this.add(checkPruefbedingung, null);
		this.add(labLimit, null);
		this.add(labUntertitel, null);
		this.add(tfUntertitel, null);
		this.add(labVormerkungen, null);
		this.add(tfVormerkungen, null);
		tfVormerkungen.setEnabled(false);
		
		groupPruefbedingung.add( rbBis );
		groupPruefbedingung.add( rbAb );
		tfKapitel.setEnabled( false );
		tfTitelgruppe.setEnabled( false );
		tfBudget.setEnabled( false );
		checkPruefbedingung.addActionListener( this );
		rbBis.addActionListener( this );
		rbAb.addActionListener( this );
	}
	
	/**
	 * Die Textfelder mit den Daten des angegebenen ZVTitels fühlen.
	 * @param zvTitel = ZVTitel, von dem die Daten übernommen werden. 
	 */
	public void setTextfields( ZVTitel zvTitel ){
		if( (this.zvTitel = zvTitel) == null )
			return;
		
		tfBezeichnung.setText( zvTitel.getBezeichnung() );
		tfKapitel.setValue( zvTitel.getZVKonto().getKapitel() );
		tfKategorie.setValue( zvTitel.getKategorie() );
		tfTitelgruppe.setValue( zvTitel.getTGR() );
		tfBemerkung.setText( zvTitel.getBemerkung() );
		tfBudget.setValue( new Float( zvTitel.getBudget() ) );
		checkPruefbedingung.setSelected( zvTitel.isPruefungActive() );
		rbBis.setSelected( zvTitel.isPruefungBis() );
		rbAb.setSelected( !zvTitel.isPruefungBis() );
		tfPruefbedingung.setValue( new Float( zvTitel.getPruefsumme() ) );
		tfUntertitel.setValue( "" );
		tfVormerkungen.setValue( new Float( zvTitel.getVormerkungen() ) );
		
		actionPerformed( new ActionEvent( checkPruefbedingung, 0, "" ) );
	}
	
	/**
	 * Ermittlung der Fehler beim Aktualisieren des vorhandenen ZVTitels entstanden sind
	 * @return String mit Fehlern. Wenn String leer, dann sind keine Fehler aufgetreten. 
	 */
	public String getEditErrorString() {
		String error = "";
		if( tfBezeichnung.getText().equalsIgnoreCase( "" ) )
			error += " - Die Bezeichnung darf nicht leer sein.\n";
		
		return error;
	}
	
	/**
	 * Ermittlung der Fehler beim eines neuen ZVUntertitels zum vorhandenen ZVTitel entstanden sind
	 * @return String mit Fehlern. Wenn String leer, dann sind keine Fehler aufgetreten. 
	 */
	public String getAddErrorString() {
		String error = "";
		if( tfBezeichnung.getText().equalsIgnoreCase( "" ) )
			error += " - Die Bezeichnung darf nicht leer sein.\n";
		if( !tfKategorie.getText().equalsIgnoreCase( zvTitel.getKategorie() ) )
			error += " - Die Kategorie darf nicht verändert werden.\n";
		
		return error;
	}
	
	/**
	 * Erstellen von einem aktualisiertem ZVTitel.
	 * @return ZVTitel, der aus den Daten des Panels erzeugt wurde. 
	 */
	public ZVTitel getEditZVTitel() {
		if( zvTitel == null )
			return null;
		
		return new ZVTitel( zvTitel.getId(), zvTitel.getZVKonto(), tfBezeichnung.getText(),
						tfKategorie.getText() + tfTitelgruppe.getText() , "", zvTitel.getBudget(),
						zvTitel.getVormerkungen(), tfBemerkung.getText(),
						ZVTitel.getPruefung( checkPruefbedingung.isSelected(), rbBis.isSelected(), tfPruefbedingung.getValue() ), 
						zvTitel.getGeloescht() );
	}
	
	/**
	 * Einen neuen ZVUntertitel zum angegebenem ZVTitel erzeugen. 
	 * @return ZVUntertitel, der aus den Daten des Panels erzeugt wurde. 
	 */
	public ZVUntertitel getNewZVUntertitel() {
		if( zvTitel == null )
			return null;
		
		return new ZVUntertitel( 0, zvTitel, tfBezeichnung.getText(), tfKategorie.getText() + tfTitelgruppe.getText(),
						tfUntertitel.getText(),	0.0f, 0.0f, tfBemerkung.getText(),
						ZVTitel.getPruefung( checkPruefbedingung.isSelected(), rbBis.isSelected(), tfPruefbedingung.getValue() ), 
						false );
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
 * Panel zum Anzeigen aller Textfelder, die gebraucht werden wenn ein Untertitel-Knoten von einem TGRKonto<br>
 * im Baum ausgewählt wurde. Hier kann man den Untertitel aktualisieren. <br>
 * Es werden auch alle Funktion implementiert, die dazu benötigt werden.
 * @author w.flat
 */
class TGRZVKontoUntertitelPanel extends JPanel implements ActionListener {

	ZVUntertitel zvUntertitel;
	IntTextField tfKategorie = new IntTextField(3);
	JLabel labTGR = new JLabel();
	JLabel labKapitel = new JLabel();
	JLabel labBemerkung = new JLabel();
	JLabel labBezeichnung = new JLabel();
	JTextField tfBezeichnung = new JTextField();
	JLabel labBudget = new JLabel();
	IntTextField tfTitelgruppe = new IntTextField(2);
	IntTextField tfKapitel = new IntTextField(5);
	CurrencyTextField tfBudget = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	JLabel labKategorie = new JLabel();
	CurrencyTextField tfPruefbedingung = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );
	JCheckBox checkPruefbedingung = new JCheckBox();
	ButtonGroup groupPruefbedingung = new ButtonGroup();
	JRadioButton rbBis = new JRadioButton( "Bis", true );
	JRadioButton rbAb = new JRadioButton( "Ab", false );
	JTextField tfBemerkung = new JTextField();
	JLabel labUntertitel = new JLabel();
	IntTextField tfUntertitel = new IntTextField(2);
	JLabel labVormerkungen = new JLabel();
	CurrencyTextField tfVormerkungen = new CurrencyTextField( Integer.MIN_VALUE, Integer.MAX_VALUE );

	/**
	 * Erzeugen vom <code>TGRZVKontoUntertitelPanel</code>.
	 */
	public TGRZVKontoUntertitelPanel() {
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
		tfBemerkung.setText("");
		tfBemerkung.setBounds(new Rectangle(102, 112, 210, 21));
		checkPruefbedingung.setText("Prüfbedingung");
		checkPruefbedingung.setBounds(new Rectangle(12, 142, 140, 22));
		checkPruefbedingung.setSelected(false);
		tfPruefbedingung.setBounds(new Rectangle(372, 142, 120, 21));
		labKategorie.setText("Kategorie");
		labKategorie.setBounds(new Rectangle(12, 47, 80, 15));
		tfBudget.setBounds(new Rectangle(92, 82, 100, 21));
		tfKapitel.setText("");
		tfKapitel.setBounds(new Rectangle(412, 12, 80, 21));
		rbAb.setText("Ab");
		rbAb.setBounds(new Rectangle(262, 142, 60, 22));
		tfTitelgruppe.setText("");
		tfTitelgruppe.setBounds(new Rectangle(257, 47, 70, 21));
		labBudget.setText("Budget");
		labBudget.setBounds(new Rectangle(12, 82, 80, 15));
		tfBezeichnung.setText("");
		tfBezeichnung.setBounds(new Rectangle(102, 12, 210, 21));
		labBezeichnung.setText("Bezeichnung");
		labBezeichnung.setBounds(new Rectangle(12, 12, 90, 15));
		labBemerkung.setText("Bemerkung");
		labBemerkung.setBounds(new Rectangle(12, 112, 90, 15));
		labKapitel.setText("Kapitel");
		labKapitel.setBounds(new Rectangle(322, 12, 90, 15));
		labTGR.setText("Titelgruppe");
		labTGR.setBounds(new Rectangle(177, 47, 80, 15));
		tfKategorie.setText("");
		tfKategorie.setBounds(new Rectangle(92, 47, 70, 21));
		rbBis.setText("Bis");
		rbBis.setBounds(new Rectangle(202, 142, 60, 22));
		this.setLayout(null);
		labUntertitel.setText("Untertitel");
		labUntertitel.setBounds(new Rectangle(342, 49, 80, 15));
		tfUntertitel.setText("");
		tfUntertitel.setBounds(new Rectangle(422, 47, 70, 21));
		this.setBorder(BorderFactory.createEtchedBorder());
		labVormerkungen.setText("Vormerkungen");
		labVormerkungen.setBounds(new Rectangle(302, 82, 90, 15));
		tfVormerkungen.setBounds(new Rectangle(392, 82, 100, 21));
		this.add(rbBis, null);
		this.add(tfKategorie, null);
		this.add(labTGR, null);
		this.add(labKapitel, null);
		this.add(labBemerkung, null);
		this.add(labBezeichnung, null);
		this.add(tfBezeichnung, null);
		this.add(labBudget, null);
		this.add(tfTitelgruppe, null);
		this.add(rbAb, null);
		this.add(tfKapitel, null);
		this.add(tfBudget, null);
		this.add(labKategorie, null);
		this.add(tfPruefbedingung, null);
		this.add(checkPruefbedingung, null);
		this.add(tfBemerkung, null);
		this.add(labUntertitel, null);
		this.add(tfUntertitel, null);
		this.add(labVormerkungen, null);
		this.add(tfVormerkungen, null);
		tfVormerkungen.setEnabled(false);
		
		tfKapitel.setEnabled( false );
		tfKategorie.setEnabled( false );
		tfTitelgruppe.setEnabled( false );
		tfBudget.setEnabled( false );
		checkPruefbedingung.addActionListener( this );
		rbBis.addActionListener( this );
		rbAb.addActionListener( this );
		groupPruefbedingung.add( rbBis );
		groupPruefbedingung.add( rbAb );	  
	}
	
	/**
	 * Die Textfelder mit den Daten des übergebenden ZVUntertitels fühlen. 
	 * @param zvUntertitel = ZVUntertitel, von dem die Daten übernommen werden.
	 */
	public void setTextfields( ZVUntertitel zvUntertitel ){
		if( (this.zvUntertitel = zvUntertitel) == null )
			return;
		
		tfBezeichnung.setText( zvUntertitel.getBezeichnung() );
		tfKapitel.setValue( zvUntertitel.getZVTitel().getZVKonto().getKapitel() );
		tfKategorie.setValue( zvUntertitel.getZVTitel().getKategorie() );
		tfTitelgruppe.setValue( zvUntertitel.getZVTitel().getTGR() );
		tfUntertitel.setValue( zvUntertitel.getUntertitel() );
		tfBemerkung.setText( zvUntertitel.getBemerkung() );
		tfBudget.setValue( new Float( zvUntertitel.getBudget() ) );
		checkPruefbedingung.setSelected( zvUntertitel.isPruefungActive() );
		rbBis.setSelected( zvUntertitel.isPruefungBis() );
		rbAb.setSelected( !zvUntertitel.isPruefungBis() ); 
		tfPruefbedingung.setValue( new Float( zvUntertitel.getPruefsumme() ) );
		tfVormerkungen.setValue( new Float( zvUntertitel.getVormerkungen() ) );
		
		actionPerformed( new ActionEvent( checkPruefbedingung, 0, "" ) );
	}
	
	/**
	 * Ermittlung der Fehler, die beim Aktualisieren des ZVUntertitels entstanden sind.
	 * @return String mit Fehlern. Wenn String leer, dann sind keine Fehler aufgetreten. 
	 */
	public String getEditErrorString() {
		if( zvUntertitel == null )
			return new String( " - Es wurde kein ZVUntertitel angegeben.\n" );
			
		String error = "";
		if( tfBezeichnung.getText().equalsIgnoreCase( "" ) )
			error += " - Die Bezeichnung darf nicht leer sein.\n";
		
		return error;
	}
	
	/**
	 * Erstellen des aktualisierten ZVUntertitels.
	 * @return ZVUntertitel, das aus den Daten des Panels erzeugt wurde. 
	 */
	public ZVUntertitel getEditZVUntertitel() {
		if( zvUntertitel == null )
			return null;
			
		return new ZVUntertitel( zvUntertitel.getId(), zvUntertitel.getZVTitel(), tfBezeichnung.getText(),
					zvUntertitel.getZVTitel().getTitel(), tfUntertitel.getText(), zvUntertitel.getBudget(), 
					zvUntertitel.getVormerkungen(), tfBemerkung.getText(),
					ZVTitel.getPruefung( checkPruefbedingung.isSelected(), rbBis.isSelected(), tfPruefbedingung.getValue() ), 
					zvUntertitel.getGeloescht() );
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
