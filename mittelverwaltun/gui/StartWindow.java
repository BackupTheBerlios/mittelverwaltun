package gui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.rmi.*;

import applicationServer.*;
import dbObjects.*;

/**
 * Anmeldungs-Fenster zum Anmelden im FBMittelverwaltungs-System.
 * @author w.flat
 */
public class StartWindow extends JFrame implements ActionListener {
	
	JPanel panelWindow = new JPanel();
	JLabel labPasswort = new JLabel();
	JLabel labBenutzername = new JLabel();
	JButton butAnmelden = new JButton();
	JPasswordField tfPasswort = new JPasswordField();
	JTextField tfBenutzername = new JTextField();
	
	JButton butAbbrechen = new JButton();
	JLabel labFHLogo;
	
	ApplicationServer applicationServer;
	CentralServer centralServer;
	Benutzer benutzer;
	final static String host = "localhost";					// Adresse und
	final static String serverName = "mittelverwaltung";	// ServerName
	
	final static int WND_WIDTH = 320;		// Breite des Login-Fensters
	final static int WND_HEIGHT = 252;		// Höhe des Login-Fensters

	/**
	 * Erzeugt ein Fenster mit demm die Anmeldung im FBMittelverwaltungs-System erfolgt.
	 */
	public StartWindow() {
		super("Login Mittelverwaltung");
		try {
			this.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
			this.setResizable( false );
			Dimension size = this.getToolkit().getScreenSize();		// Login-Fenster in der Mitte des Bildschirms
			this.setBounds(new Rectangle((int)((size.width - WND_WIDTH) / 2), (int)((size.height - WND_HEIGHT) / 2), 322, 252));
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					actionPerformed(new ActionEvent(butAbbrechen, 0, ""));
				}
			});
			this.show();
			jbInit();
			tfBenutzername.requestFocus();
			centralServer = (CentralServer)Naming.lookup("//" + host + "/" + serverName);
			InetAddress addr = InetAddress.getLocalHost();
			String applServerName = centralServer.getMyApplicationServer(addr.getHostName(), addr.getHostAddress());
			if(applServerName == null)
				throw new Exception("Der ApplicationServer konnte nicht gestartet werden.");
			System.out.println(applServerName);
			applicationServer = (ApplicationServer)Naming.lookup("//" + host + "/" + applServerName);
			System.out.println(applicationServer.getName());
		} catch(Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Warnung", JOptionPane.ERROR_MESSAGE);
			centralServer = null;
			applicationServer = null;
			this.actionPerformed(new ActionEvent(butAbbrechen, 0, ""));
		}
	}
	
	/**
	 * Initialisierung der graphischen Oberfläche. 
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		panelWindow.setBorder(BorderFactory.createEtchedBorder());
		panelWindow.setOpaque(true);
		panelWindow.setLayout(null);
		labPasswort.setText("Passwort");
		labPasswort.setBounds(new Rectangle(10, 140, 145, 15));
		labBenutzername.setText("Benutzername");
		labBenutzername.setBounds(new Rectangle(10, 100, 145, 15));
		butAnmelden.setBounds(new Rectangle(165, 190, 140, 25));
		butAnmelden.setText("Anmelden");
		tfPasswort.setText("");
		tfPasswort.setBounds(new Rectangle(160, 140, 145, 21));
		tfBenutzername.setText("");
		tfBenutzername.setBounds(new Rectangle(160, 100, 145, 21));
		butAbbrechen.setBounds(new Rectangle(10, 190, 140, 25));
		butAbbrechen.setText("Abbrechen");
		this.getContentPane().add(panelWindow, null);
		panelWindow.add(labPasswort, null);
		panelWindow.add(labBenutzername, null);
		panelWindow.add(tfPasswort, null);
		panelWindow.add(butAbbrechen, null);
		panelWindow.add(butAnmelden, null);
		panelWindow.add(tfBenutzername, null);
		
		// Das FH-Mannheim-Logo
		labFHLogo = new JLabel(Functions.getFHLogo(getClass()), JLabel.CENTER);
		labFHLogo.setBounds(new Rectangle(10, 10, 295, 80));
		panelWindow.add(labFHLogo, null);
		
		// Button-Icons und den ActionListener festlegen 
		butAnmelden.addActionListener( this );
		butAnmelden.setIcon(Functions.getConnectorIcon(getClass()));
		butAbbrechen.addActionListener( this );
		butAbbrechen.setIcon(Functions.getCloseIcon(getClass()));
		
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

	
	/**
	 * Reaktion auf die Button-Ereignisse. 
	 * @param e = Ereignis, das ausgelöst wurde.
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == butAbbrechen) {		// Wenn der Abbrechen-Button betätigt wurde
			if(applicationServer != null) {
				try {
					applicationServer.logout();
				} catch (Exception e1) {
				}
			}
			if(centralServer != null && applicationServer != null) {
				try {
					centralServer.delUser(applicationServer.getName());
				} catch (Exception e1) {
				}
			}
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
					// Benutzer abfragen
					benutzer = applicationServer.login(tfBenutzername.getText(), psw);
					// Dem CentralServer den Namen des Users übertragen
					centralServer.addBenutzerNameToUser( applicationServer.getName(), benutzer.getBenutzername() );
					// Fenster unsichtbar schalten
					setVisible(false);
					// Haupt-Fenster generieren
					new MainFrame(centralServer, applicationServer, benutzer);
					dispose();	// Fenster freigeben
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(this, exc.getMessage(), "Warnung", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

}


