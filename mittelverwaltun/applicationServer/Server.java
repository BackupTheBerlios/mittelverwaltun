package applicationServer;

import gui.Functions;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.rmi.Naming;

public class Server extends JFrame implements ActionListener {
	
	Process rmiProcess = null;
	CentralServerImpl centralServer = null;


	String rmiregistry = "C:\\j2sdk1.4.0\\bin\\rmiregistry.exe ";
	String classpath = "-J-classpath -J\"C:\\j2sdk1.4.0\\lib";
	final int delay = 3000;
	JScrollPane scrollList = new JScrollPane();
	DefaultListModel listModel = new DefaultListModel();
	JList listUser = new JList(listModel);
	JButton buRegistry = new JButton();
	JButton buServer = new JButton();
	JButton buDelUser = new JButton();
	StatusBar statusBar = new StatusBar();
	JButton buClose = new JButton();

	
	int userCount = 1;
	
	public Server() {
		super("Central Server");
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jbInit() throws Exception {
		this.getContentPane().setLayout(null);
		buRegistry.setBounds(new Rectangle(320, 10, 200, 25));
		buRegistry.setText("Start Registry");
		buServer.setBounds(new Rectangle(320, 52, 200, 25));
		buServer.setText("Server registrieren");
		buDelUser.setBounds(new Rectangle(320, 93, 200, 25));
		buDelUser.setText("Benutzer entfernen");
		buClose.setBounds(new Rectangle(320, 135, 200, 25));
		buClose.setText("Server beenden");
		statusBar.setBounds(new Rectangle(5, 170, 520, 26));
		this.setLocale(java.util.Locale.getDefault());
		scrollList.setBounds(new Rectangle(10, 10, 300, 150));
		this.getContentPane().add(scrollList, null);
		scrollList.getViewport().add(listUser, null);
		listUser.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		// Button Registry
		this.getContentPane().add(buRegistry, null);
		buRegistry.addActionListener( this );
		buRegistry.setIcon(Functions.getWebIcon(getClass()));
		// Button Server
		this.getContentPane().add(buServer, null);
		buServer.addActionListener( this );
		buServer.setEnabled( false );
		buServer.setIcon(Functions.getServerIcon(getClass()));
		// Button Benutzer löschen
		this.getContentPane().add(buDelUser, null);
		buDelUser.addActionListener( this );
		buDelUser.setIcon(Functions.getDelIcon(getClass()));
		// Button Beenden
		this.getContentPane().add(buClose, null);
		buClose.addActionListener( this );
		buClose.setIcon(Functions.getCloseIcon(getClass()));
		this.getContentPane().add(statusBar, null);
		
		this.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
		this.setResizable( false );
		this.setBounds( 100, 100, 538, 228 );
		this.setVisible( true );
		
		this.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Server server = (Server)e.getSource();
				server.actionPerformed( new ActionEvent( server.getCloseButton(), 0, "" ) );
			}
		} );
	}
	
	/**
	 * Einen neuen ApplicationServer generieren.
	 * @param hostName
	 * @param hostAdress
	 * @return ApplicationServerImpl
	 */
	public ApplicationServerImpl getNewApplicationServer(  String hostName, String hostAdress  ) {
		User user = new User( ++userCount, hostName, hostAdress );
		listModel.addElement( user );
		statusBar.showTextForMilliseconds( "Neuer User @" + hostAdress + " hat sich angemeldet.", delay );
		
		return user.getApplicationServer();
	}
	
	/**
	 * Den Benutzer-Namen zum User hinzufügen, da man beim Anlegen eines Users noch nicht weiß, wer sich anmelden wird.
	 * @param id
	 * @param benutzerName
	 */
	public void addBenutzerNameToUser( int id, String benutzerName ) {
		User temp;
		for( int i = 0; i < listModel.size(); i++ ) {
			temp = (User)listModel.getElementAt( i );
			if( temp.getId() == id ) {
				temp.setBenutzer( benutzerName );
				listModel.setElementAt( temp, i );
				statusBar.showTextForMilliseconds( "Der User @" + temp.getHostAdress() + " hat seinen Namen übermittelt.", delay );
				break;
			}
		}
	}
	
	/**
	 * Einen User aus der ListBox entfernen
	 * @param id des Users
	 */
	public void delUser( int id ) {
		User temp;
		for( int i = 0; i < listModel.size(); i++ ) {
			temp = (User)listModel.getElementAt( i );
			if( temp.getId() == id ) {
				temp.logout();
				listModel.removeElementAt( i );
				statusBar.showTextForMilliseconds( "Der User '" + temp.getBenutzer() + "' hat sich abgemeldet.", delay );
				break;
			}
		}
	}
	
	public static void main(String args[]){
		new Server();
	}
	
	public JButton getCloseButton() {
		return buClose; 
	}
	
		
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == buRegistry ) {
			if( rmiProcess == null && centralServer == null  ){
				try {
					rmiProcess = Runtime.getRuntime().exec(rmiregistry + " " + classpath);
					buRegistry.setText( "Stop Registry" );
					buServer.setEnabled( true );
					statusBar.showTextForMilliseconds( "RMI-Registry wurde gestartet.", delay );
				} catch (IOException e1) {
					e1.printStackTrace();
					rmiProcess = null;
				}
			} else if( rmiProcess != null && centralServer == null  ) {
				rmiProcess.destroy();
				rmiProcess = null;
				statusBar.showTextForMilliseconds( "RMI-Registry wurde gestopt.", delay );
				buRegistry.setText( "Start Registry" );
				buServer.setEnabled( false );
			} else if( rmiProcess != null && centralServer != null ) {
				JOptionPane.showMessageDialog( this, "Sie müssen zuerst den Server aus der Registry entfernen !",
														"Fehler !", JOptionPane.ERROR_MESSAGE );
			}
		} else if( e.getSource() == buServer ) {
			if( rmiProcess == null && centralServer == null  ){
				JOptionPane.showMessageDialog( this, "Sie müssen zuerst Registry starten !",
														"Fehler !", JOptionPane.ERROR_MESSAGE );
			} else 	if( rmiProcess != null && centralServer == null  ){
				try {
					Naming.rebind("mittelverwaltung", centralServer = new CentralServerImpl( this ));
					buRegistry.setEnabled( false );
					buServer.setText( "Server entfernen" );
					statusBar.showTextForMilliseconds( "Server wurde registriert.", delay );
				} catch(Exception ex) {
					ex.printStackTrace();
					centralServer = null;
				}
			} else 	if( rmiProcess != null && centralServer != null  ){
				try {
					Naming.unbind( "mittelverwaltung" );
					centralServer = null;
					buRegistry.setEnabled( true );
					buServer.setText( "Server registrieren" );
					statusBar.showTextForMilliseconds( "Server wurde entfernt.", delay );
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		} else if( e.getSource() == buDelUser ) {
			if( listUser.getSelectedIndex() >= 0 ) {
				User temp = (User)listModel.getElementAt( listUser.getSelectedIndex() );
				if( JOptionPane.showConfirmDialog( this, "Wollen Sie den User '" + temp.getBenutzer() + 
														"' wirklich entfernen ?", "Entfernen ?", JOptionPane.YES_NO_OPTION,
														JOptionPane.QUESTION_MESSAGE  ) == JOptionPane.YES_OPTION ){
					temp.logout();
					listModel.removeElementAt( listUser.getSelectedIndex() );
					statusBar.showTextForMilliseconds( "Der User '" + temp.getBenutzer() + "' wurde entfernt.", delay );
				}
			}
		} else if( e.getSource() == buClose ) {
			int result = JOptionPane.YES_OPTION;
			if( listModel.size() > 0 ) {
				 result = JOptionPane.showConfirmDialog( this, 	"Es sind noch User angemeldet.\n" + 
																"Wollen Sie wirklich beenden ?",
																"Beenden ?", JOptionPane.YES_NO_OPTION,
																JOptionPane.QUESTION_MESSAGE  );
			}
			if( result == JOptionPane.NO_OPTION )
				return;
			for( int i = 0; i < listModel.size(); i++ )
				((User)listModel.getElementAt( i )).logout();
			if( rmiProcess != null ) {
				rmiProcess.destroy();
			}
			centralServer = null;
			this.dispose();
			System.exit( 0 );
		}
	}
}

