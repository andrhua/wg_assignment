package com.kekonyan.nmrealm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Const{
    public static final int
            CELL_SIZE = 50,
            CHIP_RADIUS = 15,
            WIDTH = Gdx.graphics.getWidth(),
            HEIGHT = Gdx.graphics.getHeight();
    public static final Vector2 GLOBAL_TO_TABLE = new Vector2((Const.WIDTH - Const.CELL_SIZE * 5)/2, (Const.HEIGHT - Const.CELL_SIZE * 4.8f)/2);
    public static final Color RED = Color.valueOf("B71C1C"),
            ORANGE = Color.valueOf("fb8c00"),
            YELLOW = Color.valueOf("fbc02d"),
            INDIGO = Color.valueOf("3f51b5"),
            GRAY = Color.valueOf("212121");
}

