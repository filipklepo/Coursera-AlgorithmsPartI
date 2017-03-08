import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Board {

    private final int[][] blocks;
    private final int dimension;
    private int hammingSum;
    private int manhattanSum;
    private List<Board> neigbors;

    public Board(int[][] blocks) {
        Objects.requireNonNull(blocks);
        if (blocks.length < 2) {
            throw new IllegalArgumentException("Board dimension should be >= 2.");
        }

        this.blocks = copy(blocks);
        dimension = blocks.length;

        calculateHammingSum();
        calculateManhattanSum();
    }

    private void determineNeighbors() {
        List<Board> currentNeighbors = new ArrayList<>();

        int blankSquareI = 0;
        int blankSquareJ = 0;

        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (blocks[i][j] == 0) {
                    blankSquareI = i;
                    blankSquareJ = j;
                    break;
                }
            }
        }

        if (Integer.compare(blankSquareI, 0) > 0) {
            currentNeighbors.add(
                    new Board(swap(copy(blocks), blankSquareI, blankSquareJ, blankSquareI - 1, blankSquareJ)));
        }
        if (Integer.compare(blankSquareI, dimension - 1) < 0) {
            currentNeighbors.add(
                    new Board(swap(copy(blocks), blankSquareI, blankSquareJ, blankSquareI + 1, blankSquareJ)));
        }
        if (Integer.compare(blankSquareJ, 0) > 0) {
            currentNeighbors.add(
                    new Board(swap(copy(blocks), blankSquareI, blankSquareJ, blankSquareI, blankSquareJ - 1)));
        }
        if (Integer.compare(blankSquareJ, dimension - 1) < 0) {
            currentNeighbors.add(
                    new Board(swap(copy(blocks), blankSquareI, blankSquareJ, blankSquareI, blankSquareJ + 1)));
        }

        neigbors = currentNeighbors;
    }

    private static int[][] copy(int[][] matrix) {
        int [][] newMatrix = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            newMatrix[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        }

        return newMatrix;
    }

    private void calculateHammingSum() {
        int hammingSum = 0;

        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (blocks[i][j] != dimension * i + j + 1) {
                    if (blocks[i][j] != 0) {
                        hammingSum++;
                    }
                }
            }
        }

        this.hammingSum = hammingSum;
    }

    private void calculateManhattanSum() {
        int manhattanSum = 0;

        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (blocks[i][j] != dimension * i + j + 1) {
                    if (blocks[i][j] != 0) {
                        manhattanSum +=
                                Math.abs(((blocks[i][j] - 1) / dimension) - i) +
                                        Math.abs(((blocks[i][j] - 1) % dimension) - j);
                    }
                }
            }
        }

        this.manhattanSum = manhattanSum;
    }

    public int dimension() {
        return dimension;
    }

    public int hamming() {
        return hammingSum;
    }

    public int manhattan() { return manhattanSum; }

    public boolean isGoal() {
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (i == dimension - 1 && j == dimension - 1) {
                    break;
                }
                if (blocks[i][j] != dimension * i + j + 1) {
                    return false;
                }
            }
        }

        return true;
    }

    public Board twin() {

        int firstI = StdRandom.uniform(0, dimension);
        int firstJ = StdRandom.uniform(0, dimension);
        while (blocks[firstI][firstJ] == 0) {
            firstI = StdRandom.uniform(0, dimension);
            firstJ = StdRandom.uniform(0, dimension);
        }

        int secondI = firstI;
        int secondJ = firstJ;
        while ((firstI == secondI && firstJ == secondJ) ||
                blocks[secondI][secondJ] == 0) {
            secondI = StdRandom.uniform(0, dimension);
            secondJ = StdRandom.uniform(0, dimension);
        }

        return new Board(swap(copy(blocks), firstI, firstJ, secondI, secondJ));
    }

    private static int[][] swap(int[][] data, int i1, int j1, int i2, int j2) {
        int tmp = data[i1][j1];
        data[i1][j1] = data[i2][j2];
        data[i2][j2] = tmp;

        return data;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        return Arrays.deepEquals(blocks, board.blocks);
    }

    public Iterable<Board> neighbors() {
        if (Objects.isNull(neigbors)) {
            determineNeighbors();
        }

        return new ArrayList<>(neigbors);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension()).append("\n ");

        for (int i = 0, length = dimension(); i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                sb.append(blocks[i][j]).append("  ");
            }
            sb.append("\n ");
        }

        return sb.toString();
    }

    public static void main(String[] args) {
    }
}
