package com.kekonyan.nmrealm;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Array;

class Grid {
    private MyGame game;
    private Table table;
    Color[][]grid = new Color[5][5];
    private Array<Chip> chips;

    Grid(MyGame game, VerticalGroup vertical){
        this.game = game;
        table = new Table();
        table.setTouchable(Touchable.disabled);
        table.defaults().width(Const.CELL_SIZE).height(Const.CELL_SIZE);

        chips = new Array<Chip>(15);
        for (int i = 0; i < 5; i++) {
            chips.add(new Chip(Const.YELLOW));
            chips.add(new Chip(Const.ORANGE));
            chips.add(new Chip(Const.RED));
        }

        vertical.expand().center().addActor(table);
    }

    void initGrid(){
        table.debugCell();
        table.clear();
        chips.shuffle();
        int index = 0;
        for (int j = 4; j >= 0; j--){
            for (int i = 0; i < 5; i++){
                table.add();
                Chip chip = null;
                if (i % 2 == 0){
                    chip = chips.get(index++);
                } else if (j % 2 == 0 && i % 2 == 1){
                    chip = new Chip();
                }
                if (chip != null){
                    this.grid[i][j] = chip.color;
                    chip.setPosition(Const.GLOBAL_TO_TABLE.x + i * Const.CELL_SIZE, Const.GLOBAL_TO_TABLE.y + j * Const.CELL_SIZE);
                    game.gameStage.addActor(chip);
                }
            }
            table.row();
        }
    }

    void swapColors(int selectedX, int selectedY, int desiredX, int desiredY){
        grid[desiredX][desiredY] = grid[selectedX][selectedY];
        grid[selectedX][selectedY] = null;
    }

    Vector2 toTableLocal(float screenX, float screenY){
        return new Vector2((int)((screenX - Const.GLOBAL_TO_TABLE.x)/Const.CELL_SIZE), (int)((screenY - Const.GLOBAL_TO_TABLE.y)/Const.CELL_SIZE));
    }

    Color getColorAt(int x, int y){
        return this.grid[x][y];
    }

    Table getTable() {
        return table;
    }
}