/**
 * Object der zum Anzeigen in der JList verwendet wird. Es wird der Host-Name, die Host-Adresse,
 * der Benutzer und der ApplicationServer gespeichert. Und die toString-Methode überlagert. 
 */
class User{
	String hostName = null;
	String hostAdress = null;
	String benutzer = null;
	int id;

	/**
	 * 
	 * @uml.property name="applicationServer"
	 * @uml.associationEnd 
	 * @uml.property name="applicationServer"
	 */
	ApplicationServerImpl applicationServer = null;

	
	public User( int id, String hostName, String hostAdress ) {
		this.id = id;
		this.hostName = hostName;
		this.hostAdress = hostAdress;
		applicationServer = new ApplicationServerImpl();
		applicationServer.setId( id );
	}

	/**
	 * 
	 * @uml.property name="applicationServer"
	 */
	public ApplicationServerImpl getApplicationServer() {
		return applicationServer;
	}

	/**
	 * 
	 * @uml.property name="benutzer"
	 */
	public void setBenutzer(String benutzer) {
		this.benutzer = benutzer;
	}

	
	public void logout() {
//		try {
//			applicationServer.logout();
//		} catch (ConnectionException e) {
//		}
	}

	/**
	 * 
	 * @uml.property name="hostAdress"
	 */
	public String getHostAdress() {
		return hostAdress;
	}

