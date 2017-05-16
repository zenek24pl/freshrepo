package battle.ships;

import battle.core.Ship;

public class FivePlacesShip extends Ship {
	
	public FivePlacesShip(int posX, int posY, int direction){
		this.setPosX(posX);
		this.setPosY(posY);
		this.setSize(5);
		this.setDirection(direction);
		this.setSink(false);
	}
	
}
