package battle.starter;

import battle.events.OnClickTouch;
import battle.core.Player;
import battle.core.Ship;
import battle.screens.PlaceScreen;
import battle.screens.PlayScreen;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Seafighter extends Game{

	Screen menu;
	Screen place;
	Screen play;

	private Socket socket;

	private Player player1;
	private Player player2;
	private int height;
	private int width;

	public int playerID;
	public boolean turn = true;

	public boolean value = false;


	@Override
	public void create() {
		setPlayer1(new Player());
		setPlayer2(new Player());
        place = new PlaceScreen(this);
		socketEvents();
        setScreen(place);	}

	public void socketEvents() {

		connectSocket();

		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIO", "Connected");
			}
		}).on("newPlayer", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					int playersNumber = data.getInt("players");
					Gdx.app.log("SocketIO", ""+playersNumber);
					socket.emit("test", data);
				} catch (JSONException e){
					Gdx.app.log("SocketIO", "Error");
				}}
		}).on("2playerConnected", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIO", "WTF");
				JSONObject data = (JSONObject) args[0];
				try{
					Ship ship;
					int[] shipsSize = {5,4,3,3,2};
					String json = data.getString("ships");
					if (json != null && json.length() > 0){
						String[] ships = json.split(",,");
						Gdx.app.log("SocketIO", "" + "aaaa");

						ArrayList<Ship> shipsList = new ArrayList<Ship>();
						for (int i = 0; i<5;i++){
							String[] shipParams = ships[i].split(",");
							ship = new Ship();
							ship.setPosX(Integer.valueOf(shipParams[0]));
							ship.setPosY(Integer.valueOf(shipParams[1]));
							ship.setDirection(Integer.valueOf(shipParams[2]));
							ship.setSize(shipsSize[i]);
							shipsList.add(ship);
						}
						setPlayer2(new Player(shipsList));
					}
					Gdx.app.log("SocketIO", "" + "gfhf");
				} catch (JSONException e){
					Gdx.app.log("SocketIO", "Error parse");
				}
			}
		}).on("getID", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					playerID = data.getInt("id");
					Gdx.app.log("SocketIO", "" + playerID);
				} catch (JSONException e){
					Gdx.app.log("SocketIO", "Error");
				}}
		}).on("playersReady", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				value = true;
			}
		}).on("enemyMove", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					int x = data.getInt("x");
					int y = data.getInt("y");
					float value = BigDecimal.valueOf(data.getDouble("value")).floatValue();
					getPlayer1().getOwnBoard().setBoardElement(x,y,value);
				} catch (JSONException e){
					Gdx.app.log("SocketIO", "Error");
				}}
		}).on("turnChange", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				//JANUSZ OF PROGRAMMING HITS AGAIN
				//AKA STATIC VARS
				OnClickTouch.turn = true;
			}
		}).on("enemyWin", new Emitter.Listener() {
			@Override
			public void call(Object... args) {

			}
		});
	}

	public void getEnemy(){
		socket.emit("getShips", playerID);
	}

	public void emitTurnChange(){

		socket.emit("turnChange");
	}

	public void stopThreadJanuszProgrammerXDDDD(){
		//JANUSZ OF PROGRAMMING HITS AGAIN
		while(!this.turn)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void enemyMove(float x, float y, float value){
		JSONObject data = new JSONObject();
		try{
			data.put("x",(int) x);
			data.put("y",(int) y);
			data.put("value",(double) value);
			socket.emit("enemyMove", data);
		} catch (JSONException e){
			Gdx.app.log("SocketIO", "Error init");
		}

	}

	public void initPlayersShip(){
		JSONObject data = new JSONObject();
		String json = "";
		try {
			for (int i =0; i<player1.getShips().size();i++){
				Ship ship = player1.getShips().get(i);
				json += ship.getPosX() + "," + ship.getPosY() + "," + ship.getDirection() + ",,";

			}
			data.put("ships", json);
			socket.emit("init", data);
		}	catch (JSONException e) {
			Gdx.app.log("SocketIO", "Error init");
		}
	}

	public void connectSocket(){
		try {
			socket = IO.socket("http://10.0.2.2:8080");
			socket.connect();
		} catch(Exception e){
			System.out.println(e);
		}
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
	
	public void gesture(OnClickTouch onClickTouch){
		
		if (onClickTouch.getX() >= 0 && onClickTouch.getY() >= 0){
			this.getPlayer1().play(onClickTouch.getX(), onClickTouch.getY(),this.getPlayer2());
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
