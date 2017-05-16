package battle.logic;

public class Submarine extends Ship {

	public Submarine(int posX, int posY, int direction) {
		this.setPosX(posX);
		this.setPosY(posY);
		this.setSize(3);
		this.setDirection(direction);
		this.setSink(false);
	}
}
