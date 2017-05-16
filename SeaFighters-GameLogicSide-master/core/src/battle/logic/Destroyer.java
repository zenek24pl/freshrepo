package battle.logic;

public class Destroyer extends Ship {

	public Destroyer(int posX, int posY,int direction){
		this.setPosX(posX);
		this.setPosY(posY);
		this.setSize(2);
		this.setDirection(direction);
		this.setSink(false);
	}
}
