package Display;

import ParameterSettings.Parameters;
import SimulationObjects.Boid;
import SimulationObjects.School;
import SimulationObjects.SimulationObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MyPanel extends JPanel implements ActionListener {
    Timer timer;

    MyPanel() {
        this.setPreferredSize(new Dimension(Parameters.panelWidth, Parameters.panelHeight));
        this.setBackground(Color.black);
        timer = new Timer(Parameters.deltaT, this);
        timer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        if (Parameters.showGrid) {
            drawGrid(g2D, true);
        }
        ArrayList<SimulationObject> objects = School.getObjects();
        for (SimulationObject object : objects) {
            g2D.setPaint(object.getColor());
            if (object.isBoid()) {
                int[][] pts = ((Boid) object).getBodyCoordinates();
                g2D.fillPolygon(pts[0], pts[1], 4);
//                g2D.fillOval((int) object.getX(), (int) object.getY(), 5, 5);
            } else {
                g2D.drawOval((int) object.getX(), (int) object.getY(), 15, 15);
            }
        }
    }

    private void drawGrid(Graphics2D g2D, boolean showCellIndices) {
        g2D.setPaint(new Color(86, 88, 96));
        for (int i = 0; i < Parameters.numHorizontalBoxes - 1; i++) {
            g2D.drawLine((i + 1) * (int) Parameters.perceptionRadius, 0, (i + 1) * (int) Parameters.perceptionRadius, Parameters.panelHeight);
        }
        for (int i = 0; i < Parameters.numVerticalBoxes - 1; i++) {
            g2D.drawLine(0, (i + 1) * (int) Parameters.perceptionRadius, Parameters.panelWidth, (i + 1) * (int) Parameters.perceptionRadius);
        }
        if (showCellIndices) {
            for (int i = 0; i < Parameters.numVerticalBoxes; i++) {
                for (int j = 0; j < Parameters.numHorizontalBoxes; j++) {
                    g2D.drawString(Integer.toString(i * Parameters.numHorizontalBoxes + j), (int) (Parameters.perceptionRadius / 2 + j * Parameters.perceptionRadius), (int) (Parameters.perceptionRadius + i * Parameters.perceptionRadius));
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (Parameters.isRunning) {
            School.update();
        }
        repaint();
    }
}
