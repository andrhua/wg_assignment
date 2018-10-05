package com.kekonyan.nmrealm;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

class Chip extends Button {
    Color color;

    Chip(Color color){
        super(new SpriteDrawable(new Sprite(new Texture(Utils.getCircle(color)))));
        this.color = color;
        this.setTransform(true);
        this.setOrigin(Align.center);
        this.setTouchable(Touchable.enabled);
    }

    Chip(){
        super(new SpriteDrawable(new Sprite(new Texture(Utils.getRect()))));
        this.color = Const.GRAY;
        this.setTouchable(Touchable.disabled);
    }
}
