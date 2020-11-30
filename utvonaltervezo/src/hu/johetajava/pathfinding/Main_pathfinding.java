package hu.johetajava.pathfinding;

import javax.swing.*;
import java.util.ArrayList;

public class Main_pathfinding {
    final static boolean debug = true;

    //robot params
    final static int robotDInMMs = 270;
    static double robotR = (double) robotDInMMs / (2 * 115);
    final static int robotSpeed = 15; //*0.001 tile/s, default: 7
    final static double robotTurnSpeed = 0.9; //*1000 degrees/s, default: 0.2

    //map params
    final static String input1 = "(O,F,N,I)(K,R,M,T)(F,P,H,R)(N,Q,P,S)(Q,N,S,P)(E,H,G,J)";
    final static String input2 = "(O,F,N,I)(R,L,T,N)(D,K,F,M)(Q,Q,O,S)(L,R,N,T)(E,H,G,J)";
    final static String input3 = "(O,F,N,I)(G,H,E,J)(O,Q,Q,S)(H,P,F,R)(T,L,R,N)(K,O,M,Q)";
    final static String input4 = "(O,F,N,I)(M,R,K,T)(R,K,T,M)(F,L,D,N)(G,G,E,I)(Q,Q,O,S)";
    final static int trueMap = 3;

    //TODO check colors with robot
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
    static String[] readMaps = new String[4];

    static ArrayList<Map> trueMaps = new ArrayList<>();

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
            getNextRoute();
            robot.move(nextRoute);
            int wrongMap = 0;
            for (Map map : trueMaps) {
                for (int i = 0; i < map.pickUpPositions.length; i++) {
                    if (robot.pos.equals(map.pickUpPositions[i])) {
                        if (map.id != trueMap) {
                            wrongMap = map.id;
                            break;
                        } else {
                            wrongMap = -1;
                            allColors[map.boxes[i].color].known = true;
                            allColors[map.boxes[i].color].boxId = i;
                            if (map.boxes[i].color == nextColor) {
                                nextColor = map.boxes[i].colorOnTop;
                                found++;
                            }
                        }
                    }
                }
                if (wrongMap != 0) {
                    break;
                }
            }

            if (wrongMap != 0) {
                if (wrongMap == -1) {
                    ArrayList<Map> tempMap = new ArrayList<>();
                    for (Map map : trueMaps) {
                        if (map.id == trueMap) {
                            tempMap.add(map);
                            break;
                        }
                    }

                    trueMaps = new ArrayList<>();
                    trueMaps.addAll(tempMap);
                } else {


                    ArrayList<Map> tempTrueMaps = new ArrayList<>();
                    for (Map map : trueMaps) {
                        if (map.id != wrongMap) {
                            tempTrueMaps.add(map);
                        }
                    }

                    trueMaps = new ArrayList<>();
                    trueMaps.addAll(tempTrueMaps);
                }
            }
        }

        getNextRoute();
        robot.move(nextRoute);
        getNextRoute();
        robot.move(nextRoute);
    }

    public static void run(RobotInterface robotInterface){
        //read and calculate maps
        readMaps = robotInterface.readMaps();
        map1 = new Map(readMaps[0], 0, robotInterface);
        map2 = new Map(readMaps[1], 1, robotInterface);
        map3 = new Map(readMaps[2], 2, robotInterface);
        map4 = new Map(readMaps[3], 3, robotInterface);
        trueMaps.add(map1);
        trueMaps.add(map2);
        trueMaps.add(map3);
        trueMaps.add(map4);

        //get first route
        getNextRoute();



        /*boolean isRunning = true;
        while(isRunning){
            robotInterface.go(4);
            robotInterface.turn(0.25); // 90 fok balra
            robotInterface.turn(-0.25); // 90 fok jobbra
            robotInterface.positionToCube(); // Mielőtt ezt lefuttatjuk, a kocka szélétől kb 2 unitra kell legyen a robot. Visszaadja a kockák színét.
            robotInterface.goToEdge(); // Az előbbi függvény után le kell futtatni ezt is, hogy odamenjünk közel a kockához kockafelvételre
        }*/
    }

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
            Position posTo = new Position(Main_pathfinding.trueMaps.get(0).pickUpPositions[allColors[nextColor].boxId]);
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

            if (shortestRoute == null) {
                System.err.println("null route");
            }

            nextRoute = new Route(shortestRoute);
        }
    }
}

