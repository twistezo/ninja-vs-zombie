package com.twistezo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.twistezo.NinjaGame;
import com.twistezo.utils.PlayerScore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author twistezo (24.04.2017)
 */

public class HighScoreScreen extends AbstractScreen {
    private final int TABLE_SPACE = 5;
    private Skin uiSkin;
    private Texture background;
    private Image backgroundImg;
    private Label testLabel;
    private Label.LabelStyle labelCustomFont;
    private ArrayList<Label> playerScoreLabelList;
    private ArrayList<PlayerScore> playerScoreList;
    private Table table;
    private MyButton buttonLeft;
    private Texture buttonLeftTexture;

    public HighScoreScreen(NinjaGame game) {
        super(game);
        init();
    }

    @Override
    protected void init() {
        initSkinAndCustomFont();
        initLists();
        initBackground();
        initBackButton();
        initTestPlayersScores();
        sortByScore();
        cutScoresList(5);
        generateLabelsFromPlayersScores();
        initTable();
    }

    private void initSkinAndCustomFont() {
        uiSkin = new Skin(Gdx.files.internal("default skin/uiskin.json"));
        FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("CHILLER.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 30;
        BitmapFont fontTitle = freeTypeFontGenerator.generateFont(param);
        labelCustomFont = new Label.LabelStyle(fontTitle, Color.WHITE);
    }

    private void initLists() {
        playerScoreLabelList = new ArrayList<>();
        playerScoreList = new ArrayList<>();
    }

    private void initBackground() {
        background = new Texture("menu/background-highscore.png");
        backgroundImg = new Image(background);
        backgroundImg.setPosition(0, 0);
        stage.addActor(backgroundImg);
    }

    private void initBackButton() {
        final int OFFSET = 25;
        buttonLeftTexture = new Texture("button-left.png");
        buttonLeft = new MyButton(buttonLeftTexture, buttonLeftTexture);
        buttonLeft.setPosition(NinjaGame.SCREEN_WIDTH - buttonLeft.getWidth() - OFFSET, OFFSET);
        stage.addActor(buttonLeft);
    }

    private void initTestPlayersScores() {
        addNewScore("John Smith", 1234);
        addNewScore("Adam B", 4321);
        addNewScore("Lorem Ips", 2);
        addNewScore("a", 999999);
        addNewScore("b", 0);
        addNewScore("c", 1);
        addNewScore("d", 154);
        addNewScore("e", 65);
    }

    private void sortByScore() {
        Collections.sort(playerScoreList, new Comparator<PlayerScore>() {
            @Override
            public int compare(PlayerScore p1, PlayerScore p2) {
                return p2.getScore() - p1.getScore();
            }
        });
    }

    private void cutScoresList(int numberOfPositions) {
        while (playerScoreList.size() > numberOfPositions) {
            playerScoreList.remove(
                    playerScoreList.get(playerScoreList.size()-1));
        }
    }

    private void generateLabelsFromPlayersScores() {
        for(int i=0; i<playerScoreList.size(); i++) {
            playerScoreLabelList.add(
                    new Label(
                            playerScoreList.get(i).getName()
                                    + " - "
                                    +playerScoreList.get(i).getScore()
                                    + " pts"
                            ,uiSkin));
        }
    }

    private void backButtonListeners() {
        buttonLeft.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                NinjaGame.setMenuScreen();
                return super.touchDown(event, x, y, pointer, button);
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                NinjaGame.setMenuScreen();
            }
        });
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            NinjaGame.setMenuScreen();
        }
    }
    private void initTable() {
        table = new Table(uiSkin);
        Label title = new Label("FIVE BEST KILLERS", uiSkin);
        title.setStyle(labelCustomFont);
        table.add(title).spaceBottom(TABLE_SPACE);
        table.row();

        for(int i=0; i<playerScoreLabelList.size(); i++) {
            table.add(playerScoreLabelList.get(i)).spaceBottom(TABLE_SPACE).align(Align.left);
            playerScoreLabelList.get(i).setStyle(labelCustomFont);
            table.row();
        }
        table.left().top();
        table.setPosition(50, 50);
        table.setSize(NinjaGame.SCREEN_WIDTH/3, NinjaGame.SCREEN_HEIGHT-190);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update();
        spriteBatch.begin();
        stage.draw();
        spriteBatch.end();
        backButtonListeners();
    }

    private void update() {
        stage.act();
    }

    public void addNewScore(String name, int score) {
        playerScoreList.add(new PlayerScore(name, score));
    }


}
