package com.twistezo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.twistezo.screens.GameScreen;
import com.twistezo.screens.HighScoreScreen;
import com.twistezo.screens.MenuScreen;

public class GameScreenManager extends Game {
    public final static int SCREEN_WIDTH = 800;
    public final static int SCREEN_HEIGHT = 480;
    public final static String GAME_NAME = "ninja vs zombie";
    private static Game game = null;
    private static Screen menuScreen;
    public static Screen gameScreen;
    private static Screen highscoreScreen;

    public void create() {
        GameScreenManager.game = this;
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

    public static void createNewGameScreen() {
        gameScreen = new GameScreen((GameScreenManager) GameScreenManager.game);
    }

    public static void disposeAndDeleteGameScreen() {
        gameScreen.dispose();
        gameScreen = null;
    }
}
