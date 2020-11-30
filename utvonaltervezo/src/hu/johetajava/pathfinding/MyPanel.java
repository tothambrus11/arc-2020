package hu.johetajava.pathfinding;

import javax.swing.*;
import java.awt.*;

public class MyPanel extends JComponent {
    public void paint(Graphics g) {
        //Paint border
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Main_pathfinding.screenWidth, Main_pathfinding.screenHeight);
        g.clearRect(50 / 4, 50 / 4, Main_pathfinding.screenWidth - 100 / 4, Main_pathfinding.screenHeight - 100 / 4);

        //Paint grid
        g.setColor(Color.BLACK);
        for (int i = 1; i < 20; i++) {
            g.drawLine(50 / 4 + i * 115 / 4, 0, 50 / 4 + i * 115 / 4, Main_pathfinding.screenHeight);
            g.drawLine(0, 50 / 4 + i * 115 / 4, Main_pathfinding.screenWidth, 50 / 4 + i * 115 / 4);
        }

        //paint parking lot
        g.setColor(Color.GREEN);
        g.drawLine(
                (int) (Main_pathfinding.parkingZone.upper_right.x * 115 / 4 + 50 / 4),
                (int) (Main_pathfinding.parkingZone.upper_right.y * 115 / 4 + 50 / 4),
                (int) (Main_pathfinding.parkingZone.lower_right.x * 115 / 4 + 50 / 4),
                (int) (Main_pathfinding.parkingZone.lower_right.y * 115 / 4 + 50 / 4)
        );
        g.setColor(Color.BLACK);
        g.drawLine(
                (int) (Main_pathfinding.parkingZone.lower_left.x * 115 / 4 + 50 / 4),
                (int) (Main_pathfinding.parkingZone.lower_left.y * 115 / 4 + 50 / 4),
                (int) (Main_pathfinding.parkingZone.lower_right.x * 115 / 4 + 50 / 4),
                (int) (Main_pathfinding.parkingZone.lower_right.y * 115 / 4 + 50 / 4)
        );
        g.drawLine(
                (int) (Main_pathfinding.parkingZone.lower_left.x * 115 / 4 + 50 / 4),
                (int) (Main_pathfinding.parkingZone.lower_left.y * 115 / 4 + 50 / 4),
                (int) (Main_pathfinding.parkingZone.upper_left.x * 115 / 4 + 50 / 4),
                (int) (Main_pathfinding.parkingZone.upper_left.y * 115 / 4 + 50 / 4)
        );

        //paint robot position
        g.setColor(Color.GREEN);
        g.fillOval(
                (int) (Main_pathfinding.robot.pos.x * 115 / 4 - Main_pathfinding.robotDInMMs / 8 + 50 / 4),
                (int) (Main_pathfinding.robot.pos.y * 115 / 4 - Main_pathfinding.robotDInMMs / 8 + 50 / 4),
                Main_pathfinding.robotDInMMs / 4, Main_pathfinding.robotDInMMs / 4
        );

        //paint robot direction
        double angle = ((-Main_pathfinding.robot.dir + 180) * Math.PI / 180) % 360;
        double endX = (Main_pathfinding.robot.pos.x + Main_pathfinding.robotR * 1.2 * Math.sin(angle));
        double endY = (Main_pathfinding.robot.pos.y + Main_pathfinding.robotR * 1.2 * Math.cos(angle));
        g.setColor(Color.RED);
        g.drawLine(
                (int) (Main_pathfinding.robot.pos.x * 115 / 4 + 50 / 4),
                (int) (Main_pathfinding.robot.pos.y * 115 / 4 + 50 / 4),
                (int) (endX * 115 / 4 + 50 / 4),
                (int) (endY * 115 / 4 + 50 / 4)
        );

        //paint start position
        g.setColor(Color.GREEN);
        g.fillOval(
                (int) (Main_pathfinding.startPos.x * 115 / 4 + 50 / 4 - 5),
                (int) (Main_pathfinding.startPos.y * 115 / 4 + 50 / 4 - 5),
                10, 10
        );

        //paint next hu.johetajava.pathfinding.Route
        g.setColor(Color.BLUE);
        if (Main_pathfinding.nextRoute.stops.size() > 0) {
            g.drawLine(
                    (int) (Main_pathfinding.nextRoute.startPos.x * 115 / 4 + 50 / 4),
                    (int) (Main_pathfinding.nextRoute.startPos.y * 115 / 4 + 50 / 4),
                    (int) (Main_pathfinding.nextRoute.stops.get(0).x * 115 / 4 + 50 / 4),
                    (int) (Main_pathfinding.nextRoute.stops.get(0).y * 115 / 4 + 50 / 4)
            );
            for (int i = 1; i < Main_pathfinding.nextRoute.stops.size(); i++) {
                g.drawLine(
                        (int) (Main_pathfinding.nextRoute.stops.get(i - 1).x * 115 / 4 + 50 / 4),
                        (int) (Main_pathfinding.nextRoute.stops.get(i - 1).y * 115 / 4 + 50 / 4),
                        (int) (Main_pathfinding.nextRoute.stops.get(i).x * 115 / 4 + 50 / 4),
                        (int) (Main_pathfinding.nextRoute.stops.get(i).y * 115 / 4 + 50 / 4)
                );
            }
            g.drawLine(
                    (int) (Main_pathfinding.nextRoute.stops.get(Main_pathfinding.nextRoute.stops.size() - 1).x * 115 / 4 + 50 / 4),
                    (int) (Main_pathfinding.nextRoute.stops.get(Main_pathfinding.nextRoute.stops.size() - 1).y * 115 / 4 + 50 / 4),
                    (int) (Main_pathfinding.nextRoute.endPos.x * 115 / 4 + 50 / 4),
                    (int) (Main_pathfinding.nextRoute.endPos.y * 115 / 4 + 50 / 4)
            );
        } else {
            g.drawLine(
                    (int) (Main_pathfinding.nextRoute.startPos.x * 115 / 4 + 50 / 4),
                    (int) (Main_pathfinding.nextRoute.startPos.y * 115 / 4 + 50 / 4),
                    (int) (Main_pathfinding.nextRoute.endPos.x * 115 / 4 + 50 / 4),
                    (int) (Main_pathfinding.nextRoute.endPos.y * 115 / 4 + 50 / 4)
            );
        }

        for (Map map : Main_pathfinding.trueMaps) {
            //paint danger zones
            /*g.setColor(Color.RED);
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
            }*/

            //paint boxes
            for (int i = 0; i < map.boxes.length; i++) {
                if (!Main_pathfinding.allColors[map.boxes[i].color].known) {
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
                if (!Main_pathfinding.allColors[map.boxes[i].color].known) {
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
            for (int i = 0; i < map.boxes.length; i++) {
                g.fillOval(
                        (int) (map.boxes[i].pickUpPos.x * 115 / 4 - 5 + 50 / 4),
                        (int) (map.boxes[i].pickUpPos.y * 115 / 4 - 5 + 50 / 4),
                        10, 10
                );
            }
        }
    }

}
