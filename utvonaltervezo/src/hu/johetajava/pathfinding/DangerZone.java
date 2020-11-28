package hu.johetajava.pathfinding;

public class DangerZone {
    Position center;
    int r;

    DangerZone(Position position) {
        center = position;
        r = Main_pathfinding.robotDInMMs;
    }
}
