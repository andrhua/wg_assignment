package com.kekonyan.nmrealm;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;

public class Utils {
    static Pixmap getCircle(Color color){
        Pixmap pixmap = new Pixmap(Const.CELL_SIZE, Const.CELL_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillCircle(Const.CELL_SIZE/2, Const.CELL_SIZE/2, Const.CHIP_RADIUS);
        return pixmap;
    }

    static Pixmap getRect(){
        Pixmap pixmap = new Pixmap(Const.CELL_SIZE, Const.CELL_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Const.GRAY);
        pixmap.fillRectangle(0, 0, Const.CELL_SIZE, Const.CELL_SIZE);
        return pixmap;
    }


    static int manhattanDist(int uX, int uY, int vX, int vY){
        return Math.abs(uX-vX) + Math.abs(uY-vY);
    }

    static int toTableLocalX(float x){
        return (int)Math.floor((x - Const.GLOBAL_TO_TABLE.x) / Const.CELL_SIZE);
    }

    static int toTableLocalY(float y){
        return (int)Math.floor((y - Const.GLOBAL_TO_TABLE.y) / Const.CELL_SIZE);
    }
}
