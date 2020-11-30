package hu.johetajava.pathfinding;

import hu.johetajava.imageProcessing.CubePosInfo;
import hu.johetajava.imageProcessing.MyColor;

import java.awt.*;

public class Box {
    int id;
    Position pos;
    int color, colorOnTop;

    Box(Position position, int id) {
        pos = position;
        color = Main_pathfinding.colorsInOrder[0][id];
        colorOnTop = Main_pathfinding.colorsInOrder[1][id];
        this.id = id;
    }

    void setColors(int color, int colorOnTop){
        this.color = color;
        this.colorOnTop = colorOnTop;
    }

    void setColors(CubePosInfo cubePosInfo){
        color = convertColor(cubePosInfo.boxColor);
        colorOnTop = convertColor(cubePosInfo.cubeColor);
    }

    private int convertColor(MyColor myColor){
        switch (myColor){
            case ORANGE:
                return Colors.ORANGE;

            case GREEN:
                return Colors.GREEN;

            case RED:
                return Colors.RED;

            case YELLOW:
                return Colors.YELLOW;

            case BLUE:
                return Colors.BLUE;

            default:
                return -1;
        }
    }
}
