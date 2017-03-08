import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

   private static final int MIN_TRIALS = 1;

   private double mean;
   private double stddev;
   private double confidenceLo;
   private double confidenceHi;

   public PercolationStats(int n, int trials) {
      if (trials < MIN_TRIALS) {
         throw new IllegalArgumentException("Number of trials must be >= " + MIN_TRIALS + ".");
      }
      double[] probabilities = new double[trials];

      for (int i = 0; i < trials; ++i) {
         int openedSites = 0;
         Percolation p = new Percolation(n);

         while (!p.percolates()) {
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);

            if (!p.isOpen(row, col)) {
               p.open(row, col);
               openedSites++;
            }
         }

         probabilities[i] = openedSites * 1.0 / (n * n);
      }

      mean = StdStats.mean(probabilities);
      stddev = StdStats.stddev(probabilities);
      double intervalHalf = 1.96 * stddev / Math.sqrt(n);
      confidenceLo = mean - intervalHalf;
      confidenceHi = mean + intervalHalf;
   }

   public double mean() {
      return mean;
   }

   public double stddev() {
      return stddev;
   }

   public double confidenceLo() {
      return confidenceLo;
   }

   public double confidenceHi() {
      return confidenceHi;
   }

   public static void main(String[] args) {
   }
}
