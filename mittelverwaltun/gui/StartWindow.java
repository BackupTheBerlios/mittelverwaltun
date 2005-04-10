package gui;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.rmi.*;
import java.io.*;

import applicationServer.*;
import dbObjects.*;

/**
 * Anmeldungs-Fenster zum Anmelden im FBMittelverwaltungs-System.
 * @author w.flat
 */
public class StartWindow extends JFrame implements ActionListener {

	BorderLayout borderLayout = new BorderLayout();
	JTabbedPane tabPane = new JTabbedPane();
	JPanel panelLogin = new JPanel();
	JPanel panelEinstellungen = new JPanel();
	JPanel panelInfo = new JPanel();
	JLabel labBGImage;
	JLabel labMS = new JLabel();
	JLabel labRD = new JLabel();
	JLabel labWF = new JLabel();
	JLabel labVorlesung = new JLabel();
	JLabel labFHMannheim = new JLabel();
	JLabel labDesigned = new JLabel();
	JLabel labHostname = new JLabel();
	JTextField tfHostname = new JTextField();
	JLabel labServername = new JLabel();
	JTextField tfServername = new JTextField();
	JButton butSave = new JButton();
	JLabel labPasswort = new JLabel();
	JLabel labBenutzername = new JLabel();
	JButton butAnmelden = new JButton();
	JTextField tfBenutzername = new JTextField();
	JPasswordField tfPasswort = new JPasswordField();
	JButton butAbbrechen = new JButton();
	JLabel labFHLogo;
	
	ApplicationServer applicationServer;
	CentralServer centralServer;
	Benutzer benutzer;
	
	/**
	 * Variable zum Einstellen der Hostbezeichnung, wo sich der FB-Mittelverwaltung-Server befindet. 
	 */
	public static String CLIENT_SERVER_HOST = "";

	/**
	 * Variable zum Einstellen vom Namen des Servers. 
	 */
	public static String CLIENT_SERVER_NAME = "";

	/**
	 * Datei zum Speichern der Einstellungen. 
	 */
	final static String xmlFileName = "client.xml"; 
	final static String xmlPackage = "xml"; 
	
	/**
	 * Breite des Login-Fensters
	 */
	final static int WND_WIDTH = 325;
	
	/**
	 * Höhe des Login-Fensters
	 */
	final static int WND_HEIGHT = 275;

