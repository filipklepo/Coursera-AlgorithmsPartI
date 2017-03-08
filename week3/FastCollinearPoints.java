import java.util.*;
import java.util.stream.Collectors;

public class FastCollinearPoints {

    private static final double EQUALITY_THRESHOLD = 1e-8;

    private final LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        Objects.requireNonNull(points);
        Arrays.stream(points).forEach(Objects::requireNonNull);

        for (int i = 0; i < points.length; ++i) {
            Point currentPoint = points[i];

            if (Arrays.stream(allExcept(points, i)).filter(t -> t.equals(currentPoint))
                    .collect(Collectors.toList()).size() > 0) {
                throw new IllegalArgumentException("Input array contains duplicate points,");
            }
        }

        segments = getLineSegments(Arrays.copyOf(points, points.length));
    }

    private static LineSegment[] getLineSegments(Point[] points) {
        Arrays.sort(points);

        List<List<Point>> pointsBySlope = new ArrayList<>();
        for (int i = 0, length = points.length; i < length; ++i) {
            Point[] remainingPoints = allExcept(points, i);

            List<List<Point>> currentPointsBySlope = groupBySlopeTo(remainingPoints, points[i]);
            currentPointsBySlope  = currentPointsBySlope .stream()
                    .filter(t -> t.size() >= 4)
                    .map(t -> t.stream().sorted().collect(Collectors.toList()))
                    .collect(Collectors.toList());
            pointsBySlope.addAll(currentPointsBySlope);
        }

        pointsBySlope = removeSubsegments(pointsBySlope);
        List<LineSegment> segments = pointsBySlope.stream()
                .map(t -> new LineSegment(t.get(0), t.get(t.size() - 1)))
                .collect(Collectors.toList());

        LineSegment[] result = new LineSegment[segments.size()];
        return segments.toArray(result);
    }

    private static List<List<Point>> removeSubsegments(List<List<Point>> segments) {
        List<List<Point>> remainingSegments = new ArrayList<>(segments);
        List<List<Point>> resultSegments = new ArrayList<>();

        while (!remainingSegments.isEmpty()) {
            List<Point> segment = remainingSegments.get(0);
            double segmentSlope = slope(segment);

            List<List<Point>> sameSlopeAndStartPointSegments = remainingSegments.stream()
                    .filter(t -> equal(segmentSlope, slope(t)) && t.get(0).compareTo(segment.get(0)) == 0)
                    .collect(Collectors.toList());

            List<Point> maximalSegment = sameSlopeAndStartPointSegments.stream().max(new Comparator<List<Point>>() {
                @Override
                public int compare(List<Point> firstSegment, List<Point> secondSegment) {
                    return Integer.compare(firstSegment.size(), secondSegment.size());
                }
            }).get();
            resultSegments.add(maximalSegment);

            remainingSegments.removeAll(sameSlopeAndStartPointSegments);
        }

        return resultSegments;
    }

    private static double slope(List<Point> segment) {
        return segment.get(0).slopeTo(segment.get(segment.size() - 1));
    }

    private static List<List<Point>> groupBySlopeTo(Point[] points, Point comparingPoint) {
        Arrays.sort(points, comparingPoint.slopeOrder());
        List<List<Point>> groupsBySlope = new ArrayList<>();

        for (int i = 0, length = points.length; i < length; ++i) {

            if (i == 0 || !equal(comparingPoint.slopeTo(points[i-1]), comparingPoint.slopeTo(points[i]))) {
                List<Point> currentSegmentPoints = new ArrayList<>();
                currentSegmentPoints.add(comparingPoint);
                groupsBySlope.add(currentSegmentPoints);
            }
            groupsBySlope.get(groupsBySlope.size() - 1).add(points[i]);
        }

        return groupsBySlope;
    }

    private static boolean equal(double x, double y) {
        return Double.compare(x, y) == 0 || Math.abs(x - y) < EQUALITY_THRESHOLD;
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

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        return segments;
    }

    public static void main(String[] args) {}

}
