package util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Simple FIFO Queue with bound so can only hold a finite number of objects.
 * Implemented with a circular buffer made from a single finite array.
 */
public class BoundedQueue<Item> implements Iterable<Item> {

    private Item[] q;            // queue elements
    private int N     = 0;       // number of elements on queue
    private int first = 0;       // index of first element of queue
    private int last  = 0;       // index of next available slot

    public BoundedQueue(int bound) {
        this.q = (Item[]) new Object[bound];
    }

    public int size()  { return N;        }
    public int bound() { return q.length; }

    public boolean isEmpty() { return size() == 0;       }
    public boolean isFull()  { return size() == bound(); }

    public boolean enqueue(Item item) {
        if (isFull()) {
            return false;
        } else {
            q[last++] = item;                // add item
            N++;                             // increment size
            if (last == q.length) last = 0;  // wrap-around
            return true;
        }
    }

    public Item dequeue() {
        if (isEmpty()) {
            return null;
        } else {
            Item item = q[first];             // pull first item
            q[first] = null;                  // to avoid loitering
            N--;                              // decrement size
            first++;                          // increment first pointer
            if (first == q.length) first = 0; // wrap-around
            return item;
        }
    }

    public Iterator<Item> iterator() { return new QueueIterator(); }

    // an simple iterator, doesn't implement remove() since it's optional
    private class QueueIterator implements Iterator<Item> {
        private int i = 0;
        public boolean hasNext()  { return i < N;                               }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = q[i % q.length];
            i++;
            return item;
        }
    }

    public String toString() {
        String s = "BoundedQueue:{";

        s += "bound=" + bound() + ", ";
        s += "size=" + size() + ", ";
        s += "items=[";
        for (Item item : this) s += item + ", ";
        s += "]";

        return s + "}";
    }
}
