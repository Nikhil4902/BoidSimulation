package Utils;

import java.awt.*;

public enum BoidColors {
    BLUE(new Color(65, 131, 255)),
    GREEN(new Color(135, 250, 194)),
    PURPLE(new Color(190, 141, 255)),
    PINK(new Color(228, 179, 232)),
    YELLOW(new Color(255, 255, 138)),
    WHITE(new Color(233, 241, 237)),
    RED(new Color(241, 49, 49));
    public final Color value;

    BoidColors(Color color) {
        this.value = color;
    }

    @Override
    public String toString() {
        return this.name();
    }

    public Color getValue() {
        return this.value;
    }


}
