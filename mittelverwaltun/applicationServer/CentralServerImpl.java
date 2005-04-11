package applicationServer;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

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
	 * Der Name des Central-Servers.
	 */
	public static String CENTRAL_NAME = "";

	/**
	 * Der Dateiname von RMI-Registry.
	 */
	public static String CENTRAL_RMI = "";
	
	/**
	 * Der Classpath wird aus dem Namen vom rmiregistry ermittelt.
	 */
	public static String CENTRAL_CLASSPATH = "";

	/**
	 * Einen CentralServer erzeugen. <br>
	 * Dabei weren gleich alle Namen des ApplicationServers erzeugt. 
	 */
	protected CentralServerImpl() throws RemoteException {
		super();
	}

	/**
	 * Einen CentralServer erzeugen. <br>
	 * Dabei weren gleich alle Namen des ApplicationServers erzeugt. 
	 * @param server = Server, der zur graphischen Verwaltung des CentralServers dient.
	 */
	protected CentralServerImpl( Server server ) throws RemoteException {
		super();
		this.server = server;
	}
	
	/**
	 * Für Testzwecke einen ApplicationServer generieren. Ohne GUI-Anmeldung.
	 */
	public ApplicationServer getMyApplicationServer() throws RemoteException {
		return new ApplicationServer();
	}

	/**
	 * Dem Benutzer einen eigenen ApplicationServer generieren, bie der rmiregistry anmelden.<br>
	 * Der Name des gestarteten ApplicationServers wird an den Benurtzer per Rückgabewert übermittelt.
	 * @param hostName = Der Hostname, wo sich der Benutzer befindet.
	 * @param hostAdress = Die Hostadresse, wo sich der Benutzer befindet.
	 * @return Der Name des gestarteten ApplicationServers, wenn erfolgreich. Sonst wird der Zeiger null zurückgegeben.
	 */
	public ApplicationServer getMyApplicationServer( String hostName, String hostAdress ) throws RemoteException {
		ApplicationServer temp = new ApplicationServer();
		
		if( server != null && temp != null ) {
			server.addNewApplicationServer( hostName, hostAdress, temp.getId() );
		}
		
		return temp;
	}
	
	/**
	 * Dem CentralServer den Benutzernamen des angegebenen ApplicationServers mitteilen.
	 * @param serverName = Der Name des gestarteten ApplicationServers.
	 * @param benutzerName = Der BenutzerName der Benutzers der den ApplicationServer in Anspruch nimmt.
	 */
	public void addBenutzerNameToUser( int serverId, String benutzerName ) throws RemoteException {
		if( server != null ) {
			server.addBenutzerNameToUser( serverId, benutzerName );
		}
	}
	
	/**
	 * Dem CentralServer mitteilen, dass der angegebene ApplicationServer nicht mehr gebraucht wird.
	 * @param serverName = Der Name des nicht mehr benötigten ApplicationServers.
	 */
	public void delUser( int serverId ) throws RemoteException {
		if( server != null ) {
			server.delUser( serverId );
		}
	}
}
