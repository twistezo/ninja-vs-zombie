package com.twistezo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.twistezo.GameScreenManager;

/**
 * Abstract class for cleaning up every children screen classes
 */

public abstract class AbstractScreen implements Screen {
    protected GameScreenManager game;
    protected Stage stage;
    private OrthographicCamera camera;
    protected SpriteBatch spriteBatch;

    public AbstractScreen(GameScreenManager game) {
        this.game = game;
        createCamera();
        /* Stage for actors */
        stage = new Stage(new StretchViewport(GameScreenManager.SCREEN_WIDTH, GameScreenManager.SCREEN_HEIGHT, camera));
        /* Batch for sprites */
        spriteBatch = new SpriteBatch();
        init();
    }

    protected abstract void init();

    private void createCamera() {
        /* Orthographic means like in CAD drawings */
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameScreenManager.SCREEN_WIDTH, GameScreenManager.SCREEN_HEIGHT);
        camera.update();
    }

    /** Clean screen on black color between render frames */
    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        /* Stage takes user inputs */
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
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
        game.dispose();
    }
}
