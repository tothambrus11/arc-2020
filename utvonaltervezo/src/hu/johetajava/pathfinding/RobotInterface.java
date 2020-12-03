package hu.johetajava.pathfinding;

import hu.johetajava.imageProcessing.CubePosInfo;

public abstract class RobotInterface {
    public abstract void go(double units);

    public abstract void turn(double fullRotations);

    public abstract CubePosInfo positionToCube();

    public abstract void goToEdge();

    public abstract String[] readMaps();
    /*
    returnol egy 4 hosszú String[]-et, aminek minden eleme egy beolvasott QR kód, az alábbi formátumban:
    return new String[]{
    "(O,F,N,I)(K,R,M,T)(F,P,H,R)(N,Q,P,S)(Q,N,S,P)(E,H,G,J)",
    "(O,F,N,I)(R,L,T,N)(D,K,F,M)(Q,Q,O,S)(L,R,N,T)(E,H,G,J)",
    "(O,F,N,I)(G,H,E,J)(O,Q,Q,S)(H,P,F,R)(T,L,R,N)(K,O,M,Q)",
    "(O,F,N,I)(M,R,K,T)(R,K,T,M)(F,L,D,N)(G,G,E,I)(Q,Q,O,S)"
    };
    */


    //megnézi hogy van-e előttünk doboz
    //innen tudjuk, hogy igazi-e az a map amin a jelenlegi pozíciónk előtt van egy doboz
    public abstract boolean isTrueBox();

    public abstract void pickUpBoxLeft();

    public abstract void switchBox(boolean isLeft); //ha a bal oldalra veszi fel az újat akkor true
}
