import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw() {
        StdDraw.point(x, y);
    }

    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    public double slopeTo(Point that) {
        if (Integer.compare(this.x, that.x) == 0 && Integer.compare(this.y, that.y) == 0) {
            return -1.0 / 0;
        }
        if (Integer.compare(this.x, that.x) == 0) {
            return 1.0 / 0;
        }
        if (Integer.compare(this.y, that.y) == 0) {
            return 0;
        }

        return (that.y - this.y) * 1.0 / (that.x - this.x);
    }

    public int compareTo(Point that) {
        Integer yAxisComparison = Integer.compare(this.y, that.y);

        return yAxisComparison != 0 ? yAxisComparison : Integer.compare(this.x, that.x);
    }

    public Comparator<Point> slopeOrder() {
        return new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                return Double.compare(slopeTo(p1), slopeTo(p2));
            }
        };
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public static void main(String[] args) {
    }
}