	/**
	 * 
	 * @uml.property name="benutzer"
	 */
	public String getBenutzer() {
		if (benutzer != null)
			return benutzer;
		else
			return hostName;
	}

	/**
	 * 
	 * @uml.property name="id"
	 */
	public int getId() {
		return id;
	}

	
	public String toString() {
		if( benutzer == null ) {
			return "user @ " + hostAdress + " (" + hostName + ")";
		} else {
			return benutzer + " @ " + hostAdress + " (" + hostName + ")";
		}
	}
}


/**
 * Anzeigen eines am unterem Rand eines Fensters, die Größe wird im Fenster geregelt.
 * Man kann den Text einfach anzeigen oder für eine bestimmte Zeit(Millisekunden) anzeigen lassen.
 */
class StatusBar extends JPanel implements ActionListener {

	/**
	 * 
	 * @uml.property name="labText"
	 * @uml.associationEnd 
	 * @uml.property name="labText"
	 */
	JLabel labText = new JLabel("");

	/**
	 * 
	 * @uml.property name="timer"
	 * @uml.associationEnd 
	 * @uml.property name="timer"
	 */
	Timer timer = new Timer(1000, this);

	
	public StatusBar() {
		super();
		this.setBorder( BorderFactory.createLoweredBevelBorder() );
		this.setLayout( new FlowLayout( FlowLayout.LEFT ) );
		this.setColor( Color.blue );
		this.add( labText );
	}
	
	/**
	 * Einen Text anzeigen. Bleibt bestehen bis man ihn überschreibt.
	 * @param text
	 */
	public void setText( String text ) {
		labText.setText( text );
	}
	
	/**
	 * Die Farbe des Textes Bestimmen. Es werden die Farben der Klasse Color verwendet. 
	 * @param color
	 */
	public void setColor( Color color ) {
		labText.setForeground( color );
	}
	
	/**
	 * Einen für eine bestimmte Zeit anzeigen
	 * @param text
	 * @param milliseconds
	 */
	public void showTextForMilliseconds( String text, int milliseconds ) {
		if( timer.isRunning() ) {
			timer.stop();
		}
		timer.setInitialDelay( milliseconds );
		labText.setText( text );
		timer.start();
	}
	
	/**
	 * Hier wird der Timer ausgeschaltet und der Text gelöscht.
	 */
	public void actionPerformed(ActionEvent e) {
		timer.stop();
		labText.setText( "" );
	}
}

