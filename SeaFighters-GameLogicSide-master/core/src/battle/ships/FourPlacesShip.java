package battle.ships;

import battle.core.Ship;

public class FourPlacesShip extends Ship {

	public FourPlacesShip(int posX, int posY, int direction){
		this.setPosX(posX);
		this.setPosY(posY);
		this.setSize(4);
		this.setDirection(direction);
		this.setSink(false);
	}
}
