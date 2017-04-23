package com.twistezo.screens;

import com.badlogic.gdx.graphics.Texture;
import com.twistezo.NinjaGame;

/**
 * @author twistezo (23.04.2017)
 */

public class SplashScreen extends AbstractScreen {
    private Texture splashImg;

    public SplashScreen(NinjaGame game) {
        super(game);
        init();
    }

    private void init() {
        // TODO implement better assets loading when game grows
        splashImg = new Texture("badlogic.jpg");
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        spriteBatch.begin();
        spriteBatch.draw(splashImg, 0, 0);
        spriteBatch.end();
    }
}
