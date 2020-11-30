package hu.johetajava.pathfinding;

public class Map {
    int id;
    String input;
    Box[] boxes = new Box[5];
    DangerZone[] dangerZones = new DangerZone[5 * 5 + 4 + 3];
    //Position[] pickUpPositions = new Position[5];
    //double[] pickUpDirections = new double[5];
    RobotInterface robotInterface;

    Map(String input, int id) {
        this.id = id;
        this.input = input;

        getObjects();
        calculateDangerZones();
        getRobot();
        //getPickUpPositions();
        getStartPos();
    }

    Map(String input, int id, RobotInterface robotInterface) {
        this.id = id;
        this.input = input;
        this.robotInterface = robotInterface;

        getObjects();
        calculateDangerZones();
        getRobot();
        //getPickUpPositions();
        getStartPos();
    }

    int letter(char a) {
        return a - 65;
    }

    void getObjects() {
        //get parking zone position
        Main_pathfinding.parkingZone = new Parking(
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

        dangerZones[25] = new DangerZone(Main_pathfinding.parkingZone.upper_right);
        dangerZones[26] = new DangerZone(Main_pathfinding.parkingZone.lower_right);
        dangerZones[27] = new DangerZone(Main_pathfinding.parkingZone.lower_left);
        dangerZones[28] = new DangerZone(Main_pathfinding.parkingZone.upper_left);

        dangerZones[29] = new DangerZone(Main_pathfinding.parkingZone.middle_right);
        dangerZones[30] = new DangerZone(Main_pathfinding.parkingZone.middle_left);
        dangerZones[31] = new DangerZone(Main_pathfinding.parkingZone.middle_back);
    }

    void getRobot() {
        if (robotInterface == null) {
            Main_pathfinding.robot = new Robot(
                    new Position(
                            (Main_pathfinding.parkingZone.lower_right.x + Main_pathfinding.parkingZone.upper_left.x) / 2,
                            (Main_pathfinding.parkingZone.lower_right.y + Main_pathfinding.parkingZone.upper_left.y) / 2
                    )
            );
        } else {
            Main_pathfinding.robot = new Robot(
                    new Position(
                            (Main_pathfinding.parkingZone.lower_right.x + Main_pathfinding.parkingZone.upper_left.x) / 2,
                            (Main_pathfinding.parkingZone.lower_right.y + Main_pathfinding.parkingZone.upper_left.y) / 2
                    ), robotInterface
            );
        }

    }

    /*void getPickUpPositions() {
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
                            boxes[i].pos.x, boxes[i].pos.y + (3)
                    );
                    pickUpDirections[i] = 0;
                    boxes[i].pickUpDir = 0;
                    break;

                case 1:
                    pickUpPositions[i] = new Position(
                            boxes[i].pos.x - (3), boxes[i].pos.y
                    );
                    pickUpDirections[i] = 90;
                    boxes[i].pickUpDir = 90;
                    break;

                case 2:
                    pickUpPositions[i] = new Position(
                            boxes[i].pos.x, boxes[i].pos.y - (3)
                    );
                    pickUpDirections[i] = 180;
                    boxes[i].pickUpDir = 180;
                    break;

                case 3:
                    pickUpPositions[i] = new Position(
                            boxes[i].pos.x + (3), boxes[i].pos.y
                    );
                    pickUpDirections[i] = 270;
                    boxes[i].pickUpDir = 270;
                    break;
            }
        }
    }*/

    void getStartPos() {
        double dx, dy;

        dx = Main_pathfinding.parkingZone.upper_right.x - Main_pathfinding.parkingZone.lower_right.x;
        dy = Main_pathfinding.parkingZone.upper_right.y - Main_pathfinding.parkingZone.lower_right.y;

        Main_pathfinding.startPos = new Position(Main_pathfinding.robot.pos.x + dx, Main_pathfinding.robot.pos.y + dy);

        Main_pathfinding.homePos = new Position(Main_pathfinding.robot.pos);
    }
}
