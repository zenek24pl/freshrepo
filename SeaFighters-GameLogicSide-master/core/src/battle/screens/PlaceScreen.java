package battle.screens;

import battle.ships.FivePlacesShip;
import battle.ships.FourPlacesShip;
import battle.ships.TwoPlacesShip;
import battle.events.OnClickTouch;
import battle.starter.Seafighter;
import battle.core.Player;
import battle.core.Ship;
import battle.ships.ThreePlacesShip;

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

public class PlaceScreen implements Screen, InputProcessor {

    private Seafighter game;
    private SpriteBatch batch;
    private Sprite sprite;
    private Viewport viewport;
    private int nShip = 0;
    private int size = 0;
    private int direction = 0;
    private Ship tempShip;
    private int tempShipPos = -1;

    private OnClickTouch onClickTouch;
    private ShapeRenderer shapeRenderer;

    private final static int WIDTH = 10;
    private final static int HEIGHT = 16;

    public PlaceScreen(Seafighter game) {
        this.game = game;
        Texture placeShipsImage;
        placeShipsImage = new Texture("menu.jpg");
        batch = new SpriteBatch();
        sprite = new Sprite(placeShipsImage);
        setCamera();

        onClickTouch = new OnClickTouch(Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),
                this.game);
        onClickTouch.setScreen(1);
        shapeRenderer = new ShapeRenderer();
        setFontGen();

    }

    private void setCamera() {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        viewport.apply();
    }

    private void setFontGen() {
        FreeTypeFontGenerator generator;
        FreeTypeFontParameter parameter;
        BitmapFont font;
        generator = new FreeTypeFontGenerator(Gdx.files.internal("MODERNE SANS.ttf"));
        parameter = new FreeTypeFontParameter();
        parameter.size = 22;
        font = generator.generateFont(parameter); // font size 12 pixels
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        generator.dispose();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        onClickTouch.setWidth(Gdx.graphics.getWidth());
        onClickTouch.setHeight(Gdx.graphics.getHeight());
        batch.setProjectionMatrix(viewport.getCamera().combined);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        sprite.setPosition(0, 0);
        sprite.setSize(10, 16);
        batch.begin();
        sprite.draw(batch);
        batch.end();
        buttons();
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
        shapeRenderer.setColor(1, 1, 1, 0.5f);
        dragAndDrop();
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeType.Filled);
        changeDirection();
        shapeRenderer.end();

    }

    private void shipsToPlace() {
        int sizes[] = {5, 4, 3, 3, 2};
        if (nShip < 5)
            for (int i = 0; i < sizes[nShip]; i++)
                shapeRenderer.rect(5.25f + i - sizes[nShip] * 0.5f, 13.5f, 1, 1);
    }

    private void buttons() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeType.Line);
        Gdx.gl20.glLineWidth(6);
        shapeRenderer.setColor(1, 0, 0, 1);

        //button 1
        shapeRenderer.line(0.2f, 0.2f, 4.8f, 0.2f);
        shapeRenderer.line(0.2f, 1.8f, 4.8f, 1.8f);
        shapeRenderer.line(0.2f, 0.2f, 0.2f, 1.8f);
        shapeRenderer.line(4.8f, 0.2f, 4.8f, 1.8f);

        //button 2
        if (game.getPlayer1().getShips().size() < 5)
            shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.line(5.2f, 0.2f, 9.8f, 0.2f);
        shapeRenderer.line(5.2f, 1.8f, 9.8f, 1.8f);
        shapeRenderer.line(5.2f, 0.2f, 5.2f, 1.8f);
        shapeRenderer.line(9.8f, 0.2f, 9.8f, 1.8f);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 0.1f);
        shapeRenderer.rect(0.2f, 0.2f, 4.6f, 1.6f);
        shapeRenderer.rect(5.2f, 0.2f, 4.6f, 1.6f);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
        if (onClickTouch.getAutoButton()) {
            onClickTouch.setAutoButton(false);
            game.getPlayer1().eraseShips();
            game.getPlayer1().getOwnBoard().initBoard();
            game.getPlayer1().randomBoard();
            nShip = 6;
        } else if (onClickTouch.getPlayButton()) {
            if (game.getPlayer1().getShips().size() == 5) {
                onClickTouch.setScreen(2);
                game.initPlay();
                game.setScreen(game.getPlay());
            }
            onClickTouch.setPlayButton(false);
        }
    }


    private void dragAndDrop() {
        Player p1 = game.getPlayer1();
        if (onClickTouch.getPan()) {
            if (!onClickTouch.getGrab() && !onClickTouch.getBlank())
                if (onClickTouch.getX() >= 0 && onClickTouch.getX() <= 9
                        && onClickTouch.getY() >= 0
                        && onClickTouch.getY() <= 9)
                    if (p1.getOwnBoard().getBoard()[onClickTouch.getX()][onClickTouch
                            .getY()] >= 0) {
                        size = p1.getShips().get((int) p1.getOwnBoard().getBoard()[onClickTouch.getX()][onClickTouch.getY()]).getSize();
                        direction = p1.getShips().get((int) p1.getOwnBoard().getBoard()[onClickTouch.getX()][onClickTouch.getY()]).getDirection();
                        onClickTouch.setGrab(true);
                        tempShipPos = (int) p1.getOwnBoard().getBoard()[onClickTouch.getX()][onClickTouch.getY()];
                        tempShip = p1.getShips().get(tempShipPos);
                        tempShip.cleanBoard(p1.getOwnBoard());
                        onClickTouch.setGrab(true);
                    } else {
                        onClickTouch.setBlank(true);
                    }
            if (onClickTouch.getGrab())
                if (direction == 0)
                    for (int i = 0; i < size; i++)
                        shapeRenderer.rect(onClickTouch.getX() + i,
                                9 - onClickTouch.getY() + 2, 1, 1);
                else {
                    for (int i = 0; i < size; i++)
                        shapeRenderer.rect(onClickTouch.getX(), 9
                                - onClickTouch.getY() - i + 2, 1, 1);
                }
        }

        if (!onClickTouch.getPan())
            if (onClickTouch.getDeltaX() >= 0
                    && onClickTouch.getDeltaY() >= 0) {
                if (tempShipPos >= 0 && !onClickTouch.getBlank() && onClickTouch.getGrab()) {
                    int oldX = tempShip.getPosX();
                    int oldY = tempShip.getPosY();

                    p1.getShips().remove(tempShipPos);
                    tempShip.setPosX(onClickTouch.getDeltaX());
                    tempShip.setPosY(onClickTouch.getDeltaY());
                    p1.getShips().add(tempShipPos, tempShip);
                    onClickTouch.setX(-1);
                    onClickTouch.setY(-1);

                    if (!p1.validShip(tempShipPos) || !p1.fillBoardShip(tempShipPos)) {
                        p1.getShips().remove(tempShipPos);
                        tempShip.setPosX(oldX);
                        tempShip.setPosY(oldY);
                        p1.getShips().add(tempShipPos, tempShip);
                        p1.validShip(tempShipPos);
                        p1.fillBoardShip(tempShipPos);
                    }
                    tempShipPos = -1;
                }
                onClickTouch.setGrab(false);
                onClickTouch.setBlank(false);
            }


    }

    private void drawLines() {
        shapeRenderer.setColor(1, 1, 1, 1);
        Gdx.gl20.glLineWidth(1);
        for (int j = 0; j <= WIDTH; j++)
            shapeRenderer.line(0, j + 2, 10, j + 2);
        for (int j = 0; j <= WIDTH; j++)
            shapeRenderer.line(j, 2, j, 12);
    }

    private void changeDirection() {
        Player p1 = game.getPlayer1();
        if (!onClickTouch.getBlank() && !onClickTouch.getGrab()) {
            if (p1.getShips().size() > 0)
                if (onClickTouch.getX() >= 0 && onClickTouch.getX() <= 9
                        && onClickTouch.getY() >= 0
                        && onClickTouch.getY() <= 9)
                    if ((int) p1.getOwnBoard().getBoard()[onClickTouch.getX()][onClickTouch
                            .getY()] >= 0)
                        changeShipDir(onClickTouch.getX(),
                                onClickTouch.getY());
        }
    }

    private boolean changeShipDir(int x, int y) {
        Player p1 = game.getPlayer1();
        Ship s1 = p1.getShips().get((int) p1.getOwnBoard().getBoard()[x][y]);
        int pos = (int) p1.getOwnBoard().getBoard()[x][y];
        if (s1.getDirection() == 0) {
            p1.getShips().remove(pos);
            s1.cleanBoard(p1.getOwnBoard());
            s1.setDirection(1);
            p1.getShips().add(pos, s1);
            if (!p1.validShip(pos) || !p1.fillBoardShip(pos)) {
                p1.getShips().remove(pos);
                s1.setDirection(0);
                p1.getShips().add(pos, s1);
                if (!p1.validShip(pos) || !p1.fillBoardShip(pos))
                    return false;
            }
        } else {
            p1.getShips().remove(pos);
            s1.cleanBoard(p1.getOwnBoard());
            s1.setDirection(0);
            p1.getShips().add(pos, s1);
            if (!p1.validShip(pos) || !p1.fillBoardShip(pos)) {
                p1.getShips().remove(pos);
                s1.setDirection(1);
                p1.getShips().add(pos, s1);
                if (!p1.validShip(pos) || !p1.fillBoardShip(pos))
                    return false;
            }

        }
        onClickTouch.setX(-1);
        onClickTouch.setY(-1);
        return true;
    }

    private void placeShip() {
        Player p1 = game.getPlayer1();
        if (!onClickTouch.getPan() && !onClickTouch.getGrab())
            if (nShip < 5)
                if (onClickTouch.getX() >= 0 && onClickTouch.getX() <= 9
                        && onClickTouch.getY() >= 0
                        && onClickTouch.getY() <= 9)
                    if (p1.getOwnBoard().getBoard()[onClickTouch.getX()][onClickTouch
                            .getY()] == -8)
                        if (addShip(onClickTouch.getX(),
                                onClickTouch.getY(), 0, nShip)) {
                            nShip++;
                            onClickTouch.setX(-1);
                            onClickTouch.setY(-1);
                        }

    }

    private void drawShips() {
        Player p1 = game.getPlayer1();
        for (int i = 0; i < p1.getShips().size(); i++) {
            for (int j = 0; j < 10; j++)
                for (int k = 0; k < 10; k++) {
                    float[][] b1 = p1.getOwnBoard().getBoard();
                    if (b1[k][j] >= 0)
                        if (p1.getShips().get((int) b1[k][j]).getDirection() == 0)
                            for (int g = 0; g < p1.getShips()
                                    .get((int) b1[k][j]).getSize(); g++)
                                shapeRenderer.rect(
                                        p1.getShips().get((int) b1[k][j])
                                                .getPosX()
                                                + g,
                                        9 - p1.getShips().get((int) b1[k][j])
                                                .getPosY() + 2, 1, 1);
                        else
                            for (int g = 0; g < p1.getShips()
                                    .get((int) b1[k][j]).getSize(); g++)
                                shapeRenderer.rect(
                                        p1.getShips().get((int) b1[k][j])
                                                .getPosX(),
                                        9
                                                - p1.getShips()
                                                .get((int) b1[k][j])
                                                .getPosY() - g + 2, 1, 1);
                }
        }
    }

    private boolean addShip(int x, int y, int dir, int ship) {
        Ship[] createShips = {new FivePlacesShip(x, y, dir),
                new FourPlacesShip(x, y, dir), new ThreePlacesShip(x, y, dir),
                new ThreePlacesShip(x, y, dir), new TwoPlacesShip(x, y, dir)};
        Player p1 = game.getPlayer1();
        p1.getShips().add(createShips[ship]);
        if (!p1.validShip(ship) || !p1.fillBoardShip(ship)) {
            Ship s1 = p1.getShips().get(ship);
            p1.getShips().remove(ship);
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
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
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
}
