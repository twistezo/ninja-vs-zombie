package com.twistezo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.twistezo.screens.GameScreen;
import com.twistezo.screens.HighScoreScreen;
import com.twistezo.screens.MenuScreen;

/**
 * @author twistezo (23.04.2017)
 */

public class NinjaGame extends Game{
    public final static int SCREEN_WIDTH = 800;
    public final static int SCREEN_HEIGHT = 480;
    public final static String GAME_NAME = "ninja vs zombie";
    private static Game game = null;
    private static Screen menuScreen;
    private static Screen gameScreen;
    private static Screen highscoreScreen;

    @Override
    public void create() {
        NinjaGame.game = this;
        menuScreen = new MenuScreen(this);
        gameScreen = new GameScreen(this);
        highscoreScreen = new HighScoreScreen(this);
        setMenuScreen();
    }

    public static void setMenuScreen() {
        game.setScreen(menuScreen);
    }

    public static void setGameScreen() {
        game.setScreen(gameScreen);
    }

    public static void setHighscoreScreen() {
        game.setScreen(highscoreScreen);
    }
}
