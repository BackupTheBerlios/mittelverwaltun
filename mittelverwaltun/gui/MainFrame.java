package gui;

import javax.swing.*;
import java.awt.Frame;
import java.awt.event.*;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import javax.swing.JFrame;
import applicationServer.ApplicationServer;
import applicationServer.CentralServer;
import applicationServer.ConnectionException;
import dbObjects.Benutzer;

public class MainFrame extends JFrame implements ActionListener {

	ApplicationServer applicationServer;
	CentralServer centralServer;
	LoginDialog login;
	JDesktopPane desk;
	Benutzer benutzer;

	
	final String host = "localhost";				// Adresse und
	final String serverName = "mittelverwaltung";	// ServerName

	public MainFrame(){
		super( "Mittelverwaltungsprogramm" );
		try{
			centralServer = (CentralServer)Naming.lookup("//" + host + "/" + serverName);
			InetAddress addr = InetAddress.getLocalHost();
			applicationServer = centralServer.getMyApplicationServer( addr.getHostName(), addr.getHostAddress() );
			this.setVisible(false);
			login = new LoginDialog(this, "Login Mittelverwaltung", true);
			login.show();
			setJMenuBar( new MainMenu( this ) );
			this.desk = new JDesktopPane();
			desk.setDesktopManager(new DefaultDesktopManager());
			setContentPane(desk);
			setBounds( 50, 50, 850, 400 );
			this.addWindowListener( new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					((MainFrame)e.getSource()).onWindowClosing();
				}
			} );
			this.setExtendedState(Frame.MAXIMIZED_BOTH);
			this.show();
		}catch(Exception e){
			JOptionPane.showMessageDialog(
				this,
				e.getMessage(),
				"Warnung",
				JOptionPane.ERROR_MESSAGE);
			this.onWindowClosing();
		}
	}
	
	/**
	 * Den Benutzernamen an den Server senden.
	 */
	public void sendBenutzer() {
		if( centralServer != null && applicationServer != null ) {
			try {
				centralServer.addBenutzerNameToUser( applicationServer.getId(), benutzer.getBenutzername() );
			} catch (RemoteException e1) {
			}
		}
	}

	public MainFrame(String title){
		super( title );
	
		this.desk = new JDesktopPane();
		desk.setDesktopManager(new DefaultDesktopManager());
		setContentPane(desk);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				((MainFrame)e.getSource()).onWindowClosing();
			}
		} );
	}
		
		
	public void setBenutzer(Benutzer benutzer) {
		this.benutzer = benutzer;
	}

	public Benutzer getBenutzer() {
		return benutzer;
	}
	
	public void setApplicationServer(ApplicationServer applicationServer) {
		this.applicationServer = applicationServer;
	}

	public ApplicationServer getApplicationServer() {
		return applicationServer;
	}

	
	public void addChild(JInternalFrame child) {
		desk.add(child);
		child.setVisible(true);
	}
	
	/**
	 * Logout durchführen und Fenster schließen.
	 */
	public void onWindowClosing() {
		if( centralServer != null && applicationServer != null ) {
			try {
				centralServer.delUser( applicationServer.getId() );
				applicationServer.logout();
			} catch(RemoteException e1) {
			} catch(ConnectionException ec) {
			}
		}
	   this.dispose();
	   System.exit(0);
	}
	
	public void actionPerformed( ActionEvent e ) {
		
	}

}
