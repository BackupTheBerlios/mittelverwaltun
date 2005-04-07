package applicationServer;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Diese Klasse dient zur Verwaltung der ApplicationServer. 
 * @author w.flat
 */
public class CentralServerImpl extends UnicastRemoteObject implements CentralServer {

	/**
	 * Die GUI des CentralServers. 
	 */
	Server server;
	
	/**
	 * Anzahl der zuerstellenden ApplicationServers.
	 */
	final static int NUM_APPLICATION_SERVERS = 100;
	
	/**
	 * Liste mit den Namen der ApplicationServers, die verwendet werden d�rfen, <br>
	 * d.h. dieses Server sind noch nicht gestartet. 
	 */
	ArrayList listFree = new ArrayList();
	
	/**
	 * Liste mit den Namen der ApplicationServers, die zur Zeit von anderen Benutzern verwendet werden. 
	 */
	ArrayList listBusy = new ArrayList();
	
	/**
	 * Einen CentralServer erzeugen. <br>
	 * Dabei weren gleich alle Namen des ApplicationServers erzeugt. 
	 */
	protected CentralServerImpl() throws RemoteException {
		super();
		generateNames();
	}

	/**
	 * Einen CentralServer erzeugen. <br>
	 * Dabei weren gleich alle Namen des ApplicationServers erzeugt. 
	 * @param server = Server, der zur graphischen Verwaltung des CentralServers dient.
	 */
	protected CentralServerImpl( Server server ) throws RemoteException {
		super();
		this.server = server;
		generateNames();
	}
	
//	public ApplicationServer getMyApplicationServer() throws RemoteException {
//		return new ApplicationServerImpl();
//	}

	/**
	 * Dem Benutzer einen eigenen ApplicationServer generieren, bie der rmiregistry anmelden.<br>
	 * Der Name des gestarteten ApplicationServers wird an den Benurtzer per R�ckgabewert �bermittelt.
	 * @param hostName = Der Hostname, wo sich der Benutzer befindet.
	 * @param hostAdress = Die Hostadresse, wo sich der Benutzer befindet.
	 * @return Der Name des gestarteten ApplicationServers, wenn erfolgreich. Sonst wird der Zeiger null zur�ckgegeben.
	 */
	public String getMyApplicationServer( String hostName, String hostAdress ) throws RemoteException {
		if(listFree.size() == 0)	// Wenn keine freien ApplicationServer exisitieren. 
			return null;
		String serverToStart = (String)listFree.get(0);		// Server der gestartet werden soll
		try {
			Naming.rebind(serverToStart, new ApplicationServerImpl(serverToStart));
		} catch(Exception ex) {		// Konnte nicht gestartet werden
			return null;
		}
		// Anmeldung war erfolgreich => 
		listFree.remove(0);				// aus listFree l�schen
		listBusy.add(0, serverToStart);	// und die listBusy einf�gen
			
		if( server != null ) {
			server.addNewApplicationServer( hostName, hostAdress, serverToStart );
		}
		
		return serverToStart;
	}
	
	/**
	 * Dem CentralServer den Benutzernamen des angegebenen ApplicationServers mitteilen.
	 * @param serverName = Der Name des gestarteten ApplicationServers.
	 * @param benutzerName = Der BenutzerName der Benutzers der den ApplicationServer in Anspruch nimmt.
	 */
	public void addBenutzerNameToUser( String serverName, String benutzerName ) throws RemoteException {
		if( server != null ) {
			server.addBenutzerNameToUser( serverName, benutzerName );
		}
	}
	
	/**
	 * Dem CentralServer mitteilen, dass der angegebene ApplicationServer nicht mehr gebraucht wird.
	 * @param serverName = Der Name des nicht mehr ben�tigten ApplicationServers.
	 */
	public void delUser( String serverName ) throws RemoteException {
		if( server != null ) {
			server.delUser( serverName );
		}
		removeServer(serverName);
	}
	
	/**
	 * Den angegebenen ApplicationServer aus der rmiregistry entfernen. 
	 * @param serverName = Der Name des ApplicationServers der entfernt werden soll. 
	 */
	public void removeServer(String serverName) {
		// Den Server aus rmiregistry entfernen. 
		try {
			((ApplicationServer)Naming.lookup("//localhost/" + serverName)).logout();
			Naming.unbind(serverName);
		} catch(Exception ex) {
		}
		// Schleife um den serverName aus der listBusy zu extrahieren
		for(int i = 0; i < listBusy.size(); i++) {
			if(((String)listBusy.get(i)).equalsIgnoreCase(serverName)) {
				listFree.add(listBusy.remove(i));	// Aus der listBusy entfernen und in die listFree einf�gen.
				break;
			}
		}
	}
	
	/**
	 * Alle ApplicationServer aus der rmiregistry entfernen. 
	 */
	public void removeAllServer() {
		// Die Server aus rmiregistry entfernen. 
		for(int i = 0; i < listBusy.size(); i++) {
			try {
				((ApplicationServer)Naming.lookup("//localhost/" + (String)listBusy.get(i))).logout();
				Naming.unbind((String)listBusy.get(i));
			} catch(Exception ex) {
			}
		}
		// Schleife um die listBusy in die listFree zu speichern
		while(listBusy.size() > 0) {
			listFree.add(listBusy.remove(0));	// Aus der listBusy entfernen und in die listFree einf�gen.
		}
	}
	
	/**
	 * Erzeugen aller Namen f�r die ApplicationServer.
	 */
	private void generateNames() {
		for(int i = 1; i < NUM_APPLICATION_SERVERS; i++) {
			listFree.add(new String("mittelverwaltung" + i));
		}
		listBusy.clear();
	}
}
