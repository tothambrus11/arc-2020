import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Main {
    //robot params
    final static int robotDInMMs = 270;
    final static double robotR = (double) robotDInMMs / (2 * 115);
    final static int robotSpeed = 25; //*0.001 tile/s, default: 7
    final static double robotTurnSpeed = 1.1; //*1000 degrees/s, default: 0.2

    //map params, TODO read these
    final static String input1 = "(I,H,G,J)(T,K,R,M)(Q,G,S,I)(D,L,F,N)(O,Q,Q,S)(N,R,L,T)";
    final static String input2 = "(I,H,G,J)(I,Q,G,S)(F,L,D,N)(O,M,Q,O)(Q,G,S,I)(N,R,L,T)";
    final static String input3 = "(I,H,G,J)(Q,O,S,Q)(R,L,T,N)(M,R,O,T)(I,Q,G,S)(F,M,D,O)";
    final static String input4 = "(I,H,G,J)(R,L,T,N)(J,N,H,P)(M,R,K,T)(N,Q,P,S)(Q,H,S,J)";
    final static int trueMap = 2;

    static int[][] colorsInOrder = new int[][]{
            {Colors.ORANGE, Colors.GREEN, Colors.RED, Colors.YELLOW, Colors.BLUE},
            {Colors.GREEN, Colors.RED, Colors.YELLOW, Colors.BLUE, Colors.ORANGE}
    };
    static Colors[] allColors = new Colors[]{
            new Colors(Colors.BLUE),
            new Colors(Colors.GREEN),
            new Colors(Colors.ORANGE),
            new Colors(Colors.RED),
            new Colors(Colors.YELLOW)
    };
    static Parking parkingZone;
    static Position startPos, homePos;
    static Robot robot;
    static JFrame frame;
    static MyPanel panel;
    static int nextColor = Colors.BLUE;
    static Route nextRoute;
    static int found = 0;

    static Map map1 = new Map(input1, 1);
    static Map map2 = new Map(input2, 2);
    static Map map3 = new Map(input3, 3);
    static Map map4 = new Map(input4, 4);

    static ArrayList<Map> trueMaps = new ArrayList<>();

    //TODO timer

    static final int screenWidth = 600;
    static final int screenHeight = 600;

    public static void main(String[] args) {
        trueMaps.add(map1);
        trueMaps.add(map2);
        trueMaps.add(map3);
        trueMaps.add(map4);


        getNextRoute();
        createFrame();

        robot.move(nextRoute);
        while (found < 3) {
            System.err.println("elkezdte");
            getNextRoute();
            robot.move(nextRoute);
            //TODO int wrongMap
            for (Map map : trueMaps) {
                for (int i = 0; i < map.pickUpPositions.length; i++) {
                    if (robot.pos.equals(map.pickUpPositions[i])) {
                        if (map.id != trueMap) {

                        } else {
                            allColors[map.boxes[i].color].known = true;
                            allColors[map.boxes[i].color].boxId = i;
                            if (map.boxes[i].color == nextColor) {
                                nextColor = map.boxes[i].colorOnTop;
                                found++;
                            }
                        }
                    }
                }
            }
        }

        getNextRoute();
        robot.move(nextRoute);
        getNextRoute();
        robot.move(nextRoute);
    }

    /*
    private static int letter(char a) {
        return a - 65;
    }
    */

    private static char number(double a) {
        return (char) (a + 65);
    }

    /*private static void writeObjects() {
        System.out.println(
                "Parking Zone: ("
                        + number(parkingZone.upper_right.x) + ","
                        + number(parkingZone.upper_right.y) + ","
                        + number(parkingZone.lower_right.x) + ","
                        + number(parkingZone.lower_right.y) + ")"
        );

        for (int i = 0; i < 5; i++) {
            System.out.println(
                    "Box" + (i + 1) + ": ("
                            + number(map1.boxes[i].pos.x) + ","
                            + number(map1.boxes[i].pos.y) + ")"
            );
        }
    }*/

    /*
    private static void getObjects() {
        //get parking zone position
        parkingZone = new Parking(
                new Position(letter(input.charAt(1)), letter(input.charAt(3))),
                new Position(letter(input.charAt(5)), letter(input.charAt(7)))
        );

        //get box positions
        Position posA, posB, middlePos;
        for (int i = 0; i < 5; i++) {
            posA = new Position(letter(input.charAt(10 + 9 * i)), letter(input.charAt(12 + 9 * i)));
            posB = new Position(letter(input.charAt(14 + 9 * i)), letter(input.charAt(16 + 9 * i)));
            middlePos = new Position((posA.x + posB.x) / 2, (posA.y + posB.y) / 2);

            boxes[i] = new Box(middlePos, i);
        }
    }

    private static void calculateDangerZones() {
        for (int i = 0; i < 5; i++) {
            dangerZones[i * 5] = new DangerZone(new Position(boxes[i].pos.x - 1, boxes[i].pos.y - 1));
            dangerZones[i * 5 + 1] = new DangerZone(new Position(boxes[i].pos.x - 1, boxes[i].pos.y + 1));
            dangerZones[i * 5 + 2] = new DangerZone(new Position(boxes[i].pos.x + 1, boxes[i].pos.y - 1));
            dangerZones[i * 5 + 3] = new DangerZone(new Position(boxes[i].pos.x + 1, boxes[i].pos.y + 1));
            dangerZones[i * 5 + 4] = new DangerZone(boxes[i].pos);
        }

        dangerZones[25] = new DangerZone(parkingZone.upper_right);
        dangerZones[26] = new DangerZone(parkingZone.lower_right);
        dangerZones[27] = new DangerZone(parkingZone.lower_left);
        dangerZones[28] = new DangerZone(parkingZone.upper_left);

        dangerZones[29] = new DangerZone(parkingZone.middle_right);
        dangerZones[30] = new DangerZone(parkingZone.middle_left);
        dangerZones[31] = new DangerZone(parkingZone.middle_back);
    }

    private static void getRobot() {
        robot = new Robot(
                new Position(
                        (parkingZone.lower_right.x + parkingZone.upper_left.x) / 2,
                        (parkingZone.lower_right.y + parkingZone.upper_left.y) / 2
                )
        );
    }

    private static void getPickUpPositions() {
        for (int i = 0; i < 5; i++) {
            int closest;

            if (boxes[i].pos.x < 10) {
                if (boxes[i].pos.y < 10) {
                    if (boxes[i].pos.x < boxes[i].pos.y) {
                        closest = 3;
                    } else {
                        closest = 0;
                    }
                } else {
                    if (boxes[i].pos.x < 20 - boxes[i].pos.y) {
                        closest = 3;
                    } else {
                        closest = 2;
                    }
                }
            } else {
                if (boxes[i].pos.y < 10) {
                    if (20 - boxes[i].pos.x < boxes[i].pos.y) {
                        closest = 1;
                    } else {
                        closest = 0;
                    }
                } else {
                    if (20 - boxes[i].pos.x < 20 - boxes[i].pos.y) {
                        closest = 1;
                    } else {
                        closest = 2;
                    }
                }
            }

            switch (closest) {
                case 0:
                    pickUpPositions[i] = new Position(
                            boxes[i].pos.x, boxes[i].pos.y + (1.05 + Main.robotR)
                    );
                    pickUpDirections[i] = 0;
                    break;

                case 1:
                    pickUpPositions[i] = new Position(
                            boxes[i].pos.x - (1.05 + Main.robotR), boxes[i].pos.y
                    );
                    pickUpDirections[i] = 90;
                    break;

                case 2:
                    pickUpPositions[i] = new Position(
                            boxes[i].pos.x, boxes[i].pos.y - (1.05 + Main.robotR)
                    );
                    pickUpDirections[i] = 180;
                    break;

                case 3:
                    pickUpPositions[i] = new Position(
                            boxes[i].pos.x + (1.05 + Main.robotR), boxes[i].pos.y
                    );
                    pickUpDirections[i] = 270;
                    break;
            }
        }
    }

    private static void getStartPos() {
        double dx, dy;

        dx = parkingZone.upper_right.x - parkingZone.lower_right.x;
        dy = parkingZone.upper_right.y - parkingZone.lower_right.y;

        startPos = new Position(robot.pos.x + dx, robot.pos.y + dy);

        homePos = new Position(robot.pos);
    }
    */

    private static void createFrame() {
        frame = new JFrame("Game");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(screenWidth, screenHeight);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);

        panel = new MyPanel();
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    static void refreshFrame() {
        frame.getContentPane().remove(panel);
        panel = new MyPanel();
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    static boolean isKnown(int color) {
        return allColors[color].known;
    }

    static void getNextRoute() {
        if (found == 3) {
            if (robot.pos.equals(startPos)) {
                nextRoute = new Route(robot.pos, homePos);
            } else {
                nextRoute = new Route(robot.pos, startPos);
            }
        } else if (robot.pos.equals(homePos)) {
            nextRoute = new Route(robot.pos, startPos);
        } else if (isKnown(nextColor)) {
            Position posTo = new Position(Main.trueMaps.get(0).pickUpPositions[allColors[nextColor].boxId]);
            nextRoute = new Route(robot.pos, posTo);
        } else {
            Route shortestRoute = null;
            for (Map map : trueMaps) {
                for (int i = 0; i < map.pickUpPositions.length; i++) {
                    Route route = new Route(robot.pos, map.pickUpPositions[i]);

                    if (!allColors[map.boxes[i].color].known) {
                        if (shortestRoute == null || route.length() < shortestRoute.length()) {
                            shortestRoute = new Route(route);
                        }
                    }
                }
            }

            nextRoute = new Route(shortestRoute);
        }
    }

}

class Position {
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
        for (Map map : Main.trueMaps) {
            for (DangerZone dangerZone : map.dangerZones) {
                if (dist(dangerZone.center) <= Main.robotR) return true;
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

class DistFromLine {
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

class Parking {
    Position upper_right, lower_right, lower_left, upper_left, middle_right, middle_back, middle_left;

    Parking(Position upper, Position lower) {
        upper_right = upper;
        lower_right = lower;

        int deltaX = Math.abs((int) (lower_right.x - upper_right.x));
        int deltaY = Math.abs((int) (lower_right.y - upper_right.y));

        switch (Math.abs(deltaX - deltaY)) {
            case 2:
                if (lower_right.x - upper_right.x == 3) {
                    if (lower_right.y - upper_right.y == 1) {
                        lower_right = new Position(upper_right.x + 2.85f, upper_right.y + 0.95f);
                    } else {
                        lower_right = new Position(upper_right.x + 2.85f, upper_right.y - 0.95f);
                    }
                } else if (lower_right.x - upper_right.x == 1) {
                    if (lower_right.y - upper_right.y == 3) {
                        lower_right = new Position(upper_right.x + 0.95f, upper_right.y + 2.85f);
                    } else {
                        lower_right = new Position(upper_right.x + 0.95f, upper_right.y - 2.85f);
                    }
                } else if (lower_right.x - upper_right.x == -1) {
                    if (lower_right.y - upper_right.y == 3) {
                        lower_right = new Position(upper_right.x - 0.95f, upper_right.y + 2.85f);
                    } else {
                        lower_right = new Position(upper_right.x - 0.95f, upper_right.y - 2.85f);
                    }
                } else if (lower_right.x - upper_right.x == -3) {
                    if (lower_right.y - upper_right.y == 1) {
                        lower_right = new Position(upper_right.x - 2.85f, upper_right.y + 0.95f);
                    } else {
                        lower_right = new Position(upper_right.x - 2.85f, upper_right.y - 0.95f);
                    }
                }
                break;

            case 1:
                if (lower_right.x - upper_right.x == 2) {
                    if (lower_right.y - upper_right.y == 1) {
                        lower_right = new Position(upper_right.x + 2.68f, upper_right.y + 1.34f);
                    } else {
                        lower_right = new Position(upper_right.x + 2.68f, upper_right.y - 1.34f);
                    }
                } else if (lower_right.x - upper_right.x == 1) {
                    if (lower_right.y - upper_right.y == 2) {
                        lower_right = new Position(upper_right.x + 1.34f, upper_right.y + 2.68f);
                    } else {
                        lower_right = new Position(upper_right.x + 1.34f, upper_right.y - 2.68f);
                    }
                } else if (lower_right.x - upper_right.x == -1) {
                    if (lower_right.y - upper_right.y == 2) {
                        lower_right = new Position(upper_right.x - 1.34f, upper_right.y + 2.68f);
                    } else {
                        lower_right = new Position(upper_right.x - 1.34f, upper_right.y - 2.68f);
                    }
                } else if (lower_right.x - upper_right.x == -2) {
                    if (lower_right.y - upper_right.y == 1) {
                        lower_right = new Position(upper_right.x - 2.68f, upper_right.y + 1.34f);
                    } else {
                        lower_right = new Position(upper_right.x - 2.68f, upper_right.y - 1.34f);
                    }
                }
                break;

            case 0:
                if (lower_right.x - upper_right.x == 2) {
                    if (lower_right.y - upper_right.y == 2) {
                        lower_right = new Position(upper_right.x + 2.12f, upper_right.y + 2.12f);
                    } else {
                        lower_right = new Position(upper_right.x + 2.12f, upper_right.y - 2.12f);
                    }
                } else {
                    if (lower_right.y - upper_right.y == 2) {
                        lower_right = new Position(upper_right.x - 2.12f, upper_right.y + 2.12f);
                    } else {
                        lower_right = new Position(upper_right.x - 2.12f, upper_right.y - 2.12f);
                    }
                }

        }

        double dX, dY;

        dY = lower_right.x - upper_right.x;
        dX = -lower_right.y + upper_right.y;

        lower_left = new Position(lower_right.x + dX, lower_right.y + dY);

        dY = lower_left.x - lower_right.x;
        dX = -lower_left.y + lower_right.y;

        upper_left = new Position(lower_left.x + dX, lower_left.y + dY);

        middle_right = new Position((lower_right.x + upper_right.x) / 2, (lower_right.y + upper_right.y) / 2);
        middle_left = new Position((lower_left.x + upper_left.x) / 2, (lower_left.y + upper_left.y) / 2);
        middle_back = new Position((lower_right.x + lower_left.x) / 2, (lower_right.y + lower_left.y) / 2);
    }
}

class Box {
    int id;
    Position pos;
    int color, colorOnTop;

    Box(Position position, int id) {
        pos = position;
        color = Main.colorsInOrder[0][id];
        colorOnTop = Main.colorsInOrder[1][id];
        this.id = id;
    }

    void checkColor() {
        //TODO check color
    }
}

class DangerZone {
    Position center;
    int r;

    DangerZone(Position position) {
        center = position;
        r = Main.robotDInMMs;
    }
}

class Robot {
    Position pos;
    double dir;
    int speed;
    double turnSpeed;

    Robot(Position position) {
        pos = position;
        dir = 0;
        speed = Main.robotSpeed;
        turnSpeed = Main.robotTurnSpeed;
    }

    void move(Route route) {
        for (Position stop : route.stops) {
            moveTo(stop);
        }

        moveTo(route.endPos);

        for (Map map : Main.trueMaps) {
            for (int i = 0; i < map.pickUpPositions.length; i++) {
                if (map.pickUpPositions[i].equals(route.endPos)) {
                    turnTo(map.pickUpDirections[i]);
                    break;
                }
            }
        }
    }

    void moveTo(Position position) {
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

            Main.refreshFrame();
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

            Main.refreshFrame();
        }
    }
}

class Route {
    Position startPos, endPos;
    ArrayList<Position> stops = new ArrayList<>();
    ArrayList<Route> possibleRoutes = new ArrayList<>();

    Route(Position startPos, Position endPos) {
        this.startPos = startPos;
        this.endPos = endPos;

        calcAsGrid();

        System.out.println("route found");
    }

    Route(Route route) {
        startPos = route.startPos;
        endPos = route.endPos;

        stops = new ArrayList<>();
        stops.addAll(route.stops);
    }

    void calcAsGrid() {
        possibleRoutes = new ArrayList<>();
        possibleRoutes.add(this);

        boolean finished = noCross();
        int limit = 0;
        while (!finished && limit < 2) {
            limit++;
            System.out.println("calculating..." + possibleRoutes.size());
            ArrayList<Route> tempPossibleRoutes = new ArrayList<>();

            for (Route route : possibleRoutes) {
                for (int i = 0; i < 20; i++) {
                    for (int j = 0; j < 20; j++) {
                        Route route1 = new Route(route);
                        route1.stops.add(new Position(i, j));
                        tempPossibleRoutes.add(route1);
                        if (route1.noCross()) {
                            finished = true;
                        }
                    }
                }
            }

            if (finished) {
                Route shortestRoute = null;

                for (Route route : tempPossibleRoutes) {
                    if (route.noCross()) {
                        if (shortestRoute == null || route.length() < shortestRoute.length()) {
                            shortestRoute = new Route(route);
                        }
                    }
                }

                if (shortestRoute != null) {
                    beACopy(shortestRoute);
                }
            } else {
                possibleRoutes = new ArrayList<>();
                possibleRoutes.addAll(tempPossibleRoutes);
            }
        }

        if(!finished){
            startPos = null;
        }
    }

    /*
    void calcAsCircle() {
        possibleRoutes.add(new Route(this));
        boolean finished = false;
        int a = 0;
        while (!finished) {
            finished = true;

            ArrayList<Route> tempPossibleRoutes = new ArrayList<>();

            System.out.println(a);
            a++;

            for (int i = 0; i < possibleRoutes.size(); i++) {
                Route route = possibleRoutes.get(i);

                Position[] stopAtPos;
                int where;


                if (route.stops.size() == 0) {
                    stopAtPos = whereToAddStop(route.startPos, route.endPos);
                    where = 0;

                    if (stopAtPos != null) {
                        Position[] stopPos = stopPos(stopAtPos);

                        Route newRoute1 = addedStop(
                                route, new Position[]{stopPos[0], stopPos[1]}, where
                        );

                        Route newRoute2 = addedStop(
                                route, new Position[]{stopPos[2], stopPos[3]}, where
                        );

                        if (!newRoute1.isIn(possibleRoutes) && !newRoute1.isIn(tempPossibleRoutes)) {
                            tempPossibleRoutes.add(newRoute1);
                            finished = false;
                        }

                        if (!newRoute2.isIn(possibleRoutes) && !newRoute2.isIn(tempPossibleRoutes)) {
                            tempPossibleRoutes.add(newRoute2);
                            finished = false;
                        }
                    }
                } else {
                    stopAtPos = whereToAddStop(route.startPos, route.stops.get(0));
                    where = 0;

                    if (stopAtPos != null) {
                        Position[] stopPos = stopPos(stopAtPos);

                        Route newRoute1 = addedStop(
                                route, new Position[]{stopPos[0], stopPos[1]}, where
                        );

                        Route newRoute2 = addedStop(
                                route, new Position[]{stopPos[2], stopPos[3]}, where
                        );

                        if (!newRoute1.isIn(possibleRoutes) && !newRoute1.isIn(tempPossibleRoutes)) {
                            tempPossibleRoutes.add(newRoute1);
                            finished = false;
                        }

                        if (!newRoute2.isIn(possibleRoutes) && !newRoute2.isIn(tempPossibleRoutes)) {
                            tempPossibleRoutes.add(newRoute2);
                            finished = false;
                        }
                    }

                    for (int j = 1; j < route.stops.size() - 1; j++) {
                        stopAtPos = whereToAddStop(route.stops.get(j - 1), route.stops.get(j));
                        where = j;

                        if (stopAtPos != null) {
                            Position[] stopPos = stopPos(stopAtPos);

                            Route newRoute1 = addedStop(
                                    route, new Position[]{stopPos[0], stopPos[1]}, where
                            );

                            Route newRoute2 = addedStop(
                                    route, new Position[]{stopPos[2], stopPos[3]}, where
                            );

                            if (!newRoute1.isIn(possibleRoutes) && !newRoute1.isIn(tempPossibleRoutes)) {
                                tempPossibleRoutes.add(newRoute1);
                                finished = false;
                            }

                            if (!newRoute2.isIn(possibleRoutes) && !newRoute2.isIn(tempPossibleRoutes)) {
                                tempPossibleRoutes.add(newRoute2);
                                finished = false;
                            }
                        }
                    }

                    stopAtPos = whereToAddStop(route.stops.get(route.stops.size() - 1), route.endPos);
                    where = route.stops.size();

                    if (stopAtPos != null) {
                        Position[] stopPos = stopPos(stopAtPos);

                        Route newRoute1 = addedStop(
                                route, new Position[]{stopPos[0], stopPos[1]}, where
                        );

                        Route newRoute2 = addedStop(
                                route, new Position[]{stopPos[2], stopPos[3]}, where
                        );

                        if (!newRoute1.isIn(possibleRoutes) && !newRoute1.isIn(tempPossibleRoutes)) {
                            tempPossibleRoutes.add(newRoute1);
                            finished = false;
                        }

                        if (!newRoute2.isIn(possibleRoutes) && !newRoute2.isIn(tempPossibleRoutes)) {
                            tempPossibleRoutes.add(newRoute2);
                            finished = false;
                        }
                    }
                }
            }

            for (Route route : tempPossibleRoutes) {
                if (route.noCross()) finished = true;
            }

            possibleRoutes.addAll(tempPossibleRoutes);
        }

        Route shortestRoute = null;
        for (Route route : possibleRoutes) {
            if (route.noCross()) {


                if (shortestRoute == null || route.length() < shortestRoute.length()) {
                    shortestRoute = new Route(route);
                }
            }
        }

        if (shortestRoute == null) return;

        beACopy(shortestRoute);

        removeRedundantStops();
    }

    void calcAsRectangle() {
        ArrayList<Position> edgePoints = new ArrayList<>();
        for (DangerZone dangerZone : Main.dangerZones) {
            edgePoints.add(new Position(dangerZone.center.x - 1, dangerZone.center.y - 1));
            edgePoints.add(new Position(dangerZone.center.x + 1, dangerZone.center.y - 1));
            edgePoints.add(new Position(dangerZone.center.x - 1, dangerZone.center.y + 1));
            edgePoints.add(new Position(dangerZone.center.x + 1, dangerZone.center.y + 1));
        }

        possibleRoutes = new ArrayList<>();
        possibleRoutes.add(this);

        boolean finished = noCross();
        while (!finished) {
            ArrayList<Route> tempPossibleRoutes = new ArrayList<>();

            for (Route route : possibleRoutes) {
                for (Position position : edgePoints) {
                    Route route1 = new Route(route);
                    route1.stops.add(position);
                    tempPossibleRoutes.add(route1);
                    if (route1.noCross()) {
                        finished = true;
                    }
                }
            }

            if (finished) {
                Route shortestRoute = null;

                for (Route route : tempPossibleRoutes) {
                    if (route.noCross()) {
                        if (shortestRoute == null || route.length() < shortestRoute.length()) {
                            shortestRoute = new Route(route);
                        }
                    }
                }

                if (shortestRoute != null) {
                    beACopy(shortestRoute);
                }
            } else {
                possibleRoutes = new ArrayList<>();
                possibleRoutes.addAll(tempPossibleRoutes);
            }
        }
    }
    */

    static Route addedStop(Route route, Position[] stop, int at) {

        Route myRoute = new Route(route);

        if (!stop[1].inDanger()) {
            myRoute.stops.add(at, stop[1]);
        }

        if (!stop[0].inDanger()) {
            myRoute.stops.add(at, stop[0]);
        }

        return myRoute;
    }

    static Position[] whereToAddStop(Position segmentStart, Position segmentEnd) {

        for (Map map : Main.trueMaps) {
            for (DangerZone dangerZone : map.dangerZones) {

                DistFromLine distFromLine = dangerZone.center.distFromLine(segmentStart, segmentEnd);

                if (distFromLine.dist < Main.robotR) {
                    return new Position[]{
                            distFromLine.closest, dangerZone.center,
                            segmentStart, segmentEnd
                    };
                }
            }
        }

        return null;
    }

    static Position[] stopPos(Position[] stopAt) {
        Position at = stopAt[0];
        Position center = stopAt[1];
        Position start = stopAt[2];
        Position end = stopAt[3];

        Position p1, p2;

        if (start.x <= end.x) {
            p1 = new Position(start);
            p2 = new Position(end);
        } else {
            p1 = new Position(end);
            p2 = new Position(start);

        }

        double steep = (p2.y - p1.y) / (p2.x - p1.x);
        double c = p1.y - steep * p1.x;

        double perp = -(1 / steep);
        double q = center.y - perp * center.x;

        double dist = at.dist(center);

        double d_bottom, d_left, d_right, d_top;

        double r = Main.robotR + 0.05;

        if (steep < 0) {
            d_bottom = c - (steep + 1 / steep) * (r - dist) / Math.sqrt(1 + 1 / (steep * steep));
            d_top = c + (steep + 1 / steep) * (r + dist) / Math.sqrt(1 + 1 / (steep * steep));
            d_right = q - (perp + 1 / perp) * (Main.robotR) / Math.sqrt(1 + 1 / (perp * perp));
            d_left = q + (perp + 1 / perp) * (Main.robotR) / Math.sqrt(1 + 1 / (perp * perp));
        } else {
            d_bottom = c + (steep + 1 / steep) * (r + dist) / Math.sqrt(1 + 1 / (steep * steep));
            d_top = c - (steep + 1 / steep) * (r - dist) / Math.sqrt(1 + 1 / (steep * steep));
            d_left = q - (perp + 1 / perp) * (Main.robotR) / Math.sqrt(1 + 1 / (perp * perp));
            d_right = q + (perp + 1 / perp) * (Main.robotR) / Math.sqrt(1 + 1 / (perp * perp));
        }

        double x_ll = (d_bottom - d_left) / (perp - steep);
        double y_ll = (steep * x_ll + d_bottom);
        Position ll = new Position(x_ll, y_ll);

        double x_lr = (d_bottom - d_right) / (perp - steep);
        double y_lr = (steep * x_lr + d_bottom);
        Position lr = new Position(x_lr, y_lr);

        double x_ul = (d_top - d_left) / (perp - steep);
        double y_ul = (steep * x_ul + d_top);
        Position ul = new Position(x_ul, y_ul);

        double x_ur = (d_top - d_right) / (perp - steep);
        double y_ur = (steep * x_ur + d_top);
        Position ur = new Position(x_ur, y_ur);

        return new Position[]{ll, lr, ul, ur};
    }

    boolean noCross() {
        if (stops.size() > 0) {
            if (whereToAddStop(startPos, stops.get(0)) != null) {
                return false;
            }

            for (int i = 1; i < stops.size(); i++) {
                if (whereToAddStop(stops.get(i - 1), stops.get(i)) != null) {
                    return false;
                }
            }

            return whereToAddStop(stops.get(stops.size() - 1), endPos) == null;

        } else {
            return whereToAddStop(startPos, endPos) == null;
        }
    }

    double length() {
        double length = 0;

        if(startPos == null) return 1000000;

        if (stops.size() > 0) {
            length += Math.sqrt(
                    Math.pow(startPos.x - stops.get(0).x, 2) +
                            Math.pow(startPos.y - stops.get(0).y, 2)
            );

            for (int i = 1; i < stops.size(); i++) {
                length += Math.sqrt(
                        Math.pow(stops.get(i - 1).x - stops.get(i).x, 2) +
                                Math.pow(stops.get(i).y - stops.get(i).y, 2)
                );
            }

            length += Math.sqrt(
                    Math.pow(stops.get(stops.size() - 1).x - endPos.x, 2) +
                            Math.pow(stops.get(stops.size() - 1).y - endPos.y, 2)
            );
        } else {
            length += Math.sqrt(
                    Math.pow(startPos.x - endPos.x, 2) +
                            Math.pow(startPos.y - endPos.y, 2)
            );
        }

        return length;
    }

    void removeRedundantStops() {
        boolean finished = false;
        while (!finished) {
            finished = true;

            for (int i = 0; i < stops.size(); i++) {
                Route route = new Route(this);
                route.stops.remove(i);

                if (route.noCross() && route.length() < length()) {
                    finished = false;
                    beACopy(route);
                    break;
                }
            }
        }
    }

    void beACopy(Route route) {
        startPos = route.startPos;
        endPos = route.endPos;

        stops = new ArrayList<>();
        stops.addAll(route.stops);

        possibleRoutes = new ArrayList<>();
        possibleRoutes.addAll(route.possibleRoutes);
    }

    boolean equals(Route route) {

        if (stops.size() != route.stops.size()) return false;

        for (int i = 0; i < stops.size(); i++) {
            if (!stops.get(i).equals(route.stops.get(i))) return false;
        }

        return startPos.equals(route.startPos) && endPos.equals(route.endPos);
    }

    boolean isIn(ArrayList<Route> routes) {
        for (Route route : routes) {
            if (equals(route)) return true;
        }

        return false;
    }
}

class MyPanel extends JComponent {
    public void paint(Graphics g) {
        //Paint border
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Main.screenWidth, Main.screenHeight);
        g.clearRect(50 / 4, 50 / 4, Main.screenWidth - 100 / 4, Main.screenHeight - 100 / 4);

        //Paint grid
        g.setColor(Color.BLACK);
        for (int i = 1; i < 20; i++) {
            g.drawLine(50 / 4 + i * 115 / 4, 0, 50 / 4 + i * 115 / 4, Main.screenHeight);
            g.drawLine(0, 50 / 4 + i * 115 / 4, Main.screenWidth, 50 / 4 + i * 115 / 4);
        }

        //paint parking lot
        g.setColor(Color.GREEN);
        g.drawLine(
                (int) (Main.parkingZone.upper_right.x * 115 / 4 + 50 / 4),
                (int) (Main.parkingZone.upper_right.y * 115 / 4 + 50 / 4),
                (int) (Main.parkingZone.lower_right.x * 115 / 4 + 50 / 4),
                (int) (Main.parkingZone.lower_right.y * 115 / 4 + 50 / 4)
        );
        g.setColor(Color.BLACK);
        g.drawLine(
                (int) (Main.parkingZone.lower_left.x * 115 / 4 + 50 / 4),
                (int) (Main.parkingZone.lower_left.y * 115 / 4 + 50 / 4),
                (int) (Main.parkingZone.lower_right.x * 115 / 4 + 50 / 4),
                (int) (Main.parkingZone.lower_right.y * 115 / 4 + 50 / 4)
        );
        g.drawLine(
                (int) (Main.parkingZone.lower_left.x * 115 / 4 + 50 / 4),
                (int) (Main.parkingZone.lower_left.y * 115 / 4 + 50 / 4),
                (int) (Main.parkingZone.upper_left.x * 115 / 4 + 50 / 4),
                (int) (Main.parkingZone.upper_left.y * 115 / 4 + 50 / 4)
        );

        //paint robot position
        g.setColor(Color.GREEN);
        g.fillOval(
                (int) (Main.robot.pos.x * 115 / 4 - Main.robotDInMMs / 8 + 50 / 4),
                (int) (Main.robot.pos.y * 115 / 4 - Main.robotDInMMs / 8 + 50 / 4),
                Main.robotDInMMs / 4, Main.robotDInMMs / 4
        );

        //paint robot direction
        double angle = ((-Main.robot.dir + 180) * Math.PI / 180) % 360;
        double endX = (Main.robot.pos.x + Main.robotR * 1.2 * Math.sin(angle));
        double endY = (Main.robot.pos.y + Main.robotR * 1.2 * Math.cos(angle));
        g.setColor(Color.BLACK);
        g.drawLine(
                (int) (Main.robot.pos.x * 115 / 4 + 50 / 4),
                (int) (Main.robot.pos.y * 115 / 4 + 50 / 4),
                (int) (endX * 115 / 4 + 50 / 4),
                (int) (endY * 115 / 4 + 50 / 4)
        );

        //paint start position
        g.setColor(Color.GREEN);
        g.fillOval(
                (int) (Main.startPos.x * 115 / 4 + 50 / 4 - 5),
                (int) (Main.startPos.y * 115 / 4 + 50 / 4 - 5),
                10, 10
        );

        //paint next Route
        g.setColor(Color.BLUE);
        if (Main.nextRoute.stops.size() > 0) {
            g.drawLine(
                    (int) (Main.nextRoute.startPos.x * 115 / 4 + 50 / 4),
                    (int) (Main.nextRoute.startPos.y * 115 / 4 + 50 / 4),
                    (int) (Main.nextRoute.stops.get(0).x * 115 / 4 + 50 / 4),
                    (int) (Main.nextRoute.stops.get(0).y * 115 / 4 + 50 / 4)
            );
            for (int i = 1; i < Main.nextRoute.stops.size(); i++) {
                g.drawLine(
                        (int) (Main.nextRoute.stops.get(i - 1).x * 115 / 4 + 50 / 4),
                        (int) (Main.nextRoute.stops.get(i - 1).y * 115 / 4 + 50 / 4),
                        (int) (Main.nextRoute.stops.get(i).x * 115 / 4 + 50 / 4),
                        (int) (Main.nextRoute.stops.get(i).y * 115 / 4 + 50 / 4)
                );
            }
            g.drawLine(
                    (int) (Main.nextRoute.stops.get(Main.nextRoute.stops.size() - 1).x * 115 / 4 + 50 / 4),
                    (int) (Main.nextRoute.stops.get(Main.nextRoute.stops.size() - 1).y * 115 / 4 + 50 / 4),
                    (int) (Main.nextRoute.endPos.x * 115 / 4 + 50 / 4),
                    (int) (Main.nextRoute.endPos.y * 115 / 4 + 50 / 4)
            );
        } else {
            g.drawLine(
                    (int) (Main.nextRoute.startPos.x * 115 / 4 + 50 / 4),
                    (int) (Main.nextRoute.startPos.y * 115 / 4 + 50 / 4),
                    (int) (Main.nextRoute.endPos.x * 115 / 4 + 50 / 4),
                    (int) (Main.nextRoute.endPos.y * 115 / 4 + 50 / 4)
            );
        }

        for (Map map : Main.trueMaps) {
            //paint danger zones
            g.setColor(Color.RED);
            for (int i = 0; i < map.dangerZones.length; i++) {
                if (map.dangerZones[i] == null) {
                    System.out.println(i + " null");
                    continue;
                }
                g.fillOval(
                        (int) (map.dangerZones[i].center.x * 115 / 4 + 50 / 4 - map.dangerZones[i].r / 8),
                        (int) (map.dangerZones[i].center.y * 115 / 4 + 50 / 4 - map.dangerZones[i].r / 8),
                        map.dangerZones[i].r / 4, map.dangerZones[i].r / 4
                );
            }

            //paint boxes
            for (int i = 0; i < map.boxes.length; i++) {
                if (!Main.allColors[map.boxes[i].color].known) {
                    g.setColor(Color.BLACK);
                } else switch (map.boxes[i].color) {
                    case Colors.BLUE:
                        g.setColor(Color.BLUE);
                        break;

                    case Colors.YELLOW:
                        g.setColor(Color.YELLOW);
                        break;

                    case Colors.RED:
                        g.setColor(Color.RED);
                        break;

                    case Colors.GREEN:
                        g.setColor(Color.GREEN);
                        break;

                    case Colors.ORANGE:
                        g.setColor(Color.ORANGE);
                        break;
                }
                g.fillRect(
                        (int) ((map.boxes[i].pos.x - 1) * 115 / 4 + 50 / 4),
                        (int) ((map.boxes[i].pos.y - 1) * 115 / 4 + 50 / 4),
                        115 * 2 / 4, 115 * 2 / 4
                );
            }

            //paint top boxes
            for (int i = 0; i < map.boxes.length; i++) {
                if (!Main.allColors[map.boxes[i].color].known) {
                    g.setColor(Color.BLACK);
                } else switch (map.boxes[i].colorOnTop) {
                    case Colors.BLUE:
                        g.setColor(Color.BLUE);
                        break;

                    case Colors.YELLOW:
                        g.setColor(Color.YELLOW);
                        break;

                    case Colors.RED:
                        g.setColor(Color.RED);
                        break;

                    case Colors.GREEN:
                        g.setColor(Color.GREEN);
                        break;

                    case Colors.ORANGE:
                        g.setColor(Color.ORANGE);
                        break;
                }
                g.fillRect(
                        (int) (map.boxes[i].pos.x * 115 / 4 + 50 / 4 - 5),
                        (int) (map.boxes[i].pos.y * 115 / 4 + 50 / 4 - 5),
                        10, 10
                );
            }

            //paint pick up positions
            g.setColor(Color.BLUE);
            for (int i = 0; i < map.pickUpPositions.length; i++) {
                g.fillOval(
                        (int) (map.pickUpPositions[i].x * 115 / 4 - 5 + 50 / 4),
                        (int) (map.pickUpPositions[i].y * 115 / 4 - 5 + 50 / 4),
                        10, 10
                );
            }
        }
    }

}

class Colors {
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

class Map {
    int id;
    String input;
    Box[] boxes = new Box[5];
    DangerZone[] dangerZones = new DangerZone[5 * 5 + 4 + 3];
    Position[] pickUpPositions = new Position[5];
    double[] pickUpDirections = new double[5];
    //TODO boolean fake = true;

    Map(String input, int id) {
        this.id = id;
        this.input = input;

        getObjects();
        calculateDangerZones();
        getRobot();
        getPickUpPositions();
        getStartPos();
    }

    int letter(char a) {
        return a - 65;
    }

    void getObjects() {
        //get parking zone position
        Main.parkingZone = new Parking(
                new Position(letter(input.charAt(1)), letter(input.charAt(3))),
                new Position(letter(input.charAt(5)), letter(input.charAt(7)))
        );

        //get box positions
        Position posA, posB, middlePos;
        for (int i = 0; i < 5; i++) {
            posA = new Position(letter(input.charAt(10 + 9 * i)), letter(input.charAt(12 + 9 * i)));
            posB = new Position(letter(input.charAt(14 + 9 * i)), letter(input.charAt(16 + 9 * i)));
            middlePos = new Position((posA.x + posB.x) / 2, (posA.y + posB.y) / 2);

            boxes[i] = new Box(middlePos, i);
        }
    }

    void calculateDangerZones() {
        for (int i = 0; i < 5; i++) {
            dangerZones[i * 5] = new DangerZone(new Position(boxes[i].pos.x - 1, boxes[i].pos.y - 1));
            dangerZones[i * 5 + 1] = new DangerZone(new Position(boxes[i].pos.x - 1, boxes[i].pos.y + 1));
            dangerZones[i * 5 + 2] = new DangerZone(new Position(boxes[i].pos.x + 1, boxes[i].pos.y - 1));
            dangerZones[i * 5 + 3] = new DangerZone(new Position(boxes[i].pos.x + 1, boxes[i].pos.y + 1));
            dangerZones[i * 5 + 4] = new DangerZone(boxes[i].pos);
        }

        dangerZones[25] = new DangerZone(Main.parkingZone.upper_right);
        dangerZones[26] = new DangerZone(Main.parkingZone.lower_right);
        dangerZones[27] = new DangerZone(Main.parkingZone.lower_left);
        dangerZones[28] = new DangerZone(Main.parkingZone.upper_left);

        dangerZones[29] = new DangerZone(Main.parkingZone.middle_right);
        dangerZones[30] = new DangerZone(Main.parkingZone.middle_left);
        dangerZones[31] = new DangerZone(Main.parkingZone.middle_back);
    }

    void getRobot() {
        Main.robot = new Robot(
                new Position(
                        (Main.parkingZone.lower_right.x + Main.parkingZone.upper_left.x) / 2,
                        (Main.parkingZone.lower_right.y + Main.parkingZone.upper_left.y) / 2
                )
        );
    }

    void getPickUpPositions() {
        for (int i = 0; i < 5; i++) {
            int closest;

            if (boxes[i].pos.x < 10) {
                if (boxes[i].pos.y < 10) {
                    if (boxes[i].pos.x < boxes[i].pos.y) {
                        closest = 3;
                    } else {
                        closest = 0;
                    }
                } else {
                    if (boxes[i].pos.x < 20 - boxes[i].pos.y) {
                        closest = 3;
                    } else {
                        closest = 2;
                    }
                }
            } else {
                if (boxes[i].pos.y < 10) {
                    if (20 - boxes[i].pos.x < boxes[i].pos.y) {
                        closest = 1;
                    } else {
                        closest = 0;
                    }
                } else {
                    if (20 - boxes[i].pos.x < 20 - boxes[i].pos.y) {
                        closest = 1;
                    } else {
                        closest = 2;
                    }
                }
            }

            switch (closest) {
                case 0:
                    pickUpPositions[i] = new Position(
                            boxes[i].pos.x, boxes[i].pos.y + (1.05 + Main.robotR)
                    );
                    pickUpDirections[i] = 0;
                    break;

                case 1:
                    pickUpPositions[i] = new Position(
                            boxes[i].pos.x - (1.05 + Main.robotR), boxes[i].pos.y
                    );
                    pickUpDirections[i] = 90;
                    break;

                case 2:
                    pickUpPositions[i] = new Position(
                            boxes[i].pos.x, boxes[i].pos.y - (1.05 + Main.robotR)
                    );
                    pickUpDirections[i] = 180;
                    break;

                case 3:
                    pickUpPositions[i] = new Position(
                            boxes[i].pos.x + (1.05 + Main.robotR), boxes[i].pos.y
                    );
                    pickUpDirections[i] = 270;
                    break;
            }
        }
    }

    void getStartPos() {
        double dx, dy;

        dx = Main.parkingZone.upper_right.x - Main.parkingZone.lower_right.x;
        dy = Main.parkingZone.upper_right.y - Main.parkingZone.lower_right.y;

        Main.startPos = new Position(Main.robot.pos.x + dx, Main.robot.pos.y + dy);

        Main.homePos = new Position(Main.robot.pos);
    }
}
