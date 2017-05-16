package battle.gui;

import battle.logic.GestureHandler;
import battle.logic.MyGame;
import battle.logic.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PlayScreen implements Screen,
		InputProcessor {
	
	private SpriteBatch batch;

	 Texture img;
	 Texture ship;
	 Texture hit;
	 Texture miss;
	 Texture blank;

	 BitmapFont font;
	 
	 FreeTypeFontGenerator generator;

	 FreeTypeFontParameter parameter;

	 String nShips;
	
	 GestureHandler gestureHandler;
	
	 MyGame game;

	 Viewport viewport;
	 
	 ShapeRenderer shapeRenderer;
	
	 Sprite sprite;
	 
	 Texture playScreenImage;
	 
	 //FileHandle fontFile;
	 
	 
	public final static int WIDTH = 10;
	public final static int HEIGHT = 16;

	public PlayScreen(MyGame game)
	{
		this.game = game;
		
		batch = new SpriteBatch();
		
		
		//fontFile=getFontFile("SUNN.otf");
				
		//font= new BitmapFont(Gdx.files.internal("SUNN.ttf"));
		
		//FreeTypeFontGenerator da;
		
		generator = new FreeTypeFontGenerator(Gdx.files.internal("MODERNE SANS.ttf"));
		parameter = new FreeTypeFontParameter();
		
	//	parameter.size = (int)Math.ceil(25);
		generator.scaleForPixelHeight((int)Math.ceil(16));
		
		parameter.minFilter = Texture.TextureFilter.Nearest;
		parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		
		parameter.size = 15;
		font = generator.generateFont(parameter); // font size 12 pixels
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		generator.dispose();
		
		playScreenImage = new Texture("413.jpg");
		batch = new SpriteBatch();
		sprite = new Sprite(playScreenImage);
		
		ship = new Texture("32.png");
		hit = new Texture("36tes3t.jpg");
		miss = new Texture("36test6.jpg");
		blank = new Texture("30.png"); //6 7 15 17 18
	
		//font = new BitmapFont();
		
		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(false,WIDTH,HEIGHT);
		viewport = new FitViewport(WIDTH,HEIGHT,camera);
		viewport.apply();
		
		//this.game.getPlayer1().randomBoard();
		this.game.getPlayer2().randomBoard();

		//this.game.getPlayer1().getOwnBoard().printBoard();
		
		gestureHandler=new GestureHandler(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		
		gestureHandler.setScreen(2);
		shapeRenderer = new ShapeRenderer();
		
	}

	@Override
	public void render(float delta) {
		
		batch.setProjectionMatrix(viewport.getCamera().combined);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		sprite.setPosition(0, 0);
		sprite.setSize(10, 12);
		
		batch.begin();
			sprite.draw(batch);
		batch.end();
		
		gestureHandler.setWidth(Gdx.graphics.getWidth());
		gestureHandler.setHeight(Gdx.graphics.getHeight());
		
		//viewport.apply();
			
		
		game.gesture(gestureHandler);

		System.out.println("x:"+gestureHandler.getX()+" y: "+gestureHandler.getY());
		
		//Gdx.gl.glClearColor(0, 0, 0, 1);	
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
		
		//shapeRenderer.begin(ShapeType.Filled);
		//	shapeRenderer.setColor(1, 1, 1, 1);
			//shapeRenderer.rect(0, 0, WIDTH, HEIGHT);
	//	shapeRenderer.end();
		
		
		batch.begin();
			//Enemy board/main board
			drawTable(game.getPlayer2(),1);
		batch.end();
		
		
	
		batch.getProjectionMatrix().translate(0, WIDTH+1, 0);
		
		batch.begin();
			//Own board/small board
			drawTable(game.getPlayer1(),0.5f);
		batch.end();
		
		batch.getProjectionMatrix().translate(5f,4.8f,0);
		batch.getProjectionMatrix().scale(0.05f, 0.05f, 0f);
		
		batch.begin();
			drawInfo();
		batch.end();
		
		int sizes[]={5,4,3,3,2};
		shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1,1,1,1);
		
		for(int j=0; j < 5; j++){
			for (int i=0; i < sizes[j]; i++)
				shapeRenderer.rect(7.2f+i*0.5f-sizes[j]*0.25f+0.25f,14.5f-j*0.8f, 0.5f,0.5f);
		}
		shapeRenderer.end();
		
		batch.getProjectionMatrix().translate(-50f,-100f,0);
		
		batch.begin();
		if (game.getPlayer2().checkLost()){
			System.out.println("PLAYER 1 WINS");
			nShips="PLAYER 1 WINS";
			font.draw(batch, nShips,0,0);
		}else if (game.getPlayer1().checkLost()){
			nShips="PLAYER 2 WINS";
			font.draw(batch, nShips,0,0);
		}
		
		
		batch.end();
	
	}
	
	void drawInfo(){
		
		float inc= (1/0.06f);
		
		
		
		font.draw(batch, "YOU",5,0);
		font.draw(batch, "ENEMY",50,0);
		
		for (int i = 0; i < 5; i++) {

			if (game.getPlayer1().getShips().get(i).isSink())
				nShips = "0";
			else
				nShips = "1";
			
			font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

			font.draw(batch, nShips,10,-i*inc-15);
			
			if (game.getPlayer2().getShips().get(i).isSink())
				nShips = "0";
			else
				nShips = "1";

			font.draw(batch, nShips, 5*inc,-i*inc-15);	
		}
		
		
		
	}
	
	void drawTable(Player player, float scale)
	{
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {

				if (player.getOwnBoard().getBoard()[j][i] == 0
						|| player.getOwnBoard().getBoard()[j][i] == 1
						|| player.getOwnBoard().getBoard()[j][i] == 2
						|| player.getOwnBoard().getBoard()[j][i] == 3
						|| player.getOwnBoard().getBoard()[j][i] == 4) {
					
					if (scale == 1)
						batch.draw(blank, j * scale, (9 - i) * scale, scale, scale);
					else
						batch.draw(ship, j * scale, (9 - i) * scale, scale, scale);
					continue;
				}
				if (player.getOwnBoard().getBoard()[j][i] == 0.5
						|| player.getOwnBoard().getBoard()[j][i] == 1.5
						|| player.getOwnBoard().getBoard()[j][i] == 2.5
						|| player.getOwnBoard().getBoard()[j][i] == 3.5
						|| player.getOwnBoard().getBoard()[j][i] == 4.5) {

					batch.draw(hit, j * scale, +(9 - i) * scale, scale, scale);
					continue;
				}
				if (player.getOwnBoard().getBoard()[j][i] == -9) {
					batch.draw(miss, j * scale, (9 - i) * scale, scale, scale);
					continue;
				}

				if (player.getOwnBoard().getBoard()[j][i] == -8) {
					batch.draw(blank, j * scale, (9 - i) * scale, scale, scale);
					continue;
				}
			}
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		// font.dispose();
	}

	@Override
	public void resize(int width, int height){
		viewport.update(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	/*
	 * @Override public boolean touchDown(float x, float y, int pointer, int
	 * button) { // TODO Auto-generated method stub return true; }
	 * 
	 * @Override public boolean tap(float x, float y, int count, int button) {
	 * 
	 * message = "Tap performed, finger" + Integer.toString(button);
	 * 
	 * int inc=width/10; int inc1=inc-1;
	 * 
	 * if( x <= width && x >=0 && y >= height-width && y <= height){ X=(int)
	 * (x/(inc)); Y=(int) (y-(height-width))/inc;
	 * System.out.println("x: "+X+" y: "+Y); p1.play(X,Y,p2); }
	 * 
	 * p1.getOwnBoard().printBoard();
	 * 
	 * 
	 * 
	 * return true; }
	 * 
	 * @Override public boolean longPress(float x, float y) { message =
	 * "Long press performed"; return true; }
	 * 
	 * @Override public boolean fling(float velocityX, float velocityY, int
	 * button) { message = "Fling performed, velocity:" +
	 * Float.toString(velocityX) + "," + Float.toString(velocityY); return true;
	 * }
	 * 
	 * @Override public boolean pan(float x, float y, float deltaX, float
	 * deltaY) { message = "Pan performed, delta:" + Float.toString(deltaX) +
	 * "," + Float.toString(deltaY); return true; }
	 * 
	 * @Override public boolean zoom(float initialDistance, float distance) {
	 * message = "Zoom performed, initial Distance:" +
	 * Float.toString(initialDistance) + " Distance: " +
	 * Float.toString(distance); return true; }
	 * 
	 * @Override public boolean pinch(Vector2 initialPointer1, Vector2
	 * initialPointer2, Vector2 pointer1, Vector2 pointer2) { message =
	 * "Pinch performed"; return true; }
	 * 
	 * @Override public boolean panStop(float x, float y, int pointer, int
	 * button) { // TODO Auto-generated method stub return false; }
	 */

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

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

}