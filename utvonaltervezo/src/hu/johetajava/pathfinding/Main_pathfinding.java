package hu.johetajava.pathfinding;

import javax.swing.*;
import java.util.ArrayList;

public class Main_pathfinding {
    //robot params
    final static int robotDInMMs = 360;
    static double robotR = (double) robotDInMMs / (2 * 115);
    final static int robotSpeed = 7; //*0.001 tile/s, default: 7
    final static double robotTurnSpeed = 0.4; //*1000 degrees/s, default: 0.2

    //map params (L,G,K,I)(K,R,I,T)(T,I,R,K)(C,K,E,M)(N,Q,P,S)(H,P,F,R)
    final static String input1 = "(L,G,K,I)(K,R,I,T)(T,I,R,K)(C,K,E,M)(N,Q,P,S)(H,P,F,R)";
    final static String input2 = "(L,G,K,I)(K,R,I,T)(T,I,R,K)(C,K,E,M)(N,Q,P,S)(H,P,F,R)";
    final static String input3 = "(L,G,K,I)(K,R,I,T)(T,I,R,K)(C,K,E,M)(N,Q,P,S)(H,P,F,R)";
    final static String input4 = "(L,G,K,I)(K,R,I,T)(T,I,R,K)(C,K,E,M)(N,Q,P,S)(H,P,F,R)";

    final static int trueMap = 1;

    static int[][] colorsInOrder = new int[][]{
            {Colors.ORANGE, Colors.GREEN, Colors.YELLOW, Colors.RED, Colors.BLUE},
            {Colors.RED, Colors.ORANGE, Colors.BLUE, Colors.YELLOW, Colors.GREEN}
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

        createFrame();


        robot.turnTo(startPos);
        robot.dir += 180;

        robot.pos = new Position(startPos);


        while (found < 3) {
            getNextRoute();
            robot.move(nextRoute);
            int wrongMap = 0;
            for (Map map : trueMaps) {
                for (int i = 0; i < map.boxes.length; i++) {
                    if (robot.isPickUpFrom(map.boxes[i].pickUpDir, map.boxes[i].pos)) {
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

    public static void run(RobotInterface robotInterface) {
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

        //adjust pos and dir
        robot.turnTo(startPos);
        robot.dir += 180;

        robot.pos = new Position(startPos);

        //while we don't have the 3rd cube
        while (found < 3) {
            //get and navigate next route
            getNextRoute();
            robot.move(nextRoute);

            int wrongMap = 0;
            for (Map map : trueMaps) {
                for (int i = 0; i < map.boxes.length; i++) {
                    if (robot.isPickUpFrom(map.boxes[i].pickUpDir, map.boxes[i].pos)) {
                        if (!robotInterface.isTrueBox()) {
                            wrongMap = map.id;
                            break;
                        } else {
                            wrongMap = -1;
                            if (!allColors[map.boxes[i].color].known) {
                                map.boxes[i].setColors(robotInterface.positionToCube());
                                allColors[map.boxes[i].color].known = true;
                                allColors[map.boxes[i].color].boxId = i;
                            }

                            if (map.boxes[i].color == nextColor) {
                                nextColor = map.boxes[i].colorOnTop;
                                robotInterface.goToEdge();

                                switch (found) {
                                    case 0:
                                        robotInterface.pickUpBoxLeft();
                                        break;

                                    case 1:
                                        robotInterface.switchBox(false);
                                        break;

                                    case 2:
                                        robotInterface.switchBox(true);
                                        break;

                                    default:
                                        System.err.println("túl sokadik doboz");
                                        break;
                                }

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
            nextRoute = new Route(robot.pos, trueMaps.get(0).boxes[allColors[nextColor].boxId]);
        } else {
            Route shortestRoute = null;
            for (Map map : trueMaps) {
                for (int i = 0; i < map.boxes.length; i++) {
                    if (!allColors[map.boxes[i].color].known) {
                        Route route = new Route(robot.pos, map.boxes[i]);

                        if (shortestRoute == null || route.length() < shortestRoute.length()) {
                            shortestRoute = new Route(route);
                        }
                    }
                }
            }

            if (shortestRoute == null) {
                System.err.println("null route");
            }

            System.out.println(shortestRoute.endDir);
            nextRoute = new Route(shortestRoute);
        }
    }
}

