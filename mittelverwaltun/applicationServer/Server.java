package applicationServer;

import org.w3c.dom.*;

import gui.Functions;
import gui.IntegerTextField;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.io.*;
import java.rmi.*;
import java.text.DateFormat;
import java.util.Date;

import com.gc.systray.*;

/**
 * Klasse zum Erzeugen des Servers. <br>
 * Mit dem Server kann man die "rmiregistry" starten und den CentralServer bei der "rmiregistry" anmelden.
 * @author w.flat
 */
public class Server extends JFrame implements ActionListener, SystemTrayIconListener, ListSelectionListener {
	
	JTabbedPane tabPane = new JTabbedPane();
	JPanel panelServer = new JPanel();
	JPanel panelClients = new JPanel();
	JPanel panelEinstellungen = new JPanel();
	JPanel panelInfo = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	JLabel labTextBusyClients = new JLabel();
	JLabel labTextFreeClients = new JLabel();
	JLabel labNumBusyClients = new JLabel();
	JLabel labNumFreeClients = new JLabel();
	JButton butRMIRegistry = new JButton();
	JButton butCentralServer = new JButton();
	JButton butBeenden = new JButton();
	JScrollPane scrollClientList = new JScrollPane();
	DefaultListModel listModel = new DefaultListModel();
	JList listClients = new JList(listModel);
	JLabel labTextStartzeit = new JLabel();
	JLabel labStartzeit = new JLabel();
	JButton butDelUser = new JButton();
	JLabel labDBDriver = new JLabel();
	JTextField tfDBTreiber = new JTextField();
	JLabel labDBURL = new JLabel();
	JTextField tfDBURL= new JTextField();
	JLabel labDBName = new JLabel();
	JTextField tfDBName = new JTextField();
	JLabel labDBPswd = new JLabel();
	JPasswordField tfDBPswd1 = new JPasswordField();
	JLabel labNumClients = new JLabel();
	IntegerTextField tfMaxClients = new IntegerTextField(1);
	JButton butSave = new JButton();
	JPasswordField tfDBPswd2 = new JPasswordField();
	JLabel labServerName = new JLabel();
	JTextField tfServerName = new JTextField();
	JLabel labRMIRegistry = new JLabel();
	JTextField tfRMIRegistry = new JTextField();
	JButton butRMISuchen = new JButton();
	JLabel labFHMannheim = new JLabel();
	JLabel labVorlesung = new JLabel();
	JLabel labWF = new JLabel();
	JLabel labRD = new JLabel();
	JLabel labMS = new JLabel();
	JLabel labDesigned = new JLabel();
	JLabel labFHLogo = null;
	Component lastSelectedPanel = null;
	JFileChooser fileChooser = new JFileChooser(System.getProperty("java.home"));
	
	final static int WND_WIDTH = 320;		// Breite des Login-Fensters
	final static int WND_HEIGHT = 285;		// Höhe des Login-Fensters
	
	// RMI-Prozess
	Process rmiProcess = null;
	CentralServerImpl centralServer = null;
	String startRMI = "RMI-Registry Starten";
	String stopRMI = "RMI-Registry Stopen";
	String startServer = "Central-Server Starten";
	String stopServer = "Central-Server Stopen";
	
	// SystemTray Komponenten
	JPopupMenu sysTrayMenu = new JPopupMenu();
	JMenuItem miRegistry = new JMenuItem();
	JMenuItem miServer = new JMenuItem();
	JMenuItem miClose = new JMenuItem();
	SystemTrayIconManager sysTrayMgr;
	int icons[] = new int[3];
	int presIcon;
	String presToolTip = "FB-Mittelverwaltung\nFH-Mannheim SS 2005";
		
	// Verzeichnisse und Ordner für die Dateien, die ausgelagert werden müssen
	final static String xmlPackage = "xml";
	final static String xmlFileName = "server.xml";
	final static String imagePackage = "image";
	final static String[] iconNames = {"traf_red.ICO", "traf_yellow.ICO", "traf_green.ICO"};
	final static String serverPackage = "applicationServer";
	final static String[] serverSkelStub = {"ApplicationServerImpl_Stub.class", "CentralServerImpl_Stub.class",
											"ApplicationServerException.class", "ConnectionException.class", 
											"CentralServer.class", "ApplicationServer.class"};

	/**
	 * Erstellen vom <code>Server</code>.
	 */
	public Server() {
		super("FB-Mittelverwaltung Server");
		try {
			restoreFiles();
			jbInit();
			initSysTray();
			loadXMLFile();
			loadVariables();
		} catch(Exception e) {
			e.printStackTrace();
		}
		this.setVisible(true);
	}
	
	/**
	 * Generieren und starten des Central-Servers.
	 */
	public static void main(String args[]) {
		Server server = new Server();
		server.synchronize();
	}
	
