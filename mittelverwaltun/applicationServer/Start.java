/*
 * Created on 13.06.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package applicationServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.rmi.Naming;

public class Start extends JFrame implements ActionListener {

	/**
	 * 
	 * @uml.property name="registry"
	 * @uml.associationEnd 
	 * @uml.property name="registry"
	 */
	JButton registry;

	/**
	 * 
	 * @uml.property name="server"
	 * @uml.associationEnd 
	 * @uml.property name="server"
	 */
	JButton server;

	/**
	 * 
	 * @uml.property name="client"
	 * @uml.associationEnd 
	 * @uml.property name="client"
	 */
	JButton client;

	/**
	 * 
	 * @uml.property name="stop"
	 * @uml.associationEnd 
	 * @uml.property name="stop"
	 */
	JButton stop;

	int clientNo = 1;

	Start (){
		getContentPane().setLayout( new GridLayout( 4, 1 ) );

		registry = new JButton("RMI-Registry starten");
		registry.addActionListener(this);
		getContentPane().add(registry);

		server = new JButton("Server registrieren");
		server.addActionListener(this);
		server.setEnabled(false);
		getContentPane().add(server);

		stop = new JButton("Fenster und evtl. Registry, Server beenden");
		stop.addActionListener(this);
		getContentPane().add(stop);

		setTitle("FBMittelverwaltung CentralServer");
		setLocationRelativeTo(null);
		setSize( 350, 200 );
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==registry) {
			String reg = "C:\\j2sdk1.4.0\\bin\\rmiregistry.exe ";
			reg += "-J-classpath -J\"C:\\j2sdk1.4.0\\lib";
//			String reg = "C:\\Programme\\Java\\j2re1.4.0\\bin\\rmiregistry.exe ";
//			reg += "-J-classpath -J\"C:\\Programme\\Java\\j2re1.4.0\\lib";
			try {
				Runtime.getRuntime().exec(reg);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			registry.setEnabled(false);
			server.setEnabled(true);
		} else if (e.getSource()==server) {
			try {
				Naming.rebind("mittelverwaltung", new CentralServerImpl());
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			server.setEnabled(false);
	
		} else if (e.getSource()==stop) {
			this.dispose();
			System.exit(0);
			
		}
	}

	public static void main(String args[]) {
		new Start().show();
	}

}
