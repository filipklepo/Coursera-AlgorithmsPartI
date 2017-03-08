import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.stream.IntStream;

public class Percolation {

    private final WeightedQuickUnionUF percolationUf;
    private final WeightedQuickUnionUF fullSitesUf;

    private final boolean[][] openedSites;

    private static final int MIN_N = 1;    
    private static final int NO_OF_ADDITIONAL_SITES = 2;
    private static final int MIN_X = 1;
    private static final int MIN_Y = 1;

    private final int topIndex;
    private final int bottomIndex;

    private final int n;

    public Percolation(int n) {
        if (n < MIN_N) {
            throw new IllegalArgumentException(
                    "Got " + n + ", expected n to be >= " + MIN_N + ".");
        }
        this.n = n;

        int arrayLength = n * n;
        openedSites = new boolean[n][n];
        int ufSize = arrayLength + NO_OF_ADDITIONAL_SITES;
        percolationUf = new WeightedQuickUnionUF(ufSize);
        fullSitesUf = new WeightedQuickUnionUF(ufSize);

        topIndex = 0;
        bottomIndex = n * n + 1;

        for (int j = 1; j <= n; ++j) {
            percolationUf.union(topIndex, j);
            percolationUf.union(n * (n - 1) + j, bottomIndex);
            fullSitesUf.union(topIndex, j);
        }
    }

    public void open(int row, int col) {
        validateXy(row, col);

        openedSites[row - 1][col - 1] = true;
        updateUf(row, col);
    }

    private void updateUf(int row, int col) {
        if (col > MIN_Y && isOpen(row, col - 1)) {
            percolationUf.union(xyTo1D(row, col - 1), xyTo1D(row, col));
            fullSitesUf.union(xyTo1D(row, col - 1), xyTo1D(row, col));
        }
        if (col < n && isOpen(row, col + 1)) {
            percolationUf.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            fullSitesUf.union(xyTo1D(row, col), xyTo1D(row, col + 1));
        }

        if (row > MIN_X && isOpen(row - 1, col)) {
            percolationUf.union(xyTo1D(row - 1, col), xyTo1D(row, col));
            fullSitesUf.union(xyTo1D(row - 1, col), xyTo1D(row, col));
        }
        if (row < n && isOpen(row + 1, col)) {
            percolationUf.union(xyTo1D(row, col), xyTo1D(row + 1, col));
            fullSitesUf.union(xyTo1D(row, col), xyTo1D(row + 1, col));
        }
    }

    public boolean isOpen(int row, int col) {
        validateXy(row, col);

        return openedSites[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        validateXy(row, col);

        int index = xyTo1D(row, col);
        return openedSites[row - 1][col - 1] && fullSitesUf.connected(topIndex, index);
    }


    private int xyTo1D(int x, int y) {
        return (x - 1) * n + y;
    }

    private void validateXy(int x, int y) {
        if (x < MIN_X || x > n || y < MIN_Y || y > n) {
            throw new IndexOutOfBoundsException("Invalid coordinates: (" + x + ", " + y + ").");
        }
    }

    public boolean percolates() {
        if(n == 1) {
            return openedSites[0][0];
        } else {
            return percolationUf.connected(topIndex, bottomIndex);
        }
    }
}
