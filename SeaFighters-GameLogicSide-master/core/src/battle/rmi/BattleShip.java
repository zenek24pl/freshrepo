package battle.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BattleShip extends Remote {
	public long add(long a, long b) throws RemoteException;

	public long sub(long a, long b) throws RemoteException;
}