	/**
	 * Erzeugt ein Fenster mit demm die Anmeldung im FBMittelverwaltungs-System erfolgt.
	 */
	public StartWindow() {
		super("Login Mittelverwaltung");
				
		try {
			this.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
			this.setResizable( false );
			Dimension size = this.getToolkit().getScreenSize();		// Login-Fenster in der Mitte des Bildschirms
			this.setBounds(new Rectangle((int)((size.width - WND_WIDTH) / 2), (int)((size.height - WND_HEIGHT) / 2), 
														WND_WIDTH, WND_HEIGHT));
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					actionPerformed(new ActionEvent(butAbbrechen, 0, ""));
				}
			});
			Functions.restoreFile(xmlPackage, xmlFileName, getClass());
			jbInit();
			loadXMLFile();
			this.setVisible(true);
			tfBenutzername.requestFocus();
		} catch(Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Warnung", JOptionPane.ERROR_MESSAGE);
			this.actionPerformed(new ActionEvent(butAbbrechen, 0, ""));
		}
	}
	
	/**
	 * Die Einstellungen aus der XML-Datei laden.
	 */
	private void loadXMLFile() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder  = factory.newDocumentBuilder();
			Document document = builder.parse(new FileInputStream(new File(xmlPackage + File.separator + xmlFileName)));
			StartWindow.CLIENT_SERVER_HOST = document.getElementsByTagName("hostname").item(0).getFirstChild().getNodeValue();
			StartWindow.CLIENT_SERVER_NAME = document.getElementsByTagName("servername").item(0).getFirstChild().getNodeValue();
			tfHostname.setText(StartWindow.CLIENT_SERVER_HOST);
			tfServername.setText(StartWindow.CLIENT_SERVER_NAME);
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
			document.appendChild(document.createElement("client"));
			Node rootNode = document.getDocumentElement();
			Node tempNode = document.createElement("hostname");
			tempNode.appendChild(document.createTextNode(StartWindow.CLIENT_SERVER_HOST));
			rootNode.appendChild(tempNode);
			tempNode = document.createElement("servername");
			tempNode.appendChild(document.createTextNode(StartWindow.CLIENT_SERVER_NAME));
			rootNode.appendChild(tempNode);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource( document );
			FileOutputStream os = new FileOutputStream(new File(xmlPackage + File.separator + xmlFileName));
			StreamResult result = new StreamResult( os );
			transformer.transform( source, result );
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Methode zum Überprüfen ob die Einstelungen in Ordnung sind.
	 * @return String mit Fehlermeldung. Wenn keine Fehler aufgetreten, dann ist der String leer.
	 */
	private String setSettings() {
		String error = "";
		if(tfHostname.getText().length() == 0)
			error += " - Es wurde keine Server-Hostbezeichnung eingetragen.\n";
		if(tfServername.getText().length() == 0)
			error += " - Es wurde kein Server-Name eingetragen.\n";
		
		if(error.length() == 0) {		// Wen keine Fehler aufgetreten, dann Änderungen übernehmen
			StartWindow.CLIENT_SERVER_HOST = tfHostname.getText();
			StartWindow.CLIENT_SERVER_NAME = tfServername.getText();
		}
		
		return error;
	}
	
	/**
	 * Reaktion auf die Button-Ereignisse. 
	 * @param e = Ereignis, das ausgelöst wurde.
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == butAbbrechen) {		// Wenn der Abbrechen-Button betätigt wurde
			if(centralServer != null && applicationServer != null) {
				try {
					centralServer.delUser(applicationServer.getName());
				} catch (Exception e1) { }
			}
			saveXMLFile();
			setVisible(false);
			dispose();
			System.exit(0);
		} else if(e.getSource() == butAnmelden) {		// Wenn der Anmelden-Button betätigt wurde
			// Wenn eines der TextFelder leer ist
			if(tfBenutzername.getText().equals("") || tfPasswort.getPassword().length == 0){
				JOptionPane.showMessageDialog(this, "Benutzername oder/und Passwort ist/sind leer !!!",
												"Warnung", JOptionPane.ERROR_MESSAGE);
			} else {	// Sonst Überprüfung der Angaben
				String psw = "";
				try{	// Das Password MD5 kodiert
					PasswordEncrypt pe = new PasswordEncrypt();
					psw = pe.encrypt(new String(tfPasswort.getPassword()).toString());
				}catch(Exception ex){
					System.out.println("MessageDigest Fehler");
				}	
				
				try{
					centralServer = (CentralServer)Naming.lookup("//" + CLIENT_SERVER_HOST + "/" + CLIENT_SERVER_NAME);
					InetAddress addr = InetAddress.getLocalHost();
					String applServerName = centralServer.getMyApplicationServer(addr.getHostName(), addr.getHostAddress());
					if(applServerName == null)
						throw new Exception("Der ApplicationServer konnte nicht gestartet werden.");
					applicationServer = (ApplicationServer)Naming.lookup("//" + CLIENT_SERVER_HOST + "/" + applServerName);
					// Benutzer abfragen
					benutzer = applicationServer.login(tfBenutzername.getText(), psw);
					// Dem CentralServer den Namen des Users übertragen
					centralServer.addBenutzerNameToUser(applicationServer.getName(), benutzer.getBenutzername());
					// Fenster unsichtbar schalten
					setVisible(false);
					new MainFrame(centralServer, applicationServer, benutzer);
					dispose();	// Fenster freigeben
				} catch (Exception exc) {
					if(centralServer != null) {
						if(applicationServer != null) {
							try{
								centralServer.delUser(applicationServer.getName());
							} catch(Exception ex) { }
						}
					}
					centralServer = null;
					applicationServer = null;
					JOptionPane.showMessageDialog(this, exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if(e.getSource() == butSave) {
			String error = setSettings();
			if(error.length() > 0){
				JOptionPane.showMessageDialog( this, "Folgende Fehler sidn aufgetreten :\n" + error, 
														"Error !", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/**
	 * Initialisierung der graphischen Oberfläche. 
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		this.getContentPane().setLayout(borderLayout);
		panelLogin.setLayout(null);
		panelEinstellungen.setLayout(null);
		panelInfo.setLayout(null);
		labBGImage = new JLabel(Functions.getLoginImage(getClass()), JLabel.CENTER);
		labBGImage.setText("");
		labBGImage.setBounds(new Rectangle(10, 10, 295, 80));
		labMS.setFont(new java.awt.Font("Dialog", 0, 11));
		labMS.setText("Mario Schmitt");
		labMS.setBounds(new Rectangle(120, 185, 150, 15));
		labRD.setFont(new java.awt.Font("Dialog", 0, 11));
		labRD.setText("Robert Driesner");
		labRD.setBounds(new Rectangle(120, 145, 150, 15));
		labWF.setFont(new java.awt.Font("Dialog", 0, 11));
		labWF.setRequestFocusEnabled(true);
		labWF.setText("Waldemar Flat");
		labWF.setBounds(new Rectangle(120, 165, 150, 15));
		labVorlesung.setText("Vorlesung OO2 + DBA");
		labVorlesung.setBounds(new Rectangle(20, 115, 200, 16));
		labFHMannheim.setText("FH-Mannheim SS2005");
		labFHMannheim.setBounds(new Rectangle(20, 95, 200, 16));
		labDesigned.setText("Designed by :");
		labDesigned.setBounds(new Rectangle(20, 145, 100, 16));
		labHostname.setFont(new java.awt.Font("Dialog", 0, 11));
		labHostname.setText("Name vom Server-Host :");
		labHostname.setBounds(new Rectangle(10, 20, 130, 15));
		tfHostname.setFont(new java.awt.Font("Dialog", 0, 11));
		tfHostname.setText("");
		tfHostname.setBounds(new Rectangle(140, 20, 155, 19));
		labServername.setFont(new java.awt.Font("Dialog", 0, 11));
		labServername.setText("Name des Servers :");
		labServername.setBounds(new Rectangle(10, 55, 130, 15));
		tfServername.setFont(new java.awt.Font("Dialog", 0, 11));
		tfServername.setText("");
		tfServername.setBounds(new Rectangle(140, 55, 155, 19));
		butSave.setBounds(new Rectangle(50, 100, 205, 22));
		butSave.setFont(new java.awt.Font("Dialog", 0, 11));
		butSave.setText("Einstellungen Speichern");
		labPasswort.setText("Passwort");
		labPasswort.setBounds(new Rectangle(10, 140, 145, 15));
		labBenutzername.setText("Benutzername");
		labBenutzername.setBounds(new Rectangle(10, 100, 145, 15));
		butAnmelden.setBounds(new Rectangle(165, 190, 140, 25));
		butAnmelden.setText("Anmelden");
		tfBenutzername.setText("");
		tfBenutzername.setBounds(new Rectangle(160, 100, 145, 21));
		tfPasswort.setText("");
		tfPasswort.setBounds(new Rectangle(160, 140, 145, 21));
		butAbbrechen.setBounds(new Rectangle(10, 190, 140, 25));
		butAbbrechen.setText("Abbrechen");
		labFHLogo = new JLabel(Functions.getFHLogo(getClass()), JLabel.CENTER);
		labFHLogo.setText("");
		labFHLogo.setBounds(new Rectangle(10, 10, 295, 80));
		this.getContentPane().add(tabPane,  BorderLayout.CENTER);
		tabPane.add(panelLogin,   "Anmelden");
		panelLogin.add(labPasswort, null);
		panelLogin.add(labBenutzername, null);
		panelLogin.add(butAnmelden, null);
		panelLogin.add(tfBenutzername, null);
		panelLogin.add(tfPasswort, null);
		panelLogin.add(butAbbrechen, null);
		panelLogin.add(labBGImage, null);
		tabPane.add(panelEinstellungen,  "Einstellungen");
		panelEinstellungen.add(labHostname, null);
		panelEinstellungen.add(tfHostname, null);
		panelEinstellungen.add(labServername, null);
		panelEinstellungen.add(tfServername, null);
		panelEinstellungen.add(butSave, null);
		tabPane.add(panelInfo,  "Info");
		panelInfo.add(labFHLogo, null);
		panelInfo.add(labMS, null);
		panelInfo.add(labRD, null);
		panelInfo.add(labWF, null);
		panelInfo.add(labVorlesung, null);
		panelInfo.add(labFHMannheim, null);
		panelInfo.add(labDesigned, null);
		
		// Button-Icons und den ActionListener festlegen 
		butAnmelden.addActionListener( this );
		butAnmelden.setIcon(Functions.getConnectorIcon(getClass()));
		butAbbrechen.addActionListener( this );
		butAbbrechen.setIcon(Functions.getCloseIcon(getClass()));
		butSave.addActionListener( this );
		butSave.setIcon(Functions.getSaveIcon(getClass()));
		
		// KeyListener der Buttons und TextFelder wenn die ENTER-Taste gedrückt wird
		tfBenutzername.addKeyListener(new KeyAdapter() {
									public void keyPressed(KeyEvent e) {
										if(KeyEvent.VK_ENTER == e.getKeyCode())
											actionPerformed(new ActionEvent(butAnmelden, 0, ""));
									}
								});
		tfPasswort.addKeyListener(new KeyAdapter() {
									public void keyPressed(KeyEvent e) {
										if(KeyEvent.VK_ENTER == e.getKeyCode())
											actionPerformed(new ActionEvent(butAnmelden, 0, ""));
									}
								});
		butAnmelden.addKeyListener(new KeyAdapter() {
									public void keyPressed(KeyEvent e) {
										if(KeyEvent.VK_ENTER == e.getKeyCode())
											actionPerformed(new ActionEvent(butAnmelden, 0, ""));
									}
								});
		butAbbrechen.addKeyListener(new KeyAdapter() {
									public void keyPressed(KeyEvent e) {
										if(KeyEvent.VK_ENTER == e.getKeyCode())
											actionPerformed(new ActionEvent(butAbbrechen, 0, ""));
									}
								});
	}

}
