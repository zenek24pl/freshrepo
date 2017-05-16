package battle.rmi;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;


public class BattleShipImpl extends UnicastRemoteObject implements BattleShip {

	// Must have explicit constructor to declare RemoteException
	public BattleShipImpl() throws RemoteException {
		super();
	}

	public long add(long a, long b) throws RemoteException {
		return a + b;
	}

	public long sub(long a, long b) throws RemoteException {
		return a - b;
	}

}
