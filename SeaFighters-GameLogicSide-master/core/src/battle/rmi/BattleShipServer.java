package battle.rmi;

import java.rmi.Naming;

public class BattleShipServer {
	public static void main(String args[]) {
		try {
			// create the remote object
			BattleShip c = new BattleShipImpl();
			// expose with a name for client programs
			Naming.rebind("rmi://localhost:1099/BattleShipService", c);
			System.out.println("Server up!");
		} catch (Exception e) {
			System.out.println("Trouble: " + e);
		}
	}
}