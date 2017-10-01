package com.twistezo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.twistezo.GameScreenManager;

public class MenuScreen extends AbstractScreen {
    private Texture background;
    private Image backgroundImg;
    private MyButton playButton;
    private Texture playButtonUp;
    private Texture playButtonDown;
    private MyButton highscoreButton;
    private Texture highscoreButtonUp;
    private Texture highscoreButtonDown;
    private MyButton exitButton;
    private Texture exitButtonUp;
    private Texture exitButtonDown;

    public MenuScreen(GameScreenManager game) {
        super(game);
        init();
    }

    @Override
    protected void init() {
        initBackground();
        initButtons();
        initTable();
    }

    private void initBackground() {
        background = new Texture("menu/background.png");
        backgroundImg = new Image(background);
        backgroundImg.setPosition(0, 0);
        stage.addActor(backgroundImg);
    }

    private void initButtons() {
        playButtonUp = new Texture("menu/start_1.png");
        playButtonDown = new Texture("menu/start_2.png");
        playButton = new MyButton(playButtonUp, playButtonDown);
        highscoreButtonUp = new Texture("menu/highscore_1.png");
        highscoreButtonDown = new Texture("menu/highscore_2.png");
        highscoreButton = new MyButton(highscoreButtonUp, highscoreButtonDown);
        exitButtonUp = new Texture("menu/exit_1.png");
        exitButtonDown = new Texture("menu/exit_2.png");
        exitButton = new MyButton(exitButtonUp, exitButtonDown);
    }

    private void initTable() {
        int tableSpace = 30;
        Table table = new Table();
        table.add(playButton).spaceBottom(tableSpace);
        table.row();
        table.add(highscoreButton).spaceBottom(tableSpace);
        table.row();
        table.add(exitButton);
        table.left().bottom();
        table.setPosition(50, 80);
        stage.addActor(table);
    }

    private void buttonsListeners() {
        playButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (GameScreenManager.gameScreen != null) {
                    GameScreenManager.setGameScreen();
                } else {
                    GameScreenManager.createNewGameScreen();
                }
            }
        });
        highscoreButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                GameScreenManager.setHighscoreScreen();
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update();
        spriteBatch.begin();
        stage.draw();
        spriteBatch.end();
        buttonsListeners();
    }

    private void update() {
        stage.act();
    }
}
