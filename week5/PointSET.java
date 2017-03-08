import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.*;

public class PointSET {

    private Set<Point2D> points;

    public PointSET() {
        points = new TreeSet<>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        Objects.requireNonNull(p);

        points.add(p);
    }

    public boolean contains(Point2D p) {
        Objects.requireNonNull(p);

        return points.contains(p);
    }

    public void draw() {
        points.forEach(p -> StdDraw.point(p.x(), p.y()));
    }

    public Iterable<Point2D> range(RectHV rect) {
        Objects.requireNonNull(rect);

        List<Point2D> pointsInside = new ArrayList<>();

        for (Point2D point : points) {
            if (point.x() >= rect.xmin() && point.x() <= rect.xmax() &&
                    point.y() >= rect.ymin() && point.y() <= rect.ymax()) {
                pointsInside.add(point);
            }
        }

        return pointsInside;
    }

    public Point2D nearest(Point2D p) {
        Objects.requireNonNull(p);

        double minimalDistance = 0.0;
        Point2D nearestPoint = null;

        for (Point2D point : points) {
            double distance = p.distanceTo(point);

            if (Objects.isNull(nearestPoint)) {
                minimalDistance = distance;
                nearestPoint = point;
                continue;
            }

            if (distance < minimalDistance) {
                minimalDistance = distance;
                nearestPoint = point;
            }
        }

        return nearestPoint;
    }
}
