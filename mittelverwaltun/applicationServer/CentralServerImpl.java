package applicationServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class CentralServerImpl extends UnicastRemoteObject implements CentralServer {

	Server server;
	
	protected CentralServerImpl() throws RemoteException {
		super();
	}

	protected CentralServerImpl( Server server ) throws RemoteException {
		super();
		this.server = server;
	}
	
	public ApplicationServer getMyApplicationServer() {
		return new ApplicationServerImpl();
	}

	public ApplicationServer getMyApplicationServer( String hostName, String hostAdress ) {
		if( server != null ) {
			return server.getNewApplicationServer( hostName, hostAdress );
		}
		
		return null;
	}
	
	public void addBenutzerNameToUser( int id, String name ) throws RemoteException {
		if( server != null ) {
			server.addBenutzerNameToUser( id, name );
		}
	}
	
	public void delUser( int id ) throws RemoteException {
		if( server != null ) {
			server.delUser( id );
		}
	}
}
