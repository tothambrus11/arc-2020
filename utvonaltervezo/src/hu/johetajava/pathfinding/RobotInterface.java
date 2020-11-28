package hu.johetajava.pathfinding;

import hu.johetajava.imageProcessing.CubePosInfo;

public abstract class RobotInterface {
    public abstract void go(double units);

    public abstract void turn(double fullRotations);

    public abstract CubePosInfo positionToCube();

    public abstract void goToEdge();

}
