package com.kekonyan.nmrealm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

public class Pointer extends InputAdapter {
    Image render;
    private MyGame game;

    Pointer(MyGame game){
        this.game = game;
        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Const.INDIGO);
        pixmap.fillRectangle(0, 0, 10, 10);
        render = new Image(new Texture(pixmap));
        render.setOrigin(Align.center);
        render.setRotation(45);
        game.uiStage.addActor(render);
    }

    @Override
    public boolean keyDown(int keycode) {
        int x = 0, y = 0;
        int pointerX = Utils.toTableLocalX(render.getX());
        int pointerY = Utils.toTableLocalY(render.getY());
        switch (keycode){
            case Input.Keys.UP:
                if (pointerY < 4) y = Const.CELL_SIZE;
                break;
            case Input.Keys.DOWN:
                if (pointerY > 0) y = - Const.CELL_SIZE;
                break;
            case Input.Keys.LEFT:
                if (pointerX > 0) x = - Const.CELL_SIZE;
                break;
            case Input.Keys.RIGHT:
                if (pointerX < 4) x = Const.CELL_SIZE;
                break;
            case Input.Keys.SPACE:
                game.select(pointerX, pointerY);
                break;
            case Input.Keys.H:
                game.helpDialog.show(game.uiStage);
                Gdx.input.setInputProcessor(game.helpDialog.getStage());
                break;
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                break;
        }
        if (x != 0 || y != 0){
            render.addAction(Actions.moveBy(x, y, .25f, Interpolation.exp5Out));
        }
        return true;
    }


}
