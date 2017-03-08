import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BruteCollinearPoints {

    private static final double EQUALITY_THRESHOLD = 1e-8;

    private final LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) {
        Objects.requireNonNull(points);
        Arrays.stream(points).forEach(Objects::requireNonNull);

        for (int i = 0; i < points.length; ++i) {
            Point currentPoint = points[i];

            if (Arrays.stream(allExcept(points, i)).filter(t -> t.equals(currentPoint))
                                                  .collect(Collectors.toList()).size() > 0) {
                throw new IllegalArgumentException("Input array contains duplicate points,");
            }
        }

        lineSegments = getLineSegments(Arrays.copyOf(points, points.length));
    }

    private static Point[] allExcept(Point[] points, int index) {
        Point[] newPoints = new Point[points.length - 1];
        for (int i = 0, j = 0, length = points.length; i < length; ++i) {
            if (i != index) {
                newPoints[j++] = points[i];
            }
        }

        return newPoints;
    }

    private static LineSegment[] getLineSegments(Point[] points) {
        Arrays.sort(points);

        List<LineSegment> currentLineSegments = new ArrayList<>();
        for (int i = 0, length = points.length; i < length; ++i) {
            for (int j = i + 1; j < length; ++j) {
                for (int k = j + 1; k < length; ++k) {
                    for (int l = k + 1; l < length; ++l) {
                        if (allLieOnSameLineSegment(
                                points[i], points[j], points[k], points[l])) {
                            currentLineSegments.add(new LineSegment(points[i], points[l]));
                        }
                    }
                }
            }
        }

        return currentLineSegments.toArray(new LineSegment[currentLineSegments.size()]);
    }

    private static boolean allLieOnSameLineSegment(
            Point p, Point q, Point r, Point s) {
        double pqSlope = p.slopeTo(q);
        double prSlope = p.slopeTo(r);
        double psSlope = p.slopeTo(s);

        return equal(pqSlope, prSlope) && equal(pqSlope, psSlope);
    }

    private static boolean equal(double x, double y) {
        return Double.compare(x, y) == 0 || Math.abs(x - y) < EQUALITY_THRESHOLD;
    }

    public int numberOfSegments() {
        return lineSegments.length;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(lineSegments, lineSegments.length);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment );
            segment.draw();
        }
        StdDraw.show();
    }
}
