package hu.johetajava.pathfinding;

public class Colors {
    static final int BLUE = 0;
    static final int GREEN = 1;
    static final int ORANGE = 2;
    static final int RED = 3;
    static final int YELLOW = 4;

    int color;
    boolean known;
    int boxId;

    Colors(int color) {
        this.color = color;
        known = false;

        boxId = -1;
    }
}
