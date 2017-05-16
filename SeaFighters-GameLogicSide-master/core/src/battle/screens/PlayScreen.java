package battle.screens;

import battle.events.OnClickTouch;
import battle.starter.Seafighter;
import battle.core.Player;

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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PlayScreen implements Screen,
		InputProcessor {

	private int blankCell=-8;
	private int missCell=-9;
	 private SpriteBatch batch;
	 Texture ship,hit,miss,blank,playScreenImage;
	 BitmapFont font;
	 FreeTypeFontGenerator generator;
	 FreeTypeFontParameter parameter;
	 String labelText;
	 OnClickTouch onClickTouch;
	 Seafighter game;
	 Viewport viewport;
	 ShapeRenderer shapeRenderer;
	 Sprite sprite;

	public final static int WIDTH = 10;
	public final static int HEIGHT = 16;

	public PlayScreen(Seafighter game)
	{
		this.game = game;
		this.game.initPlayersShip();
		batch = new SpriteBatch();
		setFont();
		textureLoader();
		batch = new SpriteBatch();
		setCamera();
		set2ndPlayer();
	}
	public void textureLoader(){
		ship = new Texture("ship.png");
		hit = new Texture("hit.jpg");
		miss = new Texture("miss.jpg");
		blank = new Texture("empty.png");
	}
	public void setCamera(){
		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(false,WIDTH,HEIGHT);
		viewport = new FitViewport(WIDTH,HEIGHT,camera);
		viewport.apply();
	}
	public void setFont(){
		generator = new FreeTypeFontGenerator(Gdx.files.internal("MODERNE SANS.ttf"));
		parameter = new FreeTypeFontParameter();
		generator.scaleForPixelHeight((int)Math.ceil(16));
		parameter.minFilter = Texture.TextureFilter.Nearest;
		parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		parameter.size = 15;
		font = generator.generateFont(parameter); // font size 12 pixels
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		generator.dispose();
	}
	public void set2ndPlayer(){
	//	this.game.getPlayer2().randomBoard();

		while(!this.game.value)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.game.getEnemy();


		onClickTouch =new OnClickTouch(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(), this.game);
		onClickTouch.setScreen(2);
		shapeRenderer = new ShapeRenderer();
	}
	@Override
	public void render(float delta) {
		batch.setProjectionMatrix(viewport.getCamera().combined);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		onClickTouch.setWidth(Gdx.graphics.getWidth());
		onClickTouch.setHeight(Gdx.graphics.getHeight());
		game.gesture(onClickTouch);
		if (onClickTouch.getX()>-1) {
			game.enemyMove(onClickTouch.getX()
					, onClickTouch.getY()
					, this.game.getPlayer2()
							.getOwnBoard()
							.getBoard()[onClickTouch.getX()][onClickTouch.getY()]);
		}
		batch.begin();
			//Enemy board/main board
			drawBoard(game.getPlayer2(),1);
		batch.end();
		batch.getProjectionMatrix().translate(0, WIDTH+1, 0);
		batch.begin();
			//Own board/small board
			drawBoard(game.getPlayer1(),0.5f);
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
			labelText ="PLAYER 1 WYGRAL";
			font.draw(batch, labelText,0,0);
		}else if (game.getPlayer1().checkLost()){
			labelText ="PLAYER 2 WYGRAL";
			font.draw(batch, labelText,0,0);
		}
		batch.end();
	}
	
	void drawInfo(){
		float inc= (1/0.06f);
		font.draw(batch, "TY",5,0);
		font.draw(batch, "WROG",45,0);
		for (int i = 0; i < 5; i++) {
			if (game.getPlayer1().getShips().get(i).isSink())
				labelText = "0";
			else
				labelText = "1";
			font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			font.draw(batch, labelText,10,-i*inc-15);
			if (game.getPlayer2().getShips().get(i).isSink())
				labelText = "0";
			else
				labelText = "1";
			font.draw(batch, labelText, 5*inc,-i*inc-15);
		}
	}
	
	void drawBoard(Player player, float scale)
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
				if (player.getOwnBoard().getBoard()[j][i] == missCell) {
					batch.draw(miss, j * scale, (9 - i) * scale, scale, scale);
					continue;
				}
				if (player.getOwnBoard().getBoard()[j][i] == blankCell) {
					batch.draw(blank, j * scale, (9 - i) * scale, scale, scale);
					continue;
				}
			}
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
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

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

}