package hu.johetajava.pathfinding;

public abstract class RobotInterface {
    public abstract void go(double units);

    public abstract void turn(double fullRotations);

    public abstract CubesColor positionToCube();
}
