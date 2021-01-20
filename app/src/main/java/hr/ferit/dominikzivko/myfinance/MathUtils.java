package hr.ferit.dominikzivko.myfinance;

public class MathUtils {

    public static float map(float val, float x1, float x2, float y1, float y2) {
        return (val - x1) / (x2 - x1) * (y2 - y1) + y1;
    }
}
