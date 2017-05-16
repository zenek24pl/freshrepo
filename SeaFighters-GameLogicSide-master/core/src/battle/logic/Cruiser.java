package battle.logic;

public class Cruiser extends Ship {

	public Cruiser(int posX, int posY,int direction){
		this.setPosX(posX);
		this.setPosY(posY);
		this.setSize(3);
		this.setDirection(direction);
		this.setSink(false);
	}
}
