package hu.johetajava.pathfinding;

public class Robot {
    Position pos;
    double dir;
    int speed;
    double turnSpeed;

    Robot(Position position) {
        pos = position;
        dir = 0;
        speed = Main_pathfinding.robotSpeed;
        turnSpeed = Main_pathfinding.robotTurnSpeed;
    }

    void move(Route route) {
        for (Position stop : route.stops) {
            moveTo(stop);
        }

        moveTo(route.endPos);

        for (Map map : Main_pathfinding.trueMaps) {
            for (int i = 0; i < map.pickUpPositions.length; i++) {
                if (map.pickUpPositions[i].equals(route.endPos)) {
                    turnTo(map.pickUpDirections[i]);
                    break;
                }
            }
        }
    }

    void moveTo(Position position) {
        if(position.equals(pos)){
            return;
        }

        double dX = position.x - pos.x;
        double dY = position.y - pos.y;
        double dist = pos.dist(position);
        double moveX = dX * speed / (dist * 1000);
        double moveY = dY * speed / (dist * 1000);

        int offSet;
        if (dX > 0) {
            offSet = 90;
        } else {
            offSet = -90;
        }
        double angle = Math.atan(dY / dX);
        double dirTo = (angle * 180) / Math.PI + offSet;

        turnTo(dirTo);

        while (!pos.equals(position)) {
            dist = pos.dist(position);
            pos = new Position(pos.x + moveX, pos.y + moveY);
            if (pos.dist(position) >= dist) {
                pos = new Position(position);
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Main_pathfinding.refreshFrame();
        }
    }

    void turnTo(double dirTo) {
        while (dir != dirTo) {
            double delta = dirTo - dir;
            boolean right = Math.abs(dir - dirTo) < 360 - Math.abs(dir - dirTo);
            if (delta < 0) {
                right = !right;
            }

            double leftUntil = Math.abs(dir - dirTo);
            if (right) {
                dir += turnSpeed;
            } else {
                dir -= turnSpeed;
            }
            if (Math.abs(dir - dirTo) > leftUntil) dir = dirTo;

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Main_pathfinding.refreshFrame();
        }
    }
}
