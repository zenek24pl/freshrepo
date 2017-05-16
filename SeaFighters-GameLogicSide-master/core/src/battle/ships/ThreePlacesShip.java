package battle.ships;

import battle.core.Ship;

public class ThreePlacesShip extends Ship {

	public ThreePlacesShip(int posX, int posY, int direction) {
		this.setPosX(posX);
		this.setPosY(posY);
		this.setSize(3);
		this.setDirection(direction);
		this.setSink(false);
	}
}
