import edu.princeton.cs.algs4.StdArrayIO;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int N = 1;

    private int size;
    private Item[] items;

    public RandomizedQueue() {
        items = emptyOfLength(N);
    }

    @SuppressWarnings("unchecked")
    private Item[] emptyOfLength(int length) {
        return  (Item[]) new Object[length];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private int getFullIndex() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty deque");
        }

        int index = StdRandom.uniform(items.length);
        while (items[index] == null) {
            index = StdRandom.uniform(items.length);
        }

        return index;
    }

    private int getNullIndex() {
        if (items.length == size()) {
            throw new NoSuchElementException("Full queue.");
        }

        int index = StdRandom.uniform(items.length);
        while (items[index] != null) {
            index = StdRandom.uniform(items.length);
        }

        return index;
    }

    public void enqueue(Item item) {
        if (Objects.isNull(item)) {
            throw new NullPointerException("Null elements are illegal.");
        }

        if (size() == items.length) {
            resize(items.length * 2);
        } else if (!isEmpty() && size() == items.length / 4) {
            resize(items.length / 2);
        }

        items[getNullIndex()] = item;
        ++size;
    }

    private void resize(int newSize) {
        Item[] resizedArray = emptyOfLength(newSize);

        for (int i = 0, j = 0, length = items.length; i < length; ++i) {
            if (Objects.nonNull(items[i])) {
                resizedArray[j++] = items[i];
            }
        }

        items = resizedArray;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty queue.");
        }

        int fullIndex = getFullIndex();
        Item item = items[fullIndex];
        items[fullIndex] = null;
        --size;

        return item;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty queue.");
        }

        return items[getFullIndex()];
    }

    public Iterator<Item> iterator() {
        Item[] iteratorItems = emptyOfLength(size());
        for (int i = 0, length = items.length, j = 0; i < length; ++i) {
            if (Objects.nonNull(items[i])) {
                iteratorItems[j++] = items[i];
            }
        }
        StdRandom.shuffle(iteratorItems);


        return new Iterator<Item>() {

            Item[] items = iteratorItems;
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < items.length;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more items.");
                }

                Item item = items[index];
                items[index++] = null;
                return item;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove is not allowed.");
            }
        };
    }

    public static void main(String[] args) {
    }
}
