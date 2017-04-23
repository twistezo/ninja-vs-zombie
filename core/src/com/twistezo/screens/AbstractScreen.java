package com.twistezo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.twistezo.NinjaGame;

/**
 * @author twistezo (23.04.2017)
 */

public abstract class AbstractScreen implements Screen {
    protected NinjaGame game;
    protected Stage stage;
    private OrthographicCamera camera;
    protected SpriteBatch spriteBatch;

    public AbstractScreen(NinjaGame game) {
        this.game = game;
        createCamera();
        stage = new Stage(new StretchViewport(NinjaGame.SCREEN_WIDTH, NinjaGame.SCREEN_HEIGHT, camera));
        spriteBatch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        clearScreen();
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        game.setPaused(true);
    }

    @Override
    public void resume() {
        game.setPaused(false);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        game.dispose();
    }

    private void createCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, NinjaGame.SCREEN_WIDTH, NinjaGame.SCREEN_HEIGHT);
        camera.update();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
