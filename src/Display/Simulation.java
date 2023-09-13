package Display;

import ParameterSettings.Parameters;
import SimulationObjects.Boid;
import SimulationObjects.Obstacle;
import SimulationObjects.School;
import Utils.BoidColors;
import Utils.ObjectType;
import Utils.SpacialHash;
import Utils.Vector2D;

import javax.swing.*;
import java.awt.event.*;

public class Simulation extends JFrame implements KeyListener, MouseListener, MouseMotionListener {
    public MyPanel panel;

    public Simulation() {
        School.createSchool();
        SpacialHash.setSpacialHash(School.getObjects());
        panel = new MyPanel();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.add(panel);
        this.pack();
        this.setLocation(0, Parameters.panelHeight);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        switch (keyChar) {
            case 'e' -> {
                Parameters.isRunning = !Parameters.isRunning;
            }
            case 'b' -> {
                Parameters.currentObject = ObjectType.BOID;
            }
            case 'o' -> {
                Parameters.currentObject = ObjectType.OBSTACLE;
            }
            case 'p' -> {
                Parameters.currentObject = ObjectType.PREDATOR;
            }
            case '1' -> {
                Parameters.currentColor = BoidColors.BLUE;
            }
            case '2' -> {
                if (Parameters.numTypes > 1) {
                    Parameters.currentColor = BoidColors.GREEN;
                }
            }
            case '3' -> {
                if (Parameters.numTypes > 2) {
                    Parameters.currentColor = BoidColors.PURPLE;
                }
            }
            case '4' -> {
                if (Parameters.numTypes > 3) {
                    Parameters.currentColor = BoidColors.PINK;
                }
            }
            case '5' -> {
                if (Parameters.numTypes > 4) {
                    Parameters.currentColor = BoidColors.YELLOW;
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (Parameters.currentObject) {
            case BOID -> {
                School.addObject(new Boid(new Vector2D(e.getX() - 8, e.getY() - 38), new Vector2D(), Parameters.currentColor));
            }
            case PREDATOR -> {
                School.addObject(new Boid(new Vector2D(e.getX() - 8, e.getY() - 38), new Vector2D(), Parameters.currentColor
                        , true));
            }
            case OBSTACLE -> {
                School.addObject((new Obstacle(new Vector2D(e.getX() - 8, e.getY() - 38))));
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
