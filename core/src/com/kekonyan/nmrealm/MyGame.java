package com.kekonyan.nmrealm;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MyGame extends ApplicationAdapter {
    Stage gameStage, uiStage;
    Grid grid;
    Actor selectedChip;
    Table helperPanel;
    int selectedX, selectedY;
    Pointer pointer;
    Dialog winDialog, helpDialog;

    private void startGame(){
        selectedChip = null;
        helperPanel.debugCell();
        grid.initGrid();
        pointer.render.setPosition(Const.GLOBAL_TO_TABLE.x + Const.CELL_SIZE * 2.5f - 5, Const.GLOBAL_TO_TABLE.y + Const.CELL_SIZE * 2.5f - 5);
        Gdx.input.setInputProcessor(pointer);
    }
	
	@Override
	public void create () {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Dorsa-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Const.HEIGHT/9;
        parameter.color = Const.GRAY;
        BitmapFont titleFont = generator.generateFont(parameter);
        parameter.size = Const.HEIGHT/13;
        BitmapFont textFont = generator.generateFont(parameter);

        Pixmap pixmap = new Pixmap(Const.WIDTH, Const.HEIGHT, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillRectangle(0, 0, Const.WIDTH, Const.HEIGHT);
        SpriteDrawable drawable = new SpriteDrawable(new Sprite(new Texture(pixmap)));
        Window.WindowStyle windowStyle = new Window.WindowStyle(titleFont, Const.GRAY, drawable);
        winDialog = new Dialog("", windowStyle){
            @Override
            protected void result(Object object) {
                winDialog.hide();
                startGame();
            }
        };
        winDialog.key(Input.Keys.N, null);
        Label.LabelStyle labelStyle = new Label.LabelStyle(titleFont, Const.GRAY);
        winDialog.text("You won!", labelStyle);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = textFont;
        textButtonStyle.fontColor = Const.GRAY;
        winDialog.button("New game (N)", null, textButtonStyle);
        winDialog.getTitleLabel().setAlignment(Align.center);

        Pixmap pixmap1 = new Pixmap(Const.WIDTH, Const.HEIGHT, Pixmap.Format.RGBA8888);
        pixmap1.setColor(1,1,1,.3f);
        pixmap1.fillRectangle(0, 0, Const.WIDTH, Const.HEIGHT);
        windowStyle.background = new SpriteDrawable(new Sprite(new Texture(pixmap1)));
        helpDialog = new Dialog("", windowStyle){
            @Override
            protected void result(Object object) {
                helpDialog.hide();
                Gdx.input.setInputProcessor(pointer);
            }
        };
        helpDialog.text("use arrows to move cursor\npress 'space' to select cell under cursor", new Label.LabelStyle(textFont, Const.GRAY)).padRight(Const.WIDTH/2 + 50);
        helpDialog.key(Input.Keys.H, null);

	    selectedChip = null;
	    ScreenViewport viewport = new ScreenViewport();
		gameStage = new Stage(viewport);
        uiStage = new Stage(viewport);

        VerticalGroup vertical = new VerticalGroup();
        vertical.setFillParent(true);
		initHelperPanel(vertical);
		grid = new Grid(this, vertical);
        HorizontalGroup horizontal = new HorizontalGroup();
        horizontal.addActor(new TextButton("Help (H)", textButtonStyle).padRight(25));
        horizontal.addActor(new TextButton("Exit (Esc)", textButtonStyle).padLeft(25));
        vertical.addActor(horizontal.padTop(50));
        uiStage.addActor(vertical);
        pointer = new Pointer(this);

        startGame();
	}

    private void initHelperPanel(VerticalGroup vertical){
        Table table = new Table();
        table.defaults().width(Const.CELL_SIZE).padBottom(Const.CELL_SIZE);
        table.add(new Image(new Texture(Utils.getCircle(Const.YELLOW))));
        table.add();
        table.add(new Image(new Texture(Utils.getCircle(Const.ORANGE))));
        table.add();
        table.add(new Image(new Texture(Utils.getCircle(Const.RED))));
        vertical.addActor(table);
        helperPanel = table;
    }

    private void checkWin(){
        if (threeColorsInARows()){
            grid.getTable().setDebug(false);
            helperPanel.setDebug(false);
            winDialog.show(uiStage);
            Gdx.input.setInputProcessor(winDialog.getStage());
        }
    }

    void select(int desiredX, int desiredY){
        Chip selectedActor = (Chip)gameStage.hit(desiredX * Const.CELL_SIZE  + Const.GLOBAL_TO_TABLE.x + Const.CELL_SIZE / 2,
                desiredY * Const.CELL_SIZE + Const.GLOBAL_TO_TABLE.y + Const.CELL_SIZE / 2, true);
        if (selectedChip == null && selectedActor != null){
            pointer.render.addAction(Actions.rotateBy(90, .35f, Interpolation.pow2Out));
            selectedChip = selectedActor;
            selectedChip.addAction(Actions.scaleTo(.7f, .7f, .35f, Interpolation.pow2InInverse));
            selectedX = desiredX;
            selectedY = desiredY;
        } else
        if (selectedChip != null){
            if (selectedActor == null){
                Color selectedColor = grid.getColorAt(desiredX, desiredY);
                if (selectedColor != Const.GRAY && Utils.manhattanDist(selectedX, selectedY, desiredX, desiredY) == 1){
                    grid.swapColors(selectedX, selectedY, desiredX, desiredY);
                    selectedChip.addAction(Actions.sequence(
                            Actions.parallel(
                                    Actions.scaleTo(1, 1, .45f, Interpolation.pow2OutInverse),
                                    Actions.moveBy((desiredX - selectedX)*Const.CELL_SIZE, (desiredY - selectedY)*Const.CELL_SIZE, .4f, Interpolation.pow5Out)
                            ),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    checkWin();
                                }
                            }))
                    );
                    selectedChip = null;
                }
            } else {
                selectedChip.addAction(Actions.scaleTo(1, 1,.3f, Interpolation.pow2In));
                pointer.render.addAction(Actions.rotateBy(90, .35f, Interpolation.pow2Out));
                selectedActor.addAction(Actions.scaleTo(.7f, .7f, .35f, Interpolation.pow2InInverse));
                selectedChip = selectedActor;
                selectedX = desiredX;
                selectedY = desiredY;
            }

        }
    }

    private boolean threeColorsInARows(){
        boolean isWin;
        for (int i = 0; i < 5; i+=2) {
            for (int j = 0; j < 5; j++) {
                isWin = grid.getColorAt(i, j) == (i == 0 ? Const.YELLOW: i == 2 ? Const.ORANGE : Const.RED);
                if (!isWin) return false;
            }
        }
        return true;
    }

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float delta = Gdx.graphics.getDeltaTime();
		gameStage.act(delta);
		gameStage.draw();
		uiStage.act(delta);
		uiStage.draw();
	}

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height, true);
    }

    @Override
	public void dispose () {
		gameStage.dispose();
	}
}
