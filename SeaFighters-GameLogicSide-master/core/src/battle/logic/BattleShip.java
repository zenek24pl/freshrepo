package battle.logic;

public class BattleShip extends Ship {

	public BattleShip(int posX, int posY,int direction){
		this.setPosX(posX);
		this.setPosY(posY);
		this.setSize(4);
		this.setDirection(direction);
		this.setSink(false);
	}
}
