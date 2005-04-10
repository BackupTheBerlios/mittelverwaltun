package gui;

import javax.swing.*;

import org.jfree.report.Boot;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.rmi.*;

import applicationServer.*;
import dbObjects.Benutzer;

/**
 * Hauptfenster von der FB-Mittelverwaltung. <br>
 * Das Frame besitzt ein Menü, womit die einzelnen InternalFrames aufgerufen werden. 
 * @author w.flat
 */
public class MainFrame extends JFrame {
	
	ApplicationServer applicationServer;	// Der individuelle ApplicationServer
	CentralServer centralServer;	// Der CentralServer zum Mitteilen, dass der ApplicationServer nicht mehr benötigt wird
	JDesktopPane desk;				// Pane zum Verwalten der Internal-Frames
	Benutzer benutzer;				// Benutzer des Frames
	Image bg;						// Das Hintergrund-Bild
	ImageIcon bgIcon;				// Das Hintergrund-Bild
	
	/**
	 * Zum Erstellen des Mittelverwaltung-Frames. Das <code>StartWindow</code> übernimmt das Einlogen<br>
	 * und wenn es erfolgreich war, dann wird der MainFrame gestartet. 
	 * @param centralServer = CentralServer von dem man den ApplicationServer bekommen hat.
	 * @param applicationServer = Der individuelle ApplicationServer des Frames. 
	 * @param benutzer = Benutzer, der sich bei dem System angemeldet hat. 
	 */
	public MainFrame(CentralServer centralServer, ApplicationServer applicationServer, Benutzer benutzer) {
		super( "Mittelverwaltungsprogramm" );
		try{
			this.centralServer = centralServer;
			this.applicationServer = applicationServer;
			this.benutzer = benutzer;
			Boot.start();	// Initialisierung der Module für das Drucken
			InetAddress addr = InetAddress.getLocalHost();
			bg = Functions.loadImageResource("image", "bg.jpg", getClass());
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
			desk.setBackground(new Color(109,183,218));
			setContentPane(desk);
			setBounds( 50, 50, 800, 600 );
			this.addWindowListener( new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					((MainFrame)e.getSource()).onWindowClosing();
				}
			} );
			this.setExtendedState(Frame.MAXIMIZED_BOTH);
			this.doLayout();
			this.setVisible(true);
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
	 * Konstruktor zum Erstellen eines Frames zum Testen der InternalFrames. 
	 * @param title = Der Titel des MainFrames. 
	 */
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
	
	/**
	 * Den Benutzer des MainFrames festlegen.
	 * @param benutzer = Neuer Benutzer des Frames.
	 */
	public void setBenutzer(Benutzer benutzer) {
		this.benutzer = benutzer;
	}
	
	/**
	 * Den Benutzer des Frames abfragen. 
	 * @return Der aktuelle Benutzer des MainFrames. 
	 */
	public Benutzer getBenutzer() {
		return benutzer;
	}
	
	/**
	 * Den ApplicationServer dem MainFrame zuweisen.
	 * @param applicationServer = Der neue ApplicationServer für das Frame. 
	 */
	public void setApplicationServer(ApplicationServer applicationServer) {
		this.applicationServer = applicationServer;
	}

	/**
	 * Den ApplicationServer beim Frame abfragen, damit alle InternalFrames ihn benutzen können. 
	 * @return Der aktuelle ApplicationServer. 
	 */
	public ApplicationServer getApplicationServer() {
		return applicationServer;
	}

	/**
	 * Neues InternalFrame(Child) im Fenster erzeugen. 
	 * @param child = Neues InternalFrame, das angezeigt werden soll. 
	 */
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
				centralServer.delUser( applicationServer.getName() );
			} catch(RemoteException e1) {
			}
		}
	   this.dispose();
	   System.exit(0);
	}
}
