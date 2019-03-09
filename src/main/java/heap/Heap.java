package heap;
/*
 * Author: Chris Lokken
 * Date: 3/6/19
 * Purpose: Heap class takes a given value and sorts it into a min-heap, where
            the value with the smallest priority is at the root of the heap.
            Also includes a HashTable that maps values to their index in the 
            Arraylist that represents this heap. This HashTable is used to make things
            like changing priority and finding elements in the heap easier and more
            effecient.
 */
import java.util.NoSuchElementException;

/** An instance is a min-heap of distinct values of type V with
 *  priorities of type P. Since it's a min-heap, the value
 *  with the smallest priority is at the root of the heap. */
public final class Heap<V, P extends Comparable<P>> {

    protected AList<Entry> c;
    protected HashTable<V, Integer> map;

    /** Constructor: an empty heap with capacity 10. */
    public Heap() {
        c = new AList<Entry>(10);
        map = new HashTable<V, Integer>();
    }

    /** An Entry contains a value and a priority. */
    class Entry {
        public V value;
        public P priority;

        /** An Entry with value v and priority p*/
        Entry(V v, P p) {
            value = v;
            priority = p;
        }

        public String toString() {
            return value.toString();
        }
    }

    /** Add v with priority p to the heap.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  In Phase 3 only:
     *  @throws IllegalArgumentException if v is already in the heap.*/
    public void add(V v, P p) throws IllegalArgumentException {
        if (contains(v))
            throw new IllegalArgumentException();
        Entry e = new Entry(v, p);
        c.append(e);
        map.put(v, c.size() - 1);
        bubbleUp(c.size() - 1);
    }

    /** Return the number of values in this heap.
     *  This operation takes constant time. */
    public int size() {
        return c.size();
    }

    /** Swap c[h] and c[k].
     *  precondition: h and k are >= 0 and < c.size() */
    protected void swap(int h, int k) {

        Entry temp = c.get(h);
        map.put(c.get(k).value, h);
        c.put(h, c.get(k));
        c.put(k, temp);
        map.put(temp.value, k);
    }

    /** Bubble c[k] up in heap to its right place.
     *  Precondition: Priority of every c[i] >= its parent's priority
     *                except perhaps for c[k] */
    protected void bubbleUp(int k) {
        if (k != 0)
        {
            int pIndex = (k-1) / 2;
            if (c.get(pIndex).priority.compareTo(c.get(k).priority) > 0)
            {
                swap(pIndex, k);
                bubbleUp(pIndex);
            }
        }
    }

    /** Return the value of this heap with lowest priority. Do not
     *  change the heap. This operation takes constant time.
     *  @throws NoSuchElementException if the heap is empty. */
    public V peek() throws NoSuchElementException {
        if (c.size() == 0)
            throw new NoSuchElementException();
        return c.get(0).value;
    }

    /** Remove and return the element of this heap with lowest priority.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  @throws NoSuchElementException if the heap is empty. */
    public V poll() throws NoSuchElementException {
        if (c.size() == 0)
            throw new NoSuchElementException();
        V retVal = c.get(0).value;
        map.remove(retVal);
        c.put(0, c.pop());
        if (c.size() > 0)
        {
            map.put(c.get(0).value, 0);
            bubbleDown(0);
        }
        return retVal;

    }

    /** Bubble c[k] down in heap until it finds the right place.
     *  If there is a choice to bubble down to both the left and
     *  right children (because their priorities are equal), choose
     *  the right child.
     *  Precondition: Each c[i]'s priority <= its childrens' priorities
     *                except perhaps for c[k] */
    protected void bubbleDown(int k) {
        int leftIndex = 2*k + 1;
        int rightIndex = 2*k + 2;
        int target;
        if (rightIndex >= c.size())
        {
            if (leftIndex >= c.size())
                return;
            else
                target = leftIndex;
        } else
        {
            if (c.get(leftIndex).priority.compareTo(c.get(rightIndex).priority) < 0)
                target = leftIndex;
            else
                target = rightIndex;
        }
        if (c.get(k).priority.compareTo(c.get(target).priority) > 0)
        {
            swap(target, k);
            bubbleDown(target);
        }

    }

    /** Return true if the value v is in the heap, false otherwise.
     *  The average case runtime is O(1).  */
    public boolean contains(V v) {
        if (map.containsKey(v))
            return true;
        return false;
    }

    /** Change the priority of value v to p.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  @throws IllegalArgumentException if v is not in the heap. */
    public void changePriority(V v, P p) throws IllegalArgumentException {
        if (!(contains(v)))
            throw new IllegalArgumentException();
        int index = map.get(v);
        Entry newEntry = new Entry(v, p);
        c.put(index, newEntry);
        bubbleUp(index);
        bubbleDown(index);
    }

}
