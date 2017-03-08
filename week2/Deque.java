import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Deque<Item> implements Iterable<Item> {

   private static class Node<Item> {
      Item item;
      Node<Item> next;
      Node<Item> previous;
   }

   private Node<Item> nodeOf(Item item) {
      Node<Item> node = new Node<>();
      node.item = item;
      return node;
   }

   private Node<Item> first;
   private Node<Item> last;

   private int size;

   public Deque() {}

   public boolean isEmpty() {
      return Objects.isNull(first);
   }

   public int size() {
      return size;
   }

   public void addFirst(Item item) {
      if (Objects.isNull(item)) {
         throw new NullPointerException("No null elements allowed.");
      }

      Node<Item> newFirst = nodeOf(item);

      if (isEmpty()) {
         first = newFirst;
         last = first;
      } else {
         newFirst.next = first;
         if (!Objects.isNull(first)) {
            first.previous = newFirst;
         }
         first = newFirst;
      }
      ++size;
   }

   public void addLast(Item item) {
      if (Objects.isNull(item)) {
         throw new NullPointerException("No null elements allowed.");
      }

      Node<Item> newLast = nodeOf(item);

      if (isEmpty()) {
         first = newLast;
         last = first;
      } else {
         if (!Objects.isNull(last)) {
            last.next = newLast;
         }
         newLast.previous = last;
         last = newLast;
      }
      ++size;
   }

   public Item removeFirst() {
      if (isEmpty()) {
         throw new NoSuchElementException("Empty deque.");
      }

      Item item = first.item;
      first = first.next;
      if (!Objects.isNull(first)) {
         first.previous = null;
      }
      --size;

      return item;
   }

   public Item removeLast() {
      if (isEmpty()) {
         throw new NoSuchElementException("Empty deque.");
      }

      Item item = last.item;
      last = last.previous;
      if (!Objects.isNull(last)) {
         last.next = null;
      }
      --size;

      return item;
   }

   public Iterator<Item> iterator() {
      return new Iterator<Item>() {

         private Node<Item> current = first;

         @Override
         public boolean hasNext() {
            return !Objects.isNull(current);
         }

         @Override
         public Item next() {
            if (!hasNext()) {
               throw new NoSuchElementException("No more elements.");
            }

            Item nextItem = current.item;
            current = current.next;

            return nextItem;
         }

         @Override
         public void remove() {
            throw new UnsupportedOperationException();
         }

      };
   }

   public static void main(String[] args) {
   }
}
