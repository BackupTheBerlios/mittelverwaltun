package applicationServer;

import gui.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.rmi.*;
import com.gc.systray.*;

public class Server extends JFrame implements ActionListener, SystemTrayIconListener {
	
	Process rmiProcess = null;
	CentralServerImpl centralServer = null;

	String rmiregistry = "C:\\j2sdk1.4.2\\bin\\rmiregistry.exe ";
	String classpath = "-J-classpath -J\"C:\\j2sdk1.4.2\\lib";

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
	
	// SystemTray Komponenten
	JPopupMenu sysTrayMenu = new JPopupMenu();
	JMenuItem miRegistry = new JMenuItem();
	JMenuItem miServer = new JMenuItem();
	JMenuItem miClose = new JMenuItem();
	SystemTrayIconManager sysTrayMgr;
	int icons[] = new int[3];
	int presIcon;
	String presToolTip = "FB-Mittelverwaltung\nFH-Mannheim SS 2005";
	
	
	public Server() {
		super("Central Server");
		try {
			jbInit();
			initSysTray();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		Server server = new Server();
		server.synchronize();
	}

	/**
	 * SystemTrayIconListener implementation
	 */
	public void mouseClickedLeftButton(Point pos, SystemTrayIconManager source) {
	}
	public void mouseClickedRightButton(Point pos, SystemTrayIconManager ssource) {
	}
	public void mouseLeftDoubleClicked(Point pos, SystemTrayIconManager source) {
		sysTrayMgr.setVisible(false);
		this.setVisible(true);
		this.setExtendedState(Frame.NORMAL);
		this.toFront();
	}
	public void mouseRightDoubleClicked(Point pos, SystemTrayIconManager source) {
	}
	
	private void initSysTray() throws Exception {
		// MenuItem Registry
		sysTrayMenu.add(miRegistry);
		miRegistry.setText(buRegistry.getText());
		miRegistry.addActionListener( this );
		miRegistry.setIcon(Functions.getWebIcon(getClass()));
		// MenuItem Server
		sysTrayMenu.add(miServer);
		miServer.setText(buServer.getText());
		miServer.addActionListener( this );
		miServer.setEnabled( false );
		miServer.setIcon(Functions.getServerIcon(getClass()));
		// MenuItem Close
		sysTrayMenu.add(miClose);
		miClose.setText(buClose.getText());
		miClose.addActionListener( this );
		miClose.setIcon(Functions.getCloseIcon(getClass()));

		if (!SystemTrayIconManager.initializeSystemDependent(this.getClass())) {
			throw new Exception("No DesktopIndicator.dll - File.");
		}
		presIcon = icons[0] = loadIcon("traf_red.ICO", getClass());
		icons[1] = loadIcon("traf_yellow.ICO", getClass());
		icons[2] = loadIcon("traf_green.ICO", getClass());
		for(int i = 0; i < icons.length; i++) {
			if(icons[i] == -1) {
				throw new Exception("No Icon.");
			}
		}
		sysTrayMgr = new SystemTrayIconManager(presIcon, presToolTip);
		sysTrayMgr.addSystemTrayIconListener(this);
		sysTrayMgr.setRightClickView(sysTrayMenu);
		sysTrayMgr.setVisible(true);
		sysTrayMgr.setVisible(false);
	}
	
	private int loadIcon(String fileName, Class clazz) {
		try {
			File file = new File(".\\image\\");
			if(!file.exists()) {
				file.mkdir();
			}
			file = new File(".\\image\\" + fileName);
			if(!file.exists()) {
				InputStream in = clazz.getResourceAsStream("/image/" + fileName);
				FileOutputStream out = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
				  out.write(buf, 0, len);
				}
				out.close();
				in.close();        		
			}
			return SystemTrayIconManager.loadImage("./image/" + fileName);
		} catch( Exception x ) {
			return -1;
		}
	}
	
	public void synchronize() {
		try {
			while (true) {
				synchronized( this ) {
					this.wait(1);
				}
			}
		} catch( InterruptedException x ) {
		}
		sysTrayMgr.setVisible(false);
		sysTrayMgr.removeSystemTrayIconListener(this);
		this.setVisible(true);
		for( int i = 0; i < listModel.size(); i++ )
			((User)listModel.getElementAt( i )).logout();
		if( rmiProcess != null ) {
			rmiProcess.destroy();
		}
		centralServer = null;
		this.dispose();
		System.exit( 0 );
	}
	
	public void iconify() {
		this.setVisible(false);
		sysTrayMgr.update(presIcon, presToolTip);
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
			public void windowIconified(WindowEvent e) {
				Server server = (Server)e.getSource();
				server.iconify();
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
		
	public JButton getCloseButton() {
		return buClose; 
	}
	
		
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == buRegistry || e.getSource() == miRegistry ) {
			if( rmiProcess == null && centralServer == null  ){
				try {
					rmiProcess = Runtime.getRuntime().exec(rmiregistry + " " + classpath);
					buRegistry.setText( "Stop Registry" );
					buServer.setEnabled( true );
					miRegistry.setText( "Stop Registry" );
					miServer.setEnabled( true );
					presIcon = icons[1];
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
				miRegistry.setText( "Start Registry" );
				miServer.setEnabled( false );
				presIcon = icons[0];
			} else if( rmiProcess != null && centralServer != null ) {
				JOptionPane.showMessageDialog( this, "Sie müssen zuerst den Server aus der Registry entfernen !",
														"Fehler !", JOptionPane.ERROR_MESSAGE );
			}
			if(this.getExtendedState() == Frame.ICONIFIED) {
				sysTrayMgr.update(presIcon, presToolTip);
			}
		} else if( e.getSource() == buServer || e.getSource() == miServer ) {
			if( rmiProcess == null && centralServer == null  ){
				JOptionPane.showMessageDialog( this, "Sie müssen zuerst Registry starten !",
														"Fehler !", JOptionPane.ERROR_MESSAGE );
			} else 	if( rmiProcess != null && centralServer == null  ){
				try {
					Naming.rebind("mittelverwaltung", centralServer = new CentralServerImpl( this ));
					buRegistry.setEnabled( false );
					buServer.setText( "Server entfernen" );
					miRegistry.setEnabled( false );
					miServer.setText( "Server entfernen" );
					presIcon = icons[2];
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
					miRegistry.setEnabled( true );
					miServer.setText( "Server registrieren" );
					presIcon = icons[1];
					statusBar.showTextForMilliseconds( "Server wurde entfernt.", delay );
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			if(this.getExtendedState() == Frame.ICONIFIED) {
				sysTrayMgr.update(presIcon, presToolTip);
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
		} else if( e.getSource() == buClose || e.getSource() == miClose ) {
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
			sysTrayMgr.setVisible(false);
			sysTrayMgr.removeSystemTrayIconListener(this);
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

