import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class KdTree {

    private enum KdKeyType {
        X_COORDINATE,
        Y_COORDINATE
    }

    private static class KdNode {

        final KdKeyType type;
        Point2D value;

        KdNode leftChild;
        KdNode rightChild;

        int size;

        public KdNode(KdKeyType type, Point2D value) {
            this.type = type;
            this.value = value;
            size = 1;
        }
    }

    private KdNode root;

    public KdTree() {}

    public boolean isEmpty() {
        return Objects.isNull(root);
    }

    public int size() {
        return size(root);
    }

    private static KdNode insert(KdNode currentNode, Point2D p) {

        int compare = compare(p, currentNode);
        if (compare < 0) {

            if (Objects.isNull(currentNode.leftChild)) {
                currentNode.leftChild = new KdNode(childKeyType(currentNode), p);
            } else {
                currentNode.leftChild = insert(currentNode.leftChild, p);
            }
        } else {

            if (Objects.isNull(currentNode.rightChild)) {
                currentNode.rightChild = new KdNode(childKeyType(currentNode), p);
            } else {
                currentNode.rightChild = insert(currentNode.rightChild, p);
            }
        }

        currentNode.size = 1 + size(currentNode.leftChild) + size(currentNode.rightChild);
        return currentNode;
    }

    private static KdKeyType childKeyType(KdNode node) {
        if (node.type == KdKeyType.X_COORDINATE) {
            return KdKeyType.Y_COORDINATE;
        } else {
            return KdKeyType.X_COORDINATE;
        }
    }

    private static int size(KdNode node) {
        return Objects.isNull(node) ? 0 : node.size;
    }

    private static int compare(Point2D p, KdNode node) {
        if (node.type == KdKeyType.X_COORDINATE)
            return Double.compare(p.x(), node.value.x());
        else
            return Double.compare(p.y(), node.value.y());
    }

    public void insert(Point2D p) {
        Objects.requireNonNull(p);

        if (Objects.isNull(root)) {
            root = new KdNode(KdKeyType.X_COORDINATE, p);
        } else {
            root = insert(root, p);
        }
    }

    public boolean contains(Point2D p) {
        Objects.requireNonNull(p);

        return contains(root, p);
    }

    private static boolean contains(KdNode node, Point2D p) {
        if (Objects.isNull(node)) {
            return false;
        } else if (node.value.equals(p)) {
            return true;
        } else {
            if (compare(p, node) < 0) {
                return contains(node.leftChild, p);
            } else {
                return contains(node.rightChild, p);
            }
        }
    }

    public void draw() {}

    private static void inorder(KdNode node) {
        if (Objects.isNull(node)) return;

        inorder(node.leftChild);
        System.out.println(node.value + " " + (node.type == KdKeyType.X_COORDINATE ? "|" : "="));
        inorder(node.rightChild);
    }

    public Iterable<Point2D> range(RectHV rect) {
        Objects.requireNonNull(rect);

        List<Point2D> rangePoints = new ArrayList<>();
        range(root, rangePoints, rect, new RectHV(0, 0, 1, 1));
        return rangePoints;
    }

    private static void range(KdNode currentNode, Collection<Point2D> points,
                       RectHV rangeRect, RectHV currentNodeRect) {
        if (Objects.isNull(currentNode)) {
            return;
        }
        if (rangeRect.contains(currentNode.value)) {
            points.add(currentNode.value);
        }

        RectHV leftSubtreeRectangle = splitRectangleBy(currentNodeRect, currentNode, true);
        RectHV rightSubtreeRectangle = splitRectangleBy(currentNodeRect, currentNode, false);

        if (rangeRect.intersects(leftSubtreeRectangle)) {
            range(currentNode.leftChild, points, rangeRect, leftSubtreeRectangle);
        }
        if (rangeRect.intersects(rightSubtreeRectangle)) {
            range(currentNode.rightChild, points, rangeRect, rightSubtreeRectangle);
        }
    }

    public Point2D nearest(Point2D p) {
        Objects.requireNonNull(p);

        return nearest(root, p, new RectHV(0, 0, 1, 1));
    }

    private static Point2D nearest(KdNode currentNode, Point2D p, RectHV currentNodeRect) {
        if (Objects.isNull(currentNode)) {
            return null;
        } else if (currentNode.value.equals(p)) {
            return p;
        }
        
        RectHV leftSubtreeRectangle = splitRectangleBy(currentNodeRect, currentNode, true);
        RectHV rightSubtreeRectangle = splitRectangleBy(currentNodeRect, currentNode, false);

        Point2D leftSubtreeResult;
        Point2D rightSubtreeResult;

        if (leftSubtreeRectangle.contains(p)) {
            leftSubtreeResult = nearest(currentNode.leftChild, p, leftSubtreeRectangle);
            if (Objects.nonNull(leftSubtreeResult) &&
                    (Double.compare(leftSubtreeResult.distanceTo(p),
                                   rightSubtreeRectangle.distanceTo(p)) < 0)) {
                return leftSubtreeResult;
            } else {
                rightSubtreeResult = nearest(currentNode.rightChild, p, rightSubtreeRectangle);
            }
        } else {
            rightSubtreeResult = nearest(currentNode.rightChild, p, rightSubtreeRectangle);
            if (Objects.nonNull(rightSubtreeResult) &&
                    (Double.compare(rightSubtreeResult.distanceTo(p),
                            leftSubtreeRectangle.distanceTo(p)) < 0)) {
                return rightSubtreeResult;
            } else {
                leftSubtreeResult = nearest(currentNode.leftChild, p, leftSubtreeRectangle);
            }
        }

        return minimalDistance(leftSubtreeResult, rightSubtreeResult, currentNode.value, p);
    }

    private static Point2D minimalDistance(Point2D firstPoint,
                                    Point2D secondPoint,
                                    Point2D comparingPoint) {
        if (Objects.isNull(firstPoint)) {
            return secondPoint;
        } else if (Objects.isNull(secondPoint)) {
            return firstPoint;
        }

        if (Double.compare(firstPoint.distanceTo(comparingPoint),
                          secondPoint.distanceTo(comparingPoint)) < 0) {
            return firstPoint;
        }

        return secondPoint;
    }

    private static Point2D minimalDistance(Point2D firstPoint,
                                           Point2D secondPoint,
                                           Point2D thirdPoint,
                                           Point2D comparingPoint) {
        if (Objects.isNull(firstPoint)) {
            return minimalDistance(secondPoint, thirdPoint, comparingPoint);
        } else if (Objects.isNull(secondPoint)) {
            return minimalDistance(firstPoint, thirdPoint, comparingPoint);
        } else if (Objects.isNull(thirdPoint)) {
            return minimalDistance(firstPoint, secondPoint, comparingPoint);
        }

        double firstPointDistance = firstPoint.distanceTo(comparingPoint);
        double secondPointDistance = secondPoint.distanceTo(comparingPoint);
        double thirdPointDistance = thirdPoint.distanceTo(comparingPoint);

        if (firstPointDistance <= secondPointDistance && firstPointDistance <= thirdPointDistance) {
            return firstPoint;
        } else if (secondPointDistance <= firstPointDistance & secondPointDistance <= thirdPointDistance) {
            return secondPoint;
        } else {
            return thirdPoint;
        }
    }

    private static RectHV splitRectangleBy(RectHV rect, KdNode node, boolean left) {
        if (node.type == KdKeyType.X_COORDINATE) {
            return new RectHV(left ? rect.xmin() : node.value.x(),
                              rect.ymin(),
                              left ? node.value.x() : rect.xmax(),
                              rect.ymax());
        } else {
            return new RectHV(rect.xmin(),
                              left ? rect.ymin() : node.value.y(),
                              rect.xmax(),
                              left ? node.value.y() : rect.ymax());
        }
    }

    public static void main(String[] args) {
    }

}
