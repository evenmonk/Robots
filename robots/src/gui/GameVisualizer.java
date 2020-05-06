package gui;

import utils.Map;
import utils.Robot;
import utils.Target;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;
import static java.lang.Math.round;
import javax.swing.JPanel;

public class GameVisualizer extends JPanel {

    private final Map map;

    GameVisualizer() {
        map = new Map(500,500);
        map.setRobot(new Robot(100, 100, 0));
        map.setTarget(new Target(150, 100));

        var timer = new Timer("Events generator", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (map) {
                    map.update();
                }
            }
        }, 0, 10);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                synchronized (map) {
                    map.getTarget().move(e.getX(), e.getY());
                }
                repaint();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                synchronized (map) {
                    map.setWidth(e.getComponent().getWidth());
                    map.setHeight(e.getComponent().getHeight());
                }
            }
        });

        setDoubleBuffered(true);
    }

    private void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        double robotX, robotY, robotDirection, targetX, targetY;
        synchronized (map) {
            robotX = map.getRobot().getX();
            robotY = map.getRobot().getY();
            robotDirection = map.getRobot().getDirection();
            targetX = map.getTarget().getX();
            targetY = map.getTarget().getY();
        }
        Graphics2D g2d = (Graphics2D)g; 
        drawRobot(g2d, (int) round(robotX), (int) round(robotY), robotDirection);
        drawTarget(g2d, (int) round(targetX), (int) round(targetY));
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction)
    {
        g.setTransform(AffineTransform.getRotateInstance(direction, x, y));
        drawOvalWithCircuit(g, x, y, 30, 10, Color.MAGENTA);
        drawOvalWithCircuit(g, x + 10, y, 5, 5, Color.WHITE);
        g.setTransform(AffineTransform.getRotateInstance(0, 0, 0));
    }
    
    private void drawTarget(Graphics2D g, int x, int y)
    {
        drawOvalWithCircuit(g, x, y, 5, 5, Color.GREEN);
    }

    private static void drawOvalWithCircuit(Graphics g, int x, int y, int diam1, int diam2, Color fill) {
        x -= diam1 / 2;
        y -= diam2 / 2;
        g.setColor(fill);
        g.fillOval(x, y, diam1, diam2);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, diam1, diam2);
    }
}
