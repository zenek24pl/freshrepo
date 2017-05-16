package battle.logic;

public class AircraftCarrier extends Ship {
	
	public AircraftCarrier(int posX, int posY,int direction){
		this.setPosX(posX);
		this.setPosY(posY);
		this.setSize(5);
		this.setDirection(direction);
		this.setSink(false);
	}
	
}
