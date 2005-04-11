package applicationServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * Interface des CentralServers, der bei der "rmiregistry" angemeldet wird und<br>
 * das anschlie�end jedem Benutzer einen eigenen <code>ApplicationServer</code> bei der "rmiregistry" anmeldet<br>
 * und den Namen des neuen gestarteten ApplicationServers als R�ckgabewert zur�ckliefert.
 * @author w.flat
 */
public interface CentralServer extends Remote {
	
	/**
	 * Dem Benutzer einen eigenen ApplicationServer generieren, bie der rmiregistry anmelden.<br>
	 * Der Name des gestarteten ApplicationServers wird an den Benurtzer per R�ckgabewert �bermittelt.
	 * @param hostName = Der Hostname, wo sich der Benutzer befindet.
	 * @param hostAdress = Die Hostadresse, wo sich der Benutzer befindet.
	 * @return Der Name des gestarteten ApplicationServers, wenn erfolgreich. Sonst wird der Zeiger null zur�ckgegeben.
	 */
	public ApplicationServer getMyApplicationServer( String hostName, String hostAdress ) throws RemoteException;

	/**
	 * F�r Testzwecke einen ApplicationServer generieren. Ohne GUI-Anmeldung.
	 */
	public ApplicationServer getMyApplicationServer() throws RemoteException;

	/**
	 * Dem CentralServer den Benutzernamen des angegebenen ApplicationServers mitteilen.
	 * @param serverName = Der Name des gestarteten ApplicationServers.
	 * @param benutzerName = Der BenutzerName der Benutzers der den ApplicationServer in Anspruch nimmt.
	 */
	public void addBenutzerNameToUser( int serverId, String benutzerName ) throws RemoteException;

	/**
	 * Dem CentralServer mitteilen, dass der angegebene ApplicationServer nicht mehr gebraucht wird.
	 * @param serverName = Der Name des nicht mehr ben�tigten ApplicationServers.
	 */
	public void delUser( int serverId ) throws RemoteException;
}
