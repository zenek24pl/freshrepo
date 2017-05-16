package battle.rmi;

import java.rmi.Naming;

public class BattleShipClient {
	public static void main(String[] args) {
		try {
			// Locate the remote object and get a remote object
			// reference (proxy)
			BattleShip c = (BattleShip) Naming
					.lookup("rmi://localhost:1099/BattleShipService");
			System.out.println(c.sub(4, 3));
			System.out.println(c.add(4, 5));
		} catch (Exception e) {
			System.out.println("Client exception: " + e);
		}
	}
}