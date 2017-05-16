package battle.ships;

import battle.core.Ship;

public class TwoPlacesShip extends Ship {

	public TwoPlacesShip(int posX, int posY, int direction){
		this.setPosX(posX);
		this.setPosY(posY);
		this.setSize(2);
		this.setDirection(direction);
		this.setSink(false);
	}
}
