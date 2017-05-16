package battle.gui;

import battle.logic.MyGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType.Bitmap;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuScreen implements Screen {

   // private MyGame game;
    private Texture mainMenuImage= new Texture("menu.jpg");
    private SpriteBatch batch= new SpriteBatch();
    private Sprite sprite=new Sprite(mainMenuImage);
    private float aspectRatio = (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();
	private OrthographicCamera camera=new OrthographicCamera(25 * aspectRatio ,25);
	private Viewport viewport;
	final float width = 50;
	final float height = 25;

     public MenuScreen(MyGame game){
            // this.game = game;
     }

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {

		 	sprite.setPosition(0,0);
		    sprite.setSize(50,25);
		    
		    viewport = new FillViewport(Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight(), camera);
		    
		    camera = new OrthographicCamera(25 * aspectRatio ,25);
		    
		    camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
	
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	     camera.update();
	     batch.setProjectionMatrix(camera.combined);
	    
		 batch.begin();
		 	sprite.draw(batch);
	     batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		
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
		// TODO Auto-generated method stub
		
	}
}
