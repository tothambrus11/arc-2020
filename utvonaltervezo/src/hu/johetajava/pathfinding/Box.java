package hu.johetajava.pathfinding;

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
}
