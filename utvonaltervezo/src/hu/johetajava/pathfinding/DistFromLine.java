package hu.johetajava.pathfinding;

public class DistFromLine {
    double dist;
    Position closest, lower_left, lower_right, upper_left, upper_right;

    DistFromLine(double dist, Position closest) {
        this.dist = dist;
        this.closest = closest;
    }

    DistFromLine(
            double dist, Position closest,
            Position lower_left, Position lower_right,
            Position upper_left, Position upper_right
    ) {
        this.dist = dist;
        this.closest = closest;
        this.lower_left = lower_left;
        this.lower_right = lower_right;
        this.upper_left = upper_left;
        this.upper_right = upper_right;
    }

    DistFromLine(DistFromLine distFromLine) {
        dist = distFromLine.dist;
        closest = distFromLine.closest;
        lower_left = distFromLine.lower_left;
        lower_right = distFromLine.lower_right;
        upper_left = distFromLine.upper_left;
        upper_right = distFromLine.upper_right;
    }
}
