package hu.johetajava.pathfinding;

public class Parking {
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
