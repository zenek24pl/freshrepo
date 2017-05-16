package battle.gui;

import battle.logic.AircraftCarrier;
import battle.logic.BattleShip;
import battle.logic.Cruiser;
import battle.logic.Destroyer;
import battle.logic.GestureHandler;
import battle.logic.MyGame;
import battle.logic.Player;
import battle.logic.Ship;
import battle.logic.Submarine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PlaceScreen implements Screen, InputProcessor {

	MyGame game;
	Texture placeShipsImage;
	SpriteBatch batch;
	Sprite sprite;
	float aspectRatio;
	OrthographicCamera camera;
	Viewport viewport;
	int nShip = 0;
	int size = 0;
	int direction = 0;
	Ship tempShip;
	int tempShipPos=-1;

	GestureHandler gestureHandler;

	ShapeRenderer shapeRenderer;
	
	FreeTypeFontGenerator generator;

	FreeTypeFontParameter parameter;
	 
	BitmapFont font;
	 

	public final static int WIDTH = 10;
	public final static int HEIGHT = 16;

	public PlaceScreen(MyGame game) {
		this.game = game;

		placeShipsImage = new Texture("41.jpg");
		batch = new SpriteBatch();
		sprite = new Sprite(placeShipsImage);

		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		viewport = new FitViewport(WIDTH, HEIGHT, camera);
		viewport.apply();

		gestureHandler = new GestureHandler(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		gestureHandler.setScreen(1);
		
		shapeRenderer = new ShapeRenderer();
		

		generator = new FreeTypeFontGenerator(Gdx.files.internal("MODERNE SANS.ttf"));
		parameter = new FreeTypeFontParameter();
		

		parameter.size = 22;
		font = generator.generateFont(parameter); // font size 12 pixels
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		generator.dispose();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		gestureHandler.setWidth(Gdx.graphics.getWidth());
		gestureHandler.setHeight(Gdx.graphics.getHeight());

		// viewport.apply();
		batch.setProjectionMatrix(viewport.getCamera().combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

		/*
		 * shapeRenderer.begin(ShapeType.Filled); shapeRenderer.setColor(1, 0,
		 * 0, 1); shapeRenderer.rect(0, 0, WIDTH, HEIGHT); shapeRenderer.rect(0,
		 * 0, 10, 10); shapeRenderer.end();
		 */

		sprite.setPosition(0, 0);
		sprite.setSize(10, 16);

		
		batch.begin();
			sprite.draw(batch);
		batch.end();

	
		buttons();
		
		
	/*	batch.begin();
		font.setColor(1, 1, 1, 1);
			font.draw(batch, "1", 5,5);
		batch.end();*/
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(255 / 255f, 255 / 255f, 255 / 255f, 0.5f);
			shipsToPlace();
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Line);
			drawLines();
		shapeRenderer.end();

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(255 / 255f, 255 / 255f, 255 / 255f, 1);
		drawShips();
		shapeRenderer.end();

		placeShip();

		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1,1,1, 0.5f);
			dragAndDrop();
		shapeRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		shapeRenderer.begin(ShapeType.Filled);
		// shapeRenderer.setColor(1,0,0, 1); TODO: quando clica e nao da para
		// girar mudar cor
		changeDirection();
		shapeRenderer.end();
		
	}

	public void shipsToPlace(){
		
		int sizes[]={5,4,3,3,2};
		
		if (nShip < 5)
			for (int i=0; i < sizes[nShip]; i++)
				shapeRenderer.rect(5.25f+i-sizes[nShip]*0.5f,13.5f, 1, 1);
			
	}
	
	public void buttons(){
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    
		shapeRenderer.begin(ShapeType.Line);
		Gdx.gl20.glLineWidth(1);
		shapeRenderer.setColor(1,1,1,1);
		
		//button 1
		shapeRenderer.line(0.2f,0.2f,4.8f, 0.2f);
		shapeRenderer.line(0.2f,1.8f,4.8f, 1.8f);
		
		shapeRenderer.line(0.2f,0.2f,0.2f, 1.8f);
		shapeRenderer.line(4.8f,0.2f,4.8f, 1.8f);
		

		//button 2
		if( game.getPlayer1().getShips().size() < 5)
			shapeRenderer.setColor(1,0,0,1);
		
		shapeRenderer.line(5.2f,0.2f,9.8f, 0.2f);
		shapeRenderer.line(5.2f,1.8f,9.8f, 1.8f);
		
		shapeRenderer.line(5.2f,0.2f,5.2f, 1.8f);
		shapeRenderer.line(9.8f,0.2f,9.8f, 1.8f);
		
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(1,1,1,0.1f);
			shapeRenderer.rect(0.2f,0.2f, 4.6f, 1.6f);
			shapeRenderer.rect(5.2f,0.2f, 4.6f, 1.6f);
		shapeRenderer.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		if (gestureHandler.getAutoButton()){
			gestureHandler.setAutoButton(false);
			game.getPlayer1().eraseShips();
			
			System.out.println(game.getPlayer1().getShips().size());
			
			game.getPlayer1().getOwnBoard().initBoard();
			game.getPlayer1().randomBoard();
			
			nShip=6;
		}else if (gestureHandler.getPlayButton()){
			if( game.getPlayer1().getShips().size() == 5){
			
				gestureHandler.setScreen(2);
				game.initPlay();
				game.setScreen(game.getPlay());
			}
			gestureHandler.setPlayButton(false);
			
		}
		
		
		
	}
	
	
	public void dragAndDrop() {

		Player p1 = game.getPlayer1();
		
		if (gestureHandler.getPan()) {
			//shapeRenderer.setColor(1, 0, 0, 1);

			// System.out.println("DEKTA:"+gestureHandler.getX()+" "+(9-gestureHandler.getY()));

			if (!gestureHandler.getGrab() && !gestureHandler.getBlank())
				if (gestureHandler.getX() >= 0 && gestureHandler.getX() <= 9
						&& gestureHandler.getY() >= 0
						&& gestureHandler.getY() <= 9)
					if (p1.getOwnBoard().getBoard()[gestureHandler.getX()][gestureHandler
							.getY()] >= 0) {
						size = p1.getShips().get((int) p1.getOwnBoard().getBoard()[gestureHandler.getX()][gestureHandler.getY()]).getSize();
						direction = p1.getShips().get((int) p1.getOwnBoard().getBoard()[gestureHandler.getX()][gestureHandler.getY()]).getDirection();gestureHandler.setGrab(true);
						
						tempShipPos=(int) p1.getOwnBoard().getBoard()[gestureHandler.getX()][gestureHandler.getY()];
						
						tempShip=p1.getShips().get(tempShipPos);
						
					//	p1.getShips().remove(tempShipPos);
						
						tempShip.cleanBoard(p1.getOwnBoard());
						
						gestureHandler.setGrab(true);
						
					} else {
						gestureHandler.setBlank(true);
					}
				

			if (gestureHandler.getGrab())
				if (direction == 0)
					for (int i = 0; i < size; i++)
						shapeRenderer.rect(gestureHandler.getX() + i,
								9 - gestureHandler.getY()+2, 1, 1);
				else {
					for (int i = 0; i < size; i++)
						shapeRenderer.rect(gestureHandler.getX(), 9
								- gestureHandler.getY() - i+2, 1, 1);
				}

			// shapeRenderer.rect(gestureHandler.getX(),
			// 9-gestureHandler.getY(), 1, 1);
		}

		if (!gestureHandler.getPan())
			if (gestureHandler.getDeltaX() >= 0
					&& gestureHandler.getDeltaY() >= 0) {
				
					//shapeRenderer.setColor(0, 1, 0, 1);
					
				//	shapeRenderer.rect(gestureHandler.getDeltaX(),9 - gestureHandler.getDeltaY(), 1, 1);
					
					
					
				if (tempShipPos >= 0 && !gestureHandler.getBlank() && gestureHandler.getGrab()){
					//gestureHandler.setPaned(true);
					
					//Ship s1 = tempShip;
					
					int oldX=tempShip.getPosX();
					int oldY=tempShip.getPosY();
					
					p1.getShips().remove(tempShipPos);
					
					System.out.println("VALUES1: "+tempShip.getPosX()+" "+tempShip.getPosY());
				//	System.out.println("VALUES: "+s1.getPosX()+" "+tempShip.getPosX());
				//	System.out.println("VALUES: "+s1.getPosY()+" "+tempShip.getPosY());
					
					tempShip.setPosX(gestureHandler.getDeltaX());
					tempShip.setPosY(gestureHandler.getDeltaY());
					
					p1.getShips().add(tempShipPos, tempShip);
					
					System.out.println("VALUES2: "+tempShip.getPosX()+" "+tempShip.getPosY());
					
					
					gestureHandler.setX(-1);
					gestureHandler.setY(-1);
					
					if (!p1.validShip(tempShipPos) || !p1.fillBoardShip(tempShipPos)) {
	

						//p1.getShips().get(tempShipPos).cleanBoard(p1.getOwnBoard());
						p1.getShips().remove(tempShipPos);
						
						tempShip.setPosX(oldX);
						tempShip.setPosY(oldY);
						
						System.out.println("VALUES3: "+tempShip.getPosX()+" "+tempShip.getPosY());
						
						p1.getShips().add(tempShipPos, tempShip);

						
						p1.validShip(tempShipPos);
						p1.fillBoardShip(tempShipPos);
			
						
					}
					
					tempShipPos=-1;

			}	
					gestureHandler.setGrab(false);
					gestureHandler.setBlank(false);
					
			}
		

	}

	public void drawLines() {
		shapeRenderer.setColor(1, 1, 1, 1);
		Gdx.gl20.glLineWidth(1);
		
		for (int j = 0; j <= WIDTH; j++)
			shapeRenderer.line(0, j+2, 10, j+2);

		for (int j = 0; j <= WIDTH; j++)
			shapeRenderer.line(j, 2, j, 12);

		

	}

	public void changeDirection() {
		Player p1 = game.getPlayer1();
		
		if (!gestureHandler.getBlank() && !gestureHandler.getGrab()){

			if (p1.getShips().size() > 0)
				if (gestureHandler.getX() >= 0 && gestureHandler.getX() <= 9
						&& gestureHandler.getY() >= 0
						&& gestureHandler.getY() <= 9)
					if ((int) p1.getOwnBoard().getBoard()[gestureHandler.getX()][gestureHandler
							.getY()] >= 0)
						changeShipDir(gestureHandler.getX(),
								gestureHandler.getY());
		}
		// shapeRenderer.rect(gestureHandler.getX(),10-gestureHandler.getY(), 1,
		// 1);

	}

	public boolean changeShipDir(int x, int y) {
		System.out.println("MUDAR DIRECAO");

		Player p1 = game.getPlayer1();
		Ship s1 = p1.getShips().get((int) p1.getOwnBoard().getBoard()[x][y]);

		int pos = (int) p1.getOwnBoard().getBoard()[x][y];

		if (s1.getDirection() == 0) {
			System.out.println("HORIZONTAL");

			p1.getShips().remove(pos);

			s1.cleanBoard(p1.getOwnBoard());

			s1.setDirection(1);

			p1.getShips().add(pos, s1);

			if (!p1.validShip(pos) || !p1.fillBoardShip(pos)) {
				System.out.println("NAO MUDOU1");
				p1.getShips().remove(pos);

				// s1.cleanBoard(p1.getOwnBoard());

				s1.setDirection(0);

				p1.getShips().add(pos, s1);

				if (!p1.validShip(pos) || !p1.fillBoardShip(pos))
					System.out.println("MERDA");

				return false;
			}
		} else {
			System.out.println("VERTICAL");

			p1.getShips().remove(pos);

			s1.cleanBoard(p1.getOwnBoard());

			s1.setDirection(0);

			p1.getShips().add(pos, s1);

			if (!p1.validShip(pos) || !p1.fillBoardShip(pos)) {
				System.out.println("NAO MUDOU2");
				p1.getShips().remove(pos);

				// s1.cleanBoard(p1.getOwnBoard());

				s1.setDirection(1);

				p1.getShips().add(pos, s1);

				if (!p1.validShip(pos) || !p1.fillBoardShip(pos))
					System.out.println("MERDA");

				return false;
			}

		}
		gestureHandler.setX(-1);
		gestureHandler.setY(-1);

		return true;
	}

	public void placeShip() {

		Player p1 = game.getPlayer1();

		if (!gestureHandler.getPan() && !gestureHandler.getGrab())
			if (nShip < 5)
				if (gestureHandler.getX() >= 0 && gestureHandler.getX() <= 9
						&& gestureHandler.getY() >= 0
						&& gestureHandler.getY() <= 9)
					if (p1.getOwnBoard().getBoard()[gestureHandler.getX()][gestureHandler
							.getY()] == -8)
						if (addShip(gestureHandler.getX(),
								gestureHandler.getY(), 0, nShip)) {
							nShip++;
							gestureHandler.setX(-1);
							gestureHandler.setY(-1);
							System.out.println("ADICIONOU BARCO");

						}

	}

	public void drawShips() {
		Player p1 = game.getPlayer1();
		
		//if (!gestureHandler.getGrab())
		for (int i = 0; i < p1.getShips().size(); i++) {
			for (int j = 0; j < 10; j++)
				for (int k = 0; k < 10; k++) {
					float[][] b1 = p1.getOwnBoard().getBoard();

				//	if ( b1[k][j] >= p1.getShips().size())
					//	continue;
					
					if (b1[k][j] >= 0 )
						if (p1.getShips().get((int) b1[k][j]).getDirection() == 0)
							for (int g = 0; g < p1.getShips()
									.get((int) b1[k][j]).getSize(); g++)
								shapeRenderer.rect(
										p1.getShips().get((int) b1[k][j])
												.getPosX()
												+ g,
										9 - p1.getShips().get((int) b1[k][j])
												.getPosY()+2, 1, 1);
						else
							for (int g = 0; g < p1.getShips()
									.get((int) b1[k][j]).getSize(); g++)
								shapeRenderer.rect(
										p1.getShips().get((int) b1[k][j])
												.getPosX(),
										9
												- p1.getShips()
														.get((int) b1[k][j])
														.getPosY() - g+2, 1, 1);

				}
			// p1.getOwnBoard().printBoard();
		}
	}

	public boolean addShip(int x, int y, int dir, int ship) {

		Ship[] createShips = { new AircraftCarrier(x, y, dir),
				new BattleShip(x, y, dir), new Cruiser(x, y, dir),
				new Submarine(x, y, dir), new Destroyer(x, y, dir) };

		Player p1 = game.getPlayer1();

		p1.getShips().add(createShips[ship]);

		if (!p1.validShip(ship) || !p1.fillBoardShip(ship)) {

			Ship s1 = p1.getShips().get(ship);
			p1.getShips().remove(ship);

			// s1.setPosX(x);
			// s1.setPosY(y);

			if (dir == 0)
				s1.setDirection(1);
			else
				s1.setDirection(0);

			p1.getShips().add(ship, s1);

			if (!p1.validShip(ship) || !p1.fillBoardShip(ship)) {
				p1.getShips().remove(ship);
				p1.getOwnBoard().printBoard();
				return false;
			}

		}

		return true;
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		batch.dispose();

	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
