package applicationServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CentralServer extends Remote {

	public ApplicationServer getMyApplicationServer() throws RemoteException;

	public ApplicationServer getMyApplicationServer( String hostName, String hostAdress ) throws RemoteException;
	public void addBenutzerNameToUser( int id, String name ) throws RemoteException;
	public void delUser( int id ) throws RemoteException;
}
