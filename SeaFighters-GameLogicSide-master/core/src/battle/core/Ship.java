package battle.core;

public class Ship {
	
	private int posX;
	private int posY;
	private int direction; //0 horinzontal 1 vertical
	private int size;
	private boolean isSink;
	private int blank=-8;

	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	
	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public boolean isSink() {
		return isSink;
	}
	public void setSink(boolean isSink) {
		this.isSink = isSink;
	}

	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public void cleanBoard(battle.board.Board b1){
		
		if (this.direction == 0){
			for (int i=0; i < this.size; i++)
				b1.getBoard()[this.getPosX()+i][this.getPosY()]=blank;
		
		}else{
			for (int i=0; i < this.size; i++)
				b1.getBoard()[this.getPosX()][this.getPosY()+i]=blank;
		}
			
	}
	

}
