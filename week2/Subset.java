import edu.princeton.cs.algs4.StdIn;

public class Subset {

   private static final int MIN_K = 0;

   public static void main(String[] args) {
      Integer k;
      try {
         k = Integer.parseInt(args[0]);
         if(k < MIN_K) {
            throw new IllegalArgumentException("K should be at least " + MIN_K + ", "
                    + k + " given.");
         }
      } catch (Exception ex) {
         throw new IllegalArgumentException("One integer argument expected.");
      }

      String[] words = StdIn.readLine().trim().split(" ");
      if(words.length < k) {
         throw new IllegalArgumentException("Expected at least " + k + " words, "
                 + words.length + " were given.");
      }

      RandomizedQueue<String> rq = new RandomizedQueue<>();
      for(String word : words) {
         rq.enqueue(word);
      }

      for(int i = 0; i < k; ++i) {
         System.out.println(rq.dequeue());
      }
   }

}
