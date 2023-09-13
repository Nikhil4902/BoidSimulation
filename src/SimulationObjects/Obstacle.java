package SimulationObjects;

import Utils.BoidColors;
import Utils.Vector2D;

public class Obstacle extends SimulationObject {
    public Obstacle(Vector2D position) {
        super(position, false);
        this.setColor(BoidColors.RED);
    }

    @Override
    public String toString() {
        return "Obstacle{" +
                "position=" + position.round(2) +
                '}';
    }
}
