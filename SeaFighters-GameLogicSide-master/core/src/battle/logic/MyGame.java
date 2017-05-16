package battle.logic;

import battle.gui.PlaceScreen;
import battle.gui.PlayScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class MyGame extends Game{

	Screen menu;
	Screen place;
	Screen play;
	
	private Player player1;
	private Player player2;
	private int height;
	private int width;

	@Override
	public void create() {
		setPlayer1(new Player());
		setPlayer2(new Player());
		
		//menu = new MainMenuScreen(this);
        place = new PlaceScreen(this);
       
       
        setScreen(place);
	
	}
	
	public void initPlay(){
		 play = new PlayScreen(this);
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}
	
	public void gesture(GestureHandler gestureHandler){
		
		if (gestureHandler.getX() >= 0 && gestureHandler.getY() >= 0){
			this.getPlayer1().play(gestureHandler.getX(),gestureHandler.getY(),this.getPlayer2());
		}
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public Screen getPlay(){
		return play;
	}
	
}