	/**
	 * Die Einstellungen aus der XML-Datei laden.
	 */
	private void loadXMLFile() {
		try {
			File temp = new File(xmlFileName);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder  = factory.newDocumentBuilder();
			Document document = builder.parse(new FileInputStream(new File(xmlPackage + File.separator + xmlFileName)));
			CentralServerImpl.CENTRAL_RMI = document.getElementsByTagName("rmi").item(0).getFirstChild().getNodeValue();
			setRmiClaspath(CentralServerImpl.CENTRAL_RMI);
			CentralServerImpl.CENTRAL_NUM_APPL = Integer.parseInt(document.getElementsByTagName("number").item(0).getFirstChild().getNodeValue());
			CentralServerImpl.CENTRAL_NAME = document.getElementsByTagName("servername").item(0).getFirstChild().getNodeValue();
			ApplicationServerImpl.APPL_DB_DRIVER = document.getElementsByTagName("driver").item(0).getFirstChild().getNodeValue();
			ApplicationServerImpl.APPL_DB_NAME = document.getElementsByTagName("dbname").item(0).getFirstChild().getNodeValue();
			ApplicationServerImpl.APPL_DB_URL = document.getElementsByTagName("dburl").item(0).getFirstChild().getNodeValue();
			ApplicationServerImpl.APPL_DB_PSWD = document.getElementsByTagName("dbpswd").item(0).getFirstChild().getNodeValue();
	} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Die Einstellungen in die XML-Datei speichern.
	 */
	private void saveXMLFile() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder  = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			document.appendChild(document.createElement("server"));
			Node rootNode = document.getDocumentElement();
			Node tempNode = document.createElement("rmi");
			tempNode.appendChild(document.createTextNode(CentralServerImpl.CENTRAL_RMI));
			rootNode.appendChild(tempNode);
			tempNode = document.createElement("number");
			tempNode.appendChild(document.createTextNode("" + CentralServerImpl.CENTRAL_NUM_APPL));
			rootNode.appendChild(tempNode);
			tempNode = document.createElement("servername");
			tempNode.appendChild(document.createTextNode(CentralServerImpl.CENTRAL_NAME));
			rootNode.appendChild(tempNode);
			tempNode = document.createElement("driver");
			tempNode.appendChild(document.createTextNode(ApplicationServerImpl.APPL_DB_DRIVER));
			rootNode.appendChild(tempNode);
			tempNode = document.createElement("dbname");
			tempNode.appendChild(document.createTextNode(ApplicationServerImpl.APPL_DB_NAME));
			rootNode.appendChild(tempNode);
			tempNode = document.createElement("dburl");
			tempNode.appendChild(document.createTextNode(ApplicationServerImpl.APPL_DB_URL));
			rootNode.appendChild(tempNode);
			tempNode = document.createElement("dbpswd");
			tempNode.appendChild(document.createTextNode(ApplicationServerImpl.APPL_DB_PSWD));
			rootNode.appendChild(tempNode);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource( document );
			OutputStream os = new FileOutputStream(new File(xmlPackage + File.separator + xmlFileName));
			StreamResult result = new StreamResult( os );
			transformer.transform( source, result );
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Laden der Umgebungsvariablen in den Server. 
	 */
	private void loadVariables() {
		this.tfDBURL.setText(ApplicationServerImpl.APPL_DB_URL);
		this.tfDBName.setText(ApplicationServerImpl.APPL_DB_NAME);
		this.tfDBTreiber.setText(ApplicationServerImpl.APPL_DB_DRIVER);
		this.tfDBPswd1.setText(ApplicationServerImpl.APPL_DB_PSWD);
		this.tfDBPswd2.setText(ApplicationServerImpl.APPL_DB_PSWD);
		this.tfMaxClients.setValue(new Integer(CentralServerImpl.CENTRAL_NUM_APPL));
		this.tfRMIRegistry.setText(CentralServerImpl.CENTRAL_RMI);
		this.tfServerName.setText(CentralServerImpl.CENTRAL_NAME);
		this.labNumBusyClients.setText("");
		this.labNumFreeClients.setText("");
	}
	
	/**
	 * Methode zum Überprüfen ob die Einstelungen in Ordnung sind.
	 * @return String mit Fehlermeldung. Wenn keine Fehler aufgetreten, dann ist der String leer.
	 */
	private String checkSettings() {
		String error = "";
		if(tfDBURL.getText().length() == 0)
			error += " - Es wurde keine DB-Hostbezeichnung eingetragen.\n";
		if(tfDBName.getText().length() == 0)
			error += " - Es wurde keine DB-Name eingetragen.\n";
		if(!(new String(tfDBPswd1.getPassword())).equalsIgnoreCase(new String(tfDBPswd2.getPassword())))
			error += " - Die beiden Passwörter sind nicht identisch.\n";
		if(tfDBPswd1.getPassword().length == 0 || tfDBPswd2.getPassword().length == 0)
			error += " - Mindestens eines der Paswörter ist leer.\n";
		if(tfDBTreiber.getText().length() == 0)
			error += " - Es wurde kein DB-Treiber eingegeben.\n";
		if(tfServerName.getText().length() == 0)
			error += " - Es wurde kein Servername eingegeben.\n";
		if(CentralServerImpl.CENTRAL_CLASSPATH.length() == 0)
			error += " - Der CLASSPATH wurde nicht gefunden.\n";
		if(CentralServerImpl.CENTRAL_RMI.length() == 0)
			error += " - RMI-Registry wurde nicht ausgewählt.\n";
		
		return error;
	}
	
	/**
	 * Einen neuen ApplicationServer generieren.
	 * @param hostName = Der Hostname, wo sich der Benutzer befindet.
	 * @param hostAdress = Die Hostadresse, wo sich der Benutzer befindet.
	 * @param serverName = Der Name des gestarteten ApplicationServers.
	 */
	public void addNewApplicationServer( String hostName, String hostAdress, String serverName  ) {
		User user = new User( hostName, hostAdress, serverName );
		listModel.addElement( user );
		this.labNumBusyClients.setText(centralServer.getNumBusy());
		this.labNumFreeClients.setText(centralServer.getNumFree());
	}
	
	/**
	 * Den Benutzer-Namen zum User hinzufügen, da man beim Anlegen eines Users noch nicht weiß, wer sich anmelden wird.
	 * @param serverName = Name des Servers, dem der BenutzerName zugeordnet wird.
	 * @param benutzerName = benutzerName, der dem serverNamen zugeordnet wird. 
	 */
	public void addBenutzerNameToUser( String serverName, String benutzerName ) {
		User temp;
		for( int i = 0; i < listModel.size(); i++ ) {
			temp = (User)listModel.getElementAt( i );
			if( temp.serverName.equalsIgnoreCase(serverName) ) {
				temp.benutzerName = benutzerName;
				listModel.setElementAt( temp, i );
				break;
			}
		}
	}

	/**
	 * Einen User aus der ListBox entfernen
	 * @param serverName = Name des Servers, der aus der Liste entfernt werden soll.
	 */
	public void delUser(String serverName) {
		User temp;
		for( int i = 0; i < listModel.size(); i++ ) {
			temp = (User)listModel.getElementAt( i );
			if( temp.serverName.equalsIgnoreCase(serverName) ) {
				listModel.removeElementAt( i );
				break;
			}
		}
		this.labStartzeit.setText("");
		this.labNumBusyClients.setText(centralServer.getNumBusy());
		this.labNumFreeClients.setText(centralServer.getNumFree());
	}
	
	/**
	 * Aus der angegebenen rmiregistry-Datei die Variablen CentralServerImpl.CENTRAL_RMI und <br>
	 * CentralServerImpl.CENTRAL_CLASSPATH generieren.
	 * @param rmiFile = Der Dateiname der rmiregistry.exe.
	 * @return String mit Fehlermeldung. Wenn keine Fehler aufgetreten, dann ist der String leer.
	 */
	private String setRmiClaspath(String rmiFile) {
		String error = "";
		try {
			File f = new File(rmiFile);
			if(!f.getName().equals("rmiregistry.exe"))
				throw new Exception("Es keine RMI-Registry ausgewählt.");
			if(!f.exists())
				throw new Exception("Die RMI-Registry existiert nicht.");
			if(!f.exists())
				throw new Exception("Die RMI-Registry existiert nicht.");
			File libDir = new File((new File(f.getParent())).getParent() + System.getProperty("file.separator") + "lib");
			if(!libDir.exists()) 
				throw new Exception("Der Class-Path wurde nicht gefunden.");
			CentralServerImpl.CENTRAL_RMI = f.getAbsolutePath();
			CentralServerImpl.CENTRAL_CLASSPATH = "-J-classpath -J\"" + libDir.getAbsolutePath();
		} catch(Exception e) {
			error += " - " + e.toString() + "\n";
			CentralServerImpl.CENTRAL_CLASSPATH = CentralServerImpl.CENTRAL_RMI = "";
		}
		return error;
	}
	
	/**
	 * Methode zum Übernehmen der Einträge aus den Textfeldern. <br>
	 * Die Variablen CLASSPATH und RMI des werden aber nur durch den Dialog gesetzt.
	 */
	private void setSettings() {
		ApplicationServerImpl.APPL_DB_URL= tfDBURL.getText();
		ApplicationServerImpl.APPL_DB_NAME = tfDBName.getText();
		ApplicationServerImpl.APPL_DB_PSWD = new String(tfDBPswd1.getPassword());
		ApplicationServerImpl.APPL_DB_DRIVER = tfDBTreiber.getText();
		CentralServerImpl.CENTRAL_NAME = tfServerName.getText();
		CentralServerImpl.CENTRAL_NUM_APPL = ((Integer)tfMaxClients.getValue()).intValue();
	}
	
	/**
	 * Verarbeitung der Button-Ereignisse. 
	 */
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == butRMIRegistry || e.getSource() == miRegistry ) {
			if(butRMIRegistry.getText().equals(startRMI)){
				String error = checkSettings();
				if(error.length() > 0) {		// Wenn bei den Einstellungen Fehler aufgetreten sind
					JOptionPane.showMessageDialog(this, "Es sind folgende Fehler aufgetreten : \n" + error,
														"Fehler !", JOptionPane.ERROR_MESSAGE);
					return;			// Meldung ausgeben und zurückspringen
				}
				setSettings();		// Keine Fehler aufgetretten, dann die Einstellungen speichern
				try {
					rmiProcess = Runtime.getRuntime().exec(CentralServerImpl.CENTRAL_RMI + " " + 
															CentralServerImpl.CENTRAL_CLASSPATH);
					butRMIRegistry.setText(stopRMI);
					butCentralServer.setEnabled( true );
					miRegistry.setText(stopRMI);
					miServer.setEnabled( true );
					presIcon = icons[1];
				} catch (IOException e1) {
					e1.printStackTrace();
					rmiProcess = null;
				}
			} else if(butRMIRegistry.getText().equals(stopRMI)) {
				rmiProcess.destroy();
				rmiProcess = null;
				butRMIRegistry.setText(startRMI);
				butCentralServer.setEnabled( false );
				miRegistry.setText(startRMI);
				miServer.setEnabled( false );
				presIcon = icons[0];
			}
			if(this.getExtendedState() == Frame.ICONIFIED) {
				sysTrayMgr.update(presIcon, presToolTip);
			}
		} else if( e.getSource() == butCentralServer || e.getSource() == miServer ) {
			if(butCentralServer.getText().equals(startServer)){
				try {
					if(centralServer == null)
						Naming.rebind(CentralServerImpl.CENTRAL_NAME, centralServer = new CentralServerImpl(this));
					else {
						Naming.rebind(CentralServerImpl.CENTRAL_NAME, centralServer);
						centralServer.generateNames();
					}
					butRMIRegistry.setEnabled( false );
					butCentralServer.setText(stopServer);
					miRegistry.setEnabled( false );
					miServer.setText(stopServer);
					presIcon = icons[2];
					this.labNumBusyClients.setText(centralServer.getNumBusy());
					this.labNumFreeClients.setText(centralServer.getNumFree());
				} catch(Exception ex) {
					ex.printStackTrace();
					centralServer = null;
				}
			} else if(butCentralServer.getText().equals(stopServer)){
				try {
					int result = JOptionPane.YES_OPTION;
					if( listModel.size() > 0 ) {
						 result = JOptionPane.showConfirmDialog( this, 	"Es sind noch User angemeldet.\n" + 
																		"Wollen Sie wirklich beenden ?",
																		"Beenden ?", JOptionPane.YES_NO_OPTION,
																		JOptionPane.QUESTION_MESSAGE  );
					}
					if( result == JOptionPane.NO_OPTION )
						return;
					Naming.unbind(CentralServerImpl.CENTRAL_NAME);
					centralServer.removeAllServer();
					listModel.removeAllElements();
					this.labNumBusyClients.setText("");
					this.labNumFreeClients.setText("");
					labStartzeit.setText("");
					butRMIRegistry.setEnabled( true );
					butCentralServer.setText(startServer);
					miRegistry.setEnabled( true );
					miServer.setText(startServer);
					presIcon = icons[1];
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			if(this.getExtendedState() == Frame.ICONIFIED) {
				sysTrayMgr.update(presIcon, presToolTip);
			}
		} else if( e.getSource() == butDelUser ) {
			if( listClients.getSelectedIndex() >= 0 ) {
				User temp = (User)listModel.getElementAt( listClients.getSelectedIndex() );
				if( JOptionPane.showConfirmDialog( this, "Wollen Sie den User " + 
														(temp.benutzerName == null ? "" : "'" + temp.benutzerName + "' ") + 
														"wirklich entfernen ?", "Entfernen ?", JOptionPane.YES_NO_OPTION,
														JOptionPane.QUESTION_MESSAGE  ) == JOptionPane.YES_OPTION ){
					try {
						centralServer.delUser(temp.serverName);
					} catch(Exception exc) { }
				}
			}
		} else if( e.getSource() == butBeenden || e.getSource() == miClose ) {
			int result = JOptionPane.YES_OPTION;
			if( listModel.size() > 0 ) {
				 result = JOptionPane.showConfirmDialog( this, 	"Es sind noch User angemeldet.\n" + 
																"Wollen Sie wirklich beenden ?",
																"Beenden ?", JOptionPane.YES_NO_OPTION,
																JOptionPane.QUESTION_MESSAGE  );
			}
			if( result == JOptionPane.NO_OPTION )
				return;
			if( rmiProcess != null ) {
				rmiProcess.destroy();
			}
			sysTrayMgr.setVisible(false);
			sysTrayMgr.removeSystemTrayIconListener(this);
			saveXMLFile();
			centralServer = null;
			this.dispose();
			System.exit( 0 );
		} else if(e.getSource() == butRMISuchen) {
			if( fileChooser.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION ){
				File tempFile = fileChooser.getSelectedFile();
				if( tempFile == null){
					JOptionPane.showMessageDialog( this, "Fehler beim Öffnen der Datei.", "Error !", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String error = setRmiClaspath(tempFile.getAbsolutePath());
				if(error.length() > 0) {
					JOptionPane.showMessageDialog( this, "Folgender Fehler ist aufgetreten: \n" + error, 
														"Error !", JOptionPane.ERROR_MESSAGE);
					return;
				}
				tfRMIRegistry.setText(tempFile.getAbsolutePath());
			}
		} else if(e.getSource() == butSave) {
			String error = checkSettings();
			if(error.length() > 0) {		// Wenn bei den Einstellungen Fehler aufgetreten sind
				JOptionPane.showMessageDialog(this, "Es sind folgende Fehler aufgetreten : \n" + error,
													"Fehler !", JOptionPane.ERROR_MESSAGE);
				return;			// Meldung ausgeben und zurückspringen
			}
			setSettings();		// Keine Fehler aufgetretten, dann die Einstellungen speichern
		}
	}

	/**
	 * Initialisieren der graphischen Oberfläche. 
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		Dimension size = this.getToolkit().getScreenSize();		// Server in der Mitte des Bildschirms
		this.setBounds(new Rectangle(	(int)((size.width - WND_WIDTH) / 2), 
										(int)((size.height - WND_HEIGHT) / 2), 
										WND_WIDTH, WND_HEIGHT));
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setLocale(java.util.Locale.getDefault());
		this.setResizable(false);
		this.getContentPane().setLayout(borderLayout1);
		tabPane.setTabPlacement(JTabbedPane.TOP);
		tabPane.setBorder(BorderFactory.createEtchedBorder());
		panelServer.setLayout(null);
		panelClients.setLayout(null);
		panelEinstellungen.setLayout(null);
		panelInfo.setLayout(null);
		labTextBusyClients.setFont(new java.awt.Font("Dialog", 0, 11));
		labTextBusyClients.setText("Anzahl der angemeldeten Clients : ");
		labTextBusyClients.setBounds(new Rectangle(10, 20, 180, 15));
		labTextFreeClients.setFont(new java.awt.Font("Dialog", 0, 11));
		labTextFreeClients.setText("Anzahl der freien Clients :");
		labTextFreeClients.setBounds(new Rectangle(10, 50, 180, 15));
		labNumBusyClients.setFont(new java.awt.Font("Dialog", 0, 12));
		labNumBusyClients.setForeground(Color.red);
		labNumBusyClients.setText("0");
		labNumBusyClients.setBounds(new Rectangle(190, 20, 80, 16));
		labNumFreeClients.setFont(new java.awt.Font("Dialog", 0, 12));
		labNumFreeClients.setForeground(Color.blue);
		labNumFreeClients.setText("100");
		labNumFreeClients.setBounds(new Rectangle(190, 50, 80, 16));
		butRMIRegistry.setBounds(new Rectangle(30, 90, 240, 26));
		butRMIRegistry.setActionCommand("jButton1");
		butRMIRegistry.setText("RMI-Registry Starten");
		butCentralServer.setBounds(new Rectangle(30, 130, 240, 26));
		butCentralServer.setText("Central-Server Starten");
		butCentralServer.setEnabled(false);
		butBeenden.setBounds(new Rectangle(30, 170, 240, 26));
		butBeenden.setText("Anwendung Beenden");
		labTextStartzeit.setFont(new java.awt.Font("Dialog", 0, 11));
		labTextStartzeit.setText("Startzeit :");
		labTextStartzeit.setBounds(new Rectangle(10, 170, 80, 15));
		labStartzeit.setFont(new java.awt.Font("Dialog", 0, 11));
		labStartzeit.setForeground(SystemColor.desktop);
		labStartzeit.setText("");
		labStartzeit.setBounds(new Rectangle(100, 170, 180, 15));
		butDelUser.setText("Benutzer Löschen");
		butDelUser.setBounds(new Rectangle(50, 195, 210, 25));
		labDBDriver.setFont(new java.awt.Font("Dialog", 0, 11));
		labDBDriver.setText("DB Treiber");
		labDBDriver.setBounds(new Rectangle(10, 10, 90, 15));
		tfDBTreiber.setFont(new java.awt.Font("Dialog", 0, 11));
		tfDBTreiber.setBounds(new Rectangle(100, 10, 195, 19));
		listClients.setFont(new java.awt.Font("Dialog", 0, 11));
		labDBURL.setFont(new java.awt.Font("Dialog", 0, 11));
		labDBURL.setText("DB URL");
		labDBURL.setBounds(new Rectangle(10, 35, 90, 15));
		tfDBURL.setFont(new java.awt.Font("Dialog", 0, 11));
		tfDBURL.setText("");
		tfDBURL.setBounds(new Rectangle(100, 35, 195, 19));
		labDBName.setFont(new java.awt.Font("Dialog", 0, 11));
		labDBName.setText("DB Name");
		labDBName.setBounds(new Rectangle(10, 60, 90, 15));
		tfDBName.setFont(new java.awt.Font("Dialog", 0, 11));
		tfDBName.setText("");
		tfDBName.setColumns(0);
		tfDBName.setBounds(new Rectangle(100, 60, 195, 19));
		labDBPswd.setFont(new java.awt.Font("Dialog", 0, 11));
		labDBPswd.setText("DB Password");
		labDBPswd.setBounds(new Rectangle(10, 85, 90, 15));
		tfDBPswd1.setFont(new java.awt.Font("Dialog", 0, 11));
		tfDBPswd1.setText("");
		tfDBPswd1.setBounds(new Rectangle(100, 85, 195, 19));
		tfDBPswd2.setFont(new java.awt.Font("Dialog", 0, 11));
		tfDBPswd2.setText("");
		tfDBPswd2.setBounds(new Rectangle(100, 110, 195, 19));
		labNumClients.setFont(new java.awt.Font("Dialog", 0, 11));
		labNumClients.setText("Max. Clients");
		labNumClients.setBounds(new Rectangle(10, 150, 90, 15));
		tfMaxClients.setFont(new java.awt.Font("Dialog", 0, 11));
		tfMaxClients.setText("");
		tfMaxClients.setBounds(new Rectangle(100, 150, 60, 19));
		butSave.setFont(new java.awt.Font("Dialog", 0, 11));
		butSave.setBounds(new Rectangle(170, 150, 125, 19));
		butSave.setActionCommand("butSave");
		butSave.setText("Speichern");
		labServerName.setFont(new java.awt.Font("Dialog", 0, 11));
		labServerName.setText("Server-Name");
		labServerName.setBounds(new Rectangle(10, 175, 90, 15));
		tfServerName.setFont(new java.awt.Font("Dialog", 0, 11));
		tfServerName.setText("");
		tfServerName.setBounds(new Rectangle(100, 175, 195, 19));
		labRMIRegistry.setFont(new java.awt.Font("Dialog", 0, 11));
		labRMIRegistry.setText("RMI-Registry");
		labRMIRegistry.setBounds(new Rectangle(10, 200, 90, 15));
		tfRMIRegistry.setFont(new java.awt.Font("Dialog", 0, 11));
		tfRMIRegistry.setText("");
		tfRMIRegistry.setEnabled(false);
		tfRMIRegistry.setBounds(new Rectangle(100, 200, 175, 19));
		labFHMannheim.setText("FH-Mannheim SS2005");
		labFHMannheim.setBounds(new Rectangle(20, 100, 200, 16));
		labVorlesung.setText("Vorlesung OO2 + DBA");
		labVorlesung.setBounds(new Rectangle(20, 120, 200, 16));
		labWF.setFont(new java.awt.Font("Dialog", 0, 11));
		labWF.setRequestFocusEnabled(true);
		labWF.setText("Waldemar Flat");
		labWF.setBounds(new Rectangle(121, 170, 150, 15));
		labRD.setFont(new java.awt.Font("Dialog", 0, 11));
		labRD.setText("Robert Driesner");
		labRD.setBounds(new Rectangle(121, 150, 150, 15));
		labMS.setFont(new java.awt.Font("Dialog", 0, 11));
		labMS.setText("Mario Schmitt");
		labMS.setBounds(new Rectangle(121, 190, 150, 15));
		labDesigned.setText("Designed by :");
		labDesigned.setBounds(new Rectangle(20, 150, 100, 16));
		labFHLogo = new JLabel(Functions.getFHLogo(getClass()), JLabel.CENTER);
		labFHLogo.setBounds(new Rectangle(5, 10, 295, 80));
		scrollClientList.setBounds(new Rectangle(10, 10, 285, 150));
		butRMISuchen.setBounds(new Rectangle(275, 200, 20, 20));
		this.getContentPane().add(tabPane, BorderLayout.CENTER);
		tabPane.add(panelServer,    "Server");
		panelServer.add(labTextBusyClients, null);
		panelServer.add(labTextFreeClients, null);
		panelServer.add(labNumBusyClients, null);
		tabPane.add(panelClients,   "Clients");
		panelClients.add(scrollClientList, null);
		tabPane.add(panelEinstellungen,  "Einstellungen");
		panelEinstellungen.add(labDBDriver, null);
		panelEinstellungen.add(tfDBTreiber, null);
		panelEinstellungen.add(labDBURL, null);
		tabPane.add(panelInfo,  "Info");
		panelInfo.add(labFHMannheim, null);
		panelInfo.add(labVorlesung, null);
		panelServer.add(labNumFreeClients, null);
		panelServer.add(butRMIRegistry, null);
		panelServer.add(butCentralServer, null);
		panelServer.add(butBeenden, null);
		scrollClientList.getViewport().add(listClients, null);
		listClients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panelClients.add(labTextStartzeit, null);
		panelClients.add(labStartzeit, null);
		panelClients.add(butDelUser, null);
		panelEinstellungen.add(tfDBURL, null);
		panelEinstellungen.add(labDBName, null);
		panelEinstellungen.add(tfDBName, null);
		panelEinstellungen.add(labDBPswd, null);
		panelEinstellungen.add(tfDBPswd1, null);
		panelEinstellungen.add(tfDBPswd2, null);
		panelEinstellungen.add(labNumClients, null);
		panelEinstellungen.add(tfMaxClients, null);
		panelEinstellungen.add(butSave, null);
		panelEinstellungen.add(labServerName, null);
		panelEinstellungen.add(tfServerName, null);
		panelEinstellungen.add(labRMIRegistry, null);
		panelEinstellungen.add(tfRMIRegistry, null);
		panelEinstellungen.add(butRMISuchen, null);
		panelInfo.add(labDesigned, null);
		panelInfo.add(labRD, null);
		panelInfo.add(labWF, null);
		panelInfo.add(labMS, null);
		panelInfo.add(labFHLogo, null);
		
		// Nur ein File kann ausgwählt werden
		fileChooser.setMultiSelectionEnabled( false );		

		this.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				actionPerformed( new ActionEvent( butBeenden, 0, "" ) );
			}
			public void windowIconified(WindowEvent e) {
				iconify();
			}
		} );
		
		listClients.addListSelectionListener(this);
		
		// ActionListenet zuweisen
		butRMIRegistry.addActionListener( this );
		butRMIRegistry.setIcon(Functions.getWebIcon(getClass()));
		// Button Server
		butCentralServer.addActionListener( this );
		butCentralServer.setIcon(Functions.getServerIcon(getClass()));
		// Button Beenden
		butBeenden.addActionListener( this );
		butBeenden.setIcon(Functions.getCloseIcon(getClass()));
		// Button Dialog öffnen
		butRMISuchen.addActionListener( this );
		butRMISuchen.setIcon(Functions.getOpenIcon(getClass()));
		// Button Benutzer löschen
		butDelUser.addActionListener( this );
		butDelUser.setIcon(Functions.getDelIcon(getClass()));
		// Button Einstellungen speichern
		butSave.addActionListener( this );
		butSave.setIcon(Functions.getSaveIcon(getClass()));
	}
	
	/**
	 * Reaktion auf das Clicken in der Liste. 
	 */
	public void valueChanged(ListSelectionEvent e) {
		if( listClients.getSelectedIndex() >= 0 ) {
			User temp = (User)listModel.getElementAt( listClients.getSelectedIndex() );
			labStartzeit.setText(temp.startTime);
		}
	}

	/**
	 * SystemTrayIconListener : Mit der linken Maustaste einmal auf das Icon clicken.<br>
	 * Diese Methode wird nicht implementiert. 
	 */
	public void mouseClickedLeftButton(Point pos, SystemTrayIconManager source) {
	}

	/**
	 * SystemTrayIconListener : Mit der rechten Maustaste einmal auf das Icon clicken.<br>
	 * Diese Methode wird nicht implementiert. 
	 */
	public void mouseClickedRightButton(Point pos, SystemTrayIconManager ssource) {
	}

	/**
	 * SystemTrayIconListener : Mit der linken Maustaste doppelt auf das Icon clicken. <br>
	 * Nur diese Methode wird implementiert. 
	 */
	public void mouseLeftDoubleClicked(Point pos, SystemTrayIconManager source) {
		sysTrayMgr.setVisible(false);
		this.setVisible(true);
		this.setExtendedState(Frame.NORMAL);
		this.toFront();
	}

	/**
	 * SystemTrayIconListener : Mit der rechten Maustaste doppelt auf das Icon clicken.<br>
	 * Diese Methode wird nicht implementiert. 
	 */
	public void mouseRightDoubleClicked(Point pos, SystemTrayIconManager source) {
	}
	
	/**
	 * Initialisierung der SysTray. Es wird die DesktopIndicator.dll geladen <br>
	 * und alle benötigten Icons. Das PopUp-Menü für das TrayIcon wird auch initialisiert. 
	 * @throws Exception
	 */
	private void initSysTray() throws Exception {
		// MenuItem Registry
		sysTrayMenu.add(miRegistry);
		miRegistry.setFont(new java.awt.Font("Arial", 0, 11));
		miRegistry.setText(butRMIRegistry.getText());
		miRegistry.addActionListener( this );
		miRegistry.setIcon(Functions.getWebIcon(getClass()));
		// MenuItem Server
		sysTrayMenu.add(miServer);
		miServer.setFont(new java.awt.Font("Arial", 0, 11));
		miServer.setText(butCentralServer.getText());
		miServer.addActionListener( this );
		miServer.setEnabled( false );
		miServer.setIcon(Functions.getServerIcon(getClass()));
		// MenuItem Close
		sysTrayMenu.add(miClose);
		miClose.setFont(new java.awt.Font("Arial", 0, 11));
		miClose.setText(butBeenden.getText());
		miClose.addActionListener( this );
		miClose.setIcon(Functions.getCloseIcon(getClass()));

		if (!SystemTrayIconManager.initializeSystemDependent()) {
			throw new Exception("No DesktopIndicator.dll - File.");
		}
		for(int i = 0; i < icons.length; i++) {
			if((icons[i] = SystemTrayIconManager.loadImage("./" + imagePackage + "/" + iconNames[i])) == -1) {
				throw new Exception("No Icon. [" + i + "]");
			}
		}
		presIcon = icons[0];
		sysTrayMgr = new SystemTrayIconManager(presIcon, presToolTip);
		sysTrayMgr.addSystemTrayIconListener(this);
		sysTrayMgr.setRightClickView(sysTrayMenu);
		sysTrayMgr.setVisible(true);
		sysTrayMgr.setVisible(false);
	}
	
	/**
	 * Auslagern der benötigten externen Dateien aus der Jar-Datei in normale Verzeichnisse.
	 */
	private void restoreFiles() {
		try {
			Functions.restoreFile(	SystemTrayIconManager.dllPackage, 		// Die DLL auslagern
									SystemTrayIconManager.dllName + "." + SystemTrayIconManager.dllExt,
									getClass());
			for(int i = 0; i < iconNames.length; i++) {						// Die Bilddateien auslagern
				Functions.restoreFile(imagePackage, iconNames[i], getClass());
			}
			for(int i = 0; i < serverSkelStub.length; i++) {				// Die Serverdateien auslagern
				Functions.restoreFile(serverPackage, serverSkelStub[i], getClass());
			}
			Functions.restoreFile(xmlPackage, xmlFileName, getClass());		// Die XML-Datei auslagern
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * Synchronisierung zwischen SysTray und dem Server. 
	 */
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
		if( rmiProcess != null ) {
			rmiProcess.destroy();
		}
		centralServer = null;
		this.dispose();
		System.exit( 0 );
	}
	
	/**
	 * Das Frame soll minimiert werden.
	 */
	public void iconify() {
		this.setVisible(false);
		sysTrayMgr.update(presIcon, presToolTip);
	}

}

/**
 * Object der zum Anzeigen in der JList verwendet wird. Es wird der Host-Name, die Host-Adresse, <br>
 * der BenutzerName und der ApplicationServerName gespeichert. Und die toString-Methode wird überlagert. 
 * @author w.flat
 */
class User {
	public String hostName = null;
	public String hostAdress = null;
	public String serverName = null;
	public String benutzerName = null;
	public String startTime = null;
	
	/**
	 * Konstruktor zu Erstellen eines neuen Users.
	 * @param hostName = Der HostName, wo sich der neue User befindet. 
	 * @param hostAdress = Die HostAdresse, wo sich der neue User befindet. 
	 * @param serverName = Die Name des ApplicationServers, der für den neuen User gestartet wurde. 
	 */
	public User( String hostName, String hostAdress, String serverName ) {
		this.hostName = hostName;
		this.hostAdress = hostAdress;
		this.serverName = serverName;
		this.startTime = DateFormat.getDateInstance().format(new Date(System.currentTimeMillis())) + " um " + 
						DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis()));
	}

	/**
	 * Die überlagerte toString-Methode.
	 * @return Format: benutzerName @ hostAdress (hostName)
	 */
	public String toString() {
		if( benutzerName == null ) {
			return "user @ " + hostAdress + " (" + hostName + ")";
		} else {
			return benutzerName + " @ " + hostAdress + " (" + hostName + ")";
		}
	}
}
