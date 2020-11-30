package hu.johetajava.pathfinding;

import hu.johetajava.imageProcessing.CubePosInfo;
import hu.johetajava.imageProcessing.MyColor;

import java.awt.*;

public class Box {
    int id;
    Position pos;
    int color, colorOnTop;
    double pickUpDir;
    Position pickUpPos;

    Box(Position position, int id) {
        pos = position;
        color = Main_pathfinding.colorsInOrder[0][id];
        colorOnTop = Main_pathfinding.colorsInOrder[1][id];
        this.id = id;

        getPickupPosition();
    }

    void setColors(CubePosInfo cubePosInfo) {
        color = convertColor(cubePosInfo.boxColor);
        colorOnTop = convertColor(cubePosInfo.cubeColor);
    }

    private int convertColor(MyColor myColor) {
        switch (myColor) {
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

    void getPickupPosition() {
        int closest;

        if (pos.x < 10) {
            if (pos.y < 10) {
                if (pos.x < pos.y) {
                    closest = 3;
                } else {
                    closest = 0;
                }
            } else {
                if (pos.x < 20 - pos.y) {
                    closest = 3;
                } else {
                    closest = 2;
                }
            }
        } else {
            if (pos.y < 10) {
                if (20 - pos.x < pos.y) {
                    closest = 1;
                } else {
                    closest = 0;
                }
            } else {
                if (20 - pos.x < 20 - pos.y) {
                    closest = 1;
                } else {
                    closest = 2;
                }
            }
        }

        switch (closest) {
            case 0:
                pickUpPos = new Position(
                        pos.x, pos.y + (3)
                );
                pickUpDir = 0;
                break;

            case 1:
                pickUpPos = new Position(
                        pos.x - (3), pos.y
                );
                pickUpDir = 90;
                break;

            case 2:
                pickUpPos = new Position(
                        pos.x, pos.y - (3)
                );
                pickUpDir = 180;
                break;

            case 3:
                pickUpPos = new Position(
                        pos.x + (3), pos.y
                );
                pickUpDir = 270;
                break;
        }
    }
}
