package gui;

import javax.swing.*;

import org.jfree.report.Boot;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.rmi.*;

import applicationServer.*;
import dbObjects.Benutzer;

public class MainFrame extends JFrame implements ActionListener {

	ApplicationServer applicationServer;
	CentralServer centralServer;
	JDesktopPane desk;
	Benutzer benutzer;
	Image bg;
	ImageIcon bgIcon;
	
	final String host = "localhost";				// Adresse und
	final String serverName = "mittelverwaltung";	// ServerName
	
	public MainFrame(CentralServer centralServer, ApplicationServer applicationServer, Benutzer benutzer) {
		super( "Mittelverwaltungsprogramm" );
		try{
			this.centralServer = centralServer;
			this.applicationServer = applicationServer;
			this.benutzer = benutzer;
			Boot.start();	// Initialisierung der Module für das Drucken
			InetAddress addr = InetAddress.getLocalHost();
			bg = loadImageResource("image","bg.jpg");
			if (bg != null)
				bgIcon =  new ImageIcon(bg);
				
			setJMenuBar( new MainMenu( this ) );
			this.desk = new JDesktopPane(){
													public void paintComponent(Graphics g) {
													  super.paintComponent(g);
														g.drawImage(bg, getWidth()/2 - 240, getHeight()/2 - 180, 480, 353, this);
													}
											};
			desk.setDesktopManager(new DefaultDesktopManager());
			setContentPane(desk);
			setBounds( 50, 50, 800, 600 );
			this.addWindowListener( new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					((MainFrame)e.getSource()).onWindowClosing();
				}
			} );
			this.setExtendedState(Frame.MAXIMIZED_BOTH);
			this.doLayout();
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
				centralServer.addBenutzerNameToUser( applicationServer.getName(), benutzer.getBenutzername() );
			} catch (RemoteException e1) {
			}
		}
	}

	public MainFrame(String title){
		super( title );
	
		this.desk = new JDesktopPane();
		desk.setDesktopManager(new DefaultDesktopManager());
		setContentPane(desk);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
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
				applicationServer.logout();
				centralServer.delUser( applicationServer.getName() );
			} catch(RemoteException e1) {
			} catch(ConnectionException ec) {
			}
		}
	   this.dispose();
	   System.exit(0);
	}

	/**
	 * Logout durchführen und Fenster schließen.
	 */
	public void windowClosing() {
		if( centralServer != null && applicationServer != null ) {
			try {
				applicationServer.logout();
			} catch(Exception e1) {
			}
		}
	   this.dispose();
	   System.exit(0);
	}
		
	public void actionPerformed( ActionEvent e ) {
		
	}
	
	private Image loadImageResource (String pkgname, String fname) throws IOException{
	 Image ret = null;

	 InputStream is = getResourceStream(pkgname, fname);

	 if (is != null){

		byte[] buffer = new byte[0];
		byte[] tmpbuf = new byte[1024];

		while (true){
				  int len = is.read(tmpbuf);
				  if (len<=0){
							 break;
				  }
				  byte[] newbuf = new byte[buffer.length + len];
				  System.arraycopy(buffer, 0, newbuf, 0, buffer.length);
				  System.arraycopy(tmpbuf, 0, newbuf, buffer.length, len);
				  buffer = newbuf;
		}

		// create image
		ret = Toolkit.getDefaultToolkit().createImage(buffer);
		is.close();
	 }

	 return ret;
  }
	  
	private InputStream getResourceStream (String pkgname, String fname){
		 String resname = "/" + pkgname.replace('.','/')+ "/" + fname;
		 Class clazz = getClass();
		 InputStream is = clazz.getResourceAsStream(resname);
		 return is;
	}

}
