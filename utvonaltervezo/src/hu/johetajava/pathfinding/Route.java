package hu.johetajava.pathfinding;

import java.util.ArrayList;

public class Route {
    Position startPos, endPos;
    ArrayList<Position> stops = new ArrayList<>();
    ArrayList<Route> possibleRoutes = new ArrayList<>();
    boolean isPossible = true;
    double endDir = 10000;

    Route(Position startPos, Position endPos) {
        this.startPos = startPos;
        this.endPos = endPos;

        calcAsGrid();

        if (!noCross()) {
            isPossible = false;
        }
    }

    Route(Position startPos, Box endBox) {
        this.startPos = startPos;
        endPos = endBox.pickUpPos;
        endDir = endBox.pickUpDir;

        calcAsGrid();

        if (!noCross()) {
            isPossible = false;
        }
    }

    Route(Route route) {
        startPos = route.startPos;
        endPos = route.endPos;
        endDir = route.endDir;

        stops = new ArrayList<>();
        stops.addAll(route.stops);
    }

    void calcAsGrid() {
        if(startPos.equals(endPos)){
            return;
        }

        if (!possible()) {
            return;
        }

        possibleRoutes = new ArrayList<>();
        possibleRoutes.add(this);

        boolean finished = noCross();
        int limit = 0;
        while (!finished && limit < 2) {
            limit++;
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

        if (!finished) {
            isPossible = false;
        }
    }

    boolean possible() {
        if (!isPossible) {
            return false;
        }

        for (Map map : Main_pathfinding.trueMaps) {
            for (DangerZone dangerZone : map.dangerZones) {
                if (endPos.dist(dangerZone.center) <= Main_pathfinding.robotR + 0.05) {
                    return false;
                }
            }
        }

        return true;
    }

    /*
    void calcAsCircle() {
        possibleRoutes.add(new hu.johetajava.pathfinding.Route(this));
        boolean finished = false;
        int a = 0;
        while (!finished) {
            finished = true;

            ArrayList<hu.johetajava.pathfinding.Route> tempPossibleRoutes = new ArrayList<>();

            System.out.println(a);
            a++;

            for (int i = 0; i < possibleRoutes.size(); i++) {
                hu.johetajava.pathfinding.Route route = possibleRoutes.get(i);

                hu.johetajava.pathfinding.Position[] stopAtPos;
                int where;


                if (route.stops.size() == 0) {
                    stopAtPos = whereToAddStop(route.startPos, route.endPos);
                    where = 0;

                    if (stopAtPos != null) {
                        hu.johetajava.pathfinding.Position[] stopPos = stopPos(stopAtPos);

                        hu.johetajava.pathfinding.Route newRoute1 = addedStop(
                                route, new hu.johetajava.pathfinding.Position[]{stopPos[0], stopPos[1]}, where
                        );

                        hu.johetajava.pathfinding.Route newRoute2 = addedStop(
                                route, new hu.johetajava.pathfinding.Position[]{stopPos[2], stopPos[3]}, where
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
                        hu.johetajava.pathfinding.Position[] stopPos = stopPos(stopAtPos);

                        hu.johetajava.pathfinding.Route newRoute1 = addedStop(
                                route, new hu.johetajava.pathfinding.Position[]{stopPos[0], stopPos[1]}, where
                        );

                        hu.johetajava.pathfinding.Route newRoute2 = addedStop(
                                route, new hu.johetajava.pathfinding.Position[]{stopPos[2], stopPos[3]}, where
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
                            hu.johetajava.pathfinding.Position[] stopPos = stopPos(stopAtPos);

                            hu.johetajava.pathfinding.Route newRoute1 = addedStop(
                                    route, new hu.johetajava.pathfinding.Position[]{stopPos[0], stopPos[1]}, where
                            );

                            hu.johetajava.pathfinding.Route newRoute2 = addedStop(
                                    route, new hu.johetajava.pathfinding.Position[]{stopPos[2], stopPos[3]}, where
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
                        hu.johetajava.pathfinding.Position[] stopPos = stopPos(stopAtPos);

                        hu.johetajava.pathfinding.Route newRoute1 = addedStop(
                                route, new hu.johetajava.pathfinding.Position[]{stopPos[0], stopPos[1]}, where
                        );

                        hu.johetajava.pathfinding.Route newRoute2 = addedStop(
                                route, new hu.johetajava.pathfinding.Position[]{stopPos[2], stopPos[3]}, where
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

            for (hu.johetajava.pathfinding.Route route : tempPossibleRoutes) {
                if (route.noCross()) finished = true;
            }

            possibleRoutes.addAll(tempPossibleRoutes);
        }

        hu.johetajava.pathfinding.Route shortestRoute = null;
        for (hu.johetajava.pathfinding.Route route : possibleRoutes) {
            if (route.noCross()) {


                if (shortestRoute == null || route.length() < shortestRoute.length()) {
                    shortestRoute = new hu.johetajava.pathfinding.Route(route);
                }
            }
        }

        if (shortestRoute == null) return;

        beACopy(shortestRoute);

        removeRedundantStops();
    }

    void calcAsRectangle() {
        ArrayList<hu.johetajava.pathfinding.Position> edgePoints = new ArrayList<>();
        for (hu.johetajava.pathfinding.DangerZone dangerZone : hu.johetajava.pathfinding.Main.dangerZones) {
            edgePoints.add(new hu.johetajava.pathfinding.Position(dangerZone.center.x - 1, dangerZone.center.y - 1));
            edgePoints.add(new hu.johetajava.pathfinding.Position(dangerZone.center.x + 1, dangerZone.center.y - 1));
            edgePoints.add(new hu.johetajava.pathfinding.Position(dangerZone.center.x - 1, dangerZone.center.y + 1));
            edgePoints.add(new hu.johetajava.pathfinding.Position(dangerZone.center.x + 1, dangerZone.center.y + 1));
        }

        possibleRoutes = new ArrayList<>();
        possibleRoutes.add(this);

        boolean finished = noCross();
        while (!finished) {
            ArrayList<hu.johetajava.pathfinding.Route> tempPossibleRoutes = new ArrayList<>();

            for (hu.johetajava.pathfinding.Route route : possibleRoutes) {
                for (hu.johetajava.pathfinding.Position position : edgePoints) {
                    hu.johetajava.pathfinding.Route route1 = new hu.johetajava.pathfinding.Route(route);
                    route1.stops.add(position);
                    tempPossibleRoutes.add(route1);
                    if (route1.noCross()) {
                        finished = true;
                    }
                }
            }

            if (finished) {
                hu.johetajava.pathfinding.Route shortestRoute = null;

                for (hu.johetajava.pathfinding.Route route : tempPossibleRoutes) {
                    if (route.noCross()) {
                        if (shortestRoute == null || route.length() < shortestRoute.length()) {
                            shortestRoute = new hu.johetajava.pathfinding.Route(route);
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

    /*static hu.johetajava.pathfinding.Route addedStop(hu.johetajava.pathfinding.Route route, hu.johetajava.pathfinding.Position[] stop, int at) {

        hu.johetajava.pathfinding.Route myRoute = new hu.johetajava.pathfinding.Route(route);

        if (!stop[1].inDanger()) {
            myRoute.stops.add(at, stop[1]);
        }

        if (!stop[0].inDanger()) {
            myRoute.stops.add(at, stop[0]);
        }

        return myRoute;
    }*/

    static Position[] whereToAddStop(Position segmentStart, Position segmentEnd) {

        for (Map map : Main_pathfinding.trueMaps) {
            for (DangerZone dangerZone : map.dangerZones) {

                DistFromLine distFromLine = dangerZone.center.distFromLine(segmentStart, segmentEnd);

                if (distFromLine.dist < Main_pathfinding.robotR) {
                    return new Position[]{
                            distFromLine.closest, dangerZone.center,
                            segmentStart, segmentEnd
                    };
                }
            }
        }

        return null;
    }

    /*static hu.johetajava.pathfinding.Position[] stopPos(hu.johetajava.pathfinding.Position[] stopAt) {
        hu.johetajava.pathfinding.Position at = stopAt[0];
        hu.johetajava.pathfinding.Position center = stopAt[1];
        hu.johetajava.pathfinding.Position start = stopAt[2];
        hu.johetajava.pathfinding.Position end = stopAt[3];

        hu.johetajava.pathfinding.Position p1, p2;

        if (start.x <= end.x) {
            p1 = new hu.johetajava.pathfinding.Position(start);
            p2 = new hu.johetajava.pathfinding.Position(end);
        } else {
            p1 = new hu.johetajava.pathfinding.Position(end);
            p2 = new hu.johetajava.pathfinding.Position(start);

        }

        double steep = (p2.y - p1.y) / (p2.x - p1.x);
        double c = p1.y - steep * p1.x;

        double perp = -(1 / steep);
        double q = center.y - perp * center.x;

        double dist = at.dist(center);

        double d_bottom, d_left, d_right, d_top;

        double r = hu.johetajava.pathfinding.Main.robotR + 0.05;

        if (steep < 0) {
            d_bottom = c - (steep + 1 / steep) * (r - dist) / Math.sqrt(1 + 1 / (steep * steep));
            d_top = c + (steep + 1 / steep) * (r + dist) / Math.sqrt(1 + 1 / (steep * steep));
            d_right = q - (perp + 1 / perp) * (hu.johetajava.pathfinding.Main.robotR) / Math.sqrt(1 + 1 / (perp * perp));
            d_left = q + (perp + 1 / perp) * (hu.johetajava.pathfinding.Main.robotR) / Math.sqrt(1 + 1 / (perp * perp));
        } else {
            d_bottom = c + (steep + 1 / steep) * (r + dist) / Math.sqrt(1 + 1 / (steep * steep));
            d_top = c - (steep + 1 / steep) * (r - dist) / Math.sqrt(1 + 1 / (steep * steep));
            d_left = q - (perp + 1 / perp) * (hu.johetajava.pathfinding.Main.robotR) / Math.sqrt(1 + 1 / (perp * perp));
            d_right = q + (perp + 1 / perp) * (hu.johetajava.pathfinding.Main.robotR) / Math.sqrt(1 + 1 / (perp * perp));
        }

        double x_ll = (d_bottom - d_left) / (perp - steep);
        double y_ll = (steep * x_ll + d_bottom);
        hu.johetajava.pathfinding.Position ll = new hu.johetajava.pathfinding.Position(x_ll, y_ll);

        double x_lr = (d_bottom - d_right) / (perp - steep);
        double y_lr = (steep * x_lr + d_bottom);
        hu.johetajava.pathfinding.Position lr = new hu.johetajava.pathfinding.Position(x_lr, y_lr);

        double x_ul = (d_top - d_left) / (perp - steep);
        double y_ul = (steep * x_ul + d_top);
        hu.johetajava.pathfinding.Position ul = new hu.johetajava.pathfinding.Position(x_ul, y_ul);

        double x_ur = (d_top - d_right) / (perp - steep);
        double y_ur = (steep * x_ur + d_top);
        hu.johetajava.pathfinding.Position ur = new hu.johetajava.pathfinding.Position(x_ur, y_ur);

        return new hu.johetajava.pathfinding.Position[]{ll, lr, ul, ur};
    }*/

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

        if (!possible()) return 1000000;

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

    /*void removeRedundantStops() {
        boolean finished = false;
        while (!finished) {
            finished = true;

            for (int i = 0; i < stops.size(); i++) {
                hu.johetajava.pathfinding.Route route = new hu.johetajava.pathfinding.Route(this);
                route.stops.remove(i);

                if (route.noCross() && route.length() < length()) {
                    finished = false;
                    beACopy(route);
                    break;
                }
            }
        }
    }*/

    void beACopy(Route route) {
        startPos = route.startPos;
        endPos = route.endPos;

        stops = new ArrayList<>();
        stops.addAll(route.stops);

        possibleRoutes = new ArrayList<>();
        possibleRoutes.addAll(route.possibleRoutes);
    }

    /*boolean equals(hu.johetajava.pathfinding.Route route) {

        if (stops.size() != route.stops.size()) return false;

        for (int i = 0; i < stops.size(); i++) {
            if (!stops.get(i).equals(route.stops.get(i))) return false;
        }

        return startPos.equals(route.startPos) && endPos.equals(route.endPos);
    }*/

    /*boolean isIn(ArrayList<hu.johetajava.pathfinding.Route> routes) {
        for (hu.johetajava.pathfinding.Route route : routes) {
            if (equals(route)) return true;
        }

        return false;
    }*/
}
