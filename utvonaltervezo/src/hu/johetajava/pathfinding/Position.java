package hu.johetajava.pathfinding;

public class Position {
    double x;
    double y;

    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    Position(Position position) {
        x = position.x;
        y = position.y;
    }

    boolean equals(Position position) {
        return (x == position.x && y == position.y);
    }

    boolean inDanger() {
        for (Map map : Main_pathfinding.trueMaps) {
            for (DangerZone dangerZone : map.dangerZones) {
                if (dist(dangerZone.center) <= Main_pathfinding.robotR) return true;
            }
        }

        return false;
    }

    double dist(Position position) {
        return Math.sqrt(
                Math.pow(x - position.x, 2) + Math.pow(y - position.y, 2)
        );
    }

    String string() {
        return "(" + x + ", " + y + ")";
    }

    DistFromLine distFromLine(Position pos1, Position pos2) {
        if (pos1.x == pos2.x) {
            double dist = Math.abs(pos1.x - x);
            Position closest = new Position(pos1.x, y);

            return new DistFromLine(dist, closest);
        }

        if (pos1.y == pos2.y) {
            double dist = Math.abs(pos1.y - y);
            Position closest = new Position(x, pos1.y);

            return new DistFromLine(dist, closest);
        }

        Position p1, p2;

        if (pos1.x < pos2.x) {
            p1 = new Position(pos1);
            p2 = new Position(pos2);
        } else {
            p1 = new Position(pos2);
            p2 = new Position(pos1);

        }

        double steep = (p2.y - p1.y) / (p2.x - p1.x);
        double c = p1.y - steep * p1.x;

        double perp = -(1 / steep);
        double q = y - perp * x;

        double a = p1.y - perp * p1.x;
        double b = p2.y - perp * p2.x;

        Position crossP;
        double dist;


        if (steep < 0 && y > perp * x + a) {
            crossP = new Position(p1);
        } else if (steep < 0 && y < perp * x + b) {
            crossP = new Position(p2);
        } else if (steep > 0 && y < perp * x + a) {
            crossP = new Position(p1);
        } else if (steep > 0 && y > perp * x + b) {
            crossP = new Position(p2);
        } else {
            double crossX = (q - c) / (steep - perp);
            double crossY = steep * crossX + c;

            crossP = new Position(crossX, crossY);
        }
        dist = crossP.dist(this);

        return new DistFromLine(dist, crossP);
    }
}
