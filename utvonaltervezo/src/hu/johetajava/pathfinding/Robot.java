package hu.johetajava.pathfinding;

public class Robot {
    Position pos;
    double dir;
    int speed;
    double turnSpeed;
    RobotInterface robotInterface;

    Robot(Position position) {
        pos = position;
        dir = 0;
        speed = Main_pathfinding.robotSpeed;
        turnSpeed = Main_pathfinding.robotTurnSpeed;
    }

    Robot(Position position, RobotInterface robotInterface) {
        pos = position;
        dir = 0;
        speed = Main_pathfinding.robotSpeed;
        turnSpeed = Main_pathfinding.robotTurnSpeed;
        this.robotInterface = robotInterface;
    }

    void move(Route route) {
        for (Position stop : route.stops) {
            moveTo(stop);
        }

        moveTo(route.endPos);

        if(route.endDir == 10000){
            for (Map map : Main_pathfinding.trueMaps) {
                for (int i = 0; i < map.boxes.length; i++) {
                    if (map.boxes[i].pickUpPos.equals(route.endPos)) {
                        turnTo(map.boxes[i].pickUpDir);
                        break;
                    }
                }
            }
        } else {
            turnTo(route.endDir);
        }
    }

    void moveTo(Position position) {
        if (position.equals(pos)) {
            return;
        }

        double dX = position.x - pos.x;
        double dY = position.y - pos.y;
        double dist = pos.dist(position);
        double moveX = dX * speed / (dist * 1000);
        double moveY = dY * speed / (dist * 1000);

        int offSet = 0;
        if (dX > 0) {
            offSet = 90;
        } else if (dX < 0) {
            offSet = -90;
        }

        double angle = Math.atan(dY / dX);
        double dirTo = (angle * 180) / Math.PI + offSet;

        if (dX == 0) {
            if (dY > 0) {
                dirTo = 180;
            } else {
                dirTo = 0;
            }
        }

        turnTo(dirTo);

        if (robotInterface == null) {
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
        } else {
            robotInterface.go(dist);
            pos = new Position(position);
        }
    }

    void moveToWithoutTurn(Position position) {
        if (position.equals(pos)) {
            return;
        }

        if(robotInterface != null){
            robotInterface.go(-pos.dist(position));
        }

        pos = new Position(position);
    }

    void turnTo(Position position){
        if (position.equals(pos)) {
            return;
        }

        double dX = position.x - pos.x;
        double dY = position.y - pos.y;

        int offSet = 0;
        if (dX > 0) {
            offSet = 90;
        } else if (dX < 0) {
            offSet = -90;
        }

        double angle = Math.atan(dY / dX);
        double dirTo = (angle * 180) / Math.PI + offSet;

        if (dX == 0) {
            if (dY > 0) {
                dirTo = 180;
            } else {
                dirTo = 0;
            }
        }

        dir = dirTo;
    }

    void turnTo(double dirTo) {
        double turnInDegrees = (dir - dirTo) % 360;
        if (turnInDegrees > 180) {
            turnInDegrees -= 360;
        } else if (turnInDegrees < -180) {
            turnInDegrees += 360;
        }

        boolean right = turnInDegrees > 0;

        if (robotInterface == null) {
            while (dir != dirTo) {
                if (right) {
                    dir -= turnSpeed;
                    turnInDegrees -= turnSpeed;
                } else {
                    dir += turnSpeed;
                    turnInDegrees += turnSpeed;
                }
                if ((right && turnInDegrees < 0) || (!right && turnInDegrees > 0)) dir = dirTo;

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Main_pathfinding.refreshFrame();
            }
        } else {
            robotInterface.turn(turnInDegrees / 360);
            dir = dirTo;
        }
    }

    boolean isPickUpFrom(double pickUpDirection, Position cubePosition) {
        if(pickUpDirection != dir) return false;

        switch ((int) pickUpDirection) {
            case 0:
                return cubePosition.equals(new Position(pos.x, pos.y - 3));

            case 90:
                return cubePosition.equals(new Position(pos.x + 3, pos.y));

            case 180:
                return cubePosition.equals(new Position(pos.x, pos.y + 3));

            case 270:
                return cubePosition.equals(new Position(pos.x - 3, pos.y));

            default:
                return false;
        }
    }
}
